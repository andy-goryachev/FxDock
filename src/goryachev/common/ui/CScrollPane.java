// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.CScrollBar;
import goryachev.common.ui.theme.AgScrollPaneUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;


public class CScrollPane
	extends JScrollPane
{
	private boolean trackComponentDimensions;


	public CScrollPane(Component view, boolean horScrollBar)
	{
		super(view, VERTICAL_SCROLLBAR_AS_NEEDED, horScrollBar ? HORIZONTAL_SCROLLBAR_AS_NEEDED : HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	
	public CScrollPane(Component view, boolean verticalScrollBar, boolean horScrollBar)
	{
		super(view, verticalScrollBar ? VERTICAL_SCROLLBAR_ALWAYS : VERTICAL_SCROLLBAR_AS_NEEDED, horScrollBar ? HORIZONTAL_SCROLLBAR_AS_NEEDED : HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	
	public CScrollPane(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view, vsbPolicy, hsbPolicy);
	}


	public CScrollPane(Component view)
	{
		this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
		
	
	public void updateUI()
	{
		setUI(new AgScrollPaneUI());
	}
	

	public JScrollBar createHorizontalScrollBar()
	{
		return new CScrollBar(this, JScrollBar.HORIZONTAL);
	}


	public JScrollBar createVerticalScrollBar()
	{
		return new CScrollBar(this, JScrollBar.VERTICAL);
	}


	/** a JTextArea in a scrollpane with this flag set would resize automatically */
	public void setTrackComponentDimensions(boolean on)
	{
		trackComponentDimensions = on;
	}


	public Dimension getMinimumSize()
	{
		if(trackComponentDimensions)
		{
			Component c = getViewport().getView();
			if(c != null)
			{
				return c.getMinimumSize();
			}
		}
			
		return super.getMinimumSize();
	}
	
	
	public Dimension getPreferredSize()
	{
		Dimension d = null;
		
		if(trackComponentDimensions)
		{
			Component c = getViewport().getView();
			if(c != null)
			{
				d = c.getPreferredSize();
				
				if(c instanceof JTable)
				{
					// account for headers
					JTableHeader h = ((JTable)c).getTableHeader();
					if(h != null)
					{
						d.height += h.getPreferredSize().height;
					}
				}
				
				Insets m = getInsets();
				d.height += (m.top + m.bottom);
				d.width += (m.left + m.right);
			}
		}
		
		if(d == null)
		{
			d = super.getPreferredSize();
		}
		
		Dimension max = getMaximumSize();
		if(max != null)
		{
			if(max.width > 0)
			{
				if(max.width < d.width)
				{
					d.width = max.width;
				}
			}
			
			if(max.height > 0)
			{
				if(max.height < d.height)
				{
					d.height = max.height;
				}
			}
		}
		
		return d;
	}
	
	
	/** sets scrollpane and its viewport background */ 
	public void setBackground2(Color c)
	{
		setBackground(c);
		getViewport().setBackground(c);
	}
	
	
	/** sets opaque flag on both the scroll pane and its viewport */
	public void setOpaque2(boolean on)
	{
		setOpaque(on);
		getViewport().setOpaque(on);
	}
	
	
	public void noBorder()
	{
		setBorder(CBorder.NONE);
	}
}
