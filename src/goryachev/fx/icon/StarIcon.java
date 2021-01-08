// Copyright © 2019-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.common.util.CKit;
import goryachev.fx.FxPath;
import goryachev.fx.IconBase;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;


/**
 * Star Icon.
 */
public class StarIcon
	extends IconBase
{
	public StarIcon(double size, Color fill)
	{
		super(size);
		
		double rm = size * 0.4;
		double r0 = size * 0.15;
		double w = size * 0.0325;
		
		FxPath p = new FxPath();
		p.setStrokeLineCap(StrokeLineCap.ROUND);
		p.setStrokeLineJoin(StrokeLineJoin.ROUND);
		p.setStroke(Color.BLACK);
		p.setStrokeWidth(w);
		p.setFill(fill);

		for(int i=0; i<11; i++)
		{
			double a = Math.PI * i / 5;
			double r = CKit.isEven(i) ? rm : r0;
			double x = r * Math.cos(a);
			double y = r * Math.sin(a);
			
			switch(i)
			{
			case 0:
				p.moveto(x, y);
				break;
			case 10:
				p.close();
				break;
			default:
				p.lineto(x, y);
				break;
			}
		}
		
		Group g = new Group(p);
		g.setRotate(-90);
		g.setTranslateX(size * 0.5);
		g.setTranslateY(size * 0.5);
		
		add(g);
	}
}
