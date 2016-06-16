// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import com.sun.javafx.css.StyleManager;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import javafx.application.Platform;


/**
 * CssLoader.
 */
public class CssLoader
{
	public static final String CONTINUOUS_REFRESH_PROPERTY = "CssLoader.ContinuousRefresh";
	public static final String PREFIX = "embeddedcss";
	private static CssLoader instance;
	private String url;
	private CssGenerator generator;
	
	
	protected CssLoader()
	{
		try
		{
			URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory()
			{
				public URLStreamHandler createURLStreamHandler(String protocol)
				{
					if(PREFIX.equals(protocol))
					{
						return new URLStreamHandler()
						{
							protected URLConnection openConnection(URL url) throws IOException
							{
								return new URLConnection(url)
								{
									public void connect() throws IOException
									{
									}
									
									public InputStream getInputStream() throws IOException
									{
										byte[] b = decode(url.toString());
										return new ByteArrayInputStream(b);
									}
								};
							}
						};
					}
					return null;
				}
			});
			
			if(Boolean.getBoolean(CONTINUOUS_REFRESH_PROPERTY))
			{
				Thread t = new Thread("reloading css")
				{
					public void run()
					{
						for(;;)
						{
							CKit.sleep(999);
							updateStyles();
						}
					}
				};
				t.setDaemon(true);
				t.start();
			}
		}
		catch(Throwable e)
		{
			// css will be disabled
			Log.fail(e);
		}
	}
	
	
	public static void setStyles(CssGenerator g)
	{
		instance().setGenerator(g);
	}
	
	
	private static CssLoader instance()
	{
		if(instance == null)
		{
			instance = new CssLoader();
		}
		return instance;
	}
	
	
	public void setGenerator(CssGenerator g)
	{
		generator = g;
		updateStyles();
	}
	
	
	public static String encode(String css)
	{
		return PREFIX + ":" + Base64.encode(CKit.getBytes(css));
	}
	
	
	public static byte[] decode(String css) throws IOException
	{
		css = css.substring(PREFIX.length() + 1);
		return Base64.decode(CKit.getBytes(css));
	}
	
	
	public synchronized void updateStyles()
	{
		try
		{
			String css = generator.generateStyleSheet();
			String encoded = encode(css);
			
			// there is no way to set the stylesheet programmatically
			// so we have to jump through these hoops: set url factory, encode, decode...
			
			if(CKit.notEquals(encoded, url))
			{
				String old = url;
				url = encoded;
				
				if(Platform.isFxApplicationThread())
				{
					update(old, url);
				}
				else
				{
					System.err.println("reloading css");
					
					Platform.runLater(() -> update(old, url));
				}
			}
		}
		catch(Throwable e)
		{
			Log.fail(e);
		}
	}
		
		
	protected void update(String old, String cur)
	{
		if(old != null)
		{
			StyleManager.getInstance().removeUserAgentStylesheet(old);
		}
		StyleManager.getInstance().addUserAgentStylesheet(cur);
	}
}
