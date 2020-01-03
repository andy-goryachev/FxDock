// Copyright © 2008-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.html;


// parses an html/xml file
// can be invoked only once
public abstract class AbstractHtmlParser
{
	public abstract void addBreak(int start, int end, int line, String original, String unicode);
	public abstract void addComment(int start, int end, int line, String original, String unicode);
	public abstract void addScript(int start, int end, int line, String original, String unicode);
	public abstract void addTag(int start, int end, int line, String original, String unicode);
	public abstract void addText(int start, int end, int line, String original, String unicode);

	//

	private static enum HtmlSegment { Break, Comment, Script, Tag, Text };
	
	public final String text;
	private int line = 1;
	private int offset;
	private int startOffset;
	private boolean skipLF;
	private boolean inBreak;
	private boolean inComment;
	private boolean inTags;
	private boolean inScript;
	private boolean tagWillEnd;
	
	
	public AbstractHtmlParser(String text)
	{
		this.text = text;
	}
	
	
	public int getLineCount()
	{
		return line;
	}
	

	// \n,\r,\r\n
	// <...>
	// <!-- ... -->
	// <script> ... </script>
	// TODO <style>
	public void parse() throws Exception
	{
		int length = text.length();
		for(offset=0; offset<length; offset++)
		{
			boolean breakStarts = false;
			boolean breakEnds = false;
			boolean commentStarts = false;
			boolean commentEnds = false;
			boolean scriptStarts = false;
			boolean scriptEnds = false;
			boolean tagStarts = false;
			boolean tagEnds = tagWillEnd;
			
			if(tagEnds)
			{
				tagWillEnd = false;
				if(inComment)
				{
					if(isCommentEndTag())
					{
						commentEnds = true;
					}
				}
				else
				{
					if(isScriptEndTag())
					{
						scriptEnds = true;
					}
				}
			}
			
			char c = text.charAt(offset);
			switch(c)
			{
			case '\r':
				++line;
				skipLF = true;
				breakStarts = true;
				break;

			case '\n':
				if(skipLF)
				{
					skipLF = false;
				}
				else
				{
					++line;
					breakStarts = true;
				}
				break;
				
			case '<':
				breakEnds = true;
				if(inComment && !commentEnds)
				{
					break;
				}
				else
				{
					if(isCommentTag())
					{
						commentStarts = true;
					}
					else
					{
						if(isScriptTag())
						{
							scriptStarts = true;
						}
						else if(scriptEnds || !inScript)
						{
							tagStarts = true;
						}
					}
				}
				break;
				
			case '>':
				breakEnds = true;
				tagWillEnd = true;
				break;
				
			default:
				breakEnds = true;
				break;
			}
			
			boolean addChunk =
				(inBreak && breakEnds) ||
				(!inBreak && breakStarts) ||
				(inComment && commentEnds) ||
				(!inComment && commentStarts) ||
				(inScript && scriptEnds) ||
				(!inScript && scriptStarts) ||
				(!inTags && tagStarts) ||
				(inTags && tagEnds);
			
			if(addChunk)
			{
				addChunk();
			}
			
			if(breakStarts)
			{
				inBreak = true;
			}
			else if(breakEnds)
			{
				inBreak = false;
			}
			
			if(commentStarts)
			{
				inComment = true;
			}
			else if(commentEnds)
			{
				inComment = false;
			}
			
			if(scriptStarts)
			{
				inScript = true;
				inComment = false;
			}
			else if(scriptEnds)
			{
				inScript = false;
			}
			
			if(tagStarts)
			{
				inTags = true;
				inComment = false;
				inScript = false;
			}
			else if(tagEnds)
			{
				inTags = false;
			}
		}
		
		addChunk();
	}
	
	
	protected void addChunk()
	{
		if(inBreak)
		{
			addChunk(HtmlSegment.Break);
			inBreak = false;
		}
		else if(inComment)
		{
			addChunk(HtmlSegment.Comment);	
		}
		else if(inScript)
		{
			addChunk(HtmlSegment.Script);
		}
		else if(inTags)
		{
			addChunk(HtmlSegment.Tag);
		}
		else
		{
			addChunk(HtmlSegment.Text);
		}
	}
	
	
	protected void addChunk(HtmlSegment type)
	{
		int endOffset = offset;
		
		if(endOffset > startOffset)
		{
			String original = text.substring(startOffset, endOffset);
			String unicode = HtmlTools.decodeHtmlCharacterEntities(original);
			
			switch(type)
			{
			case Break:
				addBreak(startOffset, endOffset, line, original, unicode);
				break;
			case Comment:
				addComment(startOffset, endOffset, line, original, unicode);
				break;
			case Script:
				addScript(startOffset, endOffset, line, original, unicode);
				break;
			case Tag:
				addTag(startOffset, endOffset, line, original, unicode);
				break;
			case Text:
				addText(startOffset, endOffset, line, original, unicode);
				break;
			}
			
			startOffset = endOffset;
		}
	}
	

	protected boolean isCommentTag()
	{
		try
		{
			return isStartOf("<!--");
		}
		catch(Exception e)
		{ }
		
		return false;
	}
	
	
	protected boolean isScriptTag()
	{
		return isTagToRight("<script") || isTagToRight("<style");
	}
	
	
	protected boolean isTagToRight(String tag)
	{
		try
		{
			if(tag.equalsIgnoreCase(text.substring(offset, offset + tag.length())))
			{
				char c = text.charAt(offset + tag.length());
				// TODO potential problem with open-ended tags: 
				// "<tag ...."
				return ((c == '>') || (Character.isWhitespace(c))); 
			}
		}
		catch(Exception e)
		{ }
		
		return false;
	}
	
	
	protected boolean isScriptEndTag()
	{
		return isEndOf("</script>") || isEndOf("</style>");
	}
	
	
	protected boolean isCommentEndTag()
	{
		return isEndOf("-->");
	}
	
	
	protected boolean isEndOf(String tag)
	{
		try
		{
			return tag.equals(text.substring(offset - tag.length(), offset));
		} 
		catch(Exception e)
		{ }
		return false;
	}
	
	
	protected boolean isStartOf(String tag)
	{
		try
		{
			return tag.equals(text.substring(offset, offset + tag.length()));
		} 
		catch(Exception e)
		{ }
		return false;
	}
}
