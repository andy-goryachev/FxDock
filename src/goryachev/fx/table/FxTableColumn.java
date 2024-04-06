// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.common.util.CKit;
import goryachev.fx.FxObject;
import java.util.function.Function;
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
	protected ICellRenderer<CELL> renderer;
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
		setCellValueFactory((cdf) -> new FxObject(CKit.toStringOrNull(cdf.getValue())));
	}
	
	
	public FxTableColumn<ITEM,CELL> setAlignment(Pos a)
	{
		alignment = (a == null ? Pos.CENTER_LEFT : a);
		return this;
	}
	
	
	/** convenience method sets right cell alignment */
	public FxTableColumn<ITEM,CELL> setRightAlignment()
	{
		setAlignment(Pos.CENTER_RIGHT);
		return this;
	}
	
	
	/** sets the name of the column as its tooltip */
	@Deprecated // FIX or remove
	public void setToolTip()
	{
		String text = getText();
		setToolTip(text);
	}
	
	
	/** sets a tooltip on the column header TODO: DOES NOT WORK */
	@Deprecated // FIX or remove
	public void setToolTip(String text)
	{
		/** 
		 * oops not that easy: https://stackoverflow.com/questions/23224826/how-to-add-a-tooltip-to-a-tableview-header-cell-in-javafx-8
		 * 
	    TableColumn<Person, String> firstNameCol = new TableColumn<>();
	    Label firstNameLabel = new Label("First Name");
	    firstNameLabel.setTooltip(new Tooltip("This column shows the first name"));
	    firstNameCol.setGraphic(firstNameLabel);
	    
	    nameLabel.textProperty().bindBidirectional(textProperty());
		nameLabel.getStyleClass().add("column-header-label");
		nameLabel.setMaxWidth(Double.MAX_VALUE); //Makes it take up the full width of the table column header and tooltip is shown more easily.
		
		.table-view .column-header .label{
		    -fx-content-display: graphic-only;
		}
		.table-view .column-header .label .column-header-label{
		    -fx-content-display: text-only;
		}
	    */
		//FX.setTooltip(this, text);
	}
	
	
	public Pos getAlignment()
	{
		return alignment;
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
	public FxTableColumn<ITEM,CELL> setRenderer(ICellRenderer<CELL> r)
	{
		renderer = r;
		return this;
	}
	
	
	public ICellRenderer<CELL> getRenderer()
	{
		return renderer;
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
			return new FxObject<CELL>(v);
		});
		return this;
	}
	
	
	/** javafx does not honor pref width */
	public FxTableColumn<ITEM,CELL> setRealPrefWidth(double width)
	{
		setMaxWidth(width * 100);
		setPrefWidth(width);
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
							setText(null);
							setGraphic(null);
						}
						else
						{
							if(item instanceof Node)
							{
								super.setText(null);
								super.setGraphic((Node)item);
							}
							else if(renderer != null)
							{
								Object rendered = renderer.renderCell(this, (CELL)item);
								if(rendered == null)
								{
									// renderer has configured the table cell
								}
								else if(rendered instanceof Node)
								{
									Node n = (Node)rendered;
									super.setText(null);
									super.setGraphic(n);
								}
								else
								{
									String text = rendered.toString();
									super.setText(text);
									super.setGraphic(null);
								}
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