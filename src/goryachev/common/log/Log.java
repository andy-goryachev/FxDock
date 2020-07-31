// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.log.internal.ConsoleAppender;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSet;
import goryachev.common.util.SB;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;


/**
 * A Logger Facade channel.
 */
public class Log
{
	private final String name;
	private Log parent;
	private LogLevel level;
	private boolean needsCaller;
	private CopyOnWriteArrayList<AppenderBase> appenders = new CopyOnWriteArrayList();
	private final CMap<String,Log> children = new CMap(0);
	protected static AbstractLogConfig config = LogUtil.createDisabledLogConfig();
	protected static final CSet<String> ignore = LogUtil.initIgnoreClassNames();
	protected static final CList<AppenderBase> allAppenders = new CList();
	protected static final Log root = new Log(null, null);


	protected Log(Log parent, String name)
	{
		this.parent = parent;
		this.name = name;
	}
	
	
	/** returns a channel instance for the specified name */
	public static synchronized Log get(String name)
	{
		String[] ss = CKit.split(name, '.');
		Log log = root;
		
		for(String s: ss)
		{
			Log ch = log.children.get(s);
			if(ch == null)
			{
				ch = new Log(log, s);
				ch.needsCaller = log.needsCaller;
				log.children.put(s, ch);				

				ch.applyConfig(config);
			}
			log = ch;
		}
		
		return log;
	}
	
	
	public static void initForDebug()
	{
		SimpleLogConfig c = new SimpleLogConfig();
		c.setDefaultLogLevel(LogLevel.INFO);
		c.addAppender(new ConsoleAppender(System.out));
		setConfig(c);
	}
	
	
	public static void setConfig(AbstractLogConfig cf)
	{
		if(cf == null)
		{
			cf = LogUtil.createDisabledLogConfig();
		}
		
		List<AppenderBase> as;
		try
		{
			as = cf.getAppenders();
		}
		catch(Throwable e)
		{
			LogUtil.internalError(e);
			return;
		}
	
		removeAppenders();
		
		config = cf;
		
		try
		{
			LogLevel lv = cf.getDefaultLogLevel();
			root.setLevel(lv);
			root.applyConfig(cf);
		}
		catch(Throwable e)
		{
			LogUtil.internalError(e);
			return;
		}
		
		try
		{
			setAppenders(as);
		}
		catch(Throwable e)
		{
			LogUtil.internalError(e);
			return;
		}
	}
	
	
	protected static void setAppenders(List<AppenderBase> as)
	{
		if(as != null)
		{
			for(AppenderBase a: as)
			{
				Log.allAppenders.clear();
				Log.allAppenders.add(a);
				
				if(a.getChannels().size() == 0)
				{
					Log.root.clearAppenders();
					Log.root.addAppender(a);
				}
				else
				{
					for(String name: a.getChannels())
					{
						Log ch = Log.get(name);
						ch.clearAppenders();
						ch.addAppender(a);
					}
				}
			}
		}
	}
	
	
	protected void applyConfig(AbstractLogConfig cf)
	{
		if(cf == null)
		{
			level = LogLevel.OFF;
		}
		else
		{
			LogLevel lv = cf.getLogLevel(name);
			if(lv == null)
			{
				cf.getLogLevel(name); // FIX
				if(parent == null)
				{
					level = cf.getDefaultLogLevel();
				}
				else
				{
					level = parent.getLevel();
				}
			}
			else
			{
				level = lv;
			}
		}
		
		for(Log ch: children.values())
		{
			ch.applyConfig(cf);
		}
	}
	
	
	protected static void removeAppenders()
	{
		for(AppenderBase a: allAppenders)
		{
			for(String name: a.getChannels())
			{
				Log ch = get(name);
				ch.removeAppender(a);
			}
		}
		
		allAppenders.clear();
	}
	

	/** add a secret to be masked with *** in the output */
	public static void addSecret(String secret)
	{
		// TODO
	}
	
	
	/** add secrets to be masked with *** in the output */
	public static void addSecrets(Iterable<String> secrets)
	{
		// TODO
	}
	
	
	public static void setLevel(String channel, LogLevel level)
	{
		// TODO
	}
	
	
	public static Log getRoot()
	{
		return root;
	}
	
	
	protected Log getParent()
	{
		return parent;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	protected void setLevel(LogLevel level)
	{
		this.level = level;
	}
	
	
	public LogLevel getLevel()
	{
		return level;
	}
	
	
	protected boolean isEnabled(LogLevel lv)
	{
		if(level == null)
		{
			return false;
		}
		return level.isGreaterThanOrEqual(lv);
	}
	
	
	protected void addAppender(AppenderBase a)
	{
		appenders.add(a);
		setNeedsCallerRecursively(true);
	}
	
	
	protected void removeAppender(AppenderBase a)
	{
		appenders.remove(a);
		setNeedsCallerRecursively(false);
	}
	
	
	protected void clearAppenders()
	{
		appenders.clear();
	}
	
	
	protected void setNeedsCallerRecursively(boolean on)
	{
		if(on != needsCaller)
		{	
			if(!on)
			{
				boolean v = LogUtil.checkNeedsCaller(appenders);
				if(v)
				{
					return;
				}
			}
			
			needsCaller = on;
			
			for(Log ch: children.values())
			{
				ch.setNeedsCallerRecursively(on);
			}
		}
	}
	
	
	protected StackTraceElement extractCaller(Throwable err)
	{
		StackTraceElement[] stack = err.getStackTrace();
		
		for(int i=0; i<stack.length; i++)
		{
			StackTraceElement em = stack[i];
			String name = em.getClassName();
			if(ignore.contains(name))
			{
				continue;
			}
			
			return em;
		}
		
		throw new Error();
	}
	
	
	protected void logEvent(LogLevel level, Throwable err, Object message)
	{
		long time = System.currentTimeMillis();
		
		StackTraceElement caller;
		if(needsCaller)
		{
			caller = extractCaller(err == null ? new Throwable() : err);
		}
		else
		{
			caller = null;
		}
		
		processEvent(level, time, caller, err, message);
	}
	
	
	protected void processEvent(LogLevel lv, long time, StackTraceElement caller, Throwable err, Object message)
	{
		String msg = message == null ? "" : message.toString();
		processEvent(lv, time, caller, err, msg);
	}
	
	
	protected void processEvent(LogLevel lv, long time, StackTraceElement caller, Throwable err, String msg)
	{
		for(AppenderBase a: appenders)
		{
			a.append(lv, time, caller, err, msg);
		}
		
		if(parent != null)
		{
			parent.processEvent(lv, time, caller, err, msg);
		}
	}
	
	
	/** 
	 * prints to stderr - do not leave in production code.  
	 * this is not a replacement for proper logging.
	 */
	public static void p(Object ... items)
	{
		SB sb = new SB();

		StackTraceElement t = new Throwable().getStackTrace()[1];
		
		String s = t.getClassName();
		int ix = s.lastIndexOf('.');
		if(ix >= 0)
		{
			sb.append(s.substring(ix+1, s.length()));
		}
		else
		{
			sb.append(s);
		}
		
		sb.append('.');
		sb.append(t.getMethodName());
		sb.sp();
		
		for(Object x: items)
		{
			if(sb.length() > 0)
			{
				sb.append(" ");
			}
			sb.append(x);
		}
		
		System.err.println(sb);
	}
	
	
	// LogLevel.ERROR level
	
	
	public void error(Throwable err)
	{
		if(isEnabled(LogLevel.ERROR))
		{
			logEvent(LogLevel.ERROR, err, null);
		}
	}
	
	
	public void error(Object message, Throwable err)
	{
		if(isEnabled(LogLevel.ERROR))
		{
			logEvent(LogLevel.ERROR, err, message);
		}
	}
	
	
	public void error(Object message)
	{
		if(isEnabled(LogLevel.ERROR))
		{
			logEvent(LogLevel.ERROR, null, message);
		}
	}
	
	
	public void error(String format, Object ... args)
	{
		if(isEnabled(LogLevel.ERROR))
		{
			String msg = String.format(format, args);
			logEvent(LogLevel.ERROR, null, msg);
		}
	}
	
	
	public void error(Supplier<Object> lambda)
	{
		if(isEnabled(LogLevel.ERROR))
		{
			Object msg = lambda.get();
			logEvent(LogLevel.ERROR, null, msg);
		}
	}
	
	
	// LogLevel.WARN level
	
	
	public void warn(Throwable err)
	{
		if(isEnabled(LogLevel.WARN))
		{
			logEvent(LogLevel.WARN, err, null);
		}
	}
	
	
	public void warn(Object message, Throwable err)
	{
		if(isEnabled(LogLevel.WARN))
		{
			logEvent(LogLevel.WARN, err, message);
		}
	}
	
	
	public void warn(Object message)
	{
		if(isEnabled(LogLevel.WARN))
		{
			logEvent(LogLevel.WARN, null, message);
		}
	}
	
	
	public void warn(String format, Object ... args)
	{
		if(isEnabled(LogLevel.WARN))
		{
			String msg = String.format(format, args);
			logEvent(LogLevel.WARN, null, msg);
		}
	}
	
	
	public void warn(Supplier<Object> lambda)
	{
		if(isEnabled(LogLevel.WARN))
		{
			Object msg = lambda.get();
			logEvent(LogLevel.WARN, null, msg);
		}
	}
	
	
	// LogLevel.INFO level
	
	
	public void info(Throwable err)
	{
		if(isEnabled(LogLevel.INFO))
		{
			logEvent(LogLevel.INFO, err, null);
		}
	}
	
	
	public void info(Object message, Throwable err)
	{
		if(isEnabled(LogLevel.INFO))
		{
			logEvent(LogLevel.INFO, err, message);
		}
	}
	
	
	public void info(Object message)
	{
		if(isEnabled(LogLevel.INFO))
		{
			logEvent(LogLevel.INFO, null, message);
		}
	}
	
	
	public void info(String format, Object ... args)
	{
		if(isEnabled(LogLevel.INFO))
		{
			String msg = String.format(format, args);
			logEvent(LogLevel.INFO, null, msg);
		}
	}
	
	
	public void info(Supplier<Object> lambda)
	{
		if(isEnabled(LogLevel.INFO))
		{
			Object msg = lambda.get();
			logEvent(LogLevel.INFO, null, msg);
		}
	}
	
	
	// LogLevel.DEBUG level
	
	
	public void debug(Throwable err)
	{
		if(isEnabled(LogLevel.DEBUG))
		{
			logEvent(LogLevel.DEBUG, err, null);
		}
	}
	
	
	public void debug(Object message, Throwable err)
	{
		if(isEnabled(LogLevel.DEBUG))
		{
			logEvent(LogLevel.DEBUG, err, message);
		}
	}
	
	
	public void debug(Object message)
	{
		if(isEnabled(LogLevel.DEBUG))
		{
			logEvent(LogLevel.DEBUG, null, message);
		}
	}
	
	
	public void debug(String format, Object ... args)
	{
		if(isEnabled(LogLevel.DEBUG))
		{
			String msg = String.format(format, args);
			logEvent(LogLevel.DEBUG, null, msg);
		}
	}
	
	
	public void debug(Supplier<Object> lambda)
	{
		if(isEnabled(LogLevel.DEBUG))
		{
			Object msg = lambda.get();
			logEvent(LogLevel.DEBUG, null, msg);
		}
	}
	
	
	// LogLevel.TRACE level
	
	
	public void trace()
	{
		if(isEnabled(LogLevel.TRACE))
		{
			logEvent(LogLevel.TRACE, null, "");
		}
	}
	
	
	public void trace(Throwable err)
	{
		if(isEnabled(LogLevel.TRACE))
		{
			logEvent(LogLevel.TRACE, err, null);
		}
	}
	
	
	public void trace(Object message, Throwable err)
	{
		if(isEnabled(LogLevel.TRACE))
		{
			logEvent(LogLevel.TRACE, err, message);
		}
	}
	
	
	public void trace(Object message)
	{
		if(isEnabled(LogLevel.TRACE))
		{
			logEvent(LogLevel.TRACE, null, message);
		}
	}
	
	
	public void trace(String format, Object ... args)
	{
		if(isEnabled(LogLevel.TRACE))
		{
			String msg = String.format(format, args);
			logEvent(LogLevel.TRACE, null, msg);
		}
	}
	
	
	public void trace(Supplier<Object> lambda)
	{
		if(isEnabled(LogLevel.TRACE))
		{
			Object msg = lambda.get();
			logEvent(LogLevel.TRACE, null, msg);
		}
	}
}

