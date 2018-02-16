// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;


/**
 * Mouse Controller.
 */
public class StyledTextPaneMouseController
{
	protected final StyledTextPane editor;


	public StyledTextPaneMouseController(StyledTextPane p)
	{
		this.editor = p;
		
		TextFlow ed = p.textField;
		ed.addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> handleKeyPressed(ev));
		ed.addEventFilter(KeyEvent.KEY_RELEASED, (ev) -> handleKeyReleased(ev));
		ed.addEventFilter(KeyEvent.KEY_TYPED, (ev) -> handleKeyTyped(ev));
		ed.addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> handleMousePressed(ev));
		ed.addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> handleMouseReleased(ev));
		ed.addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> handleMouseDragged(ev));
	}
	

	protected void handleKeyPressed(KeyEvent ev)
	{
		// TODO
//		switch(ev.getCode())
//		{
//		case PAGE_DOWN:
//		}
	}
	
	
	protected void handleKeyReleased(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		// FIX problem: misses mouse clicks on the empty margin
		// when a non-zero padding is set on styled pane
		int ix = editor.getTextPos(ev.getScreenX(), ev.getScreenY());
		editor.setSelection(ix);
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
	}
	
	
	protected void handleMouseReleased(MouseEvent ev)
	{
	}
}