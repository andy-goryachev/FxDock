package goryachev.common.ui.table;
import goryachev.common.ui.CBorder;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;


public class CBooleanRenderer
	extends JCheckBox
	implements TableCellRenderer, UIResource
{
	private static final CBorder noFocusBorder = new CBorder(1);


	public CBooleanRenderer()
	{
		setHorizontalAlignment(JLabel.CENTER);
		setBorderPainted(true);
	}


	public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col)
	{
		if(sel)
		{
			setForeground(t.getSelectionForeground());
			super.setBackground(t.getSelectionBackground());
		}
		else
		{
			setForeground(t.getForeground());
			setBackground(t.getBackground());
		}
		
		setSelected((val != null && ((Boolean)val).booleanValue()));

		if(focus)
		{
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		}
		else
		{
			setBorder(noFocusBorder);
		}

		return this;
	}
}