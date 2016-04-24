// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import java.awt.Container;
import java.text.BreakIterator;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.LabelView;
import javax.swing.text.Segment;
import javax.swing.text.View;


// there is an issue in java 7 where it does not break long words, see
// http://stackoverflow.com/questions/8666727/wrap-long-words-in-jtextpane-java-7
// http://bugs.sun.com/view_bug.do?bug_id=6539700
// This class and XParagprahView seem to fix the problem
public class XLabelView
	extends LabelView
{
	public XLabelView(Element elem)
	{
		super(elem);
	}


	// 1.6
//	public float getMinimumSpan(int axis)
//	{
//		int w = getResizeWeight(axis);
//		if(w == 0)
//		{
//			// can't resize
//			return getPreferredSpan(axis);
//		}
//		return 0;
//	}

	// https://forums.oracle.com/forums/thread.jspa?messageID=10690405
	public float getMinimumSpan(int axis)
	{
		switch(axis)
		{
		case View.X_AXIS:
			return 0;
		case View.Y_AXIS:
			return super.getMinimumSpan(axis);
		default:
			throw new IllegalArgumentException("Invalid axis: " + axis);
		}
	}


	public int getBreakWeight(int axis, float pos, float len)
	{
		if(axis == View.X_AXIS)
		{
			checkPainter();
			int p0 = getStartOffset();
			int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
			if(p1 == p0)
			{
				// can't even fit a single character
				return View.BadBreakWeight;
			}
			if(getBreakSpot(p0, p1) != -1)
			{
				return View.ExcellentBreakWeight;
			}
			// Nothing good to break on.
			return View.GoodBreakWeight;
		}
		return super.getBreakWeight(axis, pos, len);
	}


	protected int getBreakSpot(int p0, int p1)
	{
//		Document doc = getDocument();
//		if(doc != null && Boolean.TRUE.equals(doc.getProperty(AbstractDocument.MultiByteProperty)))
//		{
//			return getBreakSpotUseBreakIterator(p0, p1);
//		}
		return getBreakSpotUseWhitespace(p0, p1);
	}


	protected int getBreakSpotUseWhitespace(int p0, int p1)
	{
		Segment s = getText(p0, p1);

		for(char ch = s.last(); ch != Segment.DONE; ch = s.previous())
		{
			if(Character.isWhitespace(ch))
			{
				// found whitespace
//				SegmentCache.releaseSharedSegment(s);
				return s.getIndex() - s.getBeginIndex() + 1 + p0;
			}
		}
//		SegmentCache.releaseSharedSegment(s);
		return -1;
	}


	/**
	 * Returns the appropriate place to break based on BreakIterator.
	 */
	protected int getBreakSpotUseBreakIterator(int p0, int p1)
	{
		// Certain regions require context for BreakIterator, start from
		// our parents start offset.
		Element parent = getElement().getParentElement();
		int parent0;
		int parent1;
		Container c = getContainer();
		BreakIterator breaker;

		if(parent == null)
		{
			parent0 = p0;
			parent1 = p1;
		}
		else
		{
			parent0 = parent.getStartOffset();
			parent1 = parent.getEndOffset();
		}
		if(c != null)
		{
			breaker = BreakIterator.getLineInstance(c.getLocale());
		}
		else
		{
			breaker = BreakIterator.getLineInstance();
		}

		Segment s = getText(parent0, parent1);
		int breakPoint;

		// Needed to initialize the Segment.
		s.first();
		breaker.setText(s);

		if(p1 == parent1)
		{
			breakPoint = breaker.last();
		}
		else if(p1 + 1 == parent1)
		{
			breakPoint = breaker.following(s.offset + s.count - 2);
			if(breakPoint >= s.count + s.offset)
			{
				breakPoint = breaker.preceding(s.offset + s.count - 1);
			}
		}
		else
		{
			breakPoint = breaker.preceding(p1 - parent0 + s.offset + 1);
		}

		int retValue = -1;
		if(breakPoint != BreakIterator.DONE)
		{
			breakPoint = breakPoint - s.offset + parent0;
			if(breakPoint > p0)
			{
				if(p0 == parent0 && breakPoint == p0)
				{
					retValue = -1;
				}
				else if(breakPoint <= p1)
				{
					retValue = breakPoint;
				}
			}
		}

		//		SegmentCache.releaseSharedSegment(s);
		return retValue;
	}


	public View breakView(int axis, int p0, float pos, float len)
	{
		if(axis == View.X_AXIS)
		{
			checkPainter();
			int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
			int breakSpot = getBreakSpot(p0, p1);

			if(breakSpot != -1)
			{
				p1 = breakSpot;
			}
			
			// else, no break in the region, return a fragment of the
			// bounded region.
			if(p0 == getStartOffset() && p1 == getEndOffset())
			{
				return this;
			}
			
			GlyphView v = (GlyphView)createFragment(p0, p1);
			
			// FIX no setter
			//v.x = (int)pos;
			
			return v;
		}
		return this;
	}
}
