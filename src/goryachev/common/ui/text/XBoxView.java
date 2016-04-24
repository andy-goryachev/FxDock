// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.util.Log;
import javax.swing.text.BoxView;
import javax.swing.text.Element;


public class XBoxView
	extends BoxView
{
	public XBoxView(Element em, int axis)
	{
		super(em, axis);
	}
	
	
	// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7141488
	public void setSize(float width, float height)
	{
		try
		{
			super.setSize(width, height);
		}
		catch(Exception e)
		{
			Log.print(e);
		}
	}


	// https://netbeans.org/bugzilla/show_bug.cgi?id=200649
	protected int getOffset(int axis, int childIndex)
	{
		try
		{
			return super.getOffset(axis, childIndex);
		}
		catch(Exception e)
		{
			Log.print(e);
			return 0;
		}
	}
}
