// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CLanguage;
import java.util.ArrayList;


// {0}, {1}, etc.
// {{ codes for a single {
// plural forms {0,plural1,plural2,...}
public class TXTFormat
{
	private CLanguage language;
	private String fmt;
	private Object[] args;
	private StringBuilder sb;


	public TXTFormat(CLanguage la, String fmt, Object[] args)
	{
		this.language = la;
		this.fmt = fmt;
		this.args = args;
		sb = new StringBuilder();
	}


	public String format()
	{
		int ix = 0;
		do
		{
			ix = replace(ix);
		} while(ix >= 0);
		return sb.toString();
	}

	
	protected static String[] split(String part)
	{
		// TODO optimize
		return part.split(",");
	}


	protected int charAt(int ix)
	{
		if(ix >= fmt.length())
		{
			return -1;
		}
		else
		{
			return fmt.charAt(ix);
		}
	}
	
	
	protected static boolean isNumber(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}


	protected int replace(int start)
	{
		int ix = fmt.indexOf('{', start);
		if(ix < 0)
		{
			sb.append(fmt.substring(start, fmt.length()));
			return -1;
		}
		else
		{
			if(start != ix)
			{
				sb.append(fmt.substring(start, ix));
			}
			
			int c = charAt(ix+1);
			if(c < 0)
			{
				// single {
				sb.append('{');
				return -1;
			}
			else if(c == '{')
			{
				// {{ codes for {
				sb.append('{');
				return ix+2;
			}
			else if(Character.isDigit(c))
			{
				// {0
			}
			else
			{
				// {?
				sb.append('{');
				sb.append((char)c);
				return ix+2;
			}

			int ex = fmt.indexOf('}', ix);
			if(ex < 0)
			{
				sb.append(fmt.substring(ix, fmt.length()));
				return -1;
			}

			String part = fmt.substring(ix + 1, ex);
			try
			{
				int cix = part.indexOf(',');
				if(cix < 0)
				{
					// argument
					int index = Integer.parseInt(part);
					
					Object d;
					try
					{
						d = args[index];
					}
					catch(Exception e)
					{
						d = null;
					}

					if(d != null)
					{
						sb.append(d.toString());
					}
				}
				else
				{
					// plural form
					String parts[] = split(part);
					
					int index = Integer.parseInt(parts[0]);
					int number;
					try
					{
						number = Integer.parseInt(args[index].toString());
					}
					catch(Exception e)
					{
						number = 0;
					}

					int pluralIndex = (language == null ? 0 : PluralRules.getQuantityIndex(language, number));
					
					try
					{
						sb.append(parts[pluralIndex + 1]);
					}
					catch(Exception e)
					{ }
				}
			}
			catch(Exception e)
			{
				sb.append('{').append(part).append('}');
				return ex+1;
			}
			
			return ex + 1;
		}
	}
	

	public static String[] extractVariables(String s)
	{
		int ix = s.indexOf('{');
		if(ix < 0)
		{
			return null;
		}
		
		ArrayList<String> a = new ArrayList();
		while(ix >= 0)
		{
			ix += 1;
			if(ix < s.length())
			{
				char c = s.charAt(ix);
				if(c == '{')
				{
					a.add("{{");
					++ix;
					if(ix >= s.length())
					{
						// done
						break;
					}
				}
				else
				{
					int ex = s.indexOf('}', ix);
					if(ex < 0)
					{
						// malformed, done
						break;
					}
					else
					{
						String fmt = s.substring(ix,ex);
						if(isNumber(fmt))
						{
							a.add('{' + fmt + '}');
						}
					}
				}
			}
			else
			{
				// malformed string, done
				break;
			}
			
			ix = s.indexOf('{', ix);
		}
		
		return a.toArray(new String[a.size()]);
	}
	

	public static String[] extractPlurals(String s, CLanguage lang)
	{
		int ix = s.indexOf('{');
		if(ix < 0)
		{
			return null;
		}
		
		ArrayList<String> a = new ArrayList();
		while(ix >= 0)
		{
			ix += 1;
			if(ix < s.length())
			{
				char c = s.charAt(ix);
				if(c == '{')
				{
					a.add("{{");
					++ix;
					if(ix >= s.length())
					{
						// done
						break;
					}
				}
				else
				{
					int ex = s.indexOf('}', ix);
					if(ex < 0)
					{
						// malformed, done
						break;
					}
					else
					{
						String fmt = s.substring(ix,ex);
						if(fmt.indexOf(',') >= 0)
						{
							String[] parts = split(fmt);
							try
							{
								if(isNumber(parts[0]))
								{
									a.add('{' + fmt + '}');
								}
							}
							catch(Exception e)
							{ 
								// ignore
							}
						}
					}
				}
			}
			else
			{
				// malformed string, done
				break;
			}
			
			ix = s.indexOf('{', ix);
		}
		
		return a.toArray(new String[a.size()]);
	}
}
