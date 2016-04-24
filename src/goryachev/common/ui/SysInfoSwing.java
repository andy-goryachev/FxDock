// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.text.CDocument;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.util.CComparator;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.common.util.platform.SysInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class SysInfoSwing
	extends SysInfo
{
	public SysInfoSwing(Out out)
	{
		super(out);
	}
	
	
	/** generates system information report as text string */
	public static String getSystemInfo()
	{
		return getSystemInfo(false);
	}
	
	
	/** generates system information report as text string, with or without ui defaults part */
	public static String getSystemInfo(boolean ui)
	{
		StringOut out = new StringOut();
		SysInfoSwing s = new SysInfoSwing(out);
		
		s.extractApp();
		s.extractGraphics();
		s.extractEnvironment();
		s.extractSystemProperties();
		
		if(ui)
		{
			s.extractUIDefaults("UIManager.getLookAndFeelDefaults", null);
		}
		
		return out.getReport();
	}
	
	
	protected void header(String title)
	{
		out.header(title);
	}
	
	
	protected void nl()
	{
		out.nl();
	}
	
	
	protected void print(String x)
	{
		print(1, x);
	}
	
	
	protected void print(int indents, String x)
	{
		out.print(indents, x);
	}
	
	
	protected String number(Object x)
	{
		return numberFormat.format(x);
	}
	
	
	protected String safe(String s)
	{
		if(s != null)
		{
			boolean notSafe = false;
			int sz = s.length();
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				if(c < 0x20)
				{
					notSafe = true;
					break;
				}
			}
			
			if(notSafe)
			{
				SB sb = new SB(sz);
				for(int i=0; i<sz; i++)
				{
					char c = s.charAt(i);
					if(c < 0x20)
					{
						sb.a(unicode(c));
					}
					else
					{
						sb.a(c);
					}
				}
				s = sb.toString();
			}
		}
		return s;
	}
	
	
	protected static String unicode(char c)
	{
		return "\\u" + Hex.toHexString(c, 4);
	}
	
	
	public void extractApp()
	{
		header("Application");
		if(CKit.isNotBlank(Application.getTitle()))
		{
			print("Title: " + Application.getTitle());
			print("Version: " + Application.getVersion());
		}
		
		print("Time: " + new SimpleDateFormat("yyyy-MMdd HH:mm:ss").format(System.currentTimeMillis()));
		
		long max = Runtime.getRuntime().maxMemory();
		long free = max - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		
		print("Available Memory: " + number(max));
		print("Free Memory:" + number(free));
		nl();
	}

	
	public void extractEnvironment()
	{
		header("Environment");
		
		Map<String,String> env = System.getenv();
		CList<String> keys = new CList(env.keySet());
		CSorter.sort(keys);
		for(String key: keys)
		{
			print(key + " = " + safe(env.get(key)));
		}
		nl();
	}
	
	
	public void extractSystemProperties()
	{
		header("System Properties");
		
		Properties p = System.getProperties();
		CList<String> keys = new CList(p.stringPropertyNames());
		CSorter.sort(keys);
		for(String key: keys)
		{
			print(key + " = " + safe(p.getProperty(key)));
		}
		nl();
	}
	

	public void extractUIDefaults(String name, UIDefaults defs)
	{
		header(name);

		try
		{
			if(defs == null)
			{
				defs = UIManager.getLookAndFeelDefaults();
			}

			CList<Object> keys = new CList(defs.keySet());
			new CComparator<Object>()
			{
				public int compare(Object a, Object b)
				{
					return compareText(a, b);
				}
			}.sort(keys);

			for(Object key: keys)
			{
				Object v = defs.get(key);
				out.describe(key, v);
			}
		}
		catch(Throwable e)
		{
			print(CKit.stackTrace(e));
		}
		
		nl();
	}
	
	
	protected void sc(String name, Color c)
	{
		out.describe(name, c);
	}
	
	
	public void extractSystemColors()
	{
		header("System Colors");
		
	    sc("activeCaption", SystemColor.activeCaption);
	    sc("activeCaptionText", SystemColor.activeCaptionText);
	    sc("activeCaptionBorder", SystemColor.activeCaptionBorder);
	    sc("control", SystemColor.control);
	    sc("controlDkShadow", SystemColor.controlDkShadow);
	    sc("controlHighlight", SystemColor.controlHighlight);
	    sc("controlLtHighlight", SystemColor.controlLtHighlight);
	    sc("controlShadow", SystemColor.controlShadow);
	    sc("controlText", SystemColor.controlText);
		sc("desktop", SystemColor.desktop);
	    sc("inactiveCaption", SystemColor.inactiveCaption);
	    sc("inactiveCaptionText", SystemColor.inactiveCaptionText);
	    sc("inactiveCaptionBorder", SystemColor.inactiveCaptionBorder);
	    sc("info", SystemColor.info);
	    sc("infoText", SystemColor.infoText);
	    sc("menu", SystemColor.menu);
	    sc("menuText", SystemColor.menuText);
	    sc("scrollbar", SystemColor.scrollbar);
	    sc("text", SystemColor.text);
	    sc("textHighlight", SystemColor.textHighlight);
	    sc("textHighlightText", SystemColor.textHighlightText);
	    sc("textInactiveText", SystemColor.textInactiveText);
	    sc("textText", SystemColor.textText);
	    sc("window", SystemColor.window);
	    sc("windowBorder", SystemColor.windowBorder);
	    sc("windowText", SystemColor.windowText);
		
		nl();
	}
	

	public void extractGraphics()
	{
		try
		{
			header("Graphics");

			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			for(GraphicsDevice dev: env.getScreenDevices())
			{
				print(dev.getIDstring() + ":");

				for(GraphicsConfiguration gc: dev.getConfigurations())
				{
					Rectangle r = gc.getBounds();
					print(2, r.width + "x" + r.height + " @(" + r.x + "," + r.y + ")");
				}
			}
		}
		catch(Throwable ignore)
		{
		}
		
		nl();
	}
	
	
	public void extractSecurity()
	{
		header("Security");
		
		listSecurityAlgorithms("Cipher");
		listSecurityAlgorithms("KeyStore");
		listSecurityAlgorithms("Mac");
		listSecurityAlgorithms("MessageDigest");
		listSecurityAlgorithms("Signature");
		
		nl();
	}
	
	
	protected void listSecurityAlgorithms(String name)
	{
		print(name);

		try
		{
			CList<String> names = new CList(Security.getAlgorithms(name));
			CSorter.sort(names);
			
			for(String s: names)
			{
				print(2, s);
			}
		}
		catch(Exception e)
		{
			print(CKit.stackTrace(e));
		}
	}
	
	
	//
	
	
//	public abstract static class Out2 // FIX kill
//	{
//		public abstract void header(String title);
//		
//		public abstract void nl();
//		
//		public abstract void print(int count, String x);
//		
//		public abstract void describe(Object key, Object v);
//		
//		protected abstract Out a(Object x);
//		
//		//
//		
//
//		protected void describe(Object x)
//		{
//			if(x == null)
//			{
//			}
//			else if(x instanceof String)
//			{
//				a('"');
//				a(x);
//				a('"');
//			}
//			else if(x instanceof Color)
//			{
//				a(x.getClass().getSimpleName());
//				describeColor((Color)x);
//			}
//			else if(x instanceof Font)
//			{
//				a(x.getClass().getSimpleName());				
//				describeFont((Font)x);
//			}
//			else if(x instanceof Dimension)
//			{
//				a(x.getClass().getSimpleName());
//				
//				Dimension d = (Dimension)x;
//				a("(w=").a(d.getWidth());
//				a(",h=").a(d.getHeight());
//				a(")");
//			}
//			else if(x instanceof Insets)
//			{
//				a(x.getClass().getSimpleName());
//				describeInsets((Insets)x);
//			}
//			else if(x instanceof Icon)
//			{
//				a(x.getClass().getSimpleName());
//				describeIcon((Icon)x);
//			}
//			else if(x instanceof Border)
//			{
//				a(x.getClass().getName());
//				describeBorder((Border)x);
//			}
//			else if(x instanceof InputMap)
//			{
//				a(x.getClass().getSimpleName());
//			}
//			else if(x instanceof ActionMap)
//			{
//				a(x.getClass().getSimpleName());
//			}
//			else if(x instanceof Component)
//			{
//				a(x.getClass().getName());
//			}
//			else if(x instanceof Object[])
//			{
//				Object[] a = (Object[])x;
//				a("Object[");
//				a(a.length);
//				a("]");
//			}
//			else if(x instanceof int[])
//			{
//				int[] a = (int[])x;
//				a("int[");
//				a(a.length);
//				a("]");
//			}
//			else
//			{
//				a(x);
//			}
//		}
//	}
	
	
	//
	
	
	public static class DocumentOut
		extends Out
	{
		private final CDocumentBuilder b;
		private final Font font;
		private String indent = "    ";

		
		public DocumentOut()
		{
			b = new CDocumentBuilder();
			font = Theme.monospacedFont();
			b.setFont(font);
		}
		
		
		public void header(String title)
		{
			b.setUnderline(true);
			b.bold(title);
			b.setUnderline(false);
			b.nl();
		}
		
		
		public void nl()
		{
			b.nl();
		}
		
		
		public void print(int count, String x)
		{
			for(int i=0; i<count; i++)
			{
				b.a(indent);
			}
			b.append(x);		
			b.nl();
		}
		
		
		protected void describe(Object x)
		{
			if(x instanceof Color)
			{
				a(x.getClass().getSimpleName());
				describeColor((Color)x);
			}
			else if(x instanceof Font)
			{
				a(x.getClass().getSimpleName());				
				describeFont((Font)x);
			}
			else if(x instanceof Dimension)
			{
				a(x.getClass().getSimpleName());
				
				Dimension d = (Dimension)x;
				a("(w=").a(d.getWidth());
				a(",h=").a(d.getHeight());
				a(")");
			}
			else if(x instanceof Insets)
			{
				a(x.getClass().getSimpleName());
				describeInsets((Insets)x);
			}
			else if(x instanceof Icon)
			{
				a(x.getClass().getSimpleName());
				describeIcon((Icon)x);
			}
			else if(x instanceof Border)
			{
				a(x.getClass().getName());
				describeBorder((Border)x);
			}
			else if(x instanceof InputMap)
			{
				a(x.getClass().getSimpleName());
			}
			else if(x instanceof ActionMap)
			{
				a(x.getClass().getSimpleName());
			}
			else if(x instanceof Component)
			{
				a(x.getClass().getName());
			}
			else
			{
				super.describe(x);
			}
		}
	
		public void describe(Object key, Object v)
		{
			b.a(indent);
			b.a(key);
			b.a(" = ");
			describe(v);
			b.nl();
		}
		
		
		public Out a(Object x)
		{
			b.a(x);
			return this;
		}
		
		
		protected void describeInsets(Insets m)
		{
			a("(t=").a(m.top);
			a(",l=").a(m.left);
			a(",b=").a(m.bottom);
			a(",r=").a(m.right);
			a(")");
		}

		
		protected void describeColor(Color c)
		{
			a("(");
			a(Hex.toHexByte(c.getRed()));
			a(Hex.toHexByte(c.getGreen()));
			a(Hex.toHexByte(c.getBlue()));
			
			if(c.getAlpha() != 255)
			{
				a(".");
				a(Hex.toHexByte(c.getAlpha()));
			}
			
			a(")");
			
			nl();
			a(indent);
			a(indent);

			JLabel t = new JLabel("   ");
			t.setOpaque(true);
			t.setBorder(new CBorder(Theme.TEXT_FG));
			t.setBackground(c);
			t.setAlignmentY(1.0f);
			b.addComponent(t);
		}
		
		
		protected void describeFont(Font f)
		{
			a("(");
			a(f.getFamily());
			
			if(CKit.notEquals(f.getFamily(), f.getName()))
			{
				a("/");
				a(f.getName());
			}
			
			a(",");
			a(f.getSize());
			a(",");

			if(f.isBold())
			{
				if(f.isItalic())
				{
					a("bolditalic");
				}
				else
				{
					a("bold");
				}
			}
			else
			{
				if(f.isItalic())
				{
					a("italic");
				}
				else
				{
					a("plain");
				}
			}
			
			a(")");

			b.nl();
			
			a(indent);
			a(indent);
			
			JLabel t = new JLabel("The quick brown fox jumped over the lazy dog 0123456789.");
			t.setFont(f);
			t.setOpaque(true);
			t.setForeground(Color.black);
			t.setBackground(UI.mix(Color.yellow, 0.2, Color.white));
			t.setBorder(new CBorder(1));
			b.addComponent(t);
		}
		
		
		protected void describeIcon(Icon ic)
		{
			a("(w=").a(ic.getIconWidth());
			a(",h=").a(ic.getIconHeight());
			a(")");
			
			nl();
			a(indent);
			a(indent);
			
			JLabel t = new JLabel(ic)
			{
				public void paint(Graphics g)
				{
					try
					{
						super.paint(g);
					}
					catch(ClassCastException e)
					{
						setIcon(null);
						setOpaque(true);
						setBackground(UI.mix(Color.white, 0.5, Color.magenta));
						setForeground(Color.black);
						setText(e.getMessage());
					}
				}
			};
			b.addComponent(t);
		}
		
		
		protected void describeBorder(Border border)
		{			
			describeInsets(border.getBorderInsets(new JLabel()));
			
			nl();
			a(indent);
			a(indent);
			
			JLabel t = new JLabel("   ")
			{
				public void paint(Graphics g)
				{
					try
					{
						super.paint(g);
					}
					catch(ClassCastException e)
					{
						setIcon(null);
						setOpaque(true);
						setBackground(UI.mix(Color.white, 0.5, Color.magenta));
						setForeground(Color.black);
						setText(e.getMessage());
					}
				}
			};
			t.setOpaque(true);
			t.setBorder(border);
			t.setBackground(Theme.TEXT_BG);
			b.addComponent(t);
		}
		
		
		public CDocument getReport()
		{
			return b.getDocument();
		}
	}
}
