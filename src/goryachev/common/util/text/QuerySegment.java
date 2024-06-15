// Copyright © 2013-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.SB;


public abstract class QuerySegment
{
	public abstract boolean isMatch(String s);
	
	public abstract int indexOf(String s, int fromIndex);
	
	public abstract int length();
	
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
		
		
		@Override
		public boolean isMatch(String s)
		{
			s = normalize(s);
			return s.contains(pattern);
		}
		
		
		@Override
		public String toString()
		{
			return "I." + pattern; 
		}


		@Override
		public int indexOf(String s, int fromIndex)
		{
			s = normalize(s);
			return s.indexOf(pattern, fromIndex);
		}


		@Override
		public int length()
		{
			return pattern.length();
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
		
		
		@Override
		public boolean isMatch(String s)
		{
			return s.contains(pattern);
		}
		
		
		@Override
		public String toString()
		{
			return "E." + pattern; 
		}
		
		
		@Override
		public int indexOf(String s, int fromIndex)
		{
			return s.indexOf(pattern, fromIndex);
		}
		
		
		@Override
		public int length()
		{
			return pattern.length();
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
		
		
		@Override
		public boolean isMatch(String s)
		{
			s = normalize(s);
			return s.contains(pattern);
		}
		
		
		@Override
		public String toString()
		{
			return "X." + pattern; 
		}
		
		
		@Override
		public int indexOf(String s, int fromIndex)
		{
			s = normalize(s);
			return s.indexOf(pattern, fromIndex);
		}
		
		
		@Override
		public int length()
		{
			return pattern.length();
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
		
		
		@Override
		public boolean isMatch(String s)
		{
			return s.contains(pattern);
		}
		
		
		@Override
		public String toString()
		{
			return "XE." + pattern; 
		}
		
		
		@Override
		public int indexOf(String s, int fromIndex)
		{
			return s.indexOf(pattern, fromIndex);
		}
		
		
		@Override
		public int length()
		{
			return pattern.length();
		}
	}
}