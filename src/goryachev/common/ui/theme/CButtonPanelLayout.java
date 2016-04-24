// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.util.CList;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


public class CButtonPanelLayout
	implements LayoutManager2
{
	protected static class Entry
	{
		public Component comp;
		public float width;
	}
	
	//
	
	public static final float PREFERRED = -1.0f;
	public static final float FILL = -2.0f;
	private int gap = 10;
	private int minButtonSize = 70;
	private CList<Entry> entries = new CList();
	
	
	public CButtonPanelLayout()
	{
	}
	
	
	public CButtonPanelLayout(int gap)
	{
		this.gap = gap;
	}


	public void setMinButtonSize(int sz)
	{
		minButtonSize = sz;
	}


	public int getGap()
	{
		return gap;
	}


	public void setGap(int gap)
	{
		this.gap = gap;
	}


	public float getLayoutAlignmentX(Container parent)
	{
		return 0.5f;
	}


	public float getLayoutAlignmentY(Container parent)
	{
		return 0.5f;
	}


	public void invalidateLayout(Container target)
	{
	}


	public void addLayoutComponent(String name, Component c)
	{
		addLayoutComponent(c, name);
	}
	

	public void addLayoutComponent(Component c, Object constraints)
	{
		Entry en = new Entry();
		en.comp = c;
		en.width = PREFERRED;
		
		entries.add(en);
	}
	
	
	public void addSpace(float w)
	{
		Entry en = new Entry();
		en.width = w;
		
		entries.add(en);
	}


	public void removeLayoutComponent(Component c)
	{
		for(int i=entries.size()-1; i>=0; --i)
		{
			Entry en = entries.get(i);
			if(en.comp == c)
			{
				entries.remove(i);
				return;
			}
		}
	}
	
	
	protected Dimension computeSize(Container target, int[] cols, boolean preferred)
	{
		int w = 0;
		int h = 0;
		
		int sz = entries.size();
		for(int i=0; i<sz; i++)
		{
			Entry en = entries.get(i);
			Component c = en.comp;
			int cw;
			
			if(c == null)
			{
				// only real pixels
				if(en.width >= 1.0f)
				{
					cw = (int)en.width;
				}
				else
				{
					cw = 0;
				}
			}
			else
			{
				Dimension d = (preferred ? c.getPreferredSize() : c.getMinimumSize());
			
				h = Math.max(d.height, h);
				cw = Math.max(d.width, minButtonSize);
			}
			
			w += cw;
			
			if(cols != null)
			{
				cols[i] = cw;
			}
		}
		
		// gaps
		w += ((sz - 1) * gap);

		// insets
		Insets m = target.getInsets();
		w += (m.left + m.right);
		h += (m.top + m.bottom);
		
		return new Dimension(w, h);
	}


	public Dimension minimumLayoutSize(Container target)
	{
		synchronized(target.getTreeLock())
		{
			return computeSize(target, null, false);
		}
	}


	public Dimension preferredLayoutSize(Container target)
	{
		synchronized(target.getTreeLock())
		{
			return computeSize(target, null, true);
		}
	}


	public Dimension maximumLayoutSize(Container target)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	
	protected int assignWidths(int[] cols, int delta)
	{
		int sz = entries.size();
		int fillCount = 0;
		float percent = 0;
		boolean hasPercent = false;
		
		// count fills and percents
		for(int i=0; i<sz; i++)
		{
			Entry en = entries.get(i);
			float w = en.width;
			
			if(w == FILL)
			{
				fillCount++;
			}
			else if((w >= 0.f) && (w < 1.0f))
			{
				percent += w;
				hasPercent = true;
			}
		}
		
		// assign percents first
		int rem = delta;
		
		for(int i=0; i<sz; i++)
		{
			Entry en = entries.get(i);
			float w = en.width;
			
			if((w >= 0.f) && (w < 1.0f))
			{
				int cw;
				if(rem > 0)
				{
					cw = Math.round(w * delta);
					rem -= cw;
				}
				else
				{
					cw = 0;
				}
				
				cols[i] = cw;
			}
		}
		
		// then fills
		if(fillCount > 0)
		{
			int fw = rem / fillCount;
			
			for(int i=0; i<sz; i++)
			{
				Entry en = entries.get(i);
				float w = en.width;
				
				if(w == FILL)
				{
					int cw;
					if(rem >= 0)
					{
						cw = Math.min(fw, rem);
						rem -= cw;
					}
					else
					{
						cw = 0;
					}
					
					cols[i] = cw;
				}
			}
		}
		
		if((fillCount == 0) && (!hasPercent))
		{
			return delta;
		}
		else
		{
			return 0;
		}
	}


	public void layoutContainer(Container target)
	{
		synchronized(target.getTreeLock())
		{
			Insets m = target.getInsets();
			int top = m.top;
			int bottom = target.getHeight() - m.bottom;
			int left = m.left;
			int right = target.getWidth() - m.right;
			int h = bottom - top;
			
			int sz = entries.size();
			int[] cols = new int[sz];

			int pref = computeSize(target, cols, true).width;
			
			int delta = target.getWidth() - pref;
			if(delta < 0)
			{
				delta = 0;
			}
			
			int offset = assignWidths(cols, delta);
			
			boolean ltr = target.getComponentOrientation().isLeftToRight();
			if(ltr)
			{
				int x = left + offset;
				
				for(int i=0; i<sz; i++)
				{
					Entry en = entries.get(i);
					Component c = en.comp;
					
					int w = cols[i];
					
					if(c != null)
					{
						c.setBounds(x, top, w, h);
					}
					
					x += (w + gap);
				}
			}
			else
			{
				int x = right - offset;
				
				for(int i=0; i<sz; i++)
				{
					Entry en = entries.get(i);
					Component c = en.comp;
					
					int w = cols[i];
					
					if(c != null)
					{
						c.setBounds(x - w, top, w, h);
					}
					
					x -= (w + gap);
				}
			}
		}
	}
}
