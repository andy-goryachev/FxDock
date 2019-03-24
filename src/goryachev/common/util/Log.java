// Copyright © 2006-2019 Andy Goryachev <andy@goryachev.com>
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
 *   static methods
 * Log.configFromFile(File);
 * Log.reset();
 * Log. TODO connect loggers to appenders
 * Log.ex()
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
	private String name;
	private volatile boolean enabled = true;
	private volatile boolean printCallingMethod = true;
	// using unsynchronized list, which will be replaced with a new instance on any change
	private volatile CList<ILogWriter> writers = new CList<>(0);

	private static final CMap<String,Log> channels = new CMap<>();
	private static final CMap<String,LogWriter> writerByName = new CMap<>();
	private static volatile Log errorChannel = initErrorChannel();
	
	static
	{
		initConsole();
	}
	
	
	public Log(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public void err(Throwable e)
	{
		if(enabled)
		{
			LogEntry en = new LogEntry(e, null, 0);
			if(printCallingMethod)
			{
				en.setCaller(e, 0);
			}
			add(en);
		}
	}
	
	
	public void err(String message)
	{
		err(message, 2);
	}
	
	
	protected void err(String message, int level)
	{
		if(enabled)
		{
			Throwable e = new Throwable(message);
			LogEntry en = new LogEntry(e, null, level);
			if(printCallingMethod)
			{
				en.setCaller(e, level);
			}
			add(en);
		}
	}
	
	
	public void print()
	{
		if(enabled)
		{
			LogEntry en = new LogEntry("");
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b, Object c)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			Log.append(sb, c);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b, Object c, Object d)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			Log.append(sb, c);
			Log.append(sb, d);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b, Object c, Object d, Object e)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			Log.append(sb, c);
			Log.append(sb, d);
			Log.append(sb, e);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	protected void add(LogEntry en)
	{
		CList<ILogWriter> ws = writers;
		for(int i=ws.size()-1; i>=0; i--)
		{
			ws.get(i).write(en);
		}
	}
	
	
	public void setEnabled(boolean on)
	{
		enabled = on;
	}
	
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	
	public boolean getPrintCallingMethod()
	{
		return printCallingMethod;
	}
	
	
	public void setPrintCallingMethod(boolean on)
	{
		printCallingMethod = on;
	}


	public synchronized void addWriter(ILogWriter wr)
	{
		CList<ILogWriter> ws = new CList<>(writers);
		ws.add(wr);
		writers = ws;
	}
	
	
	public synchronized void removeWriter(ILogWriter wr)
	{
		CList<ILogWriter> ws = new CList<>(writers);
		ws.remove(wr);
		writers = ws;
	}
	
	
	private static Log initErrorChannel()
	{
		Log ch = get(null);
		ch.addWriter(ErrorLogWriter.instance);
		return ch;
	}
	
	
	/** sends an exception to the error channel */
	public static void ex(Throwable e)
	{
		errorChannel.err(e);
	}
	
	
	/** sends a message to the error channel */
	public static void ex(String message)
	{
		errorChannel.err(message, 2);
	}
	
	
	public static void info(String message)
	{
		errorChannel.print(message);
	}
	
	
	protected static void append(SB sb, Object x)
	{
		if(sb.isNotEmpty())
		{
			sb.append('\t');
		}
		
		sb.append(x);
	}
	
	
//	public static void print(Object a)
//	{
//		if(errorChannel.isEnabled())
//		{
//			SB sb = new SB();
//			append(sb, a);
//			
//			LogEntry en = new LogEntry(sb.toString());
//			if(errorChannel.getPrintCallingMethod())
//			{
//				en.setCaller(new Throwable(), 1);
//			}
//			errorChannel.add(en);
//		}
//	}
//	
//	
//	public static void print(Object a, Object b)
//	{
//		if(errorChannel.isEnabled())
//		{
//			SB sb = new SB();
//			append(sb, a);
//			append(sb, b);
//			
//			LogEntry en = new LogEntry(sb.toString());
//			if(errorChannel.getPrintCallingMethod())
//			{
//				en.setCaller(new Throwable(), 1);
//			}
//			errorChannel.add(en);
//		}
//	}
//	
//	
//	public static void print(Object a, Object b, Object c)
//	{
//		if(errorChannel.isEnabled())
//		{
//			SB sb = new SB();
//			append(sb, a);
//			append(sb, b);
//			append(sb, c);
//			
//			LogEntry en = new LogEntry(sb.toString());
//			if(errorChannel.getPrintCallingMethod())
//			{
//				en.setCaller(new Throwable(), 1);
//			}
//			errorChannel.add(en);
//		}
//	}
//	
//	
//	public static void print(Object a, Object b, Object c, Object d)
//	{
//		if(errorChannel.isEnabled())
//		{
//			SB sb = new SB();
//			append(sb, a);
//			append(sb, b);
//			append(sb, c);
//			append(sb, d);
//			
//			LogEntry en = new LogEntry(sb.toString());
//			if(errorChannel.getPrintCallingMethod())
//			{
//				en.setCaller(new Throwable(), 1);
//			}
//			errorChannel.add(en);
//		}
//	}
//	
//	
//	public static void print(Object a, Object b, Object c, Object d, Object e)
//	{
//		if(errorChannel.isEnabled())
//		{
//			SB sb = new SB();
//			append(sb, a);
//			append(sb, b);
//			append(sb, c);
//			append(sb, d);
//			append(sb, e);
//			
//			LogEntry en = new LogEntry(sb.toString());
//			if(errorChannel.getPrintCallingMethod())
//			{
//				en.setCaller(new Throwable(), 1);
//			}
//			errorChannel.add(en);
//		}
//	}
	
	
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


	// FIX hierarchy
	public synchronized static Log get(String name)
	{
		Log ch = channels.get(name);
		if(ch == null)
		{
			ch = new Log(name);
			channels.put(name, ch);
		}
		return ch;
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
		writerByName.put(wr.getName(), wr);
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
		writerByName.put(wr.getName(), wr);
		errorChannel.addWriter(wr);
	}
	
	
	public static synchronized void addErrorWriter(LogWriter wr)
	{
		writerByName.put(wr.getName(), wr);
		errorChannel.addWriter(wr);
	}
	
	
	public static synchronized void addLogWriter(LogWriter wr)
	{
		writerByName.put(wr.getName(), wr);
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
		for(Log ch: channels.values())
		{
			ch.addWriter(wr);
		}
	}
	
	
	public synchronized static void removeMonitor(ILogWriter wr)
	{
		for(Log ch: channels.values())
		{
			ch.removeWriter(wr);
		}
	}


	public static FileLogWriter createFileWriter(String name, File file, long maxSize, int rounds, boolean async)
	{
		FileLogWriter wr = new FileLogWriter(name, file, maxSize, rounds);
		wr.setAsync(async);
		addLogWriter(wr);
		return wr;
	}
	
	
	public static synchronized void connect(String channelName, String writerName)
	{
		try
		{
			Log ch = get(channelName);
			LogWriter wr = writerByName.get(writerName);
			wr.getName();
			ch.addWriter(wr);
		}
		catch(Exception e)
		{
			ex(e);
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
		Log log = get(name);
		log.setEnabled(on);
	}
}