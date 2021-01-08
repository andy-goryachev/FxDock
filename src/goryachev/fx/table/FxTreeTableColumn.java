// Copyright © 2016-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.common.util.CKit;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.util.Callback;


/**
 * FxTreeTable Column.
 */
public class FxTreeTableColumn<ITEM,CELL>
	extends TreeTableColumn<ITEM,CELL>
{
	protected Function<CELL,String> formatter;
	protected Function<CELL,Node> renderer;
	protected OverrunStyle overrunStyle = OverrunStyle.ELLIPSIS;
	protected Pos alignment = Pos.CENTER_LEFT;

	
	public FxTreeTableColumn(String name)
	{
		super(name);
		init();
	}


	public FxTreeTableColumn()
	{
		init();
	}
	
	
	private void init()
	{
		setCellFactory(cellFactory());
		setCellValueFactory((cdf) -> new ReadOnlyObjectWrapper(CKit.toString(cdf.getValue())));
	}
	
	
	public FxTreeTableColumn<ITEM,CELL> setAlignment(Pos a)
	{
		alignment = a;
		return this;
	}
	
	
	public FxTreeTableColumn<ITEM,CELL> setFormatter(Function<CELL,String> formatter)
	{
		this.formatter = formatter;
		return this;
	}
	
	
	public FxTreeTableColumn<ITEM,CELL> setTextOverrun(OverrunStyle x)
	{
		overrunStyle = x;
		return this;
	}
	
	
	public FxTreeTableColumn<ITEM,CELL> setRenderer(Function<CELL,Node> r)
	{
		renderer = r;
		return this;
	}
	
	
	/** 
	 * A simplified alternative to setCellValueFactory().  
	 * Registers a function that returns an ObservableValue from the data item.
	 */
	public FxTreeTableColumn<ITEM,CELL> setAccessor(Function<ITEM,ObservableValue<CELL>> func)
	{
		setCellValueFactory((cdf) -> 
		{
			ITEM item = cdf.getValue().getValue();
			return func.apply(item);
		});
		return this;
	}
	
	
	/**
	 * A simplified alternative to setCellValueFactory(), suitable for when the cell value 
	 * has no corresponding ObservableValue, but simply returns a constant value.
	 * Registers a function that returns a constant cell value, to be wrapped into a ReadOnlyObjectWrapper.
	 */
	public FxTreeTableColumn<ITEM,CELL> setSimpleAccessor(Function<ITEM,CELL> func)
	{
		setCellValueFactory((cdf) ->
		{
			ITEM item = cdf.getValue().getValue();
			CELL v = func.apply(item);
			return new ReadOnlyObjectWrapper<CELL>(v);
		});
		return this;
	}
	
	
	/** javafx does not honor pref width */
	public FxTreeTableColumn<ITEM,CELL> setRealPrefWidth(double width)
	{
		setMaxWidth(width * 100);
		return this;
	}
	
	
	/** sets checkbox renderer */
	public void setCheckboxCell()
	{
		setCellFactory((cb) -> new CheckBoxTreeTableCell<>());
	}
	
	
	private Callback cellFactory()
	{
		return new Callback<TreeTableColumn<?,?>,TreeTableCell<?,?>>()
		{
			@Override
			public TreeTableCell<?,?> call(TreeTableColumn<?,?> param)
			{
				return new TreeTableCell<Object,Object>()
				{
					@Override
					protected void updateItem(Object item, boolean empty)
					{
						if(item != getItem())
						{
							super.updateItem(item, empty);
		
							if(item == null)
							{
								super.setText(null);
								super.setGraphic(null);
							}
							else if(item instanceof Node)
							{
								super.setText(null);
								super.setGraphic((Node)item);
							}
							else if(renderer == null)
							{
								String text = (formatter == null ? item.toString() : formatter.apply((CELL)item));
								super.setText(text);
								super.setGraphic(null);
								super.setAlignment(alignment);
								super.setTextOverrun(overrunStyle);
							}
							else
							{
								Node n = renderer.apply((CELL)item);
								super.setText(null);
								super.setGraphic(n);
							}
						}
					}
				};
			}
		};
	}
}
