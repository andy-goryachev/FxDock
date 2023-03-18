// Copyright © 2008-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.html;
import goryachev.common.util.CMap;


// http://www.w3.org/TR/html4/sgml/entities.html
public class Html4SymbolEntities
{
	private CMap<Object,Object> entities = new CMap();
	
	
	public Html4SymbolEntities()
	{
		add("nbsp",      '\u00A0');
		add("iexcl",     '\u00A1');
		add("cent",      '\u00A2');
		add("pound",     '\u00A3');
		add("curren",    '\u00A4');
		add("yen",       '\u00A5');
		add("brvbar",    '\u00A6');
		add("sect",      '\u00A7');
		add("uml",       '\u00A8');
		add("copy",      '\u00A9');
		add("ordf",      '\u00AA');
		add("laquo",     '\u00AB');
		add("not",       '\u00AC');
		add("shy",       '\u00AD');
		add("reg",       '\u00AE');
		add("macr",      '\u00AF');
		add("deg",       '\u00B0');
		add("plusmn",    '\u00B1');
		add("sup2",      '\u00B2');
		add("sup3",      '\u00B3');
		add("acute",     '\u00B4');
		add("micro",     '\u00B5');
		add("para",      '\u00B6');
		add("middot",    '\u00B7');
		add("cedil",     '\u00B8');
		add("sup1",      '\u00B9');
		add("ordm",      '\u00BA');
		add("raquo",     '\u00BB');
		add("frac14",    '\u00BC');
		add("frac12",    '\u00BD');
		add("frac34",    '\u00BE');
		add("iquest",    '\u00BF');
		add("Agrave",    '\u00C0');
		add("Aacute",    '\u00C1');
		add("Acirc",     '\u00C2');
		add("Atilde",    '\u00C3');
		add("Auml",      '\u00C4');
		add("Aring ",    '\u00C5');
		add("AElig",     '\u00C6');
		add("Ccedil",    '\u00C7');
		add("Egrave",    '\u00C8');
		add("Eacute",    '\u00C9');
		add("Ecirc",     '\u00CA');
		add("Euml",      '\u00CB');
		add("Igrave",    '\u00CC');
		add("Iacute",    '\u00CD');
		add("Icirc",     '\u00CE');
		add("Iuml",      '\u00CF');
		add("ETH",       '\u00D0');
		add("Ntilde",    '\u00D1');
		add("Ograve",    '\u00D2');
		add("Oacute",    '\u00D3');
		add("Ocirc",     '\u00D4');
		add("Otilde",    '\u00D5');
		add("Ouml",      '\u00D6');
		add("times",     '\u00D7');
		add("Oslash",    '\u00D8');
		add("Ugrave",    '\u00D9');
		add("Uacute",    '\u00DA');
		add("Ucirc",     '\u00DB');
		add("Uuml",      '\u00DC');
		add("Yacute",    '\u00DD');
		add("THORN",     '\u00DE');
		add("szlig",     '\u00DF');
		add("agrave",    '\u00E0');
		add("aacute",    '\u00E1');
		add("acirc",     '\u00E2');
		add("atilde",    '\u00E3');
		add("auml",      '\u00E4');
		add("aring",     '\u00E5');
		add("aelig",     '\u00E6');
		add("ccedil",    '\u00E7');
		add("egrave",    '\u00E8');
		add("eacute",    '\u00E9');
		add("ecirc",     '\u00EA');
		add("euml",      '\u00EB');
		add("igrave",    '\u00EC');
		add("iacute",    '\u00ED');
		add("icirc",     '\u00EE');
		add("iuml",      '\u00EF');
		add("eth",       '\u00F0');
		add("ntilde",    '\u00F1');
		add("ograve",    '\u00F2');
		add("oacute",    '\u00F3');
		add("ocirc",     '\u00F4');
		add("otilde",    '\u00F5');
		add("ouml",      '\u00F6');
		add("divide",    '\u00F7');
		add("oslash",    '\u00F8');
		add("ugrave",    '\u00F9');
		add("uacute",    '\u00FA');
		add("ucirc",     '\u00FB');
		add("uuml",      '\u00FC');
		add("yacute",    '\u00FD');
		add("thorn",     '\u00FE');
		add("yuml",      '\u00FF');
		//
		add("fnof",      '\u0192');
		add("Alpha",     '\u0391');
		add("Beta",      '\u0392');
		add("Gamma",     '\u0393');
		add("Delta",     '\u0394');
		add("Epsilon",   '\u0395');
		add("Zeta",      '\u0396');
		add("Eta",       '\u0397');
		add("Theta",     '\u0398');
		add("Iota",      '\u0399');
		add("Kappa",     '\u039A');
		add("Lambda",    '\u039B');
		add("Mu",        '\u039C');
		add("Nu",        '\u039D');
		add("Xi",        '\u039E');
		add("Omicron",   '\u039F');
		add("Pi",        '\u03A0');
		add("Rho",       '\u03A1');
		add("Sigma ",    '\u03A3');
		add("Tau",       '\u03A4');
		add("Upsilon",   '\u03A5');
		add("Phi",       '\u03A6');
		add("Chi",       '\u03A7');
		add("Psi",       '\u03A8');
		add("Omega",     '\u03A9');
		add("alpha",     '\u03B1');
		add("beta",      '\u03B2');
		add("gamma",     '\u03B3');
		add("delta",     '\u03B4');
		add("epsilon",   '\u03B5');
		add("zeta",      '\u03B6');
		add("eta",       '\u03B7');
		add("theta",     '\u03B8');
		add("iota",      '\u03B9');
		add("kappa",     '\u03BA');
		add("lambda",    '\u03BB');
		add("mu",        '\u03BC');
		add("nu",        '\u03BD');
		add("xi",        '\u03BE');
		add("omicron",   '\u03BF');
		add("pi",        '\u03C0');
		add("rho",       '\u03C1');
		add("sigmaf",    '\u03C2');
		add("sigma",     '\u03C3');
		add("tau",       '\u03C4');
		add("upsilon",   '\u03C5');
		add("phi",       '\u03C6');
		add("chi",       '\u03C7');
		add("psi",       '\u03C8');
		add("omega",     '\u03C9');
		add("thetasym",  '\u03D1');
		add("upsih",     '\u03D2');
		add("piv",       '\u03D6');
		add("bull",      '\u2022');
		add("hellip",    '\u2026');
		add("prime",     '\u2032');
		add("Prime",     '\u2033');
		add("oline",     '\u203E');
		add("frasl",     '\u2044');
		add("weierp",    '\u2118');
		add("image",     '\u2111');
		add("real",      '\u211C');
		add("trade",     '\u2122');
		add("alefsym",   '\u2135');
		add("larr",      '\u2190');
		add("uarr",      '\u2191');
		add("rarr",      '\u2192');
		add("darr",      '\u2193');
		add("harr",      '\u2194');
		add("crarr",     '\u21B5');
		add("lArr",      '\u21D0');
		add("uArr",      '\u21D1');
		add("rArr",      '\u21D2');
		add("dArr",      '\u21D3');
		add("hArr",      '\u21D4');
		add("forall",    '\u2200');
		add("part",      '\u2202');
		add("exist",     '\u2203');
		add("empty",     '\u2205');
		add("nabla",     '\u2207');
		add("isin",      '\u2208');
		add("notin",     '\u2209');
		add("ni",        '\u220B');
		add("prod",      '\u220F');
		add("sum",       '\u2211');
		add("minus",     '\u2212');
		add("lowast",    '\u2217');
		add("radic",     '\u221A');
		add("prop",      '\u221D');
		add("infin",     '\u221E');
		add("ang",       '\u2220');
		add("and",       '\u2227');
		add("or",        '\u2228');
		add("cap",       '\u2229');
		add("cup",       '\u222A');
		add("int",       '\u222B');
		add("there4",    '\u2234');
		add("sim",       '\u223C');
		add("cong",      '\u2245');
		add("asymp",     '\u2248');
		add("ne",        '\u2260');
		add("equiv",     '\u2261');
		add("le",        '\u2264');
		add("ge",        '\u2265');
		add("sub",       '\u2282');
		add("sup",       '\u2283');
		add("nsub",      '\u2284');
		add("sube",      '\u2286');
		add("supe",      '\u2287');
		add("oplus",     '\u2295');
		add("otimes",    '\u2297');
		add("perp",      '\u22A5');
		add("sdot",      '\u22C5');
		add("lceil",     '\u2308');
		add("rceil",     '\u2309');
		add("lfloor",    '\u230A');
		add("rfloor",    '\u230B');
		add("lang",      '\u2329');
		add("rang",      '\u232A');
		add("loz",       '\u25CA');
		add("spades",    '\u2660');
		add("clubs",     '\u2663');
		add("hearts",    '\u2665');
		add("diams",     '\u2666');
		//
		add("quot",      '\"');
		add("amp",       '\u0026');
		add("lt",        '\u003C');
		add("gt",        '\u003E');
		add("OElig",     '\u0152');
		add("oelig",     '\u0153');
		add("Scaron",    '\u0160');
		add("scaron",    '\u0161');
		add("Yuml",      '\u0178');
		add("circ",      '\u02C6');
		add("tilde",     '\u02DC');
		add("ensp",      '\u2002');
		add("emsp",      '\u2003');
		add("thinsp",    '\u2009');
		add("zwnj",      '\u200C');
		add("zwj",       '\u200D');
		add("lrm",       '\u200E');
		add("rlm",       '\u200F');
		add("ndash",     '\u2013');
		add("mdash",     '\u2014');
		add("lsquo",     '\u2018');
		add("rsquo",     '\u2019');
		add("sbquo",     '\u201A');
		add("ldquo",     '\u201C');
		add("rdquo",     '\u201D');
		add("bdquo",     '\u201E');
		add("dagger",    '\u2020');
		add("Dagger",    '\u2021');
		add("permil",    '\u2030');
		add("lsaquo",    '\u2039');
		add("rsaquo",    '\u203A');
		add("euro",      '\u20AC');
	}
	
	
	protected void add(String element, char c)
	{
		entities.put(element, c);
		entities.put(c, element);
	}
	
	
	/** returns corresponding HTML4 entity or null */
	public synchronized String lookupElement(char c)
	{
		return (String)entities.get(c);
	}
	
	
	/** returns corresponding character or null */
	public synchronized Character lookupChar(String element)
	{
		return (Character)entities.get(element);
	}
}
