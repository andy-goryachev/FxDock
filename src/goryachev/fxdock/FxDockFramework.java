// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.fxdock.internal.FxDockSchema;
import java.util.List;


/**
 * FxDock Framework.
 */
public class FxDockFramework
{
	private static final CMap<Object,Object> windows = new CMap();
	
	
	public static int loadLayout(Class<? extends FxDockWindow> appWindowClass)
	{
		return 0;
	}


	public static void open(FxDockWindow w)
	{
		String id = newWindowID();
		windows.put(id, w);
		windows.put(w, id);
		
		w.showingProperty().addListener((src,old,cur) ->
		{
			if(!cur)
			{
				windows.remove(w);
				windows.remove(id);
			}
		});
		
		w.show();
	}
	
	
	private static String newWindowID()
	{
		int i = 0;
		for( ; i<100000; i++)
		{
			String id = FxDockSchema.PREFIX_WINDOW + i;
			if(!windows.containsKey(id))
			{
				return id;
			}
		}
		throw new Error("?" + i);
	}
	
	
	public static List<FxDockWindow> getWindows()
	{
		CList<FxDockWindow> rv = new CList(windows.size() / 2);
		for(Object x: windows.keySet())
		{
			if(x instanceof FxDockWindow)
			{
				rv.add((FxDockWindow)x);
			}
		}
		return rv;
	}
}
