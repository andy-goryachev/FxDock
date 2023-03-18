// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public interface Committable
{
	/** commit changes or throws an exception if anything fails */
	public void commit() throws Exception;
}
