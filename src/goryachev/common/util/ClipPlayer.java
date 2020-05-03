// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;


public class ClipPlayer
{
	public static void play(InputStream in)
	{
		try
		{
			AudioInputStream stream = AudioSystem.getAudioInputStream(in);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			final Clip clip = (Clip)AudioSystem.getLine(info);
			clip.addLineListener(new LineListener()
			{
				public void update(LineEvent ev)
				{
					LineEvent.Type t = ev.getType();
					if(t == LineEvent.Type.STOP)
					{
						clip.close();
					}
				}
			});
			clip.open(stream);
			clip.start();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public static void playLocal(Object parent, String name)
	{
		InputStream in = parent.getClass().getResourceAsStream(name);
		play(in);
	}
}
