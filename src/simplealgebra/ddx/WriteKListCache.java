




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



import java.io.PrintStream;
import java.util.ArrayList;

import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing ArrayList<K> descs.
 * 
 * @author thorngreen
 *
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class WriteKListCache<K extends Elem<?,?>> extends AbstractCache<ArrayList<K>,ArrayList<K>,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteKListCache( WriteElemCache.IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 *  Writes an array list to a print stream.
	 * 
	 * @param withRespectTo The array list.
	 * @param kCache The cache of elems.
	 * @param ps The print stream.
	 * @return String corresponding to the array list.
	 */
	public String writeDesc( ArrayList<K> withRespectTo , WriteElemCache kCache , PrintStream ps )
	{
		String staKList = get( withRespectTo );
		if( staKList == null )
		{
			 staKList = getIncrementVal();
			 ps.print( "final ArrayList<? extends Elem<?,?>> " );
			 ps.print( staKList );
			 ps.println( " = new ArrayList<? extends Elem<?,?>>();" );
			 for( final K vl : withRespectTo )
			 {
				 final String sl = vl.writeDesc( kCache , ps);
				 ps.print( staKList );
				 ps.print( ".add(" );
				 ps.print( sl );
				 ps.println( " );" );
			 }
		}
		return( staKList );
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


