// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import com.sun.javafx.scene.text.TextLayout;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;


/**
 * FX Hacks.
 * 
 * This class undoes access restrictions imposed on us by the FX developers.
 * the idea is to have two versions - one for Java 8 and one for Java 9 where
 * supposedly all the necessary internal machinery will have been made public. 
 */
public abstract class FxHacks
{
	/** returns the shape of the caret at the specified index */
	public abstract PathElement[] getCaretShape(TextFlow t, int index, boolean leading);
	
	/** returns the shape of the text selection */
	public abstract PathElement[] getRange(TextFlow t, int start, int end);
	
	/** returns the text position at the specified local coordinates */
	public abstract TextPos getTextPos(TextFlow t, double x, double y);
	
	//
	
	private static FxHacks instance;
	
	
	protected FxHacks()
	{
	}
	
	
	public static FxHacks get()
	{
		if(instance == null)
		{
			// supply Java9 when it comes
			instance = new FxHacksJava8();
		}
		return instance;
	}
}
