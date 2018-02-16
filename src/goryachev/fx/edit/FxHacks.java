// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;


/**
 * FX Hacks.
 * 
 * This class undoes access restrictions imposed on us by the FX developers.
 * the idea is to have two implementations - one for Java 8 and one for Java 9 where
 * supposedly all the necessary internal machinery will have been made public. 
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
	
	//
	
	private static FxHacks instance;

	
	protected FxHacks()
	{
	}
	
	
	public static FxHacks get()
	{
		if(instance == null)
		{
			// TODO supply Java9 when it comes
			instance = new FxHacksJava8();
		}
		return instance;
	}
}
