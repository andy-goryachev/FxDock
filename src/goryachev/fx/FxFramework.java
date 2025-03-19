// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.CssLoader;
import goryachev.fx.settings.FxSettingsSchema;
import goryachev.fx.settings.WindowMonitor;
import java.util.function.Supplier;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * FX Application Framework.
 */
// TODO shorter name?
public class FxFramework
{
	private static FxSettingsSchema schema;
	

	/**
	 * Sets application-wide stylesheet.  This method must be invoked from the FX application thread.
	 * <p>
	 * The first call initializes the global stylesheet subsystem, possibly starting the periodic
	 * refresh and dumping of the stylesheet to stdout.
	 * 
	 * @param generator generator which supplies the actual CSS stylesheet (can be null)
	 * @see FxFlags.CSS_REFRESH
	 * @see FxFlags.CSS_DUMP
	 */
	public static synchronized void setStyleSheet(Supplier<FxStyleSheet> generator)
	{
		CssLoader.setGenerator(generator);
	}

	
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
		if(schema != null)
		{
			schema.restoreWindow(w);
		}
	}
	
	
	public static void save()
	{
		schema.save();
	}
	
	
	public static void exit()
	{
		storeLayout();
		WindowMonitor.exit();
	}
}
