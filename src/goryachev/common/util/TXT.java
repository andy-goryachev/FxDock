// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.util.lz.PseudoLocalizationPromptProvider;
import goryachev.common.util.lz.TXTFormat;
import java.util.Locale;


/**
 * This class is used to supply localized prompts in one language, as in, for example,
 * a desktop application.  This code should not be used in a server environment where
 * access to localized prompts may be required in several languages at the same time.
 * <p>
 * 
 * The "master" prompts in a "source" language are hard-coded into the code base. 
 * A resource containing the actual translation is loaded at run time.  This simplifies 
 * software development and localization, since the developers do not have to maintain
 * text resources such as message.properties files separately.  A special scanner is required
 * to extract the source prompts from the code base.
 * 
 * <p><pre>
 * Usage:
 *   TXT.get(String id, String master);
 *   TXT.get(String id, String master, Object ... args);
 * </pre>
 * The id must be globally unique (sorry).  I would recommend to use the class name prefix
 * which incidentally imposes a limitation of globally unique class names (as asking for a package
 * name would be too much).
 * <p>
 * 
 * The key (prompt id) is visible to the translators.  Since there are no limitations on the length or
 * format of the prompt id, it is recommended to provide as much contextual information as possible -
 * especially for short prompts.  Where possible, part of the key may contain the prompt used in a phrase,
 * which would later help translators identify whether the prompt is a
 * <p>
 * - verb or a noun<br>
 * - applies to an animate or inanimate object<br>
 * - specify an object the prompt applies to<br>
 * <p>
 * (Example: a single "Print" prompt may be a verb or a noun, but if you write "print the document" 
 * the meaning becomes absolutely clear.
 * <p>
 * 
 * Arguments:
 *   TXT.get("Longer.property NAME is set to VALUE","Property {0} is set to {1}", arg1, arg2);
 * <p>
 *  
 * Test Mode:
 *   Add the following JVM command line option:<pre>
 *     -Di18n.test=dot      adds a unicode dot prefix to internationalized prompts.
 *     -Di18n.test=pseudo   turns on pseudolocalization.
 *     -Di18n.test=pd       turns on pseudolocalization with dot prefix.
 */
public class TXT
{
	enum Mode 
	{
		OFF, 
		DOT, 
		PSEUDO,
		PSEUDO_DOT
	};
	
	public static final String PROPERTY_KEY = "i18n.test";
	public static final char TEST_DOT_CHAR = '\u2022';
	private static PromptProvider provider = createDefaultProvider();
	private static Character testChar;
	private static Mode mode = Mode.OFF;
	private static WeakList<HasPrompts> hasPrompts;
	private static CObjectProperty<CLanguage> languageProperty;

	
	/** returns localized prompt in the current language */
	public static String get(String key, String master)
	{
		String s = provider.getPrompt(key, master);
		if(s == null)
		{
			s = master;
		}
		
		switch(mode)
		{
		case DOT:
		case PSEUDO_DOT:
			// mark internationalized untranslated prompts with a bullet
			String html = "<html>";
			if(CKit.startsWithIgnoreCase(s, html))
			{
				// retain html formatting 
				return html + testChar + s.substring(html.length(), s.length());
			}
			else
			{
				return testChar + s;
			}
		}
			
		return s;
	}
	
	
	/**
	 * Returns parameterized string constructed from localized pattern resolved with 
	 * supplied arguments.  This is a preferred way to construct complex strings, 
	 * as the order of words may change from language to language.
	 * 
	 * Unlike MessageFormat.format(), this method does not render numbers and dates
	 * using the current locale.  Additionally, null arguments are ignored.
	 * 
	 * Example:
	 *   String message = get("My.Message","Your name is {0} {1}\n",firstName,lastName);
	 *   
	 * TODO allow for proper localization of plural forms, something like this:
	 *   get("id", "You have exactly {0,plural,month,months} to live", months);
	 *   
	 * see also - get(String,String)
	 */
	public static String get(CLanguage la, String key, String format, Object[] args)
	{
		return new TXTFormat(la, get(key,format), args).format();
	}
	
	
	/**
	 * Returns parameterized string constructed from localized pattern resolved with 
	 * supplied arguments.  This is a preferred way to construct complex strings, 
	 * as the order of words may change from language to language.
	 * 
	 * Unlike MessageFormat.format(), this method does not render numbers and dates
	 * per current locale.  Neither this method swallows ' characters.
	 * null arguments renderer into empty strings.
	 * 
	 * Example:
	 *   String message = TXT.get("My.Message","Your name is {0} {1}\n",firstName,lastName);
	 *   
	 * TODO allow for proper localization of plural forms, something like this:
	 *   TXT.get("id","You have exactly {0,plural,month,months} to live",months);
	 *   
	 * see also - get(String,String)
	 */
	public static String get(String key, String format, Object... args)
	{
		return get(getLanguage(), key, format, args);
	}
	
	
	private static void setTestMode(Mode m)
	{
		mode = m;
		
		switch(m)
		{
		case PSEUDO_DOT:
			{
				PseudoLocalizationPromptProvider p = new PseudoLocalizationPromptProvider();
				p.setLanguage(getLanguage());
				setTestChar(TEST_DOT_CHAR);
				setPromptProvider(p);
			}
			break;
			
		case PSEUDO:
			{
				PseudoLocalizationPromptProvider p = new PseudoLocalizationPromptProvider();
				p.setLanguage(getLanguage());
				setTestChar(null);
				setPromptProvider(p);
			}
			break;
			
		case OFF:
			setTestChar(null);
			break;
			
		case DOT:
			setTestChar(TEST_DOT_CHAR);
			break;
		}
	}


	/** add -Di18n.test=[dot,pseudo] to JVM parameters to set the i18n test mode */
	public static void checkTestMode()
	{
		String s = System.getProperty(PROPERTY_KEY);
		if("dot".equals(s))
		{
			setTestMode(Mode.DOT);
		}
		else if("pseudo".equals(s))
		{
			setTestMode(Mode.PSEUDO);
		}
		else if("pd".equals(s))
		{
			setTestMode(Mode.PSEUDO_DOT);
		}
		else
		{
			setTestMode(Mode.OFF);
		}
	}


	public static String format(String fmt, Object ... args)
	{
		return new TXTFormat(getLanguage(), fmt, args).format();
	}
	
	
	public static void setTestChar(Character c)
	{
		testChar = c;
	}
	

	private static PromptProvider createDefaultProvider()
	{
		return new PromptProvider()
		{
			public String getPrompt(String id, String master) { return master; }
			public void setLanguage(CLanguage la) { }
		};
	}
	
	
	public static void setPromptProvider(PromptProvider p)
	{
		if(mode == Mode.PSEUDO)
		{
			if(!(p instanceof PseudoLocalizationPromptProvider))
			{
				// ignore
				return;
			}
		}
		
		if(p == null)
		{
			p = createDefaultProvider();
		}
		
		provider = p;
	}
	
	
	public static CObjectProperty<CLanguage> getLanguageProperty()
	{
		if(languageProperty == null)
		{
			languageProperty = new CObjectProperty<CLanguage>(CLanguage.getDefault());
		}
		return languageProperty;
	}
	
	
	public static void setLanguage(CLanguage la)
	{
		if(CKit.notEquals(getLanguage(), la))
		{
			getLanguageProperty().set(la);
			Locale.setDefault(la.getLocale());
			
			provider.setLanguage(la);
			
			CList<HasPrompts> clients = getPromptListeners();
			if(clients != null)
			{
				for(HasPrompts c: clients)
				{
					c.updatePrompts();
				}
			}
		}
	}
	
	
	public static CLanguage getLanguage()
	{
		return getLanguageProperty().get();
	}
	
	
	public synchronized static void registerListener(HasPrompts c)
	{
		if(hasPrompts == null)
		{
			hasPrompts = new WeakList<HasPrompts>();
		}
		hasPrompts.add(c);
	}
	
	
	private synchronized static CList<HasPrompts> getPromptListeners()
	{
		if(hasPrompts != null)
		{
			return hasPrompts.asList();
		}
		return null;
	}
}
