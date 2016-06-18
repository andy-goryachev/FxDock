// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
		FxIconBuilder b = new FxIconBuilder(200);
		b.setStrokeWidth(10);
		b.setStrokeColor(Color.WHITE);
		b.moveTo(10, 10);
		b.lineRel(180, 180);
		b.moveTo(10, 190);
		b.lineRel(180, -180);
		
		return b.getIcon();
	}
}
