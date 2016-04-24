// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


//	52 49 46 46 	  	RIFF
//	ANI 	  	Windows animated cursor
//	CMX 	  	Corel Presentation Exchange (Corel 10 CMX) Metafile
//	CDR 	  	CorelDraw document
//	DAT 	  	Video CD MPEG or MPEG1 movie file
//	
//	DS4 	  	Micrografx Designer v4 graphic file
//	4XM 	  	4X Movie video
//	
//	52 49 46 46 xx xx xx xx
//	41 56 49 20 4C 49 53 54 	  	RIFF....
//	AVI LIST
//	AVI 	  	Resource Interchange File Format -- Windows Audio
//	Video Interleave file
//	
//	52 49 46 46 xx xx xx xx
//	43 44 44 41 66 6D 74 20 	  	RIFF....
//	CDDAfmt
//	CDA 	  	Resource Interchange File Format -- Compact Disc
//	Digital Audio (CD-DA) file
//	
//	52 49 46 46 xx xx xx xx
//	51 4C 43 4D 66 6D 74 20 	  	RIFF....
//	QLCMfmt
//	QCP 	  	Resource Interchange File Format -- Qualcomm
//	PureVoice
//	
//	52 49 46 46 xx xx xx xx
//	52 4D 49 44 64 61 74 61 	  	RIFF....
//	RMIDdata
//	RMI 	  	Resource Interchange File Format -- Windows Musical
//	Instrument Digital Interface file
//	
//	52 49 46 46 xx xx xx xx
//	57 41 56 45 66 6D 74 20 	  	RIFF....
//	WAVEfmt
//	WAV 	  	Resource Interchange File Format -- Audio for
//	Windows file
public class MatcherRIFF
	extends FFMatcher
{
	private byte[] prefix = bytes("52494646");
	private byte[] avi = bytes("415649204C495354");
	//private byte[] cdda = bytes("43444441666D7420");
	private byte[] wav = bytes("57415645666D7420");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, prefix))
		{
			if(match(b, avi, 8))
			{
				return FileType.AVI;
			}
			else if(match(b, wav, 8))
			{
				return FileType.WAV;
			}
//			else if(match(b, cdda, 8))
//			{
//				return FileType.CDDA;
//			}
		}
		return null;
	}
}
