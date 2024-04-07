// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import goryachev.common.util.CSet;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.CssLoader;
import goryachev.fx.FX;
import goryachev.fx.FxFramework;
import goryachev.fx.FxObject;
import goryachev.fx.util.FxTools;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * Window Monitor.
 * Remembers the location/size and attributes of windows.
 * Keeps track of Z order of open windows.
 */
public class WindowMonitor
{
	private static final Log log = Log.get("WindowMonitor");
	private static final String SEPARATOR = "_";
	private static final Object WIN_MONITOR_PROP = new Object();
	/** in reverse order: top window is last */
	private static final CList<Window> stack = new CList<>();
	private final static FxObject<Node> lastFocusOwner = new FxObject<>();
	static { init(); }
	
	private final Window window;
	private final String id;
	private double x;
	private double y;
	private double w;
	private double h;
	private double xNorm;
	private double yNorm;
	private double wNorm;
	private double hNorm;


	public WindowMonitor(Window win, String id)
	{
		this.window = win;
		this.id = id;

		x = win.getX();
		y = win.getY();
		w = win.getWidth();
		h = win.getHeight();
		
		win.sceneProperty().addListener((src,prev,cur) ->
		{
			if(cur != null)
			{
				// TODO remove listener!
				cur.focusOwnerProperty().addListener((s,p,n) ->
				{
					updateFocusOwner(n);
				});
			}
		});
		
		win.focusedProperty().addListener((src,prev,cur) ->
		{
			if(cur)
			{
				updateFocusedWindow(win);
			}
		});

		win.xProperty().addListener((p) ->
		{
			xNorm = x;
			x = win.getX();
		});
		
		win.yProperty().addListener((p) ->
		{
			yNorm = y;
			y = win.getY();
		});
		
		win.widthProperty().addListener((p) ->
		{
			wNorm = w;
			w = win.getWidth();
		});
		
		win.heightProperty().addListener((p) ->
		{
			hNorm = h;
			h = win.getHeight();
		});
		
		if(win instanceof Stage s)
		{
			s.iconifiedProperty().addListener((p) ->
			{
				if(s.isIconified())
				{
					x = xNorm;
					y = yNorm;
				}
			});

			s.maximizedProperty().addListener((p) ->
			{
				if(s.isMaximized())
				{
					x = xNorm;
					y = yNorm;
				}
			});

			s.fullScreenProperty().addListener((p) ->
			{
				if(s.isFullScreen())
				{
					x = xNorm;
					y = yNorm;
					w = wNorm;
					h = hNorm;
				}
			});
		}
	}
	
	
	private static void init()
	{
		FX.addChangeListener(Window.getWindows(), (ch) ->
		{
			while(ch.next())
			{
				if(ch.wasAdded())
				{
					for(Window w: ch.getAddedSubList())
					{
						log.debug("added: %s", w);
						// window is already showing
						FxFramework.restore(w);
						applyStyleSheet(w);
					}
				}
				else if(ch.wasRemoved())
				{
					for(Window w: ch.getRemoved())
					{
						log.debug("removed: %s", w);
						// the only problem here is that window is already hidden - does it matter?
						// if it does, need to listen to WindowEvent.WINDOW_HIDING event
						FxFramework.store(w);
						stack.remove(w);
					}
					
					GlobalSettings.save();
				}
			}
		});
		stack.addAll(Window.getWindows());
		dumpStack();
	}
	
	
	private static void dumpStack()
	{
		log.debug(() ->
		{
			return stack.stream().
				map((w) -> FxTools.describe(w)).
				toList().
				toString();
		});
	}
	
	
	public Window getWindow()
	{
		return window;
	}
	
	
	public String getID()
	{
		return id;
	}


	private static String createID(Window win, String useID)
	{
		String name = FX.getName(win);
		if(name != null)
		{
			// collect existing ids
			CSet<String> ids = new CSet<>();
			for(Window w: Window.getWindows())
			{
				if(w != win)
				{
					WindowMonitor m = get(w);
					if(m != null)
					{
						String id = m.getID();
						if(id.startsWith(name))
						{
							ids.add(id);
						}
					}
				}
			}
			
			if(useID != null)
			{
				// check if this combination does not exist already
				String id = name + SEPARATOR + useID;
				if(ids.contains(id))
				{
					// this should not happen if FxSettings.openLayout() is called once at the launch
					throw new Error("duplicate id:" + id);
				}
				return id;
			}

			for(int i=0; i<200_000; i++)
			{
				String id = name + SEPARATOR + i;
				if(!ids.contains(id))
				{
					return id;
				}
			}
		}
		return null;
	}
	
	
	/** strips the window name and the separator, returning the id part only */
	public String getIDPart()
	{
		int ix = id.lastIndexOf(SEPARATOR);
		if(ix < 0)
		{
			throw new Error("no id: " + id);
		}
		return id.substring(ix + 1);
	}
	
	
	public static WindowMonitor forWindow(Window w)
	{
		return forWindow(w, null);
	}


	public static WindowMonitor forWindow(Window w, String useID)
	{
		if(w != null)
		{
			if(w instanceof Tooltip)
			{
				return null;
			}
			else if(w instanceof ContextMenu)
			{
				return null;
			}
			
			WindowMonitor m = get(w);
			if(m == null)
			{
				String id = createID(w, useID);
				if(id != null)
				{
					m = new WindowMonitor(w, id);
					set(w, m);
				}
			}
			return m;
		}
		return null;
	}
	
	
	private static WindowMonitor get(Window w)
	{
		Object x = w.getProperties().get(WIN_MONITOR_PROP);
		if(x instanceof WindowMonitor m)
		{
			return m;
		}
		return null;
	}

	
	private static void set(Window w, WindowMonitor m)
	{
		w.getProperties().put(WIN_MONITOR_PROP, m);
	}

	
	public static WindowMonitor forNode(Node n)
	{
		Scene s = n.getScene();
		if(s != null)
		{
			Window w = s.getWindow();
			if(w != null)
			{
				return forWindow(w);
			}
		}
		return null;
	}


	public double getX()
	{
		return x;
	}
	
	
	public double getY()
	{
		return y;
	}
	
	
	public double getW()
	{
		return w;
	}
	
	
	public double getH()
	{
		return h;
	}
	
	
	private static void applyStyleSheet(Window w)
	{
		try
		{
			String style = CssLoader.getCurrentStyleSheet();
			FX.applyStyleSheet(w, null, style);
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}
	

	/**
	 * returns the list of windows in the reverse order - the top window is last.
	 */
	public static List<Window> getWindowStack()
	{
		return new CList<>(stack);
	}
	
	
	/**
	 * Finds a topmost window in the supplied list.
	 */ 
	public static <W extends Window> W findTopWindow(List<W> list)
	{
		for(int i=stack.size() - 1; i>=0; i--)
		{
			Window w = stack.get(i);
			if(list.contains(w))
			{
				return (W)w;
			}
		}
		return null;
	}
	
	
	static void updateFocusedWindow(Window w)
	{
		log.debug(w);
		stack.remove(w);
		stack.add(w);
		dumpStack();
	}
	
	
	static void updateFocusOwner(Node n)
	{
		if(n != null)
		{
			log.debug(n);
			lastFocusOwner.set(n);
		}
	}
	
	
	public static Node getLastFocusOwner()
	{
		return lastFocusOwner.get();
	}
	
	
	public static ReadOnlyObjectProperty<Node> lastFocusOwnerProperty()
	{
		return lastFocusOwner.getReadOnlyProperty();
	}
}
