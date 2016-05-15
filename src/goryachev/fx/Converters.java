// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Parsers;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;


/**
 * Converters.
 */
public class Converters
{
	public static StringConverter<Boolean> cboolean;
	public static StringConverter<Integer> cint;
	public static StringConverter<String> cstring;
	
	
	public static <T> StringConverter<T> get(Property<T> p)
	{
		if(p instanceof BooleanProperty)
		{
			return (StringConverter<T>)BOOLEAN();
		}
		else if(p instanceof IntegerProperty)
		{
			return (StringConverter<T>)INT();
		}
		else if(p instanceof StringProperty)
		{
			return (StringConverter<T>)STRING();
		}
		else
		{
			throw new Error("?" + p);
		}
	}
	
	
	public static StringConverter<Boolean> BOOLEAN()
	{
		if(cboolean == null)
		{
			cboolean = new StringConverter<Boolean>()
			{
				public Boolean fromString(String s)
				{
					return Parsers.parseBoolean(s);
				}

				public String toString(Boolean x)
				{
					return Boolean.TRUE.equals(x) ? "true" : "false";
				}
			};
		}
		return cboolean;
	}
	
	
	public static StringConverter<Integer> INT()
	{
		if(cint == null)
		{
			cint = new StringConverter<Integer>()
			{
				public Integer fromString(String s)
				{
					return Parsers.parseInt(s, 0);
				}

				public String toString(Integer x)
				{
					return String.valueOf(x);
				}
			};
		}
		return cint;
	}
	
	
	public static StringConverter<String> STRING()
	{
		if(cstring == null)
		{
			cstring = new StringConverter<String>()
			{
				public String toString(String s)
				{
					return s;
				}

				public String fromString(String s)
				{
					return s;
				}
			};
		}
		return cstring;
	}
}
