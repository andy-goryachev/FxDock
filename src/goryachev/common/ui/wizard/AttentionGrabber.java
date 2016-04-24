// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.text.JTextComponent;


public class AttentionGrabber 
	implements ActionListener
{
	public static final int PERIOD = 100;
	private JComponent target;
	private Timer timer;
	private int maxCount = 10;
	private int count;
	private Color oldBG;
	
	
	public AttentionGrabber(JComponent c)
	{
		this.target = c;
		timer = new Timer(PERIOD, this);
		
		if(c instanceof JTextComponent)
		{
			oldBG = c.getBackground();
			// TODO if opaque?
		}
	}


	public void start()
	{
		timer.start();
	}


	public void actionPerformed(ActionEvent ev)
	{
		++count;
		if(count >= maxCount)
		{
			timer.stop();
			target.setBackground(oldBG);
		}
		else
		{
			float mix = count / (float)maxCount;
			Color bg = UI.mix(Color.red, mix, oldBG);
			target.setBackground(bg);
		}
	}
}
