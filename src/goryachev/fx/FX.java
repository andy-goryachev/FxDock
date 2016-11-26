// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.internal.WindowsFx;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
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
	
	
	public static FxWindow getWindow(Node n)
	{
		Window w = n.getScene().getWindow();
		if(w instanceof FxWindow)
		{
			return (FxWindow)w;
		}
		return null;
	}
	
	
	public static void storeSettings(Node n)
	{
		FxWindow w = getWindow(n);
		storeSettings(w);
	}
	
	
	public static void restoreSettings(Node n)
	{
		FxWindow w = getWindow(n);
		restoreSettings(w);
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
	
	
	public static CAction exitAction()
	{
		return windowsFx.exitAction();
	}
	
	
	/** creates a label */
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
					n.getStyleClass().add(CommonStyles.BOLD.getName());
					break;
				case FOCUSABLE:
					n.setFocusTraversable(true);
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
					n.getStyleClass().add(CommonStyles.BOLD.getName());
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
						n.getStyleClass().add(CommonStyles.BOLD.getName());
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
						((Labeled)n).setWrapText(true);
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
	
	
	/** Creates Color from an RGB value. */
	public static Color rgb(int rgb)
	{
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >>  8) & 0xff;
		int b = (rgb      ) & 0xff;
		return Color.rgb(r, g, b);
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
	
	
	/** returns parent window or null */
	public static Window getParentWindow(Node n)
	{
		if(n != null)
		{
			Scene s = n.getScene();
			if(s != null)
			{
				return s.getWindow();
			}
		}
		return null;
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
	
	
	/** bind a property to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, Property<T> p)
	{
		windowsFx.bindings(n, true).add(subKey, p, null);
	}
	
	
	/** bind an object with settings to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, HasSettings x)
	{
		windowsFx.bindings(n, true).add(subKey, x);
	}
	
	
	/** bind a property to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, Property<T> p, StringConverter<T> c)
	{
		windowsFx.bindings(n, true).add(subKey, p, c);
	}
	
	
	/** bind a property to be saved as part of FxWindow settings using the specified subkey */
	public static <T> void bind(Node n, String subKey, Property<T> p, SSConverter<T> c)
	{
		windowsFx.bindings(n, true).add(subKey, c, p);
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
	public static Color alpha(Color c, double alpha)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
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
	public static void tooltip(Control n, Object tooltip)
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
}
