// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.internal.FxHacks;
import javafx.scene.Node;
import javafx.scene.shape.PathElement;
import javafx.scene.text.TextFlow;


/**
 * A TextFlow with public methods otherwise inaccessible in JavaFX.
 */
public class CTextFlow
	extends TextFlow
{
	public CTextFlow(Node ... children)
	{
		super(children);
	}


	public CTextFlow()
	{
	}


	public PathElement[] getCaretShape(int index, boolean leading)
	{
		return FxHacks.get().getCaretShape(this, index, leading);
	}


	public PathElement[] getRange(int start, int end)
	{
		return FxHacks.get().getRange(this, start, end);
	}


	/** returns text position at the specified local coordinates */
	public TextPos getTextPos(int line, double x, double y)
	{
		return FxHacks.get().getTextPos(this, line, x, y);
	}
}
