// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.D;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.fx.CCheckMenuItem;
import goryachev.fx.CDialog;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.GlobalBooleanProperty;
import goryachev.fx.OnWindowClosing;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.WindowListMenuItem;
import goryachev.fxdock.internal.DockTools;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


/**
 * Demo Window.
 */
public class DemoWindow
    extends FxDockWindow
{
	public static final FxAction newBrowserAction = new FxAction(DemoWindow::actionNewBrowser);
	public static final FxAction newCPaneAction = new FxAction(DemoWindow::actionNewCPane);
	public static final FxAction newHPaneAction = new FxAction(DemoWindow::actionNewHPane);
	public static final FxAction newVPaneAction = new FxAction(DemoWindow::actionNewVPane);
	public static final FxAction newLoginAction = new FxAction(DemoWindow::actionNewLogin);
	public static final FxAction newWindowAction = new FxAction(DemoWindow::actionNewWindow);
	public static final FxAction quitApplicationAction = new FxAction(FxDockFramework::exit);
	public static final FxAction saveSettingsAction = new FxAction(DemoWindow::actionSaveSettings);
	public final FxAction windowCheckAction = new FxAction();
	public final Label statusField = new Label();
	private static GlobalBooleanProperty showCloseDialogProperty = new GlobalBooleanProperty("show.close.dialog", true);

	
	public DemoWindow()
	{
		setTop(createMenu());
		setBottom(createStatusBar());
		setTitle(DockDemoApp.TITLE);
		
		bind("CHECKBOX_MENU", windowCheckAction.selectedProperty());
	}
	
	
	protected Node createMenu()
	{
		CMenuBar mb = new CMenuBar();
		CMenu m;
		CMenu m2;
		// file
		mb.add(m = new CMenu("File"));
		m.add("Save Settings", saveSettingsAction);
		m.separator();
		m.add("Close Window", closeWindowAction);
		m.separator();
		m.add("Quit Application", quitApplicationAction);
		// window
		mb.add(m = new CMenu("Window"));
		m.add("New Browser", newBrowserAction);
		m.add("New Demo Window", newWindowAction);
		m.add("New Login Window", newLoginAction);
		m.separator();
		m.add("CPane Example", newCPaneAction);
		m.add("HPane Example", newHPaneAction);
//		m.add("VPane Example", newVPaneAction);
		m.separator();
		m.add(new CCheckMenuItem("Confirm Window Closing", showCloseDialogProperty));
		m.add(new WindowListMenuItem(this, m));
		// help
		mb.add(m = new CMenu("Help"));
		m.add(new CCheckMenuItem("Check Box Menu", windowCheckAction));
		m.add(m2 = new CMenu("Test", new FxAction(() -> D.print("test"))));
		m2.add("T2", new FxAction(() -> D.print("t2")));
		m.add("T3", new FxAction(() -> D.print("t3")));
		return mb;
	}


	protected Node createStatusBar()
	{
		BorderPane p = new BorderPane();
		p.setLeft(statusField);
		p.setRight(FX.label(DockDemoApp.COPYRIGHT, new Insets(1, 20, 1, 10)));
		return p;
	}

	
	public static DemoWindow actionNewWindow()
	{
		SB sb = new SB();
		c(sb);
		c(sb);
		c(sb);
		String type = sb.toString();
			
		DemoWindow w = new DemoWindow();
		w.setContent(new DemoPane(type));
		w.setWidth(300);
		w.setHeight(200);
		w.open();
		return w;
	}
	
	
	public static DemoWindow actionNewBrowser()
	{
		return openBrowser("https://github.com/andy-goryachev/FxDock");
	}
	
	
	public static DemoWindow actionNewCPane()
	{
		DemoWindow w = new DemoWindow();
		w.setTitle("CPane Demo");
		w.setContent(new DemoCPane());
		w.setWidth(1000);
		w.setHeight(750);
		w.open();
		return w;
	}
	
	
	public static DemoWindow actionNewHPane()
	{
		DemoWindow w = new DemoWindow();
		w.setTitle("HPane Demo");
		w.setContent(new DemoHPane());
		w.setWidth(1000);
		w.setHeight(750);
		w.open();
		return w;
	}
	
	
	public static DemoWindow actionNewVPane()
	{
		DemoWindow w = new DemoWindow();
		w.setTitle("VPane Demo");
//		w.setContent(new DemoVPane());
		w.setWidth(1000);
		w.setHeight(750);
		w.open();
		return w;
	}
	
	
	public static DemoWindow actionNewLogin()
	{
		DemoWindow w = new DemoWindow();
		w.setContent(new DemoLoginPane());
		w.setWidth(400);
		w.setHeight(300);
		w.open();
		return w;
	}
	
	
	public static DemoWindow openBrowser(String url)
	{
		DemoBrowser b = new DemoBrowser();
		b.openPage(url);
		
		DemoWindow w = new DemoWindow();
		w.setContent(b);
		w.setWidth(900);
		w.setHeight(700);
		w.open();
		return w;
	}
	
	
	protected static void c(SB sb)
	{
		int min = 100;
		int v = min + new Random().nextInt(255 - min);
		sb.append(Hex.toHexByte(v));
	}
	
	
	protected static void actionSaveSettings()
	{
		FxDockFramework.saveLayout();
		GlobalSettings.save();
	}


	public void storeSettings(String prefix)
	{
		super.storeSettings(prefix);
		
		String s = DockTools.saveLayout(getContent()).toString();
		statusField.setText(s);
	}
	
	
	public void save()
	{
		// indicates saving the changes
		D.print("save");
	}


	// this method illustrates how to handle closing a window,
	// or closing multiple window when quitting the application.
	public void confirmClosing(OnWindowClosing ch)
	{
		if(!showCloseDialogProperty.get())
		{
			return;
		}
		
		if(ch.isSaveAll())
		{
			save();
			return;
		}
		else if(ch.isDiscardAll())
		{
			return;
		}
		
		toFront();
		
		CDialog d = new CDialog(this);
		d.setTitle("Save Changes?");
		d.setContentText("This is an example of a dialog shown when closing a window.");
		
		Object save = d.addButton("Save");
		Object saveAll = null;
		if(ch.isClosingMultipleWindows())
		{
			saveAll = d.addButton("Save All");
		}
		d.addButton("Discard");
		Object discardAll = null;
		if(ch.isClosingMultipleWindows())
		{
			discardAll = d.addButton("Discard All");
		}
		Object cancel = d.addButton("Cancel", ButtonBar.ButtonData.APPLY);
		
		d.showAndWait();
		Object rv = d.getResult();

		if(rv == cancel)
		{
			ch.setCancelled();
		}
		else if(rv == save)
		{
			save();
		}
		else if(rv == saveAll)
		{
			ch.setSaveAll();
			save();
		}
		else if(rv == discardAll)
		{
			ch.setDiscardAll();
		}
	}
}
