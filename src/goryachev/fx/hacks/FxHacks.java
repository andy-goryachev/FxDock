// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;
import goryachev.common.util.ByteArrayClassLoader;
import goryachev.common.util.CKit;
import java.util.List;
import javafx.scene.Scene;
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
 * 
 * How to update:
 * 
 * 1. set jdk to 8
 * 2. checkout java8-hacks branch
 * 3. compile
 * 4. copy out/goryachev/fx/hacks/FxHacksJava8.class to ~/FxHacksJava8.bin
 * 5. commit
 * 
 * 6. set jdk to 9 (or 10)
 * 7. checkout java9-hacks branch
 * 8. compile
 * 9. copy out/goryachev/fx/hacks/FxHacksJava9.class to ~/FxHacksJava9.bin
 * 10. commit
 * 
 * 11. checkout master branch
 * 12. copy ~/FxHackJava?.bin to src/goryachev/fx/hacks
 * 13. commit
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
		catch(Throwable e)
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
	

	/** applies global stylesheet on top of the javafx one */
	public final void applyStyleSheet(String old, String cur)
	{
		for(Window w: getWindows())
		{
			applyStyleSheet(w, old, cur);
		}
	}
	
	
	/** applies global stylesheet to a specific window on top of the javafx one */
	public final void applyStyleSheet(Window w, String old, String cur)
	{
		if(cur != null)
		{
			Scene scene = w.getScene();
			if(scene != null)
			{
				if(old != null)
				{
					scene.getStylesheets().remove(old);
				}
				
				scene.getStylesheets().add(cur);
			}			
		}
	}
}
