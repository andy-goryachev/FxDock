// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.Button;


/**
 * CButton.
 */
public class CButton
	extends Button
{
	public CButton(String text, CAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public CButton(String text)
	{
		super(text);
	}
}
