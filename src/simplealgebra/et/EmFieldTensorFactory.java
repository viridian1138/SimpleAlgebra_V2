





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

import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for the Em field defined by <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>F</mi>
 *      <mrow>
 *        <mi>&alpha;</mi>
 *        <mi>&beta;</mi>
 *      </mrow>
 *  </msub>
 *  <mo>=</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>&alpha;</mi>
 *  </msub>
 *  <msub>
 *          <mi>A</mi>
 *        <mi>&beta;</mi>
 *  </msub>
 *  <mo>-</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>&beta;</mi>
 *  </msub>
 *  <msub>
 *          <mi>A</mi>
 *        <mi>&alpha;</mi>
 *  </msub>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>A</mi>
 *        <mi>&alpha;</mi>
 *  </msub>
 * </mrow>
 * </math> is the vector potential and <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>&alpha;</mi>
 *  </msub>
 * </mrow>
 * </math> is the ordinary derivative.
 * 
 * <P>
 * <P> See <A href="http://en.wikipedia.org/wiki/Maxwell%27s_equations_in_curved_spacetime">http://en.wikipedia.org/wiki/Maxwell%27s_equations_in_curved_spacetime</A>
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type for the variables over which to take derivatives.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class EmFieldTensorFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * Factory for symbolic elems.
	 */
	SymbolicElemFactory<R, S> fac;
	
	/**
	 * Factory for the partial derivatives of a directional derivative.
	 */
	DirectionalDerivativePartialFactory<R, S, K> deriv;
	
	/**
	 * Factory for the terms of the vector potential.
	 */
	VectorPotentialFactory<R, S> vect;
	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac Factory for symbolic elems.
	 * @param _deriv Factory for the partial derivatives of a directional derivative.
	 * @param _vect Factory for the terms of the vector potential.
	 */
	public EmFieldTensorFactory( SymbolicElemFactory<R, S> _fac , 
			DirectionalDerivativePartialFactory<R, S, K> _deriv,
			VectorPotentialFactory<R, S> _vect )
	{
		fac = _fac;
		deriv = _deriv;
		vect = _vect;
	}
	
	
	/**
	 * Returns the Em field tensor.
	 * 
	 * @param index0 Tensor index for resulting rank two tensor.
	 * @param index1 Tensor index for resulting rank two tensor.
	 * @param numElem The number of dimensions for the tensor.
	 * @return The Em field tensor.
	 */
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> 
		getEmFld( Z index0 , Z index1 , BigInteger numElem )
		{
		
			ArrayList<Z> contravariantIndices = new ArrayList<Z>();
			ArrayList<Z> covariantIndices = new ArrayList<Z>();
			
			
			covariantIndices.add( index0 );
			covariantIndices.add( index1 );
			
		
			EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> tel =
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( fac , contravariantIndices , covariantIndices );
			
			
			for( BigInteger cnti = BigInteger.ZERO ; cnti.compareTo(numElem) < 0 ; cnti = cnti.add( BigInteger.ONE ) )
			{
				for( BigInteger cntj = BigInteger.ZERO ; cntj.compareTo(numElem) < 0 ; cntj = cntj.add( BigInteger.ONE ) )
				{
					ArrayList<BigInteger> el = new ArrayList<BigInteger>();
					el.add( cnti );
					el.add( cntj );
					final SymbolicElem<R,S> di = deriv.getPartial( cnti );
					final SymbolicElem<R,S> dj = deriv.getPartial( cntj );
					final SymbolicElem<R,S> vi = vect.getVectorPotential( cnti );
					final SymbolicElem<R,S> vj = vect.getVectorPotential( cntj );
					SymbolicElem<R,S> elem = ( di.mult( vj ) ).add( ( dj.mult( vi ) ).negate() );
					tel.setVal( el , elem );
				}
			}
			
			return( tel );
		}

}


