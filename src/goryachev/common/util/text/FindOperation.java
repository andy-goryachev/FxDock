// Copyright © 2014-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.CKit;
import goryachev.common.util.ElasticIntArray;
import java.util.Locale;


public class FindOperation
{
	private final Locale locale;
	private final String pattern;
	private final boolean caseSensitive;
	private final boolean ignoreAccents;
	private final boolean wholeWords;
	
	
	public FindOperation(Locale locale, String pattern, boolean caseSensitive, boolean ignoreAccents, boolean wholeWords)
	{
		this.locale = locale;
		this.caseSensitive = caseSensitive;
		this.ignoreAccents = ignoreAccents;
		this.wholeWords = wholeWords;
		this.pattern = normalize(pattern);
	}
	
	
	protected String normalize(String s)
	{
		if(s == null)
		{
			return null;
		}
		
		if(!caseSensitive)
		{
			s = s.toLowerCase(locale);
		}
		
		if(ignoreAccents)
		{
			s = AccentedCharacters.removeAccents(s);
		}
		
		return s;
	}

	
	protected static boolean isWholeWord(String a, int ix, int len)
	{
		if(ix > 0)
		{
			if(!isWordSpace(a.charAt(ix-1)))
			{
				return false;
			}
		}
		
		ix += len;
		if(ix < a.length())
		{
			if(!isWordSpace(a.charAt(ix)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	public static boolean isWordSpace(char c)
	{
		return !Character.isLetterOrDigit(c);
	}
	

	/* returns array of indexes to match offsets or null if no match is found */
	public FindOperationResult find(String text)
	{
		if(CKit.isBlank(text))
		{
			return null;
		}
		
		text = normalize(text);

		int ix = text.indexOf(pattern);
		if(ix < 0)
		{
			return null;
		}

		ElasticIntArray ixs = null;
		while(ix >= 0)
		{
			if(wholeWords)
			{
				if(!isWholeWord(text, ix, pattern.length()))
				{
					ix = text.indexOf(pattern, ix+1);
					continue;
				}
			}
			
			if(ixs == null)
			{
				ixs = new ElasticIntArray(16);
			}
			
			ixs.add(ix);
			ix += pattern.length();
			ix = text.indexOf(pattern, ix);
		}

		if(ixs == null)
		{
			return null;
		}
		
		return new FindOperationResult(text, pattern, ixs.toArray());
	}
}