// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.Timer;


public class CAnimator
	implements ActionListener
{
	public interface Client
	{
		public void nextFrame();
	}
	
	//
	
	private WeakReference<Client> ref;
	private Timer timer;
	
	
	public CAnimator(Client c, int period)
	{
		ref = new WeakReference<Client>(c);

		timer = new Timer(period, this);
		timer.start();
	}
	

	public void actionPerformed(ActionEvent e)
	{
		Client b = ref.get();
		if(b == null)
		{
			// can be garbage collected
			timer.stop();
			timer = null;
		}
		else
		{
			b.nextFrame();
		}
	}
	
	
	public void stop()
	{
		timer.stop();
	}
}