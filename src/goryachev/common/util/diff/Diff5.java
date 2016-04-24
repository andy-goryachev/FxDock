// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.diff;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;


public class Diff5
{
	private static final char DELIMITER = '\n';
	
	
	public static CList<Difference> compute(Reader left, Reader right) throws Exception
	{
		Uniqtor u = new Uniqtor();
		final CList<String> ls = scan(u, left);
		final CList<String> rs = scan(u, right);
		// gc
		u = null;

		final CList<Difference> a = new CList<Difference>();

		new MyersDiffObject(ls,rs).compute(new DiffBlockClient()
		{
			// TODO unnecessary loops, optimize
			public void changed(int startLeft, int sizeLeft, int startRight, int sizeRight) throws Exception
			{
				int sz = Math.max(sizeLeft, sizeRight);

				// convert back to '\n'-delimited strings
				final SB lb = new SB(2048);
				for(int i=0; i<sizeLeft; i++)
				{
					CKit.checkCancelled();
					
					lb.append(ls.get(startLeft + i));
					lb.append(DELIMITER);
				}
				
				final SB rb = new SB(2048);
				for(int i=0; i<sizeRight; i++)
				{
					CKit.checkCancelled();
					
					rb.append(rs.get(startRight + i));
					rb.append(DELIMITER);
				}
				
				for(int i=0; i<sz; i++)
				{
					CKit.checkCancelled();
					
					String sl = (i < sizeLeft ? ls.get(startLeft + i) : null);
					String sr = (i < sizeRight ? rs.get(startRight + i) : null);
					a.add(Difference.create(sl,sr,null));
				}
			}

			public void unchanged(int startLeft, int sizeLeft, int startRight)
			{
				for(int i=0; i<sizeLeft; i++)
				{
					a.add(Difference.create(ls.get(startLeft + i)));
				}
			}	
		},true);
		
		return a;
	}
	
	
	protected static CList<String> scan(Uniqtor u, Reader reader) throws Exception
	{
		BufferedReader rd = toBufferedReader(reader);
		
		CList<String> a = new CList(4096);
		String s;
		while((s = rd.readLine()) != null)
		{
			CKit.checkCancelled();
			
			a.add(u.getUnique(s));
		}
		return a;
	}
	
	
	protected static BufferedReader toBufferedReader(Reader rd)
	{
		if(rd instanceof BufferedReader)
		{
			return (BufferedReader)rd;
		}
		else if(rd != null)
		{
			return new BufferedReader(rd);
		}
		else
		{
			return new BufferedReader(new StringReader(""));
		}
	}
		
	
	//
	
	
	public static class Uniqtor extends CMap<String,String>
	{
		public String getUnique(String s)
		{
			String unique = get(s);
			if(unique == null)
			{
				put(s,s);
				return s;
			}
			else
			{
				return unique;
			}
		}
	}
	
	
	//
	
	
	// helper class which constructs intra-block diffs
	// using char-by-char comparison
	public static class Differ implements DiffBlockClient
	{
		CList<String> leftStrings;
		CList<String> rightStrings;
		CharSequence left;
		CharSequence right;
		CList<Difference> out;
		CList<Difference> parts;
		int leftString;
		int rightString;
		int leftOffset;
		int rightOffset;

		
		public Differ(CList<String> leftStrings, CList<String> rightStrings, CharSequence left, CharSequence right, CList<Difference> out)
		{
			this.leftStrings = leftStrings;
			this.rightStrings = rightStrings;
			this.left = left;
			this.right = right;
			this.out = out;
			parts = new CList();
		}
		
		
		public void compute() throws Exception
		{
			new MyersDiffChar(left,right).compute(this,true);
		}
		
		
		public void changed(int startLeft, int sizeLeft, int startRight, int sizeRight)
		{
		}

		
		public void unchanged(int startLeft, int sizeLeft, int startRight)
		{
			int start = startLeft;
			for(int i=0; i<sizeLeft; i++)
			{
				if(left.charAt(startLeft + i) == DELIMITER)
				{
					// line end
					parts.add(Difference.create(left.subSequence(start, startLeft + i).toString()));
					start = startLeft + i + 1;
					
					addLine();
				}
			}
			
			if(start != (startLeft + sizeLeft))
			{
				parts.add(Difference.create(left.subSequence(start, startLeft + sizeLeft).toString()));
				addLine();
			}
		}
		
		
		protected void addLine()
		{
			Difference[] ps = parts.toArray(new Difference[parts.size()]);
			// FIX left right strings
			out.add(Difference.create(null,null,ps));
		}
	}
}
