// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;


/**
 * Fx Icon Builder.
 * 
 * Enables programmatic creation of icons.
 */
public class FxIconBuilder
{
	private final double width;
	private final double height;
	private final CList<Node> elements;
	private double xcenter;
	private double ycenter;
	private double strokeWidth = 1.0;
	private Paint strokeColor;
	private StrokeType strokeType;
	private StrokeLineCap lineCap;
	private StrokeLineJoin lineJoin;
	private double miterLimit;
	private double dashOffset;
	private FillRule fillRule;	
	private Effect effect;
	private Color fill;
	private Path path;
	
	
	public FxIconBuilder(double width, double height, double xcenter, double ycenter)
	{
		this(width, height);
		setCenter(xcenter, ycenter);
	}


	public FxIconBuilder(double width, double height)
	{
		this.width = width;
		this.height = height;
		this.elements = new CList<>();
	}
	
	
	public FxIconBuilder(double size)
	{
		this(size, size);
	}
	
	
	/** sets the builder's center point */
	public void setCenter(double xcenter, double ycenter)
	{
		this.xcenter = xcenter;
		this.ycenter = ycenter;
	}
	
	
	/** creates new path segment */
	public Path newPath()
	{
		path = new Path();
		path.setEffect(effect);
		path.setFill(fill);
		path.setFillRule(fillRule);
		path.setStroke(strokeColor);
		path.setStrokeDashOffset(dashOffset);
		path.setStrokeLineCap(lineCap);
		path.setStrokeLineJoin(lineJoin);
		path.setStrokeMiterLimit(miterLimit);
		path.setStrokeType(strokeType);
		path.setStrokeWidth(strokeWidth);
		
		elements.add(path);
		return path;
	}
	
	
	public void setStrokeWidth(double w)
	{
		strokeWidth = w;
		
		if(path != null)
		{
			path.setStrokeWidth(w);
		}
	}
	
	
	public void setStrokeColor(Paint c)
	{
		strokeColor = c;
		
		if(path != null)
		{
			path.setStroke(c);
		}
	}
	
	
	protected void add(PathElement em)
	{
		if(path == null)
		{
			path = newPath();
		}
		path.getElements().add(em);
	}
	
	
	protected Point2D currentPos()
	{
		if(path == null)
		{
			return new Point2D(xcenter, ycenter);
		}
		
		ObservableList<PathElement> es = path.getElements();
		int sz = es.size();
		if(sz == 0)
		{
			return new Point2D(xcenter, ycenter);
		}
		
		PathElement em = es.get(sz - 1);
		if(em instanceof LineTo)
		{
			LineTo p = (LineTo)em;
			return new Point2D(p.getX(), p.getY());
		}
		else if(em instanceof MoveTo)
		{
			MoveTo p = (MoveTo)em;
			return new Point2D(p.getX(), p.getY());
		}
		else if(em instanceof ArcTo)
		{
			ArcTo p = (ArcTo)em;
			return new Point2D(p.getX(), p.getY());
		}
		else if(em instanceof CubicCurveTo)
		{
			CubicCurveTo p = (CubicCurveTo)em;
			return new Point2D(p.getX(), p.getY());
		}
//		else if(em instanceof HLineTo)
//		{
//			// TODO back up one element
//			HLineTo p = (HLineTo)em;
//			return new Point2D(p.getX(), p.getY());
//		}
		else if(em instanceof QuadCurveTo)
		{
			QuadCurveTo p = (QuadCurveTo)em;
			return new Point2D(p.getX(), p.getY());
		}
//		else if(em instanceof VLineTo)
//		{
//			// TODO back up one element
//			VLineTo p = (VLineTo)em;
//			return new Point2D(p.getX(), p.getY());
//		}
		else
		{
			throw new Error("?" + em);
		}
	}
	
	
	/** move to absolute coordinates */
	public void moveTo(double x, double y)
	{
		add(new MoveTo(x + xcenter, y + ycenter));
	}
	
	
	/** move to absolute coordinates */
	public void moveRel(double dx, double dy)
	{
		Point2D p = currentPos();
		add(new MoveTo(dx + p.getX(), dy + p.getY()));
	}
	
	
	/** line to absolute coordinates */
	public void lineTo(double x, double y)
	{
		add(new LineTo(x + xcenter, y + ycenter));
	}
	
	
	/** line to absolute coordinates */
	public void lineRel(double dx, double dy)
	{
		Point2D p = currentPos();
		add(new LineTo(dx + p.getX(), dy + p.getY()));
	}
	
	
	/** returns a new instance of the generated icon */
	public IconBase getIcon()
	{
		IconBase ic = new IconBase(width, height);
		ic.addAll(elements);
		return ic;
	}
}
