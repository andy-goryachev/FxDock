// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.FX;
import goryachev.fx.FxWindow;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;


/**
 * Keys and functions used to store ui settings.
 */
public class FxSchema
{
	public static final String FX_PREFIX = "FX.";
	
	public static final String SFX_BINDINGS = ".BINDINGS";
	public static final String SFX_COLUMNS = ".COLS";
	public static final String SFX_DIVIDERS = ".DIVS";
	public static final String SFX_SELECTION = ".SEL";
	
	public static final String SORT_ASCENDING = "A";
	public static final String SORT_DESCENDING = "D";
	public static final String SORT_NONE = "N";
	
	public static final String WINDOW_FULLSCREEN = "F";
	public static final String WINDOW_MAXIMIZED = "X";
	public static final String WINDOW_ICONIFIED = "I";
	public static final String WINDOW_NORMAL = "N";
	
	private static final Object PROP_LOAD_HANDLER = new Object();
	private static final Object PROP_NAME = new Object();
	
	
	public static void storeWindow(String prefix, FxWindow win)
	{
		double x = win.getNormalX();
		double y = win.getNormalY();
		double w = win.getNormalWidth();
		double h = win.getNormalHeight();
		
		SStream s = new SStream();
		s.add(x);
		s.add(y);
		s.add(w);
		s.add(h);
		
		if(win.isFullScreen())
		{
			s.add(WINDOW_FULLSCREEN);
		}
		else if(win.isMaximized())
		{
			s.add(WINDOW_MAXIMIZED);
		}
		else if(win.isIconified())
		{
			s.add(WINDOW_ICONIFIED);
		}
		else
		{
			s.add(WINDOW_NORMAL);
		}

		GlobalSettings.setStream(FX_PREFIX + prefix, s);
	}
	
	
	public static void restoreWindow(String prefix, FxWindow win)
	{
		try
		{
			String id = FX_PREFIX + prefix;
			SStream s = GlobalSettings.getStream(id);
			double x = s.nextDouble(-1);
			double y = s.nextDouble(-1);
			double w = s.nextDouble(-1);
			double h = s.nextDouble(-1);
			String state = s.nextString(WINDOW_NORMAL);
			
			if((w > 0) && (h > 0))
			{
				// unnecessary anymore
				if(FX.isValidCoordinates(x, y))
				{
					// iconified windows have (x,y) of -32000 for some reason
					// their coordinates are essentially lost (unless there is a way to get them in FX)
					win.setX(x);
					win.setY(y);
				}
				win.setWidth(w);
				win.setHeight(h);
				
				switch(state)
				{
//				case WINDOW_ICONIFIED:
//					win.setIconified(true);
//					break;
				case WINDOW_FULLSCREEN:
					win.setFullScreen(true);
					break;
				case WINDOW_MAXIMIZED:
					win.setMaximized(true);
					break;
				}
			}
		}
		catch(Exception e)
		{ }
	}
	

	private static void storeLocalSettings(String prefix, LocalSettings s)
	{
		String k = prefix + SFX_BINDINGS;
		s.saveValues(k);
	}
	
	
	private static void restoreLocalSettings(String prefix, LocalSettings s)
	{
		String k = prefix + SFX_BINDINGS;
		s.loadValues(k);
	}

	
	private static void storeSplitPane(String prefix, SplitPane sp)
	{
		SStream s = new SStream();
		s.add(sp.getDividers().size());
		s.addAll(sp.getDividerPositions());
		
		String k = prefix + SFX_DIVIDERS;
		GlobalSettings.setStream(k, s);
	}
	
	
	private static void restoreSplitPane(String prefix, SplitPane sp)
	{
		String k = prefix + SFX_DIVIDERS;
		SStream s = GlobalSettings.getStream(k);
		
		// must run later because of FX split pane inability to set divider positions exactly
		FX.later(() ->
		{
			int ct = s.nextInt();
			if(sp.getDividers().size() == ct)
			{
				for(int i=0; i<ct; i++)
				{
					double div = s.nextDouble();
					sp.setDividerPosition(i, div);
				}
			}
		});
	}
	
	
	private static void storeTableView(String prefix, TableView t)
	{
		ObservableList<TableColumn<?,?>> cs = t.getColumns();
		int sz = cs.size();
		ObservableList<TableColumn<?,?>> sorted = t.getSortOrder();
		
		// columns: count,[id,width,sortOrder(0 for none, negative for descending, positive for ascending)
		SStream s = new SStream();
		s.add(sz);
		for(int i=0; i<sz; i++)
		{
			TableColumn<?,?> c = cs.get(i);
			
			int sortOrder = sorted.indexOf(c);
			if(sortOrder < 0)
			{
				sortOrder = 0;
			}
			else
			{
				sortOrder++;
				if(c.getSortType() == TableColumn.SortType.DESCENDING)
				{
					sortOrder = -sortOrder;
				}
			}
			
			s.add(c.getId());
			s.add(c.getWidth());
			s.add(sortOrder);
		}
		// FIX separate columns and width/sort
		GlobalSettings.setStream(prefix + SFX_COLUMNS, s);
		
		// selection
		int ix = t.getSelectionModel().getSelectedIndex();
		GlobalSettings.setInt(prefix + SFX_SELECTION, ix);
	}
	
	
	private static void restoreTableView(String prefix, TableView t)
	{
		ObservableList<TableColumn<?,?>> cs = t.getColumns();
		
		// columns
		SStream s = GlobalSettings.getStream(prefix + SFX_COLUMNS);
		int sz = s.nextInt();
		if(sz == cs.size())
		{
			for(int i=0; i<sz; i++)
			{
				TableColumn<?,?> c = cs.get(i);
				
				String id = s.nextString();
				double w = s.nextDouble();
				int sortOrder = s.nextInt();
				
				// TODO
			}
		}
		
		// selection
		int ix = GlobalSettings.getInt(prefix + SFX_SELECTION, -1);
		if(ix >= 0)
		{
			// if done immediately it screws up the selection model for some reason
			// FIX does not select for some reason.
			/* TODO 
			FX.later(() ->
			{
				if(ix < t.getItems().size())
				{
					t.getSelectionModel().select(ix);
				}
			});
			*/
		}
	}
	
	
	/** returns full path for the Node, starting with the window id, or null if saving is not permitted */
	private static String getFullName(String windowPrefix, Node root, Node n)
	{
		String name = getName(n);
		if(name == null)
		{
			return null;
		}
		
		SB sb = getFullNamePrivate(windowPrefix, null, root, n);
		return sb == null ? null : sb.toString();
	}


	private static SB getFullNamePrivate(String windowPrefix, SB sb, Node root, Node n)
	{
		if(n == null)
		{
			return null;
		}
		
		String name = getName(n);
		if(name == null)
		{
			return null;
		}
		
		if(n == root)
		{
			sb = new SB();
			sb.a(FX_PREFIX);
			sb.a(windowPrefix);
		}
		else
		{
			Parent p = n.getParent();
			if(p == null)
			{
				return null;
			}
			
			sb = getFullNamePrivate(windowPrefix, sb, root, p);
			if(sb == null)
			{
				return null;
			}
			
			sb.a('.');
			sb.a(name);
		}
		return sb;
	}
	

	public static void storeNode(String windowPrefix, Node root, Node n)
	{
		// TODO skip property
		
		String name = getFullName(windowPrefix, root, n);
		if(name == null)
		{
			return;
		}
		
		LocalSettings s = LocalSettings.find(n);
		if(s != null)
		{
			storeLocalSettings(name, s);
		}
		
		if(n instanceof SplitPane)
		{
			storeSplitPane(name, (SplitPane)n);
		}
		else if(n instanceof TableView)
		{
			storeTableView(name, (TableView)n);
		}
		
		if(n instanceof Parent)
		{
			for(Node ch: ((Parent)n).getChildrenUnmodifiable())
			{
				storeNode(windowPrefix, root, ch);
			}
		}
		
		// TODO trigger runnable
	}
	
	
	public static void restoreNode(String windowPrefix, Node root, Node n)
	{
		// TODO skip property
				
		String name = getFullName(windowPrefix, root, n);
		if(name == null)
		{
			return;
		}
		
		if(n instanceof SplitPane)
		{
			restoreSplitPane(name, (SplitPane)n);
		}
		else if(n instanceof TableView)
		{
			restoreTableView(name, (TableView)n);
		}
		
		if(n instanceof Parent)
		{
			for(Node ch: ((Parent)n).getChildrenUnmodifiable())
			{
				restoreNode(windowPrefix, root, ch);
			}
		}
		
		LocalSettings s = LocalSettings.find(n);
		if(s != null)
		{
			restoreLocalSettings(name, s);
		}
		
		Runnable r = getOnSettingsLoaded(n);
		if(r != null)
		{
			r.run();
		}
	}
	
	
	public static void setName(Node n, String name)
	{
		n.getProperties().put(PROP_NAME, name);
	}
	
	
	private static String getName(Node n)
	{
		Object x = n.getProperties().get(PROP_NAME);
		if(x instanceof String)
		{
			return (String)x;
		}
		
		if(n instanceof MenuBar)
		{
			return null;
		}
		else if(n instanceof Shape)
		{
			return null;
		}
		else if(n instanceof ImageView)
		{
			return null;
		}
		
		String id = n.getId();
		if(id != null)
		{
			return id;
		}
				
		return n.getClass().getSimpleName();
	}
	
	
	public static void setOnSettingsLoaded(Node n, Runnable r)
	{
		n.getProperties().put(PROP_LOAD_HANDLER, r);
	}
	
	
	private static Runnable getOnSettingsLoaded(Node n)
	{
		Object x = n.getProperties().get(PROP_LOAD_HANDLER);
		if(x instanceof Runnable)
		{
			return (Runnable)x;
		}
		return null;
	}
}
