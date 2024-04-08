// Copyright © 2017-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.util;
import goryachev.common.util.CList;
import java.util.List;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Utility to simplify code for building complex paths.
 */
public class FxPathBuilder
{
	private final CList<PathElement> path = new CList<>();
	
	
	public FxPathBuilder()
	{
	}
	
	
	public void moveto(double x, double y)
	{
		add(new MoveTo(x, y));
	}
	
	
	public void lineto(double x, double y)
	{
		add(new LineTo(x, y));
	}
	
	
	public List<PathElement> getPath()
	{
		return path;
	}
	
	
	public void add(PathElement em)
	{
		path.add(em);
	}
	
	
	public void addAll(PathElement[] elements)
	{
		path.addAll(elements);
	}
}
