// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
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
		
		ed.addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> handleKeyPressed(ev));
		ed.addEventFilter(KeyEvent.KEY_RELEASED, (ev) -> handleKeyReleased(ev));
		ed.addEventFilter(KeyEvent.KEY_TYPED, (ev) -> handleKeyTyped(ev));
		
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
	
	
	public void scrollRelative(double pixels)
	{
		if(pixels < 0)
		{
			double toScroll = pixels;
			int ix = editor.getViewStartLine();
			int offsety = editor.getOffsetY();
			
			// TODO
			// using the current layout, add lines until scrolled up to the necessary number of pixels
			// or the first/last line
			while(toScroll > 0)
			{
				if(ix <= 0)
				{
					break;
				}
			}
		}
		else
		{
			
		}
	}


	protected void handleKeyPressed(KeyEvent ev)
	{
		switch(ev.getCode())
		{
		case PAGE_DOWN:
			scrollRelative(editor.getHeight());
			break;
		case LEFT:
			// TODO
			break;
		case PAGE_UP:
			scrollRelative(-editor.getHeight());
			break;
		case RIGHT:
			// TODO
			break;
		}
	}
	
	
	protected void handleKeyReleased(KeyEvent ev)
	{
		// TODO
		D.print(ev);
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		// TODO
		D.print(ev);
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		if(ev.isShiftDown())
		{
			// TODO multiple selection
		}
		else
		{
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