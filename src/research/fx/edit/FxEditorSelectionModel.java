// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import goryachev.fx.FX;
import goryachev.fx.FxInvalidationListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;


/**
 * FxEditor Selection and Caret Model.
 */
public class FxEditorSelectionModel
{
	protected final FxEditor editor;
	protected final Path caret;
	protected final Timeline caretTimeline;
	protected final Path highlight;
	protected final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	private TextPos anchor;
	
	
	public FxEditorSelectionModel(FxEditor ed)
	{
		this.editor = ed;
		
		highlight = new Path();
		FX.style(highlight, FxEditor.HIGHLIGHT);
		highlight.setManaged(false);
		highlight.setStroke(null);
		highlight.setFill(Color.rgb(255, 255, 0, 0.25));
		
		caret = new Path();
		FX.style(caret, FxEditor.CARET);
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);
		
		caretTimeline = new Timeline();
		caretTimeline.setCycleCount(Animation.INDEFINITE);
		new FxInvalidationListener(ed.blinkRateProperty(), true, () -> updateBlinkRate(ed.getBlinkRate()));

	}
	
	
	protected void setCaretVisible(boolean on)
	{
		caret.setVisible(on);
	}
	
	
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
	
	
	public void clear()
	{
		caret.getElements().clear();
		highlight.getElements().clear();
		segments.clear();
	}


	public void setCaretElements(PathElement[] es)
	{
		// reset caret so it's always on when moving, unlike MS Word
		caretTimeline.stop();
		caret.getElements().setAll(es);
		caretTimeline.play();
	}


	public void setAnchor(TextPos pos)
	{
		anchor = pos;
	}

	
	public TextPos getAnchor()
	{
		if(anchor == null)
		{
			anchor = new TextPos(0, 0, true);
		}
		return anchor;
	}
	
	
	public boolean isInsideSelection(TextPos pos)
	{
		for(SelectionSegment s: segments)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	

	/** adds a new segment from start to end */
	public void addSegment(TextPos start, TextPos end)
	{
		SelectionSegment s = new SelectionSegment(start, end);
		segments.add(s);
		highlight.getElements().addAll(createHighlight(s));
	}
	
	
	protected double right()
	{
		// FIX padding
		return editor.getWidth();
	}
	
	
	protected double left()
	{
		// FIX padding
		return 0.0;
	}
	
	
	protected boolean isNearlySame(double a, double b)
	{
		// in case for some reason floating point computation result is slightly off
		return Math.abs(a - b) < 0.01;
	}
	
	
	protected CList<PathElement> createHighlight(SelectionSegment s)
	{
		TextPos start = s.getStart();
		TextPos end = s.getEnd();
		
		if(start.compareTo(end) > 0)
		{
			start = end;
			end = s.getStart();
		}
		
		CaretLocation top = editor.getCaretLocation(start);
		CaretLocation bot = editor.getCaretLocation(end);
		
		CList<PathElement> rv = new CList<>();
		rv.add(new MoveTo(top.x0, top.y0));
		if(isNearlySame(top.y0, bot.y0))
		{
			rv.add(new LineTo(bot.x0, top.y0));
			rv.add(new LineTo(bot.x0, bot.y1));
			rv.add(new LineTo(top.x0, bot.y1));
			rv.add(new ClosePath());
		}
		else
		{
			rv.add(new LineTo(right(), top.y0));
			rv.add(new LineTo(right(), bot.y0));
			rv.add(new LineTo(bot.x0, bot.y0));
			rv.add(new LineTo(bot.x0, bot.y1));
			rv.add(new LineTo(left(), bot.y1));
			rv.add(new LineTo(left(), top.y1));
			rv.add(new LineTo(top.x0, top.y1));
			rv.add(new ClosePath());
		}
		
		return rv;
	}
}
