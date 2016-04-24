// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AgTabbedPaneUI;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import javax.swing.JTabbedPane;


public class CTabbedPane
	extends JTabbedPane
{
	public CTabbedPane()
	{
		setFocusable(false);
	}
	
	
	public void updateUI()
	{
		setUI(new AgTabbedPaneUI());
	}
	
	
	public AgTabbedPaneUI getCTabbedPaneUI()
	{
		return (AgTabbedPaneUI)getUI();
	}
	
	
	public void setEnabled(Component c, boolean on)
	{
		int ix = indexOfComponent(c);
		if(ix >=0 )
		{
			setEnabledAt(ix, on);
		}
	}


	public Component[] getPanels()
	{
		int sz = getTabCount();
		Component[] cs = new Component[sz];
		for(int i=0; i<sz; i++)
		{
			cs[i] = getComponentAt(i);
		}
		return cs;
	}


	// select the tab which is the ancestor of the specified component 
	public static void selectTab(Component c)
	{
		while(c != null)
		{
			Container parent = c.getParent();
			if(parent instanceof JTabbedPane)
			{
				((JTabbedPane)parent).setSelectedComponent(c);
			}
			
			c = parent;
		}
	}
	
	
	public void setContentBorderInsets(Insets m)
	{
		getCTabbedPaneUI().setContentBorderInsets(m);
	}
	
	
	public void setContentBorderInsets(int m)
	{
		getCTabbedPaneUI().setContentBorderInsets(new Insets(m, m, m, m));
	}
}
