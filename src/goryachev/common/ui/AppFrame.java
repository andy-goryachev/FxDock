// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AssignMnemonic;
import goryachev.common.util.Log;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;


public class AppFrame
	extends JFrame 
{
	/** invoked when user attempts to close the window.  return false to prevent closing the window */
	public boolean onWindowClosing() { return true; }
	
	/** called after window appears on screen */
	public void onWindowOpened() { }
	
	/** called on close() */
	public void onWindowClosed() { }
	
	
	//
	
	
	public final CAction closeAction = new CAction() { public void action() { actionWindowClose(); } }; 
	public final CAction appExitAction = new CAction() { public void action() { Application.exit(); } }; 
	
	protected Rectangle normalBounds;
	

	public AppFrame(String name)
	{
		setName(name);
		setTitle(Application.getTitle());
		setIcon(Application.getIcon());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		WindowAdapter li = new WindowAdapter()
		{
			public void windowClosing(WindowEvent ev)
			{
				if(onWindowClosing())
				{
					close();
				}
			}
			
			public void windowOpened(WindowEvent ev) 
			{
				onWindowOpened();
			}
			
			public void windowStateChanged(WindowEvent ev)
			{
				if(ev.getNewState() == NORMAL)
				{
					normalBounds = getBounds();
				}
			}
		};
		addWindowListener(li);
		addWindowStateListener(li);
	}


	protected void actionWindowClose()
	{
		if(onWindowClosing())
		{
			close();
		}
	}

	
	public Rectangle getNormalSize()
	{
		return normalBounds;
	}
	
	
	public void setNormalBounds(int x, int y, int w, int h)
	{
		normalBounds = new Rectangle(x,y,w,h);
	}
	
	
	public Window open()
	{
		// Window prev = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
		// http://stackoverflow.com/questions/450069/detect-which-monitor-shows-the-window
		
		UI.resize(this,0.8f);
		GlobalSettings.opening(this);
		
		AssignMnemonic.assign(getRootPane());
		
		// TODO open on the monitor where the last focused component is
		setVisible(true);
		
		return this;
	}

	
	public void close()
	{
		GlobalSettings.closing(this);
		GlobalSettings.save();

		try
		{
			onWindowClosed();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		int count = 0;
		Window last = null;
		
		for(Window w: Window.getWindows())
		{
			if(w.isVisible())
			{
				if(!(w instanceof Dialog))
				{
					last = w;
					count++;
				}
			}
		}
		
		if(count == 0)
		{
			Application.exit();
		}
		else if(count == 1)
		{
			if(last == this)
			{
				// exit when closing the last window
				Application.exit();
			}
		}
		
		// close after exit in order to write a proper snapshot
		setVisible(false);
		dispose();
	}
	
	
	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w,h));
	}
	
	
	public void setIcon(ImageIcon ic)
	{
		if(ic != null)
		{
			setIconImage(ic.getImage());
		}
	}
	
	
	public void setTitleWithVersion()
	{
		setTitle(Application.getTitle() + " - " + Application.getVersion());
	}
	
	
	protected void setContentPaneComponent(Component c, Object constraints)
	{
		Container cp = getContentPane();
		Component old = getContentPaneComponent(constraints);
		if(old != null)
		{
			cp.remove(old);
		}
		
		if(c != null)
		{
			cp.add(c, constraints);
		}
		
		UI.validateAndRepaint(this);
	}
	
	
	protected Component getContentPaneComponent(Object constraints)
	{
		Container cp = getContentPane();
		return ((BorderLayout)cp.getLayout()).getLayoutComponent(constraints);
	}
	
	
	public void setCenter(Component c)
	{
		setContentPaneComponent(c, BorderLayout.CENTER);
	}
	
	
	public Component getCenter()
	{
		return getContentPaneComponent(BorderLayout.CENTER);
	}
	
	
	public void setContent(Component c)
	{
		setContentPaneComponent(c, BorderLayout.CENTER);
	}
	
	
	public void setSouth(Component c)
	{
		setContentPaneComponent(c, BorderLayout.SOUTH);
	}
	
	
	public Component getSouth()
	{
		return getContentPaneComponent(BorderLayout.SOUTH);
	}
	
	
	public void setNorth(Component c)
	{
		setContentPaneComponent(c, BorderLayout.NORTH);
	}
	
	
	public Component getNorth()
	{
		return getContentPaneComponent(BorderLayout.NORTH);
	}
	
	
	public void setLeading(Component c)
	{
		setContentPaneComponent(c, BorderLayout.LINE_START);
	}
	
	
	public Component getLeading()
	{
		return getContentPaneComponent(BorderLayout.LINE_START);
	}
	
	
	public void setTrailing(Component c)
	{
		setContentPaneComponent(c, BorderLayout.LINE_END);
	}
	
	
	public Component getTrailing()
	{
		return getContentPaneComponent(BorderLayout.LINE_END);
	}
	
	
	public void setEast(Component c)
	{
		setContentPaneComponent(c, BorderLayout.EAST);
	}
	
	
	public Component getEast()
	{
		return getContentPaneComponent(BorderLayout.EAST);
	}
	
	
	public void setWest(Component c)
	{
		setContentPaneComponent(c, BorderLayout.WEST);
	}
	
	
	public Component getWest()
	{
		return getContentPaneComponent(BorderLayout.WEST);
	}
	

	public CStatusBar createStatusBar(boolean memoryBar)
	{
		return UI.createStatusBar(memoryBar);
	}


	public static AppFrame getAppFrame(Component c)
	{
		return UI.getAncestorOfClass(AppFrame.class, c);
	}


	public void setFixedSize(int w, int h)
	{
		Dimension d = new Dimension(w, h);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
	}
	
	
	public void toFront()
	{
		int st = getExtendedState();
		if(st == Frame.ICONIFIED)
		{
			setExtendedState(Frame.NORMAL);
		}
		
		super.toFront();
	}
	
	
	public void restoreFrame()
	{
		setExtendedState(NORMAL);
	}
	
	
	public void minimizeFrame()
	{
		setExtendedState(MAXIMIZED_BOTH);
	}
	
	
	// in windows, this method is not synchronized, but in openJDK it is
	public synchronized void setExtendedState(int state)
	{
		// will this help with flashing window on mac os x?
		if(getExtendedState() != state)
		{
			super.setExtendedState(state);
		}
	}
	
	
	public void replaceMenuBar(JMenuBar m)
	{
		JRootPane r = getRootPane();
		r.setJMenuBar(m);
		r.validate();
		r.repaint();
	}


	public static void closeAll()
	{
		for(Window w: UI.getVisibleWindows())
		{
			if(w instanceof AppFrame)
			{
				((AppFrame)w).close();
			}
		}
	}
	
	
	public void setCloseOnEscape()
	{
		UI.whenAncestorOfFocusedComponent(this, KeyEvent.VK_ESCAPE, closeAction);
	}
}
