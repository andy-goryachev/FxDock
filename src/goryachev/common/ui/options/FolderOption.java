// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.io.File;
import java.util.Collection;


// just like FileOption, but deals with folders (directories)
public class FolderOption
	extends FileOption
{
	public FolderOption(String propertyName, CSettings p, Collection<COption<?>> list)
	{
		super(propertyName, p, list);
	}
	
	
	public FolderOption(String propertyName, File defaultValue)
	{
		super(propertyName, defaultValue);
	}
	
	
	public FolderOption(String propertyName)
	{
		super(propertyName);
	}
	
	
	public void set(File f)
	{
		if(f != null)
		{
			if(f.isFile())
			{
				f = f.getParentFile();
			}
		}
		
		super.set(f);
	}
}
