// Copyright © 2024-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.input;
import goryachev.common.util.CMap;
import goryachev.fx.input.internal.EHandlers;
import goryachev.fx.input.internal.HPriority;
import java.util.Map;
import java.util.function.BooleanSupplier;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;


/**
 * Skin InputMap.
 */
public class SkinInputMap
{
	@FunctionalInterface
	public static interface Client<T extends Event>
	{
		public void process(EventType<T> t, HPriority pri, EventHandler<T> h);
	}
	
	
	// Func -> Runnable, BooleanSupplier
	// KB -> Func
	// EventType -> EventHandler or null for key binding handler
	private final CMap<Object,Object> map = new CMap<>(16);
	
	
	public SkinInputMap()
	{
	}
	
	
	public void func(Func f, Runnable r)
	{
		map.put(f, r);
	}
	
	
	public void func(Func f, BooleanSupplier r)
	{
		map.put(f, r);
	}
	
	
	public void key(KB k, Func f)
	{
		map.put(k, f);
		EventType t = k.getEventType();
		addHandler(t, HPriority.SKIN_KB, null);
	}


	public <T extends Event> void addHandler(EventType<T> t, EventHandler<T> h)
	{
		addHandler(t, HPriority.SKIN_EH, h);
	}
	
	
	private <T extends Event> void addHandler(EventType<T> t, HPriority pri, EventHandler<T> handler)
	{
		Object v = map.get(t);
		EHandlers hs;
		if(v instanceof EHandlers h)
		{
			hs = h;
		}
		else
		{
			hs = new EHandlers();
			map.put(t, hs);
		}
		hs.add(pri, handler);
	}


	// called from InputMap
	boolean execFunc(Func f)
	{
		Object v = map.get(f);
		if(v instanceof Runnable r)
		{
			r.run();
			return true;
		}
		else if(v instanceof BooleanSupplier b)
		{
			return b.getAsBoolean();
		}
		return false;
	}


	// called from InputMap
	Object valueFor(KB k)
	{
		return map.get(k);
	}
	
	
	// called from InputMap
	void forEachHandler(Client c)
	{
		for(Map.Entry<Object,Object> en: map.entrySet())
		{
			if(en.getKey() instanceof EventType t)
			{
				EHandlers hs = (EHandlers)en.getValue();
				hs.forEachHandler((pri, h) ->
				{
					c.process(t, pri, h);
					return true;
				});
			}
		}
	}
}
