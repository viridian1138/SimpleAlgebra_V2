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




package test_simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.MetricTensorInvertingFactory;
import simplealgebra.et.RankTwoDeterminantFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Factory for generating metric tensors in the ADM formalism where
 * the number of dimensions is reduced due to the removal of the
 * lapse and shift vectors.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 */
public abstract class MetricTensorInvertingFactoryZn<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends
	MetricTensorInvertingFactory<Z, U, R, S> {
	
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
	

	@Override
	protected EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>
		genMatrixInverseLeft( final U dim , final SymbolicElemFactory<R, S> fac ,
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> elem
				) 
		{
			final Adim odim = new Adim( dim.getVal().subtract( BigInteger.ONE ) );
		
			final SquareMatrixElem<Adim,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> tmp = 
					new SquareMatrixElem<Adim,SymbolicElem<R, S>,SymbolicElemFactory<R, S>>( fac , odim );
			
			final Iterator<ArrayList<BigInteger>> ita = elem.getKeyIterator();
			while( ita.hasNext() )
			{
				final ArrayList<BigInteger> keyA = ita.next();
				final BigInteger row = keyA.get( 0 ).subtract( BigInteger.ONE );
				final BigInteger col = keyA.get( 1 ).subtract( BigInteger.ONE );
				tmp.setVal( row , col , elem.getVal( keyA ) );
			}
			
			try
			{
				final SquareMatrixElem<Adim,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> inv = tmp.invertLeft();
				
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> rA =
						new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , elem.getContravariantIndices() , elem.getCovariantIndices() );
				
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
						new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , elem.getContravariantIndices() , elem.getCovariantIndices() );
				
				inv.toRankTwoTensor( rA );
				
				final Iterator<ArrayList<BigInteger>> itb = rA.getKeyIterator();
				while( itb.hasNext() )
				{
					final ArrayList<BigInteger> keyA = itb.next();
					final ArrayList<BigInteger> keyB = new ArrayList<BigInteger>();
					keyB.add( keyA.get( 0 ).add( BigInteger.ONE ) );
					keyB.add( keyA.get( 1 ).add( BigInteger.ONE ) );
					ret.setVal( keyB , rA.getVal( keyA ) );
				}
				
				return( ret );
			}
			catch( NotInvertibleException ex )
			{
				throw( new RuntimeException( "Invert Failed" ) );
			}
			
		}
	
	
	
	
	@Override
	protected EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>
		genMatrixInverseRight( final U dim , final SymbolicElemFactory<R, S> fac ,
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> elem
				) 
		{
			final Adim odim = new Adim( dim.getVal().subtract( BigInteger.ONE ) );
			
			final SquareMatrixElem<Adim,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> tmp = 
					new SquareMatrixElem<Adim,SymbolicElem<R, S>,SymbolicElemFactory<R, S>>( fac , odim );
			
			final Iterator<ArrayList<BigInteger>> ita = elem.getKeyIterator();
			while( ita.hasNext() )
			{
				final ArrayList<BigInteger> keyA = ita.next();
				final BigInteger row = keyA.get( 0 ).subtract( BigInteger.ONE );
				final BigInteger col = keyA.get( 1 ).subtract( BigInteger.ONE );
				tmp.setVal( row , col , elem.getVal( keyA ) );
			}
			
			try
			{
				final SquareMatrixElem<Adim,SymbolicElem<R, S>,SymbolicElemFactory<R, S>> inv = tmp.invertRight();
				
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> rA =
						new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , elem.getContravariantIndices() , elem.getCovariantIndices() );
				
				final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret =
						new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>(fac , elem.getContravariantIndices() , elem.getCovariantIndices() );
				
				inv.toRankTwoTensor( rA );
				
				final Iterator<ArrayList<BigInteger>> itb = rA.getKeyIterator();
				while( itb.hasNext() )
				{
					final ArrayList<BigInteger> keyA = ita.next();
					final ArrayList<BigInteger> keyB = new ArrayList<BigInteger>();
					keyB.add( keyA.get( 0 ).add( BigInteger.ONE ) );
					keyB.add( keyA.get( 1 ).add( BigInteger.ONE ) );
					ret.setVal( keyB , rA.getVal( keyA ) );
				}
				
				return( ret );
			}
			catch( NotInvertibleException ex )
			{
				throw( new RuntimeException( "Invert Failed" ) );
			}
			
		}
	
	

}


