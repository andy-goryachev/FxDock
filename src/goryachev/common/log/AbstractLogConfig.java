// Copyright © 2020-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import java.util.List;


/**
 * Log Config base class.
 */
public abstract class AbstractLogConfig
{
	public abstract boolean isVerbose();
	
	
	public abstract LogLevel getLogLevel(String name);
	
	
	public abstract LogLevel getDefaultLogLevel();
	
	
	public abstract List<AppenderBase> getAppenders() throws Exception;
}
