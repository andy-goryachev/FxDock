// Copyright © 2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.SB;
import goryachev.fx.CssLoader;
import javafx.scene.Node;


/**
 * FX has rather poor support for programmating setting of certain properties,
 * such as TableView row height.  This class plugs into CssLoader and its 
 * global styles to provide an easy way to set these properies.
 * 
 * This hack requires CssLoader to be initialized.
 */
public class CssHack<T>
{
	private final String name;
	private final String css;
	private final T value;
	private final double doubleValue;
	
	
	public CssHack(String name, String css, T value, double doubleValue)
	{
		this.name = name;
		this.css = css;
		this.value = value;
		this.doubleValue = doubleValue;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String getCSS()
	{
		return css;
	}
	
	
	public T getValue()
	{
		return value;
	}
	
	
	public double doubleValue()
	{
		return doubleValue;
	}
	
	
	public static String generateName(String prefix, String suffix)
	{
		SB sb = new SB();
		sb.a(prefix);
		sb.a(suffix);
		
		sb.replace(' ', '∘');
		sb.replace('-', '−');
		sb.replace('.', '·');
		
		return sb.toString();
	}
	
	
	public static <X> CssHack<X> get(Node owner, Object key)
	{
		Object x = owner.getProperties().get(key);
		if(x instanceof CssHack)
		{
			return (CssHack)x;
		}
		return null;
	}
	
	
	public static void remove(Node owner, Object key)
	{
		CssHack h = get(owner, key);
		if(h != null)
		{
			String name = h.getName();
			owner.getStyleClass().remove(name);
		}
	}


	public void attachTo(Node owner, Object key)
	{
		owner.getStyleClass().add(name);
		owner.getProperties().put(key, this);
		
		CssLoader.addGlobalStyle(css);
	}
}
