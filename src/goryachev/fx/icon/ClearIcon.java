// Copyright © 2019-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.fx.FxPath;
import goryachev.fx.IconBase;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;


/**
 * Clear Field Icon.
 */
public class ClearIcon
	extends IconBase
{
	public ClearIcon(double size)
	{
		super(size);
		
		double r = 0.4 * size;
		double w = 0.075 * size;
		double d = 0.14 * size;
		
		Circle c = new Circle(0, 0, r);
		c.setFill(null);
		c.setStrokeWidth(0);
		c.setStroke(null);
		c.setFill(Color.LIGHTGRAY);
		
		FxPath p = new FxPath();
		p.setStrokeLineCap(StrokeLineCap.SQUARE);
		p.setStroke(Color.WHITE);
		p.setStrokeWidth(w);
		p.moveto(-d, -d);
		p.lineto(d, d);
		p.moveto(d, -d);
		p.lineto(-d, d);
		
		Group g = new Group(c, p);
		g.setTranslateX(size * 0.5);
		g.setTranslateY(size * 0.5);
		
		add(g);
	}
}
