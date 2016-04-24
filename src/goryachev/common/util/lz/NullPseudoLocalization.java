// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;


public class NullPseudoLocalization
	extends PseudoLocalization
{
	public String getPrompt(String id, String master)
	{
		return master;
	}


	public String pseudoLocalize(String s)
	{
		return s;
	}
}
