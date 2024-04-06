// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.D;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxCheckMenuItem;
import goryachev.fx.FxFramework;
import goryachev.fx.FxMenu;
import goryachev.fx.FxMenuBar;
import goryachev.fx.GlobalBooleanProperty;
import goryachev.fx.OnWindowClosing;
import goryachev.fx.internal.LocalSettings;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.Version;
import goryachev.fxdock.WindowListMenuItem;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
	public static final FxAction saveSettingsAction = new FxAction(DemoWindow::actionSaveSettings);
	public final FxAction windowCheckAction = new FxAction();
	public final Label statusField = new Label();
	private static GlobalBooleanProperty showCloseDialogProperty = new GlobalBooleanProperty("show.close.dialog", true);

	
	public DemoWindow()
	{
		super("DemoWindow");
		
		setTop(createMenu());
		setBottom(createStatusBar());
		setTitle(DockDemoApp.TITLE + " " + Version.VERSION);
		
		LocalSettings.get(this).add("CHECKBOX_MENU", windowCheckAction);
	}
	
	
	protected FxMenuBar createMenu()
	{
		FxMenuBar m = new FxMenuBar();
		// file
		m.menu("File");
		m.item("Save Settings", saveSettingsAction);
		m.separator();
		m.item("Close Window", closeWindowAction);
		m.separator();
		m.item("Quit Application", FxFramework.exitAction());
		// window
		m.menu("Window");
		m.item("New Browser", newBrowserAction);
		m.item("New Demo Window", newWindowAction);
		m.item("New Login Window", newLoginAction);
		m.separator();
		m.item("CPane Example", newCPaneAction);
		m.item("HPane Example", newHPaneAction);
//		m.add("VPane Example", newVPaneAction);
		m.separator();
		m.add(new FxCheckMenuItem("Confirm Window Closing", showCloseDialogProperty));
		m.add(new WindowListMenuItem(this, m.lastMenu()));
		// help
		m.menu("Help");
		m.add(new FxCheckMenuItem("Check Box Menu", windowCheckAction));
		FxMenu m2 = m.menu("Test", new FxAction(() -> D.print("test")));
		m2.item("T2", new FxAction(() -> D.print("t2")));
		m.item("T3", new FxAction(() -> D.print("t3")));
		return m;
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
		b.setUrl(url);
		
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
		FxFramework.storeLayout();
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
		
		// FIX switch to FxDialog
		Dialog d = new Dialog();
		d.initOwner(this);
		d.setTitle("Save Changes?");
		d.setContentText("This is an example of a dialog shown when closing a window.");
		
		Object save = addButton(d, "Save", ButtonBar.ButtonData.OTHER);
		Object saveAll = null;
		if(ch.isClosingMultipleWindows())
		{
			saveAll = addButton(d, "Save All", ButtonBar.ButtonData.OTHER);
		}
		addButton(d, "Discard", ButtonBar.ButtonData.OTHER);
		Object discardAll = null;
		if(ch.isClosingMultipleWindows())
		{
			discardAll = addButton(d, "Discard All", ButtonBar.ButtonData.OTHER);
		}
		Object cancel = addButton(d, "Cancel", ButtonBar.ButtonData.APPLY);
		
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
	
	
	protected static Object addButton(Dialog d, String text, ButtonBar.ButtonData type)
	{
		ButtonType b = new ButtonType(text, type);
		d.getDialogPane().getButtonTypes().add(b);
		return b;
	}
}
