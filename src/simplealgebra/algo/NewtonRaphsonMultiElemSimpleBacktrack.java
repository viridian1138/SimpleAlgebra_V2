






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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.algo.DescentAlgorithmMultiElem.DescentInverseFailedException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicZero;


/**
 * Evaluator for multivariate Newton-Raphson, using the formula <math display="inline">
 * <mrow>
 *  <msub>
 *          <mi>J</mi>
 *        <mi>F</mi>
 *  </msub>
 *  <mfenced open="(" close=")" separators=",">
 *    <mrow>
 *      <msub>
 *              <mi>x</mi>
 *            <mi>n</mi>
 *      </msub>
 *    </mrow>
 *  </mfenced>
 *  <mfenced open="(" close=")" separators=",">
 *    <mrow>
 *      <msub>
 *              <mi>x</mi>
 *          <mrow>
 *            <mi>n</mi>
 *            <mo>+</mo>
 *            <mn>1</mn>
 *          </mrow>
 *      </msub>
 *      <mo>-</mo>
 *      <msub>
 *              <mi>x</mi>
 *            <mi>n</mi>
 *      </msub>
 *    </mrow>
 *  </mfenced>
 *  <mo>=</mo>
 *  <mo>-</mo>
 *  <mi>F</mi>
 *  <mfenced open="(" close=")" separators=",">
 *    <mrow>
 *      <msub>
 *              <mi>x</mi>
 *            <mi>n</mi>
 *      </msub>
 *    </mrow>
 *  </mfenced>
 * </mrow>
 * </math>
 *
 * Includes a simple backtracking method.
 * 
 * <P>See http://en.wikipedia.org/wiki/Newton's_method
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions.
 * @param <R> The enclosed type for the evaluation.
 * @param <S> The factory for the enclosed type for the evaluation.
 */
public class NewtonRaphsonMultiElemSimpleBacktrack<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>>
	extends DescentAlgorithmMultiElem<U,R,S> {
	
	/**
	 * The functions over which to evaluate Netwon-Raphson.
	 */
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions;
	
	/**
	 * The last iteration values calculated.
	 */
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> lastValues = null;
	
	/**
	 * The implicit space over which to evaluate the functions.
	 */
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = null;
	
	/**
	 * The evaluations of the functions for use in the Newton-Raphson method.
	 */
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> evals;
	
	/**
	 * The evaluation of the Jacobian for use in the Newton-Raphson method.
	 */
	protected SquareMatrixElem<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> partialEvalJacobian;
	
	/**
	 * The number of dimensions over which to evaluate Newton-Raphson.
	 */
	protected U dim;
	
	/**
	 * The factory for the enclosed type.
	 */
	protected SymbolicElemFactory<R,S> sfac;
	
	/**
	 * Input parameters for the algorithm.
	 */
	protected DescentAlgorithmMultiElemInputParam<U,R,S> param;
	
	/**
	 * Constructs the evaluator.
	 * 
	 * @param _param The input parameters for the evaluator.
	 * @param cache Cache to be used for symbolic evals if useCachedEval() returns true.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public NewtonRaphsonMultiElemSimpleBacktrack( 
			final DescentAlgorithmMultiElemInputParam<U,R,S> _param,
			final HashMap<SCacheKey<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, SymbolicElem<R, S>> cache )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		functions = _param.getFunctions();
		dim = _param.getDim();
		sfac = _param.getSfac();
		param = _param;
		final SimplificationType useSimplification = _param.useSimplification();
		final boolean useCachedEval = _param.useCachedEval();
		evals = new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( 
				_param.getSfac() , _param.getDim() , new GeometricAlgebraOrd<U>() );
		for( final Entry<HashSet<BigInteger>, SymbolicElem<SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> ii : functions.getEntrySet() )
		{
			final HashSet<BigInteger> key = ii.getKey();
			SymbolicElem<R,S> evalF = useCachedEval ? 
					ii.getValue().evalCached( _param.getImplicitSpaceFirstLevel() , cache ) :
					ii.getValue().eval( _param.getImplicitSpaceFirstLevel() );
			evalF = handleSimplification( evalF , useSimplification );
			evals.setVal( key , evalF );
		}
		partialEvalJacobian = new SquareMatrixElem<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>(
				_param.getSfac() , _param.getDim() );
		BigInteger bcnt = BigInteger.ZERO;
		for( final ArrayList<? extends Elem<?,?>> withRespectTo : _param.getWithRespectTos() )
		{
			final BigInteger key = bcnt;
			bcnt = bcnt.add( BigInteger.ONE );
			for( final HashSet<BigInteger> key2A : functions.getKeySet() )
			{
				final BigInteger key2 = key2A.iterator().next();
				final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> fun = _param.getFunctions().get( key2A );
				SymbolicElem<R,S> evalP = useCachedEval ?
						fun.evalPartialDerivativeCached( withRespectTo , _param.getImplicitSpaceFirstLevel() , cache) :
						fun.evalPartialDerivative( withRespectTo , _param.getImplicitSpaceFirstLevel() );
				evalP = handleSimplification( evalP , useSimplification );
				if( !( evalP instanceof SymbolicZero<?,?> ) ) // Allow the matrix to be sparse in instances where the derivative is zero.
				{
					partialEvalJacobian.setVal( key2 , key , evalP );
				}
			}
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
	public GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		implicitSpace = implicitSpaceInitialGuess;
		lastValues = evalValues();
		while( !( param.iterationsDone() ) )
		{
			performIteration();
		}
		return( lastValues );
	}
	
	
	/**
	 * Performs a Netwon-Raphson iteration.
	 * 
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected void performIteration() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final SquareMatrixElem<U,R,S> derivativeJacobian = evalPartialDerivativeJacobian();
		SquareMatrixElem<U,R,S> derivativeJacobianInverse = null;
		
		try
		{
			derivativeJacobianInverse = derivativeJacobian.invertLeft();
		}
		catch( SquareMatrixElem.NoPivotException ex )
		{
			// printInverseCheck( derivativeJacobian );
			throw( new DescentInverseFailedException( ex.getElemNum() ) );
		}
		
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset =
				new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S>(
						sfac.getFac(), dim, new GeometricAlgebraOrd<U>() );
		lastValues.colVectorMultLeftDefault( derivativeJacobianInverse , iterationOffset );
		iterationOffset = iterationOffset.negate();
		
		param.performIterationUpdate( iterationOffset );
		
		
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> nextValues = evalIndicatesImprovement();
		if( nextValues != null )
		{
			lastValues = nextValues;
		}
		else
		{
			int cnt = param.getMaxIterationsBacktrack();
			iterationOffset = iterationOffset.negate();
			while( ( cnt > 0 ) && ( nextValues == null ) )
			{
				cnt--;
				iterationOffset = iterationOffset.divideBy( 2 );
				
				param.performIterationUpdate( iterationOffset );
				
				nextValues = evalIndicatesImprovement();
			}
			// System.out.println( cnt );
			if( nextValues != null )
			{
				lastValues = nextValues;
			}
			else
			{
				// No suitable iteration can be found.
				lastValues = evalValues();
			}
		}
		
	}
	
	
	
	/**
	 * Returns the result of the Newton-Raphson evaluation if convergence-wise the new function value should be accepted as an improvement over the old function value.  Otherwise returns null.
	 * 
	 * @return The result of the Newton-Raphson evaluation if convergence-wise the new function value should be accepted as an improvement over the old function value.  Otherwise returns null.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> evalIndicatesImprovement( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> ev = evalValues();
		
		if( !( param.evalIterationImproved( lastValues , ev ) ) )
		{
			ev = null;
		}
		
		return( ev );
	}
	
	
	
	
	/**
	 * Evaluates result values for a single Netwon-Raphson iteration.
	 * 
	 * @return The result values for a single Newton-Raphson iteration.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> evalValues( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> ret = new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S>(
				sfac.getFac(), dim, new GeometricAlgebraOrd<U>() );
		
		for( final Entry<HashSet<BigInteger>, SymbolicElem<R, S>> ii : evals.getEntrySet() )
		{
			final HashSet<BigInteger> key = ii.getKey();
			ret.setVal( key , ii.getValue().eval( implicitSpace ) );
		}
		
		return( ret );
	}
	
	
	/**
	 * Evaluates the Jacobian for a single Newton-Raphson iteration.
	 * 
	 * @return The Jacobian for a single Newton-Raphson iteration.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected SquareMatrixElem<U,R,S> evalPartialDerivativeJacobian() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final SquareMatrixElem<U,R,S> evalJacobian = new SquareMatrixElem<U,R,S>( sfac.getFac() , dim );
		
		for( final HashSet<BigInteger> key2A : functions.getKeySet() )
		{
			final BigInteger key2 = key2A.iterator().next();
			final GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> row = 
						new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>(
								sfac , dim, new GeometricAlgebraOrd<U>() );
			partialEvalJacobian.rowVectorToGeometricAlgebra( key2 , row );
			for( final HashSet<BigInteger> keyA : row.getKeySet() )
			{
				final BigInteger key = keyA.iterator().next();
				final SymbolicElem<R,S> pe = partialEvalJacobian.get( key2 , key );
				// System.out.println( "*****" );
				// pe.writeString( System.out );
				evalJacobian.setVal( key2 , key , pe.eval( implicitSpace ) );
				// System.out.println( "-----" );
			}
		}
		
		return( evalJacobian );
	}
	
	
	
	/**
	 * Copies an instance for cloneThread();
	 * 
	 * @param in The instance to copy.
	 * @param threadIndex The index of the thread for which to clone.
	 */
	protected NewtonRaphsonMultiElemSimpleBacktrack( NewtonRaphsonMultiElemSimpleBacktrack<U,R,S> in , final BigInteger threadIndex )
	{
		functions = in.functions.cloneThread(threadIndex);
		
		if( in.lastValues != null )
		{
			lastValues = in.lastValues.cloneThread(threadIndex);
		}
		
		for( final Entry<? extends Elem<?, ?>, ? extends Elem<?, ?>> ii : in.implicitSpace.entrySet() )
		{
			final Elem<?,?> ikey = ii.getKey();
			final Elem<?,?> ival = ii.getValue();
			( (HashMap) implicitSpace ).put( ikey.cloneThread(threadIndex) , ival.cloneThread(threadIndex) );
		}
		
		evals = in.evals.cloneThread(threadIndex);
		
		partialEvalJacobian = in.partialEvalJacobian.cloneThread(threadIndex);
		
		// It is presumed that the NumDimensions dim is immutable.
		dim = in.dim;
		
		sfac = in.sfac.cloneThread(threadIndex);
		
		param = in.param.cloneThread(threadIndex);
	}
	
	
	
	@Override
	public NewtonRaphsonMultiElemSimpleBacktrack<U,R,S> cloneThread( BigInteger threadIndex )
	{
		return( new NewtonRaphsonMultiElemSimpleBacktrack<U,R,S>( this , threadIndex ) );
	}
	
	

}



