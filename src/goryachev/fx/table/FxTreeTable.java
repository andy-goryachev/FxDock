// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.fx.CPane;
import goryachev.fx.FxBoolean;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;
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
	public final FxBoolean autoResizeMode = new FxBoolean();
	public final TreeTableView<T> tree;
	
	
	public FxTreeTable()
	{
		tree = new TreeTableView<T>();
		tree.skinProperty().addListener((s,p,c) -> fixHorizontalScrollbar());
		setCenter(tree);
	}
	
	
	public void setEditable(boolean on)
	{
		tree.setEditable(on);
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
	
	
	public TreeItem<T> getRoot()
	{
		return tree.getRoot();
	}
	
	
	public void setShowRoot(boolean on)
	{
		tree.setShowRoot(on);
	}
	
	
	public void addColumn(String name)
	{
		tree.getColumns().add(new TreeTableColumn<T,T>(name));
	}
	
	
	public void addColumn(FxTreeTableColumn<T> c)
	{
		tree.getColumns().add(c);
	}
	
	
	public void addColumn()
	{
		tree.getColumns().add(new TreeTableColumn<T,T>());
	}
	
	
	public TreeTableColumn<T,?> getColumn(int ix)
	{
		return tree.getColumns().get(ix);
	}
	
	
	// temporarily public until I figure out a better api
	public TreeTableColumn<T,?> lastColumn()
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
	
	
	private void init()
	{
		tree.skinProperty().addListener((s,p,c) -> fixHorizontalScrollbar());
	}
	
	
	public boolean isAutoResizeMode()
	{
		return autoResizeMode.get();
	}
	
	
	public void setAutoResizeMode(boolean on)
	{
		autoResizeMode.set(on);

		if(on)
		{
			// TODO implement a better resizing policy that uses preferred column width
			tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		}
		else
		{
			tree.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
		}
		fixHorizontalScrollbar();
	}
	
	
	protected void fixHorizontalScrollbar()
	{
		Skin skin = tree.getSkin();
		if(skin == null)
		{
			return;
		}
		
		for(Node n: skin.getNode().lookupAll(".scroll-bar"))
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


	public int getExpandedItemCount()
	{
		return tree.getExpandedItemCount();
	}


	public TreeItem<T> getTreeItem(int row)
	{
		return tree.getTreeItem(row);
	}
	
	
	public void edit(int row, TreeTableColumn<T,?> c)
	{
		tree.edit(row, c);
	}
}
