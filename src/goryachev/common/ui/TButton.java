// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;


/** or use CToolBarButton */
public class TButton
	extends JButton
{
	// avoid re-implementing all this logic by using a single renderer
	private static final TButtonRenderer RENDERER = new TButtonRenderer();

	// transparent colors are compatible with any color scheme
	public static final Color DARK = new Color(0,0,0,192);
	public static final Color LIGHT = new Color(255,255,255,192);
	
	// FIX theme!
	
	// colored glow
	public static Color armedColor = Color.yellow; //new Color(245,204,44);
	// enabled control
	public static Color enabledColor = Color.black; //new Color(222,222,222);
	// disabled control
	public static Color disabledColor = new Color(105,105,105);

	
	//
	
	
	protected boolean pressed;
	protected boolean mouseover;
	

	// FIX text and tooltip separate!
	public TButton(Icon icon, String tooltip, boolean hideText, Action a)
	{
		this(tooltip, hideText, a);
		setIcon(icon);
	}
	
	
	public TButton(String tooltip, boolean hideText, Action a)
	{
		this(a, hideText);
		setToolTipText(tooltip);
	}
	
	
	public TButton(Action a, boolean hideText)
	{
		setFocusPainted(false);
		setBorder(new TButtonBorder());
		setOpaque(false);
		
		// must set property before setting action... who would know?
		// unbelievable - it actually compares to FALSE instead of getting value!
		if(hideText)
		{
			putClientProperty("hideActionText",Boolean.TRUE);
		}
		
		setAction(a);
		
		addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent ev)
			{
				if(isEnabled())
				{
					setMouseOver(true);
				}
			}

			public void mouseExited(MouseEvent ev)
			{
				setMouseOver(false);
			}

			public void mousePressed(MouseEvent ev)
			{
				if(isEnabled())
				{
					setPressed(true);
				}
			}

			public void mouseReleased(MouseEvent ev)
			{
				setPressed(false);
			}
		});
		
		if(a instanceof CAction)
		{
			setToolTipText(((CAction)a).getToolTipText());
		}
	}
	
	
	public TButton(Action a)
	{
		this(a,false);
	}
	
	
	protected void setPressed(boolean on)
	{
		if(on != pressed)
		{
			pressed = on;
			repaint();
		}
	}

	
	protected void setMouseOver(boolean on)
	{
		if(mouseover != on)
		{
			mouseover = on;
			repaint();
		}
	}
	
	
	public void paintComponent(Graphics g)
	{
		RENDERER.set(this);
		RENDERER.paint(g);
	}
	
	
	public void setMargins(int horizontal, int vertical)
	{
		((TButtonBorder)getBorder()).setMargins(horizontal,vertical);
		revalidate();
		repaint();
	}
	
	
//	public void setIcon(Icon ic)
//	{
//		disabledIcon = null;
//		super.setIcon(ic);
//	}
	
	
	public void setSelected(boolean on)
	{
		super.setSelected(on);
	}
	
	
	public void setMargin(int m)
	{
		setMargin(m, m, m, m);
	}


	public void setMargin(int vert, int hor)
	{
		setMargin(vert, hor, vert, hor);
	}


	public void setMargin(int top, int left, int bottom, int right)
	{
		setMargin(new Insets(top, left, bottom, right));
	}
	
	
	//
	
	
	public static class TButtonRenderer extends JLabel
	{
		public static final Color SELECTED_BG = new Color(255,255,0,90);
		public static final Color SELECTED_AND_PRESSED_BG = new Color(0,0,0,100);

		private Color fillColor;
		
		
		public TButtonRenderer()
		{
			super(null,null,CENTER);
			setVerticalAlignment(CENTER);
			setOpaque(false);
		}
		
		
		public void set(TButton b)
		{
			setBounds(b.getX(),b.getY(),b.getWidth(),b.getHeight());
			setBorder(b.getBorder());

			if(b.isSelected())
			{
				if(b.pressed)
				{
					fillColor = SELECTED_AND_PRESSED_BG;
				}
				else
				{
					fillColor = SELECTED_BG;
				}
			}
			else
			{
				if(b.pressed)
				{
					fillColor = SELECTED_BG;
				}
				else
				{
					fillColor = null;
				}
			}

			if(b.isEnabled())
			{
				if(b.isSelected())
				{
					setForeground(armedColor);
				}
				else
				{
					setForeground(enabledColor);
				}
			}
			else
			{
				setForeground(disabledColor);
			}
			
			if(b.isEnabled())
			{
				if(b.isSelected())
				{
					setIcon(b.getIcon()); //getSelectedIcon());
				}
				else
				{
					setIcon(b.getIcon());
				}
			}
			else
			{
				setIcon(b.getDisabledIcon());
			}
			
			if(Boolean.TRUE.equals(b.getClientProperty("hideActionText")))
			{
				setText(null);
			}
			else
			{
				setText(b.getText());
			}
		}
		
		
		public void paintComponent(Graphics g)
		{
			if(fillColor != null)
			{
				g.setColor(fillColor);
				g.fillRect(0,0,getWidth(),getHeight());
			}
			super.paintComponent(g);
		}
	}

	
	//
	
	
	// can't be a static class because it's renderer who is painting, not the original button
	public class TButtonBorder implements Border
	{
		private Insets hoverInsets;
		private Insets normalInsets;


		public TButtonBorder()
		{
			setMargins(2,1);
		}
		
		
		public Insets getBorderInsets(Component c)
		{
			return ((isSelected() || pressed || !mouseover) ? normalInsets : hoverInsets);
		}

		
		public void setMargins(int horizontal, int vertical)
		{
			hoverInsets = new Insets(2+vertical,2+horizontal,3+vertical,3+horizontal);
			normalInsets = new Insets(3+vertical,3+horizontal,2+vertical,2+horizontal);
		}


		public boolean isBorderOpaque()
		{
			return false;
		}

		
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
		{
			int w = c.getWidth() - 1;
			int h = c.getHeight() - 1;
			
			if((isEnabled() && mouseover) || (pressed) || isSelected())
			{
				if(pressed || isSelected())
				{
					g.setColor(LIGHT);
					g.drawLine(w,0,w,h);
					g.drawLine(w,h,0,h);
					g.setColor(DARK);
					g.drawLine(0,h-1,0,0);
					g.drawLine(0,0,w-1,0);
				}
				else
				{
					g.setColor(DARK);
					g.drawLine(w,0,w,h);
					g.drawLine(w,h,0,h);
					g.setColor(LIGHT);
					g.drawLine(0,h-1,0,0);
					g.drawLine(0,0,w-1,0);
				}
			}
		}
	}
}
