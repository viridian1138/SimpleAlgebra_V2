
package test_simplealgebra;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.RankTwoDeterminantFactory;


/**
 * Factory for generating the determinant of a reduced rank-two tensor in the ADM formalism.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions of the matrix for the determinant.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 */
public class RankTwoDeterminantFactoryZn<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>>
	extends RankTwoDeterminantFactory<Z, NumDimensions, R, S>
{

	/**
	 * The dimensionality for the reduced matrix.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class Adim extends NumDimensions
	{
		/**
		 * The number of dimensions.
		 */
		BigInteger dim;
		
		/**
		 * Constructs the dimension.
		 * 
		 * @param _dim The number of dimensions.
		 */
		public Adim( BigInteger _dim )
		{
			dim = _dim;
		}

		/**
		 * Gets the number of dimensions.
		 * @return The number of dimensions.
		 */
		@Override
		public BigInteger getVal() {
			return( dim );
		}
		
	};
	
	
	
	/**
	 * Constructs the factory for determinants.
	 * 
	 * @param _dim The number of dimensions of the matrix for the determinant.
	 */
	public RankTwoDeterminantFactoryZn( U _dim )
	{
		super( new Adim( _dim.getVal().subtract( BigInteger.ONE ) ) );
	}
	
	
	
	@Override
	public R getDeterminantComponent( EinsteinTensorElem<Z,R,S> in )
	{
		final SquareMatrixElem<Adim,R,S> matrix = new SquareMatrixElem<Adim,R,S>( in.getFac().getFac(), (Adim) dim );
		
		final Iterator<ArrayList<BigInteger>> ita = in.getKeyIterator();
		while( ita.hasNext() )
		{
			final ArrayList<BigInteger> keyA = ita.next();
			final BigInteger row = keyA.get( 0 ).subtract( BigInteger.ONE );
			final BigInteger col = keyA.get( 1 ).subtract( BigInteger.ONE );
			matrix.setVal( row , col , in.getVal( keyA ) );
		}
		
		return( matrix.determinant() );
	}
	
	
	
}


