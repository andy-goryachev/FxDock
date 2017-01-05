// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;


/**
 * Slightly improved ComboBox.
 */
public class CComboBox<T>
	extends ComboBox<T>
{
	public CComboBox(ObservableList<T> items)
	{
		super(items);
	}
	
	
	public CComboBox(T[] items)
	{
		setValues(items);
	}
	
	
	public CComboBox(Collection<T> items)
	{
		setValues(items);
	}
	
	
	public CComboBox()
	{
	}
	
	
	public void setValues(T[] items)
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
	
	
	public void setValues(Collection<T> items)
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
}
