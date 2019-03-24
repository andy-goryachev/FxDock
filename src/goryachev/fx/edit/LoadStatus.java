// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * Load Status.
 */
public class LoadStatus
{
	public static final LoadStatus UNKNOWN = new LoadStatus(0.0, true, false);
	
	private final double progress;
	private final boolean loading;
	private final boolean valid;
	
	
	public LoadStatus(double progress, boolean loading, boolean valid)
	{
		this.progress = progress;
		this.loading = loading;
		this.valid = valid;
	}
	
	
	public double getProgress()
	{
		return progress;
	}
	
	
	public boolean isLoading()
	{
		return loading;
	}
	
	
	public boolean isValid()
	{
		return valid;
	}
}
