// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.util.log.ConsoleLogWriter;
import goryachev.common.util.log.ErrorLogWriter;
import goryachev.common.util.log.FileLogWriter;
import goryachev.common.util.log.LogEntry;
import goryachev.common.util.log.LogWriter;
import goryachev.common.util.platform.SysInfo;
import java.io.File;


/**
 * Global logging subsystem.
 * 
 * Log.configFromFile(File);
 * Log.reset();
 * Log. FIX connect loggers to appenders
 * 
 * Log.conf("ChannelName", Log.Level.INFO);
 * 
 * Log log = Log.get("ChannelName");
 * log.err(...);
 * log.warn(...);
 * log.info(...);
 * log.debug(...);
 * 
 */
public class Log
{
	private static final CMap<String,CLog> channels = new CMap();
	private static final CMap<String,LogWriter> writers = new CMap();
	private static volatile CLog errorChannel = initErrorChannel();
	
	
	private Log()
	{
	}
	
	
	private static CLog initErrorChannel()
	{
		CLog ch = getLog(null);
		ch.addWriter(ErrorLogWriter.instance);
		return ch;
	}
	
	
	public static void err(Throwable e)
	{
		errorChannel.err(e);
	}
	
	
	public static void err(String message)
	{
		errorChannel.err(message, 2);
	}
	
	
	protected static void append(SB sb, Object x)
	{
		if(sb.isNotEmpty())
		{
			sb.append('\t');
		}
		
		sb.append(x);
	}
	
	
	public static void print(Object a)
	{
		if(errorChannel.isEnabled())
		{
			SB sb = new SB();
			append(sb, a);
			
			LogEntry en = new LogEntry(sb.toString());
			if(errorChannel.getPrintCallingMethod())
			{
				en.setCaller(new Throwable(), 1);
			}
			errorChannel.add(en);
		}
	}
	
	
	public static void print(Object a, Object b)
	{
		if(errorChannel.isEnabled())
		{
			SB sb = new SB();
			append(sb, a);
			append(sb, b);
			
			LogEntry en = new LogEntry(sb.toString());
			if(errorChannel.getPrintCallingMethod())
			{
				en.setCaller(new Throwable(), 1);
			}
			errorChannel.add(en);
		}
	}
	
	
	public static void print(Object a, Object b, Object c)
	{
		if(errorChannel.isEnabled())
		{
			SB sb = new SB();
			append(sb, a);
			append(sb, b);
			append(sb, c);
			
			LogEntry en = new LogEntry(sb.toString());
			if(errorChannel.getPrintCallingMethod())
			{
				en.setCaller(new Throwable(), 1);
			}
			errorChannel.add(en);
		}
	}
	
	
	public static void print(Object a, Object b, Object c, Object d)
	{
		if(errorChannel.isEnabled())
		{
			SB sb = new SB();
			append(sb, a);
			append(sb, b);
			append(sb, c);
			append(sb, d);
			
			LogEntry en = new LogEntry(sb.toString());
			if(errorChannel.getPrintCallingMethod())
			{
				en.setCaller(new Throwable(), 1);
			}
			errorChannel.add(en);
		}
	}
	
	
	public static void print(Object a, Object b, Object c, Object d, Object e)
	{
		if(errorChannel.isEnabled())
		{
			SB sb = new SB();
			append(sb, a);
			append(sb, b);
			append(sb, c);
			append(sb, d);
			append(sb, e);
			
			LogEntry en = new LogEntry(sb.toString());
			if(errorChannel.getPrintCallingMethod())
			{
				en.setCaller(new Throwable(), 1);
			}
			errorChannel.add(en);
		}
	}
	
	
	public static void writeStartupLog(File f)
	{
		try
		{
			File p = f.getParentFile();
			if(p != null)
			{
				p.mkdirs();
			}
			
			// TODO logger errors?
			CKit.write(f, SysInfo.getSystemInfo());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public synchronized static CLog getLog(String name)
	{
		CLog ch = channels.get(name);
		if(ch == null)
		{
			ch = new CLog(name);
			channels.put(name, ch);
		}
		return ch;
	}
	
	
	public static CLog get(String name)
	{
		return getLog(name);
	}


	public static void init(File dir)
	{
		// write startup log first
		try
		{
			writeStartupLog(new File(dir, "startup.log"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// TODO read "logger.config"
		// configure
		
		initConsole();
		
		LogWriter wr = new FileLogWriter("file", new File(dir, "error.log"), 5000000L, 5);
		wr.setAsync(true);
		writers.put(wr.getName(), wr);
		errorChannel.addWriter(wr);
		
		Runtime.getRuntime().addShutdownHook(new Thread("waiting for logger shutdown...")
		{
			public void run()
			{
				onShutdown();
			}
		});
	}
	
	
	public static void initConsole()
	{
		LogWriter wr = new ConsoleLogWriter("console");
		wr.setAsync(true);
		writers.put(wr.getName(), wr);
		errorChannel.addWriter(wr);
	}
	
	
	public static synchronized void addErrorWriter(LogWriter wr)
	{
		writers.put(wr.getName(), wr);
		errorChannel.addWriter(wr);
	}
	
	
	public static synchronized void addWriter(LogWriter wr)
	{
		writers.put(wr.getName(), wr);
	}
	
	
	protected static void onShutdown()
	{
		try
		{
			// TODO wait for all threads to finish writing, but no more than X
//			for(CLog ch: channels.values())
//			{
//				ch.setEnabled(false);
//			}
//			
//			for(LogWriter wr: writers.values())
//			{
//				wr.drain();
//			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public synchronized static void addMonitor(ILogWriter wr)
	{
		for(CLog ch: channels.values())
		{
			ch.addWriter(wr);
		}
	}
	
	
	public synchronized static void removeMonitor(ILogWriter wr)
	{
		for(CLog ch: channels.values())
		{
			ch.removeWriter(wr);
		}
	}


	public static FileLogWriter createFileWriter(String name, File file, long maxSize, int rounds, boolean async)
	{
		FileLogWriter wr = new FileLogWriter(name, file, maxSize, rounds);
		wr.setAsync(async);
		addWriter(wr);
		return wr;
	}
	
	
	public static synchronized void connect(String channelName, String writerName)
	{
		try
		{
			CLog ch = getLog(channelName);
			LogWriter wr = writers.get(writerName);
			wr.getName();
			ch.addWriter(wr);
		}
		catch(Exception e)
		{
			err(e);
		}
	}
	
	
	public static void addErrorChannelMonitor(ErrorLogWriter.Monitor m)
	{
		ErrorLogWriter.instance.addMonitor(m);
	}
	
	
	public static void removeErrorChannelMonitor(ErrorLogWriter.Monitor m)
	{
		ErrorLogWriter.instance.removeMonitor(m);
	}
	
	
	public static void conf(String name, boolean on)
	{
		CLog log = get(name);
		log.setEnabled(on);
	}
}