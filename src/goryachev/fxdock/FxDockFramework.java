// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.common.util.CList;
import goryachev.common.util.CLog;
import goryachev.common.util.CMap;
import goryachev.common.util.Log;
import goryachev.common.util.WeakList;
import goryachev.fxdock.internal.FxDockSchema;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;


/**
 * FxDock Framework.
 */
public class FxDockFramework
{
	public static interface Generator
	{
		public FxDockWindow createWindow(String type);
		
		public FxDockPane createPane(String type);
	}
	
	//
	
	protected static final CLog log = Log.get("FxDockFramework");
	private static final CMap<Object,Object> windows = new CMap<>();
	private static final WeakList<FxDockWindow> windowStack = new WeakList<>(); // top window last
	private static Generator generator;
	
	
	/** generator allows for creation of custom docking Stages and docking Panes */ 
	public static void setGenerator(Generator g)
	{
		generator = g;
	}
	
	
	public static FxDockWindow createWindow(String type)
	{
		return  generator().createWindow(type);
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
		for(int i=0; i<ct; i++)
		{
			try
			{
				String type = null; // TODO
				FxDockWindow w = createWindow(type);
				String id = FxDockSchema.windowID(i);
				
				Node n = FxDockSchema.restoreNode(id);
				w.setContent(n);
				
				FxDockSchema.restoreWindow(id, w);
				openPrivate(w, id);
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
			String id = getWindowID(w);
			FxDockSchema.storeNode(id, w.getContent());
			FxDockSchema.storeWindow(id, w);
		}
	}


	public static void open(FxDockWindow w)
	{
		String id = newWindowID();
		openPrivate(w, id);
	}
	
	
	private static void openPrivate(FxDockWindow w, String id)
	{
		windows.put(id, w);
		windows.put(w, id);
		
		w.showingProperty().addListener((src,old,cur) ->
		{
			if(!cur)
			{
				windows.remove(w);
				windows.remove(id);
			}
		});
		
		w.show();
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
	
	
	public static List<FxDockWindow> getWindows()
	{
		CList<FxDockWindow> rv = new CList(windows.size() / 2);
		for(Object x: windows.keySet())
		{
			if(x instanceof FxDockWindow)
			{
				rv.add((FxDockWindow)x);
			}
		}
		return rv;
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
	
	
	public static void exit()
	{
		// TODO confirm exiting
		
		saveLayout();
		
		Platform.exit();
	}
}
