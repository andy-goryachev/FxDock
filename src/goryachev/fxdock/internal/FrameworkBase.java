// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.WeakList;
import goryachev.fx.OnWindowClosing;
import goryachev.fx.internal.LocalSettings;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.WindowEvent;


/**
 * Docking Framework Implementation.
 */
public class FrameworkBase
{
	/** creates windows and panes */
	protected FxDockFramework.Generator generator;
	/** window stack: top window first */
	protected final WeakList<FxDockWindow> windowStack = new WeakList<>();
	protected static final Log log = Log.get("FxDockFramework");
	
	
	/** generator allows for creation of custom docking Stages and docking Panes */ 
	public void setGenerator(FxDockFramework.Generator g)
	{
		generator = g;
	}
	
	
	public FxDockWindow createWindow()
	{
		return generator().createWindow();
	}
	
	
	public FxDockPane createPane(String type)
	{
		return generator().createPane(type);
	}
	
	
	protected FxDockFramework.Generator generator()
	{
		if(generator == null)
		{
			throw new Error("Please configure generator");
		}
		return generator;
	}
	
	
	public int loadLayout()
	{
		int ct = FxDockSchema.getWindowCount();
		// restore in proper z order
		for(int i=ct-1; i>=0; i--)
		{
			try
			{
				FxDockWindow w = createWindow();
				String prefix = FxDockSchema.windowID(i);
				
				FxDockSchema.restoreWindow(prefix, w);
				registerWindow(w);
				
				Node n = FxDockSchema.loadLayout(prefix);
				w.setContent(n);
				
				LocalSettings settings = LocalSettings.find(w);
				if(settings != null)
				{
					String k = prefix + FxDockSchema.SUFFIX_BINDINGS;
					settings.loadValues(k);
				}
				
				FxDockSchema.loadContentSettings(prefix, w.getContent());
				
				w.show();
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		return ct;
	}
	
	
	public void saveLayout()
	{
		List<FxDockWindow> ws = getWindows();
		int ct = ws.size();
		
		FxDockSchema.clearSettings();
		FxDockSchema.setWindowCount(ct);
		
		for(int i=0; i<ct; i++)
		{
			FxDockWindow w = ws.get(i);
			// ensure proper z-order
			storeWindow(ct - i - 1, w);
		}
		
		GlobalSettings.save();
	}
	
	
	protected void storeWindow(int ix, FxDockWindow w)
	{
		String prefix = FxDockSchema.windowID(ix);
		
		FxDockSchema.saveLayout(prefix, w.getContent());
		
		LocalSettings settings = LocalSettings.get(w);
		if(settings != null)
		{
			String k = prefix + FxDockSchema.SUFFIX_BINDINGS;
			settings.saveValues(k);
		}
		
		FxDockSchema.saveContentSettings(prefix, w.getContent());
		
		FxDockSchema.storeWindow(prefix, w);
	}
	

	public void open(FxDockWindow w)
	{
		registerWindow(w);
		w.show();
	}
	
	
	protected void registerWindow(FxDockWindow w)
	{
		w.setOnCloseRequest((ev) -> handleClose(w, ev));
		
		w.showingProperty().addListener((src,old,cur) ->
		{
			if(!cur)
			{
				unlinkWindow(w);
			}
		});
		
		// FX cannot tell us which window is on top, so we have to do the dirty work ourselves
		w.focusedProperty().addListener((src,old,v) ->
		{
			if(v)
			{
				onWindowFocused(w);
			}
		});
	}
	
	
	protected void handleClose(FxDockWindow w, WindowEvent ev)
	{
		if(getWindowCount() == 1)
		{
			saveLayout();
		}
		
		OnWindowClosing ch = new OnWindowClosing(false);
		w.confirmClosing(ch);
		if(ch.isCancelled())
		{
			// don't close the window
			ev.consume();
		}
	}
	
	
	protected void unlinkWindow(FxDockWindow w)
	{
		int ct = getWindowCount();
		if(ct == 0)
		{
			exitPrivate();
		}
	}

	
	protected void onWindowFocused(FxDockWindow win)
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
	
	
	public FxDockWindow findTopWindow(List<FxDockWindow> ws)
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
	
	
	/** returns a list of vidible windows, topmost window first */
	public List<FxDockWindow> getWindows()
	{
		int sz = windowStack.size();
		CList<FxDockWindow> rv = new CList<>(sz);
		for(int i=0; i<sz; i++)
		{
			FxDockWindow w = windowStack.get(i);
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
			FxDockWindow w = windowStack.get(i);
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
		List<FxDockWindow> ws = getWindows(); 
		OnWindowClosing choice = new OnWindowClosing(ws.size() > 1);
		
		for(FxDockWindow w: ws)
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
		saveLayout();
		
		if(confirmExit())
		{
			exitPrivate();
		}
	}
	
	
	protected void exitPrivate()
	{
		Platform.exit();
		System.exit(0);
	}
}
