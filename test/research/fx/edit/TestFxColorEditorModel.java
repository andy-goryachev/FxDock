// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * test plain text model with 2 billion rows
 */
public class TestFxColorEditorModel
	extends FxPlainEditorModel
{
	private NumberFormat format = NumberFormat.getInstance();
	
	
	public TestFxColorEditorModel()
	{
	}
	
	
	public Region getDecoratedLine(int line)
	{
		CList<Segment> ss = getSegments(line, true);
		
		TextFlow f = new TextFlow();
		for(Segment s: ss)
		{
			Text t = new Text(s.text);
			t.setFill(s.color);
			
			f.getChildren().add(t);
		}
		return f;
	}
	
	
	public String getSearchText(int line)
	{
		CList<Segment> ss = getSegments(line, false);
		SB sb = new SB();
		for(Segment s: ss)
		{
			sb.a(s.text);
		}
		return sb.toString();
	}


	public LoadInfo getLoadInfo()
	{
		return null; 
	}


	public int getLineCount()
	{
		return Integer.MAX_VALUE;
	}


	protected CList<Segment> getSegments(int line, boolean styles)
	{
		String s = String.valueOf(line);
		int sz = s.length();
		
		CList<Segment> ss = new CList<>();
		
		ss.add(new Segment(format.format(line) + ": ", Color.BLACK));
		
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			String w = toWord(c);
			
			ss.add(new Segment(w, styles ? toColor(c) : null));
		}
		return ss;
	}
	
	
	protected String toWord(char c)
	{
		switch(c)
		{
		case '0': return "zero ";
		case '1': return "one ";
		case '2': return "two ";
		case '3': return "three ";
		case '4': return "four ";
		case '5': return "five ";
		case '6': return "six ";
		case '7': return "seven ";
		case '8': return "eight ";
		case '9': return "nine ";
		default: return String.valueOf(c);
		}
	}
	
	
	protected Color toColor(char c)
	{
		switch(c)
		{
		case '0': return Color.BLACK;
		case '1': return Color.DARKRED;
		case '2': return Color.DARKORANGE;
		case '3': return Color.DARKGOLDENROD;
		case '4': return Color.DARKGREEN;
		case '5': return Color.DARKBLUE;
		case '6': return Color.DARKVIOLET;
		case '7': return Color.DARKMAGENTA;
		case '8': return Color.CYAN;
		case '9': return Color.DARKCYAN;
		default: return null;
		}
	}
	
	
	//
	
	
	public static class Segment
	{
		public final String text;
		public final Color color;
		
		
		public Segment(String text, Color color)
		{
			this.text = text;
			this.color = color;
		}
	}
}
