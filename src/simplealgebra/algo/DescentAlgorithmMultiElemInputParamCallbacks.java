






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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Input parameter for a MultiElem descent algorithm.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions.
 * @param <R> The enclosed type for the evaluation.
 * @param <S> The factory for the enclosed type for the evaluation.
 */
public abstract class DescentAlgorithmMultiElemInputParamCallbacks<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	
	

	/**
	 * Updates function parameters based on the iteration.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected abstract void performIterationUpdate( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset );
	
	
	
	/**
	 * Sets the current value of the iteration.
	 * 
	 * @param value The new value.
	 */
	protected abstract void setIterationValue( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> value );
	
	
	
	/**
	 * Gets the current value of the iteration.
	 * 
	 * @return The current value.
	 */
	protected abstract GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> getIterationValue( );
	
	
	/**
	 * Caches the current iteration value.
	 */
	protected abstract void cacheIterationValue();
	
	
	/**
	 * Retrieves the current iteration value from the cache.
	 */
	protected abstract void retrieveIterationValue();
	
	
	/**
	 * Returns whether the iterations have completed.
	 * 
	 * @return True iff. the iterations are to complete.
	 */
	protected abstract boolean iterationsDone( );
	
	
	/**
	 * Returns the type of simplification to be used.  
	 * Override this method to turn off expression simplification.
	 * 
	 * @return The type of simplification to be used.
	 */
	protected SimplificationType useSimplification()
	{
		return( SimplificationType.DISTRIBUTE_SIMPLIFY2 );
	}
	
	
	/**
	 * Returns whether cached evals are to be used.
	 * Override this method to turn on cached evals.
	 * 
	 * @return True iff. cached evals are to be used.
	 */
	protected boolean useCachedEval()
	{
		return( false );
	}
	
	
	/**
	 * In the event that an attempted descent algorithm iteration diverges from the desired answer, 
	 * gets the maximum number of attempts that can be used to backtrack onto the original pre-iteration value.
	 * 
	 * @return The maximum number of backtrack iterations.
	 */
	protected int getMaxIterationsBacktrack()
	{
		return( 100 );
	}
	
	
	/**
	 * Returns whether convergence-wise the new function value should be accepted as an improvement over the old function value.
	 * 
	 * @param lastValue The old function value.
	 * @param newValue The new function value.
	 * @return True iff. the new function value should be accepted as an improvement over the old function value.
	 */
	protected boolean evalIterationImproved( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> lastValue , 
			GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> newValue )
	{
		return( true );
	}
	
	
	
	
	
	/**
	 * Constructs the callback object.
	 */
	public DescentAlgorithmMultiElemInputParamCallbacks( )
	{
		
	}

	
	

}




