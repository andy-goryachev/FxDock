// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.text.Text;


/**
 * Plain Text FxEditor Model Base Class.
 */
public abstract class AbstractPlainTextEditorModel
	extends FxEditorModel
{
	public AbstractPlainTextEditorModel()
	{
	}
	
	
	public LineBox getLineBox(int line)
	{
		LineBox b = new LineBox();
		String s = getPlainText(line);
		if(s != null)
		{
			Text tx = new Text(s);
			b.addText(tx);
		}
		return b;
	}
}
