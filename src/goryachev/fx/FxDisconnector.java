// Copyright © 2021-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import goryachev.common.util.IDisconnectable;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;


/**
 * Fx Disconnector.
 */
public class FxDisconnector
	implements IDisconnectable
{
	private final CList<IDisconnectable> items = new CList();
	private static final Object KEY = new Object();
	
	
	public FxDisconnector()
	{
	}
	
	
	public static FxDisconnector get(Node n)
	{
		Object x = n.getProperties().get(KEY);
		if(x instanceof FxDisconnector)
		{
			return (FxDisconnector)x;
		}
		FxDisconnector d = new FxDisconnector();
		n.getProperties().put(KEY, d);
		return d;
	}
	
	
	public static void disconnect(Node n)
	{
		Object x = n.getProperties().get(KEY);
		if(x instanceof FxDisconnector d)
		{
			d.disconnect();
		}
	}
	
	
	public void addDisconnectable(IDisconnectable d)
	{
		items.add(d);
	}


	@Override
	public void disconnect()
	{
		for(int i=items.size()-1; i>=0; i--)
		{
			IDisconnectable d = items.remove(i);
			d.disconnect();
		}
	}
	
	
	// change listeners
	
	
	public IDisconnectable addChangeListener(Runnable callback, ObservableValue<?> ... props)
	{
		return addChangeListener(callback, false, props);
	}
	

	public IDisconnectable addChangeListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		ChLi li = new ChLi()
		{
			@Override
			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}

			
			@Override
			public void changed(ObservableValue p, Object oldValue, Object newValue)
			{
				onChange.run();
			}
		};
		
		items.add(li);
		
		for(ObservableValue p: props)
		{
			p.addListener(li);
		}
		
		if(fireImmediately)
		{
			onChange.run();
		}
		
		return li;
	}
	
	
	public <T> IDisconnectable addChangeListener(ObservableValue<T> prop, ChangeListener<T> li)
	{
		return addChangeListener(prop, false, li);
	}
	
	
	public <T> IDisconnectable addChangeListener(ObservableValue<T> prop, boolean fireImmediately, ChangeListener<T> li)
	{
		IDisconnectable d = new IDisconnectable()
		{
			@Override
			public void disconnect()
			{
				prop.removeListener(li);
			}
		};
		
		items.add(d);
		prop.addListener(li);
		
		if(fireImmediately)
		{
			T v = prop.getValue();
			li.changed(prop, null, v);
		}
		
		return d;
	}


	public IDisconnectable addWeakChangeListener(Runnable onChange, ObservableValue<?> ... props)
	{
		return addWeakChangeListener(onChange, false, props);
	}


	public IDisconnectable addWeakChangeListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		WeakReference<Runnable> ref = new WeakReference(onChange);

		ChLi li = new ChLi()
		{
			@Override
			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}


			@Override
			public void changed(ObservableValue p, Object oldValue, Object newValue)
			{
				Runnable r = ref.get();
				if(r == null)
				{
					disconnect();
				}
				else
				{
					r.run();
				}
			}
		};

		items.add(li);

		for(ObservableValue p: props)
		{
			p.addListener(li);
		}

		if(fireImmediately)
		{
			onChange.run();
		}

		return li;
	}


	public <T> IDisconnectable addWeakChangeListener(ObservableValue<T> prop, ChangeListener<T> li)
	{
		return addChangeListener(prop, false, li);
	}


	public <T> IDisconnectable addWeakChangeListener(ObservableValue<T> prop, boolean fireImmediately, ChangeListener<T> listener)
	{
		WeakReference<ChangeListener<T>> ref = new WeakReference<>(listener);

		ChLi<T> d = new ChLi<T>()
		{
			@Override
			public void disconnect()
			{
				prop.removeListener(this);
			}


			@Override
			public void changed(ObservableValue<? extends T> p, T oldValue, T newValue)
			{
				ChangeListener<T> li = ref.get();
				if(li == null)
				{
					disconnect();
				}
				else
				{
					li.changed(p, oldValue, newValue);
				}
			}
		};

		items.add(d);
		prop.addListener(d);

		if(fireImmediately)
		{
			T v = prop.getValue();
			listener.changed(prop, null, v);
		}
		
		return d;
	}
	
	
	// invalidation listeners
	
	
	public IDisconnectable addInvalidationListener(Runnable callback, ObservableValue<?> ... props)
	{
		return addInvalidationListener(callback, false, props);
	}
	

	public IDisconnectable addInvalidationListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		InLi li = new InLi()
		{
			@Override
			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}


			@Override
			public void invalidated(Observable p)
			{
				onChange.run();
			}
		};
		
		items.add(li);
		
		for(ObservableValue p: props)
		{
			p.addListener(li);
		}
		
		if(fireImmediately)
		{
			onChange.run();
		}
		
		return li;
	}
	
	
	public <T> IDisconnectable addInvalidationListener(ObservableValue<T> prop, InvalidationListener li)
	{
		return addInvalidationListener(prop, false, li);
	}
	
	
	public <T> IDisconnectable addInvalidationListener(ObservableValue<T> prop, boolean fireImmediately, InvalidationListener li)
	{
		IDisconnectable d = new IDisconnectable()
		{
			@Override
			public void disconnect()
			{
				prop.removeListener(li);
			}
		};
		
		items.add(d);
		prop.addListener(li);
		
		if(fireImmediately)
		{
			li.invalidated(prop);
		}
		
		return d;
	}


	public IDisconnectable addWeakInvalidationListener(Runnable onChange, ObservableValue<?> ... props)
	{
		return addWeakInvalidationListener(onChange, false, props);
	}


	public IDisconnectable addWeakInvalidationListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		WeakReference<Runnable> ref = new WeakReference(onChange);

		InLi li = new InLi()
		{
			@Override
			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}


			@Override
			public void invalidated(Observable p)
			{
				Runnable r = ref.get();
				if(r == null)
				{
					disconnect();
				}
				else
				{
					r.run();
				}
			}
		};

		items.add(li);

		for(ObservableValue p: props)
		{
			p.addListener(li);
		}

		if(fireImmediately)
		{
			onChange.run();
		}

		return li;
	}


	public IDisconnectable addWeakInvalidationListener(ObservableValue<?> prop, InvalidationListener li)
	{
		return addWeakInvalidationListener(prop, false, li);
	}


	public IDisconnectable addWeakInvalidationListener(ObservableValue<?> prop, boolean fireImmediately, InvalidationListener listener)
	{
		WeakReference<InvalidationListener> ref = new WeakReference<>(listener);

		InLi d = new InLi()
		{
			@Override
			public void disconnect()
			{
				prop.removeListener(this);
			}


			@Override
			public void invalidated(Observable p)
			{
				InvalidationListener li = ref.get();
				if(li == null)
				{
					disconnect();
				}
				else
				{
					li.invalidated(p);
				}
			}
		};

		items.add(d);
		prop.addListener(d);

		if(fireImmediately)
		{
			listener.invalidated(prop);
		}
		
		return d;
	}
	
	
	// list change listeners
	
	
	public <T> IDisconnectable addListChangeListener(ObservableList<T> list, ListChangeListener<T> listener)
	{
		IDisconnectable d = new IDisconnectable()
		{
			@Override
			public void disconnect()
			{
				list.removeListener(listener);
			}
		};
		
		items.add(d);
		list.addListener(listener);
		
		return d;
	}
	
	
	public <T> IDisconnectable addWeakListChangeListener(ObservableList<T> list, ListChangeListener<T> listener)
	{
		WeakReference<ListChangeListener<T>> ref = new WeakReference<>(listener);

		LiChLi<T> li = new LiChLi<T>()
		{
			@Override
			public void disconnect()
			{
				list.removeListener(this);
			}

			@Override
			public void onChanged(Change<? extends T> ch)
			{
				ListChangeListener<T> li = ref.get();
				if(li == null)
				{
					disconnect();
				}
				else
				{
					li.onChanged(ch);
				}
			}
		};
		
		items.add(li);
		list.addListener(li);
		
		return li;
	}
	
	
	// TODO event handlers
	
	
	// TODO event filters
	
	
	// bidirectional binding
	
	
	public <T> IDisconnectable bindBidirectional(Property<T> p1, Property<T> p2)
	{
		Bindings.bindBidirectional(p1, p2);
		IDisconnectable d = new IDisconnectable()
		{
			@Override
			public void disconnect()
			{
				Bindings.unbindBidirectional(p1, p2);
			}
		};
		items.add(d);
		return d;
	}
	
	
	//
	
	
	protected static abstract class ChLi<T> implements IDisconnectable, ChangeListener<T> { }
	
	protected static abstract class InLi implements IDisconnectable, InvalidationListener { }
	
	protected static abstract class LiChLi<T> implements IDisconnectable, ListChangeListener<T> { }
}
