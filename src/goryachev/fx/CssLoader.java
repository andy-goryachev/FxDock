// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.UrlStreamFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.function.Supplier;
import com.sun.javafx.css.StyleManager;
import javafx.application.Platform;


/**
 * JavaFX CSS Loader uses a URL stream factory to register a special protocol
 * in order to be able to change fx style sheets dynamically. 
 * 
 * -Dcss.refresh=true
 * -Dcss.dump=true
 */
public class CssLoader
{
	/** -Dcss.refresh=true forces periodic check for css changes */ 
	public static final String CONTINUOUS_REFRESH_PROPERTY = "css.refresh";
	/** -Dcss.dump=true results in CSS being dumped to stderr */
	public static final String DUMP_CSS_PROPERTY = "css.dump";
	
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
					// stderr is ok here
					System.err.println("reloading css");
					
					Platform.runLater(() -> update(old, url));
				}
				
				if(Boolean.getBoolean(DUMP_CSS_PROPERTY))
				{
					// stderr is ok here
					System.err.println(css);
				}
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
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
