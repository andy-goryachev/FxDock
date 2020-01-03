// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;
import java.io.IOException;


public class PrimitiveListOutput
	implements PrimitiveOutput
{
	private CList<Object> out = new CList();
	
	
	public PrimitiveListOutput()
	{
	}
	

	public void close() throws IOException
	{
	}


	public void write(Object x) throws Exception
	{
		out.add(x);
	}
	
	
	public CList<Object> getList()
	{
		return out;
	}
}
