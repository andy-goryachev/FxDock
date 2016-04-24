// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.TXT;
import java.io.File;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


public class FilePicker
{
	public final CAction browseAction = new CAction(Menus.Browse) { public void action() { openDialog(); }};
	public final JTextField fileField;
	private String title;
	private String button;
	private int mode;
	private boolean multiSelection;
	private CList<FileFilter> filters;
	private boolean acceptAllFileFilter;
	private String extension;
	private String property;
	private Runnable onChange;
	
	
	public FilePicker()
	{
		fileField = new JTextField();
	}
	
	
	public void setOnChange(Runnable r)
	{
		onChange = r;
	}
	
	
	public void setProperty(String s)
	{
		property = s;
	}
	
	
	public void setDialogTitle(String s)
	{
		title = s;
	}
	
	
	public void setButtonText(String s)
	{
		button = s;
	}
	
	
	public void setMultiSelection(boolean on)
	{
		multiSelection = on;
	}
	
	
	public void setFileMode()
	{
		mode = CFileChooser.FILES_ONLY;
	}
	
	
	public void setFolderMode()
	{
		mode = CFileChooser.DIRECTORIES_ONLY;
	}
	
	
	public void setFileAndFolderMode()
	{
		mode = CFileChooser.FILES_AND_DIRECTORIES;
	}
	
	
	public void addFileFilters(FileFilter ... fs)
	{
		for(FileFilter f: fs)
		{
			addFileFilter(f);
		}
	}
	
	
	public void addFileFilter(FileFilter f)
	{
		if(filters == null)
		{
			filters = new CList();
		}
		
		filters.add(f);
	}
	
	
	public void addFileFilter(String name, String ... ext)
	{
		addFileFilter(new CExtensionFileFilter(name, ext));
	}
	
	
	public void setAcceptAllFileFilterUsed(boolean on)
	{
		acceptAllFileFilter = on;
	}
	
	
	public void setEnsureExtension(String ext)
	{
		extension = ext;
	}
	
	
	public void setFile(File f)
	{
		fileField.setText(f == null ? null : f.getAbsolutePath());
	}
	
	
	public File getFile()
	{
		String s = fileField.getText();
		if(CKit.isNotBlank(s))
		{
			return new File(s);
		}
		return null;
	}
	
	
	public File[] getFiles()
	{
		return null;
	}
	
	
	public File openDialog()
	{
		CFileChooser d = new CFileChooser(fileField, property);
		
		if(title == null)
		{
			switch(mode)
			{
			case CFileChooser.FILES_ONLY:
				title = TXT.get("FilePicker.dialog title.select file", "Select File");
				break;
			case CFileChooser.DIRECTORIES_ONLY:
				title = TXT.get("FilePicker.dialog title.select folder", "Select Folder");
				break;
			default:
				title = TXT.get("FilePicker.dialog title.select files", "Select Files");
				break;
			}
		}
		d.setTitle(title);
		
		if(button == null)
		{
			button = Menus.Select;
		}
		d.setApproveButtonText(button);
		
		d.setMultiSelectionEnabled(multiSelection);
		
		d.setFileSelectionMode(mode);
		if(mode == CFileChooser.FILES_ONLY)
		{
			d.setRememberFileName(true);
		}
		
		d.setAcceptAllFileFilterUsed(acceptAllFileFilter);
		if(filters != null)
		{
			d.replaceFileFilters(filters);
			
			if(filters.size() > 0)
			{
				FileFilter f = filters.get(0);
				d.setFileFilter(f);
			}
		}
		
		d.setSelectedFile(getFile());
		
		// open the dialog
		// FIX does not allow for multiple file selection!
		File f = d.openFileChooser();
		
		if(f != null)
		{
			if(extension != null)
			{
				f = CKit.ensureExtension(f, extension);
			}
			
			setFile(f);
			
			if(onChange != null)
			{
				onChange.run();
			}
		}
		
		return f;
	}
}
