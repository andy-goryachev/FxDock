// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import java.nio.charset.Charset;


public class ChineseTraditionalPseudoLocalization
	extends AbstractPseudoLocalization
{
	private String big5;
	
	
	public ChineseTraditionalPseudoLocalization()
	{
		big5 = initBig5();
	}
	
	
	protected void initMap(CMap<Character,String> m)
	{
	}
	
	
	public synchronized String pseudoLocalize(String master)
	{
		return master + gbk();
	}
	
	
	// randomly generates a Big5-encoded string with one character
	// http://en.wikipedia.org/wiki/Big5
	//
	// First byte    0x81 to 0xfe (or 0xa1 to 0xf9 for non-user-defined characters)
	// Second byte   0x40 to 0x7e, 0xa1 to 0xfe
	//
	// 0x8140 to 0xa0fe 	Reserved for user-defined characters (not included)
	// 0xa140 to 0xa3bf 	"Graphical characters" (not included)
	// 0xa3c0 to 0xa3fe 	Reserved, not for user-defined characters (not included)
	// 0xa440 to 0xc67e 	Frequently used characters
	// 0xc6a1 to 0xc8fe 	Reserved for user-defined characters (not included)
	// 0xc940 to 0xf9d5 	Less frequently used characters (not included)
	// 0xf9d6 to 0xfefe 	Reserved for user-defined characters (not included)
	private String initBig5()
	{
		SB sb = new SB();
		Charset enc = Charset.forName("Big5");
		
		init(sb, enc, 0xa4, 0xc6, 0x40, 0x7e);
		init(sb, enc, 0xa4, 0xc6, 0xa1, 0xfe);
		
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
		return big5.charAt(random(big5.length()));
	}
}
