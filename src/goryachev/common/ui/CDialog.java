// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AssignMnemonic;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class CDialog
	extends JDialog
{
	/** called after window appears on screen */
	public void onWindowOpened() { }
	
	/** called on close() */
	public void onWindowClosed() { }
	
	// return true if ok to close
	public boolean onWindowClosing() { return true; }
	
	// no client code: called from a constructor
	@Deprecated // TODO kill
	protected final boolean closeOnEscape(boolean x) { return true; }
	
	//
	
	public static final int DEFAULT_WIDTH  = 700;
	public static final int DEFAULT_HEIGHT = 550;
	public static final int MARGIN = 10;

	public final CAction closeDialogAction = new CAction() { public void action() { actionWindowClose(); } };
	
	private Component defaultFocusComponent;

	//
	
	public CDialog(Component parent, String name, boolean modal)
	{
		super(UI.getParentWindow(parent));
		setMinimumSize(400, 300);
		setIcon(Application.getIcon());
		setModal(modal);
		setName(name);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent ev)
			{
				focusDefaultComponent();
				onWindowOpened();
			}

			public void windowClosing(WindowEvent ev)
			{
				if(onWindowClosing())
				{
					close();
				}
			}
		});
		
		getContentPane().setLayout(new BorderLayout());
	}
	
	
	public void setCloseOnEscape()
	{
		UI.whenAncestorOfFocusedComponent(this, KeyEvent.VK_ESCAPE, closeDialogAction);
	}
	
	
	/** sets dialog title to app title (s==null) or "s - app title" */
	public void setDialogTitle(String s)
	{
		String appTitle = Application.getTitle();
		String title;
		if(s == null)
		{
			title = appTitle;
		}
		else
		{
			title = s + " - " + appTitle;
		}
		setTitle(title);
	}
	
	
	protected void actionWindowClose()
	{
		if(onWindowClosing())
		{
			close();
		}
	}

	
	public void setDefaultFocusComponent(Component c)
	{
		defaultFocusComponent = c;
	}
	
	
	public void setDefaultButton(JButton b)
	{
		getRootPane().setDefaultButton(b);
	}
	

	public void setDefaultButton()
	{
		CList<CButtonPanel> ps = UI.collectChildrenOfType(CButtonPanel.class, this);
		if(ps.size() == 1)
		{
			CButtonPanel bp = ps.get(0);
			JButton b = bp.getLastButton();
			if(b != null)
			{
				setDefaultButton(b);
				
				// not sure about this
				setDefaultFocusComponent(b);
			}
		}
	}
	
	
	protected void focusDefaultComponent()
	{
		if(defaultFocusComponent != null)
		{
			defaultFocusComponent.requestFocus();
		}
	}
	
	
	public void setIcon(Icon icon)
	{
		if(icon instanceof ImageIcon)
		{
			setIconImage(((ImageIcon)icon).getImage());
		}
	}
	
	
	public Window getParentWindow()
	{
		return UI.getParentWindow(getParent());
	}
	
	
	public CDialog open()
	{
		GlobalSettings.opening(this);
		AssignMnemonic.assign(getRootPane());
		setVisible(true);
		toFront();
		return this;
	}


	public void close()
	{
		GlobalSettings.closing(this);
		
		try
		{
			// FIX this is incorrect
			onWindowClosed();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		setVisible(false);
		dispose();
	}


	public JFrame getOwnerFrame()
	{
		return (JFrame)getOwner();
	}
	
	
	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w,h));
	}
	
	
	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w,h));
	}

	
	public void pack()
	{
		//super.pack();
		UI.setPreferredMinimumSize(this, getRootPane());
	}
	

	/** sets borderless mode.  should be called prior to invoking panel() or buttonPanel() */
	public void borderless()
	{
		CPanel p = new CPanel()
		{
			public CButtonPanel buttonPanel()
			{
				CButtonPanel p = super.buttonPanel();				
				if(p.getBorder() == null)
				{
					p.setBorder(new CBorder(10));
				}
				return p;
			}
		};
		
		setCenter(p);
	}
	
	
	/** returns center CPanel, creating it if necessary */ 
	public CPanel panel()
	{
		Container cp = getContentPane();
		BorderLayout la = (BorderLayout)cp.getLayout();
		
		Component c = la.getLayoutComponent(BorderLayout.CENTER);
		if(c instanceof CPanel)
		{
			return (CPanel)c;
		}
		else
		{
			CPanel p = new CPanel();
			p.setGaps(MARGIN, MARGIN);
			p.setBorder(MARGIN);

			setCenter(p);
			
			return p;
		}
	}
	
	
	public CButtonPanel buttonPanel()
	{
		return panel().buttonPanel();
	}
	
	
	public void setCenter(Component c)
	{
		Container cp = getContentPane();
		BorderLayout la = (BorderLayout)cp.getLayout();
		
		Component old = la.getLayoutComponent(BorderLayout.CENTER);
		if(old != null)
		{
			cp.remove(old);
		}
		
		cp.add(c, BorderLayout.CENTER);
		UI.validateAndRepaint(this);
	}
}
