// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Common Styles.
 */
public class CommonStyles
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");

	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	public static void generate(CssGenerator g)
	{
		// bold
		g.sel(BOLD);
		g.fontWeight("bold");
		
		// disables horizontal scroll bar
		g.sel(NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal");
		g.maxHeight(0);
		g.padding(0);
		g.opacity(0);
		g.sel(NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal *");
		g.maxHeight(0);
		g.padding(0);
		g.opacity(0);
	}
}
