// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CKit;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CLanguageCode;


/**
 * This facility implements language-specific rules for plural (and singular) forms.
 *  
 * http://translate.sourceforge.net/wiki/l10n/pluralforms
 * http://unicode.org/repos/cldr-tmp/trunk/diff/supplemental/language_plural_rules.html
 * http://docs.translatehouse.org/projects/localization-guide/en/latest/l10n/pluralforms.html
 * http://mxr.mozilla.org/mozilla/source/intl/locale/src/PluralForm.jsm
 * 
 * TODO
	the handling of plurals is unknown for these languages:
	Abkhazian Afar Armenian Assamese Avaric Avestan
	Bashkir Chamorro Chechen Corsican Cree
	Fijian
	Greenlandic Guarani 
	Haitian Herero HiriMotu
	Ido Interlingue Inupiaq
	Kanuri Kashmiri Kikuyu Kinyarwanda Komi Kongo Kwanyama
	Latin Limburgan LubaKatanga
	Marshallese 
	Nauru Navajo Ndonga
	Ojibwa OldSlavonic Oromo Ossetian 
	Pali Pashtu
	Quechua
	Rundi
	Samoan Sanskrit Sardinian
	Tahitian Twi
	Volapuck
	Welsh
	Yiddish
	Zhuang
 */
public abstract class PluralRules
{
	protected abstract PluralQuantity getQuantity(int n);
	
	protected abstract int getExample(PluralQuantity q);
	
	protected abstract boolean requiresVariable(PluralQuantity q);
	
	//
	
	private static CLanguage last;
	private static PluralRules lastRules;
	protected final PluralQuantity[] forms;
	
	public static final PluralRules ZERO_ONE = new PluralRules(PluralQuantity.ONE, PluralQuantity.OTHER)
	{
		protected PluralQuantity getQuantity(int n) { return (n>1 ? PluralQuantity.OTHER : PluralQuantity.ONE); }
		protected int getExample(PluralQuantity q) { return (q==PluralQuantity.ONE ? 1 : 9); }
		protected boolean requiresVariable(PluralQuantity q) { return true; }
	};
	
	public static final PluralRules ONE = new PluralRules(PluralQuantity.ONE, PluralQuantity.OTHER)
	{
		protected PluralQuantity getQuantity(int n) { return (n==1 ? PluralQuantity.ONE : PluralQuantity.OTHER); }
		protected int getExample(PluralQuantity q) { return (q==PluralQuantity.ONE ? 1 : 9); }
		protected boolean requiresVariable(PluralQuantity q) { return q != PluralQuantity.ONE; }
	};
	
	public static final PluralRules ONE_TWO = new PluralRules(PluralQuantity.ONE, PluralQuantity.TWO, PluralQuantity.OTHER)
	{
		protected PluralQuantity getQuantity(int n) { return (n==1 ? PluralQuantity.ONE : n==2 ? PluralQuantity.TWO : PluralQuantity.OTHER); }
		protected int getExample(PluralQuantity q) { return (q==PluralQuantity.ONE ? 1 : q==PluralQuantity.TWO ? 2 : 9); }
		protected boolean requiresVariable(PluralQuantity q) 
		{
			switch(q)
			{
			case ONE:
			case TWO:
				return false;
			}
			return true;
		}
	};
	
	public static final PluralRules SINGULAR = new PluralRules(PluralQuantity.OTHER)
	{
		protected PluralQuantity getQuantity(int n) { return PluralQuantity.OTHER; }
		protected int getExample(PluralQuantity q) { return 1; }
		protected boolean requiresVariable(PluralQuantity q) { return true; }
	};
	
	public static final PluralRules UNKNOWN = new PluralRules(PluralQuantity.OTHER)
	{
		protected PluralQuantity getQuantity(int n) { return PluralQuantity.OTHER; }
		protected int getExample(PluralQuantity q) { return -1; }
		protected boolean requiresVariable(PluralQuantity q) { return true; }
	};
	
	
	//
	
	
	public PluralRules(PluralQuantity ... forms)
	{
		this.forms = forms;
	}
	
	
	public static PluralQuantity getPluralQuantity(CLanguage la, int n)
	{
		return getRules(la).getQuantity(Math.abs(n));
	}
	

	/**
	 * Returns a number characteristic of the specified plural quantity.
	 */
	public static int getExampleNumberFor(CLanguage la, PluralQuantity f)
	{
		return getRules(la).getExample(f);
	}
	

	/**
	 * Returns an index into an array of plural forms for a given quantity.  
	 * The value returned is from 0 to getPluralFormCount().
	 */
	public static int getQuantityIndex(CLanguage la, PluralQuantity q)
	{
		return CKit.indexOf(getRules(la).forms, q);
	}
	
	
	/**
	 * Returns an index into an array of plural forms for a given number. 
	 * The value returned is from 0 to getPluralFormCount().
	 */
	public static int getQuantityIndex(CLanguage la, int number)
	{
		PluralRules r = getRules(la);
		PluralQuantity q = r.getQuantity(Math.abs(number));
		return CKit.indexOf(r.forms, q);
	}
	
	
	/** Returns the number of plural forms for a given language */
	public static int getPluralFormCount(CLanguage la)
	{
		return getRules(la).forms.length;
	}
	
	
	/** Returns the number of plural forms for a given language */
	public static PluralQuantity[] getPluralForms(CLanguage la)
	{
		return getRules(la).forms;
	}
	
	
	public static boolean requiresVariable(CLanguage la, PluralQuantity q)
	{
		return getRules(la).requiresVariable(q);
	}
	
	
	protected synchronized static PluralRules getRules(CLanguage la)
	{
		if(la == null)
		{
			return UNKNOWN;
		}
		
		// very simple caching
		if(la == last)
		{
			return lastRules;
		}
		
		lastRules = lookup(la);
		return lastRules;
	}
		
	
	protected static PluralRules lookup(CLanguage la)
	{
		CLanguageCode code = la.getLanguageCode();
		if(code != null)
		{
			switch(code)
			{
			case Abkhazian: 
				return UNKNOWN; // FIX
				
			case Afar:
				return UNKNOWN; // FIX
				
			case Afrikaans:
				return ONE;
				
			case Akan:
				return ZERO_ONE;
				
			case Albanian:
				return ONE;
				
			case Amharic:
				return ZERO_ONE;
				
			case Arabic:
				return new PluralRules(PluralQuantity.ZERO, PluralQuantity.ONE, PluralQuantity.TWO, PluralQuantity.FEW, PluralQuantity.MANY, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==0 ? PluralQuantity.ZERO : n==1 ? PluralQuantity.ONE : n==2 ? PluralQuantity.TWO : (n%100>=3 && n%100<=10) ? PluralQuantity.FEW : n%100>=11 ? PluralQuantity.MANY : PluralQuantity.OTHER);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ZERO: return 0;
						case ONE:  return 1;
						case TWO:  return 2;
						case FEW:  return 3;
						case MANY: return 99;
						default:   return 100; 
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						switch(q)
						{
						case ZERO:
						case ONE:
						case TWO:
							return false;
						}
						return true;
					}
				};
				
			case Aragonese:
				return ONE;
				
			case Armenian:
				return UNKNOWN; // FIX
				
			case Assamese:
				return UNKNOWN; // FIX
				
			case Asturian:
				return ONE;
				
			case Avaric:
				return UNKNOWN; // FIX
				
			case Avestan:
				return UNKNOWN; // FIX
				
			case Aymara:
				return SINGULAR;
				
			case Azerbaijani:
				return ONE;
				
			case Bambara:
				return SINGULAR;
				
			case Bashkir:
				return UNKNOWN; // FIX
				
			case Basque:
				return ONE;
				
			case Belarusian:
				// same as Russian
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return ((n%10==1 && n%100!=11) ? PluralQuantity.ONE : (n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20)) ? PluralQuantity.FEW : PluralQuantity.MANY);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Bengali:
				return ONE;
				
			case Bihari:
				return ZERO_ONE;
				
			case Bislama:
				return UNKNOWN;
				
			case Bosnian:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n%10==1 && n%100!=11 ? PluralQuantity.ONE : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? PluralQuantity.FEW : PluralQuantity.MANY);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Breton:
				return ZERO_ONE;
				
			case Bulgarian:
				return ONE;
				
			case Burmese:
				return SINGULAR;
				
			case Catalan:
				return ONE;
				
			case Chamorro:
				return UNKNOWN; // FIX
				
			case Chechen:
				return UNKNOWN; // FIX
				
			case Chinese:
				return SINGULAR;
				
			case ChineseHK:
				return SINGULAR;
				
			case ChineseSimplified:
				return SINGULAR;
				
			case ChineseTraditional:
				return SINGULAR;
				
			case Chuvash:
				return UNKNOWN;
				
			case Cornish:
				return ONE_TWO;
				
			case Corsican:
				return UNKNOWN; // FIX
				
			case Cree:
				return UNKNOWN; // FIX
				
			case Croatian:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						// same as bosnian apparently
						return (n%10==1 && n%100!=11 ? PluralQuantity.ONE : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? PluralQuantity.FEW : PluralQuantity.MANY);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};

			case Czech:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1) ? PluralQuantity.ONE : (n>=2 && n<=4) ? PluralQuantity.FEW : PluralQuantity.OTHER;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return q != PluralQuantity.ONE;
					}
				};
				
			case Danish:
				return ONE;
				
			case Divehi:
				return ONE;
				
			case Dutch:
				return ONE;
				
			case Dzongkha:
				return SINGULAR;
				
			case English:
			case EnglishUK:
			case EnglishUS:
				return ONE;
				
			case Esperanto:
				return ONE;
				
			case Estonian:
				return ONE;
				
			case Ewe:
				return ONE;
				
			case Faroese:
				return ONE;
				
			case Fijian:
				return UNKNOWN; // FIX
				
			case Finnish:
				return ONE;
				
			case French:
			case FrenchCA:
				return ZERO_ONE;
				
			case Frisian:
				return ONE;
				
			case Fulah:
				return ZERO_ONE;
				
			case Gaelic:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.TWO, PluralQuantity.FEW, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1 || n==11) ? PluralQuantity.ONE : (n==2 || n==12) ? PluralQuantity.TWO : (n > 2 && n < 20) ? PluralQuantity.FEW : PluralQuantity.OTHER;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case TWO: return 2;
						case FEW: return 3;
						default: return 20;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Galician:
				return ONE;
				
			case Ganda:
				return ONE;
				
			case Georgian:
				return SINGULAR;
				
			case German:
				return ONE;
				
			case Greek:
				return ONE;
					
			case Greenlandic:
				return UNKNOWN; // FIX
				
			case Guarani:
				return UNKNOWN; // FIX
				
			case Gujarati:
				return ONE;
				
			case Haitian:
				return UNKNOWN; // FIX
				
			case Hausa:
				return ONE;
				
			case Hebrew:
			case HebrewIW:
				return ONE;
				
			case Herero:
				return UNKNOWN; // FIX
				
			case Hindi:
				return ONE;
				
			case HiriMotu:
				return UNKNOWN; // FIX
				
			case Hungarian:
				// unicode says singular.
				return ONE;
				
			case Icelandic:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n%10!=1 || n%100==11) ? PluralQuantity.OTHER : PluralQuantity.ONE;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						default: return 100;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Ido:
				return UNKNOWN; // FIX
				
			case Igbo:
				return SINGULAR;
				
			case Indonesian:
				return SINGULAR;
				
			case Interlingua:
				return ONE;
				
			case Interlingue:
				return UNKNOWN; // FIX
				
			case Inuktitut:
				return ONE_TWO;
				
			case Inupiaq:
				return UNKNOWN; // FIX
				
			case Irish:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.TWO, PluralQuantity.FEW, PluralQuantity.MANY, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return n==1 ? PluralQuantity.ONE : n==2 ? PluralQuantity.TWO : n<7 ? PluralQuantity.FEW : n<11 ? PluralQuantity.MANY : PluralQuantity.OTHER;
					}

					protected int getExample(PluralQuantity q)
					{
						switch(q)
						{
						case ONE:  return 1;
						case TWO:  return 2;
						case FEW:  return 5;
						case MANY: return 10;
						default:   return 99; 
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						switch(q)
						{
						case ONE:
						case TWO:
							return false;
						}
						return true;
					}
				};
				
			case Italian:
				return ONE;
				
			case Japanese:
				return SINGULAR;
				
			case Javanese:
				return SINGULAR;
				
			case Kannada:
				// TODO unicode says singular
				return ONE;
				
			case Kanuri:
				return UNKNOWN; // FIX
				
			case Kashmiri:
				return UNKNOWN; // FIX
				
			case Kazakh:
				// TODO unicode says one, looks like it applies to personal pronouns
				return SINGULAR;
				
			case Khmer:
				return SINGULAR;
				
			case Kikuyu:
				return UNKNOWN; // FIX
				
			case Kinyarwanda:
				return UNKNOWN; // FIX
				
			case Komi:
				return UNKNOWN; // FIX
				
			case Kongo:
				return UNKNOWN; // FIX
				
			case Korean:
				return SINGULAR;
				
			case Kurdish:
				return ONE;
				
			case Kwanyama:
				return UNKNOWN; // FIX
				
			case Kyrgyz:
				return SINGULAR;
				
			case Lao:
				return SINGULAR;
				
			case Latin:
				return UNKNOWN; // FIX
				
			case Latvian:
				return new PluralRules(PluralQuantity.ZERO, PluralQuantity.ONE, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n%10==1 && n%100!=11 ? PluralQuantity.ONE : n != 0 ? PluralQuantity.OTHER : PluralQuantity.ZERO);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ZERO: return 0;
						case ONE:  return 1;
						default:   return 99; 
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Limburgan:
				return UNKNOWN; // FIX
				
			case Lingala:
				return ZERO_ONE;
				
			case Lithuanian:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n%10==1 && n%100!=11 ? PluralQuantity.ONE : (n%10>=2 && (n%100<10 || n%100>=20)) ? PluralQuantity.FEW : PluralQuantity.OTHER);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE:  return 1;
						case FEW:  return 2;
						default:   return 10;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case LubaKatanga:
				return UNKNOWN; // FIX
				
			case Luxembourgish:
				return ONE;
				
			case Macedonian:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1 || n%10==1) ? PluralQuantity.ONE : PluralQuantity.OTHER;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE:  return 1;
						default:   return 2;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Malagasy:
				return ZERO_ONE;
				
			case Malay:
				return SINGULAR;
				
			case Malayalam:
				return ONE;
				
			case Maltese:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1 ? PluralQuantity.ONE : n==0 || ( n%100>1 && n%100<11) ? PluralQuantity.FEW : (n%100>10 && n%100<20 ) ? PluralQuantity.MANY : PluralQuantity.OTHER);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE:  return 1;
						case FEW:  return 2;
						case MANY: return 11; 
						default:   return 20;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return q != PluralQuantity.ONE;
					}
				};
				
			case Manx:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return ((n%10==1) || (n%10==2) || (n%20==0)) ? PluralQuantity.ONE : PluralQuantity.OTHER;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE:  return 1;
						default:   return 3;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Maori:
				return ZERO_ONE;
				
			case Marathi:
				return ONE;
				
			case Marshallese:
				return UNKNOWN; // FIX
				
			case Mongolian:
				return ONE;
				
			case Nauru:
				return UNKNOWN; // FIX
				
			case Navajo:
				return UNKNOWN; // FIX
				
			case Ndonga:
				return UNKNOWN; // FIX
				
			case Nepali:
				return ONE;
				
			case NorthernSami:
				return ONE_TWO;
				
			case NorthNdebele:
				return ONE;
				
			case Norwegian:
			case NorwegianNB:
			case NorwegianNN:				
				return ONE;
				
			case Nyanja:
				return ONE;
				
			case Ojibwa:
				return UNKNOWN; // FIX
				
			case OldSlavonic:
				return UNKNOWN; // FIX
				
			case Oriya:
				return ONE;
				
			case Oromo:
				return UNKNOWN; // FIX
				
			case Ossetian:
				return UNKNOWN; // FIX
				
			case Pali:
				return UNKNOWN; // FIX
				
			case Panjabi:
				return ONE;
				
			case Pashto:
				return ONE;
				
			case Pashtu:
				return UNKNOWN; // FIX
				
			case Persian:
				return SINGULAR;
				
			case Polish:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1 ? PluralQuantity.ONE : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? PluralQuantity.FEW : PluralQuantity.MANY);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return q != PluralQuantity.ONE;
					}
				};
				
			case Portuguese:
			case PortugueseBR:
				return ONE;
				
			case Provencal:
				// occitan?
				return ZERO_ONE;
				
			case Quechua:
				return UNKNOWN; // FIX
				
			case Romanian:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1 ? PluralQuantity.ONE : (n==0 || (n%100 > 0 && n%100 < 20)) ? PluralQuantity.FEW : PluralQuantity.OTHER);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 20;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return q != PluralQuantity.ONE;
					}
				};
				
			case Romansh:
				return ONE;
				
			case Rundi:
				return UNKNOWN; // FIX
				
			case Russian:
				// same as belarusian
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return ((n%10==1 && n%100!=11) ? PluralQuantity.ONE : (n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20)) ? PluralQuantity.FEW : PluralQuantity.MANY);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Samoan:
				return UNKNOWN; // FIX
				
			case Sango:
				return SINGULAR;
				
			case Sanskrit:
				return UNKNOWN; // FIX
				
			case Sardinian:
				return UNKNOWN; // FIX
				
			case Serbian:
			case SerboCroatian:
			case SerboCroatianHR:
			case SerboCroatianSR:
			case SerboCroatianYU:				
				// same as russian, belarusian
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return ((n%10==1 && n%100!=11) ? PluralQuantity.ONE : (n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20)) ? PluralQuantity.FEW : PluralQuantity.MANY);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Shona:
				return ONE;
				
			case SichuanYi:
				return SINGULAR;
				
			case Sindhi:
				return ONE;
				
			case Sinhala:
				return ONE;
				
			case Slovak:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1) ? PluralQuantity.ONE : (n>=2 && n<=4) ? PluralQuantity.FEW : PluralQuantity.OTHER;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return q != PluralQuantity.ONE;
					}
				};
				
			case Slovenian:
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.TWO, PluralQuantity.FEW, PluralQuantity.OTHER)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n%100==1 ? PluralQuantity.ONE : n%100==2 ? PluralQuantity.TWO : n%100==3 || n%100==4 ? PluralQuantity.FEW : PluralQuantity.OTHER);
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case TWO: return 2;
						case FEW: return 3;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Somali:
				return ONE;
				
			case SouthernSotho:
				return ONE;
				
			case SouthNdebele:
				return ONE;
				
			case Spanish:
			case SpanishAR:
			case SpanishBO:
			case SpanishCL:
			case SpanishCO:
			case SpanishCR:
			case SpanishCU:
			case SpanishDO:
			case SpanishEC:
			case SpanishES:
			case SpanishGT:
			case SpanishHN:
			case SpanishMX:
			case SpanishNI:
			case SpanishPA:
			case SpanishPE:
			case SpanishPR:
			case SpanishPY:
			case SpanishSV:
			case SpanishUS:
			case SpanishUY:
			case SpanishVE:
				return ONE;
				
			case Sundanese:
				return SINGULAR;
				
			case Swahili:
				return ONE;
				
			case Swati:
				return ONE;
				
			case Swedish:
				return ONE;
				
			case Tagalog:
				return ZERO_ONE;
				
			case Tahitian:
				return UNKNOWN; // FIX
				
			case Tajik:
				return ZERO_ONE;
				
			case Tamil:
				return ONE;
				
			case Tatar:
				return SINGULAR;
				
			case Telugu:
				return ONE;
				
			case Thai:
				return SINGULAR;
				
			case Tibetan:
				return SINGULAR;
				
			case Tigrinya:
				return ZERO_ONE;
				
			case Tonga:
				return SINGULAR;
				
			case Tsonga:
				return ONE;
				
			case Tswana:
				return ONE;
				
			case Turkish:
				// TODO unicode is wrong
				return ZERO_ONE;
				
			case Turkmen:
				return ONE;
				
			case Twi:
				return UNKNOWN; // FIX
				
			case Ukrainian:
				// same as russian, belarusian, serbian
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n%10==1 && n%100!=11) ? PluralQuantity.ONE : (n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20)) ? PluralQuantity.FEW : PluralQuantity.MANY;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ONE: return 1;
						case FEW: return 2;
						default: return 5;
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						return true;
					}
				};
				
			case Urdu:
				return ONE;
				
			case Uyghur:
				return SINGULAR;
				
			case Uzbek:
				return ZERO_ONE;
				
			case Venda:
				return ONE;
				
			case Vietnamese:
				return SINGULAR;
				
			case Volapuck:
				return UNKNOWN; // FIX
				
			case Walloon:
				return ZERO_ONE;
				
			case Welsh:
				// TODO unicode has ZERO+OTHER and specifies a different logic
				return new PluralRules(PluralQuantity.ONE, PluralQuantity.TWO, PluralQuantity.FEW, PluralQuantity.MANY)
				{
					protected PluralQuantity getQuantity(int n)
					{
						return (n==1) ? PluralQuantity.ONE : (n==2) ? PluralQuantity.TWO : (n != 8 && n != 11) ? PluralQuantity.FEW : PluralQuantity.MANY;
					}

					protected int getExample(PluralQuantity f)
					{
						switch(f)
						{
						case ZERO: return 0;
						case ONE:  return 1;
						case TWO:  return 2;
						case FEW:  return 5;
						case MANY: return 10;
						default:   return 99; 
						}
					}
					
					protected boolean requiresVariable(PluralQuantity q) 
					{
						switch(q)
						{
						// case ZERO: ?
						case ONE:
						case TWO:
							return false;
						}
						return true;
					}
				};
				
			case WesternFrisian:
				return ONE;
				
			case Xhosa:
				return ONE;
				
			case Yiddish:
				return UNKNOWN; // FIX
				
			case Yoruba:
				return ONE;
				
			case Zhuang:
				return UNKNOWN; // FIX
				
			case Zulu:
				return ONE;
			}
		}			
		return UNKNOWN;
	}
}
