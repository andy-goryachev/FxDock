// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.MailTools;
import goryachev.common.util.ProductInfo;
import java.awt.Component;


public class ContactSupport
{
	public static CAction action = new CAction() { public void action() { openMail(getSourceWindow(), null); }};
	
	
	public static void openMail(Component w, String body)
	{
		try
		{
			String email = ProductInfo.getSupportEmail();
			String subject = "Question about " + Application.getTitle() + " ver. " + Application.getVersion();
			MailTools.mail(email, subject, body);
		}
		catch(Exception e)
		{
			Dialogs.error(w, e);
		}
	}
}
