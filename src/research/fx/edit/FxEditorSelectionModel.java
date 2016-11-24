// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FX;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;


/**
 * FxEditor Selection and Caret Model.
 */
public class FxEditorSelectionModel
{
	// TODO move to FxCaret?
	protected final Path caret;
	protected final Timeline caretTimeline;
	protected final Path highlight;
	
	
	public FxEditorSelectionModel()
	{
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
		caretTimeline.getKeyFrames().addAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(Duration.millis(500), (ev) -> setCaretVisible(false)),
			new KeyFrame(Duration.millis(1000))
		);
		caretTimeline.play();
	}
	
	
	protected void setCaretVisible(boolean on)
	{
		caret.setVisible(on);
	}
	
	
	public void clear()
	{
		caret.getElements().clear();
		highlight.getElements().clear();
	}


	public void setCaret(PathElement[] es)
	{
		// reset caret so it's always on when moving, unlike MS Word
		caretTimeline.stop();
		caret.getElements().setAll(es);
		caretTimeline.play();
	}
}
