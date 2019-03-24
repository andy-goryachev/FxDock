// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Parsers;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;


/**
 * Converters.
 */
public class Converters
{
	protected static StringConverter<Boolean> booleanConverter;
	protected static StringConverter<Integer> intConverter;
	protected static StringConverter<Number> doubleNumberConverter;
	protected static StringConverter<Number> intNumberConverter;
	protected static StringConverter<String> stringConverter;
	protected static StringConverter<Object> objectConverter;
	
	
	@SuppressWarnings("unchecked")
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
		else if(p instanceof DoubleProperty)
		{
			return (StringConverter<T>)NUMBER_DOUBLE();
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
		if(booleanConverter == null)
		{
			booleanConverter = new StringConverter<Boolean>()
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
		return booleanConverter;
	}
	
	
	public static StringConverter<Integer> INT()
	{
		if(intConverter == null)
		{
			intConverter = new StringConverter<Integer>()
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
		return intConverter;
	}
	
	
	public static StringConverter<Number> NUMBER_INT()
	{
		if(intNumberConverter == null)
		{
			intNumberConverter = new StringConverter<Number>()
			{
				public Number fromString(String s)
				{
					return Parsers.parseInt(s, 0);
				}

				public String toString(Number x)
				{
					return String.valueOf(x);
				}
			};
		}
		return intNumberConverter;
	}
	
	
	public static StringConverter<Number> NUMBER_DOUBLE()
	{
		if(doubleNumberConverter == null)
		{
			doubleNumberConverter = new StringConverter<Number>()
			{
				public Number fromString(String s)
				{
					return Parsers.parseDouble(s, 0.0);
				}

				public String toString(Number x)
				{
					return String.valueOf(x);
				}
			};
		}
		return doubleNumberConverter;
	}
	
	
	public static StringConverter<String> STRING()
	{
		if(stringConverter == null)
		{
			stringConverter = new StringConverter<String>()
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
		return stringConverter;
	}
	
	
	public static StringConverter<Object> OBJECT()
	{
		if(objectConverter == null)
		{
			objectConverter = new StringConverter<Object>()
			{
				public String toString(Object x)
				{
					return x == null ? null : x.toString();
				}

				public Object fromString(String s)
				{
					return s;
				}
			};
		}
		return objectConverter;
	}
}
