



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

import java.math.BigInteger;




/**
 * A function that mutates an elem. into another elem. of the same type.
 * Subclasses of Mutator are usually immutable.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the argument and return.
 */
public interface Mutator<T extends Elem<T,?>> {

	/**
	 * Returns the mutated input.
	 * 
	 * @param in The input.
	 * @return The mutated input.
	 * @throws NotInvertibleException
	 */
	public T mutate( T in ) throws NotInvertibleException;
	
	/**
	 * Returns true if the elem exposes derivatives to elems by which it is multiplied.
	 * 
	 * @return True if the elem exposes derivatives to elems by which it is multiplied.
	 */
	public boolean exposesDerivatives();
	
	/**
	 * Returns a string representation of the elem.
	 * 
	 * @return A string representation of the elem.
	 */
	public String writeString();
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public Mutator<T> cloneThread( final BigInteger threadIndex );

	
}


