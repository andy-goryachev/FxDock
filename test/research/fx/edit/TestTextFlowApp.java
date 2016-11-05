// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import goryachev.fx.FxWindow;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Test CTextFlow app
 */
public class TestTextFlowApp
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	public void init() throws Exception
	{
		// TODO change to something visible in Documents? platform-specific?
		File baseDir = new File(System.getProperty("user.home"), ".goryachev.com");
			
		File logFolder = new File(baseDir, "logs"); 
		Log.init(logFolder);
		
		File settingsFile = new File(baseDir, "settings.conf");
		FileSettingsProvider p = new FileSettingsProvider(settingsFile);
		GlobalSettings.setProvider(p);
		p.loadQuiet();
	}


	public void start(Stage stage) throws Exception
	{
		new TestTextFlowWindow().open();
	}
	
	
	//
	
	
	public static class TestTextFlowWindow
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
}
