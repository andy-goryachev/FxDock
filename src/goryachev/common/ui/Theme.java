// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.ATheme;
import goryachev.common.ui.theme.AgButtonUI;
import goryachev.common.ui.theme.AgCheckBoxUI;
import goryachev.common.ui.theme.AgComboBoxUI;
import goryachev.common.ui.theme.AgMenuBarUI;
import goryachev.common.ui.theme.AgMenuItemUI;
import goryachev.common.ui.theme.AgPanelUI;
import goryachev.common.ui.theme.AgPopupMenuUI;
import goryachev.common.ui.theme.AgPopupSeparatorUI;
import goryachev.common.ui.theme.AgRadioButtonUI;
import goryachev.common.ui.theme.AgScrollBarUI;
import goryachev.common.ui.theme.AgScrollPaneUI;
import goryachev.common.ui.theme.AgSeparatorUI;
import goryachev.common.ui.theme.AgSplitPaneUI;
import goryachev.common.ui.theme.AgTabbedPaneUI;
import goryachev.common.ui.theme.AgTableHeaderUI;
import goryachev.common.ui.theme.AgToolBarUI;
import goryachev.common.ui.theme.AgToolTipUI;
import goryachev.common.ui.theme.AgTreeUI;
import goryachev.common.ui.theme.CFieldBorder;
import goryachev.common.ui.theme.SpinningGearIcon;
import goryachev.common.ui.theme.ThemeColor;
import goryachev.common.ui.theme.ThemeOptions;
import goryachev.common.ui.theme.TimePeriodFormatter;
import goryachev.common.util.CList;
import goryachev.common.util.CPlatform;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.InsetsUIResource;


/** defines color/font theme and forces a platform-independent look and feel */
public class Theme
{
	public static final ThemeColor AFFIRM_BUTTON_COLOR = ThemeColor.create(ThemeKey.AFFIRM_BUTTON_COLOR);
	public static final ThemeColor DESTRUCTIVE_BUTTON_COLOR = ThemeColor.create(ThemeKey.DESTRUCTIVE_BUTTON_COLOR);
	public static final ThemeColor FIELD_BG = ThemeColor.create(ThemeKey.FIELD_BG);
	public static final ThemeColor FIELD_FG = ThemeColor.create(ThemeKey.FIELD_FG);
	public static final ThemeColor FOCUS_COLOR = ThemeColor.create(ThemeKey.FOCUS_COLOR);
	public static final ThemeColor GRID_COLOR = ThemeColor.create(ThemeKey.GRID_COLOR);
	public static final ThemeColor LINE_COLOR = ThemeColor.create(ThemeKey.LINE_COLOR);
	public static final ThemeColor LINK_COLOR = ThemeColor.create(ThemeKey.LINK_COLOR);
	public static final ThemeColor PANEL_BG = ThemeColor.create(ThemeKey.PANEL_BG);
	public static final ThemeColor PANEL_FG = ThemeColor.create(ThemeKey.PANEL_FG);
	public static final ThemeColor TARGET_COLOR = ThemeColor.create(ThemeKey.TARGET_COLOR);
	public static final ThemeColor TEXT_BG = ThemeColor.create(ThemeKey.TEXT_BG);
	public static final ThemeColor TEXT_FG = ThemeColor.create(ThemeKey.TEXT_FG);
	public static final ThemeColor TEXT_SELECTION_BG = ThemeColor.create(ThemeKey.TEXT_SELECTION_BG);
	public static final ThemeColor TEXT_SELECTION_FG = ThemeColor.create(ThemeKey.TEXT_SELECTION_FG);
	public static final ThemeColor TOOLBAR_BG = ThemeColor.create(ThemeKey.TOOLBAR_COLOR);
	public static final ThemeColor TOOL_TIP_BG = ThemeColor.create(ThemeKey.TOOL_TIP_BG);
	
	protected static Border border10;
	protected static Border fieldBorder;
	protected static Border lineBorder;
	protected static Border noBorder;
	protected static Border raisedBevelBorder;
	protected static Border loweredBevelBorder;
	protected static Dimension preferredToolbarDimensions;


	public static void init()
	{
		try
		{
			if(CPlatform.isWindows())
			{
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}
		}
		catch(Exception e)
		{ }
		
		try
		{
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		// init default colors and borders
		theme();
		
		// borders
		fieldBorder = new CFieldBorder();
		border10 = new CBorder(10);
		noBorder = new CBorder(0);
		lineBorder = new CBorder(LINE_COLOR);
		
		UIDefaults d = UIManager.getLookAndFeelDefaults();
		
		AgButtonUI.init(d);
		AgCheckBoxUI.init(d);
		AgComboBoxUI.init(d);
		AgMenuBarUI.init(d);
		AgMenuItemUI.init(d);
		AgPanelUI.init(d);
		AgPopupMenuUI.init(d);
		AgPopupSeparatorUI.init(d);
		AgRadioButtonUI.init(d);
		AgScrollBarUI.init(d);
		AgScrollPaneUI.init(d);
		AgSeparatorUI.init(d);
		AgSplitPaneUI.init(d);
		AgTabbedPaneUI.init(d);
		AgTableHeaderUI.init(d);
		AgToolBarUI.init(d);
		AgToolTipUI.init(d);
		AgTreeUI.init(d);

		if(CPlatform.isMac())
		{
			// margins
			d.put("EditorPane.margin", new InsetsUIResource(3, 3, 3, 3));
			d.put("TextPane.margin", new InsetsUIResource(3, 3, 3, 3));
			
			// ui
			d.put("MenuUI", "javax.swing.plaf.basic.BasicMenuUI");
			d.put("Menu.background", Theme.PANEL_BG);
			d.put("Menu.useMenuBarBackgroundForTopLevel", Boolean.TRUE);
		}
		
		// label
		d.put("Label.background", Theme.PANEL_BG);
		d.put("Label.foreground", Theme.TEXT_FG);
		
		// menu
		d.put("Menu.background", Theme.PANEL_BG);
		d.put("Menu.foreground", Theme.TEXT_FG);
		
		// table
		d.put("Table.background", Theme.TEXT_BG);
		d.put("Table.foreground", Theme.TEXT_FG);
		d.put("Table.selectionBackground", Theme.TEXT_SELECTION_BG);
		d.put("Table.selectionForeground", Theme.TEXT_SELECTION_FG);
		d.put("Table.gridColor", Theme.GRID_COLOR);
		//d.put("Table.cellNoFocusBorder", new CBorder.UIResource(2,1));
		
		// text area
		d.put("TextArea.background", Theme.TEXT_BG);
		d.put("TextArea.foreground", Theme.TEXT_FG);
		
		// text field
		d.put("TextField.border", fieldBorder);
		d.put("TextField.background", Theme.TEXT_BG);
		d.put("TextField.foreground", Theme.TEXT_FG);
		d.put("TextField.inactiveBackground", ThemeColor.create(ThemeKey.TEXT_BG, 0.5, ThemeKey.PANEL_BG));
		d.put("TextField.inactiveForeground", ThemeColor.create(ThemeKey.TEXT_FG, 0.5, ThemeKey.PANEL_BG));
		
		// text pane
		d.put("TextPane.background", Theme.TEXT_BG);
		d.put("TextPane.foreground", Theme.TEXT_FG);
		
		//d.put("PasswordFieldUI.border", BORDER_FIELD);
	}
	
	
	public static boolean isDark()
	{
		return theme().isDark();
	}
	
	
	// FIX use ThemeColor-derived colors
	@Deprecated
	private static float gradientFactor = 0.84f;

	@Deprecated
	public static Color darker(Color c)
	{
		return ColorTools.darker(c, gradientFactor);
	}
	@Deprecated
	public static Color darker(Color c, float factor)
	{
		return ColorTools.darker(c, gradientFactor * factor);
	}
	@Deprecated
	public static Color brighter(Color c)
	{
		return ColorTools.brighter(c, gradientFactor);
	}
	@Deprecated
	public static Color brighter(Color c, float factor)
	{
		return ColorTools.brighter(c, gradientFactor * factor);
	}
	@Deprecated
	public static float getGradientFactor()
	{
		return gradientFactor;
	}
	
	
	public static Border lineBorder()
	{
		return lineBorder;
	}
	
	
	public static Border fieldBorder()
	{
		return fieldBorder;
	}
	
	
	public static Border border10()
	{
		return border10;
	}
	
	
	public static Border noBorder()
	{
		return noBorder;
	}


	public static Font plainFont()
	{
		return theme().getFont(ThemeKey.BASE_FONT);
	}


	public static Font monospacedFont()
	{
		return theme().getFont(ThemeKey.MONOSPACED_FONT);
	}


	public static Font boldFont()
	{
		return theme().getFont(ThemeKey.BOLD_FONT);
	}


	public static Font titleFont()
	{
		return theme().getFont(ThemeKey.TITLE_FONT);
	}

	
	public static Font getFont(double scale, boolean bold)
	{
		return UI.deriveFont(plainFont(), bold, (float)scale);
	}
	

	public static CToolBar toolbar()
	{
		if(preferredToolbarDimensions == null)
		{
			CToolBar t = new CToolBar();
			t.add(new CButton("W"));
			t.add(new CComboBox());
			t.add(new JLabel("W"));
			preferredToolbarDimensions = t.getPreferredSize();
			preferredToolbarDimensions.width = -1;
		}

		CToolBar t = new CToolBar();
		t.setMinimumSize(preferredToolbarDimensions);
		t.setPreferredSize(preferredToolbarDimensions);
		return t;
	}


	public static CMenuBar menubar()
	{
		CMenuBar b = new CMenuBar();
		b.setBorder(null);
		return b;
	}
	
	
	public static JButton tbutton()
	{
		CButton t = new CButton();
		t.setFocusable(false);
		return t;
	}


	public static JButton tbutton(String text, Action a)
	{
		JButton b = tbutton();
		b.setAction(a);
		b.setText(text);
		return b;
	}
	
	
	public static JButton tbutton(String text, Icon icon, Action a)
	{
		JButton b = tbutton();
		b.setAction(a);
		b.setText(text);
		b.setIcon(icon);
		return b;
	}


	public static JButton tbutton(String text, String tooltip, Action a)
	{
		JButton b = tbutton();
		b.setAction(a);
		b.setText(text);
		b.setToolTipText(tooltip);
		return b;
	}
	
	
	public static String formatDateTime(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		// FIX ltr?
		return ThemeOptions.dateFormat.get().format(x) + " " + ThemeOptions.timeFormat.get().format(x);
	}
	
	
	public static String formatDate(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		return ThemeOptions.dateFormat.get().format(x);
	}
	
	
	public static String formatTime(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		return ThemeOptions.timeFormat.get().format(x);
	}
	
	
	public static String formatNumber(Object x)
	{
		if(x instanceof Number)
		{
			return ThemeOptions.numberFormat.get().format(x);
		}
		else
		{
			return "";
		}
	}
	
	
	// FIX use number format!
	public static String formatPercent(Object x)
	{
		if(x instanceof Number)
		{
			float val = ((Number)x).floatValue();
			if(Math.abs(val) < 0.1f)
			{
				return new DecimalFormat("0.##%").format(val);
			}
			else if(Math.abs(val) < 1f)
			{
				return new DecimalFormat("00.#%").format(val);
			}
			else
			{
				return new DecimalFormat("#00%").format(val);
			}
		}
		return null;
	}
	
	
	/** 
	 * Formats time period in days/hours/minutes/seconds/milliseconds.  
	 * Accepts Long or Date argument.
	 * Returns null if time is negative or null.  
	 */
	public static String formatTimePeriod2(Object x)
	{
		// FIX
		return TimePeriodFormatter.format(x);
	}
	
	
	public static String formatTimePeriod2(long t)
	{
		// FIX
		return TimePeriodFormatter.format(t);
	}
	
	
	public static String formatTimePeriodRough(Object x)
	{
		return TimePeriodFormatter.formatRough(x);
	}
	
	
	public static String formatTimePeriodRough(long t)
	{
		return TimePeriodFormatter.formatRough(t);
	}
	
	
	public static Icon waitIcon(int size)
	{
		return new SpinningGearIcon(size);
	}
	
	
	/** minimum button width in button panels */
	public static int minimumButtonWidth()
	{
		// TODO depends on font size
		return 70;
	}
	
	
	private static ATheme theme()
	{
		return ATheme.getTheme();
	}


	/** returns theme color specified either by a ThemeKey or a Color */
	public static Color getColor(Object x)
	{
		if(x instanceof ThemeKey)
		{
			return theme().getColor((ThemeKey)x);
		}
		else if(x instanceof Color)
		{
			return (Color)x;
		}
		else
		{
			return null;
		}
	}


	/** returns theme font specified by the key */
	public static Font getFont(ThemeKey key)
	{
		return theme().getFont(key);
	}
	
	
	public static void setTheme(String name)
	{
		ATheme.setTheme(name, true);
	}
	
	
	public static String getTheme()
	{
		return ATheme.getTheme().getName();
	}
	
	
	public static CList<String> getAvailableThemes()
	{
		return ATheme.getAvailableThemeNames();
	}
}
