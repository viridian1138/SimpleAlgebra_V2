





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








package simplealgebra.bigfixedpoint;

import java.io.PrintStream;
import java.math.BigInteger;


/**
 * The precision of a BigFixedPointElem.  Subclasses of Precision are usually immutable.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the Precision.
 */
public abstract class Precision<T extends Precision<T>> {
	
	/**
	 * Returns the BigInteger for the unit value.
	 * 
	 * @return The BigInteger for the unit value.
	 */
	public abstract BigInteger getVal();
	
	/**
	 * Returns the BigInteger for the square of the unit value.
	 * 
	 * @return The BigInteger for the square of the unit value.
	 */
	public abstract BigInteger getValSquared();
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * Subclasses of Precision are presumed to be immutable.  Override
	 * this method to implement a Precision that is not immutable.
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public T cloneThread( final BigInteger threadIndex )
	{
		// Subclasses of Precision are presumed to be immutable.  Override this method to implement a Precision that is not immutable.
		return( (T) this );
	}
	
	
	/**
	 * Writes the type of the precision.
	 * 
	 * @param ps The type to which to write the precision.
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
	public String writeDesc( WritePrecisionCache<T> cache , PrintStream ps )
	{
		String st = cache.getFac( (T) this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.putFac( (T) this, st );
			writeTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeTypeString( ps );
			ps.println( "();" );
		}
		return( st );
	}

	
}

