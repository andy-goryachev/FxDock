// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * CssGenerator.
 */
public abstract class CssGenerator
{
	protected abstract Object[] generate();
	
	//
	
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	
	public static final String TRANSPARENT = "transparent";
	
	
	public String generateStyleSheet()
	{
		Object[] ss = generate();
		return new CssBuilder(ss).build();
	}
	
	
	private static CssProperty p(String name, Object x)
	{
		return new CssProperty(name, x);
	}
	
	
	public static CssProperty backgroundColor(Object x) { return p("-fx-background-color", CssTools.toColor(x)); }
	public static CssProperty backgroundInsets(Object x) { return p("-fx-background-insets", CssTools.toValue(x)); }
	public static CssProperty backgroundRadius(Object x) { return p("-fx-background-radius", CssTools.toValue(x)); }
	public static CssProperty fontSize(Object x) { return p("-fx-font-size", x); }
	public static CssProperty padding(Object x) { return p("-fx-padding", CssTools.toValue(x)); }
	public static CssProperty regionBackground(Object x) { return p("-fx-region-background", CssTools.toValue(x)); }
	public static CssProperty textFill(Object x) { return p("-fx-text-fill", CssTools.toColor(x)); }
}
