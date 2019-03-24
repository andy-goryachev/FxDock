// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.FX;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;


/**
 * FxEditor Mouse Handler.
 */
public class FxEditorMouseHandler
{
	protected final FxEditor editor;
	protected final SelectionController selector;
	protected final Timeline autoScrollTimer;
	private boolean fastAutoScroll;
	private Duration autoScrollPeriod = Duration.millis(100); // arbitrary number
	private double fastScrollThreshold = 100; // arbitrary number
	private double autoScrollStepFast = 200; // arbitrary
	private double autoScrollStepSlow = 20; // arbitrary
	private boolean autoScrollUp;
	private double scrollWheelStepSize = 0.1;


	public FxEditorMouseHandler(FxEditor ed, SelectionController sel)
	{
		this.editor = ed;
		this.selector = sel;
		
		autoScrollTimer = new Timeline(new KeyFrame(autoScrollPeriod, (ev) -> autoScroll()));
		autoScrollTimer.setCycleCount(Timeline.INDEFINITE);
	}
	
	
	protected void handleScroll(ScrollEvent ev)
	{
		if(ev.isShiftDown())
		{
			// TODO horizontal scroll perhaps?
			D.print("horizontal scroll", ev.getDeltaX());
		}
		else if(ev.isShortcutDown())
		{
			// page up / page down
			if(ev.getDeltaY() >= 0)
			{
				editor.pageUp();
			}
			else
			{
				editor.pageDown();
			}
		}
		else
		{
			// vertical block scroll
			double frac = scrollWheelStepSize * (ev.getDeltaY() >= 0 ? -1 : 1); 
			editor.scroll(frac); 
		}
	}
	
	
	protected Marker getTextPos(MouseEvent ev)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		return editor.getTextPos(x, y);
	}
	
	
	public void handleMouseClicked(MouseEvent ev)
	{
		if(ev.getButton() != MouseButton.PRIMARY)
		{
			return;
		}
		
		int clicks = ev.getClickCount();
		switch(clicks)
		{
		case 2:
			editor.selectWord(getTextPos(ev));
			break;
		case 3:
			editor.selectLine(getTextPos(ev));
			ev.consume();
			break;
		}
	}
	
	
	public void handleMousePressed(MouseEvent ev)
	{
		// not sure - perhaps only ignore if the mouse press is within a selection
		// and reset selection if outside?
		if(FX.isPopupTrigger(ev))
		{
			return;
		}

		Marker pos = getTextPos(ev);
		editor.setSuppressBlink(true);
		
		if(ev.isShiftDown())
		{
			// expand selection from the anchor point to the current position
			// clearing existing (possibly multiple) selection
			selector.clearAndExtendLastSegment(pos);
		}
		else if(ev.isShortcutDown())
		{
			if(selector.isInsideOfSelection(pos) || (!editor.isMultipleSelectionEnabled()))
			{
				selector.setAnchor(pos);
				selector.setSelection(pos);
			}
			else
			{
				// add a new caret
				selector.setAnchor(pos);
				selector.addSelectionSegment(pos, pos);
			}
		}
		else
		{
			editor.clearSelection();
			selector.addSelectionSegment(pos, pos);
			selector.setAnchor(pos);
		}
		
		editor.requestFocus();
	}
	

	public void handleMouseDragged(MouseEvent ev)
	{
		if(!FX.isLeftButton(ev))
		{
			return;
		}
		
		double y = ev.getY();
		if(y < 0)
		{
			// above vflow
			autoScroll(y);
			return;
		}
		else if(y > editor.vflow.getHeight())
		{
			// below vflow
			autoScroll(y - editor.vflow.getHeight());
			return;
		}
		else
		{
			stopAutoScroll();
		}
		
		Marker pos = getTextPos(ev);
		selector.extendLastSegment(pos);
	}
	
	
	public void handleMouseReleased(MouseEvent ev)
	{
		stopAutoScroll();
		editor.setSuppressBlink(false);
		selector.commitSelection();
	}
	
	
	protected void autoScroll(double delta)
	{
		autoScrollUp = delta < 0;
		fastAutoScroll = Math.abs(delta) > fastScrollThreshold;
		autoScrollTimer.play();
	}
	
	
	protected void stopAutoScroll()
	{
		autoScrollTimer.stop();
	}
	
	
	protected void autoScroll()
	{
		double delta = fastAutoScroll ? autoScrollStepFast : autoScrollStepSlow;
		if(autoScrollUp)
		{
			delta = -delta;
		}
		editor.blockScroll(delta);
		
		Point2D p;
		if(autoScrollUp)
		{
			p = editor.vflow.localToScreen(0, 0);
		}
		else
		{
			p = editor.vflow.localToScreen(0, editor.vflow.getHeight());
		}
		
		// TODO this could be done on mouse released!
		editor.scrollToVisible(p);
		
		Marker pos = editor.getTextPos(p.getX(), p.getY());
		selector.extendLastSegment(pos);
	}
}