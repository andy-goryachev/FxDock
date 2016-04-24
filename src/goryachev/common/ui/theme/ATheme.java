// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.ui.options.StringOption;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CPlatform;
import goryachev.common.util.CSorter;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.util.Hashtable;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;


// TODO static: native os colors and fonts
// TODO "based on" parent theme
// TODO allow to change individual items via GlobalSettings
// TODO load and save in global settings
public class ATheme
{
	/** override to set specific colors using set() */
	protected void customize() { }
	
	//
	
	public static final String NAME_ANDY = "Andy";
	public static final String NAME_DARK = "Dark";
	public static final String NAME_MAC = "Mac OS";
	public static final String NAME_OS = "OS";
	public static final String NAME_WINDOWS = "Windows";
	
	public static final float TITLE_FONT_FACTOR = 1.6f;
	
	public static final StringOption themeOption = new StringOption("ui.theme", NAME_ANDY);
	
	protected static Font basePlainFont;
	protected static Font baseMonospacedFont;
	protected static Color basePanelBG;
	protected static Color baseSelectionFG;
	protected static Color baseSelectionBG;
	protected static Color baseTextBG;
	protected static Color baseTextFG;
	private static ATheme instance;

	private String name;
	private boolean dark;
	private Hashtable map = new Hashtable();
	
	static
	{
		UIDefaults d = UIManager.getLookAndFeelDefaults();		
		initFonts(d);
		initColors(d);
	}
	
	
	public ATheme(String name)
	{
		setName(name);
		
		set(ThemeKey.AFFIRM_BUTTON_COLOR, Color.green);
		set(ThemeKey.DESTRUCTIVE_BUTTON_COLOR, Color.magenta);
		set(ThemeKey.FIELD_BG, UI.mix(Color.white, 0.85, basePanelBG));
		set(ThemeKey.FIELD_FG, UI.mix(Color.white, 0.15, Color.black));
		set(ThemeKey.FOCUS_COLOR, new Color(90, 90, 90));
		set(ThemeKey.GRID_COLOR, new Color(242, 242, 242));
		set(ThemeKey.LINE_COLOR, new Color(0xaca8a2));
		set(ThemeKey.LINK_COLOR, new Color(0, 102, 0));
		set(ThemeKey.PANEL_BG, basePanelBG);
		set(ThemeKey.PANEL_FG, basePanelBG.darker());
		set(ThemeKey.TARGET_COLOR, new Color(253, 178, 84));
		set(ThemeKey.TEXT_BG, baseTextBG);
		set(ThemeKey.TEXT_FG, baseTextFG);
		set(ThemeKey.TEXT_SELECTION_BG, baseSelectionBG);
		set(ThemeKey.TEXT_SELECTION_FG, baseSelectionFG);
		set(ThemeKey.TOOL_TIP_BG, new Color(255, 255, 225));
		set(ThemeKey.TOOLBAR_COLOR, new Color(0xb2aea7));
		
		set(ThemeKey.BASE_FONT, basePlainFont);
		set(ThemeKey.BOLD_FONT, basePlainFont.deriveFont(Font.BOLD));
		set(ThemeKey.MONOSPACED_FONT, baseMonospacedFont);
		set(ThemeKey.TITLE_FONT, UI.deriveFont(basePlainFont, true, TITLE_FONT_FACTOR));
	}
	

	public String getName()
	{
		return name;
	}


	public void setName(String s)
	{
		name = s;
	}


	public static CList<String> getAvailableThemeNames()
	{
		CList<String> rv = new CList();
		rv.add(NAME_ANDY);
		rv.add(NAME_DARK);
		
		if(CPlatform.isMac())
		{
			rv.add(NAME_MAC);
		}
		else if(CPlatform.isWindows())
		{
			rv.add(NAME_WINDOWS);
		}
		else
		{
			rv.add(NAME_OS);
		}
		
		CSorter.sort(rv);
		return rv;
	}
	
	
	public static ATheme getTheme()
	{
		if(instance == null)
		{
			String name = themeOption.get();
			instance = create(name);
		}
		return instance;
	}

	
	public static void setTheme(String name, boolean save)
	{
		if(save || CKit.notEquals(name, getTheme().getName()))
		{
			ATheme t = ATheme.create(name);
			
			if(save)
			{
				themeOption.set(t.getName());
			}
			
			instance = t;
			
			for(Window w: UI.getVisibleWindows())
			{
				SwingUtilities.updateComponentTreeUI(w);
			}
		}
	}


	
	public static ATheme create(String name)
	{
		ATheme t;
		if(NAME_DARK.equals(name))
		{
			// TODO
			t = new DarkTheme(NAME_DARK);
		}
		else if(NAME_MAC.equals(name))
		{
			t = new ATheme(NAME_MAC);
		}
		else if(NAME_OS.equals(name))
		{
			t = new ATheme(NAME_OS);
		}
		else if(NAME_WINDOWS.equals(name))
		{
			t = new ATheme(NAME_WINDOWS);
		}
		else
		{
			t = new ThemeAndy();
		}
		
		t.customize();
		t.prepare();
		return t;
	}
	
	
	private static void setBySuffix(UIDefaults defs, Object value, String suffix)
	{
		for(Object key: defs.keySet())
		{
			if(key instanceof String)
			{
				String k = key.toString();
				if(k.endsWith(suffix))
				{
					defs.put(key, value);
				}
			}
		}
	}


	private static void setValue(UIDefaults defs, Object value, String... keys)
	{
		for(Object key: keys)
		{
			defs.put(key, value);
		}
	}


	private static void initFonts(UIDefaults defs)
	{
		// base font
		Font f = UIManager.getLookAndFeelDefaults().getFont("Panel.font");
		basePlainFont = f.deriveFont(Font.PLAIN, f.getSize2D());

		// monospaced font
		int size;
		if(CPlatform.isWindows())
		{
			size = 12;
		}
		else
		{
			size = basePlainFont.getSize();
		}
		baseMonospacedFont = new Font("Monospaced", Font.PLAIN, size);
		
		// force font
		setBySuffix(defs, basePlainFont, ".font");
	}
	
	
	private static void initColors(UIDefaults d)
	{
		basePanelBG = d.getColor("Panel.background");
		baseSelectionFG = d.getColor("TextField.selectionForeground");
		baseSelectionBG = d.getColor("TextField.selectionBackground");
		baseTextBG = d.getColor("TextField.background");
		baseTextFG = d.getColor("TextField.foreground");
		
		// panel background
		setValue
		(
			d, 
			Theme.PANEL_BG,
			"Button.background",
			"Button.light",
			"CheckBox.background",
			"CheckBox.light",
			"ColorChooser.background",
			"ColorChooser.swatchesDefaultRecentColor",
			"ComboBox.buttonBackground",
			"ComboBox.disabledBackground",
			"EditorPane.disabledBackground",
			"FormattedTextField.disabledBackground",
			"FormattedTextField.inactiveBackground",
			"InternalFrame.activeBorderColor",
			"InternalFrame.borderColor",
			"InternalFrame.borderLight",
			"InternalFrame.inactiveBorderColor",
			"InternalFrame.inactiveTitleForeground",
			"InternalFrame.minimizeIconBackground",
			"InternalFrame.resizeIconHighlight",
			"Label.background",
			"Menu.background",
			"MenuBar.background",
			"OptionPane.background",
			"Panel.background",
			"PasswordField.disabledBackground",
			"PasswordField.inactiveBackground",
			"ProgressBar.background",
			"ProgressBar.selectionForeground",
			"RadioButton.background",
			"RadioButton.light",
			"RadioButtonMenuItem.background",
			"ScrollBar.background",
			"ScrollBar.foreground",
			"ScrollBar.thumb",
			"ScrollBar.trackForeground",
			"ScrollPane.background",
			"Slider.background",
			"Slider.foreground",
			"Spinner.background",
			"Spinner.foreground",
			"SplitPane.background",
			"TabbedPane.background",
			"TabbedPane.light",
			"TabbedPane.contentAreaColor",
			"Table.light",
			"TableHeader.background",
			"TextArea.disabledBackground",
			"TextField.disabledBackground",
			"TextField.inactiveBackground",
			"TextField.light",
			"TextPane.disabledBackground",
			"ToggleButton.background",
			"ToggleButton.light",
			"ToolBar.background",
			"ToolBar.dockingBackground",
			"ToolBar.floatingBackground",
			"ToolBar.light",
			"Viewport.background",
			"activeCaptionBorder",
			"control",
			"controlHighlight",
			"inactiveCaptionBorder",
			"inactiveCaptionText",
			"menu",
			"scrollbar"
		);

		// selection
		setBySuffix(d, Theme.TEXT_SELECTION_FG, ".selectionForeground");
		setBySuffix(d, Theme.TEXT_SELECTION_BG, ".selectionBackground");
		
		// caret
		setBySuffix(d, Theme.TEXT_FG, ".caretForeground");
		
		// password field
		d.put("PasswordField.background", Theme.TEXT_BG);
		d.put("PasswordField.border", Theme.fieldBorder());
		d.put("PasswordField.foreground", Theme.TEXT_FG);
		d.put("PasswordField.inactiveBackground", Theme.FIELD_BG);
		d.put("PasswordField.inactiveForeground", Theme.FIELD_FG);
	}
	
	
	public Color getColor(ThemeKey k)
	{
		Object v = map.get(k);
		if(v instanceof Color)
		{
			return (Color)v;
		}
		
		Log.err("color not found " + k + " in " + name);
		return Color.magenta;
	}
	

	public Font getFont(ThemeKey k)
	{
		Object v = map.get(k);
		if(v instanceof Font)
		{
			return (Font)v;
		}
		
		Log.err("font not found " + k + " in " + name);
		return basePlainFont;
	}
	
	
	protected void set(ThemeKey k, Object x)
	{
		map.put(k, x);
	}
	
	
	public static Color shadow(double alpha)
	{
		return ThemeColor.create(ThemeKey.TEXT_FG, alpha);
	}
	
	
	public static Color highlight(double alpha)
	{
		return ThemeColor.create(ThemeKey.TEXT_BG, alpha);
	}
	
	
	public boolean isDark()
	{
		return dark;
	}
	
	
	protected void prepare()
	{
		Color c = getColor(ThemeKey.TEXT_BG);
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		dark = Math.sqrt(r*r + g*g + b*b) < 128; 
	}
}
