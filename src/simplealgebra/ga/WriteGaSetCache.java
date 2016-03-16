




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







package simplealgebra.ga;


import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashSet;

import simplealgebra.AbstractCache;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing HashSet<BigInteger> descs.
 * 
 * @author thorngreen
 */
public class WriteGaSetCache extends AbstractCache<HashSet<BigInteger>,HashSet<BigInteger>,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteGaSetCache( WriteElemCache.IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 * Writes the hash set to the print stream.
	 * 
	 * @param key The hash set to write to the stream.
	 * @param bic Cache of big integers.
	 * @param ps The print stream.
	 * @return String corresponding to the hash set.
	 */
	public String writeDesc( HashSet<BigInteger> key , WriteBigIntegerCache bic , PrintStream ps )
	{
		String stair = get( key );
		if( stair == null )
		{
			stair = getIncrementVal();
			ps.print( "final HashSet<BigInteger> " );
			ps.print( stair );
			ps.println( " = new HashSet<BigInteger>();" );
			put(key, stair);
			for( final BigInteger vl : key )
			{
				String sl = bic.writeDesc( vl , ps );
				ps.print( stair );
				ps.print( ".add( " );
				ps.print( sl );
				ps.println( " );" );
			}
		}
		return( stair );
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


