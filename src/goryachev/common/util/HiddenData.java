// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.DReader;
import goryachev.common.io.DWriterBytes;
import java.util.Random;


/** Simple silly data encoder.  Is it portable though (may have a different Random instance)? */
@SuppressWarnings("resource")
public class HiddenData
{
	public static byte[] encode(byte[] b) throws Exception
	{
		DReader in = new DReader(b);
		Random r = new Random();
		long seed = r.nextLong();
		r.setSeed(seed);
		
		DWriterBytes wr = new DWriterBytes();
		wr.writeLong(seed);
		
		int c;
		while((c = in.readByteRaw()) >= 0)
		{
			wr.writeByte(c ^ r.nextInt());
		}
		
		return wr.toByteArray();
	}

	
	public static byte[] decode(byte[] b) throws Exception
	{
		DReader in = new DReader(b);
		long seed = in.readLong();
		Random r = new Random();
		r.setSeed(seed);
		
		DWriterBytes wr = new DWriterBytes();
		int c;
		while((c = in.readByteRaw()) >= 0)
		{
			wr.writeByte(c ^ r.nextInt());
		}
		
		return wr.toByteArray();
	}
}
