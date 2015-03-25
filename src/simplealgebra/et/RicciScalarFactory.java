





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
 * Factory for Ricci scalars as defined by the equation <math display="inline">
 * <mrow>
 * <mi>R</mi>
 * <mo>=</mo>
 * <msup>
 *         <mi>g</mi>
 *     <mrow>
 *       <mi>u</mi>
 *       <mi>v</mi>
 *     </mrow>
 * </msup>
 * <msub>
 *         <mi>R</mi>
 *     <mrow>
 *       <mi>u</mi>
 *       <mi>v</mi>
 *     </mrow>
 * </msub>
 * </mrow>
 * </math> where the <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>R</mi>
 *      <mrow>
 *        <mi>u</mi>
 *        <mi>v</mi>
 *      </mrow>
 *  </msub>
 * </mrow>
 * </math> term is the Ricci tensor.
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
public class RicciScalarFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * A factory for generating metric tensors.
	 */
	MetricTensorFactory<Z,R,S> metric;
	
	/**
	 * A factory for generating temporary u, v indices in the Ricci scalar.
	 */
	TemporaryIndexFactory<Z> temp;
	
	/**
	 * A factory for generating Ricci tensors.
	 */
	RicciTensorFactory<Z,U,R,S,K> ricci;
	
	
	/**
	 * Constructs a factory for generating Ricci scalars.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _metric A factory for generating metric tensors.
	 * @param _temp A factory for generating temporary u, v indices in the Ricci scalar.
	 * @param _deriv A factory for generating ordinary derivatives.
	 */
	public RicciScalarFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , MetricTensorFactory<Z,R,S> _metric , 
			TemporaryIndexFactory<Z> _temp , OrdinaryDerivativeFactory<Z,U,R,S,K> _deriv )
	{
		metric = _metric;
		temp = _temp;
		ricci = new RicciTensorFactory<Z,U,R,S,K>( _fac , _metric , _temp , _deriv );
	}
	
	
	/**
	 * Returns an expression for the Ricci scalar.
	 * 
	 * @return An expression for the Ricci scalar.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getRicciScalar( )
	{
		Z u = temp.getTemp();
		
		Z v = temp.getTemp();
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			met = metric.getMetricTensor(false, u, v);
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ric = ricci.getRicciTensor(u, v);
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = met.mult( ric );
		
		return( ret );
	}
	
	

}


