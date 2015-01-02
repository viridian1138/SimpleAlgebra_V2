





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

import java.math.BigInteger;

/**
 * The precision of a BigFixedPointElem.
 * 
 * @author thorngreen
 *
 */
public abstract class Precision {
	
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

}

