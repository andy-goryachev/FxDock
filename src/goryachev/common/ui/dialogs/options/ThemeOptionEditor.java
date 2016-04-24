// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.InputTracker;
import goryachev.common.ui.Theme;
import goryachev.common.ui.options.OptionEditorInterface;
import goryachev.common.ui.theme.ATheme;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import javax.swing.JComponent;


public class ThemeOptionEditor
	extends CPanel
	implements OptionEditorInterface
{
	public final CAction editThemeAction = new CAction() { public void action() { actionEditTheme(); }};
	public final CComboBox selectorField;
	protected ThemePreviewPanel preview;
	private String old;
	private boolean changed;
	
	
	public ThemeOptionEditor()
	{
		old = Theme.getTheme();
		
		selectorField = new CComboBox(Theme.getAvailableThemes());
		selectorField.setSelectedItem(old);
		new InputTracker(selectorField)
		{
			public void onInputEvent()
			{
				updateTheme();
			}
		};
		
		preview = new ThemePreviewPanel(false);
		
		// FIX
		editThemeAction.setEnabled(System.getProperty("edit.theme") != null);
		
		CPanel p = new CPanel(false);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED
		);
		
		p.setBorder(5);
		p.setGaps(5, 5);
		p.row(0, p.label(TXT.get("ThemeOptionEditor.theme name", "Name:")));
		p.row(1, selectorField);
		p.row(2, new CButton("Customize", editThemeAction));
		
		setNorth(p);
		setCenter(preview);
	}
	

	public void init()
	{
	}


	public JComponent getComponent()
	{
		return this;
	}


	public boolean isFullWidth()
	{
		return true;
	}


	public float getPreferredHeight()
	{
		return HEIGHT_MAX;
	}


	public boolean isModified()
	{
		return CKit.notEquals(old, getThemeName());
	}
	
	
	public String getThemeName()
	{
		return (String)selectorField.getSelectedItem();
	}


	public void commit() throws Exception
	{
		String name = getThemeName();
		Theme.setTheme(name);
	}
	
	
	public void revert()
	{
		if(changed)
		{
			Theme.setTheme(old);
		}
	}


	public String getSearchString()
	{
		return null;
	}
	
	
	protected void updateTheme()
	{
		String name = getThemeName();
		ATheme.setTheme(name, false);
		changed = true;
	}
	
	
	protected void actionEditTheme()
	{
		String base = getThemeName();
		new ThemeEditorDialog(this, base).open();
	}
}
