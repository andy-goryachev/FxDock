// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.options.OptionEditorInterface;
import javax.swing.ImageIcon;


public class OptionTreeBuilder
{
	private OptionTreeNode root;
	private OptionTreeNode last;
	private OptionEntry lastEntry;


	public OptionTreeBuilder()
	{
		root = new OptionTreeNode(null, null);
		last = root;
	}


	public OptionTreeNode getRoot()
	{
		return root;
	}


	public void addChild(ImageIcon icon, String name)
	{
		OptionTreeNode nd = new OptionTreeNode(icon, name);
		last.addChild(nd);
		last = nd;
	}


	public void addChild(String name)
	{
		addChild(null, name);
	}
	

	public void addSection(String title)
	{
		lastEntry = last.addSection(title);
	}


	public void addOption(OptionEditorInterface x)
	{
		lastEntry = last.addOption(null, x);
	}
	
	
	public void addOption(String title, OptionEditorInterface x)
	{
		lastEntry = last.addOption(title, x);
	}


	public void setRestartRequired()
	{
		lastEntry.setRestartRequired(true);
	}


	public void end()
	{
		last = (OptionTreeNode)last.getParent();
		lastEntry = last.getLastEntry();
	}
}
