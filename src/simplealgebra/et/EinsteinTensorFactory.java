





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
 * Factory for Einstein tensors as defined by the equation <math display="inline">
 * <mrow>
 * <msub>
 *         <mi>G</mi>
 *     <mrow>
 *       <mi>u</mi>
 *       <mi>v</mi>
 *     </mrow>
 * </msub>
 * <mo>=</mo>
 * 
 *   <mrow>
 *     <msub>
 *             <mi>R</mi>
 *         <mrow>
 *           <mi>u</mi>
 *           <mi>v</mi>
 *         </mrow>
 *     </msub>
 *     <mo>-</mo>
 *     <mfrac>
 *       <mrow>
 *         <mn>1</mn>
 *       </mrow>
 *       <mrow>
 *         <mn>2</mn>
 *       </mrow>
 *     </mfrac>
 *     <msub>
 *             <mi>g</mi>
 *         <mrow>
 *           <mi>u</mi>
 *           <mi>v</mi>
 *         </mrow>
 *     </msub>
 *   </mrow>
 * 
 * 
 *     <mi>R</mi>
 * 
 * </mrow>
 * </math> where the term <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>g</mi>
 *      <mrow>
 *        <mi>u</mi>
 *        <mi>v</mi>
 *      </mrow>
 *  </msub>
 * </mrow>
 * </math> is the metric tensor, <math display="inline">
 * <mrow>
 *  <mi>R</mi>
 * </mrow>
 * </math> is the Ricci scalar, and <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>R</mi>
 *      <mrow>
 *        <mi>u</mi>
 *        <mi>v</mi>
 *      </mrow>
 *  </msub>
 * </mrow>
 * </math> is the Ricci tensor.
 *
 *
 * <P>
 * <P> See <A href="http://en.wikipedia.org/wiki/Einstein_tensor">http://en.wikipedia.org/wiki/Einstein_tensor</A>
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
public class EinsteinTensorFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * The factory for the enclosed type.
	 */
	EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac;
	
	/**
	 * A factory for generating metric tensors.
	 */
	MetricTensorFactory<Z,R,S> metric;
	
	/**
	 * A factory for generating Ricci tensors.
	 */
	RicciTensorFactory<Z,U,R,S,K> ricci;
	
	/**
	 * A factory for generating Ricci scalars.
	 */
	RicciScalarFactory<Z,U,R,S,K> ricciS;
	
	
	/**
	 * Constructs a factory for generating Einstein tensors.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _metric A factory for generating metric tensors.
	 * @param _temp A factory for generating temporary indices in the Einstein tensor.
	 * @param _deriv A factory for generating ordinary derivatives.
	 */
	public EinsteinTensorFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , MetricTensorFactory<Z,R,S> _metric , 
			TemporaryIndexFactory<Z> _temp , OrdinaryDerivativeFactory<Z,U,R,S,K> _deriv )
	{
		fac = _fac;
		metric = _metric;
		ricci = new RicciTensorFactory<Z,U,R,S,K>( _fac , _metric , _temp , _deriv );
		ricciS = new RicciScalarFactory<Z,U,R,S,K>( _fac , _metric , _temp , _deriv );
	}
	
	
	/**
	 * Returns an expression for the Einstein tensor.
	 * 
	 * @param u The tensor u index.
	 * @param v The tensor v index.
	 * @return An expression for the Einstein tensor.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getEinsteinTensor( Z u , Z v )
	{
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ric = ricci.getRicciTensor( u, v);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			met = metric.getMetricTensor(true, u, v);
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ricS = ricciS.getRicciScalar();
		
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			term2 = met.mult( ricS ).divideBy( 2 ).negate();
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = ric.add( term2 );
		
		return( ret );
	}
	
	

}


