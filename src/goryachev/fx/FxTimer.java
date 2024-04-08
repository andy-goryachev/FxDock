// Copyright © 2021-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


/**
 * FxTimer provides API similar to java.swing.Timer.
 */
public class FxTimer
{
	protected static final Log log = Log.get("FxTimer");
	private final Runnable action;
	private final Timeline timeline;
	
	
	/** single shot timer */
	public FxTimer(Duration delay, Runnable action)
	{
		this.action = action;
		
		timeline = new Timeline(new KeyFrame(delay, (ev) -> fire()));
		timeline.setCycleCount(1);
	}
	
	
	/** periodic timer */
	public FxTimer(Duration initialDelay, Duration delay, Runnable action)
	{
		this.action = action;
		
		timeline = new Timeline(new KeyFrame(delay, (ev) -> fire()));
		timeline.setDelay(initialDelay);
		timeline.setCycleCount(Timeline.INDEFINITE);
	}
	
	
	/** single shot timer */
	public FxTimer(int delayMillis, Runnable action)
	{
		this(Duration.millis(delayMillis), action);
	}
	
	
	/** periodic timer */
	public FxTimer(int initialDelay, int delay, Runnable action)
	{
		this(Duration.millis(initialDelay), Duration.millis(delay), action);
	}
	
	
	public void start()
	{
		timeline.play();
	}
	
	
	public void stop()
	{
		timeline.stop();
	}
	
	
	public void restart()
	{
		timeline.stop();
		timeline.play();
	}
	
	
	protected void fire()
	{
		try
		{
			action.run();
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}
}
