// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.CList;
import goryachev.common.util.D;
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
		else if(n instanceof FxDockBorderPane)
		{
			return isEmpty(((FxDockBorderPane)n).getCenter());
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
		else if(n instanceof FxDockRootPane)
		{
			return isEmpty(((FxDockRootPane)n).getContent());
		}
		throw new Error("?" + n);
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
		Parent p = n.getParent();
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
	
	
	public static void setParent(Node parent, Node child)
	{
		if(child instanceof FxDockBorderPane)
		{
			((FxDockBorderPane)child).parent.set(parent);
		}
		else if(child instanceof FxDockSplitPane)
		{
			((FxDockSplitPane)child).parent.set(parent);
		}
		else if(child instanceof FxDockTabPane)
		{
			((FxDockTabPane)child).parent.set(parent);
		}
		else if(child instanceof FxDockEmptyPane)
		{
			((FxDockEmptyPane)child).parent.set(parent);
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
	

	public static void collapseEmptySpace(Node parent, int ix, Node client)
	{
		if(parent instanceof FxDockSplitPane)
		{
			FxDockSplitPane sp = (FxDockSplitPane)parent;
			if(ix >= 0)
			{
				Node n = sp.getPane(ix + 1);
				if(isEmpty(n))
				{
					sp.removePane(ix + 1);
				}
				
				n = sp.getPane(ix - 1);
				if(isEmpty(n))
				{
					sp.removePane(ix - 1);
				}
			}
			
			if(sp.getPaneCount() == 0)
			{
				ix = indexInParent(sp);
				collapseEmptySpace(sp.getParent(), ix, sp);
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


	public static void insertPane(Node target, Object where, FxDockPane client)
	{
		if(target instanceof FxDockRootPane)
		{
			Node p = getParent(client);
			int ix = indexInParent(client);
			
			FxDockRootPane rp = (FxDockRootPane)target;
			Node old = rp.getContent();
			rp.setContent(makeSplit(old, client, where));
			
			collapseEmptySpace(p, ix, client);
		}
		else
		{
			// TODO
			throw new Error("?" + target);
		}
	}
}
