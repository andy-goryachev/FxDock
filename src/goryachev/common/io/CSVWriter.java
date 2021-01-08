// Copyright © 2012-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.List;


public class CSVWriter
	implements Closeable
{
	private Writer wr;
	private char delimiter = ',';
	private Character quoteChar = '"';
	private Character escapeChar = '\\';
	private String lineEnd = "\r\n";
	private boolean forceQuote;
	private int column;


	public CSVWriter(Writer wr)
	{
		this.wr = CKit.toBufferedWriter(wr);
	}
	
	
	public void setDelimiterChar(Character c)
	{
		delimiter = c;
	}
	
	
	public void setQuoteChar(Character c)
	{
		quoteChar = c;
	}
	
	
	public void setEscapeChar(Character c)
	{
		escapeChar = c;
	}

	
	public void setLineEnd(String s)
	{
		lineEnd = s;
	}
	
	
	public void setForceQuote(boolean on)
	{
		forceQuote = on;
	}
	
	
	public void write(String s) throws Exception
	{
		wr.write(s);
	}
	
	
	public void write(char c) throws Exception
	{
		wr.write(c);
	}
	

	public void writeLines(List<String[]> cells) throws Exception
	{
		for(String[] line: cells)
		{
			writeLine(line);
		}
	}

	
	public void writeLine(String[] cells) throws Exception
	{
		if(cells != null)
		{
			for(String s: cells)
			{
				writeCell(s);
			}
		}
		
		writeNewLine();
	}
	
	
	public void writeCell(Object x) throws Exception
	{
		if(column > 0)
		{
			wr.write(delimiter);
		}
		
		if(x != null)
		{
			String cell = x.toString();
			if(containsSpecialCharacters(cell))
			{
				if(quoteChar != null)
				{
					wr.write(quoteChar);
				}
				
				cell = convertCell(cell);
				wr.write(cell);
				
				if(quoteChar != null)
				{
					wr.write(quoteChar);
				}
			}
			else
			{
				if(forceQuote)
				{
					if(quoteChar != null)
					{
						wr.write(quoteChar);
					}
				}
				
				wr.write(cell);
				
				if(forceQuote)
				{
					if(quoteChar != null)
					{
						wr.write(quoteChar);
					}
				}
			}
		}
		
		column++;
	}
	
	
	public void writeNewLine() throws Exception
	{
		wr.write(lineEnd);
		column = 0;
	}


	protected boolean containsSpecialCharacters(String s)
	{
		if(s.startsWith(" "))
		{
			return true;
		}
		else if(s.endsWith(" "))
		{
			return true;
		}
			
		if(quoteChar != null)
		{
			if(s.indexOf(quoteChar) >= 0)
			{
				return true;
			}
		}
		
		if(s.indexOf(delimiter) >= 0)
		{
			return true;
		}
		
		if(s.indexOf('\n') >= 0)
		{
			return true;
		}

		return false;
	}


	protected String convertCell(String s)
	{
		SB sb = new SB(s.length() + 32);
		for(int i=0; i<s.length(); i++)
		{
			char c = s.charAt(i);
			if((quoteChar != null) && (c == quoteChar))
			{
				sb.append(quoteChar).append(quoteChar);
			}
			else if((escapeChar != null) && (c == escapeChar))
			{
				sb.append(escapeChar).append(c);
			}
			else
			{
				sb.append(c);
			}
		}

		return sb.toString();
	}


	public void flush() throws IOException
	{
		wr.flush();
	}


	public void close() throws IOException
	{
		wr.close();
	}
}
