// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.license.StandardLicense;
import goryachev.common.ui.text.DocumentTools;
import goryachev.common.util.CJob;
import goryachev.common.util.CKit;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CObjectProperty;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.platform.ApplicationSupport;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.text.Document;


public abstract class Application
{
	/** override to provide splashscreen image */
	protected Image createSplashImage() { return null; }
	
	/** called  after app log is initialized */
	protected void initCustomLog() { }

	/** called after user preferences are loaded */
	protected void initI18N() throws Exception { }
	
	/** called before creating main window.  In headless mode, use this method to start the headless application */
	protected abstract void initApplication() throws Exception;
	
	/** always in EDT, creates the main window */
	public abstract void openMainWindow() throws Exception;
	
	/** before exiting: return true if it is ok to exit */
	public boolean exiting() { return true; }
	
	public File getAppSettingsDirectory() { return getDefaultSettingsDirectory(); }
	
	// to be used in dialogs etc.
	public abstract String getAppTitle();

	// to be used in dialogs etc.
	public abstract ImageIcon getAppIcon();

	//
	
	public static final CAction exitAction = new CAction() { public void action() { exit(); }};

	public final String profileName;
	public final String version;
	public final String copyright;
	private static String company = "goryachev.com";
	protected Window splash;
	private static Application instance;
	private static boolean exiting;
	private static AWTEventListener awtListener;
	protected static ComponentOrientation orientation;


	public Application(String profileName, String version, String copyright)
	{
		this.profileName = profileName;
		this.version = version;
		this.copyright = copyright;
	}
	
	
	public static String getID()
	{
		return instance == null ? null : instance.profileName;
	}
	
	
	protected void loadUserPreferences()
	{
		GlobalSettings.init(getPreferencesFile());
	}
	
	
	public File getLogFolder()
	{
		return getDefaultLogFolder();
	}
	
	
	public static File getDefaultLogFolder()
	{
		return new File(Application.getSettingsDirectory(), "logs");
	}
	
	
	protected void initLogger()
	{
		File dir = getLogFolder();
		dir.mkdirs();
		
		Log.init(dir);
		
		initCustomLog();
	}
	
	
	protected void initLookAndFeel()
	{
		Theme.init();
	}
	
	
	private synchronized void setInstance()
	{
		if(instance != null)
		{
			throw new RuntimeException("Application already started");
		}
		instance = this;
	}
	

	public final void start()
	{
		setInstance();
		
		try
		{
			initLogger();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		
		long start = System.nanoTime();
		
		// splash screen
		try
		{
			UI.inEDTW(new Runnable()
			{
				public void run()
				{
					splash = createSplashScreen();
					if(splash != null)
					{
						splash.setVisible(true);
					}
				}
			});
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		try
		{
			loadUserPreferences();
		}
		catch(Exception e)
		{
			Log.err(e);
			System.exit(-1);
		}
		
		try
		{
			initI18N();
		}
		catch(Exception e)
		{
			Log.err(e);
			System.exit(-2);
		}
		
		try
		{
			initApplication();
		}
		catch(final Exception e)
		{
			// failed app init
			Log.err(e);

			try
			{
				UI.inEDTW(new Runnable()
				{
					public void run()
					{
						showError(e);
					}
				});
			}
			catch(Exception er)
			{ }
			
			System.exit(-3);
		}
		
		if(splash != null)
		{
			long t = (System.nanoTime() - start)/1000000;
			if(t < 500)
			{
				CKit.sleep((int)t);
			}
		}
		
		UI.inEDT(new Runnable()
		{
			public void run()
			{
				startUI();
			}
		});
	}
	
	
	// allows the app to start if no display is found (as in on server)
	// return true if app started, false if headless mode is not supported.
	public final void startHeadless() 
	{
		setInstance();
		
		try
		{
			initLogger();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		
		try
		{
			loadUserPreferences();
		}
		catch(Exception e)
		{
			Log.err(e);
			System.exit(-1);
		}
		
		try
		{
			initI18N();
		}
		catch(Exception e)
		{
			Log.err(e);
			System.exit(-2);
		}
		
		try
		{
			initApplication();
		}
		catch(final Exception e)
		{
			// failed app init
			Log.err(e);
			System.exit(-3);
		}
	}


	// in EDT
	protected void startUI()
	{
		try
		{
			initLookAndFeel();
			
			CFocusMonitor.init();
			
			TXT.getLanguageProperty().addListener(true, new CObjectProperty.Listener<CLanguage>()
			{
				public void onPropertyChange(final CLanguage old, final CLanguage la)
				{
					UI.inEDT(new Runnable()
					{
						public void run()
						{
							updateLanguage(old, la);
						}
					});
				}
			});
			
			openMainWindow();
			closeSplashScreen();
		}
		catch(Throwable e)
		{
			showError(e);
			System.exit(-5);
		}
	}
	
	
	// in EDT
	public void showError(Throwable e)
	{
		Log.err(e);
		
		String msg = e.getMessage();
		if(CKit.isBlank(msg))
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			msg = w.toString();
		}
		
		closeSplashScreen();
		
		// FIX
		Dialogs.startupError(getIcon(), TXT.get("Application.startup error title","Startup Error") + " - " + getTitle(), msg);
	}
	
	
	protected void closeSplashScreen()
	{
		if(splash != null)
		{
			splash.dispose();
			splash = null;
		}
	}
	
	
	public synchronized static void exit()
	{
		exiting = true;
		
		try
		{
			if(instance != null)
			{
				if(!instance.exiting())
				{
					return;
				}
				
				savePreferences();
			}
			
			if(ApplicationSupport.shutdownCJobExecutor)
			{
				// shutdown CJob executor if it has been started
				CJob.shutdown();
			}
			
			System.exit(0);
		}
		finally
		{
			exiting = false;
		}
	}
	
	
	public static boolean isExiting()
	{
		return exiting;
	}
	

	public static void savePreferences()
	{
		GlobalSettings.save();
	}
	
	
	public String preferencesFileName()
	{
		return "preferences.dat";
	}
	
	
	public File getPreferencesFile()
	{
		return new File(getSettingsDirectory(), preferencesFileName());
	}


	public static File getSettingsDirectory()
	{
		return instance.getAppSettingsDirectory();
	}


	public static File getDefaultSettingsDirectory()
	{
		File f = new File(getUserHome(), ".goryachev.com" + File.separator + instance.profileName);
		f.mkdirs();
		return f;
	}


	public static File getUserHome()
	{
		return new File(System.getProperty("user.home"));
	}
	
	
	protected Window createSplashScreen()
	{
		Image splashImage = createSplashImage();
		if(splashImage != null)
		{
			return new DefaultSplashScreen(splashImage);
		}
		else
		{
			return null;
		}
	}

	
	public static void load(Properties p, File file) throws Exception
	{
		FileInputStream in = new FileInputStream(file);
		try
		{
			p.load(in);
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	public static void save(Properties p, File file) throws Exception
	{
		FileOutputStream out = new FileOutputStream(file);
		try
		{
			p.store(out,"");
		}
		finally
		{
			CKit.close(out);
		}
	}
	

	public static ImageIcon getIcon()
	{
		return instance == null ? null : instance.getAppIcon();
	}
	
	
	public static String getTitle()
	{
		return instance == null ? null : instance.getAppTitle();
	}
	
	
	public static String getVersion()
	{
		return instance == null ? null : instance.version;
	}
	
	
	public static String getCopyright()
	{
		return instance == null ? null : instance.copyright;
	}
	
	
	public static String getCompany()
	{
		return company;
	}
	

	public static void closeAllWindow()
	{
		for(Window w: Window.getWindows())
		{
			w.dispose();
		}
	}
	
	
	public static CAction licenseAction()
	{
		return new CAction()
		{
			public void action()
			{
				StandardLicense lic = new StandardLicense();
				
				CDialog d = new CDialog(getSourceWindow(), "ApplicationLicenseDialog", true);
				d.setSize(500, 700);
				d.borderless();
				d.setCloseOnEscape();
				d.setTitle(lic.getTitle() + " - " + getTitle() + " " + getVersion());
				
				Document ld = lic.getDocument();
				d.panel().setCenter(Panels.scrollDocument(ld));

				// TODO copy rich text as well
				String s = DocumentTools.toString(ld);
				d.buttonPanel().addButton(Menus.CopyToClipboard, ClipboardTools.copyAction(s));
				d.buttonPanel().fill();
				d.buttonPanel().addButton(Menus.OK, d.closeDialogAction, true);
				
				d.open();
			}
		};
	}
	
	
	protected void updateLanguage(CLanguage old, CLanguage la)
	{
		boolean oldltr = CLanguage.isLeftToRight(old);
		boolean ltr = CLanguage.isLeftToRight(la); 
		if(ltr != oldltr)
		{
			orientation = ltr ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT;
			UI.setLeftToRightOrientation(orientation);
			
			if(awtListener == null)
			{
				// install awt listener to track events in order to set the proper orientation
				awtListener = new AWTEventListener()
				{
					public void eventDispatched(AWTEvent ev)
					{
						int id = ev.getID();
						switch(id)
						{
						case HierarchyEvent.HIERARCHY_CHANGED:
							{
								Component c = ((HierarchyEvent)ev).getComponent();
								c.applyComponentOrientation(orientation);
							}
							break;
							
						case ContainerEvent.COMPONENT_ADDED:
							{
								Component c = ((ContainerEvent)ev).getChild();
								c.applyComponentOrientation(orientation);
							}
							break;
						}
					}
				};
				
				long mask = AWTEvent.HIERARCHY_EVENT_MASK;
				
				Toolkit.getDefaultToolkit().addAWTEventListener(awtListener, mask);
			}
		}
	}
}
