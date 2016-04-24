// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.ThemeKey;
import goryachev.common.util.CList;
import goryachev.common.util.TXT;


public class Features
{
	private String name;
	private CList<ThemeKey> list = new CList();
	
	
	protected Features(String name)
	{
		this.name = name;
	}
	
	
	public static Features getAll()
	{
		Features f = new Features(null);
		f.addAll(ThemeKey.values());
		return f;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public int size()
	{
		return list.size();
	}
	
	
	public void add(ThemeKey x)
	{
		list.add(x);
	}
	
	
	public void addAll(ThemeKey ... xs)
	{
		for(ThemeKey x: xs)
		{
			add(x);
		}
	}
	
	
	public boolean isFont(int ix)
	{
		ThemeKey f = list.get(ix);
		return f.isFont();
	}
	
	
	public boolean isColor(int ix)
	{
		ThemeKey f = list.get(ix);
		return f.isColor();
	}


	public ThemeKey getTFeature(int ix)
	{
		return list.get(ix);
	}

	
	protected String getName(int ix)
	{
		ThemeKey f = list.get(ix);
		switch(f)
		{
		case AFFIRM_BUTTON_COLOR: return TXT.get("Features.name.color.affirmative button", "Affirmative action color");
		case DESTRUCTIVE_BUTTON_COLOR: return TXT.get("Features.name.color.destructive button", "Destructive action color");
		case FIELD_BG: return TXT.get("Features.name.color.text field background", "Teext field background color");
		case FIELD_FG: return TXT.get("Features.name.color.field foreground", "Text field foreground color");
		case FOCUS_COLOR: return TXT.get("Features.name.color.focus", "Focus color");
		case GRID_COLOR: return TXT.get("Features.name.color.grid", "Grid color");
		case LINE_COLOR: return TXT.get("Features.name.color.line", "Line color");
		case LINK_COLOR: return TXT.get("Features.name.color.link", "Web link color");
		case PANEL_BG: return TXT.get("Features.name.color.panel background", "Panel background color");
		case PANEL_FG: return TXT.get("Features.name.color.panel foreground", "Panel foreground color");
		case TARGET_COLOR: return TXT.get("Features.name.color.target", "Target color");
		case TEXT_BG: return TXT.get("Features.name.color.text background", "Text background color");
		case TEXT_FG: return TXT.get("Features.name.color.text foreground", "Text foreground color");
		case TEXT_SELECTION_BG: return TXT.get("Features.name.color.text selection background", "Selected text background color");
		case TEXT_SELECTION_FG: return TXT.get("Features.name.color.text selection foreground", "Selected text foreground color");
		case TOOL_TIP_BG: return TXT.get("Features.name.color.tool tip", "Tool tip background color");
		case TOOLBAR_COLOR: return TXT.get("Features.name.color.tool bar", "Tool bar color");
		// fonts
		case BASE_FONT: return TXT.get("Features.name.font.base", "Text font");
		case BOLD_FONT: break;
		case MONOSPACED_FONT: return TXT.get("Features.name.font.mono", "Monospaced font");
		case TITLE_FONT: return TXT.get("Features.name.font.title", "Title font");
		}
		
		return f.toString();
	}
}
