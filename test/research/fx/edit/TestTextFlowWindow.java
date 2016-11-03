// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.SB;
import goryachev.fx.FxWindow;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 * Test CTextFlow Window.
 */
public class TestTextFlowWindow
	extends FxWindow
{
	protected Text info = new Text();
	
	
	public TestTextFlowWindow()
	{
		super("TestTextFlowWindow");
		
		setTitle("TextFlow Test");
		setSize(600, 200);

		setTop(tf());
		setBottom(new CTextFlow(info));
	}
	
	
	protected CTextFlow tf()
	{
		String text = "The quick brown fox jumps over the lazy dog.  از ویکی‌پدیا، دانشنامهٔ آزاد すばしっこい茶色の狐はのろまな犬を飛び越える";
		
		Text t = new Text(text);
		
		CTextFlow f = new CTextFlow(t);
		f.addEventFilter(MouseEvent.ANY, (ev) -> handleMouseEvent(f, ev));
		return f;
	}


	protected void handleMouseEvent(CTextFlow t, MouseEvent ev)
	{
		Point2D p = t.screenToLocal(ev.getScreenX(), ev.getScreenY());
		TextPos pos = t.getTextPos(p.getX(), p.getY());
			
		SB sb = new SB();
		sb.a(pos);
		
		info.setText(sb.toString());
	}
}
