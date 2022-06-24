// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Css ID
 */
public class CssID
{
	private final String id;
	private static final AtomicLong seq = new AtomicLong();
	
	
	public CssID(String id)
	{
		this.id = id + "_" + seq.incrementAndGet();
	}
	
	
	public CssID()
	{
		this("");
	}
	
	
	public String getID()
	{
		return id;
	}
	
	
	public String toString()
	{
		return getID();
	}
}
