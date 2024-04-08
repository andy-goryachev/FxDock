// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.FxSettingsSchema;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * FX Application Framework.
 */
public class FxFramework
{
	// TODO move name/ignore settings here?

	// TODO create an abstract base class ASettingsSchema (?) in the main package, not internal
	private static FxSettingsSchema schema;

	
	/** 
	 * Opens application windows stored in the global settings.
	 * If no settings are stored, invokes the generator with a null name to
	 * open the main window.<p>
	 * The generator must return a default window when supplied with a null name.
	 * To ensure the right settings are loaded, the newly created window must remain hidden. 
	 */ 
	public static int openLayout(FxSettingsSchema s)
	{
		if(schema == null)
		{
			schema = s;
		}
		else if(schema != s)
		{
			throw new Error("schema already set");
		}

		return schema.openLayout();
	}
	
	
	public static Stage createDefaultWindow()
	{
		return schema.createDefaultWindow();
	}
	
	
	public static FxSettingsSchema getSchema()
	{
		return schema;
	}
	
	
	public static void storeLayout()
	{
		schema.storeLayout();
	}
	
	
	public static void store(Node n)
	{
		if(n != null)
		{
			schema.storeNode(n);
			schema.save();
		}
	}
	
	
	public static void restore(Node n)
	{
		if(n != null)
		{
			schema.restoreNode(n);
		}
	}
	
	
	public static void store(Window w)
	{
		schema.storeWindow(w);
		schema.save();
	}
	
	
	public static void restore(Window w)
	{
		schema.restoreWindow(w);
	}
	
	
	public static void save()
	{
		schema.save();
	}
	
	
	// TODO
	// TODO replace with ClosingOperation (AUTOCLOSE)
	// TODO ClosingOperation
	private static boolean exiting;
	
	
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
		FxFramework.storeLayout();
		
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
}
