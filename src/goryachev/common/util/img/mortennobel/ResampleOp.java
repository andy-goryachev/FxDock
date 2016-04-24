/*
 * Copyright 2013, Morten Nobel-Joergensen
 *
 * License: The BSD 3-Clause License
 * http://opensource.org/licenses/BSD-3-Clause
 */
package goryachev.common.util.img.mortennobel;
import goryachev.common.util.CJob;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Based on work from Java Image Util ( http://schmidt.devlib.org/jiu/ )
 * Note that the filter method is not thread safe.
 *
 * @author Morten Nobel-Joergensen
 * @author Heinz Doerr
 */
public class ResampleOp
	extends AdvancedResizeOp
{
	public static class SubSamplingData
	{
		protected int[] arrN; // individual - per row or per column - nr of contributions
		protected int[] arrPixel; // 2Dim: [wid or hei][contrib]
		protected float[] arrWeight; // 2Dim: [wid or hei][contrib]
		protected int numContributors; // the primary index length for the 2Dim arrays : arrPixel and arrWeight


		protected SubSamplingData(int[] arrN, int[] arrPixel, float[] arrWeight, int numContributors)
		{
			this.arrN = arrN;
			this.arrPixel = arrPixel;
			this.arrWeight = arrWeight;
			this.numContributors = numContributors;
		}
		
		
		public void gc()
		{
			arrN = null;
			arrPixel = null;
			arrWeight = null;
		}
	}
	
	//
		
	private int nrChannels;
	private int srcWidth;
	private int srcHeight;
	private int dstWidth;
	private int dstHeight;
	private SubSamplingData horizontalSubsamplingData;
	private SubSamplingData verticalSubsamplingData;
	private int processedItems;
	private float totalItems;
	protected int numberOfThreads = Runtime.getRuntime().availableProcessors();
	private AtomicInteger multipleInvocationLock = new AtomicInteger();
	private ResampleFilter filter;
	protected byte[][] workPixels;
	protected byte[] outPixels;
	private static final byte MAX_CHANNEL_VALUE = (byte)255;


	public ResampleOp(int destWidth, int destHeight)
	{
		this(DimensionConstraint.createAbsolutionDimension(destWidth, destHeight));
	}


	public ResampleOp(DimensionConstraint dimensionConstrain)
	{
		super(dimensionConstrain);
	}


	public ResampleFilter getFilter()
	{
		if(filter == null)
		{
			filter = new Lanczos3Filter();
		}
		return filter;
	}


	public void setFilter(ResampleFilter filter)
	{
		this.filter = filter;
	}


	protected void setProgress()
	{
		fireProgressChanged(processedItems / totalItems);
	}


	protected int getResultBufferedImageType(BufferedImage srcImg)
	{
		return nrChannels == 3 ? BufferedImage.TYPE_3BYTE_BGR : (nrChannels == 4 ? BufferedImage.TYPE_4BYTE_ABGR : (srcImg.getSampleModel().getDataType() == DataBuffer.TYPE_USHORT ? BufferedImage.TYPE_USHORT_GRAY : BufferedImage.TYPE_BYTE_GRAY));
	}


	public int getNumberOfThreads()
	{
		return numberOfThreads;
	}


	public void setNumberOfThreads(int numberOfThreads)
	{
		this.numberOfThreads = numberOfThreads;
	}
	
	
	private static byte toByte(float f)
	{
		if(f < 0f)
		{
			return 0;
		}
		if(f > 255f)
		{
			return MAX_CHANNEL_VALUE;
		}
		return (byte)(f + 0.5f); // add 0.5 same as Math.round
	}


	public BufferedImage doFilter(BufferedImage src, BufferedImage dst, int dstWidth, int dstHeight)
	{
		this.dstWidth = dstWidth;
		this.dstHeight = dstHeight;

		if(dstWidth < 3 || dstHeight < 3)
		{
			throw new RuntimeException("Error doing rescale. Target size was " + dstWidth + "x" + dstHeight + " but must be at least 3x3.");
		}

		assert multipleInvocationLock.incrementAndGet() == 1 : "Multiple concurrent invocations detected";

		if(src.getType() == BufferedImage.TYPE_BYTE_BINARY || src.getType() == BufferedImage.TYPE_BYTE_INDEXED || src.getType() == BufferedImage.TYPE_CUSTOM)
		{
			src = ResampleTools.convert(src, src.getColorModel().hasAlpha() ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR);
		}

		this.nrChannels = ResampleTools.nrChannels(src);
		assert nrChannels > 0;
		this.srcWidth = src.getWidth();
		this.srcHeight = src.getHeight();
		this.workPixels = new byte[srcHeight][dstWidth * nrChannels];
		this.processedItems = 0;
		this.totalItems = srcHeight + dstWidth;

		// Pre-calculate sub-sampling
		ResampleFilter f = getFilter();
		horizontalSubsamplingData = createSubSampling(f, srcWidth, dstWidth);
		verticalSubsamplingData = createSubSampling(f, srcHeight, dstHeight);

		// horizontal
		final BufferedImage srcCopy = src;
		int sz = numberOfThreads - 1;
		CJob[] jobs = new CJob[sz];

		for(int i=0; i<sz; i++)
		{
			final int start = i + 1;
			
			CJob j = new CJob()
			{
				protected void process() throws Exception
				{
					horizontallyFromSrcToWork(srcCopy, workPixels, start, numberOfThreads);
				}
			};
			jobs[i] = j;
			j.submit();
		}
		horizontallyFromSrcToWork(srcCopy, workPixels, 0, numberOfThreads);
		
		CJob.waitForAll(jobs);

		// vertical
		outPixels = new byte[dstWidth * dstHeight * nrChannels];
		
		for(int i=0; i<sz; i++)
		{
			final int start = i + 1;
			
			CJob j = new CJob()
			{
				protected void process() throws Exception
				{
					verticalFromWorkToDst(workPixels, outPixels, start, numberOfThreads);
				}
			};
			jobs[i] = j;
			j.submit();
		}
		verticalFromWorkToDst(workPixels, outPixels, 0, numberOfThreads);
		
		CJob.waitForAll(jobs);

		workPixels = null; // gc

		BufferedImage out;
		if(dst != null && dstWidth == dst.getWidth() && dstHeight == dst.getHeight())
		{
			out = dst;
			int nrDestChannels = ResampleTools.nrChannels(dst);
			if(nrDestChannels != nrChannels)
			{
				String errorMgs = String.format("Destination image must be compatible width source image. Source image had %d channels destination image had %d channels", nrChannels, nrDestChannels);
				throw new RuntimeException(errorMgs);
			}
		}
		else
		{
			out = new BufferedImage(dstWidth, dstHeight, getResultBufferedImageType(src));
		}

		ResampleTools.setBGRPixels(outPixels, out, 0, 0, dstWidth, dstHeight);
		
		outPixels = null; // gc
		horizontalSubsamplingData.gc();
		verticalSubsamplingData.gc();

		assert multipleInvocationLock.decrementAndGet() == 0 : "Multiple concurrent invocations detected";

		return out;
	}


	protected static SubSamplingData createSubSampling(ResampleFilter filter, int srcSize, int dstSize)
	{
		float scale = (float)dstSize / (float)srcSize;
		int[] arrN = new int[dstSize];
		int numContributors;
		float[] arrWeight;
		int[] arrPixel;
		float fwidth = filter.getSamplingRadius();
		float centerOffset = 0.5f / scale;

		if(scale < 1.0f)
		{
			float width = fwidth / scale;
			numContributors = (int)(width * 2.0f + 2); // Heinz: added 1 to be save with the ceilling
			arrWeight = new float[dstSize * numContributors];
			arrPixel = new int[dstSize * numContributors];

			float fNormFac = (float)(1f / (Math.ceil(width) / fwidth));
			//
			for(int i=0; i<dstSize; i++)
			{
				int subindex = i * numContributors;
				float center = i / scale + centerOffset;
				int left = (int)Math.floor(center - width);
				int right = (int)Math.ceil(center + width);
				
				for(int j=left; j<=right; j++)
				{
					float weight;
					weight = filter.apply((center - j) * fNormFac);

					if(weight == 0.0f)
					{
						continue;
					}
					
					int n;
					if(j < 0)
					{
						n = -j;
					}
					else if(j >= srcSize)
					{
						n = srcSize - j + srcSize - 1;
					}
					else
					{
						n = j;
					}
					
					int k = arrN[i];
					arrN[i]++;
					if(n < 0 || n >= srcSize)
					{
						weight = 0.0f; // flag that cell should not be used
					}
					
					arrPixel[subindex + k] = n;
					arrWeight[subindex + k] = weight;
				}
				
				// normalize the filter's weight's so the sum equals to 1.0, very important for avoiding box type of artifacts
				int max = arrN[i];
				float tot = 0;
				for(int k=0; k<max; k++)
				{
					tot += arrWeight[subindex + k];
				}
				
				if(tot != 0f)
				{ 
					// 0 should never happen except bug in filter
					for(int k=0; k<max; k++)
					{
						arrWeight[subindex + k] /= tot;
					}
				}
			}
		}
		else
		{
			// super-sampling
			// Scales from smaller to bigger height
			numContributors = (int)(fwidth * 2.0f + 1);
			arrWeight = new float[dstSize * numContributors];
			arrPixel = new int[dstSize * numContributors];

			for(int i=0; i<dstSize; i++)
			{
				int subindex = i * numContributors;
				float center = i / scale + centerOffset;
				int left = (int)Math.floor(center - fwidth);
				int right = (int)Math.ceil(center + fwidth);
				
				for(int j=left; j<=right; j++)
				{
					float weight = filter.apply(center - j);
					if(weight == 0.0f)
					{
						continue;
					}
					
					int n;
					if(j < 0)
					{
						n = -j;
					}
					else if(j >= srcSize)
					{
						n = srcSize - j + srcSize - 1;
					}
					else
					{
						n = j;
					}
					
					int k = arrN[i];
					arrN[i]++;
					if(n < 0 || n >= srcSize)
					{
						weight = 0.0f; // flag that cell should not be used
					}
					
					arrPixel[subindex + k] = n;
					arrWeight[subindex + k] = weight;
				}
				
				// normalize the filter's weight's so the sum equals to 1.0, very important for avoiding box type of artifacts
				int max = arrN[i];
				float tot = 0;
				
				for(int k=0; k<max; k++)
				{
					tot += arrWeight[subindex + k];
				}
				
				assert tot != 0 : "should never happen except bug in filter";
				
				if(tot != 0f)
				{
					for(int k=0; k<max; k++)
					{
						arrWeight[subindex + k] /= tot;
					}
				}
			}
		}
		
		return new SubSamplingData(arrN, arrPixel, arrWeight, numContributors);
	}


	protected void verticalFromWorkToDst(byte[][] workPixels, byte[] outPixels, int start, int delta)
	{
		if(nrChannels == 1)
		{
			verticalFromWorkToDstGray(workPixels, outPixels, start, numberOfThreads);
			return;
		}
		
		boolean useChannel3 = nrChannels > 3;
		for(int x=start; x<dstWidth; x += delta)
		{
			int xLocation = x * nrChannels;
			for(int y=dstHeight-1; y>=0; y--)
			{
				int yTimesNumContributors = y * verticalSubsamplingData.numContributors;
				int max = verticalSubsamplingData.arrN[y];
				int sampleLocation = (y * dstWidth + x) * nrChannels;
				float sample0 = 0.0f;
				float sample1 = 0.0f;
				float sample2 = 0.0f;
				float sample3 = 0.0f;
				int index = yTimesNumContributors;
				
				for(int j=max-1; j>=0; j--)
				{
					int valueLocation = verticalSubsamplingData.arrPixel[index];
					float arrWeight = verticalSubsamplingData.arrWeight[index];
					sample0 += (workPixels[valueLocation][xLocation] & 0xff) * arrWeight;
					sample1 += (workPixels[valueLocation][xLocation + 1] & 0xff) * arrWeight;
					sample2 += (workPixels[valueLocation][xLocation + 2] & 0xff) * arrWeight;
					if(useChannel3)
					{
						sample3 += (workPixels[valueLocation][xLocation + 3] & 0xff) * arrWeight;
					}

					index++;
				}

				outPixels[sampleLocation] = toByte(sample0);
				outPixels[sampleLocation + 1] = toByte(sample1);
				outPixels[sampleLocation + 2] = toByte(sample2);
				if(useChannel3)
				{
					outPixels[sampleLocation + 3] = toByte(sample3);
				}
			}
			
			processedItems++;
			if(start == 0)
			{ 
				// only update progress listener from main thread
				setProgress();
			}
		}
	}


	protected void verticalFromWorkToDstGray(byte[][] workPixels, byte[] outPixels, int start, int delta)
	{
		for(int x=start; x<dstWidth; x+=delta)
		{
			int xLocation = x;
			
			for(int y=dstHeight-1; y>=0; y--)
			{
				int yTimesNumContributors = y * verticalSubsamplingData.numContributors;
				int max = verticalSubsamplingData.arrN[y];
				int sampleLocation = (y * dstWidth + x);
				float sample0 = 0.0f;
				int index = yTimesNumContributors;

				for(int j=max-1; j>=0; j--)
				{
					int valueLocation = verticalSubsamplingData.arrPixel[index];
					float arrWeight = verticalSubsamplingData.arrWeight[index];
					sample0 += (workPixels[valueLocation][xLocation] & 0xff) * arrWeight;

					index++;
				}

				outPixels[sampleLocation] = toByte(sample0);
			}
			
			processedItems++;
			if(start == 0)
			{
				// only update progress listener from main thread
				setProgress();
			}
		}
	}


	/**
	 * Apply filter to sample horizontally from Src to Work
	 * @param srcImg
	 * @param workPixels
	 */
	protected void horizontallyFromSrcToWork(BufferedImage srcImg, byte[][] workPixels, int start, int delta)
	{
		if(nrChannels == 1)
		{
			horizontallyFromSrcToWorkGray(srcImg, workPixels, start, delta);
			return;
		}
		
		int[] tempPixels = new int[srcWidth]; // Used if we work on int based bitmaps, later used to keep channel values
		byte[] srcPixels = new byte[srcWidth * nrChannels]; // create reusable row to minimize memory overhead
		boolean useChannel3 = nrChannels > 3;

		for(int k=start; k<srcHeight; k+=delta)
		{
			ResampleTools.getPixelsBGR(srcImg, k, srcWidth, srcPixels, tempPixels);

			for(int i=dstWidth-1; i>=0; i--)
			{
				int sampleLocation = i * nrChannels;
				int max = horizontalSubsamplingData.arrN[i];

				float sample0 = 0.0f;
				float sample1 = 0.0f;
				float sample2 = 0.0f;
				float sample3 = 0.0f;
				int index = i * horizontalSubsamplingData.numContributors;
				
				for(int j=max-1; j>=0; j--)
				{
					float arrWeight = horizontalSubsamplingData.arrWeight[index];
					int pixelIndex = horizontalSubsamplingData.arrPixel[index] * nrChannels;

					sample0 += (srcPixels[pixelIndex] & 0xff) * arrWeight;
					sample1 += (srcPixels[pixelIndex + 1] & 0xff) * arrWeight;
					sample2 += (srcPixels[pixelIndex + 2] & 0xff) * arrWeight;
					if(useChannel3)
					{
						sample3 += (srcPixels[pixelIndex + 3] & 0xff) * arrWeight;
					}
					
					index++;
				}

				workPixels[k][sampleLocation] = toByte(sample0);
				workPixels[k][sampleLocation + 1] = toByte(sample1);
				workPixels[k][sampleLocation + 2] = toByte(sample2);
				if(useChannel3)
				{
					workPixels[k][sampleLocation + 3] = toByte(sample3);
				}
			}
			
			processedItems++;
			if(start == 0)
			{ 
				// only update progress listener from main thread
				setProgress();
			}
		}
	}


	/**
	 * Apply filter to sample horizontally from Src to Work
	 * @param srcImg
	 * @param workPixels
	 */
	protected void horizontallyFromSrcToWorkGray(BufferedImage srcImg, byte[][] workPixels, int start, int delta)
	{
		int[] tempPixels = new int[srcWidth]; // Used if we work on int based bitmaps, later used to keep channel values
		byte[] srcPixels = new byte[srcWidth]; // create reusable row to minimize memory overhead

		for(int k=start; k<srcHeight; k+=delta)
		{
			ResampleTools.getPixelsBGR(srcImg, k, srcWidth, srcPixels, tempPixels);

			for(int i=dstWidth-1; i>=0; i--)
			{
				int sampleLocation = i;
				int max = horizontalSubsamplingData.arrN[i];
				float sample0 = 0.0f;
				int index = i * horizontalSubsamplingData.numContributors;
				
				for(int j=max-1; j>=0; j--)
				{
					float arrWeight = horizontalSubsamplingData.arrWeight[index];
					int pixelIndex = horizontalSubsamplingData.arrPixel[index];

					sample0 += (srcPixels[pixelIndex] & 0xff) * arrWeight;
					index++;
				}

				workPixels[k][sampleLocation] = toByte(sample0);
			}
			
			processedItems++;
			if(start == 0)
			{ 
				// only update progress listener from main thread
				setProgress();
			}
		}
	}
}
