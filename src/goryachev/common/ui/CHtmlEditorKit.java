// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.text.LocalImageView;
import goryachev.common.ui.text.XBoxView;
import goryachev.common.util.Log;
import goryachev.common.util.html.HtmlTools;
import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;


public class CHtmlEditorKit 
	extends HTMLEditorKit 
{	
	private boolean allowExternalImages;
	private StyleSheet styles;
	
	
	public CHtmlEditorKit()
	{
	}
	
	
	public void setAllowExternalImages(boolean on)
	{
		allowExternalImages = on;
	}
	
	
	public ViewFactory getViewFactory() 
	{
		return new CHtmlFactory();
	}
	
	
	public void setBodyFont(Font f)
	{
		getStyleSheet().addRule("body { font-size:" + f.getSize2D() + "px; font-family:\"" + f.getName() + "\"; }");
		getStyleSheet().addRule("a { color:#006600; }");
	}
	
	
	public void setBodyForeground(Color c)
	{
		getStyleSheet().addRule("body { color:" + HtmlTools.color(c) + "; }");
	}
	
	
	public StyleSheet getStyleSheet() 
	{
		if(styles == null)
		{
			styles = new StyleSheet();
			styles.addStyleSheet(super.getStyleSheet());
		}
		return styles;
	}
	
	
	public Document createDefaultDocument()
	{
		Document d = super.createDefaultDocument();
		d.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		return d;
	}
	
	
	protected ImageIcon loadImageIcon(String src)
	{
		try
		{
			String lower = src.toLowerCase();
			if(lower.startsWith("http://"))
			{
				if(allowExternalImages)
				{
					return new ImageIcon(new URL(src));
				}
			}
			else if(lower.startsWith("file:/"))
			{
				if(allowExternalImages)
				{
					return new ImageIcon(new URL(src));
				}
			}
			else if(lower.startsWith("data:"))
			{
				byte[] b = HtmlTools.parseBase64Data(src);
				if(b != null)
				{
					return new ImageIcon(b);
				}
			}
			else
			{
				if(allowExternalImages)
				{
					return new ImageIcon(ClassLoader.getSystemClassLoader().getResource(src));
				}
			}
		}
		catch(Exception e)
		{
			Log.err(new Exception("Unable to load icon: " + src, e));
		}
		
		return CIcons.TBD;
	}
	
	
	//
	
	
	public class CHtmlFactory 
		extends HTMLEditorKit.HTMLFactory 
	{
		public View create(Element elem) 
		{
			Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
			//Log.print(o);
			if(o instanceof HTML.Tag) 
			{
				HTML.Tag kind = (HTML.Tag)o;
				if(kind == HTML.Tag.IMG) 
				{
					return new LocalImageView(elem)
					{
						public ImageIcon loadImage(String src)
						{
							return loadImageIcon(src);
						}
					};
				}
				else if(kind == HTML.Tag.SCRIPT)
				{
					//Log.print("!");
				}
			}
			
			String name = elem.getName();
			if(name != null)
			{
				if(name.equals(AbstractDocument.SectionElementName))
				{
					return new XBoxView(elem, View.Y_AXIS);
				}
			}
			
			View v = super.create(elem);
			//CKit.print(CKit.className(v));
			return v;
		}
	}
}