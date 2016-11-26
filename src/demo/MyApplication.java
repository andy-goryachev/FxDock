package demo;
import goryachev.common.util.GlobalSettings;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockWindow;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MyApplication
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	@Override
	public void start(Stage stage) throws Exception
	{
		GlobalSettings.setFileProvider(new File("my-settings.conf"));
		FxDockFramework.setGenerator(new MyGenerator());

		if(FxDockFramework.loadLayout() == 0) // try to load saved layout
		{
			// create default layout
			FxDockWindow window = new MyWindow();
			window.setWidth(200);
			window.setHeight(400);
			window.open();
		}
	}
}

