// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;


public class CSVParser
{
	private char separator = ',';
	private char quoteChar = '"';
	private char escapeChar = '\\';
	private boolean strictQuotes;
	private boolean ignoreLeadingWhiteSpace = true;
	private boolean inCell;
	private String savedLine;


	public CSVParser()
	{
	}
	
	
	public void setSeparatorChar(char c)
	{
		separator = c;
	}
	
	
	public void setQuoteChar(char c)
	{
		quoteChar = c;
	}
	
	
	public void setEscapeChar(char c)
	{
		escapeChar = c;
	}

	
	public void setStrictQuotes(boolean on)
	{
		strictQuotes = on;
	}
	
	
	public void setIgnoreLeadingWhitespace(boolean on)
	{
		ignoreLeadingWhiteSpace = on;
	}
	

	protected boolean isPending()
	{
		return savedLine != null;
	}


	protected String[] parseLineMulti(String nextLine) throws Exception
	{
		return parseLine(nextLine, true);
	}


	public String[] parseLine(String nextLine) throws Exception
	{
		return parseLine(nextLine, false);
	}


	protected String[] parseLine(String nextLine, boolean multi) throws Exception
	{
		if(!multi && savedLine != null)
		{
			savedLine = null;
		}

		if(nextLine == null)
		{
			if(savedLine != null)
			{
				String s = savedLine;
				savedLine = null;
				return new String[] { s };
			}
			else
			{
				return null;
			}
		}

		CList<String> tokensOnThisLine = new CList<String>();
		StringBuilder sb = new StringBuilder(256);
		boolean inQuotes = false;
		if(savedLine != null)
		{
			sb.append(savedLine);
			savedLine = null;
			inQuotes = true;
		}

		for(int i = 0; i < nextLine.length(); i++)
		{
			char c = nextLine.charAt(i);
			if(c == this.escapeChar)
			{
				if(isNextCharacterEscapable(nextLine, inQuotes || inCell, i))
				{
					sb.append(nextLine.charAt(i + 1));
					i++;
				}
			}
			else if(c == quoteChar)
			{
				if(isNextCharacterEscapedQuote(nextLine, inQuotes || inCell, i))
				{
					sb.append(nextLine.charAt(i + 1));
					i++;
				}
				else
				{
					if(!strictQuotes)
					{
						if
						(
							(i > 2) &&
							(nextLine.charAt(i - 1) != separator) &&
							(nextLine.length() > (i + 1) && nextLine.charAt(i + 1) != separator)
						)
						{
							if(ignoreLeadingWhiteSpace && sb.length() > 0 && isWhiteSpace(sb))
							{
								sb.setLength(0);
							}
							else
							{
								sb.append(c);
							}
						}
					}

					inQuotes = !inQuotes;
				}
				inCell = !inCell;
			}
			else if(c == separator && !inQuotes)
			{
				tokensOnThisLine.add(sb.toString());
				sb.setLength(0);
				inCell = false;
			}
			else
			{
				if(!strictQuotes || inQuotes)
				{
					sb.append(c);
					inCell = true;
				}
			}
		}
		
		if(inQuotes)
		{
			if(multi)
			{
				sb.append("\n");
				savedLine = sb.toString();
				sb = null;
			}
			else
			{
				throw new Exception("quoted value must be terminated");
			}
		}
		if(sb != null)
		{
			tokensOnThisLine.add(sb.toString());
		}
		return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);
	}


	protected boolean isNextCharacterEscapable(String nextLine, boolean inQuotes, int i)
	{
		return 
			inQuotes &&
			(nextLine.length() > (i + 1)) &&
			(nextLine.charAt(i + 1) == quoteChar || nextLine.charAt(i + 1) == escapeChar);
	}


	protected boolean isNextCharacterEscapedQuote(String nextLine, boolean inQuotes, int i)
	{
		return 
			inQuotes &&
			(nextLine.length() > (i + 1)) &&
			(nextLine.charAt(i + 1) == quoteChar);
	}


	protected boolean isWhiteSpace(CharSequence sb)
	{
		boolean result = true;
		for(int i = 0; i < sb.length(); i++)
		{
			char c = sb.charAt(i);
			if(!Character.isWhitespace(c))
			{
				return false;
			}
		}
		return result;
	}
}
