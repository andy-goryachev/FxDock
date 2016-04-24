/*
Software License Agreement (BSD License)
Copyright (c) 2005-2007 by Matthias Hertel, http://www.mathertel.de/
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of the copyright owners nor the names of its contributors 
   may be used to endorse or promote products derived from this software without
    specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER 
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS 
OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
OF SUCH DAMAGE.



This Class implements the Difference Algorithm published in
"An O(ND) Difference Algorithm and its Variations" by Eugene Myers
Algorithmica Vol. 1 No. 2, 1986, p 251.  
 
There are many C, Java, Lisp implementations public available but they all seem to come
from the same source (diffutils) that is under the (unfree) GNU public License
and cannot be reused as a sourcecode for a commercial application.
There are very old C implementations that use other (worse) algorithms.
Microsoft also published sourcecode of a diff-tool (windiff) that uses some tree data.
Also, a direct transfer from a C source to C# is not easy because there is a lot of pointer
arithmetic in the typical C solutions and i need a managed solution.
These are the reasons why I implemented the original published algorithm from the scratch and
make it avaliable without the GNU license limitations.
I do not need a high performance diff tool because it is used only sometimes.
I will do some performace tweaking when needed.
 
The algorithm itself is comparing 2 arrays of numbers so when comparing 2 text documents
each line is converted into a (hash) number. See DiffText(). 
 
Some chages to the original algorithm:
The original algorithm was described using a recursive approach and comparing zero indexed arrays.
Extracting sub-arrays and rejoining them is very performance and memory intensive so the same
(readonly) data arrays are passed arround together with their lower and upper bounds.
This circumstance makes the LCS and SMS functions more complicate.
I added some code to the LCS function to get a fast response on sub-arrays that are identical,
completely deleted or inserted.
 
The result from a comparisation is stored in 2 arrays that flag for modified (deleted or inserted)
lines in the 2 data arrays. These bits are then analysed to produce a array of Item objects.
 
In SMS is a lot of boundary arithmetic in the for-D and for-k loops that can be done by increment
and decrement of local variables.
 
diff.cs: A port of the algorythm to C#
Created by Matthias Hertel, see http://www.mathertel.de
This work is licensed under a BSD style license. See http://www.mathertel.de/License.aspx
 
Changes:
2002.09.20 There was a "hang" in some situations.
           Now I undestand a little bit more of the SMS algorithm. 
           There have been overlapping boxes; that where analyzed partial differently.
           One return-point is enough.
           An assertion was added in CreateDiffs when in debug-mode, that counts the number of equal 
           (not modified) lines in both arrays.  They must be identical.
2003.02.07 Out of bounds error in the Up/Down vector arrays in some situations.
           The two vetors are now accessed using different offsets that are adjusted using the start k-Line. 
           A test case is added. 
2006.03.05 Some documentation and a direct Diff entry point.
2006.03.08 Refactored the API to static methods on the Diff class to make usage simpler.
2006.03.10 using the standard Debug class for self-test now.
           compile with: csc /target:exe /out:diffTest.exe /d:DEBUG /d:TRACE /d:SELFTEST Diff.cs
2007.01.05 The license changed to a <a rel="license" href="http://www.mathertel.de/License.aspx">BSD style license</a>.
2007.06.03 added the Optimize method.
2007.09.23 UpVector and DownVector optimization by Jan Stoklasa ().
2007.11.17 Ported to Java by Andy Goryachev <andy@goryachev.com>
 
*/

package goryachev.common.util.diff;
import java.util.Hashtable;


// http://www.mathertel.de/
public class MyersDiff
{
	public static interface Client
	{
		public void intact(int start, int size);
		
		public void deleted(int start, int size);
		
		public void inserted(int start, int startB, int sizeB);
	}
	

	// Find the difference in 2 texts, comparing by textlines.
	// 
	// <param name="TextA">A-version of the text (usualy the old one)</param>
	// <param name="TextB">B-version of the text (usualy the new one)</param>
	// <returns>Returns a array of Items that describe the differences.</returns>
//	public static Item[] diffLines(String a, String b)
//	{
//		return diffLines(a, b, false, false, false);
//	}


	// Find the difference in 2 text documents, comparing by textlines.
	// The algorithm itself is comparing 2 arrays of numbers so when comparing 2 text documents
	// each line is converted into a (hash) number. This hash-value is computed by storing all
	// textlines into a common hashtable so i can find dublicates in there, and generating a 
	// new number each time a new textline is inserted.
	// 
	// <param name="a">A-version of the text (usualy the old one)</param>
	// <param name="b">B-version of the text (usualy the new one)</param>
	// <param name="trimSpace">When set to true, all leading and trailing whitespace characters are stripped out before the comparation is done.</param>
	// <param name="ignoreSpace">When set to true, all whitespace characters are converted to a single space character before the comparation is done.</param>
	// <param name="ignoreCase">When set to true, all characters are converted to their lowercase equivivalence before the comparation is done.</param>
	// <returns>Returns a array of Items that describe the differences.</returns>
//	public static Item[] diffLines(String a, String b, boolean trimSpace, boolean ignoreSpace, boolean ignoreCase)
//	{
//		// prepare the input-text and convert to comparable numbers.
//		Hashtable h = new Hashtable(a.length() + b.length());
//		// The A-Version of the data (original data) to be compared.
////		DiffData dataA = new DiffData(diffCodes(a, h, trimSpace, ignoreSpace, ignoreCase));
////		// The B-Version of the data (modified data) to be compared.
////		DiffData dataB = new DiffData(diffCodes(b, h, trimSpace, ignoreSpace, ignoreCase));
//		
//		return new MyersDiff(diffCodes(a, h, trimSpace, ignoreSpace, ignoreCase), diffCodes(b, h, trimSpace, ignoreSpace, ignoreCase)).compute();
//
////		int MAX = dataA.length + dataB.length + 1;
////		// vector for the (0,0) to (x,y) search
////		int[] downVector = new int[2 * MAX + 2];
////		// vector for the (u,v) to (N,M) search
////		int[] upVector = new int[2 * MAX + 2];
////		
////		findLCS(dataA, 0, dataA.length, dataB, 0, dataB.length, downVector, upVector);
////		optimize(dataA);
////		optimize(dataB);
////		return createDiffs(dataA, dataB);
//	}
	
	
	//
	
	
	protected int[] dataA;
	protected int[] dataB;
	private boolean[] modifiedA;
	private boolean[] modifiedB;
	private int[] downVector;
	private int[] upVector;
	
	
	public MyersDiff(int[] a, int[] b) throws InterruptedException
	{
		dataA = a;
		dataB = b;
		
		modifiedA = new boolean[a.length + 2];
		modifiedB = new boolean[b.length + 2];
		
		int sz = 2 * (dataA.length + dataB.length + 2);
		downVector = new int[sz];
		upVector = new int[sz];
	}
	
	
	public void compute() throws InterruptedException
	{
		findLongestCommonSequence(0, dataA.length, 0, dataB.length);
	}
	
	
	public void computeAndOptimize() throws InterruptedException
	{
		findLongestCommonSequence(0, dataA.length, 0, dataB.length);
		optimize(dataA,modifiedA);
		optimize(dataB,modifiedB);
	}
	
	
//	protected Item[] compute()
//	{
//		findLongestCommonSequence(0, dataA.length, 0, dataB.length);
//		optimize(dataA,modifiedA);
//		optimize(dataB,modifiedB);
//		return createDiffs();
//	}

	
	// If a sequence of modified lines starts with a line that contains the same content
	// as the line that appends the changes, the difference sequence is modified so that the
	// appended line and not the starting line is marked as modified.
	// This leads to more readable diff sequences when comparing text files.
	// <param name="Data">A Diff data buffer containing the identified changes.</param>
	private static void optimize(int[] data, boolean[] modified)
	{
		int start = 0;
		int end;

		while(start < data.length)
		{
			while((start < data.length) && (modified[start] == false))
			{
				start++;
			}
			end = start;
			while((end < data.length) && (modified[end] == true))
			{
				end++;
			}

			if((end < data.length) && (data[start] == data[end]))
			{
				modified[start] = false;
				modified[end] = true;
			}
			else
			{
				start = end;
			}
		}
	}


	// This function converts all textlines of the text into unique numbers for every unique textline
	// so further work can work only with simple numbers.
	// 
	// <param name="aText">the input text</param>
	// <param name="h">This extern initialized hashtable is used for storing all ever used textlines.</param>
	// <param name="trimSpace">ignore leading and trailing space characters</param>
	// <returns>a array of integers.</returns>
	protected static int[] diffCodes(String text, Hashtable h, boolean trimSpace, boolean ignoreSpace, boolean ignoreCase)
	{
		// get all codes of the text
		//int lastUsedCode = h.size();

		// strip off all cr, only use lf as textline separator.
		text = text.replace("\r", "");
		String[] lines = text.split("\n");
		int[] codes = new int[lines.length];

		for(int i=0; i<lines.length; ++i)
		{
			String s = lines[i];
			if(trimSpace)
			{
				s = s.trim();
			}

			if(ignoreSpace)
			{
				s = s.replace("\\s+", " "); // TODO: optimization: faster blank removal.
			}

			if(ignoreCase)
			{
				s = s.toLowerCase();
			}

			Object aCode = h.get(s);
			if(aCode == null)
			{
				codes[i] = h.size();
				h.put(s,h.size());
			}
			else
			{
				codes[i] = ((Integer)aCode).intValue();
			}
		}
		return codes;
	}


	// This is the algorithm to find the Shortest Middle Snake
	// 
	// lowerA - lower bound of the actual range in DataA
	// upperA - upper bound of the actual range in DataA (exclusive)
	// lowerB - lower bound of the actual range in DataB
	// upperB - upper bound of the actual range in DataB (exclusive)
	//
	// returns a ShortestMiddleSnake record containing x,y and u,v
	//
	private ShortestMiddleSnake findShortestMiddleSnake(int lowerA, int upperA, int lowerB, int upperB) throws InterruptedException
	{
		int max = dataA.length + dataB.length + 1;

		int downK = lowerA - lowerB; // the k-line to start the forward search
		int upK = upperA - upperB; // the k-line to start the reverse search

		int delta = (upperA - lowerA) - (upperB - lowerB);
		boolean oddDelta = (delta & 1) != 0;

		// The vectors in the publication accepts negative indexes. the vectors implemented here are 0-based
		// and are access using a specific offset: upOffset upVector and DownOffset for downVector
		int downOffset = max - downK;
		int upOffset = max - upK;
		int maxD = ((upperA - lowerA + upperB - lowerB) / 2) + 1;

		// init vectors
		downVector[downOffset + downK + 1] = lowerA;
		upVector[upOffset + upK - 1] = upperA;

		for(int d=0; d <= maxD; d++)
		{
			// is this a good point for interruption?
			if(Thread.currentThread().isInterrupted())
			{
				throw new InterruptedException();
			}
			
			// Extend the forward path.
			for(int k = downK - d; k <= downK + d; k += 2)
			{
				// find the only or better starting point
				int x, y;
				if(k == downK - d)
				{
					x = downVector[downOffset + k + 1]; // down
				}
				else
				{
					x = downVector[downOffset + k - 1] + 1; // a step to the right
					if((k < downK + d) && (downVector[downOffset + k + 1] >= x))
					{
						x = downVector[downOffset + k + 1]; // down
					}
				}
				y = x - k;

				// find the end of the furthest reaching forward D-path in diagonal k.
				while((x < upperA) && (y < upperB) && (dataA[x] == dataB[y]))
				{
					x++;
					y++;
				}
				downVector[downOffset + k] = x;

				// overlap ?
				if(oddDelta && (upK - d < k) && (k < upK + d))
				{
					if(upVector[upOffset + k] <= downVector[downOffset + k])
					{
						return new ShortestMiddleSnake(downVector[downOffset + k], downVector[downOffset + k] - k);
					}
				}
			}

			// Extend the reverse path.
			for(int k = upK - d; k <= upK + d; k += 2)
			{
				// find the only or better starting point
				int x, y;
				if(k == upK + d)
				{
					x = upVector[upOffset + k - 1]; // up
				}
				else
				{
					x = upVector[upOffset + k + 1] - 1; // left
					if((k > upK - d) && (upVector[upOffset + k - 1] < x))
					{
						x = upVector[upOffset + k - 1]; // up
					}
				}
				y = x - k;

				while((x > lowerA) && (y > lowerB) && (dataA[x - 1] == dataB[y - 1]))
				{
					x--;
					y--; // diagonal
				}
				upVector[upOffset + k] = x;

				// overlap ?
				if(!oddDelta && (downK - d <= k) && (k <= downK + d))
				{
					if(upVector[upOffset + k] <= downVector[downOffset + k])
					{
						return new ShortestMiddleSnake(downVector[downOffset + k], downVector[downOffset + k] - k);
					}
				}
			}
		} 

		throw new RuntimeException("the algorithm should never come here.");
	}


	// This is the divide-and-conquer implementation of the longest common-subsequence (LCS) 
	// algorithm.
	// The published algorithm passes recursively parts of the A and B sequences.
	// To avoid copying these arrays the lower and upper bounds are passed while the sequences stay constant.
	// 
	// dataA  - sequence A
	// lowerA - lower bound of the actual range in DataA
	// upperA - upper bound of the actual range in DataA (exclusive)
	// dataB  - sequence B
	// lowerB - lower bound of the actual range in DataB
	// upperB - upper bound of the actual range in DataB (exclusive)
	//
	private void findLongestCommonSequence(int lowerA, int upperA, int lowerB, int upperB) throws InterruptedException
	{
		// Fast walkthrough equal lines at the start
		while((lowerA < upperA) && (lowerB < upperB) && (dataA[lowerA] == dataB[lowerB]))
		{
			lowerA++;
			lowerB++;
		}

		// Fast walkthrough equal lines at the end
		while((lowerA < upperA) && (lowerB < upperB) && (dataA[upperA - 1] == dataB[upperB - 1]))
		{
			--upperA;
			--upperB;
		}

		if(lowerA == upperA)
		{
			// mark as inserted lines.
			while(lowerB < upperB)
			{
				modifiedB[lowerB++] = true;
			}
		}
		else if(lowerB == upperB)
		{
			// mark as deleted lines.
			while(lowerA < upperA)
			{
				modifiedA[lowerA++] = true;
			}
		}
		else
		{
			// Find the middle snake and length of an optimal path for A and B
			ShortestMiddleSnake s = findShortestMiddleSnake(lowerA, upperA, lowerB, upperB);

			// The path is from LowerX to (x,y) and (x,y) ot UpperX
			findLongestCommonSequence(lowerA, s.x, lowerB, s.y);
			findLongestCommonSequence(s.x, upperA, s.y, upperB); 
		}
	}
	
		
	// send changes to the client
	// - intact [start,end]
	// - inserted[start,Bstart,Bend]
	// - deleted[start,end]
	public void generateChangeList(Client client)
	{
		int lineA = 0;
		int lineB = 0;
		int start = 0;
		while((lineA < dataA.length) || (lineB < dataB.length))
		{
			if((lineA < dataA.length) && (!modifiedA[lineA]) && (lineB < dataB.length) && (!modifiedB[lineB]))
			{
				// equal lines
				lineA++;
				lineB++;
			}
			else
			{
				// maybe deleted and/or inserted lines
				int startA = lineA;
				int startB = lineB;
				if(start < lineA)
				{
					client.intact(start, lineA - start);
				}

				while((lineA < dataA.length) && (lineB >= dataB.length || modifiedA[lineA]))
				{
					lineA++;
				}
				
				while((lineB < dataB.length) && (lineA >= dataA.length || modifiedB[lineB]))
				{
					lineB++;
				}
				
				if(startA < lineA)
				{
					client.deleted(startA, lineA - startA);
				}
				
				if(startB < lineB)
				{
					client.inserted(startA, startB, lineB - startB);
				}

				start = lineA;
			}
		}
		
		if(start < lineA)
		{
			client.intact(start, lineA - start);
		}
	}
}
