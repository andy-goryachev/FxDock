// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;


/**
 * Binder.
 */
public class Binder
{
	/** fires handler if any of the observables change */
	public static void onChange(Runnable handler, Observable ... props)
	{
		onChange(handler, false, props);
	}
	
	
	/** fires handler if any of the observables change */
	public static void onChange(Runnable handler, boolean immediately, Observable ... props)
	{
//		Helper li = new Helper(handler);

		for(Observable p: props)
		{
//			p.addListener(li);
			
			// weak listener gets collected... but why??
			p.addListener((src) -> handler.run());
		}
		
		if(immediately)
		{
			handler.run();
		}
	}


	//


	public static class Helper
		implements InvalidationListener
	{
		private final WeakReference<Runnable> ref;


		public Helper(Runnable handler)
		{
			ref = new WeakReference<>(handler);
		}


		public void invalidated(Observable observable)
		{
			final Runnable h = ref.get();
			if(h == null)
			{
//				observable.removeListener(this);
			}
			else
			{
				h.run();
			}
		}
	}
}
