// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.log.internal.ConsoleAppender;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import java.util.List;


/**
 * Simple Log Config.
 */
public class SimpleLogConfig
	implements ILogConfig
{
	private boolean verbose;
	private LogLevel defaultLogLevel = LogLevel.OFF;
	private final CMap<String,LogLevel> channels = new CMap<>();
	private final CList<IAppender> appenders = new CList<>();
	
	
	public SimpleLogConfig()
	{
	}
	
	
	public void verbose()
	{
		verbose = true;
	}


	@Override
	public boolean isVerbose()
	{
		return verbose;
	}
	
	
	public void all(String name)
	{
		setLevel(name, LogLevel.ALL);
	}
	
	
	public void trace(String name)
	{
		setLevel(name, LogLevel.TRACE);
	}
	
	
	public void debug(String name)
	{
		setLevel(name, LogLevel.DEBUG);
	}
	
	
	public void info(String name)
	{
		setLevel(name, LogLevel.INFO);
	}
	
	
	public void warn(String name)
	{
		setLevel(name, LogLevel.WARN);
	}
	
	
	public void error(String name)
	{
		setLevel(name, LogLevel.ERROR);
	}
	
	
	public void fatal(String name)
	{
		setLevel(name, LogLevel.FATAL);
	}
	
	
	public void off(String name)
	{
		setLevel(name, LogLevel.OFF);
	}
	
	
	public void setLevel(String name, LogLevel lv)
	{
		channels.put(name, lv);
	}


	@Override
	public LogLevel getLogLevel(String name)
	{
		LogLevel lv = channels.get(name);
		if(lv == null)
		{
			return defaultLogLevel;
		}
		return lv;
	}
	
	
	public void setDefaultLogLevel(LogLevel lv)
	{
		defaultLogLevel = lv;
	}


	@Override
	public LogLevel getDefaultLogLevel()
	{
		return defaultLogLevel;
	}
	
	
	public void addConsole()
	{
		ConsoleAppender a = new ConsoleAppender(System.out);
		addAppender(a);
	}
	
	
	public void addAppender(AppenderBase a)
	{
		appenders.add(a);
	}


	@Override
	public List<IAppender> getAppenders() throws Exception
	{
		return appenders;
	}
}
