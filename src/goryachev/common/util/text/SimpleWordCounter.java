// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.TextTools;


public class SimpleWordCounter
{
	private enum Type 
	{
		CJK,
		Space,
		Word
	};
	
	private final String text;
	private Type type;
	private int count;
	
	
	public SimpleWordCounter(String text)
	{
		this.text = text;
	}
	
	
	public int countWords()
	{
		count = 0;
		int len = text.length();
		type = Type.Space;
		
		for(int i=0; i<len; i++)
		{
			char c = text.charAt(i);
			Type t = getType(c);
			switch(t)
			{
			case CJK:
				count++;
				break;
			case Word:
				if(type != t)
				{
					count++;
				}
				break;
			}

			type = t;
		}
		
		return count;
	}
	
	
	protected void cnt()
	{
		switch(type)
		{
		case CJK:
		case Word:
			count++;
			break;
		}
	}
	
	
	protected Type getType(char c)
	{
		if(TextTools.isWhitespace(c))
		{
			return Type.Space;
		}
		else if(TextTools.isWordDelimiter(c))
		{
			return Type.Space;
		}
		else if(TextTools.isCJK(c))
		{
			return Type.CJK;
		}
		else
		{
			return Type.Word;
		}
	}
	
	
	public static int count(String s)
	{
		if(s == null)
		{
			return 0;
		}
		
		return new SimpleWordCounter(s).countWords();
	}
}
