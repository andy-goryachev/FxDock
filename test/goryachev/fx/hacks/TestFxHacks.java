// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;
import goryachev.common.util.D;
import goryachev.fx.FX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * Test FxHacks.
 */
public class TestFxHacks
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	public void start(Stage stage)
	{
		StackPane root = new StackPane();

		Scene scene = new Scene(root, 500, 400);

		stage.setTitle("Test FxHacks");
		stage.setScene(scene);
		stage.show();
		
		FX.later(() -> diag());
	}
	
	
	protected void diag()
	{
		D.describe(FxHacks.get().getWindows());
		FxHacks.get().applyStyleSheet(null, "");
	}
}
