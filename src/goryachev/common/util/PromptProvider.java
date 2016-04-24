// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public interface PromptProvider
{
	public void setLanguage(CLanguage language);


	public String getPrompt(String id, String master);
}
