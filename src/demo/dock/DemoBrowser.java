// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CSystem;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxButton;
import goryachev.fx.HPane;
import goryachev.fx.internal.LocalSettings;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockStyles;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;


/**
 * Demo Browser.
 */
public class DemoBrowser
	extends FxDockPane
{
	protected static final Log log = Log.get("DemoBrowser");
	public final FxAction reloadAction = new FxAction(this::reload);
	public final TextField addressField;
	public final WebView view;
	public final Label statusField;
	
	
	public DemoBrowser()
	{
		super(DemoGenerator.BROWSER);
		setTitle("Browser / " + CSystem.getJavaVersion());
		
		addressField = new TextField();
		addressField.addEventHandler(KeyEvent.KEY_PRESSED, (ev) -> handleKeyTyped(ev));
		LocalSettings.get(this).add("URL", addressField);
		
		view = new WebView();
		view.getEngine().setOnError((ev) -> handleError(ev));
		view.getEngine().setOnStatusChanged((ev) -> handleStatusChange(ev));
		Worker<Void> w = view.getEngine().getLoadWorker();
		w.stateProperty().addListener(new ChangeListener<Worker.State>()
		{
			public void changed(ObservableValue v, Worker.State old, Worker.State cur)
			{
				log.debug(cur);
				
				if(w.getException() != null && cur == State.FAILED)
				{
					log.error(w.getException());
				}
			}
		});

		statusField = new Label();
		
		CPane p = new CPane();
		p.setGaps(10, 5);
		p.setCenter(view);
		p.setBottom(statusField);
		setContent(p);
		
		FX.later(() -> reload());
	}
	
	
	protected Node createToolBar(boolean tabMode)
	{
		HPane t = new HPane(5);
		t.setMaxWidth(Double.MAX_VALUE);
		t.setPadding(new Insets(2, 2, 2, 2));

		if(!tabMode)
		{
			Button b = new Button("x");
			FX.style(b, FxDockStyles.TOOLBAR_CLOSE_BUTTON);
			closeAction.attach(b);

			t.add(titleField);
			t.add(b);
		}
		t.fill(addressField);
		t.add(new FxButton("Reload", reloadAction));
		return t;
	}
	
	
	public void reload()
	{
		String url = getUrl();
		if(CKit.isNotBlank(url))
		{
			openPage(url);
		}
	}
	
	
	public String getUrl()
	{
		return addressField.getText();
	}


	protected void handleStatusChange(WebEvent<String> ev)
	{
		statusField.setText(ev.getData());
	}


	protected void handleError(WebErrorEvent ev)
	{
		log.error(ev);
	}


	public void openPage(String url)
	{
		log.info(url);
		
		addressField.setText(url);
		view.getEngine().load(url);
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		KeyCode c = ev.getCode();
		switch(c)
		{
		case ENTER:
			openPage(addressField.getText());
			break;
		default:
			return;
		}
		
		ev.consume();
	}
}
