// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CMap;
import goryachev.common.util.CPlatform;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import java.io.File;
import javafx.scene.Node;
import javafx.stage.Stage;


/**
 * FxSettings.
 */
public class FxSettings
{
	public static final String PREFIX = "FX";
	
	public static final String DIALOG = ".DIALOG";
	public static final String STAGE = ".STAGE";
	public static final String CHECK = ".CHECK";
	public static final String SPLIT = ".SPLIT";
	public static final String TABLE = ".TABLE";
	public static final String TABLE_SELECTION = ".Table.S";
	public static final String TABLE_SORT = ".TableSort";
	public static final String TEXTFIELD = ".TextField";
	public static final String TREE = ".Tree";
	public static final String TREE_TABLE = ".TreeTable";
	public static final String TREE_TABLE_SELECTION = ".TreeTable.S";
	
	public static final String STAGE_FULLSCREEN = "F";
	public static final String STAGE_MAXIMIZED = "X";
	public static final String STAGE_ICONIFIED = "I";
	public static final String STAGE_NORMAL = "N";

	private static final CMap<Object,Object> stages = new CMap();
	
	
	public static File getUserFolder()
	{
		CPlatform p = CPlatform.get();
		return p.getDefaultSettingsFolder();
	}
	
	
	public static void onStageOpening(Stage w, String name)
	{
		String id = addStage(w, name);
		restoreStage(w, name);
	}


	public static void onStageClosing(Stage w, String name)
	{
		storeStage(w, name);
		removeStage(w);
		save();
	}
	
	
	protected static String addStage(Stage w, String name)
	{
		int i = 0;
		String id = "";
		
		// small limit ensures quick return at the price of forgetting settings for that many windows
		for(; i<1000; i++)
		{
			id = name + i;
			if(!stages.containsKey(id))
			{
				break;
			}
		}
		
		stages.put(w, id);
		stages.put(id, w);
		return id;
	}


	protected static void removeStage(Stage w)
	{
		Object id = stages.remove(w);
		if(id instanceof String)
		{
			stages.remove(id);
		}
	}

	
	protected static String lookupStageName(String name, Stage w)
	{
		Object x = stages.get(w);
		if(x instanceof String)
		{
			return (String)x;
		}
		else
		{
			for(int i=0; i<999; i++)
			{
				String id = name + "." + i;
				x = stages.get(id);
				if(!(x instanceof Stage))
				{
					return id;
				}
			}

			throw new Error("too many open Stages?");
		}
	}


	protected static void setProperty(String key, String val)
	{
		GlobalSettings.setString(key, val);
	}
	
	
	protected static String getProperty(String key)
	{
		return GlobalSettings.getString(key);
	}
	
	
	protected static void setStream(String key, SStream s)
	{
		GlobalSettings.setStream(key, s);
	}
	
	
	protected static SStream getStream(String key)
	{
		return GlobalSettings.getStream(key);
	}
	
	
	// Stage: x,y,w,h,{maximized,minimized}
	public static void storeStage(Stage win, String name)
	{
		double x = win.getX();
		double y = win.getY();
		double w = win.getWidth();
		double h = win.getHeight();
		
		String state;
		if(win.isFullScreen())
		{
			state = STAGE_FULLSCREEN;
		}
		else if(win.isMaximized())
		{
			state = STAGE_MAXIMIZED;
		}
		else if(win.isIconified())
		{
			state = STAGE_ICONIFIED;
		}
		else
		{
			state = STAGE_NORMAL;
		}
		
		// TODO store Screen configuration?
		
		SStream s = new SStream();
		s.add(x);
		s.add(y);
		s.add(w);
		s.add(h);
		s.add(state);

		setStream(PREFIX + STAGE + "." + lookupStageName(name, win), s);
	}
	
	
	public static void restoreStage(Stage win, String name)
	{
		try
		{
			String id = PREFIX + STAGE + "." + lookupStageName(name, win);
			SStream s = getStream(id);
			double x = s.nextDouble(-1);
			double y = s.nextDouble(-1);
			double w = s.nextDouble(-1);
			double h = s.nextDouble(-1);
			String state = s.nextString(STAGE_NORMAL);
			
			if((w > 0) && (h > 0))
			{
				win.setX(x);
				win.setY(y);
				win.setWidth(w);
				win.setHeight(h);
				
				switch(state)
				{
				case STAGE_ICONIFIED:
					win.setIconified(true);
					break;
				case STAGE_FULLSCREEN:
					win.setFullScreen(true);
					break;
				}
			}
		}
		catch(Exception e)
		{ }
	}
	
	
	public static void storeNode(Node n)
	{
		// TODO
	}
	
	
	public static void restoreNode(Node n)
	{
		// TODO
	}
	
	
	public static void save()
	{
		GlobalSettings.save();
	}
}
