// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.settings;
import goryachev.common.log.Log;
import goryachev.common.util.ASettingsStore;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxDialog;
import goryachev.fx.FxFramework;
import goryachev.fx.util.FxTools;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * Stores and restores the UI state.
 */
// TODO consider making it generic <ActualWindow>
public abstract class FxSettingsSchema
{
	public abstract Stage createDefaultWindow();

	protected abstract Stage createWindow(String name);
	
	protected void loadWindowContent(WindowMonitor m, Stage w) { }

	//
	
	protected static final String FX_PREFIX = "FX.";
	
	private static final String SFX_WINDOWS = "WINDOWS";
	private static final String SFX_COLUMNS = ".COLS";
	private static final String SFX_DIVIDERS = ".DIVS";
	private static final String SFX_EXPANDED = ".EXP";
	private static final String SFX_SELECTION = ".SEL";
	private static final String SFX_SETTINGS = ".SETTINGS";
	
	private static final String SORT_ASCENDING = "A";
	private static final String SORT_DESCENDING = "D";
	private static final String SORT_NONE = "N";
	
	private static final String WINDOW_FULLSCREEN = "F";
	private static final String WINDOW_MAXIMIZED = "X";
	private static final String WINDOW_ICONIFIED = "I";
	private static final String WINDOW_NORMAL = "N";
	
	private static final Log log = Log.get("FxSettingsSchema");
	private final ASettingsStore store;
	
	
	public FxSettingsSchema(ASettingsStore store)
	{
		this.store = store;
	}
	
	
	public void storeWindow(Window w)
	{
		log.debug(() -> FxTools.describe(w));
		
		if(FX.isSkipSettings(w))
		{
			return;
		}

		WindowMonitor m = WindowMonitor.forWindow(w);
		if(m != null)
		{
			storeWindowLocal(w, m);
		}
	}
	
	
	protected void storeWindowLocal(Window w, WindowMonitor m)
	{
		double x = m.getX();
		double y = m.getY();
		double width = m.getW();
		double height = m.getH();
		
		SStream ss = new SStream();
		ss.add(x);
		ss.add(y);
		ss.add(width);
		ss.add(height);
		
		if(w instanceof Stage s)
		{
			if(s.isFullScreen())
			{
				ss.add(WINDOW_FULLSCREEN);
			}
			else if(s.isMaximized())
			{
				ss.add(WINDOW_MAXIMIZED);
			}
			else if(s.isIconified())
			{
				ss.add(WINDOW_ICONIFIED);
			}
			else
			{
				ss.add(WINDOW_NORMAL);
			}
		}

		store.setStream(FX_PREFIX + m.getID(), ss);
		
		LocalSettings s = LocalSettings.getOrNull(w);
		if(s != null)
		{
			String k = FX_PREFIX + m.getID() + SFX_SETTINGS;
			s.saveValues(k, store);
		}
		
		Node n = w.getScene().getRoot();
        storeNode(n);
	}
	
	
	public void restoreWindow(Window w)
	{
		log.debug(() -> FxTools.describe(w));

		if(w instanceof PopupWindow)
		{
			return;
		}
		
		if(FX.isSkipSettings(w))
		{
			return;
		}
		else if(w instanceof Stage s)
		{
			if(s.getModality() != Modality.NONE)
			{
				return;
			}
		}
		
		WindowMonitor m = WindowMonitor.forWindow(w);
		if(m != null)
		{
			restoreWindowLocal(w, m);
			
			LocalSettings s = LocalSettings.getOrNull(w);
			if(s != null)
			{
				String k = FX_PREFIX + m.getID() + SFX_SETTINGS;
				s.loadValues(k, store);
			}
			
			Node n = w.getScene().getRoot();
            restoreNode(n);
		}
	}
	
	
	protected void restoreWindowLocal(Window w, WindowMonitor m)
	{
		SStream ss = store.getStream(FX_PREFIX + m.getID());
		if(ss != null)
		{
			double x = ss.nextDouble(-1);
			double y = ss.nextDouble(-1);
			double width = ss.nextDouble(-1);
			double height = ss.nextDouble(-1);
			String state = ss.nextString(WINDOW_NORMAL);
			
			if((width > 0) && (height > 0))
			{
				if
				(
					FX.isValidCoordinates(x, y) &&
					(!(w instanceof FxDialog))
				)
				{
					// iconified windows have (x,y) of -32000 for some reason
					// their coordinates are essentially lost (unless there is a way to get them in FX)
					w.setX(x);
					w.setY(y);
				}

				if(w instanceof Stage s)
				{
					if(s.isResizable())
					{
						w.setWidth(width);
						w.setHeight(height);
					}
					else
					{
						width = w.getWidth();
						height = w.getHeight();
					}
					
					switch(state)
					{
					case WINDOW_FULLSCREEN:
						s.setFullScreen(true);
						break;
					case WINDOW_MAXIMIZED:
						s.setMaximized(true);
						break;
					}
					
					if(w instanceof FxDialog d)
					{
						Window parent = d.getOwner();
						if(parent != null)
						{
							double cx = parent.getX() + (parent.getWidth() / 2);
							double cy = parent.getY() + (parent.getHeight() / 2);
							// TODO check 
							d.setX(cx - width/2);
							d.setY(cy - height/2);
						}
					}
				}
			}
		}
	}


	public void storeNode(Node n)
	{
		if(n == null)
		{
			return;
		}

		if(FX.isSkipSettings(n))
		{
			return;
		}

		String name = computeName(n);
		if(name == null)
		{
			return;
		}

		LocalSettings s = LocalSettings.getOrNull(n);
		if(s != null)
		{
			String k = name + SFX_SETTINGS;
			s.saveValues(k, store);
		}
		
		storeNodeLocal(n, name);
	}
	
	
	protected void storeNodeLocal(Node n, String name)
	{
		if(n instanceof CheckBox cb)
		{
			storeCheckBox(cb, name);
		}
		else if(n instanceof ComboBox cb)
		{
			storeComboBox(cb, name);
		}
		else if(n instanceof ListView v)
		{
			storeListView(v, name);
		}
		else if(n instanceof SplitPane sp)
		{
			storeSplitPane(sp, name);
		}
		else if(n instanceof ScrollPane sp)
		{
			storeNode(sp.getContent());
		}
		else if(n instanceof TitledPane tp)
		{
			storeTitledPane(tp, name);
		}
		else if(n instanceof TableView t)
		{
			storeTableView(t, name);
		}
		else if(n instanceof TabPane t)
		{
			storeTabPane(t, name);
		}
		else
		{
			if(n instanceof Parent p)
			{
				for(Node ch: p.getChildrenUnmodifiable())
				{
					storeNode(ch);
				}
			}
		}
	}
	
	
	public void restoreNode(Node n)
	{
		if(n == null)
		{
			return;
		}
		else if(FX.isSkipSettings(n))
		{
			return;
		}
		else if(handleNullScene(n))
		{
			return;
		}
		
		String name = computeName(n);
		if(name == null)
		{
			return;
		}

		LocalSettings s = LocalSettings.getOrNull(n);
		if(s != null)
		{
			String k = name + SFX_SETTINGS;
			s.loadValues(k, store);
		}
		
		restoreNodeLocal(n, name);
	}
	
	
	protected void restoreNodeLocal(Node n, String name)
	{
		if(n instanceof CheckBox cb)
		{
			restoreCheckBox(cb, name);
		}
		else if(n instanceof ComboBox cb)
		{
			restoreComboBox(cb, name);
		}
		else if(n instanceof ListView v)
		{
			restoreListView(v, name);
		}
		else if(n instanceof SplitPane sp)
		{
			restoreSplitPane(sp, name);
		}
		else if(n instanceof ScrollPane sp)
		{
			restoreNode(sp.getContent());
		}
		else if(n instanceof TitledPane tp)
		{
			restoreTitledPane(tp, name);
		}
		else if(n instanceof TableView t)
		{
			restoreTableView(t, name);
		}
		else if(n instanceof TabPane t)
		{
			restoreTabPane(t, name);
		}
		else
		{
			if(n instanceof Parent p)
			{
				for(Node ch: p.getChildrenUnmodifiable())
				{
					restoreNode(ch);
				}
			}
		}
	}
	

	protected boolean handleNullScene(Node node)
	{
		if(node == null)
		{
			return true;
		}
		else if(node.getScene() == null)
		{
			node.sceneProperty().addListener(new ChangeListener<Scene>()
			{
				@Override
				public void changed(ObservableValue<? extends Scene> src, Scene old, Scene sc)
				{
					if(sc != null)
					{
						Window w = sc.getWindow();
						if(w != null)
						{
							node.sceneProperty().removeListener(this);
							restoreNode(node);
						}
					}
				}
			});
			return true;
		}
		return false;
	}


	protected String computeName(Node n)
	{
		WindowMonitor m = WindowMonitor.forNode(n);
		if(m != null)
		{
			SB sb = new SB();
			if(collectNames(sb, n))
			{
				String id = m.getID();
				return id + sb;
			}
		}
		return null;
	}


	// returns false if Node should be ignored
	// n is not null
	protected boolean collectNames(SB sb, Node n)
	{
		if(n instanceof MenuBar)
		{
			return false;
		}
		else if(n instanceof Shape)
		{
			return false;
		}
		else if(n instanceof ImageView)
		{
			return false;
		}

		Parent p = n.getParent();
		if(p != null)
		{
			if(!collectNames(sb, p))
			{
				return false;
			}
		}

		String name = getNodeName(n);
		if(name == null)
		{
			return false;
		}

		sb.append('.');
		sb.append(name);
		return true;
	}


	protected String getNodeName(Node n)
	{
		String name = FX.getName(n);
		if(name != null)
		{
			return name;
		}

		if(n instanceof Pane)
		{
			if(n instanceof AnchorPane)
			{
				return "AnchorPane";
			}
			else if(n instanceof BorderPane)
			{
				return "BorderPane";
			}
			else if(n instanceof CPane)
			{
				return "CPane";
			}
			else if(n instanceof DialogPane)
			{
				return "DialogPane";
			}
			else if(n instanceof FlowPane)
			{
				return "FlowPane";
			}
			else if(n instanceof GridPane)
			{
				return "GridPane";
			}
			else if(n instanceof HBox)
			{
				return "HBox";
			}
			else if(n instanceof StackPane)
			{
				return "StackPane";
			}
			else if(n instanceof TextFlow)
			{
				return null;
			}
			else if(n instanceof TilePane)
			{
				return "TilePane";
			}
			else if(n instanceof VBox)
			{
				return "VBox";
			}
			else
			{
				return "Pane";
			}
		}
		else if(n instanceof Control)
		{
			return n.getClass().getSimpleName();
		}
		else if(n instanceof Group)
		{
			return "Group";
		}
		else if(n instanceof Region)
		{
			return "Region";
		}
		return null;
	}
	

	protected void storeCheckBox(CheckBox n, String name)
	{
		boolean sel = n.isSelected();
		store.setBoolean(FX_PREFIX + name, sel);
	}


	protected void restoreCheckBox(CheckBox n, String name)
	{
		Boolean sel = store.getBoolean(FX_PREFIX + name);
		if(sel != null)
		{
			n.setSelected(sel);
		}
	}


	protected void storeComboBox(ComboBox n, String name)
	{
		if(n.getSelectionModel() != null)
		{
			int ix = n.getSelectionModel().getSelectedIndex();
			if(ix >= 0)
			{
				store.setInt(FX_PREFIX + name, ix);
			}
		}
	}


	protected void restoreComboBox(ComboBox n, String name)
	{
		if(n.getSelectionModel() != null)
		{
			int ix = store.getInt(FX_PREFIX + name, -1);
			if((ix >= 0) && (ix < n.getItems().size()))
			{
				n.getSelectionModel().select(ix);
			}
		}
	}


	protected void storeListView(ListView n, String name)
	{
		if(n.getSelectionModel() != null)
		{
			int ix = n.getSelectionModel().getSelectedIndex();
			if(ix >= 0)
			{
				store.setInt(FX_PREFIX + name, ix);
			}
		}
	}


	protected void restoreListView(ListView n, String name)
	{
		if(n.getSelectionModel() != null)
		{
			int ix = store.getInt(FX_PREFIX + name, -1);
			if((ix >= 0) && (ix < n.getItems().size()))
			{
				n.getSelectionModel().select(ix);
			}
		}
	}


	protected void storeSplitPane(SplitPane sp, String name)
	{
		double[] div = sp.getDividerPositions();
		SStream ss = new SStream();
		ss.add(div.length);
		ss.addAll(div);
		store.setStream(FX_PREFIX + name + SFX_DIVIDERS, ss);

		for(Node ch: sp.getItems())
		{
			storeNode(ch);
		}
	}
	

	protected void restoreSplitPane(SplitPane sp, String name)
	{
		SStream ss = store.getStream(FX_PREFIX + name + SFX_DIVIDERS);
		if(ss != null)
		{
			int sz = ss.nextInt(-1);
			if(sz > 0)
			{
				double[] divs = new double[sz];
				for(int i=0; i<sz; i++)
				{
					double v = ss.nextDouble(-1);
					if(v < 0)
					{
						return;
					}
					divs[i] = v;
				}
				// FIX must run later because of FX split pane inability to set divider positions exactly
				// it's likely a bug in SplitPane
				sp.setDividerPositions(divs);
				FX.later(() ->
				{
					sp.setDividerPositions(divs);
				});
			}
		}

		for(Node ch: sp.getItems())
		{
			restoreNode(ch);
		}
	}
	
	
	protected void storeTableView(TableView t, String name)
	{
		ObservableList<TableColumn<?,?>> cs = t.getColumns();
		int sz = cs.size();
		ObservableList<TableColumn<?,?>> sorted = t.getSortOrder();
		
		// FIX hash of column name instead of id!
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
		store.setStream(FX_PREFIX + name + SFX_COLUMNS, s);
		
		// selection
		int ix = t.getSelectionModel().getSelectedIndex();
		if(ix >= 0)
		{
			store.setInt(FX_PREFIX + name + SFX_SELECTION, ix);
		}
	}
	
	
	protected void restoreTableView(TableView t, String name)
	{
		ObservableList<TableColumn<?,?>> cs = t.getColumns();
		
		// columns
		SStream ss = store.getStream(FX_PREFIX + name + SFX_COLUMNS);
		if(ss != null)
		{
			int sz = ss.nextInt();
			if(sz == cs.size())
			{
				for(int i=0; i<sz; i++)
				{
					TableColumn<?,?> c = cs.get(i);
					
					String id = ss.nextString();
					double w = ss.nextDouble();
					int sortOrder = ss.nextInt();
					
					// TODO
				}
			}
		}
		
		int ix = store.getInt(FX_PREFIX + name + SFX_SELECTION, -1);
		if(ix >= 0)
		{
			// TODO
		}
	}


	protected void storeTabPane(TabPane p, String name)
	{
		// selection
		int ix = p.getSelectionModel().getSelectedIndex();
		store.setInt(FX_PREFIX + name + SFX_SELECTION, ix);

		// content
		var sm = p.getSelectionModel();
		if(sm != null)
		{
			var item = sm.getSelectedItem();
			if(item != null)
			{
				storeNode(item.getContent());
			}
		}
	}


	protected void restoreTabPane(TabPane p, String name)
	{
		// selection
		int ix = store.getInt(FX_PREFIX + name + SFX_SELECTION, -1);
		if(ix >= 0)
		{
			if(ix < p.getTabs().size())
			{
				p.getSelectionModel().select(ix);
			}
		}

		// content
		var sm = p.getSelectionModel();
		if(sm != null)
		{
			var item = sm.getSelectedItem();
			if(item != null)
			{
				restoreNode(item.getContent());
			}
		}
	}
	
	
	protected void storeTitledPane(TitledPane p, String name)
	{
		store.setBoolean(FX_PREFIX + name + SFX_EXPANDED, p.isExpanded());
		
		storeNode(p.getContent());
	}
	
	
	protected void restoreTitledPane(TitledPane p, String name)
	{
		boolean expanded = store.getBoolean(FX_PREFIX + name + SFX_EXPANDED, true);
		p.setExpanded(expanded);		

		restoreNode(p.getContent());
	}
	
	
	/**
	 * Opens all previously opened windows using the specified generator.
	 * Open a default window when no windows has been opened from the settings.
	 */
	public int openLayout()
	{
		// ensure WinMonitor is initialized 
		WindowMonitor.forWindow(null);
		
		// numEntries,name,id,... in reverse order 
		SStream st = store.getStream(FX_PREFIX + SFX_WINDOWS);
		int count = 0;

		int numEntries = st.nextInt(-1);
		if(numEntries > 0)
		{
			for(int i=0; i<numEntries; i++)
			{
				String name = st.nextString();
				String id = st.nextString();
				Stage w = createWindow(name);
				if(w != null)
				{
					// ensure that the window monitor is created with the right id
					WindowMonitor m = WindowMonitor.forWindow(w, id);
					
					loadWindowContent(m, w);

					if(!w.isShowing())
					{
						w.show();
					}
					
					count++;
				}
			}
		}
		
		if(count == 0)
		{
			Stage w = createDefaultWindow();
			if(!w.isShowing())
			{
				w.show();
			}
			count++;
		}
		
		return count;
	}
	
	
	public void storeLayout()
	{
		log.debug();

		SStream ss = new SStream();
		List<Window> ws = WindowMonitor.getWindowStack();
		
		int sz = ws.size();
		ss.add(sz);
		
		for(int i=0; i<sz; i++)
		{
			Window w = ws.get(i);
			FxFramework.store(w);
			
			String name = FX.getName(w);
			String id = WindowMonitor.forWindow(w).getIDPart();
			
			ss.add(name);
			ss.add(id);
		}

		store.setStream(FX_PREFIX + SFX_WINDOWS, ss);

		store.save();
	}


	public void save()
	{
		store.save();
	}
	
	
	protected ASettingsStore store()
	{
		return store;
	}
}
