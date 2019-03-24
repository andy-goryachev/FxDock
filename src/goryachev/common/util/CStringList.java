// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Arrays;
import java.util.Collection;


public class CStringList
	extends CList<String>
{
	public CStringList(int initialCapacity)
	{
		super(initialCapacity);
	}


	public CStringList()
	{
		super();
	}


	public CStringList(Collection<String> c)
	{
		super(c);
	}


	public CStringList(String[] a)
	{
		super(Arrays.asList(a));
	}


	public String[] toStringArray()
	{
		return toArray(new String[size()]);
	}
}
