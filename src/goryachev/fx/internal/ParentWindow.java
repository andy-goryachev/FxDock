// Copyright © 2020-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;


/**
 * This class creates a chain of listeners 
 * which monitor the parent window 'showing' status.
 * 
 * scene.window -> window -> window.showing
 * 
 * This class simplifies code when action needs to be taken when the Node
 * is added to a visible parent Window, or when that Window is closed 
 * (hidden, as there is no destroy() method in FX).
 */
public class ParentWindow
{
	private final ReadOnlyObjectWrapper<Window> windowProperty = new ReadOnlyObjectWrapper();
	private final ChangeListener<Scene> sceneListener;
	
	
	public ParentWindow(Node n)
	{
		sceneListener = new ChangeListener<Scene>()
		{
			public void changed(ObservableValue<? extends Scene> observable, Scene prev, Scene cur)
			{
				handleSceneChange(prev, cur);
			}
		};
		
		n.sceneProperty().addListener(sceneListener);
		
		Scene sc = n.getScene();
		update(sc);
	}


	protected void update(Scene sc)
	{
		if(sc == null)
		{
			windowProperty.set(null);
		}
		else
		{
			Window w = sc.getWindow();
			if(w == null)
			{
				windowProperty.set(null);
			}
			else
			{
				w.showingProperty().addListener(createShowingListener(w));
				if(w.isShowing())
				{
					windowProperty.set(w);
				}
				else
				{
					windowProperty.set(null);
				}
			}
		}
	}
	
	
	public ReadOnlyObjectProperty<Window> windowProperty()
	{
		return windowProperty.getReadOnlyProperty();
	}
	
	
	protected ChangeListener<Boolean> createShowingListener(Window w)
	{
		return new ChangeListener<Boolean>()
		{
			public void changed(ObservableValue<? extends Boolean> observable, Boolean prev, Boolean cur)
			{
				handleShowingChange(w, cur);
			}
		};
	}
	
	
	protected void handleSceneChange(Scene old, Scene cur)
	{
		update(cur);
	}
	
	
	protected void handleShowingChange(Window w, boolean showing)
	{
		if(showing)
		{
			windowProperty.set(w);
		}
		else
		{
			windowProperty.set(null);
		}
	}
}
