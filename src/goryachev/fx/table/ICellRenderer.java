// Copyright © 2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import javafx.scene.control.TableCell;


/**
 * Cell Renderer.
 */
public interface ICellRenderer<CELL>
{
	/**
	 * Renders the table cell value.
	 * 
	 * @return String, Node, or null.  When null is returned, it is expected that the 
	 * renderer configured the TableCell (setText, setGraphic, setAlignment etc. 
	 */
	public Object renderCell(TableCell tableCell, CELL value);
}
