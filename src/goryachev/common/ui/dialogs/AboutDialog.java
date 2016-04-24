// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs;
import goryachev.common.ui.Application;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CHtmlPane;
import goryachev.common.ui.CTextPane;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Panels;
import goryachev.common.util.TXT;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.Document;


public class AboutDialog
	extends CDialog
{
	protected final JLabel iconField;
	protected final MouseAdapter clickHandler;
	
	
	public AboutDialog(Component parent)
	{
		super(parent, "AboutDialog", true);
		setTitle(TXT.get("AboutDialog.title.PRODUCT VERSION", "About {0} {1}", Application.getTitle(), Application.getVersion()));
		Dialogs.size(this);
		borderless();
		setCloseOnEscape();
		
		iconField = Panels.iconField();

		clickHandler = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev) { close(); }
		};
		panel().addMouseListener(clickHandler);
		iconField.addMouseListener(clickHandler);
	}
	

	public void setLogo(Icon icon)
	{
		iconField.setIcon(icon);
		panel().setLeading(iconField);
	}
	
	
	public void setLogoComponent(Component c)
	{
		panel().setLeading(c);
		c.addMouseListener(clickHandler);
	}
	
	
	public void setTextHtml(String html)
	{
		CHtmlPane t = Panels.textHtml(html);
		t.setBorder(20);
		panel().setCenter(Panels.scroll(t));
	}
	
	
	public void setDocument(Document d)
	{
		CTextPane t = Panels.textPane(d);
		t.setBorder(20);
		panel().setCenter(Panels.scroll(t));
	}
}
