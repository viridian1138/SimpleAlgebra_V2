





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
import java.math.BigInteger;

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.WriteElemCache;



/**
 * A factory for generating temporary indices for use in tensor products.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the temporary indices.
 */
public abstract class TemporaryIndexFactory<Z extends Object> {
	
	/**
	 * Generates a new temporary index.
	 * 
	 * @return The new temporary index.
	 */
	public abstract Z getTemp();
	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public TemporaryIndexFactory<Z> cloneThread( final BigInteger threadIndex )
	{
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public String writeDesc( WriteTemporaryIndexFactoryCache<Z> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( this.getClass().getSimpleName() );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( this.getClass().getSimpleName() );
			ps.println( "();" );
		}
		return( st );
	}
	

}

