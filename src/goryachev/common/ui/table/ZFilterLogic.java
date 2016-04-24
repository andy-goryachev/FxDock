// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CTextFieldWithPrompt;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.theme.CFieldBorder;
import goryachev.common.ui.theme.ThemeColor;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.Timer;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;


// implements filter by string value logic
// works with slight delay
// TODO text quick search: "quotes", -exclude, "nested "quotes"", case insensitive
// TODO pulldown with recent searches (persistent? or per session?)
// TODO highlight text
public class ZFilterLogic
{
	public void onFilterChange(boolean filterOn) { }
	
	//
	
	public static final int DEFAULT_DELAY = 500;
	public static final Color FRACTION_COLOR = ThemeColor.create(ThemeKey.TARGET_COLOR, 0.25, ThemeKey.TEXT_BG);
	
	public final ZTable table;
	public final CTextFieldWithPrompt textField;
	protected ZStringRowFilter filter;
	public final CPanel panel;
	public final JLabel buttonLabel;
	protected Timer timer;
	protected boolean isFiltering;
	protected int viewRows;
	protected int modelRows;
	
	
	/** filtering enables sorting in the table */
	public ZFilterLogic(ZTable t)
	{
		this(t, DEFAULT_DELAY);
	}
	
	
	/** filtering enables sorting in the table */
	public ZFilterLogic(ZTable t, int delay)
	{
		this.table = t;
		table.setSortable(true);
		table.putClientProperty(ZFilterLogic.class, this);
		
		// timer
		timer = new Timer(delay, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				trigger();
			}
		});
		timer.setRepeats(false);
		
		// field
		textField = new CTextFieldWithPrompt();
		textField.setBorder(CBorder.NONE);
		textField.setOpaque(false);
		textField.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent ev)
			{
				// similar code in OptionPanel, move to a dedicated class?
				timer.stop();
				if(isEscape(ev))
				{
					textField.setText(null);
					clear();
					ev.consume();
				}
				else
				{
					// trigger sorting after small delay
					timer.start();
				}
			}
		});
		textField.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e) 
			{
				if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != textField)
				{
					if(textField.getSelectionStart() == textField.getSelectionEnd())
					{
						textField.selectAll();
					}
				}
			}
		});
		
		buttonLabel = new JLabel();
		buttonLabel.setOpaque(false);
		buttonLabel.setToolTipText(TXT.get("StandardFilterLogic.tooltip","Click to clear filter"));
		buttonLabel.setMinimumSize(new Dimension(-1, CIcons.FilterCancel.getIconHeight()));
		buttonLabel.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e) { clear(); }
		});
			
		panel = new CPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				// paint bar
				int w = getWidth();
				int h = getHeight();

				if(isFiltering)
				{
					int x = w * viewRows / modelRows;
					Color c;
					
					// displayed
					c = Theme.TEXT_BG;
					g.setColor(c);
					g.fillRect(0,0,x,h);
					
					// filtered
					c = FRACTION_COLOR;
					g.setColor(c);
					g.fillRect(x,0,w,h);
				}
				else
				{
					g.setColor(Theme.TEXT_BG);
					g.fillRect(0,0,w,h);
				}
			}
		};
		panel.setCenter(textField);
		panel.setEast(buttonLabel);
		panel.setBorder(new CFieldBorder());
		panel.setOpaque(false);

		table.getRowSorter().addRowSorterListener(new RowSorterListener()
		{
			public void sorterChanged(RowSorterEvent ev)
			{
				RowSorter sorter = ev.getSource();
				viewRows = sorter.getViewRowCount();
				modelRows = sorter.getModelRowCount();
				filterChanged();
				onFilterChange(isFiltering);
				
				// refresh scrollbar
//				table.validate();
//				table.repaint();
				
				UI.later(new Runnable()
				{
					public void run()
					{
						table.validate();
						table.repaint();
					}
				});
			}
		});
		
		setFieldWidth(200);
		setPrompt(Menus.find);
	}
	
	
	public void setPrompt(String s)
	{
		textField.setPrompt(s);	
	}
	
	
	public void trigger()
	{
		String expression = textField.getText();
		filter().setExpression(expression);
		buttonLabel.setIcon(CKit.isBlank(expression) ? null : CIcons.FilterCancel);
		
		table.setRowFilter(filter().copy());
		table.repaint();
		
		int row = table.getSelectedRow();
		if(row < 0)
		{
			row = 0;
		}
		Rectangle r = table.getCellRect(row, 0, false);
		table.scrollRectToVisible(r);
	}
	
	
	protected ZStringRowFilter filter()
	{
		if(filter == null)
		{
			filter = createRowFilter();
		}
		return filter;
	}
	
	
	protected ZStringRowFilter createRowFilter()
	{
		return new ZStringRowFilter();
	}
	
	
	public void restart()
	{
		timer.restart();
	}
	
	
	public void clear()
	{
		textField.setText(null);
		trigger();
	}
	
	
	public void filterChanged()
	{
		String tooltip;
		if(viewRows == modelRows)
		{
			tooltip = null;
			isFiltering = false;
		}
		else
		{
			tooltip = TXT.get("CFilterLogic.showing x/y rows","Showing {0} out of {1} rows", Theme.formatNumber(viewRows), Theme.formatNumber(modelRows));
			isFiltering = true;
		}
		
		textField.setToolTipText(tooltip);
		UI.showTooltip(textField);
		panel.repaint();
	}
	
	
	protected static boolean isEscape(KeyEvent ev)
	{
		if(ev.getModifiers() != 0)
		{
			return false;
		}
		
		return (ev.getKeyCode() == KeyEvent.VK_ESCAPE);
	}
	

	public void setDelay(int ms)
	{
		timer.setDelay(ms);
	}
	
	
	public JComponent getComponent()
	{
		return panel;
	}
	
	
	public JTextField getField()
	{
		return textField;
	}
	
	
	public void setFieldWidth(int w)
	{
		panel.setPreferredSize(new Dimension(w,-1));
	}


	public static ZFilterLogic get(JComponent c)
	{
		Object x = c.getClientProperty(ZFilterLogic.class);
		if(x instanceof ZFilterLogic)
		{
			return (ZFilterLogic)x;
		}
		return null;
	}
	
	
	public boolean isFiltering()
	{
		return isFiltering;
	}
}
