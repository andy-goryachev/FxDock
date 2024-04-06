// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.log.Log;
import goryachev.fx.FxAction;
import goryachev.fx.FxSettings;
import goryachev.fx.FxWindow;
import goryachev.fx.OnWindowClosing;
import javafx.application.Platform;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * WindowMgr manages windows to:
 * - keep track of last focused window
 * - proper shutdown
 * - launching multiple windows from settings
 */
// TODO replace with ClosingOperation (AUTOCLOSE)
@Deprecated
public class WindowMgr
{
	protected static final Log log = Log.get("WindowMgr");
	// TODO ClosingOperation
	private static boolean exiting;
	
	
//	public static void init()
//	{
//		// TODO flag to make sure it's called only once
//		FX.addChangeListener(Window.getWindows(), (ch) ->
//		{
//			while(ch.next())
//			{
//				if(ch.wasAdded())
//				{
//					for(Window w: ch.getAddedSubList())
//					{
//						FxSchema.restoreWindow(w);
//						applyStyleSheet(w);
//					}
//				}
//				else if(ch.wasRemoved())
//				{
//					for(Window w: ch.getRemoved())
//					{
//						FxSchema.storeWindow(w);
//					}
//					
//					GlobalSettings.save();
//				}
//			}
//		});
//	}
	
	
	// TODO
//	private static int getEssentialWindowCount()
//	{
//		int ct = 0;
//		for(int i=windowStack.size()-1; i>=0; --i)
//		{
//			FxWindow w = windowStack.get(i);
//			if(w == null)
//			{
//				windowStack.remove(i);
//			}
//			else
//			{
//				if(w.isShowing() && w.isEssentialWindow())
//				{
//					ct++;
//				}
//			}
//		}
//		return ct;
//	}
	
	
//	protected int getFxWindowCount()
//	{
//		int ct = 0;
//		for(Window w: Window.getWindows())
//		{
//			if(w instanceof FxWindow)
//			{
//				if(w.isShowing())
//				{
//					ct++;
//				}
//			}
//		}
//		return ct;
//	}
	
	
	private static boolean confirmExit()
	{
		OnWindowClosing choice = new OnWindowClosing(true);
		// TODO in focus order?
		for(Window w: Window.getWindows())
		{
			if(w instanceof FxWindow fw)
			{
				fw.confirmClosing(choice);
	
				if(choice.isCancelled())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	
	public static void exit()
	{
		FxSettings.storeLayout();
		
		if(confirmExit())
		{
			exitPrivate();
		}
	}

	
	public static FxAction exitAction()
	{
		return new FxAction(() -> exit());
	}
	
	
	private static void exitPrivate()
	{
		exiting = true;
		// calls Application.close()
		Platform.exit();
	}
	
	
//	protected FxWindow getFxWindow(Node n)
//	{
//		Scene sc = n.getScene();
//		if(sc != null)
//		{
//			Window w = sc.getWindow();
//			if(w instanceof FxWindow)
//			{
//				return (FxWindow)w;
//			}
//		}
//		return null;
//	}
	

	// TODO
//	public void open(FxWindow w)
//	{
//		if(w.isShowing())
//		{
//			return;
//		}
//		
//		w.setOnCloseRequest((ev) -> handleClose(w, ev));
//		
//		w.showingProperty().addListener((src,old,cur) ->
//		{
//			if(!cur)
//			{
//				unlinkWindow(w);
//			}
//		});
//		
//		addWindow(w);
//		restoreWindow(w);
//		
//		applyStyleSheet(w);
//		
//		switch(w.getModality())
//		{
//		case APPLICATION_MODAL:
//		case WINDOW_MODAL:
//			w.showAndWait();
//			break;
//		default:
//			w.show();	
//		}
//	}
	
	
//	protected void unlinkWindow(FxWindow w)
//	{
//		if(!exiting)
//		{
//			if(!(w instanceof FxDialog))
//			{
//				if(getFxWindowCount() == 1)
//				{
//					storeWindows();
//				}
//			}
//		}
//		
//		storeWindow(w);
//		GlobalSettings.save();
//
//		Object id = windows.remove(w);
//		if(id instanceof String)
//		{
//			windows.remove(id);
//		}
//		
//		if(getEssentialWindowCount() == 0) // FIX
//		{
//			exitPrivate();
//		}
//	}
	
	
	// TODO
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
//	public void addFocusListener(FxWindow w)
//	{
//		w.focusedProperty().addListener((src,old,v) ->
//		{
//			if(v)
//			{
//				onWindowFocused(w);
//			}
//		});
//	}
	
	
//	protected void onWindowFocused(FxWindow win)
//	{
//		int ix = 0;
//		while(ix < windowStack.size())
//		{
//			FxWindow w = windowStack.get(ix);
//			if((w == null) || (w == win))
//			{
//				windowStack.remove(ix);
//			}
//			else
//			{
//				ix++;
//			}
//		}
//		windowStack.add(win);
//	}
	
	
//	public FxWindow findTopWindow(List<FxWindow> ws)
//	{
//		int sz = ws.size();
//		for(int i=windowStack.size()-1; i>=0; --i)
//		{
//			FxWindow w = windowStack.get(i);
//			if(w == null)
//			{
//				windowStack.remove(i);
//			}
//			else
//			{
//				for(int j=0; j<sz; j++)
//				{
//					if(w == ws.get(j))
//					{
//						return w;
//					}
//				}
//			}
//		}
//		return null;
//	}
}
