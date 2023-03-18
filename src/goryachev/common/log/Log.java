// Copyright © 2017-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.log.internal.ConsoleAppender;
import goryachev.common.util.CKit;
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
	private static final int ALL = LogLevel.ALL.ordinal();
	private static final int TRACE = LogLevel.TRACE.ordinal();
	private static final int DEBUG = LogLevel.DEBUG.ordinal();
	private static final int INFO = LogLevel.INFO.ordinal();
	private static final int WARN = LogLevel.WARN.ordinal();
	private static final int ERROR = LogLevel.ERROR.ordinal();
	private static final int FATAL = LogLevel.FATAL.ordinal();
	private static final int OFF = LogLevel.OFF.ordinal();
	private static final CopyOnWriteArrayList<IAppender> appenders = new CopyOnWriteArrayList<>();
	private static int appendersThreshold = OFF;
	private static boolean needCaller;
	protected static boolean showInternalErrors;
	protected static final CSet<String> ignore = LogUtil.initIgnoreClassNames();
	protected static final Log root = new Log(null, null, null);
	
	private final String fullName;
	private final String name;
	private Log parent;
	private LogLevel level;
	private int effectiveLevel;
	private final CMap<String,Log> children = new CMap(0); // TODO null by default


	protected Log(Log parent, String name, String fullName)
	{
		this.parent = parent;
		this.fullName = fullName;
		this.name = name;
	}
	
	
	/** returns a channel instance for the specified name */
	public static Log get(String fullName)
	{
		String[] ss = CKit.split(fullName, '.');
		Log log = root;
		
		synchronized(Log.class)
		{
			for(String s: ss)
			{
				Log ch = log.children.get(s);
				if(ch == null)
				{
					ch = new Log(log, s, fullName);
					log.children.put(s, ch);				
	
					ch.updateEffectiveLevelsRecursively(null);
				}
				log = ch;
			}
		}
		
		return log;
	}
	
	
	/** disables all the logging and removes all the appenders. */
	public static void reset()
	{
		setConfig(LogUtil.createDisabledLogConfig());
	}
	
	
	/** resets the logging to have one console appender at the DEBUG level */
	public static void initConsoleForDebug()
	{
		initConsole(LogLevel.DEBUG);
	}
	
	
	/** resets the logging to have one console appender at the specified level */
	public static void initConsole(LogLevel level)
	{
		SimpleLogConfig c = new SimpleLogConfig();
		c.setDefaultLogLevel(level);
		c.addAppender(new ConsoleAppender(level, System.out));
		setConfig(c);
	}
	
	
	/** sets whether to show internal errors */
	public static void setShowInternalErrors(boolean on)
	{
		showInternalErrors = on;
	}
	
	
	/** resets the logging to the specified configuration */
	public static void setConfig(ILogConfig cf)
	{
		if(cf == null)
		{
			cf = LogUtil.createDisabledLogConfig();
		}
		
		showInternalErrors = cf.isVerbose();
		
		List<IAppender> as;
		try
		{
			as = cf.getAppenders();
		}
		catch(Throwable e)
		{
			LogUtil.internalError(e);
			return;
		}
	
		synchronized(Log.class)
		{
			updateAll(cf);
		}
	}
	
	
	protected static void updateAll(ILogConfig cf)
	{
		if(cf != null)
		{
			appenders.clear();
			
			try
			{
				List<IAppender> as = cf.getAppenders();
				if(as != null)
				{
					for(IAppender a: as)
					{
						appenders.add(a);
					}
				}
			}
			catch(Exception e)
			{
				LogUtil.internalError(e);
			}
		}
		
		int th = OFF;
		boolean caller = false;
		for(IAppender a: appenders)
		{
			if(a.getThreshold() < th)
			{
				th = a.getThreshold();
			}
			
			caller |= a.needsCaller();
		}
		
		appendersThreshold = th; // TODO use this for updateEffectiveLevelsRecursively()
		needCaller = caller;
		
		root.updateEffectiveLevelsRecursively(cf);
	}
	
	
	public static synchronized void addAppender(IAppender a)
	{
		appenders.add(a);
		updateAll(null);
	}
	
	
	public static synchronized void removeAppender(IAppender a)
	{
		if(appenders.remove(a))
		{
			updateAll(null);
		}
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
	
	
	/** sets log level for a specific log, does not update children */
	public static synchronized void setLevel(String channel, LogLevel level)
	{
		Log log = get(channel);
		log.setLevel(level);
	}
	
	
	/** @return root logger */
	public static Log getRoot()
	{
		return root;
	}
	

	/** @return logger channel name */
	public String getName()
	{
		return name;
	}
	
	
	protected void updateEffectiveLevelsRecursively(ILogConfig cf)
	{
		if(cf == null)
		{
			if(level == null)
			{
				if(parent == null)
				{
					effectiveLevel = OFF;
				}
				else
				{
					effectiveLevel = parent.effectiveLevel;
				}
			}
			else
			{
				int eff = level.ordinal();
				if(eff == effectiveLevel)
				{
					return;
				}
				else
				{
					effectiveLevel = eff;
				}
			}
		}
		else
		{
			level = cf.getLogLevel(fullName);
			if(level == null)
			{
				if(parent == null)
				{
					effectiveLevel = cf.getDefaultLogLevel().ordinal();
				}
				else
				{
					effectiveLevel = parent.effectiveLevel;
				}
			}
			else
			{
				effectiveLevel = level.ordinal();
			}
		}
		
		for(Log ch: children.values())
		{
			ch.updateEffectiveLevelsRecursively(cf);
		}
	}
	
	
	protected Log getParent()
	{
		return parent;
	}
	
	
	protected void setLevel(LogLevel level)
	{
		this.level = level;
		updateEffectiveLevelsRecursively(null);
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
		if(needCaller)
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
		for(IAppender a: appenders)
		{
			a.append(lv, time, caller, err, msg);
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
	
	
	protected static String format(String fmt, Object[] args)
	{
		try
		{
			return String.format(fmt, args);
		}
		catch(Throwable e)
		{
			return "Error in logging statement [" + fmt + "]: " + CKit.stackTrace(e) + "]";
		}
	}
	
	
	// LogLevel.ERROR level
	
	
	public void error(Throwable err)
	{
		if(ERROR >= effectiveLevel)
		{
			logEvent(LogLevel.ERROR, err, null);
		}
	}
	
	
	public void error(Object message, Throwable err)
	{
		if(ERROR >= effectiveLevel)
		{
			logEvent(LogLevel.ERROR, err, message);
		}
	}
	
	
	public void error()
	{
		if(ERROR >= effectiveLevel)
		{
			logEvent(LogLevel.ERROR, null, "");
		}
	}
	
	
	public void error(Object message)
	{
		if(ERROR >= effectiveLevel)
		{
			logEvent(LogLevel.ERROR, null, message);
		}
	}
	
	
	public void error(String format, Object ... args)
	{
		if(ERROR >= effectiveLevel)
		{
			String msg = format(format, args);
			logEvent(LogLevel.ERROR, null, msg);
		}
	}
	
	
	public void error(Supplier<Object> lambda)
	{
		if(ERROR >= effectiveLevel)
		{
			Object msg = lambda.get();
			logEvent(LogLevel.ERROR, null, msg);
		}
	}
	
	
	// LogLevel.WARN level
	
	
	public void warn(Throwable err)
	{
		if(WARN >= effectiveLevel)
		{
			logEvent(LogLevel.WARN, err, null);
		}
	}
	
	
	public void warn(Object message, Throwable err)
	{
		if(WARN >= effectiveLevel)
		{
			logEvent(LogLevel.WARN, err, message);
		}
	}
	
	
	public void warn()
	{
		if(WARN >= effectiveLevel)
		{
			logEvent(LogLevel.WARN, null, "");
		}
	}
	
	
	public void warn(Object message)
	{
		if(WARN >= effectiveLevel)
		{
			logEvent(LogLevel.WARN, null, message);
		}
	}
	
	
	public void warn(String format, Object ... args)
	{
		if(WARN >= effectiveLevel)
		{
			String msg = format(format, args);
			logEvent(LogLevel.WARN, null, msg);
		}
	}
	
	
	public void warn(Supplier<Object> lambda)
	{
		if(WARN >= effectiveLevel)
		{
			Object msg = lambda.get();
			logEvent(LogLevel.WARN, null, msg);
		}
	}
	
	
	// LogLevel.INFO level
	
	
	public void info(Throwable err)
	{
		if(INFO >= effectiveLevel)
		{
			logEvent(LogLevel.INFO, err, null);
		}
	}
	
	
	public void info(Object message, Throwable err)
	{
		if(INFO >= effectiveLevel)
		{
			logEvent(LogLevel.INFO, err, message);
		}
	}
	
	
	public void info()
	{
		if(INFO >= effectiveLevel)
		{
			logEvent(LogLevel.INFO, null, "");
		}
	}
	
	
	public void info(Object message)
	{
		if(INFO >= effectiveLevel)
		{
			logEvent(LogLevel.INFO, null, message);
		}
	}
	
	
	public void info(String format, Object ... args)
	{
		if(INFO >= effectiveLevel)
		{
			String msg = format(format, args);
			logEvent(LogLevel.INFO, null, msg);
		}
	}
	
	
	public void info(Supplier<Object> lambda)
	{
		if(INFO >= effectiveLevel)
		{
			Object msg = lambda.get();
			logEvent(LogLevel.INFO, null, msg);
		}
	}
	
	
	// LogLevel.DEBUG level
	
	
	public void debug(Throwable err)
	{
		if(DEBUG >= effectiveLevel)
		{
			logEvent(LogLevel.DEBUG, err, null);
		}
	}
	
	
	public void debug(Object message, Throwable err)
	{
		if(DEBUG >= effectiveLevel)
		{
			logEvent(LogLevel.DEBUG, err, message);
		}
	}
	
	
	public void debug()
	{
		if(DEBUG >= effectiveLevel)
		{
			logEvent(LogLevel.DEBUG, null, "");
		}
	}
	
	
	public void debug(Object message)
	{
		if(DEBUG >= effectiveLevel)
		{
			logEvent(LogLevel.DEBUG, null, message);
		}
	}
	
	
	public void debug(String format, Object ... args)
	{
		if(DEBUG >= effectiveLevel)
		{
			String msg = format(format, args);
			logEvent(LogLevel.DEBUG, null, msg);
		}
	}
	
	
	public void debug(Supplier<Object> lambda)
	{
		if(DEBUG >= effectiveLevel)
		{
			Object msg = lambda.get();
			logEvent(LogLevel.DEBUG, null, msg);
		}
	}
	
	
	// LogLevel.TRACE level
	
	
	public void trace()
	{
		if(TRACE >= effectiveLevel)
		{
			logEvent(LogLevel.TRACE, null, "");
		}
	}
	
	
	public void trace(Throwable err)
	{
		if(TRACE >= effectiveLevel)
		{
			logEvent(LogLevel.TRACE, err, null);
		}
	}
	
	
	public void trace(Object message, Throwable err)
	{
		if(TRACE >= effectiveLevel)
		{
			logEvent(LogLevel.TRACE, err, message);
		}
	}
	
	
	public void trace(Object message)
	{
		if(TRACE >= effectiveLevel)
		{
			logEvent(LogLevel.TRACE, null, message);
		}
	}
	
	
	public void trace(String format, Object ... args)
	{
		if(TRACE >= effectiveLevel)
		{
			String msg = format(format, args);
			logEvent(LogLevel.TRACE, null, msg);
		}
	}
	
	
	public void trace(Supplier<Object> lambda)
	{
		if(TRACE >= effectiveLevel)
		{
			Object msg = lambda.get();
			logEvent(LogLevel.TRACE, null, msg);
		}
	}
}

