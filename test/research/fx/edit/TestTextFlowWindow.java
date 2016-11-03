// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.SB;
import goryachev.fx.FxWindow;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;


/**
 * Test CTextFlow Window.
 */
public class TestTextFlowWindow
	extends FxWindow
{
	protected final Text info;
	protected final Path caret;
	protected final Path highlight;
	
	
	public TestTextFlowWindow()
	{
		super("TestTextFlowWindow");
		
		setTitle("TextFlow Test");
		setSize(600, 200);
		
		info = new Text();
		
		highlight = new Path();
		highlight.setManaged(false);
		highlight.setStroke(null);
		highlight.setFill(Color.YELLOW);
		
		caret = new Path();
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);

		setTop(tf());
		setBottom(new CTextFlow(info));
	}
	
	
	protected CTextFlow tf()
	{
		String text = "The quick brown fox jumps over the lazy dog.  از ویکی‌پدیا، دانشنامهٔ آزاد すばしっこい茶色の狐はのろまな犬を飛び越える 𓀀𓀁𓀂𓀃 𓀄𓀅𓀆𓀇𓀈 𓀉𓀊𓀋𓀌𓀍𓀎𓀏";
		
		Text t = new Text(text);
		t.setStyle("-fx-font-size:300%");
		
		CTextFlow f = new CTextFlow(highlight, t, caret);
		f.addEventFilter(MouseEvent.ANY, (ev) -> handleMouseEvent(f, ev));
		return f;
	}


	protected void handleMouseEvent(CTextFlow t, MouseEvent ev)
	{
		Point2D p = t.screenToLocal(ev.getScreenX(), ev.getScreenY());
		TextPos pos = t.getTextPos(p.getX(), p.getY());
		
		highlight.getElements().setAll(t.getRange(0, pos.getInsertionIndex()));
		
		caret.getElements().setAll(t.getCaretShape(pos.getIndex(), pos.isLeading()));
			
		SB sb = new SB();
		sb.a(pos);
		
		info.setText(sb.toString());
	}
}
