// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public interface BackgroundOperation
{
	public interface Monitor
	{
		public void print(String text);
		
		public void printError(String text);
		
		public boolean isCancelled();
	}
	
	//
	
	/** background operation body.  returns normally or throws an exception. */
	public void processBackgroundOperation(Monitor m) throws CancelledException, Exception;
	
	/** returns operation progress value of null if progress can not be determined. */
	public Progress getProgress();
	
	
	/** release all the resources after op completed normally or with an error */
	public void dispose();
}
