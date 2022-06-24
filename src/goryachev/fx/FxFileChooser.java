// Copyright © 2020-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Window;


/**
 * A more convenient FileChooser.
 * 
 * TODO this needs to be reimplemented:
 * - does not show all .txt files even with an extension filter!
 * - does not highlight files matching the extension, when the user wants to see all the files in the directory
 * - does not store changes to dialog size, location, view settings etc. 
 */
public class FxFileChooser
{
	private final Window parent;
	private final FileChooser fc;
	
	
	public FxFileChooser(Object nodeOrWindow, Object lastDirProperty)
	{
		this.parent = FX.getParentWindow(nodeOrWindow);
		this.fc = new FileChooser();
	}
	
	
	public void setTitle(String title)
	{
		fc.setTitle(title);
	}
	
	
	public void addExtensionFilter(String desc, String ... extensions)
	{
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(desc, CList.of(extensions)));
	}
	
	
	public void addExtensionFilter(FileChooser.ExtensionFilter f)
	{
		fc.getExtensionFilters().add(f);
	}


	public void setInitialFileName(String name)
	{
		fc.setInitialFileName(name);
	}
	
	
	public void setInitialDirectory(File dir)
	{
		fc.setInitialDirectory(dir);
	}


	public File showSaveDialog()
	{
		return fc.showSaveDialog(parent);
	}
	
	
	public File showOpenDialog()
	{
		return fc.showOpenDialog(parent);
	}
	
	
	public List<File> showOpenMultipleDialog()
	{
		return fc.showOpenMultipleDialog(parent);
	}
	
	
	public FileChooser.ExtensionFilter getSelectedExtensionFilter()
	{
		return fc.getSelectedExtensionFilter();
	}
	
	
	/** returns the first extension from the selected filter, or null */
	public String getSelectedExtension()
	{
		FileChooser.ExtensionFilter f = getSelectedExtensionFilter();
		if(f != null)
		{
			List<String> ss = f.getExtensions();
			if(ss != null)
			{
				if(ss.size() > 0)
				{
					return ss.get(0);
				}
			}
		}
		return null;
	}
}
