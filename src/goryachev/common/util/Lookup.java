// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Service Registry.
 */
public class Lookup
{
	@FunctionalInterface
	public interface Provider<T>
	{
		public T createService();	
	}
	
	//
	
	private static final CMap<Class,Provider> providers = new CMap();
	
	
	/** looks up a service */
	public static <T> T get(Class<T> type)
	{
		Provider<T> p = getProvider(type);
		if(p == null)
		{
			throw new Error("Must register a provider before first use: Lookup.register(" + type + ", provider);");
		}
		
		return p.createService();
	}
	
	
	private synchronized static <T> Provider<T> getProvider(Class<T> type)
	{
		return providers.get(type);
	}
	
	
	/** register a service provider */
	public synchronized static <T> void register(Class<T> type, Provider<T> provider)
	{
		if(providers.containsKey(type))
		{
			throw new Error("Provider already registered: " + type);
		}
		
		providers.put(type, provider);
	}
	
	
	/** register a service provider, unless another provider is already registered, in which case do nothing */
	public synchronized static <T> void registerIfEmpty(Class<T> type, Provider<T> provider)
	{
		if(!providers.containsKey(type))
		{
			providers.put(type, provider);
		}
	}
}
