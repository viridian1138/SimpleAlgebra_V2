






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






package simplealgebra.algo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Base class for multi=elem descent algorithms.
 *
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions.
 * @param <R> The enclosed type for the evaluation.
 * @param <S> The factory for the enclosed type for the evaluation.
 */
public abstract class DescentAlgorithmMultiElem<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	/**
	 * Exception indicating the failure of the descent inverse process.
	 * 
	 * @author tgreen
	 *
	 */
	public static final class DescentInverseFailedException extends NotInvertibleException
	{
		/**
		 * The key of the elem. related to the inverse failure.
		 */
		protected BigInteger elemNum;
		
		/**
		 * Constructs the exception.
		 * 
		 * @param elemNum_ The key of the elem. related to the inverse failure.
		 */
		public DescentInverseFailedException( final BigInteger elemNum_ )
		{
			elemNum = elemNum;
		}
		
		@Override
		public String toString()
		{
			return( "Descent Inverse Failed For Key " + elemNum );
		}
		
		/**
		 * Returns the key of the elem. related to the inverse failure.
		 * 
		 * @return The key of the elem. related to the inverse failure.
		 */
		public BigInteger getElemNum()
		{
			return( elemNum );
		}
		
	};
	
	
	/**
	 * Runs the descent algorithm.
	 * 
	 * @param implicitSpaceInitialGuess The implicit space for the initial guess.
	 * @return An iterated result.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public abstract GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract DescentAlgorithmMultiElem<U,R,S> cloneThread( final BigInteger threadIndex );
	
	
	/**
	 * Handles the simplification of the elem.
	 * 
	 * @param in The elem to be simplified.
	 * @param smplType The type of simplification to be performed.
	 * @return The simplified elem.
	 * @throws NotInvertibleException
	 */
	protected SymbolicElem<R,S> handleSimplification( final SymbolicElem<R,S> in , final SimplificationType smplType ) throws NotInvertibleException
	{
		switch( smplType )
		{
			case NONE:
				return( in );
			case DISTRIBUTE_SIMPLIFY:
				return( in.distributeSimplify() );
			case DISTRIBUTE_SIMPLIFY2:
				return( in.distributeSimplify2() );
		}
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	
	/**
	 * Estimates whether the rows and columns of the Jacobian matrix appear to be invertible.
	 * 
	 * @param derivativeJacobian The Jacobian matrix to check.
	 */
	protected void printInverseCheck( final SquareMatrixElem<U,R,S> derivativeJacobian )
	{
		final HashSet<BigInteger> zeroRows = new HashSet<BigInteger>();
		final HashSet<BigInteger> zeroCols = new HashSet<BigInteger>();
		
		derivativeJacobian.checkInverse( zeroRows, zeroCols );
		
		System.out.println( zeroRows );
		System.out.println( zeroCols );
	}
	

}



