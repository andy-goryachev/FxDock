// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


// 3rd Generation Partnership Project 3GPP (nn=0x14)
// and 3GPP2 (nn=0x20) multimedia files (3GG, 3GP, 3G2)
// 00 00 00 nn 66 74 79 70 33 67 70
// 00 00 00 nn 66 74 79 70 33 67 32 - (nn=0x18) 3G2
public class MatcherFTyp
	extends FFMatcher
{
	private byte[] sig = bytes("000000");
	private byte[] prefix3GP = bytes("667479703367");
	private byte[] prefixMP4 = bytes("667479706d7034");
	private byte[] prefixMOV = bytes("6674797071742020");
	private byte[] prefixM4A = bytes("667479704D344120");
	
		
	public FileType match(String filename, byte[] b)
	{
		if(match(b, sig))
		{
			if(match(b, prefix3GP, 4))
			{
				return FileType.TGP;
			}
			else if(match(b, prefixMP4, 4))
			{
				return FileType.MP4;
			}
			else if(match(b, prefixMOV, 4))
			{
				return FileType.MOV;
			}
			else if(match(b, prefixM4A, 4))
			{
				return FileType.M4A;
			}
		}
		return null;
	}
}
