// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;


public class RussianPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
		add
		(
			"AА",
			"BВБ",
			"CС",
			"EЕЁ",
			"FГ",
			"HН",
			"KК",
			"MМ",
			"NИ",
			"OО",
			"PР",
			"RЯ",
			"TТ",
			"UЦ",
			"WШЩ",
			"XХ",
			"YУ",
			//
			"aа",
			"bьъб",
			"cс",
			"eеё",
			"hн",
			"kк",
			"mм",
			"nп",
			"oо",
			"pр",
			"rг",
			"tт",
			"uц",
			"wшщ",
			"xх",
			"yу"
		);
	}
}
