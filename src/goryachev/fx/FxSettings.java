// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CPlatform;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import java.io.File;


/**
 * FxSettings.
 */
@Deprecated
public class FxSettings
{
	// TODO move to CPlatform
	public static File getUserFolder()
	{
		CPlatform p = CPlatform.get();
		return p.getDefaultSettingsFolder();
	}
}
