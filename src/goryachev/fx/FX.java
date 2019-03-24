// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CPlatform;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.hacks.FxHacks;
import goryachev.fx.internal.CssTools;
import goryachev.fx.internal.FxSchema;
import goryachev.fx.internal.WindowsFx;
import goryachev.fx.table.FxTable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;


/**
 * Making FX-ing easier.
 */
public final class FX
{
	public static final double TWO_PI = Math.PI + Math.PI;
	public static final double PI_2 = Math.PI / 2.0;
	public static final double DEGREES_PER_RADIAN = 180.0 / Math.PI;
	private static WindowsFx windowsFx = new WindowsFx();
	private static Text helper;

	
	public static FxWindow getWindow(Node n)
	{
		Scene sc = n.getScene();
		if(sc != null)
		{
			Window w = sc.getWindow();
			if(w instanceof FxWindow)
			{
				return (FxWindow)w;
			}
		}
		return null;
	}
	
	
	public static void storeSettings(Node n)
	{
		if(n != null)
		{
			windowsFx.storeNode(n);
			GlobalSettings.save();
		}
	}
	
	
	public static void restoreSettings(Node n)
	{
		if(n != null)
		{
			windowsFx.restoreNode(n);
		}
	}
	
	
	public static void storeSettings(FxWindow w)
	{
		windowsFx.storeWindow(w);
		GlobalSettings.save();
	}
	
	
	public static void restoreSettings(FxWindow w)
	{
		windowsFx.restoreWindow(w);
		GlobalSettings.save();
	}
	
	
	public static void open(FxWindow w)
	{
		windowsFx.open(w);
	}
	
	
	public static void close(FxWindow w)
	{
		windowsFx.close(w);
	}
	
	
	public static void exit()
	{
		windowsFx.exit();
	}
	
	
	public static FxAction exitAction()
	{
		return windowsFx.exitAction();
	}
	
	
	/** creates a label.  accepts: CssStyle, CssID, FxCtl, Insets, OverrunStyle, Pos, TextAlignment, Color, Node, Background */
	public static Label label(Object ... attrs)
	{
		Label n = new Label();
		
		for(Object a: attrs)
		{
			if(a == null)
			{
				// ignore
			}
			else if(a instanceof CssStyle)
			{
				n.getStyleClass().add(((CssStyle)a).getName());
			}
			else if(a instanceof CssID)
			{
				n.setId(((CssID)a).getID());
			}
			else if(a instanceof FxCtl)
			{
				switch((FxCtl)a)
				{
				case BOLD:
					n.getStyleClass().add(CssTools.BOLD.getName());
					break;
				case FOCUSABLE:
					n.setFocusTraversable(true);
					break;
				case FORCE_MAX_WIDTH:
					n.setMaxWidth(Double.MAX_VALUE);
					break;
				case FORCE_MIN_HEIGHT:
					n.setMinHeight(Control.USE_PREF_SIZE);
					break;
				case FORCE_MIN_WIDTH:
					n.setMinWidth(Control.USE_PREF_SIZE);
					break;
				case NON_FOCUSABLE:
					n.setFocusTraversable(false);
					break;
				case WRAP_TEXT:
					n.setWrapText(true);
					break;
				default:
					throw new Error("?" + a);
				}
			}
			else if(a instanceof Insets)
			{
				n.setPadding((Insets)a);
			}
			else if(a instanceof OverrunStyle)
			{
				n.setTextOverrun((OverrunStyle)a);
			}
			else if(a instanceof Pos)
			{
				n.setAlignment((Pos)a);
			}
			else if(a instanceof String)
			{
				n.setText((String)a);
			}
			else if(a instanceof TextAlignment)
			{
				n.setTextAlignment((TextAlignment)a);
			}
			else if(a instanceof Color)
			{
				n.setTextFill((Color)a);
			}
			else if(a instanceof StringProperty)
			{
				n.textProperty().bind((StringProperty)a);
			}
			else if(a instanceof Node)
			{
				n.setGraphic((Node)a);
			}
			else if(a instanceof Background)
			{
				n.setBackground((Background)a);
			}
			else
			{
				throw new Error("?" + a);
			}			
		}
		
		return n;
	}
	
	
	/** creates a text segment */
	public static Text text(Object ... attrs)
	{
		Text n = new Text();
		
		for(Object a: attrs)
		{
			if(a == null)
			{
				// ignore
			}
			else if(a instanceof CssStyle)
			{
				n.getStyleClass().add(((CssStyle)a).getName());
			}
			else if(a instanceof CssID)
			{
				n.setId(((CssID)a).getID());
			}
			else if(a instanceof FxCtl)
			{
				switch((FxCtl)a)
				{
				case BOLD:
					n.getStyleClass().add(CssTools.BOLD.getName());
					break;
				case FOCUSABLE:
					n.setFocusTraversable(true);
					break;
				case NON_FOCUSABLE:
					n.setFocusTraversable(false);
					break;
				default:
					throw new Error("?" + a);
				}
			}
			else if(a instanceof String)
			{
				n.setText((String)a);
			}
			else if(a instanceof TextAlignment)
			{
				n.setTextAlignment((TextAlignment)a);
			}
			else
			{
				throw new Error("?" + a);
			}			
		}
		
		return n;
	}
	
	
	/** apply styles to a Node */
	public static void style(Node n, Object ... attrs)
	{
		if(n != null)
		{
			for(Object a: attrs)
			{
				if(a == null)
				{
					// ignore
				}
				else if(a instanceof CssStyle)
				{
					n.getStyleClass().add(((CssStyle)a).getName());
				}
				else if(a instanceof CssID)
				{
					n.setId(((CssID)a).getID());
				}
				else if(a instanceof FxCtl)
				{
					switch((FxCtl)a)
					{
					case BOLD:
						n.getStyleClass().add(CssTools.BOLD.getName());
						break;
					case EDITABLE:
						((TextInputControl)n).setEditable(true);
						break;
					case FOCUSABLE:
						n.setFocusTraversable(true);
						break;
					case FORCE_MIN_HEIGHT:
						((Region)n).setMinHeight(Control.USE_PREF_SIZE);
						break;
					case FORCE_MIN_WIDTH:
						((Region)n).setMinWidth(Control.USE_PREF_SIZE);
						break;
					case NON_EDITABLE:
						((TextInputControl)n).setEditable(false);
						break;
					case NON_FOCUSABLE:
						n.setFocusTraversable(false);
						break;
					case WRAP_TEXT:
						if(n instanceof Labeled)
						{
							((Labeled)n).setWrapText(true);
						}
						else if(n instanceof TextArea)
						{
							((TextArea)n).setWrapText(true);
						}
						else
						{
							throw new Error("?wrap for " + n);
						}
						break;
					default:
						throw new Error("?" + a);
					}
				}
				else if(a instanceof Insets)
				{
					((Region)n).setPadding((Insets)a);
				}
				else if(a instanceof OverrunStyle)
				{
					((Labeled)n).setTextOverrun((OverrunStyle)a);
				}
				else if(a instanceof Pos)
				{
					if(n instanceof Labeled)
					{
						((Labeled)n).setAlignment((Pos)a);
					}
					else if(n instanceof TextField)
					{
						((TextField)n).setAlignment((Pos)a);
					}
					else
					{
						throw new Error("?" + n);
					}
				}
				else if(a instanceof String)
				{
					if(n instanceof Labeled)
					{
						((Labeled)n).setText((String)a);
					}
					else if(n instanceof TextInputControl)
					{
						((TextInputControl)n).setText((String)a);
					}
					else
					{
						throw new Error("?" + n);
					}
				}
				else if(a instanceof TextAlignment)
				{
					((Labeled)n).setTextAlignment((TextAlignment)a);
				}
				else if(a instanceof Background)
				{
					((Region)n).setBackground((Background)a);
				}
				else
				{
					throw new Error("?" + a);
				}
			}
		}
	}


	/** Creates a simple color background. */
	public static Background background(Paint c)
	{
		return new Background(new BackgroundFill(c, null, null));
	}
	
	
	public static Color gray(int col)
	{
		return Color.rgb(col, col, col);
	}
	
	
	/** Creates Color from an RGB value. */
	public static Color rgb(int rgb)
	{
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >>  8) & 0xff;
		int b = (rgb      ) & 0xff;
		return Color.rgb(r, g, b);
	}
	
	
	/** Creates Color from an RGB value. */
	public static Color rgb(int red, int green, int blue)
	{
		return Color.rgb(red, green, blue);
	}
	
	
	/** Creates Color from an RGB value + alpha. */
	public static Color rgb(int rgb, double alpha)
	{
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >>  8) & 0xff;
		int b = (rgb      ) & 0xff;
		return Color.rgb(r, g, b, alpha);
	}
	
	
	/** Creates Color from an RGB value + alpha */
	public static Color rgb(int red, int green, int blue, double alpha)
	{
		return Color.rgb(red, green, blue, alpha);
	}


	public static boolean contains(Node n, double screenx, double screeny)
	{
		if(n != null)
		{
			Point2D p = n.screenToLocal(screenx, screeny);
			if(p != null)
			{
				return n.contains(p);
			}
		}
		return false;
	}
	
	
	/** returns true if (x,y) point in eventSource coordinates is contained by eventTarget node */
	public static boolean contains(Node eventSource, Node eventTarget, double x, double y)
	{
		Point2D p = eventSource.localToScreen(x, y);
		if(p != null)
		{
			p = eventTarget.screenToLocal(p);
			if(p != null)
			{
				return eventTarget.contains(p);
			}
		}
		return false;
	}


	public static boolean isParent(Node parent, Node child)
	{
		while(child != null)
		{
			if(child == parent)
			{
				return true;
			}
			
			child = child.getParent();
		}
		return false;
	}
	
	
	public static void setProperty(Node n, Object k, Object v)
	{
		if(v == null)
		{
			n.getProperties().remove(k);
		}
		else
		{
			n.getProperties().put(k, v);
		}
	}
	
	
	public static Object getProperty(Node n, Object k)
	{
		return n.getProperties().get(k);
	}
	
	
	/** 
	 * returns parent window or null, accepts either a Node or a Window.
	 * unfortunately, FX Window is not a Node, so we have to lose some type safety 
	 */
	public static Window getParentWindow(Object nodeOrWindow)
	{
		if(nodeOrWindow == null)
		{
			return null;
		}
		else if(nodeOrWindow instanceof Window)
		{
			return (Window)nodeOrWindow;
		}
		else if(nodeOrWindow instanceof Node)
		{
			Scene s = ((Node)nodeOrWindow).getScene();
			if(s != null)
			{
				return s.getWindow();
			}
			return null;
		}
		else
		{
			throw new Error("node or window");
		}
	}
	
	
	/** rounds a double value to int */
	public static int round(double x)
	{
		return (int)Math.round(x);
	}
	
	
	/** returns int ceiling of a double value */
	public static int ceil(double x)
	{
		return (int)Math.ceil(x);
	}
	
	
	/** returns int floor of a double value */
	public static int floor(double x)
	{
		return (int)Math.floor(x);
	}
	
	
	/** shortcut for Platform.runLater() */
	public static void later(Runnable r)
	{
		Platform.runLater(r);
	}
	
	
	/** swing invokeAndWait() analog.  if called from an FX application thread, simply invokes the producer. */
	public static <T> T invokeAndWait(Callable<T> producer) throws Exception
	{
		if(Platform.isFxApplicationThread())
		{
			return producer.call();
		}
		else
		{
			FutureTask<T> t = new FutureTask(producer);
			FX.later(t);
			return t.get();
		}
	}


	/** returns window decoration insets */
	public static Insets getDecorationInsets(Window w)
	{
		Scene s = w.getScene();
		double left = s.getX();
		double top = s.getY();
		double right = w.getWidth() - s.getWidth() - left;
		double bottom = w.getHeight() - s.getHeight() - top;
		return new Insets(top, right, bottom, left);
	}


	/** 
	 * returns margin between the node and its containing window.
	 * WARNING: does not check if window is indeed a right one. 
	 */ 
	public static Insets getInsetsInWindow(Window w, Node n)
	{
		Bounds b = n.localToScreen(n.getBoundsInLocal());
		
		double left = b.getMinX() - w.getX();
		double top = b.getMinY() - w.getY();
		double right = w.getX() + w.getWidth() - b.getMaxX();
		double bottom = w.getY() + w.getHeight() - b.getMaxY();

		return new Insets(top, right, bottom, left);
	}
	
	
	/** assign a name to the node for the purposes of saving settings */
	public static void setName(Node n, String name)
	{
		FxSchema.setName(n, name);
	}
	
	
	/** 
	 * attaches a handler to be notified when settings for the node have been loaded.  
	 * setting null clears the handler 
	 */
	public static void setOnSettingsLoaded(Node n, Runnable r)
	{
		FxSchema.setOnSettingsLoaded(n, r);
	}
	
	
	/** bind a property to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, Property<T> p)
	{
		FxSchema.bindings(n, true).add(subKey, p, null);
	}
	
	
	/** bind an object with settings to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, HasSettings x)
	{
		FxSchema.bindings(n, true).add(subKey, x);
	}
	
	
	/** bind a property to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, Property<T> p, StringConverter<T> c)
	{
		FxSchema.bindings(n, true).add(subKey, p, c);
	}
	
	
	/** bind a property to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, Property<T> p, SSConverter<T> c)
	{
		FxSchema.bindings(n, true).add(subKey, c, p);
	}
	
	
	/** returns true if the coordinates belong to one of the Screens */
	public static boolean isValidCoordinates(double x, double y)
	{
		for(Screen screen: Screen.getScreens())
		{
			Rectangle2D r = screen.getVisualBounds();
			if(r.contains(x, y))
			{
				return true;
			}
		}
		return false;
	}
	
	
	/** converts degrees to radians */
	public static double toRadians(double degrees)
	{
		return degrees / DEGREES_PER_RADIAN;
	}
	
	
	/** converts radians to degrees */
	public static double toDegrees(double radians)
	{
		return radians * DEGREES_PER_RADIAN;
	}
	
	
	/** sets an opacity value for a color */
	public static Color alpha(Color c, double opacity)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
	}
	
	
	/** adds a fraction of color to the base, using perceptual intensity law */ 
	public static Color mix(Color base, Color add, double fraction)
	{
		if(fraction < 0)
		{
			return base;
		}
		else if(fraction >= 1.0)
		{
			return add;
		}
		
		if(base.isOpaque() && add.isOpaque())
		{
			double r = mix(base.getRed(), add.getRed(), fraction);
			double g = mix(base.getGreen(), add.getGreen(), fraction);
			double b = mix(base.getBlue(), add.getBlue(), fraction);
			return new Color(r, g, b, 1.0);
		}
		else
		{
			double baseOp = base.getOpacity();
			double addOp = add.getOpacity();
			
			double r = mix(base.getRed(), baseOp, add.getRed(), addOp, fraction);
			double g = mix(base.getGreen(), baseOp, add.getGreen(), addOp, fraction);
			double b = mix(base.getBlue(), baseOp, add.getBlue(), addOp, fraction);
			double a = baseOp * (1.0 - fraction) + addOp * fraction;
			return new Color(r, g, b, a);
		}
	}


	private static double mix(double a, double b, double fraction)
	{
		// using square law (gamma = 2)
		return limit(Math.sqrt((a * a) * (1.0 - fraction) + (b * b) * fraction));
	}
	

	private static double mix(double a, double opacityA, double b, double opacityB, double fraction)
	{
		// using square law (gamma = 2)
		// I am guessing with opacity values here
		return limit(Math.sqrt((a * a * opacityA) * (1.0 - fraction) + (b * b * opacityB) * fraction));
	}

	
	private static double limit(double c)
	{
		if(c < 0)
		{
			return 0;
		}
		else if(c >= 1.0)
		{
			return 1.0;
		}
		return c;
	}
	
	
	/** deiconify and toFront() */
	public static void toFront(Stage w)
	{
		if(w.isIconified())
		{
			w.setIconified(false);
		}
		
		w.toFront();
	}
	
	
	public static Image loadImage(Class<?> c, String resource)
	{
		return new Image(c.getResourceAsStream(resource));
	}


	/** permanently hides the table header */
	public static void hideHeader(TableView<?> t)
	{
		t.skinProperty().addListener((s, p, v) ->
		{
			Pane h = (Pane)t.lookup("TableHeaderRow");
			if(h.isVisible())
			{
				h.setMaxHeight(0);
				h.setMinHeight(0);
				h.setPrefHeight(0);
				h.setVisible(false);
			}
		});
	}
	
	
	/** sets a tool tip on the control. */
	public static void toolTip(Control n, Object tooltip)
	{
		if(tooltip == null)
		{
			n.setTooltip(null);
		}
		else if(tooltip instanceof Tooltip)
		{
			n.setTooltip((Tooltip)tooltip);
		}
		else
		{
			n.setTooltip(new Tooltip(tooltip.toString()));
		}
	}
	
	
	public static void storeSettings()
	{
		windowsFx.storeSettings();
	}
	
	
	public static ObservableValue toObservableValue(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof ObservableValue)
		{
			return (ObservableValue)x;
		}
		else
		{
			return new SimpleObjectProperty(x);
		}
	}
	
	
	public static double clip(double val, double min, double max)
	{
		if(val < min)
		{
			return min;
		}
		else if(val > max)
		{
			return max;
		}
		else
		{
			return val;
		}
	}
	

	/** adds or removes the specified style */
	public static void setStyle(Node n, CssStyle st, boolean on)
	{
		if(n == null)
		{
			return;
		}
		else if(st == null)
		{
			return;
		}
		
		String name = st.getName();
		ObservableList<String> ss = n.getStyleClass();
		if(on)
		{
			if(!ss.contains(name))
			{
				ss.add(st.getName());
			}
		}
		else
		{
			ss.remove(name);
		}
	}
	
	
	public static void setDisable(boolean on, Object ... nodes)
	{
		for(Object x: nodes)
		{
			if(x instanceof Node)
			{
				((Node)x).setDisable(on);
			}
			else if(x instanceof FxAction)
			{
				((FxAction)x).setDisabled(on);
			}
		}
	}
	
	
	/** adds a callback which will be invoked before any FxWindow gets shown */
	public static void addWindowMonitor(Consumer<FxWindow> monitor)
	{
		windowsFx.addWindowMonitor(monitor);
	}
	
	
	/** removes a window monitor */
	public static void removeWindowMonitor(Consumer<FxWindow> monitor)
	{
		windowsFx.removeWindowMonitor(monitor);
	}
	
	
	/** creates an instance of Insets(horizontal,vertical).  why there is not such a constructor you might ask? */
	public static Insets insets(double vertical, double horizontal)
	{
		return new Insets(vertical, horizontal, vertical, horizontal);
	}
	
	
	/** adds an invalidation listener to an observable */
	public static void listen(Runnable handler, Observable prop)
	{
		prop.addListener((src) -> handler.run());
	}
	
	
	/** adds an invalidation listener to an observable */
	public static void listen(Runnable handler, boolean fireImmediately, Observable prop)
	{
		prop.addListener((src) -> handler.run());
			
		if(fireImmediately)
		{
			handler.run();
		}
	}
	
	
	/** adds an invalidation listener to multiple observables */
	public static void listen(Runnable handler, Observable ... props)
	{
		for(Observable prop: props)
		{
			prop.addListener((src) -> handler.run());
		}
	}
	
	
	/** adds an invalidation listener to multiple observables */
	public static void listen(Runnable handler, boolean fireImmediately, Observable ... props)
	{
		for(Observable prop: props)
		{
			prop.addListener((src) -> handler.run());
		}
			
		if(fireImmediately)
		{
			handler.run();
		}
	}


	public static <T> ObservableList<T> observableArrayList()
	{
		return FXCollections.observableArrayList();
	}


	/** creates a fixed spacer */
	public static Region spacer(double size)
	{
		Region r = new Region();
		r.setMinSize(size, size);
		r.setMaxSize(size, size);
		r.setPrefSize(size, size);
		return r;
	}
	
	
	// from http://stackoverflow.com/questions/15593287/binding-textarea-height-to-its-content/19717901#19717901
	public static FxSize getTextBounds(TextArea t, double width)
	{
		if(helper == null)
		{
			helper = new Text();
		}
		
		String text = t.getText();
		if(width < 0)
		{
			// Note that the wrapping width needs to be set to zero before
			// getting the text's real preferred width.
			helper.setWrappingWidth(0);
		}
		else
		{
			helper.setWrappingWidth(width);
		}
		helper.setText(text);
		helper.setFont(t.getFont());
		Bounds r = helper.getLayoutBounds();
		
		Insets m = t.getInsets();
		Insets p = t.getPadding();
		double w = Math.ceil(r.getWidth() + m.getLeft() + m.getRight());
		double h = Math.ceil(r.getHeight() + m.getTop() + m.getBottom());
		
		return new FxSize(w, h);
	}
	

	/** requests focus in Platform.runLater() */
	public static void focusLater(Node n)
	{
		later(() -> n.requestFocus());
	}
	
	
	/** returns a parent of the specified type, or null.  if comp is an instance of the specified class, returns comp */
	public static <T> T getAncestorOfClass(Class<T> c, Node comp)
	{
		while(comp != null)
		{
			if(c.isInstance(comp))
			{
				return (T)comp;
			}
			
//			if(comp instanceof JPopupMenu)
//			{
//				if(comp.getParent() == null)
//				{
//					comp = ((JPopupMenu)comp).getInvoker();
//					continue;
//				}
//			}
			
			comp = comp.getParent();
		}
		return null;
	}
	
	
	public static List<Window> getWindows()
	{
		return FxHacks.get().getWindows();
	}
	
	
	/** attach a popup menu to the node */
	public static void setPopupMenu(Node owner, Supplier<FxPopupMenu> generator)
	{
		owner.setOnContextMenuRequested((ev) ->
		{
			if(generator != null)
			{
				FX.later(() ->
				{
					FxPopupMenu m = generator.get();
					if(m != null)
					{
						if(m.getItems().size() > 0)
						{
							// javafx does not dismiss the popup when the user
							// clicks on the owner node
							EventHandler<MouseEvent> li = new EventHandler<MouseEvent>()
							{
								public void handle(MouseEvent event)
								{
									m.hide();
									owner.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
								}
							};
							
							owner.addEventFilter(MouseEvent.MOUSE_PRESSED, li);
							m.show(owner, ev.getScreenX(), ev.getScreenY());
						}
					}
				});
			}
			ev.consume();
		});
	}
	
	
	public static void checkThread()
	{
		if(!Platform.isFxApplicationThread())
		{
			throw new Error("must be called from an FX application thread");
		}
	}


	public static void onKey(Node node, KeyCode code, FxAction a)
	{
		node.addEventHandler(KeyEvent.KEY_PRESSED, (ev) ->
		{
			if(ev.getCode() == code)
			{
				if(ev.isAltDown() || ev.isControlDown() || ev.isMetaDown() || ev.isShiftDown() || ev.isShortcutDown())
				{
					return;
				}
				else
				{
					a.action();
					ev.consume();
				}
			}
		});
	}
	
	
	public static <T> void addOneShotListener(Property<T> p, Consumer<T> c)
	{
		p.addListener(new ChangeListener<T>()
		{
			public void changed(ObservableValue<? extends T> observable, T old, T cur)
			{
				c.accept(cur);
				p.removeListener(this);
			}
		});
	}
	
	
	/** Prevents the node from being resized when the SplitPane is resized. */
	public static void preventSplitPaneResizing(Node nd)
	{
		SplitPane.setResizableWithParent(nd, Boolean.FALSE);
	}
	
	
	public static boolean isLeftButton(MouseEvent ev)
	{
		return (ev.getButton() == MouseButton.PRIMARY);
	}
	
	
	/** sometimes MouseEvent.isPopupTrigger() is not enough */
	public static boolean isPopupTrigger(MouseEvent ev)
	{
		if(ev.getButton() == MouseButton.SECONDARY)
		{
			if(CPlatform.isMac())
			{
				if
				(
					!ev.isAltDown() &&
					!ev.isMetaDown() &&
					!ev.isShiftDown()
				)
				{
					return true;
				}
			}
			else
			{
				if
				(
					!ev.isAltDown() &&
					!ev.isControlDown() &&
					!ev.isMetaDown() &&
					!ev.isShiftDown()
				)
				{
					return true;
				}
			}
		}
		return false;
	}


	public static void disableAlternativeRowColor(FxTable<?> table)
	{
		FX.style(table.table, CommonStyles.DISABLE_ALTERNATIVE_ROW_COLOR);
	}
	
	
	public static void disableAlternativeRowColor(TableView<?> table)
	{
		FX.style(table, CommonStyles.DISABLE_ALTERNATIVE_ROW_COLOR);
	}
	
	
	public static void disableAlternativeRowColor(ListView<?> v)
	{
		FX.style(v, CommonStyles.DISABLE_ALTERNATIVE_ROW_COLOR);
	}


	/** 
	 * returns a key code that represents a shortcut on this platform.
	 * why this functionality is not public in javafx is unclear to me.
	 */
	public static KeyCode getShortcutKeyCode()
	{
		KeyEvent ev = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", KeyCode.CONTROL, false, true, false, false);
		if(ev.isShortcutDown())
		{
			return KeyCode.CONTROL;
		}
		
		ev = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", KeyCode.META, false, false, false, true);
		if(ev.isShortcutDown())
		{
			return KeyCode.META;
		}
		
		ev = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", KeyCode.ALT, false, false, true, false);
		if(ev.isShortcutDown())
		{
			return KeyCode.ALT;
		}
		
		ev = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "", KeyCode.SHIFT, true, false, false, false);
		if(ev.isShortcutDown())
		{
			return KeyCode.SHIFT;
		}
		
		return null;
	}
}
