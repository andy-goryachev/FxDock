// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.common.util.SStream;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
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
		ReadOnlyObjectWrapper<Node> prop = getParentProperty(child);
		if(prop != null)
		{
			Node oldp = prop.get();
			if(oldp != null)
			{
				if(oldp == p)
				{
					D.print("same parent", p); // FIX ???
				}
				else
				{
					int ix = indexInParent(child);
					if(ix >= 0)
					{
						replaceChild(oldp, ix, new DeletedPane());
					}
				}
			}
			prop.set(p);
		}
	}
	
	
	private static ReadOnlyObjectWrapper<Node> getParentProperty(Node n)
	{
		if(n instanceof FxDockBorderPane)
		{
			return ((FxDockBorderPane)n).parent;
		}
		else if(n instanceof FxDockSplitPane)
		{
			return ((FxDockSplitPane)n).parent;
		}
		else if(n instanceof FxDockTabPane)
		{
			return ((FxDockTabPane)n).parent;
		}
		else if(n instanceof FxDockEmptyPane)
		{
			return ((FxDockEmptyPane)n).parent;
		}
		else if(n instanceof DeletedPane)
		{
			return null;
		}
//		return null;
		throw new Error("?" + n);
	}
	
	
	public static void remove(Node n)
	{
		Node p = getParent(n);
		remove(p, n);
		collapseEmptySpace(p);
	}
	
	
	private static void remove(Node parent, Node child)
	{
		if(parent instanceof FxDockTabPane)
		{
			((FxDockTabPane)parent).removeTab(child);
		}
		else if(parent instanceof FxDockSplitPane)
		{
			((FxDockSplitPane)parent).removePane(child);
		}
		else if(parent instanceof Pane)
		{
			((Pane)parent).getChildren().remove(child);
		}
		else
		{
			throw new Error("?" + parent);
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
	
	
	// returns true if the window has been closed
	public static boolean closeWindowUnlessLast(Node n)
	{
		FxDockWindow w = getWindow(n);
		if(w != null)
		{
			if(FxDockFramework.getWindowCount() > 1)
			{
				// TODO this is not used
				// w.discardSettings = true;
				w.close();
				return true;
			}
		}
		return false;
	}

	
	/** for debugging or information purposes */
	public static SStream saveLayout(Node n)
	{
		return FxDockSchema.saveLayoutPrivate(n);
	}
	

	public static boolean aboveDiagonal(double x, double y, double x0, double y0, double x1, double y1)
	{
		double liney = y0 + (y1 - y0) * (x - x0) / (x1 - x0);
		return y < liney;
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
			if(n instanceof Pane)
			{
				Pane p = (Pane)n;
				if(p.getParent() == sp)
				{
					rv.add(p);
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
					Node ch = findDockElement(((FxDockSplitPane)n).getPanes(), screenx, screeny);
					if(ch == null)
					{
						return n;
					}
					else
					{
						// on a divider
						return ch;
					}
				}
				else if(n instanceof FxDockTabPane)
				{
					FxDockTabPane t = (FxDockTabPane)n;
					return t.getSelectedTab();
				}
				
				if(n instanceof Parent)
				{
					Node ch = findDockElement(((Parent)n).getChildrenUnmodifiable(), screenx, screeny);
					if(ch != null)
					{
						return ch;
					}
				}
			}
		}
		return null;
	}


	private static Node findDockElement(List<Node> ns, double screenx, double screeny)
	{
		for(Node ch: ns)
		{
			Node found = findDockElement(ch, screenx, screeny);
			if(found != null)
			{
				return found;
			}
		}
		return null;
	}


	private static void unlink(Node n)
	{
		Node p = getParent(n);
		if(p == null)
		{
			// ok
		}
		else if(p instanceof FxDockSplitPane)
		{
			// make sure an empty pane is left in place
			int ix = indexInParent(n);
			((FxDockSplitPane)p).setPane(ix, new DeletedPane());
		}
		else if(p instanceof FxDockTabPane)
		{
			// make sure an empty pane is left in place
			int ix = indexInParent(n);
			((FxDockTabPane)p).setTab(ix, new DeletedPane());
		}
		else if(p instanceof FxDockRootPane)
		{
			((FxDockRootPane)p).setContent(new DeletedPane());
		}
		else if(p instanceof DeletedPane)
		{
			// no problemo
		}
		else
		{
			throw new Error("?" + p);
		}
	}


	public static Node prepareToAdd(Node n)
	{
		if(n == null)
		{
			return new FxDockEmptyPane();
		}
		else
		{
			// TODO this does not look right - use unlink before starting the move
			unlink(n);
			return n;
		}
	}
	

	private static Node makeSplit(Node client, Node old, Where where)
	{
		if(client == old)
		{
			old = new FxDockEmptyPane();
		}
		
		// check if nested splits are not needed
		Node p = getParent(old);
		if(p instanceof FxDockSplitPane)
		{
			FxDockSplitPane sp = (FxDockSplitPane)p;
			if(sp.getOrientation() == Orientation.HORIZONTAL)
			{
				switch(where)
				{
				case LEFT:
					sp.addPane(0, client);
					return sp;
				case RIGHT:
					sp.addPane(client);
					return sp;
				}
			}
			else
			{
				switch(where)
				{
				case TOP:
					sp.addPane(0, client);
					return sp;
				case BOTTOM:
					sp.addPane(client);
					return sp;
				}
			}
		}
		
		switch(where)
		{
		case BOTTOM:
			return new FxDockSplitPane(Orientation.VERTICAL, old, client);
		case LEFT:
			return new FxDockSplitPane(Orientation.HORIZONTAL, client, old);
		case RIGHT:
			return new FxDockSplitPane(Orientation.HORIZONTAL, old, client);
		case TOP:
			return new FxDockSplitPane(Orientation.VERTICAL, client, old);
		case CENTER:
		default:
			throw new Error("?" + where);
		}
	}
	
	
	private static FxDockTabPane makeTab(Node old, Node client)
	{
		FxDockTabPane t = new FxDockTabPane();
		t.addTab(old);
		t.addTab(client);
		t.select(client);
		return t;
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
	
	
	/** replaces a child at the specified index in the parent */
	private static void replaceChild(Node p, int index, Node newChild)
	{
		if(p instanceof FxDockSplitPane)
		{
			((FxDockSplitPane)p).setPane(index, newChild);
		}
		else if(p instanceof FxDockTabPane)
		{
			((FxDockTabPane)p).setTab(index, newChild);
		}
		else if(p instanceof FxDockRootPane)
		{
			((FxDockRootPane)p).setContent(newChild);
		}
		else
		{
			throw new Error("?" + p);
		}
	}
	

	public static void collapseEmptySpace(Node parent)
	{
		if(parent instanceof FxDockSplitPane)
		{
			FxDockSplitPane p = (FxDockSplitPane)parent;
			
			// remove deleted panes
			for(int i=p.getPaneCount()-1; i>=0; --i)
			{
				Node n = p.getPane(i);
				if(n instanceof DeletedPane)
				{
					p.removePane(i);
				}
			}
			
			// combine empty panes
			boolean empty = false;
			int ct = 0;
			int index = -1;
			for(int i=p.getPaneCount()-1; i>=0; --i)
			{
				Node n = p.getPane(i);
				if(isEmpty(n))
				{
					if(empty)
					{
						p.removePane(i);
					}
					else
					{
						empty = true;
					}
				}
				else
				{
					empty = false;
					index = i;
					ct++;
				}
			}
			
			if(ct < 2)
			{
				int ix = indexInParent(p);
				Node pp = getParent(p); 
				
				switch(ct)
				{
				case 0:
					replaceChild(pp, ix, new DeletedPane());
					collapseEmptySpace(pp);
					break;
				case 1:
					// no need to have split pane with a single pane
					Node n = p.getPane(index);
					replaceChild(pp, ix, n);
					break;
				}
			}
		}
		else if(parent instanceof FxDockTabPane)
		{
			FxDockTabPane p = (FxDockTabPane)parent;
			
			// remove deleted panes
			for(int i=p.getTabCount()-1; i>=0; --i)
			{
				Node n = p.getTab(i);
				if(n == null)
				{
					p.removeTab(i);
				}
				else if(n instanceof DeletedPane)
				{
					p.removeTab(i);
				}
			}
			
			// combine empty panes
			boolean empty = false;
			int ct = 0;
			int index = -1;
			for(int i=p.getTabCount()-1; i>=0; --i)
			{
				Node n = p.getTab(i);
				if(isEmpty(n))
				{
					if(empty)
					{
						p.removeTab(i);
					}
					else
					{
						empty = true;
					}
				}
				else
				{
					empty = false;
					index = i;
					ct++;
				}
			}
			
			if(ct < 2)
			{
				int ix = indexInParent(p);
				Node p2 = getParent(p); 
				
				switch(ct)
				{
				case 0:
					replaceChild(p2, ix, new DeletedPane());
					collapseEmptySpace(p2);
					break;
				case 1:
					// no need to have tab pane with a single tab
					Node n = p.getTab(index);
					replaceChild(p2, ix, n);
					break;
				}
			}
		}
		else if(parent instanceof FxDockRootPane)
		{
			FxDockRootPane rp = (FxDockRootPane)parent;
			Node n = rp.getContent();
			if(isEmpty(n))
			{
				if(!closeWindowUnlessLast(parent))
				{
					rp.setContent(null);
				}
			}
		}
	}


	private static boolean insertSplit(FxDockPane client, FxDockSplitPane sp, Object where)
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


	public static void insertPane(FxDockPane client, Node target, Object where)
	{
		if(target instanceof FxDockRootPane)
		{
			FxDockRootPane rp = (FxDockRootPane)target;
			
			BeforeDrop b = new BeforeDrop(client, target);
			boolean makesplit = true;
			Node old = rp.getContent();
			if(old instanceof FxDockSplitPane)
			{
				if(insertSplit(client, (FxDockSplitPane)old, where))
				{
					makesplit = false;
				}
			}
			
			if(makesplit)
			{
				rp.setContent(makeSplit(client, old, (Where)where));
			}
			
			collapseEmptySpace(b.clientParent);
			b.adjustSplits();
		}
		else if(target instanceof FxDockPane)
		{
			// FIX
			throw new Error("replace with split or tab");
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
		double w = client.getWidth();
		double h = client.getHeight();
				
		FxDockWindow win = FxDockFramework.createWindow();
		win.setContent(client);		
		
		win.setX(screenx);
		win.setY(screeny);
		
		// moving window after show() seems to cause flicker
		double op = win.getOpacity();
		win.setOpacity(0);
		
		FxDockFramework.open(win);

		// take into account window decorations
		// apparently, this is available only after show()
		Node n = win.getDockRootPane().getCenter();
		Insets m = FX.getInsetsInWindow(win, n);
		
		// this may still cause flicker
		win.setX(screenx - m.getLeft());
		win.setY(screeny - m.getTop());
		win.setWidth(w + m.getLeft() + m.getRight());
		win.setHeight(h + m.getTop() + m.getBottom());
		
		win.setOpacity(op);
		
		collapseEmptySpace(p);
	}
	
	
	public static void moveToNewWindow(FxDockPane client)
	{
		Node p = getParent(client);
		Window clientWindow = getWindow(client);
		
		// TODO still not correct
		Insets m = getWindowInsets(clientWindow);
		Point2D pos = client.localToScreen(0, 0);
		
		FxDockWindow w = FxDockFramework.createWindow();

		w.setContent(client);
		w.setX(pos.getX() - m.getLeft());
		w.setY(pos.getY() - m.getTop());
		w.setWidth(client.getWidth() + m.getRight() + m.getLeft());
		w.setHeight(client.getHeight() + m.getTop() + m.getBottom());
		
		FxDockFramework.open(w);
		
		collapseEmptySpace(p);
	}
	
	
	public static Insets getWindowInsets(Window w)
	{
		Scene s = w.getScene();
		
		double left = s.getX();
		double top = s.getY();
		double right = w.getWidth() - s.getWidth() - left;
		double bottom = w.getHeight() - s.getHeight() - top;
		
		return new Insets(top, right, bottom, left);
	}


	public static void moveToSplit(FxDockPane client, FxDockSplitPane sp, int index)
	{
		BeforeDrop b = new BeforeDrop(client, sp);
		sp.addPane(index, client);
		collapseEmptySpace(b.clientParent);
		b.adjustSplits();
	}
	
	
	private static void addToRootPane(FxDockPane client, FxDockRootPane rp, Where where)
	{
		Node old = rp.getContent();

		switch(where)
		{
		case CENTER:
			if(old instanceof FxDockEmptyPane)
			{
				rp.setContent(client);
			}
			else
			{
				rp.setContent(makeTab(old, client));
			}
			break;
		default:
			rp.setContent(makeSplit(client, old, where));
			break;			
		}
	}
	
	
	private static void addToTabPane(FxDockPane client, FxDockTabPane tp, int index, Where where)
	{
		switch(where)
		{
		case CENTER:
			tp.addTab(client);
			tp.select(client);
			break;
		default:
			Node p = getParent(tp);
			int ix = indexInParent(tp);
			Node sp = makeSplit(client, tp, where);
			if(p != sp)
			{
				replaceChild(p, ix, sp);
			}
			break;			
		}
	}
	
	
	private static void addToSplitPane(FxDockPane client, Pane target, FxDockSplitPane sp, int index, Where where)
	{
		// determine index from where and sp orientation
		int ix;
		if(sp.getOrientation() == Orientation.HORIZONTAL)
		{
			switch(where)
			{
			case LEFT:
				ix = index;
				break;
			case RIGHT:
				ix = index+1;
				break;
			default:
				ix = -1;
				break;
			}
		}
		else
		{
			switch(where)
			{
			case BOTTOM:
				ix = index+1;
				break;
			case TOP:
				ix =  index;
				break;
			default:
				ix = -1;
				break;
			}
		}
		
		if(ix < 0)
		{
			Node p = getParent(target);
			int ip = indexInParent(target);
			
			switch(where)
			{
			case CENTER:
				if(target instanceof FxDockEmptyPane)
				{
					sp.setPane(ip, client);
				}
				else
				{
					Node t = makeTab(target, client);
					replaceChild(p, ip, t);
				}
				break;
			default:
				Node n = makeSplit(client, target, where);
				replaceChild(p, ip, n);
				break;
			}
		}
		else
		{
			// simply insert another pane into this split pane
			sp.addPane(ix, client);
		}
	}


	/** moves client pane to new position, creating necessary splits or tabs */
	public static void moveToPane(FxDockPane client, Pane target, Where where)
	{
		BeforeDrop b = new BeforeDrop(client, target);
		Node targetParent = getParent(target);

		if(targetParent instanceof FxDockSplitPane)
		{
			int targetIndex = indexInParent(target);
			addToSplitPane(client, target, (FxDockSplitPane)targetParent, targetIndex, where);
		}
		else if(targetParent instanceof FxDockTabPane)
		{
			int targetIndex = indexInParent(target);
			addToTabPane(client, (FxDockTabPane)targetParent, targetIndex, where);
		}
		else if(targetParent instanceof FxDockRootPane)
		{
			addToRootPane(client, (FxDockRootPane)targetParent, where);
		}
		else
		{
			throw new Error("?" + targetParent);
		}

		collapseEmptySpace(b.clientParent);
		b.adjustSplits();
	}
}
