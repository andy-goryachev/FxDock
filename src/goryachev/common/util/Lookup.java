// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Service lookup.
 */
public class Lookup
{
	public interface ServiceProvider<T>
	{
		public T createService();	
	}
	
	//
	
	private static final CMap<Class,ServiceProvider> providers = new CMap();
	
	
	/** lookup service */
	public static <T> T getService(Class<T> type)
	{
		ServiceProvider<T> p = getProvider(type);
		if(p == null)
		{
			throw new Error("Provider not registered for " + type);
		}
		
		return p.createService();
	}
	
	
	private synchronized static <T> ServiceProvider<T> getProvider(Class<T> type)
	{
		return providers.get(type);
	}
	
	
	/** register service provider */
	public synchronized static <T> void registerProvider(Class<T> type, ServiceProvider<T> provider)
	{
		if(providers.containsKey(type))
		{
			throw new Error("Provider already registered: " + type);
		}
		
		providers.put(type, provider);
	}
}
