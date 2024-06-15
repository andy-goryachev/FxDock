// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.internal;
import goryachev.common.log.AppenderBase;
import goryachev.common.log.LogLevel;
import java.io.PrintStream;


/**
 * Console Appender.
 */
public class ConsoleAppender
	extends AppenderBase
{
	private final PrintStream out;
	
	
	public ConsoleAppender(LogLevel threshold, PrintStream out)
	{
		super(threshold);
		this.out = out;
	}
	
	
	public ConsoleAppender(PrintStream out)
	{
		this(LogLevel.ALL, out);
	}
	

	@Override
	public void emit(String s)
	{
		out.println(s);
	}
}
