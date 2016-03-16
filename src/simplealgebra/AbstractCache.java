




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
 * Cache for mapping an object to another type.
 * 
 * @author thorngreen
 *
 * @param <T> The object type.
 * @param <R> The factory for the object type.
 * @param <A> The mapping of the object type.
 * @param <A> The mapping of the factory for the object type.
 */
public abstract class AbstractCache<T extends Object, R extends Object, A extends Object, B extends Object>
{

	/**
	 * Constructs the cache.
	 */
	public AbstractCache( )
	{
	}
	
	/**
	 * Gets a object from the object cache.
	 * 
	 * @param in The object to be cloned.
	 * @return The cached object or null.
	 */
	public A get( T in )
	{
		return( map.get( in ) );
	}
	
	/**
	 * Puts an object into the object cache.
	 * 
	 * @param key The object to be cloned.
	 * @param value The object to be cached.
	 */
	public void put( T key , A value )
	{
		map.put(key, value);
	}
	
	/**
	 * Gets a factory from the factory cache.
	 * 
	 * @param in The factory to be cloned.
	 * @return The cached factory or null.
	 */
	public B getFac( R in )
	{
		return( facmap.get( in ) );
	}
	
	/**
	 * Puts a factory into the factory cache.
	 * 
	 * @param key The factory to be cloned.
	 * @param value The factory to be cached.
	 */
	public void putFac( R key , B value )
	{
		facmap.put(key, value);
	}
	
	
	/**
	 * Map of objects.
	 */
	protected HashMap<T,A> map = new HashMap<T,A>();
	
	/**
	 * Map of factories.
	 */
	protected HashMap<R,B> facmap = new HashMap<R,B>();
	
}


