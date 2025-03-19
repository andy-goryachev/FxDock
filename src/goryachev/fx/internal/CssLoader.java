// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.log.Log;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.CSet;
import goryachev.common.util.SB;
import goryachev.fx.FX;
import goryachev.fx.FxFlags;
import goryachev.fx.FxStyleSheet;
import goryachev.fx.settings.WindowMonitor;
import java.util.function.Supplier;
import javafx.application.Platform;


/**
 * JavaFX CSS Loader uses a URL stream factory to register a special protocol
 * in order to be able to change fx style sheets dynamically. 
 */
public class CssLoader
{
	/** enables stylesheet auto refresh */
	public static final boolean REFRESH = Boolean.getBoolean(FxFlags.CSS_REFRESH);
	/** dumps the stylesheet to stdout */
	public static final boolean DUMP = Boolean.getBoolean(FxFlags.CSS_DUMP);

	private static final Log log = Log.get("CssLoader");
	
	private static RefreshThread refreshThread;
	private static String url;
	private static Supplier<FxStyleSheet> generator;
	private static CSet<String> styles;
	
	
	public static synchronized void setGenerator(Supplier<FxStyleSheet> gen)
	{
		FX.checkThread();
		
		generator = gen;
		
		// ensure WinMonitor is initialized 
		WindowMonitor.forWindow(null);

		updateStyles();

		if(REFRESH)
		{
			if(gen == null)
			{
				if(refreshThread != null)
				{
					refreshThread.cancel();
					refreshThread = null;
				}
			}
			else
			{
				if(refreshThread == null)
				{
					refreshThread = new RefreshThread();
					refreshThread.start();
				}
			}
		}
	}
	
	
	public static String getCurrentStyleSheet()
	{
		return url;
	}
	
	
	protected static String encode(String css)
	{
		return "data:text/css;base64," + Base64.encode(CKit.getBytes(css));
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
			if(CKit.notEquals(encoded, url))
			{
				log.trace(css);
				if(DUMP)
				{
					System.out.println(css);
				}
				
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

	
	//
	
	
	static class RefreshThread extends Thread
	{
		private volatile boolean running = true;
		
		
		public RefreshThread()
		{
			super("reloading css");
			setDaemon(true);
		}


		@Override
		public void run()
		{
			while(running)
			{
				CKit.sleep(999);

				if(running)
				{
					Platform.runLater(() ->
					{
						updateStyles();
					});
				}
			}
		}
		
		
		public void cancel()
		{
			running = false;
		}
	}
}
