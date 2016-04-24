// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.ui.CAction;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


// file list maintains a List of files
public class FileListOption
	extends COption<List<File>>
{
	public static final String DIVIDER = "\n";
	private int max;


	public FileListOption(String key, int max)
	{
		super(key);
		this.max = max;
	}


	public List<File> defaultValue()
	{
		return Collections.emptyList();
	}


	public List<File> parseProperty(String s)
	{
		ArrayList<File> list = new ArrayList(max);
		if(s != null)
		{
			for(String name: s.split(DIVIDER))
			{
				add(new File(name));
			}
		}
		return list;
	}


	public String toProperty(List<File> value)
	{
		SB sb = new SB();
		for(File f: value)
		{
			if(sb.length() > 0)
			{
				sb.append(DIVIDER);
			}

			try
			{
				sb.append(f.getCanonicalPath());
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		return sb.toString();
	}


	public void clear()
	{
		set(new ArrayList<File>());
	}


	public void add(File f)
	{
		if(f != null)
		{
			try
			{
				f = f.getCanonicalFile();

				ArrayList<File> a = new ArrayList();
				a.add(f);

				for(File old: get())
				{
					if(a.size() >= max)
					{
						break;
					}

					if(!CKit.equals(f, old))
					{
						a.add(old);
					}
				}

				set(a);
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
	}


	public int getMaxCount()
	{
		return max;
	}


	// 


	// serves as a marker 
	// see JPopupMenu.Separator
	protected static class MenuSeparator
		extends JSeparator
	{
		public MenuSeparator()
		{
			super(HORIZONTAL);
		}


		public String getUIClassID()
		{
			return "PopupMenuSeparatorUI";
		}
	}
	
	
	//


	public abstract class MenuItem
		extends JMenuItem
		implements MenuListener
	{
		public abstract void menuFileSelected(File f);

		//
		
		private JMenu menu;


		public MenuItem(JMenu menu)
		{
			this.menu = menu;
			menu.add(new MenuSeparator());
			menu.addMenuListener(this);
		}


		protected MenuItem(Action a)
		{
			super(a);
		}


		public void menuSelected(MenuEvent e)
		{
			// dynamically modify menu

			java.awt.Component[] cs = menu.getMenuComponents();
			menu.removeAll();

			boolean separatorFound = false;
			for(java.awt.Component c: cs)
			{
				if(c instanceof MenuSeparator)
				{
					if(!separatorFound)
					{
						menu.add(c);
						// add new list
						java.util.List<File> recentFiles = get();
						if(recentFiles.size() > 0)
						{
							for(final File f: recentFiles)
							{
								// could use a different object here
								menu.add(new MenuItem(new CAction(f.getAbsolutePath())
								{
									public void action()
									{
										menuItemSelected(f);
									}
								})
								{
									public void menuFileSelected(File f)
									{
									}
								});
							}

							menu.add(new MenuSeparator());
						}

						separatorFound = true;
					}
				}
				else if(c instanceof MenuItem)
				{
					// skip
				}
				else
				{
					menu.add(c);
				}
			}
		}


		protected void menuItemSelected(File f)
		{
			menuFileSelected(f);
		}


		public void menuDeselected(MenuEvent e)
		{
		}


		public void menuCanceled(MenuEvent e)
		{
		}
	}
}
