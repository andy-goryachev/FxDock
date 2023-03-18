// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.Hex;
import goryachev.fx.FX;
import goryachev.fx.FxPopupMenu;
import goryachev.fx.HPane;
import goryachev.fx.internal.LocalSettings;
import goryachev.fxdock.FxDockPane;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


/**
 * An example of an FxDockPane.
 */
public class DemoPane
    extends FxDockPane
{
	/** sequence number for debugging */
	private static int seq = 1;
	private int pseq;
	
	
	public DemoPane(String type)
	{
		super(type);
		
		CheckBox cb = new CheckBox("boolean property");
		LocalSettings.get(this).add("CHECKBOX", cb);
		
		TextField textField = new TextField();
		LocalSettings.get(this).add("TEXTFIELD", textField); 
		
//		VPane vb = new VPane();
//		a(vb, 2, 0.25, 0.25, HPane.FILL);
//		a(vb, 2, 30, 30, 100);
//		a(vb, 2, 0.2, 0.2, 0.6);
//		a(vb, 2, HPane.PREF, HPane.FILL, HPane.PREF);
//		a(vb, 2, HPane.FILL, HPane.FILL, HPane.FILL);
//		a(vb, 2, 20, HPane.FILL, 20);
//		a(vb, 2, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL, HPane.FILL);
//		a(vb, 2, 50, HPane.FILL, HPane.FILL, 50);
		
		HPane vb = new HPane(2);
		vb.add(cb);
		vb.add(textField);
			
		BorderPane bp = new BorderPane();
		bp.setCenter(createColorNode(type));
		bp.setBottom(vb);
		
		setCenter(bp);
		this.pseq = seq++;
		setTitle("pane " + pseq);
		
		// set up context menu off the title field
		FX.setPopupMenu(titleField, this::createTitleFieldPopupMenu);
	}
	
	
	protected FxPopupMenu createTitleFieldPopupMenu()
	{
		FxPopupMenu m = new FxPopupMenu();
		m.item("Pop up in Window", popToWindowAction);
		m.item("Close", closeAction);
		return m;
	}
	
	
	private static void a(Pane p, int gap, double ... specs)
	{
		HPane hp = new HPane(2);
		int ix = 0;
		for(double w: specs)
		{
			Color c = Color.gray(0.5 + 0.5 * ix / (specs.length - 1));
			String text = DemoTools.spec(w);
			TextField t = new TextField(text);
			t.setEditable(false);
			t.setPrefColumnCount(3);
			t.setBackground(FX.background(c));
			t.setPadding(Insets.EMPTY);
			hp.add(t, w);
			ix++;
		}
		p.getChildren().add(hp);
	}


	private Node createColorNode(String c)
	{
		int rgb = Hex.parseInt(c, 0);
		Region r = new Region();
		r.setBackground(FX.background(FX.rgb(rgb)));
		return r;
	}
}
