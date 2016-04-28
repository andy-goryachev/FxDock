// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.D;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.internal.FxDockTools;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Dock Drag and Drop handler.
 */
public class DragAndDropHandler
{
	public static final double DRAG_WINDOW_OPACITY = 0.5;
	private static double deltax;
	private static double deltay;
	private static Stage dragWindow;
	
	
	public static void attach(Node n, FxDockPane client)
	{
		n.addEventHandler(MouseEvent.DRAG_DETECTED, (ev) -> onDragDetected(ev, client));
		n.addEventHandler(MouseEvent.MOUSE_DRAGGED, (ev) -> onMouseDragged(ev, client));
		n.addEventHandler(MouseEvent.MOUSE_RELEASED, (ev) -> onMouseReleased());
	}


	protected static void onDragDetected(MouseEvent ev, FxDockPane client)
	{
		D.print(ev);
		
//		ev.setDragDetect(false);
//		ev.consume();
	}
	
	
	protected static void onMouseDragged(MouseEvent ev, FxDockPane client)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();

		if(dragWindow == null)
		{
			Point2D p = client.screenToLocal(x, y);
			deltax = p.getX();
			deltay = p.getY();

			dragWindow = createDragWindow(client);
			dragWindow.show();
		}
		
		dragWindow.setX(x - deltax);
		dragWindow.setY(y - deltay);
		
		DropOp op = createDropOp(x, y);
	}
	

	protected static void onMouseReleased()
	{
		hideDragWindow();
	}
	
	
	protected static void hideDragWindow()
	{
		if(dragWindow != null)
		{
			dragWindow.hide();
			dragWindow = null;
		}
	}
	
	
	protected static Stage createDragWindow(FxDockPane client)
	{
		double w = client.getWidth();
		double h = client.getHeight();
		
		WritableImage im = new WritableImage((int)w, (int)h);
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		client.snapshot(sp, im);
		
		Pane p = new Pane(new ImageView(im));
		Stage s = new Stage(StageStyle.UNDECORATED);
		s.setScene(new Scene(p, w, h));
		s.setOpacity(DRAG_WINDOW_OPACITY);
		return s;
	}
	
	
	protected static DropOp createDropOp(double screenx, double screeny)
	{
		FxDockWindow w = FxDockTools.findWindow(screenx, screeny);
		if(w != null)
		{
			Object em = FxDockTools.findDockElement(w.getContent(), screenx, screeny);
			D.print(em);
		}
		return null;
	}
}
