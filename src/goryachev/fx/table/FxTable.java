// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.fx.FX;
import goryachev.fx.internal.CssTools;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.layout.BorderPane;


/**
 * FxTable.
 * 
 * empty rows: http://stackoverflow.com/questions/26298337/tableview-adjust-number-of-visible-rows
	.table-row-cell:empty 
	{
		-fx-background-color: white;
		-fx-border-color: white;
	} 
 */
public class FxTable<T>
	extends BorderPane
{
	public final TableView<T> table;
	
	
	public FxTable()
	{
		table = new TableView<T>();
		setCenter(table);
	}
	
	
	public FxTableColumn<T> addColumn(FxTableColumn<T> tc)
	{
		table.getColumns().add(tc);
		return tc;
	}
	
	
	public FxTableColumn<T> addColumn()
	{
		FxTableColumn<T> tc = new FxTableColumn<T>();
		table.getColumns().add(tc);
		return tc;
	}
	
	
	public void setColumns(Collection<FxTableColumn<T>> cs)
	{
		table.getColumns().setAll(cs);
	}
	
	
	public void setColumns(FxTableColumn<T> ... cs)
	{
		table.getColumns().setAll(cs);
	}
	
	
	public int getColumnCount()
	{
		return table.getColumns().size();
	}
	
	
	public FxTableColumn<T> lastColumn()
	{
		ObservableList<TableColumn<T,?>> cs = table.getColumns();
		return (FxTableColumn<T>)cs.get(cs.size() - 1);
	}
	
	
	public void setItems(Collection<T> items)
	{
		if(items == null)
		{
			table.getItems().clear();
		}
		else
		{
			table.getItems().setAll(items);
		}
	}
	
	
	public void setItems(T[] items)
	{
		if(items == null)
		{
			table.getItems().clear();
		}
		else
		{
			table.getItems().setAll(items);
		}
	}
	
	
	public void setItems(ObservableList<T> source)
	{
		table.setItems(source);
	}
	
	
	public void setResizePolicyConstrained()
	{
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		FX.style(table, CssTools.NO_HORIZONTAL_SCROLL_BAR);
	}
	
	
	public void hideHeader()
	{
		FX.hideHeader(table);
	}
	
	
	public T getSelectedItem()
	{
		return table.getSelectionModel().getSelectedItem();
	}
	
	
	public ObservableList<T> getSelectedItems()
	{
		return table.getSelectionModel().getSelectedItems();
	}
	
	
	public void setPlaceholder(String s)
	{
		table.setPlaceholder(new Label(s));
	}
	
	
	public void setPlaceholder(Node n)
	{
		table.setPlaceholder(n);
	}
	
	
	public void selectFirst()
	{
		table.getSelectionModel().selectFirst();
	}
	
	
	public TableViewSelectionModel<T> getSelectionModel()
	{
		return table.getSelectionModel();
	}
	
	
	public void setMultipleSelection(boolean on)
	{
		table.getSelectionModel().setSelectionMode(on ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
	}
}
