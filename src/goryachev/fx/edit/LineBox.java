// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.HasText;
import goryachev.common.util.SB;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.internal.CssTools;
import goryachev.fx.util.FxPathBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;


/**
 * Represents a box enclosing a logical line of text.
 * Typically, it contains a CTextFlow which may be rendered on several rows in the view,
 * or it may contain a single Region representing a non-text component.
 */
public class LineBox
	implements HasText
{
	private int lineNumber;
	private Labeled lineNumberComponent;
	private final Region center;
	private double height;
	private double y;
	private static Insets LINE_NUMBERS_PADDING = new Insets(0, 7, 0, 0);
	
	
	/** creates a text flow based line box */
	public LineBox()
	{
		this(new CTextFlow());
	}
	
	
	/** creates a full width line box with the specified component */
	public LineBox(Region center)
	{
		this.center = center;
	}
	
	
	public String getText()
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow f = (CTextFlow)center;
			return f.getText();
		}
		else if(center instanceof HasText)
		{
			return ((HasText)center).getText();
		}
		return null;
	}
	
	
	public int getTextLength()
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)center;
			return t.getText().length();
		}
		else if(center instanceof HasText)
		{
			String s = ((HasText)center).getText();
			if(s != null)
			{
				return s.length();
			}
		}
		return 0;
	}
	
	
	public String toString()
	{
		return "LineBox:" + lineNumber;
	}
	

	public Region getCenter()
	{
		return center;
	}
	
	
	public void init(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}
	
	
	public int getLineNumber()
	{
		return lineNumber;
	}
	
	
	public double getY()
	{
		return y;
	}
	
	
	public void setY(double y)
	{
		this.y = y;
	}


	public void setHeight(double h)
	{
		height = h;
	}
	
	
	public double getHeight()
	{
		return height;
	}
	
	
	/** returns selection shape for a given range */
	public PathElement[] getRange(int start, int end)
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)center;
			return t.getRange(start, end);
		}
		else
		{
			double w = center.getWidth();
			double h = center.getHeight();
			
			return new PathElement[]
			{
				new MoveTo(0, 0),
				new LineTo(w, 0),
				new LineTo(w, h),
				new LineTo(0, h),
				new LineTo(0, 0)
			};
		}
	}
	
	
	/** returns selection shape for a given range */
	public PathElement[] getCaretShape(int index, boolean leading)
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)center;
			return t.getCaretShape(index, leading);
		}
		else
		{
			double x = leading ? 0 : center.getWidth();
			double h = center.getHeight();
			
			return new PathElement[]
			{
				new MoveTo(x, 0),
				new LineTo(x, h)
			};
		}
	}
	
	
	/** returns the text flow node, creating it as necessary */
	public CTextFlow text()
	{
		if(center instanceof CTextFlow)
		{
			return (CTextFlow)center;
		}
		else
		{
			throw new Error("not a CTextFlow: " + center);
		}
	}
	
	
	public LineBox addText(Text t)
	{
		text().getChildren().add(t);
		return this;
	}
	
	
	public LineBox addText(Text ... items)
	{
		text().getChildren().addAll(items);
		return this;
	}
	

	public void setLineNumberComponent(Labeled c)
	{
		lineNumberComponent = c;
	}


	public Labeled getLineNumberComponent()
	{
		if(lineNumberComponent == null)
		{
			lineNumberComponent = createLineNumberComponent();
		}
		return lineNumberComponent;
	}
	
	
	public Labeled getLineNumberComponentRaw()
	{
		return lineNumberComponent;
	}


	protected Labeled createLineNumberComponent()
	{
		return FX.label(FxEditor.LINE_NUMBER, Color.LIGHTGRAY, LINE_NUMBERS_PADDING,  FxCtl.FORCE_MIN_WIDTH);
	}


	public void addBoxOutline(FxPathBuilder b, double w)
	{
		double y0 = center.getLayoutY();
		double y1 = y0 + center.getHeight();
		
		b.moveto(0, y0);
		b.lineto(w, y0);
		b.lineto(w, y1);
		b.lineto(0, y1);
		b.lineto(0, y0);
	}
	
	
	public void addText(TStyle s, String text)
	{
		Text t = constructText(s, text);
		text().getChildren().add(t);
	}
	

	protected Text constructText(TStyle st, String text)
	{
		Text t = new Text(text);
		
		if(st != null)
		{
			String css = createCss(st);
			if(css != null)
			{
				t.setStyle(css);
			}
			
			String style = st.getStyle(); 
			if(style != null)
			{
				t.getStyleClass().add(style);
			}
		}
		
		return t;
	}
	
	
	protected String createCss(TStyle s)
	{
		SB sb = new SB();
		
		if(s.isBold())
		{
			// FIX does not work on Mac!!
			sb.append("-fx-font-weight:bold;");
		}
		
		if(s.isItalic())
		{
			// FIX does not work on Windows!!
			sb.append("-fx-font-style:italic;");
		}
		
		if(s.isStrikeThrough())
		{
			sb.append("-fx-strikethrough:true;");
		}
		
		// these are not easily supported in javafx
//		if(s.isSubScript())
//		{
//			// TODO use scaling + border? 
//		}
//		if(s.isSuperScript())
//		{
//			// TODO use scaling + border? 
//		}
		
		if(s.isUnderline())
		{
			sb.append("-fx-underline:true;");
		}
		
		if(s.getForeground() != null)
		{
			sb.append("-fx-fill:").append(CssTools.toColor(s.getForeground())).append(";");
		}
		
//		if(s.getBackground() != null)
//		{
//			// TODO perhaps add a shape in the shape of text run
//			sb.append("-fx-background-color:").append(CssTools.toColor(s.getBackground())).append(";");
//		}

		return sb.toString();
	}
}