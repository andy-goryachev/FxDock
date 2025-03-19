// Copyright © 2024-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.input;
import goryachev.common.util.CKit;
import goryachev.common.util.CPlatform;
import goryachev.common.util.FH;
import goryachev.fx.input.internal.KMod;
import java.util.EnumSet;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Key Binding represents a key press (or release, or typed) event
 * with the optional modifier keys.
 */
public class KB
{
	/** not applicable to the current platform */
	public final static KB NA = new KB(null, null);
	private final Object key;
	private final EnumSet<KMod> modifiers;


	private KB(Object key, EnumSet<KMod> modifiers)
	{
		this.key = key;
		this.modifiers = modifiers;
	}


	EventType<KeyEvent> getEventType()
	{
		if(modifiers.contains(KMod.KEY_RELEASED))
		{
			return KeyEvent.KEY_RELEASED;
		}
		else if(modifiers.contains(KMod.KEY_TYPED))
		{
			return KeyEvent.KEY_TYPED;
		}
		return KeyEvent.KEY_PRESSED;
	}


	@Override
	public int hashCode()
	{
		int h = FH.hash(KB.class);
		h = FH.hash(h, key);
		return FH.hash(h, modifiers);
	}


	@Override
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof KB k)
		{
			return CKit.equals(key, k.key) && modifiers.equals(k.modifiers);
		}
		return false;
	}


	public static Builder b(KeyCode k)
	{
		return new Builder(k);
	}


	public static Builder b(String ch)
	{
		return new Builder(ch);
	}


	private static KB create(Object k, KMod ... mods)
	{
		return new Builder(k, mods).build();
	}


	public static KB of(KeyCode k)
	{
		return create(k);
	}


	public static KB alt(KeyCode k)
	{
		return create(k, KMod.ALT);
	}


	public static KB command(KeyCode k)
	{
		return create(k, KMod.COMMAND);
	}
	
	
	public static KB commandShift(KeyCode k)
	{
		return create(k, KMod.COMMAND, KMod.SHIFT);
	}


	public static KB ctrl(KeyCode k)
	{
		return create(k, KMod.CTRL);
	}


	public static KB ctrlShift(KeyCode k)
	{
		return create(k, KMod.CTRL, KMod.SHIFT);
	}


	public static KB meta(KeyCode k)
	{
		return create(k, KMod.META);
	}


	public static KB option(KeyCode k)
	{
		return create(k, KMod.OPTION);
	}
	
	
	public static KB optionShift(KeyCode k)
	{
		return create(k, KMod.OPTION, KMod.SHIFT);
	}


	public static KB shift(KeyCode k)
	{
		return create(k, KMod.SHIFT);
	}


	public static KB shiftOption(KeyCode k)
	{
		return create(k, KMod.SHIFT, KMod.OPTION);
	}


	public static KB shiftShortcut(KeyCode k)
	{
		return create(k, KMod.SHIFT, KMod.SHORTCUT);
	}


	public static KB shortcut(KeyCode k)
	{
		return create(k, KMod.SHORTCUT);
	}


	public static KB windows(KeyCode k)
	{
		return create(k, KMod.WINDOWS);
	}


	public static KB fromKeyEvent(KeyEvent ev)
	{
		Object key;
		EnumSet<KMod> m = EnumSet.noneOf(KMod.class);
		EventType<KeyEvent> t = ev.getEventType();
		if(t == KeyEvent.KEY_PRESSED)
		{
			key = ev.getCode();
		}
		else if(t == KeyEvent.KEY_RELEASED)
		{
			m.add(KMod.KEY_RELEASED);
			key = ev.getCode();
		}
		else if(t == KeyEvent.KEY_TYPED)
		{
			m.add(KMod.KEY_TYPED);
			key = ev.getCharacter();
		}
		else
		{
			return NA;
		}

		boolean mac = CPlatform.isMac();
		boolean win = CPlatform.isWindows();

		boolean alt = ev.isAltDown();
		boolean ctrl = ev.isControlDown();
		boolean meta = ev.isMetaDown();
		boolean shortcut = ev.isShortcutDown();
		boolean option = false;
		boolean command = false;

		// must match KB.Builder.build()
		if(mac)
		{
			if(alt)
			{
				alt = false;
				option = true;
			}
			if(shortcut)
			{
				meta = false;
				command = true;
			}
		}
		else
		{
			if(ctrl)
			{
				shortcut = false;
			}
		}

		if(alt)
		{
			m.add(KMod.ALT);
		}
		
		if(command)
		{
			m.add(KMod.COMMAND);
		}
		
		if(ctrl)
		{
			m.add(KMod.CTRL);
		}
		
		if(meta)
		{
			m.add(KMod.META);
		}
		
		if(option)
		{
			m.add(KMod.OPTION);
		}
		
		if(ev.isShiftDown())
		{
			m.add(KMod.SHIFT);
		}
		
		return new KB(key, m);
	}


	/**
	 * Builder.
	 */
	public static class Builder
	{
		private final Object key;
		private final EnumSet<KMod> mods = EnumSet.noneOf(KMod.class);


		public Builder(Object key)
		{
			this.key = key;
		}


		public Builder(Object key, KMod ... ms)
		{
			this(key);
			for(KMod m: ms)
			{
				mods.add(m);
			}
		}


		public Builder alt()
		{
			mods.add(KMod.ALT);
			return this;
		}


		public Builder command()
		{
			mods.add(KMod.COMMAND);
			return this;
		}


		public Builder ctrl()
		{
			mods.add(KMod.CTRL);
			return this;
		}


		public Builder meta()
		{
			mods.add(KMod.META);
			return this;
		}


		public Builder option()
		{
			mods.add(KMod.OPTION);
			return this;
		}


		public Builder shift()
		{
			mods.add(KMod.SHIFT);
			return this;
		}


		public Builder shortcut()
		{
			mods.add(KMod.ALT);
			return this;
		}


		public Builder keyReleased()
		{
			mods.add(KMod.KEY_RELEASED);
			mods.remove(KMod.KEY_TYPED);
			return this;
		}


		public Builder keyTyped()
		{
			mods.add(KMod.KEY_TYPED);
			mods.remove(KMod.KEY_RELEASED);
			return this;
		}
		
		
		private void replaceIfFound(KMod remove, KMod replace)
		{
			if(mods.contains(remove))
			{
				mods.remove(remove);
				mods.add(replace);
			}
		}


		public KB build()
		{
			boolean mac = CPlatform.isMac();
			boolean win = CPlatform.isWindows();
			boolean linux = CPlatform.isLinux();

			// must match KB.forKeyEvent()
			if(linux)
			{
				replaceIfFound(KMod.SHORTCUT, KMod.CTRL);
			}
			else if(mac)
			{
				replaceIfFound(KMod.ALT, KMod.OPTION);
				replaceIfFound(KMod.META, KMod.COMMAND);
				replaceIfFound(KMod.SHORTCUT, KMod.COMMAND);
			}
			else if(win)
			{
				replaceIfFound(KMod.SHORTCUT, KMod.CTRL);
			}
			
			if(!mac)
			{
				if(mods.contains(KMod.COMMAND) || mods.contains(KMod.OPTION))
				{
					return NA;
				}

				replaceIfFound(KMod.WINDOWS, KMod.META);
			}
			
			return new KB(key, mods);
		}
	}
}
