// Copyright © 2019-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.css;
import goryachev.fx.CssPseudo;


/**
 * Style Sheet Constants and Methods.
 */
public interface StyleSheetConstants
{
	public static final CssPseudo ARMED = new CssPseudo(":armed");
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo EDITABLE = new CssPseudo(":editable");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	public static final CssPseudo SELECTED = new CssPseudo(":selected");
}
