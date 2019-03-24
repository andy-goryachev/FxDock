// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


public class BitStreamCommon
{
	protected static final int BITS_PER_BYTE = 8;
	protected static final int MASK[] =
	{
		0x00000000, 
		0x00000001, 
		0x00000003, 
		0x00000007, 
		0x0000000f, 
		0x0000001f, 
		0x0000003f, 
		0x0000007f, 
		0x000000ff, 
		0x000001ff, 
		0x000003ff, 
		0x000007ff, 
		0x00000fff, 
		0x00001fff, 
		0x00003fff, 
		0x00007fff, 
		0x0000ffff, 
		0x0001ffff, 
		0x0003ffff, 
		0x0007ffff, 
		0x000fffff, 
		0x001fffff, 
		0x003fffff, 
		0x007fffff, 
		0x00ffffff, 
		0x01ffffff, 
		0x03ffffff, 
		0x07ffffff, 
		0x0fffffff, 
		0x1fffffff, 
		0x3fffffff, 
		0x7fffffff, 
		0xffffffff
	};
	
	
	public static int getMask(int bits)
	{
		return MASK[bits];
	}
}
