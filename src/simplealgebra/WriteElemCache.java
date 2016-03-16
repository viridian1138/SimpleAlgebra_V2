




//$$strtCprt
/**
* Simple Algebra 
* 
* Copyright (C) 2014 Thornton Green
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program; if not, 
* see <http://www.gnu.org/licenses>.
* Additional permission under GNU GPL version 3 section 7
*
*/
//$$endCprt







package simplealgebra;

import java.math.BigInteger;
import java.util.HashMap;


/**
 * Cache mapping elems to strings.
 * 
 * @author thorngreen
 *
 * @param <T> The elem type.
 * @param <R> The factory for the elem type.
 */
public class WriteElemCache<T extends Elem<T,?>, R extends ElemFactory<T,R>> extends AbstractCache<T,R,String,String>
{
	
	
	/**
	 * Increment cache val.
	 * 
	 * @author tgreen
	 *
	 */
	public static class IntVal
	{
		/**
		 * The current value.
		 */
		protected BigInteger val = BigInteger.ZERO;
		
		/**
		 * Gets the integer for the current cache value.
		 * 
		 * @return The integer for the current cache value.
		 */
		public BigInteger getIntVal()
		{
			val = val.add( BigInteger.ONE );
			return( val );
		}
		
		/**
		 * Gets the string for the current cache value.
		 * 
		 * @return The string for the current cache value.
		 */
		public String getIncrementVal()
		{
			BigInteger b = getIntVal();
			return( "a" + b );
		}
		
	};

	
	
	/**
	 * Constructs the cache.
	 */
	public WriteElemCache( )
	{
		super();
		cacheVal = new IntVal();
	}
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache val.
	 */
	protected WriteElemCache( IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 * Gets the value of the increment cache val.
	 * 
	 * @return The value of the increment cache val.
	 */
	public String getIncrementVal()
	{
		return( cacheVal.getIncrementVal() );
	}
	
	
	
	
	
	/**
	 * Gets a cache from the aux cache.
	 * 
	 * @param clss The cache to retrieve.
	 * @return The cache to retrieve, or null.
	 */
	public AbstractCache<?, ?, ?, ?> getAuxCache( Class<? extends AbstractCache<?,?,?,?>> clss ) {
		if( auxCache == null )
		{
			auxCache = ( HashMap<Class<? extends AbstractCache<?,?,?,?>>,AbstractCache<?,?,?,?>>)( new HashMap() );
		}
		return( auxCache.get( clss ) );
	}



	/**
	 * Applies a cache to the aux cache, if needed.
	 * 
	 * @param aux The cache to apply.
	 */
	public void applyAuxCache(AbstractCache<?, ?, ?, ?> aux) {
		Class<? extends AbstractCache<?,?,?,?>> clss = (Class<? extends AbstractCache<?, ?, ?, ?>>) aux.getClass();
		if( auxCache == null )
		{
			auxCache = ( HashMap<Class<? extends AbstractCache<?,?,?,?>>,AbstractCache<?,?,?,?>>)( new HashMap() );
		}
		if( auxCache.get( clss ) == null )
		{
			auxCache.put( clss , aux );
		}
	}
	

	/**
	 * Gets the increment cache val.
	 * 
	 * @return The cache val.
	 */
	public IntVal getCacheVal() {
		return cacheVal;
	}



	/**
	 * Gets the cache for the enclosed type (if applicable).
	 * 
	 * @return The cache for the enclosed type (if applicable).
	 */
	public WriteElemCache<?,?> getInnerCache()
	{
		if( innerCache == null )
		{
			innerCache = (WriteElemCache<?,?>)( new WriteElemCache( cacheVal ) );
		}
		return( innerCache );
	}
	
	
	/**
	 * Cache for the enclosed type (if applicable).
	 */
	protected WriteElemCache<?,?> innerCache;
	
	/**
	 * The increment cache val.
	 */
	protected IntVal cacheVal;
	
	/**
	 * The aux cache for non-elems.
	 */
	protected HashMap<Class<? extends AbstractCache<?,?,?,?>>,AbstractCache<?,?,?,?>> auxCache;
	
}


