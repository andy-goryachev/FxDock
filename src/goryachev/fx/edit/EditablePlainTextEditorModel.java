// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import javafx.scene.text.Text;


/**
 * Editable plain text FxEditor model.
 */
public class EditablePlainTextEditorModel
	extends FxEditorModel
{
	protected final CList<String> lines = new CList("");
	
	
	public EditablePlainTextEditorModel()
	{
	}
	
	
	public void setText(String text)
	{
		// TODO
	}
	
	
	public void addText(String text)
	{
		// TODO
	}
	
	
	public void insertText(int line, int pos, String text)
	{
		// TODO
	}
	
	
	public LineBox getLineBox(int line)
	{
		LineBox box = new LineBox();
		String s = getPlainText(line);
		if(s != null)
		{
			Text t = new Text(s);
			box.addText(t);
		}
		return box;
	}
	
	
	public String getPlainText(int line)
	{
		return lines.get(line);
	}
	
	
	public LoadInfo getLoadInfo()
	{
		return null; 
	}
	

	public int getLineCount()
	{
		return lines.size();
	}
	
	
	public Edit edit(Edit ed) throws Exception
	{
		// TODO
		throw new Exception();
	}
}
