// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.input.DataFormat;


/**
 * Clipboard Handler Base.
 */
public abstract class ClipboardHandlerBase
{
	public abstract Object copy(FxEditorModel model, EditorSelection sel) throws Exception;
	
	//
	
	private final DataFormat format;
	
	
	public ClipboardHandlerBase(DataFormat format)
	{
		this.format = format;
	}
	
	
	public DataFormat getFormat()
	{
		return format;
	}
}
