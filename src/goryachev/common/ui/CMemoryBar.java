// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import goryachev.common.util.platform.SysInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;


public class CMemoryBar
	extends JLabel
	implements CAnimator.Client
{
	public final CAction gcAction = new CAction() { public void action() { actionGC(); } };
	public final CAction sysInfoAction = new CAction() { public void action() { actionSysInfo(); } };
	
	private boolean startAnimator = true;
	private int period = 750;
	private String usedText;
	private String totalText;
	private String maxText;
	private float usedFraction;
	private float totalFraction;
	private Insets margins = new Insets(0,0,0,0);
	
	private Color usedColor = new Color(0,0,0,50);
	private Color totalColor = new Color(0,0,0,20);
	private Color freeColor = new Color(255,255,255,30);
	
	private DecimalFormat wholeFormat = new DecimalFormat("##0");
	private DecimalFormat fractionFormat = new DecimalFormat("#0.0");
	public static final long ONE_K = 1024;
	public static final long TEN_K = 10 * ONE_K;
	public static final long ONE_MEG = ONE_K * ONE_K;
	public static final long TEN_MEG = 10 * ONE_MEG;
	public static final long ONE_GIG = ONE_MEG * ONE_K;
	public static final long TEN_GIG = 10 * ONE_GIG;
	

	
	public CMemoryBar()
	{
		updateText();
		setForeground(Color.gray);
		setHorizontalAlignment(CENTER);
		setBorder(new CBorder(Color.gray));
		setPreferredSize(new Dimension(75, -1));

		new CPopupMenuController(this)
		{
			public JPopupMenu constructPopupMenu()
			{
				return createPopupMenu();
			}

			public void onDoubleClick()
			{
				actionGC();
			}
		};
	}


	public JPopupMenu createPopupMenu()
	{
		CPopupMenu m = new CPopupMenu();
		m.add(new CMenuItem(TXT.get("CMemoryBar.run memory garbage collector", "Run Garbage Collector (double click)"), gcAction));
		m.addSeparator();
		m.add(new CMenuItem(TXT.get("CMemoryBar.show system info", "System Information"), sysInfoAction));
		m.add(TXT.get("CMemoryBar.show error log", "Error Log"));
		return m;
	}


	public void setUsedColor(Color c)
	{
		usedColor = c;
	}
	
	
	public void setTotalColor(Color c)
	{
		totalColor = c;
	}
	
	
	public void setFreeColor(Color c)
	{
		freeColor = c;
	}
	
	
	public void setPeriod(int ms)
	{
		period = ms;
	}
	
	
	public void setPreferredWidth(int w)
	{
		setPreferredSize(new Dimension(w,-1));
	}

	
	public boolean isOpaque()
	{
		return false;
	}
	
	
	public void nextFrame()
	{
		updateText();
		repaint();
	}
	
	
	public boolean displayText()
	{
		return false;
	}
	
	
	protected void updateText()
	{
		long total = Runtime.getRuntime().totalMemory();
		long used = total - Runtime.getRuntime().freeMemory();
		long max = Runtime.getRuntime().maxMemory();
		
		usedText = formatNumber(used);
		totalText = formatNumber(total);
		maxText = formatNumber(max);
		
		// seems to be the same on mac os x
		if(displayText())
		{
			if(CKit.equals(totalText, maxText))
			{
				setText(usedText + " / " + totalText);
			}
			else
			{
				setText(usedText + " / " + totalText + " / " + maxText);
			}
		}
		
		usedFraction = used / (float)max;
		totalFraction = total / (float)max;
		
		String tooltip = 
			"<html>" + 
			TXT.get("CMemoryBar.used ram", "Used memory: {0}", usedText) + "<br>" +
			TXT.get("CMemoryBar.total ram", "Allocated: {0}", totalText) + "<br>" +
			TXT.get("CMemoryBar.max ram", "Maximum: {0}", maxText) + "<br>" +
			"<i>" +
			TXT.get("CMemoryBar.for menu", "Right click for menu");
		
		setToolTipText(tooltip);
	}
	
	
	protected String formatNumber(long x)
	{
		String unit;
		DecimalFormat f;
		long div;
		if(x >= ONE_GIG)
		{
			div = ONE_GIG;
			unit = TXT.get("CMemoryBar.gigabytes","GB");
			if(x >= TEN_GIG)
			{
				f = wholeFormat;
			}
			else
			{
				f = fractionFormat;
			} 
		}
		else //if(x >= ONE_MEG)
		{
			div = ONE_MEG;
			unit = TXT.get("CMemoryBar.megabytes","MB");
			if(x >= TEN_MEG)
			{
				f = wholeFormat;
			}
			else
			{
				f = fractionFormat;
			}
		}
//		else if(x >= ONE_K)
//		{
//			div = ONE_K;
//			unit = TXT.get("CMemoryBar.kilobytes","K");
//			if(x >= TEN_K)
//			{
//				f = wholeFormat;
//			}
//			else 
//			{
//				f = fractionFormat;
//			}
//		}
//		else
//		{
//			div = 1;
//			unit = "";
//			f = wholeFormat;
//		}
		
		if(f == fractionFormat)
		{
			return f.format(x / (float)div) + " " + unit;
		}
		else
		{
			return f.format(x / div) + " " + unit;
		}
	}
	
	
	public void paintComponent(Graphics g)
	{
		Insets m = getInsets(margins);
		int left = m.left;
		int top = m.top;
		int w = getWidth() - left - m.right;
		int h = getHeight() - top - m.bottom;
		
		int x0 = Math.round(usedFraction * w);
		int x1 = Math.round(totalFraction * w);
		
		if(x0 != 0)
		{
			g.setColor(usedColor);
			g.fillRect(left, top, x0, h);
		}

		int dx = x1 - x0;
		if(dx > 0)
		{
			g.setColor(totalColor);
			g.fillRect(left + x0, top, dx, h);
		}

		if(freeColor != null)
		{
			dx = w - x1;
			if(dx > 0)
			{
				g.setColor(freeColor);
				g.fillRect(left + x1, top, dx, h);
			}
		}
		
		super.paintComponent(g);

		if(startAnimator)
		{
			initHandler();
			startAnimator = false;
		}
	}
	
	
	protected void initHandler()
	{
		new CAnimator(this, period);
	}
	
	
	protected void actionGC()
	{
		System.gc();
		System.runFinalization();
		System.gc();
	}
	
	
	protected void actionSysInfo()
	{
		String text = SysInfo.getSystemInfo();
		
		CDialog d = new CDialog(this, "SystemInformationDialog", true);
		d.borderless();
		d.setSize(700, 750);
		d.setTitle(TXT.get("CMemoryBar.system information","System Information"));

		d.panel().setCenter(Panels.scrollText(text));
		
		d.buttonPanel().addButton(Menus.CopyToClipboard, ClipboardTools.copyAction(text));
		d.buttonPanel().fill();
		d.buttonPanel().add(new CButton(Menus.OK, d.closeDialogAction, true));
		d.setCloseOnEscape();
		d.open();
	}
}
