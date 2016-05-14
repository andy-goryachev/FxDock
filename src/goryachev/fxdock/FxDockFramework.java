// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.common.util.CList;
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
				String prefix = FxDockSchema.windowID(i);
				
				FxDockSchema.restoreWindow(prefix, w);
				
				Node n = FxDockSchema.loadLayout(prefix);
				w.setContent(n);

				openPrivate(w);
				
				w.loadSettings(prefix);
				
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
		int ct = ws.size();
		
		FxDockSchema.setWindowCount(ct);
		
		for(int i=0; i<ct; i++)
		{
			FxDockWindow w = ws.get(i);
			// ensure proper z-order
			storeWindow(ct - i - 1, w);
		}
		
		GlobalSettings.save();
	}
	
	
	private static void storeWindow(int ix, FxDockWindow w)
	{
		String prefix = FxDockSchema.windowID(ix);
		FxDockSchema.saveLayout(prefix, w.getContent());
		w.saveSettings(prefix);
		FxDockSchema.storeWindow(prefix, w);
	}
	

	public static void open(FxDockWindow w)
	{
		openPrivate(w);
		w.show();
	}
	
	
	private static void openPrivate(FxDockWindow w)
	{
		w.showingProperty().addListener((src,old,cur) ->
		{
			if(!cur)
			{
				unlinkWindow(w);
			}
		});
	}
	
	
	private static void unlinkWindow(FxDockWindow w)
	{
		if(getWindowCount() == 1)
		{
			saveLayout();
			
			if(confirmExit())
			{
				exitPrivate();
			}
		}
		else
		{
			saveLayout();
		}
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
	
	
	public static int getWindowCount()
	{
		int ct = 0;
		for(int i=windowStack.size()-1; i>=0; --i)
		{
			FxDockWindow w = windowStack.get(i);
			if(w == null)
			{
				windowStack.remove(i);
			}
			else
			{
				ct++;
			}
		}
		return ct;
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
