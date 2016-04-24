// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;


public class CComboPopup
	extends BasicComboPopup
{
	public CComboPopup(JComboBox c)
	{
		super(c);
	}


	protected void configurePopup()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorderPainted(true);
		setBorder(Theme.lineBorder());
		setOpaque(false);
		add(scroller);
		setDoubleBuffered(true);
		setFocusable(false);
	}
}
