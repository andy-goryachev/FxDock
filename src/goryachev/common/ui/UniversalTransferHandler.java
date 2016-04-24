// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.im.InputContext;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;


// universal transfer handler
// see BasicTextUI.TextTransferHandler
public abstract class UniversalTransferHandler
	extends TransferHandler
{
	/** implement to enable input */
	public abstract boolean handleTransferredObject(Object x, JComponent c, Point p, DataFlavor f);
	
	/** implement to enable export */
	protected abstract Transferable createTransferable(JComponent c);
	
	//
	
	public interface FHandler
	{
		public boolean isMatch(DataFlavor f);
		
		public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception;
	}
	
	//
	
	private CList<FHandler> handlers = new CList();
	private JTextComponent textComp;
	private boolean remove;
	private Position p0;
	private Position p1;
	private boolean modeBetween;
	private boolean isDrop;
	private int dropAction = MOVE;
	private Position.Bias dropBias;
	protected Point dropLocation;
	
	
	public UniversalTransferHandler()
	{
	}
	
	
	public void enableFiles()
	{
		addHandler(new FileHandler());
	}
	
	
	public void enableTextPlain()
	{
		addHandler(new PlainTextHandler());
	}
	
	
	public void enableTextHtml()
	{
		addHandler(new HtmlHandler());
	}
	
	
	public void enableString()
	{
		addHandler(new StringHandler());
	}
	
	
	public void enableJavaObjects()
	{
		addHandler(new JavaObjectHandler());
	}
	
	
	public void enableFlavor(final DataFlavor flavor)
	{
		addHandler(new FHandler()
		{
			public boolean isMatch(DataFlavor f)
			{
				return flavor.equals(f);
			}

			public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception
			{
				Object x = tr.getTransferData(flavor);
				return handleTransferredObject(x, c, dropLocation, flavor);
			}
		});
	}
	
	
	protected void addHandler(FHandler h)
	{
		handlers.add(h);
	}
	

	public boolean canImport(JComponent c, DataFlavor[] flavors)
	{
		if(c instanceof JTextComponent)
		{
			JTextComponent tc = (JTextComponent)c;
			if(!tc.isEditable())
			{
				return false;
			}
			else if(!tc.isEnabled())
			{
				return false;
			}
		}
		
		return (findDataFlavor(flavors, c) != null);
	}


	// returns the flavor based on the priority, as defined by the order of transferXXX() methods.
	protected DataFlavor findDataFlavor(DataFlavor[] flavors, Component c)
	{
		int match = handlers.size();
		DataFlavor found = null;
		
		for(int i=0; i<match; i++)
		{
			FHandler h = handlers.get(i);
			
			for(DataFlavor f: flavors)
			{
				if(h.isMatch(f))
				{
					match = i;
					found = f;
					break;
				}
			}
		}
		
		return found;
	}
	
	
	protected FHandler lookupHandler(DataFlavor f)
	{
		for(FHandler h: handlers)
		{
			if(h.isMatch(f))
			{
				return h;
			}
		}
		throw new Rex("No handler defined for: " + f);
	}
	
	
	public boolean importData(JComponent c, Transferable t)
	{
		try
		{
			if(c instanceof JTextComponent)
			{
				JTextComponent tc = (JTextComponent)c;
				int pos = modeBetween ? tc.getDropLocation().getIndex() : tc.getCaretPosition();
	
				// if we are importing to the same component that we exported from
				// then don't actually do anything if the drop location is inside
				// the drag location and set shouldRemove to false so that exportDone
				// knows not to remove any data
				int pos0 = (p0 == null ? 0 : p0.getOffset());
				int pos1 = (p1 == null ? 0 : p1.getOffset());
				if(dropAction == MOVE && c == textComp && pos >= pos0 && pos <= pos1)
				{
					remove = false;
					return true;
				}
			}
			
			DataFlavor f = findDataFlavor(t.getTransferDataFlavors(), c);
			if(f != null)
			{
				FHandler h = lookupHandler(f);
				return h.handleDrop(f, c, t);
			}
		}
		catch(Exception e)
		{ 
			Log.err(e);
		}
		
		return false;
	}
	
	
	protected boolean importPlainText(JComponent c, Object data)
	{
		try
		{
			if(c instanceof JTextComponent)
			{
				JTextComponent tc = (JTextComponent)c;
			
				int pos = modeBetween ? tc.getDropLocation().getIndex() : tc.getCaretPosition();
		
				// if we are importing to the same component that we exported from
				// then don't actually do anything if the drop location is inside
				// the drag location and set shouldRemove to false so that exportDone
				// knows not to remove any data
				int pos0 = (p0 == null ? 0 : p0.getOffset());
				int pos1 = (p1 == null ? 0 : p1.getOffset());
				if(dropAction == MOVE && c == textComp && pos >= pos0 && pos <= pos1)
				{
					remove = false;
					return true;
				}
		
				InputContext ic = c.getInputContext();
				if(ic != null)
				{
					ic.endComposition();
				}
	
				Reader r;
				if(data instanceof Reader)
				{
					r = (Reader)data;
				}
				else if(data instanceof String)
				{
					r = new StringReader((String)data);
				}
				else
				{
					return false;
				}
	
				if(modeBetween)
				{
					Caret caret = tc.getCaret();
					if(caret instanceof DefaultCaret)
					{
						((DefaultCaret)caret).setDot(pos, dropBias);
					}
					else
					{
						tc.setCaretPosition(pos);
					}
				}
	
				boolean useRead = false;
				handleReaderImport(r, tc, useRead);
	
				if(isDrop)
				{
					c.requestFocus();
					Caret caret = tc.getCaret();
					if(caret instanceof DefaultCaret)
					{
						int newPos = caret.getDot();
						Position.Bias newBias = ((DefaultCaret)caret).getDotBias();
	
						((DefaultCaret)caret).setDot(pos, dropBias);
						((DefaultCaret)caret).moveDot(newPos, newBias);
					}
					else
					{
						tc.select(pos, tc.getCaretPosition());
					}
				}
				
				return true;
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		return false;
	}
	

	@Deprecated // I don't know why this is here
	protected boolean importTextData(JTextComponent c, Transferable t) throws Exception
	{
		int pos = modeBetween ? c.getDropLocation().getIndex() : c.getCaretPosition();

		// if we are importing to the same component that we exported from
		// then don't actually do anything if the drop location is inside
		// the drag location and set shouldRemove to false so that exportDone
		// knows not to remove any data
		int pos0 = (p0 == null ? 0 : p0.getOffset());
		int pos1 = (p1 == null ? 0 : p1.getOffset());
		if(dropAction == MOVE && c == textComp && pos >= pos0 && pos <= pos1)
		{
			remove = false;
			return true;
		}

		DataFlavor f = findDataFlavor(t.getTransferDataFlavors(), c);
		if(f != null)
		{
			boolean useRead = false;
			
			// JTextPane
//			if(!c.getContentType().startsWith("text/plain") && f.getMimeType().startsWith(c.getContentType()))
//			{
//				useRead = true;
//			}

			InputContext ic = c.getInputContext();
			if(ic != null)
			{
				ic.endComposition();
			}

			Reader r = f.getReaderForText(t);

			if(modeBetween)
			{
				Caret caret = c.getCaret();
				if(caret instanceof DefaultCaret)
				{
					((DefaultCaret)caret).setDot(pos, dropBias);
				}
				else
				{
					c.setCaretPosition(pos);
				}
			}

			handleReaderImport(r, c, useRead);

			if(isDrop)
			{
				c.requestFocus();
				Caret caret = c.getCaret();
				if(caret instanceof DefaultCaret)
				{
					int newPos = caret.getDot();
					Position.Bias newBias = ((DefaultCaret)caret).getDotBias();

					((DefaultCaret)caret).setDot(pos, dropBias);
					((DefaultCaret)caret).moveDot(newPos, newBias);
				}
				else
				{
					c.select(pos, c.getCaretPosition());
				}
			}
			
			return true;
		}
		
		return false;
	}


	protected void handleReaderImport(Reader in, JTextComponent c, boolean useRead) throws BadLocationException, IOException
	{
		if(useRead)
		{
			int startPosition = c.getSelectionStart();
			int endPosition = c.getSelectionEnd();
			int length = endPosition - startPosition;
			EditorKit kit = c.getUI().getEditorKit(c);
			Document doc = c.getDocument();
			if(length > 0)
			{
				doc.remove(startPosition, length);
			}
			kit.read(in, doc, startPosition);
		}
		else
		{
			char[] buff = new char[1024];
			int nch;
			boolean lastWasCR = false;
			int last;
			StringBuffer sbuff = null;

			// Read in a block at a time, mapping \r\n to \n, as well as single
			// \r to \n.
			while((nch = in.read(buff, 0, buff.length)) != -1)
			{
				if(sbuff == null)
				{
					sbuff = new StringBuffer(nch);
				}
				
				last = 0;
				for(int counter = 0; counter < nch; counter++)
				{
					switch(buff[counter])
					{
					case '\r':
						if(lastWasCR)
						{
							if(counter == 0)
							{
								sbuff.append('\n');
							}
							else
							{
								buff[counter - 1] = '\n';
							}
						}
						else
						{
							lastWasCR = true;
						}
						break;
					case '\n':
						if(lastWasCR)
						{
							if(counter > (last + 1))
							{
								sbuff.append(buff, last, counter - last - 1);
							}
							// else nothing to do, can skip \r, next write will
							// write \n
							lastWasCR = false;
							last = counter;
						}
						break;
					default:
						if(lastWasCR)
						{
							if(counter == 0)
							{
								sbuff.append('\n');
							}
							else
							{
								buff[counter - 1] = '\n';
							}
							lastWasCR = false;
						}
						break;
					}
				}
				if(last < nch)
				{
					if(lastWasCR)
					{
						if(last < (nch - 1))
						{
							sbuff.append(buff, last, nch - last - 1);
						}
					}
					else
					{
						sbuff.append(buff, last, nch - last);
					}
				}
			}
			if(lastWasCR)
			{
				if(sbuff != null)
				{
					sbuff.append('\n');
				}
			}
			c.replaceSelection(sbuff != null ? sbuff.toString() : "");
		}
	}


	public int getSourceActions(JComponent c)
	{
		if(c instanceof JTextComponent)
		{
			return ((JTextComponent)c).isEditable() ? COPY_OR_MOVE : COPY;
		}
		else
		{
			return COPY;
		}
	}


	/** should be called first use to create a standard text transferable */
	public void addPlainTextFromComponent(JComponent c, CTransferable t)
	{
		if(c instanceof JTextComponent)
		{
			textComp = (JTextComponent)c;
			remove = true;
			
			int pos0 = textComp.getSelectionStart();
			int pos1 = textComp.getSelectionEnd();
			
			if(pos0 != pos1)
			{
				try
				{
					p0 = textComp.getDocument().createPosition(pos0);
					p1 = textComp.getDocument().createPosition(pos1);
				
					t.addTextPlain(textComp.getDocument().getText(pos0, pos1 - pos0));
				}
				catch(Exception e)
				{
					Log.err(e);
				}
			}
		}
	}
	

	protected void exportDone(JComponent source, Transferable data, int action)
	{
		// only remove the text if shouldRemove has not been set to
		// false by importData and only if the action is a move
		if(remove && action == MOVE)
		{
			removeText();
		}

		textComp = null;
		p0 = null;
		p1 = null;
	}
	
	
	protected void removeText()
	{
		if(textComp == null)
		{
			return;
		}
		if(p0 == null)
		{
			return;
		}
		if(p1 == null)
		{
			return;
		}

		if(p0.getOffset() != p1.getOffset())
		{
			try
			{
				Document doc = textComp.getDocument();
				doc.remove(p0.getOffset(), p1.getOffset() - p0.getOffset());
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
	}


	public boolean importData(TransferSupport support)
	{
		isDrop = support.isDrop();
		if(isDrop)
		{
			dropLocation = support.getDropLocation().getDropPoint();
			
			if(support.getComponent() instanceof JTextComponent)
			{
				modeBetween = ((JTextComponent)support.getComponent()).getDropMode() == DropMode.INSERT;
				dropBias = ((JTextComponent.DropLocation)support.getDropLocation()).getBias();
				dropAction = support.getDropAction();
			}
		}
		else
		{
			dropLocation = null;
		}

		try
		{
			return super.importData(support);
		}
		finally
		{
			isDrop = false;
			modeBetween = false;
			dropBias = null;
			dropAction = MOVE;
			dropLocation = null;
		}
	}
	
	
	protected String getTransferredString(Transferable t) throws Exception
	{
		return (String)t.getTransferData(DataFlavor.stringFlavor);
	}
	
	
	public static boolean isPlainTextFlavor(DataFlavor f)
	{
		return CTransferable.isPlainTextFlavor(f);
	}
	
	
	//
	
	
	public class PlainTextHandler implements FHandler
	{
		public boolean isMatch(DataFlavor f)
		{
			if(DataFlavor.stringFlavor.equals(f))
			{
				return true;
			}
			
			String mime = f.getMimeType();
			if(mime.startsWith("text/plain"))
			{
				return true;
			}
			return false;
		}


		public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception
		{
			if(c instanceof JTextComponent)
			{
				return importTextData((JTextComponent)c, tr);
			}
			else
			{
				String s = getTransferredString(tr);
				return handleTransferredObject(s, c, dropLocation, f);
			}
		}
	}
	
	
	//
	
	
	public class FileHandler implements FHandler
	{
		public boolean isMatch(DataFlavor f)
		{
			if(DataFlavor.javaFileListFlavor.equals(f))
			{
				return true;
			}
			return false;
		}


		public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception
		{
			// TODO file transferable bug on linux
			List fs = (List)tr.getTransferData(DataFlavor.javaFileListFlavor);
			File[] files = new File[fs.size()];
			fs.toArray(files);

			return handleTransferredObject(files, c, dropLocation, f);
		}
	}
	
	
	//
	
	
	public class StringHandler implements FHandler
	{
		public boolean isMatch(DataFlavor f)
		{
			if(DataFlavor.stringFlavor.equals(f))
			{
				return true;
			}
			else
			{
				String mime = f.getMimeType();
				if(mime.startsWith("application/x-java-jvm-local-objectref") && (f.getRepresentationClass() == String.class))
				{
					return true;
				}
			}
				
			return false;
		}


		public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception
		{
			String s = getTransferredString(tr);
			return handleTransferredObject(s, c, dropLocation, f);
		}
	}
	
	
	//
	
	
	public class HtmlHandler implements FHandler
	{
		public boolean isMatch(DataFlavor f)
		{
			return CTransferable.isHtmlFlavor(f);
		}


		public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception
		{
			Object x = tr.getTransferData(f);
			return handleTransferredObject(x, c, dropLocation, f);
		}
	}
	
	
	//
	
	
	public class JavaObjectHandler implements FHandler
	{
		public boolean isMatch(DataFlavor f)
		{
			if(f.isFlavorSerializedObjectType())
			{
				return true;
			}
			return false;
		}


		public boolean handleDrop(DataFlavor f, JComponent c, Transferable tr) throws Exception
		{
			Object x = tr.getTransferData(f);
			return handleTransferredObject(x, c, dropLocation, f);
		}
	}
}
