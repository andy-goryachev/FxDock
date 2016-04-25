// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * DNDWindow.
 */
public class DNDWindow
	extends Stage
{
	public DNDWindow(Region n)
	{
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage im = n.snapshot(sp, null);
		
		Group gr = new Group(new ImageView(im));
		
		Scene s = new Scene(gr);
		setScene(s);
		
		setWidth(n.getWidth());
		setHeight(n.getHeight());
		
		Point2D p = n.localToScreen(n.getLayoutX(), n.getLayoutY());
		
		setX(p.getX());
		setY(p.getY());
		
		gr.addEventFilter(MouseEvent.ANY, (ev) ->
		{
			D.print(ev);
		});
	}
	
	
	public void handle(MouseEvent event)
	{
		
	}
}
