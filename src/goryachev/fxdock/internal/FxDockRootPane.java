// Copyright © 2016-2026 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
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
		
		FxDockStyles.FX_ROOT_PANE.set(this);
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
