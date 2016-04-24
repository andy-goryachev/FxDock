// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.CMenuItem;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import goryachev.common.util.html.HtmlTools;
import java.io.File;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public abstract class RecentFilesOption
	extends COption<CList<File>>
{
	protected abstract void onRecentFileSelected(File f);
	
	//
	
	protected int maxRecentFiles = 10;
	private static final char DIVIDER = '\0';
	public final CAction clearRecentAction = new CAction() { public void action() { clear(); } };
	
	
	public RecentFilesOption(String id)
	{
		super(id);
	}
	
	
	public void setMaxRecentFiles(int n)
	{
		this.maxRecentFiles = n;
	}
	
	
	public void clear()
	{
		set(new CList());
	}


	public CList<File> parseProperty(String s)
	{
		CList<File> a = new CList();
		try
		{
			s = CKit.decompressString(s);

			if(CKit.isNotBlank(s))
			{
				for(String name: CKit.split(s, DIVIDER))
				{
					a.add(new File(name));
				}
			}
		}
		catch(Exception e)
		{ }
		return a;
	}


	public String toProperty(CList<File> fs)
	{
		try
		{
			SB sb = new SB();
			for(File f: fs)
			{
				if(sb.getLength() > 0)
				{
					sb.a(DIVIDER);
				}
				sb.a(f.getAbsolutePath());
			}
		
			return CKit.compressString(sb.toString());
		}
		catch(Exception e)
		{
			return null;
		}
	}


	public CList<File> defaultValue()
	{
		return new CList();
	}


	public void add(File f)
	{
		if(f != null)
		{
			CList<File> fs = get();
			if(fs == null)
			{
				fs = new CList();
			}
			else
			{
				fs = new CList(fs);
			}
			
			int sz = fs.size();
			for(int i=sz-1; i>=0; --i)
			{
				if(fs.get(i).equals(f))
				{
					fs.remove(i);
				}
			}
			
			fs.add(0, f);
			while(fs.size() > maxRecentFiles)
			{
				fs.remove(fs.size() - 1);
			}
			
			set(fs);
		}
	}
	
	
	public File getRecentFile()
	{
		CList<File> fs = get();
		if(fs != null)
		{
			if(fs.size() > 0)
			{
				return fs.get(0);
			}
		}
		return null;
	}
	
	
	public CList<File> getRecentFiles()
	{
		return new CList(get());
	}
	
	
	public int getRecentFileCount()
	{
		CList<File> fs = get();
		return fs == null ? 0 : fs.size();
	}


	public CMenu recentFilesMenu()
	{
		return new RecentFilesMenu();
	}
	
	
	//
	
	
	public class RecentFilesMenu 
		extends CMenu
		implements MenuListener
	{
		public RecentFilesMenu()
		{
			super("Open Recent");
			addMenuListener(this);
		}


		public String getText()
		{
			boolean old = super.isEnabled();
			boolean on = getRecentFileCount() > 0;
			if(old != on)
			{
				super.setEnabled(on);
			}

			return super.getText();
		}


		protected void rebuild()
		{
			removeAll();

			CList<File> fs = get();
			if(fs != null)
			{
				for(final File f: fs)
				{
					String name = f.getName();
					String path = f.getAbsolutePath();
	
					if(path.endsWith(name))
					{
						path = path.substring(0, path.length() - name.length());
						path = "<html>" + HtmlTools.safe(path) + "<b>" + HtmlTools.safe(name) + "</b>";
					}
	
					add(new CMenuItem(path, new CAction()
					{
						public void action()
						{
							onRecentFileSelected(f);
						}
					}));
				}
			}

			if(getRecentFileCount() > 0)
			{
				addSeparator();
			}
			add(new CMenuItem(TXT.get("RecentFilesOption.menu.clear", "Clear Recent Files"), clearRecentAction));
		}


		public void menuSelected(MenuEvent ev)
		{
			rebuild();
		}


		public void menuDeselected(MenuEvent ev) { }
		public void menuCanceled(MenuEvent ev) { }
	}
}
