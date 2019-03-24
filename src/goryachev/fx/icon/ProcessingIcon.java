// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.icon;
import goryachev.fx.FxIconBuilder;
import goryachev.fx.IconBase;
import goryachev.fx.internal.WeakAnimation;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;


/**
 * Processing Icon.
 * 
 * TODO extends IconBase
 */
public class ProcessingIcon
{
	private ProcessingIcon()
	{
	}
	
	
	public static IconBase create(double size)
	{
		double sz2 = size / 2.0;
		FxIconBuilder b = new FxIconBuilder(size, sz2, sz2);
		
		double a = Math.PI / 4;
		double r = 0.8 * sz2;
		double w = 0.15 * sz2;
		
		b.setFill(null);
		b.setStrokeWidth(w);
		b.setStrokeLineCap(StrokeLineCap.ROUND);
		b.setStrokeColor(Color.BLACK);
		
		// beware of clipping
		// https://bugs.openjdk.java.net/browse/JDK-8088365
		// b.setEffect(new Bloom(0.5));

		b.newPath();
		b.moveTo(r * Math.cos(a), -r * Math.sin(a));
		b.arcRel(0, 0, r, -(Math.PI - a - a));
		b.moveTo(r * Math.cos(a), r * Math.sin(a));
		b.arcRel(0, 0, r, (Math.PI - a - a));
		
		IconBase ic = b.getIcon();
		
		new WeakAnimation<IconBase>(ic, Duration.millis(25))
		{
			protected void handleFrame(IconBase ic)
			{
				rotate(ic);
			}
		};
		
		return ic;
	}
	
	
	protected static void rotate(IconBase ic)
	{
		double a = ic.getRotate();
		a += 10;
		ic.setRotate(a);
	}
}
