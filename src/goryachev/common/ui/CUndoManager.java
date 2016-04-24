// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;


public class CUndoManager
	extends UndoManager
{
	public static final Object KEY_UNDO_MANAGER = new Object();
	public final LocalUndoAction localUndoAction = new LocalUndoAction();
	public final LocalRedoAction localRedoAction = new LocalRedoAction();
	public static final GlobalUndoAction globalUndoAction = new GlobalUndoAction();
	public static final GlobalRedoAction globalRedoAction = new GlobalRedoAction();
	protected static CUndoManager last;
	static
	{
		init();
	}
	

	public CUndoManager()
	{
	}
	
	
	public void attachTo(Container c)
	{
		JComponent jc = findJComponent(c);
		jc.putClientProperty(KEY_UNDO_MANAGER, this);
	}
	
	
	private static void init()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent ev)
			{
				Component c = CFocusMonitor.getLastComponent();
				if(c instanceof JRootPane)
				{
					return;
				}
				
//				if(c != null)
//				{
//					D.p(c.isFocusable());
//				}
				//D.p(Dump.componentHierarchy(c));
				
				CUndoManager u = findUndoManager(c);
				if(u == null)
				{
					// check for popup menu cases
					if(c == null)
					{
						return;
					}
				}

				if(u != last)
				{
					globalUndoAction.setManager(u);
					globalRedoAction.setManager(u);
					last = u;
				}
			}
		});
	}
	
	
//	protected static void updateGlobal(CUndoManager u)
//	{
//
//	}
	
	
	public static JComponent findJComponent(Container c)
	{
		if(c instanceof JFrame)
		{
			// this is a bad idea: invoking a menu is going to lose undo manager attached to some other component
			// if the root pane has another undo manager set.
			return ((JFrame)c).getRootPane();
		}
		else if(c instanceof JComponent)
		{
			return (JComponent)c;
		}
		else
		{
			return null;
		}
	}
	
	
	/** each JTextComponent gets its own instance of CUndoManager */
	public static void monitor(Object ... xs)
	{
		for(Object x: xs)
		{
			CUndoManager u = getUndoManager(x);
			if(u == null)
			{
				if(x instanceof JTextComponent)
				{
					Document d = ((JTextComponent)x).getDocument();
					d.addUndoableEditListener(new CUndoManager());
				}
				else if(x instanceof AbstractDocument)
				{
					((AbstractDocument)x).addUndoableEditListener(new CUndoManager());
				}
				else
				{
					throw new Rex("unable to attach undo manager to " + CKit.simpleName(x));
				}
			}
		}
	}
	
	
	public static void clear(Object ... xs)
	{
		for(Object x: xs)
		{
			CUndoManager u = getUndoManager(x);
			if(u != null)
			{
				u.discardAllEdits();
			}
		}
	}
	
	
	protected static CUndoManager getUndoManager(Object x)
	{
		if(x instanceof JTextComponent)
		{
			Document d = ((JTextComponent)x).getDocument();
			if(d instanceof AbstractDocument)
			{
				for(UndoableEditListener ul: ((AbstractDocument)d).getUndoableEditListeners())
				{
					if(ul instanceof CUndoManager)
					{
						return (CUndoManager)ul;
					}
				}
			}
		}
		else if(x instanceof AbstractDocument)
		{
			for(UndoableEditListener ul: ((AbstractDocument)x).getUndoableEditListeners())
			{
				if(ul instanceof CUndoManager)
				{
					return (CUndoManager)ul;
				}
			}
		}
		else if(x instanceof JComponent)
		{
			Object v = ((JComponent)x).getClientProperty(KEY_UNDO_MANAGER);
			if(v instanceof CUndoManager)
			{
				return (CUndoManager)v;
			}
		}
		
		return null;
	}
	
	
	protected static CUndoManager findUndoManager(Component c)
	{
		while(c != null)
		{
			CUndoManager u = getUndoManager(c);
			if(u != null)
			{
				return u;
			}
			
			c = c.getParent();
		}
		return null;
	}

	
	public synchronized boolean addEdit(UndoableEdit ed)
	{
		boolean rv = super.addEdit(ed);
		update();
		return rv;
	}


	public synchronized void discardAllEdits()
	{
		super.discardAllEdits();
		update();
	}


	protected void update()
	{
		localUndoAction.updateUndoState();
		localRedoAction.updateRedoState();
		
		// FIX name sometimes is wrong...
		globalUndoAction.setManager(this);
		globalRedoAction.setManager(this);
	}
	

	//
	
	
	public class LocalRedoAction
		extends CAction
	{
		public LocalRedoAction()
		{
			super(Menus.Redo);
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
	
	
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				redo();
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			update();
		}
	
	
		protected void updateRedoState()
		{
			if(canRedo())
			{
				setEnabled(true);
				setName(getRedoPresentationName());
			}
			else
			{
				setEnabled(false);
				setName(Menus.Redo);
			}
		}
	}
	
	
	//
	
	
	public class LocalUndoAction
		extends CAction
	{
		public LocalUndoAction()
		{
			super(Menus.Undo);
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
	
	
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				undo();
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			update();
		}
	
	
		protected void updateUndoState()
		{
			if(canUndo())
			{
				setEnabled(true);
				setName(getUndoPresentationName());
			}
			else
			{
				setEnabled(false);
				setName(Menus.Undo);
			}
		}
	}
	
	
	//
	
	
	public abstract static class AbstractGlobalAction
		extends CAction 
		implements PropertyChangeListener
	{
		protected abstract Action getAction(CUndoManager m);
		
		//
		
		private Action action;
		private String defaultText;
		
		
		public AbstractGlobalAction(String defaultText)
		{
			this.defaultText = defaultText;
			setEnabled(false);
			update();
		}
		
		
		public void action()
		{
			// not called
		}
		
		
		public void actionPerformed(ActionEvent ev)
		{
			if(action != null)
			{
				action.actionPerformed(ev);
			}
		}
	
	
		public void propertyChange(PropertyChangeEvent ev)
		{
			String id = ev.getPropertyName();
//			if("Name".equals(id))
//			{
//				update();
//			}
//			else 
			if("enabled".equals(id))
			{
				update();
			}
		}
		
		
		protected void setManager(CUndoManager m)
		{
			Action a = getAction(m);
			if(action != a)
			{
				// change action
				if(action != null)
				{
					action.removePropertyChangeListener(this);
				}
				
				action = a;
				
				if(action != null)
				{
					action.addPropertyChangeListener(this);
				}
				
				update();
			}
		}
		
		
		protected void update()
		{
			boolean en;
			String text;
			
			if(action == null)
			{
				en = false;
				text = null;
			}
			else
			{
				en = action.isEnabled();
				text = (String)action.getValue(Action.NAME);
			}
			
			setEnabled(en);
			
			if(CKit.isBlank(text))
			{
				text = defaultText;
			}
			setText(text);
		}
	}
	
	
	//
	
	
	public static class GlobalUndoAction
		extends AbstractGlobalAction
	{
		public GlobalUndoAction()
		{
			super(Menus.Undo);
		}
		
		
		protected Action getAction(CUndoManager m)
		{
			return m == null ? null : m.localUndoAction;
		}
	}
	
	
	//
	
	
	public static class GlobalRedoAction
		extends AbstractGlobalAction
	{
		public GlobalRedoAction()
		{
			super(Menus.Redo);
		}
		
		
		protected Action getAction(CUndoManager m)
		{
			return m == null ? null : m.localRedoAction;
		}
	}
}
