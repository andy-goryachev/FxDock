// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.CButton;
import goryachev.fx.CComboBox;
import goryachev.fx.CPane;
import goryachev.fx.FX;


/**
 * Find Pane.
 */
public class FindPane
	extends CPane
{
	public final CComboBox searchField;
	public final CButton ignoreCaseButton;
	public final CButton wholeWordButton;
	public final CButton prevButton;
	public final CButton nextButton;
	
	
	public FindPane()
	{
		setPadding(2);
		
		searchField = new CComboBox();
		searchField.setEditable(true);
		
		ignoreCaseButton = new CButton("Aa");
		
		wholeWordButton = new CButton("[]");
		
		prevButton = new CButton("<");
		
		nextButton = new CButton(">");
		
		setHGap(5);
		addColumns
		(
			CPane.PREF,
			CPane.FILL,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF
		);
		
		add(0, 0, FX.label("Find:"));
		add(1, 0, searchField);
		add(2, 0, ignoreCaseButton);
		add(3, 0, wholeWordButton);
		add(4, 0, prevButton);
		add(5, 0, nextButton);
	}
	
	
	public void focusSearch()
	{
		searchField.getEditor().requestFocus();
	}
}
