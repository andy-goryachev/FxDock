// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Iterator;


/** 
 * Immutable, memory efficient range of comparable values which allows discontiguous sub-ranges.
 * This implementation provides support for integer, long and BigInteger ranges.
 */
public abstract class Range<T extends Comparable<T>>
	implements Iterable<T>, Serializable
{
	public abstract int getItemCountInt();
	
	public abstract long getItemCountLong();
	
	public abstract BigInteger getItemCountBigInteger();
	
	protected abstract T createSmallNumber(int smallNumber);
	
	protected abstract T add(T a, T b);
	
	protected abstract T subtract(T a, T b);
	
	protected abstract T increment(T x);
	
	protected abstract T decrement(T x);
	
	protected abstract Range<T> create(CList<T> ranges);
			
	//
	
	private CList<T> ranges; // (smaller, larger), (smaller, larger), ... progressively larger
	public static final char DELIMITER = ',';
	public static final char SEPARATOR = ':';
	
	
	public Range(T startInclusive, T endInclusive)
	{
		if(startInclusive.compareTo(endInclusive) > 0)
		{
			throw new IllegalArgumentException("invalid range: " + startInclusive + " ... " + endInclusive);
		}
		
		ranges = new CList();
		ranges.add(startInclusive);
		ranges.add(endInclusive);
	}
	
	
	public Range()
	{
		ranges = new CList();
	}
	
	
	protected Range(CList<T> r)
	{
		ranges = new CList(r);
		
		// validate
		int sz = ranges.size();
		if(CKit.isOdd(sz))
		{
			throw new Error("should have even number of elements (min,max)[]");
		}
		
		for(int i=0; i<sz-1; i++)
		{
			Object min = ranges.get(i);
			Object max = ranges.get(i + 1);
			
			int n = ((Comparable)min).compareTo(max);
			if(n > 0)
			{
				throw new Error("elements in sequence (min,max)[] should increase monotonically");
			}
		}
	}
	
	
	public Iterator<T> iterator()
	{
		return new RangeIterator(ranges);
	}
	
	
	public IntervalStream<T> getIntervalStream()
	{
		return new IntervalStream<T>(ranges, false);
	}
	
	
	public IntervalStream<T> getIntervalStreamReverse()
	{
		return new IntervalStream<T>(ranges, true);
	}
	
	
	public String toString()
	{
		SB sb = new SB();
		sb.a("Range[");

		buildPattern(sb);
		
		sb.a("]");
		return sb.toString();
	}
	
	
	public String toPattern()
	{
		SB sb = new SB();
		buildPattern(sb);
		return sb.toString();
	}
	
	
	protected void buildPattern(SB sb)
	{
		IntervalStream ss = getIntervalStream();
		boolean comma = false;
		while(ss.hasNext())
		{
			if(comma)
			{
				sb.a(DELIMITER);
			}
			else
			{
				comma = true;
			}
			
			Interval s = ss.next();
			if(s.isPoint())
			{
				sb.a(s.min);
			}
			else
			{
				sb.a(s.min);
				sb.a(SEPARATOR);
				sb.a(s.max);
			}
		}
	}
	
	
	protected T itemCount()
	{
		T count = null;
		
		IntervalStream<T> it = getIntervalStream();
		while(it.hasNext())
		{
			Interval<T> n = it.next();
			count = addPrivate(count, getItemCount(n));
		}
		
		return count;
	}
	
	
	protected T addPrivate(T a, T b)
	{
		if(a == null)
		{
			if(b == null)
			{
				return null;
			}
			else
			{
				return b;
			}
		}
		else
		{
			if(b == null)
			{
				return a;
			}
			else
			{
				return add(a, b);
			}
		}
	}
	
		
	public T getMinimum()
	{
		if(ranges.size() == 0)
		{
			return null;
		}
		return ranges.get(0);
	}

	
	public T getMaximum()
	{
		if(ranges.size() == 0)
		{
			return null;
		}
		return ranges.get(ranges.size() - 1);
	}
	
	
	public boolean contains(T item)
	{
		int sz = ranges.size();
		if(sz > 0)
		{
			for(int i=0; i<sz-1; i+=2)
			{
				T min = ranges.get(i);
				T max = ranges.get(i + 1);
				
				int cmin = item.compareTo(min);
				if(cmin < 0)
				{
					// * [------]
					return false;
				}
				
				int cmax = item.compareTo(max);
				if((cmin >= 0) && (cmax <= 0))
				{
					// [---*---]
					return true;
				}
			}
		}
		return false;
	}
	
	
	protected Range<T> empty()
	{
		return create(new CList());
	}
	
	
	protected Range<T> create(T min, T max)
	{
		CList<T> r = new CList();
		r.add(min);
		r.add(max);
		return create(r);
	}
	
	
	protected T size(Interval<T> n)
	{
		T sz = subtract(n.max, n.min);
		return add(sz, createSmallNumber(1));
	}
	
	
	public Range<T> getRangeLow(T maxCount)
	{
		Range<T> res = empty();
		IntervalStream s = getIntervalStream();
		T left = maxCount;
		T zero = createSmallNumber(0);
		
		for(;;)
		{
			if(left.compareTo(zero) <= 0)
			{
				return res;
			}

			if(s.hasNext())
			{
				Range<T> r;
				Interval<T> n = s.next();
				T sz = size(n);
				if(left.compareTo(sz) >= 0)
				{
					r = create(n.min, n.max);
					res = res.addRange(r);
					left = subtract(left, sz);
				}
				else
				{
					// partial
					T x = add(n.min, left);
					r = create(n.min, decrement(x));
					res = res.addRange(r);
					return res;
				}
			}
			else
			{
				return res;
			}			
		}
	}
	
	
	/** removes count items from the high end of the range */
	public Range<T> removeHigh(T count)
	{
		Range delta = getRangeHigh(count);
		return removeRange(delta);
	}
	
	
	public Range<T> getRangeHigh(T maxCount)
	{
		Range<T> res = empty();
		IntervalStream s = getIntervalStreamReverse();
		T left = maxCount;
		T zero = createSmallNumber(0);
		
		for(;;)
		{
			if(left.compareTo(zero) <= 0)
			{
				return res;
			}

			if(s.hasNext())
			{
				Range<T> r;
				Interval<T> n = s.next();
				T sz = size(n);
				if(left.compareTo(sz) >= 0)
				{
					r = create(n.min, n.max);
					res = res.addRange(r);
					left = subtract(left, sz);
				}
				else
				{
					// partial
					T x = subtract(n.max, left);
					r = create(increment(x), n.max);
					res = res.addRange(r);
					return res;
				}
			}
			else
			{
				return res;
			}			
		}
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof Range)
		{
			return ranges.equals(((Range)x).ranges);
		}

		return false;
	}
	
	
	public int hashCode()
	{
		return Range.class.hashCode() ^ ranges.hashCode(); 
	}
	
	
	protected T getItemCount(Interval<T> n)
	{
		T res = createSmallNumber(1);
		if(!n.isPoint())
		{
			res = add(res, subtract(n.max, n.min));
		}
		return res;
	}
	
	
	protected boolean isAdjacent(T smaller, T larger)
	{
		T a = increment(smaller);
		return a.compareTo(larger) == 0;
	}
	
	
	public Range<T> addRange(Range<T> r)
	{
		CList<T> result = new CList(ranges.size() + r.ranges.size());
		
		IntervalStream me = getIntervalStream();
		IntervalStream other = r.getIntervalStream();
		
		Interval<T> a = null;
		Interval<T> b = null;
		Interval<T> cur = null;
		Interval<T> next = null;
		
		for(;;)
		{
			// pick the smallest interval (cur)
			if(cur == null)
			{
				if(a == null)
				{
					if(me.hasNext())
					{
						a = me.next();
					}
				}
				
				if(b == null)
				{
					if(other.hasNext())
					{
						b = other.next();
					}
				}
				
				if(a == null)
				{
					if(b == null)
					{
						// all done
						break;
					}
					else
					{
						cur = b;
						b = null;
					}
				}
				else
				{
					if(b == null)
					{
						cur = a;
						a = null;
					}
					else
					{
						int d = a.min.compareTo(b.min);
						if(d < 0)
						{
							cur = a;
							a = null;
						}
						else
						{
							cur = b;
							b = null;
						}
					}
				}
			}
			
			// pick the next interval
			if(a == null)
			{
				if(me.hasNext())
				{
					a = me.next();
				}
			}
			
			if(b == null)
			{
				if(other.hasNext())
				{
					b = other.next();
				}
			}
			
			if(a == null)
			{
				if(b == null)
				{
					// add current interval, then done
					result.add(cur.min);
					result.add(cur.max);
					break;
				}
				else
				{
					next = b;
					b = null;
				}
			}
			else
			{
				if(b == null)
				{
					next = a;
					a = null;
				}
				else
				{
					int d = a.min.compareTo(b.min);
					if(d < 0)
					{
						next = a;
						a = null;
					}
					else
					{
						next = b;
						b = null;
					}
				}
			}
			
			// compare cur and next
			if(cur.intersects(next) || isAdjacent(cur.max, next.min))
			{
				// cur absorbs next
				cur.combineIntersecting(next);
				next = null;
			}
			else
			{
				// no intersection, add current
				result.add(cur.min);
				result.add(cur.max);
				cur = next;
				next = null;
			}
		}
		
		return create(result);
	}
	
	
	protected Interval<T> interval(T min, T max)
	{
		int d = min.compareTo(max);
		if(d <= 0)
		{
			return new Interval<T>(min, max);
		}
		else
		{
			return null;
		}
	}
	
	
	public Range<T> removeRange(Range<T> r)
	{
		CList<T> result = new CList<>(ranges.size() + r.ranges.size());
		
		IntervalStream me = getIntervalStream();
		IntervalStream other = r.getIntervalStream();
		
		Interval<T> a = null;
		Interval<T> b = null;
		
		for(;;)
		{
			if(a == null)
			{
				if(me.hasNext())
				{
					a = me.next();
				}
			}
			
			if(a == null)
			{
				// all done
				break;
			}
			
			if(b == null)
			{
				if(other.hasNext())
				{
					b = other.next();
				}
			}
			
			if(b == null)
			{
				result.add(a.min);
				result.add(a.max);
				a = null;
				continue;
			}
			
			int d = a.max.compareTo(b.min);
			if(d < 0)
			{
				// a is smaller than b
				result.add(a.min);
				result.add(a.max);
				a = null;
				continue;
			}
			else if(d == 0)
			{
				// special case: a touches b
				T t = decrement(a.max);
				d = a.min.compareTo(t);
				if(d <= 0)
				{
					result.add(a.min);
					result.add(t);
				}
				a = null;
				
				t = increment(b.min);
				b = interval(t, b.max);
				continue;
			}
			else if(a.intersects(b))
			{
				d = a.max.compareTo(b.max);
				if(d <= 0)
				{
					d = a.min.compareTo(b.min);
					if(d < 0)
					{
						// a.min .. b.min-1
						result.add(a.min);
						result.add(decrement(b.min));
						a = null;
						
						T bmin = increment(b.min);
						b = interval(bmin, b.max);
						continue;
					}
					else
					{
						// a gets removed
						a = null;
						continue;
					}
				}
				else
				{
					d = a.min.compareTo(b.min);
					if(d >= 0)
					{
						// retry with b.max+1 .. a.max
						a = interval(increment(b.max), a.max);
						b = null;
						continue;
					}
					else
					{
						// [a.min .. b.min-1], continue with a = [b.max+1, a.max]
						result.add(a.min);
						result.add(decrement(b.min)); // check for 0?
						
						a = interval(increment(b.max), a.max);
						b = null;
						continue;
					}
				}
			}
			else
			{
				// b is smaller than a, no action
				b = null;
				continue;
			}
		}
		
		return create(result);
	}
	
	
	public static Range.BIG parseBigInteger(String s) throws Exception
	{
		return new Range.BIG(new RangeParser<BigInteger>()
		{
			protected BigInteger parseValue(String s) throws Exception
			{
				return new BigInteger(s);
			}
		}.parse(s));
	}

	
	public static Range.INT parseInt(String s) throws Exception
	{
		return new Range.INT(new RangeParser<Integer>()
		{
			protected Integer parseValue(String s) throws Exception
			{
				return Integer.parseInt(s);
			}
		}.parse(s));
	}


	public static Range.LONG parseLong(String s) throws Exception
	{
		return new Range.LONG(new RangeParser<Long>()
		{
			protected Long parseValue(String s) throws Exception
			{
				return Long.parseLong(s);
			}
		}.parse(s));
	}
	
	
	//
	
	
	public abstract static class RangeParser<V>
	{
		protected abstract V parseValue(String s) throws Exception;
		
		//
		
		private CList<V> res;
		
		
		public CList<V> parse(String s) throws Exception
		{
			if(s == null)
			{
				return null;
			}
			
			String[] ss = CKit.split(s, DELIMITER);
			int sz = ss.length;
			res = new CList<>(sz + sz);
			
			for(int i=0; i<sz; i++)
			{
				String[] sg = CKit.split(ss[i], SEPARATOR);
				switch(sg.length)
				{
				case 1:
					{
						V v = parseValue(sg[0].trim());
						res.add(v);
						res.add(v);
					}
					break;
				case 2:
					{
						V min = parseValue(sg[0].trim());
						res.add(min);
						
						V max = parseValue(sg[1].trim());
						res.add(max);
					}
					break;
				default:
					throw new Exception("unparseable " + ss[i]);
				}
			}
			
			return res;
		}
	}
	
	
	//
	
	
	public class RangeIterator
		implements Iterator<T>
	{
		private CList<T> ranges;
		private int index;
		private T next;
		
		
		public RangeIterator(CList<T> ranges)
		{
			this.ranges = ranges;
		}
		
		
		public boolean hasNext()
		{
			return index < ranges.size();
		}
	
	
		public T next()
		{			
			T rv;

			if(next == null)
			{
				rv = ranges.get(index);
			}
			else
			{
				rv = next;
			}
			
			next = increment(rv);
			
			T max = ranges.get(index+1);
			if(next.compareTo(max) > 0)
			{
				index += 2;
				next = null;
			}
				
			return rv;
		}
	
	
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	
	//
	
	
	public static class IntervalStream<T extends Comparable<T>>
	{
		private CList<T> ranges;
		private boolean reverse;
		private int index;
		
		
		public IntervalStream(CList<T> ranges, boolean reverse)
		{
			this.ranges = ranges;
			this.reverse = reverse;
			if(reverse)
			{
				this.index = ranges.size();
			}
		}


		public boolean hasNext()
		{
			if(reverse)
			{
				return index > 0;
			}
			else
			{
				return index < ranges.size();
			}
		}


		public Interval<T> next()
		{
			if(reverse)
			{
				T max = ranges.get(--index);
				T min = ranges.get(--index);
				return new Interval<T>(min, max);
			}
			else
			{
				T min = ranges.get(index++);
				T max = ranges.get(index++);
				return new Interval<T>(min, max);
			}
		}
		
		
		public int getElementCount()
		{
			return ranges.size();
		}
		
		
		public T getElement(int ix)
		{
			return ranges.get(ix);
		}
	}
	
	
	//
		
	
	public static class INT
		extends Range<Integer>
	{
		public INT()
		{
		}
		
		
		public INT(int min, int max)
		{
			super(min, max);
		}
		
		
		public INT(CList<Integer> ranges)
		{
			super(ranges);
		}
		

		protected Integer createSmallNumber(int n)
		{
			return Integer.valueOf(n);
		}
		
		
		protected Integer add(Integer a, Integer b)
		{
			return a + b;
		}
		
		
		protected Integer subtract(Integer a, Integer b)
		{
			return a - b;
		}
		
		
		protected Integer increment(Integer x)
		{
			return ++x;
		}
		
		
		protected Integer decrement(Integer x)
		{
			return --x;
		}


		protected Range<Integer> create(CList<Integer> ranges)
		{
			return new INT(ranges);
		}
		
		
		public int getItemCountInt()
		{
			Integer c = itemCount();
			return c == null ? 0 : c; 
		}
		
		
		public long getItemCountLong()
		{
			return getItemCountInt();
		}
		
		
		public BigInteger getItemCountBigInteger()
		{
			return BigInteger.valueOf(getItemCountLong());
		}
	}
	
	
	//
	
	
	public static class LONG
		extends Range<Long>
	{
		public LONG()
		{
		}
		
		
		public LONG(long min, long max)
		{
			super(min, max);
		}
		
		
		public LONG(CList<Long> ranges)
		{
			super(ranges);
		}
		

		protected Long createSmallNumber(int n)
		{
			return Long.valueOf(n);
		}
		
		
		protected Long add(Long a, Long b)
		{
			return a + b;
		}
		
		
		protected Long subtract(Long a, Long b)
		{
			return a - b;
		}
		
		
		protected Long increment(Long x)
		{
			return ++x;
		}
		
		
		protected Long decrement(Long x)
		{
			return --x;
		}


		protected Range<Long> create(CList<Long> ranges)
		{
			return new LONG(ranges);
		}
		
		
		public int getItemCountInt()
		{
			throw new Error("please use getItemCountLong()");
		}
		
		
		public long getItemCountLong()
		{
			Long c = itemCount();
			return c == null ? 0 : c; 
		}
		
		
		public BigInteger getItemCountBigInteger()
		{
			return BigInteger.valueOf(getItemCountLong());
		}
	}
	
	
	//
	
	
	public static class BIG
		extends Range<BigInteger>
	{
		public BIG()
		{
		}
		
		
		public BIG(BigInteger min, BigInteger max)
		{
			super(min, max);
		}
		
		
		public BIG(long min, long max)
		{
			super(BigInteger.valueOf(min), BigInteger.valueOf(max));
		}
		
		
		public BIG(CList<BigInteger> ranges)
		{
			super(ranges);
		}
		

		protected BigInteger createSmallNumber(int n)
		{
			return BigInteger.valueOf(n);
		}
		
		
		protected BigInteger add(BigInteger a, BigInteger b)
		{
			return a.add(b);
		}
		
		
		protected BigInteger subtract(BigInteger a, BigInteger b)
		{
			return a.subtract(b);
		}
		
		
		protected BigInteger increment(BigInteger x)
		{
			return x.add(BigInteger.ONE);
		}
		
		
		protected BigInteger decrement(BigInteger x)
		{
			return x.subtract(BigInteger.ONE);
		}


		protected Range<BigInteger> create(CList<BigInteger> ranges)
		{
			return new BIG(ranges);
		}
		
		
		public int getItemCountInt()
		{
			throw new Error("please use getItemCountBigInteger()");
		}
		
		
		public long getItemCountLong()
		{
			throw new Error("please use getItemCountBigInteger()");
		}
		
		
		public BigInteger getItemCountBigInteger()
		{
			BigInteger c = itemCount();
			return c == null ? BigInteger.ZERO : c; 
		}
	}
}
