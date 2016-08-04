// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fx.CInsets;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.HPane;
import goryachev.fxdock.FxDockPane;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
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
public class DemoHPane
	extends FxDockPane
{
	public final TextFlow infoField;
	public final CPane p;
	
	
	public DemoHPane()
	{
		super(DemoGenerator.HPANE);
		setTitle("DemoHPane.java");

		String info = "HPane is a horizontal Pane with a single row layout similar to CPane.";

		infoField = new TextFlow(new Text(info));

		p = new CPane();
		p.setGaps(10, 7);
		p.setPadding(10);
		p.addColumns
		(
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
			CPane.PREF,
			CPane.PREF
		);
		int r = 0;
		p.add(0, r++, infoField);
		p.add(0, r++, p(HPane.FILL));
		p.add(0, r++, p(1.0));
		p.add(0, r++, p(0.1, HPane.FILL, HPane.FILL, 0.1));
		
		setContent(p);
	}
	
	
	protected Node p(double ... specs)
	{
		HPane p = new HPane(5);
		int ix = 0;
		for(double w: specs)
		{
			String text = DemoTools.spec(w);
			Label t = new Label(text);
			
			t.setBackground(FX.background(Color.WHITE));
			t.setPadding(new CInsets(1, 3));
			t.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
			
			p.add(t, w);
			ix++;
		}
		return p;
	}
	
	
	protected Node t()
	{
		Label t = new Label();
		
		t.setBackground(FX.background(Color.WHITE));
		t.setPadding(new CInsets(1, 3));
		t.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
		
		t.textProperty().bind(Bindings.createStringBinding(() -> DemoTools.f(t.getWidth()) + "x" + DemoTools.f(t.getHeight()), t.widthProperty(), t.heightProperty()));
		
		return t;
	}
}
