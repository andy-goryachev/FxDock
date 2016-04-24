// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect;
import goryachev.common.util.TXT;


public enum FileType
{
	ASF
	{
		public String getCode() { return "ASF"; }
		public String getName() { return TXT.get("FileType.asf", "Windows ASF"); }
	},
	AVI	
	{
		public String getCode() { return "AVI"; }
		public String getName() { return TXT.get("FileType.avi", "Windows AVI"); }
	},
	BMP
	{
		public String getCode() { return "BMP"; }
		public String getName() { return TXT.get("FileType.bmp", "Windows Bitmap"); }
	},
	CLASS
	{
		public String getCode() { return "CLASS"; }
		public String getName() { return TXT.get("FileType.class", "Java Class"); }
	},
	DOC
	{
		public String getCode() { return "DOC"; }
		public String getName() { return TXT.get("FileType.doc", "MS Word Document"); }
	},
	DOCX
	{
		public String getCode() { return "DOCX"; }
		public String getName() { return TXT.get("FileType.docx", "MS Word Document"); }
	},
	EXE
	{
		public String getCode() { return "EXE"; }
		public String getName() { return TXT.get("FileType.exe", "Windows Executable"); }
	},
	FLV
	{
		public String getCode() { return "FLV"; }
		public String getName() { return TXT.get("FileType.flv", "Flash Video"); }
	},
	GIF
	{
		public String getCode() { return "GIF"; }
		public String getName() { return TXT.get("FileType.gif", "GIF Image"); }
	},
	GZ
	{
		public String getCode() { return "GZ"; }
		public String getName() { return TXT.get("FileType.gz", "GZIP Archive"); }
	},
	HTML
	{
		public String getCode() { return "HTML"; }
		public String getName() { return TXT.get("FileType.html", "HTML File"); }
	},
	ICNS
	{
		public String getCode() { return "ICNS"; }
		public String getName() { return TXT.get("FileType.icns", "Apple Icon"); }
	},
	ICO
	{
		public String getCode() { return "ICO"; }
		public String getName() { return TXT.get("FileType.ico", "Windows Icon"); }
	},
	IFF
	{
		public String getCode() { return "IFF"; }
		public String getName() { return TXT.get("FileType.iff", "IFF Image"); }
	},
	JAR
	{
		public String getCode() { return "JAR"; }
		public String getName() { return TXT.get("FileType.jar", "JAR Archive"); }
	},
	JPEG
	{
		public String getCode() { return "JPEG"; }
		public String getName() { return TXT.get("FileType.jpeg", "JPEG Image"); }
	},
	M4A
	{
		public String getCode() { return "M4A"; }
		public String getName() { return TXT.get("FileType.m4a", "Apple Lossless Audio"); }
	},
	MOV
	{
		public String getCode() { return "MOV"; }
		public String getName() { return TXT.get("FileType.mov", "QuickTime Movie"); }
	},
	MP3
	{
		public String getCode() { return "MP3"; }
		public String getName() { return TXT.get("FileType.mp3", "MP3 Audio"); }
	},
	MP4
	{
		public String getCode() { return "MP4"; }
		public String getName() { return TXT.get("FileType.mp4", "MPEG-4 Video"); }
	},
	NETPBM
	{
		public String getCode() { return "PPM"; }
		public String getName() { return TXT.get("FileType.ppm", "Netpbm Image"); }
	},
	ODP
	{
		public String getCode() { return "ODP"; }
		public String getName() { return TXT.get("FileType.odp", "OpenOffice Presentation"); }
	},
	ODS
	{
		public String getCode() { return "ODS"; }
		public String getName() { return TXT.get("FileType.ods", "OpenOffice Spreadsheet"); }
	},
	ODT
	{
		public String getCode() { return "ODT"; }
		public String getName() { return TXT.get("FileType.odt", "OpenOffice Text Document"); }
	},
	OTF
	{
		public String getCode() { return "OTF"; }
		public String getName() { return TXT.get("FileType.otf", "Open Type Font"); }
	},
	OTT
	{
		public String getCode() { return "OTT"; }
		public String getName() { return TXT.get("FileType.ott", "OpenOffice Template"); }
	},
	PDF
	{
		public String getCode() { return "PDF"; }
		public String getName() { return TXT.get("FileType.pdf", "PDF Document"); }
	},
	PICT
	{
		public String getCode() { return "PICT"; }
		public String getName() { return TXT.get("FileType.pict", "Macintosh PICT"); }
	},
	PNG
	{
		public String getCode() { return "PNG"; }
		public String getName() { return TXT.get("FileType.png", "PNG Image"); }
	},
	PPT
	{
		public String getCode() { return "PPT"; }
		public String getName() { return TXT.get("FileType.ppt", "MS Office Presentation"); }
	},
	PPTX
	{
		public String getCode() { return "PPTX"; }
		public String getName() { return TXT.get("FileType.pptx", "MS Office Presentation"); }
	},
	PS
	{
		public String getCode() { return "PS"; }
		public String getName() { return TXT.get("FileType.ps", "PostScript"); }
	},
	PSD
	{
		public String getCode() { return "PSD"; }
		public String getName() { return TXT.get("FileType.psd", "Photoshop Image"); }
	},
	RAW
	{
		public String getCode() { return "RAW"; }
		public String getName() { return TXT.get("FileType.raw", "Raw Image"); }
	},
	RTF
	{
		public String getCode() { return "RTF"; }
		public String getName() { return TXT.get("FileType.rtf", "RTF Document"); }
	},
	SXC
	{
		public String getCode() { return "SXC"; }
		public String getName() { return TXT.get("FileType.sxc", "OpenOffice Spreadsheet"); }
	},
	SXD
	{
		public String getCode() { return "SXD"; }
		public String getName() { return TXT.get("FileType.sxd", "OpenOffice Drawing"); }
	},
	SXI
	{
		public String getCode() { return "SXI"; }
		public String getName() { return TXT.get("FileType.sxi", "OpenOffice Presentation"); }
	},
	SXW
	{
		public String getCode() { return "SXW"; }
		public String getName() { return TXT.get("FileType.sxw", "OpenOffice Writer Document"); }
	},
	SVG
	{
		public String getCode() { return "SVG"; }
		public String getName() { return TXT.get("FileType.svg", "SVG Image"); }
	},
	TEXT
	{
		public String getCode() { return "TXT"; }
		public String getName() { return TXT.get("FileType.text", "Text File"); }
	},
	TGP
	{
		public String getCode() { return "3GP"; }
		public String getName() { return TXT.get("FileType.3gp", "3GPP Multimedia"); }
	},
	TIFF
	{
		public String getCode() { return "TIFF"; }
		public String getName() { return TXT.get("FileType.tiff", "TIFF Image"); }
	},
	TTF
	{
		public String getCode() { return "TTF"; }
		public String getName() { return TXT.get("FileType.ttf", "TrueType Font"); }
	},
	UNKNOWN
	{
		public String getCode() { return "UNKNOWN"; }
		public String getName() { return TXT.get("FileType.binary", "Binary File"); }
	},
	VSD
	{
		public String getCode() { return "VSD"; }
		public String getName() { return TXT.get("FileType.vsd", "MS Visio Diagram"); }
	},
	WAV
	{
		public String getCode() { return "WAV"; }
		public String getName() { return TXT.get("FileType.wav", "Windows WAV Audio"); }
	},
	WMA
	{
		public String getCode() { return "WMA"; }
		public String getName() { return TXT.get("FileType.wma", "Windows WMA Audio"); }
	},
	WMV
	{
		public String getCode() { return "WMV"; }
		public String getName() { return TXT.get("FileType.wmv", "Windows WMV Video"); }
	},
	XLS
	{
		public String getCode() { return "XLS"; }
		public String getName() { return TXT.get("FileType.xls", "MS Excel Spreadsheet"); }
	},
	XLSX
	{
		public String getCode() { return "XLSX"; }
		public String getName() { return TXT.get("FileType.xlsx", "MS Excel Spreadsheet"); }
	},
	XML
	{
		public String getCode() { return "XML"; }
		public String getName() { return TXT.get("FileType.xml", "XML File"); }
	},
	ZIP
	{
		public String getCode() { return "ZIP"; }
		public String getName() { return TXT.get("FileType.zip", "ZIP Archive"); }
	};

	//
	
	public abstract String getCode();
	
	public abstract String getName();
	
	//
	
	FileType()
	{
	}
	
	
	public static FileType parse(Object x)
	{
		if(x != null)
		{
			String id = x.toString();
			for(FileType t: values())
			{
				if(t.getCode().equals(id))
				{
					return t;
				}
			}
		}
		return UNKNOWN;
	}
}
