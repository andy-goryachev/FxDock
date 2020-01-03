// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;
import goryachev.common.util.CList;
import goryachev.fx.edit.CHitInfo;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.shape.PathElement;
import javafx.scene.text.HitInfo;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;


/**
 * FxHacks for Java 9.
 */
public class FxHacksJava9
	extends FxHacks
{
	public FxHacksJava9()
	{
	}
	
	
	public PathElement[] getCaretShape(TextFlow t, int index, boolean leading)
	{
		return t.caretShape(index, leading);
	}
	
	
	public PathElement[] getRange(TextFlow t, int start, int end)
	{
		return t.rangeShape(start, end);
	}
	
	
	protected HitInfo getHitInfo(TextFlow t, double x, double y)
	{
		Point2D p = new Point2D(x, y);
		return t.hitTest(p);
	}
	
	
	public CHitInfo getHit(TextFlow t, double x, double y)
	{
		HitInfo h = getHitInfo(t, x, y);
		int ix = h.getCharIndex();
		boolean leading = h.isLeading();
		return new CHitInfo(ix, leading);
	}
	
	
	public int getTextPos(TextFlow t, double x, double y)
	{
		HitInfo h = getHitInfo(t, x, y);
		return h.getInsertionIndex();
	}


	public void applyStyleSheet(String old, String cur)
	{
		for(Window w: getWindows())
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


	public List<Window> getWindows()
	{
		return new CList(Window.getWindows());
	}
}