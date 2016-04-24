// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.ThemeKeyType;


public enum ThemeKey
{
	// colors
	AFFIRM_BUTTON_COLOR(ThemeKeyType.COLOR, "color.button.affirm"),
	DESTRUCTIVE_BUTTON_COLOR(ThemeKeyType.COLOR, "color.button.destructive"),
	FIELD_BG(ThemeKeyType.COLOR, "color.field.bg"),
	FIELD_FG(ThemeKeyType.COLOR, "color.field.fg"),
	FOCUS_COLOR(ThemeKeyType.COLOR, "color.focus"),
	GRID_COLOR(ThemeKeyType.COLOR, "color.grid"),
	LINE_COLOR(ThemeKeyType.COLOR, "color.line"),
	LINK_COLOR(ThemeKeyType.COLOR, "color.link"),
	PANEL_BG(ThemeKeyType.COLOR, "color.panel.bg"),
	PANEL_FG(ThemeKeyType.COLOR, "color.panel.fg"),
	TARGET_COLOR(ThemeKeyType.COLOR, "color.target"),
	TEXT_BG(ThemeKeyType.COLOR, "color.text.bg"),
	TEXT_FG(ThemeKeyType.COLOR, "color.text.fg"),
	TEXT_SELECTION_BG(ThemeKeyType.COLOR, "color.text.selection.bg"),
	TEXT_SELECTION_FG(ThemeKeyType.COLOR, "color.text.selection.fg"),
	TOOL_TIP_BG(ThemeKeyType.COLOR, "color.tooltip.bg"),
	TOOLBAR_COLOR(ThemeKeyType.COLOR, "color.toolbar"),

	// fonts
	BASE_FONT(ThemeKeyType.FONT, "font.base"),
	BOLD_FONT(ThemeKeyType.FONT, "font.bold"),
	MONOSPACED_FONT(ThemeKeyType.FONT, "font.monospaced"),
	TITLE_FONT(ThemeKeyType.FONT, "font.title");
	
	//
	
	public final ThemeKeyType type;
	public final String id;
	
	
	ThemeKey(ThemeKeyType type, String id)
	{
		this.type = type;
		this.id = id;
	}
	
	
	public boolean isFont()
	{
		return (type == ThemeKeyType.FONT);
	}
	
	
	public boolean isColor()
	{
		return (type == ThemeKeyType.COLOR);
	}
}