// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;


/**
 * FxEditor Selection and Caret Model.
 */
public class FxEditorSelectionModel
{
	protected final Path caret;
	protected final Path highlight;
	
	
	public FxEditorSelectionModel()
	{
		highlight = new Path();
		// TODO styles
		highlight.setManaged(false);
		highlight.setStroke(null);
		highlight.setFill(Color.YELLOW);
		
		caret = new Path();
		// TODO styles
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);
	}
	
	
	public void clear()
	{
		caret.getElements().clear();
		highlight.getElements().clear();
	}


	public void setCaret(PathElement[] es)
	{
		caret.getElements().setAll(es);
	}
}
