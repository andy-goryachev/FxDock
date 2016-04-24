// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.LanguageOption;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CLookup;
import goryachev.common.util.CSorter;
import goryachev.common.util.CStringList;


public class LanguageOptionEditor
	extends ChoiceOptionEditor<CLanguage>
{
	private CLookup languages;
	
	
	public LanguageOptionEditor(LanguageOption op, CLanguage[] supported, CLanguage selected)
	{
		super(op);
		
		languages = new CLookup();
		
		CStringList names = new CStringList();
		for(CLanguage la: supported)
		{
			String s = la.getLocalName();
			try
			{
				languages.add(la, s);
				names.add(s);
			}
			catch(Exception e)
			{ }
		}
		
		CSorter.sort(names);
		
		setChoices(names.toStringArray());
		
		if(selected != null)
		{
			setSelectedValue(selected);
		}
	}


	protected CLanguage parseEditorValue(String s)
	{
		Object x = languages.lookup(s);
		if(x instanceof CLanguage)
		{
			return (CLanguage)x;
		}
		else
		{
			return null;
		}
	}


	protected String toEditorValue(CLanguage item)
	{
		Object x = languages.lookup(item);
		if(x instanceof String)
		{
			return (String)x;
		}
		else
		{
			return null;
		}
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}
