// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.common.util.WeakList;
import goryachev.fx.CAction;
import goryachev.fx.FxWindow;
import goryachev.fx.OnWindowClosing;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.WindowEvent;


/**
 * WindowsFx deals with managing FxWindows and their settings.
 */
public class WindowsFx
{
	/** in the focus order */
	protected final WeakList<FxWindow> windowStack = new WeakList<>();
	/** prefix->window and window->prefix */
	protected final CMap<Object,Object> windows = new CMap<>();
	/** window open callbacks */
	protected final CList<Consumer<FxWindow>> monitors = new CList<>();
	
	
	/** returns a list of vidible windows, topmost window first */
	public List<FxWindow> getWindows()
	{
		int sz = windowStack.size();
		CList<FxWindow> rv = new CList<>(sz);
		for(int i=0; i<sz; i++)
		{
			FxWindow w = windowStack.get(i);
			if(w != null)
			{
				if(w.isShowing())
				{
					rv.add(w);
				}
			}
		}
		return rv;
	}
	
	
	public int getWindowCount()
	{
		int ct = 0;
		for(int i=windowStack.size()-1; i>=0; --i)
		{
			FxWindow w = windowStack.get(i);
			if(w == null)
			{
				windowStack.remove(i);
			}
			else
			{
				if(w.isShowing())
				{
					ct++;
				}
			}
		}
		return ct;
	}
	
	
	protected boolean confirmExit()
	{
		OnWindowClosing choice = new OnWindowClosing(true);
		for(FxWindow w: getWindows())
		{
			w.confirmClosing(choice);

			if(choice.isCancelled())
			{
				return false;
			}
		}
		return true;
	}
	
	
	public void exit()
	{
		storeSettings();
		
		if(confirmExit())
		{
			exitPrivate();
		}
	}
	
	
	public void storeSettings()
	{
		for(FxWindow w: getWindows())
		{
			storeWindow(w);
		}
		
		GlobalSettings.save();		
	}
	
	
	public CAction exitAction()
	{
		return new CAction(this::exit);
	}
	
	
	protected void exitPrivate()
	{
		Platform.exit();
		System.exit(0);
	}

	
	public void storeWindow(FxWindow w)
	{
		String prefix = lookupPrefix(w);
		w.storeSettings(prefix);
		FxSchema.storeWindow(w, prefix);
		FxSchema.storeNode(prefix, w.getScene().getRoot());
	}
	
	
	public void restoreWindow(FxWindow w)
	{
		String prefix = lookupPrefix(w);
		w.loadSettings(prefix);
		FxSchema.restoreWindow(w, prefix);
		FxSchema.restoreNode(prefix, w.getScene().getRoot());
	}
	

	public void open(FxWindow w)
	{
		if(w.isShowing())
		{
			// design error: you should use open() instead of show()
			throw new Error();
		}
		
		w.setOnCloseRequest((ev) -> handleClose(w, ev));
		
		w.showingProperty().addListener((src,old,cur) ->
		{
			if(!cur)
			{
				unlinkWindow(w);
			}
		});
		
		addWindow(w);
		restoreWindow(w);
		
		try
		{
			if(monitors.size() > 0)
			{
				for(Consumer<FxWindow> m: monitors)
				{
					m.accept(w);
				}
			}
		}
		catch(Exception e)
		{
			Log.ex(e);
		}
		
		w.show();
	}
	
	
	protected void unlinkWindow(FxWindow w)
	{
		storeWindow(w);
		GlobalSettings.save();

		Object id = windows.remove(w);
		if(id instanceof String)
		{
			windows.remove(id);
		}
		
		if(getWindowCount() == 0)
		{
			exitPrivate();
		}
	}

	
	public void close(FxWindow w)
	{
		w.hide();
	}
	
	
	protected void handleClose(FxWindow w, WindowEvent ev)
	{
		OnWindowClosing ch = new OnWindowClosing(false);
		w.confirmClosing(ch);
		if(ch.isCancelled())
		{
			// don't close the window
			ev.consume();
		}
	}
	
	
	// FX cannot tell us which window is on top, so we have to do the dirty work ourselves
	public void addFocusListener(FxWindow w)
	{
		w.focusedProperty().addListener((src,old,v) ->
		{
			if(v)
			{
				onWindowFocused(w);
			}
		});
	}
	
	
	protected void onWindowFocused(FxWindow win)
	{
		int ix = 0;
		while(ix < windowStack.size())
		{
			FxWindow w = windowStack.get(ix);
			if((w == null) || (w == win))
			{
				windowStack.remove(ix);
			}
			else
			{
				ix++;
			}
		}
		windowStack.add(win);
	}
	
	
	public FxWindow findTopWindow(List<FxWindow> ws)
	{
		int sz = ws.size();
		for(int i=windowStack.size()-1; i>=0; --i)
		{
			FxWindow w = windowStack.get(i);
			if(w == null)
			{
				windowStack.remove(i);
			}
			else
			{
				for(int j=0; j<sz; j++)
				{
					if(w == ws.get(j))
					{
						return w;
					}
				}
			}
		}
		return null;
	}
	
	
	protected String addWindow(FxWindow w)
	{
		String name = w.getName();
		int i = 0;
		String prefix = "";
		
		// small limit ensures quick return at the price of forgetting settings for that many windows
		for(; i<10000; i++)
		{
			prefix = name + "." + i;
			if(!windows.containsKey(prefix))
			{
				break;
			}
		}
		
		windows.put(w, prefix);
		windows.put(prefix, w);
		
		onWindowFocused(w);
		
		return prefix;
	}

	
	protected String lookupPrefix(FxWindow w)
	{
		Object x = windows.get(w);
		if(x instanceof String)
		{
			return (String)x;
		}
		else
		{
			return addWindow(w);
		}
	}


	public LocalBindings bindings(Node n, boolean create)
	{
		return FxSchema.bindings(n, create);
	}
	
	
	/** adds a callback which will be invoked before any FxWindow gets shown */
	public void addWindowMonitor(Consumer<FxWindow> m)
	{
		monitors.add(m);
	}
	
	
	/** removes a window monitor */
	public void removeWindowMonitor(Consumer<FxWindow> m)
	{
		monitors.remove(m);
	}
}
