





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
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Factory for generating Riemann tensors, where a Riemann tensor is defined by <math display="inline">
 * <mrow>
 *  <msubsup>
 *          <mi>R</mi>
 *      <mrow>
 *        <mi>&sigma;</mi>
 *        <mi>&mu;</mi>
 *        <mi>&nu;</mi>
 *      </mrow>
 *        <mi>&rho;</mi>
 *  </msubsup>
 *  <mo>=</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>&mu;</mi>
 *  </msub>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&nu;</mi>
 *        <mi>&sigma;</mi>
 *      </mrow>
 *        <mi>&rho;</mi>
 *  </msubsup>
 *  <mo>-</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>&upsilon;</mi>
 *  </msub>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&mu;</mi>
 *        <mi>&sigma;</mi>
 *      </mrow>
 *        <mi>&rho;</mi>
 *  </msubsup>
 *  <mo>+</mo>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&mu;</mi>
 *        <mi>&lambda;</mi>
 *      </mrow>
 *        <mi>&rho;</mi>
 *  </msubsup>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&nu;</mi>
 *        <mi>&sigma;</mi>
 *      </mrow>
 *        <mi>&lambda;</mi>
 *  </msubsup>
 *  <mo>-</mo>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&nu;</mi>
 *        <mi>&lambda;</mi>
 *      </mrow>
 *        <mi>&rho;</mi>
 *  </msubsup>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>&mu;</mi>
 *        <mi>&sigma;</mi>
 *      </mrow>
 *        <mi>&lambda;</mi>
 *  </msubsup>
 * </mrow>
 * </math> where the <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> terms are ordinary derivatives and the <math display="inline">
 * <mrow>
 *  <mi>&Gamma;</mi>
 * </mrow>
 * </math> terms are connection coefficients. See <A href="http://en.wikipedia.org/wiki/Einstein%E2%80%93Hilbert_action">http://en.wikipedia.org/wiki/Einstein%E2%80%93Hilbert_action</A>
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
public class RiemannTensorFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * A factory for generating temporary lambda indices in the Riemann tensor.
	 */
	TemporaryIndexFactory<Z> temp;
	
	/**
	 * A factory for generating ordinary derivatives.
	 */
	OrdinaryDerivativeFactory<Z,U,R,S,K> deriv;
	
	/**
	 * A factory for generating connection coefficients.
	 */
	ConnectionCoefficientFactory<Z,U,R,S,K> affine;
	
	
	
	/**
	 * Constructs a factory for generating Riemann tensors.
	 * 
	 * @param _metric A factory for generating metric tensors.
	 * @param _temp A factory for generating temporary lambda indices in the Riemann tensor.
	 * @param _deriv A factory for generating ordinary derivatives.
	 */
	public RiemannTensorFactory( MetricTensorFactory<Z,R,S> _metric , 
			TemporaryIndexFactory<Z> _temp , OrdinaryDerivativeFactory<Z,U,R,S,K> _deriv )
	{
		temp = _temp;
		deriv = _deriv;
		affine = new ConnectionCoefficientFactory<Z,U,R,S,K>( _metric , _temp , _deriv );
	}
	
	
	/**
	 * Returns an expression for the Riemann tensor.
	 * 
	 * @param sigma The sigma index.
	 * @param u The u index.
	 * @param v The v index.
	 * @param rho The rho index.
	 * @return An expression for the Riemann tensor.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getRiemannTensor( Z sigma , Z u , Z v , Z rho )
	{
		final Z lambda = temp.getTemp();
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			affineUd = affine.getConnectionCoefficient(v, sigma, rho);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			affineVd = affine.getConnectionCoefficient(u, sigma, rho);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			pAA = affine.getConnectionCoefficient(u, lambda, rho);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			pAB = affine.getConnectionCoefficient(v, sigma, lambda);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			pBA = affine.getConnectionCoefficient(v, lambda , rho);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			pBB = affine.getConnectionCoefficient(u , sigma, lambda);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			dU = deriv.getOrdinaryDerivative( affineUd , u );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			dV = deriv.getOrdinaryDerivative( affineVd , v );
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = dU.add( dV.negate() ).add( pAA.mult( pAB ) ).add( pBA.mult( pBB ).negate() );
		
		
		return( ret );
	}
	
	

}


