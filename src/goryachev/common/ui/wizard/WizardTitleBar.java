// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import javax.swing.JLabel;


public class WizardTitleBar
	extends CPanel
{
	public final JLabel titleField;
	
	
	public WizardTitleBar()
	{
		titleField = new JLabel();
		titleField.setFont(Theme.titleFont());
		titleField.setForeground(Theme.TEXT_FG);
		titleField.setBorder(new CBorder(10));
		
		setCenter(titleField);
		setBorder(new CBorder(0, 0, 1, 0, Theme.LINE_COLOR));
	}


	public void setText(String s)
	{
		titleField.setText(s);
	}
}
