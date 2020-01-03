// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


public class CSVReader
	implements Closeable
{
	public final CSVParser parser;
	private BufferedReader rd;
	private boolean hasNext = true;
	private int skipLineCount;
	private boolean linesSkipped;


	public CSVReader(Reader rd)
	{
		this.rd = new BufferedReader(rd);
		this.parser = new CSVParser();
	}
	
	
	public CSVReader(String text)
	{
		this(new StringReader(text));
	}
	
	
	public void setSeparatorChar(char c)
	{
		parser.setSeparatorChar(c);
	}
	
	
	public void setQuoteChar(char c)
	{
		parser.setQuoteChar(c);
	}
	
	
	public void setEscapeChar(char c)
	{
		parser.setEscapeChar(c);
	}

	
	public void setStrictQuotes(boolean on)
	{
		parser.setStrictQuotes(on);
	}
	
	
	public void setIgnoreLeadingWhitespace(boolean on)
	{
		parser.setIgnoreLeadingWhitespace(on);
	}
	
	
	public void setSkipLineCount(int n)
	{
		skipLineCount = n;
	}


	public CList<String[]> read() throws Exception
	{
		CList<String[]> cells = new CList<String[]>();
		while(hasNext)
		{
			String[] nextLineAsTokens = readNext();
			if(nextLineAsTokens != null)
			{
				cells.add(nextLineAsTokens);
			}
		}
		return cells;
	}


	public String[] readNext() throws Exception
	{
		String[] result = null;
		do
		{
			String nextLine = getNextLine();
			if(!hasNext)
			{
				return result;
			}
			
			String[] r = parser.parseLineMulti(nextLine);
			if(r.length > 0)
			{
				if(result == null)
				{
					result = r;
				}
				else
				{
					String[] t = new String[result.length + r.length];
					System.arraycopy(result, 0, t, 0, result.length);
					System.arraycopy(r, 0, t, result.length, r.length);
					result = t;
				}
			}
			
		} while(parser.isPending());
		return result;
	}


	protected String getNextLine() throws Exception
	{
		if(!linesSkipped)
		{
			for(int i = 0; i < skipLineCount; i++)
			{
				rd.readLine();
			}
			linesSkipped = true;
		}
		
		String nextLine = rd.readLine();
		if(nextLine == null)
		{
			hasNext = false;
		}
		return hasNext ? nextLine : null;
	}


	public void close() throws IOException
	{
		rd.close();
	}
}
