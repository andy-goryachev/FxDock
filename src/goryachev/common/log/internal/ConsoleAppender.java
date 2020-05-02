// Copyright © 2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.internal;
import goryachev.common.log.AppenderBase;
import java.io.PrintStream;


/**
 * Console Appender.
 */
public class ConsoleAppender
	extends AppenderBase
{
	private final PrintStream out;
	
	
	public ConsoleAppender(PrintStream out, String[] channels)
	{
		super(channels);
		
		this.out = out;
	}
	

	public void emit(String s)
	{
		out.println(s);
	}


	public static AppenderBase create(LogConfig.AppenderInfo inf, PrintStream out)
	{
		ConsoleAppender a = new ConsoleAppender(out, inf.channels);
		return a;
	}
}
