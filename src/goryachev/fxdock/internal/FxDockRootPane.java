// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockStyles;
import goryachev.fxdock.FxDockWindow;
import javafx.scene.Node;


/**
 * FxDockRootPane.
 */
public class FxDockRootPane
	extends FxDockBorderPane
{
	private final FxDockWindow window;
	
	
	public FxDockRootPane(FxDockWindow w)
	{
		this.window = w;
		
		FX.style(this, FxDockStyles.FX_ROOT_PANE);
		setContent(new FxDockEmptyPane());
	}
	
	
	public final void setContent(Node n)
	{
		if(n == null)
		{
			n = new FxDockEmptyPane();
		}
		setCenter(n);
		DockTools.setParent(this, n);
	}
	
	
	public final Node getContent()
	{
		return getCenter();
	}
}
