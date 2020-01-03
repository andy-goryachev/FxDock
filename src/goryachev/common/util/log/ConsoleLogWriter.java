// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import java.io.IOException;


public class ConsoleLogWriter
	extends LogWriter
{
	public ConsoleLogWriter(String name)
	{
		super(name);
	}

	
	public void print(String s)
	{
		System.out.println(s);
	}


	public void close() throws IOException
	{
	}
}
