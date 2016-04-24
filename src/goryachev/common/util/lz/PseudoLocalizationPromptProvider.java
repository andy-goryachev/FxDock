// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CLanguageCode;
import goryachev.common.util.PromptProvider;


public class PseudoLocalizationPromptProvider
	implements PromptProvider
{
	private PseudoLocalization provider;
	
	
	public PseudoLocalizationPromptProvider()
	{
		setLanguage(new CLanguage(CLanguageCode.EnglishUS));
	}


	public void setLanguage(CLanguage la)
	{
		provider = PseudoLocalization.createPseudoLocalization(la);
	}


	public String getPrompt(String id, String master)
	{
		return provider.getPrompt(id, master);
	}


	public CLanguage[] getAvailableLanguages()
	{
		return null;
	}
}
