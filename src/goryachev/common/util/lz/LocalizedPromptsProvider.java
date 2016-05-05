// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CLanguage;
import goryachev.common.util.Log;
import goryachev.common.util.PromptProvider;
import java.io.File;


public abstract class LocalizedPromptsProvider
	implements PromptProvider
{
	protected CLanguage language;
	protected LocalizedPrompts prompts;
	
	
	public LocalizedPromptsProvider()
	{
	}
	
	
	public String getPrompt(String id, String master)
	{
		if(prompts != null)
		{
			String s = prompts.get(id);
			if(s != null)
			{
				return s;
			}
		}
		return master;
	}
	
	
	public static LocalizedPromptsProvider loadLocalResource(Class c, String name) throws Exception
	{
		String prefix = c.getPackage().getName();
		prefix = prefix.replace(".", "/");
		return loadLocalResource(prefix + "/" + name);
	}
	
	
	public static LocalizedPromptsProvider loadLocalResource(final String prefix) throws Exception
	{
		return new LocalizedPromptsProvider()
		{
			public void setLanguage(CLanguage language)
			{
				String path = prefix + "_" + language.getID() + LocalizedPromptsFormat.EXTENSION;
				try
				{
					prompts = LocalizedPrompts.load(path);
				}
				catch(Exception e)
				{
					Log.fail(new Exception("failed to load prompts for " + language + " from path " + path, e));
					prompts = null;
				}
			}
		};
	}
	
	
	public static LocalizedPromptsProvider loadFileResource(final File folder, final String name) throws Exception
	{
		return new LocalizedPromptsProvider()
		{
			public void setLanguage(CLanguage language)
			{
				File f = new File(folder, name + "_" + language.getLangCode() + LocalizedPromptsFormat.EXTENSION);
				try
				{
					prompts = LocalizedPrompts.load(f);
				}
				catch(Exception e)
				{
					Log.fail(new Exception("failed to load prompts for " + language + " from file " + f, e));
					prompts = null;
				}
			}
		};
	}
}
