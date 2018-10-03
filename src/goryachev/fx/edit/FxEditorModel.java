// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.Log;
import goryachev.fx.FxBoolean;
import java.io.Writer;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;


/**
 * FxEditor Model Base Class.
 */
public abstract class FxEditorModel
{
	public static class LoadInfo
	{
		public final double progress;
		public final int lineCount;
		public final long startTime;
		public final long currentTime;
		
		
		public LoadInfo(double progress, int lineCount, long startTime, long currentTime)
		{
			this.progress = progress;
			this.lineCount = lineCount;
			this.startTime = startTime;
			this.currentTime = currentTime;
		}
	}
	
	//
	
	/** 
	 * returns information about the loading process status and an estimate of line count/file size. 
	 * returns null if the data has been already loaded. */ 
	public abstract LoadInfo getLoadInfo();
	
	/** returns a known line count.  if the model is still loading, returns the best estimate of the number of lines. */
	public abstract int getLineCount();
	
	/** 
	 * returns a non-null LineBox containing components that represent a logical line.
	 */
	public abstract LineBox getLineBox(int line);
	
	/**
	 * Applies modification to the model.  The model makes necessary changes to its internal state, 
	 * calls FxEditor's event* callbacks, and returns a corresponding undo Edit object.
	 * Throws an exception if this model is read-only.
	 */
	public abstract Edit edit(Edit ed) throws Exception;
	
	//

	protected FxBoolean editableProperty = new FxBoolean(false);
	protected CList<FxEditor> listeners = new CList<>();
	protected final CMap<DataFormat,ClipboardHandlerBase> clipboardHandlers = new CMap(); 
	private static FxEditorModel empty;
	
	
	public FxEditorModel()
	{
		addClipboardHandler(new PlainTextClipboardHandler());
	}


	public void addListener(FxEditor li)
	{
		listeners.add(li);
	}
	
	
	public void removeListener(FxEditor li)
	{
		listeners.remove(li);
	}
	
	
	public boolean isEditable()
	{
		return editableProperty.get();
	}
	
	
	public void setEditable(boolean on)
	{
		editableProperty.set(on);
	}
	
	
	public BooleanProperty editableProperty()
	{
		return editableProperty;
	}
	
	
	public static FxEditorModel getEmptyModel()
	{
		if(empty == null)
		{
			empty = new FxEditorModel()
			{
				private final LineBox box = new LineBox();
				
				public LoadInfo getLoadInfo()
				{
					long t = System.currentTimeMillis();
					return new LoadInfo(1.0, 0, t, t); 
				}
				public int getLineCount() { return 0; }
				public LineBox getLineBox(int line) { return box; }
				public Edit edit(Edit ed) throws Exception { throw new Exception(); }
			};
		}
		return empty;
	}
	
	
	public void fireAllChanged()
	{
		fireEvent((li) -> li.eventAllChanged());
	}
	
	
	protected void fireEvent(Consumer<FxEditor> f)
	{
		for(FxEditor li: listeners)
		{
			f.accept(li);
		}
	}
	
	
	public void addClipboardHandler(ClipboardHandlerBase h)
	{
		clipboardHandlers.put(h.getFormat(), h);
	}
	
	
	public void removeClipboardHandler(DataFormat f)
	{
		clipboardHandlers.remove(f);
	}
	
	
	/** returns all supported copy/paste formats */
	public DataFormat[] getSupportedFormats()
	{
		return clipboardHandlers.keySet().toArray(new DataFormat[clipboardHandlers.size()]);
	}
	
	
	/** returns true if the format is supported */
	public boolean isFormatSupported(DataFormat f)
	{
		return (clipboardHandlers.get(f) != null);
	}
	
	
	/** copies specified data formats to the clipboard.  silently ignores unsupported data format.  DataFormat.PLAIN_TEXT is always supported. */
	public void copy(EditorSelection sel, Consumer<Throwable> errorHandler, DataFormat ... formats)
	{
		try
		{
			CMap<DataFormat,Object> m = new CMap();
			
			for(DataFormat f: formats)
			{
				if(isFormatSupported(f))
				{
					ClipboardHandlerBase handler = clipboardHandlers.get(f);
					if(handler != null)
					{
						Object v = handler.copy(this, sel);
						if(v != null)
						{
							m.put(f, v);
						}						
					}
				}
			}
			
			Clipboard c = Clipboard.getSystemClipboard();
			c.setContent(m);
		}
		catch(Throwable e)
		{
			if(errorHandler == null)
			{
				Log.ex(e);
			}
			else
			{
				errorHandler.accept(e);
			}
		}
	}
	

	/** plain text copy, expecting ordered selection ranges */
	public void getPlainText(EditorSelection sel, Writer wr) throws Exception
	{
		for(SelectionSegment s: sel.getSegments())
		{
			CKit.checkCancelled();
			writePlainText(s, wr);
		}
	}


	protected void writePlainText(SelectionSegment seg, Writer wr) throws Exception
	{
		Marker m0 = seg.getMin();
		Marker m1 = seg.getMax();
		
		int first = m0.getLine();
		int last = m1.getLine();
		
		for(int i=first; i<=last; i++)
		{
			CKit.checkCancelled();
			String s = getPlainText(i);
			
			if(i == first)
			{
				if(i == last)
				{
					s = s.substring(m0.getLineOffset(), m1.getLineOffset());
				}
				else
				{
					s = s.substring(m0.getLineOffset());
				}
			}
			else
			{
				wr.write("\n");
				
				if(i == last)
				{
					s = s.substring(0, m1.getLineOffset());
				}
			}
			
			wr.write(s);
		}
	}

	
	public int getTextLength(int line)
	{
		String s = getPlainText(line);
		return s == null ? 0 : s.length();
	}
	
	
	/** returns plain text at the specified line, or null if unknown */
	public String getPlainText(int line)
	{
		LineBox b = getLineBox(line);
		return b.getText();
	}
}
