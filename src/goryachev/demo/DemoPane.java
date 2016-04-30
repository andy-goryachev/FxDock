// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo;
import goryachev.common.util.Hex;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockPane;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;


/**
 * DemoDockPane.
 */
public class DemoPane
    extends FxDockPane
{
	private static int seq = 1;
	private int __seq;
	
	
	public DemoPane(String type)
	{
		super(type);
		setCenter(createColorNode(type));
		this.__seq = seq++;
		setTitle("pane " + __seq);
	}
	
	
	private Node createColorNode(String c)
	{
		int rgb = Hex.parseInt(c, 0);
		Region r = new Region();
		r.setBackground(new Background(new BackgroundFill(FX.rgb(rgb), null, null)));
		return r;
	}
}
