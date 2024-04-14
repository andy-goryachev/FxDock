// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Closing Window Operation.
 */
@FunctionalInterface
public interface ClosingWindowOperation
{
	/**
	 * Valid inputs: DISCARD_ALL, SAVE_ALL, UNDEFINED.
	 * Valid outputs: CANCEL, CONTINUE, DISCARD_ALL, SAVE_ALL
	 */
	public ShutdownChoice confirmClosing(boolean exiting, boolean multiple, ShutdownChoice choice); 
}
