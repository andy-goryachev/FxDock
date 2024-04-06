// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;


/**
 * Slightly improved ComboBox.
 */
public class FxComboBox<T>
	extends ComboBox<T>
{
	public FxComboBox(ObservableList<T> items)
	{
		super(items);
	}
	
	
	public FxComboBox(T ... items)
	{
		setItems(items);
	}
	
	
	public FxComboBox(Collection<T> items)
	{
		setItems(items);
	}
	
	
	public FxComboBox()
	{
	}
	
	
	public void setItems(T ... items)
	{
		if(items == null)
		{
			getItems().clear();
		}
		else
		{
			getItems().setAll(items);
		}
	}
	
	
	public void setItems(Collection<T> items)
	{
		if(items == null)
		{
			getItems().clear();
		}
		else
		{
			getItems().setAll(items);
		}
	}
	
	
	public void addItem(T item)
	{
		getItems().add(item);
	}
	
	
	public void selectFirst()
	{
		getSelectionModel().selectFirst();
	}
	
	
	public void select(int ix)
	{
		getSelectionModel().select(ix);
	}
	
	
	public void select(T item)
	{
		getSelectionModel().select(item);
	}
	
	
	/** 
	 * selects an item by some key (based on equality), where the converter extracts a key of a similar type from containing items.
	 * does nothing if key is null.
	 */  
	public <V> void select(V key, Function<T,V> converter)
	{
		if(key == null)
		{
			return;
		}
		
		List<T> items = getItems();
		for(int i=items.size()-1; i>=0; i--)
		{
			T item = items.get(i);
			V k = converter.apply(item);
			if(key.equals(k))
			{
				select(i);
				return;
			}
		}
	}
	
	
	public void selectOrFirst(T item)
	{
		int ix = indexOf(item);
		if(ix < 0)
		{
			selectFirst();
		}
		else
		{
			select(ix);
		}
	}
	
	
	/** notice: utility method, the property will change if the underlying model has been changed */
	public final ReadOnlyObjectProperty<T> selectedItemProperty()
	{
		return getSelectionModel().selectedItemProperty();
	}
	
	
	public T getSelectedItem()
	{
		return getSelectionModel().getSelectedItem();
	}
	
	
	public int getSelectedIndex()
	{
		return getSelectionModel().getSelectedIndex();
	}
	
	
	public int indexOf(T item)
	{
		return getItems().indexOf(item);
	}


	public String getSelectedItemAsString()
	{
		T x = getSelectedItem();
		return x == null ? null : x.toString();
	}
}
