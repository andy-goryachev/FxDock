// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.ThemeTools;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;


/**
 * A border with a line and empty space inside.
 */
public class CBorder
	extends AbstractBorder
	implements Cloneable
{
	public static final CBorder NONE = new CBorder();
	//public static final CBorder LINE = new CBorder(1, ThemeTools.DARKER);
	
	private short padLeft;
	private short padRight;
	private short padTop;
	private short padBottom;
	private short lineLeft;
	private short lineRight;
	private short lineTop;
	private short lineBottom;
	private short gapLeft;
	private short gapRight;
	private short gapTop;
	private short gapBottom;
	private Color color;
	
	
	public CBorder(int topLine, int leftLine, int bottomLine, int rightLine, Color c, int top, int left, int bottom, int right)
	{
		setLineColor(c);
		setLineTop(topLine);
		setLineLeft(leftLine);
		setLineBottom(bottomLine);
		setLineRight(rightLine);
		setGapTop(top);
		setGapBottom(bottom);
		setGapLeft(left);
		setGapRight(right);
	}
	
	
	public CBorder(CBorder c)
	{
		padLeft = c.padLeft;
		padRight = c.padRight;
		padTop = c.padTop;
		padBottom = c.padBottom;
		lineLeft = c.lineLeft;
		lineRight = c.lineRight;
		lineTop = c.lineTop;
		lineBottom = c.lineBottom;
		gapLeft = c.gapLeft;
		gapRight = c.gapRight;
		gapTop = c.gapTop;
		gapBottom = c.gapBottom;
		color = c.color;
	}
	
	
	public CBorder(int topLine, int leftLine, int bottomLine, int rightLine, Color c, int vertGap, int horGap)
	{
		setLineColor(c);
		setLineTop(topLine);
		setLineLeft(leftLine);
		setLineBottom(bottomLine);
		setLineRight(rightLine);
		setGapTop(vertGap);
		setGapBottom(vertGap);
		setGapLeft(horGap);
		setGapRight(horGap);
	}
	
	
	public CBorder(int line, Color c, int vertGap, int horGap)
	{
		setLineColor(c);
		setLineTop(line);
		setLineLeft(line);
		setLineBottom(line);
		setLineRight(line);
		setGapTop(vertGap);
		setGapBottom(vertGap);
		setGapLeft(horGap);
		setGapRight(horGap);
	}
	
	
	public CBorder(int topLine, int leftLine, int bottomLine, int rightLine, Color c, int gap)
	{
		setLineColor(c);
		setLineTop(topLine);
		setLineLeft(leftLine);
		setLineBottom(bottomLine);
		setLineRight(rightLine);
		setGap(gap);
	}
	
	
	public CBorder(int topLine, int leftLine, int bottomLine, int rightLine, Color c)
	{
		setLineColor(c);
		setLineTop(topLine);
		setLineLeft(leftLine);
		setLineBottom(bottomLine);
		setLineRight(rightLine);
	}
	
	
	// 1 pixel line border
	public CBorder(Color c)
	{
		setLineColor(c);
		setLine(1);
	}
	
	
	// 1 pixel line border with gap inside
	public CBorder(Color c, int gap)
	{
		setLineColor(c);
		setLine(1);
		setGap(gap);
	}
	
	
	// 1 pixel line border with gap inside
	public CBorder(Color c, int vertGap, int horGap)
	{
		setLineColor(c);
		setLine(1);
		setGapTop(vertGap);
		setGapBottom(vertGap);
		setGapLeft(horGap);
		setGapRight(horGap);
	}
	
	
	public CBorder(Color c, int top, int left, int bottom, int right)
	{
		setLineColor(c);
		setLine(1);
		setGapTop(top);
		setGapBottom(bottom);
		setGapLeft(left);
		setGapRight(right);
	}
	
	
	public CBorder(int top, int left, int bottom, int right)
	{
		setGapTop(top);
		setGapBottom(bottom);
		setGapLeft(left);
		setGapRight(right);
	}
	
	
	// color line thick pixels wide, no gap
	public CBorder(int line, Color c)
	{
		setLine(line);
		setLineColor(c);
	}
	
	
	public CBorder(int line, Color c, int gap)
	{
		setLine(line);
		setLineColor(c);
		setGap(gap);
	}

	
	// empty border
	public CBorder(int vertGap, int horGap)
	{
		setGapTop(vertGap);
		setGapBottom(vertGap);
		setGapLeft(horGap);
		setGapRight(horGap);
	}
	
	
	// empty border
	public CBorder(int gap)
	{
		setGap(gap);
	}
	
	
	public CBorder()
	{
	}
	
	
	public CBorder copy()
	{
		return new CBorder(this);
	}
	
	
	public void adjustGaps(int d)
	{
		gapLeft += d;
		gapRight += d;
		gapTop += d;
		gapBottom += d;
	}
	
	
	public void setLine(int t)
	{
		setLineTop(t);
		setLineLeft(t);
		setLineBottom(t);
		setLineRight(t);
	}
	
	
	public void setGap(int gap)
	{
		setGapTop(gap);
		setGapBottom(gap);
		setGapLeft(gap);
		setGapRight(gap);
	}
	
	
	public Color getLineColor()
	{
		return color;
	}
	
	
	public void setLineColor(Color c)
	{
		color = c;
	}
	
	
	public int getPaddingTop()    { return padTop; }
	public int getPaddingBottom() { return padBottom; }
	public int getPaddingLeft()   { return padLeft; }
	public int getPaddingRight()  { return padRight; }
	public int getLineTop()       { return lineTop; }
	public int getLineBottom()    { return lineBottom; }
	public int getLineLeft()      { return lineLeft; }
	public int getLineRight()     { return lineRight; }
	public int getGapTop()        { return gapTop; }
	public int getGapBottom()     { return gapBottom; }
	public int getGapLeft()       { return gapLeft; }
	public int getGapRight()      { return gapRight; }
	
	public void setPaddingTop(int x)    { padTop = validate(x); }
	public void setPaddingBottom(int x) { padBottom = validate(x); }
	public void setPaddingLeft(int x)   { padLeft = validate(x); }
	public void setPaddingRight(int x)  { padRight = validate(x); }
	public void setLineTop(int x)       { lineTop = validate(x); }
	public void setLineBottom(int x)    { lineBottom = validate(x); }
	public void setLineLeft(int x)      { lineLeft = validate(x); }
	public void setLineRight(int x)     { lineRight = validate(x); }
	public void setGapTop(int x)        { gapTop = validate(x); }
	public void setGapBottom(int x)     { gapBottom = validate(x); }
	public void setGapLeft(int x)       { gapLeft = validate(x); }
	public void setGapRight(int x)      { gapRight = validate(x); }
	
	
	protected short validate(int x)
	{
		if(x < 0)
		{
			throw new IllegalArgumentException("negative value: " + x);
		}
		else if(x > Short.MAX_VALUE)
		{
			throw new IllegalArgumentException("value too large: " + x);
		}
		return (short)x;
	}
	

	public Insets getBorderInsets(Component c)
	{
		return new Insets
		(
			getPaddingTop() + getLineTop() + getGapTop(),
			getPaddingLeft() + getLineLeft() + getGapLeft(),
			getPaddingBottom() + getLineBottom() + getGapBottom(),
			getPaddingRight() + getLineRight() + getGapRight()
		);
	}


	public Insets getBorderInsets(Component c, Insets m)
	{
		m.top    = getPaddingTop() + getLineTop() + getGapTop();
		m.left   = getPaddingLeft() + getLineLeft() + getGapLeft();
		m.bottom = getPaddingBottom() + getLineBottom() + getGapBottom();
		m.right  = getPaddingRight() + getLineRight() + getGapRight();
		return m;
	}
	
	
	public boolean isOpaque()
	{
		return false;
	}
	

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		g.translate(x, y);
		
		Color old = g.getColor();
		g.setColor(getLineColor());
		
		int t = getLineTop();
		int b = getLineBottom();
		int le = getLineLeft();
		int ri = getLineRight();
		
		// top
		if(t == 1)
		{
			g.drawLine(padLeft, padTop, w-1-padRight, padTop);
		}
		else if(t > 1)
		{
			g.fillRect(padLeft, padTop, w-padLeft-padRight, t);
		}
		
		// left
		if(le == 1)
		{
			g.drawLine(padLeft, t+padTop, padLeft, h-b-1-padBottom);
		}
		else if(le > 1)
		{
			g.fillRect(padLeft, t+padTop, le, h-t-b-padBottom-padTop); 
		}
		
		// bottom
		if(b == 1)
		{
			g.drawLine(padLeft, h-1-padBottom, w-1-padRight, h-1-padBottom);
		}
		else if(b > 1)
		{
			g.fillRect(padLeft, h-b-padBottom, w-padLeft-padRight, b);
		}
		
		// right
		if(ri == 1)
		{
			g.drawLine(w-1-padRight, t+padTop, w-1-padRight, h-b-1-padBottom);
		}
		else if(ri > 1)
		{
			g.fillRect(w-ri-padRight, t+padTop, ri, h-t-b-padTop-padBottom);
		}
		
		g.setColor(old);
		g.translate(-x, -y);
	}
	
	
	//
	
	
	public static class UIResource extends CBorder
	{
		public UIResource(int vg, int hg)
		{
			super(vg,hg);
		}
		
		
		public UIResource(int g)
		{
			super(g);
		}
		
		
		public UIResource()
		{
		}
	}
}
