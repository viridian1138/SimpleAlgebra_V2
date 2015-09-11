





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

/**
 * Factory for generating the determinant of a rank-two tensor.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions of the matrix for the determinant.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 */
public class RankTwoDeterminantFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * The number of dimensions of the matrix for the determinant.
	 */
	protected U dim;
	
	
	/**
	 * Constructs the factory for determinants.
	 * 
	 * @param _dim The number of dimensions of the matrix for the determinant.
	 */
	public RankTwoDeterminantFactory( U _dim )
	{
		dim = _dim;
	}
	
	
	/**
	 * Gets the determinant as a tensor.
	 * 
	 * @param in The rank-two tensor from which to get the determinant.
	 * @return The determinant as a tensor.
	 */
	public EinsteinTensorElem<Z,R,S> getDeterminant( EinsteinTensorElem<Z,R,S> in )
	{
		final R det = getDeterminantComponent( in );
		
		final EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>( det , in.getFac().getFac() );
		
		return( ret );
	}
	
	
	/**
	 * Gets the determinant in its component type.
	 * 
	 * @param in The rank-two tensor from which to get the determinant.
	 * @return The determinant in its component type.
	 */
	public R getDeterminantComponent( EinsteinTensorElem<Z,R,S> in )
	{
		final SquareMatrixElem<U,R,S> matrix = new SquareMatrixElem<U,R,S>( in.getFac().getFac(), dim );
		
		in.rankTwoTensorToSquareMatrix( matrix );
		
		return( matrix.determinant() );
	}
	
	

}


