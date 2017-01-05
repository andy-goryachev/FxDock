// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;

/**
 * Common Styles.
 */
public class CommonStyles
	extends FxStyleSheet
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");

	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	public CommonStyles()
	{
		add
		(
			// bold
			new Selector(BOLD).defines
			(
				fontWeight("bold")
			),
			
			// disables horizontal scroll bar
			new Selector(NO_HORIZONTAL_SCROLL_BAR).defines
			(
				new Selector(".scroll-bar:horizontal").defines
				(
					maxHeight(0),
					padding(0),
					opacity(0)
				),
				new Selector(".scroll-bar:horizontal *").defines
				(
					maxHeight(0),
					padding(0),
					opacity(0)
				)
			)
		);
	}
}
