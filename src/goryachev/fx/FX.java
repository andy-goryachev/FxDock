// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CPlatform;
import goryachev.common.util.Disconnectable;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SystemTask;
import goryachev.fx.internal.CssTools;
import goryachev.fx.internal.DisconnectableIntegerListener;
import goryachev.fx.internal.FxSchema;
import goryachev.fx.internal.FxStyleHandler;
import goryachev.fx.internal.ParentWindow;
import goryachev.fx.internal.WindowsFx;
import goryachev.fx.table.FxTable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.TransformationList;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * Making JavaFX "easier".
 */
public final class FX
{
	protected static final Log log = Log.get("FX");
	public static final double TWO_PI = Math.PI + Math.PI;
	public static final double PI_2 = Math.PI / 2.0;
	public static final double DEGREES_PER_RADIAN = 180.0 / Math.PI;
	public static final double GAMMA = 2.2;
	public static final double ONE_OVER_GAMMA = 1.0 / GAMMA;
	private static WindowsFx windowsFx = new WindowsFx();
	private static Text helper;
	private static final Object PROP_TOOLTIP = new Object();

	
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
	
	
	/** 
	 * disables persisting of settings for this node only.  
	 * the LocalSettings, and the settings for its children will still be persisted.
	 */
	public static void setSkipSettings(Node n)
	{
		if(n != null)
		{
			FxSchema.setSkipSettings(n);
		}
	}
	
	
	/** 
	 * loads application windows stored in the global settings.  
	 * A window of specified defaultWindowType is created when the layout does not contain any windows saved,
	 * or when no window of the defaultWindowType was created.
	 * Generator:
	 * - for null argument (default window id) must return a non-null instance 
	 * - may return null window for a non-null window id
	 */ 
	public static void openWindows(Function<String,FxWindow> generator, Class<? extends FxWindow> defaultWindowType)
	{
		windowsFx.openWindows(generator, defaultWindowType);
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
	
	
	/** adds a style to a Styleable */
	public static void style(Styleable n, CssStyle style)
	{
		n.getStyleClass().add(style.getName());
	}
	
	
	/** adds or removes the specified style, depending on the condition */
	public static void style(Styleable n, boolean condition, CssStyle st)
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
		if(condition)
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
	
	
	/** removes styles from a Styleable */
	public static void removeStyles(Styleable n, CssStyle ... styles)
	{
		for(CssStyle st: styles)
		{
			n.getStyleClass().remove(st.getName());
		}
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
		if(c == null)
		{
			return null;
		}
		return new Background(new BackgroundFill(c, null, null));
	}
	
	
	public static Color gray(int col)
	{
		return Color.rgb(col, col, col);
	}
	
	
	public static Color gray(int col, double alpha)
	{
		return Color.rgb(col, col, col, alpha);
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
	
	
	/** shortcut for Platform.runLater() */
	public static void later(Runnable r)
	{
		Platform.runLater(r);
	}
	
	
	/** invokes Platform.runLater() after the specified delay */
	public static void later(int delay, Runnable r)
	{
		SystemTask.schedule(delay, () ->
		{
			Platform.runLater(r);
		});
	}
	
	
	/** execute in FX application thread directly if called from it, or in runLater() */
	public static void inFX(Runnable r)
	{
		if(Platform.isFxApplicationThread())
		{
			r.run();
		}
		else
		{
			FX.later(r);
		}
	}
	
	
	/** alias for Platform.isFxApplicationThread() */
	public static boolean isFX()
	{
		return Platform.isFxApplicationThread();
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
	
	
	/** swing invokeAndWait() analog.  if called from an FX application thread, simply invokes the producer. */
	public static void invokeAndWait(Runnable action) throws Exception
	{
		if(Platform.isFxApplicationThread())
		{
			action.run();
		}
		else
		{
			FutureTask<Boolean> t = new FutureTask<>(() ->
			{
				action.run();
				return Boolean.TRUE;
			});
			FX.later(t);
			t.get();
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
	
	
	public static String getName(Node n)
	{
		return FxSchema.getName(n);
	}
	
	
	/** 
	 * attaches a handler to be notified when settings for the node have been loaded.  
	 * setting null clears the handler 
	 */
	public static void setOnSettingsLoaded(Node n, Runnable r)
	{
		FxSchema.setOnSettingsLoaded(n, r);
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
	
	
	/** 
	 * adds a fraction of color to the base, using standard gamma value 
	 * https://en.wikipedia.org/wiki/Alpha_compositing
	 */ 
	public static Color mix(Color base, Color over, double fraction)
	{
		if(fraction <= 0.0)
		{
			return base;
		}
		
		if(base == null)
		{
			if(over == null)
			{
				return null;
			}
			else
			{
				return new Color(over.getRed(), over.getGreen(), over.getBlue(), over.getOpacity() * fraction);
			}
		}
		else if(over == null)
		{
			return base;
		}

		if(base.isOpaque())
		{
			if(over.isOpaque())
			{
				// simplified case of both colors opaque 
				double r = mix(base.getRed(), over.getRed(), fraction);
				double g = mix(base.getGreen(), over.getGreen(), fraction);
				double b = mix(base.getBlue(), over.getBlue(), fraction);
				return new Color(r, g, b, 1.0);
			}
		}
		
		// full alpha blending
		double opacityBase = base.getOpacity();
		double opacityOver = clip(over.getOpacity() * fraction);

		double alpha = opacityOver + (opacityBase * (1.0 - opacityOver));
		if(alpha < 0.00001)
		{
			return new Color(0, 0, 0, 0);
		}
		
		double r = mix(base.getRed(), opacityBase, over.getRed(), opacityOver, alpha);
		double g = mix(base.getGreen(), opacityBase, over.getGreen(), opacityOver, alpha);
		double b = mix(base.getBlue(), opacityBase, over.getBlue(), opacityOver, alpha);
		return new Color(r, g, b, alpha);
	}


	private static double mix(double base, double over, double fraction)
	{
		double v = Math.pow(over, GAMMA) * fraction + Math.pow(base, GAMMA) * (1.0 - fraction);
		v = Math.pow(v, ONE_OVER_GAMMA);
		return clip(v);
	}
	

	private static double mix(double base, double opacityBase, double over, double opacityOver, double alpha)
	{
		double v = Math.pow(over, GAMMA) * opacityOver + Math.pow(base, GAMMA) * (1.0 - opacityOver);
		v = v / alpha;
		v = Math.pow(v, ONE_OVER_GAMMA);
		return clip(v);
	}
	
	
	public static Color mix(Color[] colors, double gamma)
	{
		int sz = colors.length;
		
		double red = 0.0;
		double green = 0.0;
		double blue = 0.0;
		
		for(int i=0; i<sz; i++)
		{
			Color c = colors[i];
			double op = c.getOpacity();
			
			double r = c.getRed();
			red += (Math.pow(r, gamma) * op);
			
			double g = c.getGreen();
			green += (Math.pow(g, gamma) * op);
			
			double b = c.getBlue();
			blue += (Math.pow(b, gamma) * op);
		}
		
		double oneOverGamma = 1.0 / gamma;
		red = clip(Math.pow(red / sz, oneOverGamma));
		green = clip(Math.pow(green / sz, oneOverGamma));
		blue = clip(Math.pow(blue / sz, oneOverGamma));

		return Color.color(red, green, blue);
	}

	
	public static Color mix(Color[] colors)
	{
		return mix(colors, GAMMA);
	}
	

	private static double clip(double c)
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

	
	/** sets a tool tip on the control. */
//	@Deprecated
//	public static void setTooltip(Control n, Object tooltip)
//	{
//		if(tooltip == null)
//		{
//			n.setTooltip(null);
//		}
//		else if(tooltip instanceof Tooltip)
//		{
//			n.setTooltip((Tooltip)tooltip);
//		}
//		else
//		{
//			n.setTooltip(new Tooltip(tooltip.toString()));
//		}
//	}
	
	
	/** attaches or removes (text=null) the Node's tooltip */
	public static void setTooltip(Node n, String text)
	{
		if(n != null)
		{
			if(text == null)
			{
				Tooltip t = getTooltip(n);
				Tooltip.uninstall(n, t);
				n.getProperties().remove(PROP_TOOLTIP);
			}
			else
			{
				Tooltip t = new Tooltip(text);
				Tooltip.install(n, t);
				n.getProperties().put(PROP_TOOLTIP, t);
			}
		}
	}
	
	
	private static Tooltip getTooltip(Node n)
	{
		if(n != null)
		{
			return (Tooltip)n.getProperties().get(PROP_TOOLTIP);
		}
		return null;
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
	

	public static <T> ObservableList<T> observableArrayList()
	{
		return FXCollections.observableArrayList();
	}
	
	
	public static <K,V> ObservableMap<K,V> observableHashMap()
	{
		return FXCollections.observableHashMap();
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
	public static FxSize getTextBounds(TextArea textArea, double targetWidth)
	{
		String text = textArea.getText();
		Font f = textArea.getFont();

		Bounds r = computeTextBounds(text, f, targetWidth);
		
		Insets m = textArea.getInsets();
		Insets p = textArea.getPadding();
		double w = Math.ceil(r.getWidth() + m.getLeft() + m.getRight());
		double h = Math.ceil(r.getHeight() + m.getTop() + m.getBottom());
		
		return new FxSize(w, h);
	}
	
	
	public static Bounds computeTextBounds(String text, Font f)
	{
		return computeTextBounds(text, f, -1);
	}
	
	
	public static Bounds computeTextBounds(String text, Font f, double targetWidth)
	{
		if(helper == null)
		{
			helper = new Text();
		}

		if(targetWidth < 0)
		{
			// Note that the wrapping width needs to be set to zero before
			// getting the text's real preferred width.
			helper.setWrappingWidth(0);
		}
		else
		{
			helper.setWrappingWidth(targetWidth);
		}
		
		helper.setText(text);
		helper.setFont(f);

		return helper.getLayoutBounds();
	}

	

	/** requests focus in Platform.runLater() */
	public static void focusLater(Node n)
	{
		later(() -> n.requestFocus());
	}
	
	
	/** returns a parent of the specified type, or null.  if comp is an instance of the specified class, returns comp */
	public static <T> T getAncestorOfClass(Class<T> c, Node node)
	{
		if(Window.class.isAssignableFrom(c))
		{
			Scene sc = node.getScene();
			if(sc != null)
			{
				Window w = sc.getWindow();
				while(w != null)
				{
					if(w.getClass().isAssignableFrom(c))
					{
						return (T)w;
					}
					
					// the window can be a dialog, check the owner
					if(w instanceof Stage)
					{
						Stage stage = (Stage)w;
						w = stage.getOwner();
					}
				}
			}
			return null;
		}
		else
		{
			while(node != null)
			{
				if(c.isInstance(node))
				{
					return (T)node;
				}
				
	//			if(comp instanceof JPopupMenu)
	//			{
	//				if(comp.getParent() == null)
	//				{
	//					comp = ((JPopupMenu)comp).getInvoker();
	//					continue;
	//				}
	//			}
				
				node = node.getParent();
			}
		}
		return null;
	}
	
	
	/** 
	 * attaches a double click handler to a node.
	 */
	public static void onDoubleClick(Node owner, Runnable handler)
	{
		if(owner == null)
		{
			throw new NullPointerException("cannot attach a double click handler to null");
		}
		
		owner.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) ->
		{
			if(ev.getClickCount() == 2)
			{
				if(ev.getButton() == MouseButton.PRIMARY)
				{
					handler.run();
				}
			}
		});
	}
	
	
	/** 
	 * attach a popup menu to a node.
	 * WARNING: sometimes, as the case is with TableView/FxTable header, 
	 * the requested node gets created by the skin at some later time.
	 * In this case, additional dance must be performed, see for example
	 * FxTable.setHeaderPopupMenu()   
	 */
	public static void setPopupMenu(Node owner, Supplier<ContextMenu> generator)
	{
		if(owner == null)
		{
			throw new NullPointerException("cannot attach popup menu to null");
		}
		
		owner.setOnContextMenuRequested((ev) ->
		{
			if(generator != null)
			{
				ContextMenu m = generator.get();
				if(m != null)
				{
					if(m.getItems().size() > 0)
					{
						FX.later(() ->
						{
							// javafx does not dismiss the popup when the user
							// clicks on the owner node
							EventHandler<MouseEvent> li = new EventHandler<MouseEvent>()
							{
								public void handle(MouseEvent event)
								{
									m.hide();
									owner.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
									event.consume();
								}
							};
							
							owner.addEventFilter(MouseEvent.MOUSE_PRESSED, li);
							m.show(owner, ev.getScreenX(), ev.getScreenY());
						});
						ev.consume();
					}
				}
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
					a.invokeAction();
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
	
	
	/** adds a ChangeListener to the specified ObservableValue(s) */
	public static Disconnectable onChange(Runnable callback, ObservableValue<?> ... props)
	{
		return onChange(callback, false, props);
	}
	
	
	/** adds a ChangeListener to the specified ObservableValue(s) */
	public static Disconnectable onChange(Runnable callback, boolean fireImmediately, ObservableValue<?> ... props)
	{
		FxChangeListener li = new FxChangeListener(callback);
		li.listen(props);
		
		if(fireImmediately)
		{
			li.fire();
		}
		
		return li;
	}
	
	
	/** adds a ChangeListener to the specified ObservableValue(s).  The callback will be invokedLater() */
	public static Disconnectable onChangeLater(Runnable callback, ObservableValue<?> ... props)
	{
		FxChangeListener li = new FxChangeListener(callback)
		{
			@Override
			protected void invokeCallback()
			{
				later(() ->
				{
					super.invokeCallback();
				});
			}
		};

		li.listen(props);
		
		return li;
	}
	
	
	/** adds a ListChangeListener to the specified ObservableValue(s) */
	public static void onChange(Runnable handler, ObservableList<?> list)
	{
		onChange(handler, false, list);
	}
	
	
	/** adds a ListChangeListener to the specified ObservableValue(s) */
	public static void onChange(Runnable handler, boolean fireImmediately, ObservableList list)
	{
		list.addListener((Change ch) -> handler.run());
		
		if(fireImmediately)
		{
			handler.run();
		}
	}
	
	
	/** adds an invalidation listener to an observable */
	public static void onInvalidation(Runnable handler, Observable prop)
	{
		prop.addListener((src) -> handler.run());
	}
	
	
	/** adds an invalidation listener to an observable */
	public static void onInvalidation(Runnable handler, boolean fireImmediately, Observable prop)
	{
		prop.addListener((src) -> handler.run());
			
		if(fireImmediately)
		{
			handler.run();
		}
	}
	
	
	/** adds an invalidation listener to multiple observables */
	public static void onInvalidation(Runnable handler, Observable ... props)
	{
		for(Observable prop: props)
		{
			prop.addListener((src) -> handler.run());
		}
	}
	
	
	/** adds an invalidation listener to multiple observables */
	public static void onInvalidation(Runnable handler, boolean fireImmediately, Observable ... props)
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


	/** converts non-null Color to #RRGGBBAA */
	public static String toFormattedColor(Color c)
	{
        int r = CKit.round(c.getRed() * 255.0);
        int g = CKit.round(c.getGreen() * 255.0);
        int b = CKit.round(c.getBlue() * 255.0);
        int a = CKit.round(c.getOpacity() * 255.0);
		return String.format("#%02X%02X%02X%02X", r, g, b, a);
	}
	
	
	/** converts non-null Color to #RRGGBB */
	public static String toFormattedColorRGB(Color c)
	{
        int r = CKit.round(c.getRed() * 255.0);
        int g = CKit.round(c.getGreen() * 255.0);
        int b = CKit.round(c.getBlue() * 255.0);
		return String.format("#%02X%02X%02X", r, g, b);
	}


	public static boolean isParentWindowVisible(Node n)
	{
		if(n == null)
		{
			return false;
		}
		
		Scene s = n.getScene();
		if(s == null)
		{
			return false;
		}
		
		Window w = s.windowProperty().get();
		if(w == null)
		{
			return false;
		}
		
		return w.isShowing();
	}
	
	
	/** returns a read-only property that tracks parent window of a Node */
	public static  ReadOnlyObjectProperty<Window> parentWindowProperty(Node n)
	{
		return new ParentWindow(n).windowProperty();
	}
	
	
	/** avoid ambiguous signature warning when using addListener */
	public static <T> void addChangeListener(ObservableList<T> list, ListChangeListener<? super T> li)
	{
		list.addListener(li);
	}
	
	
	/** avoid ambiguous signature warning when using addListener */
	public static <T> void addChangeListener(ObservableList<T> list, Runnable callback)
	{
		list.addListener((ListChangeListener.Change<? extends T> ch) -> callback.run());
	}
	
	
	/** avoid ambiguous signature warning when using addListener */
	public static <T> void addChangeListener(ObservableList<T> list, boolean fireImmediately, Runnable callback)
	{
		list.addListener((ListChangeListener.Change<? extends T> ch) -> callback.run());
		
		if(fireImmediately)
		{
			callback.run();
		}
	}
	

	public static <T> void addChangeListener(ObservableValue<T> prop, ChangeListener<? super T> li)
	{
		prop.addListener(li);
	}
	
	
	/** simplified version of addChangeListener that only accepts the current value */
	public static <T> void addChangeListener(ObservableValue<T> prop, Consumer<? super T> li)
	{
		prop.addListener((s,p,current) -> li.accept(current));
	}
	
	
	/** simplified version of addChangeListener that only invokes the callback on change */
	public static <T> void addChangeListener(ObservableValue<T> prop, Runnable callback)
	{
		prop.addListener((s,p,current) -> callback.run());
	}
	
	
	/** simplified version of addChangeListener that only invokes the callback on change */
	public static <T> void addChangeListener(ObservableValue<T> prop, boolean fireImmediately, Runnable callback)
	{
		prop.addListener((s,p,current) -> callback.run());
		
		if(fireImmediately)
		{
			callback.run();
		}
	}
	
	
	/** simplified version of addChangeListener that only accepts the current value */
	public static <T> void addChangeListener(ObservableValue<T> prop, boolean fireImmediately, Consumer<? super T> li)
	{
		prop.addListener((s,p,current) -> li.accept(current));
		
		if(fireImmediately)
		{
			li.accept(prop.getValue());
		}
	}
	

	/** converts java fx Color to a 32 bit RGBA integer */
	public static Integer toRGBA(Color c)
	{
        int r = (int)Math.round(c.getRed() * 255.0);
        int g = (int)Math.round(c.getGreen() * 255.0);
        int b = (int)Math.round(c.getBlue() * 255.0);
        int a = (int)Math.round(c.getOpacity() * 255.0);
		return r | (g << 8) | (b << 16) | (a << 24);
	}
	
	
	/** copies text to clipboard.  does nothing if text is null */
	public static void copy(String text)
	{
		if(text != null)
		{
			ClipboardContent cc = new ClipboardContent();
            cc.putString(text);
            Clipboard.getSystemClipboard().setContent(cc);
		}
	}
	
	
	public static Insets insets(double top, double right, double bottom, double left)
	{
		return new Insets(top, right, bottom, left);
	}
	
	
	public static Insets insets(double vert, double hor)
	{
		return new Insets(vert, hor, vert, hor);
	}
	
	
	public static Insets insets(double gap)
	{
		return new Insets(gap);
	}
	
	
	// FIX own file
	public static <S,T> void bindContentWithTransform(ObservableList<? extends S> source, ObservableList<T> target, Function<S,T> converter)
	{
		ListContentBinding.bind(source, target, converter);
	}
	
	
	private static class ListContentBinding<S,T>
		implements ListChangeListener<S>, WeakListener
	{
		private final Function<S,T> converter;
		private final WeakReference<List<T>> ref;
		

		public ListContentBinding(List<T> target, Function<S,T> converter)
		{
			this.ref = new WeakReference<List<T>>(target);
			this.converter = converter;
		}
		
		
		public static <S,T> void bind(ObservableList<? extends S> source, ObservableList<T> target, Function<S,T> converter)
		{
			ListContentBinding<S,T> li = new ListContentBinding<S,T>(target, converter);
			target.setAll(transform(source, converter));
			source.removeListener(li);
			source.addListener(li);
		}
		
		
		protected T transform(S item)
		{
			return converter.apply(item);
		}
		
		
		protected static <S,T> List<T> transform(List<? extends S> items, Function<S,T> converter)
		{
			int sz = items.size();
			CList<T> rv = new CList<T>(sz);
			for(int i=0; i<sz; i++)
			{
				S item = items.get(i);
				T val = converter.apply(item);
				rv.add(val);
			}
			return rv;
		}


		@Override
		public void onChanged(Change<? extends S> ch)
		{
			List<T> target = ref.get();
			if(target == null)
			{
				ch.getList().removeListener(this);
				return;
			}
			
			while(ch.next())
			{
				if(ch.wasPermutated())
				{
					target.subList(ch.getFrom(), ch.getTo()).clear();
					target.addAll(ch.getFrom(), transform(ch.getList().subList(ch.getFrom(), ch.getTo()), converter));
				}
				else
				{
					if(ch.wasRemoved())
					{
						target.subList(ch.getFrom(), ch.getFrom() + ch.getRemovedSize()).clear();
					}
					
					if(ch.wasAdded())
					{
						target.addAll(ch.getFrom(), transform(ch.getAddedSubList(), converter));
					}
				}
			}
		}


		@Override
		public boolean wasGarbageCollected()
		{
			return ref.get() == null;
		}


		@Override
		public int hashCode()
		{
			Object me = ref.get();
			if(me == null)
			{
				return 0;
			}
			return me.hashCode();
		}


		@Override
		public boolean equals(Object x)
		{
			if(this == x)
			{
				return true;
			}

			Object me = ref.get();
			if(me == null)
			{
				return false;
			}
			else if(x instanceof ListContentBinding)
			{
				return me == ((ListContentBinding)x).ref.get();
			}
			else
			{
				return false;
			}
		}
	}
	
	
	/** returns an instance of TransformationList wrapped around the source ObservableList */
	public static <S,T> ObservableList<T> transform(ObservableList<S> source, Function<S,T> converter)
	{
		return new TransformationList<T,S>(source)
		{
			public int getSourceIndex(int index)
			{
				return index;
			}
			
			
			public int getViewIndex(int index)
			{
				return index;
			}


			public T get(int index)
			{
				S src = getSource().get(index);
				return converter.apply(src);
			}


			public int size()
			{
				return getSource().size();
			}
			
			
			protected void sourceChanged(Change<? extends S> c)
			{
				fireChange(new Change<T>(this)
				{
					public List<T> getRemoved()
					{
						ArrayList<T> rv = new ArrayList<>(c.getRemovedSize());
						for(S item: c.getRemoved())
						{
							rv.add(converter.apply(item));
						}
						return rv;
					}
					

					public boolean wasAdded()
					{
						return c.wasAdded();
					}


					public boolean wasRemoved()
					{
						return c.wasRemoved();
					}


					public boolean wasReplaced()
					{
						return c.wasReplaced();
					}


					public boolean wasUpdated()
					{
						return c.wasUpdated();
					}


					public boolean wasPermutated()
					{
						return c.wasPermutated();
					}


					public int getPermutation(int ix)
					{
						return c.getPermutation(ix);
					}


					protected int[] getPermutation()
					{
						return new int[0];
					}


					public int getFrom()
					{
						return c.getFrom();
					}


					public int getTo()
					{
						return c.getTo();
					}


					public boolean next()
					{
						return c.next();
					}


					public void reset()
					{
						c.reset();
					}
				});
			}
		};
	}
	
	
	/** returns the first window of the specified type, or null */ 
	public static <T extends FxWindow> T findFirstWindowOfType(Class<T> type, boolean exact)
	{
		for(Window w: Window.getWindows())
		{
			if(exact)
			{
				if(w.getClass() == type)
				{
					return (T)w;
				}
			}
			else
			{
				if(type.isAssignableFrom(w.getClass()))
				{
					return (T)w;
				}
			}
		}
		return null;
	}
	
	
	/** 
	 * guarantees to open a single instance of the specified type:
	 * - creates a new instance (using the specified generator) if no instance is currently opened
	 * - restores and brings to focus an existing instance
	 */
	public static <T extends FxWindow> T openSingleWindow(Class<T> type, Supplier<T> gen)
	{
		FX.checkThread();
		
		T w = findFirstWindowOfType(type, true);
		if(w == null)
		{
			w = gen.get();
			w.open();
		}
		else
		{
			if(w.isIconified())
			{
				w.setIconified(false);
			}
		}
		
		w.requestFocus();
		return w;
	}
	
	
	/** 
	 * guarantees to open a single instance of the specified type:
	 * - creates a new instance (using a no-arg constructor) if no instance is currently opened
	 * - restores and brings to focus an existing instance
	 */
	public static <T extends FxWindow> T openSingleWindow(Class<T> type)
	{
		return openSingleWindow(type, () -> 
		{
			try
			{
				return type.newInstance();
			}
			catch(Throwable e)
			{
				log.error(e);
				throw new Error(type + " must declare a no-arg constructor", e);
			}
		});
	}
	
	
	/** returns a boolean property which indicates whether a node is visible in a scene */
	public static ReadOnlyBooleanProperty getNodeVisibleInSceneProperty(Node node)
	{
		ReadOnlyBooleanWrapper showing = new ReadOnlyBooleanWrapper();

		ChangeListener<Window> windowChangeListener = (s,p,win) ->
		{
			showing.unbind();
			
			if(win != null)
			{
				showing.bind(win.showingProperty());
			}
			else
			{
				showing.set(false);
			}
		};

		ChangeListener<Scene> sceneChangeListener = (s,prevScene,currScene) ->
		{
			showing.unbind();
			
			if(prevScene != null)
			{
				prevScene.windowProperty().removeListener(windowChangeListener);
			}
			
			if(currScene == null)
			{
				showing.set(false);
			}
			else
			{
				currScene.windowProperty().addListener(windowChangeListener);
				
				if(currScene.getWindow() == null)
				{
					showing.set(false);
				}
				else
				{
					showing.bind(currScene.getWindow().showingProperty());
				}
			}
		};

		node.sceneProperty().addListener(sceneChangeListener);
		
		Scene scene = node.getScene();
		if(scene == null)
		{
			showing.set(false);
		}
		else
		{
			scene.windowProperty().addListener(windowChangeListener);
			
			Window w = scene.getWindow();
			if(w == null)
			{
				showing.set(false);
			}
			else
			{
				showing.bind(w.showingProperty());
			}
		}

		return showing.getReadOnlyProperty();
	}
	
	
	public static void onMousePressed(Node n, Runnable action)
	{
		n.addEventHandler(MouseEvent.MOUSE_PRESSED, (ev) ->
		{
			action.run();
		});
	}


	public static void openFile(File file)
	{
		String uri = file.toURI().toString();
		FxApplication.getInstance().getHostServices().showDocument(uri);
	}


	public static Disconnectable onChange(ReadOnlyIntegerProperty prop, IntConsumer onChange)
	{
		return new DisconnectableIntegerListener(prop, onChange);
	}
	
	
	/** adds a new style */
	public static void setStyle(Node n, String property, Object value)
	{
		if(n != null)
		{
			String s = n.getStyle();
			FxStyleHandler m = new FxStyleHandler(s);
			m.put(property, value);
			String s2 = m.toStyleString();
			n.setStyle(s2);
		}
	}
	
	
	public static void removeStyle(Node n, String property)
	{
		if(n != null)
		{
			String s = n.getStyle();
			FxStyleHandler m = new FxStyleHandler(s);
			m.remove(property);
			String s2 = m.toStyleString();
			n.setStyle(s2);
		}
	}
	
	
	/** applies global stylesheet on top of the javafx one */
	public static void applyStyleSheet(String old, String cur)
	{
		for(Window w: Window.getWindows())
		{
			applyStyleSheet(w, old, cur);
		}
	}
	
	
	/** applies global stylesheet to a specific window on top of the javafx one */
	public static void applyStyleSheet(Window w, String old, String cur)
	{
		if(cur != null)
		{
			Scene scene = w.getScene();
			if(scene != null)
			{
				if(old != null)
				{
					scene.getStylesheets().remove(old);
				}
				
				scene.getStylesheets().add(cur);
			}			
		}
	}
}
