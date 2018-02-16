// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.internal.CssTools;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.layout.Pane;
import javafx.util.Callback;


/**
 * FxTable.
 */
public class FxTreeTable<T>
	extends CPane
{
	public final TreeTableView<T> tree;
	
	
	public FxTreeTable()
	{
		tree = new TreeTableView<T>();
		setCenter(tree);
	}
	
	
	public void selectFirst()
	{
		tree.getSelectionModel().selectFirst();
	}
	
	
	public TreeTableViewSelectionModel<T> getSelectionModel()
	{
		return tree.getSelectionModel();
	}
	
	
	// TODO
	// even java fx drops generics here
	private Callback createCellFactory()
	{
		return new Callback<TreeTableColumn<T,?>,TreeTableCell<T,?>>()
		{
			@Override
			public TreeTableCell<T,?> call(TreeTableColumn<T,?> column)
			{
				return new TreeTableCell()
				{
					@Override
					protected void updateItem(Object item, boolean empty)
					{
						if(item == getItem())
						{
							return;
						}

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
						else
						{
							super.setText(item.toString());
							super.setGraphic(null);
						}
					}
				};
			}
		};
	}


	public void setRoot(TreeItem<T> root)
	{
		tree.setRoot(root);
	}
	
	
	public void setShowRoot(boolean on)
	{
		tree.setShowRoot(on);
	}
	
	
	public void addColumn(String name)
	{
		tree.getColumns().add(new TreeTableColumn<T,T>(name));
	}
	
	
	public void addColumn()
	{
		tree.getColumns().add(new TreeTableColumn<T,T>());
	}
	
	
	protected TreeTableColumn<T,?> lastColumn()
	{
		ObservableList<TreeTableColumn<T,?>> cs = tree.getColumns();
		return cs.get(cs.size() - 1);
	}
	
	
	public void setCellFactory(FxTreeTableCellFactory<T> f)
	{
		 TreeTableColumn c = lastColumn();
		 c.setCellFactory(f);
	}
	
	
	public void setCellValueFactory(FxTreeTableCellValueFactory<T> f)
	{
		 TreeTableColumn c = lastColumn();
		 c.setCellValueFactory(f);
	}
	
	
	public void setResizePolicyConstrained()
	{
		tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		FX.style(tree, CssTools.NO_HORIZONTAL_SCROLL_BAR);
	}
	
	
	public void hideHeader()
	{
		tree.skinProperty().addListener((s, p, v) ->
		{
			Pane header = (Pane)tree.lookup("TableHeaderRow");
			if(header.isVisible())
			{
				header.setMaxHeight(0);
				header.setMinHeight(0);
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
	}
}
