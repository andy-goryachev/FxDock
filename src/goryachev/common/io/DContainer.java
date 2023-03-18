// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;


public class DContainer
{
	public static class ObjectMarker
	{
		public final String type;
		
		public ObjectMarker(String type)
		{
			this.type = type;
		}
		
		public String getType()
		{
			return type;
		}
		
		public String toString()
		{
			return "Object:" + getType();
		}
	}
	
	//
	
	private CList<Object> data;
	
	
	public DContainer(DContainer x)
	{
		data = new CList<>(x.data);
	}
	
	
	public DContainer()
	{
		data = new CList<>();
	}
	
	
	public Object clone()
	{
		return copyObjectList();
	}
	
	
	public DContainer copyObjectList()
	{
		return new DContainer(this);
	}
	
	
	public Object[] toArray()
	{
		return data.toArray();
	}
	
	
	public int size()
	{
		return data.size();
	}
	
	
	public Object get(int ix)
	{
		return data.get(ix);
	}
	
	
	public void add(Object x)
	{
		data.add(x);
	}
}
