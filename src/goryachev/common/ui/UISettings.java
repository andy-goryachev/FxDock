// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.table.CTreeNode;
import goryachev.common.ui.table.CTreeTable;
import goryachev.common.util.CKit;
import goryachev.common.util.CSettings;
import goryachev.common.util.CSettingsProvider;
import goryachev.common.util.Hex;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import goryachev.common.util.SB;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class UISettings
	extends CSettings
{
	public static final String DIALOG = ".Dialog";
	public static final String WINDOW = ".Window";
	public static final String CHECK = ".Check";
	public static final String SPLIT = ".Split";
	public static final String TABLE = ".Table";
	public static final String TABLE_SELECTION = ".Table.S";
	public static final String TABLE_SORT = ".TableSort";
	public static final String TEXTFIELD = ".TextField";
	public static final String TREE = ".Tree";
	public static final String TREE_TABLE = ".TreeTable";
	public static final String TREE_TABLE_SELECTION = ".TreeTable.S";
	private static final String DELIMITER = "\u0000";
	
	public static final String FRAME_NORMAL = "N";
	public static final String FRAME_MINIMIZED = "M";
	public static final String FRAME_MAXIMIZED = "X";
	
	public static final char SORT_ASCENDING = 'a';
	public static final char SORT_DESCENDING = 'd';
	public static final char SORT_NONE = 'u';
	

	public UISettings()
	{
	}
	
	
	public UISettings(CSettingsProvider p)
	{
		super(p);
	}
	
	
	protected String encode(String s)
	{
		if(s != null)
		{
			try
			{
				byte[] b = s.getBytes(CKit.CHARSET_UTF8);
				return Hex.toHexString(b);
			}
			catch(Exception e)
			{ }
		}
		return "";
	}
	
	
	protected String decode(String s)
	{
		try
		{
			byte[] b = Parsers.parseByteArray(s);
			return new String(b, CKit.CHARSET_UTF8);
		}
		catch(Exception e)
		{ }
		return "";
	}
	
	
	// CTreeTable
	public void storeTreeTable(String prefix, CTreeTable t)
	{
		storeTable(prefix, t, false);
		
		try
		{
			SB sb = new SB();
			int n = t.getRowCount();
			for(int i=0; i<n; i++)
			{
				if(t.isRowExpanded(i))
				{
					if(sb.length() > 0)
					{
						sb.a(",");
					}
					sb.append(encode(getPathString(t, i)));
				}
			}
			set(prefix + TREE_TABLE, sb);
			
			String sel;
			int ix = t.getSelectedRow();
			if(ix < 0)
			{
				sel = null;
			}
			else
			{
				sel = getPathString(t, ix);
			}
			
			set(prefix + TREE_TABLE_SELECTION, sel);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	protected String getPathString(CTreeTable t, int ix)
	{
		CTreeNode nd = t.getNode(ix);
		if(nd != null)
		{
			String[] ss = nd.getPathKeys();
			SB sb = new SB();
			for(String s: ss)
			{
				if(sb.length() > 0)
				{
					sb.a(DELIMITER);
				}
				sb.a(s);
			}
			return sb.toString();
		}
		return null;
	}


	public void restoreTreeTable(String prefix, CTreeTable t)
	{
		try
		{
			String s = getProperty(prefix + TREE_TABLE);
			if(CKit.isNotBlank(s))
			{
				String[] ss = CKit.split(s, ",");
				for(String path: ss)
				{
					String decoded = decode(path);
					String[] fullPath = CKit.split(decoded, DELIMITER);
					t.expandPath(fullPath);
				}
			}
			
			s = getProperty(prefix + TREE_TABLE_SELECTION);
			if(CKit.isNotBlank(s))
			{
				String[] path = CKit.split(s, DELIMITER);
				int ix = t.findRowByPath(path);
				if(ix >= 0)
				{
					t.selectRow(ix);
				}
			}
		}
		catch(Exception e)
		{ }
		
		restoreTable(prefix, t, false);
	}
	
	
	// JTable
	// format: ...TABLE.[width,modelIndex,sort]*COLUMNS, -1
	// if sorting is enabled
	//         ...TABLE.[sort,index]*N,-1
	// sort option:
	//   'a' for ascending (number corresponds to the position in sort keys)
	//   'd' for descending
	//   'u' for unsorted
	public void storeTable(String prefix, JTable t, boolean storeSelection)
	{
		if(t.getColumnCount() > 0)
		{
			SB sb = new SB();
	
			RowSorter<?> sorter = t.getRowSorter();
			if(sorter != null)
			{
				List<? extends RowSorter.SortKey> keys = sorter.getSortKeys();
				if(keys != null)
				{
					for(RowSorter.SortKey k: keys)
					{
						switch(k.getSortOrder())
						{
						case ASCENDING: 
							sb.append(SORT_ASCENDING);
							sb.append(',');
							break;
						case DESCENDING:
							sb.append(SORT_DESCENDING);
							sb.append(',');
							break;
						default:
							sb.append(SORT_NONE);
							sb.append(',');
							break;
						}
						sb.append(k.getColumn());
						sb.append(',');
					}
					sb.append(-1);
					
					set(prefix + TABLE_SORT, sb);
					sb.setLength(0);
				}
			}
			
			Enumeration<TableColumn> en = t.getColumnModel().getColumns();
			while(en.hasMoreElements())
			{
				TableColumn col = en.nextElement();
				// width
				sb.append(col.getWidth());
				sb.append(',');
				// model index
				sb.append(col.getModelIndex());
				sb.append(',');
			}
			sb.append("-1");
			
			String s = sb.toString();
			set(prefix + TABLE, s);
		}
		
		if(storeSelection)
		{
			int ix = t.getSelectedRow();
			if(ix >= 0)
			{
				try
				{
					ix = t.convertRowIndexToModel(ix);
					set(prefix + TABLE_SELECTION, ix);
				}
				catch(Exception e)
				{ }
			}
		}
	}


	public void restoreTable(String prefix, JTable t, boolean restoreSelection)
	{
		try
		{
			String prop = getProperty(prefix + TABLE);
			if(prop != null)
			{
				STokenizer tok = new STokenizer(prop);
				TableColumnModel m = t.getColumnModel();
				
				int sz = m.getColumnCount();
				int i = 0;
				int w;
				while((w = tok.nextInt(-1)) != -1)
				{
					int modelIndex = tok.nextInt(-1);
					
					// find index of the requested column
					int ix = i;
					for(int j=i; j<sz; j++)
					{
						if(m.getColumn(j).getModelIndex() == modelIndex)
						{
							ix = j;
							break;
						}
					}
					
					if(i != ix)
					{
						m.moveColumn(ix,i);
					}

					TableColumn col = m.getColumn(i);
					col.setPreferredWidth(w);
					
					i++;
				}
			}
			
			// exception above prevents setting sorting
			RowSorter<?> sorter = t.getRowSorter();
			if(sorter != null)
			{
				prop = getProperty(prefix + TABLE_SORT);
				if(prop != null)
				{
					ArrayList<RowSorter.SortKey> keys = new ArrayList();
					STokenizer tok = new STokenizer(prop);
					try
					{
						while(tok.hasMoreTokens())
						{
							SortOrder order;
							String s = tok.nextToken();
							switch(s.charAt(0))
							{
							case SORT_ASCENDING:
								order = SortOrder.ASCENDING;
								break;
							case SORT_DESCENDING:
								order = SortOrder.DESCENDING;
								break;
							case SORT_NONE:
								order = SortOrder.UNSORTED;
								break;
							default:
								order = null;
								break;
							}
							
							if(order == null)
							{
								break;
							}
							
							int column = tok.nextInt(-1);
							if(column < 0)
							{
								break;
							}
							
							keys.add(new RowSorter.SortKey(column,order));
						}
					}
					catch(Exception e)
					{ }
					
					sorter.setSortKeys(keys);	
				}
			}
		}
		catch(Exception e)
		{ }
		
		try
		{
			if(restoreSelection)
			{
				int ix = getInt(prefix + TABLE_SELECTION, -1);
				if(ix >= 0)
				{
					ix = t.convertRowIndexToView(ix);
					if(ix >= 0)
					{
						t.changeSelection(ix, 0, false, false);
					}
				}
			}
		}
		catch(Exception e)
		{ }
	}
	
	
	// JCheckBox
	public void storeCheckbox(String prefix, JCheckBox c)
	{
		set(prefix + CHECK, c.isSelected());
	}
	
	
	public void restoreCheckbox(String prefix, JCheckBox c)
	{
		String s = getProperty(prefix + CHECK);
		c.setSelected("true".equalsIgnoreCase(s));
	}
	
	
	// JTextField
	public void storeTextField(String prefix, JTextField c)
	{
		set(prefix + TEXTFIELD, c.getText());
	}
	
	
	public void restoreTextField(String prefix, JTextField c)
	{
		String s = getProperty(prefix + TEXTFIELD);
		c.setText(s);
	}
	
	
	// JTree
	public void storeTree(String prefix, JTree tree)
	{
		try
		{
			SB sb = new SB();
			int n = tree.getRowCount();
			for(int i=0; i<n; i++)
			{
				// TODO by name?
				sb.append(tree.isExpanded(i) ? '1' : '0');
			}		
			set(prefix + TREE, sb);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public void restoreTree(String prefix, JTree tree)
	{
		try
		{
			String s = getProperty(prefix + TREE);
			if(s != null)
			{
				int n = s.length();
				for(int i=0; i<n; i++)
				{
					if(s.charAt(i) == '1')
					{
						tree.expandRow(i);
					}
				}
			}
		}
		catch(Exception e)
		{ }
	}
	
	
	// JSplitPane
	public void restoreSplitPane(String prefix, JSplitPane c)
	{
		try
		{
			STokenizer tok = new STokenizer(getProperty(prefix + SPLIT));
			int div = tok.nextInt(-1);
			if(div > 0)
			{
				int wid = tok.nextInt(-1);
				if(isRTL(c))
				{
					if(wid > 0)
					{
						div = wid - div;
						c.setSize(wid, c.getHeight());
					}
				}
				
				c.setDividerLocation(div);
			}
		}
		catch(Exception e)
		{ }
	}

	
	public void storeSplitPane(String prefix, JSplitPane c)
	{
		int div = c.getDividerLocation();
		int wid = c.getWidth();
		
		if(isRTL(c))
		{
			div = wid - div;
		}
		
		set(prefix + SPLIT, div + "," + wid);		
	}
	
	
	// make the divider location compatible between different component orientations
	protected boolean isRTL(JSplitPane c)
	{
		if(c.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
		{
			return !c.getComponentOrientation().isLeftToRight();
		}
		return false;
	}
	
	
	// Window
	// x,y,w,h,{maximized,minimized}
	public void storeWindow(String prefix, Window win)
	{
		if(win instanceof Dialog)
		{
			if(!((Dialog)win).isResizable())
			{
				return;
			}
		}
		
		int x = win.getX();
		int y = win.getY();
		int w = win.getWidth();
		int h = win.getHeight();
		
		String state = FRAME_NORMAL;
		if(win instanceof Frame)
		{
			Frame f = (Frame)win;
			switch(f.getExtendedState())
			{
			case Frame.MAXIMIZED_BOTH:
				state = FRAME_MAXIMIZED;
				break;
			case Frame.ICONIFIED:
				state = FRAME_MINIMIZED;
			}
		}
		
		if(win instanceof AppFrame)
		{
			// size before maximized
			Rectangle r = ((AppFrame)win).getNormalSize();
			if(r != null)
			{
				if(state == FRAME_MAXIMIZED)
				{
					x = r.x;
					y = r.y;
					w = r.width;
					h = r.height;
				}
			}
		}
		
		set(prefix + WINDOW, x + "," + y + "," + w + "," + h + "," + state);
	}
	
	
	public void restoreWindow(String prefix, Window win)
	{
		try
		{
			STokenizer tok = new STokenizer(getProperty(prefix + WINDOW));
			int x = tok.nextInt(-1);
			int y = tok.nextInt(-1);
			int w = tok.nextInt(-1);
			int h = tok.nextInt(-1);
			String state = tok.nextString(FRAME_NORMAL);
			
			if((w < 0) || (h < 0))
			{
				if((win.getWidth() > 0) && (win.getHeight() > 0))
				{
					// size set already, position at the center
					UI.center(win);
					return;
				}
				else
				{
					// window natural size and center
					if(isResizable(win))
					{
						win.pack();
					}
					UI.center(win);
					return;
				}
			}
			else
			{
				// keep unresizable frame dimensions
				if(!isResizable(win))
				{
					w = win.getWidth();
					h = win.getHeight();
				}
				
				Rectangle r = new Rectangle(x,y,w,h);
				
				// check if it will be at least partially visible
				boolean partiallyVisible = false;
				GraphicsDevice[] ds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
				for(GraphicsDevice dev: ds)
				{
					Rectangle screen = dev.getDefaultConfiguration().getBounds();
					if(r.intersects(screen))
					{
						partiallyVisible = true;
						break;
					}
				}

				if(!partiallyVisible)
				{
					// show on main screen and resize if necessary
					Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
					if(x < screen.x)
					{
						x = screen.x;
					}
					if(y < screen.y)
					{
						y = screen.y;
					}
					if(x + w > screen.width)
					{
						x = screen.width - w;
						if(x < screen.x)
						{
							w = screen.width;
							x = screen.x;
						}
					}
					if(y + h > screen.height)
					{
						y = screen.height - h;
						if(y < screen.y)
						{
							w = screen.height;
							y = screen.y;
						}
					}
				}
				
				if(isResizable(win))
				{
					win.setBounds(x,y,w,h);
					
					if(win instanceof AppFrame)
					{
						((AppFrame)win).setNormalBounds(x,y,w,h);
					}
				}
				else
				{
					win.setLocation(x,y);
				}
				
				if(win instanceof Frame)
				{
					Frame f = (Frame)win;
					if(FRAME_MAXIMIZED.equals(state))
					{
						f.setExtendedState(Frame.MAXIMIZED_BOTH);
					}
					else if(FRAME_MINIMIZED.equals(state))
					{
						f.setExtendedState(Frame.ICONIFIED);
					}
				}
			}
		}
		catch(Exception e)
		{ }
	}
	
	
	protected boolean isResizable(Window w)
	{
		if(w instanceof Frame)
		{
			return ((Frame)w).isResizable();
		}
		else if(w instanceof Dialog)
		{
			return ((Dialog)w).isResizable();
		}
		else
		{
			return true;
		}
	}
	
	
	// Dialog
	// w,h
	public void storeDialog(String prefix, Dialog win)
	{
		int w = win.getWidth();
		int h = win.getHeight();
		
		set(prefix + DIALOG, w + "," + h);
	}
	
	
	public void restoreDialog(String prefix, Dialog win)
	{
		try
		{
			STokenizer tok = new STokenizer(getProperty(prefix + DIALOG));
			int w = tok.nextInt(-1);
			int h = tok.nextInt(-1);
			//String state = tok.nextString(FRAME_NORMAL);
			
			if(w < 0)
			{
				w = win.getWidth();
			}
			
			if(h < 0)
			{
				h = win.getHeight();
			}
			
			UI.center(win, win.getParent(), w, h);
		}
		catch(Exception e)
		{ }
	}
	
	
	//
	
	
	public static class STokenizer extends StringTokenizer
	{
		public STokenizer(String s)
		{
			super(s == null ? "" : s,",");
		}
		
		
		public int nextInt(int defaultValue)
		{
			if(hasMoreTokens())
			{
				return Integer.parseInt(nextToken());
			}
			else
			{
				return defaultValue;
			}
		}


		public String nextString(String defaultValue)
		{
			if(hasMoreTokens())
			{
				return nextToken();
			}
			else
			{
				return defaultValue;
			}
		}
	}
}
