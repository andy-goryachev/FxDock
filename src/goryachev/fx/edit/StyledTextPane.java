// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.hacks.FxHacks;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;


/**
 * A styleable TextArea.
 */
public class StyledTextPane
	extends StackPane
{
	public static final CssStyle CARET = new CssStyle("StyledTextPane_CARET");
	
	protected final CTextFlow textField;
	protected final Path caret;
	protected final Timeline caretTimeline;
	protected final ReadOnlyIntegerWrapper selectionIndex = new ReadOnlyIntegerWrapper(-1);


	public StyledTextPane()
	{
		textField = new CTextFlow();
		
		caret = new Path();
		FX.style(caret, CARET);
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);
		
		getChildren().add(textField);
		
		caretTimeline = new Timeline();
		caretTimeline.setCycleCount(Animation.INDEFINITE);
		// TODO property
		updateBlinkRate(Duration.millis(500));
		
		// FIX allow custom handlers
		new StyledTextPaneMouseController(this);
	}
	
	
	// TODO blinkRate property
	protected void updateBlinkRate(Duration d)
	{
		Duration period = d.multiply(2);
		
		caretTimeline.stop();
		caretTimeline.getKeyFrames().setAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(d, (ev) -> setCaretVisible(false)),
			new KeyFrame(period)
		);
		caretTimeline.play();
	}


	public PathElement[] getCaretShape(int index, boolean leading)
	{
		return FxHacks.get().getCaretShape(textField, index, leading);
	}


	public PathElement[] getRange(int start, int end)
	{
		return FxHacks.get().getRange(textField, start, end);
	}


	/** returns text position at the specified screen coordinates */
	public int getTextPos(double x, double y)
	{
		Point2D p = textField.screenToLocal(x, y);
		return FxHacks.get().getTextPos(textField, p.getX(), p.getY());
	}

	
	public void setSelection(int ix)
	{
		if(ix < 0)
		{
			caret.getElements().clear();
			setCaretVisible(false);
		}
		else
		{
			if(caret.getParent() == null)
			{
				textField.getChildren().add(caret);
			}
			
			PathElement[] es = getCaretShape(ix, true);
			caret.getElements().setAll(es);
			setCaretVisible(true);
		}
		
		selectionIndex.set(ix);
	}
	
	
	public void setCaretVisible(boolean on)
	{
		caret.setVisible(on);
	}
	
	
	// TODO replace with selection span
	public ReadOnlyIntegerProperty selectionIndexProperty()
	{
		return selectionIndex.getReadOnlyProperty();
	}


	// FIX replace with styled segments
	public void setText(CList<Node> textNodes)
	{
		textField.getChildren().setAll(textNodes);
		setSelection(0);
	}


	public String getText()
	{
		return textField.getText();
	}
}
