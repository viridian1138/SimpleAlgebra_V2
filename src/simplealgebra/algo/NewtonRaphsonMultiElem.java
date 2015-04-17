






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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicOps;
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
public abstract class NewtonRaphsonMultiElem<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
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
	 * Constructs the evaluator.
	 * 
	 * @param _functions The functions over which to evaluate Netwon-Raphson.
	 * @param _withRespectTos The set of variables over which to take derivatives.
	 * @param implicitSpaceFirstLevel The initial implicit space over which to take the functions and their derivatives.
	 * @param _sfac The factory for the enclosed type.
	 * @param _dim The number of dimensions over which to evaluate Newton-Raphson.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public NewtonRaphsonMultiElem( final GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _functions , 
			final ArrayList<ArrayList<? extends Elem<?,?>>> _withRespectTos , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel ,
			final SymbolicElemFactory<R,S> _sfac ,
			final U _dim )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		functions = _functions;
		dim = _dim;
		sfac = _sfac;
		final boolean useSimplification = useSimplification();
		Iterator<HashSet<BigInteger>> ita = functions.getKeyIterator();
		evals = new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( 
				_sfac , _dim , new GeometricAlgebraOrd<U>() );
		while( ita.hasNext() )
		{
			final HashSet<BigInteger> key = ita.next();
			SymbolicElem<R,S> evalF = functions.get( key ).eval( implicitSpaceFirstLevel );
			if( useSimplification )
			{
				evalF = evalF.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
			}
			evals.setVal( key , evalF );
		}
		partialEvalJacobian = new SquareMatrixElem<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>(_sfac, _dim);
		final Iterator<ArrayList<? extends Elem<?, ?>>> itb = _withRespectTos.iterator();
		BigInteger bcnt = BigInteger.ZERO;
		while( itb.hasNext() )
		{
			final BigInteger key = bcnt;
			bcnt = bcnt.add( BigInteger.ONE );
			final ArrayList<? extends Elem<?,?>> withRespectTo = itb.next();
			ita = functions.getKeyIterator();
			while( ita.hasNext() )
			{
				final HashSet<BigInteger> key2A = ita.next();
				final BigInteger key2 = key2A.iterator().next();
				final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> fun = _functions.get( key2A );
				SymbolicElem<R,S> evalP = fun.evalPartialDerivative( withRespectTo , implicitSpaceFirstLevel );
				if( useSimplification )
				{
					evalP = evalP.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
				}
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
		while( !( iterationsDone() ) )
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
		final SquareMatrixElem<U,R,S> derivativeJacobianInverse = derivativeJacobian.invertLeft();
		
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset =
				new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S>(
						sfac.getFac(), dim, new GeometricAlgebraOrd<U>() );
		lastValues.colVectorMultLeftDefault( derivativeJacobianInverse , iterationOffset );
		iterationOffset = iterationOffset.negate();
		
		performIterationUpdate( iterationOffset );
		
		lastValues = evalValues();
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
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> ret = new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S>(
				sfac.getFac(), dim, new GeometricAlgebraOrd<U>() );
		
		Iterator<HashSet<BigInteger>> ita = evals.getKeyIterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> key = ita.next();
			ret.setVal( key , evals.get( key ).eval( implicitSpace ) );
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
		
		final Iterator<HashSet<BigInteger>> ita = functions.getKeyIterator();
		while( ita.hasNext() )
		{
			final HashSet<BigInteger> key2A = ita.next();
			final BigInteger key2 = key2A.iterator().next();
			final GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> row = 
						new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>(
								sfac , dim, new GeometricAlgebraOrd<U>() );
			partialEvalJacobian.rowVectorToGeometricAlgebra( key2 , row );
			final Iterator<HashSet<BigInteger>> itb = row.getKeyIterator();
			while( itb.hasNext() )
			{
				final HashSet<BigInteger> keyA = itb.next();
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
	 * Updates function parameters based on the iteration.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected abstract void performIterationUpdate( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset );
	
	
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
	

}



