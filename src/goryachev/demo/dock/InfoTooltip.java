// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo.dock;
import goryachev.common.util.SB;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;


/**
 * InfoTooltip.
 */
public class InfoTooltip
	extends Tooltip
{
	public InfoTooltip(Region n)
	{
		n.hoverProperty().addListener((s) -> updateTooltip(n));
	}
	
	
	protected void updateTooltip(Region n)
	{
		SB sb = new SB();
		sb.a("width=").a(n.getWidth()).nl();
		sb.a("height=").a(n.getHeight()).nl();
		setText(sb.toString());
	}
}
