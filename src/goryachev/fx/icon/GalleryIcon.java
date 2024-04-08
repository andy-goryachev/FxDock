// Copyright © 2019-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.fx.FX;
import goryachev.fx.FxPath;
import goryachev.fx.IconBase;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;


/**
 * Gallery Icon.
 */
public class GalleryIcon
	extends IconBase
{
	public GalleryIcon(double size)
	{
		this(size, 3, 3);
	}
	
	
	public GalleryIcon(double size, int cols, int rows)
	{
		super(size);
		
		double N = 3;
		double dx = size / (N * cols + Math.max(0, cols - 1));
		double dy = size / (N * rows + Math.max(0, rows - 1));
		double th = size * 0.05 / Math.min(cols, rows);
		double w = N * dx;
		double h = N * dy;
		
		FxPath p = new FxPath();
		p.setStroke(Color.BLACK);
		p.setStrokeWidth(th);
		p.setStrokeLineCap(StrokeLineCap.ROUND);
		p.setFill(FX.alpha(Color.BLACK, 0.5));
		
		for(int r=0; r<rows; r++)
		{
			double y0 = r * (h + dy);
			double y1 = y0 + h;
			
			for(int c=0; c<cols; c++)
			{
				double x0 = c * (w + dx);
				double x1 = x0 + w;
				
				p.moveto(x0, y0);
				p.lineto(x1, y0);
				p.lineto(x1, y1);
				p.lineto(x0, y1);
				p.close();
			}
		}
		
		add(p);
	}
}
