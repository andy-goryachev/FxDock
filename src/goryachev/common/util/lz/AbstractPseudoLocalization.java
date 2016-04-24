// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;
import goryachev.common.util.Rex;
import goryachev.common.util.SB;
import java.util.Random;


public abstract class AbstractPseudoLocalization
	extends PseudoLocalization
{
	/** 
	 * Example 
	 * <pre>
		protected void initMap(CMap<Character,String> m)
		{
			addBulk
			(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
			);
			add
			(
				"e", "её",
				"B", "ВБ",
				"b", "ьъб",
				"E", "ЕЁ",
				"W", "ШЩ",
				"w", "шщ"
			);
		}
	*/
	protected abstract void initMap(CMap<Character,String> m);
	
	//
	
	private CMap<Character,String> substitutionMap;
	private CMap<String,String> cache = new CMap(2048);
	private Random random = new Random();
	
	
	public AbstractPseudoLocalization()
	{
	}
	
	
	public synchronized String getPrompt(String id, String master)
	{
		// cache prompts to ensure consistency between different invocations of the same prompt
		String rv = cache.get(id);
		if(rv == null)
		{
			// pseudolocalize the prompt
			rv = pseudoLocalize(master);
			cache.put(id, rv);
		}
		return rv;
	}
	
	
	public synchronized String pseudoLocalize(String master)
	{
		try
		{
			int sz = master.length();
			SB sb = new SB(sz + sz);
			for(int i=0; i<sz; i++)
			{
				char c = master.charAt(i);
				c = substitute(c);
				sb.append(c);
			}
			return sb.toString();
		}
		catch(NullPointerException e)
		{
			// first call to pseudoLocalize() will see null substitutionMap and throw an NPE
			init();
			// after init, all should work
			return pseudoLocalize(master);
		}
	}
	
	
	protected char substitute(char c)
	{
		String s = substitutionMap.get(c);
		if(s != null)
		{
			return substitute(s);
		}
		return c;
	}
	
	
	private void init()
	{
		substitutionMap = new CMap();
		initMap(substitutionMap);
	}

	
	protected char substitute(String s)
	{
		int len = s.length();
		if(len == 1)
		{
			return s.charAt(0);
		}
		else
		{
			int ix = random(len);
			return s.charAt(ix);
		}
	}
	
	
	protected synchronized int random(int max)
	{
		return random.nextInt(max);
	}
	
	
	/** adds entry where first character is source, the rest of them are used in pseudolocalization */
	protected void add(String ... ss)
	{
		for(int i=0; i<ss.length; )
		{
			String s = ss[i++];
			if(s.length() < 2)
			{
				throw new Rex("must contain at least two characters: " + s);
			}
			
			char k = s.charAt(0);
			substitutionMap.put(k, s);
		}
	}
}
