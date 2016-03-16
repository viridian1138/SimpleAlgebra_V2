




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







package simplealgebra.et;


import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing DerivativeRemap descs.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor index.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class WriteDerivativeRemapCache<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends AbstractCache<DerivativeRemap<Z,R,S>,DerivativeRemap<Z,R,S>,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteDerivativeRemapCache( WriteElemCache.IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 * Gets the increment cache val.
	 * 
	 * @return The increment cache val.
	 */
	public String getIncrementVal()
	{
		return( cacheVal.getIncrementVal() );
	}
	
	
	/**
	 * The increment cache.
	 */
	protected WriteElemCache.IntVal cacheVal;
	
}


