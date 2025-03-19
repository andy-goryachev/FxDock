// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.CssLoader;
import javafx.collections.ObservableList;
import javafx.css.Styleable;


/**
 * CSS Style Identifier.
 * 
 * Usage example:
 * <pre>
 * public static final CssStyle EXAMPLE = new CssStyle();
 * ...
 * {
 *     Pane pane = new Pane();
 *     EXAMPLE.set(pane);
 * }
 * <pre>
 * 
 * @see FxStyleSheet
 */
public class CssStyle
{
	private final String name;
	private static int seq;
	
	
	public CssStyle()
	{
		name = generateName(new Throwable().getStackTrace()[1]);
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	@Override
	public String toString()
	{
		return name;
	}
	
	
	/**
	 * Adds the CssStyle to the Styleable.
	 */
	public void set(Styleable n)
	{
		ObservableList<String> ss = n.getStyleClass();
		if(!ss.contains(name))
		{
			n.getStyleClass().add(getName());
		}
	}
	
	
	/**
	 * Adds or removes the CssStyle based on the condition.
	 */
	public void set(Styleable n, boolean condition)
	{
		if(n == null)
		{
			return;
		}
		
		String name = getName();
		ObservableList<String> ss = n.getStyleClass();
		if(condition)
		{
			if(!ss.contains(name))
			{
				ss.add(name);
			}
		}
		else
		{
			ss.remove(name);
		}
	}
	
	
	private static String generateName(StackTraceElement s)
	{
		if(CssLoader.DUMP)
		{
			return s.getClassName().replace('.', '_') + "_L" + s.getLineNumber();
		}
		else
		{
			synchronized(CssStyle.class)
			{
				return "S" + (seq++);
			}
		}
	}
}
