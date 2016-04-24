// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import java.nio.charset.Charset;


public class ChineseSimplifiedPseudoLocalization
	extends AbstractPseudoLocalization
{
	private String gbk;
	
	
	public ChineseSimplifiedPseudoLocalization()
	{
		gbk = initGBK();
	}
	
	
	protected void initMap(CMap<Character,String> m)
	{
	}
	
	
	public synchronized String pseudoLocalize(String master)
	{
		return master + gbk();
	}
	
	
	// randomly generates a GBK-encoded string with one character
	// http://en.wikipedia.org/wiki/GBK
	// range        byte 1  byte 2
	// Level GBK/1  A1–A9   A1–FE
	// Level GBK/2  B0–F7   A1–FE
	// Level GBK/3  81–A0   40–FE except 7F 	
	// Level GBK/4  AA–FE   40–A0 except 7F
	// Level GBK/5  A8–A9   40–A0 except 7F 
	private String initGBK()
	{
		SB sb = new SB();
		Charset enc = Charset.forName("GBK");
		
		init(sb, enc, 0x81, 0xa0, 0x40, 0x7e);
		init(sb, enc, 0x81, 0xa0, 0x80, 0xfe);
		init(sb, enc, 0xa1, 0xa9, 0xa1, 0xfe);
		init(sb, enc, 0xaa, 0xfe, 0x40, 0x7e);
		init(sb, enc, 0xaa, 0xfe, 0x80, 0xa0);
		init(sb, enc, 0xa8, 0xa9, 0x40, 0x7e);
		init(sb, enc, 0xa8, 0xa9, 0x80, 0xa0);
		init(sb, enc, 0xb0, 0xb7, 0xa1, 0xfe);
		
		return sb.toString();
	}
	
	
	private void init(SB sb, Charset enc, int b1start, int b1end, int b2start, int b2end)
	{
		
		byte[] ch = new byte[2];
		for(int b1=b1start; b1<=b1end; b1++)
		{
			for(int b2=b2start; b2<=b2end; b2++)
			{
				ch[0] = (byte)b1;
				ch[1] = (byte)b2;
				String s = new String(ch, enc);
				sb.append(s.charAt(0));
			}
		}
	}
	
	
	protected char gbk()
	{
		return gbk.charAt(random(gbk.length()));
	}
}
