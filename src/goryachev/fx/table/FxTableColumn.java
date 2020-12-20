// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;


/**
 * A slightly more convenient TableColumn for FxTable.
 * 
 * <ITEM> - row type for FxTable.getItems()
 * <CELL> - cell type for the given column
 * 
 * Adds convenience methods:
 * 
 * - setAccessor(): accesses an ObservableValue
 * - setSimpleAccessor(): creates a ReadOnlyObjectWrapper for a constant value
 * - setFormatter(): formats TYPE to a string representation
 * - setRenderer(): renders the TYPE value in a custom Node
 */
public class FxTableColumn<ITEM,CELL>
	extends TableColumn<ITEM,CELL>
{
	protected Function<CELL,String> formatter;
	protected Function<CELL,Node> renderer;
	protected BiConsumer<TableCell,CELL> decorator;
	protected OverrunStyle overrunStyle = OverrunStyle.ELLIPSIS;
	protected Pos alignment = Pos.CENTER_LEFT;
	
	
	public FxTableColumn(String name)
	{
		super(name);
		init();
	}
	
	
	public FxTableColumn()
	{
		init();
	}
	
	
	private void init()
	{
		setCellFactory(cellFactory());
		setCellValueFactory((cdf) -> new ReadOnlyObjectWrapper(CKit.toString(cdf.getValue())));
	}
	
	
	public FxTableColumn<ITEM,CELL> setAlignment(Pos a)
	{
		alignment = a;
		return this;
	}
	
	
	public FxTableColumn<ITEM,CELL> setFormatter(Function<CELL,String> formatter)
	{
		this.formatter = formatter;
		return this;
	}
	
	
	public FxTableColumn<ITEM,CELL> setTextOverrun(OverrunStyle x)
	{
		overrunStyle = x;
		return this;
	}
	
	
	/** WARNING: might cause infinite layout() loop if table is inside a split pane? */ 
	public FxTableColumn<ITEM,CELL> setRenderer(Function<CELL,Node> r)
	{
		renderer = r;
		return this;
	}
	
	
	public FxTableColumn<ITEM,CELL> setDecorator(BiConsumer<TableCell,CELL> d)
	{
		decorator = d;
		return this;
	}


	/** 
	 * A simplified alternative to setCellValueFactory().  
	 * Registers a function that returns an ObservableValue from the data item.
	 */
	public FxTableColumn<ITEM,CELL> setAccessor(Function<ITEM,ObservableValue<CELL>> func)
	{
		setCellValueFactory((cdf) -> 
		{
			ITEM item = cdf.getValue();
			return func.apply(item);
		});
		return this;
	}
	
	
	/**
	 * A simplified alternative to setCellValueFactory(), suitable for when the cell value 
	 * has no corresponding ObservableValue, but simply returns a constant value.
	 * Registers a function that returns a constant cell value, to be wrapped into a ReadOnlyObjectWrapper.
	 */
	public FxTableColumn<ITEM,CELL> setSimpleAccessor(Function<ITEM,CELL> func)
	{
		setCellValueFactory((cdf) ->
		{
			ITEM item = cdf.getValue();
			CELL v = func.apply(item);
			return new ReadOnlyObjectWrapper<CELL>(v);
		});
		return this;
	}
	
	
	/** javafx does not honor pref width */
	public FxTableColumn<ITEM,CELL> setRealPrefWidth(double width)
	{
		setMaxWidth(width * 100);
		return this;
	}
	
	
	/** sets checkbox renderer */
	public void setCheckboxCell()
	{
		setCellFactory((cb) -> new CheckBoxTableCell<>());
	}


	protected Callback cellFactory()
	{
		return new Callback<TableColumn<?,?>,TableCell<?,?>>()
		{
			@Override
			public TableCell<?,?> call(TableColumn<?,?> param)
			{
				return new TableCell<Object,Object>()
				{
					@Override
					protected void updateItem(Object item, boolean empty)
					{
						super.updateItem(item, empty);

						if(empty || (item == null))
						{
							if(decorator != null)
							{
								decorator.accept(this, null);
							}
							else
							{
								setText(null);
								setGraphic(null);
							}
						}
						else
						{
							if(item instanceof Node)
							{
								super.setText(null);
								super.setGraphic((Node)item);
							}
							else if(decorator != null)
							{
								decorator.accept(this, (CELL)item);
							}
							else if(renderer != null)
							{
								Node n = renderer.apply((CELL)item);
								super.setText(null);
								super.setGraphic(n);
							}
							else
							{
								String text = (formatter == null ? item.toString() : formatter.apply((CELL)item));
								super.setText(text);
								super.setGraphic(null);
								super.setAlignment(alignment);
								super.setTextOverrun(overrunStyle);
							}
						}
					}
				};
			}
		};
	}
}