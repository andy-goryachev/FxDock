// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FxSize;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
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
	
	/** returns the text position at the specified local coordinates */
	public abstract TextPos getTextPos(TextFlow t, double x, double y);
	
	//
	
	private static FxHacks instance;
	private static Text helper = new Text();

	
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
	

	// from http://stackoverflow.com/questions/15593287/binding-textarea-height-to-its-content/19717901#19717901
	// FIX return Dimension2D (or Dim)
	public static FxSize getTextBounds(TextArea t, double width)
	{
		String text = t.getText();
		if(width < 0)
		{
			// Note that the wrapping width needs to be set to zero before
			// getting the text's real preferred width.
			helper.setWrappingWidth(0);
		}
		else
		{
			helper.setWrappingWidth(width);
		}
		helper.setText(text);
		helper.setFont(t.getFont());
		Bounds r = helper.getLayoutBounds();
		
		Insets m = t.getInsets();
		Insets p = t.getPadding();
		double w = Math.ceil(r.getWidth() + m.getLeft() + m.getRight());
		double h = Math.ceil(r.getHeight() + m.getTop() + m.getBottom());
		
		return new FxSize(w, h);
	}
}
