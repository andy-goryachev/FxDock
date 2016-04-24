// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CIcon;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.ChoiceDialog;
import goryachev.common.ui.InfoField;
import goryachev.common.ui.Theme;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import goryachev.common.util.TXT;
import goryachev.common.util.UserException;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.Document;


public abstract class ProcessPanel
	extends CPanel 
{
	protected abstract void execute() throws Exception;
	
	protected abstract void onSuccess();
	
	protected void onFailure(Throwable e) { }
	
	//
	
	public final CAction cancelAction = new CAction() { public void action() { actionCancel(); }};
	protected final Action continueAction;
	protected final Action[] otherActions;
	public final JLabel statusField;
	public final InfoField textField;
	public final CScrollPane scroll;
	private BackgroundThread thread;
	protected Throwable error;
	protected boolean cancelled;
	
	
	public ProcessPanel(Action continueAction, Action ... otherActions)
	{
		this.continueAction = continueAction;
		this.otherActions = otherActions;
		
		statusField = new JLabel(new CIcon(32));
		statusField.setBorder(new CBorder(5));
		statusField.setBackground(Theme.FIELD_BG);
		statusField.setVerticalAlignment(JLabel.TOP);
		statusField.setOpaque(true);
		
		textField = new InfoField();
		textField.setBorder(new CBorder(5));
		textField.setEditable(false);
		textField.setScrollableTracksViewportWidth(true);
		
		scroll = new CScrollPane(textField, CScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, CScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		setWest(statusField);
		setCenter(scroll);
	}
	
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	
	public boolean isRunning()
	{
		return (thread != null);
	}
	
	
	public boolean isError()
	{
		return (error != null);
	}
	
	
	public void comfortSleep(int ms)
	{
		if(thread != null)
		{
			thread.comfortSleep(ms);
		}
	}
	
	
	protected void updateActions()
	{
		if(hasButtonPanel())
		{
			boolean running = (thread != null);
			buttonPanel().setButtonEnabled(continueAction, !running && (error == null));
			for(Action a: otherActions)
			{
				buttonPanel().setButtonEnabled(a, !running);
			}
			buttonPanel().setButtonEnabled(cancelAction, running);
		}
	}
	
	
	public void start()
	{
		if(thread != null)
		{
			throw new Rex("already started");
		}
		
		thread = new BackgroundThread("ProcessPage")
		{
			public void process() throws Throwable
			{
				// process body
				execute();
			}
			
			public void success()
			{
				setResult(this, null);
				onSuccess();
			}
			
			public void onError(Throwable e)
			{
				setResult(this, e);
				onFailure(e);
			}
		};

		setStatusIcon(Theme.waitIcon(48));
		error = null;
		updateActions();
		
		thread.start();
	}
	
	
	protected void setResult(BackgroundThread t, Throwable err)
	{
		if(t == thread)
		{
			thread = null;
						
			if(err == null)
			{
				setStatusIcon(CIcons.Success48);
				setText(TXT.get("ProcessPanel.process completed", "Completed"));
			}
			else if(CancelledException.is(err))
			{
				setStatusIcon(CIcons.Cancelled48);
				setText(TXT.get("ProcessPanel.process.cancelled", "Cancelled"));
			}
			else
			{
				error = err;
				
				setStatusIcon(CIcons.Error48);
				setText(TXT.get("ProcessPanel.process.failed", "Failed"));
				
				if(err instanceof UserException)
				{
					setText(err.getMessage());
				}
				else
				{
					Log.err(err);
					setText(CKit.stackTrace(err));
					
					// this looks better
					textField.setScrollableTracksViewportWidth(false);
				}
			}
			
			updateActions();
		}
	}
	
	
	public void setStatusIcon(Icon icon)
	{
		statusField.setIcon(icon);
	}
	
	
	public void setText(String s)
	{
		textField.setText(s);
		textField.setCaretPosition(0);
	}
	
	
	public void setDocument(Document d)
	{
		textField.setDocument(d);
		textField.setCaretPosition(0);
	}
	
	
	protected void actionCancel()
	{
		ChoiceDialog<Boolean> d = new ChoiceDialog
		(
			this,
			TXT.get("ProcessPanel.interrupt.title", "Interrupt?"), 
			TXT.get("ProcessPanel.interrupt.d", "Do you want to interrupt the current operation?")
		);
		
		d.addButton(TXT.get("ProcessPanel.button.interrupt", "Interrupt"), Boolean.TRUE, Theme.DESTRUCTIVE_BUTTON_COLOR);
		d.addButton(TXT.get("ProcessPanel.button.allow", "Allow to Continue"), null, true);
		
		Object rv = d.openChoiceDialog();
		if(Boolean.TRUE.equals(rv))
		{
			cancel();
		}
	}
	
	
	public void cancel()
	{
		if(thread != null)
		{
			thread.cancel();
			cancelled = true;
			thread = null;
			error = null;
			
			setStatusIcon(CIcons.Cancelled48);
			setText(TXT.get("ProcessPanel.interrupted", "Operation interrupted by user."));
			updateActions();
		}
	}
}
