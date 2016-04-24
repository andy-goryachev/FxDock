// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CBrowser;
import goryachev.common.util.CKit;
import goryachev.common.util.FileTools;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLDocument;


public class CHtmlPane
	extends CTextPane
{
	public CHtmlPane()
	{
		// setting it editable makes "link" etc. fields to appear
		setEditable(false);
		
		addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent ev)
			{
				if(ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
					onHyperLink(ev.getURL());
				}
			}
		});
		
		((CHtmlEditorKit)getEditorKit()).setBodyFont(getFont());
		
		putClientProperty(JTextPane.W3C_LENGTH_UNITS, Boolean.TRUE);
		putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.FALSE); // use system sizes
	}
	
	
	public void setAllowExternalImages(boolean on)
	{
		getPreviewHtmlEditorKit().setAllowExternalImages(on);
	}
	
	
	public void setFont(Font f)
	{
		super.setFont(f);
		getPreviewHtmlEditorKit().setBodyFont(f);
	}

	
	public CHtmlEditorKit getPreviewHtmlEditorKit()
	{
		return (CHtmlEditorKit)getEditorKit();
	}


	protected EditorKit createDefaultEditorKit()
	{
		return new CHtmlEditorKit();
	}
	
	
	public HTMLDocument getHTMLDocument()
	{
		return (HTMLDocument)getDocument();
	}
	

	public void show(File file, String encoding)
	{
		try
		{
			// TODO load first few KB of file and determine the format
			// image -> show as image
			// html -> show as html
			// everything else -> show as plain text
			
			// TODO auto-detect encoding
			// TODO when loading a large file, trim and append ("original file 123123 bytes long is trimmed by Localizer"
			
			if(FileTools.isImage(file))
			{
				setText("<html><body><img src=\"file:///" + file + "\"></body></html>");
			}
			else
			{
				setText(FileTools.loadText(file, encoding, 1000000));
			}
		}
		catch(Exception e)
		{
			setText(CKit.stackTrace(e));
		}
		
		setCaretPosition(0);
	}
	
	
	public void setText(String html)
	{
		if(html == null)
		{
			// clear stray styles
			html = "<html><body></body>";
		}
		super.setText(html);
		setCaretPosition(0);
	}
	
	
	protected void onHyperLink(URL url)
	{
		CBrowser.openLinkQuiet(url);
	}
}
