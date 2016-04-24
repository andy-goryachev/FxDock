// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.diff;


public abstract class Difference
{
	public abstract String getLeft();
	public abstract String getRight();
	public abstract boolean isChanged();
	public abstract Difference[] getParts();


	public static Difference create(final String s)
	{
		return new Difference()
		{
			public String getLeft() { return s; }
			public String getRight() { return s; }
			public boolean isChanged() { return false; }
			public Difference[] getParts() { return null; }
		};
	}
	
	
	public static Difference create(final String left, final String right, final Difference[] parts)
	{
		return new Difference()
		{
			public String getLeft() { return left; }
			public String getRight() { return right; }
			public boolean isChanged() { return true; }
			public Difference[] getParts() { return parts; }
		};
	}
}
