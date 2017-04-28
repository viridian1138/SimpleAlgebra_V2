




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
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Random;


/**
 * A primitive random number generator.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public abstract class PrimitiveRandom {

	/**
	 * Returns a random double up to a limit of max.
	 * @param max The maximum limit for the random double.
	 * @return The random double.
	 */
	public abstract double nextRandom( double max );
	
	/**
	 * Returns a random BigInteger up to a limit of max.
	 * @param max The maximum limit for the random BigInteger.
	 * @return The random BigInteger.
	 */
	public abstract BigInteger nextRandom( BigInteger max );
	
	/**
	 * Writes the type of the PrimitiveRandom.
	 * 
	 * @param ps The type to which to write the PrimitiveRandom.
	 */
	public void writeTypeString( PrintStream ps )
	{
		ps.print( this.getClass().getSimpleName() );
	}
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public String writeDesc( WritePrimitiveRandomCache cache , PrintStream ps ) 
	{
		String st = cache.getFac( this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.putFac( this, st );
			writeTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeTypeString( ps );
			ps.println( "();" );
		}
		return( st );
	}
	
	/**
	 * Returns a default PrimitiveRandom for a Java random number generator.
	 * @param rand The Java random number generator.
	 * @return The associated PrimitiveRandom.
	 */
	public static PrimitiveRandom genRand( final Random rand )
	{
		return( new DefaultPrimitiveRandom( rand ) );
	}

}


