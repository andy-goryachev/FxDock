// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CMap;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.Utilities;


/**
 *  This class will display line numbers for a related text component. The text
 *  component must use the same line height for each line. TextLineNumber
 *  supports wrapped lines and will highlight the line number of the current
 *  line in the text component.
 *
 *  This class was designed to be used as a component added to the row header
 *  of a JScrollPane.
 *  
 *  http://tips4java.wordpress.com/2009/05/23/text-component-line-number/
 *  Rob Camick said April 11, 2011 at 10:01 am: 
 *  You are free to use the code however you wish.
 *  
 *  FIX vertical line seems to get messed up when scrolling
 *  see also LineNumberComponent
 */
public class TextLineNumberComponent
	extends JPanel
	implements CaretListener, DocumentListener, PropertyChangeListener
{
	public final static float LEFT = 0.0f;
	public final static float CENTER = 0.5f;
	public final static float RIGHT = 1.0f;
	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

	//  Text component this TextTextLineNumber component is in sync with
	protected final JTextComponent component;
	private Document document;

	//  Properties that can be changed
	private boolean updateFont;
	private int borderGap;
	private float digitAlignment;
	private int minimumDisplayDigits;

	//  Keep history information to reduce the number of times the component needs to be repainted
	private int lastDigits;
	protected int lastHeight;
	private int lastLine;
	private CMap<String,FontMetrics> fonts;
	private CMap<Integer,Color> highlights;


	/**
	 *	Create a line number component for a text component. This minimum
	 *  display width will be based on 3 digits.
	 *  @param component  the related text component
	 */
	public TextLineNumberComponent(JTextComponent component)
	{
		this(component, 3);
	}


	/**
	 *	Create a line number component for a text component.
	 *  @param component  the related text component
	 *  @param minimumDisplayDigits  the number of digits used to calculate
	 *                               the minimum width of the component
	 */
	public TextLineNumberComponent(JTextComponent c, int minimumDisplayDigits)
	{
		this.component = c;

		setFont(c.getFont());

		setBorderGap(5);
		setDigitAlignment(RIGHT);
		setMinimumDisplayDigits(minimumDisplayDigits);

		document = c.getDocument();
		document.addDocumentListener(this);
		c.addCaretListener(this);
		c.addPropertyChangeListener("font", this);
		c.addPropertyChangeListener("document", this);
		
		setBackground(Theme.FIELD_BG);
	}


	/**
	 *  Gets the update font property
	 *  @return the update font property
	 */
	public boolean getUpdateFont()
	{
		return updateFont;
	}


	/**
	 *  Set the update font property. Indicates whether this Font should be
	 *  updated automatically when the Font of the related text component
	 *  is changed.
	 *  @param updateFont  when true update the Font and repaint the line
	 *                     numbers, otherwise just repaint the line numbers.
	 */
	public void setUpdateFont(boolean updateFont)
	{
		this.updateFont = updateFont;
	}


	/**
	 *  Gets the border gap
	 *  @return the border gap in pixels
	 */
	public int getBorderGap()
	{
		return borderGap;
	}


	/**
	 *  The border gap is used in calculating the left and right insets of the
	 *  border. Default value is 5.
	 *  @param borderGap  the gap in pixels
	 */
	public void setBorderGap(int borderGap)
	{
		this.borderGap = borderGap;
		
		setBorder(new CBorder(0, borderGap, 0, borderGap));
		
		lastDigits = 0;
		setPreferredWidth();
	}

	
	/**
	 *  Gets the digit alignment
	 *  @return the alignment of the painted digits
	 */
	public float getDigitAlignment()
	{
		return digitAlignment;
	}


	/**
	 *  Specify the horizontal alignment of the digits within the component.
	 *  Common values would be:
	 *  <ul>
	 *  <li>TextLineNumber.LEFT
	 *  <li>TextLineNumber.CENTER
	 *  <li>TextLineNumber.RIGHT (default)
	 *	</ul>
	 *  @param currentLineForeground  the Color used to render the current line
	 */
	public void setDigitAlignment(float digitAlignment)
	{
		this.digitAlignment = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
	}


	/**
	 *  Gets the minimum display digits
	 *  @return the minimum display digits
	 */
	public int getMinimumDisplayDigits()
	{
		return minimumDisplayDigits;
	}


	/**
	 *  Specify the mimimum number of digits used to calculate the preferred
	 *  width of the component. Default is 3.
	 *  @param minimumDisplayDigits  the number digits used in the preferred
	 *                               width calculation
	 */
	public void setMinimumDisplayDigits(int minimumDisplayDigits)
	{
		this.minimumDisplayDigits = minimumDisplayDigits;
		setPreferredWidth();
	}


	/**
	 *  Calculate the width needed to display the maximum line number
	 */
	protected void setPreferredWidth()
	{
		Element root = component.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

		//  Update sizes when number of digits in the line number changes
		if(lastDigits != digits)
		{
			lastDigits = digits;
			FontMetrics fm = getFontMetrics(getFont());
			int width = fm.charWidth('0') * digits;
			Insets insets = getInsets();
			int preferredWidth = insets.left + insets.right + width;

			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize(d);
			setSize(d);
		}
	}


	/** Draw the line numbers */
	public void paintComponent(Graphics g)
	{
//		g.setColor(getBackground());
//		g.fillRect(0, 0, getWidth(), getHeight());
//		
//		g.setColor(Color.red); // FIX
//		g.drawLine(1, 0, 1, getHeight());
		
		super.paintComponent(g);

		//	Determine the width of the space available to draw the line number
		FontMetrics fm = component.getFontMetrics(component.getFont());
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		//  Determine the rows to draw within the clipped bounds.
		Rectangle clip = g.getClipBounds();
		int rowStartOffset = component.viewToModel(new Point(0, clip.y));
		int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

		while(rowStartOffset <= endOffset)
		{
			try
			{
				//  Get the line number as a string and then determine the
				//  "X" and "Y" offsets for drawing the string.
				Integer index = getTextLineIndex(rowStartOffset);
				String lineNumber = (index == null ? null : Theme.formatNumber(index + 1));
				int y = getOffsetY(rowStartOffset, fm);

				Color bg = getLineBackground(rowStartOffset);
				if(bg != null)
				{
					g.setColor(bg);
					// HACK I don't understand why the very first column of pixels gets messed up
					// a "solution" would be not to paint those pixels
					int hack = 1;
					g.fillRect(hack, y - fm.getMaxAscent(), getWidth()-hack, fm.getMaxAscent() + fm.getMaxDescent());
				}

				if(lineNumber != null)
				{
					g.setColor(getForeground());
					int stringWidth = fm.stringWidth(lineNumber);
					int x = getOffsetX(availableWidth, stringWidth) + insets.left;
					g.drawString(lineNumber, x, y);
				}

				//  move to the next row
				rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
			}
			catch(Exception e)
			{
				Log.err(e);
				break;
			}
		}
	}

	
	protected Color getLineBackground(int rowStartOffset)
	{
		Color color = null;
		if(highlights != null)
		{
			int index = getLineNumber(rowStartOffset);
			color = highlights.get(index);
		}
		
		if(isCurrentLine(rowStartOffset))
		{
			Color c = Color.lightGray;
			if(color == null)
			{
				color = c;
			}
			else
			{
				color = UI.mix(color, 0.5, c);
			}
		}
		
		if(color != null)
		{
			color = UI.mix(color, 0.5, getBackground()); 
		}
		
		return color;
	}
	
	
	protected int getLineNumber(int rowStartOffset)
	{
		Element root = component.getDocument().getDefaultRootElement();
		return root.getElementIndex(rowStartOffset);
	}


	// We need to know if the caret is currently positioned on the line we
	// are about to paint so the line number can be highlighted.
	protected boolean isCurrentLine(int rowStartOffset)
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		if(root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	// Get the line index, or null when a line of text has wrapped.
	protected Integer getTextLineIndex(int rowStartOffset)
	{
		Element root = component.getDocument().getDefaultRootElement();
		int index = root.getElementIndex(rowStartOffset);
		Element line = root.getElement(index);

		if(line.getStartOffset() == rowStartOffset)
		{
			return index;
		}
		else
		{
			return null;
		}
	}


	/** Determine the X offset to properly align the line number when drawn */
	protected int getOffsetX(int availableWidth, int stringWidth)
	{
		return (int)((availableWidth - stringWidth) * digitAlignment);
	}


	/** Determine the Y offset for the current row */
	protected int getOffsetY(int rowStartOffset, FontMetrics fontMetrics) throws BadLocationException
	{
		// Get the bounding rectangle of the row
		Rectangle r = component.modelToView(rowStartOffset);
		int lineHeight = fontMetrics.getHeight();
		
		// FIX r = null sometimes
		int y = r.y + r.height;
		int descent = 0;

		// The text needs to be positioned above the bottom of the bounding
		// rectangle based on the descent of the font(s) contained on the row.
		if(r.height == lineHeight) // default font is being used
		{
			descent = fontMetrics.getDescent();
		}
		else
		{
			// We need to check all the attributes for font changes
			if(fonts == null)
			{
				fonts = new CMap<String,FontMetrics>();
			}

			Element root = component.getDocument().getDefaultRootElement();
			int index = root.getElementIndex(rowStartOffset);
			Element line = root.getElement(index);

			for(int i=0; i<line.getElementCount(); i++)
			{
				Element child = line.getElement(i);
				AttributeSet as = child.getAttributes();
				String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
				Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
				String key = fontFamily + fontSize;

				FontMetrics fm = fonts.get(key);

				if(fm == null)
				{
					Font font = new Font(fontFamily, Font.PLAIN, fontSize);
					fm = component.getFontMetrics(font);
					fonts.put(key, fm);
				}

				descent = Math.max(descent, fm.getDescent());
			}
		}

		return y - descent;
	}


	// CaretListener interface
	public void caretUpdate(CaretEvent ev)
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		int currentLine = root.getElementIndex(caretPosition);

		//  Need to repaint so the correct line number can be highlighted
		if(lastLine != currentLine)
		{
			repaint();
			lastLine = currentLine;
		}
	}


	// DocumentListener interface
	public void changedUpdate(DocumentEvent ev)
	{
		documentChanged();
	}


	public void insertUpdate(DocumentEvent ev)
	{
		documentChanged();
	}


	public void removeUpdate(DocumentEvent ev)
	{
		documentChanged();
	}


	/** A document change may affect the number of displayed lines of text.
	 *  Therefore the lines numbers will also change.
	 */
	protected void documentChanged()
	{
		// View of the component has not been updated at the time
		// the DocumentEvent is fired
		UI.later(new Runnable()
		{
			public void run()
			{
				try
				{
					int endPos = component.getDocument().getLength();
					Rectangle rect = component.modelToView(endPos);

					if(rect != null && rect.y != lastHeight)
					{
						setPreferredWidth();
						repaint();
						lastHeight = rect.y;
					}
				}
				catch(BadLocationException ex)
				{
				}
			}
		});
	}


	// PropertyChangeListener interface
	public void propertyChange(PropertyChangeEvent ev)
	{
		String prop = ev.getPropertyName();
		if("font".equals(prop))
		{
			if(updateFont)
			{
				Font newFont = (Font)ev.getNewValue();
				setFont(newFont);
				lastDigits = 0;
				setPreferredWidth();
			}
			else
			{
				repaint();
			}
		}
		else if("document".equals(prop))
		{
			if(document != null)
			{
				document.removeDocumentListener(this);
			}
			
			document = (Document)ev.getNewValue();
			if(document != null)
			{
				document.addDocumentListener(this);
				documentChanged();
			}
		}
	}
	
	
	public void addHighlight(Color c, int line)
	{
		if(highlights == null)
		{
			highlights = new CMap();
		}
		highlights.put(line, c);
		repaint(30);
	}
	
	
	public void addHighlights(Color c, Collection<Integer> lines)
	{
		if(lines != null)
		{
			if(highlights == null)
			{
				highlights = new CMap();
			}
			
			for(int line: lines)
			{
				highlights.put(line, c);
			}
			repaint();
		}
	}
	
	
	public void setHighlights(Color c, Collection<Integer> lines)
	{
		clearHighlights();
		addHighlights(c, lines);
	}
	
	
	public void clearHighlights()
	{
		if(highlights != null)
		{
			highlights = null;
			repaint();
		}
	}
}
