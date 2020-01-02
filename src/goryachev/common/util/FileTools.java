// Copyright © 2004-2019 Andy Goryachev <andy@goryachev.com>
// Contains fragments of Apache FileNameUtils code
// http://www.apache.org/licenses/LICENSE-2.0
package goryachev.common.util;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Stack;


public class FileTools
{
	public static final String COPYRIGHT = "Copyright © Andy Goryachev <andy@goryachev.com>.  All Rights Reserved.";
	
	
	public static final String[] IMAGE =
	{
		".png",
		".jpg",
		".jpeg",
		".gif"
//		".bmp"
//		".tif",
//		".tiff"
	};

	public static final String[] JAVA =
	{
		".java",
	};
	
	public static final String[] HTML =
	{
		".html",
		".htm"
	};
	
	public static final String[] XML =
	{
		".xml"
	};
	
	public static final String[] PROPERTIES =
	{
		".properties"
	};
	
	public static final String[] SYSTEM_FOLDERS =
	{
		"cvs",
		".svn",
		"_svn",
		".git",
		".TemporaryItems"
	};

	public static final String[] SYSTEM_FILES =
	{
		"thumbs.db",
		".DS_Store"
	};

	//
	
	public static boolean isImage(File f) { return endsWith(f, IMAGE); }
	public static boolean isImage(String filename) { return endsWith(filename, IMAGE); }
	public static boolean isHtml(File f) { return endsWith(f, HTML); }
	public static boolean isHtml(String filename) { return endsWith(filename, HTML); }
	public static boolean isJava(File f) { return endsWith(f, JAVA); }
	public static boolean isJava(String filename) { return endsWith(filename, JAVA); }	
	public static boolean isXml(File f) { return endsWith(f, XML); }
	public static boolean isXml(String filename) { return endsWith(filename, XML); }
	public static boolean isProperties(File f) { return endsWith(f, PROPERTIES); }
	
	
	public static boolean isHidden(File f)
	{
		if(f.isHidden())
		{
			if(CPlatform.isWindows())
			{
				if(f.getParentFile() == null)
				{
					// for some reason C:// is hidden
					// http://stackoverflow.com/questions/11862212/java-thinks-c-drive-is-hidden
					return false;
				}
			}
			return true;
		}
		
		return false;
	}
	
	
	public static boolean isHiddenOrSystem(File f) 
	{
		if(isHidden(f))
		{
			return true;
		}
		else if(isSystem(f))
		{
			return true;
		}
		
		return false;
	}
	
	
	public static boolean isSystem(File f)
	{
		if(f.getName().startsWith("."))
		{
			return true;
		}

		if(f.isDirectory())
		{
			if(matches(f, SYSTEM_FOLDERS))
			{
				return true;
			}
		}
		else
		{
			if(matches(f, SYSTEM_FILES))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public static boolean endsWith(File f, String[] extensions)
	{
		if(f == null)
		{
			return false;
		}
		return endsWith(f.getName(), extensions);
	}

	
	private static boolean endsWith(String name, String[] extensions)
	{
		if(name == null)
		{
			return false;
		}
		
		name = name.toLowerCase();
		for(String ext: extensions)
		{
			if(name.endsWith(ext))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public static boolean matches(File f, String[] extensions)
	{
		if(f == null)
		{
			return false;
		}
		return matches(f.getName(), extensions);
	}

	
	private static boolean matches(String name, String[] extensions)
	{
		if(name == null)
		{
			return false;
		}
		
		for(String ext: extensions)
		{
			if(ext.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		
		return false;
	}


	public static String loadText(File f, String encoding, int max) throws Exception
	{
		byte[] buf = CKit.readBytes(f, max);
		try
		{
			if(encoding != null)
			{
				return new String(buf, encoding); 
			}
		}
		catch(Exception e)
		{ }
		
		return new String(buf);
	}


	// returns path to root
	public static String getPathToRoot(File root, File file)
	{
		String rootPath = root.getAbsolutePath();
		String path = file.getAbsolutePath();
		
		path = path.substring(rootPath.length());
		
		// remove file name
		path = path.substring(0, path.length() - file.getName().length());
		if(path.endsWith("/") || path.endsWith("\\"))
		{
			path = path.substring(0, path.length() - 1);
		}
		
		if(path.startsWith("/") || path.startsWith("\\"))
		{
			return path.substring(1);
		}
		return path;
	}

	
	// serialize object to a file
	@Deprecated // don't use serialization
	public static void write(Object d, File file) throws Exception
	{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		try
		{
			out.writeObject(d);
		}
		finally
		{
			close(out);
		}
	}
	
	
	// serialize object to a file
	@Deprecated // don't use serialization
	public static void write(Object d, String filename) throws Exception
	{
		write(d, new File(filename));
	}


	// deserialize object from a file
	@Deprecated // don't use serialization
	public static Object read(File file) throws Exception
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		try
		{
			Object d = in.readObject();
			return d;
		}
		finally
		{
			close(in);
		}
	}


	// deserialize object from a file
	@Deprecated // don't use serialization
	public static Object read(String filename) throws Exception
	{
		return read(new File(filename));
	}


	public static byte[] readByteArray(File file) throws Exception
	{
		byte[] array = new byte[(int)file.length()];
		FileInputStream in = new FileInputStream(file);
		try
		{
			in.read(array);
		}
		finally
		{
			close(in);
		}
		return array;
	}
	
	
	// get byte array from the file system 
	public static byte[] readByteArray(String name) throws Exception
	{
		File file = new File(name);	
		FileInputStream inp = new FileInputStream(file);
		// small files only
		int length = (int)file.length();
		byte[] data = new byte[length];
		inp.read(data);
		inp.close();

		return data;
	}
	
	
	public static void writeByteArray(File file, byte[] data) throws Exception
	{
		FileOutputStream out = new FileOutputStream(file);
		try
		{
			// small files only
			out.write(data);
		}
		finally
		{
			close(out);
		}
	}

	
	// get input stream from either a JAR or a file
	public static InputStream getInputStream(String name)
	{
		InputStream is = null;
		try
		{
			// try jar resource
			is = ClassLoader.getSystemResourceAsStream(name);
		}
		catch(Exception e)
		{ }
		
		if(is == null)
		{
			// try local path
			try
			{
				is = new FileInputStream(name);
			}
			catch(Exception ignore)
			{ }
		}
		return is;
	}
	
	
	public static String loadText(String name)
	{
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		try
		{
			in = getInputStream(name);
			int c;
			while((c = in.read()) != -1)
			{
				sb.append((char)c);
			}
		}
		catch(Exception e)
		{ }
		finally
		{
			close(in);
		}
		return sb.toString();
	}
	

	
	public static String loadUnicodeText(String name)
	{
		StringBuilder sb = new StringBuilder();
		InputStreamReader in = null;
		try
		{
			in = new InputStreamReader(getInputStream(name),"UTF-16");
			int c;
			while((c = in.read()) != -1)
			{
				sb.append((char)c);
			}
		}
		catch(Exception e)
		{ }
		finally
		{
			close(in);
		}
		return sb.toString();
	}

	
	// load object from file system or jar file
	public static Object loadObject(String name) throws Exception
	{
		ObjectInputStream in = new ObjectInputStream(getInputStream(name));
		try
		{
			Object d = in.readObject();
			return d;
		}
		finally
		{
			close(in);
		}
	}


	// read file as an ASCII string
	public static String loadFile(String file) throws Exception
	{
		File f = new File(file);
		byte[] b = new byte[(int)f.length()];
		FileInputStream in = new FileInputStream(f);

		try
		{
			in.read(b);
			return new String(b);
		}
		finally
		{
			close(in);
		}
	}
	
	
	public static void close(Closeable c)
	{
		try
		{
			c.close();
		}
		catch(Exception ignore)
		{ }
	}
	

	public static void load(Properties p, String filename) throws Exception
	{
		FileInputStream in = new FileInputStream(filename);
		try
		{
			p.load(in);
		}
		finally
		{
			close(in);
		}
	}


	public static String getPathToParent(File parent, File f)
	{
		StringBuilder sb = new StringBuilder();
		
		for(;;)
		{
			f = f.getParentFile();
			if(f == null)
			{
				break;
			}
			else if(parent.equals(f))
			{
				break;
			}
			
			if(sb.length() > 0)
			{
				sb.insert(0, "/");
			}
			sb.insert(0, f.getName());
		}
		
		return sb.toString();
	}
	
	
	public static boolean isFileExist(File f)
	{
		try
		{
			if(f.exists())
			{
				if(f.isFile())
				{
					return true;
				}
			}
		}
		catch(Exception e)
		{ }
		
		return false;
	}
	
	
	
	public static boolean isFolderExist(File f)
	{
		try
		{
			if(f.exists())
			{
				if(f.isDirectory())
				{
					return true;
				}
			}
		}
		catch(Exception e)
		{ }
		
		return false;
	}


	public static void createZeroLengthFile(File f) throws Exception
	{
		ensureParentFolder(f);
		FileOutputStream os = new FileOutputStream(f);
		CKit.close(os);
	}
	
	
	/** copies file, preserving the file stamp.  throws an exception if both arguments point to the same absolute file */
	public static void copy(File src, File dst) throws Exception
	{
		if(src.getAbsoluteFile().equals(dst.getAbsoluteFile()))
		{
			throw new Exception("same file");
		}
		
		long t = src.lastModified();
		FileInputStream in = new FileInputStream(src);
		copy(in, dst);
		dst.setLastModified(t);
	}


	public static void copy(InputStream src, File dst) throws Exception
	{
		try
		{
			FileOutputStream out = new FileOutputStream(dst);
			try
			{
				CKit.copy(src, out);
			}
			finally
			{
				CKit.close(out);
			}
		}
		finally
		{
			CKit.close(src);
		}
	}
	

	/** deletes file or folder, recursing into subdirectories when necessary.  returns true if operation succeeded in deleting. */
	public static boolean deleteRecursively(File file) throws Exception
	{
		CKit.checkCancelled();
		
		boolean result = true;
		if(file.exists())
		{
			if(file.isDirectory())
			{
				File[] fs = file.listFiles();
				if(fs != null)
				{
					for(File f: fs)
					{
						result &= deleteRecursively(f);
					}
				}
			}

			result &= file.delete();
		}
		return result;
	}
	
	
	/**
	 * Checks a filename to see if it matches the specified wildcard matcher,
	 * always testing case-sensitive.
	 * <p>
	 * The wildcard matcher uses the characters '?' and '*' to represent a
	 * single or multiple (zero or more) wildcard characters.
	 * This is the same as often found on Dos/Unix command lines.
	 * The check is case-sensitive always.
	 * <pre>
	 * wildcardMatch("c.txt", "*.txt")      --> true
	 * wildcardMatch("c.txt", "*.jpg")      --> false
	 * wildcardMatch("a/b/c.txt", "a/b/*")  --> true
	 * wildcardMatch("c.txt", "*.???")      --> true
	 * wildcardMatch("c.txt", "*.????")     --> false
	 * </pre>
	 * N.B. the sequence "*?" does not work properly at present in match strings.
	 */
	// apache code
	public static boolean wildcardMatch(String filename, String wildcard)
	{
		return wildcardMatch(filename, wildcard, true);
	}


	/**
	 * Checks a filename to see if it matches the specified wildcard matcher
	 * allowing control over case-sensitivity.
	 * <p>
	 * The wildcard matcher uses the characters '?' and '*' to represent a
	 * single or multiple (zero or more) wildcard characters.
	 * N.B. the sequence "*?" does not work properly at present in match strings.
	 */
	// apache code
	public static boolean wildcardMatch(String filename, String wildcard, boolean caseSensitive)
	{
		if(filename == null && wildcard == null)
		{
			return true;
		}
		if(filename == null || wildcard == null)
		{
			return false;
		}

		String[] wcs = splitOnTokens(wildcard);
		boolean anyChars = false;
		int textIdx = 0;
		int wcsIdx = 0;
		Stack<int[]> backtrack = new Stack<int[]>();

		// loop around a backtrack stack, to handle complex * matching
		do
		{
			if(backtrack.size() > 0)
			{
				int[] array = backtrack.pop();
				wcsIdx = array[0];
				textIdx = array[1];
				anyChars = true;
			}

			// loop whilst tokens and text left to process
			while(wcsIdx < wcs.length)
			{
				if(wcs[wcsIdx].equals("?"))
				{
					// ? so move to next text char
					textIdx++;
					if(textIdx > filename.length())
					{
						break;
					}
					anyChars = false;

				}
				else if(wcs[wcsIdx].equals("*"))
				{
					// set any chars status
					anyChars = true;
					
					if(wcsIdx == wcs.length - 1)
					{
						textIdx = filename.length();
					}
				}
				else
				{
					// matching text token
					if(anyChars)
					{
						// any chars then try to locate text token
						textIdx = checkIndexOf(filename, textIdx, wcs[wcsIdx], caseSensitive);
						if(textIdx == -1)
						{
							// token not found
							break;
						}
						
						int repeat = checkIndexOf(filename, textIdx + 1, wcs[wcsIdx], caseSensitive);
						if(repeat >= 0)
						{
							backtrack.push(new int[]
							{
								wcsIdx, repeat
							});
						}
					}
					else
					{
						// matching from current position
						if(!checkRegionMatches(filename, textIdx, wcs[wcsIdx], caseSensitive))
						{
							// couldnt match token
							break;
						}
					}

					// matched text token, move text index to end of matched token
					textIdx += wcs[wcsIdx].length();
					anyChars = false;
				}

				wcsIdx++;
			}

			// full match
			if(wcsIdx == wcs.length && textIdx == filename.length())
			{
				return true;
			}

		} while(backtrack.size() > 0);

		return false;
	}
	
	
	// apache code
	protected static String[] splitOnTokens(String text)
	{
		// used by wildcardMatch
		// package level so a unit test may run on this
		if(text.indexOf('?') == -1 && text.indexOf('*') == -1)
		{
			return new String[]
			{
				text
			};
		}

		char[] array = text.toCharArray();
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder buffer = new StringBuilder();
		for(int i=0; i<array.length; i++)
		{
			if(array[i] == '?' || array[i] == '*')
			{
				if(buffer.length() != 0)
				{
					list.add(buffer.toString());
					buffer.setLength(0);
				}
				
				if(array[i] == '?')
				{
					list.add("?");
				}
				else if(list.isEmpty() || i > 0 && list.get(list.size() - 1).equals("*") == false)
				{
					list.add("*");
				}
			}
			else
			{
				buffer.append(array[i]);
			}
		}
		
		if(buffer.length() != 0)
		{
			list.add(buffer.toString());
		}

		return list.toArray(new String[list.size()]);
	}
	
	
	// apache code
	protected static int checkIndexOf(String str, int strStartIndex, String search, boolean caseSensitive)
	{
		int endIndex = str.length() - search.length();
		if(endIndex >= strStartIndex)
		{
			for(int i=strStartIndex; i<=endIndex; i++)
			{
				if(checkRegionMatches(str, i, search, caseSensitive))
				{
					return i;
				}
			}
		}
		return -1;
	}


	protected static boolean checkRegionMatches(String str, int strStartIndex, String search, boolean caseSensitive)
	{
		return str.regionMatches(!caseSensitive, strStartIndex, search, 0, search.length());
	}
	
	
	public static void touch(File f) throws Exception
	{
		if(f.exists())
		{
			f.setLastModified(System.currentTimeMillis());
		}
		else
		{
			CKit.write(new byte[0], f);
		}
	}
	
	
	public static void ensureParentFolder(File f)
	{
		if(f != null)
		{
			File folder = f.getParentFile();
			if(folder != null)
			{
				folder.mkdirs();
			}
		}
	}
	
	
	/** renames a file within the same directory.  returns true if rename has succeeded */
	public static boolean rename(File f, String newName)
	{
		File dst = new File(f.getParent(), newName);
		return f.renameTo(dst);
	}
	
	
	/** create a backup copy of a file (by renaming it to *.backup) */
	public static void createBackup(File f)
	{
		// delete old backup
		File backup = new File(f.getAbsolutePath() + ".backup");
		backup.delete();
		
		// backup
		f.renameTo(backup);		
	}
	

	/** returns true if 'parent' file is a parent of 'file' */
	public static boolean isParent(File parent, File file)
	{
		while(file != null)
		{
			if(file.equals(parent))
			{
				return true;
			}
			
			file = file.getParentFile();
		}
		return false;
	}
	

	/** returns true if the specified file is either an empty directory or does not exist */
	public static boolean isEmptyDirectory(File f)
	{
		if(!f.exists())
		{
			return true;
		}
		else if(f.isDirectory())
		{
			File[] fs = f.listFiles();
			if(fs == null)
			{
				return true;
			}
			else
			{
				if(fs.length == 0)
				{
					return true;
				}
			}
		}
		return false;
	}


	/** returns canonical path.  this is for presentation only, to avoid exception */
	public static String getCanonicalPath(File f)
	{
		if(f == null)
		{
			return null;
		}
		
		try
		{
			return f.getCanonicalPath();
		}
		catch(Exception e)
		{
			return f.getAbsolutePath();
		}
	}
	
	
	/** creates a file in the same directory */
	public static File inSameDir(File file, String name)
	{
		File parent = file.getParentFile();
		return new File(parent, name);
	}
	
	
	/** returns file extension (after the last period) or null */ 
	public static String getExtension(String name)
	{
		if(name == null)
		{
			return null;
		}
		
		int ix = name.lastIndexOf('.');
		if(ix >= 0)
		{
			return name.substring(ix + 1);
		}
		else
		{
			return null;
		}
	}
	
	
	/** returns base file name (before the last period) */ 
	public static String getBaseName(String name)
	{
		if(name == null)
		{
			return null;
		}
		
		int ix = name.lastIndexOf('.');
		if(ix >= 0)
		{
			return name.substring(0, ix);
		}
		else
		{
			return name;
		}
	}
}