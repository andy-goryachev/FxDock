// Copyright © 2009-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.StringTokenizer;


public abstract class BasicFileFilter
	implements FileFilter
{
	public static final FileFilter ACCEPT = new FileFilter() { public boolean accept(File f) { return true; } };
	public static final FileFilter DENY = new FileFilter() { public boolean accept(File f) { return false; } };


	//


	public static BasicFileFilter parse(final String spec)
	{
		if(spec.contains("*") || spec.contains("?"))
		{
			return parseWildCard(spec);
		}
		else
		{
			return new BasicFileFilter()
			{
				public boolean accept(File f)
				{
					if(f != null)
					{
						return spec.equalsIgnoreCase(f.getName());
					}
					return false;
				}
			};
		}
	}
	
	
	public static BasicFileFilter parseWildCard(final String spec)
	{
		ArrayList<SegmentMatcher> a = new ArrayList();
		StringTokenizer tok = new StringTokenizer(spec.toLowerCase(), "*?", true);
		boolean wild = false; 
		boolean star = false;
		while(tok.hasMoreTokens())
		{
			String s = tok.nextToken();
			if("*".equals(s))
			{
				star = true;
			}
			else if("?".equals(s))
			{
				a.add(new AnyCharMatcher());
				wild = false;
			}
			else
			{
				if(star)
				{
					a.add(new WildcardMatcher(s));
					wild = true;
				}
				else
				{
					a.add(new RegularMatcher(s));
					wild = false;
				}
				star = false;
			}
		}
		
		if(star)
		{
			a.add(new WildcardMatcher(""));
		}
		else if(!wild)
		{
			a.add(new EndMatcher());
		}
		
		return new WildCardFileFilter(a.toArray(new SegmentMatcher[a.size()]));
	}
	
	
	//
	
	
	public static class WildCardFileFilter extends BasicFileFilter
	{
		private SegmentMatcher[] segments;
		
		
		public WildCardFileFilter(SegmentMatcher[] segments)
		{
			this.segments = segments;
		}


		public boolean accept(File f)
		{
			String name = f.getName().toLowerCase();
			int ix = 0;
			for(SegmentMatcher s: segments)
			{
				ix = s.match(name,ix);
				if(ix < 0)
				{
					return false;
				}
			}
			
			return true;
		}
		
		
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			for(SegmentMatcher s: segments)
			{
				s.dump(sb);
			}
			return sb.toString();
		}
	}
	
	
	//
	
	
	protected abstract static class SegmentMatcher
	{
		// returns -1 if no match, otherwise the next position
		public abstract int match(String text, int start);

		public abstract void dump(StringBuilder sb);
	}
	
	
	//
	
	
	protected static class RegularMatcher extends SegmentMatcher
	{
		String pattern;
		
		
		public RegularMatcher(String pattern)
		{
			this.pattern = pattern;
		}
		

		public int match(String text, int start)
		{
			if(text.startsWith(pattern, start))
			{
				return start + pattern.length();
			}
			else
			{
				return -1;
			}
		}
		
		
		public void dump(StringBuilder sb)
		{
			sb.append("<M:").append(pattern).append(">");
		}
	}
	
	
	//
	
	
	protected static class WildcardMatcher extends SegmentMatcher
	{
		String pattern;

		
		public WildcardMatcher(String pattern)
		{
			this.pattern = pattern;
		}
		
		
		// returns -1 if no match, otherwise the next position
		public int match(String text, int start)
		{
			int ix = text.indexOf(pattern, start); 
			if(ix < 0)
			{
				return -1;
			}
			else
			{
				return ix + pattern.length();
			}
		}
		
		
		public void dump(StringBuilder sb)
		{
			sb.append("<W:").append(pattern).append(">");
		}
	}
	
	
	//

	
	// matches any character - "?"
	protected static class AnyCharMatcher extends SegmentMatcher
	{
		public int match(String text, int start)
		{
			int ix = start + 1;
			if(ix <= text.length())
			{
				return ix;
			}
			else
			{
				return -1;
			}
		}
		
		
		public void dump(StringBuilder sb)
		{
			sb.append("<A>");
		}
	}
	
	
	//
	

	// matches end of text 
	protected static class EndMatcher extends SegmentMatcher
	{
		public int match(String text, int start)
		{
			return (start == text.length() ? start : -1);
		}
		
		
		public void dump(StringBuilder sb)
		{
			sb.append("<E>");
		}
	}
}
