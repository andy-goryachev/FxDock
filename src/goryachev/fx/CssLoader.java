// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.UrlStreamFactory;
import goryachev.fx.hacks.FxHacks;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.function.Supplier;
import javafx.application.Platform;


/**
 * JavaFX CSS Loader uses a URL stream factory to register a special protocol
 * in order to be able to change fx style sheets dynamically. 
 */
public class CssLoader
{
	public static final String PREFIX = "javafxcss";
	private static CssLoader instance;
	private String url;
	private Supplier<FxStyleSheet> generator;
	
	
	protected CssLoader()
	{
		try
		{
			UrlStreamFactory.registerHandler(PREFIX, new URLStreamHandler()
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
			});
			
			if(FxConfig.continuousCssRefresh())
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
			Log.ex(e);
		}
	}
	
	
	public static void setStyles(Supplier<FxStyleSheet> g)
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
	
	
	public static String getCurrentStyleSheet()
	{
		if(instance != null)
		{
			return instance.url;
		}
		return null;
	}
	
	
	public void setGenerator(Supplier<FxStyleSheet> g)
	{
		this.generator = g;
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
			if(generator == null)
			{
				return;
			}
			
			String css = generator.get().generateStyleSheet();
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
					Platform.runLater(() -> update(old, url));
				}
				
				if(FxConfig.dumpCSS())
				{
					// stdout is ok here
					System.out.println(css);
				}
			}
		}
		catch(Error e)
		{
			Log.ex(e);
			throw e;
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
		
		
	protected void update(String old, String cur)
	{
		FxHacks.get().applyStyleSheet(old, cur);
	}
}
