// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FX;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;


/**
 * FxEditor Controller.
 */
public class FxEditorController
	implements FxEditorModel.Listener
{
	protected final FxEditor editor;
	protected ScrollBar vscroll;
	protected ScrollBar hscroll;


	public FxEditorController(FxEditor ed)
	{
		this.editor = ed;
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
}