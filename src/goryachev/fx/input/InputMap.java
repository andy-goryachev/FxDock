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
import javafx.scene.control.Control;
import javafx.scene.input.KeyEvent;


/**
 * Input Map class serves as a repository of event handlers and key mappings,
 * arbitrating the event processing between application and the skin. 
 */
public class InputMap
{
	private final Control control;
	// Func -> Runnable
	// KB -> Func or Runnable
	// EventType -> EHandlers, or null for key binding handler
	static final Object TYPES = new Object();
	private final CMap<Object,Object> map = new CMap<>(16);
	private final EventHandler<Event> eventHandler = this::handleEvent;
	private SkinInputMap skinInputMap;
	
	
	public InputMap(Control c)
	{
		this.control = c;
	}
	
	
	public void regKey(KB k, Runnable r)
	{
		map.put(k, r);
		EventType<KeyEvent> t = k.getEventType(); 
		addHandler(t, HPriority.USER_KB, null);
	}
	
	
	public void regKey(KB k, Func f)
	{
		map.put(k, f);
		EventType<KeyEvent> t = k.getEventType(); 
		addHandler(t, HPriority.USER_KB, null);
	}


	public void regFunc(Func f, Runnable r)
	{
		map.put(f, r);
	}
	

	/**
	 * Adds a user event handler which is guaranteed to be called before any of the skin's event handlers.
	 */
	public <T extends Event> void addHandler(EventType<T> type, EventHandler<T> h)
	{
		addHandler(type, HPriority.USER_EH, h);
	}

	
	private void handleEvent(Event ev)
	{
		if(ev.isConsumed())
		{
			// why is fx dispatching consumed events?
			return;
		}

		EventType<?> t = ev.getEventType();
		Object x = map.get(t);
		if(x instanceof EHandlers hs)
		{
			hs.forEachHandler((pri, h) ->
			{
				if(h == null)
				{
					handleKeyBindingEvent(ev);
				}
				else
				{
					h.handle(ev);
				}
				return !ev.isConsumed();
			});
		}
	}


	private void handleKeyBindingEvent(Event ev)
	{
		KB k = KB.fromKeyEvent((KeyEvent)ev);
		if(k != null)
		{
			boolean consume = handleKeyBinding(k);
			if(consume)
			{
				ev.consume();
			}
		}
	}


	// returns true if the event must be consumed
	private boolean handleKeyBinding(KB k)
	{
		Object v = map.get(k);
		if(v == null)
		{
			if(skinInputMap != null)
			{
				v = skinInputMap.valueFor(k);
			}
		}
		
		if(v != null)
		{
			if(v instanceof Func f)
			{
				return exec(f);
			}
			else if(v instanceof Runnable r)
			{
				r.run();
				return true;
			}
			else if(v instanceof BooleanSupplier h)
			{
				return h.getAsBoolean();
			}
		}
		return false;
	}
	
	
	private boolean execFunc(Func f)
	{
		Object v = map.get(f);
		if(v instanceof Runnable r)
		{
			r.run();
			return true;
		}
		else
		{
			return execDefaultFunc(f);
		}
	}
	
	
	private boolean execDefaultFunc(Func f)
	{
		if(skinInputMap != null)
		{
			return skinInputMap.execFunc(f);
		}
		return false;
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
			control.addEventHandler(t, eventHandler); // TODO if handler == null, then key bindings eH
		}
		hs.add(pri, handler);
	}
	
	
	public void setSkinInputMap(SkinInputMap m)
	{
		if(skinInputMap != null)
		{
			// remove any skin handlers
			for(Map.Entry<Object,Object> en: map.entrySet())
			{
				if(en.getKey() instanceof EventType t)
				{
					EHandlers hs = (EHandlers)en.getValue();
					if(hs.removeSkinHandlers())
					{
						map.remove(en.getKey());
						control.removeEventHandler(t, eventHandler);
					}
				}
			}
		}
		
		skinInputMap = m;
		
		if(skinInputMap != null)
		{
			skinInputMap.forEachHandler((t,p,h) ->
			{
				addHandler(t, p, h);
			});
		}
	}


	// returns true if the event must be consumed
	public boolean exec(Func f)
	{
		Object v = map.get(f);
		if(v instanceof Runnable r)
		{
			r.run();
			return true;
		}
		return execDefaultFunc(f);
	}
}
