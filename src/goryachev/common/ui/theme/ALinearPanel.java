// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.LayoutManager;


public class ALinearPanel
	extends ScrollablePanel
{
	public ALinearPanel(boolean horizontal, int gap)
	{
		super(new LinearLayout(horizontal));
		setGap(gap);
	}
	
	
	public ALinearPanel(boolean horizontal)
	{
		this(horizontal, 0);
	}
	
	
	public void space(float spec)
	{
		insertCell(spec);
	}
	
	
	public void space()
	{
		space(10);
	}


	public Component add(float spec, Component c)
	{
		int ix = getCellCount();
		add(c, spec);
		return c;
	}


	public Component add(Component c)
	{
		add(LinearLayout.PREFERRED, c);
		return c;
	}


	public Component fill(Component c)
	{
		add(LinearLayout.FILL, c);
		return c;
	}
	
	
	public void fill()
	{
		insertCell(LinearLayout.FILL);
	}
	
	
	protected void insertCell(float spec)
	{
		int ix = getCellCount();
		linearLayout().setCellSpec(ix, spec);
	}
	
	
	public int getCellCount()
	{
		return linearLayout().getCellCount();
	}


	public void setGap(int gap)
	{
		linearLayout().setGap(gap);
	}
	
	
	public void setLayout(LayoutManager m)
	{
		if(m instanceof LinearLayout)
		{
			super.setLayout(m);
		}
		else
		{
			throw new Rex();
		}
	}
	

	public LinearLayout linearLayout()
	{
		return (LinearLayout)getLayout();
	}
	
	
	public void setCellMinimumSize(int ix, int size)
	{
		linearLayout().setCellMaximumSize(ix, size);
	}
	
	
	public void setCellMaximumSize(int ix, int size)
	{
		linearLayout().setCellMaximumSize(ix, size);
	}
	
	

	/** creates standard 10-pixel border */
	public void border()
	{
		setBorder();
	}


	/** creates standard 10-pixel border */
	public void setBorder()
	{
		setBorder(Theme.border10());
	}


	public CBorder setBorder(int gap)
	{
		CBorder b = new CBorder(gap);
		setBorder(b);
		return b;
	}
	
	
	public CBorder setBorder(int vertGap, int horGap)
	{
		CBorder b = new CBorder(vertGap, horGap); 
		setBorder(b);
		return b;
	}
	
	
	public CBorder setBorder(int top, int left, int bottom, int right)
	{
		CBorder b = new CBorder(top, left, bottom, right);
		setBorder(b);
		return b;
	}
}
