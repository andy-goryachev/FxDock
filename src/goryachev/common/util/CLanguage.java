// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.ComponentOrientation;
import java.util.Locale;


// See also http://dicts.info/languages2.php 
public class CLanguage
	implements HasProperty
{
	private static TextSplitter splitter;
	private final String locale;
	private final CLanguageCode code;
	
	
	public CLanguage(CLanguageCode code)
	{
		this.locale = code.getCode();
		this.code = code;
	}
	
	
	public CLanguage(String locale)
	{
		this.locale = locale;
		this.code = CLanguageCode.parse(locale);
	}
	
	
	public boolean isDefined()
	{
		return code != null;
	}
	
	
	public CLanguageCode getLanguageCode()
	{
		return code;
	}
	
	
	public static CLanguage[] all()
	{
		CLanguageCode[] codes = CLanguageCode.values();
		int sz = codes.length;
		CLanguage[] langs = new CLanguage[sz];
		for(int i=0; i<sz; i++)
		{
			langs[i] = parse(codes[i]);
		}
		return langs;
	}
	
	
	public static CLanguageCode byID(String lang, String country)
	{
		lang = lang.toLowerCase();
		if(lang.length() < 2)
		{
			throw new RuntimeException("not a language code: " + lang);
		}
		else if(lang.length() > 2)
		{
			lang = lang.substring(0,2);
		}
		
		if(country != null)
		{
			if(country.length() != 2)
			{
				throw new RuntimeException("accepts only two-character country code: " + country);
			}
			country = country.toUpperCase();
		}
		
		CList<CLanguageCode> a = new CList();
		CLanguageCode nocountry = null;
		
		for(CLanguageCode c: CLanguageCode.values())
		{
			if(c.getCode().equals(lang) && (country == null))
			{
				return c;
			}
			else if(c.getCode().startsWith(lang))
			{
				// language match, but need to identify country 
				a.add(c);
				if(c.getCode().length() == 2)
				{
					nocountry = c;
				}
			}
		}
		
		if(a.size() == 1)
		{
			return a.get(0);
		}
		else if(a.size() > 1)
		{
			if(country != null)
			{
				// find country match or return the countryless variant
				for(CLanguageCode c: a)
				{
					String id = c.getCode();
					if(id.length() > 3)
					{
						id = id.substring(3);
						if(id.equals(country))
						{
							return c;
						}
					}
				}
			}
			
			if(nocountry != null)
			{
				return nocountry;
			}
		}
		
		throw new RuntimeException("Unknow language/country code: " + lang + "," + country);
	}
	
	
	public String getID()
	{
		return locale;
	}
	
	
	public String getProperty()
	{
		return getID();
	}
	
	
	public boolean equals(Object x)
	{
		if(this == x)
		{
			return true;
		}
		else if(x instanceof CLanguage)
		{
			CLanguage r = (CLanguage)x;
			if(code != null)
			{
				if(r.code != null)
				{
					return code == r.code;
				}
			}
			return CKit.equals(locale, r.locale);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return CKit.hashCode(CLanguage.class, locale, code);
	}
	
	
	public String toString()
	{
		return getName();
	}
	
	
	/** returns two letter ISO-693-1 language code */
	public String getLangCode()
	{
		String id = getID();
		int ix = id.indexOf('_');
		if(ix < 0)
		{
			return id;
		}
		else
		{
			return id.substring(0,ix);
		}
	}
	
	
	public CLanguage getBaseLanguage()
	{
		return CLanguage.parse(getLangCode());
	}
	
	
	/** returns two-letter ISO-3166 country code */
	public String getCountryCode()
	{
		String id = getID();
		int ix = id.indexOf('_');
		if(ix < 0)
		{
			// should return null
			return "";
		}
		else
		{
			return id.substring(ix+1,id.length());
		}
	}
	
	
	public String getCountryVariant()
	{
		String code = getCountryCode();
		if(CKit.isBlank(code))
		{
			return null;
		}
		else
		{
			return ISO3166.byCountryCode(code);
		}
	}
	
	
	public Locale getLocale()
	{
		return new Locale(getLangCode(), getCountryCode());
	}
	
	
	public static CLanguage getDefault()
	{
		return get(Locale.getDefault());
	}


	public static CLanguage get(Locale locale)
	{
		try
		{
			return new CLanguage(byID(locale.getLanguage(), locale.getCountry()));
		}
		catch(Exception e)
		{
			return new CLanguage(locale.toString());
		}
	}


	public static CLanguage parse(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof CLanguage)
		{
			return (CLanguage)x;
		}
		else if(x instanceof CLanguageCode)
		{
			return new CLanguage((CLanguageCode)x);
		}
		
		String id = x.toString();
		if(CKit.isBlank(id))
		{
			return null;
		}
		
		CLanguageCode c = CLanguageCode.parse(id);
		if(c != null)
		{
			return new CLanguage(c);
		}
		
		// convert to canonical presentation language_COUNTRY
		if(splitter == null)
		{
			splitter = new TextSplitter()
			{
				public boolean isTextSeparator(char c)
				{
					return !Character.isLetter(c);
				}
			};
		}
		CList<String> ss = splitter.split(id);
		if(ss.size() >= 2)
		{
			String la = ss.get(0).toLowerCase();
			c = CLanguageCode.parse(la);
			if(la != null)
			{
				String co = ss.get(1).toUpperCase();
				if(CKit.isBlank(co))
				{
					return new CLanguage(c);
				}
				else
				{
					String id2 = la + "_" + co;
					c = CLanguageCode.parse(id2);
					if(c == null)
					{
						return new CLanguage(id2);
					}
					else
					{
						return new CLanguage(c);
					}
				}
			}
		}
		
		//int ix = id.indexOf('_');
		
		return new CLanguage(id);
	}
	
	
	// for use in a selector
	// http://meta.wikimedia.org/wiki/List_of_Wikipedias
	public String getLocalName()
	{
		if(code == null)
		{
			// allows to support arbitrary country combined with a known language
			String co = getCountryCode();
			if(CKit.isNotBlank(co))
			{
				String ln = getLangCode();
				CLanguageCode c = CLanguageCode.parse(ln);
				if(c != null)
				{
					return c.getLocalName() + "(" + co + ")";
				}
			}
						
			return locale;
		}
		
		return code.getLocalName();
	}
	
	
	public String getName()
	{
		if(code == null)
		{
			// allows to support arbitrary country combined with a known language
			String co = getCountryCode();
			if(CKit.isNotBlank(co))
			{
				String ln = getLangCode();
				CLanguageCode c = CLanguageCode.parse(ln);
				if(c != null)
				{
					return c.getName() + " (" + co + ")";
				}
			}
			
			return locale;
		}
		
		return code.getName();
	}
	
	
	public static CLanguage guess(String s)
	{
		// guess by language
		CLanguageCode lc = CLanguageCode.parse(s);
		if(lc != null)
		{
			return new CLanguage(lc);
		}
		
		// guess by name
		for(CLanguageCode la: CLanguageCode.values())
		{
			if(TextTools.containsIgnoreCase(s, la.getEnglishName()))
			{
				return new CLanguage(la);
			}
			else if(TextTools.containsIgnoreCase(s, TextTools.trimPunctuation(la.getName())))
			{
				return new CLanguage(la);
			}
			else if(TextTools.containsIgnoreCase(s, TextTools.trimPunctuation(la.getLocalName())))
			{
				return new CLanguage(la);
			}
		}
		
		// guess by code
		CLanguageCode found = null;
		for(CLanguageCode la: CLanguageCode.values())
		{
			String code = la.getCode();
			int start = 0;
			for(;;)
			{
				int ix = TextTools.indexOfIgnoreCase(s, code, start);
				if(ix < 0)
				{
					break;
				}
				
				// surrounded by delimiters?
				if(isDelimiter(getChar(s, ix-1)) && isDelimiter(getChar(s, ix+code.length())))
				{
					if(found == null)
					{
						found = la;
					}
					else
					{
						if(found.getCode().length() < code.length())
						{
							found = la;
						}
					}
					break;
				}
				
				start = ix + 1;
			}
		}
		
		if(found != null)
		{
			return new CLanguage(found);
		}
		
		return null;
	}
	
	
	private static int getChar(String s, int pos)
	{
		if(pos < 0)
		{
			return -1;
		}
		else if(pos >= s.length())
		{
			return -1;
		}
		else
		{
			return s.charAt(pos);
		}
	}
	
	
//	private static String[] split(String s)
//	{
//		CList<String> list = new CList();
//		
//		int start = 0;
//		boolean delim = true;
//		for(int i=0; i<s.length(); i++)
//		{
//			char c = s.charAt(i);
//			boolean de = isDelimiter(c);
//			if(de != delim)
//			{
//				if(de)
//				{
//					if(i > start)
//					{
//						list.add(s.substring(start, i));
//					}
//				}
//				else
//				{
//					start = i;
//				}
//				
//				delim = de;
//			}
//		}
//		
//		if(!delim && (start < s.length()))
//		{
//			list.add(s.substring(start));
//		}
//		
//		return list.toArray(new String[list.size()]);
//	}
	
	
	private static boolean isDelimiter(int c)
	{
		switch(c)
		{
		case '_':
			return true;
		}
		
		return !Character.isLetter(c);
	}


	public static Locale getLocale(CLanguage la)
	{
		return la == null ? null : la.getLocale();
	}


	public boolean isEnglish()
	{
		return getBaseLanguage().getLangCode().equals("en");
	}
	
	
	public boolean isLeftToRight()
	{
		return CLanguageCode.isLeftToRight(code);
	}
	

	public static boolean isLeftToRight(CLanguage la)
	{
		return la == null ? true : la.isLeftToRight();
	}
	
	
	public ComponentOrientation getComponentOrientation()
	{
		return isLeftToRight() ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT;
	}
}
