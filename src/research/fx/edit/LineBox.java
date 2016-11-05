// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import javafx.scene.layout.Region;


/**
 * Represents a box enclosing a single line of source text.
 */
public class LineBox
{
	public final int index;
	public final Region box;
	public boolean invalid;
	// TODO line numbers
	
	
	public LineBox(int index, Region box)
	{
		this.index = index;
		this.box = box;
	}
}