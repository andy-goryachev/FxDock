// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CMap;
import goryachev.common.util.CPlatform;
import goryachev.common.util.FH;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * KeyMap.
 */
public class KeyMap
{
	protected static final int SHIFT = 0x0000_0001;
	protected static final int CTRL = 0x0000_0002;
	protected static final int ALT = 0x0000_0004;
	protected static final int META = 0x0000_0008;
	protected static final int SHORTCUT = 0x0000_0010;
	protected static final int KEY_PRESSED = 0x8000_0000;
	protected static final int KEY_RELEASED = 0x4000_0000;
	
	protected final CMap<Key,Runnable> keymap = new CMap();
	
	
	public KeyMap()
	{
	}
	
	
	public void add(KeyCode k, Runnable r)
	{
		add(KEY_PRESSED, k, r);
	}
	
	
	public void ctrl(KeyCode k, Runnable r)
	{
		add(KEY_PRESSED | CTRL, k, r);
	}
	
	
	public void shift(KeyCode k, Runnable r)
	{
		add(KEY_PRESSED | SHIFT, k, r);
	}
	
	
	public void shortcut(KeyCode k, Runnable r)
	{
		int flags = KEY_PRESSED | (CPlatform.isMac() ? META : CTRL);
		add(flags, k, r);
	}
	
	
	protected void add(int flags, KeyCode k, Runnable r)
	{
		keymap.put(new Key(flags, k), r);
	}


	public Runnable getActionForKeyEvent(KeyEvent ev)
	{
		return keymap.get(key(ev));
	}
	
	
	protected static Key key(KeyEvent ev)
	{
		int flags = 0;
		
		if(ev.getEventType() == KeyEvent.KEY_PRESSED)
		{
			flags |= KEY_PRESSED;
		}
		else if(ev.getEventType() == KeyEvent.KEY_RELEASED)
		{
			flags |= KEY_RELEASED;
		}
		
		if(ev.isShiftDown())
		{
			flags |= SHIFT;
		}
		
		if(ev.isControlDown())
		{
			flags |= CTRL;
		}
		
		if(ev.isMetaDown())
		{
			flags |= META;
		}
		
		return new Key(flags, ev.getCode());
	}
	
	
	//
	
	
	protected static class Key
	{
		protected final int flags;
		protected final KeyCode keyCode;


		public Key(int flags, KeyCode keyCode)
		{
			this.flags = flags;
			this.keyCode = keyCode;
		}
		
		
		public boolean equals(Object x)
		{
			if(x == this)
			{
				return true;
			}
			else if(x instanceof Key)
			{
				Key k = (Key)x;
				return (keyCode == k.keyCode) && (flags == k.flags);
			}
			else
			{
				return false;
			}
		}
		
		
		public int hashCode()
		{
			int h = FH.hash(Key.class);
			h = FH.hash(h, keyCode);
			return FH.hash(h, flags);
		}
	}
}
