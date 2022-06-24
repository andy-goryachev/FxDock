// Copyright © 2010-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.Map;


/** An extended version of StringBuilder */
public class SB
	implements Appendable, CharSequence
{
	protected StringBuilder sb;


	public SB(int capacity)
	{
		sb = new StringBuilder(capacity);
	}


	public SB()
	{
		this(32);
	}


	public SB(String s)
	{
		sb = new StringBuilder(s);
	}


	public SB(CharSequence cs)
	{
		sb = new StringBuilder(cs);
	}
	
	
	// SB methods
	
	
	public SB nl()
	{
		sb.append("\n");
		return this;
	}
	
	
	public SB nl(int count)
	{
		for(int i=0; i<count; i++)
		{
			sb.append("\n");
		}
		return this;
	}
	
	
	public SB comma()
	{
		sb.append(',');
		return this;
	}
	
	
	public SB tab()
	{
		sb.append("\t");
		return this;
	}
	
	
	public SB tab(int count)
	{
		for(int i=0; i<count; i++)
		{
			sb.append("\t");
		}
		return this;
	}
	
	
	/** append an object, separating it with the specified delimiter if the buffer is not empty */
	public SB a(char delimiter, Object x)
	{
		if(isNotEmpty())
		{
			sb.append(delimiter);
		}
		
		return a(x);
	}
	
	
	public SB a(Object x)
	{
		if(x != null)
		{
			sb.append(x);
		}
		return this;
	}
	
	
	public SB a(char c)
	{
		sb.append(c);
		return this;
	}
	
	
	public SB line(Object x)
	{
		a(x);
		nl();
		return this;
	}
	
	
	public SB sp()
	{
		sb.append(" ");
		return this;
	}
	
	
	public SB sp(int count)
	{
		for(int i=0; i<count; i++)
		{
			sb.append(" ");
		}
		return this;
	}
	
	
	public SB append(char c, int count)
	{
		for(int i=0; i<count; i++)
		{
			sb.append(c);
		}
		return this;
	}
	
	
	public void safeHtml(Object x)
	{
		if(x != null)
		{
			String s = x.toString();
			s = s.replace("<", "&lt;");
			s = s.replace("&", "&amp;");
			sb.append(s);
		}
	}
	
	
	/** appends json-escaped value */
	public SB safeJson(Object x)
	{
		if(x == null)
		{
			sb.append("null");
		}
		else
		{
			String s = JsonDump.toJsonString(x);
			sb.append(s);
		}
		return this;
	}
	
	
	// StringBuilder methods


	public SB append(Object x)
	{
		sb.append(x);
		return this;
	}


	public SB append(String s)
	{
		sb.append(s);
		return this;
	}
	
	
	public Appendable append(CharSequence cs)
	{
		sb.append(cs);
		return this;
	}


	public SB append(CharSequence cs, int start, int end)
	{
		sb.append(cs, start, end);
		return this;
	}


	public SB append(char str[])
	{
		sb.append(str);
		return this;
	}


	public SB append(char str[], int offset, int len)
	{
		sb.append(str, offset, len);
		return this;
	}


	public SB append(boolean x)
	{
		sb.append(x);
		return this;
	}


	public SB append(char x)
	{
		sb.append(x);
		return this;
	}


	public SB append(int x)
	{
		sb.append(x);
		return this;
	}


	public SB append(long x)
	{
		sb.append(x);
		return this;
	}


	public SB append(float x)
	{
		sb.append(x);
		return this;
	}


	public SB append(double d)
	{
		sb.append(d);
		return this;
	}


	public SB appendCodePoint(int codePoint)
	{
		sb.appendCodePoint(codePoint);
		return this;
	}


	public SB delete(int start, int end)
	{
		sb.delete(start, end);
		return this;
	}


	public SB deleteCharAt(int index)
	{
		sb.deleteCharAt(index);
		return this;
	}


	public SB replace(int start, int end, String str)
	{
		sb.replace(start, end, str);
		return this;
	}


	public SB insert(int index, char str[], int offset, int len)
	{
		sb.insert(index, str, offset, len);
		return this;
	}


	public SB insert(int offset, Object x)
	{
		sb.insert(offset, x);
		return this;
	}


	public SB insert(int offset, String str)
	{
		sb.insert(offset, str);
		return this;
	}


	public SB insert(int offset, char str[])
	{
		sb.insert(offset, str);
		return this;
	}


	public SB insert(int dstOffset, CharSequence cs)
	{
		sb.insert(dstOffset, cs);
		return this;
	}


	public SB insert(int dstOffset, CharSequence cs, int start, int end)
	{
		sb.insert(dstOffset, cs, start, end);
		return this;
	}


	public SB insert(int offset, boolean x)
	{
		sb.insert(offset, x);
		return this;
	}


	public SB insert(int offset, char c)
	{
		sb.insert(offset, c);
		return this;
	}
	
	
	public SB insert(int offset, char c, int count)
	{
		if(count > 0)
		{
			char[] cs = new char[count];
			Arrays.fill(cs, c);
			
			sb.insert(offset, cs);
		}
		return this;
	}


	public SB insert(int offset, int x)
	{
		return insert(offset, String.valueOf(x));
	}


	public SB insert(int offset, long x)
	{
		return insert(offset, String.valueOf(x));
	}


	public SB insert(int offset, float f)
	{
		return insert(offset, String.valueOf(f));
	}


	public SB insert(int offset, double d)
	{
		return insert(offset, String.valueOf(d));
	}


	public int indexOf(String s)
	{
		return indexOf(s, 0);
	}


	public int indexOf(String s, int fromIndex)
	{
		return sb.indexOf(s, fromIndex);
	}
	
	
	public int indexOf(char c)
	{
		return indexOf(c, 0);
	}
	
	
	public int indexOf(char ch, int fromIndex)
	{
		int sz = sb.length();
		for(int i=fromIndex; i<sz; i++)
		{
			char c = sb.charAt(i);
			if(c == ch)
			{
				return i;
			}
		}
		return -1;
	}
	

	public int lastIndexOf(String s)
	{
		return sb.lastIndexOf(s);
	}


	public int lastIndexOf(String s, int fromIndex)
	{
		return sb.lastIndexOf(s, fromIndex);
	}
	
	
	public int lastIndexOf(char c)
	{
		return lastIndexOf(c, sb.length());
	}


	public int lastIndexOf(char ch, int fromIndex)
	{
		if(fromIndex < 0)
		{
			return -1;
		}
		
		int sz = sb.length();
		if(fromIndex > sz)
		{
			fromIndex = sz;
		}
		
		for(int i=fromIndex-1; i>=0; --i)
		{
			char c = sb.charAt(i);
			if(c == ch)
			{
				return i;
			}
		}
		return -1;
	}


	public SB reverse()
	{
		sb.reverse();
		return this;
	}


	public String toString()
	{
		return sb.toString();
	}
	
	
	public int length()
	{
		return getLength();
	}
	
	
	public int getLength()
	{
		return sb.length();
	}
	

	public void setLength(int length)
	{
		sb.setLength(length);
	}


	public void clear()
	{
		sb.setLength(0);
	}


	public char charAt(int ix)
	{
		return sb.charAt(ix);
	}


	public SB replace(String s)
	{
		sb.replace(0, sb.length(), s);
		return this;
	}


	public String substring(int start, int end)
	{
		return sb.substring(start, end);
	}


	public String substring(int start)
	{
		return sb.substring(start);
	}


	public boolean isEmpty()
	{
		return sb.length() == 0;
	}
	
	
	public boolean isNotEmpty()
	{
		return sb.length() > 0;
	}


	public boolean isBlank()
	{
		int len = sb.length();
		for(int i=0; i<len; i++)
		{
			if(!Character.isWhitespace(sb.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}


	public void setCharAt(int index, char c)
	{
		sb.replace(index, index + 1, String.valueOf(c));
	}


	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
	{
		sb.getChars(srcBegin, srcEnd, dst, dstBegin);
	}
	
	
	public char[] getChars()
	{
		int sz = sb.length();
		char[] rv = new char[sz];
		sb.getChars(0, sz, rv, 0);
		return rv;
	}
	
	
	public char[] getCharsAndClear()
	{
		char[] rv = getChars();
		sb.setLength(0);
		return rv;
	}


	public String getAndClear()
	{
		String s = sb.toString();
		sb.setLength(0);
		return s;
	}


	public int indexOfIgnoreCase(String pattern, int fromIndex)
	{
		int len = sb.length();
		int plen = pattern.length();
		
		if(fromIndex >= len)
		{
			return (plen == 0 ? len : -1);
		}
		if(fromIndex < 0)
		{
			fromIndex = 0;
		}
		if(plen == 0)
		{
			return fromIndex;
		}

		char c0 = pattern.charAt(0);
		int max = (len - plen);

		for(int i=fromIndex; i<=max; i++)
		{
			if(!TextTools.isSameIgnoreCase(sb.charAt(i), c0))
			{
				while((++i <= max) && (!TextTools.isSameIgnoreCase(sb.charAt(i), c0)))
				{					
				}
			}

			if(i <= max)
			{
				int j = i + 1;
				int end = j + plen - 1;
				for(int k=1; ((j<end) && (TextTools.isSameIgnoreCase(sb.charAt(j), pattern.charAt(k)))); j++,k++)
				{
				}

				if(j == end)
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public void replace(String old, String newText)
	{
		int start = 0;
		for(;;)
		{
			int ix = sb.indexOf(old, start);
			if(ix < 0)
			{
				return;
			}
			
			sb.replace(ix, ix + old.length(), newText);
			
			start = ix + newText.length();
		}
	}
	
	
	public void replace(char old, String newText)
	{
		int start = 0;
		for(;;)
		{
			int ix = indexOf(old, start);
			if(ix < 0)
			{
				return;
			}
			
			sb.replace(ix, ix + 1, newText);
			
			start = ix + newText.length();
		}
	}
	
	
	public void replace(char old, char newChar)
	{
		for(int i=sb.length()-1; i>=0; i--)
		{
			char c = sb.charAt(i);
			if(c == old)
			{
				sb.setCharAt(i, newChar);
			}
		}
	}
	
	
	public void toLowerCase()
	{
		int sz = sb.length();
		for(int i=0; i<sz; i++)
		{
			char c = sb.charAt(i);
			char n = Character.toLowerCase(c);
			if(n != c)
			{
				sb.setCharAt(i, n);
			}
		}
	}
	
	
	public void toUpperCase()
	{
		int sz = sb.length();
		for(int i=0; i<sz; i++)
		{
			char c = sb.charAt(i);
			char n = Character.toUpperCase(c);
			if(n != c)
			{
				sb.setCharAt(i, n);
			}
		}
	}


	public boolean conditionalNewline()
	{
		if(sb.length() > 0)
		{
			sb.append('\n');
			return true;
		}
		return false;
	}


	public void padLeading(char c, int max, Object v)
	{
		String s = (v == null ? "" : v.toString());
		for(int i=s.length(); i<max; i++)
		{
			sb.append(c);
		}
		sb.append(s);
	}
	
	
	public void padTrailing(char c, int max, Object v)
	{
		String s = (v == null ? "" : v.toString());
		sb.append(s);
		for(int i=s.length(); i<max; i++)
		{
			sb.append(c);
		}
	}
	
	
	/** append all items separated by the separator (all nulls are treated as empty strings) */
	public void addAll(Object[] ss, Object sep)
	{
		boolean first = true;
		for(Object s: ss)
		{
			if(first)
			{
				first = false;
			}
			else
			{
				a(sep);
			}
			
			a(s);
		}
	}


	public CharSequence subSequence(int start, int end)
	{
		return sb.subSequence(start, end);
	}
	
	
	public SB repeat(char c, int count)
	{
		for(int i=0; i<count; i++)
		{
			sb.append(c);
		}
		return this;
	}
	
	
	public SB list(Collection<?> items, char delimiter)
	{
		if(items != null)
		{
			boolean sep = false;
			
			for(Object x: items)
			{
				if(sep)
				{
					sb.append(delimiter);
				}
				else
				{
					sep = true;
				}
				sb.append(x);
			}
		}
		return this;
	}
	
		
	public SB list(Object[] items, char delimiter)
	{
		if(items != null)
		{
			boolean sep = false;
			
			for(Object x: items)
			{
				if(sep)
				{
					sb.append(delimiter);
				}
				else
				{
					sep = true;
				}
				sb.append(x);
			}
		}
		return this;
	}
	
	
	public SB list(Map<?,?> items, char delimiter)
	{
		if(items != null)
		{
			boolean sep = false;
			// would be nice to sort, but keys may not be sortable
			for(Object k: items.keySet())
			{
				if(sep)
				{
					sb.append(delimiter);
				}
				else
				{
					sep = true;
				}
				
				Object v = items.get(k);
				sb.append(k);
				sb.append('=');
				sb.append(v);
			}
		}
		return this;
	}
	
	
	/** appends formatted string, see String.format() */
	public SB format(String fmt, Object ... args)
	{
		Formatter f = new Formatter(sb);
		try
		{
			f.format(fmt, args);
		}
		finally
		{
			CKit.close(f);
		}
		return this;
	}
	
	
	public byte[] getBytes(Charset cs)
	{
		return toString().getBytes(cs);
	}
}
