// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CPlatform;
import javax.swing.JLabel;


public class CStatusBar
	extends HorizontalPanel
{
	public CStatusBar()
	{
		if(CPlatform.isMac())
		{
			setBorder(new CBorder(2, 1, 2, 20));
		}
		else
		{
			setBorder(new CBorder(2, 1, 1, 5));
		}
	}


	public void copyright()
	{
		JLabel c = new JLabel(Application.getCopyright(), null, JLabel.RIGHT);
		c.setForeground(Theme.PANEL_FG);
		add(c);
	}
}
