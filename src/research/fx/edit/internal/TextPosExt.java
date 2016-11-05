// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit.internal;
import research.fx.edit.LineBox;
import research.fx.edit.TextPos;


/**
 * TextPos with cached LineBox reference.
 */
public class TextPosExt
	extends TextPos
{
	public final LineBox line;
	
	
	public TextPosExt(LineBox b, TextPos p)
	{
		super(p.getIndex(), p.isLeading());
		
		line = b;
	}
}
