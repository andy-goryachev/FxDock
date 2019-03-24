// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public interface Cancellable
{
	public void cancel();
	
	public boolean isCancelled();
}
