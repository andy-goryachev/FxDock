// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.icons.CIcons;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


public class CTreeCellRenderer
	extends DefaultTreeCellRenderer
{
	public CTreeCellRenderer()
	{
		setBorderSelectionColor(null);
		
		setLeafIcon(CIcons.Page);
		setClosedIcon(CIcons.Folder);
		setOpenIcon(CIcons.FolderOpen);

//		setTextSelectionColor(DefaultLookup.getColor(this, ui, "Tree.selectionForeground"));
//		setTextNonSelectionColor(DefaultLookup.getColor(this, ui, "Tree.textForeground"));
//		setBackgroundSelectionColor(DefaultLookup.getColor(this, ui, "Tree.selectionBackground"));
//		setBackgroundNonSelectionColor(DefaultLookup.getColor(this, ui, "Tree.textBackground"));
	}

	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
//		if(value instanceof ADecoratedCell)
//		{
//			super.getTreeCellRendererComponent(tree, null, sel, expanded, leaf, row, hasFocus);
//
//			ADecoratedCell cell = (ADecoratedCell)value;
//			setIcon(cell.getIcon());
//			setText(cell.getText());
//			setToolTipText(cell.getToolTip());
//			
//			
////			if(sel)
////			{
////				setForeground(super.getForeground());
////				setBackground(getBackgroundSelectionColor());
////			}
////			else
////			{
////				setForeground(tree.getForeground());
////				setBackground(tree.getBackground());
////			}
//			
////			Color c = cell.getBackground();
////			if(c != null)
////			{
////				setBackground(CKit.mix(210, super.getBackground(), c));
////			}
//			
////			Color c = cell.getForeground();
////			if(c != null)
////			{
////				setForeground(c);
////			}
////			else
////			{
////				setForeground(getForeground());
////			}
//			
//			if(cell.isBold())
//			{
//				setFont(tree.getFont().deriveFont(Font.BOLD));
//			}
//			else
//			{
//				setFont(tree.getFont());
//			}
//		}
//		else
		{
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
		
//		return this;
	}


//	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
//	{
//		String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
//
//		this.tree = tree;
//		this.hasFocus = hasFocus;
//		setText(stringValue);
//
//		Color fg = null;
//		isDropCell = false;
//
//		JTree.DropLocation dropLocation = tree.getDropLocation();
//		if(dropLocation != null && dropLocation.getChildIndex() == -1 && tree.getRowForPath(dropLocation.getPath()) == row)
//		{
//
//			Color col = DefaultLookup.getColor(this, ui, "Tree.dropCellForeground");
//			if(col != null)
//			{
//				fg = col;
//			}
//			else
//			{
//				fg = getTextSelectionColor();
//			}
//
//			isDropCell = true;
//		}
//		else if(sel)
//		{
//			fg = getTextSelectionColor();
//		}
//		else
//		{
//			fg = getTextNonSelectionColor();
//		}
//
//		setForeground(fg);
//
//		Icon icon = null;
//		if(leaf)
//		{
//			icon = getLeafIcon();
//		}
//		else if(expanded)
//		{
//			icon = getOpenIcon();
//		}
//		else
//		{
//			icon = getClosedIcon();
//		}
//
//		if(!tree.isEnabled())
//		{
//			setEnabled(false);
//			LookAndFeel laf = UIManager.getLookAndFeel();
//			Icon disabledIcon = laf.getDisabledIcon(tree, icon);
//			if(disabledIcon != null)
//				icon = disabledIcon;
//			setDisabledIcon(icon);
//		}
//		else
//		{
//			setEnabled(true);
//			setIcon(icon);
//		}
//		setComponentOrientation(tree.getComponentOrientation());
//
//		selected = sel;
//
//		return this;
//	}
}