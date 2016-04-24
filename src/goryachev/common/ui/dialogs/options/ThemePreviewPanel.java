// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CRadioButton;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.CTextPane;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTable;
import goryachev.common.ui.text.CDocument;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.util.TXT;
import goryachev.common.util.html.HtmlTools;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class ThemePreviewPanel 
	extends CPanel
{
	public final CAction dummyAction = new CAction() { public void action() { }};
	private boolean editor;
	private static final int GAP = 5;
	private static final Object KEY = new Object();
	
	
	public ThemePreviewPanel(boolean editor)
	{
		this.editor = editor;
		
		setPreferredSize(-1, 300);
		
		// text field
		CTextField textField = createTextField("3.141592");
		
		// combo box
		CComboBox comboBox = createComboBox(new Object[] 
		{
			"One", 
			"Two", 
			"Three", 
			"Four", 
			"Five", 
			"Six", 
			"Seven", 
			"Eight", 
			"Nine", 
			"Ten", 
			"Eleven", 
			"Twelve", 
			"Thirteen", 
			"Fourteen", 
			"Fifteen", 
			"Sixteen" 
		});
		
		// checkbox
		CCheckBox checkBox = createCheckBox("Enabled", true); 
		CCheckBox checkBoxDisabled = createCheckBox("Disabled", false);

		// radio button
		CRadioButton radioButton = createRadioButton("Enabled", true);
		CRadioButton radioButtonDisabled = createRadioButton("Disabled", false);

		// text area
		JComponent textArea = createTextArea();
		
		// table
		JComponent table = createTable();
		
		// toolbar
		JComponent tb = createToolBar(); 

		CPanel p = new CPanel();
		p.setGaps(5, 2);
		p.setBorder(5);

		p.addColumns
		(
			CPanel.PREFERRED, 
			CPanel.FILL,
			CPanel.FILL, 
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.FILL
		);

		p.row(0, p.label("Text field:"));
		p.row(1, 2, textField);
		p.row(3, p.label("Check box:"));
		p.row(4, checkBox);
		p.row(5, checkBoxDisabled);
		p.nextRow();
		p.row(0, p.label("Combo box:"));
		p.row(1, 2, comboBox);
		p.row(3, p.label("Radio button:"));
		p.row(4, radioButton);
		p.row(5, radioButtonDisabled);
		p.nextRow();
		p.row(0, new JLabel("Text area:"));
		p.row(3, new JLabel("Table:"));
		p.nextFillRow();
		p.row(0, 3, textArea);
		p.row(3, 3, table);
		p.nextRow();
		p.row(0, 6, createStatusBar());

		setFeatures
		(
			p,
			"Panel",
			ThemeKey.PANEL_BG
		);
		
		CPanel pp = new CPanel(false);
		pp.setCenter(p);
		pp.setBorder(new CompoundBorder(new CBorder(GAP), new CBorder(1, Theme.TEXT_FG)));
		pp.setNorth(tb);
		
		setBackground(Theme.FIELD_BG);
		setCenter(pp);
	}
	
	
	protected Features setFeatures(JComponent c, String name, ThemeKey ... keys)
	{
		Features f = new Features(name);
		c.putClientProperty(KEY, f);
		
		if(editor)
		{
			c.setToolTipText
			(
				"<html>" + 
				HtmlTools.safe(name) +
				"<br>" +
				HtmlTools.safe(TXT.get("Features.tooltip", "Click to reveal fonts and colors that can be changed."))
			);
		}
		
		for(ThemeKey x: keys)
		{
			f.add(x);
		}
		return f;
	}
	
	
	public static Features getFeatures(Component c)
	{
		while(c != null)
		{
			if(c instanceof JComponent)
			{
				Object v = ((JComponent)c).getClientProperty(KEY);
				if(v instanceof Features)
				{
					return (Features)v;
				}
			}
			
			c = c.getParent();
		}
		return null;
	}
	

	protected TableModel createTableModel()
	{
		ZModel<Entry> m = new ZModel();
		m.addColumn("C1", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x) { return x.key; }
		});
		m.addColumn("C2", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x) { return x.value; }
		});
		m.setRightAlignment();
		
		for(int i=1; i<=5; i++)
		{
			Entry en = new Entry();
			en.key = String.valueOf(i);
			en.value = i;
			m.addItem(en);
		}
		
		return m;
	}
	
	
//	public static String getComponentName(Component c)
//	{
//		if(c instanceof JComponent)
//		{
//			Object v = ((JComponent)c).getClientProperty(PROPERTY_NAME);
//			return Parsers.parseString(v);
//		}
//	    return null;
//	}
	
	
	protected CTextField createTextField(String text)
	{
		CTextField t = new CTextField(text);
		
		setFeatures
		(
			t,
			"Text Field",
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);
		
		return t;
	}
	
	
	protected CComboBox createComboBox(Object[] items)
	{
		CComboBox t = new CComboBox(items);
		
		setFeatures
		(
			t,
			"Combo Box",
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);
		
		return t;
	}
	
	
	protected CCheckBox createCheckBox(String text, boolean enabled)
	{
		CCheckBox t = new CCheckBox(text);
		t.setSelected(true);
		t.setEnabled(enabled);
		
		setFeatures
		(
			t,
			"Check Box",
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);
		
		return t;
	}
	
	
	protected CRadioButton createRadioButton(String text, boolean enabled)
	{
		CRadioButton t = new CRadioButton(text);
		t.setSelected(true);
		t.setEnabled(enabled);
		
		setFeatures
		(
			t,
			"Radio Button",
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);
		
		return t;
	}
	
	
	protected JComponent createTextArea()
	{
		CDocumentBuilder b = new CDocumentBuilder();
		b.setForeground(Theme.TEXT_FG);
		b.setBackground(Theme.TEXT_BG);
		b.setFont(Theme.titleFont());
		b.a("Title");
		b.setFont(Theme.plainFont());
		b.a("\nNormal font").nl();
		b.bold("Bold typeface").nl();
		b.setForeground(Theme.TEXT_SELECTION_FG);
		b.setBackground(Theme.TEXT_SELECTION_BG);
		b.a("Selected text.").nl();
		b.setForeground(Theme.TEXT_FG);
		b.setBackground(Theme.TEXT_BG);
		b.setFont(Theme.monospacedFont());
		b.a("Monospaced font\n    line 1.\n    line 2.\n\n\n\n\n\n").nl();
		
		CDocument d = b.getDocument();
		d.setDocumentFilter(new DocumentFilter()
		{
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException { }
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException { }
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException { }
		});
		
		CTextPane t = new CTextPane();
		t.setScrollableTracksViewportWidth(true);
		t.setDocument0(d);
		t.select(10, 30);
		
		setFeatures
		(
			t,
			"Text Area",
			ThemeKey.BASE_FONT,
			ThemeKey.TITLE_FONT,
			ThemeKey.MONOSPACED_FONT,
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);

		CScrollPane sp = new CScrollPane(t);
		sp.setBorder(Theme.fieldBorder());
		return sp;
	}
	
	
	protected JComponent createTable()
	{
		ZTable t = new ZTable(createTableModel());
		t.changeSelection(0, 0, false, false);
		t.changeSelection(1, 0, false, true);
		
		setFeatures
		(
			t, 
			"Table", 
			ThemeKey.BASE_FONT,
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG,
			ThemeKey.GRID_COLOR,
			ThemeKey.FOCUS_COLOR
		);
		
		CScrollPane sp = new CScrollPane(t);
		sp.setBorder(Theme.lineBorder());
		return sp;
	}
	
	
	protected CButton createButton(String text, String name, Action a, ThemeKey k)
	{
		Color c = Theme.getColor(k);
		CButton b = new CButton(text, a, c);
		
		if(k == null)
		{
			setFeatures(b, name, ThemeKey.TEXT_FG);
		}
		else
		{
			setFeatures(b, name, ThemeKey.TEXT_FG, k);
		}

		return b;
	}
	
	
	protected JComponent createStatusBar()
	{
		JLabel t = new JLabel(Application.getCopyright());
		t.setForeground(Theme.PANEL_FG);
		
		JLabel link = new JLabel("http://goryachev.com");
		link.setForeground(Theme.LINK_COLOR);
		
		setFeatures
		(
			link, 
			"Link",
			ThemeKey.LINK_COLOR
		);
		
		CPanel p = new CPanel(false);
		p.setHGap(5);
		p.addColumns(CPanel.FILL, CPanel.PREFERRED, CPanel.PREFERRED);
		p.row(1, t);
		p.row(2, link);
		return p;
	}
	
	
	protected JComponent createToolBar()
	{
		JLabel p = new JLabel("Preview");
		p.setFont(Theme.titleFont());
		
		setFeatures
		(
			p, 
			"Title",
			ThemeKey.TITLE_FONT,
			ThemeKey.TEXT_FG
		);
		
		CToolBar t = Theme.toolbar();
		t.space(5);
		t.add(p);
		t.fill();
		t.add("Toolbar buttons:");
		t.space(5);
		t.add(createButton("Affirmative", "Affirmative Button", dummyAction, ThemeKey.AFFIRM_BUTTON_COLOR));
		t.add(createButton("Destructive", "Destructive Action Button", dummyAction, ThemeKey.DESTRUCTIVE_BUTTON_COLOR));
		t.add(createButton("Disabled", "Disabled Button", CAction.TODO, null));
		t.add(createButton("Regular", "Button", dummyAction, null));

		setFeatures
		(
			t, 
			"Tool Bar", 
			ThemeKey.TOOLBAR_COLOR,
			ThemeKey.TEXT_FG
		);
		
		return t;
	}

	
	//
	
	
	protected static class Entry
	{
		public String key;
		public Object value;
	}
}
