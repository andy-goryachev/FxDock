// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.Application;
import goryachev.common.util.TXT;


/**
 * WelcomeWizard may dynamically change language.  The prompt translation may change at runtime,
 * which is incompatible with static nature of Menus prompts.
 */
public class WelcomePrompts
{
	public static String acceptLicense() { return TXT.get("WelcomePrompts.button.accept license", "Accept"); }
	public static String back() { return TXT.get("WelcomePrompts.button.go back", "Back"); }
	public static String browse() { return TXT.get("WelcomePrompts.button.browse files", "Browse"); }
	public static String close() { return TXT.get("WelcomePrompts.button.close", "Close"); }
	public static String licenseAgreement() { return  TXT.get("WelcomePrompts.license agreement", "License Agreement"); }
	public static String next() { return TXT.get("WelcomePrompts.next page", "Next"); }
	public static String runApplication() { return TXT.get("WelcomePrompts.run APP", "Run {0}", Application.getTitle()); }
	public static String thankYou() { return TXT.get("WelcomePrompts.thank you for dowloading APP", "Thank you for dowloading {0}!", Application.getTitle()); };
	public static String welcome() { return TXT.get("WelcomePrompts.welcome", "Welcome!"); }
	public static String welcomeToApp() { return TXT.get("WelcomePrompts.welcome to APP VERSION", "Welcome to {0} Version {1}", Application.getTitle(), Application.getVersion()); }
}
