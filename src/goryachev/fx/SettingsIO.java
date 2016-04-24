// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;


/**
 * SettingsIO.
 */
public class SettingsIO
{
	public static class Reader
	{
		private final String text;
		private int pos;
		
		
		public Reader(String text)
		{
			this.text = text;
		}
		
		
		public int readInt(int defaultValue)
		{
			String s = readString();
			if(CKit.isNotBlank(s))
			{
				try
				{
					return Integer.parseInt(s);
				}
				catch(Exception ignore)
				{ }
			}
			return defaultValue;
		}
		
		
		public String readString()
		{
			// FIX
			return null;
		}
		
		
		public char readChar(char defaultValue)
		{
			String s = readString();
			if(s != null)
			{
				if(s.length() > 0)
				{
					return s.charAt(0);
				}
			}
			return defaultValue;
		}
	}
	
	
	//
	
	
	public static class Writer
	{
		private final SB sb;
		
		
		public Writer()
		{
			sb = new SB();
		}
		
		
		public void comma()
		{
			sb.a(',');
		}
		
		
		public void write(int x)
		{
			sb.a(String.valueOf(x));
		}
		
		
		public void write(char x)
		{
			sb.a(String.valueOf(x));
		}
		
		
		public void write(double x)
		{
			int y = (int)x;
			if(y == x)
			{
				sb.a(String.valueOf(y));
			}
			else
			{
				sb.a(String.valueOf(x));
			}
		}
		
		
		public String toString()
		{
			return sb.toString();
		}
	}
}
