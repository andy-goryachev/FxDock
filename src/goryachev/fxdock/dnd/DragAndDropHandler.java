// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.D;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.internal.DockTools;
import goryachev.fxdock.internal.FxDockRootPane;
import goryachev.fxdock.internal.FxDockSplitPane;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	public static final double WINDOW_EDGE_HORIZONTAL = 40;
	public static final double WINDOW_EDGE_VERTICAL = 40;
	private static double deltax;
	private static double deltay;
	private static Stage dragWindow;
	private static DropOp dropOp;
	
	
	public static void attach(Node n, FxDockPane client)
	{
		n.addEventHandler(MouseEvent.DRAG_DETECTED, (ev) -> onDragDetected(ev, client));
		n.addEventHandler(MouseEvent.MOUSE_DRAGGED, (ev) -> onMouseDragged(ev, client));
		n.addEventHandler(MouseEvent.MOUSE_RELEASED, (ev) -> onMouseReleased());
	}


	protected static void onDragDetected(MouseEvent ev, FxDockPane client)
	{
		if(dragWindow == null)
		{
			double x = ev.getScreenX();
			double y = ev.getScreenY();
			Point2D p = client.screenToLocal(x, y);
			deltax = p.getX();
			deltay = p.getY();

			dragWindow = createDragWindow(client);
			dragWindow.addEventHandler(KeyEvent.KEY_PRESSED, (ke) -> onKeyPress(ke.getCode()));
			
			dragWindow.setX(x - deltax);
			dragWindow.setY(y - deltay);
			
			dragWindow.show();
			
			ev.consume();
		}
	}
	
	
	protected static void onMouseDragged(MouseEvent ev, FxDockPane client)
	{
		if(dragWindow != null)
		{
			double x = ev.getScreenX();
			double y = ev.getScreenY();
			
			dragWindow.setX(x - deltax);
			dragWindow.setY(y - deltay);
			
			DropOp op = createDropOp(client, x, y);
			setDropOp(op);
		}
	}
	
	
	protected static void setDropOp(DropOp op)
	{
		if(dropOp != null)
		{
			if(dropOp.isSame(op))
			{
				return;
			}
			else
			{
				dropOp.removeHighlights();
			}
		}

		dropOp = op;

		if(op != null)
		{
			op.installHighlights();
		}
	}
	

	protected static void onMouseReleased()
	{
		// remove drag window
		stopDrag();
		
		if(dropOp != null)
		{
			dropOp.execute();
			dropOp = null;
		}
	}
	
	
	protected static void onKeyPress(KeyCode k)
	{
		// ESCAPE key cancels the drag operation
		if(k == KeyCode.ESCAPE)
		{
			stopDrag();
			dropOp = null;
		}
	}
	
	
	protected static void stopDrag()
	{
		// delete the drag window
		if(dragWindow != null)
		{
			dragWindow.hide();
			dragWindow = null;
		}
		
		// remove the highlights but not the op
		if(dropOp != null)
		{
			dropOp.removeHighlights();
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
		Stage s = new Stage(StageStyle.UTILITY);
		s.setScene(new Scene(p, w, h));
		s.setOpacity(DRAG_WINDOW_OPACITY);
		return s;
	}
	
	
	protected static DropOp checkWindowEdge(FxDockPane client, FxDockRootPane root, double screenx, double screeny)
	{
		if(root.getContent() == client)
		{
			// don't drop on itself
			return null;
		}
		
		double w = root.getWidth();
		if(w <= WINDOW_EDGE_HORIZONTAL + WINDOW_EDGE_HORIZONTAL)
		{
			// too narrow
			return null;
		}
		
		double h = root.getHeight();
		if(w <= WINDOW_EDGE_VERTICAL + WINDOW_EDGE_VERTICAL)
		{
			// too short
			return null;
		}
		
		Point2D p = root.screenToLocal(screenx, screeny);
		double x = p.getX();
		if(x < WINDOW_EDGE_HORIZONTAL)
		{
			DropOp op = new DropOp(root, Where.LEFT)
			{
				public void execute()
				{
					DockTools.insertPane(root, getWhere(), client);
				}
			};
			op.addRect(root, 0, 0, WINDOW_EDGE_HORIZONTAL, h);
			return op;
		}
		else if(x > (w - WINDOW_EDGE_HORIZONTAL))
		{
			DropOp op = new DropOp(root, Where.RIGHT)
			{
				public void execute()
				{
					DockTools.insertPane(root, getWhere(), client);
				}
			};
			op.addRect(root, w - WINDOW_EDGE_HORIZONTAL, 0, WINDOW_EDGE_HORIZONTAL, h);
			return op;
		}
		
		double y = p.getY();
		if(y < WINDOW_EDGE_VERTICAL)
		{
			DropOp op = new DropOp(root, Where.TOP)
			{
				public void execute()
				{
					DockTools.insertPane(root, getWhere(), client);
				}
			};
			op.addRect(root, 0, 0, w, WINDOW_EDGE_VERTICAL);
			return op;
		}
		else if(y > (h - WINDOW_EDGE_VERTICAL))
		{
			DropOp op = new DropOp(root, Where.BOTTOM)
			{
				public void execute()
				{
					DockTools.insertPane(root, getWhere(), client);
				}
			};
			op.addRect(root, 0, h - WINDOW_EDGE_VERTICAL, w, WINDOW_EDGE_VERTICAL);
			return op;
		}

		return null;
	}
	
	
	protected static Where determineWhere(Node nd, double screenx, double screeny)
	{
		// TODO
		return null;
	}
	
	
	protected static DropOp createDropOnSplitDivider(FxDockPane client, FxDockSplitPane sp, double screenx, double screeny)
	{
		// TODO what if parent is the same as target? - allow only if not adjacent
		
		List<Pane> splits = DockTools.collectDividers(sp);
		for(int i=splits.size()-1; i>=0; i--)
		{
			Pane n = splits.get(i);
			
			int ix = i + 1;
			if(FX.contains(n, screenx, screeny))
			{
				DropOp op = new DropOp(n, ix)
				{
					public void execute()
					{
						DockTools.moveToSplit(sp, ix, client);
					}
				};
				op.addRect(n, 0, 0, n.getWidth(), n.getHeight());
				return op;
			}
		}
		return null;
	}
	
	
	protected static DropOp createDropOp(FxDockPane client, double screenx, double screeny)
	{
		FxDockWindow w = DockTools.findWindow(screenx, screeny);
		if(w == null)
		{
			return new DropOp(null, new WhereScreen(screenx, screeny))
			{
				public void execute()
				{
					DockTools.moveToNewWindow(client, screenx, screeny);
				}
			};
		}
		
		DropOp op = checkWindowEdge(client, w.getDockRootPane(), screenx, screeny);
		if(op != null)
		{
			return op;
		}
		
		Node p = DockTools.findDockElement(w.getContent(), screenx, screeny);
		if(p == null)
		{
			return null;
		}
		
		if(p instanceof FxDockSplitPane)
		{
			op = createDropOnSplitDivider(client, (FxDockSplitPane)p,  screenx, screeny);
			if(op != null)
			{
				return op;
			}
		}
		
		if(p instanceof Pane)
		{
			Object where = determineWhere(p, screenx, screeny);
			if(where != null)
			{
				op = new DropOp((Pane)p, where)
				{
					public void execute()
					{
						D.print(); // TODO
					}
				};
				//op.addHighlight();
				return op;
			}
		}
		return null;
	}
}
