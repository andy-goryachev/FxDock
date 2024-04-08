// Copyright © 2021-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.application.Application;


/**
 * Base Class for FX Application.
 */
public abstract class FxApplication
	extends Application
{
	private static FxApplication instance;
	
	
	public FxApplication()
	{
		if(instance != null)
		{
			throw new Error("there could be only one FxApplication");
		}
		instance = this;
	}
	
	
	public static FxApplication getInstance()
	{
		if(instance == null)
		{
			throw new Error("your application must extend FxApplication");
		}
		return instance;
	}
}
