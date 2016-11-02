// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import javafx.scene.layout.Region;


/**
 * FxEditor Model.
 */
public abstract class FxEditorModel
{
	public interface Listener
	{
		public void eventLinesDeleted(int start, int count);
		
		public void eventLinesInserted(int start, int count);
		
		public void eventLinesModified(int start, int count);
	}
	
	//
	
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
	
	/** returns a load info structure that gives us information about the loading process and estimates of line count/file size, 
	 * or null if the data has been loaded. */ 
	public abstract LoadInfo getLoadInfo();
	
	/** returns a known line count.  if the model is still loading, returns the best estimate of the number of lines. */
	public abstract int getLineCount();
	
	/** returns plain text at the specified line, or null if unknown */
	public abstract String getSearchText(int line);
	
	/** 
	 * returns a non-null Region containing Text, TextFlow, or any other Nodes representing a line.
	 * I am not sure this should be a part of the editor model, because the presentation should be controlled by the editor ui.
	 * What this method needs to return is a list/array of segments encapsulating text, text style and colors.
	 * Another consideration is support for arbitrary Nodes such as images (tables and so on) - and for those we need to 
	 * have a ui component.
	 */
	public abstract Region getDecoratedLine(int line);
	
	//

	private CList<Listener> listeners = new CList<>();
	private static FxEditorModel empty;
	
	
	public FxEditorModel()
	{
	}
	
	
	public void addListener(Listener li)
	{
		listeners.add(li);
	}
	
	
	public void removeListener(Listener li)
	{
		listeners.remove(li);
	}
	
	
	public static FxEditorModel getEmptyModel()
	{
		if(empty == null)
		{
			empty = new FxEditorModel()
			{
				public LoadInfo getLoadInfo()
				{
					long t = System.currentTimeMillis();
					return new LoadInfo(1.0, 0, t, t); 
				}
				public int getLineCount() { return 0; }
				public String getSearchText(int line) { return null; }
				public Region getDecoratedLine(int line) { return null; }
			};
		}
		return empty;
	}
}
