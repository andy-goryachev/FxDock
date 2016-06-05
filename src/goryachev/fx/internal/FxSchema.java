// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SB;
import goryachev.common.util.SStream;
import goryachev.fx.CPane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


/**
 * Keys and functions used to store ui settings.
 */
public class FxSchema
{
	public static final String FX_PREFIX = "FX.";
	
	public static final String SFX_COLUMNS = ".COLS";
	public static final String SFX_DIVIDERS = ".DIVS";
	public static final String SFX_SELECTION = ".SEL";
	
	public static final String WINDOW_FULLSCREEN = "F";
	public static final String WINDOW_MAXIMIZED = "X";
	public static final String WINDOW_ICONIFIED = "I";
	public static final String WINDOW_NORMAL = "N";

	
	public static void storeStage(Stage win, String prefix)
	{
		double x = win.getX();
		double y = win.getY();
		double w = win.getWidth();
		double h = win.getHeight();
		
		String state;
		if(win.isFullScreen())
		{
			state = WINDOW_FULLSCREEN;
		}
		else if(win.isMaximized())
		{
			state = WINDOW_MAXIMIZED;
		}
		else if(win.isIconified())
		{
			state = WINDOW_ICONIFIED;
		}
		else
		{
			state = WINDOW_NORMAL;
		}
		
		SStream s = new SStream();
		s.add(x);
		s.add(y);
		s.add(w);
		s.add(h);
		s.add(state);

		GlobalSettings.setStream(FX_PREFIX + prefix, s);
	}
	
	
	public static void restoreStage(Stage win, String prefix)
	{
		try
		{
			String id = FX_PREFIX + prefix;
			SStream s = GlobalSettings.getStream(id);
			double x = s.nextDouble(-1);
			double y = s.nextDouble(-1);
			double w = s.nextDouble(-1);
			double h = s.nextDouble(-1);
			String state = s.nextString(WINDOW_NORMAL);
			
			if((w > 0) && (h > 0))
			{
				win.setX(x);
				win.setY(y);
				win.setWidth(w);
				win.setHeight(h);
				
				switch(state)
				{
				case WINDOW_ICONIFIED:
					win.setIconified(true);
					break;
				case WINDOW_FULLSCREEN:
					win.setFullScreen(true);
					break;
				}
			}
		}
		catch(Exception e)
		{ }
	}
	
	
	public static void storeSplitPane(String prefix, SplitPane sp)
	{
		SStream s = new SStream();
		s.add(sp.getDividers().size());
		s.addAll(sp.getDividerPositions());
		
		String k = prefix + SFX_DIVIDERS;
		GlobalSettings.setStream(k, s);
	}
	
	
	public static void restoreSplitPane(String prefix, SplitPane sp)
	{
		String k = prefix + SFX_DIVIDERS;
		SStream s = GlobalSettings.getStream(k);
		
		int ct = s.nextInt();
		if(sp.getDividers().size() == ct)
		{
			for(int i=0; i<ct; i++)
			{
				double div = s.nextDouble();
				sp.setDividerPosition(i, div);
			}
		}
	}
	
	
	private static String findName(String wprefix, Node n)
	{
		String name = getName(n);
		if(name == null)
		{
			return null;
		}
		
		SB sb = new SB();
		sb.a(FX_PREFIX);
		sb.a(wprefix);
		sb.a('.');
		findPrefixPrivate(sb, n);
		return sb.toString();
	}


	private static void findPrefixPrivate(SB sb, Node n)
	{
		if(n != null)
		{
			String name = getName(n);
			
			Parent p = n.getParent();
			if(p != null)
			{
				findPrefixPrivate(sb, p);
				sb.a('.');
			}
			
			sb.a(name);
		}
	}
	

	public static void storeNode(String wprefix, Node n)
	{
		// TODO skip is storing is not supported
		// TODO bound properties
		
		String prefix = findName(wprefix, n);
		if(prefix != null)
		{
			if(n instanceof SplitPane)
			{
				storeSplitPane(prefix, (SplitPane)n);
			}
			
			if(n instanceof Parent)
			{
				for(Node ch: ((Parent)n).getChildrenUnmodifiable())
				{
					storeNode(wprefix, ch);
				}
			}
		}
	}
	
	
	public static void restoreNode(String wprefix, Node n)
	{
		// TODO skip is storing is not supported
		// TODO bound properties
		
		String prefix = findName(wprefix, n);
		if(prefix != null)
		{
			if(n instanceof SplitPane)
			{
				restoreSplitPane(prefix, (SplitPane)n);
			}
			
			if(n instanceof Parent)
			{
				for(Node ch: ((Parent)n).getChildrenUnmodifiable())
				{
					restoreNode(wprefix, ch);
				}
			}
		}
	}
	
	
	private static String getName(Node n)
	{
		if(n instanceof MenuBar)
		{
			return null;
		}
		else if(n instanceof Shape)
		{
			return null;
		}
		
		String id = n.getId();
		if(id != null)
		{
			return id;
		}
		
		if(n instanceof CPane)
		{
			return "CPANE";
		}
		else if(n instanceof BorderPane)
		{
			return "BORDERPANE";
		}
		else if(n instanceof Group)
		{
			return "GROUP";
		}
		else if(n instanceof SplitPane)
		{
			return "SPLIT";
		}
		else if(n instanceof StackPane)
		{
			return "STACKPANE";
		}
		else if(n instanceof TableView)
		{
			return "TABLE";
		}
		else if(n instanceof TreeTableView)
		{
			return "TREETABLE";
		}
		else if(n instanceof TreeView)
		{
			return "TREE";
		}
		else if(n instanceof Region)
		{
			return "REGION";
		}
		
		throw new Error("?" + n);
	}
}
