// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;


/**
 * Simple Styled Text Model.
 * 
 * TODO insert, delete, allAll, setAll etc.
 */
public class SimpleStyledTextModel
	extends FxEditorModel
{
	private CList<LineBox> lines = new CList();
	
	
	public SimpleStyledTextModel()
	{
	}
	
	
	public LineBox getLineBox(int line)
	{
		if(lines.isValidIndex(line))
		{
			return lines.get(line);
		}
		return null;
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
		throw new Error();
	}
	
	
	public void add(TStyle s, String text)
	{
		LineBox b = new LineBox();
		b.addText(s, text);
		lines.add(b);
	}
	
	
	public void add(Object ... styleTextPairs)
	{
		LineBox b = new LineBox();
		for(int i=0; i<styleTextPairs.length; )
		{
			TStyle s = (TStyle)styleTextPairs[i++];
			String text = (String)styleTextPairs[i++];
			b.addText(s, text);
		}
		lines.add(b);
	}
}
