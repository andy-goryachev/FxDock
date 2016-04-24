// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CSplitPane;
import goryachev.common.ui.CTextFieldWithPrompt;
import goryachev.common.ui.DelayedAction;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.options.OptionEditorInterface;
import goryachev.common.ui.table.CTableColumn;
import goryachev.common.ui.table.CTreeTable;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.TXT;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class OptionPanel
	extends CPanel
{
	public final CTextFieldWithPrompt filter;
	public final JLabel buttonLabel;
	public final CPanel filterPanel;
	public final CTreeTable<OptionTreeNode> tree;
	public final CPanel detailPanel;
	public final JLabel titleField;
	public final CSplitPane split;
	public final DelayedAction delayed;
	private OptionTreeNode root;
	
	
	public OptionPanel(OptionTreeNode r)
	{
		this.root = r;
		
		delayed = new DelayedAction()
		{
			public void action()
			{
				onSearch();
			}
		};
		
		filter = new CTextFieldWithPrompt();
		filter.setPrompt(Menus.find);
		filter.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent ev)
			{
				delayed.fire();
			}
		});
		UI.whenFocused(filter, KeyEvent.VK_ESCAPE, new CAction()
		{
			public void action() throws Exception
			{
				actionClearFilter();
			}
		});
		
		buttonLabel = new JLabel();
		buttonLabel.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev)
			{
				actionClearFilter();
			}
		});

		filterPanel = new CPanel();
		filterPanel.setCenter(filter);
		filterPanel.setEast(buttonLabel);
		filterPanel.setBorder(new CBorder(2, 2, 2, 2));
		filterPanel.setBackground(Theme.PANEL_BG);
		
		tree = new CTreeTable<OptionTreeNode>();
		tree.setRoot(root, false);
		tree.addColumn(new CTableColumn<OptionTreeNode>("")
		{
			public String getText(OptionTreeNode d) { return d.getName(); }
			public void configureRenderer(OptionTreeNode d) { setIcon(d.getIcon()); }
		});
		tree.setTableHeader(null);
		tree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tree.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent ev)
			{
				if(!ev.getValueIsAdjusting())
				{
					onSelectionChange();
				}
			}
		});
		
		titleField = new JLabel(" ");
		titleField.setBorder(new CBorder(0, 0, 1, 0, Theme.LINE_COLOR, 10));
		titleField.setFont(Theme.titleFont());
		titleField.setBackground(Theme.PANEL_BG);
		titleField.setOpaque(true);

		detailPanel = new CPanel();
		detailPanel.setNorth(titleField);
		
		CScrollPane scroll = new CScrollPane(tree, CScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, CScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportBorder(new CBorder());
		scroll.setBackground2(Theme.FIELD_BG);
		
		CPanel left = new CPanel();
		left.setNorth(filterPanel);
		left.setCenter(scroll);
		left.setPreferredSize(new Dimension(200, -1));

		split = new CSplitPane(true, left, detailPanel);
		split.setBorder(new CBorder(0, 0, 1, 0, Theme.LINE_COLOR));
		
		setCenter(split);
		
		autoResizeSplit();
	}
	
	
	public void setRoot(OptionTreeNode root)
	{
		this.root = root;
		tree.setRoot(root, false);
	}
	
	
	public void autoResizeSplit()
	{
		// FIX
		//int w = tree.getColumnsPreferredWidth();
		//tree.setPreferredSize(new Dimension(w, -1));
		//split.setDividerLocation(w);
	}
	
	
	protected void actionClearFilter()
	{
		if(CKit.isNotBlank(filter.getText()))
		{
			filter.clear();
			delayed.action();
		}
	}
	
	
	protected void commitRecursively(OptionTreeNode nd, AtomicBoolean store, AtomicBoolean restart) throws Exception
	{
		for(OptionEntry en: nd.getOptionEntries())
		{
			if(en.isModified())
			{
				en.commit();
				store.set(true);
				if(en.isRestartRequired())
				{
					restart.set(true);
				}
			}
		}
		
		for(Object x: nd.getChildren())
		{
			if(x instanceof OptionTreeNode)
			{
				commitRecursively((OptionTreeNode)x, store, restart);
			}
		}
	}
	
	
	public boolean isModified()
	{
		return isModifiedRecursive(root);
	}
	
	
	protected boolean isModifiedRecursive(OptionTreeNode nd)
	{
		for(OptionEntry en: nd.getOptionEntries())
		{
			if(en.isModified())
			{
				// TODO set a break point here to find out a modified option
				en.isModified();
				return true;
			}
		}
		
		for(Object x: nd.getChildren())
		{
			if(x instanceof OptionTreeNode)
			{
				if(isModifiedRecursive((OptionTreeNode)x))
				{
					return true;
				}
			}
		}
		
		return false;
	}


	public void commit() throws Exception
	{
		AtomicBoolean store = new AtomicBoolean();
		AtomicBoolean restart = new AtomicBoolean();
		
		commitRecursively(root, store, restart);
		
		if(store.get())
		{
			GlobalSettings.save();
			
			GlobalSettings.refreshListeners();
		}
		
		if(restart.get())
		{
			Dialogs.info
			(
				this, 
				TXT.get("COptionPanel.warning.restart.title", "Restart Required"), 
				TXT.get("COptionPanel.warning.restart.message", "Please restart the application for the changes to take effect.")
			);
		}
	}
	
	
	public void revert()
	{
		revertRecursively(root);
	}
	
	
	protected void revertRecursively(OptionTreeNode nd)
	{
		for(OptionEntry en: nd.getOptionEntries())
		{
			en.revert();
		}
		
		for(Object x: nd.getChildren())
		{
			if(x instanceof OptionTreeNode)
			{
				revertRecursively((OptionTreeNode)x);
			}
		}
	}
	
	
	protected void onSelectionChange()
	{
		int row = tree.getSelectedRow();
		OptionTreeNode nd = tree.getNode(row);
		if(nd != null)
		{
			titleField.setText(nd.getName());
			
			OptionEntry[] es = nd.getOptionEntries();
			JComponent c = constructPanel(es);
			display(c);
		}
	}


	protected static JLabel createSectionLabel(String txt)
	{
		JLabel c = new JLabel(txt);
		c.setBorder(new CBorder(2, 0));
		c.setFont(Theme.boldFont());
		return c;
	}


	// creates default component for this option category
	// a list of items in a scroll pane
	// override to construct custom panel
	public static JComponent constructPanel(OptionEntry[] items)
	{
		CPanel p = new CPanel();
		p.setOpaque(true);
		p.setGaps(10, 2);
		p.addColumns
		(
			10,
			10,
			CPanel.PREFERRED, 
			CPanel.FILL,
			10
		);

		CBorder labelBorder = new CBorder(3, 5, 0, 0);
		boolean scrollRequired = true;
		int row = 0;
		boolean bottomGap = true;
		
		for(OptionEntry en: items)
		{
			JComponent c = en.getComponent();
			String name = en.getName();
			bottomGap = true;
			
			if(en.isSection())
			{
				// section label
				p.add(1, row, 3, 1, createSectionLabel(name));
			}
			else
			{
				boolean fullWidth = en.isFullWidth();				
				if(fullWidth)
				{
					// full-width
					p.add(0, row, 5, 1, c);
				}
				else
				{
					// label + editor
					JLabel label = new JLabel(name);
					label.setHorizontalAlignment(JLabel.RIGHT);
					label.setBorder(labelBorder);
					label.setVerticalAlignment(JLabel.TOP);
					label.setOpaque(false);
					
					p.add(2, row, label);
					if(c != null)
					{
						p.add(3, row, c);
					}
				}
				
				float height = en.getPreferredHeight();
				if(height == OptionEditorInterface.HEIGHT_MAX)
				{
					p.tableLayout().setRow(row, CPanel.FILL);
					if(fullWidth)
					{
						bottomGap = false;
					}
					scrollRequired = false;
				}
				else if(height > 0.0f)
				{
					p.tableLayout().setRow(row, height);
				}
			}

			++row;
		}
		
		if((items.length == 1) && (!bottomGap))
		{
			p.setBorder(null);
		}
		else
		{
			p.setBorder(new CBorder(10, 0, bottomGap ? 10 : 0, 0));	
		}

		if(scrollRequired)
		{
			CScrollPane scroll = new CScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setBorder(CBorder.NONE);
			scroll.getViewport().setOpaque(false);
			scroll.setOpaque(false);
			return scroll;
		}
		else
		{
			return p;
		}
	}


	protected void display(JComponent c)
	{
		GlobalSettings.storePreferences(this);
		
		detailPanel.setCenter(c);
		
		GlobalSettings.restorePreferences(this);
		
		validate();
		repaint();
	}
	
	
	public void ensureSelection()
	{
		if(tree.getSelectedRow() < 0)
		{
			tree.changeSelection(0, 0, false, false);
		}
	}
	
	
	protected void onSearch()
	{
		String expr = filter.getText();
		if(CKit.isBlank(expr))
		{
			// keep selection
			CList<OptionTreeNode> sel = tree.getSelectedNodes();
			
			tree.setRoot(root, false);
			buttonLabel.setIcon(null);

			if(sel.size() == 1)
			{
				tree.selectNode(sel.get(0));
			}
		}
		else
		{
			OptionTreeNode searchRoot = find(expr);
			tree.setRoot(searchRoot, false);
			buttonLabel.setIcon(CIcons.FilterCancel);
		}

		tree.expandAll();
		ensureSelection();
	}
	
	
	protected OptionTreeNode find(String expr)
	{
		expr = expr.toLowerCase();
			
		OptionTreeNode rt = findRecursive(root, null, expr);
		return rt;
	}
	
	
	// FIX also search node names
	// TODO or extract text from the container hierarchy 
	protected OptionTreeNode findRecursive(OptionTreeNode src, OptionTreeNode parent, String expr)
	{
		OptionTreeNode rv = new OptionTreeNode(src.getIcon(), src.getName());
		
		int sz = src.getChildrenCount();
		for(int i=0; i<sz; i++)
		{
			OptionTreeNode nd = (OptionTreeNode)src.getChildAt(i);
			OptionTreeNode ch = findRecursive(nd, rv, expr);
			if(ch != null)
			{
				rv.addChild(ch);
			}
		}
		
		// here we check the actual match
		boolean found = false; //matches(expr, src.getName());
		OptionEntry section = null;
		for(OptionEntry en: src.getOptionEntries())
		{
			if(section == null)
			{
				if(en.isSection())
				{
					section = en;
				}
			}
				
			if(findExpression(en, expr))
			{
				if(section != null)
				{
					// add section if any
					rv.addOptionEntry(section);
					section = null;
				}
					
				rv.addOptionEntry(en);
				found = true;
			}
		}
		
		if(found || ((rv.getChildrenCount() > 0) || (rv.getOptionEntryCount() > 0)))
		{
			if(parent != null)
			{
				parent.addChild(rv);
			}
			return rv;
		}
		else
		{
			// skip subtree
			return null;
		}
	}


	protected boolean findExpression(OptionEntry en, String expr)
	{
		if(en.isSection())
		{
			// there is no point in returning sections
			// what needs to be done is to include a section if a subsection matches
			return false;
		}
		else if(matches(expr, en.getName()))
		{
			return true;
		}
		else if(matches(expr, en.getEditorSearchString()))
		{
			return true;
		}
		else if(matches(expr, new StringCollector(en.getComponent()).collect()))
		{
			return true;
		}
		return false;
	}


	protected boolean matches(String expr, String text)
	{
		if(text == null)
		{
			return false;
		}
		else
		{
			return text.toLowerCase().contains(expr);
		}
	}


	public void setSelected(OptionEditorInterface ed)
	{
		OptionTreeNode nd = findNode(ed);
		if(nd != null)
		{
			tree.expandNode(nd);
			tree.selectNode(nd);
		}
	}


	protected OptionTreeNode findNode(OptionEditorInterface ed)
	{
		return findNodeRecursively(root, ed);
	}
	
	
	protected OptionTreeNode findNodeRecursively(OptionTreeNode nd, OptionEditorInterface ed)
	{
		for(OptionEntry en: nd.getOptionEntries())
		{
			if(en.getEditor() == ed)
			{
				return nd;
			}
		}
		
		for(Object ch: nd.getChildren())
		{
			if(ch instanceof OptionTreeNode)
			{
				OptionTreeNode rv = findNodeRecursively((OptionTreeNode)ch, ed);
				if(rv != null)
				{
					return rv;
				}
			}
		}
		
		return null;
	}
}
