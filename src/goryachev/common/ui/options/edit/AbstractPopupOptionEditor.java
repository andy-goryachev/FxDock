// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.CPopupField;
import goryachev.common.ui.options.COption;
import goryachev.common.ui.options.OptionEditor;
import javax.swing.JComponent;


public abstract class AbstractPopupOptionEditor<V, E extends JComponent>
	extends OptionEditor<V>
{
	/** create and initialize the editor from current value.  TODO current value? explicit set? */
	protected abstract E createPopupComponent(V value);
	
	/** TODO or get value from the editor? */
	protected abstract V getPopupValue(E editor);
	
	/** converts current option value to human presentation form to be displayed in the option text field */
	protected abstract String getPresentationText(V value);
	
	//
	
	protected final CPopupField<E> popupField;
	private V value;
	
	
	public AbstractPopupOptionEditor(COption<V> op)
	{
		super(op);
		
		popupField = new CPopupField<E>()
		{
			protected E createPopupComponent()
			{
				V val = getEditorValue();
				return AbstractPopupOptionEditor.this.createPopupComponent(val);
			}

			protected void commit(E editor)
			{
				V newValue = getPopupValue(editor);
				setEditorValue(newValue);
			}
		};
	}


	public JComponent getComponent()
	{
		return popupField;
	}


	public V getEditorValue()
	{
		return value;
	}


	public void setEditorValue(V v)
	{
		this.value = v;
		String s = getPresentationText(v);
		popupField.setPresentationText(s);
	}
}
