// Copyright (c) 2014-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.img;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * NetPBM image format reader.
 * 
 * http://en.wikipedia.org/wiki/Netpbm_format
 * 
 * pbm: http://netpbm.sourceforge.net/doc/pbm.html
 * pgm: http://netpbm.sourceforge.net/doc/pgm.html
 * ppm: http://netpbm.sourceforge.net/doc/ppm.html
 * pam: http://netpbm.sourceforge.net/doc/pam.html
 */
public class NetpbmImageReader
{
	private static final byte TYPE_P1 = '1';
	private static final byte TYPE_P2 = '2';
	private static final byte TYPE_P3 = '3';
	private static final byte TYPE_P4 = '4';
	private static final byte TYPE_P5 = '5';
	private static final byte TYPE_P6 = '6';
	private static final byte TYPE_P7 = '7';
	private final InputStream in;
	private byte type;
	private int width;
	private int height;
	private int maxVal;
	private boolean singleByte;
	
	
	public NetpbmImageReader(InputStream in)
	{
		this.in = in;
	}
	
	
	public NetpbmImageReader(byte[] bytes)
	{
		this.in = new ByteArrayInputStream(bytes);
	}
	
	
	protected Exception err()
	{
		return new Exception("Not a NetPBM image");
	}
	
	
	protected int read() throws Exception
	{
		return in.read() & 0xff;
	}
	
	
	protected int read2() throws Exception
	{
		int d = (in.read() & 0xff) << 8;
		return d | (in.read() & 0xff);
	}
	
	
	protected int readInt() throws Exception
	{
		return Integer.parseInt(readToken());
	}
	
	
	// "newline" refers to the character known in ASCII as Line Feed or LF. 
	protected boolean isNewline(int c)
	{
		switch(c)
		{
		case '\r':
		case '\n':
			return true;
		}
		return false;
	}
	
	
	// A "white space" character is space, CR, LF, TAB, VT, or FF
	protected boolean isWhite(int c)
	{
		switch(c)
		{
		case ' ':
		case '\r':
		case '\n':
		case '\t':
		case 0x11:
		case '\f':
			return true;
		}
		return false;
	}
	
	
	protected String readToken() throws Exception
	{
		int c;
		
		// skip whitespace
		while(isWhite((c = in.read())));
		
		if(c == '#')
		{
			// skip comment
			while(!isNewline(c = in.read()));

			// skip whitespace again
			while(isWhite((c = in.read())));
		}
		
		SB sb = new SB();
		for(;;)
		{
			if(c < 0)
			{
				break;
			}
			
			if(isWhite(c))
			{
				break;
			}
			
			sb.append((char)c);

			c = in.read();
		}
		
		return sb.toString();
	}
	
	
	public BufferedImage readImage() throws Exception
	{
		try
		{
			// read signature
			int c = in.read();
			if(c != 'P')
			{
				throw err();
			}
			
			c = in.read();
			switch(c)
			{
			case '1':
				type = TYPE_P1;
				break;
			case '2':
				type = TYPE_P2;
				break;
			case '3':
				type = TYPE_P3;
				break;
			case '4':
				type = TYPE_P4;
				break;
			case '5':
				type = TYPE_P5;
				break;
			case '6':
				type = TYPE_P6;
				break;
			case '7':
				type = TYPE_P7;
				break;
			default:
				throw err();
			}
			
			c = in.read();
			if(!isWhite(c))
			{
				throw err();
			}
			
			// read image parameters
			
			try
			{
				String s = readToken();
				width = Integer.parseInt(s);
				
				s = readToken();
				height = Integer.parseInt(s);
				
				switch(type)
				{
				case TYPE_P2:
				case TYPE_P3:
				case TYPE_P5:
				case TYPE_P6:
					s = readToken();
					maxVal = Integer.parseInt(s);
					if((maxVal <= 0) || (maxVal > 65535))
					{
						throw err();
					}
					
					singleByte = maxVal < 256;
				}
			}
			catch(Exception e)
			{
				throw err();
			}
			
			// read image
			
			switch(type)
			{
			case TYPE_P1:
				return readP1();
			case TYPE_P2:
				return readP2();
			case TYPE_P3:
				return readP3();
			case TYPE_P4:
				return readP4();
			case TYPE_P5:
				return readP5();
			case TYPE_P6:
				return readP6();
			case TYPE_P7:
				return readP7();
			default:
				// impossible
				return null;
			}
		}
		finally
		{
			CKit.close(in);
		}
	}


	// pbm: 1-bit plaintext bitmap
	protected BufferedImage readP1() throws Exception
	{
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				int v = readInt();
				
				int rgb = (v == 0) ? 0xffffff : 0; 
				im.setRGB(x, y, rgb);
			}
		}
		
		return im;
	}
	
	
	// pgm: plaintext monochrome
	protected BufferedImage readP2() throws Exception
	{
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		float f = 255f / maxVal;
		
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				int v = Math.round(readInt() * f);
			
				int rgb = (v << 16) | (v << 8) | v;
				im.setRGB(x, y, rgb);
			}
		}
		
		return im;
	}
	
	
	// ppm: plaintext rgb format
	protected BufferedImage readP3() throws Exception
	{
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		float f = 255f / maxVal;
		
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				int r = Math.round(readInt() * f);
				int g = Math.round(readInt() * f);
				int b = Math.round(readInt() * f);
			
				int rgb = (r << 16) | (g << 8) | b;
				im.setRGB(x, y, rgb);
			}
		}
		
		return im;
	}
	
	
	// pbm: 1 bit binary bitmap 
	protected BufferedImage readP4() throws Exception
	{
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		
		for(int y=0; y<height; y++)
		{
			int shift = -1;
			int b = 0;
			
			for(int x=0; x<width; x++)
			{
				if(shift < 0)
				{
					b = read();
					shift = 7;
				}
				
				int rgb = (((b >> shift) & 1) == 0) ? 0xffffff : 0; 
				im.setRGB(x, y, rgb);
				--shift;
			}
		}
		
		return im;
	}
	
	
	protected BufferedImage readP5() throws Exception
	{
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		float f = 255f / maxVal;
		
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				int v = Math.round(read() * f);
			
				int rgb = (v << 16) | (v << 8) | v;
				im.setRGB(x, y, rgb);
			}
		}
		
		return im;
	}
	
	
	protected BufferedImage readP6() throws Exception
	{
		// A raster of Height rows, in order from top to bottom. 
		// Each row consists of Width pixels, in order from left to right.
		// Each pixel is a triplet of red, green, and blue samples, in that order. 
		// Each sample is represented in pure binary by either 1 or 2 bytes. 
		// If the Maxval is less than 256, it is 1 byte. Otherwise, it is 2 bytes. 
		// The most significant byte is first.
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		if(singleByte)
		{
			boolean scale = maxVal != 255;
			float f = 255f / maxVal;
			
			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					int r = read();
					int g = read();
					int b = read();
					
					if(scale)
					{
						r = Math.round(r * f);
						g = Math.round(g * f);
						b = Math.round(b * f);
					}
					
					int rgb = (r << 16) | (g << 8) | b;
					im.setRGB(x, y, rgb);
				}
			}
		}
		else
		{
			float f = 255f / maxVal;
			
			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					int r = Math.round(read2() * f);
					int g = Math.round(read2() * f);
					int b = Math.round(read2() * f);
				
					int rgb = (r << 16) | (g << 8) | b;
					im.setRGB(x, y, rgb);
				}
			}
		}
		
		return im;
	}
	
	
	// pam: not supported
	protected BufferedImage readP7() throws Exception
	{
		// TODO
		throw new Exception("P7 not supported");
	}
}
