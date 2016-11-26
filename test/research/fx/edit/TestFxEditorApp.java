// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Test FxEditor app.
 */
public class TestFxEditorApp
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
		new TestFxEditorWindow().open();
	}
	
	
	//
	
	
	public static class TestFxEditorWindow
		extends FxWindow
	{
		public TestFxEditorWindow()
		{
			super("TestFxEditorWindow");
			
			FxEditorModel m =
				// new TestFxPlainEditorModel();
				new TestFxColorEditorModel();
			
			FxEditor ed = new FxEditor(m);
			ed.setBlinkRate(Duration.millis(300));
			
			setTitle("FxEditor Test");
			setCenter(ed);
			setSize(600, 700);
			
			FxDump.attach(this);
		}
	}
}
