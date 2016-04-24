// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import javax.swing.JSplitPane;


/** 
 * This class owes its existence to the java bug
 * http://bugs.sun.com/view_bug.do?bug_id=4265389
 * 
 * This is a variant of BasicSplitPanelUI layout manager which actually
 * honors component orientation.
 * 
 * FIX there is still a problem with divider location:
 * it is an onscreen location instead of logical location from the leading edge,
 * so when GlobalSettings restore it, it's the opposite of what it needs to be.
 */
public class CSplitPaneLayoutManager
	implements LayoutManager2
{
	protected final AgSplitPaneUI ui;
	/* left, right, divider. (in this exact order) */
	protected int[] sizes;
	protected Component[] components;
	/** Size of the splitpane the last time laid out. */
	private int lastSplitPaneSize;
	/** True if resetToPreferredSizes has been invoked. */
	private boolean doReset;
	private boolean horizontalAxis;


	public CSplitPaneLayoutManager(AgSplitPaneUI ui, boolean horizontalAxis)
	{
		this.ui = ui;
		this.horizontalAxis = horizontalAxis;
		components = new Component[3];
		sizes = new int[3];
	}
	
	
	// misnomer: returns true if we don't need to swap left and right component (normal behavior)
	protected boolean isLTR()
	{
		if(horizontalAxis)
		{
			return ui.getSplitPane().getComponentOrientation().isLeftToRight();
		}
		return true;
	}


	/**
	 * Does the actual layout.
	 */
	public void layoutContainer(Container container)
	{
		Dimension containerSize = container.getSize();

		// If the splitpane has a zero size then no op out of here.
		// If we execute this function now, we're going to cause ourselves
		// much grief.
		if(containerSize.height <= 0 || containerSize.width <= 0)
		{
			lastSplitPaneSize = 0;
			return;
		}

		JSplitPane splitPane = ui.getJSplitPane();
		int div = splitPane.getDividerLocation();
		Insets insets = splitPane.getInsets();
		int availableSize = getAvailableSize(containerSize, insets);
		int dOffset = getSizeForPrimaryAxis(insets, true);
		Dimension dSize = (components[2] == null) ? null : components[2].getPreferredSize();

		if((doReset && !ui.dividerLocationIsSetHack) || div < 0)
		{
			resetToPreferredSizes(availableSize);
		}
		else if(lastSplitPaneSize <= 0 || availableSize == lastSplitPaneSize || !ui.paintedHack || (dSize != null && getSizeForPrimaryAxis(dSize) != sizes[2]))
		{
			if(dSize != null)
			{
				sizes[2] = getSizeForPrimaryAxis(dSize);
			}
			else
			{
				sizes[2] = 0;
			}
			
			setDividerLocation(div - dOffset, availableSize);
			ui.dividerLocationIsSetHack = false;
		}
		else if(availableSize != lastSplitPaneSize)
		{
			distributeSpace(availableSize - lastSplitPaneSize, ui.getKeepHiddenHack());
		}
		
		doReset = false;
		ui.dividerLocationIsSetHack = false;
		lastSplitPaneSize = availableSize;

		// Reset the bounds of each component
		
		int nextLocation = getInitialLocation(insets);
		boolean ltr = isLTR();
		
		int i = 0;
		while(i < 3)
		{
			Component c;
			if(ltr)
			{
				c = components[i];
			}
			else
			{
				switch(i)
				{
				case 0:
					c = components[1];
					break;
				case 1:
					c = components[0];
					break;
				default:
					c = components[i];
				}
			}
			
			if(c != null && c.isVisible())
			{
				setComponentToSize(c, sizes[i], nextLocation, insets, containerSize);
				nextLocation += sizes[i];
			}
			
			switch(i)
			{
			case 0:
				i = 2;
				break;
			case 2:
				i = 1;
				break;
			case 1:
				i = 3;
				break;
			}
		}
		
		if(ui.paintedHack)
		{
			// This is tricky, there is never a good time for us
			// to push the value to the splitpane, painted appears to
			// the best time to do it. What is really needed is
			// notification that layout has completed.
			int newLocation = ui.getDividerLocation(splitPane);
			if(newLocation != (div - dOffset))
			{
				int lastLocation = splitPane.getLastDividerLocation();

				ui.ignoreDividerLocationChangeHack = true;
				
				try
				{
					splitPane.setDividerLocation(newLocation);
					
					// This is not always needed, but is rather tricky
					// to determine when... The case this is needed for
					// is if the user sets the divider location to some
					// bogus value, say 0, and the actual value is 1, the
					// call to setDividerLocation(1) will preserve the
					// old value of 0, when we really want the divider
					// location value  before the call. This is needed for
					// the one touch buttons.
					splitPane.setLastDividerLocation(lastLocation);
				}
				finally
				{
					ui.ignoreDividerLocationChangeHack = false;
				}
			}
		}
	}


	/**
	 * Adds the component at place.  Place must be one of JSplitPane.LEFT, RIGHT, TOP, BOTTOM, 
	 * or null (for the divider).
	 */
	public void addLayoutComponent(String place, Component c)
	{
		boolean isValid = true;

		if(place != null)
		{
			if(place.equals(JSplitPane.DIVIDER))
			{
				// divider
				components[2] = c;
				sizes[2] = getSizeForPrimaryAxis(c.getPreferredSize());
			}
			else if(place.equals(JSplitPane.LEFT) || place.equals(JSplitPane.TOP))
			{
				components[0] = c;
				sizes[0] = 0;
			}
			else if(place.equals(JSplitPane.RIGHT) || place.equals(JSplitPane.BOTTOM))
			{
				components[1] = c;
				sizes[1] = 0;
			}
		}
		else
		{
			isValid = false;
		}
		
		if(!isValid)
		{
			throw new IllegalArgumentException("cannot add to layout: " + "unknown constraint: " + place);
		}
		
		doReset = true;
	}


	/**
	 * Returns the minimum size needed to contain the children.
	 * The width is the sum of all the childrens min widths and
	 * the height is the largest of the childrens minimum heights.
	 */
	public Dimension minimumLayoutSize(Container container)
	{
		int minPrimary = 0;
		int minSecondary = 0;
		Insets insets = ui.getSplitPane().getInsets();

		for(int i=0; i<3; i++)
		{
			Component c = components[i];
			if(c != null)
			{
				Dimension minSize = c.getMinimumSize();
				int secSize = getSizeForSecondaryAxis(minSize);

				minPrimary += getSizeForPrimaryAxis(minSize);
				if(secSize > minSecondary)
				{
					minSecondary = secSize;
				}
			}
		}
		
		if(insets != null)
		{
			minPrimary += getSizeForPrimaryAxis(insets, true) + getSizeForPrimaryAxis(insets, false);
			minSecondary += getSizeForSecondaryAxis(insets, true) + getSizeForSecondaryAxis(insets, false);
		}
		
		if(horizontalAxis)
		{
			return new Dimension(minPrimary, minSecondary);
		}
		
		return new Dimension(minSecondary, minPrimary);
	}


	/**
	 * Returns the preferred size needed to contain the children.
	 * The width is the sum of all the childrens preferred widths and
	 * the height is the largest of the childrens preferred heights.
	 */
	public Dimension preferredLayoutSize(Container container)
	{
		int prePrimary = 0;
		int preSecondary = 0;
		Insets insets = ui.getSplitPane().getInsets();

		for(int i=0; i<3; i++)
		{
			Component c = components[i]; 
			if(c != null)
			{
				Dimension preSize = c.getPreferredSize();
				int secSize = getSizeForSecondaryAxis(preSize);

				prePrimary += getSizeForPrimaryAxis(preSize);
				if(secSize > preSecondary)
				{
					preSecondary = secSize;
				}
			}
		}
		
		if(insets != null)
		{
			prePrimary += getSizeForPrimaryAxis(insets, true) + getSizeForPrimaryAxis(insets, false);
			preSecondary += getSizeForSecondaryAxis(insets, true) + getSizeForSecondaryAxis(insets, false);
		}
		
		if(horizontalAxis)
		{
			return new Dimension(prePrimary, preSecondary);
		}
		
		return new Dimension(preSecondary, prePrimary);
	}


	/**
	 * Removes the specified component from our knowledge.
	 */
	public void removeLayoutComponent(Component c)
	{
		for(int i=0; i<3; i++)
		{
			if(components[i] == c)
			{
				components[i] = null;
				sizes[i] = 0;
				doReset = true;
			}
		}
	}


	/**
	 * Adds the specified component to the layout, using the specified
	 * constraint object.
	 * @param comp the component to be added
	 * @param constraints  where/how the component is added to the layout.
	 */
	public void addLayoutComponent(Component c, Object constraints)
	{
		if((constraints == null) || (constraints instanceof String))
		{
			addLayoutComponent((String)constraints, c);
		}
		else
		{
			throw new IllegalArgumentException("cannot add to layout: " + "constraint must be a " + "string (or null)");
		}
	}


	/**
	 * Returns the alignment along the x axis.  This specifies how
	 * the component would like to be aligned relative to other
	 * components.  The value should be a number between 0 and 1
	 * where 0 represents alignment along the origin, 1 is aligned
	 * the furthest away from the origin, 0.5 is centered, etc.
	 */
	public float getLayoutAlignmentX(Container target)
	{
		return 0.0f;
	}


	/**
	 * Returns the alignment along the y axis.  This specifies how
	 * the component would like to be aligned relative to other
	 * components.  The value should be a number between 0 and 1
	 * where 0 represents alignment along the origin, 1 is aligned
	 * the furthest away from the origin, 0.5 is centered, etc.
	 */
	public float getLayoutAlignmentY(Container target)
	{
		return 0.0f;
	}


	/**
	 * Does nothing. If the developer really wants to change the
	 * size of one of the views JSplitPane.resetToPreferredSizes should
	 * be messaged.
	 */
	public void invalidateLayout(Container c)
	{
	}


	/**
	 * Returns the maximum layout size, which is Integer.MAX_VALUE
	 * in both directions.
	 */
	public Dimension maximumLayoutSize(Container target)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}


	/**
	 * Marks the receiver so that the next time this instance is
	 * laid out it'll ask for the preferred sizes.
	 */
	public void resetToPreferredSizes()
	{
		doReset = true;
	}


	/**
	 * Resets the size of the Component at the passed in location.
	 */
	protected void resetSizeAt(int index)
	{
		sizes[index] = 0;
		doReset = true;
	}


	/**
	 * Sets the sizes to <code>newSizes</code>.
	 */
	protected void setSizes(int[] newSizes)
	{
		System.arraycopy(newSizes, 0, sizes, 0, 3);
	}


	/**
	 * Returns the sizes of the components.
	 */
	protected int[] getSizes()
	{
		int[] retSizes = new int[3];

		System.arraycopy(sizes, 0, retSizes, 0, 3);
		return retSizes;
	}


	/**
	 * Returns the width of the passed in Components preferred size.
	 */
	protected int getPreferredSizeOfComponent(Component c)
	{
		return getSizeForPrimaryAxis(c.getPreferredSize());
	}


	/**
	 * Returns the width of the passed in Components minimum size.
	 */
	protected int getMinimumSizeOfComponent(Component c)
	{
		return getSizeForPrimaryAxis(c.getMinimumSize());
	}


	/**
	 * Returns the width of the passed in component.
	 */
	protected int getSizeOfComponent(Component c)
	{
		return getSizeForPrimaryAxis(c.getSize());
	}


	/**
	 * Returns the available width based on the container size and
	 * Insets.
	 */
	protected int getAvailableSize(Dimension containerSize, Insets insets)
	{
		if(insets == null)
		{
			return getSizeForPrimaryAxis(containerSize);
		}
		return (getSizeForPrimaryAxis(containerSize) - (getSizeForPrimaryAxis(insets, true) + getSizeForPrimaryAxis(insets, false)));
	}


	/**
	 * Returns the left inset, unless the Insets are null in which case
	 * 0 is returned.
	 */
	protected int getInitialLocation(Insets insets)
	{
		if(insets != null)
		{
			return getSizeForPrimaryAxis(insets, true);
		}
		return 0;
	}


	/**
	 * Sets the width of the component c to be size, placing its
	 * x location at location, y to the insets.top and height
	 * to the containersize.height less the top and bottom insets.
	 */
	protected void setComponentToSize(Component c, int size, int location, Insets insets, Dimension containerSize)
	{
		if(insets != null)
		{
			if(horizontalAxis)
			{
				c.setBounds(location, insets.top, size, containerSize.height - (insets.top + insets.bottom));
			}
			else
			{
				c.setBounds(insets.left, location, containerSize.width - (insets.left + insets.right), size);
			}
		}
		else
		{
			if(horizontalAxis)
			{
				c.setBounds(location, 0, size, containerSize.height);
			}
			else
			{
				c.setBounds(0, location, containerSize.width, size);
			}
		}
	}


	/**
	 * If the axis == 0, the width is returned, otherwise the height.
	 */
	protected int getSizeForPrimaryAxis(Dimension size)
	{
		if(horizontalAxis)
		{
			return size.width;
		}
		return size.height;
	}


	/**
	 * If the axis == 0, the width is returned, otherwise the height.
	 */
	protected int getSizeForSecondaryAxis(Dimension size)
	{
		if(horizontalAxis)
		{
			return size.height;
		}
		return size.width;
	}


	/**
	 * Returns a particular value of the inset identified by the
	 * axis and <code>isTop</code><p>
	 *   axis isTop
	 *    0    true    - left
	 *    0    false   - right
	 *    1    true    - top
	 *    1    false   - bottom
	 */
	protected int getSizeForPrimaryAxis(Insets insets, boolean isTop)
	{
		if(horizontalAxis)
		{
			if(isTop)
			{
				return insets.left;
			}
			return insets.right;
		}
		
		if(isTop)
		{
			return insets.top;
		}
		
		return insets.bottom;
	}


	/**
	 * Returns a particular value of the inset identified by the
	 * axis and <code>isTop</code><p>
	 *   axis isTop
	 *    0    true    - left
	 *    0    false   - right
	 *    1    true    - top
	 *    1    false   - bottom
	 */
	protected int getSizeForSecondaryAxis(Insets insets, boolean isTop)
	{
		if(horizontalAxis)
		{
			if(isTop)
			{
				return insets.top;
			}
			return insets.bottom;
		}
		
		if(isTop)
		{
			return insets.left;
		}
		
		return insets.right;
	}


	/**
	 * Determines the components. This should be called whenever
	 * a new instance of this is installed into an existing
	 * SplitPane.
	 */
	protected void updateComponents()
	{
		JSplitPane splitPane = ui.getSplitPane();
		boolean ltr = isLTR();
		int ix0 = ltr ? 0 : 1;
		int ix1 = ltr ? 1 : 0;
		
		Component c = splitPane.getLeftComponent();
		if(components[ix0] != c)
		{
			components[ix0] = c;
			if(c == null)
			{
				sizes[ix0] = 0;
			}
			else
			{
				sizes[ix0] = -1;
			}
		}

		c = splitPane.getRightComponent();
		if(components[ix1] != c)
		{
			components[ix1] = c;
			if(c == null)
			{
				sizes[ix1] = 0;
			}
			else
			{
				sizes[ix1] = -1;
			}
		}

		/* Find the divider. */
		Component[] cs = splitPane.getComponents();
		Component oldDivider = components[2];

		components[2] = null;
		for(int i=cs.length-1; i>=0; i--)
		{
			if(cs[i] != components[0] && cs[i] != components[1])
			{
				if(oldDivider != cs[i])
				{
					components[2] = cs[i];
				}
				else
				{
					components[2] = oldDivider;
				}
				break;
			}
		}
		
		if(components[2] == null)
		{
			sizes[2] = 0;
		}
		else
		{
			sizes[2] = getSizeForPrimaryAxis(components[2].getPreferredSize());
		}
	}


	/**
	 * Resets the size of the first component to <code>leftSize</code>,
	 * and the right component to the remainder of the space.
	 */
	protected void setDividerLocation(int leftSize, int max)
	{
		boolean lValid = (components[0] != null && components[0].isVisible());
		boolean rValid = (components[1] != null && components[1].isVisible());
		boolean dValid = (components[2] != null && components[2].isVisible());

		if(dValid)
		{
			max -= sizes[2];
		}
		
//		if(!isLTR())
//		{
//			// flip
//			leftSize = max - leftSize;
//		}
		
		leftSize = Math.max(0, Math.min(leftSize, max));
		
		if(lValid)
		{
			if(rValid)
			{
				sizes[0] = leftSize;
				sizes[1] = max - leftSize;
			}
			else
			{
				sizes[0] = max;
				sizes[1] = 0;
			}
		}
		else if(rValid)
		{
			sizes[1] = max;
			sizes[0] = 0;
		}
	}


	/**
	 * Returns an array of the minimum sizes of the components.
	 */
	protected int[] getPreferredSizes()
	{
		int[] retValue = new int[3];

		for(int i=0; i<3; i++)
		{
			if(components[i] != null && components[i].isVisible())
			{
				retValue[i] = getPreferredSizeOfComponent(components[i]);
			}
			else
			{
				retValue[i] = -1;
			}
		}
		return retValue;
	}


	/**
	 * Returns an array of the minimum sizes of the components.
	 */
	protected int[] getMinimumSizes()
	{
		int[] retValue = new int[3];

		for(int i=0; i<2; i++)
		{
			if(components[i] != null && components[i].isVisible())
			{
				retValue[i] = getMinimumSizeOfComponent(components[i]);
			}
			else
			{
				retValue[i] = -1;
			}
		}
		
		retValue[2] = (components[2] != null) ? getMinimumSizeOfComponent(components[2]) : -1;
		return retValue;
	}


	/**
	 * Resets the components to their preferred sizes.
	 */
	protected void resetToPreferredSizes(int availableSize)
	{
		// Set the sizes to the preferred sizes (if fits), otherwise
		// set to min sizes and distribute any extra space.
		int[] sizes = getPreferredSizes();
		int totalSize = 0;

		for(int i=0; i<3; i++)
		{
			if(sizes[i] != -1)
			{
				totalSize += sizes[i];
			}
		}
		
		if(totalSize > availableSize)
		{
			sizes = getMinimumSizes();
			totalSize = 0;

			for(int i=0; i<3; i++)
			{
				if(sizes[i] != -1)
				{
					totalSize += sizes[i];
				}
			}
		}
		
		setSizes(sizes);
		distributeSpace(availableSize - totalSize, false);
	}


	/**
	 * Distributes <code>space</code> between the two components
	 * (divider won't get any extra space) based on the weighting. This
	 * attempts to honor the min size of the components.
	 *
	 * @param keepHidden if true and one of the components is 0x0
	 *                   it gets none of the extra space
	 */
	protected void distributeSpace(int space, boolean keepHidden)
	{
		boolean lValid = (components[0] != null && components[0].isVisible());
		boolean rValid = (components[1] != null && components[1].isVisible());

		if(keepHidden)
		{
			if(lValid && getSizeForPrimaryAxis(components[0].getSize()) == 0)
			{
				lValid = false;
				if(rValid && getSizeForPrimaryAxis(components[1].getSize()) == 0)
				{
					// Both aren't valid, force them both to be valid
					lValid = true;
				}
			}
			else if(rValid && getSizeForPrimaryAxis(components[1].getSize()) == 0)
			{
				rValid = false;
			}
		}

		if(lValid && rValid)
		{
			double weight = ui.getSplitPane().getResizeWeight();
			int lExtra = (int)(weight * space);
			int rExtra = (space - lExtra);

			sizes[0] += lExtra;
			sizes[1] += rExtra;

			boolean ltr = isLTR();
			int lMin = getMinimumSizeOfComponent(ltr ? components[0] : components[1]);
			int rMin = getMinimumSizeOfComponent(ltr ? components[1] : components[0]);
			boolean lMinValid = (sizes[0] >= lMin);
			boolean rMinValid = (sizes[1] >= rMin);

			if(!lMinValid && !rMinValid)
			{
				if(sizes[0] < 0)
				{
					sizes[1] += sizes[0];
					sizes[0] = 0;
				}
				else if(sizes[1] < 0)
				{
					sizes[0] += sizes[1];
					sizes[1] = 0;
				}
			}
			else if(!lMinValid)
			{
				if(sizes[1] - (lMin - sizes[0]) < rMin)
				{
					// both below min, just make sure > 0
					if(sizes[0] < 0)
					{
						sizes[1] += sizes[0];
						sizes[0] = 0;
					}
				}
				else
				{
					sizes[1] -= (lMin - sizes[0]);
					sizes[0] = lMin;
				}
			}
			else if(!rMinValid)
			{
				if(sizes[0] - (rMin - sizes[1]) < lMin)
				{
					// both below min, just make sure > 0
					if(sizes[1] < 0)
					{
						sizes[0] += sizes[1];
						sizes[1] = 0;
					}
				}
				else
				{
					sizes[0] -= (rMin - sizes[1]);
					sizes[1] = rMin;
				}
			}
			
			if(sizes[0] < 0)
			{
				sizes[0] = 0;
			}
			
			if(sizes[1] < 0)
			{
				sizes[1] = 0;
			}
		}
		else if(lValid)
		{
			sizes[0] = Math.max(0, sizes[0] + space);
		}
		else if(rValid)
		{
			sizes[1] = Math.max(0, sizes[1] + space);
		}
	}
}