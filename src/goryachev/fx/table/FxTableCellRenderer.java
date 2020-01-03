// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import javafx.scene.Node;


/**
 * FxTableCellRenderer.
 */
public abstract class FxTableCellRenderer<T>
{
	public abstract Node getCellValue(T item);
}
