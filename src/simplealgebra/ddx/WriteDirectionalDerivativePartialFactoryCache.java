




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







package simplealgebra.ddx;


import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing DirectionalDerivativePartialFactory descs.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type of the derivative.
 * @param <S> Factory for the enclosed type of the derivative.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class WriteDirectionalDerivativePartialFactoryCache<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends AbstractCache<DirectionalDerivativePartialFactory<R,S,K>,DirectionalDerivativePartialFactory<R,S,K>,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteDirectionalDerivativePartialFactoryCache( WriteElemCache.IntVal in )
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


