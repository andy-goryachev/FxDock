// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CComparator;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.common.util.SB;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Window;


/**
 * Diagnostic tool to dump FX style information with a key press.
 */
public class FxDump
{
	private final KeyCode trigger;
	private boolean showNulls;
	private double x;
	private double y;
	private PickResult r;
	private CComparator<CssMetaData<? extends Styleable,?>> sorter;

	
	public FxDump(KeyCode trigger)
	{
		this.trigger = trigger;
	}
	
	
	public FxDump()
	{
		this(KeyCode.BACK_QUOTE);
	}
	
	
	/** a shortcut to attach a default FxDump */
	public static void a(Node n)
	{
		new FxDump().attach(n);
	}
	
	
	/** a shortcut to attach a default FxDump */
	public static void a(Window n)
	{
		new FxDump().attach(n);
	}
	
	
	/** controls whether to show null properties */
	public void setShowNulls(boolean on)
	{
		showNulls = on;
	}
	
	
	public void attach(Window s)
	{
		attach(s.getScene().getRoot());
	}
	
	
	public void attach(Node n)
	{
		// I don't know how to listen to global mouse or key events in FX...
		n.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> handleKeyPress(ev));
		n.getScene().addEventFilter(MouseEvent.ANY, (ev) -> handleMouseEvent(ev));
	}


	protected void handleMouseEvent(MouseEvent ev)
	{
		x = ev.getScreenX();
		y = ev.getScreenY();
		r = ev.getPickResult();
	}
	

	protected void handleKeyPress(KeyEvent ev)
	{
		if(ev.getCode() == trigger)
		{
			dump();
		}
	}
	
	
	protected void dump()
	{
		if(r != null)
		{
			Node n = r.getIntersectedNode();
			if(n != null)
			{
				dump(n);
			}
		}
	}
	
	
	protected void sort(CList<CssMetaData<? extends Styleable,?>> list)
	{
		if(sorter == null)
		{
			sorter = new CComparator<CssMetaData<? extends Styleable,?>>()
			{
				public int compare(CssMetaData<? extends Styleable,?> a, CssMetaData<? extends Styleable,?> b)
				{
					return compareAsStrings(a.getProperty(), b.getProperty());
				}
			};
		}
		
		sorter.sort(list);
	}
	
	
	protected boolean shouldShow(Object x)
	{
		if(x == null)
		{
			return showNulls;
		}
		return true;
	}
	
	
	protected Object describe(Object x)
	{
		if(x instanceof Double)
		{
			double v = (Double)x;
			if(v == Double.MAX_VALUE)
			{
				return "MAX_VALUE";
			}
			else if(v == Double.MIN_VALUE)
			{
				return "MAX_VALUE";
			}
		}
		else if(x instanceof Double[])
		{
			Double[] v = (Double[])x;
			
			SB sb = new SB();
			sb.a("[");
			boolean sep = false;
			for(int i=0; i<v.length; i++)
			{
				if(sep)
				{
					sb.a(", ");
				}
				else
				{
					sep = true;
				}
				sb.a(v[i]);
			}
			sb.a("]");
			return sb.toString();
		}
		else if(x instanceof Background)
		{
			Background b = (Background)x;
			SB sb = new SB();
			sb.a("Background[");
			boolean sep = false;
			for(BackgroundFill f: b.getFills())
			{
				if(sep)
				{
					sb.a(",");
				}
				else
				{
					sep = true;
				}
				sb.a(describe(f.getFill()));
			}
			sb.a("]");
			return sb.toString();
		}
		
		return x;
	}
	
	
	protected void dump(Node n)
	{
		SB sb = new SB(4096);
		sb.nl();
		
		while(n != null)
		{
			sb.a(CKit.simpleName(n));
			
			String id = n.getId();
			if(CKit.isNotBlank(id))
			{
				sb.a(" #");
				sb.a(id);
			}
			
			for(String s: n.getStyleClass())
			{
				sb.a(" .").a(s);
			}
			
			for(PseudoClass c: n.getPseudoClassStates())
			{
				sb.a(" :").a(c);
			}
			
			sb.nl();
			
			CList<CssMetaData<? extends Styleable,?>> md = new CList<>(n.getCssMetaData());
			sort(md);
			
			for(CssMetaData d: md)
			{
				String k = d.getProperty();
				Object v = d.getStyleableProperty(n).getValue();
				if(shouldShow(v))
				{
					Object val = describe(v);
					sb.sp(4).a(k);
					sb.sp().a(val);
					if(d.isInherits())
					{
						sb.a(" *");
					}
					sb.nl();
				}
			}
			
			n = n.getParent();
		}
		D.print(sb);
	}
}
