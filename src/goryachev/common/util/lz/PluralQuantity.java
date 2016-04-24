// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CLanguage;
import goryachev.common.util.TXT;


/**
 * Plural quantity identifiers.
 * http://cldr.unicode.org/index/cldr-spec/plural-rules
 */
public enum PluralQuantity
{
	ZERO,
	ONE,
	TWO,
	FEW,
	MANY,
	OTHER;
	// TODO UNKNOWN
	
	
	//
	
	
	public static String getHumanName(PluralQuantity q)
	{
		if(q != null)
		{
			switch(q)
			{
			case ZERO:       return TXT.get("PluralQuantity.quantity.zero", "Zero");
			case ONE:        return TXT.get("PluralQuantity.quantity.one", "One");
			case TWO:        return TXT.get("PluralQuantity.quantity.two", "Two");
			case FEW:        return TXT.get("PluralQuantity.quantity.few", "Few");
			case MANY:       return TXT.get("PluralQuantity.quantity.many", "Many");
			case OTHER:      return TXT.get("PluralQuantity.quantity.other", "Other");
			}
		}
		return null;
	}
	
	
	public static String getQuantity(CLanguage la, PluralQuantity q)
	{
		String s;
		int n = PluralRules.getExampleNumberFor(la, q);
		if(n < 0)
		{
			s = getHumanName(q);
		}
		else
		{
			s = String.valueOf(n);
		}
		
		return TXT.get("PluralQuantity.quantity IDENTIFIER", "<Quantity~{0}>", s);
	}
}
