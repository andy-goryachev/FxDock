// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Closing Window Operation.
 */
@FunctionalInterface
public interface ClosingWindowOperation
{
	public ShutdownChoice confirmClosing(boolean exiting, boolean multiple, ShutdownChoice choice); 
}
