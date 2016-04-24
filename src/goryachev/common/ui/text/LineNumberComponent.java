// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.theme.ThemeColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


// FIX resize when can't fit (use getPreferredSize)
// FIX border repaint issue
// displays line numbers, error margins.  must be added to a scroll pane's row header.
// see also TextLineNumberComponent
public class LineNumberComponent
	extends JPanel
	implements CaretListener, DocumentListener, PropertyChangeListener
{
	public static final Color COLOR_BG = ThemeColor.shadow(ThemeKey.TEXT_BG, 0.2);
	public static final Color COLOR_FG = ThemeColor.highlight(ThemeKey.TEXT_FG, 0.2);
	
	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

	protected JTextComponent component;
	private boolean leftAlignment;
	private int borderGap;
	private Color currentLineForeground;
	private int minimumDisplayDigits;
	private int lastDigits;
	protected int lastHeight;
	private int lastLine;


	public LineNumberComponent(JTextComponent component, int minimumDisplayDigits)
	{
		this.component = component;

		setFont(component.getFont());
		setBorderGap(5);
		//setCurrentLineForeground(Color.YELLOW);
		setMinimumDisplayDigits(minimumDisplayDigits);

		component.getDocument().addDocumentListener(this);
		component.addCaretListener(this);
		component.addPropertyChangeListener("font", this);
		
		setBackground(COLOR_BG);
		setForeground(COLOR_FG);
	}


	public LineNumberComponent(JTextComponent component)
	{
		this(component, 3);
	}


	public int getBorderGap()
	{
		return borderGap;
	}


	public void setBorderGap(int gap)
	{
		this.borderGap = gap;
		// FIX borders
//		Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
//		setBorder(new CompoundBorder(OUTER, inner));
		setBorder(new CBorder(0, borderGap));
		lastDigits = 0;
		computePreferredWidth();
	}


	public Color getCurrentLineForeground()
	{
		return currentLineForeground == null ? getForeground() : currentLineForeground;
	}


	public void setCurrentLineForeground(Color currentLineForeground)
	{
		this.currentLineForeground = currentLineForeground;
	}


	public void setLeftAlignment(boolean on)
	{
		leftAlignment = on;
	}


	public int getMinimumDisplayDigits()
	{
		return minimumDisplayDigits;
	}


	public void setMinimumDisplayDigits(int minimumDisplayDigits)
	{
		this.minimumDisplayDigits = minimumDisplayDigits;
		computePreferredWidth();
	}


	protected void computePreferredWidth()
	{
		Element root = component.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

		if(lastDigits != digits)
		{
			lastDigits = digits;
			FontMetrics fontMetrics = getFontMetrics(getFont());
			int width = fontMetrics.charWidth('0') * digits;
			Insets insets = getInsets();
			int preferredWidth = insets.left + insets.right + width;

			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize(d);
			setSize(d);
		}
	}


	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		FontMetrics fm = component.getFontMetrics(component.getFont());
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		Rectangle clip = g.getClipBounds();
		int lineHeight = fm.getHeight();
		int startOffset = component.getInsets().top + fm.getAscent();
		int linesToDraw = clip.height / lineHeight + 1;
		int y = (clip.y / lineHeight) * lineHeight + startOffset;

		Point viewPoint = new Point(0, y);
		int preferredHeight = component.getPreferredSize().height;

		for(int i = 0; i <= linesToDraw; i++)
		{
			if(isCurrentLine(viewPoint))
			{
				g.setColor(getCurrentLineForeground());
			}
			else
			{
				g.setColor(getForeground());
			}

			String lineNumber = getTextLineNumber(viewPoint, preferredHeight);
			if(lineNumber != null)
			{
				int stringWidth = fm.stringWidth(lineNumber);
				int x = getXOffset(availableWidth, stringWidth) + insets.left;
				g.drawString(lineNumber, x, y);
			}

			y += lineHeight;
			viewPoint.y = y;
			if(y > preferredHeight)
			{
				break;
			}
		}
	}


	protected boolean isCurrentLine(Point viewPoint)
	{
		int offset = component.viewToModel(viewPoint);
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		return (root.getElementIndex(offset) == root.getElementIndex(caretPosition));
	}


	protected int getXOffset(int availableWidth, int stringWidth)
	{
		if(leftAlignment)
		{
			return 0;
		}
		else
		{
			return availableWidth - stringWidth;
		}
	}


	// returns line number as string or null
	protected String getTextLineNumber(Point viewPoint, int preferredHeight)
	{
		int offset = component.viewToModel(viewPoint);
		Element root = component.getDocument().getDefaultRootElement();
		int index = root.getElementIndex(offset);
		Element line = root.getElement(index);

		if(line.getStartOffset() == offset)
		{
			return String.valueOf(index + 1);
		}
		else
		{
			return null;
		}
	}


	// CaretListener interface
	public void caretUpdate(CaretEvent e)
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		int currentLine = root.getElementIndex(caretPosition);
		if(lastLine != currentLine)
		{
			lastLine = currentLine;
			repaint();
		}
	}


	public void changedUpdate(DocumentEvent e)
	{
	}


	public void insertUpdate(DocumentEvent e)
	{
		documentChanged();
	}


	public void removeUpdate(DocumentEvent e)
	{
		documentChanged();
	}


	private void documentChanged()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				int preferredHeight = component.getPreferredSize().height;
				if(lastHeight != preferredHeight)
				{
					computePreferredWidth();
					repaint();
					lastHeight = preferredHeight;
				}
			}
		});
	}


	// PropertyChangeListener interface
	public void propertyChange(PropertyChangeEvent ev)
	{
		if(ev.getNewValue() instanceof Font)
		{
			Font f = (Font)ev.getNewValue();
			setFont(f);
			lastDigits = 0;
			computePreferredWidth();
		}
	}
}
