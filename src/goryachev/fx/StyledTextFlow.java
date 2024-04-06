// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * Styled Text Flow.
 */
public class StyledTextFlow
	extends TextFlow
{
	public StyledTextFlow()
	{
	}
	
	
	public void append(CharSequence text)
	{
		Text t = new Text(text.toString());
		getChildren().add(t);
	}
	
	
	public void append(CssStyle style, CharSequence text)
	{
		Text t = new Text(text.toString());
		FX.style(t, style);
		getChildren().add(t);
	}
}
