// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


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
	
	private static final Object PROP_BINDINGS = new Object();

	
	public static void storeStage(Stage win, String prefix)
	{
		double x = win.getX();
		double y = win.getY();
		double w = win.getWidth();
		double h = win.getHeight();
		
		String state;
		if(win.isFullScreen())
		{
			state = WINDOW_FULLSCREEN;
		}
		else if(win.isMaximized())
		{
			state = WINDOW_MAXIMIZED;
		}
		else if(win.isIconified())
		{
			state = WINDOW_ICONIFIED;
		}
		else
		{
			state = WINDOW_NORMAL;
		}
		
		SStream s = new SStream();
		s.add(x);
		s.add(y);
		s.add(w);
		s.add(h);
		s.add(state);

		GlobalSettings.setStream(FX_PREFIX + prefix, s);
	}
	
	
	public static void restoreStage(Stage win, String prefix)
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
				case WINDOW_ICONIFIED:
					win.setIconified(true);
					break;
				case WINDOW_FULLSCREEN:
					win.setFullScreen(true);
					break;
				}
			}
		}
		catch(Exception e)
		{ }
	}
	

	private static void storeBindings(String prefix, LocalBindings bindings)
	{
		if(bindings != null)
		{
			String k = prefix + SFX_BINDINGS;
			bindings.saveValues(k);
		}
	}
	
	
	private static void restoreBindings(String prefix, LocalBindings bindings)
	{
		if(bindings != null)
		{
			String k = prefix + SFX_BINDINGS;
			bindings.loadValues(k);
		}
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
	
	
	private static String findName(String wprefix, Node n)
	{
		String name = getName(n);
		if(name == null)
		{
			return null;
		}
		
		SB sb = new SB();
		sb.a(FX_PREFIX);
		sb.a(wprefix);
		sb.a('.');
		findPrefixPrivate(sb, n);
		return sb.toString();
	}


	private static void findPrefixPrivate(SB sb, Node n)
	{
		if(n != null)
		{
			String name = getName(n);
			
			Parent p = n.getParent();
			if(p != null)
			{
				findPrefixPrivate(sb, p);
				sb.a('.');
			}
			
			sb.a(name);
		}
	}
	

	public static void storeNode(String wprefix, Node n)
	{
		// TODO skip is storing is not supported
		
		storeBindings(wprefix, bindings(n, false));
		
		String prefix = findName(wprefix, n);
		if(prefix != null)
		{
			if(n instanceof SplitPane)
			{
				storeSplitPane(prefix, (SplitPane)n);
			}
			else if(n instanceof TableView)
			{
				storeTableView(prefix, (TableView)n);
			}
			
			if(n instanceof Parent)
			{
				for(Node ch: ((Parent)n).getChildrenUnmodifiable())
				{
					storeNode(wprefix, ch);
				}
			}
		}
	}
	
	
	public static void restoreNode(String wprefix, Node n)
	{
		// TODO skip is storing is not supported
		
		String prefix = findName(wprefix, n);
		if(prefix != null)
		{
			if(n instanceof SplitPane)
			{
				restoreSplitPane(prefix, (SplitPane)n);
			}
			else if(n instanceof TableView)
			{
				restoreTableView(prefix, (TableView)n);
			}
			
			if(n instanceof Parent)
			{
				for(Node ch: ((Parent)n).getChildrenUnmodifiable())
				{
					restoreNode(wprefix, ch);
				}
			}
		}
		
		restoreBindings(wprefix, bindings(n, false));
	}
	
	
	public static LocalBindings bindings(Node n, boolean create)
	{
		LocalBindings b = (LocalBindings)n.getProperties().get(PROP_BINDINGS);
		if(b == null)
		{
			if(create)
			{
				b = new LocalBindings();
				n.getProperties().put(PROP_BINDINGS, b);
			}
		}
		return b;
	}
	
	
	private static String getName(Node n)
	{
		if(n instanceof MenuBar)
		{
			return null;
		}
		else if(n instanceof Shape)
		{
			return null;
		}
		
		String id = n.getId();
		if(id != null)
		{
			return id;
		}
		
		if(n instanceof CPane)
		{
			return "CPANE";
		}
		else if(n instanceof BorderPane)
		{
			return "BP";
		}
		else if(n instanceof Group)
		{
			return "GR";
		}
		else if(n instanceof SplitPane)
		{
			return "SPLIT";
		}
		else if(n instanceof StackPane)
		{
			return "SP";
		}
		else if(n instanceof TableView)
		{
			return "TABLE";
		}
		else if(n instanceof TreeTableView)
		{
			return "TREETABLE";
		}
		else if(n instanceof TreeView)
		{
			return "TREE";
		}
		else if(n instanceof Region)
		{
			return "R";
		}
		
		throw new Error("?" + n);
	}
}
