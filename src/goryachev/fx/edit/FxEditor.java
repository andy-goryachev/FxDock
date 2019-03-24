// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import goryachev.common.util.Log;
import goryachev.fx.Binder;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.Formatters;
import goryachev.fx.FxAction;
import goryachev.fx.FxBoolean;
import goryachev.fx.FxFormatter;
import goryachev.fx.FxObject;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.Markers;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 * Fx Text Editor, intended to provide capabilities missing in JavaFX, such as:
 * <pre>
 * - text editor with syntax highlighing
 * - capability to display and edit large texts (up to 2 billion rows)
 * - capability to use external models (disk, net)
 * - capability to insert arbitrary row widgets
 */
public class FxEditor
	extends Pane
{
	/** caret style */
	public static final CssStyle CARET = new CssStyle("FxEditor_CARET");
	/** caret line highlight */
	public static final CssStyle CARET_LINE_HIGHLIGHT = new CssStyle("FxEditor_CARET_LINE_HIGHLIGHT");
	/** selection highlight */
	public static final CssStyle SELECTION_HIGHLIGHT = new CssStyle("FxEditor_SELECTION_HIGHLIGHT");
	/** panel style */
	public static final CssStyle PANE = new CssStyle("FxEditor_PANEL");
	/** line number component */
	public static final CssStyle LINE_NUMBER = new CssStyle("FxEditor_LINE_NUMBER");
	/** vflow */
	public static final CssStyle VFLOW = new CssStyle("FxEditor_VFLOW");
	
	public final FxAction copyAction = new FxAction(this::copy);
	public final FxAction selectAllAction = new FxAction(this::selectAll);
	
	protected final FxBoolean editableProperty = new FxBoolean(false);
	protected final ReadOnlyObjectWrapper<FxEditorModel> modelProperty = new ReadOnlyObjectWrapper<>();
	protected final FxBoolean wordWrapProperty = new FxBoolean(true);
	protected final ReadOnlyBooleanWrapper multipleSelectionProperty = new ReadOnlyBooleanWrapper(false);
	protected final FxBoolean displayCaretProperty = new FxBoolean(true);
	protected final FxBoolean showLineNumbersProperty = new FxBoolean(false);
	protected final FxBoolean highlightCaretLineProperty = new FxBoolean(true);
	protected final ReadOnlyObjectWrapper<Duration> caretBlinkRateProperty = new ReadOnlyObjectWrapper(Duration.millis(500));
	protected final FxObject<FxFormatter> lineNumberFormatterProperty = new FxObject<>();
	protected final Markers markers = new Markers(32);
	protected final VFlow vflow;
	protected final ScrollBar vscroll;
	protected final ScrollBar hscroll;
	protected final ChangeListener<LoadStatus> loadStatusListener;
	protected final SelectionController selector;
	protected final FxEditorModelListener modelListener;
	protected boolean handleScrollEvents = true;
	protected BiConsumer<FxEditor,Marker> wordSelector = new SimpleWordSelector();

	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setFocusTraversable(true);
		FX.style(this, PANE);
		setBackground(FX.background(Color.WHITE));
		
		modelListener = new FxEditorModelListener()
		{
			public void eventAllLinesChanged()
			{
				handleAllLinesChanged();
			}

			public void eventTextUpdated(int startLine, int startPos, int startCharsInserted, int linesInserted, int endLine, int endPos, int endCharsInserted)
			{
				handleTextUpdated(startLine, startPos, startCharsInserted, linesInserted, endLine, endPos, endCharsInserted);
			}
		};
		
		selector = createSelectionController();

		loadStatusListener = new ChangeListener<LoadStatus>()
		{
			public void changed(ObservableValue<? extends LoadStatus> observable, LoadStatus prev, LoadStatus cur)
			{
				updateLoadStatus(cur);
			}
		};

		setModel(m);
		
		vflow = new VFlow(this);
		
		vscroll = createVScrollBar();
		vscroll.setOrientation(Orientation.VERTICAL);
		vscroll.setManaged(true);
		vscroll.setMin(0.0);
		vscroll.setMax(1.0);
		vscroll.valueProperty().addListener((src,old,val) -> setAbsolutePositionVertical(val.doubleValue()));
		vscroll.addEventFilter(ScrollEvent.ANY, (ev) -> ev.consume());
		
		hscroll = createHScrollBar();
		hscroll.setOrientation(Orientation.HORIZONTAL);
		hscroll.setManaged(true);
		hscroll.setMin(0.0);
		hscroll.setMax(1.0);
		hscroll.valueProperty().addListener((src,old,val) -> setAbsolutePositionHorizontal(val.doubleValue()));
		hscroll.addEventFilter(ScrollEvent.ANY, (ev) -> ev.consume());
		hscroll.visibleProperty().bind(wordWrapProperty.not());
		hscroll.valueProperty().addListener((s,p,c) -> handleHorizontalScroll(c.doubleValue()));
		
		getChildren().addAll(vflow, vscroll, hscroll);
		
		selector.segments.addListener((Observable src) -> vflow.updateCaretAndSelection());
		
		Binder.onChange(vflow::updateBlinkRate, true, blinkRateProperty());
		Binder.onChange(this::updateLayout, widthProperty(), heightProperty(), showLineNumbersProperty);
		wordWrapProperty.addListener((s,p,c) -> updateLayout());
		
		// key map
		KeyMap.onKeyPressed(this, KeyCode.A, KeyMap.SHORTCUT, this::selectAll);
		KeyMap.onKeyPressed(this, KeyCode.C, KeyMap.SHORTCUT, this::copy);
		KeyMap.onKeyPressed(this, KeyCode.DOWN, this::moveDown);
		KeyMap.onKeyPressed(this, KeyCode.PAGE_DOWN, this::pageDown);
		KeyMap.onKeyPressed(this, KeyCode.PAGE_UP, this::pageUp);
		KeyMap.onKeyPressed(this, KeyCode.UP, this::moveUp);
		
		initMouseController();
		
		// init key handler
		addEventFilter(KeyEvent.ANY, (ev) -> handleKeyEvent(ev));
	}
	
	
	public ScrollBar getVerticalScrollBar()
	{
		return vscroll;
	}
	
	
	public ScrollBar getHorizontalScrollBar()
	{
		return hscroll;
	}
	
	
	public void setContentPadding(Insets m)
	{
		vflow.setPadding(m);
	}
	
	
	public FxFormatter getLineNumberFormatter()
	{
		FxFormatter f = lineNumberFormatterProperty.get();
		if(f == null)
		{
			f = Formatters.getIntegerFormat();
		}
		return f;
	}
	
	
	public void setLineNumberFormatter(FxFormatter f)
	{
		lineNumberFormatterProperty.set(f);
		requestLayout();
	}
	
	
	/** override to provide your own selection model */
	protected SelectionController createSelectionController()
	{
		return new SelectionController();
	}
	
	
	/** override to provide your own mouse handler */
	protected void initMouseController()
	{
		FxEditorMouseHandler h = new FxEditorMouseHandler(this, selector);
		
		vflow.addEventFilter(MouseEvent.MOUSE_CLICKED, (ev) -> h.handleMouseClicked(ev));
		vflow.addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> h.handleMousePressed(ev));
		vflow.addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> h.handleMouseReleased(ev));
		vflow.addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> h.handleMouseDragged(ev));
		vflow.addEventFilter(ScrollEvent.ANY, (ev) -> h.handleScroll(ev));
	}
	
	
	public ReadOnlyObjectProperty<EditorSelection> selectionProperty()
	{
		return selector.selectionProperty();
	}
	
	
	public EditorSelection getSelection()
	{
		return selector.getSelection();
	}
	
	
	// TODO use selector for this
//	protected void setSelection(EditorSelection es)
//	{
//		selectionProperty.set(es);
//	}
	
	
	public void clearSelection()
	{
		selector.clear();
	}

	
	protected Runnable getActionForKeyEvent(KeyEvent ev)
	{
		return null;
	}
	
	
	public void setModel(FxEditorModel m)
	{
		markers.clear();
		
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(modelListener);
			old.loadStatus.removeListener(loadStatusListener);
		}
		
		modelProperty.set(m);
		
		if(m != null)
		{
			m.addListener(modelListener);
			m.loadStatus.addListener(loadStatusListener);
		}
		
		selector.clear();
		if(vflow != null)
		{
			vflow.invalidateLayout();
		}
		
		handleAllLinesChanged();
	}
	
	
	public FxEditorModel getModel()
	{
		return modelProperty.get();
	}
	
	
	public int getLineCount()
	{
		FxEditorModel m = getModel();
		return m == null ? 0 : m.getLineCount();
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		return new XScrollBar();
	}
	
	
	protected ScrollBar createHScrollBar()
	{
		return new XScrollBar();
	}
	
	
	protected void updateLoadStatus(LoadStatus s)
	{
		if(vscroll instanceof XScrollBar)
		{
			XScrollBar vs = (XScrollBar)vscroll;
			if(s.isValid())
			{
				vs.setPainer((canvas) ->
				{
					double w = canvas.getWidth();
					double h = canvas.getHeight();
					double y = s.getProgress() * h;
					GraphicsContext g = canvas.getGraphicsContext2D();
					g.setFill(Color.LIGHTGRAY);
					g.fillRect(0, y, w, h - y);
				});
			}
			else
			{
				vs.setPainer(null);
			}
		}
	}
	
	
	protected void setAbsolutePositionVertical(double pos)
	{
		if(handleScrollEvents)
		{
			// TODO account for visible line count
			int start = FX.round(pos); 
				//FX.round(getModel().getLineCount() * pos);
			setTopLineIndex(start);
		}
	}
	
	
	protected void setAbsolutePositionHorizontal(double pos)
	{
		// TODO
	}
	
	
	protected void setHandleScrollEvents(boolean on)
	{
		handleScrollEvents = on;
	}
	
	
	public boolean isWordWrap()
	{
		return wordWrapProperty.get();
	}
	
	
	public void setWordWrap(boolean on)
	{
		wordWrapProperty.set(on);
	}
	
	
	public BooleanProperty wordWrapProperty()
	{
		return wordWrapProperty;
	}
	
	
	public void setMultipleSelectionEnabled(boolean on)
	{
		multipleSelectionProperty.set(on);
	}
	
	
	public boolean isMultipleSelectionEnabled()
	{
		return multipleSelectionProperty.get();
	}
	
	
	public ReadOnlyBooleanProperty multipleSelectionProperty()
	{
		return multipleSelectionProperty.getReadOnlyProperty();
	}
	
	
	public ReadOnlyObjectProperty<FxEditorModel> modelProperty()
	{
		return modelProperty.getReadOnlyProperty();
	}
	
	
	protected void setTopLineIndex(int ix)
	{
		vflow.setTopLineIndex(ix);
		updateLayout();
	}
	
	
	protected void updateLayout()
	{
		if(vflow != null)
		{
			if(wordWrapProperty.get())
			{
				vflow.offsetx = 0;
			}
			vflow.requestLayout();
		}
		requestLayout();
	}
	
	
	protected void layoutChildren()
	{
		Insets m = getPadding();
		double x0 = m.getLeft();
		double y0 = m.getTop();
		
		double vscrollWidth = 0.0;
		double hscrollHeight = 0.0;
		
		// position the scrollbar(s)
		if(vscroll.isVisible())
		{
			vscrollWidth = vscroll.prefWidth(-1);
		}
		
		if(hscroll.isVisible())
		{
			hscrollHeight = hscroll.prefHeight(-1);
		}
		
		double w = getWidth() - m.getLeft() - m.getRight() - vscrollWidth - 1;
		double h = getHeight() - m.getTop() - m.getBottom() - hscrollHeight - 1;

		// layout children
		layoutInArea(vscroll, w, y0 + 1, vscrollWidth, h, 0, null, true, true, HPos.RIGHT, VPos.TOP);
		layoutInArea(hscroll, x0 + 1, h, w, hscrollHeight, 0, null, true, true, HPos.LEFT, VPos.BOTTOM);
		layoutInArea(vflow, x0, y0, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
	}
	
	
	/** returns text position at the specified screen coordinates */
	public Marker getTextPos(double screenx, double screeny)
	{
		return vflow.layout.getTextPos(screenx, screeny, markers);
	}
	
	
	public Marker newMarker(int lineNumber, int charIndex, boolean leading)
	{
		return markers.newMarker(lineNumber, charIndex, leading);
	}
	
	
	protected CaretLocation getCaretLocation(Marker pos)
	{
		return vflow.layout.getCaretLocation(this, pos);
	}
	
	
	protected int getViewStartLine()
	{
		return vflow.layout.getTopLine();
	}
	
	
	public ReadOnlyObjectProperty<Duration> blinkRateProperty()
	{
		return caretBlinkRateProperty.getReadOnlyProperty();
	}
	
	
	public Duration getBlinkRate()
	{
		return caretBlinkRateProperty.get();
	}
	
	
	public void setBlinkRate(Duration d)
	{
		caretBlinkRateProperty.set(d);
	}
	
	
	public boolean isEditable()
	{
		return editableProperty.get();
	}
	
	
	/** enables editing in the component.  this setting will be ignored if a a model is read only */
	public void setEditable(boolean on)
	{
		editableProperty.set(on);
	}

	
	protected void handleAllLinesChanged()
	{
		clearSelection();
		
		if(vflow != null)
		{
			vflow.invalidateLayout();
			vflow.reset();
		}
		
		if(vscroll != null)
		{
			vscroll.setValue(0);
		}
		
		if(hscroll != null)
		{
			hscroll.setValue(0);
		}
		
		updateLayout();
	}


	protected void handleTextUpdated(int startLine, int startPos, int startCharsInserted, int linesInserted, int endLine, int endPos, int endCharsInserted)
	{
		// TODO
		D.print(startLine, startPos, startCharsInserted, linesInserted, endLine, endPos, endCharsInserted);
		
		// update markers
		markers.update(startLine, startPos, startCharsInserted, linesInserted, endLine, endPos, endCharsInserted);
		// update vflow
		vflow.update(startLine, linesInserted, endLine);
	}

	
	public void setDisplayCaret(boolean on)
	{
		displayCaretProperty.set(on);
	}
	
	
	public boolean isDisplayCaret()
	{
		return displayCaretProperty.get();
	}
	
	
	public void setShowLineNumbers(boolean on)
	{
		showLineNumbersProperty.set(on);
	}
	
	
	public boolean isShowLineNumbers()
	{
		return showLineNumbersProperty.get();
	}
	
	
	public BooleanProperty showLineNumbersProperty()
	{
		return showLineNumbersProperty;
	}
	
	
	public void setHighlightCaretLine(boolean on)
	{
		highlightCaretLineProperty.set(on);
	}
	
	
	public boolean isHighlightCaretLine()
	{
		return highlightCaretLineProperty.get();
	}
	

	/** returns plain text on the specified line */
	public String getPlainText(int line)
	{
		return getModel().getPlainText(line);
	}


	/** returns selected plain text, concatenating multiple selection segments if necessary */
	public String getSelectedText() throws Exception
	{
		StringWriter wr = new StringWriter();
		getModel().getPlainText(getSelection(), wr);
		return wr.toString();
	}
	
	
	/** 
	 * outputs selected plain text, concatenating multiple selection segments if necessary.
	 * this method should be used where allocating a single (potentially large) string is undesirable,
	 * for example when saving to a file.
	 * any exceptions thrown by the writer are silently ignored and the process is aborted.
	 */
	public void writeSelectedText(Writer wr)
	{
		try
		{
			getModel().getPlainText(getSelection(), wr);
		}
		catch(Exception ignored)
		{
		}
	}
	
	
	public void pageUp()
	{
		vflow.pageUp();
	}
	
	
	public void pageDown()
	{
		vflow.pageDown();
	}
	
	
	public void moveUp()
	{
		// TODO
		D.print("moveUp");
	}
	
	
	public void moveDown()
	{
		// TODO
		D.print("moveDown");
	}
	
	
	public void scroll(double fractionOfHeight)
	{
		vflow.scroll(fractionOfHeight);
	}
	
	
	/** scrolls up (deltaInPixels < 0) or down (deltaInPixels > 0) */
	public void blockScroll(double deltaInPixels)
	{
		vflow.blockScroll(deltaInPixels);
	}
	
	
	/** copies all supported formats */
	public void copy()
	{
		copy(null, getModel().getSupportedFormats());
	}
	
	
	/** copies specified formats to clipboard, using an error handler */
	public void copy(Consumer<Throwable> errorHandler, DataFormat ... formats)
	{
		getModel().copy(getSelection(), errorHandler, formats);
	}
	
	
	public void selectAll()
	{
		int ix = getLineCount();
		if(ix > 0)
		{
			--ix;
			
			String s = getModel().getPlainText(ix);
			Marker beg = markers.newMarker(0, 0, true);
			Marker end = markers.newMarker(ix, Math.max(0, s.length() - 1), false);
			
			selector.setSelection(beg, end);
			selector.commitSelection();
		}
	}
	
	
	public void select(Marker start, Marker end)
	{
		selector.setSelection(start, end);
		selector.commitSelection();
	}
	
	
	protected void setSuppressBlink(boolean on)
	{
		vflow.setSuppressBlink(on);
	}


	public void scrollToVisible(int row)
	{
		if((row >= 0) && (row < getLineCount()))
		{
			// FIX smarter positioning so the target line is somewhere at 25% of the height
			vflow.scrollToVisible(row);
		}
	}
	
	
	public void setOrigin(int row)
	{
		if((row >= 0) && (row < getLineCount()))
		{
			vflow.setOrigin(row, 0);
		}
	}
	
	
	public void scrollToVisible(Point2D screenPoint)
	{
		Point2D p = vflow.screenToLocal(screenPoint);
		double y = p.getY();
		if(y < 0)
		{
			// above
			// FIX for now, just show the upper portion of the top line
			vflow.scrollToVisible(vflow.getTopLine());
		}
		else if(y > vflow.getHeight())
		{
			// below
			// FIX for now, just show the lower portion of the bottom line
			int ix = vflow.getTopLine() + Math.max(0, vflow.getVisibleLineCount() - 1);
			vflow.scrollToVisible(ix);
		}
	}
	
	
	protected void handleHorizontalScroll(double val)
	{
		if(handleScrollEvents)
		{
			vflow.setHorizontalScroll(val);
		}
	}
	
	
	protected void handleKeyEvent(KeyEvent ev)
	{
		if(!ev.isConsumed())
		{
			EventType<KeyEvent> t = ev.getEventType();
			if(t == KeyEvent.KEY_PRESSED)
			{
				handleKeyPressed(ev);
			}
			else if(t == KeyEvent.KEY_RELEASED)
			{
				handleKeyReleased(ev);
			}
			else if(t == KeyEvent.KEY_TYPED)
			{
				handleKeyTyped(ev);
			}
		}
	}


	protected void handleKeyPressed(KeyEvent ev)
	{
	}
	
	
	protected void handleKeyReleased(KeyEvent ev)
	{
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		FxEditorModel m = getModel();
		if(m.isEditable())
		{
			String ch = ev.getCharacter();
			if(isTypedCharacter(ch))
			{
				Edit ed = new Edit(getSelection(), ch);
				try
				{
					Edit undo = m.edit(ed);
					// TODO add to undo manager
				}
				catch(Exception e)
				{
					// TODO provide user feedback
					Log.ex(e);
				}
			}
		}
	}


	protected boolean isTypedCharacter(String ch)
	{
		if(KeyEvent.CHAR_UNDEFINED.equals(ch))
		{
			return false;
		}
		
		int len = ch.length();
		switch(len)
		{
		case 0:
			return false;
		case 1:
			break;
		default:
			return true;
		}
		
		char c = ch.charAt(0);
		if(c < ' ')
		{
			return false;
		}
		
		switch(c)
		{
		case 0x7f:
			return false;
		default:
			return true;
		}
	}
	
	
	public void setCaret(int row, int charIndex)
	{
		Marker m = newMarker(row, charIndex, true);
		select(m, m);
	}


	public void selectLine(Marker m)
	{
		if(m != null)
		{
			int line = m.getLine();
			Marker start = markers.newMarker(line, 0, true);
			
			int len = getModel().getTextLength(line);
			Marker end = markers.newMarker(line, len, false);
			
			selector.setSelection(start, end);
		}
	}
	
	
	public void selectWord(Marker m)
	{
		if(m != null)
		{
			if(wordSelector != null)
			{
				wordSelector.accept(this, m);
			}
		}
	}
	
	
	public void setWordSelector(BiConsumer<FxEditor,Marker> s)
	{
		wordSelector = s;
	}
	
	
	public int getTextLength(int line)
	{
		return getModel().getTextLength(line);
	}


	/** recreate visible area */
	public void reloadVisibleArea()
	{
		vflow.invalidateLayout();
	}
}
