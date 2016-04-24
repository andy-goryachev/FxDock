// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.TXT;
import java.io.File;
import javax.swing.filechooser.FileFilter;


public class CAcceptAllFileFilter
	extends FileFilter
{
	public boolean accept(File f)
	{
		return true;
	}


	public String getDescription()
	{
		return TXT.get("CAcceptAllFileFilter.accept all files", "All Files");
	}
}
