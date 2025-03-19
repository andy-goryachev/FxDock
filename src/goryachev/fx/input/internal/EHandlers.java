// Copyright © 2024-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.input.internal;
import goryachev.common.util.CList;
import java.util.function.Predicate;
import javafx.event.Event;
import javafx.event.EventHandler;


/**
 * Prioritized Event Handler List.
 */
public class EHandlers
{
	public static interface Client<T extends Event>
	{
		// returns true if the caller should continue iterating over handlers, false to stop
		public boolean process(HPriority pri, EventHandler<T> handler);
	}
	
	
	//
	
	
	// ordered list of (priority, handlers[])[]
	// key binding handlers are not stored (a null is used to signify the key binding handler)
	private final CList<Object> list = new CList<>();
	
	
	public EHandlers()
	{
	}
	

	public void add(HPriority pri, EventHandler<?> h)
	{
		int ix = findPosition(pri);
		if(ix < 0)
		{
			ix = -ix;
			insert(ix, pri);
			// don't store the key binding handler which is null
			if(h != null)
			{
				insert(++ix, h);
			}
		}
		else
		{
			// don't store the key binding handler which is null
			if(h != null)
			{
				insert(ix, h);
			}
		}
	}


	// returns true if no more skin event handlers left
	public boolean removeSkinHandlers()
	{
		return removeHandlers((p) -> p.isSkin());
	}
	
	
	private boolean removeHandlers(Predicate<HPriority> toRemove)
	{
		boolean remove = false;
		for(int i=0; i<list.size(); )
		{
			Object v = list.get(i);
			if(v instanceof HPriority p)
			{
				if(toRemove.test(p))
				{
					remove = true;
					list.remove(i);
				}
				else
				{
					remove = false;
					i++;
				}
			}
			else
			{
				if(remove)
				{
					list.remove(i);
				}
				else
				{
					i++;
				}
			}
		}
		return list.size() == 0;
	}


	private void insert(int ix, Object item)
	{
		if(ix < list.size())
		{
			list.add(ix, item);
		}
		else
		{
			list.add(item);
		}
	}


	// returns >= 0: the caller needs to insert the handler
	// returns < 0: the caller needs to insert priority, then the handler
	private int findPosition(HPriority pri)
	{
		boolean found = false;
		int sz = list.size();
		for(int i=0; i<sz; i++)
		{
			Object v = list.get(i);
			if(v instanceof HPriority p)
			{
				if(p.priority == pri.priority)
				{
					found = true;
					continue;
				}
				else if(p.priority < pri.priority)
				{
					return found ? i : -(i + 1);
				}
			}
		}
		return found ? sz : -(sz + 1);
	}


	private boolean isKeyBindingHandler(int ix)
	{
		if((ix >= 0) && (ix < list.size()))
		{
			Object v = list.get(ix);
			return (v instanceof HPriority);
		}
		return true;
	}


	public <T extends Event> void forEachHandler(Client<T> c)
	{
		HPriority pri = null;
		boolean stop;
		int sz = list.size();
		for(int i=0; i<sz; i++)
		{
			Object x = list.get(i);
			if(x instanceof HPriority p)
			{
				pri = p;
				if(isKeyBindingHandler(i + 1))
				{
					stop = !c.process(pri, null);
				}
				else
				{
					continue;
				}
			}
			else
			{
				stop = !c.process(pri, (EventHandler<T>)x);
			}
			
			if(stop)
			{
				break;
			}
		}
	}
}
