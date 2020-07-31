// Copyright © 2019-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.function.Function;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;


/**
 * Flat Toggle Button.
 */
public class FlatToggleButton
	extends ToggleButton
{
	public static final CssStyle STYLE = new CssStyle("FlatToggleButton_STYLE");
	private static final Object ICONS = new Object();
	
	
	public FlatToggleButton(String text, Node graphic)
	{
		super(text, graphic);
		init();
	}
	
	
	public FlatToggleButton(Node graphic)
	{
		super(null, graphic);
		init();
	}
	
	
	public FlatToggleButton(String text)
	{
		super(text);
		init();
	}
	
	
	public FlatToggleButton()
	{
		init();
	}
	
	
	private void init()
	{
		FX.style(this, STYLE);
	}
	
	
	public void setIcons(Function<Boolean,Node> generator)
	{
		Object prev = getProperties().get(ICONS);
		if(prev instanceof ChangeListener)
		{
			selectedProperty().removeListener((ChangeListener)prev);
		}
		
		ChangeListener<Boolean> li = new ChangeListener<Boolean>()
		{
			public void changed(ObservableValue<? extends Boolean> src, Boolean prev, Boolean cur)
			{
				updateIcon(generator, cur);
			}
		};
		selectedProperty().addListener(li);
		getProperties().put(ICONS, li);
		
		updateIcon(generator, isSelected());
	}
	
	
	protected void updateIcon(Function<Boolean,Node> generator, Boolean on)
	{
		Node icon = generator.apply(on);
		setGraphic(icon);
	}
}
