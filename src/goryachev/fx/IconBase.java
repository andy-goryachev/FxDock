// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.layout.Region;


/**
 * Icon Base Region.
 */
public class IconBase
	extends Region
{
	public IconBase(double size)
	{
		this(size, size);
	}
	
	
	public IconBase(double width, double height)
	{
		setMinSize(width, height);
		setPrefSize(width, height);
		setMaxSize(width, height);
	}

	
	public void add(Node n)
	{
		getChildren().add(n);
	}
	
	
	public void addAll(Node ... ns)
	{
		getChildren().addAll(ns);
	}
	
	
	public void addAll(Collection<Node> ns)
	{
		getChildren().addAll(ns);
	}
}
