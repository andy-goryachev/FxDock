// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.TXT;
import java.io.File;
import javax.swing.filechooser.FileFilter;


public class CExtensionFileFilter
	extends FileFilter
	implements java.io.FileFilter
{
	private CList<String> extensions = new CList();
	private String description;
	
	
	public CExtensionFileFilter(String description, String ... exts)
	{
		this(description);
		
		for(String ext: exts)
		{
			addExtension(ext);
		}
	}
	
	
	public CExtensionFileFilter(String description, String ext)
	{
		this(description);
		addExtension(ext);
	}
	
	
	public CExtensionFileFilter(String description)
	{
		this.description = description;
	}
	
	
	public void addExtension(String ext)
	{
		extensions.add(ext);
	}


	public boolean accept(File f)
	{
		if(f.isDirectory())
		{
			return true;
		}
		else if(matchExtension(f.getName()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public String getDescription()
	{
		return description;
	}
	
	
	protected boolean matchExtension(String name)
	{
		for(String x: extensions)
		{
			if(CKit.endsWithIgnoreCase(name, x))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public String getFirstExtension()
	{
		if(extensions.size() > 0)
		{
			return extensions.get(0);
		}
		return "";
	}
	
	
	public String[] getExtensions()
	{
		return extensions.toArray(new String[extensions.size()]);
	}
	
	
	public String toString()
	{
		return "CExtensionFileFilter(" + getDescription() + ")";
	}


	public static CExtensionFileFilter getImageIOFilters()
	{
		CExtensionFileFilter f = new CExtensionFileFilter(TXT.get("CExtensionFileFilter.image files", "Image Files"));
		f.addExtension(".jpg");
		f.addExtension(".jpeg");
		f.addExtension(".png");
		f.addExtension(".gif");
		return f;
	}
}


