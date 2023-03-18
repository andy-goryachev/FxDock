// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.io.File;
import java.io.FileFilter;


public class RFileFilter
{
	protected static final Log log = Log.get("RFileFilter");
	public static final String HIDDEN = "Hidden Files";
	public static final String SYSTEM = "System Files";
	
	public static final String TOKEN_EXCLUDE = "- ";
	public static final String TOKEN_INCLUDE = "+ ";
	public static final String TOKEN_IGNORE_HIDDEN_FILES = "-hidden";
	public static final String TOKEN_IGNORE_SYSTEM_FILES = "-system";

	private boolean ignoreHidden;
	private boolean ignoreSystem;
	private CList<RFilterPattern> excludePatterns;
	private CList<RFilterPattern> includePatterns;
	private static CList<RFilterPattern> systemPatterns = createSystemPatterns();
	private volatile Object triggeredRule;


	public RFileFilter()
	{
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof RFileFilter)
		{
			RFileFilter o = (RFileFilter)x;
			return 
				(ignoreHidden == o.ignoreHidden) &&
				(ignoreSystem == o.ignoreSystem) &&
				CKit.equals(excludePatterns, o.excludePatterns) &&
				CKit.equals(includePatterns, o.includePatterns);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(RFileFilter.class);
		h = FH.hash(h, excludePatterns);
		h = FH.hash(h, includePatterns);
		h = FH.hash(h, ignoreHidden);
		return FH.hash(h, ignoreSystem); 
	}
	
	
	public Object getTriggeredRule()
	{
		return triggeredRule;
	}

	
	public static CList<RFilterPattern> createSystemPatterns()
	{
		String[] ss =
		{
			// windows
			"Thumbs.db",
	
			// mac
			".DS_Store",
			".Spotlight-V100/",
			".TemporaryItems/",
	
			// typical temporary files
			"*~", 
			"~*",
			"#*#", 
			".#*", 
			"%*%", 
	
			// bazaar
			".bzr/", 
			".bzrignore",
	
			// cvs
			"CVS/", 
			".cvsignore",
	
			// git
			".git/", 
			".gitattributes", 
			".gitignore", 
			".gitmodules",
	
			// mercurial
			".hg/", 
			".hgignore", 
			".hgsub", 
			".hgsubstate", 
			".hgtags",
	
			// sccs
			"SCCS/", 
	
			// subversion
			".svn/", 
			"_svn/",
	
			// visual sourceSafe
			"vssver.scc",
		};
		
		CList<RFilterPattern> ps = new CList(ss.length);
		for(String s: ss)
		{
			try
			{
				ps.add(RFilterPattern.parse(s));
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		
		// TODO add platform-specific system patterns?
		
		return ps;
	}
	
	
	public static RFileFilter parse(String spec) throws Exception
	{
		if(CKit.isNotBlank(spec))
		{
			Object[] ss = CKit.split(spec, "\n");
			return parse(ss);
		}
		else
		{
			return new RFileFilter();
		}
	}
	

	/**
	 * filter Specification Format:
	 *    + or -
	 *    single space
	 *    / for paths relative to root
	 *    name, path, wildcard
	 *    trailing / for directories
	 *    newline
	 *    
	 * additionally, two tokens are used:
	 *    -hidden
	 *    -system
	 */
	public static RFileFilter parse(Object[] ss) throws Exception
	{
		RFileFilter f = new RFileFilter();
		
		// TODO replace \\ with /
		
		for(Object x: ss)
		{
			String s = x.toString().trim();
			if(CKit.isNotBlank(s))
			{
				if(s.startsWith(TOKEN_IGNORE_HIDDEN_FILES))
				{
					f.setIgnoreHiddenFiles(true);
				}
				else if(s.startsWith(TOKEN_IGNORE_SYSTEM_FILES))
				{
					f.setIgnoreSystemFiles(true);
				}
				else if(s.startsWith(TOKEN_EXCLUDE))
				{
					RFilterPattern p = RFilterPattern.parse(s.substring(TOKEN_EXCLUDE.length()));
					if(p != null)
					{
						if(f.excludePatterns == null)
						{
							f.excludePatterns = new CList();
						}
						f.excludePatterns.add(p);
					}
				}
				else if(s.startsWith(TOKEN_INCLUDE))
				{
					RFilterPattern p = RFilterPattern.parse(s.substring(TOKEN_INCLUDE.length()));
					if(p != null)
					{
						if(f.includePatterns == null)
						{
							f.includePatterns = new CList();
						}
						f.includePatterns.add(p);
					}
				}
				else
				{
					throw new UserException("invalid filter spec: " + s);
				}
			}
		}

		return f;
	}
	
	
	public static RFileFilter parseQuiet(String spec)
	{
		try
		{
			if(CKit.isNotBlank(spec))
			{
				return parse(spec);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		RFileFilter f = new RFileFilter();
		f.setIgnoreHiddenFiles(true);
		f.setIgnoreSystemFiles(true);
		return f; 
	}


	public String getSpec()
	{
		SB sb = new SB();
		
		if(ignoreHidden)
		{
			sb.a(TOKEN_IGNORE_HIDDEN_FILES);
			sb.nl();
		}
		
		if(ignoreSystem)
		{
			sb.a(TOKEN_IGNORE_SYSTEM_FILES);
			sb.nl();
		}
		
		if(excludePatterns != null)
		{
			toSpec(sb, TOKEN_EXCLUDE, excludePatterns);
		}
		
		if(includePatterns != null)
		{
			toSpec(sb, TOKEN_INCLUDE, includePatterns);
		}
		
		return sb.toString();
	}
	

	public void setExcludeSpec(String s) throws Exception
	{
		excludePatterns = parsePatterns(s);
	}
	
	
	public String getExcludeSpec()
	{
		return toSpec(null, null, excludePatterns);
	}
	

	public void setIncludeSpec(String s) throws Exception
	{
		includePatterns = parsePatterns(s);
	}
	
	
	public String getIncludeSpec()
	{
		return toSpec(null, null, includePatterns);
	}

	
	protected String toSpec(SB sb, String prefix, CList<RFilterPattern> patterns)
	{
		if(patterns != null)
		{
			for(RFilterPattern p: patterns)
			{
				if(sb == null)
				{
					sb = new SB();
				}
				
				if(prefix != null)
				{
					sb.a(prefix);
				}
				
				sb.a(p.getSpec());
				sb.nl();
			}
		}
		return sb == null ? null : sb.toString();
	}


	protected CList<RFilterPattern> parsePatterns(String spec) throws Exception
	{
		CList<RFilterPattern> list = new CList();
		String[] ss = CKit.split(spec, "\n");
		for(String s: ss)
		{
			RFilterPattern p = RFilterPattern.parse(s);
			if(p != null)
			{
				list.add(p);
			}
		}
		return list;
	}
	

	public void setIgnoreHiddenFiles(boolean on)
	{
		ignoreHidden = on;
	}
	
	
	public boolean isIgnoreHiddenFiles()
	{
		return ignoreHidden;
	}


	public void setIgnoreSystemFiles(boolean on)
	{
		ignoreSystem = on;
	}
	
	
	public boolean isIgnoreSystemFiles()
	{
		return ignoreSystem;
	}


	protected boolean isMatchFound(CList<RFilterPattern> patterns, String pathToRoot, String filename, boolean dir)
	{
		if(patterns != null)
		{
			for(RFilterPattern p: patterns)
			{
				if(p.isMatch(pathToRoot, filename, dir))
				{
					triggeredRule = p;
					return true;
				}
			}
		}
		return false;
	}


	protected boolean isExcluded(String pathToRoot, String filename, boolean dir)
	{
		return isMatchFound(excludePatterns, pathToRoot, filename, dir);
	}
	

	protected boolean isIncluded(String pathToRoot, String filename, boolean dir)
	{
		return isMatchFound(includePatterns, pathToRoot, filename, dir);
	}
	
	
	public boolean accept(String pathToRoot, String filename, boolean dir, boolean hidden)
	{
		triggeredRule = null;
		
		// inclusion overrides everything
		if(isIncluded(pathToRoot, filename, dir))
		{
			return true;
		}
		
		// hidden
		if(ignoreHidden)
		{
			if(hidden)
			{
				triggeredRule = HIDDEN;
				return false;
			}
		}
		
		// system
		if(ignoreSystem)
		{
			for(RFilterPattern p: systemPatterns)
			{
				if(p.isMatch(pathToRoot, filename, dir))
				{
					triggeredRule = SYSTEM;
					return false;
				}
			}
		}
		
		// exclusion
		if(isExcluded(pathToRoot, filename, dir))
		{
			return false;
		}
		
		// allow
		triggeredRule = null;
		return true;
	}


	public FileFilter getFilter(final File root)
	{
		return new FileFilter()
		{
			public boolean accept(File f)
			{
				String pathToRoot = CKit.pathToRoot(root, f);
				String name = f.getName();
				boolean dir = f.isDirectory();
				boolean hidden = f.isHidden();

				return RFileFilter.this.accept(pathToRoot, name, dir, hidden);
			}
		};
	}
}
