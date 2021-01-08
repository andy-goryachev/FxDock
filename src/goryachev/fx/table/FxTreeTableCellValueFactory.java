// Copyright © 2016-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;


/**
 * FxTreeTableCellValueFactory.
 */
public abstract class FxTreeTableCellValueFactory<T> 
	implements Callback<TreeTableColumn.CellDataFeatures<T,T>, ObservableValue<T>>
{
	public abstract T value(TreeItem<T> value, TreeTableColumn<T,T> col, TreeTableView<T> t);
	
	//
	
	public ObservableValue<T> call(CellDataFeatures<T,T> f)
	{
		T v = value(f.getValue(), f.getTreeTableColumn(), f.getTreeTableView());
		return new ReadOnlyObjectWrapper<T>(v);
	}
}
