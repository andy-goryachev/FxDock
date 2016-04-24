// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import java.awt.Rectangle;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.Scrollable;


public class CScrollBar
	extends JScrollBar
{
	private static int scrollLineCount = 3;
	public final CScrollPane scroll;
	private boolean unitIncrementSet;
	private boolean blockIncrementSet;


	public CScrollBar(CScrollPane scroll, int orientation)
	{
		super(orientation);
		this.scroll = scroll;
		putClientProperty("JScrollBar.fastWheelScrolling", Boolean.TRUE);
	}
	
	
//	public void setUI(ScrollBarUI ui)
//	{
//		super.setUI(new CScrollBarUI());
//	}


	public void setUnitIncrement(int unitIncrement)
	{
		unitIncrementSet = true;
		putClientProperty("JScrollBar.fastWheelScrolling", null);
		super.setUnitIncrement(unitIncrement);
	}


	public int getUnitIncrement(int direction)
	{
		JViewport vp = scroll.getViewport();
		if(!unitIncrementSet && (vp != null) && (vp.getView() instanceof Scrollable))
		{
			Scrollable view = (Scrollable)(vp.getView());
			Rectangle vr = vp.getViewRect();
			return view.getScrollableUnitIncrement(vr, getOrientation(), direction);
		}
		else
		{
			return Theme.plainFont().getSize() * scrollLineCount;
		}
	}


	public void setBlockIncrement(int blockIncrement)
	{
		blockIncrementSet = true;
		putClientProperty("JScrollBar.fastWheelScrolling", null);
		super.setBlockIncrement(blockIncrement);
	}


	public int getBlockIncrement(int direction)
	{
		JViewport vp = scroll.getViewport();
		if(blockIncrementSet || vp == null)
		{
			return super.getBlockIncrement(direction);
		}
		else if(vp.getView() instanceof Scrollable)
		{
			Scrollable view = (Scrollable)(vp.getView());
			Rectangle vr = vp.getViewRect();
			return view.getScrollableBlockIncrement(vr, getOrientation(), direction);
		}
		else if(getOrientation() == VERTICAL)
		{
			return vp.getExtentSize().height;
		}
		else
		{
			return vp.getExtentSize().width;
		}
	}
}