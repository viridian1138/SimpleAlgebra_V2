






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
import java.util.Map.Entry;

import simplealgebra.BadCreationException;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


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
public abstract class NewtonRaphsonSingleElemFunctional<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
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
	 * @param _eval The evaluation of the function for use in the Newton-Raphson method.
	 * @param _partialEval The evaluation of the partial derivative of the function for use in the Newton-Raphson method.
	 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
	 * @param cache Cache to be used for symbolic evals if useCachedEval() returns true.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public NewtonRaphsonSingleElemFunctional( final SymbolicElem<R,S> _eval ,
			final SymbolicElem<R,S> _partialEval ,
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel,
			final HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		eval = _eval;
		partialEval = _partialEval;
		final boolean useCachedEval = useCachedEval();
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
		implicitSpace = implicitSpaceInitialGuess;
		lastValue = eval.eval( implicitSpace );
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
		R derivative = evalPartialDerivative();
		R derivativeInverse = null;
		
		while( derivativeInverse == null )
		{
			try
			{
				derivativeInverse = derivative.invertLeft();
			}
			catch( NotInvertibleException ex )
			{
				derivative = handleDescentInverseFailed( derivative , ex );
			}
		}
		
		R iterationOffset = lastValue.mult( derivativeInverse ).negate();
		
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
				lastValue = eval.eval( implicitSpace );
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
		final Comparable nextC = (Comparable)( nextValue.totalMagnitude() );
		// System.out.println( ( (simplealgebra.DoubleElem)( lastValue.totalMagnitude() ) ).getVal() );
		return( nextC.compareTo( lastValue.totalMagnitude() ) < 0 );
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
		try
		{
			R ev = eval.eval( implicitSpace );
		
			if( !( evalIterationImproved( lastValue , ev ) ) )
			{
				ev = null;
			}
		
			// if( ev != null ) System.out.println( ev );
			return( ev );
		}
		catch( BadCreationException ex )
		{
			// System.out.print( "Bad Creation evalIndicatesImprovement: " );
			// ex.printStackTrace( System.out );
			return( null );
		}
	}
	
	
	
	/**
	 * Copies an instance for cloneThread();
	 * 
	 * @param in The instance to copy.
	 * @param threadIndex The index of the thread for which to clone.
	 */
	protected NewtonRaphsonSingleElemFunctional( NewtonRaphsonSingleElem<R,S> in , final BigInteger threadIndex )
	{	
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
	 * Copies an instance for cloneThreadCached();
	 * 
	 * @param in The instance to copy.
	 * @param cache The cache for cloning elems.
	 * @param cacheImplicit The cache for cloning implicit space elems.
	 * @param threadIndex The index of the thread for which to clone.
	 */
	protected NewtonRaphsonSingleElemFunctional( NewtonRaphsonSingleElem<R,S> in , 
			final CloneThreadCache<SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> cache , 
			CloneThreadCache<?,?> cacheImplicit , final BigInteger threadIndex )
	{
		final CloneThreadCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cacheA = (CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>)( cache.getInnerCache() );
		final CloneThreadCache<R,S> cacheB = (CloneThreadCache<R,S>)( cacheA.getInnerCache() );
		if( in.lastValue != null )
		{
			lastValue = in.lastValue.cloneThreadCached( threadIndex , (CloneThreadCache) cacheB );
		}
		
		if( in.implicitSpace != null )
		{
			implicitSpace = (HashMap<? extends Elem<?,?>,? extends Elem<?,?>>)( new HashMap() );
		
			for( final Entry<? extends Elem<?,?>,? extends Elem<?,?>> ii : in.implicitSpace.entrySet() )
			{
				final Elem<?,?> ikey = ii.getKey();
				final Elem<?,?> ival = ii.getValue();
				( (HashMap) implicitSpace ).put( ikey.cloneThreadCached(threadIndex,(CloneThreadCache)(cacheImplicit)) , 
						ival.cloneThreadCached(threadIndex,(CloneThreadCache)(cacheImplicit)) );
			}
		}
		
		eval = in.eval.cloneThreadCached( threadIndex , cacheA );
		
		partialEval = in.partialEval.cloneThreadCached( threadIndex , cacheA );
		
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
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param cache The cloning cache to remove duplicates.
	 * @param cacheImplicit The cloning cache used to remove implicit duplicates.
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public abstract NewtonRaphsonSingleElem<R,S> cloneThreadCached( 
			final CloneThreadCache<SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> cache , 
			CloneThreadCache<?,?> cacheImplicit ,
			final BigInteger threadIndex );
	
	
	
	
	/**
	 * Evaluates the derivative for a Netwon-Raphson iteration.
	 * 
	 * @return The calculated derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected R evalPartialDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( partialEval.eval( implicitSpace ) );
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
		return( SimplificationType.NONE );
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
	
	
	/**
	 * Handles a failure to invert the derivative.
	 * @param derivative The derivative to be inverted.
	 * @param ex The thrown inverse failure exception.
	 * @return Altered derivative to repair the inverse failure, or throw NotInvertibleException.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected R handleDescentInverseFailed( R derivative , NotInvertibleException ex )
		throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		throw( ex );
	}

	
	
}




