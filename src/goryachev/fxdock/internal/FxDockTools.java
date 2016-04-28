// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.CList;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.dnd.Where;
import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Window;


/**
 * FxDockTools.
 */
public class FxDockTools
{
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


	public static void insertPane(Node target, Object where, FxDockPane client)
	{
		if(target instanceof FxDockRootPane)
		{
			FxDockRootPane rp = (FxDockRootPane)target;
			Node old = rp.getContent();
			rp.setContent(makeSplit(old, client, where));
			// TODO collapse empty space
		}
		else
		{
			// TODO
			throw new Error("?" + target);
		}
	}
}
