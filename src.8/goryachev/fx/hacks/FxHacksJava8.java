// Copyright © 2016-2021 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.CList;
import goryachev.common.util.CMethod;
import goryachev.common.util.Reflector;
import goryachev.fx.edit.CHitInfo;
import java.util.List;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.stage.StageHelper;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;


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
	
	
	public CHitInfo getHit(TextFlow t, double x, double y)
	{
		HitInfo h = getTextLayout(t).getHitInfo((float)x, (float)y);
		int ix = h.getCharIndex();
		return new CHitInfo(h.getCharIndex(), h.isLeading());
	}
	
	
	public int getTextPos(TextFlow t, double x, double y)
	{
		HitInfo h = getTextLayout(t).getHitInfo((float)x, (float)y);
		return h.getInsertionIndex();
	}


	public void applyStyleSheet(String old, String cur)
	{
		if(old != null)
		{
			StyleManager.getInstance().removeUserAgentStylesheet(old);
		}
		StyleManager.getInstance().addUserAgentStylesheet(cur);
	}


	public List<Window> getWindows()
	{
		return new CList(Window.impl_getWindows());
	}
}