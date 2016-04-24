// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.options.OptionEditorInterface;
import javax.swing.JComponent;


public class OptionEntry
{
	private final String name;
	private final OptionEditorInterface editor;
	private boolean section;
	private boolean restartRequired;


	public OptionEntry(String name, OptionEditorInterface ed, boolean section)
	{
		this.name = name;
		this.editor = ed;
		this.section = section;
		
		if(ed != null)
		{
			ed.init();
		}
	}
	
	
	public OptionEditorInterface getEditor()
	{
		return editor;
	}
	
	
	public String getEditorSearchString()
	{
		if(editor != null)
		{
			return editor.getSearchString();
		}
		return null;
	}
	
	
	public boolean isSection()
	{
		return section;
	}


	public float getPreferredHeight()
	{
		return editor == null ? OptionEditorInterface.HEIGHT_MIN : editor.getPreferredHeight();
	}


	public boolean isFullWidth()
	{
		return editor == null ? false : editor.isFullWidth();
	}


	public void setRestartRequired(boolean on)
	{
		restartRequired = on;
	}


	public boolean isRestartRequired()
	{
		return restartRequired;
	}


	public String getName()
	{
		return name;
	}


	public JComponent getComponent()
	{
		return (editor == null ? null : editor.getComponent());
	}


	public boolean isModified()
	{
		if(editor != null)
		{
			return editor.isModified();
		}
		return false;
	}


	public void commit() throws Exception
	{
		editor.commit();
	}
	
	
	public void revert()
	{
		if(editor != null)
		{
			editor.revert();
		}
	}
}