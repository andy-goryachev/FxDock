// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Common Styles.
 */
public class CommonStyles
	extends CssGenerator
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");

	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	protected void generate()
	{
		// bold
		sel(BOLD);
		fontWeight("bold");
		
		// disables horizontal scroll bar
		sel(NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal");
		maxHeight(0);
		padding(0);
		opacity(0);
		sel(NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal *");
		maxHeight(0);
		padding(0);
		opacity(0);
	}
}
