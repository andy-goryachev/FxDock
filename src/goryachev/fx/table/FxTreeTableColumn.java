// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.fx.Converters;
import goryachev.fx.FxFormatter;
import java.util.function.Function;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.StringConverter;


/**
 * Fx Tree Table Column.
 */
public abstract class FxTreeTableColumn<T>
	extends TreeTableColumn<T,Object>
{
	/** 
	 * Override to provide Observable value for the cell.  Example:
	 * return new ReadOnlyObjectWrapper(item); 
	 */
	protected abstract ObservableValue getCellValueProperty(T item);
	
	//
	
	protected Function<Object,Node> renderer;
	protected StringConverter<Object> formatter = Converters.OBJECT();
	protected Pos alignment = Pos.CENTER_LEFT;

	
	public FxTreeTableColumn(String text, boolean sortable)
	{
		super(text);
		
		setSortable(sortable);
		setCellValueFactory((f) -> getCellValueProperty(f.getValue().getValue()));
		setCellFactory((f) -> getCellFactory(f));
	}


	public FxTreeTableColumn(String text)
	{
		this(text, true);
	}
	
	
	public FxTreeTableColumn()
	{
		this(null, true);
	}
	
	
	public FxTreeTableColumn(boolean sortable)
	{
		this(null, sortable);
	}
	
	
	/** sets renderer node generator which creates a Node to represent a value */
	public void setRenderer(Function<Object,Node> r)
	{
		renderer = r;
	}
	
	
	public void setAlignment(Pos a)
	{
		alignment = a;
	}
	
	
	public void setConverter(FxFormatter c)
	{
		formatter = (c == null ? Converters.OBJECT() : c);
	}
	
	
	public StringConverter<Object> getConverter()
	{
		return formatter;
	}
	
	
	private TreeTableCell<T,Object> getCellFactory(TreeTableColumn<T,Object> tc)
	{
		return new TreeTableCell<T,Object>()
		{
			@Override
			protected void updateItem(Object item, boolean empty)
			{
				super.updateItem(item, empty);
				
				if(empty)
				{
					setText(null);
					setGraphic(null);
				}
				else
				{
					if(renderer == null)
					{
						String text = formatter.toString(item);
						setText(text);
						setGraphic(null);
						setAlignment(alignment);
					}
					else
					{
						Node n = renderer.apply(item);
						setText(null);
						setGraphic(n);
					}
				}
			}
		};
	}
}
