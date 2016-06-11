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
	public DemoLoginPane()
	{
		super(DemoGenerator.LOGIN);
		setTitle("CPane // Login Form");
		
		String info = "This window demonstrates capabilities of CPane component.  This is a component that combines BorderPane with a table layout of the center part.  The table layout is modeled after my earlier work with Swing CPanel, which was inspired by the info.clearthought.TableLayout project."; 
		
		CButton b = new CButton("Login");
		b.setMinWidth(100);
		
		int pad = 10;
		
		CPane p = new CPane();
		p.setGaps(10, 5);
		p.setPadding(10);
		p.addColumns
		(
			pad,
			CPane.PREF,
			CPane.FILL,
			CPane.PREF,
			pad
		);
		p.addRows
		(
			pad,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.FILL,
			pad
		);
		int r = 1;
		p.add(1, r, 3, 1, FX.label(info, FxCtl.WRAP_TEXT));
		r++;
		p.add(1, r, FX.label("User name:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, new TextField());
		r++;
		p.add(1, r, FX.label("Password:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, new PasswordField());
		r++;
		p.add(3, r, b);
		
		setContent(p);
	}
}
