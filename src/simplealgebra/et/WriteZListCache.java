




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
import java.util.ArrayList;

import simplealgebra.AbstractCache;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing ArrayList<Z> descs.
 * 
 * @author thorngreen
 *
 * @param <Z> The index type.
 */
public class WriteZListCache<Z extends Object> extends AbstractCache<ArrayList<Z>,ArrayList<Z>,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteZListCache( WriteElemCache.IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 * Writes an ArrayList<Z> to a print stream.
	 * 
	 * @param contravariantReduce The array list.
	 * @param ps The print stream.
	 * @return String corresponding to the array list.
	 */
	public String writeDesc( ArrayList<Z> contravariantReduce , PrintStream ps )
	{
		String staZSetContravar = get( contravariantReduce );
		if( staZSetContravar == null )
		{
			 staZSetContravar = getIncrementVal();
			 ps.print( "final ArrayList<? extends Object> " );
			 ps.print( staZSetContravar );
			 ps.println( " = new ArrayList<? extends Object>();" );
			 for( final Z vl : contravariantReduce )
			 {
				 String sl = getIncrementVal();
				 ps.print( "final " );
				 ps.print( vl.getClass().getSimpleName() );
				 ps.print( " " );
				 ps.print( sl );
				 if( vl instanceof String )
				 {
					 ps.print( " = " );
					 ps.print( "\"" );
					 ps.print( vl );
					 ps.println( "\";" );
				 }
				 else
				 {
					 ps.print( " = new " );
					 ps.print( vl.getClass().getSimpleName() );
					 ps.print( "( \"" );
					 ps.print( vl );
					 ps.println( "\" );" );
				 }
				 ps.print( staZSetContravar );
				 ps.print( ".add( " );
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


