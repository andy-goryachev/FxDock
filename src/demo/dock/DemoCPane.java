// Copyright ©2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.Parsers;
import goryachev.fx.CComboBox;
import goryachev.fx.CInsets;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockPane;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	public final CPane p;
	
	
	public DemoCPane()
	{
		super(DemoGenerator.CPANE);
		setTitle("DemoCPane.java");

		String info = "This demonstrates table layout capabilities of CPane.  CPane is easier to use than GridPane because one does not have to set so many constraints on the inidividual nodes, and you also have border layout capability as well.";

		infoField = new TextFlow(new Text(info));

		p = new CPane();
		p.setGaps(10, 7);
		p.setPadding(10);
		p.addColumns
		(
			CPane.FILL,
			CPane.FILL,
			CPane.FILL,
			CPane.FILL,
			CPane.FILL,
			CPane.PREF
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
		p.add(0, r, 6, 1, infoField);
		r++;
		p.add(0, r, c(0));
		p.add(1, r, c(1));
		p.add(2, r, c(2));
		p.add(3, r, c(3));
		p.add(4, r, c(5));
		r++;
		p.add(0, r, 1, 1, t());
		p.add(1, r, 4, 1, t());
		p.add(5, r, r(r));
		r++;
		p.add(0, r, 2, 1, t());
		p.add(2, r, 3, 1, t());
		p.add(5, r, r(r));
		r++;
		p.add(0, r, 3, 1, t());
		p.add(3, r, 2, 1, t());
		p.add(5, r, r(r));
		r++;
		p.add(0, r, 4, 1, t());
		p.add(4, r, 1, 1, t());
		p.add(5, r, r(r));
		r++;
		p.add(0, r, 5, 1, t());
		p.add(5, r, r(r));
		r++;
		p.add(0, r, 1, 1, t());
		p.add(1, r, 3, 1, t());
		p.add(4, r, 1, 1, t());
		p.add(5, r, r(r));
		r++;
		p.add(0, r, 2, 1, t());
		p.add(2, r, 1, 1, t());
		p.add(3, r, 2, 1, t());
		p.add(5, r, r(r));
		
		TextField hgapField = new TextField("5");
		hgapField.textProperty().addListener((s,p,c) -> updateHGap(c));
		
		TextField vgapField = new TextField("5");
		vgapField.textProperty().addListener((s,p,c) -> updateVGap(c));
		
		CPane cp = new CPane();
		cp.setGaps(10, 7);
		cp.addColumns
		(
			CPane.PREF,
			CPane.FILL
		);
		cp.addRows
		(
			CPane.PREF,
			CPane.PREF
		);
		cp.add(0, 0, FX.label("Horizontal gap:"));
		cp.add(1, 0, hgapField);
		cp.add(0, 1, FX.label("Vertical gap:"));
		cp.add(1, 1, vgapField);

		p.setBottom(cp);
		
		setContent(p);
	}
	
	
	protected void updateHGap(String s)
	{
		int v = Parsers.parseInt(s, -1);
		if(v >= 0)
		{
			p.setHGap(v);
		}
	}
	
	
	protected void updateVGap(String s)
	{
		int v = Parsers.parseInt(s, -1);
		if(v >= 0)
		{
			//p.setVGap(v);
			
			// alternative
			String st = "-ag-vgap: " + v;
			p.setStyle(st);
		}
	}
	
	
	protected Node c(int ix)
	{
		CComboBox<Spec> c = new CComboBox<Spec>(Spec.values());
		c.select(Spec.FILL);
		c.getSelectionModel().selectedItemProperty().addListener((s,o,v) ->
		{
			p.setColumnSpec(ix, v.spec);
			p.layout();
		});
		return c;
	}
	
	
	protected Node r(int ix)
	{
		CComboBox<Spec> c = new CComboBox<Spec>(Spec.values());
		c.select(Spec.PREF);
		c.getSelectionModel().selectedItemProperty().addListener((s,o,v) ->
		{
			p.setRow(ix, v.spec);
			p.layout();
		});
		return c;
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
