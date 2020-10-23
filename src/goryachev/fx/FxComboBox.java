// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.Collection;
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
	
	
	/** notice: utility method, the property will change if the underlying model has been changed */
	public final ReadOnlyObjectProperty<T> selectedItemProperty()
	{
		return getSelectionModel().selectedItemProperty();
	}
	
	
	public T getSelectedItem()
	{
		return getSelectionModel().getSelectedItem();
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
