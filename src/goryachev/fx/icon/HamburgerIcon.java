// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.fx.FxPath;
import goryachev.fx.IconBase;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;


/**
 * Hamburger (Settings) Icon.
 */
public class HamburgerIcon
	extends IconBase
{
	public HamburgerIcon(double sz)
	{
		super(sz);
		
		double gapx = sz * 0.2;
		double gapy = sz * 0.25;
		double th = sz * 0.075;
		
		double x0 = gapx;
		double xm = sz - gapx;
		double y0 = gapy;
		double y1 = sz / 2;
		double ym = sz - gapy;
		
		FxPath p = new FxPath();
		p.setStroke(Color.BLACK);
		p.setStrokeWidth(th);
		p.setStrokeLineCap(StrokeLineCap.ROUND);
		
		p.moveto(x0, y0);
		p.lineto(xm, y0);
		
		p.moveto(x0, y1);
		p.lineto(xm, y1);
		
		p.moveto(x0, ym);
		p.lineto(xm, ym);
		
		add(p);
	}
}
