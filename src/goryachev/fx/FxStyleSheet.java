// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.fx.internal.CssTools;
import goryachev.fx.internal.FxCssProp;
import goryachev.fx.internal.StandardFxProperties;


/**
 * FX CSS Style Sheet Generator.
 * <p>
 * The following debugging options are defined by CssLoader:
 * <pre>
 *    -Dcss.refresh=true
 *    -Dcss.dump=true
 */
public class FxStyleSheet
	extends StandardFxProperties
{
	private final CList<Object> elements = new CList();


	public FxStyleSheet()
	{
	}
	
	
	public Selector selector(Object ... sel)
	{
		return new Selector(sel);
	}
	
	
	/** adds multiple selectors or style sheets */
	public void add(Object ... sel)
	{
		for(Object x: sel)
		{
			if(x instanceof Object[])
			{
				add((Object[])x);
			}
			else
			{
				elements.add(x);
			}
		}
	}
	
	
	/** adds a selector or a style sheet */
	public void add(Object ss)
	{
		elements.add(ss);
	}

	
	/** generates style sheet */
	public String generateStyleSheet()
	{
		SB sb = new SB();
		generate(sb);
		return sb.toString();
	}
	
	
	protected void generate(SB sb)
	{
		for(Object x: elements)
		{
			if(x instanceof Selector)
			{
				((Selector)x).write(sb, null);
			}
			else if(x instanceof FxStyleSheet)
			{
				((FxStyleSheet)x).generate(sb);
			}
			else if(x != null)
			{
				sb.append(x);
			}
		}
	}
	
	
	//


	public static class Selector
		extends StandardFxProperties
	{
		protected final String selector;
		protected final CList<Object> items = new CList();
		
		
		public Selector(Object ... sel)
		{
			selector = CssTools.selector(sel);
		}
		
		
		/** use this method to add properties or cascaded selectors */
		public Selector defines(Object ... sel)
		{
			items.addAll(sel);
			return this;
		}
		
		
		protected static Selector[] chain(Selector[] parents, Selector sel)
		{
			if(parents == null)
			{
				return new Selector[] { sel };
			}
			else
			{
				int sz = parents.length;
				Selector[] rv = new Selector[sz + 1];
				System.arraycopy(parents, 0, rv, 0, sz);
				rv[sz] = sel;
				return rv;
			}
		}
		
		
		protected void writeSelector(SB sb, boolean first, Object selector)
		{
			String s;
			if(selector instanceof Selector)
			{
				s = ((Selector)selector).selector;
			}
			else
			{
				s = (String)selector;
			}
			
			if(!first)
			{
				if(!s.startsWith(":"))
				{
					sb.sp();
				}
			}
			
			sb.a(s);
		}
		
		
		protected void write(SB sb, Selector[] parentSelectors)
		{
			if(items.size() == 0)
			{
				return;
			}
						
			boolean epilogue = false;
			
			CList<Selector> selectors = null;
			for(Object x: items)
			{
				if(x instanceof Selector)
				{
					if(selectors == null)
					{
						selectors = new CList<>();
					}
					selectors.add((Selector)x);
				}
				else if(x instanceof FxCssProp)
				{
					if(!epilogue)
					{
						// write selector prologue
						boolean first = true;
						if(parentSelectors != null)
						{
							for(Selector sel: parentSelectors)
							{
								writeSelector(sb, first, sel);
								first = false;
							}
						}

						writeSelector(sb, first, selector);
						sb.a("\n{\n");
						epilogue = true;
					}
					
					sb.a("\t");
					((FxCssProp)x).write(sb);
					sb.nl();
				}
				else if(x != null)
				{
					throw new Error("?" + x);
				}
			}

			if(epilogue)
			{
				sb.a("}\n\n");
			}

			if(selectors != null)
			{
				for(Selector sel: selectors)
				{
					sel.write(sb, chain(parentSelectors, this));
				}
			}			
		}
	}
}
