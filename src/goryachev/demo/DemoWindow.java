// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.fx.CAction;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockWindow;
import goryachev.fxdock.WindowListMenuItem;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;


/**
 * DemoWindow.
 */
public class DemoWindow
    extends FxDockWindow
{
	public static final CAction exitApplicationAction = new CAction() { public void action() { FxDockFramework.exit(); } };
	public static final CAction newWindowAction = new CAction() { public void action() { newWindow(); } };
	public static final CAction saveSettingsAction = new CAction() { public void action() { actionSaveSettings(); } };

	
	public DemoWindow()
	{
		setTop(createMenu());
		setBottom(createStatusBar());
		setMinSize(500, 300);
		setTitle("FxDock Framework Demo");
	}
	
	
	protected Node createMenu()
	{
		CMenuBar mb = new CMenuBar();
		CMenu m;
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
		return mb;
	}


	protected Node createStatusBar()
	{
		BorderPane p = new BorderPane();
		p.setRight(FX.label("copyright © 2016 andy goryachev", new Insets(1, 20, 1, 10)));
		return p;
	}

	
	public static DemoWindow newWindow()
	{
		SB sb = new SB();
		c(sb);
		c(sb);
		c(sb);
		String type = sb.toString();
			
		DemoWindow w = new DemoWindow();
		w.setContent(new DemoPane(type));
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
}
