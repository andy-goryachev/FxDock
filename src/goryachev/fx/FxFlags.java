// Copyright © 2019-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * These application-wide flags control FX subsystem.
 */
public class FxFlags
{
	/**
	 * To enable polling of css style sheet for changes:
	 * <pre>
	 * -Dcss.refresh=true
	 * </pre> 
	 */
	public static final String CSS_REFRESH = "css.refresh";
	
	/**
	 * Enables dumping of the stylesheet to stdout
	 * <pre>
	 * -Dcss.dump=true
	 * </pre>
	 */
	public static final String CSS_DUMP = "css.dump";	
}
