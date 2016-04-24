// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class DefaultSplashScreen
	extends JFrame
{
	public final JLabel splashField;


	public DefaultSplashScreen(Image im)
	{
		splashField = new JLabel(new ImageIcon(im));
		
		getRootPane().setLayout(new BorderLayout());
		getRootPane().add(splashField);
		
		setResizable(false);
		setUndecorated(true);
		pack();
		Rectangle r = getGraphicsConfiguration().getBounds();
		setLocation((r.width - getWidth()) / 2, (r.height - getHeight()) / 2);
	}
}
