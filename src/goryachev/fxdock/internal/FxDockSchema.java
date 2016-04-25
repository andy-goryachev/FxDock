// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * FxDock framework schema for the layout storage.
 */
public class FxDockSchema
{
	public static final String PREFIX_DOCK = "fxdock.";
	public static final String PREFIX_WINDOW = PREFIX_DOCK + "window.";
	
	public static final String SUFFIX_WINDOW = ".window";
	
	public static final String KEY_WINDOW_COUNT = PREFIX_DOCK + "window.count";
	
	public static final String STAGE_FULL_SCEEN = "F";
	public static final String STAGE_ICONIFIED = "I";
	public static final String STAGE_MAXIMIZED = "X";
	public static final String STAGE_NORMAL = "N";
	
	
	public static int getWindowCount()
	{
		return GlobalSettings.getInt(KEY_WINDOW_COUNT, 0);
	}
	
	
	public static void setWindowCount(int n)
	{
		 GlobalSettings.setInt(KEY_WINDOW_COUNT, n);
	}
	
	
	public static String windowID(int n)
	{
		return PREFIX_WINDOW + n;
	}


	public static void storeWindow(String id, Window w)
	{
		SStream s = new SStream();
		s.add(w.getX());
		s.add(w.getY());
		s.add(w.getWidth());
		s.add(w.getHeight());
		
		if(w instanceof Stage)
		{
			Stage stage = (Stage)w;
			if(stage.isFullScreen())
			{
				s.add(STAGE_FULL_SCEEN);
			}
			else if(stage.isMaximized())
			{
				s.add(STAGE_MAXIMIZED);
			}
			else if(stage.isIconified())
			{
				s.add(STAGE_ICONIFIED);
			}
			else
			{
				s.add(STAGE_NORMAL);
			}
		}
		
		GlobalSettings.setStream(id + SUFFIX_WINDOW, s);
	}
	
	
	public static void restoreWindow(String id, Window w)
	{
		SStream s = GlobalSettings.getStream(id + SUFFIX_WINDOW);
		if(s.size() == 5)
		{
			double x = s.nextDouble();
			double y = s.nextDouble();
			double width = s.nextDouble();
			double h = s.nextDouble();
			String t = s.nextString();
			
			w.setX(x);
			w.setY(y);
			w.setWidth(width);
			w.setHeight(h);
			
			if(w instanceof Stage)
			{
				Stage st = (Stage)w;
				if(STAGE_FULL_SCEEN.equals(t))
				{
					st.setFullScreen(true);
				}
				else if(STAGE_MAXIMIZED.equals(t))
				{
					st.setMaximized(true);
				}
				else if(STAGE_ICONIFIED.equals(t))
				{
					st.setIconified(true);
				}
			}
		}
	}


	public static Node restoreNode(String id)
	{
		return null;
	}


	public static void storeNode(String id, Node content)
	{
	}
}
