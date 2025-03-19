// Copyright © 2024-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * When a value needs display text to be shown to the user.
 */
public final class NamedValue<T> implements HasDisplayText
{
	private final String text;
	private final T value;
	
	
	public NamedValue(String text, T value)
	{
		this.text = text;
		this.value = value;
	}


	@Override
	public String getDisplayText()
	{
		return text;
	}
	
	
	@Override
	public String toString()
	{
		return text;
	}
	
	
	public T getValue()
	{
		return value;
	}
	
	
	@Override
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof NamedValue n)
		{
			return
				CKit.equals(text, n.text) &&
				CKit.equals(value, n.value);
		}
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		int h = FH.hash(NamedValue.class);
		h = FH.hash(h, text);
		h = FH.hash(h, value);
		return h;
	}
}
