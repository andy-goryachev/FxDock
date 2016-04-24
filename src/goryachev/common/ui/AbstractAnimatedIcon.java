// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.SwingUtilities;


public abstract class AbstractAnimatedIcon
	implements CAnimator.Client, Icon
{
	public abstract Image generateFrame(Component c, int frame);
	
	//
	
	private ArrayList<Repaint> repaints = new ArrayList();
	private int frame;
	private int frameCount;
	private int period;
	private boolean startAnimator = true;
	private CAnimator animator;
	
	
	public AbstractAnimatedIcon(int frameCount, int period)
	{
		this.frameCount = frameCount;
		this.period = period;
	}
	
	
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Image image = generateFrame(c, frame);
		g.drawImage(image, x, y, null);

		if(startAnimator)
		{
			animator = new CAnimator(this, period);
			startAnimator = false;
		}

		registerRepaint(c,x,y);
	}
	
	
	public void stopAnimation()
	{
		if(animator != null)
		{
			animator.stop();
		}
	}
	
	
	public void startAnimation()
	{
		if(animator == null)
		{
			// may not work!
			startAnimator = true;
		}
	}
	

	// remember the component being repainted and location of the paint area (the size of this icon)
	// in case of cell renderers, the component in question is actually the parent of a CellRendererPane
	// when the animation timer is fired, all the memorized repaints are fired again, and either of two
	// possible thing happen:
	// 1. the animated icon is still at the old location and the paintComponent() will be issued, repeating the process, or
	// 2. the animated icon is not there and no repaint would come to this icon
	// no special care is taken to eliminate duplicate repaints.
	protected void registerRepaint(Component c, int x, int y)
	{
		Component actual = findActualComponent(c);
		if(actual != null)
		{
			Point p = SwingUtilities.convertPoint(c, x, y, actual);
			c = actual;
			x = p.x;
			y = p.y;
		}
		repaints.add(new Repaint(c, x, y, getIconWidth(), getIconHeight()));
	}


	private Component findActualComponent(Component c)
	{
		Component actual = SwingUtilities.getAncestorOfClass(CellRendererPane.class, c);
		if((actual != null) && (actual.getParent() != null))
		{
			c = findActualComponent(actual.getParent());
		}
		return c;
	}
	
	
	public void nextFrame()
	{
		if(++frame >= frameCount)
		{
			frame = 0;
		}
		
		if(repaints.size() > 0)
		{
			for(Repaint r: repaints)
			{
				r.repaint();
			}
			
			repaints.clear();
		}
	}
	
	
	public int currentFrame()
	{
		return frame;
	}
	
	
	public int getFrameCount()
	{
		return frameCount;
	}
	
	
	public int getPeriod()
	{
		return period;
	}
	
	
	//


	public static class Repaint
	{
		public Component component;
		public int x;
		public int y;
		public int width;
		public int height;


		public Repaint(Component c, int x, int y, int w, int h)
		{
			this.component = c;
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
		}


		public void repaint()
		{
			component.repaint(x, y, width, height);
		}
	}
}
