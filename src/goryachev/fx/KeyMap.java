// Copyright © 2017-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;
import goryachev.common.util.FH;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Key Map maps Runnable actions to a KeyEvent.
 * These mappings are designed to be permanent - there is no provision for
 * removing a mapping.
 */
public class KeyMap
{
	public static final int SHIFT = 0x0000_0001;
	public static final int CTRL = 0x0000_0002;
	public static final int ALT = 0x0000_0004;
	public static final int META = 0x0000_0008;
	public static final int SHORTCUT = 0x0000_0010;
	
	private static final int MASK_MODIFIERS = SHIFT | CTRL | ALT | META | SHORTCUT;
	private static final int ACTUAL_SHORTCUT = getActualShortcutFlag();
	private static final int KEY_PRESSED = 0x8000_0000;
	private static final int KEY_RELEASED = 0x4000_0000;
	private static final int KEY_TYPED = 0x2000_0000;
	private static final Object KEY = new Object();
	
	protected final CMap<KKey,Runnable> actions = new CMap();
	
	
	protected KeyMap()
	{
	}
	
	
	public static void onKeyPressed(Node n, KeyCode code, int modifiers, Runnable r)
	{
		get(n).add(KEY_PRESSED | (modifiers & MASK_MODIFIERS), code, null, r);
	}
	
	
	public static void onKeyPressed(Node n, KeyCode code, int modifiers, FxAction a)
	{
		get(n).add(KEY_PRESSED | (modifiers & MASK_MODIFIERS), code, null, a::fire);
	}
	
	
	public static void onKeyPressed(Node n, KeyCode code, Runnable r)
	{
		get(n).add(KEY_PRESSED, code, null, r);
	}
	
	
	public static void onKeyPressed(Node n, KeyCode code, FxAction a)
	{
		get(n).add(KEY_PRESSED, code, null, a::fire);
	}
	
	
	public static void onKeyReleased(Node n, KeyCode code, int modifiers, Runnable r)
	{
		get(n).add(KEY_RELEASED | (modifiers & MASK_MODIFIERS), code, null, r);
	}
	
	
	public static void onKeyReleased(Node n, KeyCode code, Runnable r)
	{
		get(n).add(KEY_RELEASED, code, null, r);
	}
	
	
	public static void onKeyTyped(Node n, char ch, int modifiers, Runnable r)
	{
		get(n).add(KEY_TYPED | (modifiers & MASK_MODIFIERS), null, String.valueOf(ch), r);
	}
	
	
	public static void onKeyTyped(Node n, char ch, Runnable r)
	{
		get(n).add(KEY_TYPED, null, String.valueOf(ch), r);
	}
	
	
	public static Runnable getActionFor(Node n, KeyEvent ev)
	{
		KeyMap m;
		Object x = n.getProperties().get(KEY);
		if(x instanceof KeyMap)
		{
			m = (KeyMap)x;
		}
		else
		{
			return null;
		}
		
		return m.actions.get(key(ev));
	}
	
	
	protected static KeyMap get(Node n)
	{
		Object x = n.getProperties().get(KEY);
		if(x instanceof KeyMap)
		{
			return (KeyMap)x;
		}
		
		KeyMap m = new KeyMap();
		n.getProperties().put(KEY, m);

		n.addEventHandler(KeyEvent.KEY_PRESSED, (ev) -> m.handleEvent(ev));
		n.addEventHandler(KeyEvent.KEY_RELEASED, (ev) -> m.handleEvent(ev));
		n.addEventHandler(KeyEvent.KEY_TYPED, (ev) -> m.handleEvent(ev));
		return m;
	}
	
	
	protected void handleEvent(KeyEvent ev)
	{
		// FIX
//		SB sb = new SB();
//		if(ev.isAltDown())
//		{
//			sb.a(" alt");
//		}
//		
//		if(ev.isControlDown())
//		{
//			sb.a(" ctrl");
//		}
//
//		if(ev.isMetaDown())
//		{
//			sb.a(" meta");
//		}
//
//		if(ev.isShiftDown())
//		{
//			sb.a(" shift");
//		}
//		
//		if(ev.isShortcutDown())
//		{
//			sb.a(" shortcut");
//		}
		
//		D.print(ev.getEventType(), Hex.toHexString(ev.getCharacter().getBytes()), ev.getCode(), sb); // FIX
		
		KKey k = key(ev);
		Runnable a = actions.get(k);
		if(a != null)
		{
			a.run();
			ev.consume();
		}
	}

	
	protected void add(int flags, KeyCode code, String ch, Runnable r)
	{
		if((flags & SHORTCUT) != 0)
		{
			// convert shortcut into actual modifier key
			flags &= (~SHORTCUT);
			flags |= ACTUAL_SHORTCUT;
		}
		
//		D.f("add 0x%8x %s", flags, code);
		
		actions.put(new KKey(flags, code, ch), r);
	}
	
	
	protected static int getActualShortcutFlag()
	{
		KeyCode k = FX.getShortcutKeyCode();
		switch(k)
		{
        case ALT:
        	return ALT;
		case CONTROL:
			return CTRL;
        case META:
        	return META;
		case SHIFT:
			return SHIFT;
        default:
        	// should not happen
        	return 0;
		}
	}

	
	protected static KKey key(KeyEvent ev)
	{
		int flags = 0;
		KeyCode cd;
		String ch;
		
		if(ev.getEventType() == KeyEvent.KEY_PRESSED)
		{
			flags |= KEY_PRESSED;
			cd = ev.getCode();
			ch = null;
		}
		else if(ev.getEventType() == KeyEvent.KEY_RELEASED)
		{
			flags |= KEY_RELEASED;
			cd = ev.getCode();
			ch = null;
		}
		else if(ev.getEventType() == KeyEvent.KEY_TYPED)
		{
			flags |= KEY_TYPED;
			cd = null;
			ch = ev.getCharacter();
		}
		else
		{
			throw new Error("?" + ev.getEventType());
		}
		
		if(ev.isAltDown())
		{
			flags |= ALT;
		}
		
		if(ev.isControlDown())
		{
			flags |= CTRL;
		}

		if(ev.isMetaDown())
		{
			flags |= META;
		}

		if(ev.isShiftDown())
		{
			flags |= SHIFT;
		}
		
		if(ev.isShortcutDown())
		{
			flags |= ACTUAL_SHORTCUT;
		}
		
//		D.f("key event 0x%8x %s", flags, ev.getCode());
		
		return new KKey(flags, cd, ch);
	}
	
	
	//
	
	
	protected static class KKey
	{
		protected final int flags;
		protected final KeyCode code;
		protected final String ch;


		public KKey(int flags, KeyCode keyCode, String ch)
		{
			this.flags = flags;
			this.code = keyCode;
			this.ch = ch;
		}
		
		
		public boolean equals(Object x)
		{
			if(x == this)
			{
				return true;
			}
			else if(x instanceof KKey)
			{
				KKey k = (KKey)x;
				return
					(flags == k.flags) &&
					CKit.equals(code, k.code) &&
					CKit.equals(ch, k.ch);
			}
			else
			{
				return false;
			}
		}
		
		
		public int hashCode()
		{
			int h = FH.hash(KKey.class);
			h = FH.hash(h, flags);
			h = FH.hash(h, code);
			h = FH.hash(h, ch);
			return FH.hash(h, flags);
		}
	}
}
