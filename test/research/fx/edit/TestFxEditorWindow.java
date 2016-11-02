// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;


/**
 * Test FxEditor Window.
 */
public class TestFxEditorWindow
	extends FxWindow
{
	public TestFxEditorWindow()
	{
		super("TestFxEditorWindow");
		
		FxEditorModel m =
			// new TestFxPlainEditorModel();
			new TestFxColorEditorModel();
		
		setTitle("FxEditor Test");
		setCenter(new FxEditor(m));
		setSize(600, 700);
		
		FxDump.attach(this);
	}
}
