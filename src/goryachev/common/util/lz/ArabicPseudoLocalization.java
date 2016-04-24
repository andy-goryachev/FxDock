// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;


public class ArabicPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
	}
	
	
	public synchronized String pseudoLocalize(String master)
	{
		if(master == null)
		{
			return null;
		}
		
		String prefix = "";
		String html = "<html>";
		if(CKit.startsWithIgnoreCase(master, html))
		{
			prefix = master.substring(0, html.length());
			master = master.substring(html.length());
		}
		
		return prefix + "ا" + master + "ه";
	}
}
