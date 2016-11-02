// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * Plain Text FxEditor Model.
 */
public abstract class FxPlainEditorModel
	extends FxEditorModel
{
	public FxPlainEditorModel()
	{
	}
	
	
	public Region getDecoratedLine(int line)
	{
		TextFlow t = new TextFlow();
		String s = getSearchText(line);
		if(s != null)
		{
			Text tx = new Text(s);
			t.getChildren().add(tx); 
		}
		return t;
	}
}
