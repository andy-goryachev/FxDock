// Copyright © 2018-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.internal;
import goryachev.common.log.AbstractLogConfig;
import goryachev.common.log.AppenderBase;
import goryachev.common.log.LogLevel;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import java.util.List;


/**
 * Log Config.
 */
public class LogConfig
	extends AbstractLogConfig
{
	public static class Profile
	{
		public boolean enabled;
		public String[] off;
		public String[] fatal;
		public String[] error;
		public String[] debug;
		public String[] info;
		public String[] trace;
		public String[] all;
		public CMap<String,String> channels;
	}
	
	//
	
	public static class AppenderInfo
	{
		public String type; 
		public boolean disabled;
		public String pattern;
		public String[] channels;
	}
	
	//
	
	public String defaultLevel;
	public boolean verbose;
	public String[] off;
	public String[] fatal;
	public String[] error;
	public String[] debug;
	public String[] info;
	public String[] trace;
	public String[] all;
	public CMap<String,String> channels;
	public CMap<String,Profile> profiles;
	public CList<AppenderInfo> appenders;
	private transient CMap<String,LogLevel> levels;
	

	public boolean isVerbose()
	{
		return verbose;
	}
	
	
	public LogLevel getLogLevel(String name)
	{
		LogLevel lv;
		try
		{
			lv = levels.get(name);
		}
		catch(NullPointerException e)
		{
			// using this NPE to initialize
			levels = initLevels();
			lv = levels.get(name);
		}
		
		return lv;
	}
	
	
	public LogLevel getDefaultLogLevel()
	{
		return LogUtil.parseLevel(defaultLevel);
	}
	
	
	protected CMap<String,LogLevel> initLevels()
	{
		// channels > levels > profiles(channels > levels) > defaultValue
		
		CMap<String,LogLevel> m = new CMap();
		
		if(profiles != null)
		{
			for(Profile p: profiles.values())
			{
				if(p.enabled)
				{
					LogUtil.process(m, p.off, LogLevel.OFF);
					LogUtil.process(m, p.fatal, LogLevel.FATAL);
					LogUtil.process(m, p.error, LogLevel.ERROR);
					LogUtil.process(m, p.debug, LogLevel.DEBUG);
					LogUtil.process(m, p.info, LogLevel.INFO);
					LogUtil.process(m, p.trace, LogLevel.TRACE);
					LogUtil.process(m, p.all, LogLevel.ALL);
					
					LogUtil.process(m, p.channels);
				}
			}
		}
		
		LogUtil.process(m, off, LogLevel.OFF);
		LogUtil.process(m, fatal, LogLevel.FATAL);
		LogUtil.process(m, error, LogLevel.ERROR);
		LogUtil.process(m, debug, LogLevel.DEBUG);
		LogUtil.process(m, info, LogLevel.INFO);
		LogUtil.process(m, trace, LogLevel.TRACE);
		LogUtil.process(m, all, LogLevel.ALL);
		
		LogUtil.process(m, channels);
		
		return m;
	}
	
	
	public List<AppenderBase> getAppenders() throws Exception
	{
		CList<AppenderBase> rv = new CList();
		
		if(appenders != null)
		{
			for(AppenderInfo inf: appenders)
			{
				AppenderBase a = AppenderBase.create(inf);
				rv.add(a);
			}
		}
		
		return rv;
	}
}
