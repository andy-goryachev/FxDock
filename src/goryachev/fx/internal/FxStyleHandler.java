// Copyright © 2022-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * A utility for adding/removing CSS styles.
 */
public class FxStyleHandler
{
	private final CMap<String,String> styles = new CMap();
	
	
	public FxStyleHandler(String style)
	{
		if(style != null)
		{
			StringTokenizer tok = new StringTokenizer(style, ";");
			while(tok.hasMoreElements())
			{
				String s = tok.nextToken();
				int ix = s.indexOf(':');
				if(ix > 0)
				{
					String prop = s.substring(0, ix);
					styles.put(prop, s);
				}
			}
		}
	}


	public void put(String property, Object value)
	{
		String v = property + ":" + value;
		styles.put(property, v);
	}
	
	
	public void remove(String property)
	{
		styles.remove(property);
	}


	public String toStyleString()
	{
		SB sb = new SB(256);
		for(Map.Entry<String,String> en: styles.entrySet())
		{
			sb.append(en.getValue()).append(';');
		}
		return sb.toString();
	}
}
