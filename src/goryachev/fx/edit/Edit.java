// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * An Edit.
 */
public class Edit
{
	private final EditorSelection selection;
	private final CharSequence replaceText;
	
	
	public Edit(EditorSelection sel, CharSequence replaceText)
	{
		this.selection = sel;
		this.replaceText = replaceText;
	}
	
	
	public EditorSelection getSelection()
	{
		return selection;
	}
	
	
	public CharSequence getReplaceText()
	{
		return replaceText;
	}
}
