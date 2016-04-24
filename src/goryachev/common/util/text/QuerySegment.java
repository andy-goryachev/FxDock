// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.SB;


public abstract class QuerySegment
{
	public abstract boolean isMatch(String s);
	
	//
	
	
	public QuerySegment()
	{
	}
	
	
	protected static String normalize(String s)
	{
		SB sb = new SB(s);
		AccentedCharacters.removeAccents(sb);
		sb.toLowerCase();
		return sb.toString();
	}
	
	
	//
	
	
	public static class Include 
		extends QuerySegment
	{
		private String pattern;
		
		
		public Include(String pattern)
		{
			this.pattern = normalize(pattern);
		}
		
		
		public boolean isMatch(String s)
		{
			s = normalize(s);
			return s.contains(pattern);
		}
		
		
		public String toString()
		{
			return "I." + pattern; 
		}
	}
	
	
	//
	
	
	public static class Exact 
		extends QuerySegment
	{
		private String pattern;
		
		
		public Exact(String pattern)
		{
			this.pattern = pattern;
		}
		
		
		public boolean isMatch(String s)
		{
			return s.contains(pattern);
		}
		
		
		public String toString()
		{
			return "E." + pattern; 
		}
	}
	
	
	//
	
	
	public static class Exclude 
		extends QuerySegment
	{
		private String pattern;
		
		
		public Exclude(String pattern)
		{
			this.pattern = pattern;
		}
		
		
		public boolean isMatch(String s)
		{
			s = normalize(s);
			return s.contains(pattern);
		}
		
		
		public String toString()
		{
			return "X." + pattern; 
		}
	}
	
	
	
	//
	
	
	public static class ExcludeExact 
		extends QuerySegment
	{
		private String pattern;
		
		
		public ExcludeExact(String pattern)
		{
			this.pattern = pattern;
		}
		
		
		public boolean isMatch(String s)
		{
			return s.contains(pattern);
		}
		
		
		public String toString()
		{
			return "XE." + pattern; 
		}
	}
}