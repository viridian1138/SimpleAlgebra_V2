




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





package simplealgebra.ddx;

import java.math.BigInteger;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Factory for mapping a directional derivative (usually represented as <math display="inline">
 * <mrow>
 *  <mo>&nabla;</mo>
 * </mrow>
 * </math>) into its set of constituent partial derivatives.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type of the derivative.
 * @param <S> Factory for the enclosed type of the derivative.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public abstract class DirectionalDerivativePartialFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>>
{

	/**
	 * Returns the partial derivative for a particular index value.
	 * 
	 * @param basisIndex The input index value.
	 * @return A partial derivative expression corresponding to the index.
	 */
	public abstract SymbolicElem<R,S> getPartial( BigInteger basisIndex );

}

