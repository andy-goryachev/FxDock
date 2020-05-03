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
	
	
	public ConsoleAppender(PrintStream out)
	{
		this.out = out;
	}
	

	public void emit(String s)
	{
		out.println(s);
	}
}
