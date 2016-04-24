// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;


public class CPopupWindow
{
	protected JWindow window;
	private Component component;
	private Component parent;


	public CPopupWindow()
	{
	}


	public void show(Component invoker, int x, int y, Component popupComponent)
	{		
		Window w = UI.getParentWindow(invoker);
		this.window = new JWindow(w);
		this.parent = invoker;
		this.component = popupComponent;
		
		CPanel pp = new CPanel();
		// TODO subtle raised border
		pp.setBorder(Theme.lineBorder());
		pp.setCenter(popupComponent);
		
		window.getContentPane().add(pp);
		//window.pack();
		//window.pack();

		Point p = new Point(x, y);
		SwingUtilities.convertPointToScreen(p, invoker);
		Rectangle screenSize = getScreenBounds(invoker);
		Dimension size = component.getPreferredSize();

		int left = p.x + size.width;
		int bottom = p.y + size.height;

		if(p.x < screenSize.x)
		{
			p.x = screenSize.x;
		}
		
		if(left > screenSize.width)
		{
			p.x = screenSize.width - size.width;
		}

		if(p.y < screenSize.y)
		{
			p.y = screenSize.y;
		}
		
		if(bottom > screenSize.height)
		{
			p.y -= size.height + invoker.getHeight();
		}

		window.setLocation(p.x, p.y);
		//window.setSize(size);
		window.pack();
		window.setVisible(true);
	}


	public void hide()
	{
		if(parent != null)
		{
			parent.requestFocus();
		}

		if(window != null)
		{
			window.dispose();
			window = null;
		}
	}


	public boolean isVisible()
	{
		if(window != null)
		{
			return window.isVisible();
		}
		return false;
	}


	protected Rectangle getScreenBounds(Component caller)
	{
		Rectangle r = caller.getGraphicsConfiguration().getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(caller.getGraphicsConfiguration());
		r.x += insets.left;
		r.y += insets.top;
		r.width -= insets.left + insets.right;
		r.height -= insets.top + insets.bottom;
		return r;
	}
}