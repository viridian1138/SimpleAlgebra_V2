





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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Implements a factory for the tensor <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *  <mi>v</mi>
 * </mrow>
 * </math> is a tensor index.  This produces a rank-one tensor
 * with a set of partial derivative operators.  The name of the
 * particular index to be used is passed into the class as a
 * parameter.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class OrdinaryDerivativeFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * Constructs the tensor factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions for the index.
	 * @param _dfac Factory for generating the partial derivatives of a directional derivative.
	 * @param _remap Parameter describing how to remap the derivative.  Leave as null if no remapping is desired.
	 */
	public OrdinaryDerivativeFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac ,
			DerivativeRemap<Z,R,S> _remap )
	{
		fac = _fac;
		dim = _dim;
		dfac = _dfac;
		remap = _remap;
	}
	
	/**
	 * Applies the ordinary derivative to an expression.
	 * 
	 * @param term The expression to which to apply the derivative.
	 * @param derivativeIndex The tensor index of the ordinary derivative.
	 * @return The result of applying the derivative.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getOrdinaryDerivative( SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> term , Z derivativeIndex )
{
		final OrdinaryDerivative<Z,U,R,S,K> ord = new OrdinaryDerivative<Z,U,R,S,K>( fac , derivativeIndex , dim , dfac );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			retA = ord.mult( term );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = remap == null ? retA : remap.remap( retA );
		
		return( ret );
}
	
	
	/**
	 * The tensor index for the ordinary derivative.
	 */
	private EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
		SymbolicElemFactory<R, S>> fac;
	
	/**
	 * The number of dimensions for the index.
	 */
	private U dim;
	
	/**
	 * Factory for generating the partial derivatives of a directional derivative.
	 */
	private DirectionalDerivativePartialFactory<R,S,K> dfac;
	
	/**
	 * Function for remapping the derivative after it is calculated.  Leave as null if no remap is desired.
	 */
	private DerivativeRemap<Z,R,S> remap;

}


