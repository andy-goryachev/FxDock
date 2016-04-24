// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.diff;
import goryachev.common.util.html.HtmlTools;


public class HtmlDiff
{
	public static String compute(String original, String modified, final boolean showDeleted)
	{
		if(original == null)
		{
			original = "";
		}
		
		if(modified == null)
		{
			modified = "";
		}
		
		final String left = original;
		final String right = modified;
		final StringBuilder sb = new StringBuilder(1024);
		sb.append("<html><body>");
		
		try
		{
			new MyersDiffChar(left, right).compute(new DiffBlockClient()
			{
				public void changed(int startLeft, int sizeLeft, int startRight, int sizeRight) throws Exception
				{
					if(showDeleted)
					{
						if(sizeLeft > 0)
						{
							String s = left.substring(startLeft, startLeft+sizeLeft);
							sb.append("<span style=\"text-decoration:line-through; background-color:#ffc0c0;\">");
							sb.append(HtmlTools.safe(s));
							sb.append("</span>");
						}
					}
					
					if(sizeRight > 0)
					{
						String s = right.substring(startRight, startRight+sizeRight);
						sb.append("<span style=\"background-color:#FFFF00;\">");
						sb.append(HtmlTools.safe(s));
						sb.append("</span>");
					}
				}

				public void unchanged(int startLeft, int sizeLeft, int startRight) throws Exception
				{
					if(sizeLeft > 0)
					{
						String s = left.substring(startLeft, startLeft+sizeLeft);
						sb.append(HtmlTools.safe(s));
					}
				}
			}, false);
		}
		catch(Exception e)
		{ }
		
		return sb.toString();
	}
}
