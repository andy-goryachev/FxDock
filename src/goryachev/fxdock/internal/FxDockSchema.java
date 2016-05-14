// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import javafx.geometry.Orientation;
import javafx.scene.Node;


/**
 * FxDock framework schema for the layout storage.
 */
public class FxDockSchema
{
	public static final String PREFIX_DOCK = "fxdock.";
	public static final String PREFIX_WINDOW = PREFIX_DOCK + "window.";
	
	public static final String SUFFIX_LAYOUT = ".layout";
	public static final String SUFFIX_WINDOW = ".window";
	
	public static final String KEY_WINDOW_COUNT = PREFIX_DOCK + "window.count";
	
	public static final String STAGE_FULL_SCEEN = "F";
	public static final String STAGE_ICONIFIED = "I";
	public static final String STAGE_MAXIMIZED = "X";
	public static final String STAGE_NORMAL = "N";
	
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


	public static void storeWindow(String id, FxDockWindow w)
	{
		SStream s = new SStream();
		s.add(w.getX());
		s.add(w.getY());
		s.add(w.getWidth());
		s.add(w.getHeight());

		if(w.isFullScreen())
		{
			s.add(STAGE_FULL_SCEEN);
		}
		else if(w.isMaximized())
		{
			s.add(STAGE_MAXIMIZED);
		}
		else if(w.isIconified())
		{
			s.add(STAGE_ICONIFIED);
		}
		else
		{
			s.add(STAGE_NORMAL);
		}

		GlobalSettings.setStream(id + SUFFIX_WINDOW, s);
	}
	
	
	public static void restoreWindow(String id, FxDockWindow w)
	{
		SStream s = GlobalSettings.getStream(id + SUFFIX_WINDOW);
		if(s.size() == 5)
		{
			double x = s.nextDouble();
			double y = s.nextDouble();
			double width = s.nextDouble();
			double h = s.nextDouble();
			String t = s.nextString();
			
			w.setX(x);
			w.setY(y);
			w.setWidth(width);
			w.setHeight(h);

			if(STAGE_FULL_SCEEN.equals(t))
			{
				w.setFullScreen(true);
			}
			else if(STAGE_MAXIMIZED.equals(t))
			{
				w.setMaximized(true);
			}
			else if(STAGE_ICONIFIED.equals(t))
			{
				w.setIconified(true);
			}
		}
	}


	public static Node loadLayout(String id)
	{
		SStream s = GlobalSettings.getStream(id + SUFFIX_LAYOUT);
		return loadLayoutPrivate(s);
	}
	
	
	private static FxDockSplitPane loadSplit(SStream s, Orientation or)
	{
		FxDockSplitPane sp = new FxDockSplitPane();
		sp.setOrientation(or);
		int sz = s.nextInt();
		for(int i=0; i<sz; i++)
		{
			Node ch = loadLayoutPrivate(s);
			sp.addPane(ch);
		}
		return sp;
	}
	
	
	private static Node loadLayoutPrivate(SStream s)
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
				Node ch = loadLayoutPrivate(s);
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
		saveLayoutPrivate(s, content);
		return s;
	}


	private static void saveLayoutPrivate(SStream s, Node n)
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
				saveLayoutPrivate(s, ch);
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
				saveLayoutPrivate(s, ch);
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
}
