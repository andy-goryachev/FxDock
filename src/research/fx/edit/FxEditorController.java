// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FX;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.PathElement;


/**
 * FxEditor Controller.
 */
public class FxEditorController
	implements FxEditorModel.Listener
{
	public final FxEditorSelectionModel selection;
	protected final FxEditor editor;
	protected ScrollBar vscroll;
	protected ScrollBar hscroll;


	public FxEditorController(FxEditor ed)
	{
		this.editor = ed;
		
		selection = new FxEditorSelectionModel();
		
		ed.getChildren().addAll(selection.highlight, vscroll(), selection.caret);
		
		ed.addEventFilter(KeyEvent.ANY, (ev) -> handleKeyEvent(ev));
		ed.addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> handleMousePressed(ev));
		ed.addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> handleMouseReleased(ev));
		ed.addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> handleMouseDragged(ev));
	}
	
	
	protected ScrollBar vscroll()
	{
		if(vscroll == null)
		{
			vscroll = createVScrollBar();
		}
		return vscroll;
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.VERTICAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setAbsolutePosition(val.doubleValue()));
		return s;
	}
	
	
	public FxEditorSelectionModel selection()
	{
		return selection;
	}


	public void eventLinesDeleted(int start, int count)
	{
	}


	public void eventLinesInserted(int start, int count)
	{
	}


	public void eventLinesModified(int start, int count)
	{
	}
	
	
	public void setAbsolutePosition(double pos)
	{
		// TODO account for visible line count
		int start = FX.round(editor.getModel().getLineCount() * pos);
		editor.setStartIndex(start);
	}
	
	
	protected void handleKeyEvent(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		if(ev.isShiftDown())
		{
			
		}
		else
		{
//			TextPos p = editor.getTextPos(ev.getScreenX(), ev.getScreenY());
//			if(p != null)
//			{
//				selection.clear();
//				selection.setCaret(p);
//			}
			
			PathElement[] p = editor.getCaretShape(ev.getScreenX(), ev.getScreenY());
			if(p != null)
			{
				selection.clear();
				selection.setCaret(p);
			}
		}
	}
	
	
	protected void handleMouseReleased(MouseEvent ev)
	{
		// TODO
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		// TODO
	}
}