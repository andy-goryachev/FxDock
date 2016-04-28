// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.D;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.internal.FxDockTools;
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
	private static double startx;
	private static double starty;
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
		if(dragWindow == null)
		{
			startx = ev.getScreenX();
			starty = ev.getScreenY();

			dragWindow = createDragWindow(client);
			dragWindow.show();
		}
		
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		
		dragWindow.setX(x);
		dragWindow.setY(y);
		
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
