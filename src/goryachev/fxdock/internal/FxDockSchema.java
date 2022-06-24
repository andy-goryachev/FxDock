// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.FX;
import goryachev.fx.internal.FxSchema;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Node;


/**
 * FxDock framework schema for the layout storage.
 */
public class FxDockSchema
{
	public static final String PREFIX_DOCK = "fxdock.";
	public static final String PREFIX_WINDOW = PREFIX_DOCK + "w.";
	public static final String KEY_WINDOW_COUNT = PREFIX_WINDOW + "count";
	
	public static final String NAME_PANE = ".P";
	public static final String NAME_TAB = ".T";
	public static final String NAME_SPLIT = ".S";
	
	public static final String SUFFIX_BINDINGS = ".bindings";
	public static final String SUFFIX_LAYOUT = ".layout";
	public static final String SUFFIX_SELECTED_TAB = ".tab";
	public static final String SUFFIX_SPLITS = ".splits";
	public static final String SUFFIX_WINDOW = ".window";
	
	public static final String TYPE_EMPTY = "E";
	public static final String TYPE_PANE = "P";
	public static final String TYPE_HSPLIT = "H";
	public static final String TYPE_VSPLIT = "V";
	public static final String TYPE_TAB = "T";
	
	
	public static int getWindowCount()
	{
		return GlobalSettings.getInt(KEY_WINDOW_COUNT, 0);
	}
	
	
	public static void setWindowCount(int n)
	{
		 GlobalSettings.setInt(KEY_WINDOW_COUNT, n);
	}
	
	
	public static String windowID(int n)
	{
		return PREFIX_WINDOW + n;
	}
	
	
	public static void clearSettings()
	{
		// remove previously saved layout
		for(String k: GlobalSettings.getKeys())
		{
			if(k.startsWith(PREFIX_WINDOW))
			{
				GlobalSettings.setString(k, null);
			}
		}
	}


	public static void storeWindow(String prefix, FxDockWindow win)
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
			s.add(FxSchema.WINDOW_FULLSCREEN);
		}
		else if(win.isMaximized())
		{
			s.add(FxSchema.WINDOW_MAXIMIZED);
		}
		else if(win.isIconified())
		{
			s.add(FxSchema.WINDOW_ICONIFIED);
		}
		else
		{
			s.add(FxSchema.WINDOW_NORMAL);
		}

		GlobalSettings.setStream(prefix + SUFFIX_WINDOW, s);
	}
	
	
	public static void restoreWindow(String prefix, FxDockWindow win)
	{
		SStream s = GlobalSettings.getStream(prefix + SUFFIX_WINDOW);
		if(s.size() == 5)
		{
			double x = s.nextDouble();
			double y = s.nextDouble();
			double w = s.nextDouble();
			double h = s.nextDouble();
			String t = s.nextString();
			
			if((w > 0) && (h > 0))
			{
				// TODO unnecessary anymore
				if(FX.isValidCoordinates(x, y))
				{
					// iconified windows have (x,y) of -32000 for some reason
					// their coordinates are essentially lost (unless there is a way to get them in FX)
					win.setX(x);
					win.setY(y);
				}
				win.setWidth(w);
				win.setHeight(h);
				
				switch(t)
				{
				// there is no point in restoring to minimized state
//				case WINDOW_ICONIFIED:
//					win.setIconified(true);
//					break;
				case FxSchema.WINDOW_FULLSCREEN:
					win.setFullScreen(true);
					break;
				case FxSchema.WINDOW_MAXIMIZED:
					win.setMaximized(true);
					break;
				}
			}
		}
	}


	public static Node loadLayout(String prefix)
	{
		SStream s = GlobalSettings.getStream(prefix + SUFFIX_LAYOUT);
		return loadLayoutRecursively(s);
	}
	
	
	private static FxDockSplitPane loadSplit(SStream s, Orientation or)
	{
		FxDockSplitPane sp = new FxDockSplitPane();
		sp.setOrientation(or);
		int sz = s.nextInt();
		for(int i=0; i<sz; i++)
		{
			Node ch = loadLayoutRecursively(s);
			sp.addPane(ch);
		}
		return sp;
	}
	
	
	private static Node loadLayoutRecursively(SStream s)
	{
		String t = s.nextString();
		if(t == null)
		{
			return null;
		}
		else if(TYPE_PANE.equals(t))
		{
			String type = s.nextString();
			return FxDockFramework.createPane(type);
		}
		else if(TYPE_HSPLIT.equals(t))
		{
			return loadSplit(s, Orientation.HORIZONTAL);
		}
		else if(TYPE_VSPLIT.equals(t))
		{
			return loadSplit(s, Orientation.VERTICAL);
		}
		else if(TYPE_TAB.equals(t))
		{
			FxDockTabPane tp = new FxDockTabPane();
			int sz = s.nextInt();
			for(int i=0; i<sz; i++)
			{
				Node ch = loadLayoutRecursively(s);
				tp.addTab(ch);
			}
			return tp;
		}
		else if(TYPE_EMPTY.equals(t))
		{
			return new FxDockEmptyPane();
		}
		else
		{
			return null;
		}
	}


	public static void saveLayout(String prefix, Node n)
	{
		SStream s = saveLayoutPrivate(n);
		GlobalSettings.setStream(prefix + SUFFIX_LAYOUT, s);
	}
	
	
	protected static SStream saveLayoutPrivate(Node content)
	{
		SStream s = new SStream();
		saveLayoutRecursively(s, content);
		return s;
	}


	private static void saveLayoutRecursively(SStream s, Node n)
	{
		if(n == null)
		{
			return;
		}
		else if(n instanceof FxDockPane)
		{
			String type = ((FxDockPane)n).getDockPaneType();
			
			s.add(TYPE_PANE);
			s.add(type);
		}
		else if(n instanceof FxDockSplitPane)
		{
			FxDockSplitPane sp = (FxDockSplitPane)n;
			int ct = sp.getPaneCount();
			Orientation or = sp.getOrientation();
			
			s.add(or == Orientation.HORIZONTAL ? TYPE_HSPLIT : TYPE_VSPLIT);
			s.add(ct);
			
			for(Node ch: sp.getPanes())
			{
				saveLayoutRecursively(s, ch);
			}
		}
		else if(n instanceof FxDockTabPane)
		{
			FxDockTabPane tp = (FxDockTabPane)n;
			int ct = tp.getTabCount();
			
			s.add(TYPE_TAB);
			s.add(ct);
			
			for(Node ch: tp.getPanes())
			{
				saveLayoutRecursively(s, ch);
			}
		}
		else if(n instanceof FxDockEmptyPane)
		{
			s.add(TYPE_EMPTY);
		}
		else
		{
			throw new Error("?" + n);
		}
	}
	
	
	public static String getPath(String prefix, Node n, String suffix)
	{
		SB sb = new SB(128);
		sb.append(prefix);
		getPathRecursive(sb, n);
		if(suffix != null)
		{
			sb.a(suffix);
		}
		return sb.toString();
	}


	private static void getPathRecursive(SB sb, Node n)
	{
		Node p = DockTools.getParent(n);
		if(p != null)
		{
			getPathRecursive(sb, p);
			
			if(p instanceof FxDockSplitPane)
			{
				int ix = ((FxDockSplitPane)p).indexOfPane(n);
				sb.a('.');
				sb.a(ix);
			}
			else if(p instanceof FxDockTabPane)
			{
				int ix = ((FxDockTabPane)p).indexOfTab(n);
				sb.a('.');
				sb.a(ix);
			}
		}
		
		if(n instanceof FxDockRootPane)
		{
			return;
		}
		else if(n instanceof FxDockSplitPane)
		{
			sb.a(NAME_SPLIT);
		}
		else if(n instanceof FxDockTabPane)
		{
			sb.a(NAME_TAB);
		}
		else if(n instanceof FxDockPane)
		{
			sb.a(NAME_PANE);
		}
		else
		{
			throw new Error("?" + n);
		}
	}
	
	
	/** 
	 * default functionality provided by the docking framework to load window content settings.
	 * what's being stored:
	 * - split positions
	 * - settings bindings
	 */
	public static void loadContentSettings(String prefix, Node n)
	{
		if(n != null)
		{
			if(n instanceof FxDockPane)
			{
				((FxDockPane)n).loadPaneSettings(prefix);
			}
			else if(n instanceof FxDockSplitPane)
			{
				FxDockSplitPane p = (FxDockSplitPane)n;

				for(Node ch: p.getPanes())
				{
					loadContentSettings(prefix, ch);
				}

				// because of the split pane idiosyncrasy with layout
				Platform.runLater(() -> loadSplitPaneSettings(prefix, p));
			}
			else if(n instanceof FxDockTabPane)
			{
				FxDockTabPane p = (FxDockTabPane)n;
				loadTabPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					loadContentSettings(prefix, ch);
				}
			}
		}
	}

	
	/** 
	 * default functionality provided by the docking framework to store window content settings.
	 * what's being stored:
	 * - split positions
	 * - settings bindings
	 */
	public static void saveContentSettings(String prefix, Node n)
	{
		if(n != null)
		{
			if(n instanceof FxDockPane)
			{
				((FxDockPane)n).savePaneSettings(prefix);
			}
			else if(n instanceof FxDockSplitPane)
			{
				FxDockSplitPane p = (FxDockSplitPane)n;
				saveSplitPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					saveContentSettings(prefix, ch);
				}
			}
			else if(n instanceof FxDockTabPane)
			{
				FxDockTabPane p = (FxDockTabPane)n;
				saveTabPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					saveContentSettings(prefix, ch);
				}
			}
		}
	}
	
	
	private static void saveSplitPaneSettings(String prefix, FxDockSplitPane p)
	{
		double[] divs = p.getDividerPositions();
		
		SStream s = new SStream();
		s.addAll(divs);
		
		String k = getPath(prefix, p, SUFFIX_SPLITS);
		GlobalSettings.setStream(k, s);
	}
	
	
	private static void loadSplitPaneSettings(String prefix, FxDockSplitPane p)
	{
		String k = getPath(prefix, p, SUFFIX_SPLITS);
		SStream s = GlobalSettings.getStream(k);
		
		int ct = s.size();
		if(p.getDividers().size() == ct)
		{
			for(int i=0; i<ct; i++)
			{
				double pos = s.nextDouble();
				p.setDividerPosition(i, pos);
			}
		}
	}
	
	
	private static void saveTabPaneSettings(String prefix, FxDockTabPane p)
	{
		int ix = p.getSelectedTabIndex();
		
		String k = getPath(prefix, p, SUFFIX_SELECTED_TAB);
		GlobalSettings.setInt(k, ix);
	}
	
	
	private static void loadTabPaneSettings(String prefix, FxDockTabPane p)
	{
		String k = getPath(prefix, p, SUFFIX_SELECTED_TAB);
		int ix = GlobalSettings.getInt(k, 0);
		
		p.select(ix);
	}
}
