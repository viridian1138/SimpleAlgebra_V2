




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


import java.io.PrintStream;
import java.math.BigInteger;

import simplealgebra.AbstractCache;
import simplealgebra.WriteElemCache;


/**
 * Cache for writing BigInteger descs.
 * 
 * @author thorngreen
 */
public class WriteBigIntegerCache extends AbstractCache<BigInteger,BigInteger,String,String>
{
	
	
	/**
	 * Constructs the cache.
	 * 
	 * @param in The increment cache from the containing elem.
	 */
	public WriteBigIntegerCache( WriteElemCache.IntVal in )
	{
		super();
		cacheVal = in;
	}
	
	
	/**
	 * Writes the BigInteger to a PrintStream.
	 * 
	 * @param val The input integer.
	 * @param ps The print stream.
	 * @return String corresponding to the integer.
	 */
	public String writeDesc( BigInteger val , PrintStream ps )
	{
		String stai = get( val );
		if( stai == null )
		{
			stai = getIncrementVal();
			ps.print( "final BigInteger " );
			ps.print( stai );
			ps.print( " = new BigInteger( \"" );
			ps.print( val );
			ps.println( "\" );" );
			put(val, stai);
		}
		return( stai );
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
	 * The increment cache val.
	 */
	protected WriteElemCache.IntVal cacheVal;
	
}


