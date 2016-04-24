// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;


public class AgTreeUI
	extends BasicTreeUI
{
	private static Color hashLineColor = new Color(180, 180, 180);


	public static void init(UIDefaults defs)
	{
		defs.put("TreeUI", AgTreeUI.class.getName());
//		defs.put("Tree.expandedIcon", Img.get("tree-expanded.png"));
//		defs.put("Tree.collapsedIcon", Img.get("tree-collapsed.png"));
		defs.put("Tree.paintLines", true);
		defs.put("Tree.lineTypeDashed", true);
		defs.put("Tree.drawsFocusBorderAroundIcon", false);
		defs.put("Tree.drawDashedFocusIndicator", true);
		defs.put("Tree.rendererMargins", new Insets(0,2,0,2));
//		defs.put("Tree.dropCellBackground", dropCellColor);
//		defs.put("Tree.textBackground", dropCellColor);
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new AgTreeUI();
	}
	
	
	protected void installDefaults()
	{
		super.installDefaults();
		
		setHashColor(hashLineColor);

//		if(tree.getBackground() == null || tree.getBackground() instanceof UIResource)
//		{
//			tree.setBackground(UIManager.getColor("Tree.background"));
//		}
//		if(getHashColor() == null || getHashColor() instanceof UIResource)
//		{
//			setHashColor(UIManager.getColor("Tree.hash"));
//		}
//		if(tree.getFont() == null || tree.getFont() instanceof UIResource)
//		{
//			tree.setFont(UIManager.getFont("Tree.font"));
//		}
//		// JTree's original row height is 16.  To correctly display the
//		// contents on Linux we should have set it to 18, Windows 19 and
//		// Solaris 20.  As these values vary so much it's too hard to
//		// be backward compatable and try to update the row height, we're
//		// therefor NOT going to adjust the row height based on font.  If the
//		// developer changes the font, it's there responsibility to update
//		// the row height.
//		setExpandedIcon((Icon) UIManager.get("Tree.expandedIcon"));
//		setCollapsedIcon((Icon) UIManager.get("Tree.collapsedIcon"));
//
//		setLeftChildIndent(((Integer) UIManager.get("Tree.leftChildIndent")).intValue());
//		setRightChildIndent(((Integer) UIManager.get("Tree.rightChildIndent")).intValue());
//
		
		// TODO determined by the font
//		LookAndFeel.installProperty(tree, "rowHeight", UIManager.get("Tree.rowHeight"));
		
//
//		largeModel = (tree.isLargeModel() && tree.getRowHeight() > 0);
//
//		Object scrollsOnExpand = UIManager.get("Tree.scrollsOnExpand");
//		if(scrollsOnExpand != null)
//		{
//			LookAndFeel.installProperty(tree, "scrollsOnExpand", scrollsOnExpand);
//		}

//		paintLines = UIManager.getBoolean("Tree.paintLines");
//		lineTypeDashed = UIManager.getBoolean("Tree.lineTypeDashed");

//		Long l = (Long) UIManager.get("Tree.timeFactor");
//		timeFactor = (l != null) ? l.longValue() : 1000L;
//
//		Object showsRootHandles = UIManager.get("Tree.showsRootHandles");
//		if(showsRootHandles != null)
//		{
//			LookAndFeel.installProperty(tree, JTree.SHOWS_ROOT_HANDLES_PROPERTY, showsRootHandles);
//		}
	}


	protected void ensureRowsAreVisible(int beginRow, int endRow)
	{
		if(tree != null && beginRow >= 0 && endRow < getRowCount(tree))
		{
			Rectangle r = tree.getVisibleRect();
			if(beginRow == endRow)
			{
				Rectangle scrollBounds = getPathBounds(tree, getPathForRow(tree, beginRow));
				if(scrollBounds != null)
				{
					scrollBounds.x = r.x;
					scrollBounds.width = r.width;
					tree.scrollRectToVisible(scrollBounds);
				}
			}
			else
			{
				Rectangle beginRect = getPathBounds(tree, getPathForRow(tree, beginRow));
				Rectangle testRect = beginRect;
				int beginY = beginRect.y;
				int maxY = beginY + r.height;

				for(int counter = beginRow + 1; counter <= endRow; counter++)
				{
					testRect = getPathBounds(tree, getPathForRow(tree, counter));
					if((testRect.y + testRect.height) > maxY)
					{
						counter = endRow;
					}
				}
				tree.scrollRectToVisible(new Rectangle(r.x, beginY, 1, testRect.y + testRect.height - beginY));
			}
		}
	}


	protected TreeCellRenderer createDefaultCellRenderer()
	{
		return new CTreeCellRenderer();
	}
	
	
	protected void drawDashedHorizontalLine(Graphics g, int y, int x1, int x2)
	{
		x1 += (x1 % 2);

		for(int x=x1; x<=x2; x+=2)
		{
			g.drawLine(x, y, x, y);
		}
	}


	protected void drawDashedVerticalLine(Graphics g, int x, int y1, int y2)
	{
		y1 += (y1 % 2);

		for(int y=y1; y<=y2; y+=2)
		{
			g.drawLine(x, y, x, y);
		}
	}


	// original ui limits painting only to the text width
	// I want to paint the whole width 
	// also have to override public getPathBounds() because the other one is private
	protected CellRendererPane createCellRendererPane()
	{
		return new CellRendererPane()
		{
			public void paintComponent(Graphics g, Component c, Container p, int x, int y, int w, int h, boolean shouldValidate) 
			{
				w = p.getWidth() - x;

				Insets m = p.getInsets();
				if(m != null)
				{
					w -= m.right;
				}
				super.paintComponent(g, c, p, x, y, w, h, shouldValidate);
			}
		};
	}
	
	
	public Rectangle getPathBounds(JTree tree, TreePath path) 
	{
		Rectangle r = super.getPathBounds(tree, path);
		if(r != null)
		{
			if(tree != null)
			{
				r.width = tree.getWidth() - r.x;
			}
		}
		return r;
	}
}
