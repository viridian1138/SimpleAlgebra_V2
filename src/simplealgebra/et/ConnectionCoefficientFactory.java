





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
 * Factory for the Connection Coefficient (also known as Christoffel symbol and/or "Gamma symbol").  This is defined as:
 * 
 * 
 * <math display="inline">
 * <mrow>
 *  <msubsup>
 *          <mi>&Gamma;</mi>
 *      <mrow>
 *        <mi>j</mi>
 *        <mi>k</mi>
 *      </mrow>
 *        <mi>l</mi>
 *  </msubsup>
 *  <mo>=</mo>
 *  <mfrac>
 *    <mrow>
 *      <mn>1</mn>
 *    </mrow>
 *    <mrow>
 *      <mn>2</mn>
 *    </mrow>
 *  </mfrac>
 *  <msup>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>l</mi>
 *        <mi>r</mi>
 *      </mrow>
 *  </msup>
 * <mo>(</mo>
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>k</mi>
 *  </msub>
 *  <msub>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>r</mi>
 *        <mi>j</mi>
 *      </mrow>
 *  </msub>
 *  <mo>+</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>j</mi>
 *  </msub>
 *  <msub>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>r</mi>
 *        <mi>k</mi>
 *      </mrow>
 *  </msub>
 *  <mo>-</mo>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>r</mi>
 *  </msub>
 *  <msub>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>j</mi>
 *        <mi>k</mi>
 *      </mrow>
 *  </msub>
 * </mrow>
 * <mo>)</mo>  
 * </mrow>
 * </math> where the <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> terms are ordinary derivatives, the <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>i</mi>
 *        <mi>j</mi>
 *      </mrow>
 *  </msub>
 * </mrow>
 * </math> terms refer to the metric tensor, and the <math display="inline">
 * <mrow>
 *  <msup>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>i</mi>
 *        <mi>j</mi>
 *      </mrow>
 *  </msup>
 * </mrow>
 * </math> terms refer to the inverse of the metric tensor.
 * 
 * 
 * See http://en.wikipedia.org/wiki/Levi-Civita_connection
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
public class ConnectionCoefficientFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * A factory for generating metric tensors.
	 */
	MetricTensorFactory<Z,R,S> metric;
	
	/**
	 * A factory for generating temporary indices in the connection coefficient.
	 */
	TemporaryIndexFactory<Z> temp;
	
	/**
	 * A factory for generating ordinary derivatives.
	 */
	OrdinaryDerivativeFactory<Z,U,R,S,K> deriv;
	
	
	/**
	 * Constructs the connection coefficient factory.
	 * 
	 * @param _metric A factory for generating metric tensors.
	 * @param _temp A factory for generating temporary indices in the connection coefficient.
	 * @param _deriv A factory for generating ordinary derivatives.
	 */
	public ConnectionCoefficientFactory( MetricTensorFactory<Z,R,S> _metric , 
			TemporaryIndexFactory<Z> _temp , OrdinaryDerivativeFactory<Z,U,R,S,K> _deriv )
	{
		metric = _metric;
		temp = _temp;
		deriv = _deriv;
	}
	
	
	/**
	 * Returns an expression for the connection coefficient.
	 * 
	 * @param covar1 The first covariant index of the connection coefficient.
	 * @param covar2 The second covariant index of the connection coefficient.
	 * @param contravar1 The cobtravariant index of the connection coefficient.
	 * @return An expression for the connection coefficient.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getConnectionCoefficient( Z covar1 , Z covar2 , Z contravar1 )
	{
		final Z p = temp.getTemp();
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> outerTerm = 
				metric.getMetricTensor( false , contravar1 , p ).divideBy( 2 );
		
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> innerBeforeDeriv1 =
				metric.getMetricTensor( true , p , covar1 );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> innerBeforeDeriv2 =
				metric.getMetricTensor( true , p , covar2 );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> innerBeforeDeriv3 =
				metric.getMetricTensor( true , covar1 , covar2 );
		
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> inner1 =
				deriv.getOrdinaryDerivative( innerBeforeDeriv1 , covar2 );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> inner2 =
				deriv.getOrdinaryDerivative( innerBeforeDeriv2 , covar1 );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> inner3 =
				deriv.getOrdinaryDerivative( innerBeforeDeriv3 , p );
		
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> oi1 = outerTerm.mult( inner1 );
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> oi2 = outerTerm.mult( inner2 );
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> oi3 = outerTerm.mult( inner3 );
		
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> ret =
				oi1.add( oi2 ).add( oi3.negate() );
		
		
		
		return( ret );
	}
	
	

}


