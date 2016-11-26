// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CMethod;
import goryachev.common.util.Reflector;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;
import research.fx.edit.TextPos;


/**
 * FxHacks for Java 8.
 */
public class FxHacksJava8
	extends FxHacks
{
	private final CMethod<TextLayout> textLayoutMethod;
	
	
	public FxHacksJava8()
	{
		textLayoutMethod = Reflector.method(TextFlow.class, "getTextLayout");
	}
	
	
	protected TextLayout getTextLayout(TextFlow t)
	{
		return textLayoutMethod.invoke(t);
	}
	
	
	public PathElement[] getCaretShape(TextFlow t, int index, boolean leading)
	{
		return getTextLayout(t).getCaretShape(index, leading, 0.0f, 0.0f);
	}
	
	
	public PathElement[] getRange(TextFlow t, int start, int end)
	{
		return getTextLayout(t).getRange(start, end, TextLayout.TYPE_TEXT, 0.0f, 0.0f);
	}
	
	
	public TextPos getTextPos(TextFlow t, int line, double x, double y)
	{
		HitInfo h = getTextLayout(t).getHitInfo((float)x, (float)y);
		int ix = h.getCharIndex();
		return new TextPos(line, h.getCharIndex(), h.isLeading());
	}
}