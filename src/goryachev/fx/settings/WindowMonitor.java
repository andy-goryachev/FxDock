// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.settings;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import goryachev.common.util.CSet;
import goryachev.fx.ClosingWindowOperation;
import goryachev.fx.FX;
import goryachev.fx.FxFramework;
import goryachev.fx.FxObject;
import goryachev.fx.ShutdownChoice;
import goryachev.fx.internal.CssLoader;
import goryachev.fx.util.FxTools;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * Window Monitor.
 * Remembers the location/size and attributes of windows.
 * Keeps track of Z order of open windows.
 */
public class WindowMonitor
{
	private static final Log log = Log.get("WindowMonitor");
	private static final String SEPARATOR = "_";
	private static final Object PROP_CLOSING = new Object();
	private static final Object PROP_MONITOR = new Object();
	private static final Object PROP_NON_ESSENTIAL = new Object();
	/** in reverse order: top window is last */
	private static final CList<Window> stack = new CList<>();
	private final static FxObject<Node> lastFocusOwner = new FxObject<>();
	private static boolean exiting;
	private static ShutdownChoice shutdownChoice;
	
	static { init(); }
	
	private final Window window;
	private final String id;
	private double x;
	private double y;
	private double width;
	private double height;
	private double xNorm;
	private double yNorm;
	private double widthNorm;
	private double heightNorm;


	public WindowMonitor(Window w, String id)
	{
		this.window = w;
		this.id = id;

		x = w.getX();
		y = w.getY();
		width = w.getWidth();
		height = w.getHeight();

		ChangeListener<Node> focusListener = (s,p,c) ->
		{
			updateFocusOwner(c);
		};

		// FIX does not work
		w.sceneProperty().addListener((src,prev,cur) ->
		{
			if(prev != null)
			{
				prev.focusOwnerProperty().removeListener(focusListener);
			}
			
			if(cur != null)
			{
				cur.focusOwnerProperty().addListener(focusListener);
			}
		});
		if(w.getScene() != null)
		{
			if(w.getScene().getFocusOwner() != null)
			{
				updateFocusOwner(w.getScene().getFocusOwner());
			}
		}
		
		w.focusedProperty().addListener((src,prev,cur) ->
		{
			if(cur)
			{
				updateFocusedWindow(w);
			}
		});

		w.xProperty().addListener((p) ->
		{
			xNorm = x;
			x = w.getX();
		});
		
		w.yProperty().addListener((p) ->
		{
			yNorm = y;
			y = w.getY();
		});
		
		w.widthProperty().addListener((p) ->
		{
			widthNorm = width;
			width = w.getWidth();
		});
		
		w.heightProperty().addListener((p) ->
		{
			heightNorm = height;
			height = w.getHeight();
		});
		
		if(w instanceof Stage s)
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
					width = widthNorm;
					height = heightNorm;
				}
			});
		}
	}
	
	
	private static void init()
	{
		FX.addChangeListener(Window.getWindows(), (ch) ->
		{
			boolean save = false;

			while(ch.next())
			{
				if(ch.wasAdded())
				{
					for(Window w: ch.getAddedSubList())
					{
						if(!isIgnore(w))
						{
							log.debug("added: %s", w);
							FxFramework.restore(w);
							applyStyleSheet(w);
						}
					}
				}
				else if(ch.wasRemoved())
				{
					for(Window w: ch.getRemoved())
					{
						if(!isIgnore(w))
						{
							log.debug("removed: %s", w);
							// the only problem here is that window is already hidden - does it matter?
							// if it does, need to listen to WindowEvent.WINDOW_HIDING event
							FxFramework.store(w);
							stack.remove(w);
							save = true;
						}
					}
				}
				
				if(save)
				{
					FxFramework.save();
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
			if(isIgnore(w))
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
	
	
	static boolean isIgnore(Window w)
	{
		if(w instanceof Tooltip)
		{
			return true;
		}
		else if(w instanceof ContextMenu)
		{
			return true;
		}
		return false;
	}
	
	
	private static WindowMonitor get(Window w)
	{
		Object x = w.getProperties().get(PROP_MONITOR);
		if(x instanceof WindowMonitor m)
		{
			return m;
		}
		return null;
	}

	
	private static void set(Window w, WindowMonitor m)
	{
		w.getProperties().put(PROP_MONITOR, m);
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
		return width;
	}
	
	
	public double getH()
	{
		return height;
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
	
	
	public static void setClosingWindowOperation(Window w, ClosingWindowOperation op)
	{
		if(op == null)
		{
			Object x = w.getProperties().remove(PROP_CLOSING);
			if(x instanceof CloseRequestListener old)
			{
				w.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, old);
			}
		}
		else
		{
			CloseRequestListener c = new CloseRequestListener(op);
			Object x = w.getProperties().put(PROP_CLOSING, c);
			if(x instanceof CloseRequestListener old)
			{
				w.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, old);
			}
			w.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, c);
		}
	}
	
	
	private static ClosingWindowOperation getClosingWindowOperation(Window w)
	{
		Object x = w.getProperties().get(PROP_CLOSING);
		if(x instanceof CloseRequestListener op)
		{
			return op.operation;
		}
		return null;
	}
	
	
	public static void setNonEssentialWindow(Window w)
	{
		w.getProperties().put(PROP_NON_ESSENTIAL, Boolean.TRUE);
	}
	
	
	private static boolean isNonEssentialWindow(Window w)
	{
		return Boolean.TRUE.equals(w.getProperties().get(PROP_NON_ESSENTIAL));
	}
	
	
	private static int countEssentialWindows(Object caller)
	{
		int count = 0;
		for(Window w: stack)
		{
			if((caller == w) || isNonEssentialWindow(w))
			{
				continue;
			}
			count++;
		}
		return count;
	}
	
	
	public static void exit()
	{
		if(!exiting)
		{
			exiting = true;
			shutdownChoice = ShutdownChoice.UNDEFINED;

			if(confirmExit())
			{
				doExit();
			}
			else
			{
				exiting = false;
			}
		}
	}
	
	
	private static void doExit()
	{
		Platform.exit();
		System.exit(0);
	}
	
	
	/** returns true when ok to exit */
	private static boolean confirmExit()
	{
		for(int i=stack.size()-1; i>=0; i--)
		{
			Window w = stack.get(i);
			ClosingWindowOperation op = getClosingWindowOperation(w);
			if(op != null)
			{
				int count = countEssentialWindows(null);
				boolean multiple = count > 1;
				ShutdownChoice rsp = op.confirmClosing(exiting, multiple, shutdownChoice);
				switch(rsp)
				{
				case DISCARD_ALL:
				case SAVE_ALL:
					shutdownChoice = rsp;
					break;
				case CONTINUE:
					break;
				case CANCEL:
				case UNDEFINED:
				default:
					return false;
				}
			}
		}
		return true;
	}
	
	
	private static class CloseRequestListener implements EventHandler<WindowEvent>
	{
		public final ClosingWindowOperation operation;
		
		
		public CloseRequestListener(ClosingWindowOperation op)
		{
			this.operation = op;
		}

		
		@Override
		public void handle(WindowEvent ev)
		{
			if(exiting)
			{
				// application exit, handled in exit()
				return;
			}
			
			// user or program closing the window
			shutdownChoice = ShutdownChoice.UNDEFINED;

			ShutdownChoice rsp = operation.confirmClosing(exiting, false, shutdownChoice);
			switch(rsp)
			{
			case CANCEL:
				ev.consume();
				return;
			}
			
			if(countEssentialWindows(ev.getSource()) == 0)
			{
				doExit();
			}
		}
	}
}
