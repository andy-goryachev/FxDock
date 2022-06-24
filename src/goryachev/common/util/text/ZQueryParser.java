// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;


public class ZQueryParser
{
	private final String expression;
	private CList<QuerySegment> includes = new CList();
	private CList<QuerySegment> excludes = new CList();
	private boolean quote;
	private boolean white;
	private boolean plusMinus;
	private boolean done;
	
	
	public ZQueryParser(String expression)
	{
		this.expression = expression;
	}
	
	
	public CList<QuerySegment> getIncludes()
	{
		if(includes.size() == 0)
		{
			return null;
		}
		return includes;
	}
	
	
	public CList<QuerySegment> getExcludes()
	{
		if(excludes.size() == 0)
		{
			return null;
		}
		return excludes;
	}


	protected void add(String s)
	{
		if(s.length() > 0)
		{
			char c = s.charAt(0);
			switch(c)
			{
			case '+':
				// include
				if(s.startsWith("+\""))
				{
					if(s.length() > 3)
					{
						includes.add(new QuerySegment.Exact(s.substring(2, s.length()-1)));
					}
					else
					{
						includes.add(new QuerySegment.Exact(s.substring(1, s.length()-1)));
					}
				}
				else
				{
					if(s.length() > 1)
					{
						includes.add(new QuerySegment.Exact(s.substring(1, s.length())));
					}
					else
					{
						includes.add(new QuerySegment.Include("+"));
					}
				}
				break;
			case '-':
				// exclude
				if(s.startsWith("-\""))
				{
					if(s.length() > 3)
					{
						excludes.add(new QuerySegment.ExcludeExact(s.substring(2, s.length()-1)));
					}
					else
					{
						excludes.add(new QuerySegment.Exclude(s.substring(1, s.length()-1)));
					}
				}
				else
				{
					if(s.length() > 1)
					{
						excludes.add(new QuerySegment.Exclude(s.substring(1, s.length())));
					}
					else
					{
						includes.add(new QuerySegment.Include("-"));
					}
				}
				break;
			case '"':
				// exact
				if(s.endsWith("\""))
				{
					if(s.length() > 2)
					{
						includes.add(new QuerySegment.Exact(s.substring(1, s.length()-1)));
					}
					else
					{
						includes.add(new QuerySegment.Include("\""));
					}
				}
				else
				{
					includes.add(new QuerySegment.Include(s));
				}
				break;
			default:
				// regular token
				includes.add(new QuerySegment.Include(s));
				break;
			}
		}
	}
	
	
	protected int getChar(int ix)
	{
		if(ix < 0)
		{
			return -1;
		}
		else if(ix < expression.length())
		{
			return expression.charAt(ix);
		}
		else
		{
			return -1;
		}
	}
	
	
	protected boolean isStartQuote(int ix)
	{
		if(quote)
		{
			return false;
		}
		
		if(plusMinus)
		{
			return true;
		}
		
		return white;
	}
	
	
	public void parse()
	{
		if(done)
		{
			return;
		}
		
		if(expression != null)
		{
			white = true;
			int start = 0;
			int sz = expression.length();
			
			for(int i=0; i<sz; i++)
			{
				char c = expression.charAt(i);
				
				if(c == '"')
				{
					if(isStartQuote(i))
					{
						if(white)
						{
							start = i;
						}
						white = false;
						quote = true;
						continue;
					}
					else
					{
						quote = false;
						continue;
					}
				}
				
				if(CKit.isBlank(c))
				{
					if(!quote)
					{
						if(!white)
						{
							String sub = expression.substring(start, i);
							add(sub);
							white = true;
						}
					}
					plusMinus = false;
				}
				else
				{
					if(white)
					{
						start = i;
						white = false;
						
						switch(c)
						{
						case '+':
						case '-':
							plusMinus = true;
						}
					}
					else
					{
						plusMinus = false;
					}
				}
			}
			
			if(!white)
			{
				String sub = expression.substring(start, sz);
				add(sub);
			}
		}
		
		done = true;
	}
}
