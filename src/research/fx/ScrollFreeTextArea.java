package research.fx;
import goryachev.fx.FxSize;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import research.fx.edit.FxHacks;


// http://stackoverflow.com/questions/15593287/binding-textarea-height-to-its-content/19717901#19717901
public class ScrollFreeTextArea
	extends TextArea
{
	private ScrollPane scroll;
	
	
	public ScrollFreeTextArea()
	{
		setWrapText(true);
		setPrefHeight(USE_COMPUTED_SIZE);
	}
	
	
	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		
		if(scroll == null)
		{
			Node scrollBar = lookup(".scroll-bar:vertical");
			if(scrollBar != null)
			{
				scroll = (ScrollPane)scrollBar.getParent();
				scroll.setFitToWidth(true);
//				scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			}
		}
			
		if(scroll != null)
		{
			scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		}
		
//		ScrollBar scrollBarv = (ScrollBar)this.lookup(".scroll-bar:vertical");
//		if(scrollBarv != null)
//		{
//			//System.out.println("hiding vbar");
//			((ScrollPane)scrollBarv.getParent()).setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//		}
		/*
		ScrollBar scrollBarh = (ScrollBar)this.lookup(".scroll-bar:horizontal");
		if(scrollBarh != null)
		{
			//System.out.println("hiding hbar");
			((ScrollPane)scrollBarh.getParent()).setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		}
		*/
	}


	@Override
	protected double computePrefWidth(double height)
	{
//		return Double.MAX_VALUE;
		
//		return super.computePrefWidth(height);
		
		FxSize d = FxHacks.getTextBounds(this, -1);
		Insets insets = getInsets();
		double w = Math.ceil(d.getWidth() + insets.getLeft() + insets.getRight());
		return w;
	}


	@Override
	protected double computePrefHeight(double width)
	{
//		return super.computePrefHeight(width);

		FxSize d = FxHacks.getTextBounds(this, width);
		return d.getHeight();
	}
}