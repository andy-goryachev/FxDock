// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class AgScrollBarUI
	extends BasicScrollBarUI
{
	private static int trackThickness = 7;
	private static int thumbGap = 3;
	private static final Border BORDER = new CBorder.UIResource();
	private static final BasicStroke STROKE = new BasicStroke(trackThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static final Color THUMB_COLOR = ThemeColor.shadow(ThemeKey.TEXT_BG, 128);
	private static final Color DIRECTION_COLOR = ThemeColor.create(ThemeKey.TARGET_COLOR, 0.5, ThemeKey.TEXT_BG);
	
	
	public AgScrollBarUI()
	{
	}


	public static void init(UIDefaults d)
	{
		d.put("ScrollBarUI", AgScrollBarUI.class.getName());
		d.put("ScrollBar.minimumThumbSize", new DimensionUIResource(10, 10));
		d.put("ScrollBar.maximumThumbSize", new DimensionUIResource(4096, 4096));
		d.put("ScrollBar.border", BORDER);
		d.put("ScrollBar.track", ThemeColor.shadow(ThemeKey.TEXT_BG, 0.05));
		
		d.put("ScrollBar.background", Theme.TEXT_BG);
		d.put("ScrollBar.foreground", Theme.TEXT_FG);
		
		//thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
		//thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
		//thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
		//thumbColor = UIManager.getColor("ScrollBar.thumb");
		//trackColor = UIManager.getColor("ScrollBar.track");
		//trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
	}


	// UIManager.getUI(JComponent) uses reflection to invoke this method.  not nice.
	public static ComponentUI createUI(JComponent c)
	{
		return new AgScrollBarUI();
	}


	protected void installDefaults()
	{
		super.installDefaults();
	}
	
	
    public void installUI(JComponent c)
    {
    	super.installUI(c);
    }


	public void uninstallUI(JComponent c)
	{
		super.uninstallUI(c);
	}


	protected void installComponents()
	{
		scrollbar.setEnabled(scrollbar.isEnabled());
	}


	protected void uninstallComponents()
	{
	}


	protected JButton createDecreaseButton(int orientation)
	{
		// no buttons
		return null;
	}


	protected JButton createIncreaseButton(int orientation)
	{
		// no buttons
		return null;
	}


	protected ArrowButtonListener createArrowButtonListener()
	{
		// no buttons
		return null;
	}


	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
	{
		g.setColor(trackColor);
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

		if(trackHighlight == DECREASE_HIGHLIGHT)
		{
			paintDecreaseHighlight(g);
		}
		else if(trackHighlight == INCREASE_HIGHLIGHT)
		{
			paintIncreaseHighlight(g);
		}
	}


	protected void paintThumb(Graphics gg, JComponent c, Rectangle tr)
	{
		if(tr.isEmpty() || !scrollbar.isEnabled())
		{
			return;
		}

		Graphics2D g = (Graphics2D)gg;
		UI.setAntiAliasing(g);

		g.setStroke(STROKE);
		g.setColor(THUMB_COLOR);

		boolean vertical = (scrollbar.getOrientation() == JScrollBar.VERTICAL);
		if(vertical)
		{
			if(tr.height > trackThickness)
			{
				int x = c.getWidth() / 2;
				int y = tr.y + trackThickness / 2;
				g.drawLine(x, y + thumbGap, x, y + tr.height - trackThickness - thumbGap);
			}
		}
		else
		{
			int y = c.getHeight() / 2;
			int x = tr.x + trackThickness / 2;
			g.drawLine(x + thumbGap, y, x + tr.width - trackThickness - thumbGap, y);
		}
	}


	protected void paintDecreaseHighlight(Graphics g)
	{
		Insets m = scrollbar.getInsets();
		Rectangle tr = getThumbBounds();
		int x;
		int y;
		int w;
		int h;

		if(scrollbar.getOrientation() == JScrollBar.VERTICAL)
		{
			x = m.left;
			y = m.top;
			w = scrollbar.getWidth() - (m.left + m.right);
			h = tr.y - y;
		}
		else
		{
			x = m.left;
			y = m.top;
			w = tr.x - x;
			h = scrollbar.getHeight() - (m.top + m.bottom);
		}

		g.setColor(DIRECTION_COLOR);
		g.fillRect(x, y, w, h);
	}


	protected void paintIncreaseHighlight(Graphics g)
	{
		int x;
		int y;
		int w;
		int h;

		Insets m = scrollbar.getInsets();
		Rectangle tr = getThumbBounds();

		if(scrollbar.getOrientation() == JScrollBar.VERTICAL)
		{
			x = m.left;
			y = tr.y + tr.height;
			w = scrollbar.getWidth() - (m.left + m.right);
			h = scrollbar.getHeight() - (m.top + m.bottom) - y;
		}
		else
		{
			x = tr.x + tr.width;
			y = m.top;
			w = scrollbar.getWidth() - (m.left + m.right) - x;
			h = scrollbar.getHeight() - (m.top + m.bottom);
		}

		g.setColor(DIRECTION_COLOR);
		g.fillRect(x, y, w, h);
	}


	protected void setThumbRollover(boolean on)
	{
		super.setThumbRollover(on);
	}


	public Dimension getPreferredSize(JComponent c)
	{
		int w = trackThickness + thumbGap + thumbGap;
		return (scrollbar.getOrientation() == JScrollBar.VERTICAL) ? new Dimension(w, 48) : new Dimension(48, w);
	}


	protected void layoutVScrollbar(JScrollBar sb)
	{
		int width = sb.getWidth();
		int height = sb.getHeight();
		Insets m = sb.getInsets();

		int w = width - m.left - m.right;
		int y0 = height - m.bottom;
		
		float trackHeight = height - m.top - m.bottom;
		float min = sb.getMinimum();
		float extent = sb.getVisibleAmount();
		float range = sb.getMaximum() - min;
		float val = sb.getValue();

		int th = (range <= 0) ? getMaximumThumbSize().height : (int)(trackHeight * (extent / range));
		th = Math.max(th, getMinimumThumbSize().height);
		th = Math.min(th, getMaximumThumbSize().height);

		int ty = y0 - th;
		
		if(val < (sb.getMaximum() - sb.getVisibleAmount()))
		{
			float thumbRange = trackHeight - th;
			ty = (int)(0.5f + (thumbRange * ((val - min) / (range - extent))));
			ty += m.top;
		}

		trackRect.setBounds(m.left, m.top, w, y0 - m.top);

		if(th >= (int)trackHeight)
		{
			setThumbBounds(0, 0, 0, 0);
		}
		else
		{
			if((ty + th) > y0)
			{
				ty = y0 - th;
			}
			if(ty < m.top)
			{
				ty = m.top + 1;
			}
			
			setThumbBounds(m.left, ty, w, th);
		}
	}


	protected void layoutHScrollbar(JScrollBar sb)
	{
		int width = sb.getWidth();
		int height = sb.getHeight();
		Insets m = sb.getInsets();
		
		int h = height - m.top - m.bottom;
		int x0 = width - m.right;
		
		float trackWidth = width - m.left - m.right;
		float min = sb.getMinimum();
		float max = sb.getMaximum();
		float extent = sb.getVisibleAmount();
		float range = max - min;
		float val = sb.getValue();

		int tw = (range <= 0) ? getMaximumThumbSize().width : (int)(trackWidth * (extent / range));
		tw = Math.max(tw, getMinimumThumbSize().width);
		tw = Math.min(tw, getMaximumThumbSize().width);

		boolean ltr = sb.getComponentOrientation().isLeftToRight();
		int tx = ltr ? x0 - tw : m.left;
		
		if(val < (max - sb.getVisibleAmount()))
		{
			float thumbRange = trackWidth - tw;
			if(ltr)
			{
				tx = (int)(0.5f + (thumbRange * ((val - min) / (range - extent))));
			}
			else
			{
				tx = (int)(0.5f + (thumbRange * ((max - extent - val) / (range - extent))));
			}
			tx += m.left;
		}

		trackRect.setBounds(m.left, m.top, x0 - m.left, h);

		if(tw >= (int)trackWidth)
		{
			setThumbBounds(0, 0, 0, 0);
		}
		else
		{
			if(tx + tw > x0)
			{
				tx = x0 - tw;
			}
			if(tx < m.left)
			{
				tx = m.left + 1;
			}
			
			setThumbBounds(tx, m.top, tw, h);
		}
	}
}
