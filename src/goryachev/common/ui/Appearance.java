// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.options.LanguageOption;
import goryachev.common.ui.options.edit.LanguageOptionEditor;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CLanguageCode;
import goryachev.common.util.CList;


// TODO colors, theme
public class Appearance
{
	public static final LanguageOption interfaceLanguageOption = new LanguageOption("appearance.language");
	private static CLanguage[] supportedLanguages = { new CLanguage(CLanguageCode.EnglishUS) };


	public static CLanguage getLanguage()
	{
		return interfaceLanguageOption.get();
	}
	
	
	public static void setLanguage(CLanguage la)
	{
		interfaceLanguageOption.set(la);
		GlobalSettings.save();
	}


	public static LanguageOptionEditor getLanguageEditor()
	{
		return new LanguageOptionEditor(interfaceLanguageOption, supportedLanguages, getLanguage());
	}
	
	
	public static void setSupportedLanguages(CLanguageCode[] ls)
	{
		CLanguage[] langs;
		if(ls == null)
		{	
			langs = null;
		}
		else
		{
			int sz = ls.length;
			langs = new CLanguage[sz];
			for(int i=0; i<sz; i++)
			{
				langs[i] = new CLanguage(ls[i]);
			}
		}
		
		setSupportedLanguages(langs);
	}
	
	
	public static void setSupportedLanguages(CLanguage[] ls)
	{
		if((ls == null) || (ls.length == 0))
		{
			ls = null;
		}
		
		supportedLanguages = ls;
	}
	
	
	public static CList<CLanguage> getSupportedLanguages()
	{
		return (supportedLanguages == null) ? new CList() : new CList(supportedLanguages);
	}
}
