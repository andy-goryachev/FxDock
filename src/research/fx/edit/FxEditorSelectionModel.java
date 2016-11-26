// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FX;
import goryachev.fx.FxInvalidationListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
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
		highlight.setFill(Color.YELLOW);
		
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
}
