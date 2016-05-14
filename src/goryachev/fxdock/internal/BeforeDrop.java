// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.CMap;
import goryachev.fx.FxSize;
import javafx.scene.Node;
import javafx.scene.layout.Region;


/**
 * BeforeDrop.
 */
public class BeforeDrop
{
	public final Node client;
	public final Node clientParent;
	private final CMap<Node,Object> data = new CMap<>();
	
	//
	
	class SplitSize
	{
		public int childCount;
		public double[] dividers;
	}
	
	//
	
	public BeforeDrop(Node client, Node target)
	{
		this.client = client;
		this.clientParent = DockTools.getParent(client);
		
		collectSizes(client);
		collectSizes(target);
	}
	
	
	protected void collectSizes(Node n)
	{
		while(n != null)
		{
			if(n instanceof FxDockSplitPane)
			{
				sizeSplit((FxDockSplitPane)n);
			}
			else if(n instanceof FxDockRootPane)
			{
				return;
			}
			else
			{
				sizeNode(n);
			}
			
			n = DockTools.getParent(n);
		}
	}
	
	
	protected void sizeSplit(FxDockSplitPane p)
	{
		SplitSize s = new SplitSize();
		s.childCount = p.getPaneCount();
		s.dividers = p.getDividerPositions();
		data.put(p, s);
	}
	
	
	protected void sizeNode(Node n)
	{
		if(n instanceof Region)
		{
			Region r = (Region)n;
			data.put(n, new FxSize(r.getWidth(), r.getHeight()));
		}
	}
	
	
	public void adjustSplits()
	{
		adjustSplits(client);
	}
	
	
	protected void adjustSplits(Node n)
	{
		while(n != null)
		{
			if(n instanceof FxDockSplitPane)
			{
				restoreSplits((FxDockSplitPane)n);
			}
			else if(n instanceof FxDockRootPane)
			{
				return;
			}
			
			n = DockTools.getParent(n);
		}
	}


	// for now, simply allocate space equally between panes in the affected split panes
	protected void restoreSplits(FxDockSplitPane p)
	{
		Object x = data.get(p);
		if(x == null)
		{
			allocateEqually(p);
		}
		else
		{
			SplitSize s = (SplitSize)x;
			if(s.childCount == p.getPaneCount())
			{
				// restore previous configuration
				p.setDividerPositions(s.dividers);
			}
			else
			{
				allocateEqually(p);
			}
		}
	}


	protected void allocateEqually(FxDockSplitPane p)
	{
		int sz = p.getDividers().size();
		double total = (sz + 1.0);
		
		for(int i=0; i<sz; i++)
		{
			double pos = (i + 1) / total;
			p.setDividerPosition(i, pos);
		}
	}
}
