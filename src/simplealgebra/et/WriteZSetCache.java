




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



import java.io.PrintStream;
import java.util.HashSet;

import simplealgebra.AbstractCache;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing HashSet<Z> descs.
 * 
 * @author thorngreen
 *
 * @param <Z> The index type.
 */
public class WriteZSetCache<Z extends Object> extends AbstractCache<HashSet<Z>,HashSet<Z>,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteZSetCache( WriteElemCache.IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 * Writes a HashSet<Z> to a PrintStream.
	 * 
	 * @param contravariantReduce The hash set.
	 * @param ps The print stream.
	 * @return A string corresponding to the hash set.
	 */
	public String writeDesc( HashSet<Z> contravariantReduce , PrintStream ps )
	{
		String staZSetContravar = get( contravariantReduce );
		if( staZSetContravar == null )
		{
			 staZSetContravar = getIncrementVal();
			 ps.print( "final HashSet<? extends Object> " );
			 ps.print( staZSetContravar );
			 ps.println( " = new HashSet<? extends Object>();" );
			 for( final Z vl : contravariantReduce )
			 {
				 String sl = getIncrementVal();
				 ps.print( "final " );
				 ps.print( vl.getClass().getSimpleName() );
				 ps.print( " = new " );
				 ps.print( vl.getClass().getSimpleName() );
				 ps.print( "( \"" );
				 ps.print( vl );
				 ps.println( "\" );" );
				 ps.print( staZSetContravar );
				 ps.print( ".add(" );
				 ps.print( sl );
				 ps.println( " );" );
			 }
		}
		return( staZSetContravar );
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


