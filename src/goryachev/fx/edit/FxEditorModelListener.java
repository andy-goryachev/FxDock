// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * FxEditor Model Listener.
 * 
 * The idea is that after each event, the model indexes change.
 * The clients should query the model for new information, using new text row indexes.
 */
public interface FxEditorModelListener
{
	/**
	 * The text between two positions has changed: either deleted, replaced, or inserted.
	 * <pre>
	 * Before:
	 *   startLine ->  TTTTTTT|DDDDD                      | startPos=7
	 *                 DDDDDDDDDD                         |
	 *     endLine ->  DDDD|TTTTTTTTTTTT                  | endPos=4
	 * 
	 * After:
	 *   startLine ->  TTTTTTT|II                         | startCharsInserted=2
	 *                 IIII                               | linesInserted=1
	 *     endLine ->  I|TTTTTTTTTTTT                     | endCharsInserted=1
	 * </pre>
	 * 
	 * @param startLine - first marker line
	 * @param startPos - first marker position (0 ... length)
	 * @param startCharsInserted - number of characters inserted after startPos on the startLine
	 * @param linesInserted - number of lines inserted between (and not counting) startLine and endLine
	 * @param endLine - second marker line
	 * @param endPos - second marker position
	 * @param endCharsInserted - number of characters inserted before endPos on the endLine
	 */
	public void eventTextUpdated(int startLine, int startPos, int startCharsInserted, int linesInserted, int endPos, int endCharIndex, int endCharsInserted);

	/** 
	 * All lines in the editor have changed.  
	 * The clients should re-query the model and rebuild everything 
	 */
	public void eventAllLinesChanged();
}
