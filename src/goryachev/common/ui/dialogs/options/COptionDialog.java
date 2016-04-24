// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Menus;
import goryachev.common.ui.options.OptionEditorInterface;
import java.awt.Component;


public class COptionDialog
	extends CDialog
{
	public final CAction okAction = new CAction() { public void action() { onOk(); } };
	public final OptionPanel optionPanel;
	public final CButtonPanel buttonPanel;
	private boolean changed;
	private boolean committed;
	

	public COptionDialog(Component parent, String title, OptionTreeNode root, String name)
	{
		super(parent, name, true);
		setTitle(title);
		setMinimumSize(500, 400);

		optionPanel = new OptionPanel(root);

		buttonPanel = new CButtonPanel(10);
		buttonPanel.addButton(Menus.Cancel, closeDialogAction);
		buttonPanel.addButton(Menus.OK, okAction, true);
		buttonPanel.setBorder(new CBorder(10));

		CPanel p = new CPanel();
		p.setCenter(optionPanel);
		p.setSouth(buttonPanel);

		getContentPane().add(p);
		setSize(750, 400);

		setCloseOnEscape();
	}
	
	
	public void setRoot(OptionTreeNode root)
	{
		optionPanel.setRoot(root);
	}


	public void onWindowOpened()
	{
		optionPanel.ensureSelection();
		//optionPanel.tree.requestFocus();
		optionPanel.filter.requestFocus();
	}
	

	public void onWindowClosed()
	{
		if(!committed)
		{
			optionPanel.revert();
		}
	}

	
	public void expandTree()
	{
		optionPanel.tree.expandAll();
		optionPanel.tree.changeSelection(0, 0, false, false);
	}
	

	public boolean onWindowClosing()
	{
		return true;
	}


	public void close()
	{
		try
		{
			if(optionPanel.isModified())
			{
				switch(Dialogs.discardChanges(this))
				{
				case DISCARD:
					break;
				case SAVE:
					save();
					break;
				default:
					return;
				}
			}

			super.close();
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}
	}


	protected void save() throws Exception
	{
		optionPanel.commit();
		changed = true; // what??
	}
	
	
	public boolean isChanged()
	{
		return changed;
	}
	
	
	protected void onOk()
	{
		try
		{
			save();
			committed = true;
			super.close();
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}	
	}
	
	
	public void setSelected(OptionEditorInterface ed)
	{
		optionPanel.setSelected(ed);
	}
}
