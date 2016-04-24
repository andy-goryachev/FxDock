// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;


public class PolishPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
		add
		(
			"AAĄ",
			"BB",
			"CCĆ",
			"DD",
			"EEĘ",
			"FF",
			"GG",
			"HH",
			"II",
			"JJ",
			"KK",
			"LLŁ",
			"MM",
			"NNŃ",
			"OOÓ",
			"PP",
			"RR",
			"SSŚ",
			"TT",
			"UU",
			"WW",
			"YY",
			"ZZŹŻ",
			//
			"aaą",
			"bb",
			"ccć",
			"dd",
			"eeę",
			"ff",
			"gg",
			"hh",
			"ii",
			"jj",
			"kk",
			"llł",
			"mm",
			"nnń",
			"ooó",
			"pp",
			"rr",
			"ssś",
			"tt",
			"uu",
			"ww",
			"yy",
			"zzźż"
		);
	}
}
