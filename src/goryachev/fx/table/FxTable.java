// Copyright © 2016-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.fx.CommonStyles;
import goryachev.fx.FX;
import goryachev.fx.FxBoolean;
import java.util.Collection;
import java.util.function.Supplier;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 * Convenient FxTable.
 */
public class FxTable<T>
	extends BorderPane
{
	public final TableView<T> table;
	public final FxBoolean autoResizeMode = new FxBoolean();
	private BooleanBinding singleSelectionProperty;
	private BooleanBinding nonEmptySelectionProperty;
	
	
	public FxTable()
	{
		table = new TableView<T>();
		setCenter(table);
		init();
	}
	
	
	public FxTable(ObservableList<T> items)
	{
		table = new TableView<T>(items);
		setCenter(table);
		init();
	}
	
	
	private void init()
	{
		table.skinProperty().addListener((s,p,c) -> fixHorizontalScrollbar());
	}
	
	
	/** allow for sorting of items separately from the source list */
	public void wrapSortedList(ObservableList<T> src)
	{
		SortedList<T> s = new SortedList<>(src);
		s.comparatorProperty().bind(table.comparatorProperty());
		setItems(s);
	}
	
	
	public boolean isAutoResizeMode()
	{
		return autoResizeMode.get();
	}
	
	
	/** use FxTableColumn.setRealPrefWidth() to set the preferred width with CONSTRAINED_RESIZE_POLICY */  
	public void setAutoResizeMode(boolean on)
	{
		autoResizeMode.set(on);

		if(on)
		{
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		}
		else
		{
			table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		}
		fixHorizontalScrollbar();
	}
	
	
	protected void fixHorizontalScrollbar()
	{
		for(Node n: lookupAll(".scroll-bar"))
		{
			if(n instanceof ScrollBar)
			{
				ScrollBar b = (ScrollBar)n;
				if(b.getOrientation() == Orientation.HORIZONTAL)
				{
					if(isAutoResizeMode())
					{
						b.setManaged(false);
						b.setPrefHeight(0);
						b.setPrefWidth(0);
					}
					else
					{
						b.setManaged(true);
						b.setPrefHeight(USE_COMPUTED_SIZE);
						b.setPrefWidth(USE_COMPUTED_SIZE);
					}
				}
			}
		}
	}
	
	
	public ObservableList<TableColumn<T,?>> getColumns()
	{
		return table.getColumns();
	}
	
	
	public <C> FxTableColumn<T,C> addColumn(FxTableColumn<T,C> c)
	{
		table.getColumns().add(c);
		return c;
	}
	
	
	public <C> FxTableColumn<T,C> addColumn()
	{
		FxTableColumn<T,C> c = new FxTableColumn<>();
		table.getColumns().add(c);
		return c;
	}
	
	
	public <C> FxTableColumn<T,C> addColumn(String name)
	{
		FxTableColumn<T,C> c = new FxTableColumn<>(name);
		table.getColumns().add(c);
		return c;
	}
	
	
	public void setColumns(Collection<FxTableColumn<T,?>> cs)
	{
		table.getColumns().setAll(cs);
	}
	
	
	public void setColumns(FxTableColumn<T,?> ... cs)
	{
		table.getColumns().setAll(cs);
	}
	
	
	public int getColumnCount()
	{
		return table.getColumns().size();
	}
	

	public FxTableColumn<T,?> lastColumn()
	{
		ObservableList<TableColumn<T,?>> cs = table.getColumns();
		return (FxTableColumn<T,?>)cs.get(cs.size() - 1);
	}
	
	
	public int getRowCount()
	{
		return table.getItems().size();
	}
	
	
	public ObservableList<T> getItems()
	{
		return table.getItems();
	}
	
	
	public T getItem(int row)
	{
		if(row < 0)
		{
			return null;
		}
		
		ObservableList<T> items = table.getItems();
		if(row >= items.size())
		{
			return null;
		}
		return items.get(row);
	}
	
	
	public void setItems(Collection<T> items)
	{
		clearSelection();
		if(items == null)
		{
			table.getItems().clear();
		}
		else
		{
			table.getItems().setAll(items);
		}
		table.sort();
	}
	
	
	public void setItems(T ... items)
	{
		clearSelection();
		if(items == null)
		{
			table.getItems().clear();
		}
		else
		{
			table.getItems().setAll(items);
		}
		table.sort();
	}
	
	
	public void setItems(ObservableList<T> source)
	{
		table.setItems(source);
		table.sort();
	}
	
	
	public void addItem(T item)
	{
		table.getItems().add(item);
	}
	
	
	public void addItems(T ... items)
	{
		table.getItems().addAll(items);
	}
	
	
	public void clearItems()
	{
		clearSelection();
		table.getItems().clear();
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
	
	
	public final StringProperty placeholderLabelTextProperty()
	{
		Node n = table.getPlaceholder();
		if(n instanceof Label)
		{
			return ((Label)n).textProperty();
		}
		else
		{
			Label t = new Label();
			table.setPlaceholder(t);
			return t.textProperty();
		}
	}
	
	
	public void setPlaceholder(Node n)
	{
		table.setPlaceholder(n);
	}
	
	
	public void selectAll()
	{
		table.getSelectionModel().selectAll();
	}
	
	
	public void selectFirst()
	{
		table.getSelectionModel().selectFirst();
		table.scrollTo(0);
	}
	
	
	public void select(T item)
	{
		table.getSelectionModel().select(item);
		// TODO
//		table.scrollTo(0);
	}
	
	
	public void scrollTo(int row)
	{
		table.scrollTo(row);
	}
	
	
	public void selectRow(int ix)
	{
		table.getSelectionModel().select(ix);
	}
	
	
	public void clearSelection()
	{
		table.getSelectionModel().clearSelection();
	}
	
	
	public TableViewSelectionModel<T> getSelectionModel()
	{
		return table.getSelectionModel();
	}
	
	
	public ReadOnlyObjectProperty<T> selectedItemProperty()
	{
		return getSelectionModel().selectedItemProperty();
	}


	public ObservableList<T> selectedItemsProperty()
	{
		return getSelectionModel().getSelectedItems();
	}
	
	
	public ReadOnlyIntegerProperty selectedIndexProperty()
	{
		return getSelectionModel().selectedIndexProperty();
	}


	public void setMultipleSelection(boolean on)
	{
		table.getSelectionModel().setSelectionMode(on ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
	}
	
	
	public void setCellSelectionEnabled(boolean on)
	{
		table.getSelectionModel().setCellSelectionEnabled(on);
	}
	
	
	public void setAlternateRowsColoring(boolean on)
	{
		// https://stackoverflow.com/questions/38680711/javafx-tableview-remove-default-alternate-row-color
		FX.setStyle(table, CommonStyles.DISABLE_ALTERNATIVE_ROW_COLOR, !on);
	}
	
	
	public void setPopupMenu(Supplier<ContextMenu> generator)
	{
		FX.setPopupMenu(this, generator);
	}
	

	/** permanently hides the table header */
	public void hideHeader()
	{
		table.skinProperty().addListener((s, p, v) ->
		{
			Pane h = (Pane)table.lookup("TableHeaderRow");
			if(h != null)
			{
				if(h.isVisible())
				{
					h.setMaxHeight(0);
					h.setMinHeight(0);
					h.setPrefHeight(0);
					h.setVisible(false);
				}
			}
		});
	}
	
	
	/** this may not work if skin is not yet initialized */
	public Pane getHeader()
	{
		return (Pane)table.lookup("TableHeaderRow");
	}
	
	
	public void setHeaderPopupMenu(Supplier<ContextMenu> generator)
	{
		Pane h = getHeader();
		if(h != null)
		{
			FX.setPopupMenu(h, generator);
		}
		else
		{
			// this is idiocy
			table.skinProperty().addListener((s, p, v) ->
			{
				Pane hd = getHeader();
				if(hd != null)
				{
					FX.setPopupMenu(hd, generator);
				}
			});
		}
	}
	
	
	public BooleanBinding singleSelectionProperty()
	{
		if(singleSelectionProperty == null)
		{
			singleSelectionProperty = Bindings.createBooleanBinding
			(
				() -> (table.getSelectionModel().getSelectedIndices().size() == 1),
				table.getSelectionModel().getSelectedIndices()
			);
		}
		return singleSelectionProperty;
	}
	
	
	public BooleanBinding nonEmptySelectionProperty()
	{
		if(nonEmptySelectionProperty == null)
		{
			nonEmptySelectionProperty = Bindings.createBooleanBinding
			(
				() -> (table.getSelectionModel().getSelectedIndices().size() > 0),
				table.getSelectionModel().getSelectedIndices()
			);
		}
		return nonEmptySelectionProperty;
	}
	
	
	public void setEditable(boolean on)
	{
		table.setEditable(on);
	}
	
	
	public boolean isEditable()
	{
		return table.isEditable();
	}


	public ObservableList<TablePosition> getSelectedCells()
	{
		return getSelectionModel().getSelectedCells();
	}
}
