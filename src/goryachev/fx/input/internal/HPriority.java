// Copyright © 2024-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.input.internal;


/**
 * Handler Priority.
 */
public enum HPriority
{
	USER_EH(0x9000),
	USER_KB(0x8000),
	SKIN_EH(0x7001),
	SKIN_KB(0x6001);
	
	
	public final int priority;
	
	
	private HPriority(int priority)
	{
		this.priority = priority;
	}


	public boolean isSkin()
	{
		return (priority & 0x1) == 1;
	}
}
