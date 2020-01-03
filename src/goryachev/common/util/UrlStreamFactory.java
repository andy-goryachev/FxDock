// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Locale;


/**
 * Global Url Stream Factory simplifies plugging in multiple custom URLStreamHandlers.
 */
public class UrlStreamFactory
{
	private static CMap<String,URLStreamHandler> handlers;
	

	/** a centalized place to register a URLStreamHandler for a custom protocol */
	public static synchronized void registerHandler(String protocol, URLStreamHandler h) throws Exception
	{
		if(handlers == null)
		{
			handlers = new CMap<>();
			
			URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory()
			{
				public URLStreamHandler createURLStreamHandler(String protocol)
				{
					URLStreamHandler h = getHandler(protocol);
					return h;
				}
			});
		}
		
		handlers.put(protocol.toLowerCase(Locale.US), h);
	}
	
	
	protected static URLStreamHandler getHandler(String protocol)
	{
		return handlers.get(protocol.toLowerCase(Locale.US));
	}
}
