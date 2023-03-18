// Copyright © 2009-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;

import goryachev.common.util.CList;


// simple comma-delimited file path parser.
// accepts:
//    a, b, " c with spaces and ,,, commas inside "
//
// special cases:
//    [a, b"c] - quote will be ignored
public class CommaDelimitedParser
{
	private String text;
	private boolean trim = true;
	private char quote = '"';
	private char comma = ',';
	private int start = 0;
	private CList<String> list;


	public CommaDelimitedParser(String text)
	{
		this.text = text;
	}
	
	
	public char getQuoteChar() { return quote; }
	public void setQuoteChar(char c) { quote = c; }
	public char getCommaChar() { return comma; }
	public void setCommaChar(char c) { comma = c; }
	public boolean getTrim() { return trim; }
	public void setTrim(boolean on) { trim = on; }
	
	
	public String[] parse()
	{
		list = new CList<>();
		
		boolean inQuotes = false;
		int i = 0;
		for( ; i<text.length(); i++)
		{
			char c = text.charAt(i);
			
			if(inQuotes)
			{
				if(c == quote)
				{
					inQuotes = false;
				}
			}
			else if(c == comma)
			{
				add(i);
				start = i + 1;
			}
			else if(c == quote)
			{
				// only if right after comma!
				if(afterComma(i))
				{
					inQuotes = true;
				}
			}
		}
		add(i);
		
		int sz = list.size();
		if(sz == 0)
		{
			return null;
		}
		else
		{
			return list.toArray(new String[sz]); 
		}
	}
	
	
	protected boolean afterComma(int end)
	{
		String s = text.substring(start, end);
		return (s.trim().length() == 0);
	}
	
	
	protected void add(int end)
	{
		if(end > start)
		{
			String s = text.substring(start,end);
			start = end;
			
			if(trim)
			{
				s = s.trim();
				if(s.length() == 0)
				{
					return;
				}
			}
			
			list.add(s);
		}
	}
}
