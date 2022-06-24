// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;


public class CharCounter
{
	public static int count(String s)
	{
		int count = 0;
		
		if(s != null)
		{
			for(int i=0; i<s.length(); i++)
			{
				char c = s.charAt(i);
				if(Character.isLetter(c))
				{
					++count;
				}
			}
		}
		
		return count;
	}
}
