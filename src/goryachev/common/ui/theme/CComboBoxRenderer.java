// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;


public class CComboBoxRenderer
	extends BasicComboBoxRenderer
{
	private static final Object BORDER_KEY = new StringBuilder("BORDER_KEY");
	private static final Border NULL_BORDER = new EmptyBorder(0, 0, 0, 0);


	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Component rv = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(rv instanceof JComponent)
		{
			JComponent component = (JComponent)rv;
			if(index == -1 && isSelected)
			{
				Border border = component.getBorder();
				Border dashedBorder = new CDashedBorder(list.getForeground());
				component.setBorder(dashedBorder);

				//store current border in client property if needed
				if(component.getClientProperty(BORDER_KEY) == null)
				{
					component.putClientProperty(BORDER_KEY, (border == null) ? NULL_BORDER : border);
				}
			}
			else
			{
				if(component.getBorder() instanceof CDashedBorder)
				{
					Object storedBorder = component.getClientProperty(BORDER_KEY);
					if(storedBorder instanceof Border)
					{
						component.setBorder((storedBorder == NULL_BORDER) ? null : (Border)storedBorder);
					}
					component.putClientProperty(BORDER_KEY, null);
				}
			}
			if(index == -1)
			{
				component.setOpaque(false);
				component.setForeground(list.getForeground());
			}
			else
			{
				component.setOpaque(true);
			}
		}
		return rv;
	}
}