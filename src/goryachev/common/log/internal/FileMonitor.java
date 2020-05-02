// Copyright © 2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.internal;
import goryachev.common.util.CTask;
import goryachev.common.util.SystemTask;
import java.io.File;
import java.util.function.Consumer;


/**
 * Monitors log configuration file.
 */
public class FileMonitor
{
	protected final File file;
	protected final long period;
	protected final Consumer<File> reloader;
	private long last;
	private boolean disabled;
	
	
	public FileMonitor(File f, long period, Consumer<File> reloader)
	{
		this.file = f;
		this.period = period;
		this.reloader = reloader;
	}
	
	
	public void cancel()
	{
		disabled = true;
	}
	
	
	public void start()
	{
		last = file.lastModified();
		
		SystemTask.schedule(period, period, this::check);
	}
	
	
	protected void check()
	{
		if(disabled)
		{
			return;
		}
		
		long t = file.lastModified();
		if(t != last)
		{
			last = t;
			
			triggerUpdate();
		}
	}
	
	
	protected void triggerUpdate()
	{
		CTask.submit(() -> 
		{
			reloader.accept(file);
			SystemTask.schedule(period, this::check);
		});
	}
}
