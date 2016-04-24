// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.options.OptionEditor;
import goryachev.common.ui.theme.AgComboBoxUI;
import goryachev.common.ui.theme.CPopupWindow;
import goryachev.common.util.Log;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


/** Non-editable equivalent of a ComboBox with a custom popup */
public abstract class CPopupField<T extends JComponent>
	extends JPanel
{
	protected abstract T createPopupComponent();
	
	protected abstract void commit(T editor);
	
	//
	
	public final CAction showPopupAction = new CAction() { public void action() { actionTogglePopup(); } };
	public final CAction cancelPopupAction = new CAction() { public void action() { actionCancelPopup(); } };
	private static int arrowButtonWidth = AgComboBoxUI.DEFAULT_BUTTON_WIDTH;
	public final JLabel textField;
	public final JButton button;
	protected Handler handler;
	protected T popupComponent;
	protected volatile CPopupWindow popup;
	private Window parentWindow;
	protected boolean cancelled;
	
	
	public CPopupField()
	{
		handler = new Handler();
		
		textField = new JLabel(" ");
		textField.setFocusable(true);
		textField.setFont(Theme.plainFont());
		textField.setBorder(new CBorder(2, 2));
		textField.setOpaque(true);
		textField.setBackground(Theme.TEXT_BG);
		textField.addMouseListener(handler);
		
		button = AgComboBoxUI.createDefaultArrowButton();
		button.setAction(showPopupAction);
		
		setLayout(createLayoutManager());
		add(textField);
		add(button);
		setFocusable(true);
		setOpaque(false);
		setBorder(OptionEditor.BORDER_THIN);
	}
	
	
	public void setPresentationText(String s)
	{
		textField.setText(s);
	}

	
	protected LayoutManager createLayoutManager()
	{
		return new LayoutManager()
		{
			public void addLayoutComponent(String name, Component c)
			{
			}
			

			public void removeLayoutComponent(Component c)
			{
			}
			

			public Dimension preferredLayoutSize(Container parent)
			{
				return getMinimumSize();
			}


			public Dimension minimumLayoutSize(Container parent)
			{
				return getMinimumSize();
			}


			public void layoutContainer(Container parent)
			{
				layout(parent);
			}
		};
	}


	public Dimension getPreferredSize()
	{
		return getMinimumSize();
	}


	public Dimension getMinimumSize()
	{
		Dimension d = textField.getPreferredSize();
		Insets insets = getInsets();
		int buttonHeight = d.height;
		int buttonWidth = button.getPreferredSize().width;
		d.height += insets.top + insets.bottom;
		d.width += insets.left + insets.right + buttonWidth;
		
		if(d.width < 75)
		{
			d.width = 75;
		}
		else if(d.width > 200)
		{
			d.width = 200;
		}
		
		return new Dimension(d);
	}


	protected void layout(Container c)
	{
		int width = c.getWidth();
		int height = c.getHeight();

		Insets insets = getInsets();
		int buttonHeight = height - (insets.top + insets.bottom);
		int buttonWidth = arrowButtonWidth;
		
		if(button != null)
		{
			Insets arrowInsets = button.getInsets();
			buttonWidth = arrowButtonWidth + arrowInsets.left + arrowInsets.right;
		}

		if(button != null)
		{
			if(UI.isLeftToRight(c))
			{
				button.setBounds(width - (insets.right + buttonWidth), insets.top, buttonWidth, buttonHeight);
			}
			else
			{
				button.setBounds(insets.left, insets.top, buttonWidth, buttonHeight);
			}
		}
		
		if(textField != null)
		{
			Rectangle r = rectangleForCurrentValue();
			textField.setBounds(r);
		}
	}


	protected Rectangle rectangleForCurrentValue()
	{
		int width = getWidth();
		int height = getHeight();
		Insets insets = getInsets();
		int buttonSize = height - (insets.top + insets.bottom);
		if(button != null)
		{
			buttonSize = button.getWidth();
		}
		if(UI.isLeftToRight(this))
		{
			return new Rectangle(insets.left, insets.top, width - (insets.left + insets.right + buttonSize), height - (insets.top + insets.bottom));
		}
		else
		{
			return new Rectangle(insets.left + buttonSize, insets.top, width - (insets.left + insets.right + buttonSize), height - (insets.top + insets.bottom));
		}
	}
	
	
	protected void commit()
	{
		if(popupComponent != null)
		{
			try
			{
				commit(popupComponent);
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			popupComponent = null;
		}
	}
	
	
	protected void actionTogglePopup()
	{
		setPopupVisible(!isPopupVisible());
	}
	
	
	public void actionCancelPopup()
	{
		cancelled = true;
		setPopupVisible(false);
	}
	
	
	protected boolean isPopupVisible()
	{
		return (popup != null);
	}
	
	
	protected static boolean isParentOfPopup(Container parent, Component comp)
	{
		if(parent == null)
		{
			return false;
		}
		else if(comp == null)
		{
			return false;
		}
		
		if(comp == parent)
		{
			return true;
		}
		
		for(Container c=comp.getParent(); c != null; c = c.getParent())
		{
			if(c == parent)
			{
				return true;
			}
			
			if(c instanceof JPopupMenu)
			{
				JPopupMenu p = (JPopupMenu)c;
				if(isParentOfPopup(parent, p.getInvoker()))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	protected void setPopupVisible(boolean on)
	{
		if(on)
		{
			if(popup != null)
			{
				return;
			}
			
			cancelled = false;
			
			popupComponent = createPopupComponent();
			
			popup = new CPopupWindow();
			popup.show(this, 0, getHeight(), popupComponent);
			
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(handler);
			
			Toolkit.getDefaultToolkit().addAWTEventListener(handler, AWTEvent.MOUSE_EVENT_MASK);
			
			parentWindow = UI.getParentWindow(this);
			parentWindow.addComponentListener(handler);
		}
		else
		{
			if(popup != null)
			{
				if(!cancelled)
				{
					commit();
				}
				
				popup.hide();
				popup = null;
				
				parentWindow.removeComponentListener(handler);
				parentWindow = null;
				
				Toolkit.getDefaultToolkit().removeAWTEventListener(handler);
				
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(handler);
				
				textField.requestFocusInWindow();
			}
		}
	}
	
	
	//
	
	
	public class Handler
		implements ComponentListener, MouseListener, KeyEventDispatcher, AWTEventListener
	{
		public void eventDispatched(AWTEvent ev)
		{
			try
			{
				if(isPopupVisible())
				{
					if(ev.getID() == MouseEvent.MOUSE_PRESSED)
					{
						Component src = (Component)ev.getSource();
						if(src == button)
						{
							return;
						}
						else if(src == textField)
						{
							return;
						}
						
						if(!isParentOfPopup(popupComponent, src))
						{
							setPopupVisible(false);
						}
					}
				}
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		
		
		public void mousePressed(MouseEvent ev) 
		{
			actionTogglePopup();
		}
		
		
		public boolean dispatchKeyEvent(KeyEvent ev)
		{
			if(ev.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				actionCancelPopup();
				return true;
			}
			return false;
		}


		public void mouseClicked(MouseEvent ev) { }
		public void mouseReleased(MouseEvent ev) { }
		public void mouseEntered(MouseEvent ev) { }
		public void mouseExited(MouseEvent ev) { }
		
		public void componentResized(ComponentEvent ev) { setPopupVisible(false); }
		public void componentMoved(ComponentEvent ev) { setPopupVisible(false); }
		public void componentShown(ComponentEvent ev) { setPopupVisible(false); }
		public void componentHidden(ComponentEvent ev) { setPopupVisible(false); }
	}
}
