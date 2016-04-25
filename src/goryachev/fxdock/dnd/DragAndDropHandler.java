// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.D;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;


/**
 * Dock Drag and Drop handler.
 */
public class DragAndDropHandler
	implements EventHandler<MouseEvent>
{
	public DragAndDropHandler()
	{
	}
	
	
	public void attach(Node n)
	{
		n.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		n.addEventHandler(MouseEvent.DRAG_DETECTED, this);
		n.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		n.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
	}


	public void handle(MouseEvent ev)
	{
		D.print(ev);
		
		Object t = ev.getEventType(); 
		if(t == MouseEvent.MOUSE_PRESSED)
		{
		
		}
		else if(t == MouseEvent.DRAG_DETECTED)
		{
			
		}
	}
}
