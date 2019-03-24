// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.ByteArrayOutputStream;


public class Hex
{
	public static final String HEX = "0123456789ABCDEF";
	public static final String HEX_ALL = "0123456789ABCDEFabcdef";

	
	public static String toHexString(int d, int digits)
	{
		char[] buf = new char[digits];
		while(--digits >= 0)
		{
			buf[digits] = HEX.charAt(d & 0x0f);
			d >>= 4;
		}
		return new String(buf);
	}
	
	
	public static String toHexString(int d)
	{
		return toHexString(d,8);
	}
	
	
	public static String toHexString(short d)
	{
		return toHexString(d,4);
	}
	
	
	public static String toHexString(long d, int digits)
	{
		char[] buf = new char[digits];
		while(--digits >= 0)
		{
			buf[digits] = HEX.charAt((int)(d & 0x0f));
			d >>= 4;
		}
		return new String(buf);
	}
	
	
	public static String toHexString(long d)
	{
		return toHexString(d,16);
	}
	
	
	public static String toHexString(byte[] b)
	{
		if(b == null)
		{
			return null;
		}
		
		return toHexString(b, 0, b.length);
	}
	
	
	public static String toHexByte(int x)
	{
		char[] cs = new char[2];
		cs[0] = HEX.charAt((x >> 4) & 0x0f);
		cs[1] = HEX.charAt(x & 0x0f);
		return new String(cs);
	}
	

	public static String toHexString(byte[] b, int start, int length)
	{
		SB sb = new SB(b.length);
		appendHexString(sb, b, start, length);
		return sb.toString();
	}
	
	
	public static void appendHexString(SB sb, byte[] b, int start, int length)
	{
		int end = start + length;
		for(int i=start; i<end; i++)
		{
			int x = b[i];
			sb.append(HEX.charAt((x >> 4) & 0x0f));
			sb.append(HEX.charAt(x & 0x0f));
		}
		
	}
	
	
	public static char toHexChar(int nibble)
	{
		return HEX.charAt(nibble & 0x0f);
	}
	
	
	/** returns hex value of a char in the range 0..15, throws an exception if not a hex char */
	public static int parseHexChar(char c) throws Exception
	{
		int x = parseHexCharPrivate(c);
		if(x < 0)
		{
			throw new Exception("not a hexadecimal character: " + c);
		}
		return x;
	}
	
	
	public static int parseHexCharPrivate(char c)
	{
		return HEX.indexOf(Character.toUpperCase(c));
	}
	
	
	/** 
	 * Parses input string as a hexadecimal byte array, ignoring all non-hex symbols.
	 * Throws an exception if the number of hex symbols is not even.
	 */
	public static byte[] parseByteArray(String s) throws Exception
	{
		return parseByteArray(s, 0, s.length());
	}
	

	/** 
	 * Parses input string as a hexadecimal byte array, ignoring all non-hex symbols,
	 * starting at the specified offset and using len characters. 
	 * Throws an exception if the number of hex symbols is not even.
	 */
	public static byte[] parseByteArray(String s, int off, int len) throws Exception
	{
		int sz = len / 2;
		ByteArrayOutputStream ba = new ByteArrayOutputStream(sz);
		boolean first = true;
		int d = 0;
		
		for(int i=0; i<len; i++)
		{
			char c = s.charAt(off + i);
			if(isHexChar(c))
			{
				if(first)
				{
					d = parseHexChar(c) << 4;
					first = false;
				}
				else
				{
					d |= parseHexChar(c);
					ba.write(d);
					first = true;
				}
			}
		}
		
		if(!first)
		{
			throw new Exception("extra symbols found in the input string");
		}
		
		return ba.toByteArray();
	}
	
	
	/** parses a two-symbol string as a hex byte value */
	public static byte parseByte(String s) throws Exception
	{
		int d = parseHexChar(s.charAt(0)) << 4;
		return (byte)(d | parseHexChar(s.charAt(1)));
	}
	
	
	// dumps byte array into a nicely formatted String
	// printing address first, then 16 bytes of hex then ASCII representation then newline
	//     "0000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" or
	// "00000000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" 
	// depending on startAddress
	// address starts with startAddress
	public static String toHexStringASCII(byte[] bytes)
	{
		long startAddress = 0;
		boolean bigfile = ((startAddress + bytes.length) > 65535);
		SB sb = new SB(((bytes.length/16)+1) * 77 + 1);
		
		int col = 0;
		long addr = startAddress;
		int lineStart = 0;

		for(int i=0; i<bytes.length; i++)
		{
			// offset
			if(col == 0)
			{
				lineStart = i;
				if(bigfile)
				{
					hexByte(sb,(int)(addr >> 24));
					hexByte(sb,(int)(addr >> 16));
				}
				hexByte(sb,(int)(addr >> 8));
				hexByte(sb,(int)(addr));
				sb.append("  ");
			}
			
			// byte
			hexByte(sb,bytes[i]);
			sb.append(' ');

			// space or newline
			if(col >= 15)
			{
				sb.sp();
				dumpASCII(sb, bytes, lineStart);
				sb.nl();
				col = 0;
			}
			else
			{
				col++;
			}

			addr++;
		}

		if(col != 0)
		{
			while(col++ < 16)
			{
				sb.append("   ");
			}

			sb.sp();
			dumpASCII(sb, bytes, lineStart);
			sb.nl();
		}
		
		return sb.toString();
	}
	
	
	// dumps byte array into a nicely formatted String
	// printing address first, then 16 bytes of hex then ASCII representation then newline
	//     "0000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" or
	// "00000000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" 
	// depending on startAddress
	// address starts with startAddress
	public static String[] toHexStringsASCII(byte[] bytes)
	{
		SB sb = new SB(78);
		int len = bytes.length;
		boolean bigfile = (bytes.length > 65535);
		int lines = CKit.binCount(len, 16);
		CList<String> rv = new CList(lines);
		
		int col = 0;
		int addr = 0;
		int lineStart = 0;

		for(int i=0; i<len; i++)
		{
			// offset
			if(col == 0)
			{
				lineStart = i;
				if(bigfile)
				{
					hexByte(sb, addr >> 24);
					hexByte(sb, addr >> 16);
				}
				hexByte(sb, addr >> 8);
				hexByte(sb, addr);
				sb.append("  ");
			}
			
			// byte
			hexByte(sb,bytes[i]);
			sb.append(' ');

			// space or newline
			if(col >= 15)
			{
				sb.sp();
				dumpASCII(sb, bytes, lineStart);
				rv.add(sb.getAndClear());
				col = 0;
			}
			else
			{
				col++;
			}

			addr++;
		}

		if(col != 0)
		{
			while(col++ < 16)
			{
				sb.append("   ");
			}

			sb.sp();
			dumpASCII(sb, bytes, lineStart);
			rv.add(sb.getAndClear());
		}
		
		return CKit.toArray(rv);
	}
	
	
	public static void hexByte(SB sb, int c)
	{
		sb.append(HEX.charAt((c >> 4) & 0x0f));
		sb.append(HEX.charAt(c & 0x0f));
	}
	
	
	private static void dumpASCII(SB sb, byte[] bytes, int lineStart)
	{
		int max = Math.min(bytes.length,lineStart+16);
		for(int i=lineStart; i<max; i++)
		{
			int d = bytes[i] & 0xff;
			if((d < 0x20) || (d >= 0x7f))
			{
				d = '.';
			}
			sb.append((char)d);
		}
	}


	public static boolean isHexChar(char c)
	{
		return (HEX_ALL.indexOf(c) >= 0);
	}
	
	
	/** parses hex-encoded long value */
	public static long parseLong(String s) throws Exception
	{
		return parseLong(s, 0, s.length());
	}


	public static long parseLong(String s, int off, int len) throws Exception
	{
		if((len & 1) != 0)
		{
			throw new Exception("len must be even: " + len);
		}
		else if(len > 16)
		{
			throw new Exception("len is too large: " + len);
		}
		
		long d = 0;
		for(int i=0; i<len; i++)
		{
			d <<= 4;
			
			char c = s.charAt(i);
			int nibble = parseHexChar(c);
			d |= nibble;
		}
		return d;
	}
	
	
	/** parses hex-encoded int value */
	public static int parseInt(String s) throws Exception
	{
		return parseInt(s, 0, s.length());
	}
	
	
	/** parses hex-encoded int value */
	public static int parseInt(String s, int defaultValue)
	{
		try
		{
			return parseInt(s, 0, s.length());
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	
	/** parses hex-encoded int value */
	public static int parseInt(String s, int off, int len) throws Exception
	{
		int d = 0;
		int sz = Math.min(len, 8);
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			int nibble = parseHexChar(c);

			d <<= 4;
			d |= nibble;
		}
		return d;
	}
}
