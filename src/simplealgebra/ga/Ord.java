





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

import simplealgebra.NumDimensions;


/**
 * A node for defining multiplication rules for an algebra similar to Geometric Algebra.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the algebra.
 */
public abstract class Ord<U extends NumDimensions> {
	
	/**
	 * Defines multiplication rules for an algebra similar to Geometric Algebra.
	 * 
	 * @param ka The basis vector for the term on the left-side of the product.
	 * @param kb The basis vector for the term on the right-side of the product.
	 * @param el The basis vector for the result of the product.  This is modified by the method.
	 * @param dim The number of dimensions in the algebra.
	 * @return True iff. the result of the product is to be negated.
	 */
	public abstract boolean calcOrd( HashSet<BigInteger> ka , HashSet<BigInteger> kb , HashSet<BigInteger> el , U dim );
	
	
	/**
	 * Suggests a basis element that squares negative.
	 * @param dim The number of dimensions.
	 * @return The basis element that squares negative, or null if none found.
	 */
	public abstract HashSet<BigInteger> suggestNegativeSquare( U dim );
	
	
	/**
	 * Writes the type of the dimension.
	 * 
	 * @param dim The number of dimensions in the algebra.
	 * @param ps The type to which to write the dimension.
	 */
	public void writeTypeString( NumDimensions dim , PrintStream ps )
	{
		ps.print( this.getClass().getSimpleName() );
		ps.print( "<" );
		dim.writeTypeString( ps );
		ps.print( ">" );
	}
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param dim The number of dimensions in the algebra.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public String writeDesc( WriteOrdCache<U> cache , NumDimensions dim , PrintStream ps )
	{
		String st = cache.getFac( this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.putFac( this, st );
			writeTypeString( dim , ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeTypeString( dim , ps );
			ps.println( "();" );
		}
		return( st );
	}

	
}

