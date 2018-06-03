// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.FxButton;
import goryachev.fx.FxComboBox;
import goryachev.fx.CPane;
import goryachev.fx.FX;


/**
 * Find Pane.
 */
public class FindPane
	extends CPane
{
	public final FxComboBox searchField;
	public final FxButton ignoreCaseButton;
	public final FxButton wholeWordButton;
	public final FxButton prevButton;
	public final FxButton nextButton;
	
	
	public FindPane()
	{
		setPadding(2);
		
		searchField = new FxComboBox();
		searchField.setEditable(true);
		
		ignoreCaseButton = new FxButton("Aa");
		
		wholeWordButton = new FxButton("[]");
		
		prevButton = new FxButton("<");
		
		nextButton = new FxButton(">");
		
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
