// Copyright © 2020-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.function.Supplier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


/**
 * Convenient Fx TabPane.
 */
public class FxTabPane
	extends TabPane
{
	public FxTabPane()
	{
	}
	
	
	public Tab addTab(String name, Node n)
	{
		Tab t = new Tab(name, n);
		t.setClosable(false);
		getTabs().add(t);
		return t;
	}
	
	
	public Tab addTab(String name, Supplier<Node> generator)
	{
		Tab t = new Tab(name);

		ChangeListener<Boolean> li = new ChangeListener<Boolean>()
		{
			public void changed(ObservableValue<? extends Boolean> src, Boolean old, Boolean val)
			{
				Node n = generator.get();
				t.setContent(n);
				t.selectedProperty().removeListener(this);
			}
		};
		
		t.setClosable(false);
		t.selectedProperty().addListener(li);
		getTabs().add(t);
		
		return t;
	}
	
	
	public Tab addTab(String name)
	{
		Tab t = new Tab(name);
		t.setClosable(false);
		getTabs().add(t);
		return t;
	}
	
	
	public Tab addTab(Tab t)
	{
		getTabs().add(t);
		return t;
	}
	
	
	public void selectTab(Tab t)
	{
		getSelectionModel().select(t);
	}
	
	
	public void selectTab(int index)
	{
		getSelectionModel().select(index);
	}
	
	
	/** selects a tab with the specified content Node */
	public boolean selectNode(Node n)
	{
		for(Tab t: getTabs())
		{
			if(t.getContent() == n)
			{
				getSelectionModel().select(t);
				return true;
			}
		}
		return false;
	}
	
	
	/** selects a tab with the specified text */
	public boolean selectByText(String title)
	{
		for(Tab t: getTabs())
		{
			if(title.equals(t.getText()))
			{
				getSelectionModel().select(t);
				return true;
			}
		}
		return false;
	}


	public Tab getSelectedItem()
	{
		return getSelectionModel().getSelectedItem();
	}
	
	
	public Node getSelectedNode()
	{
		Tab t = getSelectionModel().getSelectedItem();
		if(t == null)
		{
			return null;
		}
		return t.getContent();
	}
	
	
	public int indexOf(Node n)
	{
		for(int i=0, sz=getTabs().size(); i<sz; i++)
		{
			Tab t = getTabs().get(i);
			if(t.getContent() == n)
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public Tab tabAt(int ix)
	{
		return getTabs().get(ix);
	}
}
