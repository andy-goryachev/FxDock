// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxButton;
import goryachev.fxdock.FxDockPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;


/**
 * This pane demonstrates the CPane functionality.
 */
public class DemoLoginPane
	extends FxDockPane
{
	public final TextFlow infoField;
	public final TextField userNameField;
	private final PasswordField passwordField;
	public final FxButton loginButton;
	
	
	public DemoLoginPane()
	{
		super(DemoGenerator.LOGIN);
		setTitle("CPane Demo // Login Form");

		String info = "This demonstrates table layout capabilities of CPane component.  CPane is easier to use than GridPane because one does not have to set so many constraints on the inidividual nodes, and you also have border layout capability as well.";

		infoField = new TextFlow(new Text(info));

		userNameField = new TextField();

		passwordField = new PasswordField();

		loginButton = new FxButton("Login");
		loginButton.setMinWidth(100);

		CPane p = new CPane();
		p.setGaps(10, 7);
		p.setPadding(10);
		p.addColumns
		(
			10,
			CPane.PREF,
			CPane.FILL,
			CPane.PREF,
			10
		);
		p.addRows
		(
			10,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.FILL,
			10
		);
		int r = 1;
		p.add(1, r, 3, 1, infoField); 
		r++;
		p.add(1, r, FX.label("User name:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, userNameField);
		r++;
		p.add(1, r, FX.label("Password:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, passwordField);
		r++;
		p.add(3, r, loginButton);
		
		setContent(p);
	}
}
