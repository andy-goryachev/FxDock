// Copyright © 2019-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.function.Consumer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollBar;


/**
 * A ScrollBar with underlying Canvas.
 */
public class XScrollBar
	extends ScrollBar
{
	private Canvas canvas;
	private Consumer<Canvas> painter;
	
	
	public XScrollBar()
	{
		setStyle("-fx-background-color:transparent; -fx-background-insets:0px;");
	}
	
	
	public void setPainter(Consumer<Canvas> p)
	{
		if(canvas != null)
		{
			getChildren().remove(canvas);
			canvas = null;
		}
		
		this.painter = p;
		requestLayout();
	}
	
	
	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		
		if(painter == null)
		{
			return;
		}
		
		double w = getWidth();
		double h = getHeight();
		
		if
		(
			(canvas == null) ||
			(canvas.getWidth() != w) ||
			(canvas.getHeight() != h)
		)
		{
			if(canvas != null)
			{
				getChildren().remove(canvas);
			}
			
			canvas = new Canvas(w, h);
			canvas.setManaged(false);
			getChildren().add(0, canvas);
			layoutInArea(canvas, 0, 0, w, h, 0, null, true, true, HPos.CENTER, VPos.CENTER);
			
			painter.accept(canvas);
		}
	}
}
