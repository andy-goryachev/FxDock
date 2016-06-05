// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.internal.WindowsFx;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;
import javafx.util.StringConverter;


/**
 * Making FX-ing easier.
 */
public final class FX
{
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
				default:
					throw new Error("?" + a);
				}
			}
			else if(a instanceof Insets)
			{
				n.setPadding((Insets)a);
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
					default:
						throw new Error("?" + a);
					}
				}
				else if(a instanceof Insets)
				{
					((Region)n).setPadding((Insets)a);
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
				else
				{
					throw new Error("?" + a);
				}
			}
		}
	}


	/** Creates a simple color background. */
	public static Background background(Color c)
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
}
