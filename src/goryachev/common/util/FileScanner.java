// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.io.FileFilter;


public abstract class FileScanner
{
	protected abstract void processFile(File f) throws Exception;
	
	//
	
	protected static class ScanEntry
	{
		public File folder;
		public RFileFilter filter;
	}
	
	//
	
	private CList<ScanEntry> folders = new CList();
	private boolean sort;
	private RFileFilter defaultFilter;
	
	
	public FileScanner()
	{
	}
	
	
	public void setSortingEnabled(boolean on)
	{
		sort = on;
	}
	
	
	public void setFileFilter(RFileFilter f)
	{
		defaultFilter = f;
	}
	
	
	public void setFileFilterSpec(String spec) throws Exception
	{
		defaultFilter = RFileFilter.parse(spec);
	}
	
	
	public void addFolder(File folder)
	{
		addFolder(folder, null);
	}
	
	
	public void addFolder(File folder, RFileFilter filter)
	{
		ScanEntry en = new ScanEntry();
		en.folder = folder;
		en.filter = filter;
		folders.add(en);
	}
	
	
	public void addFolder(String filename)
	{
		File f = new File(filename);
		addFolder(f);
	}
	
	
	public void scan() throws Exception
	{
		for(ScanEntry en: folders)
		{
			RFileFilter f = (en.filter == null ? defaultFilter : en.filter);
			if(f == null)
			{
				f = new RFileFilter();
			}
			
			FileFilter ff = f.getFilter(en.folder);
			
			scanFile(en.folder, ff);
		}
	}
	
	
	protected void scanFile(File file, FileFilter filter) throws Exception
	{
		CKit.checkCancelled();
		
		if(file.isFile())
		{
			processFile(file);
		}
		else if(file.isDirectory())
		{
			File[] fs = (filter == null ? file.listFiles() : file.listFiles(filter));
			if(fs != null)
			{
				if(sort)
				{
					new CComparator<File>()
					{
						public int compare(File a, File b)
						{
							CKit.checkCancelled();
							return compareText(a.getName(), b.getName());
						}
					}.sort(fs);
				}
				
				for(File f: fs)
				{
					scanFile(f, filter);
				}
			}
		}
	}
}
