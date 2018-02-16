// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.WeakList;
import goryachev.fx.edit.Marker;


/**
 * Maintains weak list of Markers.
 * This editor-specific class is needed to allow for marker adjustment after an editing operation.
 */
public class Markers
{
	private final WeakList<Marker> markers;
	
	
	public Markers(int size)
	{
		markers = new WeakList<Marker>();
	}


	public Marker newMarker(int lineNumber, int charIndex, boolean leading)
	{
		Marker m = new Marker(this, lineNumber, charIndex, leading);
		markers.add(m);
		
		if(markers.size() > 1000000)
		{
			throw new Error("too many markers");
		}
		
		return m;
	}
	
	
	public void clear()
	{
		markers.clear();
	}
}
