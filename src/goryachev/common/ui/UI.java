// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.ZeroFrame;
import goryachev.common.ui.theme.InsetsResource;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CPlatform;
import goryachev.common.util.Clearable;
import goryachev.common.util.Log;
import goryachev.common.util.Obj;
import goryachev.common.util.Rex;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.plaf.UIResource;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


/** common UI tools */
public class UI
{
	public static final String COPYRIGHT = "Copyright (c) 1996-2016 Andy Goryachev <andy@goryachev.com>  All Rights Reserved.";
	public static final int ALT = InputEvent.ALT_DOWN_MASK;
	public static final int CTRL = InputEvent.CTRL_DOWN_MASK;
	public static final int SHIFT = InputEvent.SHIFT_DOWN_MASK;
	private static final String HTML_DISABLE_CLIENT_PROPERTY = "html.disable";
	private static final Obj KEY_TABLE_HEADER_HIGHLIGHT = new Obj("KEY_TABLE_HEADER_HIGHLIGHT");
	private static CPopupMenuController defaultPopupMenuController;
	
	
	public static void resize(Component c, Point location, Dimension dimension)
	{
		if(dimension != null)
		{
			c.setSize(dimension);
		}
		
		if(location != null)
		{
			// recall last window position, making sure it's within display area  
			Rectangle bounds = c.getGraphicsConfiguration().getBounds();
			if(location.x > bounds.width-c.getWidth())
			{
				location.x = bounds.width-c.getWidth();
			}
			if(location.y > bounds.height-c.getHeight())
			{
				location.y = bounds.height-c.getHeight();
			}
			c.setLocation(location.x,location.y);
		}
	}

	
	/** mixes colors using intensity values (RGB squared) */
	public static Color mix(Color a, double fractionA, Color b)
	{
		if(fractionA < 0.0)
		{
			fractionA = 0.0;
		}
		else if(fractionA > 1.0)
		{
			// assume it's 0..255 for compatibility with legacy code
			fractionA /= 255;
			
			if(fractionA > 1.0)
			{
				fractionA = 1.0;
			}
		}

		if((a.getAlpha() == 255) && (b.getAlpha() == 255))
		{
			// no transparency
			return new Color
			(
				mixPrivate(a.getRed(), fractionA, b.getRed()),
				mixPrivate(a.getGreen(), fractionA, b.getGreen()),
				mixPrivate(a.getBlue(), fractionA, b.getBlue())
			);
		}
		else
		{
			double aa = a.getAlpha() / 255f;
			double ba = b.getAlpha() / 255f;
			
			return new Color
			(
				mixPrivate(a.getRed(), aa, fractionA, b.getRed(), ba),
				mixPrivate(a.getGreen(), aa, fractionA, b.getGreen(), ba),
				mixPrivate(a.getBlue(), aa, fractionA, b.getBlue(), ba),
				limit(CKit.round(255 * (aa * fractionA + ba * (1 - fractionA))))
			);
		}
	}
	
	
	private static int mixPrivate(int a, double fractionA, int b)
	{
		int c = CKit.round(Math.sqrt((a * a) * fractionA + (b * b) * (1 - fractionA)));
		return limit(c);
	}
	
	
	private static int mixPrivate(int a, double alphaA, double fractionA, int b, double alphaB)
	{
		int c = CKit.round(Math.sqrt((a * a) * fractionA + (b * b) * (1 - fractionA)));
		return limit(c);
	}
	
	
	private static int limit(int x)
	{
		if(x < 0)
		{
			return 0;
		}
		else if(x > 255)
		{
			return 255;
		}
		else
		{
			return x;
		}
	}


	public static Color setAlpha(Color c, int alpha)
	{
		if(c != null)
		{
			alpha = (c.getAlpha() * alpha) / 255;
			return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
		}
		return null;
	}


	public static Color setAlpha(Color c, double alpha)
	{
		if(c != null)
		{
			int a = (int)Math.round(c.getAlpha() * alpha);
			return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
		}
		return null;
	}
	
	
	public static void placeAtScreenCenter(Window c, int width, int height)
	{
		c.setSize(width,height);
		
		Rectangle bounds = c.getGraphicsConfiguration().getBounds();

		if(width > bounds.width)
		{
			width = bounds.width;
		}
		if(height > bounds.height)
		{
			height = bounds.height;
		}
		c.setSize(width,height);
		
		int x = (bounds.width - width)/2;
		int y = (bounds.height - height)/2;
		
		c.setLocation(x,y);
	}
	
	
	public static void placeAtScreenCenter(JFrame c, double percent)
	{
		Rectangle bounds = c.getGraphicsConfiguration().getBounds();
		int width = (int)(bounds.width * percent);
		int height = (int)(bounds.height * percent);
		placeAtScreenCenter(c,width,height);
	}


	public static void placeAtScreenCenter(JFrame c)
	{
		placeAtScreenCenter(c, c.getWidth(), c.getHeight());
	}
	

	public static Frame getParentFrame(Component c)
	{
		if(c instanceof Frame)
		{
			return (Frame)c;
		}
		
		return (Frame)SwingUtilities.getAncestorOfClass(Frame.class, c);
	}
	

	public static JFrame getParentJFrame(Component c)
	{
		if(c instanceof JFrame)
		{
			return (JFrame)c;
		}
		
		return (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, c);
	}
	

	public static Window getParentWindow(Component c)
	{
		if(c instanceof Window)
		{
			return (Window)c;
		}

		Window w = (Window)SwingUtilities.getAncestorOfClass(Window.class, c);
		if(w == null)
		{
			w = CFocusMonitor.getLastWindow();
		}
		return w;
	}
	
	
	public static <T extends Container> T getAncestorOfClass(Class<T> c, Component comp)
	{
		if(comp == null)
		{
			return null;
		}
		
		if(c.isAssignableFrom(comp.getClass()))
		{
			return (T)comp;
		}
		
		return (T)SwingUtilities.getAncestorOfClass(c, comp);
	}
	
	
	public static boolean isParent(Container parent, Component c)
	{
		if(parent == null)
		{
			return false;
		}
		else if(c == null)
		{
			return false;
		}
		
		if(c == parent)
		{
			return true;
		}
		
		for(Container p=c.getParent(); p != null; p = p.getParent())
		{
			if(p == parent)
			{
				return true;
			}
		}
		
		return false;
	}

	
	private static void setAction(Container comp, Action a, int condition, KeyStroke k)
	{
		if(k != null)
		{
			JComponent c;
			if(comp instanceof JComponent)
			{
				c = (JComponent)comp;
			}
			else if(comp instanceof RootPaneContainer)
			{
				c = ((RootPaneContainer)comp).getRootPane();
			}
			else
			{
				throw new Rex();
			}
			
			if(a == null)
			{
				c.getInputMap(condition).remove(k);
			}
			else
			{
				c.getActionMap().put(a, a);
				c.getInputMap(condition).put(k, a);
			}
		}
	}


	// KeyEvent.VK_Z, InputEvent.CTRL_MASK
	public static void whenFocused(Container c, int keyCode, int mask, Action a)
	{
		setAction(c, a, JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(keyCode, mask));
	}
	

	public static void whenFocused(Container c, KeyStroke k, Action a)
	{
		setAction(c, a, JComponent.WHEN_FOCUSED, k);
	}


	public static void whenFocused(Container c, int keyCode, Action a)
	{
		setAction(c, a, JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(keyCode, 0));
	}


	public static void whenInFocusedWindow(Container c, int keyCode, int mask, Action a)
	{
		setAction(c, a, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyStroke.getKeyStroke(keyCode, mask));
	}


	public static void whenInFocusedWindow(Container c, int keyCode, Action a)
	{
		setAction(c, a, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyStroke.getKeyStroke(keyCode, 0));
	}
	
	
	public static void whenInFocusedWindow(Container c, KeyStroke k, Action a)
	{
		setAction(c, a, JComponent.WHEN_IN_FOCUSED_WINDOW, k);
	}


	public static void whenAncestorOfFocusedComponent(Container c, int keyCode, int mask, Action a)
	{
		setAction(c, a, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyStroke.getKeyStroke(keyCode, mask));
	}


	public static void whenAncestorOfFocusedComponent(Container c, int keyCode, Action a)
	{
		setAction(c, a, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyStroke.getKeyStroke(keyCode, 0));
	}


	public static void paintTiledIcon(Component c, Graphics g, ImageIcon ic, int x, int y, int width, int height, int xphase, int yphase)
	{
		Image img = ic.getImage();
		int cx = x - (xphase % ic.getIconWidth());
		int cy = y - (yphase % ic.getIconHeight());
		do
		{
			int px = cx;
			do
			{
				g.drawImage(img, px, cy, c);
				px += ic.getIconWidth();
			} while(px < width);

			cy += ic.getIconHeight();
			
		} while(cy < height);
	}


	public static void disableHtml(JComponent c)
	{
		setHtmlEnabled(c, false);
	}
	
	
	public static void setHtmlEnabled(JComponent c, boolean on)
	{
		c.putClientProperty(HTML_DISABLE_CLIENT_PROPERTY, on ? Boolean.FALSE : Boolean.TRUE);
	}
	

//	public static Color darker(Color c, float factor)
//	{
//		return new Color
//		(
//			Math.max(0, (int)(c.getRed()   * factor)), 
//			Math.max(0, (int)(c.getGreen() * factor)), 
//			Math.max(0, (int)(c.getBlue()  * factor))
//		);
//	}
	

	public static void later(Runnable r)
	{
		EventQueue.invokeLater(r);
	}
	
	
	public static void later(int delay, final Runnable r)
	{
		Timer t = new Timer(delay, new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				r.run();
			}
		});
		t.setRepeats(false);
		t.start();
	}
	
	
	public static void inEDT(Runnable r)
	{
		if(EventQueue.isDispatchThread())
		{
			r.run();
		}
		else
		{
			EventQueue.invokeLater(r);
		}
	}


	public static void inEDTW(Runnable r)
	{
		try
		{
			if(EventQueue.isDispatchThread())
			{
				r.run();
			}
			else
			{
				EventQueue.invokeAndWait(r);
			}
		}
		catch(InterruptedException ignore)
		{ }
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public static void inEDTW2(Runnable r) throws Exception
	{
		if(EventQueue.isDispatchThread())
		{
			r.run();
		}
		else
		{
			EventQueue.invokeAndWait(r);
		}
	}
	
	
	public static void inIdleEDT(Runnable r)
	{
		try
		{
			if(EventQueue.isDispatchThread())
			{
				r.run();
			}
			else
			{
				try
				{
					new Robot().waitForIdle();
				}
				catch(Exception e)
				{ }
				
				EventQueue.invokeLater(r);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public static void inIdleEDTW(Runnable r)
	{
		try
		{
			if(EventQueue.isDispatchThread())
			{
				r.run();
			}
			else
			{
				try
				{
					new Robot().waitForIdle();
				}
				catch(Exception e)
				{ }
				
				EventQueue.invokeAndWait(r);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public static void checkEDT()
	{
		if(!EventQueue.isDispatchThread())
		{
			throw new Rex("must be in EDT");
		}
	}

	
	public static void beep()
	{
		Toolkit.getDefaultToolkit().beep();
	}
	
	
	// force the same preferred height on all components
	// suitable for simple one-line components
	public static void sameHeight(Component ... components)
	{
		int h = 0;
		for(int i=0; i<components.length; i++)
		{
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			if(h < d.height)
			{
				h = d.height;
			}
		}
		
		for(Component c: components)
		{
			c.setPreferredSize(new Dimension(-1, h));
		}
	}
	
	
	// force the same preferred width on all components
	public static void sameWidth(Component ... components)
	{
		int w = 0;
		for(int i=0; i<components.length; i++)
		{
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			if(w < d.width)
			{
				w = d.width;
			}
		}
		
		for(Component c: components)
		{
			c.setPreferredSize(new Dimension(w,-1));
		}
	}


	// set mnemonic from button (menu, menu item) name
	// "&File" -> "File" + mnemonic(0)
	// NOTE: will not work if the text gets changed (add listener?)
	@Deprecated
	public static void setMnemonic(AbstractButton b)
	{
		String s = b.getText();
		if(s != null)
		{
			int ix = s.indexOf('&');
			if(ix >= 0)
			{
				// sometimes & is used for 'and'
				if(!Character.isWhitespace(s.charAt(ix)))
				{
					s = s.replace("&", "");
					b.setText(s);

					// we don't want mnemonic in OSX for some reason
					if(!CPlatform.isMac())
					{
						b.setMnemonic(s.charAt(ix));
						b.setDisplayedMnemonicIndex(ix);
					}
				}
			}
		}
	}


	public static void errorFeedback()
	{
		Toolkit.getDefaultToolkit().beep();
	}
	
	
	public static String getHumanName(KeyStroke k)
	{
		if(k == null)
		{
			return null;
		}
		
		SB sb = new SB(k.toString());
		sb.replace("typed ", "-");
		sb.replace("released ", "-");
		sb.replace("pressed ", "-");
		sb.replace("shift ", TXT.get("UITools.keyboard shift", "Shift"));
		sb.replace("ctrl ", TXT.get("UITools.keyboard ctrl", "Ctrl"));
		sb.replace("meta ", TXT.get("UITools.keyboard meta", "Meta"));
		sb.replace("alt ", TXT.get("UITools.keyboard alt", "Alt"));
		sb.replace("altGraph ", TXT.get("UITools.keyboard altGraph", "AltGraph"));
		return sb.toString();
	}


	public static boolean isNullOrResource(Object x)
	{
		if(x == null)
		{
			return true;
		}
		
		return (x instanceof UIResource);
	}
	
	
	public static Insets newInsets(int top, int left, int bottom, int right)
	{
		return new InsetsResource(top, left, bottom, right);
	}
	
	
	public static boolean isLeftToRight(Component c)
	{
		return c.getComponentOrientation().isLeftToRight();
	}
	
	
	public static void setHorizontalAlignment(Object x, int alignment)
	{
		if(x instanceof JLabel)
		{
			((JLabel)x).setHorizontalAlignment(alignment);
		}
		else if(x instanceof JTextField)
		{
			((JTextField)x).setHorizontalAlignment(alignment);
		}
		else if(x instanceof JCheckBox)
		{
			((JCheckBox)x).setHorizontalAlignment(alignment);
		}
		else if(x instanceof DefaultCellEditor)
		{
			Component c = ((DefaultCellEditor)x).getComponent();
			setHorizontalAlignment(c, alignment);
		}
		else
		{
			Log.print("no horizontal alignment for " + x);
		}
	}
	
	
	public static void setTableColumnsPreferredSize(JTable t, int width)
	{
		if(t != null)
		{
			JTableHeader h = t.getTableHeader();
			if(h != null)
			{
				TableColumnModel m = h.getColumnModel();
				for(int i=0; i<m.getColumnCount(); i++)
				{
					TableColumn c = m.getColumn(i);
					c.setPreferredWidth(width);
					c.setWidth(width);
				}
			}
		}
	}
	
	
	private static boolean needsSize(Window w)
	{
		Dimension d = w.getSize();
		return ((d.width == 0) || (d.height == 0));
	}
	
	
	public static void resize(Window w, int width, int height)
	{
		if(needsSize(w))
		{
			w.setSize(width,height);
		}
		center(w);
	}
	
	
	public static Rectangle getScreenBounds()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	}
	
	
	public static void resize(Window w, float fraction)
	{
		if(needsSize(w))
		{
			Rectangle screen = getScreenBounds();
			w.setSize(Math.round(screen.width * fraction), Math.round(screen.height * fraction));
			center(w);
		}
	}
	
	
	public static void center(Window w)
	{
		Rectangle screen = getScreenBounds();
		w.setLocation
		(
			screen.x + (screen.width - w.getWidth())/2,
			screen.y + (screen.height - w.getHeight())/2
		);
	}
	
	
	public static void center(Window win, Container parentWindow, int w, int h)
	{
		// honor previously set size
//		Dimension d = win.getSize();
//		w = d.width == 0 ? w : d.width;
//		h = d.height == 0 ? h : d.height;
		
		Rectangle screen = getScreenBounds();
		Rectangle parent;
		
		if(parentWindow == null)
		{
			parent = screen;
		}
		else
		{
			parent = parentWindow.getBounds();
			if((parent.width == 0) || (parent.height == 0))
			{
				parent = screen;
			}
		}
			
		int x = parent.x + (parent.width - w)/2;
		int y = parent.y + (parent.height - h)/2;
		
		if(w > screen.width)
		{
			w = screen.width;
		}
		if(h > screen.height)
		{
			h = screen.height;
		}
		
		if(x < screen.x)
		{
			x = screen.x;
		}
		if(y < screen.y)
		{
			y = screen.y;
		}
		
		if((x + w) > screen.x+screen.width)
		{
			x -= (x + w - screen.x - screen.width);
		}
		if(y+h > screen.y + screen.height)
		{
			y -= (y + h - screen.y - screen.height);
		}
		
		win.setBounds(x,y,w,h);
	}
	
	
	public static void setPreferredMinimumSize(Window w, Component c)
	{
		Dimension d = c.getPreferredSize();
		int margin = 60;
		w.setMinimumSize(new Dimension(d.width + margin, d.height + margin));
	}
	
	
	public static void setTableHeaderHighlight(JTable t, Color c)
	{
		t.putClientProperty(KEY_TABLE_HEADER_HIGHLIGHT, c);
	}
	
	
	public static Color getTableHeaderHighlight(JTable t)
	{
		return (Color)t.getClientProperty(KEY_TABLE_HEADER_HIGHLIGHT);
	}

	
	public static CStatusBar createStatusBar(boolean memoryBar)
	{
		CStatusBar p = new CStatusBar();

		if(memoryBar)
		{
			p.add(new CMemoryBar());
		}
		
		p.fill();
		p.copyright();
		return p;
	}
	
	

	public static String getWindowTitle(Window w)
	{
		String title;
		if(w instanceof Frame)
		{
			title = ((Frame)w).getTitle();
		}
		else if(w instanceof Dialog)
		{
			title = ((Dialog)w).getTitle();
		}
		else
		{
			title = null;
		}
		
		if(CKit.isNotBlank(title))
		{
			return title;
		}
		else
		{
			return Application.getTitle();
		}
	}
	
	
	public static void addFirstMouseListener(Component c, MouseListener m)
	{
		MouseListener[] ms = c.getMouseListeners();
		for(MouseListener x: ms)
		{
			c.removeMouseListener(x);
		}
		
		c.addMouseListener(m);
		
		for(MouseListener x: ms)
		{
			c.addMouseListener(x);
		}
	}
	
	
	public static void addFirstMouseMotionListener(Component c, MouseMotionListener m)
	{
		MouseMotionListener[] ms = c.getMouseMotionListeners();
		for(MouseMotionListener x: ms)
		{
			c.removeMouseMotionListener(x);
		}
		
		c.addMouseMotionListener(m);
		
		for(MouseMotionListener x: ms)
		{
			c.addMouseMotionListener(x);
		}
	}
	

	public static void removeMouseListeners(Component c)
	{
		for(MouseListener x: c.getMouseListeners())
		{
			c.removeMouseListener(x);
		}
		
		for(MouseMotionListener x: c.getMouseMotionListeners())
		{
			c.removeMouseMotionListener(x);
		}
		
		for(MouseWheelListener x: c.getMouseWheelListeners())
		{
			c.removeMouseWheelListener(x);
		}
	}
	
	
	public static void removeKeyListeners(Component c)
	{
		for(KeyListener x: c.getKeyListeners())
		{
			c.removeKeyListener(x);
		}
	}
	
	
	public static Graphics2D getGraphics(Graphics g)
	{
		return (Graphics2D)g;
	}
	
	
	public static Graphics2D createGraphics(Graphics g)
	{
		return (Graphics2D)g.create();
	}
	
	
	public static Graphics2D createAntiAliasingAndQualityGraphics(Graphics g)
	{
		return setAntiAliasingAndQuality(g.create());
	}


	public static Graphics2D setAntiAliasing(Graphics gg)
	{
		Graphics2D g = (Graphics2D)gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return g;
	}
	
	
	public static Graphics2D setQualityRendering(Graphics gg)
	{
		Graphics2D g = (Graphics2D)gg;
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		return g;
	}
	
	
	public static Graphics2D setAntiAliasingAndQuality(Graphics gg)
	{
		Graphics2D g = (Graphics2D)gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		return g;
	}


	public static void red(JComponent c)
	{
		c.setOpaque(true);
		c.setBackground(Color.red);
	}


	public static Rectangle getBoundsIn(Container parent, Component c)
	{
		try
		{
			return SwingUtilities.convertRectangle(c, c.getBounds(), parent);
		}
		catch(Throwable e)
		{
			return null;
		}
	}
	

	/** recursively collects children components of specified type and returns in form of an array */
	public static <T> CList<T> collectChildrenOfType(Class<T> type, Container container)
	{
		CList<T> a = new CList();
		collectChildrenRecursive(type, container, a); 
		return a;
	}
	
	
	private static void collectChildrenRecursive(Class<?> type, Container container, CList a)
	{
		for(Component c: container.getComponents())
		{
			if(type.isAssignableFrom(c.getClass()))
			{
				a.add(c);
			}
			
			if(c instanceof Container)
			{
				collectChildrenRecursive(type, (Container)c, a);
			}
		}
	}


	public static void stopEditing(JTable t)
	{
		if(t != null)
		{
			TableCellEditor ed = t.getCellEditor();
			if(ed != null)
			{
				ed.stopCellEditing();
			}
		}
	}
	

	public static void showTooltip(JComponent c)
	{
		ToolTipManager.sharedInstance().mouseMoved(new MouseEvent(c, 0, 0, 0, 0, 0, 0, false));
	}
	

	public static JRootPane getRootPane(Component c)
	{
		if(c instanceof JFrame)
		{
			return ((JFrame)c).getRootPane();
		}
		return getAncestorOfClass(JRootPane.class, c);
	}
	
	
	public static Font changeFontSize(JComponent c, float factor)
	{
		Font f = c.getFont();
		deriveFont(f, factor);
		c.setFont(f);
		return f;
	}
	
	
	public static Font deriveFont(Font f, float factor)
	{
		if(f != null)
		{
			return f.deriveFont(f.getSize2D() * factor);
		}
		return null;
	}
	
	
	public static Font deriveFont(Font f, boolean bold, float factor)
	{
		if(f != null)
		{
			f = f.deriveFont(f.getSize2D() * factor);
			f = f.deriveFont(bold ? Font.BOLD : Font.PLAIN);
			return f;
		}
		return null;
	}
	
	
	public static Font deriveFontBold(Font f, boolean bold)
	{
		if(f != null)
		{
			return f.deriveFont(bold ? Font.BOLD : Font.PLAIN);
		}
		return null;
	}
	

	public static Font boldFont(Font f)
	{
		if(f != null)
		{
			return f.deriveFont(Font.BOLD);
		}
		return null;
	}
	
	
	public static void putFrameProperty(Component c, Object key, Object value)
	{
		JRootPane rp = getRootPane(c);
		rp.putClientProperty(key, value);
	}
	
	
	public static Object getFrameProperty(Component c, Object key)
	{
		JRootPane rp = getRootPane(c);
		if(rp != null)
		{
			return rp.getClientProperty(key);
		}
		return null;
	}


	public static Color clone(Color c)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	
	
	public static void toFront(Window w)
	{
		if(w instanceof Frame)
		{
			Frame f = (Frame)w;
			int st = f.getExtendedState();
			if(st == Frame.ICONIFIED)
			{
				f.setExtendedState(Frame.NORMAL);
			}
		}
		
		w.toFront();
	}


	public static <T extends Window> T findVisibleWindow(Class<T> type)
	{
		for(Window w: Window.getWindows())
		{
			if(w.isVisible())
			{
				if(type.isAssignableFrom(w.getClass()))
				{
					return (T)w;
				}
			}
		}
		return null;
	}
	
	
	public static CList<Window> getVisibleWindows()
	{
		Window[] ws = Window.getWindows();
		CList<Window> a = new CList(ws.length);
		for(Window w: ws)
		{
			if(w.isVisible())
			{
				a.add(w);
			}
		}
		return a;
	}
	
	
	/** returns all visible windows of the specified type */
	public static <T extends Window> CList<T> getWindowsOfType(Class<T> type)
	{
		CList<T> ws = new CList();
		for(Window w: Window.getWindows())
		{
			if(w.isVisible())
			{
				if(type.isAssignableFrom(w.getClass()))
				{
					ws.add((T)w);
				}
			}
		}
		return ws;
	}
	
	
	public static <T extends Window> T getWindowOfType(Class<T> type)
	{
		for(Window w: Window.getWindows())
		{
			if(w.isVisible())
			{
				if(type.isAssignableFrom(w.getClass()))
				{
					return (T)w;
				}
			}
		}
		return null;
	}
	
	
	public static JFrame getAnyVisibleFrame()
	{
		for(Window w: Window.getWindows())
		{
			if(w.isVisible())
			{
				if(w instanceof JFrame)
				{
					return (JFrame)w;
				}
			}
		}
		return null;
	}


	public static DataFlavor createLocalObjectFlavor(Class<?> c)
	{
		try
		{
			return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + c.getName());
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return null;
	}
	
	
	public static DataFlavor createDataFlavor(String s)
	{
		try
		{
			return new DataFlavor(s);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return null;
	}


	public static boolean isLeftButton(InputEvent ev)
	{
		return ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0);
	}
	
	
	public static boolean isRightButton(InputEvent ev)
	{
		return ((ev.getModifiers() & InputEvent.BUTTON3_MASK) != 0);
	}
	
	
	/** some mac mice do not have right button.  right mouse click is Ctrl-Click */
	public static boolean isPopupTrigger(InputEvent ev)
	{
		if(CPlatform.isMac())
		{
			if(isCtrlPressed(ev) && isLeftButton(ev))
			{
				return true;
			}
		}
		return ((ev.getModifiers() & InputEvent.BUTTON3_MASK) != 0);
	}
	
	
	public static boolean isCtrlPressed(InputEvent ev)
	{
		return ((ev.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0);
	}
	
	
	public static boolean isShiftPressed(InputEvent ev)
	{
		return ((ev.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0);
	}
	
	
	public static boolean isAltPressed(InputEvent ev)
	{
		return ((ev.getModifiersEx() & InputEvent.ALT_DOWN_MASK) != 0);
	}
	
	
	public static boolean isBlank(JTextComponent c)
	{
		return CKit.isBlank(c.getText());
	}


	public static void invokeCopyAction(Component c)
	{
		if(c instanceof JComponent)
		{
			JComponent comp = (JComponent)c;
			Object x = comp.getActionMap().get("copy");
			if(x instanceof Action)
			{
				((Action)x).actionPerformed(new ActionEvent(c, ActionEvent.ACTION_PERFORMED, "copy"));
			}
		}
	}


	public static void focus(JComponent c)
	{
		c.requestFocusInWindow();
	}
	
	
	public static void focusLater(final Component c)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				c.requestFocusInWindow();
			}
		});
	}
	
	
	/** returns source window even when the actual source component is a JMenuItem (which is not connected to a parent window) */
	public static Window findParentWindow(Component c)
	{
		Window w;
		if(c != null)
		{
			w = SwingUtilities.getWindowAncestor(c);
			if(w == null)
			{
				if(c instanceof JMenuItem)
				{
					// special code for menus as they don't have parent windows
					return findParent((JMenuItem)c);
				}
			}
			return w;
		}
		
		return null;
	}
	
	
	private static Window findParent(JMenuItem c)
	{
		JPopupMenu m = getAncestorOfClass(JPopupMenu.class, c);
		if(m != null)
		{
			Component ci = m.getInvoker();
			if(ci != null)
			{
				Window w = SwingUtilities.getWindowAncestor(ci);
				if(w != null)
				{
					return w;
				}
				else
				{
					if(ci instanceof JMenuItem)
					{
						return findParent((JMenuItem)ci);
					}
				}
			}
		}
		return null;
	}
	
	
	public static Insets insets(int gap)
	{
		return new Insets(gap, gap, gap, gap);
	}
	
	
	public static Insets insets(int verticalGap, int horizontalGap)
	{
		return new Insets(verticalGap, horizontalGap, verticalGap, horizontalGap);
	}

	
	/** intelligently place the popup menu below or above the caret */
	public static void showPopup(JTextComponent c, JPopupMenu popup)
	{
		try
		{
			int pos = c.getCaretPosition();
			Rectangle r = c.modelToView(pos);
			showPopup(c, r, popup);
			return;
		}
		catch(Exception e)
		{ }
		
		popup.show(c, 0, 0);
	}
	
	
	/** intelligently place the popup menu below or above the component, aligning it with the component boundary */
	public static void showPopup(Component c, JPopupMenu popup)
	{
		showPopup(c, new Rectangle(0, 0, c.getWidth(), c.getHeight()), popup);
	}
	
	
	public static void showPopup(Component c, int x, int y, JPopupMenu popup)
	{
		UI.showPopup(c, new Rectangle(x, y, 0, 0), popup);
	}

	
	public static void showPopup(Component c, Rectangle r, JPopupMenu popup)
	{
		int x = 0;
		int y = 0;

		try
		{
			GraphicsConfiguration gc = c.getGraphicsConfiguration();
			if(gc != null)
			{
				Rectangle screen = gc.getBounds();
				Point pc = c.getLocationOnScreen();
				Dimension dp = popup.getPreferredSize();
				int w = dp.width;
				int h = dp.height;
				
				// does it fit below the text?
				int bottomGap = (screen.y + screen.height) - (pc.y + r.y + r.height + h);
				if(bottomGap > 0)
				{
					y = r.y + r.height;
				}
				else
				{
					// does it fit above the text?
					int topGap = (pc.y + r.y - h) - screen.y;
					if(topGap > 0)
					{
						y = r.y - h;
					}
					else
					{
						// does not fit anywhere
						y = 0;
					}
				}
				
				// does it fit to the right side of the text?
				int rightGap = (screen.x + screen.width) - (pc.x + r.x + w);
				if(rightGap > 0)
				{
					x = r.x;
				}
				else
				{
					// does not fit, move to the left 
					x = r.x + rightGap;
					
					int leftGap = pc.x + x - screen.x;
					if(leftGap < 0)
					{
						// does not fit width, so let it clip at the right side rather than left
						x -= leftGap;
					}
				}
			}
		}
		catch(Exception e)
		{ }

		popup.show(c, x, y);
	}


	/** sets component orientation for the whole ui */
	public static void setLeftToRightOrientation(ComponentOrientation or)
	{
		for(Window w: Window.getWindows())
		{
			w.applyComponentOrientation(or);
			w.repaint();
		}
	}
	
	
	public static void layoutComponent(Component c)
	{
		synchronized(c.getTreeLock())
		{
			c.setVisible(true);
			c.doLayout();
			
			if(c instanceof Container)
			{
				Container p = (Container)c;
				for(Component ch: p.getComponents())
				{
					layoutComponent(ch);
				}
			}
		}
	}
	
	
	/** open an invisible frame in order to see the application icon in the dock. */
	public static JFrame constructInvisibleFrame(ImageIcon icon, String title)
	{
		return new ZeroFrame(icon, title);
	}


	/** scales font */
	public static Font scaleFont(Font f, double d)
	{
		if(f != null)
		{
			float sz = f.getSize2D();
			return f.deriveFont((float)(sz * d));
		}
		return null;
	}


	/** attemts to retrieve the text content from a component */
	public static String getText(JComponent c)
	{
		if(c instanceof JTextComponent)
		{
			return ((JTextComponent)c).getText();
		}
		else if(c instanceof JLabel)
		{
			return ((JLabel)c).getText();
		}
		return null;
	}


	/** returns true if dst contains the point (x,y) specified in the src coordinate system. */
	public static boolean contains(Component src, int x, int y, Component dst)
	{
		return contains(src, new Point(x, y), dst);
	}
	
	
	/** returns true if dst contains the point specified in the src coordinate system. */
	public static boolean contains(Component src, Point p, Component dst)
	{
		if(src != null)
		{
			if(dst != null)
			{
				p = SwingUtilities.convertPoint(src, p, dst);
				return dst.contains(p);
			}
		}
		return false;
	}


	/** alias for SwingUtilities.convertPoint() */
	public static Point convert(Component src, Point p, Component dst)
	{
		if(p == null)
		{
			return null;
		}
		return SwingUtilities.convertPoint(src, p, dst);
	}


	/** selects all and moves the caret to start */
	public static void selectAll(JTextComponent c)
	{
		if(c != null)
		{
			Document d = c.getDocument();
			if(d != null)
			{
				c.setCaretPosition(d.getLength());
				c.moveCaretPosition(0);
			}
		}
	}


	public static void validateAndRepaint(Component c)
	{
		if(c instanceof JFrame)
		{
			validateAndRepaint(((JFrame)c).getContentPane());
		}
		else if(c != null)
		{
			c.validate();
			c.repaint();
		}
	}


	/** cascade child window */
	public static void cascade(Window parent, Window child)
	{
		int shift = 30;

		Rectangle r = parent.getBounds();
		r.x += shift;
		r.y += shift;

		Rectangle screen = getScreenBounds();
		if(!screen.contains(r))
		{
			// simply relocate to 0,0
			r.x = screen.x;
			r.y = screen.y;
		}

		child.setBounds(r);
	}


	public static void clear(JComponent ... cs)
	{
		if(cs != null)
		{
			for(JComponent c: cs)
			{
				if(c instanceof JLabel)
				{
					((JLabel)c).setText(null);
				}
				else if(c instanceof JTextComponent)
				{
					((JTextComponent)c).setText(null);
				}
				else if(c instanceof Clearable)
				{
					((Clearable)c).clear();
				}
			}
		}
	}
	
	
	/** finds component (button) with associated action among the container's children */  
	public static Component findByAction(Component c, Action a)
	{
		if(getAction(c) == a)
		{
			return c;
		}
		
		if(c instanceof Container)
		{
			Container p = (Container)c;
			for(int i=0; i<p.getComponentCount(); i++)
			{
				Component ch = p.getComponent(i);
				Component rv = findByAction(ch, a);
				if(rv != null)
				{
					return rv;
				}
			}
		}
		
		return null;
	}
	
	
	public static Action getAction(Component c)
	{
		if(c instanceof AbstractButton)
		{
			return ((AbstractButton)c).getAction();
		}
		else
		{
			return null;
		}
	}


	/** conditionally adds a separator to a menu that already contains one or more item */
	public static void separator(JPopupMenu m)
	{
		if(m != null)
		{
			boolean populated = false;
			int sz = m.getComponentCount();
			for(int i = 0; i < sz; i++)
			{
				Component c = m.getComponent(i);
				if(c instanceof MenuElement)
				{
					populated = true;
					break;
				}
			}

			if(populated)
			{
				m.addSeparator();
			}
		}
	}


	public static void resizeToContent(JTable t)
	{
		resizeToContent(t, -1);
	}


	public static void resizeToContent(JTable t, int maxColumnWidth)
	{
		int rows = t.getRowCount();		
		if(rows > 0)
		{
			int sp = t.getIntercellSpacing().width + 2; // little space looks better
			
			int cols = t.getColumnCount();
			for(int c=0; c<cols; c++)
			{
				int w = 5;
				for(int r=0; r<rows; r++)
				{
					Object v = t.getValueAt(r, c);
					Component rend = t.getCellRenderer(r, c).getTableCellRendererComponent(t, v, false, false, r, c);
					int cw = rend.getPreferredSize().width;
					if(w < cw)
					{
						w = cw;
						
						if(maxColumnWidth > 0)
						{
							if(w >= maxColumnWidth)
							{
								w = maxColumnWidth;
								break;
							}
						}
					}
				}
				
				TableColumn tc = t.getColumnModel().getColumn(c);
				
				Component hr = t.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(t, tc.getHeaderValue(), false, false, 0, c);
				int hw = hr.getPreferredSize().width;
				if(hw > w)
				{
					w = hw;
					
					if(maxColumnWidth > 0)
					{
						if(w > maxColumnWidth)
						{
							w = maxColumnWidth;
						}
					}
				}
				
				tc.setPreferredWidth(w + sp);
			}
		}
	}


	public static void scrollRectToVisible(JComponent c)
	{
		if(c != null)
		{
			c.scrollRectToVisible(new Rectangle(0, 0, c.getWidth(), c.getHeight()));
		}
	}
	

	private static CPopupMenuController createDefaultPopupMenuController()
	{
		return new CPopupMenuController()
		{
			public JPopupMenu constructPopupMenu()
			{
				Component c = getSourceComponent();
				if(c instanceof JTextComponent)
				{
					final JTextComponent t = (JTextComponent)c;

					CAction cutAction = new CAction()
					{
						public void action() throws Exception
						{
							t.cut();
						}
					};
					cutAction.setEnabled(t.isEditable() && t.isEnabled());
					
					CAction copyAction = new CAction()
					{
						public void action() throws Exception
						{
							t.copy();
						}
					};
					
					CAction pasteAction = new CAction()
					{
						public void action() throws Exception
						{
							t.paste();
						}
					};
					pasteAction.setEnabled(t.isEditable() && t.isEnabled());
					
					CPopupMenu m = new CPopupMenu();
					m.add(new CMenuItem(Menus.Cut, cutAction));
					m.add(new CMenuItem(Menus.Copy, copyAction));
					m.add(new CMenuItem(Menus.Paste, pasteAction));
					return m;	
				}
				return null;
			}
		};
	}


	public static void installDefaultPopupMenu(JTextComponent c)
    {
		if(defaultPopupMenuController == null)
		{
			defaultPopupMenuController = createDefaultPopupMenuController();
		}
		
		defaultPopupMenuController.monitor(c);
    }
	
	
	/** alters prefereed width of the component */ 
	public static void setPreferredWidth(Component c, int width)
	{
		if(c != null)
		{
			Dimension d = c.getPreferredSize();
			d.width = width;
			c.setPreferredSize(d);
		}
	}
}
