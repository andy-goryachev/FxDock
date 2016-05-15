// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo.dock;
import goryachev.common.util.D;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.fx.CAction;
import goryachev.fx.CCheckMenuItem;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.WindowListMenuItem;
import goryachev.fxdock.internal.DockTools;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


/**
 * Demo Window.
 */
public class DemoWindow
    extends FxDockWindow
{
	public static final CAction exitApplicationAction = new CAction() { public void action() { FxDockFramework.exit(); }};
	public static final CAction newWindowAction = new CAction() { public void action() { actionNewWindow(); }};
	public static final CAction saveSettingsAction = new CAction() { public void action() { actionSaveSettings(); }};
	public final CAction windowCheckAction = new CAction() { public void action() {  }};
	public final Label statusField = new Label();

	
	public DemoWindow()
	{
		setTop(createMenu());
		setBottom(createStatusBar());
//		setMinSize(500, 300);
		setTitle("FxDock Framework Demo");
		
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
		m.addSeparator();
		m.add("Close Window", closeWindowAction);
		m.addSeparator();
		m.add("Exit Application", exitApplicationAction);
		// window
		mb.add(m = new CMenu("Window"));
		m.add("New Window", newWindowAction);
		m.add(new WindowListMenuItem(this, m));
		// help
		mb.add(m = new CMenu("Help"));
		m.add(new CCheckMenuItem("Check Box Menu", windowCheckAction));
		m.add(m2 = new CMenu("Test", new CAction() { public void action() { D.print("test"); }}));
		m2.add("T2", new CAction() { public void action() { D.print("t2"); }});
		m.add("T3", new CAction() { public void action() { D.print("t3"); }});
		return mb;
	}


	protected Node createStatusBar()
	{
		BorderPane p = new BorderPane();
		p.setLeft(statusField);
		p.setRight(FX.label("copyright © 2016 andy goryachev", new Insets(1, 20, 1, 10)));
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


	public void saveSettings(String prefix)
	{
		super.saveSettings(prefix);
		
		String s = DockTools.saveLayout(getContent()).toString();
		statusField.setText(s);
	}
}
