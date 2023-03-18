// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


public interface IBitStream
{
	public boolean hasMoreBits();
	
	
	public int getIndex();
	
	
	public int nextBits(int count) throws Exception;
		
	
	public int nextBit() throws Exception;
	
	
	public void skip(int bits);
}
