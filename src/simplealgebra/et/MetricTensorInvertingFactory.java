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
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating metric tensors as defined in General Relativity.
 * This subclass has additional functionality for defining inverse tensors.
 * In cases where the inverse of the metric is known, it is much more efficient
 * to specify it directly.  However, when the inverse of the metric is NOT
 * already known this class provides utility methods for computing it.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 */
public abstract class MetricTensorInvertingFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends
		MetricTensorFactory<Z, R, S> {
	

	/**
	 * Generates the left matrix inverse of a Rank 2 tensor.
	 * 
	 * @param dim The number of dimensions in the matrix.
	 * @param fac Factory for the enclosed type.
	 * @param elem The input Rank 2 tensor.
	 * @return The output Rank 2 tensor.
	 */
	protected EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>
		genMatrixInverseLeft( final U dim , final SymbolicElemFactory<R, S> fac ,
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> elem
				) 
		{
			final SquareMatrixElem<U,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> tmp = 
					new SquareMatrixElem<U,SymbolicElem<R, S>,SymbolicElemFactory<R, S>>( fac , dim );
			
			elem.rankTwoTensorToSquareMatrix( tmp );
			
			try
			{
				final SquareMatrixElem<U,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> inv = tmp.invertLeft();
				
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
						new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , elem.getContravariantIndices() , elem.getCovariantIndices() );
				
				inv.toRankTwoTensor( ret );
				
				return( ret );
			}
			catch( NotInvertibleException ex )
			{
				throw( new RuntimeException( "Invert Failed" ) );
			}
			
		}
	
	
	
	
	/**
	 * Generates the right matrix inverse of a Rank 2 tensor.
	 * 
	 * @param dim The number of dimensions in the matrix.
	 * @param fac Factory for the enclosed type.
	 * @param elem The input Rank 2 tensor.
	 * @return The output Rank 2 tensor.
	 */
	protected EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>
		genMatrixInverseRight( final U dim , final SymbolicElemFactory<R, S> fac ,
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> elem
				) 
		{
			final SquareMatrixElem<U,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> tmp = 
					new SquareMatrixElem<U,SymbolicElem<R, S>,SymbolicElemFactory<R, S>>( fac , dim );
			
			elem.rankTwoTensorToSquareMatrix( tmp );
			
			try
			{
				final SquareMatrixElem<U,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> inv = tmp.invertRight();
				
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
						new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , elem.getContravariantIndices() , elem.getCovariantIndices() );
				
				inv.toRankTwoTensor( ret );
				
				return( ret );
			}
			catch( NotInvertibleException ex )
			{
				throw( new RuntimeException( "Invert Failed" ) );
			}
			
		}
	
	

}


