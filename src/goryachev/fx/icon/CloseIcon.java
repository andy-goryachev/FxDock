// Copyright © 2019-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.fx.FxPath;
import goryachev.fx.IconBase;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;


/**
 * Close Icon.
 */
public class CloseIcon
	extends IconBase
{
	public CloseIcon(double size)
	{
		super(size);
		
		double r = 0.4 * size;
		double w = 0.075 * size;
		double d = 0.3 * size;
		
		FxPath p = new FxPath();
		p.setStrokeLineCap(StrokeLineCap.ROUND);
		p.setStroke(Color.BLACK);
		p.setStrokeWidth(w);
		p.moveto(-d, -d);
		p.lineto(d, d);
		p.moveto(d, -d);
		p.lineto(-d, d);
		
		Group g = new Group(p);
		g.setTranslateX(size * 0.5);
		g.setTranslateY(size * 0.5);
		
		add(g);
	}
}
