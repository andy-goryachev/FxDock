package demo;
import goryachev.fxdock.FxDockPane;
import javafx.scene.control.Label;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MyPane
	extends FxDockPane
{
	public MyPane()
	{
		super("starter-pane");

		Label l = new Label("I'm a pane");
		setCenter(l);
	}
}

