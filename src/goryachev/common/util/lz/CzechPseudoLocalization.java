// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;


public class CzechPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
		add
		(
			"AÁ",
			"CČ",
			"DĎ",
			"ÉĚ",
			"IÍ",
			"NŇ",
			"OÓ",
			"RŘ",
			"SŠ",
			"TŤ",
			"ÚŮ",
			"YÝ",
			"ZŽ",
			//
			"aá",
			"cč",
			"dď",
			"éě",
			"ií",
			"nň",
			"oó",
			"rř",
			"sš",
			"tť",
			"úů",
			"yý",
			"zž"
		);
	}
}
