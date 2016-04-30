// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.CList;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.dnd.Where;
import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Window;


/**
 * Docking Framework Tools.
 */
public class DockTools
{
	public static boolean isDockableElement(Node n)
	{
		if(n instanceof FxDockBorderPane)
		{
			return true;
		}
		else if(n instanceof FxDockEmptyPane)
		{
			return true;
		}
		else if(n instanceof FxDockSplitPane)
		{
			return true;
		}
		else if(n instanceof FxDockTabPane)
		{
			return true;
		}
		else if(n instanceof FxDockRootPane)
		{
			return true;
		}
		return false;
	}
	
	
	private static boolean isEmpty(Node n)
	{
		if(n == null)
		{
			return true;
		}
		else if(n instanceof FxDockRootPane)
		{
			return isEmpty(((FxDockRootPane)n).getContent());
		}
		else if(n instanceof FxDockBorderPane)
		{
			return false;
		}
		else if(n instanceof FxDockEmptyPane)
		{
			return true;
		}
		else if(n instanceof FxDockSplitPane)
		{
			return (((FxDockSplitPane)n).getPaneCount() == 0);
		}
		else if(n instanceof FxDockTabPane)
		{
			return (((FxDockTabPane)n).getTabCount() == 0);
		}
		return true;
	}
	
	
	public static void setParent(Node p, Node child)
	{
		if(child instanceof FxDockBorderPane)
		{
			((FxDockBorderPane)child).parent.set(p);
		}
		else if(child instanceof FxDockSplitPane)
		{
			((FxDockSplitPane)child).parent.set(p);
		}
		else if(child instanceof FxDockTabPane)
		{
			((FxDockTabPane)child).parent.set(p);
		}
		else if(child instanceof FxDockEmptyPane)
		{
			((FxDockEmptyPane)child).parent.set(p);
		}
	}
	
	
	public static Node getParent(Node n)
	{
		if(n instanceof FxDockBorderPane)
		{
			return ((FxDockBorderPane)n).parent.get();
		}
		else if(n instanceof FxDockSplitPane)
		{
			return ((FxDockSplitPane)n).parent.get();
		}
		else if(n instanceof FxDockTabPane)
		{
			return ((FxDockTabPane)n).parent.get();
		}
		else if(n instanceof FxDockEmptyPane)
		{
			return ((FxDockEmptyPane)n).parent.get();
		}
		return null;
	}
	
	
	public static FxDockWindow findWindow(double screenx, double screeny)
	{
		CList<FxDockWindow> list = null;
		List<FxDockWindow> ws = FxDockFramework.getWindows();
		for(FxDockWindow w: ws)
		{
			if(w.isIconified())
			{
				continue;
			}

			if(contains(w, screenx, screeny))
			{
				if(list == null)
				{
					list = new CList<>(ws.size());
				}
				list.add(w);
			}
		}
		
		if(list == null)
		{
			return null;
		}
		else
		{
			return FxDockFramework.findTopWindow(list);
		}
	}
	
	
	public static FxDockWindow getWindow(Node n)
	{
		if(n != null)
		{
			Scene sc = n.getScene();
			if(sc != null)
			{
				Window w = sc.getWindow();
				if(w instanceof FxDockWindow)
				{
					return (FxDockWindow)w;
				}
			}
		}
		return null;	
	}
	
	
	public static void closeWindowUnlessLast(Node n)
	{
		FxDockWindow w = getWindow(n);
		if(w != null)
		{
			if(FxDockFramework.getWindowCount() > 1)
			{
				w.discardSettings = true;
				w.close();
			}
		}
	}
	
	
	public static boolean contains(Window w, double screenx, double screeny)
	{
		double x = w.getX();
		if(screenx < x)
		{
			return false;
		}
		else if(screenx > (x + w.getWidth()))
		{
			return false;
		}
		
		double y = w.getY();
		if(screeny < y)
		{
			return false;
		}
		else if(screeny > (y + w.getHeight()))
		{
			return false;
		}
		
		return true;
	}


	public static List<Pane> collectDividers(FxDockSplitPane sp)
	{
		CList<Pane> rv = new CList<>();
		for(Node n: sp.lookupAll(".split-pane-divider"))
		{
			if(FX.isParent(sp, n))
			{
				if(n instanceof Pane)
				{
					rv.add((Pane)n);
				}
			}
		}
		return rv;
	}


	public static Node findDockElement(Node n, double screenx, double screeny)
	{
		if(n != null)
		{
			Point2D p = n.screenToLocal(screenx, screeny);
			if(n.contains(p))
			{
				if(n instanceof FxDockPane)
				{
					return n;
				}
				else if(n instanceof FxDockEmptyPane)
				{
					return n;
				}
				else if(n instanceof FxDockSplitPane)
				{
					return n;
				}
				else if(n instanceof FxDockTabPane)
				{
					return n;
				}
				
				if(n instanceof Parent)
				{
					for(Node ch: ((Parent)n).getChildrenUnmodifiable())
					{
						Node found = findDockElement(ch, screenx, screeny);
						if(found != null)
						{
							return found;
						}
					}
				}
			}
		}
		return null;
	}
	
	
	private static FxDockSplitPane makeSplit(Node old, Node client, Object where)
	{
		if(where instanceof Where)
		{
			switch((Where)where)
			{
			case BOTTOM:
				return new FxDockSplitPane(Orientation.VERTICAL, old, client);
			case LEFT:
				return new FxDockSplitPane(Orientation.HORIZONTAL, client, old);
			case RIGHT:
				return new FxDockSplitPane(Orientation.HORIZONTAL, old, client);
			case TOP:
				return new FxDockSplitPane(Orientation.VERTICAL, client, old);
			default:
				throw new Error("?" + where);
			}
		}
		
		throw new Error("?" + where);
	}
	
	
	public static int indexInParent(Node n)
	{
		Node p = getParent(n);
		if(p instanceof FxDockSplitPane)
		{
			return ((FxDockSplitPane)p).indexOfPane(n);
		}
		else if(p instanceof FxDockTabPane)
		{
			return ((FxDockTabPane)p).indexOfTab(n);
		}
		return -1;
	}
	
	
	private static void replacePane(Node p, int ix, Node client)
	{
		if(p instanceof FxDockRootPane)
		{
			((FxDockRootPane)p).setContent(client);
		}
		else
		{
			throw new Error("?" + p);
		}
	}
	

	public static void collapseEmptySpace(Node parent, int ix, Node client)
	{
		if(parent instanceof FxDockSplitPane)
		{
			FxDockSplitPane sp = (FxDockSplitPane)parent;
			if(ix >= 0)
			{
				sp.removePane(ix);

				Node n = sp.getPane(ix);
				if(n != null)
				{
					if(isEmpty(n))
					{
						sp.removePane(ix);
					}
				}
				
				n = sp.getPane(ix - 1);
				if(n != null)
				{
					if(isEmpty(n))
					{
						sp.removePane(ix - 1);
					}
				}
			}
			
			int ct = sp.getPaneCount();
			if(ct < 2)
			{
				ix = indexInParent(sp);
				Node p2 = getParent(sp); 
				
				switch(ct)
				{
				case 0:				
					collapseEmptySpace(getParent(sp), ix, sp);
					break;
				case 1:
					client = sp.getPane(0);
					replacePane(p2, ix, client);
					break;
				}
			}
		}
		else if(parent instanceof FxDockRootPane)
		{
			if(((FxDockRootPane)parent).getContent() == null)
			{
				closeWindowUnlessLast(parent);
			}
		}
	}


	private static boolean insertSplit(FxDockSplitPane sp, Object where, FxDockPane client)
	{
		if(where instanceof Where)
		{
			Orientation or = sp.getOrientation();
			switch((Where)where)
			{
			case LEFT:
				if(or == Orientation.HORIZONTAL)
				{
					sp.addPane(0, client);
					return true;
				}
				break;
			case RIGHT:
				if(or == Orientation.HORIZONTAL)
				{
					sp.addPane(client);
					return true;
				}
				break;
			case TOP:
				if(or == Orientation.VERTICAL)
				{
					sp.addPane(0, client);
					return true;
				}
				break;
			case BOTTOM:
				if(or == Orientation.VERTICAL)
				{
					sp.addPane(client);
					return true;
				}
				break;
			}
		}
		return false;
	}


	public static void insertPane(Node target, Object where, FxDockPane client)
	{
		if(target instanceof FxDockRootPane)
		{
			FxDockRootPane rp = (FxDockRootPane)target;
			
			Node p = getParent(client);
			int ix = indexInParent(client);

			boolean makesplit = true;
			Node old = rp.getContent();
			if(old instanceof FxDockSplitPane)
			{
				if(insertSplit((FxDockSplitPane)old, where, client))
				{
					makesplit = false;
				}
			}
			
			if(makesplit)
			{
				rp.setContent(makeSplit(old, client, where));
			}
			
			collapseEmptySpace(p, ix, client);
		}
		else
		{
			// TODO
			throw new Error("?" + target);
		}
	}


	public static void moveToNewWindow(FxDockPane client, double screenx, double screeny)
	{
		Node p = getParent(client);
		int ix = indexInParent(client);
		
		FxDockWindow w = FxDockFramework.createWindow();
		w.setContent(client);
		w.setX(screenx);
		w.setY(screeny);
		w.setWidth(client.getWidth());
		w.setHeight(client.getHeight());
		FxDockFramework.open(w);
		
		collapseEmptySpace(p, ix, client);
	}


	public static void moveToSplit(FxDockSplitPane sp, int index, FxDockPane client)
	{
		Node p = getParent(client);
		int ix = indexInParent(client);
		
		sp.addPane(index, client);
		
		collapseEmptySpace(p, ix, client);
	}
}
