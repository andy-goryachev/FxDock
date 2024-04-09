// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.common.log.Log;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.settings.ASettingsStore;
import goryachev.fx.settings.FxSettingsSchema;
import goryachev.fx.settings.WindowMonitor;
import goryachev.fxdock.internal.DockTools;
import goryachev.fxdock.internal.FxDockEmptyPane;
import goryachev.fxdock.internal.FxDockRootPane;
import goryachev.fxdock.internal.FxDockSplitPane;
import goryachev.fxdock.internal.FxDockTabPane;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * FxDock framework schema for the layout storage.
 */
public abstract class FxDockSchema
	extends FxSettingsSchema
{
	@Override
	public abstract Stage createWindow(String name);
	
	@Override
	public abstract Stage createDefaultWindow();
	
	public abstract FxDockPane createPane(String type);
	
	//
	
	private static final String NAME_PANE = ".P";
	private static final String NAME_TAB = ".T";
	private static final String NAME_SPLIT = ".S";
	
	private static final String SUFFIX_CONTENT = ".layout";
	private static final String SUFFIX_SELECTED_TAB = ".tab";
	private static final String SUFFIX_SPLITS = ".splits";
	
	private static final String TYPE_EMPTY = "E";
	private static final String TYPE_PANE = "P";
	private static final String TYPE_HSPLIT = "H";
	private static final String TYPE_VSPLIT = "V";
	private static final String TYPE_TAB = "T";
	
	private static final Log log = Log.get("FxDockSchema");
	
	
	public FxDockSchema(ASettingsStore s)
	{
		super(s);
	}
	

	@Override
	protected void restoreWindowLocal(Window w, WindowMonitor m)
	{
		super.restoreWindowLocal(w, m);

		if(w instanceof FxDockWindow dw)
		{
			String prefix = FX_PREFIX + m.getID();

//			if(dw.getContent() == null)
			{
				Node n = loadDockWindowContent(prefix);
				if(n != null)
				{
					dw.setContent(n);
					restoreContent(prefix, n);
				}
			}
		}
	}
	

	@Override
	protected void storeWindowLocal(Window w, WindowMonitor m)
	{
		super.storeWindowLocal(w, m);
		
		if(w instanceof FxDockWindow dw)
		{
			String prefix = FX_PREFIX + m.getID();
			Node n = dw.getContent();
			saveDockWindowContent(prefix, n);

			storeContent(prefix, n);
		}
	}


	protected Node loadDockWindowContent(String prefix)
	{
		SStream s = store().getStream(prefix + SUFFIX_CONTENT);
		return loadContentRecursively(s);
	}
	

	protected void saveDockWindowContent(String prefix, Node n)
	{
		SStream s = new SStream();
		saveContentRecursively(s, n);
		store().setStream(prefix + SUFFIX_CONTENT, s);
	}
	
	
	protected FxDockSplitPane loadSplit(SStream s, Orientation or)
	{
		FxDockSplitPane sp = new FxDockSplitPane();
		sp.setOrientation(or);
		int sz = s.nextInt();
		for(int i=0; i<sz; i++)
		{
			Node ch = loadContentRecursively(s);
			sp.addPane(ch);
		}
		return sp;
	}
	
	
	protected Node loadContentRecursively(SStream s)
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
				Node ch = loadContentRecursively(s);
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


	protected void saveContentRecursively(SStream s, Node n)
	{
		if(n == null)
		{
			return;
		}
		else if(n instanceof FxDockPane p)
		{
			String type = p.getDockPaneType();
			
			s.add(TYPE_PANE);
			s.add(type);
		}
		else if(n instanceof FxDockSplitPane p)
		{
			int ct = p.getPaneCount();
			Orientation or = p.getOrientation();
			
			s.add(or == Orientation.HORIZONTAL ? TYPE_HSPLIT : TYPE_VSPLIT);
			s.add(ct);
			
			for(Node ch: p.getPanes())
			{
				saveContentRecursively(s, ch);
			}
		}
		else if(n instanceof FxDockTabPane p)
		{
			int ct = p.getTabCount();
			
			s.add(TYPE_TAB);
			s.add(ct);
			
			for(Node ch: p.getPanes())
			{
				saveContentRecursively(s, ch);
			}
		}
		else if(n instanceof FxDockEmptyPane p)
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


	protected void getPathRecursive(SB sb, Node node)
	{
		Node n = DockTools.getParent(node);
		if(n != null)
		{
			getPathRecursive(sb, n);
			
			if(n instanceof FxDockSplitPane p)
			{
				int ix = p.indexOfPane(node);
				sb.a('.');
				sb.a(ix);
			}
			else if(n instanceof FxDockTabPane p)
			{
				int ix = p.indexOfTab(node);
				sb.a('.');
				sb.a(ix);
			}
		}
		
		if(node instanceof FxDockRootPane)
		{
			return;
		}
		else if(node instanceof FxDockSplitPane)
		{
			sb.a(NAME_SPLIT);
		}
		else if(node instanceof FxDockTabPane)
		{
			sb.a(NAME_TAB);
		}
		else if(node instanceof FxDockPane)
		{
			sb.a(NAME_PANE);
		}
		else
		{
			throw new Error("?" + node);
		}
	}
	
	
	/** 
	 * default functionality provided by the docking framework to load window content settings.
	 * what's being stored:
	 * - split positions
	 * - settings bindings
	 */
	protected void restoreContent(String prefix, Node n)
	{
		if(n != null)
		{
			if(n instanceof FxDockPane p)
			{
				//loadPaneSettings(prefix, p);
			}
			else if(n instanceof FxDockSplitPane p)
			{
				for(Node ch: p.getPanes())
				{
					restoreContent(prefix, ch);
				}

				// because of the split pane idiosyncrasy with layout
				Platform.runLater(() -> loadSplitPaneSettings(prefix, p));
			}
			else if(n instanceof FxDockTabPane p)
			{
				loadTabPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					restoreContent(prefix, ch);
				}
			}
			
			restoreNode(n);
		}
	}

	
	/** 
	 * default functionality provided by the docking framework to store window content settings.
	 * what's being stored:
	 * - split positions
	 * - settings bindings
	 */
	protected void storeContent(String prefix, Node n)
	{
		if(n != null)
		{
			storeNode(n);
			
			if(n instanceof FxDockPane p)
			{
				//savePaneSettings(prefix, p);
			}
			else if(n instanceof FxDockSplitPane p)
			{
				saveSplitPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					storeContent(prefix, ch);
				}
			}
			else if(n instanceof FxDockTabPane p)
			{
				saveTabPaneSettings(prefix, p);
				
				for(Node ch: p.getPanes())
				{
					storeContent(prefix, ch);
				}
			}
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
