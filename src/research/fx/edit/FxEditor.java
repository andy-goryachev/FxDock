// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * Fx Text Editor.
 */
public class FxEditor
	extends Pane
{
	private ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	private ReadOnlyObjectWrapper<Boolean> wrap = new ReadOnlyObjectWrapper<>();
	private Handler handler = new Handler();
	private ScrollBar vscroll;
	private ScrollBar hscroll;
	private int offsetx;
	private int offsety;
	private int startIndex;
	private FxEditorLayout layout;
	
	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		// TODO move to handler
		vscroll = new ScrollBar();
		vscroll.setOrientation(Orientation.VERTICAL);
		vscroll.setManaged(true);
		vscroll.setMin(0.0);
		vscroll.setMax(1.0);
		vscroll.valueProperty().addListener((s,p,val) -> handler.setAbsolutePosition(val.doubleValue()));
		getChildren().add(vscroll);
		
		setModel(m);
	}
	
	
	public void setModel(FxEditorModel m)
	{
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(handler);
		}
		
		model.set(m);
		
		if(m != null)
		{
			m.addListener(handler);
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
	
	
	public void scroll(double pixels)
	{
		// TODO
	}
	
	
	//
	
	
	public class Handler
		implements FxEditorModel.Listener
	{
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
			int start = FX.round(getModel().getLineCount() * pos);
			setStartIndex(start);
		}
	}
}
