// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CLanguageCode;
import goryachev.common.util.PromptProvider;


public abstract class PseudoLocalization
	implements PromptProvider
{
	public abstract String pseudoLocalize(String s);
	
	//
	
	public PseudoLocalization()
	{
	}
	
	
	public void setLanguage(CLanguage language)
	{
	}


	public CLanguage[] getAvailableLanguages()
	{
		return null;
	}
	
	
	public static PseudoLocalization createPseudoLocalization(CLanguage la)
	{
		return createPseudoLocalization(la.getLanguageCode()); 
	}
	
	
	public static PseudoLocalization createPseudoLocalization(CLanguageCode code)
	{
		if(code != null)
		{
			// first try complete code
			PseudoLocalization p = create(code);
			if(p != null)
			{
				return p;
			}
			
			// then try language-only code
			String id = code.getCode();
			int ix = id.indexOf('_');
			if(ix > 0)
			{
				id = id.substring(0, ix);
				code = CLanguageCode.parse(id);
				if(code != null)
				{
					p = create(code);
					if(p != null)
					{
						return p;
					}
				}
			}
		}
		
		// if all fails
		return new UniversalPseudoLocalization();
	}
	
	
	private static PseudoLocalization create(CLanguageCode code)
	{
		switch(code)
		{
		case Arabic:
		case Persian:
		case Urdu:
			return new ArabicPseudoLocalization();
		case ChineseSimplified: 
			return new ChineseSimplifiedPseudoLocalization();
		case ChineseTraditional: 
			return new ChineseTraditionalPseudoLocalization();
		case Czech: 
			return new CzechPseudoLocalization();
		case English: 
			return new NullPseudoLocalization();
		case French: 
			return new FrenchPseudoLocalization();
		case Greek: 
			return new GreekPseudoLocalization();
		case Hebrew:
		case HebrewIW:
			return new HebrewPseudoLocalization();
		case Japanese: 
			return new JapanesePseudoLocalization();
		case Korean: 
			return new KoreanPseudoLocalization();
		case Polish: 
			return new PolishPseudoLocalization();
		case Russian: 
			return new RussianPseudoLocalization();
		}
		
		return null;
	}
}
