// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;


/** Collects user-visible strings from the component hierarchy */
public class StringCollector
{
	private final Component component;
	private final SB sb = new SB();


	public StringCollector(Component c)
	{
		this.component = c;
	}
	
	
	public String collect()
	{
		collect(component);
		return sb.toString();
	}
	
	
	protected void add(Object x)
	{
		if(x != null)
		{
			String s = x.toString();
			if(CKit.isNotBlank(s))
			{
				if(sb.isNotEmpty())
				{
					sb.a(' ');
				}
				
				sb.a(s);
			}
		}
	}


	protected void collectComboBox(JComboBox c)
	{
		ComboBoxModel m = c.getModel();
		int sz = m.getSize();
		for(int i=0; i<sz; i++)
		{
			Object x = m.getElementAt(i);
			add(x);
		}
	}
	
	
	protected void collectTable(JTable t)
	{
		TableModel m = t.getModel();
		int cols = m.getColumnCount();
		int rows = m.getRowCount();
		for(int r=0; r<rows; r++)
		{
			for(int c=0; c<cols; c++)
			{
				Object x = m.getValueAt(r, c);
				add(x);
			}
		}
	}


	protected void collect(Component c)
	{
		if(c instanceof Container)
		{
			Container con = (Container)c;
			int sz = con.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component ch = con.getComponent(i);
				collect(ch);
			}
		}
		else if(c instanceof JLabel)
		{
			add(((JLabel)c).getText());
		}
		else if(c instanceof JTextComponent)
		{
			add(((JTextComponent)c).getText());
		}
		else if(c instanceof JComboBox)
		{
			collectComboBox((JComboBox)c);
		}
		else if(c instanceof JTable)
		{
			collectTable((JTable)c);
		}
		else if(c instanceof AbstractButton)
		{
			add(((AbstractButton)c).getText());
		}
		
		if(c instanceof JComponent)
		{
			String tooltip = ((JComponent)c).getToolTipText();
			add(tooltip);
		}
	}
}
