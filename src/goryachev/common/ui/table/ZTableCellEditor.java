// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class ZTableCellEditor
	extends DefaultCellEditor
{
	public final Border border = new CBorder(Theme.FOCUS_COLOR, 0/*CTableRendererBorder.VGAP - 1*/, CTableRendererBorder.HGAP - 1, 0, 0); 
	
	
	public ZTableCellEditor(JCheckBox c)
	{
		super(c);
	}
	
	
	public ZTableCellEditor(JComboBox c)
	{
		super(c);
	}
	
	
	public ZTableCellEditor(JTextField c)
	{
		super(c);
	}


	public Component getTableCellEditorComponent(JTable t, Object val, boolean selected, int row, int col)
	{
		Component c = super.getTableCellEditorComponent(t, val, selected, row, col);
		if(c instanceof JComponent)
		{
			((JComponent)c).setBorder(border);
		}
		return c;
	}
}
