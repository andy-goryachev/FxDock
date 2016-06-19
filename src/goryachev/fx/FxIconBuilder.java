// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Region;
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
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;


/**
 * Fx Icon Builder.
 * 
 * Enables programmatic creation of icons.
 * 
 * Coordinate system is similar to SVG one, its (0,0) origin is located at the upper left corner,
 * unless modified by setOrigin().
 * 
 * Angles are measured in radians.
 * For rotations, positive angle corresponds to clockwise direction, negative - to counter-clockwise.
 * 
 * Stroke and fill parameters are set at the moment of creation of a stroke or a shape.  Subsequent setting
 * of those parameters has no effect on existing elements.
 */
public class FxIconBuilder
{
	private final double width;
	private final double height;
	private final CList<Node> elements;
	private double xorigin;
	private double yorigin;
	private double scale = 1.0;
	private double xtranslate;
	private double ytranslate;
	private double strokeWidth = 1.0;
	private Paint fill = Color.BLACK;
	private Paint strokeColor = Color.BLACK;
	private StrokeType strokeType = StrokeType.CENTERED;
	private StrokeLineCap lineCap = StrokeLineCap.ROUND;
	private StrokeLineJoin lineJoin = StrokeLineJoin.ROUND;
	private double miterLimit;
	private double dashOffset;
	private FillRule fillRule;	
	private Effect effect;
	private Path path;
	
	
	public FxIconBuilder(double width, double height, double xcenter, double ycenter)
	{
		this(width, height);
		setOrigin(xcenter, ycenter);
	}
	
	
	public FxIconBuilder(double size, double xcenter, double ycenter)
	{
		this(size);
		setOrigin(xcenter, ycenter);
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
	
	
	/** sets the builder's origin point (normally, the origin is located at the upper left corner). */
	public void setOrigin(double xcenter, double ycenter)
	{
		this.xorigin = xcenter;
		this.yorigin = ycenter;
	}
	
	
	/** sets fill color */
	public void setFill(Paint c)
	{
		fill = c;
	}
	
	
	/** creates a full size rectangle filled with the current fill color */
	public void fill()
	{
		fill(-xorigin, -yorigin, width, height);
	}
	
	
	/** creates a rectangle filled with the current fill color */
	public void fill(double x, double y, double w, double h)
	{
		Region r = new Region();
		r.setManaged(false);
		r.resizeRelocate(x + xorigin, y + yorigin, w, h);
		r.setBackground(FX.background(fill));
		
		elements.add(r);
	}
	
	
	/** creates new path segment */
	public Path newPath()
	{
		path = new Path();
		path.setScaleX(scale);
		path.setScaleY(scale);
		path.setTranslateX(xtranslate);
		path.setTranslateY(ytranslate);
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
	
	
	public SVGPath svgPath(String svg)
	{
		SVGPath p = new SVGPath();
		
		p.setScaleX(scale);
		p.setScaleY(scale);
		p.setTranslateX(xtranslate);
		p.setTranslateY(ytranslate);
		p.setEffect(effect);
		p.setFill(fill);
		p.setFillRule(fillRule);
		p.setStroke(strokeColor);
		p.setStrokeDashOffset(dashOffset);
		p.setStrokeLineCap(lineCap);
		p.setStrokeLineJoin(lineJoin);
		p.setStrokeMiterLimit(miterLimit);
		p.setStrokeType(strokeType);
		p.setStrokeWidth(strokeWidth);

		p.setContent(svg);
		
		Label r = new Label();
		r.setGraphic(p);
		elements.add(r);
		
		return p;
	}
	
	
	public void setScale(double x)
	{
		scale = x;
	}
	
	
	public void setTranslate(double dx, double dy)
	{
		xtranslate = dx;
		ytranslate = dy;
	}
	
	
	public void setStrokeWidth(double w)
	{
		strokeWidth = w;
	}
	
	
	public void setStrokeColor(Paint x)
	{
		strokeColor = x;
	}
	
	
	public void setStrokeLineCap(StrokeLineCap x)
	{
		lineCap = x;
	}
	
	
	public void setStrokeLineJoin(StrokeLineJoin x)
	{
		lineJoin = x;
	}
	
	
	public void setStrokeMiterLimit(double x)
	{
		miterLimit = x;
	}
	
	
	public void setEffect(Effect x)
	{
		effect = x;
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
			return new Point2D(xorigin, yorigin);
		}
		
		ObservableList<PathElement> es = path.getElements();
		int sz = es.size();
		if(sz == 0)
		{
			return new Point2D(xorigin, yorigin);
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
		add(new MoveTo(x + xorigin, y + yorigin));
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
		add(new LineTo(x + xorigin, y + yorigin));
	}
	
	
	/** line to absolute coordinates */
	public void lineRel(double dx, double dy)
	{
		Point2D p = currentPos();
		add(new LineTo(dx + p.getX(), dy + p.getY()));
	}
	
	
	/** arc from current position, using the specified center coordinates, radius, and angle */
	public void arcRel(double xc, double yc, double radius, double angle)
	{
		// arcTo seems to fail if sweep angle is greater than 360
		if(angle >= FX.TWO_PI)
		{
			angle = FX.TWO_PI - 0.0000001;
		}
		else if(angle <= -FX.TWO_PI)
		{
			angle = - FX.TWO_PI + 0.0000001;
		}
		
		Point2D p = currentPos();
		
		double a = Math.atan2(yc + yorigin - p.getY(), p.getX() - xc - xorigin);
		double b = a - angle;
		double xe = xorigin + xc + radius * Math.cos(b);
		double ye = yorigin - yc - radius * Math.sin(b);

		// arcTo sweep is explained here: 
		// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/shape/ArcTo.html
		boolean large = (angle >= Math.PI);
		boolean sweep = (angle > 0);
		
		add(new ArcTo(radius, radius, 0, xe, ye, large, sweep));
	}
	
	
	/** returns a new instance of the generated icon */
	public IconBase getIcon()
	{
		IconBase ic = new IconBase(width, height);
		ic.addAll(elements);
		return ic;
	}
}
