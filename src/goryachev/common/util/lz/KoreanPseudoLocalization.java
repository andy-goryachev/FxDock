// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CMap;


public class KoreanPseudoLocalization
	extends AbstractPseudoLocalization
{
	public static final int START = 0xac00;
	public static final int RANGE = 0xd7af - START + 1;
	
	
	protected void initMap(CMap<Character,String> m)
	{
	}
	
	
	public synchronized String pseudoLocalize(String master)
	{
		return master + hangul();
	}
	
	
	protected char hangul()
	{
		return (char)(START + random(RANGE)); 
	}
}
