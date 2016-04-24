// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.SwingConstants;


public enum CAlignment
{
	LEADING()
	{
		public int getAlignment() { return SwingConstants.LEADING; }
	},
	TRAILING()
	{
		public int getAlignment() { return SwingConstants.TRAILING; }
	},
	TOP()
	{
		public int getAlignment() { return SwingConstants.TOP; }
	},
	BOTTOM()
	{
		public int getAlignment() { return SwingConstants.BOTTOM; }
	},
	CENTER()
	{
		public int getAlignment() { return SwingConstants.CENTER; }
	};
	
	
	//
	
	
	public abstract int getAlignment();


	public static int get(CAlignment a)
	{
		if(a != null)
		{
			return a.getAlignment();
		}
		return SwingConstants.LEADING;
	}
}