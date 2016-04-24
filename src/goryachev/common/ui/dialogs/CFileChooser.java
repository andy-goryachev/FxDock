// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs;
import goryachev.common.ui.CExtensionFileFilter;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.UI;
import goryachev.common.util.CKit;
import goryachev.common.util.Parsers;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class CFileChooser
	extends JFileChooser
{
	public static final String KEY_SUFFIX_FILENAME = ".fname";
	private Component parent;
	private String title;
	private String property;
	private boolean rememberFileName;
	

	public CFileChooser(Component parent, String property)
	{
		this.parent = parent;
		this.property = (property == null ? null : "CFileChooser." + property);
		
		setFileSelectionMode(FILES_ONLY);
		setMultiSelectionEnabled(false);
		setDialogType(CUSTOM_DIALOG);
	}
	
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	
	public void setRememberFileName(boolean on)
	{
		rememberFileName = on;
	}
	
	
	protected JDialog createDialog(Component parent) throws HeadlessException
	{
		Window w = UI.getParentWindow(parent);
		JDialog d = super.createDialog(w);
		if(title != null)
		{
			d.setTitle(title);
		}
		return d;
	}
	
	
	protected int openDialog()
	{
		JDialog d = createDialog(parent);
		d.setName("CFileChooser");
		
		GlobalSettings.restorePreferences(d);

		if(property != null)
		{
			File sel = getSelectedFile();
			if((sel == null) || (sel.getParentFile() == null))
			{
				setCurrentDirectory(parseFolder(GlobalSettings.getString(property)));

				if(rememberFileName)
				{
					String s = GlobalSettings.getString(property + KEY_SUFFIX_FILENAME);
					if(CKit.isNotBlank(s))
					{
						setFilename(s);
					}
				}
			}
		}
		
		int rv = showDialog(parent, null);
		
		GlobalSettings.storePreferences(d);
		
		// remember directory
		if(property != null)
		{
			File f = getSelectedFile();
			if(f != null)
			{
				// TODO different if folder chooser
				File dir;
				if(f.isDirectory())
				{
					dir = f;
				}
				else
				{
					dir = f.getParentFile();
				}
				
				if(dir != null)
				{
					GlobalSettings.set(property, dir.getPath());
				}
				
				if(rememberFileName)
				{
					GlobalSettings.set(property + KEY_SUFFIX_FILENAME, f.getName());
				}
			}
		}

		return rv;
	}


	protected File parseFolder(String name)
	{
		if(name != null)
		{
			File f = new File(name);
			do
			{
				if(f.exists() && f.isDirectory())
				{
					return f;
				}

				f = f.getParentFile();

			} while(f != null);
		}
		
		return Parsers.parseCanonicalFile(".");
	}


	public File openFileChooser()
	{
		int rv = openDialog();
		if(rv == APPROVE_OPTION)
		{
			return getSelectedFile();
		}
		else
		{
			return null;
		}
	}


	public File openFolderChooser()
	{
		setFileSelectionMode(DIRECTORIES_ONLY);
		int rv = openDialog();
		if(rv == APPROVE_OPTION)
		{
			return getSelectedFile();
		}
		else
		{
			return null;
		}
	}
	
	
	public void setFilename(String s)
	{
		if(s != null)
		{
			File f = new File(s);
//			if(!f.isAbsolute())
//			{
//				f = new File(getCurrentDirectory(), s);
//			}
			setSelectedFile(f);
		}
	}
	
	
	protected void removeFileFilters()
	{
		for(FileFilter f: getChoosableFileFilters())
		{
			removeChoosableFileFilter(f);
		}
	}
	

	public void replaceFileFilters(FileFilter ... fs)
	{
		replaceFileFilters(Arrays.asList(fs));
	}
	
	
	public void replaceFileFilters(List<FileFilter> fs)
	{
		removeFileFilters();
		
		for(FileFilter f: fs)
		{
			addChoosableFileFilter(f);
		}

		if(isAcceptAllFileFilterUsed())
		{
			addChoosableFileFilter(getAcceptAllFileFilter());
		}
	}


	/** set image filters supported by standard ImageIO configuration */
	public void setImageFileFilters()
	{
		replaceFileFilters(CExtensionFileFilter.getImageIOFilters());
	}
}
