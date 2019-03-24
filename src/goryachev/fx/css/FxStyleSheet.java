// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.css;
import goryachev.common.util.CList;
import goryachev.common.util.SB;


/**
 * FX Style Sheet.
 * <p>
 * Example:
 * <pre>
 * public class Styles extends FxStyleSheet
 * {
 *     public Styles()
 *     {
 *         selector(".root").fontSize("bold");
 *         selector("PANE", FOCUSED).color(Color.RED);
 *     }
 * }
 */
public class FxStyleSheet
	implements StyleSheetConstants
{
	protected final CList<Selector> selectors = new CList();
	
	
	public FxStyleSheet()
	{
	}
	
	
	public Selector selector(Object ... spec)
	{
		Selector s = new Selector(null, spec)
		{
			public FxStyleSheet getStyleSheet()
			{
				return FxStyleSheet.this;
			}
		};
		selectors.add(s);
		return s;
	}
	
	
	/** generates the style sheet */
	public String generateStyleSheet()
	{
		SB sb = new SB();
		generate(sb);
		return sb.toString();
	}
	
	
	protected void generate(SB sb)
	{
		for(Selector s: selectors)
		{
			s.generate(sb);
		}
	}
}
