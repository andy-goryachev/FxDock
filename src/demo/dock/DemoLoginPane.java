// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fx.CButton;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fxdock.FxDockPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;


/**
 * This pane demonstrates the CPane functionality.
 */
public class DemoLoginPane
	extends FxDockPane
{
	public final TextField userNameField;
	private final PasswordField passwordField;
	public final CButton loginButton;
	
	
	public DemoLoginPane()
	{
		super(DemoGenerator.LOGIN);
		setTitle("CPane // Login Form");

		String info = "This demonstrates capabilities of CPane component.";

		userNameField = new TextField();

		passwordField = new PasswordField();

		loginButton = new CButton("Login");
		loginButton.setMinWidth(100);

		CPane p = new CPane();
		p.setGaps(10, 5);
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
		p.add(1, r, 3, 1, FX.label(info, FxCtl.WRAP_TEXT));
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
