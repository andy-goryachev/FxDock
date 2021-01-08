// Copyright © 2019-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.fx.FxPath;
import goryachev.fx.IconBase;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;


/**
 * Find Icon.
 */
public class FindIcon
	extends IconBase
{
	public FindIcon(double size)
	{
		super(size);
		
		double r = 0.3 * size;
		double w = 0.075 * size;
		double gap = 0.12 * size;
		double handle = 0.15 * size;

		Circle c = new Circle(0, 0, r);
		c.setFill(null);
		c.setStrokeWidth(w);
		c.setStroke(Color.BLACK);
		c.setFill(null);
		
		FxPath p = new FxPath();
		p.setStrokeLineCap(StrokeLineCap.SQUARE);
		p.setStroke(Color.BLACK);
		p.setStrokeWidth(w);
		p.moveto(r, 0);
		p.lineto(r + gap, 0);
		
		FxPath h = new FxPath();
		h.setStrokeLineCap(StrokeLineCap.ROUND);
		h.setStroke(Color.BLACK);
		h.setStrokeWidth(w * 2);
		h.moveto(r + gap, 0);
		h.lineto(r + gap + handle, 0);
		
		Group g = new Group(c, p, h);
		g.setRotate(135);
		g.setTranslateX(size * 0.30);
		g.setTranslateY(size * 0.54);
		
		add(g);
	}
}
