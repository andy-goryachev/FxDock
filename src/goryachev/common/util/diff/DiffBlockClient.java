// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.diff;


public interface DiffBlockClient
{
	// size right == size left
	public void unchanged(int startLeft, int sizeLeft, int startRight) throws Exception;
	
	
	public void changed(int startLeft, int sizeLeft, int startRight, int sizeRight) throws Exception;
}