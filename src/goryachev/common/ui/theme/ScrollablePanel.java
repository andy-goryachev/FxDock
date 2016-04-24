// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


/** special panel tracks container width */
public class ScrollablePanel
	extends JPanel
	implements Scrollable
{
	private boolean trackHeight;
	private boolean trackWidth = true;
	private boolean fillViewPortHeight;
	
	
	public ScrollablePanel(LayoutManager layout)
	{
		super(layout);
	}
	
	
	public ScrollablePanel(Component c)
	{
		super(new BorderLayout());
		add(c,BorderLayout.CENTER);
	}
	
	
	public ScrollablePanel(Component c, boolean fillViewPortHeight)
	{
		this(c);
		this.fillViewPortHeight = fillViewPortHeight;
	}

	
	public Dimension getPreferredScrollableViewportSize()
	{
		return null;
	}

	
	public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction)
	{
		return 10;
	}

	
	public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction)
	{
		switch(orientation)
		{
		case SwingConstants.HORIZONTAL:
			return (r.width * 80 / 100);
		case SwingConstants.VERTICAL:
			return (r.height * 80 / 100);
		}
		return 10;
	}

	
	public boolean getScrollableTracksViewportWidth()
	{
		return trackWidth;
	}
	
	
	public void setScrollableTracksViewportWidth(boolean on)
	{
		trackWidth = on;
		validate();
		repaint();
	}

	
	public boolean getScrollableTracksViewportHeight()
	{
		if(fillViewPortHeight)
		{
			return ((getParent() instanceof JViewport) && (((JViewport)getParent()).getHeight() > getPreferredSize().height));
		}
		else
		{
			return trackHeight;
		}
	}
	
	
	public void setScrollableTracksViewportHeight(boolean on)
	{
		trackHeight = on;
		validate();
		repaint();
	}
	
	
	public void setFillViewportHeight(boolean on)
	{
		fillViewPortHeight = on;
	}
}