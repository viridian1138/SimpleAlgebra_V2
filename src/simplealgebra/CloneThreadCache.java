




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

import java.util.HashMap;


/**
 * Cache for use with the cloneThreadCached() call.
 * 
 * @author thorngreen
 *
 * @param <T> The elem type.
 * @param <R> The factory for the elem type.
 */
public class CloneThreadCache<T extends Elem<T,?>, R extends ElemFactory<T,R>>
{

	/**
	 * Constructs the cache.
	 */
	public CloneThreadCache( )
	{
	}
	
	public T get( T in )
	{
		return( map.get( in ) );
	}
	
	public void put( T key , T value )
	{
		map.put(key, value);
	}
	
	public R getFac( R in )
	{
		return( facmap.get( in ) );
	}
	
	public void putFac( R key , R value )
	{
		facmap.put(key, value);
	}
	
	/**
	 * Gets the cache for the enclosed type (if applicable).
	 * 
	 * @return The cache for the enclosed type (if applicable).
	 */
	public CloneThreadCache<?,?> getInnerCache()
	{
		if( innerCache == null )
		{
			innerCache = (CloneThreadCache<?,?>)( new CloneThreadCache() );
		}
		return( innerCache );
	}
	
	
	/**
	 * Map of elems.
	 */
	protected HashMap<T,T> map = new HashMap<T,T>();
	
	/**
	 * Map of factories.
	 */
	protected HashMap<R,R> facmap = new HashMap<R,R>();
	
	/**
	 * Cache for the enclosed type (if applicable).
	 */
	protected CloneThreadCache<?,?> innerCache;
	
}


