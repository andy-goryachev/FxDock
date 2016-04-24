// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CTabbedPane;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.HorizontalPanel;
import goryachev.common.ui.Menus;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.TXT;
import goryachev.common.util.html.HtmlTools;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class ThemeEditorDialog
	extends CDialog
{
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CTextField nameField;
	public final ThemePreviewPanel preview;
	public final CPanel selectedElementPanel;
	public final CTabbedPane tabbedPane;
	protected final AWTEventListener listener;
	
	
	public ThemeEditorDialog(Component parent, String baseTheme)
	{
		super(parent, "ThemeEditorDialog", true);
		setMinimumSize(1000, 600);
		setTitle("Customize Theme");
		borderless();
		
		nameField = new CTextField();
		nameField.setText(createNewName(baseTheme));
		
		selectedElementPanel = new CPanel();
		
		preview = new ThemePreviewPanel(true);
		preview.setPreferredSize(500, -1);
		
		listener = new AWTEventListener()
		{
			public void eventDispatched(AWTEvent ev)
			{
				if(ev.getID() == MouseEvent.MOUSE_PRESSED)
				{
					handleMouseClick((MouseEvent)ev);
				}
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);
		
		tabbedPane = new CTabbedPane();
		tabbedPane.setContentBorderInsets(1);
		tabbedPane.addTab("Fonts", createFontsPanel());
		tabbedPane.addTab("Colors", createColorsPanel());
		tabbedPane.addTab("Icons", createIconsPanel());
		tabbedPane.addTab("", selectedElementPanel);
		updateTab(null);
		
		HorizontalPanel tb = new HorizontalPanel();
		tb.setGap(5);
		tb.setBorder(5);
		tb.add(new JLabel("Name:"));
		tb.fill(nameField);
		tb.add(new CButton(Menus.Save, saveAction, true));
		
		CPanel ep = new CPanel();
		ep.setNorth(tb);
		ep.setCenter(tabbedPane);
		
		CPanel p = new CPanel();
		p.setWest(preview);
		p.setCenter(ep);
		
		panel().setCenter(p);
		
		UI.later(new Runnable()
		{
			public void run()
			{
				nameField.selectAll();
				nameField.requestFocusInWindow();
			}
		});
	}


	private String createNewName(String baseTheme)
	{
		return baseTheme + " " + new SimpleDateFormat("yyyy-MMdd-HHmmss").format(System.currentTimeMillis());
	}


	public void onWindowClosed()
	{
		Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
	}
	
	
	protected void handleMouseClick(MouseEvent ev)
	{
		Component c = SwingUtilities.getDeepestComponentAt((Component)ev.getSource(), ev.getX(), ev.getY());

		Features fs = ThemePreviewPanel.getFeatures(c);
		if(fs != null)
		{
			selectedElementPanel.setCenter(createEditors(fs.getName(), fs));
			tabbedPane.setSelectedComponent(selectedElementPanel);
			updateTab(fs.getName());
		}
		
		UI.validateAndRepaint(tabbedPane);
	}
	
	
	protected void updateTab(String name)
	{
		int ix = tabbedPane.indexOfComponent(selectedElementPanel);
		if(ix < 0)
		{
			return;
		}
		
		String s;
		if(name == null)
		{
			s = TXT.get("ThemeEditorDialog.tab.selected element", "Selected Element");
		}
		else
		{
			s = "<html>" + TXT.get("ThemeEditorDialog.tab.selected element is NAME", "Selected Element: {0}", "<b>" + HtmlTools.safe(name) + "</b>");
		}
		
		tabbedPane.setTitleAt(ix, s);
	}
	
	
	protected JComponent createEditors(String name, Features fs)
	{
		FontColorEditorPanel p = new FontColorEditorPanel();
		p.createEditors(false, fs, true);
		p.createEditors(true, fs, true);
		return p;
	}
	
	
	protected CPanel createFontsPanel()
	{
		FontColorEditorPanel p = new FontColorEditorPanel();
		p.createEditors(true, Features.getAll(), false);
		return p;
	}
	
	
	protected CPanel createColorsPanel()
	{
		FontColorEditorPanel p = new FontColorEditorPanel();
		p.createEditors(false, Features.getAll(), true);
		return p;
	}
	
	
	protected CPanel createIconsPanel()
	{
		CPanel p = new CPanel();
		p.border();
		p.setGaps(10, 10);
		p.addColumns
		(
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL
		);
		// sort by name?
		ic(p, 0, CIcons.Success32, "Success");
		ic(p, 1, CIcons.Error32, "Error");
		ic(p, 2, CIcons.Cancelled32, "Cancelled");
		ic(p, 3, CIcons.Info32, "Info");
		ic(p, 4, CIcons.Question32, "Question");
		ic(p, 5, CIcons.Warning32, "Warning");
		return p;
	}
	
	
	protected void ic(CPanel p, int col, Icon ic, String text)
	{
		JLabel t = new JLabel(text, ic, JLabel.CENTER);
		t.setVerticalTextPosition(JLabel.BOTTOM);
		t.setHorizontalTextPosition(JLabel.CENTER);
		p.row(col, t); 
	}
	
	
	protected void actionSave()
	{
		// TODO
		close();
	}
}
