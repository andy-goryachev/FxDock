// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;


/**
 * FxTable Column.
 */
public class FxTableColumn<T>
	extends TableColumn<T,T>
{
	protected Function<T,Node> renderer;
	
	
	public FxTableColumn(String id, String text, boolean sortable)
	{
		super(text);
		setId(id);
		setCellValueFactory((f) -> getCellValueProperty(f.getValue()));
		setCellFactory((f) -> getCellFactory(f));
	}

	
	private TableCell<T,T> getCellFactory(TableColumn<T,T> f)
	{
		return new TableCell<T,T>()
		{
			@Override
			protected void updateItem(T item, boolean empty)
			{
				super.updateItem(item, empty);
				
				if(empty)
				{
					item = null;
				}
				
				if(renderer == null)
				{
					setText(item == null ? null : item.toString());
				}
				else
				{
					Node n = (renderer == null ? null : renderer.apply(item));
					setGraphic(n);
				}
			}
		};
	}


	public FxTableColumn(String id, String text)
	{
		this(id, text, true);
	}
	
	
	public FxTableColumn()
	{
		this(null, null, true);
	}
	
	
	public FxTableColumn(boolean sortable)
	{
		this(null, null, sortable);
	}


	protected ObservableValue<T> getCellValueProperty(T item)
	{
		return new ReadOnlyObjectWrapper<T>(item);
	}
	
	
	public void setRenderer(Function<T,Node> r)
	{
		renderer = r;
	}
}
