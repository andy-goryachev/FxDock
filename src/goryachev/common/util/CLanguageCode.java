// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;



// ISO639 codes http://www.loc.gov/standards/iso639-2/ISO-639-2_utf-8.txt
public enum CLanguageCode
{
	// A
	Afar("aa", "Afar"),
	Abkhazian("ab", "Abkhazian"),
	Afrikaans("af", "Afrikaans"),
	Akan("ak", "Akan"),
	Albanian("sq", "Albanian"),
	Amharic("am", "Amharic"),
	Arabic("ar", "Arabic"),
	Aragonese("an", "Aragonese"),
	Armenian("hy", "Armenian"),
	Assamese("as", "Assamese"),
	Asturian("ast", "Asturian"),
	Avaric("av", "Avaric"),
	Avestan("ae", "Avestan"),
	Aymara("ay", "Aymara"),
	Azerbaijani("az", "Azerbaijani"),
	// B
	Bambara("bm", "Bambara"),
	Bashkir("ba", "Bashkir"),
	Basque("eu", "Basque"),
	Belarusian("be", "Belarusian"),
	Bengali("bn", "Bengali"),
	Bihari("bh", "Bihari"),
	Bislama("bi", "Bislama"),
	Bosnian("bs", "Bosnian"),
	Breton("br", "Breton"),
	Bulgarian("bg", "Bulgarian"),
	Burmese("my", "Burmese"),
	// C
	Catalan("ca", "Catalan"),
	Chamorro("ch", "Chamorro"),
	Chechen("ce", "Chechen"),
	Chinese("zh", "Chinese"),
	ChineseHK("zh_HK", "Chinese (HK)"),
	ChineseSimplified("zh_CN", "Chinese (Simplified)"),
	ChineseTraditional("zh_TW", "Chinese (Traditional)"),
	Chuvash("cv", "Chuvash"),
	Cornish("kw", "Cornish"),
	Corsican("co", "Corsican"),
	Cree("cr", "Cree"),
	Croatian("hr", "Croatian"),
	Czech("cs", "Czech"),
	// D
	Danish("da", "Danish"),
	Divehi("dv", "Divehi"),
	Dutch("nl", "Dutch"),
	Dzongkha("dz", "Dzongkha"),
	// E
	English("en", "English"),
	EnglishUK("en_UK", "English (UK)"),
	EnglishUS("en_US", "English (US)"),
	Esperanto("eo", "Esperanto"),
	Estonian("et", "Estonian"),
	Ewe("ee", "Ewe"),
	// F
	Faroese("fo", "Faroese"),
	Fijian("fj", "Fijian"),
	Finnish("fi", "Finnish"),
	French("fr", "French"),
	FrenchCA("fr_CA", "FrenchCA"),
	Frisian("fy_NL", "Frisian"),
	Fulah("ff", "Fulah"),
	// G
	Gaelic("gd", "Gaelic"),
	Galician("gl", "Galician"),
	Ganda("lg", "Ganda"),
	Georgian("ka", "Georgian"),
	German("de", "German"),
	Greek("el", "Greek"), 
	Greenlandic("kl", "Greenlandic"),
	Guarani("gn", "Guarani"),
	Gujarati("gu", "Gujarati"),
	// H
	Haitian("ht", "Haitian"), 
	Hausa("ha", "Hausa"),
	Hebrew("he", "Hebrew"),
	HebrewIW("iw", "Hebrew (IW)"), // obsolete BUT this is returned when setting user.language to "he"
	Herero("hz", "Herero"),
	Hindi("hi", "Hindi"),
	HiriMotu("ho", "HiriMotu"),
	Hungarian("hu", "Hungarian"),
	// I
	Icelandic("is", "Icelandic"),
	Ido("io", "Ido"),
	Igbo("ig", "Igbo"),
	Indonesian("id", "Indonesian"),
	Interlingua("ia", "Interlingua"),
	Interlingue("ie", "Interlingue"),
	Inuktitut("iu", "Inuktitut"),
	Inupiaq("ik", "Inupiaq"),
	Irish("ga", "Irish"),
	Italian("it", "Italian"),
	// J
	Japanese("ja", "Japanese"),
	Javanese("jv", "Javanese"),
	// K
	Kannada("kn", "Kannada"),
	Kanuri("kr", "Kanuri"),
	Kashmiri("ks", "Kashmiri"),
	Kazakh("kk", "Kazakh"),
	Khmer("km", "Khmer"),
	Kikuyu("ki", "Kikuyu"),
	Kinyarwanda("rw", "Kinyarwanda"),
	Komi("kv", "Komi"),
	Kongo("kg", "Kongo"),
	Korean("ko", "Korean"),
	Kurdish("ku", "Kurdish"),
	Kwanyama("kj", "Kwanyama"),
	Kyrgyz("ky", "Kyrgyz"),
	// L
	Lao("lo", "Lao"),
	Latin("la", "Latin"),
	Latvian("lv", "Latvian"),
	Limburgan("li", "Limburgan"),
	Lingala("ln", "Lingala"),
	Lithuanian("lt", "Lithuanian"),
	LubaKatanga("lu", "LubaKatanga"),
	Luxembourgish("lb", "Luxembourgish"),
	// M
	Macedonian("mk", "Macedonian"),
	Malagasy("mg", "Malagasy"),
	Malay("ms", "Malay"),
	Malayalam("ml", "Malayalam"),
	Maltese("mt", "Maltese"),
	Manx("gv", "Manx"),
	Maori("mi", "Maori"),
	Marathi("mr", "Marathi"),
	//Marathi("ms"), ?
	Marshallese("mh", "Marshallese"),
	Mongolian("mn", "Mongolian"),
	// N
	Nauru("na", "Nauru"),
	Navajo("nv", "Navajo"),
	Ndonga("ng", "Ndonga"), 
	Nepali("ne", "Nepali"),
	NorthernSami("se", "NorthernSami"),
	NorthNdebele("nd", "NorthNdebele"),
	NorwegianNB("nb", "Norwegian (NB)"),
	NorwegianNN("nn", "Norwegian (NN)"),
	Norwegian("no", "Norwegian"),
	Nyanja("ny", "Nyanja"),
	// O
	Ojibwa("oj", "Ojibwa"),
	OldSlavonic("cu", "Old Slavonic"),
	Oriya("or", "Oriya"),
	Oromo("om", "Oromo"),
	Ossetian("os", "Ossetian"),
	// P
	Pali("pi", "Pali"),
	Panjabi("pa", "Panjabi"),
	Pashto("ps", "Pashto"),
	@Deprecated
	Pashtu("pu", "Pashtu"), // obsolete?
	Persian("fa", "Persian"),
	Polish("pl", "Polish"),
	Portuguese("pt", "Portuguese"),
	PortugueseBR("pt_BR", "Portuguese (BR)"),
	Provencal("oc", "Provencal"),
	// Q
	Quechua("qu", "Quechua"),
	// R
	Romanian("ro", "Romanian"),
	Romansh("rm", "Romansh"),
	Rundi("rn", "Rundi"),
	Russian("ru", "Russian"),
	// S
	Samoan("sm", "Samoan"),
	Sango("sg", "Sango"),
	Sanskrit("sa", "Sanskrit"),
	Sardinian("sc", "Sardinian"),
	Serbian("sr", "Serbian"),
	SerboCroatian("sh", "Serbo-Croatian"),
	SerboCroatianHR("sh_HR", "Serbo-Croatian (HR)"),
	SerboCroatianSR("sh_SR", "Serbo-Croatian (SR)"),
	SerboCroatianYU("sh_YU", "Serbo-Croatian (YU)"),
	Shona("sn", "Shona"),
	SichuanYi("ii", "Sichuan Yi"),
	Sindhi("sd", "Sindhi"),
	Sinhala("si", "Sinhala"),
	Slovak("sk", "Slovak"),
	Slovenian("sl", "Slovenian"),
	Somali("so", "Somali"),
	SouthNdebele("nr", "South Ndebele"),
	SouthernSotho("st", "Southern Sotho"),
	Spanish("es", "Spanish"),
	SpanishAR("es_AR", "Spanish (AR)"),
	SpanishBO("es_BO", "Spanish (BO)"),
	SpanishCL("es_CL", "Spanish (CL)"),
	SpanishCO("es_CO", "Spanish (CO)"),
	SpanishCR("es_CR", "Spanish (CR)"),
	SpanishCU("es_CU", "Spanish (CU)"),
	SpanishDO("es_DO", "Spanish (DO)"),
	SpanishEC("es_EC", "Spanish (EC)"),
	SpanishES("es_ES", "Spanish (ES)"),
	SpanishGT("es_GT", "Spanish (GT)"),
	SpanishHN("es_HN", "Spanish (HN)"),
	SpanishMX("es_MX", "Spanish (MX)"),
	SpanishNI("es_NI", "Spanish (NI)"),
	SpanishPA("es_PA", "Spanish (PA)"),
	SpanishPE("es_PE", "Spanish (PE)"),
	SpanishPR("es_PR", "Spanish (PR)"),
	SpanishPY("es_PY", "Spanish (PY)"),
	SpanishSV("es_SV", "Spanish (SV)"),
	SpanishUS("es_US", "Spanish (US)"),
	SpanishUY("es_UY", "Spanish (UY)"),
	SpanishVE("es_VE", "Spanish (VE)"),
	Sundanese("su", "Sundanese"),
	Swahili("sw", "Swahili"),
	Swati("ss", "Swati"),
	Swedish("sv", "Swedish"),
	// T
	Tagalog("tl", "Tagalog"),
	Tahitian("ty", "Tahitian"),
	Tajik("tg", "Tajik"),
	Tamil("ta", "Tamil"),
	Tatar("tt", "Tatar"),
	Telugu("te", "Telugu"),
	Thai("th", "Thai"), 
	Tibetan("bo", "Tibetan"),
	Tigrinya("ti", "Tigrinya"),
	Tonga("to", "Tonga"),
	Tsonga("ts", "Tsonga"),
	Tswana("tn", "Tswana"),
	Turkish("tr", "Turkish"),
	Turkmen("tk", "Turkmen"),
	Twi("tw", "Twi"),
	// U
	Ukrainian("uk", "Ukrainian"),
	Urdu("ur", "Urdu"),
	Uyghur("ug", "Uyghur"),
	Uzbek("uz", "Uzbek"),
	// V
	Venda("ve", "Venda"),
	Vietnamese("vi", "Vietnamese"),
	Volapuck("vo", "Volapuck"),
	// W
	Walloon("wa", "Walloon"),
	Welsh("cy", "Welsh"), 
	WesternFrisian("fy", "Western Frisian"),
	// X
	Xhosa("xh", "Xhosa"),
	// Y
	Yiddish("yi", "Yiddish"),
	Yoruba("yo", "Yoruba"),
	// Z
	Zhuang("za", "Zhuang"),
	Zulu("zu", "Zulu");
	
	//
	
	public final String code;
	public final String english;


	private CLanguageCode(String code, String english)
	{
		this.code = code;
		this.english = english;
	}


	public String getCode()
	{
		return code;
	}
	
	
	public String getEnglishName()
	{
		return english;
	}


	public static CLanguageCode parse(Object x)
	{
		if(x instanceof CLanguageCode)
		{
			return (CLanguageCode)x;
		}
		else if(x instanceof CLanguage)
		{
			return ((CLanguage)x).getLanguageCode();
		}
		else if(x instanceof String)
		{
			String code = (String)x;
			for(CLanguageCode c: values())
			{
				if(c.code.equalsIgnoreCase(code))
				{
					return c;
				}
			}
		}
		
		return null;
	}
	
	
	public String getLocalName()
	{
		switch(this)
		{
		// A
		case Afar: return "Afar";
		case Abkhazian: return "Аҧсуа";
		case Afrikaans: return "Afrikaans";
		case Akan: return "Akana";
		case Albanian: return "Shqip";
		case Amharic: return "አማርኛ";
		case Arabic: return "العربية";
		case Aragonese: return "Aragonés";
		case Armenian: return "Հայերեն";
		case Assamese: return "অসমীয়া"; // it's there
		case Asturian: return "Asturianu";
		case Avaric: return "Авар";
		case Avestan: return "Avesta";
		case Aymara: return "Aymar";
		case Azerbaijani: return "Azərbaycan";
		// B
		case Bambara: return "Bamanankan";
		case Bashkir: return "Башҡорт";
		case Basque: return "Euskara";
		case Belarusian: return "Беларуская";
		case Bengali: return "বাংলা"; // it's there
		case Bihari: return "भोजपुरी";
		case Bislama: return "Bislama";
		case Bosnian: return "Bosanski";
		case Breton: return "Brezhoneg";
		case Bulgarian: return "Български";
		case Burmese: return "မ္ရန္‌မာစာ";
		// C
		case Catalan: return "Català";
		case Chamorro: return "Chamoru";
		case Chechen: return "Нохчийн";
		case Chinese: return "中文";
		case ChineseHK: return "廣東話";
		case ChineseSimplified: return "简体中文";
		case ChineseTraditional: return "繁體中文";
		case Chuvash: return "Чăваш";
		case Cornish: return "Kernewek";
		case Corsican: return "Corsu";
		case Cree: return "Nehiyaw";
		case Croatian: return "Hrvatski";
		case Czech: return "Česky";
		// D
		case Danish: return "Dansk";
		case Divehi: return "ދިވެހިބަސް";
		case Dutch: return "Nederlandse";
		case Dzongkha: return "ཇོང་ཁ";
		// E
		case English: return "English";
		case EnglishUK: return "English (UK)";
		case EnglishUS: return "English (US)";
		case Esperanto: return "Esperanto"; 
		case Estonian: return "Eesti";
		case Ewe: return "Eʋegbe";
		// F
		case Faroese: return "Føroyskt";
		case Fijian: return "Na Vosa Vakaviti";
		case Finnish: return "Suomi";
		case French: return "Français";
		case FrenchCA: return "Français (CA)";
		case Frisian: return "Frisian";
		case Fulah: return "Fulfulde";
		// G
		case Gaelic: return "Gàidhlig";
		case Galician: return "Galego";
		case Ganda: return "Luganda";
		case Georgian: return "ქართული";
		case German: return "Deutsch";
		case Greek: return "Ελληνικά";
		case Greenlandic: return "Kalaallisut";
		case Guarani: return "Avañe'ẽ";
		case Gujarati: return "ગુજરાતી";
		// H
		case Haitian: return "Krèyol ayisyen";
		case Hausa: return "هَوُسَ";
		case Hebrew: return "עברית";
		case HebrewIW: return "עברית";
		case Herero: return "Otsiherero";
		case Hindi: return "हिन्दी";
		case HiriMotu: return "Hiri Motu";
		case Hungarian: return "Magyar";
		// I
		case Icelandic: return "Íslenska";
		case Ido: return "Ido";
		case Igbo: return "Igbo";
		case Indonesian: return "Bahasa Indonesia";
		case Interlingua: return "Interlingua";
		case Interlingue: return "Interlingue";
		case Inuktitut: return "ᐃᓄᒃᑎᑐᑦ";
		case Inupiaq: return "Iñupiak";
		case Irish: return "Gaeilge";
		case Italian: return "Italiano";
		// J
		case Japanese: return "日本語";
		case Javanese: return "Basa Jawa";
		// K
		case Kannada: return "ಕನ್ನಡ";
		case Kanuri: return "Kanuri";
		case Kashmiri: return "कश्मीरी";
		case Kazakh: return "Қазақша";
		case Khmer: return "ភាសាខ្មែរ";
		case Kikuyu: return "Gĩkũyũ";
		case Kinyarwanda: return "Ikinyarwanda";
		case Komi: return "Коми";
		case Kongo: return "KiKongo";
		case Korean: return "한국어";
		case Kurdish: return "Kurdî";
		case Kwanyama: return "Kuanyama";
		case Kyrgyz: return "Кыргызча";
		// L
		case Lao: return "ພາສາລາວ";
		case Latin: return "Latina";
		case Latvian: return "Latviešu";
		case Limburgan: return "Limburgs";
		case Lingala: return "Lingala";
		case Lithuanian: return "Lietuvių";
		case LubaKatanga: return "Luba-Katanga";
		case Luxembourgish: return "Lëtzebuergesch";
		// M
		case Macedonian: return "Македонски";
		case Malagasy: return "Malagasy";
		case Malay: return "bahasa Melayu";
		case Malayalam: return "മലയാളം"; // it's there
		case Maltese: return "Malti";
		case Manx: return "Gaelg";
		case Maori: return "Māori";
		case Marathi: return "मराठी";
		case Marshallese: return "Ebon";
		case Mongolian: return "Монгол";
		// N
		case Nauru: return "dorerin Naoero";
		case Navajo: return "Diné bizaad";
		case Ndonga: return "Oshiwambo";
		case Nepali: return "नेपाली";
		case NorthernSami: return "Sámegiella";
		case NorthNdebele: return "isiNdebele";
		case NorwegianNB: return "Norsk (bokmål)";
		case NorwegianNN: return "Nynorsk";
		case Norwegian: return "Norsk";
		case Nyanja: return "chiCheŵa";
		// O
		case Ojibwa: return "ᐊᓂᔑᓈᐯᒧᐎᓐ";
		case OldSlavonic: return "ѩзыкъ словѣньскъ";
		case Oriya: return "ଓଡ଼ିଆ"; // it's there
		case Oromo: return "Oromoo";
		case Ossetian: return "Иронау";
		// P
		case Pali: return "पाऴि";
		case Panjabi: return "ਪੰਜਾਬੀ";
		case Pashto: return "پښتو";
		case Pashtu: return "پښتو";
		case Persian: return "فارسی";
		case Polish: return "Polski";
		case Portuguese: return "Português";
		case PortugueseBR: return "Português (BR)";
		case Provencal: return "Provençal";
		// Q
		case Quechua: return "Runa Simi";
		// R
		case Romanian: return "Română";
		case Romansh: return "Rumantsch";
		case Rundi: return "Kirundi";
		case Russian: return "Русский";
		// S
		case Samoan: return "Gagana Samoa";
		case Sango: return "Sängö";
		case Sanskrit: return "संस्कृतम्";
		case Sardinian: return "Sardu";
		case Serbian: return "Српски";
		case SerboCroatian:
		case SerboCroatianHR: 
			return "Srpskohrvatski";
		case SerboCroatianSR: return "Srpski";
		case SerboCroatianYU: return "Српски (sh_YU)";
		case Shona: return "chiShona";
		case SichuanYi: return "ꆇꉙ";
		case Sindhi: return "سنڌي";
		case Sinhala: return "සිංහල";
		case Slovak: return "Slovenčina";
		case Slovenian: return "Slovenščina";
		case Somali: return "Soomaaliga";
		case SouthNdebele: return "isiNdebele";
		case SouthernSotho: return "Sesotho";
		case Spanish: return "Español";
		case SpanishAR: return "Español (Argentina)";
		case SpanishBO: return "Español (Bolivia)";
		case SpanishCL: return "Español (Chile)";
		case SpanishCO: return "Español (Colombia)";
		case SpanishCR: return "Español (Costa Rica)";
		case SpanishCU: return "Español (Cuba)";
		case SpanishDO: return "Español (República Dominicana)";
		case SpanishEC: return "Español (Ecuador)";
		case SpanishES: return "Español (España)";
		case SpanishGT: return "Español (Guatemala)";
		case SpanishHN: return "Español (Honduras)";
		case SpanishMX: return "Español (México)";
		case SpanishNI: return "Español (Nicaragua)";
		case SpanishPA: return "Español (Panamá)";
		case SpanishPE: return "Español (Perú)";
		case SpanishPR: return "Español (Puerto Rico)";
		case SpanishPY: return "Español (Paraguay)";
		case SpanishSV: return "Español (El Salvador)";
		case SpanishUS: return "Español (Estados Unidos)";
		case SpanishUY: return "Español (Uruguay)";
		case SpanishVE: return "Español (Venezuela)";
		case Sundanese: return "Basa Sunda";
		case Swahili: return "Kiswahili";
		case Swati: return "SiSwati";
		case Swedish: return "Svenska";
		// T
		case Tagalog: return "Tagalog";
		case Tahitian: return "Reo Mā`ohi";
		case Tajik: return "Тоҷикӣ";
		case Tamil: return "தமிழ்";
		case Tatar: return "Татарча";
		case Telugu: return "తెలుగు";
		case Thai: return "ไทย";
		case Tibetan: return "བོད་སྐད";
		case Tigrinya: return "ትግርኛ";
		case Tonga: return "faka Tonga";
		case Tsonga: return "Xitsonga";
		case Tswana: return "Setswana";
		case Turkish: return "Türkçe";
		case Turkmen: return "Туркмен";
		case Twi: return "Twi";
		// U
		case Ukrainian: return "Українська";
		case Urdu: return "اردو";
		case Uyghur: return "Oyghurque";
		case Uzbek: return "O‘zbek";
		// V
		case Venda: return "Tshivenda";
		case Vietnamese: return "Tiếng Việt";
		case Volapuck: return "Volapük";
		// W
		case Walloon: return "Walon";
		case Welsh: return "Cymraeg";
		case WesternFrisian: return "Frysk";
		// X
		case Xhosa: return "isiXhosa";
		// Y
		case Yiddish: return "ייִדיש";
		case Yoruba: return "Yorùbá";
		// Z
		case Zhuang: return "Cuengh";
		case Zulu: return "isiZulu";
		default:
			return getCode();
		}
	}
	
	
	public String getName()
	{
		switch(this)
		{
		// A
		case Afar: return TXT.get("CLanguageCode.Afar", "Afar");
		case Abkhazian: return TXT.get("CLanguageCode.Abkhazian", "Abkhazian");
		case Afrikaans: return TXT.get("CLanguageCode.Afrikaans", "Afrikaans");
		case Akan: return TXT.get("CLanguageCode.Akan", "Akan");
		case Albanian: return TXT.get("CLanguageCode.Albanian", "Albanian");
		case Amharic: return TXT.get("CLanguageCode.Amharic", "Amharic");
		case Arabic: return TXT.get("CLanguageCode.Arabic", "Arabic");
		case Aragonese: return TXT.get("CLanguageCode.Aragonese", "Aragonese");
		case Armenian: return TXT.get("CLanguageCode.Armenian", "Armenian");
		case Assamese: return TXT.get("CLanguageCode.Assamese", "Assamese");
		case Asturian: return TXT.get("CLanguageCode.Asturian", "Asturian");
		case Avaric: return TXT.get("CLanguageCode.Avaric", "Avaric");
		case Avestan: return TXT.get("CLanguageCode.Avestan", "Avestan");
		case Aymara: return TXT.get("CLanguageCode.Aymara", "Aymará");
		case Azerbaijani: return TXT.get("CLanguageCode.Azerbaijani", "Azerbaijani");
		// B
		case Bambara: return TXT.get("CLanguageCode.Bambara", "Bambara");
		case Bashkir: return TXT.get("CLanguageCode.Bashkir", "Bashkir");
		case Basque: return TXT.get("CLanguageCode.Basque", "Basque");
		case Belarusian: return TXT.get("CLanguageCode.Belarusian", "Belarusian");
		case Bengali: return TXT.get("CLanguageCode.Bengali", "Bengali");
		case Bihari: return TXT.get("CLanguageCode.Bihari", "Bihari");
		case Bislama: return TXT.get("CLanguageCode.Bislama", "Bislama");
		case Bosnian: return TXT.get("CLanguageCode.Bosnian", "Bosnian");
		case Breton: return TXT.get("CLanguageCode.Breton", "Breton");
		case Bulgarian: return TXT.get("CLanguageCode.Bulgarian", "Bulgarian");
		case Burmese: return TXT.get("CLanguageCode.Burmese", "Burmese");
		// C
		case Catalan: return TXT.get("CLanguageCode.Catalan", "Catalan");
		case Chamorro: return TXT.get("CLanguageCode.Chamorro", "Chamorro");
		case Chechen: return TXT.get("CLanguageCode.Chechen", "Chechen");
		case Chinese: return TXT.get("CLanguageCode.Chinese", "Chinese");
		case ChineseHK: return TXT.get("CLanguageCode.Chinese HK", "Chinese (HK)");
		case ChineseSimplified: return TXT.get("CLanguageCode.Chinese Simplified", "Chinese (Simplified)");
		case ChineseTraditional: return TXT.get("CLanguageCode.Chinese Traditional", "Chinese (Traditional)");
		case Chuvash: return TXT.get("CLanguageCode.Chuvash", "Chuvash");
		case Cornish: return TXT.get("CLanguageCode.Cornish", "Cornish");
		case Corsican: return TXT.get("CLanguageCode.Corsican", "Corsican");
		case Cree: return TXT.get("CLanguageCode.Cree", "Cree");
		case Croatian: return TXT.get("CLanguageCode.Croatian", "Croatian");
		case Czech: return TXT.get("CLanguageCode.Czech", "Czech");
		// D
		case Danish: return TXT.get("CLanguageCode.Danish", "Danish");
		case Divehi: return TXT.get("CLanguageCode.Divehi", "Divehi");
		case Dutch: return TXT.get("CLanguageCode.Dutch", "Dutch");
		case Dzongkha: return TXT.get("CLanguageCode.Dzongkha", "Dzongkha");
		// E
		case English: return TXT.get("CLanguageCode.English", "English");
		case EnglishUK: return TXT.get("CLanguageCode.English UK", "English (UK)");
		case EnglishUS: return TXT.get("CLanguageCode.English US", "English (US)");
		case Esperanto: return TXT.get("CLanguageCode.Esperanto", "Esperanto");
		case Estonian: return TXT.get("CLanguageCode.Estonian", "Estonian");
		case Ewe: return TXT.get("CLanguageCode.Ewe", "Ewe");
		// F
		case Faroese: return TXT.get("CLanguageCode.Faroese", "Faroese");
		case Fijian: return TXT.get("CLanguageCode.Fijian", "Fijian");
		case Finnish: return TXT.get("CLanguageCode.Finnish", "Finnish");
		case French: return TXT.get("CLanguageCode.French", "French");
		case FrenchCA: return TXT.get("CLanguageCode.French CA", "French (CA)");
		case Frisian: return TXT.get("CLanguageCode.Frisian", "Frisian");
		case Fulah: return TXT.get("CLanguageCode.Fulah", "Fulah");
		// G
		case Gaelic: return TXT.get("CLanguageCode.Gaelic", "Gaelic");
		case Galician: return TXT.get("CLanguageCode.Galician", "Galician");
		case Ganda: return TXT.get("CLanguageCode.Ganda", "Ganda");
		case Georgian: return TXT.get("CLanguageCode.Georgian", "Georgian");
		case German: return TXT.get("CLanguageCode.German", "German");
		case Greek: return TXT.get("CLanguageCode.Greek", "Greek");
		case Greenlandic: return TXT.get("CLanguageCode.Greenlandic", "Greenlandic");
		case Guarani: return TXT.get("CLanguageCode.Guarani", "Guarani");
		case Gujarati: return TXT.get("CLanguageCode.Gujarati", "Gujarati");
		// H
		case Haitian: return TXT.get("CLanguageCode.Haitian", "Haitian");
		case Hausa: return TXT.get("CLanguageCode.Hausa", "Hausa");
		case Hebrew:
		case HebrewIW:
			return TXT.get("CLanguageCode.Hebrew", "Hebrew");
		case Herero: return TXT.get("CLanguageCode.Herero", "Herero");
		case Hindi: return TXT.get("CLanguageCode.Hindi", "Hindi");
		case HiriMotu: return TXT.get("CLanguageCode.Hiri Motu", "Hiri Motu");
		case Hungarian: return TXT.get("CLanguageCode.Hungarian", "Hungarian");
		// I
		case Icelandic: return TXT.get("CLanguageCode.Icelandic", "Icelandic");
		case Ido: return TXT.get("CLanguageCode.Ido", "Ido");
		case Igbo: return TXT.get("CLanguageCode.Igbo", "Igbo");
		case Indonesian: return TXT.get("CLanguageCode.Indonesian", "Indonesian");
		case Interlingua: return TXT.get("CLanguageCode.Interlingua", "Interlingua");
		case Interlingue: return TXT.get("CLanguageCode.Interlingue", "Interlingue");
		case Inuktitut: return TXT.get("CLanguageCode.Inuktitut", "Inuktitut");
		case Inupiaq: return TXT.get("CLanguageCode.Inupiaq", "Inupiaq");
		case Irish: return TXT.get("CLanguageCode.Irish", "Irish");
		case Italian: return TXT.get("CLanguageCode.Italian", "Italian");
		// J
		case Japanese: return TXT.get("CLanguageCode.Japanese", "Japanese");
		case Javanese: return TXT.get("CLanguageCode.Javanese", "Javanese");
		// K
		case Kannada: return TXT.get("CLanguageCode.Kannada", "Kannada");
		case Kanuri: return TXT.get("CLanguageCode.Kanuri", "Kanuri");
		case Kashmiri: return TXT.get("CLanguageCode.Kashmiri", "Kashmiri");
		case Kazakh: return TXT.get("CLanguageCode.Kazakh", "Kazakh");
		case Khmer: return TXT.get("CLanguageCode.Khmer", "Khmer");
		case Kikuyu: return TXT.get("CLanguageCode.Kikuyu", "Kikuyu");
		case Kinyarwanda: return TXT.get("CLanguageCode.Kinyarwanda", "Kinyarwanda");
		case Komi: return TXT.get("CLanguageCode.Komi", "Komi");
		case Kongo: return TXT.get("CLanguageCode.Kongo", "Kongo");
		case Korean: return TXT.get("CLanguageCode.Korean", "Korean");
		case Kurdish: return TXT.get("CLanguageCode.Kurdish", "Kurdish");
		case Kwanyama: return TXT.get("CLanguageCode.Kwanyama", "Kwanyama");
		case Kyrgyz: return TXT.get("CLanguageCode.Kyrgyz", "Kyrgyz");
		// L
		case Lao: return TXT.get("CLanguageCode.Lao", "Lao");
		case Latin: return TXT.get("CLanguageCode.Latin", "Latin");
		case Latvian: return TXT.get("CLanguageCode.Latvian", "Latvian");
		case Limburgan: return TXT.get("CLanguageCode.Limburgan", "Limburgan");
		case Lingala: return TXT.get("CLanguageCode.Lingala", "Lingala");
		case Lithuanian: return TXT.get("CLanguageCode.Lithuanian", "Lithuanian");
		case LubaKatanga: return TXT.get("CLanguageCode.Luba-Katanga", "Luba-Katanga");
		case Luxembourgish: return TXT.get("CLanguageCode.Luxembourgish", "Luxembourgish");
		// M
		case Macedonian: return TXT.get("CLanguageCode.Macedonian", "Macedonian");
		case Malagasy: return TXT.get("CLanguageCode.Malagasy", "Malagasy");
		case Malay: return TXT.get("CLanguageCode.Malay", "Malay");
		case Malayalam: return TXT.get("CLanguageCode.Malayalam", "Malayalam");
		case Maltese: return TXT.get("CLanguageCode.Maltese", "Maltese");
		case Manx: return TXT.get("CLanguageCode.Manx", "Manx");
		case Maori: return TXT.get("CLanguageCode.Maori", "Maori");
		case Marathi: return TXT.get("CLanguageCode.Marathi", "Marathi");
		case Marshallese: return TXT.get("CLanguageCode.Marshallese", "Marshallese");
		case Mongolian: return TXT.get("CLanguageCode.Mongolian", "Mongolian");
		// N
		case Nauru: return TXT.get("CLanguageCode.Nauru", "Nauru");
		case Navajo: return TXT.get("CLanguageCode.Navajo", "Navajo");
		case Ndonga: return TXT.get("CLanguageCode.Ndonga", "Ndonga");
		case Nepali: return TXT.get("CLanguageCode.Nepali", "Nepali");
		case NorthernSami: return TXT.get("CLanguageCode.Northern Sami", "Northern Sami");
		case NorthNdebele: return TXT.get("CLanguageCode.North Ndebele", "North Ndebele");
		case NorwegianNB: return TXT.get("CLanguageCode.Norwegian Bokmal", "Norwegian Bokmål");
		case NorwegianNN: return TXT.get("CLanguageCode.Norwegian Nynorsk", "Norwegian Nynorsk");
		case Norwegian: return TXT.get("CLanguageCode.Norwegian", "Norwegian");
		case Nyanja: return TXT.get("CLanguageCode.Nyanja", "Nyanja");
		// O
		case Ojibwa: return TXT.get("CLanguageCode.Ojibwa", "Ojibwa");
		case OldSlavonic: return TXT.get("CLanguageCode.Old Slavonic", "Old Slavonic");
		case Oriya: return TXT.get("CLanguageCode.Oriya", "Oriya");
		case Oromo: return TXT.get("CLanguageCode.Oromo", "Oromo");
		case Ossetian: return TXT.get("CLanguageCode.Ossetian", "Ossetian");
		// P
		case Pali: return TXT.get("CLanguageCode.Pali", "Pali");
		case Panjabi: return TXT.get("CLanguageCode.Panjabi", "Panjabi");
		case Pashto: return TXT.get("CLanguageCode.Pashto", "Pashto");
		case Pashtu: return TXT.get("CLanguageCode.Pashtu", "Pashtu");
		case Persian: return TXT.get("CLanguageCode.Persian", "Persian");
		case Polish: return TXT.get("CLanguageCode.Polish", "Polish");
		case Portuguese: return TXT.get("CLanguageCode.Portuguese", "Portuguese");
		case PortugueseBR: return TXT.get("CLanguageCode.Portuguese BR", "Portuguese (BR)");
		case Provencal: return TXT.get("CLanguageCode.Provencal", "Provençal");
		// Q
		case Quechua: return TXT.get("CLanguageCode.Quechua", "Quechua");
		// R
		case Romanian: return TXT.get("CLanguageCode.Romanian", "Romanian");
		case Romansh: return TXT.get("CLanguageCode.Romansh", "Romansh");
		case Rundi: return TXT.get("CLanguageCode.Rundi", "Rundi");
		case Russian: return TXT.get("CLanguageCode.Russian", "Russian");
		// S
		case Samoan: return TXT.get("CLanguageCode.Samoan", "Samoan");
		case Sango: return TXT.get("CLanguageCode.Sango", "Sango");
		case Sanskrit: return TXT.get("CLanguageCode.Sanskrit", "Sanskrit");
		case Sardinian: return TXT.get("CLanguageCode.Sardinian", "Sardinian");
		case Serbian: return TXT.get("CLanguageCode.Serbian", "Serbian");
		case SerboCroatian: return TXT.get("CLanguageCode.Serbo-Croatian", "Serbo-Croatian");
		case SerboCroatianHR: return TXT.get("CLanguageCode.Serbo-Croatian sh_HR", "Serbo-Croatian (sh_HR)");
		case SerboCroatianSR: return TXT.get("CLanguageCode.Serbo-Croatian sh_SR", "Serbo-Croatian (Latin)");
		case SerboCroatianYU: return TXT.get("CLanguageCode.Serbo-Croatian sh_YU", "Serbo-Croatian (Cyrillic)");
		case Shona: return TXT.get("CLanguageCode.Shona", "Shona");
		case SichuanYi: return TXT.get("CLanguageCode.Sichuan Yi", "Sichuan Yi");
		case Sindhi: return TXT.get("CLanguageCode.Sindhi", "Sindhi");
		case Sinhala: return TXT.get("CLanguageCode.Sinhala", "Sinhala");
		case Slovak: return TXT.get("CLanguageCode.Slovak", "Slovak");
		case Slovenian: return TXT.get("CLanguageCode.Slovenian", "Slovenian");
		case Somali: return TXT.get("CLanguageCode.Somali", "Somali");
		case SouthNdebele: return TXT.get("CLanguageCode.South Ndebele", "South Ndebele");
		case SouthernSotho: return TXT.get("CLanguageCode.Southern Sotho", "Southern Sotho");
		case Spanish: return TXT.get("CLanguageCode.Spanish", "Spanish");
		case SpanishAR: return TXT.get("CLanguageCode.Spanish Argentina", "Spanish (Argentina)");
		case SpanishBO: return TXT.get("CLanguageCode.Spanish Bolivia", "Spanish (Bolivia)");
		case SpanishCL: return TXT.get("CLanguageCode.Spanish Chile", "Spanish (Chile)");
		case SpanishCO: return TXT.get("CLanguageCode.Spanish Colombia", "Spanish (Colombia)");
		case SpanishCR: return TXT.get("CLanguageCode.Spanish Costa Rica", "Spanish (Costa Rica)");
		case SpanishCU: return TXT.get("CLanguageCode.Spanish Cuba", "Spanish (Cuba)");
		case SpanishDO: return TXT.get("CLanguageCode.Spanish Dominican Republic", "Spanish (Dominican Republic)");
		case SpanishEC: return TXT.get("CLanguageCode.Spanish Ecuador", "Spanish (Ecuador)");
		case SpanishES: return TXT.get("CLanguageCode.Spanish Spain", "Spanish (Spain)");
		case SpanishGT: return TXT.get("CLanguageCode.Spanish Guatemala", "Spanish (Guatemala)");
		case SpanishHN: return TXT.get("CLanguageCode.Spanish Honduras", "Spanish (Honduras)");
		case SpanishMX: return TXT.get("CLanguageCode.Spanish Mexico", "Spanish (Mexico)");
		case SpanishNI: return TXT.get("CLanguageCode.Spanish Nicaragua", "Spanish (Nicaragua)");
		case SpanishPA: return TXT.get("CLanguageCode.Spanish Panama", "Spanish (Panama)");
		case SpanishPE: return TXT.get("CLanguageCode.Spanish Peru", "Spanish (Peru)");
		case SpanishPR: return TXT.get("CLanguageCode.Spanish Puerto Rico", "Spanish (Puerto Rico)");
		case SpanishPY: return TXT.get("CLanguageCode.Spanish Paraguay", "Spanish (Paraguay)");
		case SpanishSV: return TXT.get("CLanguageCode.Spanish El Salvador", "Spanish (El Salvador)");
		case SpanishUS: return TXT.get("CLanguageCode.Spanish US", "Spanish (US)");
		case SpanishUY: return TXT.get("CLanguageCode.Spanish Uruguay", "Spanish (Uruguay)");
		case SpanishVE: return TXT.get("CLanguageCode.Spanish Venezuela", "Spanish (Venezuela)");
		case Sundanese: return TXT.get("CLanguageCode.Sundanese", "Sundanese");
		case Swahili: return TXT.get("CLanguageCode.Swahili", "Swahili");
		case Swati: return TXT.get("CLanguageCode.Swati", "Swati");
		case Swedish: return TXT.get("CLanguageCode.Swedish", "Swedish");
		// T
		case Tagalog: return TXT.get("CLanguageCode.Tagalog", "Tagalog");
		case Tahitian: return TXT.get("CLanguageCode.Tahitian", "Tahitian");
		case Tajik: return TXT.get("CLanguageCode.Tajik", "Tajik");
		case Tamil: return TXT.get("CLanguageCode.Tamil", "Tamil");
		case Tatar: return TXT.get("CLanguageCode.Tatar", "Tatar");
		case Telugu: return TXT.get("CLanguageCode.Telugu", "Telugu");
		case Thai: return TXT.get("CLanguageCode.Thai", "Thai");
		case Tibetan: return TXT.get("CLanguageCode.Tibetan", "Tibetan");
		case Tigrinya: return TXT.get("CLanguageCode.Tigrinya", "Tigrinya");
		case Tonga: return TXT.get("CLanguageCode.Tonga", "Tonga");
		case Tsonga: return TXT.get("CLanguageCode.Tsonga", "Tsonga");
		case Tswana: return TXT.get("CLanguageCode.Tswana", "Tswana");
		case Turkish: return TXT.get("CLanguageCode.Turkish", "Turkish");
		case Turkmen: return TXT.get("CLanguageCode.Turkmen", "Turkmen");
		case Twi: return TXT.get("CLanguageCode.Twi", "Twi");
		// U
		case Ukrainian: return TXT.get("CLanguageCode.Ukrainian", "Ukrainian");
		case Urdu: return TXT.get("CLanguageCode.Urdu", "Urdu");
		case Uyghur: return TXT.get("CLanguageCode.Uyghur", "Uyghur");
		case Uzbek: return TXT.get("CLanguageCode.Uzbek", "Uzbek");
		// V
		case Venda: return TXT.get("CLanguageCode.Venda", "Venda");
		case Vietnamese: return TXT.get("CLanguageCode.Vietnamese", "Vietnamese");
		case Volapuck: return TXT.get("CLanguageCode.Volapuk", "Volapük");
		// W
		case Walloon: return TXT.get("CLanguageCode.Walloon", "Walloon");
		case Welsh: return TXT.get("CLanguageCode.Welsh", "Welsh");
		case WesternFrisian: return TXT.get("CLanguageCode.Western Frisian", "Western Frisian");
		// X
		case Xhosa: return TXT.get("CLanguageCode.Xhosa", "Xhosa");
		// Y
		case Yiddish: return TXT.get("CLanguageCode.Yiddish", "Yiddish");
		case Yoruba: return TXT.get("CLanguageCode.Yoruba", "Yoruba");
		// Z
		case Zhuang: return TXT.get("CLanguageCode.Zhuang", "Zhuang");
		case Zulu: return TXT.get("CLanguageCode.Zulu", "Zulu");
		default:
			return getCode();
		}
	}
	
	
	public static boolean isLeftToRight(CLanguageCode c)
	{
		if(c != null)
		{
			switch(c)
			{
			case Arabic:
			case Hebrew:
			case HebrewIW:
			case Persian:
			case Urdu:
			case Yiddish:
				return false;
			}
		}
		return true;
	}
}