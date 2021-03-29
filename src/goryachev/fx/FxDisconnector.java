// Copyright © 2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import goryachev.common.util.Disconnectable;


/**
 * Fx Disconnector.
 */
public class FxDisconnector
	implements Disconnectable
{
	private final CList<Disconnectable> items = new CList();
	
	
	public FxDisconnector()
	{
	}
	
	
	public void addDisconnectable(Disconnectable d)
	{
		items.add(d);
	}


	public void disconnect()
	{
		for(int i=items.size()-1; i>=0; i--)
		{
			Disconnectable d = items.remove(i);
			d.disconnect();
		}
	}
}
