// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockPane;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * This pane demonstrates the CPane functionality.
 */
public class DemoCPane
	extends FxDockPane
{
	public final TextFlow infoField;
	
	
	public DemoCPane()
	{
		super(DemoGenerator.CPANE);
		setTitle("CPane/VPane/HPane");

		String info = "This demonstrates table layout capabilities of CPane, HPane, and VPane components.  CPane is easier to use than GridPane because one does not have to set so many constraints on the inidividual nodes, and you also have border layout capability as well.";

		infoField = new TextFlow(new Text(info));

		CPane p = new CPane();
		p.setGaps(10, 7);
		p.setPadding(10);
		p.addColumns
		(
			CPane.FILL,
			CPane.FILL,
			CPane.FILL,
			CPane.FILL,
			CPane.FILL
		);
		p.addRows
		(
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF
		);
		int r = 0;
		p.add(0, r, 5, 1, infoField);
		r++;
		p.add(0, r, 1, 1, t());
		p.add(1, r, 4, 1, t()); 
		r++;
		p.add(0, r, 2, 1, t());
		p.add(2, r, 3, 1, t()); 
		r++;
		p.add(0, r, 3, 1, t());
		p.add(3, r, 2, 1, t()); 
		r++;
		p.add(0, r, 4, 1, t());
		p.add(4, r, 1, 1, t()); 
		r++;
		p.add(0, r, 5, 1, t());
		r++;
		p.add(0, r, 1, 1, t());
		p.add(1, r, 3, 1, t());
		p.add(4, r, 1, 1, t());
		r++;
		p.add(0, r, 2, 1, t());
		p.add(2, r, 1, 1, t());
		p.add(3, r, 2, 1, t());
		
		setContent(p);
	}
	
	
	protected Node t()
	{
		Label t = new Label();
		t.setBackground(FX.background(Color.WHITE));
		t.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
		return t;
	}
}
