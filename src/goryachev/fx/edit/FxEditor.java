// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import goryachev.common.util.Log;
import goryachev.fx.Binder;
import goryachev.fx.CAction;
import goryachev.fx.CBooleanProperty;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.Formatters;
import goryachev.fx.FxFormatter;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.Markers;
import java.io.StringWriter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
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
	public static final CssStyle PANEL = new CssStyle("FxEditor_PANEL");
	/** line number component */
	public static final CssStyle LINE_NUMBER = new CssStyle("FxEditor_LINE_NUMBER");
	/** vflow */
	public static final CssStyle VFLOW = new CssStyle("FxEditor_VFLOW");
	
	public final CAction copyAction = new CAction(this::copy);
	public final CAction selectAllAction = new CAction(this::selectAll);
	
	protected final SimpleBooleanProperty editableProperty = new SimpleBooleanProperty(false);
	protected final ReadOnlyObjectWrapper<FxEditorModel> modelProperty = new ReadOnlyObjectWrapper<>();
	protected final CBooleanProperty wrapTextProperty = new CBooleanProperty(true, this::updateLayout);
	protected final ReadOnlyBooleanWrapper multipleSelectionProperty = new ReadOnlyBooleanWrapper(false);
	protected final BooleanProperty displayCaretProperty = new SimpleBooleanProperty(true);
	protected final BooleanProperty showLineNumbersProperty = new SimpleBooleanProperty(false);
	protected final BooleanProperty highlightCaretLineProperty = new SimpleBooleanProperty(true);
	protected final ReadOnlyObjectWrapper<Duration> caretBlinkRateProperty = new ReadOnlyObjectWrapper(Duration.millis(500));
	protected final SimpleObjectProperty<FxFormatter> lineNumberFormatterProperty = new SimpleObjectProperty<>();
	protected final Markers markers = new Markers(32);
	protected final VFlow vflow;
	protected final ScrollBar vscroll;
	protected final ScrollBar hscroll;
	protected final SelectionController selector;
	protected final KeyMap keymap;
	protected boolean handleScrollEvents = true;
	protected BiConsumer<FxEditor,Marker> wordSelector = new SimpleWordSelector();

	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setFocusTraversable(true);
		FX.style(this, PANEL);
		setBackground(FX.background(Color.WHITE));
		
		selector = createSelectionController();

		setTextModel(m);
		
		vflow = new VFlow(this);
		
		vscroll = createVScrollBar();
		
		hscroll = createHScrollBar();
		hscroll.visibleProperty().bind(wrapTextProperty.not());
		
		getChildren().addAll(vflow, vscroll, hscroll);
		
		selector.segments.addListener((Observable src) -> vflow.updateCaretAndSelection());

		Binder.onChange(vflow::updateBlinkRate, true, blinkRateProperty());
		Binder.onChange(this::updateLayout, widthProperty(), heightProperty(), showLineNumbersProperty);
		
		keymap = createKeyMap();
		
		initMouseController();
		
		// init key handler
		addEventFilter(KeyEvent.ANY, (ev) -> handleKeyEvent(ev));
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
	
	
	/** override to provide your own controller */
	protected KeyMap createKeyMap()
	{
		KeyMap m = new KeyMap();
		m.shortcut(KeyCode.C, this::copy);
		m.add(KeyCode.PAGE_DOWN, this::pageDown);
		m.add(KeyCode.PAGE_UP, this::pageUp);
		m.shortcut(KeyCode.A, this::selectAll);
		return m;
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
	
	
	public void setTextModel(FxEditorModel m)
	{
		markers.clear();
		
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(this);
		}
		
		modelProperty.set(m);
		
		if(m != null)
		{
			m.addListener(this);
		}
		
		selector.clear();
		if(vflow != null)
		{
			vflow.invalidateLayout();
		}
		
		eventAllChanged();
//		updateLayout();
	}
	
	
	public FxEditorModel getModel()
	{
		return modelProperty.get();
	}
	
	
	public int getLineCount()
	{
		return getModel().getLineCount();
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.VERTICAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setAbsolutePositionVertical(val.doubleValue()));
		s.addEventFilter(ScrollEvent.ANY, (ev) -> ev.consume());
		return s;
	}
	
	
	protected ScrollBar createHScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.HORIZONTAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setAbsolutePositionHorizontal(val.doubleValue()));
		s.addEventFilter(ScrollEvent.ANY, (ev) -> ev.consume());
		return s;
	}
	
	
	protected void setAbsolutePositionVertical(double pos)
	{
		if(handleScrollEvents)
		{
			// TODO account for visible line count
			int start = FX.round(getModel().getLineCount() * pos);
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
	
	
	public boolean isWrapText()
	{
		return wrapTextProperty.get();
	}
	
	
	public void setWrapText(boolean on)
	{
		wrapTextProperty.set(on);
	}
	
	
	public BooleanProperty wrapTextProperty()
	{
		return wrapTextProperty;
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
		
		double w = getWidth() - m.getLeft() - m.getRight() - vscrollWidth;
		double h = getHeight() - m.getTop() - m.getBottom() - hscrollHeight;

		// layout children
		layoutInArea(vscroll, w, y0, vscrollWidth, h, 0, null, true, true, HPos.RIGHT, VPos.TOP);
		layoutInArea(hscroll, x0, h, w, hscrollHeight, 0, null, true, true, HPos.LEFT, VPos.BOTTOM);
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

	
	protected void eventAllChanged()
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


	protected void eventLinesDeleted(int start, int count)
	{
		// FIX
		D.print(start, count);
	}


	protected void eventLinesInserted(int start, int count)
	{
		// FIX
		D.print(start, count);
	}


	protected void eventLinesModified(int start, int count)
	{
		// FIX
		D.print(start, count);
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
	public String getTextOnLine(int line)
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
	
	
	public void pageUp()
	{
		vflow.pageUp();
	}
	
	
	public void pageDown()
	{
		vflow.pageDown();
	}
	
	
	public void blockScroll(boolean up)
	{
		vflow.blockScroll(up);
	}
	
	
	public void blockScroll(double delta, boolean up)
	{
		vflow.blockScroll(delta, up);
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


	public void scrollToVisible(int ix)
	{
		if((ix >= 0) && (ix < getLineCount()))
		{
			// FIX smarter positioning so the target line is somewhere at 25% of the height
			vflow.scrollToVisible(ix);
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
	
	
	protected void handleKeyEvent(KeyEvent ev)
	{
		if(!ev.isConsumed())
		{
			Runnable a = keymap.getActionForKeyEvent(ev);
			if(a != null)
			{
				a.run();
				return;
			}
			
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
}
