// Copyright © 2014-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import goryachev.common.util.SB;


public class FindOperationResult
{
	private final String original;
	private final String pattern;
	private final int[] indexes;
	
	
	public FindOperationResult(String original, String pattern, int[] indexes)
	{
		this.original = original;
		this.pattern = pattern;
		this.indexes = indexes;
	}
	
	
	public String replace(String replace)
	{
		SB sb = new SB(2 * original.length());
		int start = 0;
		for(int ix: indexes)
		{
			if(start < ix)
			{
				sb.append(original.substring(start, ix));
				start = ix;
			}
			sb.append(replace);
			start += pattern.length();
		}

		if(start != original.length())
		{
			sb.append(original.substring(start, original.length()));
		}

		return sb.toString();
	}
	
	
	protected void safeAppend(SB sb, String s)
	{
		int start = sb.length();
		sb.append(s);
		int i = sb.length() - 1;
		while(i >= start)
		{
			switch(sb.charAt(i))
			{
			case ' ':
				sb.replace(i, i + 1, "&nbsp;");
				break;
			case '&':
				sb.replace(i, i + 1, "&#38;");
				break;
			case '<':
				sb.replace(i, i + 1, "&#60;");
				break;
			case '\r':
			case '\n':
			case '\t':
				sb.replace(i, i + 1, " ");
				break;
			}

			--i;
		}
	}


	/** returns html string with matched terms highlighted (replace=null) or replacedand highlighedt (replace!=null) */
	public String highlight(String replace)
	{
		return highlight(replace, false);
	}
	
	
	/** returns html string with matched terms highlighted (replace=null) or replacedand highlighedt (replace!=null) */
	public String highlight(String replace, boolean bold)
	{
		String prefix = "<span style='background-color:#ffff00'>";
		String postfix = "</span>";

		SB sb = new SB(3 * original.length());
		sb.append("<html>");

		int start = 0;
		for(int ix: indexes)
		{
			if(start < ix)
			{
				safeAppend(sb, original.substring(start, ix));
				start = ix;
			}

			sb.append(prefix);
			
			if(bold)
			{
				sb.a("<b>");
			}
			
			if(replace == null)
			{
				safeAppend(sb, original.substring(ix, ix + pattern.length()));
			}
			else
			{
				safeAppend(sb, replace);
			}
			
			if(bold)
			{
				sb.a("</b>");
			}
			
			sb.append(postfix);
			start += pattern.length();
		}

		if(start != original.length())
		{
			safeAppend(sb, original.substring(start, original.length()));
		}

		return sb.toString();
	}
}
