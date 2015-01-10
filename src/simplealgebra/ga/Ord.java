





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


import java.math.BigInteger;
import java.util.HashSet;

import simplealgebra.NumDimensions;


/**
 * A node for defining multiplication rules for an algebra similar to Geometric Algebra.
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

	
}

