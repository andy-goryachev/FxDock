// Copyright © 2018-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.List;


/**
 * A bidirectional Choice - Text lookup facility.
 */
public class Choices<T>
{
	protected static class Choice<C>
	{
		protected C choice;
		protected String text;
		
		public String toString() { return text; }
	}
	
	private final CList<Choice<T>> choices;
	
	
	public Choices(Object ... choiceTextPairs)
	{
		choices = new CList<Choice<T>>(choiceTextPairs.length / 2);
		for(int i=0; i<choiceTextPairs.length; )
		{
			Choice<T> ch = new Choice<T>();
			ch.choice = (T)choiceTextPairs[i++];
			ch.text = (String)choiceTextPairs[i++];
			choices.add(ch);
		}
	}
	
	
	public List<Choice<T>> asList()
	{
		return new CList<>(choices);
	}
	
	
	public String lookupText(T choice)
	{
		for(Choice<T> c: choices)
		{
			if(CKit.equals(c.choice, choice))
			{
				return c.text;
			}
		}
		return null;
	}
	
	
	public String lookupText(T choice, String defaultValue)
	{
		String s = lookupText(choice);
		return s == null ? defaultValue : s;
	}
	
	
	public T lookupChoice(String text)
	{
		for(Choice<T> c: choices)
		{
			if(CKit.equals(c.text, text))
			{
				return c.choice;
			}
		}
		return null;
	}
	
	
	public T lookupChoice(String text, T defaultValue)
	{
		T ch = lookupChoice(text);
		return ch == null ? defaultValue : ch;
	}
}
