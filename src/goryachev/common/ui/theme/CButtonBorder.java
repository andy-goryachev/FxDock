// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.border.AbstractBorder;


// TODO focused, hover effects
public class CButtonBorder
	extends AbstractBorder
{
	private boolean paintTop;
	private boolean paintBottom;
	private boolean paintLeft;
	private boolean paintRight;
	private boolean paintCorners;
	private boolean pressed;
	
	
	public CButtonBorder(boolean paintTop, boolean paintLeft, boolean paintBottom, boolean paintRight, boolean paintCorners)
	{
		this.paintTop = paintTop;
		this.paintBottom = paintBottom;
		this.paintLeft = paintLeft;
		this.paintRight = paintRight;
		this.paintCorners = paintCorners;
	}
	
	
	public CButtonBorder(boolean paintTop, boolean paintLeft, boolean paintBottom, boolean paintRight)
	{
		this(paintTop, paintLeft, paintBottom, paintRight, true);
	}


	public CButtonBorder(boolean paintCorners)
	{
		this(true, true, true, true, paintCorners);
	}
	
	
	public CButtonBorder()
	{
		this(true, true, true, true, true);
	}


	public Insets getBorderInsets(Component c)
	{
		return getBorderInsets(c, UI.newInsets(0, 0, 0, 0));
	}


	public Insets getBorderInsets(Component c, Insets insets)
	{
		Insets margin = ((AbstractButton)c).getMargin();
		if(margin != null)
		{
			insets.top    = margin.top;
			insets.left   = margin.left;
			insets.bottom = margin.bottom;
			insets.right  =  margin.right;
		}
		
		// line border
		if(paintTop)
		{
			++insets.top;
		}
		if(paintBottom)
		{
			++insets.bottom;
		}
		if(paintLeft)
		{
			++insets.left;
		}
		if(paintRight)
		{
			++insets.right;
		}
		
		return insets;
	}
	
	
	protected Color getTopLineColor()
	{
		return Theme.LINE_COLOR;
	}
	
	
	protected Color getBottomLineColor()
	{
		return Theme.LINE_COLOR;
	}
	
	
	protected Color getLeftLineColor()
	{
		return Theme.LINE_COLOR;
	}
	
	
	protected Color getRightLineColor()
	{
		return Theme.LINE_COLOR;
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		if(paintBottom)
		{
			g.setColor(getBottomLineColor());
			g.drawLine
			(	
				paintCorners ? 0 : 1,
				height-1, 
				paintCorners ? width-1 : width-2,
				height-1
			);
		}
		
		if(paintRight)
		{
			g.setColor(getRightLineColor());
			g.drawLine
			(
				width-1,
				paintCorners ? 0 : 1, 
				width-1, 
				paintCorners ? height-1 : height-2
			);
		}
		
		if(paintLeft)
		{
			g.setColor(getLeftLineColor());
			g.drawLine
			(
				0, 
				paintCorners ? 0 : 1,
				0,
				paintCorners ? height-1 : height-2
			);
		}
		
		if(paintTop)
		{
			g.setColor(getTopLineColor());
			g.drawLine
			(
				paintCorners ? 0 : 1,
				0,
				paintCorners ? width-1 : width-1,
				0
			);
		}
		
		if(pressed)
		{
			g.setColor(ThemeTools.DARKER_DARKER);
			
			g.drawLine
			(
				paintLeft   ? 1 : 0, 
				paintTop    ? 1 : 0,
				paintLeft   ? 1 : 0,
				paintBottom ? height-2 : height-1
			);
			
			g.drawLine
			(
				paintLeft  ? 1 : 0,
				paintTop   ? 1 : 0,
				paintRight ? width-2 : width-1,
				paintTop   ? 1 : 0
			);
		}
	}
	
	
	public void setPaintTop(boolean on)
	{
		paintTop = on;
	}
	
	
	public void setPaintBottom(boolean on)
	{
		paintBottom = on;
	}
	
	
	public void setPaintLeft(boolean on)
	{
		paintLeft = on;
	}
	
	
	public void setPaintRight(boolean on)
	{
		paintRight = on;
	}
	
	
	public void setPaintCorners(boolean on)
	{
		paintCorners = on;
	}
	
	
	public void setPressed(boolean on)
	{
		pressed = on;
	}
}
