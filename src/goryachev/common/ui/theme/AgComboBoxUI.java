// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;


public class AgComboBoxUI
	extends BasicComboBoxUI
{
	public static final Border BORDER_EDITOR = new CFieldBorder(false);
	public static final Border BORDER_ARROW = new CBorder(3);
	public static final CButtonBorder BORDER_BUTTON = new CButtonBorder(false, false, false, false);
	public static final int DEFAULT_BUTTON_WIDTH = 7;

	protected static int arrowButtonWidth = DEFAULT_BUTTON_WIDTH;
	protected static Insets arrowButtonInsets = new Insets(1, 1, 1, 1);
	protected static Color buttonAreaBG = ThemeColor.shadow(ThemeKey.TEXT_BG, 0.1);
	public static final String IS_TABLE_CELL_EDITOR = "JComboBox.isTableCellEditor"; // should be public in the base class
	private boolean inTable;


	public static void init(UIDefaults d)
	{
		d.put("ComboBoxUI", AgComboBoxUI.class.getName());
		d.put("ComboBox.padding", new Insets(0, 0, 0, 0));
		d.put("ComboBox.border", Theme.lineBorder());
		d.put("ComboBox.background", Theme.TEXT_BG);
		d.put("ComboBox.foreground", Theme.TEXT_FG);
		d.put("ComboBox.opaque", Boolean.TRUE);
	}


	public AgComboBoxUI()
	{
	}


	protected JButton createArrowButton()
	{
		return createDefaultArrowButton();
	}


	public static JButton createDefaultArrowButton()
	{
		JButton b = new AgArrowButton(AgArrowButton.SOUTH)
		{
			public void paint(Graphics g)
			{
				int w = getWidth();
				int h = getHeight();
				Color origColor = g.getColor();
				boolean isPressed = getModel().isPressed();
				boolean isEnabled = isEnabled();
				
				g.setColor(buttonAreaBG);
				g.fillRect(0, 0, w, h);

				BORDER_BUTTON.setPressed(isPressed);
				BORDER_BUTTON.paintBorder(this, g, 0, 0, w, h);

				if((h >= 5) && (w >= 5))
				{
					if(isPressed)
					{
						g.translate(1, 1);
					}
			
					// arrow
					int size = 3;
					paintTriangle(g, 1 + (w - size) / 2, (h - size) / 2, size, direction, isEnabled);
			
					if(isPressed)
					{
						g.translate(-1, -1);
					}
				}
				g.setColor(origColor);
			}
		};

		b.setName("ComboBox.arrowButton");
		b.setMargin(arrowButtonInsets);
		b.setBorder(BORDER_ARROW);
		return b;
	}
	
	
	protected JComboBox comboBox()
	{
		return comboBox;
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new AgComboBoxUI();
	}


	public void installUI(JComponent c)
	{
		super.installUI(c);
		
		inTable = Boolean.TRUE.equals(c.getClientProperty(IS_TABLE_CELL_EDITOR));
		comboBox.setRequestFocusEnabled(true);
	}


	public void uninstallUI(JComponent c)
	{
		super.uninstallUI(c);
	}


	protected void installListeners()
	{
		super.installListeners();
	}


	protected void uninstallListeners()
	{
		super.uninstallListeners();
	}


	protected void configureEditor()
	{
		super.configureEditor();
	}


	protected void unconfigureEditor()
	{
		super.unconfigureEditor();
	}


	public void paint(Graphics g, JComponent c)
	{
		super.paint(g, c);
	}


	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus)
	{
		if(inTable)
		{
			bounds.x -= 1; // seems to work
		}
		else
		{
			bounds.x += 1;
		}
		bounds.y += 1;
		bounds.width -= 2;
		bounds.height -= 2;
		
		super.paintCurrentValue(g, bounds, hasFocus);
	}


	public Dimension getPreferredSize(JComponent c)
	{
		Dimension d = super.getPreferredSize(c);
		d.width += 4;
		d.height += 2;
		return d;
	}


	protected LayoutManager createLayoutManager()
	{
		return new BasicComboBoxUI.ComboBoxLayoutManager()
		{
			public void layoutContainer(Container parent)
			{
				layout(parent);
			}
		};
	}
	
	
	protected void layout(Container c)
	{
		int width = c.getWidth();
		int height = c.getHeight();

		Insets insets = getInsets();
		int bh = height - (insets.top + insets.bottom);
		int bw = arrowButtonWidth;
		
		if(arrowButton != null)
		{
			Insets buttonInsets = arrowButton.getInsets();
			bw = arrowButtonWidth + buttonInsets.left + buttonInsets.right;
		}

		if(arrowButton != null)
		{
			if(UI.isLeftToRight(c))
			{
				arrowButton.setBounds(width - (insets.right + bw), insets.top, bw, bh);
			}
			else
			{
				arrowButton.setBounds(insets.left, insets.top, bw, bh);
			}
		}
		
		if(editor != null)
		{
			Rectangle r = rectangleForCurrentValue();
			editor.setBounds(r);
		}
	}
	

	protected void installKeyboardActions()
	{
		super.installKeyboardActions();
	}
	

	protected ComboPopup createPopup()
	{
		return new CComboPopup(comboBox);
	}


	protected ComboBoxEditor createEditor()
	{
		return new CComboBoxEditor();
	}


	protected ListCellRenderer createRenderer()
	{
		return new BasicComboBoxRenderer();
	}
}
