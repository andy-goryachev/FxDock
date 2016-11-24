// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.CssStyle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;


/**
 * Fx Text Editor.
 */
public class FxEditor
	extends Pane
{
	/** caret style */
	public static final CssStyle CARET = new CssStyle("FxEditor_CARET");
	/** selection highlight */
	public static final CssStyle HIGHLIGHT = new CssStyle("FxEditor_HIGHLIGHT");
	
	private ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	private ReadOnlyObjectWrapper<Boolean> wrap = new ReadOnlyObjectWrapper<>();
	private ReadOnlyObjectWrapper<Boolean> singleSelection = new ReadOnlyObjectWrapper<>();
	// TODO editable
	// TODO multiple selection
	// TODO caret visible
	// TODO caret blink rate
	// TODO line decorations/line numbers
	private FxEditorController control = new FxEditorController(this);
	private FxEditorLayout layout;
	private int offsetx;
	private int offsety;
	private int startIndex;
	
	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setFocusTraversable(true);
		setModel(m);
	}
	
	
	public void setModel(FxEditorModel m)
	{
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(control);
		}
		
		model.set(m);
		
		if(m != null)
		{
			m.addListener(control);
		}
		
		requestLayout();
	}
	
	
	public FxEditorModel getModel()
	{
		return model.get();
	}
	
	
	public ReadOnlyObjectProperty<FxEditorModel> modelProperty()
	{
		return model.getReadOnlyProperty();
	}
	
	
	protected void layoutChildren()
	{
		layout = updateLayout(layout);
	}
	
	
	public void setStartIndex(int x)
	{
		startIndex = x;
		requestLayout();
	}
	
	
	protected FxEditorLayout updateLayout(FxEditorLayout prev)
	{
		if(prev != null)
		{
			prev.removeFrom(getChildren());
		}
		
		double width = getWidth();
		double height = getHeight();

		// position the scrollbar(s)
		ScrollBar vscroll = control.vscroll();
		if(vscroll.isVisible())
		{
			double w = vscroll.prefWidth(-1);
			layoutInArea(vscroll, width - w, 0, w, height, 0, null, true, true, HPos.LEFT, VPos.TOP);
		}
		
		// TODO is loaded?
		FxEditorModel m = getModel();
		int lines = m.getLineCount();
		FxEditorLayout la = new FxEditorLayout(startIndex, offsety);
		
		double y = 0;
		
		for(int ix=startIndex; ix<lines; ix++)
		{
			Region n = m.getDecoratedLine(ix);
			n.setManaged(true);
			
			double w = n.prefWidth(-1);
			double h = n.prefHeight(w);
			
			LineBox b = new LineBox(ix, n);
			la.add(b);
			
			layoutInArea(n, 0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > height)
			{
				break;
			}
		}
		
		la.addTo(getChildren());
		
		return la;
	}
	
	
	/** returns text position at the specified screen coordinates */
	public TextPos getTextPos(double screenx, double screeny)
	{
		return layout.getTextPos(screenx, screeny);
	}
	
	
	/** returns caret shape at the specified screen coordinates */
	public PathElement[] getCaretShape(double screenx, double screeny)
	{
		return layout.getCaretShape(this, screenx, screeny);
	}
	
	
	protected int getOffsetX()
	{
		return offsetx;
	}
	
	
	protected int getOffsetY()
	{
		return offsety;
	}
	
	
	protected int getViewStartLine()
	{
		return layout.startLine();
	}
}
