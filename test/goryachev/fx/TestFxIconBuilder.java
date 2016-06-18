// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.D;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Bloom;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;


/**
 * Test FxIconBuilder.
 */
public class TestFxIconBuilder
	extends Application
{
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
	
	public void start(Stage s) throws Exception
	{
		CPane p = new CPane();
		p.setBackground(FX.background(new Color(0.3, 0.3, 0.3, 1.0)));
		p.setPadding(20);
		p.setCenter(icon());
		p.addEventFilter(MouseEvent.MOUSE_CLICKED, (ev) -> p.setCenter(icon()));
		
		Scene sc = new Scene(p);
		
		s.setScene(sc);
		s.setTitle("FxIconBuilder");
		s.show();
	}
	
	
	protected Node icon()
	{
		return makeWait();
	}


	// big X
	protected Node makeBigX()
	{
		FxIconBuilder b = new FxIconBuilder(200);
		
		b.setStrokeWidth(10);
		b.setStrokeColor(Color.WHITE);
		b.moveTo(10, 10);
		b.lineRel(180, 180);
		b.moveTo(10, 190);
		b.lineRel(180, -180);
		
		return b.getIcon();
	}
	
	
	// arcs with various sweep angle
	protected Node makeC()
	{
		FxIconBuilder b = new FxIconBuilder(200, 100, 100);
		
		b.setFill(Color.WHITE);
		b.fill();
		b.setFill(null);
		
		b.setStrokeWidth(0.2);

		b.newPath();
		b.setStrokeColor(Color.BLACK);
		b.moveTo(0, -90);
		b.lineRel(0, 180);
		b.moveTo(-90, 0);
		b.lineRel(180, 0);
		
		int steps = 12;
		for(int i=0; i<steps; i++)
		{
			double r = 90.0 * (i + 1) / (steps);
			double a = 360.0 * (i + 1) / steps;
			double c = 0.4 + 0.6 * i / (steps - 1);
			
			D.print(i, "r", r, "a", a);
			
			b.newPath();
			b.setStrokeColor(new Color(c, c, 1.0 - c, 1));
			b.setStrokeWidth(0.7);
			b.moveTo(0, -r);
			b.arcRel(0, 0, r, FX.toRadians(a));
		}
		
		return b.getIcon();
	}
	
	
	protected Node makeWait()
	{
		FxIconBuilder b = new FxIconBuilder(200, 100, 100);
		
		b.setFill(Color.gray(1, 0.1));
		b.fill();

		b.setFill(null);
		b.setStrokeWidth(0.2);
		b.setStrokeColor(Color.BLACK);

		b.newPath();
		b.moveTo(0, -90);
		b.lineRel(0, 180);
		b.moveTo(-90, 0);
		b.lineRel(180, 0);
		
		double a = Math.PI / 4;
		double r = 80;
		
		b.setStrokeWidth(10);
		b.setStrokeLineCap(StrokeLineCap.ROUND);
		b.setStrokeColor(Color.YELLOW);
		// beware of clipping
		// https://bugs.openjdk.java.net/browse/JDK-8088365
		// b.setEffect(new Bloom(0.5));

		b.newPath();
		b.moveTo(r * Math.cos(a), -r * Math.sin(a));
		b.arcRel(0, 0, r, -(Math.PI - a - a));
		b.moveTo(r * Math.cos(a), r * Math.sin(a));
		b.arcRel(0, 0, r, (Math.PI - a - a));
		
		return b.getIcon();
	}
}
