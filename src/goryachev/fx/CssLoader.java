// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.CSet;
import goryachev.common.util.SB;
import goryachev.common.util.UrlStreamFactory;
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
	protected static final Log log = Log.get("CssLoader");
	public static final String PREFIX = "javafxcss";
	private static String url;
	private static Supplier<FxStyleSheet> generator;
	private static CSet<String> styles;
	
	
	static
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
							try
							{
								byte[] b = decode(url.toString());
								return new ByteArrayInputStream(b);
							}
							catch(Throwable e)
							{
								throw new IOException(e);
							}
						}
					};
				}
			});
			
			if(FxConfig.cssRefreshEnabled())
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
			log.error(e);
		}
	}
	
	
	public static synchronized void setStyles(Supplier<FxStyleSheet> g)
	{
		generator = g;
		updateStyles();
	}
	
	
	public static String getCurrentStyleSheet()
	{
		return url;
	}
	
	
	protected static String encode(String css)
	{
		return PREFIX + ":" + Base64.encode(CKit.getBytes(css));
	}
	
	
	protected static byte[] decode(String css) throws Exception
	{
		css = css.substring(PREFIX.length() + 1);
		return Base64.decode(CKit.getBytes(css));
	}
	

	public static synchronized void updateStyles()
	{
		try
		{
			if(generator == null)
			{
				return;
			}
			
			String css = generator.get().generateStyleSheet();
			if(styles != null)
			{
				SB sb = new SB(2048);
				sb.append(css);
				sb.nl();
				for(String v: styles)
				{
					sb.append(v);
					sb.nl();
				}
				css = sb.toString();
			}
			
			String encoded = encode(css);
			
			// there is no way to set the stylesheet programmatically
			// so we have to jump through these hoops: set url factory, encode, decode...
			
			if(CKit.notEquals(encoded, url))
			{
				log.trace(css);
				
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
				
				log.trace(css);
			}
		}
		catch(Error e)
		{
			log.error(e);
			throw e;
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}
		
		
	protected static void update(String old, String cur)
	{
		FX.applyStyleSheet(old, cur);
		
		if(old == null)
		{
			log.debug("css loaded");
		}
		else
		{
			log.debug("css reloaded");
		}
	}
	
	
	public static synchronized void addGlobalStyle(String style)
	{
		if(styles == null)
		{
			styles = new CSet<>();
		}
		styles.add(style);
		updateStyles();
	}
}
