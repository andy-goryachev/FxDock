// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


/** 
 * This class attempts to rectify java bug
 * http://bugs.sun.com/view_bug.do?bug_id=4265389
 * by adding support for component orientation.
 */
public class CSplitPaneDivider
	extends BasicSplitPaneDivider
{
	private static Border border;
	protected DragControllerHack dragger;
	protected MouseHandlerHack mouseHandler;


	public CSplitPaneDivider(BasicSplitPaneUI ui)
	{
		super(ui);
	}


	public void paint(Graphics g)
	{
		Color bgColor = (splitPane.hasFocus()) ? UIManager.getColor("SplitPane.shadow") : getBackground();
		Dimension size = getSize();

		if(bgColor != null)
		{
			g.setColor(bgColor);
			g.fillRect(0, 0, size.width, size.height);
		}
		super.paint(g);
	}


	public void setBorder(Border border)
	{
	}


	public Border getBorder()
	{
		if(border == null)
		{
			border = new CSplitPaneDividerBorder();
		}
		return border;
	}
	
	
	public JSplitPane splitPane()
	{
		return splitPane;
	}
	
	
	public boolean isLTR()
	{
		if(splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
		{
			return splitPane.getComponentOrientation().isLeftToRight();
		}
		return true;
	}
	
	
	public int getOrientation()
	{
		return orientation;
	}


	public void prepareForDragging()
	{
		super.prepareForDragging();
	}


	public void dragDividerTo(int location)
	{
		super.dragDividerTo(location);
	}


	public void finishDraggingTo(int location)
	{
		super.finishDraggingTo(location);
	}


	public void setMouseOver(boolean on)
	{
		super.setMouseOver(on);
	}


	public Component hiddenDivider()
	{
		return hiddenDivider;
	}
	
	
	public void setHiddenDivider(Component c)
	{
		hiddenDivider = c;
	}


	public void setBasicSplitPaneUI(BasicSplitPaneUI newUI)
	{
		if(splitPane != null)
		{
			splitPane.removePropertyChangeListener(this);
			if(mouseHandler != null)
			{
				splitPane.removeMouseListener(mouseHandler);
				splitPane.removeMouseMotionListener(mouseHandler);
				removeMouseListener(mouseHandler);
				removeMouseMotionListener(mouseHandler);
				mouseHandler = null;
			}
		}
		
		splitPaneUI = newUI;
		
		if(newUI != null)
		{
			splitPane = newUI.getSplitPane();
			if(splitPane != null)
			{
				if(mouseHandler == null)
				{
					mouseHandler = new MouseHandlerHack();
				}
				
				splitPane.addMouseListener(mouseHandler);
				splitPane.addMouseMotionListener(mouseHandler);
				addMouseListener(mouseHandler);
				addMouseMotionListener(mouseHandler);
				splitPane.addPropertyChangeListener(this);
				if(splitPane.isOneTouchExpandable())
				{
					oneTouchExpandableChanged();
				}
			}
		}
		else
		{
			splitPane = null;
		}
	}
	
	
	//
	
	
	public class MouseHandlerHack 
		extends MouseAdapter
	{
		public void mousePressed(MouseEvent ev)
		{
			JSplitPane splitPane = splitPane();
			int orientation = getOrientation();
			BasicSplitPaneUI splitPaneUI = getBasicSplitPaneUI();
			
			if((ev.getSource() == CSplitPaneDivider.this || ev.getSource() == splitPane) && dragger == null && splitPane.isEnabled())
			{
				Component newHiddenDivider = splitPaneUI.getNonContinuousLayoutDivider();

				if(hiddenDivider() != newHiddenDivider)
				{
					if(hiddenDivider() != null)
					{
						hiddenDivider().removeMouseListener(this);
						hiddenDivider().removeMouseMotionListener(this);
					}
					
					setHiddenDivider(newHiddenDivider);
					
					if(hiddenDivider() != null)
					{
						hiddenDivider().addMouseMotionListener(this);
						hiddenDivider().addMouseListener(this);
					}
				}
				if(splitPane.getLeftComponent() != null && splitPane.getRightComponent() != null)
				{
					if(orientation == JSplitPane.HORIZONTAL_SPLIT)
					{
						dragger = new DragControllerHack(ev);
					}
					else
					{
						dragger = new VerticalDragControllerHack(ev);
					}
					
					if(!dragger.isValid())
					{
						dragger = null;
					}
					else
					{
						prepareForDragging();
						dragger.continueDrag(ev);
					}
				}
				ev.consume();
			}
		}


		public void mouseReleased(MouseEvent ev)
		{
			if(dragger != null)
			{
				if(ev.getSource() == splitPane())
				{
					dragger.completeDrag(ev.getX(), ev.getY());
				}
				else if(ev.getSource() == CSplitPaneDivider.this)
				{
					Point ourLoc = getLocation();

					dragger.completeDrag(ev.getX() + ourLoc.x, ev.getY() + ourLoc.y);
				}
				else if(ev.getSource() == hiddenDivider())
				{
					Point hDividerLoc = hiddenDivider().getLocation();
					int ourX = ev.getX() + hDividerLoc.x;
					int ourY = ev.getY() + hDividerLoc.y;

					dragger.completeDrag(ourX, ourY);
				}
				dragger = null;
				ev.consume();
			}
		}


		public void mouseDragged(MouseEvent ev)
		{
			if(dragger != null)
			{
				if(ev.getSource() == splitPane())
				{
					dragger.continueDrag(ev.getX(), ev.getY());
				}
				else if(ev.getSource() == CSplitPaneDivider.this)
				{
					Point ourLoc = getLocation();

					dragger.continueDrag(ev.getX() + ourLoc.x, ev.getY() + ourLoc.y);
				}
				else if(ev.getSource() == hiddenDivider())
				{
					Point hDividerLoc = hiddenDivider().getLocation();
					int ourX = ev.getX() + hDividerLoc.x;
					int ourY = ev.getY() + hDividerLoc.y;

					dragger.continueDrag(ourX, ourY);
				}
				ev.consume();
			}
		}


		public void mouseMoved(MouseEvent ev)
		{
		}


		public void mouseEntered(MouseEvent ev)
		{
			if(ev.getSource() == CSplitPaneDivider.this)
			{
				setMouseOver(true);
			}
		}


		public void mouseExited(MouseEvent ev)
		{
			if(ev.getSource() == CSplitPaneDivider.this)
			{
				setMouseOver(false);
			}
		}
	}


	//


	public class DragControllerHack
	{
		protected int initialX;
		protected int maxX;
		protected int minX;
		protected int offset;


		protected DragControllerHack(MouseEvent ev)
		{
			JSplitPane splitPane = getBasicSplitPaneUI().getSplitPane();
			
			boolean ltr = isLTR();
			
			Component leftC = ltr ? splitPane.getLeftComponent() : splitPane.getRightComponent();
			Component rightC = ltr ? splitPane.getRightComponent() : splitPane.getLeftComponent();

			initialX = getLocation().x;
			if(ev.getSource() == CSplitPaneDivider.this)
			{
				offset = ev.getX();
			}
			else
			{ 
				// splitPane
				offset = ev.getX() - initialX;
			}
			if(leftC == null || rightC == null || offset < -1 || offset >= getSize().width)
			{
				// Don't allow dragging.
				maxX = -1;
			}
			else
			{
				Insets insets = splitPane.getInsets();

				if(leftC.isVisible())
				{
					minX = leftC.getMinimumSize().width;
					if(insets != null)
					{
						minX += insets.left;
					}
				}
				else
				{
					minX = 0;
				}
				if(rightC.isVisible())
				{
					int right = (insets != null) ? insets.right : 0;
					maxX = Math.max(0, splitPane.getSize().width - (getSize().width + right) - rightC.getMinimumSize().width);
				}
				else
				{
					int right = (insets != null) ? insets.right : 0;
					maxX = Math.max(0, splitPane.getSize().width - (getSize().width + right));
				}
				if(maxX < minX)
				{
					minX = maxX = 0;
				}
			}
		}


		public boolean isValid()
		{
			return (maxX > 0);
		}


		protected int positionForMouseEvent(MouseEvent e)
		{
			int newX = (e.getSource() == CSplitPaneDivider.this) ? (e.getX() + getLocation().x) : e.getX();

			newX = Math.min(maxX, Math.max(minX, newX - offset));
			return newX;
		}


		protected int getNeededLocation(int x, int y)
		{
			int newX;

			newX = Math.min(maxX, Math.max(minX, x - offset));
			return newX;
		}


		protected void continueDrag(int newX, int newY)
		{
			dragDividerTo(getNeededLocation(newX, newY));
		}


		protected void continueDrag(MouseEvent e)
		{
			dragDividerTo(positionForMouseEvent(e));
		}


		protected void completeDrag(int x, int y)
		{
			finishDraggingTo(getNeededLocation(x, y));
		}


		protected void completeDrag(MouseEvent e)
		{
			finishDraggingTo(positionForMouseEvent(e));
		}
	}
	
	
	//


	public class VerticalDragControllerHack
		extends DragControllerHack
	{
		public VerticalDragControllerHack(MouseEvent ev)
		{
			super(ev);
			
			JSplitPane splitPane = getBasicSplitPaneUI().getSplitPane();
			Component leftC = splitPane.getLeftComponent();
			Component rightC = splitPane.getRightComponent();

			initialX = getLocation().y;
			if(ev.getSource() == CSplitPaneDivider.this)
			{
				offset = ev.getY();
			}
			else
			{
				offset = ev.getY() - initialX;
			}
			
			if(leftC == null || rightC == null || offset < -1 || offset > getSize().height)
			{
				// Don't allow dragging.
				maxX = -1;
			}
			else
			{
				Insets insets = splitPane.getInsets();

				if(leftC.isVisible())
				{
					minX = leftC.getMinimumSize().height;
					if(insets != null)
					{
						minX += insets.top;
					}
				}
				else
				{
					minX = 0;
				}
				
				if(rightC.isVisible())
				{
					int bottom = (insets != null) ? insets.bottom : 0;

					maxX = Math.max(0, splitPane.getSize().height - (getSize().height + bottom) - rightC.getMinimumSize().height);
				}
				else
				{
					int bottom = (insets != null) ? insets.bottom : 0;

					maxX = Math.max(0, splitPane.getSize().height - (getSize().height + bottom));
				}
				
				if(maxX < minX)
				{
					minX = maxX = 0;
				}
			}
		}


		protected int getNeededLocation(int x, int y)
		{
			int newY = Math.min(maxX, Math.max(minX, y - offset));
			return newY;
		}


		protected int positionForMouseEvent(MouseEvent e)
		{
			int newY = (e.getSource() == CSplitPaneDivider.this) ? (e.getY() + getLocation().y) : e.getY();
			newY = Math.min(maxX, Math.max(minX, newY - offset));
			return newY;
		}
	}
}