// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.css;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.fx.internal.CssTools;
import goryachev.fx.internal.FxCssProp;


/**
 * FX CSS Selector.
 */
public class Selector
{
	protected final Selector parent;
	protected final Object[] spec;
	protected final CList<Object> content = new CList();
	
	
	public Selector(Selector parent, Object[] spec)
	{
		this.parent = parent;
		this.spec = spec;
	}
	
	
	protected void generate(SB sb)
	{
		// TODO
	}
	
	
	protected Selector prop(String fxproperty, Object x)
	{
		content.add(x);
		return this;
	}
	
	
	public FxStyleSheet getStyleSheet()
	{
		return parent.getStyleSheet();
	}
	
	
	public Selector selector(Object ... spec)
	{
		Selector s = new Selector(this, spec);
		content.add(s);
		return s;
	}
	
	
	public Selector backgroundColor(Object x) { return prop("-fx-background-color", CssTools.toColor(x)); }
	public Selector backgroundImage(Object x) { return prop("-fx-background-image", CssTools.toValue(x)); }
	public Selector backgroundInsets(Object x) { return prop("-fx-background-insets", CssTools.toValue(x)); }
	public Selector backgroundRadius(Object x) { return prop("-fx-background-radius", CssTools.toValue(x)); }
	
	
	/** [ normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900 ] */
	public Selector fontWeight(Object x) { return prop("-fx-font-weight", x); }
}
