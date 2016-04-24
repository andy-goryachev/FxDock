// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.FontOption;
import goryachev.common.util.TXT;
import java.awt.Font;
import java.text.DecimalFormat;


public class FontOptionEditor
	extends AbstractPopupOptionEditor<Font,FontChooserPopup>
{
	public static final int FONT_MASK = Font.BOLD | Font.ITALIC;
	public static final int FONT_BOLD = Font.BOLD;
	public static final int FONT_ITALIC = Font.ITALIC;
	public static final int FONT_BOLD_ITALIC = Font.BOLD | Font.ITALIC;
	public static final DecimalFormat fontSizeFormatter = new DecimalFormat("#0.#");


	public FontOptionEditor(FontOption op)
	{
		super(op);
	}

	
	public static String getFontStyle(Font f)
	{
		int style = f.getStyle() & FONT_MASK;
		switch(style)
		{
		case FONT_BOLD:        return TXT.get("FontOption.bold","Bold");
		case FONT_ITALIC:      return TXT.get("FontOption.italic","Italic");
		case FONT_BOLD_ITALIC: return TXT.get("FontOption.bold+italic","Bold Italic");
		default:               return TXT.get("FontOption.regular","Regular");
		}
	}
	
	
	public static String getFontSize(Font f)
	{
		return fontSizeFormatter.format(f.getSize2D());
	}
	

	protected FontChooserPopup createPopupComponent(Font f)
	{
		return new FontChooserPopup(f);
	}


	protected Font getPopupValue(FontChooserPopup editor)
	{
		return editor.getSelectedFont();
	}


	protected String getPresentationText(Font f)
	{
		if(f == null)
		{
			return null;
		}
		else
		{
			return f.getName() + " " + getFontStyle(f) + " " + getFontSize(f);
		}
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}
