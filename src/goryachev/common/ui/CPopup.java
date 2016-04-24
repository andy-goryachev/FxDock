// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CPlatform;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicPopupMenuUI;


// a generic popup container
public class CPopup
	extends JPopupMenu
	implements HierarchyBoundsListener
{
	public CPopup(Component c)
	{
		setBorder(CBorder.NONE);

		add(c);
	}


	public void updateUI()
	{
		setUI(new BasicPopupMenuUI());
	}


	public void showAbove(Component c)
	{
		Dimension d = getPreferredSize();
		show(c, 0, -d.height);
	}


	public void showBelow(Component c)
	{
		show(c, 0, c.getHeight());
	}


	public void hidePopup()
	{
		setVisible(false);
	}


	public void ancestorResized(HierarchyEvent e)
	{
		hidePopup();
	}


	public void ancestorMoved(HierarchyEvent e)
	{
		hidePopup();
	}


	public void show(Component c, int x, int y)
	{
		super.show(c, x, y);
		
		if(CPlatform.isMac())
		{
			// mac os x has a weird property of not hiding popup menus when the window is moved
			c.addHierarchyBoundsListener(this);
		}
	}
	
	
	protected void firePopupMenuWillBecomeInvisible()
	{
		// a good time to unregister
		if(CPlatform.isMac())
		{
			getInvoker().removeHierarchyBoundsListener(this);
		}
		
		super.firePopupMenuWillBecomeInvisible();
	}
}