// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import java.util.List;


/**
 * Log Config interface.
 */
public interface ILogConfig
{
	public boolean isVerbose();
	
	
	public LogLevel getLogLevel(String name);
	
	
	public LogLevel getDefaultLogLevel();
	
	
	public List<IAppender> getAppenders() throws Exception;
}
