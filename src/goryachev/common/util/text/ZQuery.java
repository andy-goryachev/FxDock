// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.CList;


public class ZQuery
{
	private final String expression;
	private CList<QuerySegment> includes;
	private CList<QuerySegment> excludes;
	
	
	public ZQuery(String expression)
	{
		this.expression = expression;
		
		ZQueryParser p = new ZQueryParser(expression);
		p.parse();
		this.includes = p.getIncludes();
		this.excludes = p.getExcludes();
	}
	
	
	public int includedSegmentCount()
	{
		return includes == null ? 0 : includes.size();
	}
	
	
	public QuerySegment getIncludeSegment(int ix)
	{
		return includes.get(ix);
	}
	
	
	public String getExpression()
	{
		return expression;
	}
	
	
	public String toString()
	{
		return getExpression();
	}
	
	
	public boolean isIncluded(String s)
	{
		if(includes == null)
		{
			return true;
		}
		if(s == null)
		{
			return false;
		}
		
		int sz = includes.size();
		if(sz == 0)
		{
			return true;
		}
		
		for(int i=0; i<sz; i++)
		{
			QuerySegment seg = includes.get(i);
			if(!seg.isMatch(s))
			{
				// all must match
				return false;
			}
		}
		
		return true;
	}
	
	
	public boolean isExcluded(String s)
	{
		if(excludes == null)
		{
			return false;
		}
		
		if(s == null)
		{
			return false;
		}
		
		int sz = excludes.size();
		if(sz == 0)
		{
			return true;
		}
		
		for(int i=0; i<sz; i++)
		{
			QuerySegment seg = excludes.get(i);
			if(seg.isMatch(s))
			{
				// any one match
				return true;
			}
		}
		
		return false;
	}
}
