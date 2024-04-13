// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.settings.FxSettingsSchema;
import goryachev.fx.settings.WindowMonitor;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * FX Application Framework.
 */
public class FxFramework
{
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
	
	
	public static void exit()
	{
		storeLayout();
		WindowMonitor.exit();
	}
}
