// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


// patterns are case insensitive.
public class RFilterPattern
{
	private final String spec;
	private final boolean wildcard;
	private final boolean relative;
	private final boolean directory;
	
	
	protected RFilterPattern(String spec, boolean wildcard, boolean relative, boolean directory)
	{
		this.spec = spec;
		this.wildcard = wildcard;
		this.relative = relative;
		this.directory = directory;
	}

	
	public static RFilterPattern parse(String spec) throws Exception
	{	
		if(CKit.isBlank(spec))
		{
			return null;
		}
		
		String s = spec;
		boolean relative = false;
		boolean dir = false;
		
		if(s.startsWith("/"))
		{
			relative = true;
			s = s.substring(1);
		}
		
		if(s.endsWith("/"))
		{
//			s = s.substring(0, s.length() - 1);
//			if(s.length() == 0)
//			{
//				throw new Exception("Invalid pattern: " + spec);
//			}
			dir = true;
		}
		
		boolean wildcard = CKit.containsAny(s, "?*");
			
		return new RFilterPattern(s, wildcard, relative, dir);
	}
	
	
	public String getSpec()
	{
		return spec;
	}
	
	
	public String toString()
	{
		return getSpec();
	}
	
	
	protected boolean matchName(String filename)
	{
		// TODO case sensitive?
		if(wildcard)
		{
			return FileTools.wildcardMatch(filename, spec, false);
		}
		else
		{
			return filename.equalsIgnoreCase(spec);
		}
	}
	
	
	protected boolean matchPath(String pathToRoot, String filename)
	{
		String fn = pathToRoot + "/" + filename;

		if(wildcard)
		{
			// TODO case-sensitive
			return FileTools.wildcardMatch(fn, spec, false);
		}
		else
		{
			// TODO case sensitive?
			if(TextTools.startsWithIgnoreCase(fn, spec))
			{
				// make sure we picked up the whole filename part
				if(spec.endsWith("/"))
				{
					return true;
				}
				else if(spec.endsWith("/" + filename))
				{
					return true;
				}
			}
		}
		return false;
	}

	
	public boolean isMatch(String pathToRoot, String filename, boolean dir)
	{
		if(directory != dir)
		{
			return false;
		}
		
		if(dir)
		{
			filename = filename + "/";
		}
		
		if(relative)
		{
			return matchPath(pathToRoot, filename);
		}
		else
		{
			return matchName(filename);
		}
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof RFilterPattern)
		{
			return spec.equals(((RFilterPattern)x).spec);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return RFilterPattern.class.hashCode() ^ spec.hashCode();
	}
}