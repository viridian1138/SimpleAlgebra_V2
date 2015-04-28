






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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
public abstract class NewtonRaphsonSingleElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
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
	 * @param _function The function over which to evaluate Netwon-Raphson.
	 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
	 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public NewtonRaphsonSingleElem( final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> _function , 
			final ArrayList<? extends Elem<?,?>> _withRespectTo , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		function = _function;
		eval = function.eval( implicitSpaceFirstLevel );
		partialEval = function.evalPartialDerivative(_withRespectTo, implicitSpaceFirstLevel );
		if( useSimplification() )
		{
			eval = eval.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
			partialEval = partialEval.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
		}
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
		final R derivative = evalPartialDerivative();
		R iterationOffset = lastValue.mult( derivative.invertLeft() ).negate();
		
		performIterationUpdate( iterationOffset );
		
		R nextValue = evalIndicatesImprovement( );
		if( nextValue != null )
		{
			lastValue = nextValue;
		}
		else
		{
			int cnt = getMaxIterationsBacktrack();
			iterationOffset = iterationOffset.negate();
			while( ( cnt > 0 ) && ( nextValue == null ) )
			{
				cnt--;
				iterationOffset = iterationOffset.divideBy( 2 );
				
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
		R ev = eval.eval( implicitSpace );
		
		if( !( evalIterationImproved( lastValue , ev ) ) )
		{
			ev = null;
		}
		
		return( ev );
	}
	
	
	
	
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
	 * Returns true iff. expression simplification is to be used.  
	 * Override this method to turn off expression simplification.
	 * 
	 * @return True iff. simplification is to be used.
	 */
	protected boolean useSimplification()
	{
		return( true );
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



