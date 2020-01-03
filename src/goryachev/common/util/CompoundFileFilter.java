// Copyright © 2009-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.CommaDelimitedParser;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;


public class CompoundFileFilter
	implements FileFilter
{
	private FileFilter includeFolderFilter;
	private FileFilter includeFileFilter;
	private FileFilter excludeFolderFilter;
	private FileFilter excludeFileFilter;
	private boolean skipSystem;
	
	
	public CompoundFileFilter(String includeFolders, String includeFiles, String excludeFolders, String excludeFiles, boolean skipSystem)
	{
		this.includeFolderFilter = parseCommaDelimited(includeFolders, true);
		this.includeFileFilter   = parseCommaDelimited(includeFiles,   true);
		this.excludeFolderFilter = parseCommaDelimited(excludeFolders, false);
		this.excludeFileFilter   = parseCommaDelimited(excludeFiles,   false);
		this.skipSystem = skipSystem;
	}
	

	public boolean accept(File f)
	{
		if(f != null)
		{
			if(skipSystem)
			{
				if(FileTools.isHiddenOrSystem(f))
				{
					return false;
				}
			}
			
			if(f.isDirectory())
			{
				if(includeFolderFilter.accept(f))
				{
					return !excludeFolderFilter.accept(f);
				}
			}
			else
			{
				if(includeFileFilter.accept(f))
				{
					return !excludeFileFilter.accept(f);
				}
			}
		}		
		return false;
	}
	
	
	// comma delimited list of wildcarded files: "a a","*.b",c,d,"single,file"
	// accepts:
	// quoted files "with spaces, commas"
	// wildcards: *.txt, *.?xt
	public static FileFilter parseCommaDelimited(String s, boolean acceptIfBlank)
	{
		if(!CKit.isBlank(s))
		{
			CommaDelimitedParser p = new CommaDelimitedParser(s);
			String[] ss = p.parse();
			if(ss != null)
			{
				final ArrayList<FileFilter> filters = new ArrayList(ss.length);
				for(String spec: ss)
				{
					FileFilter f = BasicFileFilter.parse(spec);
					if(f != null)
					{
						filters.add(f);
					}
				}
				
				if(filters.size() == 1)
				{
					return filters.get(0);
				}
				else
				{
					// compound filter
					return new FileFilter()
					{
						public boolean accept(File f)
						{
							for(int i=0; i<filters.size(); i++)
							{
								if(filters.get(i).accept(f))
								{
									return true;
								}
							}
							return false;
						}
					};
				}
			}
		}
		
		return acceptIfBlank ? BasicFileFilter.ACCEPT : BasicFileFilter.DENY;
	}
}
