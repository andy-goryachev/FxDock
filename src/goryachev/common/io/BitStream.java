// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.EOFException;


public class BitStream
	implements IBitStream
{
	private byte[] bytes;
	private int index;
	private static final int[] MASK =
	{
		0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01
	};
	
	
	public BitStream(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	
	public int getIndex()
	{
		return index;
	}
	
	
	public boolean hasMoreBits()
	{
		return (index < (bytes.length * 8));
	}
	
	
	public int nextBits(int count) throws Exception
	{
		int d = 0;
		while(count > 0)
		{
			d = (d << 1) | nextBit();
			--count;
		}
		return d;
	}
		
	
	public int nextBit() throws Exception
	{
		int byteIndex = index/8;
		if(byteIndex >= bytes.length)
		{
			throw new EOFException();
		}
		
		int offset = index - (byteIndex * 8);
		index++;
		if((MASK[offset] & bytes[byteIndex]) == 0)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	
	public void skip(int bits)
	{
		if(bits < 0)
		{
			throw new IllegalArgumentException();
		}
		
		index += bits;
	}
}
