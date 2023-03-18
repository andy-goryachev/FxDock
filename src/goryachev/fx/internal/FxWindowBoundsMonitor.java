// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import javafx.stage.Stage;


/**
 * FxWindowMonitor keeps track of Stage normal bounds because FX does
 * some weird things like setting window (x,y) to -32,000.0 when iconifying
 * or losing the normal bounds completely when maximizing.
 * On top of that, it first changes window bounds then changes 
 * the maximized/iconified state... why?.
 */
public class FxWindowBoundsMonitor
{
	private final Stage win;
	private Duplet x = new Duplet();
	private Duplet y = new Duplet();
	private Duplet w = new Duplet();
	private Duplet h = new Duplet();
	
	
	public FxWindowBoundsMonitor(Stage w)
	{
		this.win = w;
		
		w.xProperty().addListener((p) -> updateX());
		w.yProperty().addListener((p) -> updateY());
		w.widthProperty().addListener((p) -> updateWidth());
		w.heightProperty().addListener((p) -> updateHeight());
		w.maximizedProperty().addListener((x) -> updateMaximized());
		w.iconifiedProperty().addListener((x) -> updateMinimized());
		w.fullScreenProperty().addListener((x) -> updateFullScreen());
	}
	
	
	public double getX()
	{
		return x.get();
	}
	
	
	public double getY()
	{
		return y.get();
	}
	
	
	public double getWidth()
	{
		return w.get();
	}
	
	
	public double getHeight()
	{
		return h.get();
	}
	
	
	protected void updateMaximized()
	{
		if(win.maximizedProperty().get())
		{
			x.discard();
			y.discard();
		}
	}
	
	
	protected void updateMinimized()
	{
		if(win.iconifiedProperty().get())
		{
			x.discard();
			y.discard();
		}
	}
	
	
	protected void updateFullScreen()
	{
		if(win.fullScreenProperty().get())
		{
			x.discard();
			y.discard();
			w.discard();
			h.discard();
		}
	}
	

	protected void updateX()
	{
		double v = win.xProperty().get();
		x.set(v);
	}
	
	
	protected void updateY()
	{
		double v = win.yProperty().get();
		y.set(v);
	}
	
	
	protected void updateWidth()
	{
		boolean use = !win.isMaximized();
		if(use)
		{
			double v = win.widthProperty().get();
			w.set(v);
		}
	}
	
	
	protected void updateHeight()
	{
		boolean use = !win.isMaximized();
		if(use)
		{
			double v = win.heightProperty().get();
			h.set(v);
		}
	}
	
	
	//
	
	
	class Duplet
	{
		private double value;
		private double prev;
		
		
		public void set(double v)
		{
			prev = value;
			value = v;
		}
		
		
		public double get()
		{
			return value;
		}
		
		
		public void discard()
		{
			value = prev;
		}
	}
}
