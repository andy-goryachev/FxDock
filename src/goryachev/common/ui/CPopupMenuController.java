// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Log;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;


public abstract class CPopupMenuController
	implements MouseListener
{
	public void onDoubleClick() { }
	
	public void onMouseClick(boolean left) { }
	
	/** create the popup menu.  returning null results in no popup being shown */
	public abstract JPopupMenu constructPopupMenu();
	
	//
	
	private boolean enforceTableSelection = true;
	private int clickx;
	private int clicky;
	private Component source;
	private boolean pressed;
	
	
	public CPopupMenuController(JComponent c)
	{
		monitor(c);
	}
	
	
	public CPopupMenuController(JComponent c, JComponent ... cs)
	{
		this(c);
		for(JComponent x: cs)
		{
			monitor(x);
		}
	}
	
	
	public CPopupMenuController()
	{
	}
	
	
	public void monitor(JComponent c)
	{
		// remove all instances of previously set CPopupMenuControllers
		for(MouseListener x: c.getMouseListeners())
		{
			if(x instanceof CPopupMenuController)
			{
				c.removeMouseListener(x);
			}
		}
		
		c.addMouseListener(this);
	}
	
	
	public void setEnforceTableSelection(boolean on)
	{
		enforceTableSelection = on;
	}
	
	
	/** valid only during constructPopupMenu() */
	public int getClickX()
	{
		return clickx;
	}

	
	/** valid only during constructPopupMenu() */
	public int getClickY()
	{
		return clicky;
	}
	
	
	public Point getClickPoint()
	{
		return new Point(clickx, clicky);
	}
	
	
	/** valid only during constructPopupMenu() */
	public Component getSourceComponent()
	{
		return source;
	}
	

	public void mouseClicked(MouseEvent ev)
	{
		switch(ev.getClickCount())
		{
		case 1:
			onMouseClick(UI.isLeftButton(ev));
			break;
		case 2:
			if(UI.isLeftButton(ev))
			{
				onDoubleClick();
				ev.consume();
			}
			break;
		}
	}


	public void mousePressed(MouseEvent ev)
	{
		clickx = ev.getX();
		clicky = ev.getY();
		source = (Component)ev.getSource();
		pressed = true;
		
		if(source instanceof JTable)
		{
			if(enforceTableSelection)
			{
				Point p = ev.getPoint();
				JTable table = (JTable)source;
				int row = table.rowAtPoint(p);
				int col = table.columnAtPoint(p);
				if((row >= 0) && (col >= 0))
				{
					if(!table.isCellSelected(row, col))
					{
						table.changeSelection(row, col, false, false);
					}
				}
			}
		}
		else if(source instanceof JTextComponent)
		{
			JTextComponent tc = (JTextComponent)source;
			if(UI.isRightButton(ev))
			{
				Caret c = tc.getCaret();
				if(c.getMark() == c.getDot())
				{
					// place caret under cursor if no selection
					try
					{
						int pos = tc.viewToModel(ev.getPoint());
						tc.setCaretPosition(pos);
					}
					catch(Exception e)
					{
						Log.err(e);
					}
				}
			}
			
			tc.requestFocusInWindow();
		}
		
		if(ev.isPopupTrigger())
		{
			showPopupMenu(ev);
		}
	}


	public void mouseReleased(MouseEvent ev)
	{
		windowsHack(ev);
		
		if(ev.isPopupTrigger())
		{
			showPopupMenu(ev);
		}
		
		pressed = false;
	}
	
	
	protected void windowsHack(MouseEvent ev)
	{
		// special case for Windows: when another popup menu already exists
		// the click dismisses it and no mouse button press is received. 
		if(!pressed)
		{
			// simulate press
			MouseEvent simulatedEv = new MouseEvent
			(
				(Component)ev.getSource(),
				MouseEvent.MOUSE_PRESSED,
				ev.getWhen(),
				ev.getModifiers(),
				ev.getX(),
				ev.getY(),
				ev.getXOnScreen(),
				ev.getYOnScreen(),
				ev.getClickCount(),
				ev.isPopupTrigger(),
				ev.getButton()
			);
			mousePressed(simulatedEv);
		}
	}


	public void mouseEntered(MouseEvent ev)
	{
	}


	public void mouseExited(MouseEvent ev)
	{
	}


	protected void showPopupMenu(MouseEvent ev)
	{
		try
		{
			if(source != null)
			{
				JPopupMenu m = constructPopupMenu();
				if(m != null)
				{
					UI.showPopup(source, ev.getX(), ev.getY(), m);
				}
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		ev.consume();
	}
}
