// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.util.Log;
import java.awt.FontMetrics;
import java.awt.Shape;
import java.awt.Toolkit;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.TabSet;
import javax.swing.text.ViewFactory;


public class XParagraphView
	extends ParagraphView
{
	private CEditorKit kit;


	public XParagraphView(CEditorKit kit, Element elem)
	{
		super(elem);
		this.kit = kit;
	}


	// http://java-sl.com/wrap.html
	public void layout(int width, int height)
	{
		int w = kit.isNoWrapMode() ? 0x3fffffff : width;
		super.layout(w, height);
	}


	// http://java-sl.com/wrap.html
	public float getMinimumSpan(int axis)
	{
		if(axis == X_AXIS)
		{
			return kit.isNoWrapMode() ? super.getPreferredSpan(axis) : super.getMinimumSpan(axis);
		}
		else
		{
			return super.getMinimumSpan(axis);
		}
	}


	// http://java-sl.com/tip_default_tabstop_size.html
	public float nextTabStop(float x, int tabOffset)
	{
		TabSet tabs = getTabSet();
		if(tabs == null)
		{
			FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(getContainer().getFont());
			int t = Math.round(kit.getTabSize() * fm.stringWidth(" "));
			return (getTabBase() + (((int)x / t + 1) * t));
		}

		return super.nextTabStop(x, tabOffset);
	}


	// http://java-sl.com/tip_html_letter_wrap.html
	protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r)
	{
		if(r == null)
		{
			r = new SizeRequirements();
		}
		float pref = layoutPool.getPreferredSpan(axis);
		float min = layoutPool.getMinimumSpan(axis);
		// Don't include insets, Box.getXXXSpan will include them. 
		r.minimum = (int)min;
		r.preferred = Math.max(r.minimum, (int)pref);
		r.maximum = Integer.MAX_VALUE;
		r.alignment = 0.5f;
		return r;
	}
	
	
	// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7141488
	protected void loadChildren(ViewFactory f)
	{
		try
		{
			super.loadChildren(f);
		}
		catch(Exception e)
		{
			Log.print(e);
		}
	}


	// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7141488
	public void changedUpdate(DocumentEvent changes, Shape a, ViewFactory f)
	{
		try
		{
			super.changedUpdate(changes, a, f);
		}
		catch(Exception e)
		{
			Log.print(e);
		}
	}
}