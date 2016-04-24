// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JLabel;


public class ProgressLabel
	extends JLabel
{
	private boolean paintGrid = true;
	private boolean paintBottomLine = true;
	private float value;
	private Color fillColor;
	private Color barColor = UI.setAlpha(Color.magenta, 127); // new Color(127, 255, 127, 127);
	private Color gridColor = new Color(0, 0, 0, 64);


	public ProgressLabel()
	{
		super(" ");
	}
	
	
	public void setPaintGrid(boolean on)
	{
		paintGrid = on;
		repaint();
	}
	
	
	public void setProgress(double value)
	{
		this.value = (float)value;
		repaint();
	}

	
	public void setProgress(float value)
	{
		this.value = value;
		repaint();
	}
	
	
	public void setBarColor(Color c)
	{
		barColor = c;
		repaint();
	}
	
	
	public void setFillColor(Color c)
	{
		fillColor = c;
		repaint();
	}


	public void paintComponent(Graphics g)
	{
		Insets m = getInsets();
		int w = getWidth() - m.left - m.right;
		int h = getHeight() - m.top - m.bottom;
		int b = m.top + h - (paintBottomLine ? 2 : 1);
		
		// fill
		
		if(fillColor != null)
		{
			g.setColor(fillColor);
			g.fillRect(m.left, m.top, w, h);
		}
		
		// bar
		
		int px = Math.round(w * value);
		g.setColor(barColor);
		g.fillRect(m.left, m.top, px, h);
		
		// grid
		
		if(paintGrid)
		{
			g.setColor(gridColor);

			if(paintBottomLine)
			{
				g.drawLine(m.left, b + 1, m.left + w, b + 1);
			}

			if(w > 4)
			{
				int h2 = h; //Math.round(h * 0.75f);
				int h1 = Math.round(h * 0.5f);
				int h0 = Math.round(h * 0.25f);
				
				if(w < 50)
				{
					// half
					for(int i=0; i<=2; i++)
					{
						int x = m.left + (w - 1) * i / 2;
						
						if((i % 2) == 0)
						{
							g.drawLine(x, b - h2, x, b);
						}
						else
						{
							g.drawLine(x, b - h1, x, b);
						}
					}
				}
				else if(w < 500)
				{
					// 10th
					for(int i=0; i<=10; i++)
					{
						int x = m.left + (w - 1) * i / 10;
						
						if((i % 10) == 0)
						{
							g.drawLine(x, b - h2, x, b);
						}
						else if((i % 5) == 0)
						{
							g.drawLine(x, b - h1, x, b);
						}
						else
						{
							g.drawLine(x, b - h0, x, b);
						}
					}
				}
				else
				{
					// 100th
					for(int i=0; i<=100; i++)
					{
						int x = m.left + (w - 1) * i / 100;
						
						if((i % 50) == 0)
						{
							g.drawLine(x, b - h2, x, b);
						}
						else if((i % 10) == 0)
						{
							g.drawLine(x, b - h1, x, b);
						}
						else
						{
							g.drawLine(x, b - h0, x, b);
						}
					}
				}
			}
		}

		// text
		
		super.paintComponent(g);
	}
}
