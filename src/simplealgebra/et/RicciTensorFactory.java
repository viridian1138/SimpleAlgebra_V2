





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

import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Factory for generating Ricci tensors, where a Ricci tensor is defined by <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>R</mi>
 *      <mrow>
 *        <mi>&alpha;</mi>
 *        <mi>&beta;</mi>
 *      </mrow>
 *  </msub>
 *  <mo>=</mo>
 *  <msubsup>
 *          <mi>R</mi>
 *      <mrow>
 *        <mi>&alpha;</mi>
 *        <mi>&upsilon;</mi>
 *        <mi>&beta;</mi>
 *      </mrow>
 *        <mi>&mu;</mi>
 *  </msubsup>
 * </mrow>
 * </math> where the <math display="inline">
 * <mrow>
 *  <msubsup>
 *          <mi>R</mi>
 *      <mrow>
 *        <mi>&alpha;</mi>
 *        <mi>&upsilon;</mi>
 *        <mi>&beta;</mi>
 *      </mrow>
 *        <mi>&mu;</mi>
 *  </msubsup>
 * </mrow>
 * </math> term is the Riemann tensor.
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
public class RicciTensorFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * The factory for the enclosed type.
	 */
	EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac;
	
	/**
	 * A factory for generating temporary lambda indices in the Ricci tensor.
	 */
	TemporaryIndexFactory<Z> temp;
	
	/**
	 * A factory for generating Riemann tensors.
	 */
	RiemannTensorFactory<Z,U,R,S,K> riemann;
	
	
	
	/**
	 * Constructs a factory for generating Ricci tensors.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _metric A factory for generating metric tensors.
	 * @param _temp A factory for generating temporary lambda indices in the Ricci tensor.
	 * @param _deriv A factory for generating ordinary derivatives.
	 */
	public RicciTensorFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , MetricTensorFactory<Z,R,S> _metric , 
			TemporaryIndexFactory<Z> _temp , OrdinaryDerivativeFactory<Z,U,R,S,K> _deriv )
	{
		fac = _fac;
		temp = _temp;
		riemann = new RiemannTensorFactory<Z,U,R,S,K>( _metric , _temp , _deriv );
	}
	
	
	/**
	 * Returns an expression for the Ricci tensor.
	 * 
	 * @param alpha The alpha index.
	 * @param beta The beta index.
	 * @return An expression for the Ricci tensor.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getRicciTensor( Z alpha , Z beta )
	{
		final Z lambda = temp.getTemp();
		
		final Z lambdaB = temp.getTemp();
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			rie = riemann.getRiemannTensor(alpha, lambda, beta, lambdaB);
		
		final HashSet<Z> contravariantReduce = new HashSet<Z>();
		final HashSet<Z> covariantReduce = new HashSet<Z>();
		
		contravariantReduce.add( lambdaB );
		covariantReduce.add( lambda );
		
		SymbolicIndexReduction<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> src = 
				new SymbolicIndexReduction<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( rie , 
						fac , contravariantReduce , covariantReduce );
		
		return( src );
	}
	
	

}


