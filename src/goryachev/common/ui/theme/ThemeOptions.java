// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.options.BooleanOption;
import goryachev.common.ui.options.DateFormatOption;
import goryachev.common.ui.options.NumberFormatOption;
import goryachev.common.ui.options.TimeFormatOption;


public class ThemeOptions
{
	// effects
	public static final BooleanOption hoverEffects = new BooleanOption("ui.theme.hover.effects", false);
	
	// formats
	public static final DateFormatOption dateFormat = new DateFormatOption("app.DateFormat");
	public static final TimeFormatOption timeFormat = new TimeFormatOption("app.TimeFormat");
	public static final NumberFormatOption numberFormat = new NumberFormatOption("app.NumberFormat");
}
