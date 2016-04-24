// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AgButtonUI;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;


public class CToggleToolBarButton
	extends JToggleButton
{
	private Color highlight;
	
	
	public CToggleToolBarButton(Action a)
	{
		super(a);
		init();
	}

	
	public CToggleToolBarButton(String text)
	{
		super(text);
		init();
	}
	
	
	public CToggleToolBarButton(String text, String tooltip)
	{
		super(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	public CToggleToolBarButton(String text, Action a)
	{
		super(a);
		setText(text);
		init();
	}
	
	
	public CToggleToolBarButton(Icon icon, String text, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);
		init();
	}
	
	
	public CToggleToolBarButton(Icon icon, String text, String tooltip, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	public CToggleToolBarButton(String text, String tooltip, Action a)
	{
		super(a);
		setText(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	// FIX one or the other
	public CToggleToolBarButton(Action a, String text, String tooltip)
	{
		super(a);
		setText(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	public CToggleToolBarButton(Icon icon, Action a, String text)
	{
		super(a);
		init();
	}
	
	
	public CToggleToolBarButton(Icon icon, Action a, String text, String tooltip)
	{
		super(a);
		init();
	}
	
	
	private void init()
	{
		setMinimumSize(CToolBarButton.SIZE);
		setBorder(CToolBarButton.BORDER);
		setFocusable(false);
		setFocusPainted(false);
		setMargin(CToolBarButton.MARGIN);
	}
	
	
	public void updateUI()
	{
		setUI(new AgButtonUI());
	}
	
	
	
	public void setSkin(CSkin s)
	{
		CSkin.set(this, s);
	}
	
	
	public CSkin getSkin()
	{
		return CSkin.get(this);
	}
	
	
	public void setText(String s)
	{
		super.setText(s);
		updateMnemonic();
	}
	
	
	public void updateMnemonic()
	{
		UI.setMnemonic(this);
	}
	
	
	public void setMargin(int x)
	{
		setMargin(x,x,x,x);
	}
	
	
	public void setMargin(int vert, int hor)
	{
		setMargin(vert,hor,vert,hor);
	}
	
	
	public void setMargin(int top, int left, int bottom, int right)
	{
		setMargin(new Insets(top, left, bottom, right));
	}
	
	
	public void setHighlight(Color c)
	{
		this.highlight = c;
		repaint();
	}
	
	
	public void setHighlight()
	{
		setHighlight(Theme.AFFIRM_BUTTON_COLOR);
	}
	
	
	public Color getHighlight()
	{
		return highlight;
	}

	
	
//	private void init()
//	{
//		setModel(new JToggleButton.ToggleButtonModel());
//		
//		if(getAction() instanceof CAction)
//		{
//			setSelected(((CAction)getAction()).isSelected());
//		}
//	}
	

//	protected void actionPropertyChanged(Action a, String propertyName)
//	{
//		if(Action.SELECTED_KEY.equals(propertyName))
//		{
//			if(hasSelectedKey(a))
//			{
//				setSelected2(((CAction)a).isSelected());
//			}
//		}
//		else
//		{
//			super.actionPropertyChanged(a, propertyName);
//		}
//	}
//
//
//	public boolean hasSelectedKey(Action a)
//	{
//		if(a != null)
//		{
//			return a.getValue(Action.SELECTED_KEY) != null;
//		}
//		return false;
//	}
//
//
//	// setSelectedFromAction() method is private
//	protected void setSelected2(boolean selected)
//	{
//		if(selected != isSelected())
//		{
//			// This won't notify ActionListeners, but that should be
//			// ok as the change is coming from the Action.
//			setSelected(selected);
//			
//			// Make sure the change actually took effect
//			if(!selected && isSelected())
//			{
//				if(getModel() instanceof DefaultButtonModel)
//				{
//					ButtonGroup group = ((DefaultButtonModel)getModel()).getGroup();
//					if(group != null)
//					{
//						group.clearSelection();
//					}
//				}
//			}
//		}
//	}
}
