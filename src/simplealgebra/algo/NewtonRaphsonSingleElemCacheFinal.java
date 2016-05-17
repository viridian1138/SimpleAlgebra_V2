






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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.*;


/**
 * Newton-Raphson evaluator for a single element, using the formula <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>x</mi>
 *      <mrow>
 *        <mi>n</mi>
 *        <mo>+</mo>
 *        <mn>1</mn>
 *      </mrow>
 *  </msub>
 *  <mo>=</mo>
 *  <msub>
 *          <mi>x</mi>
 *        <mi>n</mi>
 *  </msub>
 *  <mo>+</mo>
 *  <mfrac>
 *    <mrow>
 *      <mo>f</mo>
 *      <mfenced open="(" close=")" separators=",">
 *        <mrow>
 *          <msub>
 *                  <mi>x</mi>
 *                <mi>n</mi>
 *          </msub>
 *        </mrow>
 *      </mfenced>
 *    </mrow>
 *    <mrow>
 *      <msup>
 *              <mo>f</mo>
 *            <mo>'</mo>
 *      </msup>
 *      <mfenced open="(" close=")" separators=",">
 *        <mrow>
 *          <msub>
 *                  <mi>x</mi>
 *                <mi>n</mi>
 *          </msub>
 *        </mrow>
 *      </mfenced>
 *    </mrow>
 *  </mfrac>
 * </mrow>
 * </math>
 *
 * 
 * <P>See http://en.wikipedia.org/wiki/Newton's_method
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type for the evaluation.
 * @param <S> The factory for the enclosed type for the evaluation.
 */
public abstract class NewtonRaphsonSingleElemCacheFinal<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	/**
	 * The function over which to evaluate Netwon-Raphson.
	 */
	protected SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> function;
	
	/**
	 * The last iteration value calculated.
	 */
	protected R lastValue = null;
	
	/**
	 * The implicit space over which to evaluate the function.
	 */
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = null;
	
	/**
	 * The evaluation of the function for use in the Newton-Raphson method.
	 */
	protected SymbolicElem<R,S> eval;
	
	/**
	 * The evaluation of the partial derivative of the function for use in the Newton-Raphson method.
	 */
	protected SymbolicElem<R,S> partialEval;
	
	
	/**
	 * Constructs the evaluator.
	 * 
	 * @param _function The function over which to evaluate Newton-Raphson.
	 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
	 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
	 * @param cache Cache to be used for symbolic evals if useCachedEval() returns true.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public NewtonRaphsonSingleElemCacheFinal( final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> _function , 
			final ArrayList<? extends Elem<?,?>> _withRespectTo , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel,
			final HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		function = _function;
		final boolean useCachedEval = useCachedEval();
		eval = useCachedEval ? function.evalCached( implicitSpaceFirstLevel, cache) 
				: function.eval( implicitSpaceFirstLevel );
		partialEval = useCachedEval ? function.evalPartialDerivativeCached(_withRespectTo, implicitSpaceFirstLevel, cache) 
				: function.evalPartialDerivative(_withRespectTo, implicitSpaceFirstLevel );
		eval = handleSimplification( eval , useSimplification() );
		partialEval = handleSimplification( partialEval , useSimplification() );
	}
	
	
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
	 * Runs Newton-Raphson.
	 * 
	 * @param implicitSpaceInitialGuess The implicit space for the initial guess.
	 * @return An iterated result.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final HashMap<SCacheKey<R, S>, R> cache = new HashMap<SCacheKey<R, S>, R>();
		
		implicitSpace = implicitSpaceInitialGuess;
		lastValue = eval.evalCached( implicitSpace , cache );
		while( !( iterationsDone() ) )
		{
			performIteration();
		}
		return( lastValue );
	}
	
	/**
	 * Performs a Netwon-Raphson iteration.
	 * 
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected void performIteration() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final R derivative = evalPartialDerivative();
		R iterationOffset = lastValue.mult( derivative.invertLeft() ).negate();
		
		cacheIterationValue();
		
		performIterationUpdate( iterationOffset );
		
		R nextValue = evalIndicatesImprovement( );
		if( nextValue != null )
		{
			lastValue = nextValue;
		}
		else
		{
			int cnt = getMaxIterationsBacktrack();
			while( ( cnt > 0 ) && ( nextValue == null ) )
			{
				cnt--;
				iterationOffset = iterationOffset.divideBy( 2 );
				
				retrieveIterationValue();
				
				performIterationUpdate( iterationOffset );
				
				nextValue = evalIndicatesImprovement( );
			}
			if( nextValue != null )
			{
				lastValue = nextValue;
			}
			else
			{
				// No suitable iteration can be found.
				final HashMap<SCacheKey<R, S>, R> cache = new HashMap<SCacheKey<R, S>, R>();
				lastValue = eval.evalCached( implicitSpace , cache );
			}
		}
	}
	
	
	/**
	 * Returns whether convergence-wise the new function value should be accepted as an improvement over the old function value.
	 * 
	 * @param lastValue The old function value.
	 * @param nextValue The new function value.
	 * @return True iff. the new function value should be accepted as an improvement over the old function value.
	 */
	protected boolean evalIterationImproved( R lastValue , R nextValue )
	{
		return( true );
	}
	
	
	
	/**
	 * Returns the result of the Newton-Raphson evaluation if convergence-wise the new function value should be accepted as an improvement over the old function value.  Otherwise returns null.
	 * 
	 * @return The result of the Newton-Raphson evaluation if convergence-wise the new function value should be accepted as an improvement over the old function value.  Otherwise returns null.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected R evalIndicatesImprovement(  ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final HashMap<SCacheKey<R, S>, R> cache = new HashMap<SCacheKey<R, S>, R>();
		
		R ev = eval.evalCached( implicitSpace , cache  );
		
		if( !( evalIterationImproved( lastValue , ev ) ) )
		{
			ev = null;
		}
		
		return( ev );
	}
	
	
	
	/**
	 * Copies an instance for cloneThread();
	 * 
	 * @param in The instance to copy.
	 * @param threadIndex The index of the thread for which to clone.
	 */
	protected NewtonRaphsonSingleElemCacheFinal( NewtonRaphsonSingleElemCacheFinal<R,S> in , final BigInteger threadIndex )
	{
		function = in.function.cloneThread( threadIndex );
		if( in.lastValue != null )
		{
			lastValue = in.lastValue.cloneThread( threadIndex );
		}
		
		if( in.implicitSpace != null )
		{
			implicitSpace = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
			for( final Entry<? extends Elem<?,?>,? extends Elem<?,?>> ii : in.implicitSpace.entrySet() )
			{
				final Elem<?,?> ikey = ii.getKey();
				final Elem<?,?> ival = ii.getValue();
				( (HashMap) implicitSpace ).put( ikey.cloneThread(threadIndex) , ival.cloneThread(threadIndex) );
			}
		}
		
		eval = in.eval.cloneThread( threadIndex );
		
		partialEval = in.partialEval.cloneThread( threadIndex );
		
	}
	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract NewtonRaphsonSingleElem<R,S> cloneThread( final BigInteger threadIndex );
	
	
	
	
	/**
	 * Evaluates the derivative for a Netwon-Raphson iteration.
	 * 
	 * @return The calculated derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected R evalPartialDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final HashMap<SCacheKey<R, S>, R> cache = new HashMap<SCacheKey<R, S>, R>();
		
		return( partialEval.evalCached( implicitSpace , cache ) );
	}
	
	
	/**
	 * Updates function parameters based on the iteration.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected abstract void performIterationUpdate( R iterationOffset );
	
	
	/**
	 * Returns whether the iterations have completed.
	 * 
	 * @return True iff. the iterations are to complete.
	 */
	protected abstract boolean iterationsDone( );
	
	
	/**
	 * Caches the current iteration value.
	 */
	protected abstract void cacheIterationValue();
	
	
	/**
	 * Retrieves the current iteration value from the cache.
	 */
	protected abstract void retrieveIterationValue();
	
	
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
	 * In the event that an attempted Newton-Raphson iteration diverges from the desired answer, 
	 * gets the maximum number of attempts that can be used to backtrack onto the original pre-iteration value.
	 * 
	 * @return The maximum number of backtrack iterations.
	 */
	protected int getMaxIterationsBacktrack()
	{
		return( 100 );
	}

	
}



