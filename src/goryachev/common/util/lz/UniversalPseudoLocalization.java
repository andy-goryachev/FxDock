// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;


// TODO randomly add CJK symbols as well
public class UniversalPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
		add
		(
			"AАÁĀ",
			"BВБ",
			"DČСĊ",
			"DĎ",
			"EЕЁÉĚĒ",
			"FГF",
			"GĠĞ",
			"HНH",
			"IÍĲĪİ",
			"KК",
			"LLĿ",
			"MМ",
			"NИŇ",
			"OОÓŒŐŌ",
			"PPР",
			"RЯŘ",
			"SŠŞ",
			"TТŤ",
			"UЦÚŮŰŪ",
			"WШЩẀẂŴ",
			"XXХ",
			"YYУÝŸŶ",
			"ZŽ",
			//			
			"aaаáā",
			"bьъб",
			"cсčċ",
			"ddď",
			"eеёéěē",
			"ff",
			"gġğ",
			"iíĳīı",
			"kк",
			"llŀ",
			"mм",
			"nnňп",
			"oоóőō",
			"ppр",
			"rгř",
			"sšş",
			"tтť",
			"uцúůűū",
			"wшщẁẃŵ",
			"xxх",
			"yуýŷ",
			"zž"
		);
	}
}
