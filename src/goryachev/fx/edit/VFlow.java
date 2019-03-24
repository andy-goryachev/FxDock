// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.FX;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.EditorTools;
import goryachev.fx.edit.internal.SelectionHelper;
import goryachev.fx.util.FxPathBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


/**
 * Vertical Flow manages LineBoxes.
 */
public class VFlow
	extends Pane
{
	protected final FxEditor editor;
	public final Timeline caretAnimation;
	public final Path caretPath;
	public final Path selectionHighlight;
	public final Path caretLineHighlight;
	protected final BooleanProperty caretVisible = new SimpleBooleanProperty(true);
	protected final BooleanProperty suppressBlink = new SimpleBooleanProperty(false);
	protected final Rectangle clip;
	// TODO line decorations/line numbers
	protected FxEditorLayout layout;
	/** index of the topmost visible line */
	protected int topLine;
	/** horizontal shift in pixels */
	protected double offsetx;
	/** vertical offset or the viewport relative to the topmost line.  always positive */
	protected double offsety;

	
	public VFlow(FxEditor ed)
	{
		this.editor = ed;
		
		FX.style(this, FxEditor.VFLOW);
		
		clip = new Rectangle();
		
		caretPath = new Path();
		FX.style(caretPath, FxEditor.CARET);
		caretPath.setManaged(false);
		caretPath.setStroke(Color.BLACK);

		caretLineHighlight = new Path();
		FX.style(caretLineHighlight, FxEditor.CARET_LINE_HIGHLIGHT);
		caretLineHighlight.setManaged(false);
		caretLineHighlight.setStroke(null);
		caretLineHighlight.setFill(Color.rgb(255, 0, 255, 0.02));

		selectionHighlight = new Path();
		FX.style(selectionHighlight, FxEditor.SELECTION_HIGHLIGHT);
		selectionHighlight.setManaged(false);
		selectionHighlight.setStroke(null);
		selectionHighlight.setFill(Color.rgb(255, 255, 0, 0.25));
				
		caretAnimation = new Timeline();
		caretAnimation.setCycleCount(Animation.INDEFINITE);
		
		getChildren().addAll(caretLineHighlight, selectionHighlight, caretPath);
		setClip(clip);
		
		caretPath.visibleProperty().bind(new BooleanBinding()
		{
			{
				bind(caretVisible, editor.displayCaretProperty, editor.focusedProperty(), editor.disabledProperty(), suppressBlink);
			}

			protected boolean computeValue()
			{
				return (isCaretVisible() || suppressBlink.get()) && editor.isDisplayCaret() && editor.isFocused() && (!editor.isDisabled());
			}
		});
	}
	
	
	public void setOrigin(int top, double offy)
	{
		topLine = top;
		offsety = offy;
		
		layoutChildren();
		
		updateVerticalScrollBar();
	}
	
	
	/** adjusts the origin to maximize the visibility of the specified line */
	// FIX handle all cases
	public void scrollToVisible(int ix)
	{
		// TODO all this could be smarter and actually compute the new origin
		if((ix >= topLine) && (ix <= topLine + getVisibleLineCount()))
		{
			if(layout != null)
			{
				LineBox b = layout.getLineBox(ix);
				if(b != null)
				{
					double y = b.getY() + b.getHeight();
					double dy = y - getHeight();
					if(y > 0)
					{
						blockScroll(dy);
						return;
					}
				}
			}
		}
		
		setOrigin(ix, 0);
	}


	public void setTopLineIndex(int ix)
	{
		topLine = ix;
	}
	
	
	public int getTopLine()
	{
		return topLine;
	}
	
	
	public int getVisibleLineCount()
	{
		if(layout == null)
		{
			return 0;
		}
		return layout.getVisibleLineCount();
	}
	
	
	protected void layoutChildren()
	{
		// TODO skip if nothing changed!
		layout = recreateLayout(layout);
		updateCaretAndSelection();
		//updateVerticalScrollBar();
	}
	
	
	public void invalidateLayout()
	{
		if(layout != null)
		{
			layout.removeFrom(this);
		}
		layout = null;
	}
	
	
	public void reset()
	{
		offsetx = 0;
		offsety = 0;
		topLine = 0;
	}
	
	
	public void setSuppressBlink(boolean on)
	{
		suppressBlink.set(on);
		
		if(!on)
		{
			// restart animation cycle
			updateBlinkRate();
		}
	}
	
	
	public void updateBlinkRate()
	{
		Duration d = editor.getBlinkRate();
		Duration period = d.multiply(2);
		
		caretAnimation.stop();
		caretAnimation.getKeyFrames().setAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(d, (ev) -> setCaretVisible(false)),
			new KeyFrame(period)
		);
		caretAnimation.play();
	}
	
	
	/** used for blinking animation */
	protected void setCaretVisible(boolean on)
	{
		caretVisible.set(on);
	}
	
	
	public boolean isCaretVisible()
	{
		return caretVisible.get();
	}
	
	
	/** estimates the last visible line number and computes preferred width based on that */ 
	protected double estimateLineNumberColumnWidth(Labeled c)
	{			
		double h = Math.max(1.0, c.prefHeight(-1));
		int lineCount = (int)(getHeight() / h);
		int ix = Math.max(999, topLine + lineCount);
		
		setLineNumber(c, ix);
		
		c.setManaged(true);
		getChildren().add(c);
		c.applyCss();
		
		double w = c.prefWidth(-1);
		
		getChildren().remove(c);
		
		return w;
	}
	
	
	protected void setLineNumber(Labeled c, int ix)
	{
		String s = editor.getLineNumberFormatter().format(ix + 1);
		c.setText(s);
	}
	
	
	public double addAndComputePreferredHeight(Region nd)
	{
		// warning: the same code in recreateLayout() above
		Insets pad = getInsets();
		double x0 = pad.getLeft();
		boolean wrap = editor.isWordWrap();
		double width = getWidth();
		
		// TODO account for leading, trailing components
		double wid = width - x0 - pad.getRight();
		
		getChildren().add(nd);
		nd.applyCss();
		nd.setManaged(true);
		
		double w = wrap ? wid : nd.prefWidth(-1);
		nd.setMaxWidth(wrap ? wid : Double.MAX_VALUE);
		return nd.prefHeight(w);
	}
	
	
	public void updateCaretAndSelection()
	{
		if(editor.isHighlightCaretLine())
		{
			FxPathBuilder caretLineBuilder = new FxPathBuilder();
			for(SelectionSegment s: editor.selector.segments)
			{
				Marker caret = s.getCaret();
				createCurrentLineHighlight(caretLineBuilder, caret);
			}
			caretLineHighlight.getElements().setAll(caretLineBuilder.getPath());
		}
		
		FxPathBuilder selectionBuilder = new FxPathBuilder();
		FxPathBuilder caretBuilder = new FxPathBuilder();
		
		for(SelectionSegment s: editor.selector.segments)
		{
			Marker start = s.getMin();
			Marker end = s.getMax();
			Marker caret = s.getCaret();
			
			createSelectionHighlight(selectionBuilder, start, end);
			createCaretPath(caretBuilder, caret);
		}
		
		selectionHighlight.getElements().setAll(selectionBuilder.getPath());
		caretPath.getElements().setAll(caretBuilder.getPath());
	}
	
	
	protected void createCaretPath(FxPathBuilder p, Marker m)
	{
		CaretLocation c = editor.getCaretLocation(m);
		if(c != null)
		{
			p.moveto(c.x, c.y0);
			p.lineto(c.x, c.y1);
		}
	}
	
	
	protected void createCurrentLineHighlight(FxPathBuilder pbuilder, Marker mark)
	{
		LineBox b = layout.getLineBox(mark.getLine());
		if(b != null)
		{
			Insets m = getPadding();
			if(editor.isWordWrap())
			{
				b.addBoxOutline(pbuilder, 0, getWidth());
			}
			else
			{
				b.addBoxOutline(pbuilder, 0, layout.getTotalWidth()); 
			}
		}
	}
	
	
	protected FxEditorLayout getEditorLayout()
	{
		if(layout == null)
		{
			layout = new FxEditorLayout(editor, topLine);
		}
		return layout;
	}
	
	
	public void pageUp()
	{
		blockScroll(-getHeight());
	}
	
	
	public void pageDown()
	{
		blockScroll(getHeight());
	}
	
	
	public void scroll(double fractionOfHeight)
	{
		blockScroll(getHeight() * fractionOfHeight);
	}
	
	
	public void blockScroll(double delta)
	{
		if(delta < 0)
		{
			delta = -delta;
			if(delta <= offsety)
			{
				// no need to query the model
				setOrigin(topLine, offsety -= delta);
				return;
			}
			else
			{
				int ix = topLine;
				double targetY = -delta;
				double y = -offsety;
					
				for(;;)
				{
					--ix;
					if(ix < 0)
					{
						// top line
						setOrigin(0, 0);
						return;
					}
					
					double dy = getEditorLayout().getLineHeight(ix);
					y -= dy;
					if(y < targetY)
					{
						break;
					}
				}
				
				setOrigin(ix, targetY - y);
				return;
			}
		}
		else
		{
			int ix = topLine;
			double targetY = delta;
			double y = -offsety;
			
			for(;;)
			{
				if(ix >= editor.getLineCount())
				{
					// FIX need to figure out what to do in this case
					break;
				}
				
				double dy = getEditorLayout().getLineHeight(ix);
				if(y + dy > targetY)
				{
					setOrigin(ix, targetY - y);
					return;
				}
				
				y += dy;
				ix++;
			}
		}
	}
	
	
	protected PathElement[] getRangeTop()
	{
		double w = getWidth();
		
		return new PathElement[]
		{
			new MoveTo(0, -1),
			new LineTo(w, -1),
			new LineTo(w, 0),
			new LineTo(0, 0),
			new LineTo(0, -1)
		};
	}
	
	
	protected PathElement[] getRangeBottom()
	{
		double w = getWidth();
		double h = getHeight();
		double h1 = h + 1;
		
		return new PathElement[]
		{
			new MoveTo(0, h),
			new LineTo(w, h),
			new LineTo(w, h1),
			new LineTo(0, h1),
			new LineTo(0, h)
		};
	}
	
	
	protected PathElement[] getRangeShape(int line, int startOffset, int endOffset)
	{
		LineBox lineBox = layout.getLineBox(line);
		if(lineBox == null)
		{
			return null;
		}
		
		if(endOffset < 0)
		{
			endOffset = lineBox.getTextLength();
		}
		
		PathElement[] pe;
		if(startOffset == endOffset)
		{
			// not a range, use caret shape instead
			pe = lineBox.getCaretShape(startOffset, true);
		}
		else
		{
			pe = lineBox.getRange(startOffset, endOffset);
		}
		
		if(pe == null)
		{
			return null;
		}
		else
		{
			return EditorTools.translatePath(this, lineBox.getCenter(), pe);	
		}
	}
	
	
	/**
	 * Populates path builder with selection shapes between two markers.
	 * This method handles RTL and LTR text.
	 */
	protected void createSelectionHighlight(FxPathBuilder b, Marker startMarker, Marker endMarker)
	{
		if((startMarker == null) || (endMarker == null))
		{
			return;
		}
		
		// enforce startMarker < endMarker
		if(startMarker.compareTo(endMarker) > 0)
		{
			throw new Error(startMarker + "<" + endMarker);
		}
		
		if(endMarker.getLine() < topLine)
		{
			// selection is above visible area
			return;
		}
		else if(startMarker.getLine() >= (topLine + layout.getVisibleLineCount()))
		{
			// selection is below visible area
			return;
		}
		
		// get selection shapes for top and bottom segments,
		// translated to this VFlow coordinates.
		PathElement[] top;
		PathElement[] bottom;
		if(startMarker.getLine() == endMarker.getLine())
		{
			top = getRangeShape(startMarker.getLine(), startMarker.getLineOffset(), endMarker.getLineOffset());
			bottom = null;
		}
		else
		{
			top = getRangeShape(startMarker.getLine(), startMarker.getLineOffset(), -1);
			if(top == null)
			{
				top = getRangeTop();
			}
			
			bottom = getRangeShape(endMarker.getLine(), 0, endMarker.getLineOffset());
			if(bottom == null)
			{
				bottom = getRangeBottom();
			}
		}
		
		// generate shapes
		Insets m = getPadding();
		double left = m.getLeft() + layout.getLineNumbersColumnWidth(); // FIX padding? border?
		double right = editor.isWordWrap() ? (getWidth() - m.getLeft() - m.getRight()) : layout.getTotalWidth();

		// TODO
		boolean topLTR = true;
		boolean bottomLTR = true;
		
		new SelectionHelper(b, left, right).generate(top, bottom, topLTR, bottomLTR);
	}

	
	public FxEditorLayout recreateLayout(FxEditorLayout prev)
	{
		editor.setHandleScrollEvents(false);
		
		if(prev != null)
		{
			prev.removeFrom(this);
		}
		
		double height = getHeight();
		clip.setWidth(getWidth());
		clip.setHeight(height);
		
		// TODO is loaded?
		FxEditorModel model = editor.getModel();
		int lineCount = model == null ? 0 : model.getLineCount();
		FxEditorLayout la = new FxEditorLayout(editor, topLine);
		
		Insets pad = getInsets();
		double y0 = pad.getTop() - offsety;
		double ymax = height - pad.getBottom();
		double x0 = pad.getLeft();
		double wid = getWidth() - pad.getLeft() - pad.getRight();
		boolean wrap = editor.isWordWrap();
		boolean showLineNumbers = editor.isShowLineNumbers();
		boolean estimateLineNumberWidth = showLineNumbers;
		double lineNumbersColumnWidth = 0.0;
		double unwrappedWidth = -1.0;
		
		// layout from top to bottom.
		// stage 1: computing preferred sizes
		
		double x1 = x0;
		double y = y0;
		if(model != null)
		{
			for(int i=topLine; i<lineCount; i++)
			{
				LineBox b = (prev == null ? null : prev.getLineBox(i));
				if(b == null)
				{
					b = model.getLineBox(i);
					b.init(i);
				}
				
				if(estimateLineNumberWidth)
				{
					// TODO can simply get the last line pref width after the sizing cycle, before the actual layout cycle
					lineNumbersColumnWidth = estimateLineNumberColumnWidth(b.getLineNumberComponent());
					
					x1 += lineNumbersColumnWidth;
					wid -= lineNumbersColumnWidth;
					if(wid < 0)
					{
						wid = 0;
					}
					estimateLineNumberWidth = false;
				}
				
				// TODO skip sizing if the width has not changed (incl. line number component)
				
				Region nd = b.getCenter();
				nd.setManaged(true);
				getChildren().add(nd);
				nd.applyCss();
				la.addLineBox(b);
				
				// TODO can use cached value if the vflow width is the same
				double w = wrap ? wid : -1;
				nd.setMaxWidth(wrap ? wid : Double.MAX_VALUE);
				double h = nd.prefHeight(w);
				
				if(!wrap)
				{
					Region center = b.getCenter();
					double prefw = center == null ? 0.0 : center.prefWidth(-1);
					if(unwrappedWidth < prefw)
					{
						unwrappedWidth = prefw;
					}
				}
				
				if(showLineNumbers)
				{
					Labeled nc = b.getLineNumberComponent();
					setLineNumber(nc, i);
					
					nc.setManaged(true);
					getChildren().add(nc);
					nc.applyCss();
					
					// FIX
					// for some reason, label is taller than the text flow alone, even with the same font
					// the -fx-padding is correct, and -fx-label-padding is 0 on line number labels
	//				h = Math.max(h, nc.prefHeight(-1));
					
					b.setHeight(h);
					b.setY(y);
				}
				else
				{
					b.setHeight(h);
				}
				
				y += h;
				if(y > ymax)
				{
					break;
				}
			}
		}
		
		// stage 2: layout components
		
		if(unwrappedWidth < wid)
		{
			unwrappedWidth = wid;
		}
		
		la.setLineNumbersColumnWidth(lineNumbersColumnWidth);
		la.setUnwrappedWidth(unwrappedWidth);
		
		layoutFlow(la, y0, ymax, x0, x1);
				
		if(!wrap)
		{
			//double val = unwrappedWidth - wid; // FIX
			ScrollBar hscroll = editor.getHorizontalScrollBar();
			hscroll.setMin(0); // padding?
	        hscroll.setMax(unwrappedWidth);
	        hscroll.setVisibleAmount(wid);
		}
		
		editor.setHandleScrollEvents(true);
		
		return la;
	}
	
	
	protected void layoutFlow(FxEditorLayout la, double y0, double ymax, double x0, double x1)
	{
		boolean showLineNumbers = editor.isShowLineNumbers();
		// TODO is loaded?
		FxEditorModel model = editor.getModel();
		int lineCount = model == null ? 0 : model.getLineCount();
		Insets pad = getInsets();
		double wid = getWidth() - pad.getLeft() - pad.getRight();
		double w = editor.isWordWrap() ? wid : la.getTotalWidth();
		
		double y = y0;
		for(int ix=topLine; ix<lineCount; ix++)
		{
			LineBox b = la.getLineBox(ix);
			Region nd = b.getCenter();
			double h = b.getHeight();
			
			if(showLineNumbers)
			{
				Labeled nc = b.getLineNumberComponent();
				// TODO line numbers are slightly off vertically
				//nc.setPadding(Insets.EMPTY);
				layoutInArea(nc, x0, y, la.getLineNumbersColumnWidth(), h, 0, null, true, true, HPos.RIGHT, VPos.TOP);

				nd.setClip(new Rectangle(offsetx, 0, w, h));
				layoutInArea(nd, x1 - offsetx, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			}
			else
			{
				nd.setClip(null);
				layoutInArea(nd, x1 - offsetx, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			}
			
			y += h;
			if(y > ymax)
			{
				break;
			}
		}
	}
	
	
	public void setHorizontalScroll(double val)
	{
		double unwrappedWidth = layout.getTotalWidth();
		Insets pad = getPadding();
		double wid = getWidth() - pad.getLeft() - pad.getRight() - layout.getLineNumbersColumnWidth();
		
		offsetx = val * (unwrappedWidth - wid) / unwrappedWidth;
		
		double y0 = pad.getTop() - offsety;
		double ymax = getHeight() - pad.getBottom();
		boolean showLineNumbers = editor.isShowLineNumbers();
		double x0 = pad.getLeft();
		double x1 = x0 + layout.getLineNumbersColumnWidth();
		
		// no need to recreate the layout
		layoutFlow(layout, y0, ymax, x0, x1);

		updateCaretAndSelection();
	}


	// returns true if update resulted in a visual change
	public boolean update(int startLine, int linesInserted, int endLine)
	{
		try
		{
			int max = Math.max(endLine, startLine + linesInserted);
			if(max < topLine)
			{
				return false;
			}
			else if(startLine > (topLine + getVisibleLineCount()))
			{
				return false;
			}
			
			// TODO optimize, but for now simply
			invalidateLayout();
			requestLayout();
			
			return true;
		}
		finally
		{
			updateVerticalScrollBar();
		}
	}
	
	
	protected void updateVerticalScrollBar()
	{
		editor.setHandleScrollEvents(false);
		
		int max;
		int visible;
		double val;
		
//		double v = (max == 0 ? 0.0 : topLine / (double)max); 
//		editor.vscroll.setValue(v);
		
		FxEditorModel model = editor.getModel();
		if(model == null)
		{
			visible = 1;
			max = 1;
			val = 0;
		}
		else
		{
			max = model.getLineCount();
			visible = getVisibleLineCount();
			val = topLine; //(max - visible);
		}
		
		ScrollBar vscroll = editor.getVerticalScrollBar();
		vscroll.setMin(0);
        vscroll.setMax(max);
        vscroll.setVisibleAmount(visible);
        vscroll.setValue(val);
        
		editor.setHandleScrollEvents(true);
	}
}
