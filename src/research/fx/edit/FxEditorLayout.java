// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import javafx.collections.ObservableList;
import javafx.scene.Node;


/**
 * FxEditor Layout.
 */
public class FxEditorLayout
{
	private int offsety;
	private int startIndex;
	private CList<LineBox> lines = new CList<>();
	

	public FxEditorLayout(int startIndex, int offsety)
	{
		this.startIndex = startIndex;
		this.offsety = offsety;
	}
	
	
	public void addTo(ObservableList<Node> cs)
	{
		for(LineBox b: lines)
		{
			cs.add(b.box);
		}
	}


	public void removeFrom(ObservableList<Node> cs)
	{
		for(LineBox b: lines)
		{
			cs.remove(b.box);
		}
	}


	public void add(LineBox b)
	{
		lines.add(b);
	}
}