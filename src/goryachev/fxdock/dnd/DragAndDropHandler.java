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
	public static final double PANE_EDGE_HORIZONTAL = 0.3;
	public static final double PANE_EDGE_VERTICAL = 0.3;
	public static final double WINDOW_EDGE_HORIZONTAL = 30;
	public static final double WINDOW_EDGE_VERTICAL = 30;
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
		D.print(op);
		
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
		// TODO allow to create a split
		if(root.getContent() == client)
		{
			// don't drop on itself
			D.print("null client");
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
			DropOp op = new DropOp(root, WhereEdge.LEFT)
			{
				public void executePrivate()
				{
					DockTools.insertPane(client, root, Where.LEFT);
				}
			};
			op.addRect(root, 0, 0, WINDOW_EDGE_HORIZONTAL, h);
			return op;
		}
		else if(x > (w - WINDOW_EDGE_HORIZONTAL))
		{
			DropOp op = new DropOp(root, WhereEdge.RIGHT)
			{
				public void executePrivate()
				{
					DockTools.insertPane(client, root,  Where.RIGHT);
				}
			};
			op.addRect(root, w - WINDOW_EDGE_HORIZONTAL, 0, WINDOW_EDGE_HORIZONTAL, h);
			return op;
		}
		
		double y = p.getY();
		if(y < WINDOW_EDGE_VERTICAL)
		{
			DropOp op = new DropOp(root, WhereEdge.TOP)
			{
				public void executePrivate()
				{
					DockTools.insertPane(client, root, Where.TOP);
				}
			};
			op.addRect(root, 0, 0, w, WINDOW_EDGE_VERTICAL);
			return op;
		}
		else if(y > (h - WINDOW_EDGE_VERTICAL))
		{
			DropOp op = new DropOp(root, WhereEdge.BOTTOM)
			{
				public void executePrivate()
				{
					DockTools.insertPane(client, root, Where.BOTTOM);
				}
			};
			op.addRect(root, 0, h - WINDOW_EDGE_VERTICAL, w, WINDOW_EDGE_VERTICAL);
			return op;
		}

		return null;
	}
	
	
	protected static DropOp createDropToNewWindow(FxDockPane client, double screenx, double screeny)
	{
		return new DropOp(null, new WhereScreen(screenx, screeny))
		{
			public void executePrivate()
			{
				DockTools.moveToNewWindow(client, screenx, screeny);
			}
		};
	}
	
	
	protected static DropOp createDropOnPane(FxDockPane client, Pane target, double screenx, double screeny)
	{
		Point2D p = target.screenToLocal(screenx, screeny);
		double x = p.getX();
		double y = p.getY();
		double w = target.getWidth();
		double h = target.getHeight();
		double x1 = w * PANE_EDGE_HORIZONTAL;
		double x2 = w - w * PANE_EDGE_HORIZONTAL;
		double y1 = h * PANE_EDGE_VERTICAL;
		double y2 = h - h * PANE_EDGE_VERTICAL;
		
		Where where;
		if(x < x1)
		{
			if(y < y1)
			{
				// TL or LT
				where = DockTools.aboveDiagonal(x, y, 0, 0, x1, y1) ? Where.TOP_LEFT : Where.LEFT_TOP;
			}
			else if(y < y2)
			{
				where = Where.LEFT;
			}
			else
			{
				// BL or LB
				where = DockTools.aboveDiagonal(x, y, 0, h, x1, y2) ? Where.LEFT_BOTTOM : Where.BOTTOM_LEFT;
			}
		}
		else if(x < x2)
		{
			if(y < y1)
			{
				where = Where.TOP;
			}
			else if(y < y2)
			{
				// center
				where = Where.CENTER;
			}
			else
			{
				where = Where.BOTTOM;
			}
		}
		else
		{
			if(y < y1)
			{
				// TR or RT
				where = DockTools.aboveDiagonal(x, y, x2, y1, w, 0) ? Where.TOP_RIGHT : Where.RIGHT_TOP;
			}
			else if(y < y2)
			{
				where = Where.RIGHT;
			}
			else
			{
				// BR or RB
				where = DockTools.aboveDiagonal(x, y, x2, y2, w, h) ? Where.RIGHT_BOTTOM : Where.BOTTOM_RIGHT;
			}
		}
		
		DropOp op = new DropOp(target, where)
		{
			public void executePrivate()
			{
				DockTools.moveToPane(client, target, where);
			}
		};
		
		switch(where)
		{
		case BOTTOM:
			op.addRect(target, 0, y2, w, h - y2);
			break;
		case BOTTOM_LEFT:
			op.addRect(target, 0, y2, x1, h - y2);
			op.addOutline(target, 0, 0, w, y2);
			break;
		case BOTTOM_RIGHT:
			op.addRect(target, x2, y2, x2, h - y2);
			op.addOutline(target, 0, 0, w, y2);
			break;
		case CENTER:
			op.addRect(target, 0, 0, w, h);
			break;
		case LEFT:
			op.addRect(target, 0, 0, x1, h);
			break;
		case LEFT_BOTTOM: 
			op.addRect(target, 0, y2, x1, h - y2);
			op.addOutline(target, x1, 0, w - x1, h);
			break;
		case LEFT_TOP:
			op.addRect(target, 0, 0, x1, y1);
			op.addOutline(target, x1, 0, w - x1, h);
			break;
		case RIGHT:
			op.addRect(target, x2, 0, w - x2, h);
			break;
		case RIGHT_BOTTOM:
			op.addRect(target, x2, y2, w - x2, h - y2);
			op.addOutline(target, 0, 0, x2, h);
			break;
		case RIGHT_TOP:
			op.addRect(target, x2, 0, w - x2, y1);
			op.addOutline(target, 0, 0, x2, h);
			break;
		case TOP:
			op.addRect(target, 0, 0, w, y1);
			break;
		case TOP_LEFT:
			op.addRect(target, 0, 0, x1, y1);
			op.addOutline(target, 0, y1, w, h - y1);
			break;
		case TOP_RIGHT:
			op.addRect(target, x2, 0, w - x2, y1);
			op.addOutline(target, 0, y1, w, h - y1);
			break;
		}
		return op;
	}

	
	protected static DropOp createDropOnDivider(FxDockPane client, FxDockSplitPane parent, double screenx, double screeny)
	{
		List<Pane> splits = DockTools.collectDividers(parent);
		int sz = splits.size();
		for(int i=0; i<sz; i++)
		{
			Pane n = splits.get(i);
			if(FX.contains(n, screenx, screeny))
			{
				// new pane index
				int ix = i + 1;

				// found the divider, check if it's adjacent
				Node pc = DockTools.getParent(client);
				if(pc == parent)
				{
					// do not allow to drop on adjacent split
					int d = ix - DockTools.indexInParent(client);
					switch(d)
					{
					case 0:
					case 1:
						return null;
					}
				}
				
				DropOp op = new DropOp(n, ix)
				{
					public void executePrivate()
					{
						DockTools.moveToSplit(client, parent, ix);
					}
				};
				op.addRect(n, 0, 0, n.getWidth(), n.getHeight());
				return op;
			}
		}
		
		throw new Error("?div");
	}
	
	
	public static DropOp createDropOp(FxDockPane client, double screenx, double screeny)
	{
		// first, check if we are outside of any window
		FxDockWindow w = DockTools.findWindow(screenx, screeny);
		if(w == null)
		{
			return createDropToNewWindow(client, screenx, screeny);
		}
		
		// check if we can drop to window edge
		DropOp op = checkWindowEdge(client, w.getDockRootPane(), screenx, screeny);
		if(op != null)
		{
			return op;
		}
		
		// find the topmost docking element to accept the drop
		Node n = DockTools.findDockElement(w.getContent(), screenx, screeny);
		if(n == null)
		{
			return createDropToNewWindow(client, screenx, screeny);
		}
		else if(n instanceof FxDockSplitPane)
		{
			// drop on a divider
			return createDropOnDivider(client, (FxDockSplitPane)n, screenx, screeny);
		}
		else if(n instanceof Pane)
		{
			return createDropOnPane(client, (Pane)n, screenx, screeny);
		}
		else
		{
			throw new Error("?" + n);
		}
	}
}
