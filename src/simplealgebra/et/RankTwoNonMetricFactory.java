





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
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Factory for raising or lowering the indices of a rank-two tensor that is not the metric.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 */
public abstract class RankTwoNonMetricFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends RankTwoTensorFactory<Z,R,S> {
	
	
	/**
	 * Factory generating temporary indices.
	 */
	protected TemporaryIndexFactory<Z> ifac;
	
	
	/**
	 * The inverting metric tensor for performing the index raise.
	 */
	protected MetricTensorFactory<Z,R,S> metricTensor;
	
	
	
	/**
	 * Constructs the index raise factory.
	 * 
	 * @param _dim The number of dimensions of the matrix for the determinant.
	 */
	public RankTwoNonMetricFactory( TemporaryIndexFactory<Z> _ifac , MetricTensorFactory<Z,R,S> _metricTensor )
	{
		ifac = _ifac;
		metricTensor = _metricTensor;
	}
	
	
	/**
	 * Raises the indices of a covariant rank-two tensor.
	 * 
	 * @param in The covariant rank-two tensor to be raised.
	 * @return The raised tensor.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> genIndexRaise( Z indexI , Z indexJ )
	{
		final Z i0 = ifac.getTemp();
		final Z i1 = ifac.getTemp();
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			tensorToRaise = this.getTensor( true , i0 , i1 );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			gA = metricTensor.getMetricTensor( false , indexI , i0 );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			gB = metricTensor.getMetricTensor( false , indexJ , i1 );
		
		return( tensorToRaise.mult( gA ).mult( gB ) );
	}
	
	
	/**
	 * Lowers the indices of a contravariant rank-two tensor.
	 * 
	 * @param in The contravariant rank-two tensor to be lowered.
	 * @return The lowered tensor.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> genIndexLower( Z indexI , Z indexJ )
	{
		final Z i0 = ifac.getTemp();
		final Z i1 = ifac.getTemp();
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
			tensorToLower = this.getTensor( false , i0 , i1 );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			gA = metricTensor.getMetricTensor( true , indexI , i0 );
		
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			gB = metricTensor.getMetricTensor( true , indexJ , i1 );
		
		return( tensorToLower.mult( gA ).mult( gB ) );
	}
	
	

}


