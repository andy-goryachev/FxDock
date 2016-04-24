// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CPlatform;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import goryachev.common.util.platform.CPlatformMac;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;


public class KeyNames
{
	public static String getKeyName(KeyStroke key)
	{
		if(key == null)
		{
			return "";
		}
		
		SB sb = new SB();
		
		int modifiers = key.getModifiers();
		if((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0)
		{
			sb.append("Shift").append('-');
		}
		if((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) 
		{
			sb.append("Ctrl").append('-');
		}
		if((modifiers & InputEvent.META_DOWN_MASK) != 0)
		{
			sb.append(getCommandKeyName()).append('-');
		}
		if((modifiers & InputEvent.ALT_DOWN_MASK) != 0) 
		{
			sb.append("Alt").append('-');
		}
		if((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0)
		{
			sb.append("AltGr").append('-');
		}

		if(key.getKeyCode() == KeyEvent.VK_UNDEFINED)
		{
			sb.append(key.getKeyChar());
		}
		else
		{
			sb.append(KeyNames.getKeyCodeName(key.getKeyCode()));
		}

		return sb.toString();
	}
	

	// meta or pretzel
	public static String getCommandKeyName()
	{
		if(CPlatform.isMac())
		{
			return CPlatformMac.APPLE_COMMAND_SIGN;
		}
		else
		{
			return TXT.get("KeyNames.key meta","Meta");
		}
	}


	private static String numpad(char c)
	{
		return TXT.get("KeyNames.key numeric pad character", "Numeric Pad") + " " + c;
	}


	// keys that have English names printed on them are not translated
	public static String getKeyCodeName(int key)
	{
		switch(key)
		{
		case KeyEvent.VK_ENTER:                     return TXT.get("KeyNames.key enter","Enter");
		case KeyEvent.VK_BACK_SPACE:                return TXT.get("KeyNames.key backspace","Backspace");
		case KeyEvent.VK_TAB:                       return "Tab";
		case KeyEvent.VK_CANCEL:                    return TXT.get("KeyNames.key cancel","Cancel");
		case KeyEvent.VK_CLEAR:                     return TXT.get("KeyNames.key clear","Clear");
		case KeyEvent.VK_SHIFT:                     return "Shift";
		case KeyEvent.VK_CONTROL:                   return "Ctrl";
		case KeyEvent.VK_ALT:                       return "Alt";
		case KeyEvent.VK_PAUSE:                     return "Pause";
		case KeyEvent.VK_CAPS_LOCK:                 return "CapsLock";
		case KeyEvent.VK_ESCAPE:                    return "Esc";
		case KeyEvent.VK_SPACE:                     return TXT.get("KeyNames.key space","Space");
		case KeyEvent.VK_PAGE_UP:                   return "Page Up";
		case KeyEvent.VK_PAGE_DOWN:                 return "Page Down";
		case KeyEvent.VK_END:                       return "End";
		case KeyEvent.VK_HOME:                      return "Home";
		case KeyEvent.VK_PERIOD:                    return ".";
		case KeyEvent.VK_LEFT:                      return TXT.get("KeyNames.key left arrow","Left Arrow");
		case KeyEvent.VK_UP:                        return TXT.get("KeyNames.key up arrow","Up Arrow");
		case KeyEvent.VK_RIGHT:                     return TXT.get("KeyNames.key right arrow","Right Arrow");
		case KeyEvent.VK_DOWN:                      return TXT.get("KeyNames.key down arrow","Down Arrow");
		case KeyEvent.VK_NUMPAD0:                   return numpad('0'); 
		case KeyEvent.VK_NUMPAD1:                   return numpad('1');
		case KeyEvent.VK_NUMPAD2:                   return numpad('2');
		case KeyEvent.VK_NUMPAD3:                   return numpad('3');
		case KeyEvent.VK_NUMPAD4:                   return numpad('4');
		case KeyEvent.VK_NUMPAD5:                   return numpad('5');
		case KeyEvent.VK_NUMPAD6:                   return numpad('6');
		case KeyEvent.VK_NUMPAD7:                   return numpad('7');
		case KeyEvent.VK_NUMPAD8:                   return numpad('8');
		case KeyEvent.VK_NUMPAD9:                   return numpad('9');
		case KeyEvent.VK_MULTIPLY:                  return numpad('*');
		case KeyEvent.VK_ADD:                       return numpad('+');
		case KeyEvent.VK_SEPARATOR:                 return TXT.get("KeyNames.key numpad separator","Numeric Pad Separator");
		case KeyEvent.VK_SUBTRACT:                  return numpad('-');
		case KeyEvent.VK_DECIMAL:                   return numpad('.');
		case KeyEvent.VK_DIVIDE:                    return numpad('/');
		case KeyEvent.VK_DELETE:                    return TXT.get("KeyNames.key numpad delete","Numeric Pad Delete");
		case KeyEvent.VK_NUM_LOCK:                  return "NumLock";
		case KeyEvent.VK_SCROLL_LOCK:               return "ScrollLock";
		case KeyEvent.VK_F1:                        return "F1";
		case KeyEvent.VK_F2:                        return "F2";
		case KeyEvent.VK_F3:                        return "F3";
		case KeyEvent.VK_F4:                        return "F4";
		case KeyEvent.VK_F5:                        return "F5";
		case KeyEvent.VK_F6:                        return "F6";
		case KeyEvent.VK_F7:                        return "F7";
		case KeyEvent.VK_F8:                        return "F8";
		case KeyEvent.VK_F9:                        return "F9";
		case KeyEvent.VK_F10:                       return "F10";
		case KeyEvent.VK_F11:                       return "F11";
		case KeyEvent.VK_F12:                       return "F12";
		case KeyEvent.VK_F13:                       return "F13";
		case KeyEvent.VK_F14:                       return "F14";
		case KeyEvent.VK_F15:                       return "F15";
		case KeyEvent.VK_F16:                       return "F16";
		case KeyEvent.VK_F17:                       return "F17";
		case KeyEvent.VK_F18:                       return "F18";
		case KeyEvent.VK_F19:                       return "F19";
		case KeyEvent.VK_F20:                       return "F20";
		case KeyEvent.VK_F21:                       return "F21";
		case KeyEvent.VK_F22:                       return "F22";
		case KeyEvent.VK_F23:                       return "F23";
		case KeyEvent.VK_F24:                       return "F24";
		case KeyEvent.VK_PRINTSCREEN:               return "Print Screen";
		case KeyEvent.VK_INSERT:                    return "Insert";
		case KeyEvent.VK_HELP:                      return "Help";
		case KeyEvent.VK_KP_UP:                     return TXT.get("KeyNames.key numpad up","Numeric Pad Up Arrow");
		case KeyEvent.VK_KP_DOWN:                   return TXT.get("KeyNames.key numpad down","Numeric Pad Down Arrow");
		case KeyEvent.VK_KP_LEFT:                   return TXT.get("KeyNames.key numpad left","Numeric Pad Left Arrow");
		case KeyEvent.VK_KP_RIGHT:                  return TXT.get("KeyNames.key numpad right","Numeric Pad Right Arrow");
		case KeyEvent.VK_AT:                        return "@";
		case KeyEvent.VK_COLON:                     return ":";
		case KeyEvent.VK_CIRCUMFLEX:                return "^";
		case KeyEvent.VK_DOLLAR:                    return "$";
		case KeyEvent.VK_EURO_SIGN:                 return "\u20ac";
		case KeyEvent.VK_EXCLAMATION_MARK:          return "!";
		case KeyEvent.VK_INVERTED_EXCLAMATION_MARK: return "\u00a1";
		case KeyEvent.VK_LEFT_PARENTHESIS:          return "(";
		case KeyEvent.VK_NUMBER_SIGN:               return "#";
		case KeyEvent.VK_PLUS:                      return "+";
		case KeyEvent.VK_RIGHT_PARENTHESIS:         return ")";
		case KeyEvent.VK_UNDERSCORE:                return "_";
		case KeyEvent.VK_WINDOWS:                   return "Windows";
		case KeyEvent.VK_CONTEXT_MENU:              return TXT.get("KeyNames.key context menu","Context Menu");
		case KeyEvent.VK_FINAL:                     return "Final";
		case KeyEvent.VK_CONVERT:                   return "Henkan";
		case KeyEvent.VK_NONCONVERT:                return "Muhenkan";
		case KeyEvent.VK_ACCEPT:                    return "Kakutei";
		case KeyEvent.VK_MODECHANGE:                return "MODECHANGE";
		case KeyEvent.VK_KANA:                      return "Kana";
		case KeyEvent.VK_KANJI:                     return "Kanji";
		case KeyEvent.VK_ALPHANUMERIC:              return "Eisuu";
		case KeyEvent.VK_KATAKANA:                  return "Katakana";
		case KeyEvent.VK_HIRAGANA:                  return "Hiragana";
		case KeyEvent.VK_FULL_WIDTH:                return "Zenkaku";
		case KeyEvent.VK_HALF_WIDTH:                return "Hankaku";
		case KeyEvent.VK_ROMAN_CHARACTERS:          return "Roumaji";
		case KeyEvent.VK_ALL_CANDIDATES:            return "Zenkouho";
		case KeyEvent.VK_PREVIOUS_CANDIDATE:        return "Maekouho";
		case KeyEvent.VK_CODE_INPUT:                return "Kanji Bangou";
		case KeyEvent.VK_JAPANESE_KATAKANA:         return "Katakana";
		case KeyEvent.VK_JAPANESE_HIRAGANA:         return "Hiragana";
		case KeyEvent.VK_JAPANESE_ROMAN:            return "Roman";
		case KeyEvent.VK_KANA_LOCK:                 return "KanaLock";
		case KeyEvent.VK_INPUT_METHOD_ON_OFF:       return "Nihongo";
		case KeyEvent.VK_CUT:                       return "Cut";
		case KeyEvent.VK_COPY:                      return "Copy";
		case KeyEvent.VK_PASTE:                     return "Paste";
		case KeyEvent.VK_UNDO:                      return "Undo";
		case KeyEvent.VK_AGAIN:                     return "Again";
		case KeyEvent.VK_FIND:                      return "Find";
		case KeyEvent.VK_PROPS:                     return "Props";
		case KeyEvent.VK_STOP:                      return "Stop";
		case KeyEvent.VK_COMPOSE:                   return "Compose";
		case KeyEvent.VK_ALT_GRAPH:                 return "AltGr";
		case KeyEvent.VK_BEGIN:                     return "Begin";
		case KeyEvent.VK_BACK_QUOTE:                return "`";
		case KeyEvent.VK_QUOTE:                     return "'";
		}
		
		//D.print(Hex.toHexString(key));
		
		return String.valueOf((char)key);
	}
}
