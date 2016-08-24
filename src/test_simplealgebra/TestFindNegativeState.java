



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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.algo.NewtonRaphsonSingleElemFunctional;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 *  Tests the ability to find a p where p * ln( p ) is negative.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestFindNegativeState extends TestCase {
	
	

	
	/**
	 * Evaluator for computing a p where p * ln( p ) is negative.
	 * 
	 * @author tgreen
	 *
	 */
	protected class NegativeStateEvaluator
	{
		/**
		 * The argument for which to take p * ln( p ).
		 */
		protected ComplexElem<DoubleElem,DoubleElemFactory> inputValue;
		
		/**
		 * Number of iterations to build the underlying exponential approximation.
		 */
		protected int numIterExp;
		
		/**
		 * Current best value for the solution.
		 */
		protected ComplexElem<DoubleElem,DoubleElemFactory> evalValue;
		
		/**
		 * Cached solution used for backtracking.
		 */
		protected ComplexElem<DoubleElem,DoubleElemFactory> evalCache;
		
		/**
		 * Number of iterations to attempt to converge to the negative value.
		 */
		protected int numIterLn;
		
		
		/**
		 * The iteration count for Newton-Raphson iterations.
		 */
		protected int intCnt = 0;
		
		
		/**
		 * Constructs the evaluator.
		 * @param _inputValue The argument for which to compute p * ln{ p ).
		 * @param _numIterExp Number of iterations to build the underlying exponential approximation.
	     * @param _numIterLn Number of iterations to attempt to converge to the negative value.
		 */
		public NegativeStateEvaluator( ComplexElem<DoubleElem,DoubleElemFactory> _inputValue, int _numIterExp , int _numIterLn )
		{
			inputValue = _inputValue;
			numIterExp = _numIterExp;
			numIterLn = _numIterLn;
		}
		
		protected ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> revEval(
				ComplexElem<DoubleElem,DoubleElemFactory> t4 , ComplexElem<DoubleElem,DoubleElemFactory> t5 )
		{
			ComplexElem<DoubleElem,DoubleElemFactory> c1 = new ComplexElem<DoubleElem,DoubleElemFactory>( t4.getRe().add( t5.getRe().negate() ) , t4.getIm() );
			ComplexElem<DoubleElem,DoubleElemFactory> c2 = new ComplexElem<DoubleElem,DoubleElemFactory>( t5.getRe().add( t4.getRe().negate() ) , t5.getIm().add( t4.getRe().negate() ) );
			return( new ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( c1 , c2 ) );
		}
		
		/**
		 * Populates the initial guess from which to start the evaluations.
		 */
		protected void populateEvalValue() throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			evalValue = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 1.2 ) ,new  DoubleElem( 1.5 ) );
		}
		
		/**
		 * Evaluates and returns an approximate answer.
		 * @return The approximate answer.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public ComplexElem<DoubleElem,DoubleElemFactory> performEval() throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final AElemVal aelemval = new AElemVal();
			final AElemPartial aelempartial = new AElemPartial();
			
			populateEvalValue();
			
			final AEval aeval = new AEval( aelemval , aelempartial );
			aeval.eval( null );
			return( evalValue );
		}
		
		
		/**
		 * Newton-Raphson evaluator for the function to be solved.
		 * 
		 * @author tgreen
		 *
		 */
		private class AEval extends NewtonRaphsonSingleElemFunctional<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		{

			/**
			 * Constructs the evaluator.
			 * @param _eval The function to be solved.
			 * @param _partialEval The approximate partial derivative for the function to be solved.
			 * @throws NotInvertibleException
			 * @throws MultiplicativeDistributionRequiredException
			 */
			public AEval(SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _eval, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _partialEval )
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				super(_eval, _partialEval, null, null);
			}

			@Override
			protected void performIterationUpdate(ComplexElem<DoubleElem,DoubleElemFactory> iterationOffset) {
				evalValue = evalValue.add( iterationOffset );
			}

			@Override
			protected boolean iterationsDone() {
				intCnt++;
				return( intCnt > numIterLn );
			}

			@Override
			protected void cacheIterationValue() {
				evalCache = evalValue;
			}

			@Override
			protected void retrieveIterationValue() {
				evalValue = evalCache;
			}
			
			@Override
			protected ComplexElem<DoubleElem,DoubleElemFactory> handleDescentInverseFailed( ComplexElem<DoubleElem,DoubleElemFactory> derivative , NotInvertibleException ex )
			{
				return( derivative.add( derivative.getFac().identity().divideBy( 1000 ) ) );
			}

			@Override
			public NewtonRaphsonSingleElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>> cloneThread(
					BigInteger threadIndex) {
				throw( new RuntimeException( "Not Supported" ) );
			}

			@Override
			public NewtonRaphsonSingleElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>> cloneThreadCached(
					CloneThreadCache<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> cache,
					CloneThreadCache<?, ?> cacheImplicit, BigInteger threadIndex) {
				throw( new RuntimeException( "Not Supported" ) );
			}
			
		}
		
		
		/**
		 * Represents the function to be solved.
		 * 
		 * @author thorngreen
		 *
		 */
		private class AElemVal extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		{

			/**
			 * Constructs the elem.
			 */
			public AElemVal( ) {
				super( inputValue.getFac() );
			}

			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				return( ( evalValue.mult( evalValue.ln(numIterExp,numIterLn) ) ).add( inputValue.negate() ) );
			}
			
			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> evalCached(
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}
			
			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(
					ArrayList<? extends Elem<?, ?>> withRespectTo,
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public String writeDesc( WriteElemCache<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> cache , PrintStream ps )
			{
				String st = cache.get( this );
				if( st == null )
				{
					final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
					st = cache.getIncrementVal();
					cache.put(this, st);
					ps.print( AElemVal.class.getSimpleName() );
					ps.print( " " );
					ps.print( st );
					ps.print( " = new " );
					ps.print( AElemVal.class.getSimpleName() );
					ps.print( "( " );
					ps.println( " );" );
				}
				return( st );
			}
			

		}
		
		
		
		
		/**
		 * Represents the approximate derivative of the function to be solved.
		 * 
		 * @author thorngreen
		 *
		 */
		private class AElemPartial extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		{

			/**
			 * Constructs the elem.
			 */
			public AElemPartial( ) {
				super( inputValue.getFac() );
			}

			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				return( ( evalValue.ln(numIterExp,numIterLn) ).add( fac.identity() ) );
			}
			
			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> evalCached(
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}
			
			@Override
			public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(
					ArrayList<? extends Elem<?, ?>> withRespectTo,
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public String writeDesc( WriteElemCache<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> cache , PrintStream ps )
			{
				String st = cache.get( this );
				if( st == null )
				{
					final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
					st = cache.getIncrementVal();
					cache.put(this, st);
					ps.print( AElemPartial.class.getSimpleName() );
					ps.print( " " );
					ps.print( st );
					ps.print( " = new " );
					ps.print( AElemPartial.class.getSimpleName() );
					ps.print( "( " );
					ps.println( " );" );
				}
				return( st );
			}
			

		}
		
		
		
		
	}
	
	
	
	
	/**
	 * Tests the ability to find a p where p * ln( p ) is negative.
	 * @throws Throwable
	 */
	public void testFindNegativeState() throws Throwable
	{
		final double NEG_VAL = -5.5;
		final ComplexElem<DoubleElem,DoubleElemFactory> de = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( NEG_VAL ) , new DoubleElem( 0.0 ) );
		final NegativeStateEvaluator neg = new NegativeStateEvaluator( de , 20 , 20 );
		ComplexElem<DoubleElem,DoubleElemFactory> r = neg.performEval();
		// System.out.println( r.getRe().getVal() );
		// System.out.println( r.getIm().getVal() );
		ComplexElem<DoubleElem,DoubleElemFactory> res = r.mult( r.ln(20, 20) );
		Assert.assertTrue( Math.abs( res.getRe().getVal() - NEG_VAL ) < 1E-5 );
		Assert.assertTrue( Math.abs( res.getIm().getVal() ) < 1E-5 );
	}
	
	
	
	
}



