// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.internal.ASettingsStore;
import goryachev.fx.internal.FxSettingsSchema;
import goryachev.fx.internal.LocalSettings;
import goryachev.fx.internal.WinMonitor;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * FxDock framework schema for the layout storage.
 */
// TODO rename
public abstract class FxDockSchema
	extends FxSettingsSchema
{
	@Override
	public abstract Stage createWindow(String name);
	
	@Override
	public abstract Stage createDefaultWindow();
	
	public abstract FxDockPane createPane(String type);
	
	//
	
	private static final String PREFIX_DOCK = "FXDOCK.";
	private static final String PREFIX_WINDOW = PREFIX_DOCK + "w.";
	
	private static final String NAME_PANE = ".P";
	private static final String NAME_TAB = ".T";
	private static final String NAME_SPLIT = ".S";
	
	private static final String SUFFIX_BINDINGS = ".bindings";
	private static final String SUFFIX_LAYOUT = ".layout";
	private static final String SUFFIX_SELECTED_TAB = ".tab";
	private static final String SUFFIX_SPLITS = ".splits";
	private static final String SUFFIX_WINDOW = ".window";
	
	private static final String TYPE_EMPTY = "E";
	private static final String TYPE_PANE = "P";
	private static final String TYPE_HSPLIT = "H";
	private static final String TYPE_VSPLIT = "V";
	private static final String TYPE_TAB = "T";
	
	
	public FxDockSchema(ASettingsStore s)
	{
		super(s);
	}
	
	
	// FIX remove
	public static String windowID(int n)
	{
		return PREFIX_WINDOW + n;
	}
	
	
	public static void clearSettings()
	{
		// remove previously saved layout
//		for(String k: GlobalSettings.getKeys())
//		{
//			if(k.startsWith(PREFIX_WINDOW))
//			{
//				GlobalSettings.setString(k, null);
//			}
//		}
	}

	
	public void restoreWindow(Window w)
	{
		if(w instanceof FxDockWindow dw)
		{
			WinMonitor m = WinMonitor.forWindow(w);
			if(m != null)
			{
				String prefix = PREFIX_DOCK + m.getID();
				Node n = loadLayout(prefix);
				dw.setContent(n);
				
				loadContentSettings(prefix, n);

				// FIX remove duplicate call?
				LocalSettings settings = LocalSettings.find(w);
				if(settings != null)
				{
					String k = prefix + SUFFIX_BINDINGS;
					settings.loadValues(k);
				}
			}
		}
		
		super.restoreWindow(w);
	}
	
	
	public void storeWindow(Window w)
	{
		super.storeWindow(w);
		
		if(w instanceof FxDockWindow dw)
		{
			WinMonitor m = WinMonitor.forWindow(w);
			if(m != null)
			{
				String prefix = PREFIX_DOCK + m.getID();
				Node n = dw.getContent();
				saveLayout(prefix, n);
				
				// FIX remove duplicate call?
				LocalSettings settings = LocalSettings.get(w);
				if(settings != null)
				{
					String k = prefix + SUFFIX_BINDINGS;
					settings.saveValues(k);
				}
				
				saveContentSettings(prefix, n);
			}
		}
	}


	// TODO rename loadDockWindowContent
	public Node loadLayout(String prefix)
	{
		SStream s = store().getStream(prefix + SUFFIX_LAYOUT);
		return loadLayoutRecursively(s);
	}
	
	
	protected FxDockSplitPane loadSplit(SStream s, Orientation or)
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
	
	
	protected Node loadLayoutRecursively(SStream s)
	{
		String t = s.nextString();
		if(t == null)
		{
			return null;
		}
		else if(TYPE_PANE.equals(t))
		{
			String type = s.nextString();
			return createPane(type);
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


	public void saveLayout(String prefix, Node n)
	{
		SStream s = saveLayoutPrivate(n);
		store().setStream(prefix + SUFFIX_LAYOUT, s);
	}
	
	
	protected SStream saveLayoutPrivate(Node content)
	{
		SStream s = new SStream();
		saveLayoutRecursively(s, content);
		return s;
	}


	protected void saveLayoutRecursively(SStream s, Node n)
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
	
	
	protected String getPath(String prefix, Node n, String suffix)
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


	protected void getPathRecursive(SB sb, Node n)
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
	// TODO use to load content
	protected void loadContentSettings(String prefix, Node n)
	{
		if(n != null)
		{
			if(n instanceof FxDockPane p)
			{
				loadPaneSettings(prefix, p);
			}
			else if(n instanceof FxDockSplitPane p)
			{
				for(Node ch: p.getPanes())
				{
					loadContentSettings(prefix, ch);
				}

				// because of the split pane idiosyncrasy with layout
				Platform.runLater(() -> loadSplitPaneSettings(prefix, p));
			}
			else if(n instanceof FxDockTabPane p)
			{
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
	public void saveContentSettings(String prefix, Node n)
	{
		if(n != null)
		{
			if(n instanceof FxDockPane p)
			{
				savePaneSettings(prefix, p);
			}
			else if(n instanceof FxDockSplitPane p)
			{
				saveSplitPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					saveContentSettings(prefix, ch);
				}
			}
			else if(n instanceof FxDockTabPane p)
			{
				saveTabPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					saveContentSettings(prefix, ch);
				}
			}
		}
	}


	protected void loadPaneSettings(String prefix, FxDockPane p)
	{
		LocalSettings s = LocalSettings.find(p);
		if(s != null)
		{
			String k = getPath(prefix, p, SUFFIX_BINDINGS);
			s.loadValues(k);
		}
	}


	protected void savePaneSettings(String prefix, FxDockPane p)
	{
		LocalSettings s = LocalSettings.find(p);
		if(s != null)
		{
			String k = getPath(prefix, p, SUFFIX_BINDINGS);
			s.saveValues(k);
		}
	}


	protected void saveSplitPaneSettings(String prefix, FxDockSplitPane p)
	{
		double[] divs = p.getDividerPositions();
		
		SStream s = new SStream();
		s.addAll(divs);
		
		String k = getPath(prefix, p, SUFFIX_SPLITS);
		store().setStream(k, s);
	}
	
	
	protected void loadSplitPaneSettings(String prefix, FxDockSplitPane p)
	{
		String k = getPath(prefix, p, SUFFIX_SPLITS);
		SStream s = store().getStream(k);
		
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
	
	
	protected void saveTabPaneSettings(String prefix, FxDockTabPane p)
	{
		int ix = p.getSelectedTabIndex();
		
		String k = getPath(prefix, p, SUFFIX_SELECTED_TAB);
		store().setInt(k, ix);
	}
	
	
	protected void loadTabPaneSettings(String prefix, FxDockTabPane p)
	{
		String k = getPath(prefix, p, SUFFIX_SELECTED_TAB);
		int ix = store().getInt(k, 0);
		
		p.select(ix);
	}
}
