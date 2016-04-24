// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.io.File;
import java.util.Collection;


public class FileOption
	extends COption<File>
{
	private File defaultValue;


	public FileOption(String propertyName)
	{
		super(propertyName);
	}


	public FileOption(String propertyName, File defaultValue)
	{
		this(propertyName);
		this.defaultValue = defaultValue;
	}


	public FileOption(String propertyName, CSettings s, Collection<COption<?>> list)
	{
		super(propertyName, s, list);
	}


	public File defaultValue()
	{
		return defaultValue;
	}


	public String toProperty(File f)
	{
		if(f == null)
		{
			return null;
		}
		
		try
		{
			return f.getCanonicalPath();
		}
		catch(Exception e)
		{
			return f.getAbsolutePath();
		}
	}


	public File parseProperty(String s)
	{
		return new File(s);
	}
	
	
	public String getFileName()
	{
		File f = get();
		return (f == null ? null : f.getAbsolutePath());
	}
}
