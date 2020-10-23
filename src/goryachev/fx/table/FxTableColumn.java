// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.common.util.CKit;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;


/**
 * FxTable Column.
 * 
 * TODO
 * <T> -> ObjectProperty or value (to be wrapped into ObjectProperty)
 * either:
 *  Object (from property) -> Node
 * or
 *  Object (from property) -> formatter -> String
 *  String -> setText()
 */
public class FxTableColumn<T>
	extends TableColumn<T,Object>
{
	protected Function<T,Node> renderer;
	protected Function<T,Object> converter;
	protected OverrunStyle overrunStyle;
	protected Pos alignment = Pos.CENTER_LEFT;
	
	
	public FxTableColumn(String id, String text, boolean sortable)
	{
		super(text);
		setId(id);
		setSortable(sortable);
		setCellValueFactory((f) -> getCellValueProperty(f.getValue()));
		setCellFactory((f) -> getCellFactory(f));
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
	

	/** javafx does not honor pref width */
	public FxTableColumn<T> setRealPrefWidth(double width)
	{
		setMaxWidth(width * 100);
		return this;
	}


	protected ObservableValue getCellValueProperty(T item)
	{
		if(converter == null)
		{
			return new ReadOnlyObjectWrapper(item);
		}
		else
		{
			Object x = converter.apply(item);
			if(x instanceof ObservableValue)
			{
				return (ObservableValue)x;
			}
			return new ReadOnlyObjectWrapper(x);
		}
	}
	
	
	public FxTableColumn<T> setAlignment(Pos a)
	{
		alignment = a;
		return this;
	}
	
	
	public FxTableColumn<T> setRenderer(Function<T,Node> r)
	{
		renderer = r;
		return this;
	}
	

	/** 
	 * a value converter generates cell values for sorting and display
	 * (the latter only if renderer is not set)
	 */
	public FxTableColumn<T> setConverter(Function<T,Object> f)
	{
		converter = f;
		return this;
	}
	
	
	public FxTableColumn<T> setTextOverrun(OverrunStyle x)
	{
		overrunStyle = x;
		return this;
	}
	
	
	// TODO renderer(item), value->renderer(value), value->formatter
	// get value (object or property)
	// sorting value
	// display text
	// display icon
	// full cell
	private TableCell<T,Object> getCellFactory(TableColumn<T,Object> f)
	{
		return new TableCell<T,Object>()
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
						String s = CKit.toString(item);
						setText(s);
						setGraphic(null);
						setAlignment(alignment);
						
						if(overrunStyle != null)
						{
							setTextOverrun(overrunStyle);
						}
					}
					else
					{
						// damn, typecast
						Node n = renderer.apply((T)item);
						
						setText(null);
						setGraphic(n);
					}
				}
			}
		};
	}
}
