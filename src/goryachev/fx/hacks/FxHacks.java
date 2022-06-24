// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;
import goryachev.common.log.Log;
import goryachev.common.util.ByteArrayClassLoader;
import goryachev.common.util.CKit;
import goryachev.common.util.JavaVersion;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;


/**
 * FX Hacks.
 * 
 * This class undoes access restrictions imposed on us by the FX developers.
 * the idea is to have two implementations - one for Java 8 and one for Java 11+.
 * 
 * Development of java 8 and java 11+ hacks is being done as part of a private Incubator project.
 * 
 * How to update:
 * 
 * - switch to jdk 8 (jdk + compiler compliance level)
 * - checkout java8-hacks branch
 * - compile
 * - execute CopyHacks8 (copies FxHacksJava8.class -> /build.hacks/FxHacksJava8.bin)
 * 
 * - switch to jdk 11 (jdk + compiler compliance level)
 * - checkout java11-hacks branch
 * - compile
 * - execute CopyHacks11  (copies FxHacksJava11.class -> /build.hacks/FxHacksJava11.bin)
 * 
 * - checkout master branch
 * - execute CopyHacksBin (copies /build.hacks/FxHacksJava*.bin -> src)
 * - commit
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
	
	/** returns the list of currently shown Windows */
	public abstract List<Window> getWindows();
	
	/** returns current mouse coordinates (java11+), or null if not supported */
	public abstract Point2D getMousePosition();
	
	/** returns pixel color at specified screen coordinates (java11+), or null if not supported */
	public abstract Color getPixelColor(double x, double y);
	
	/** injects a key press (java11+).  returns false if not supported */
	public abstract boolean keyPress(KeyCode k);
	
	/** injects a key release (java11+).  returns false if not supported */
	public abstract boolean keyRelease(KeyCode k);
	
	/** injects a key typed event (java11+).  returns false if not supported */
	public abstract boolean keyType(KeyCode k);
	
	/** injects a mouse click (java11+).  returns false if not supported */
	public abstract boolean mouseClick(MouseButton ... buttons);
	
	/** injects a mouse press event (java11+).  returns false if not supported */
	public abstract boolean mousePress(MouseButton ... buttons);
	
	/** injects a mouse release event (java11+).  returns false if not supported */
	public abstract boolean mouseRelease(MouseButton ... buttons);
	
	/** moves the mouse to the specified screen coordinates (java11+).  returns false if not supported */
	public abstract boolean mouseMove(double x, double y);
	
	/** injects a mouse wheen movement event (java11+).  returns false if not supported */
	public abstract boolean mouseWheel(int clicks);

	/** returns screen capture (java11+), or null if not supported */
	public abstract WritableImage getScreenCapture(WritableImage image, double x, double y, double width, double height, boolean scaleToFit);
	
	/** returns true if Robot-based functionality is enabled */
	public abstract boolean isRobotAvailable();
	
	//
	
	protected static final Log log = Log.get("FxHacks");
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
		JavaVersion ver = JavaVersion.getJavaVersion();
		if(ver.isSameOrLaterThan(JavaVersion.parse("11")))
		{
			// Incubator project, java11-hacks branch
			return "FxHacksJava11";
		}
		else if(ver.isSameOrLaterThan(JavaVersion.parse("9")))
		{
			// Incubator project, java9-hacks branch
			return "FxHacksJava9";
		}
		else
		{
			// Incubator project, java8-hacks branch
			return "FxHacksJava8";
		}
	}
}
