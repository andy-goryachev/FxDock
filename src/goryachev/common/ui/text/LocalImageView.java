// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;


public abstract class LocalImageView 
	extends View 
	implements ImageObserver
{
	public abstract ImageIcon loadImage(String src);
	
	//
	
	public static final String
		TOP       = "top",
		TEXTTOP   = "texttop",
		MIDDLE    = "middle",
		ABSMIDDLE = "absmiddle",
		CENTER    = "center",
		BOTTOM    = "bottom";
	
	private AttributeSet attr;
	private Element element;
	private ImageIcon image;
	private int height;
	private int width;
	private Container container;
	private Rectangle bounds;
	private Component component;
    

	public LocalImageView(Element elem) 
	{
		super(elem);
		initialize(elem);
		StyleSheet sheet = getStyleSheet();
		attr = sheet.getViewAttributes(this);
	}
	   
    
	protected String getSourcePath() 
	{
		return (String)element.getAttributes().getAttribute(HTML.Attribute.SRC);
	}

    
	protected synchronized void initialize(Element em) 
	{
		element = em;
		String src = getSourcePath();
		if(src != null) 
		{
			image = loadImage(src);
			if(image != null)
			{
				width = image.getIconWidth();
				height = image.getIconHeight();
				return;
			}
		}
		image = null;
		width = 0;
		height = 0;
	}


	public AttributeSet getAttributes() 
	{
		return attr;
	}

	
	protected boolean isLink( ) 
	{
		AttributeSet anchorAttr = (AttributeSet)element.getAttributes().getAttribute(HTML.Tag.A);
		if(anchorAttr != null) 
		{
			return anchorAttr.isDefined(HTML.Attribute.HREF);
		}
		return false;
	}
	
	
	protected int getBorder() 
	{
		return getIntAttr(HTML.Attribute.BORDER, isLink() ? 1 : 0);
	}


	// Returns the amount of extra space to add along an axis
	protected int getSpace(int axis)
	{
		return getIntAttr(axis == X_AXIS ? HTML.Attribute.HSPACE : HTML.Attribute.VSPACE, 0);
	}


	// Returns the border's color, or null if this is not a link
	protected Color getBorderColor()
	{
		StyledDocument doc = (StyledDocument)getDocument();
		return doc.getForeground(getAttributes());
	}
    
    
	protected float getVerticalAlignment() 
	{
		String align = (String)element.getAttributes().getAttribute(HTML.Attribute.ALIGN);
		if(align != null) 
		{
			align = align.toLowerCase();
			if(align.equals(TOP) || align.equals(TEXTTOP))
			{
				return 0.0f;
			}
			else if(align.equals(CENTER) || align.equals(MIDDLE) || align.equals(ABSMIDDLE))
			{
				return 0.5f;
			}
		}
		return 1.0f;
	}
    
	
	// Look up an integer-valued attribute. Not recursive
	protected int getIntAttr(HTML.Attribute name, int deflt) 
	{
		AttributeSet attr = element.getAttributes();
		if(attr.isDefined(name)) 
		{
			// does not check parents!
			int i;
			String val = (String)attr.getAttribute(name);
			if(val == null)
			{
				i = deflt;
			}
			else
			try
			{
				i = Math.max(0, Integer.parseInt(val));
			}
			catch(NumberFormatException x) 
			{
				i = deflt;
			}
			return i;
		}
		else
		{
			return deflt;
		}
	}


	public void setParent(View parent)
	{
		super.setParent(parent);
		container = (parent != null ? getContainer() : null);
		if(parent==null && component!=null) 
		{
			component.getParent().remove(component);
			component = null;
		}
	}


	public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f)
	{
		super.changedUpdate(e, a, f);
		float align = getVerticalAlignment();

		int h = height;
		int w = width;

		initialize(getElement());

		boolean hChanged = (height != h);
		boolean wChanged = (width != w);
		if(hChanged || wChanged || getVerticalAlignment() != align)
		{
			getParent().preferenceChanged(this, hChanged, wChanged);
		}
	}


	public void paint(Graphics g, Shape a)
	{
		Color oldColor = g.getColor();
		bounds = a.getBounds();
		int border = getBorder();
		int x = bounds.x + border + getSpace(X_AXIS);
		int y = bounds.y + border + getSpace(Y_AXIS);
		int w = width;
		int h = height;
		int sel = getSelectionState();
		
		if(image != null ) 
		{
			g.drawImage(image.getImage(),x,y,w,h,this);
		}
		
		// If selected exactly, we need a black border & grow-box:
		Color bc = getBorderColor();
		if(sel == 2) 
		{
			// Make sure there's room for a border:
			int delta = 2-border;
			if(delta > 0) 
			{
				x += delta;
				y += delta;
				w -= delta<<1;
				h -= delta<<1;
				border = 2;
			}
			bc = null;
			g.setColor(Color.black);
			// Draw grow box:
			g.fillRect(x+w-5,y+h-5,5,5);
		}
		
		// draw border
		if(border > 0) 
		{
			if(bc != null) 
			{
				g.setColor(bc);
			}
			// draw a thick rectangle
			for(int i=1; i<=border; i++)
			{
				g.drawRect(x-i, y-i, w-1+i+i, h-1+i+i);
			}
			g.setColor(oldColor);
		}
	}

	
	protected void repaint(long delay) 
	{
		if(container != null && bounds!=null) 
		{
			container.repaint(delay,bounds.x,bounds.y,bounds.width,bounds.height);
		}
	}
    
	
	// Determines whether the image is selected, and if it's the only thing selected.
	// return  0 if not selected, 1 if selected, 2 if exclusively selected.
	// "Exclusive" selection is only returned when editable. */
	protected int getSelectionState()
	{
		int p0 = element.getStartOffset();
		int p1 = element.getEndOffset();
		if(container instanceof JTextComponent)
		{
			JTextComponent textComp = (JTextComponent)container;
			int start = textComp.getSelectionStart();
			int end = textComp.getSelectionEnd();
			if(start <= p0 && end >= p1)
			{
				if(start == p0 && end == p1 && isEditable())
				{
					return 2;
				}
				else
				{
					return 1;
				}
			}
		}
		return 0;
	}


	protected boolean isEditable()
	{
		return (container instanceof JEditorPane) && ((JEditorPane)container).isEditable();
	}


	protected Color getHighlightColor()
	{
		JTextComponent textComp = (JTextComponent)container;
		return textComp.getSelectionColor();
	}


	// no progressive display
	public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height)
	{
		return false;
	}
    

	public float getPreferredSpan(int axis) 
	{
		int extra = 2*(getBorder()+getSpace(axis));
		switch(axis) 
		{
		case View.X_AXIS: return width + extra;
		case View.Y_AXIS: return height + extra;
		default:          throw new IllegalArgumentException("Invalid axis: " + axis);
		}
	}


	public float getAlignment(int axis) 
	{
		switch (axis) 
		{
		case View.Y_AXIS: return getVerticalAlignment();
		default:          return super.getAlignment(axis);
		}
	}


	/**
	 * Provides a mapping from the document model coordinate space
	 * to the coordinate space of the view mapped to it.
	 * @param pos the position to convert
	 * @param a the allocated region to render into
	 * @return the bounding box of the given position
	 * @exception BadLocationException  if the given position does not represent a
	 *   valid location in the associated document
	 * @see View#modelToView
	 */
	public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
	{
		int p0 = getStartOffset();
		int p1 = getEndOffset();
		if((pos >= p0) && (pos <= p1))
		{
			Rectangle r = a.getBounds();
			if(pos == p1)
			{
				r.x += r.width;
			}
			r.width = 0;
			return r;
		}
		return null;
	}


	/**
	 * Provides a mapping from the view coordinate space to the logical
	 * coordinate space of the model.
	 *
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param a the allocated region to render into
	 * @return the location within the model that best represents the
	 *  given point of view
	 * @see View#viewToModel
	 */
	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
	{
		Rectangle alloc = (Rectangle)a;
		if(x < alloc.x + alloc.width)
		{
			bias[0] = Position.Bias.Forward;
			return getStartOffset();
		}
		bias[0] = Position.Bias.Backward;
		return getEndOffset();
	}


	public void setSize(float width, float height)
	{
		// Ignore -- image size is determined by the tag attrs
	}


	// Change the size of this image. This alters the HEIGHT and WIDTH
	// attributes of the Element and causes a re-layout
	protected void resize(int w, int h)
	{
		if(w==width && h==height)
		{
			return;
		}
    	
		width = w;
		height = h;
    	
		// Replace attributes in document
		MutableAttributeSet attr = new SimpleAttributeSet();
		attr.addAttribute(HTML.Attribute.WIDTH ,Integer.toString(w));
		attr.addAttribute(HTML.Attribute.HEIGHT,Integer.toString(h));
		((StyledDocument)getDocument()).setCharacterAttributes(element.getStartOffset(),element.getEndOffset(),attr,false);
	}
    
    
	protected StyleSheet getStyleSheet() 
	{
		HTMLDocument doc = (HTMLDocument)getDocument();
		return doc.getStyleSheet();
	}
}
