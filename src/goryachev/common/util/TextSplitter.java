// Copyright © 2013-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public abstract class TextSplitter
{
	public abstract boolean isTextSeparator(char c);
	
	//
	
	public TextSplitter()
	{
	}
	
	
	public CList<String> split(String text)
	{
		CList<String> list = new CList();
		int start = 0;
		int len = text.length();
		boolean white = true;
		
		for(int i=0; i<len; i++)
		{
			char c = text.charAt(i);
			if(isTextSeparator(c))
			{
				if(!white)
				{
					if(i > start)
					{
						add(list, text.substring(start, i));
					}
					white = true;
				}
			}
			else
			{
				if(white)
				{
					start = i;
					white = false;
				}
			}
		}
		
		if(!white)
		{
			if(start < len)
			{
				add(list, text.substring(start, len));
			}
		}
		
		return list;
	}
	
	
	protected void add(CList<String> list, String s)
	{
		if(s.length() > 0)
		{
			list.add(s);
		}
	}
}
