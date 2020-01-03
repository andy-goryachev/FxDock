// Copyright © 2008-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.util.platform.CPlatformLinux;
import goryachev.common.util.platform.CPlatformMac;
import goryachev.common.util.platform.CPlatformUnix;
import goryachev.common.util.platform.CPlatformWindows;
import java.io.File;


public abstract class CPlatform
{
	protected abstract File getSettingsFolderPrivate();
	
	//
	
	protected static final String SETTINGS_FOLDER = "goryachev.com";
	private static CPlatform instance;
	

	public static CPlatform get()
	{
		if(instance == null)
		{
			instance = detect();
		}
		return instance;
	}
	
	
	public static boolean isWindows()
	{
		return (get() instanceof CPlatformWindows); 
	}
	
	
	public static boolean isMac()
	{
		return (get() instanceof CPlatformMac); 
	}
	
	
	public static boolean isLinux()
	{
		return (get() instanceof CPlatformLinux);
	}
	
	
	public static boolean isUnix()
	{
		return (get() instanceof CPlatformUnix);
	}
	
	
	public static String getName()
	{
		return System.getProperty("os.name");
	}
	
	
	public static String getVersion()
	{
		return System.getProperty("os.version");
	}
	
	
	public static File getUserHome()
	{
		return new File(System.getProperty("user.home"));
	}
	
	
	public static File getSettingsFolder()
	{
		return get().getSettingsFolderPrivate();
	}
	
	
	/** returns application settings folder. */
	@Deprecated // use getSettingsFolder()
	public File getDefaultSettingsFolder()
	{
		return new File(getUserHome(), "." + SETTINGS_FOLDER);
	}
	
	
	/*
	awt.toolkit=sun.awt.windows.WToolkit
	file.encoding.pkg=sun.io
	file.encoding=Cp1252
	file.separator=\
	java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment
	java.awt.printerjob=sun.awt.windows.WPrinterJob
	java.class.path=D:\Projects\Common\out
	java.class.version=50.0
	java.endorsed.dirs=C:\Program Files\Java\jre1.6.0_01\lib\endorsed
	java.ext.dirs=C:\Program Files\Java\jre1.6.0_01\lib\ext;C:\WINDOWS\Sun\Java\lib\ext
	java.home=C:\Program Files\Java\jre1.6.0_01
	java.io.tmpdir=C:\DOCUME~1\Andy\LOCALS~1\Temp\
	java.library.path=C:\Program Files\Java\jre1.6.0_01\bin;.;C:\WINDOWS\Sun\Java\bin;C:\WINDOWS\system32;C:\WINDOWS;c:\cygwin\bin;c:\Bin\bin;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\Program Files\ATI Technologies\ATI Control Panel;C:\Program Files\Intel\Wireless\Bin\;C:\Program Files\Java\jdk1.5.0_07\bin;C:\Program Files\Common Files\Roxio Shared\DLLShared
	java.runtime.name=Java(TM) SE Runtime Environment
	java.runtime.version=1.6.0_01-b06
	java.specification.name=Java Platform API Specification
	java.specification.vendor=Sun Microsystems Inc.
	java.specification.version=1.6
	java.vendor.url.bug=http://java.sun.com/cgi-bin/bugreport.cgi
	java.vendor.url=http://java.sun.com/
	java.vendor=Sun Microsystems Inc.
	java.version=1.6.0_01
	java.vm.info=mixed mode, sharing
	java.vm.name=Java HotSpot(TM) Client VM
	java.vm.specification.name=Java Virtual Machine Specification
	java.vm.specification.vendor=Sun Microsystems Inc.
	java.vm.specification.version=1.0
	java.vm.vendor=Sun Microsystems Inc.
	java.vm.version=1.6.0_01-b06
	line.separator=
	os.arch=x86
	os.name=Windows XP
	os.version=5.1
	path.separator=;
	sun.arch.data.model=32
	sun.boot.class.path=C:\Program Files\Java\jre1.6.0_01\lib\resources.jar;C:\Program Files\Java\jre1.6.0_01\lib\rt.jar;C:\Program Files\Java\jre1.6.0_01\lib\sunrsasign.jar;C:\Program Files\Java\jre1.6.0_01\lib\jsse.jar;C:\Program Files\Java\jre1.6.0_01\lib\jce.jar;C:\Program Files\Java\jre1.6.0_01\lib\charsets.jar;C:\Program Files\Java\jre1.6.0_01\classes
	sun.boot.library.path=C:\Program Files\Java\jre1.6.0_01\bin
	sun.cpu.endian=little
	sun.cpu.isalist=pentium_pro+mmx pentium_pro pentium+mmx pentium i486 i386 i86
	sun.desktop=windows
	sun.io.unicode.encoding=UnicodeLittle
	sun.java.launcher=SUN_STANDARD
	sun.jnu.encoding=Cp1251
	sun.management.compiler=HotSpot Client Compiler
	sun.os.patch.level=Service Pack 2
	user.country=US
	user.dir=D:\Projects\Common
	user.home=C:\Documents and Settings\Andy
	user.language=en
	user.name=Andy
	user.timezone=America/Los_Angeles
	user.variant=
	*/
	
	
	// http://lopica.sourceforge.net/os.html
	/*
	os.name          os.version                           os.arch       Comments

	AIX              5.2                                  ppc64         sun.arch.data.model=64
	AIX              4.3                                  Power
	AIX              4.1                                  POWER_RS
	Digital Unix     4                                    alpha
	FreeBSD          2.2.2-RELEASE                        x86
	HP-UX            B.10.20                              PA-RISC       JDK 1.1.x
	HP-UX            B.11.00                              PA-RISC       JDK 1.1.x
	HP-UX            B.11.11                              PA-RISC       JDK 1.1.x
	HP-UX            B.11.11                              PA_RISC       JDK 1.2.x/1.3.x; note Java 2 returns PA_RISC and Java 1 returns PA-RISC
	HP-UX            B.11.00                              PA_RISC       JDK 1.2.x/1.3.x
	HP-UX            B.11.23                              IA64N         JDK 1.4.x
	HP-UX            B.11.11                              PA_RISC2.0    JDK 1.3.x or JDK 1.4.x, when run on a PA-RISC 2.0 system
	HP-UX            B.11.11                              PA_RISC       JDK 1.2.x, even when run on a PA-RISC 2.0 system
	HP-UX            B.11.11                              PA-RISC       JDK 1.1.x, even when run on a PA-RISC 2.0 system
	Irix             6.3                                  mips
	Linux            2.0.31                               x86           IBM Java 1.3
	Linux            (*)                                  i386          Sun Java 1.3.1, 1.4 or Blackdown Java; (*) os.version depends on Linux Kernel version
	Linux            (*)                                  x86_64        Blackdown Java; note x86_64 might change to amd64; (*) os.version depends on Linux Kernel version
	Linux            (*)                                  sparc         Blackdown Java; (*) os.version depends on Linux Kernel version
	Linux            (*)                                  ppc           Blackdown Java; (*) os.version depends on Linux Kernel version
	Linux            (*)                                  armv41        Blackdown Java; (*) os.version depends on Linux Kernel version
	Linux            (*)                                  i686          GNU Java Compiler (GCJ); (*) os.version depends on Linux Kernel version
	Linux            (*)                                  ppc64         IBM Java 1.3; (*) os.version depends on Linux Kernel version
	Mac OS           7.5.1                                PowerPC
	Mac OS           8.1                                  PowerPC
	Mac OS           9.0, 9.2.2                           PowerPC       MacOS 9.0: java.version=1.1.8, mrj.version=2.2.5; MacOS 9.2.2: java.version=1.1.8 mrj.version=2.2.5
	Mac OS X         10.1.3                               ppc
	Mac OS X         10.2.6                               ppc           Java(TM) 2 Runtime Environment, Standard Edition (build 1.4.1_01-39) Java HotSpot(TM) Client VM (build 1.4.1_01-14, mixed mode)
	Mac OS X         10.2.8                               ppc           using 1.3 JVM: java.vm.version=1.3.1_03-74, mrj.version=3.3.2; using 1.4 JVM: java.vm.version=1.4.1_01-24, mrj.version=69.1
	Mac OS X         10.3.1, 10.3.2, 10.3.3, 10.3.4       ppc           JDK 1.4.x
	Mac OS X         10.3.8                               ppc           Mac OS X 10.3.8 Server; using 1.3 JVM: java.vm.version=1.3.1_03-76, mrj.version=3.3.3; using 1.4 JVM: java.vm.version=1.4.2-38; mrj.version=141.3
	MPE/iX           C.55.00                              PA-RISC
	NetWare 4.11     4.11                                 x86
	OpenVMS          V7.2-1                               alpha         Java 1.3.1_1 on OpenVMS 7.2
	OS/2             20.4                                 x86
	OS/390           390                                  02.10.00      J2RE 1.3.1 IBM OS/390 Persistent Reusable VM
	OSF1             V5.1                                 alpha         Java 1.3.1 on Compaq (now HP) Tru64 Unix V5.1
	Solaris          2.x                                  sparc
	SunOS            5.7                                  sparc         Sun Ultra 5 running Solaris 2.7
	SunOS            5.8                                  sparc         Sun Ultra 2 running Solaris 8
	SunOS            5.9                                  sparc         Java(TM) 2 Runtime Environment, Standard Edition (build 1.4.0_01-b03) Java HotSpot(TM) Client VM (build 1.4.0_01-b03, mixed mode)
	Windows 2000     5                                    x86
	Windows 2003     5.2                                  x86           java.vm.version=1.4.2_06-b03; Note, that Windows Server 2003 identifies itself only as Windows 2003.
	Windows 95       4                                    x86
	Windows 98       4.1                                  x86           Note, that if you run Sun JDK 1.2.1 or 1.2.2 Windows 98 identifies itself as Windows 95.
	Windows CE       3.0 build 11171                      arm           Compaq iPAQ 3950 (PocketPC 2002)
	Windows Me       4.9                                  x86
	Windows NT       4                                    x86
	Windows XP       5.1                                  x86           Note, that if you run older Java runtimes Windows XP identifies itself as Windows 2000.
	*/
	private static CPlatform detect()
	{
		try
		{
			String os = System.getProperty("os.name");
			if(os.startsWith("Windows"))
			{
				return new CPlatformWindows();
			}
			else if(os.startsWith("Mac OS"))
			{
				return new CPlatformMac();
			}
			else if(os.startsWith("Linux"))
			{
				return new CPlatformLinux();
			}
		}
		catch(Exception e)
		{ 
			Log.ex(e);
		}
		
		// for all practical purposes
		return new CPlatformUnix();
	}
}
