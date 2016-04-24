// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AgButtonUI;
import goryachev.common.ui.theme.CButtonBorder;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;


public class CButton
	extends JButton
{
	private Color highlight;
	
	
	public CButton()
	{
		init();
	}
	
	
	public CButton(Action a)
	{
		super(a);
		init();
	}
	
	
	public CButton(Action a, boolean highlight)
	{
		super(a);
		init();
		if(highlight)
		{
			setHighlight();
		}
	}

	
	public CButton(String text)
	{
		setText(text);
		init();
	}
	
	
	public CButton(Icon icon)
	{
		setIcon(icon);
		init();
	}
	
	
	public CButton(String text, String tooltip)
	{
		setText(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	public CButton(String text, Action a)
	{
		super(a);
		setText(text);
		init();
	}
	
	
	public CButton(String text, Action a, boolean highlight)
	{
		this(text, a);
		if(highlight)
		{
			setHighlight();
		}
	}
	
	
	public CButton(String text, Action a, Color highlight)
	{
		this(text, a);
		setHighlight(highlight);
	}
	
	
	public CButton(Icon icon, String text, Action a, Color highlight)
	{
		this(text, a);
		setIcon(icon);
		setHighlight(highlight);
	}
	
	
	public CButton(Icon icon, String text, Action a, boolean highlight)
	{
		this(text, a);
		setIcon(icon);
		if(highlight)
		{
			setHighlight();
		}
	}
	
	
	public CButton(Action a, Color highlight)
	{
		this(a);
		setHighlight(highlight);
	}
	
	
	public CButton(Icon icon, String text, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);
		init();
	}
	
	
	public CButton(Icon icon, String text, String tooltip, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	public CButton(String text, String tooltip, Action a)
	{
		super(a);
		setText(text);
		setToolTipText(tooltip);
		init();
	}
	
	
	public CButton(String text, String tooltip, Action a, Color highlight)
	{
		this(text, tooltip, a);
		setHighlight(highlight);
	}
	
	
	public CButton(Icon icon, Action a)
	{
		super(a);
		if(icon != null)
		{
			setIcon(icon);
		}
		init();
	}
	
	
	public CButton(Icon icon, Action a, String text)
	{
		super(a);
		if(icon != null)
		{
			setIcon(icon);
		}
		if(text != null)
		{
			setText(text);
		}
		init();
	}
	
	
	public CButton(Icon icon, Action a, String text, String tooltip)
	{
		super(a);
		if(icon != null)
		{
			setIcon(icon);
		}
		if(text != null)
		{
			setText(text);
		}
		setToolTipText(tooltip);
		init();
	}
	
	
	private void init()
	{
		updateMnemonic();
	}
	
	
	public void updateMnemonic()
	{
		UI.setMnemonic(this);
	}
	
	
	public void updateUI()
	{
		setUI(new AgButtonUI());
	}
	
	
	public void setPaintBorder(boolean paintTop, boolean paintLeft, boolean paintBottom, boolean paintRight)
	{
		Border x = getBorder();
		if(x instanceof CButtonBorder)
		{
			CButtonBorder b = (CButtonBorder)x;
			b.setPaintTop(paintTop);
			b.setPaintLeft(paintLeft);
			b.setPaintBottom(paintBottom);
			b.setPaintRight(paintRight);
		}
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


	public void setMargin(int m)
	{
		setMargin(m, m, m, m);
	}


	public void setMargin(int vert, int hor)
	{
		setMargin(vert, hor, vert, hor);
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
	
	
	public CButton setHighlight()
	{
		setHighlight(Theme.AFFIRM_BUTTON_COLOR);
		return this;
	}
	
	
	public Color getHighlight()
	{
		return highlight;
	}
}
