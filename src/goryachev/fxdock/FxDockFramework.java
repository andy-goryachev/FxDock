// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.common.util.WeakList;
import goryachev.fxdock.internal.FxDockSchema;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;


/**
 * Docking Framework for JavaFX.
 */
public class FxDockFramework
{
	/** a generator must be plugged into the framework to provide custom windows and panes */ 
	public static interface Generator
	{
		public FxDockWindow createWindow();
		
		public FxDockPane createPane(String type);
	}
	
	//
	
	protected static final Log log = Log.get("FxDockFramework");
	private static final CMap<Object,Object> windows = new CMap<>();
	/** window stack: top window first */
	private static final WeakList<FxDockWindow> windowStack = new WeakList<>();
	private static Generator generator;
	
	
	/** generator allows for creation of custom docking Stages and docking Panes */ 
	public static void setGenerator(Generator g)
	{
		generator = g;
	}
	
	
	public static FxDockWindow createWindow()
	{
		return  generator().createWindow();
	}
	
	
	public static FxDockPane createPane(String type)
	{
		return  generator().createPane(type);
	}
	
	
	private static Generator generator()
	{
		if(generator == null)
		{
			throw new Error("Please configure generator");
		}
		return generator;
	}
	
	
	public static int loadLayout()
	{
		int ct = FxDockSchema.getWindowCount();
		for(int i=ct-1; i>=0; i--)
		{
			try
			{
				FxDockWindow w = createWindow();
				String id = FxDockSchema.windowID(i);
				
				FxDockSchema.restoreWindow(id, w);
				
				Node n = FxDockSchema.loadLayout(id);
				w.setContent(n);

				openPrivate(w, id);
				
				w.loadSettings(id);
				
				w.show();
			}
			catch(Exception e)
			{
				log.err(e);
			}
		}
		return ct;
	}
	
	
	public static void saveLayout()
	{
		List<FxDockWindow> ws = getWindows();
		FxDockSchema.setWindowCount(ws.size());
		
		for(FxDockWindow w: ws)
		{
			storeWindow(w);
		}
		
		GlobalSettings.save();
	}
	
	
	private static void storeWindow(FxDockWindow w)
	{
		String id = getWindowID(w);
		FxDockSchema.saveLayout(id, w.getContent());
		w.saveSettings(id);
		FxDockSchema.storeWindow(id, w);
	}
	

	public static void open(FxDockWindow w)
	{
		String id = newWindowID();
		openPrivate(w, id);
		w.show();
	}
	
	
	private static void openPrivate(FxDockWindow w, String id)
	{
		windows.put(id, w);
		windows.put(w, id);
		
		w.showingProperty().addListener((src,old,cur) ->
		{
			if(!cur)
			{
				unlinkWindow(w, id);
			}
		});
	}
	
	
	private static void unlinkWindow(FxDockWindow w, String id)
	{
		if(getWindowCount() == 1)
		{
			// save the last window
			saveLayout();
			
			if(confirmExit())
			{
				exitPrivate();
			}
		}
		else
		{
			windows.remove(w);
			windows.remove(id);
			
			saveLayout();
		}
	}
	
	
	private static String getWindowID(FxDockWindow w)
	{
		return (String)windows.get(w);
	}
	
	
	private static String newWindowID()
	{
		int i = 0;
		for( ; i<100000; i++)
		{
			String id = FxDockSchema.windowID(i);
			if(!windows.containsKey(id))
			{
				return id;
			}
		}
		throw new Error("?" + i);
	}
	
	
	public static int getWindowCount()
	{
		int ct = 0;
		for(Object x: windows.keySet())
		{
			if(x instanceof FxDockWindow)
			{
				ct++;
			}
		}
		return ct;
	}
	
	
	// FX cannot tell us which window is on top, so we have to do the dirty work ourselves
	protected static void addFocusListener(FxDockWindow w)
	{
		w.focusedProperty().addListener((src,old,v) ->
		{
			if(v)
			{
				onWindowFocused(w);
			}
		});
	}
	
	
	private static void onWindowFocused(FxDockWindow win)
	{
		int ix = 0;
		while(ix < windowStack.size())
		{
			FxDockWindow w = windowStack.get(ix);
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
	
	
	public static FxDockWindow findTopWindow(List<FxDockWindow> ws)
	{
		int sz = ws.size();
		for(int i=windowStack.size()-1; i>=0; --i)
		{
			FxDockWindow w = windowStack.get(i);
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
	
	
	/** returns a list of windows, topmost window first */
	public static List<FxDockWindow> getWindows()
	{
		int sz = windowStack.size();
		CList<FxDockWindow> rv = new CList(sz);
		for(int i=0; i<sz; i++)
		{
			FxDockWindow w = windowStack.get(i);
			if(w != null)
			{
				rv.add(w);
			}
		}
		return rv;
	}
	
	
	private static boolean confirmExit()
	{
		// TODO need to add confirmation step for each window (e.g. if modified),
		// as well as handle bulk operations such as "Save All" and "Ignore All"
		return true;
	}
	
	
	public static void exit()
	{
		saveLayout();
		
		if(confirmExit())
		{
			exitPrivate();
		}
	}
	
	
	private static void exitPrivate()
	{
		Platform.exit();
		System.exit(0);
	}
}
