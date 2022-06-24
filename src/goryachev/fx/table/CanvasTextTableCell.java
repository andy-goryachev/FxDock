// Copyright © 2021-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.fx.CPane;
import goryachev.fx.IStyledText;
import goryachev.fx.util.TextPainter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Font;


/**
 * Canvas-based Table Cell capable of rendering plain text and IStyledText
 * inside of FxTextTable.
 */
public class CanvasTextTableCell
	extends CPane
{
	protected final TextPainter painter = new TextPainter();
	protected final Object value;
	protected final HPos alignment;
	private final ObjectBinding binding;
	
	
	public CanvasTextTableCell(FxTextTable table, Object value, HPos alignment)
	{
		this.value = value;
		this.alignment = alignment;

		// using object binding callback to create and paint the canvas 
		binding = Bindings.createObjectBinding
		(
			() ->
			{
				Font f = table.getFont();
				return updateCanvas(f);	
			}, 
			table.fontProperty(), 
			widthProperty(), 
			heightProperty()
		);
		binding.addListener((c,p,v) -> 
		{
			// need this listener for the binding to fire,
			// even though the actual processing is done in updateCanvas() 
		});
		
		Font f = table.getFont();
		updateCanvas(f);
	}
	
	
	protected double computePrefHeight(double width)
	{
		return 10;
	}


	protected double computePrefWidth(double height)
	{
		double w;
		Canvas c = painter.getCanvas();
		if(c == null)
		{
			w = 0.0; 
		}
		else
		{
			w = c.getWidth();
		}
		
		Insets m = getInsets();
		return m.getLeft() + w + m.getRight();
	}
	
	
	protected void layoutChildren()
	{
		super.layoutChildren();
	}
	
	
	protected Object updateCanvas(Font f)
	{
		Canvas canvas = painter.createCanvas(this);
		setCenter(canvas);
		
		painter.clear();
		painter.setFont(f);
		
		if(value instanceof IStyledText)
		{
			painter.paint((IStyledText)value, alignment);
		}
		else
		{
			if(value == null)
			{
				// TODO paint background?
			}
			else
			{
				String text = value.toString();
				painter.paint(text, alignment);
			}
		}
		
		// will be ignored
		return canvas;
	}
}
