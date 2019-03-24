// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;
import goryachev.common.util.ByteArrayClassLoader;
import goryachev.common.util.CKit;
import goryachev.fx.edit.CHitInfo;
import java.util.List;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;


/**
 * FX Hacks.
 * 
 * This class undoes access restrictions imposed on us by the FX developers.
 * the idea is to have two implementations - one for Java 8 and one for Java 9 where
 * supposedly all the necessary internal machinery will have been made public.
 * 
 * Development of java 8 and java 9 hacks is being done as part of FxEditor project
 * https://github.com/andy-goryachev/FxEditor/ 
 */
public abstract class FxHacks
{
	/** returns the shape of the caret at the specified index */
	public abstract PathElement[] getCaretShape(TextFlow t, int index, boolean leading);
	
	/** returns the shape of the text selection */
	public abstract PathElement[] getRange(TextFlow t, int start, int end);
	
	/** returns the hit info at the specified local coordinates */
	public abstract CHitInfo getHit(TextFlow t, double x, double y);
	
	/** returns the text position at the specified local coordinates */
	public abstract int getTextPos(TextFlow t, double x, double y);
	
	/** applies global stylesheet on top of the javafx one */
	public abstract void applyStyleSheet(String old, String cur);
	
	/** applies global stylesheet to a specific window on top of the javafx one */
	public abstract void applyStyleSheet(Window w, String old, String cur);
	
	/** returns the list of Windows */
	public abstract List<Window> getWindows();
	
	//
	
	private static FxHacks instance;

	
	protected FxHacks()
	{
	}
	
	
	public static FxHacks get()
	{
		if(instance == null)
		{
			instance = create();
		}
		return instance;
	}
	
	
	private static FxHacks create()
	{
		try
		{
			String packageName = FxHacks.class.getPackage().getName();
			String name = getHackName();
			byte[] b = CKit.readBytes(FxHacks.class, name + ".bin");
			
			Class<?> c = new ByteArrayClassLoader().load(packageName + "." + name, b);
			return (FxHacks)c.getDeclaredConstructor().newInstance();
		}
		catch(Exception e)
		{
			throw new Error("failed to load FxHacks", e);
		}
	}
	
	
	private static String getHackName()
	{
		if(CKit.isJava9OrLater())
		{
			// this class lives on FxEditor java9-hacks branch
			return "FxHacksJava9";
		}
		else
		{
			// this class lives on FxEditor java8-hacks branch
			return "FxHacksJava8";
		}
	}
}
