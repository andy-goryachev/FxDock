// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo.dock;
import goryachev.common.util.Hex;
import goryachev.fx.FX;
import goryachev.fx.HPane;
import goryachev.fxdock.FxDockPane;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;


/**
 * DemoDockPane.
 */
public class DemoPane
    extends FxDockPane
{
	private static int seq = 1;
	private int pseq;
	
	
	public DemoPane(String type)
	{
		super(type);
		
		HPane hp = new HPane();
		hp.add(FX.label( "PREF"), HPane.PREF);
		hp.fill(FX.label("FILL"));
		hp.add(FX.label("PREF"), HPane.PREF);
		
		BorderPane bp = new BorderPane();
		bp.setCenter(createColorNode(type));;
		bp.setBottom(hp);
		
		setCenter(bp);
		this.pseq = seq++;
		setTitle("pane " + pseq);
	}
	
	
	private Node createColorNode(String c)
	{
		int rgb = Hex.parseInt(c, 0);
		Region r = new Region();
		r.setBackground(new Background(new BackgroundFill(FX.rgb(rgb), null, null)));
		return r;
	}
}
