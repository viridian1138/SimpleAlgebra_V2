



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




package simplealgebra;


import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.mvel2.optimizers.OptimizerFactory;

import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.algo.NewtonRaphsonSingleElemFunctional;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.LoggingConfiguration;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;
import simplealgebra.symbolic.SymbolicPlaceholder;


/**
 * An elem implements properties similar to those for a noncommutative ring, though there are some important
 * differences:
 * <P>
 * <P>
 * Division by a non-zero integer is a fundamental operation of an elem.  For instance, it is impossible
 * to calculate the mean of multiple elems without the ability to divide by the number of elems in the sum.
 * In several other important cases it is important to apply some sort of fraction.
 * <P>
 * <P>
 * When a derivative operator is "multiplied" by a term "X" the operation is interpreted as applying the
 * derivative to "X".  In such a case some typical axioms of rings do not apply.  For instance, in a typical
 * ring Y * I reduces to Y where I is the identity.  However, if Y is a partial derivative operator then
 * Y * I should be the application of the derivative to the constant I which should be zero.  Hence, the "I"
 * shouldn't be reduced from Y * I in this case. 
 * <P>
 * <P>
 * For noncommutative products, separate right-side and left-side inverses are defined.  The right-side
 * inverse doesn't necessarily equal the left-side inverse.
 * <P>
 * <P> It is possible to compute inverses of elems.  However, the algebra is not generally a division
 * algebra because it is possible to have a non-zero elem, such as a non-zero square matrix, that
 * doesn't have an inverse.
 * <P>
 * <P> If the isMultCommutative() method of the elem's type's factory returns true, then all multiplications 
 * of the elem type commute.  Otherwise, it is possible that multiplications
 * of the elem type do not commute.  
 * <P>
 * <P> See http://en.wikipedia.org/wiki/noncommutative_ring
 * <P>
 * <P>
 * <P> If the isMultAssociative() method of the elem's type's factory returns true, then all multiplications 
 * of the elem type are associative.  Otherwise, it is possible that multiplications
 * of the elem type are not associative. 
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 * @param <T> The elem type.
 * @param <R> The factory for the elem type.
 */
public abstract class Elem<T extends Elem<T,?>, R extends ElemFactory<T,R>> {

	/**
	 * Adds the elem passed in the parameter.
	 * 
	 * @param b The elem to be added.
	 * @return The result of the addition.
	 */
	public abstract T add( T b );
	
	/**
	 * Multiplies the elem in the parameter on the right.
	 * 
	 * @param b The elem to be multiplied.
	 * @return The result of the multiplication.
	 */
	public abstract T mult( T b );
	
	/**
	 * Returns the negation of the elem.
	 * 
	 * @return The negation of the elem.
	 */
	public abstract T negate( );
	
	/**
	 * Returns the left-side inverse of the elem satisfying <math display="inline">
     * <mrow>
     *  <msup>
     *          <mi>A</mi>
     *        <mo>-1L</mo>
     *  </msup>
     *  <mi>A</mi>
     *  <mo>=</mo>
     *  <mi>I</mi>
     * </mrow>
     * </math>
	 * 
	 * @return The left-inverse of the elem.
	 * @throws NotInvertibleException
	 */
	public abstract T invertLeft( ) throws NotInvertibleException;
	
	/**
	 * Returns the right-side inverse of the elem satisfying <math display="inline">
     * <mrow>
     *  <mi>A</mi>
     *  <msup>
     *          <mi>A</mi>
     *        <mo>-1R</mo>
     *  </msup>
     *  <mo>=</mo>
     *  <mi>I</mi>
     * </mrow>
     * </math>
	 * 
	 * @return The right-inverse of the elem.
	 * @throws NotInvertibleException
	 */
	public abstract T invertRight( ) throws NotInvertibleException;
	
	/**
	 * Divides the elem. by a non-zero integer.
	 * 
	 * @param val The integer by which to divide.
	 * @return The elem divided by the integer.
	 */
	public abstract T divideBy( BigInteger val );
	
	/**
	 * Returns the factory of the elem.
	 * 
	 * @return The factory of the elem.
	 */
	public abstract R getFac();
	
	/**
	 * Returns the total magnitude of the elem.  The returned elem, if not null, can be safely cast to Comparable.
	 * @return The total magnitude of the elem.  The returned elem, if not null, can be safely cast to Comparable. 
	 */
	public abstract Elem<?,?> totalMagnitude();
	
	
	
	/**
	 * Produces a clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public T cloneThread( final BigInteger threadIndex )
	{
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	/**
	 * Produces a cached clone of the object for threading.  Note that for
	 * OpenJDK thread-safety for BigInteger requires at least version
	 * 6u14.  See https://bugs.openjdk.java.net/browse/JDK-6348370
	 * 
	 * @param threadIndex The index of the thread for which to clone.
	 * @param cache The instance cache for the cloning.
	 * @return The thread-cloned object, or the same object if immutable.
	 */
	public T cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<T,R> cache )
	{
		throw( new RuntimeException( "Not Supported" ) );
	}
	
	/**
	 * Validates the structures and properties of the elem.  Throws
	 * a runtime exception if an invalid structure or property is found.
	 * 
	 * @throws RuntimeException
	 */
	public void validate() throws RuntimeException
	{
	}
	
	/**
	 * Implements a command pattern for executing optional
	 * operations that are not implemented by all elems.
	 * For instance, vectors have an outer product but real
	 * numbers do not.  Hence, only vectors would implement an
	 * optional command for an outer product.  A runtime exception
	 * should be thrown upon receipt of a command that is not supported.
	 * <P>
	 * <P> See http://en.wikipedia.org/wiki/command_pattern
	 * 
	 * @param id The identifier of the command to be executed.
	 * @param args The arguments of the command.
	 * @return The result of the command.
	 * @throws NotInvertibleException
	 */
	public T handleOptionalOp( Object id , ArrayList<T> args ) throws NotInvertibleException
	{	
		throw( new RuntimeException( "Operation Not Supported " + id ) );
	}
	
	
	/**
	 * Simplifies a symbolic elem.
	 * @return The simplified elem.
	 */
	public T distributeSimplify()
	{
		return( handleDistributeSimplify() );
	}
	
	
	/**
	 * Performs a simpler, faster simplification on a symbolic elem.
	 * @return The simplified elem.
	 */
	public T distributeSimplify2()
	{
		return( handleDistributeSimplify2() );
	}
	
	
	/**
	 * Simpler version of divideBy() for use with smaller numbers.  Divides the elem by a non-zero integer.
	 * 
	 * @param val The integer by which to divide.
	 * @return The elem divided by the integer.
	 */
	public final T divideBy( int val )
	{
		return( divideBy( BigInteger.valueOf( val ) ) );
	}
	
	
	/**
	 * Implements the exponential function <math display="inline">
     * <mrow>
     *  <msup>
     *          <mo>e</mo>
     *        <mi>x</mi>
     *  </msup>
     * </mrow>
     * </math>
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The exponent of the elem.
	 */
	public T exp( int numIter )
	{
		if( numIter == 0 )
		{
			final T x0 = getFac().identity();
			final T x1 = (T) this;
			final T x2 = x1.mult( x1 );
			final T x3 = x2.mult( x1 );
			final T ret = x0.add( x1 ).add( x2.divideBy( 2 ) ).add( x3.divideBy( 6 ) );
			return( ret );
		}
		else
		{
			final T x1 = (T) this;
			final T tmp = x1.divideBy( 2 ).exp( numIter - 1 );
			return( tmp.mult( tmp ) );
		}
	}
	
	
	/**
	 * Implements the sine function in units of radians.
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The sine of the argument.
	 */
	public T sin( int numIter )
	{
		ComplexElem<T,R> tmp = new ComplexElem<T,R>( getFac().zero() , (T) this );
		ComplexElem<T,R> btmp = tmp.exp( numIter );
		return( btmp.getIm() );
	}
	
	/**
	 * Implements the cosine function in units of radians.
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The cosine of the argument.
	 */
	public T cos( int numIter )
	{
		ComplexElem<T,R> tmp = new ComplexElem<T,R>( getFac().zero() , (T) this );
		ComplexElem<T,R> btmp = tmp.exp( numIter );
		return( btmp.getRe() );
	}
	
	/**
	 * Implements the hyperbolic sine function.
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The hyperbolic sine of the argument.
	 */
	public T sinh( int numIter )
	{
		final T x = (T) this;
		final ComplexElem<T,R> cplx = new ComplexElem<T,R>( getFac().zero() , x );
		final T ret = cplx.sin( numIter ).getIm();
		return( ret );
	}
	
	/**
	 * Implements the hyperbolic cosine function.
	 * 
	 * @param numIter The number of iterations to use in the  calculation.
	 * @return The hyperbolic cosine of the argument.
	 */
	public T cosh( int numIter )
	{
		final T x = (T) this;
		final ComplexElem<T,R> cplx = new ComplexElem<T,R>( getFac().zero() , x );
		final T ret = cplx.cos( numIter ).getRe();
		return( ret );
	}
	
	
	/**
	 * Implements an approximate arcsinh function.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The approximate arcsinh of the argument.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T asinh( final int numIterExp , final int numIterLn )  throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final T x = (T) this;
		final ComplexElem<T,R> cplx = new ComplexElem<T,R>( getFac().zero() , x );
		final T ret = cplx.asin( numIterExp , numIterLn ).getIm();
		return( ret );
	}
	
	
	
	/**
	 * Returns the better approximation for a natural logarithm.
	 * @param s0 One possibility to test.
	 * @param s1 Another possibility to test.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @return Either s0 or s1, depending on which is better.
	 */
	protected T evalBetterLnApprox( final T s0 , final T s1 , final int numIterExp )
	{
		T evA;
		T evB;
		try
		{
			evA = s0.exp( numIterExp ).add( this.negate() );
		}
		catch( Throwable ex )
		{
			return( s1 );
		}
		
		try
		{
			evB = s1.exp( numIterExp ).add( this.negate() );
		}
		catch( Throwable ex )
		{
			return( s0 );
		}
		
		final T ret = ( (Comparable) evA.totalMagnitude() ).compareTo( evB.totalMagnitude() ) <= 0 ? s0 : s1;
		return( ret );
	}
	
	
	/**
	 * Calculates an initial approximate natural logarithm that can lead to the convergence of the final logarithm calculation.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @return The initial approximate natural logarithm.
	 */
	protected T estimateLnApprox( final int numIterExp ) throws NotInvertibleException
	{
		T stval0 = (T) this;
		T stinit;
		
		do
		{
			stinit = stval0;
			stval0 = evalBetterLnApprox( stval0 , stval0.divideBy( 2 ) , numIterExp );
		}
		while( stval0 != stinit );
		
		
		final T ident = getFac().identity();
		final T mult2 = ident.add( ident );
		
		
		do
		{
			stinit = stval0;
			stval0 = evalBetterLnApprox( stval0 , stval0.mult( mult2 ) , numIterExp );
		}
		while( stval0 != stinit );
		
		
		final T i1 = ( ( (T) this ).mult( ( stval0.negate().exp( numIterExp ) ) ) ).add( getFac().identity().negate() );
		final T i2 = i1.mult( i1 );
		final T i3 = i2.mult( i1 );
		final T val = i1.add( i2.divideBy( 2 ).negate() ).add( i3.divideBy( 3 ) );
		final T ret = evalBetterLnApprox( val.add( stval0 ) , stval0 , numIterExp );
		return( ret );
	}
	
	

	/**
	 * Evaluator for computing an approximate natural logarithm.
	 * 
	 * @author tgreen
	 *
	 */
	protected class LnEvaluator
	{
		/**
		 * The argument for which to take the logarithm.
		 */
		protected T inputValue;
		
		/**
		 * Number of iterations to build the underlying exponential approximation.
		 */
		protected int numIterExp;
		
		/**
		 * Current best value for the solution.
		 */
		protected T evalValue;
		
		/**
		 * Cached solution used for backtracking.
		 */
		protected T evalCache;
		
		/**
		 * Number of iterations to attempt to converge to the logarithm.
		 */
		protected int numIterLn;
		
		
		/**
		 * The iteration count for Newton-Raphson iterations.
		 */
		protected int intCnt = 0;
		
		
		/**
		 * Constructs the evaluator.
		 * @param _inputValue The argument for which to take the logarithm.
		 * @param _numIterExp Number of iterations to build the underlying exponential approximation.
	     * @param _numIterLn Number of iterations to attempt to converge to the logarithm.
		 */
		public LnEvaluator( T _inputValue, int _numIterExp , int _numIterLn )
		{
			inputValue = _inputValue;
			numIterExp = _numIterExp;
			numIterLn = _numIterLn;
		}
		
		/**
		 * Populates the initial guess from which to start the evaluations.
		 */
		protected void populateEvalValue() throws NotInvertibleException
		{
			evalValue = inputValue.estimateLnApprox( numIterExp );
		}
		
		/**
		 * Evaluates and returns an approximate answer.
		 * @return The approximate answer.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public T performEval() throws NotInvertibleException, MultiplicativeDistributionRequiredException
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
		private class AEval extends NewtonRaphsonSingleElemFunctional<T,R>
		{

			/**
			 * Constructs the evaluator.
			 * @param _eval The function to be solved.
			 * @param _partialEval The approximate partial derivative for the function to be solved.
			 * @throws NotInvertibleException
			 * @throws MultiplicativeDistributionRequiredException
			 */
			public AEval(SymbolicElem<T,R> _eval, SymbolicElem<T,R> _partialEval )
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				super(_eval, _partialEval, null, null);
			}

			@Override
			protected void performIterationUpdate(T iterationOffset) {
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
			public NewtonRaphsonSingleElem<T, R> cloneThread(
					BigInteger threadIndex) {
				throw( new RuntimeException( "Not Supported" ) );
			}

			@Override
			public NewtonRaphsonSingleElem<T, R> cloneThreadCached(
					CloneThreadCache<SymbolicElem<SymbolicElem<T, R>, SymbolicElemFactory<T, R>>, SymbolicElemFactory<SymbolicElem<T, R>, SymbolicElemFactory<T, R>>> cache,
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
		private class AElemVal extends SymbolicElem<T, R>
		{

			/**
			 * Constructs the elem.
			 */
			public AElemVal( ) {
				super( Elem.this.getFac() );
			}

			@Override
			public T eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				return( ( evalValue.exp(numIterExp) ).add( inputValue.negate() ) );
			}
			
			@Override
			public T evalCached(
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<T, R>, T> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public T evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}
			
			@Override
			public T evalPartialDerivativeCached(
					ArrayList<? extends Elem<?, ?>> withRespectTo,
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<T, R>, T> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public String writeDesc( WriteElemCache<SymbolicElem<T,R>,SymbolicElemFactory<T,R>> cache , PrintStream ps )
			{
				String st = cache.get( this );
				if( st == null )
				{
					final String sta = fac.writeDesc( (WriteElemCache<T,R>)( cache.getInnerCache() ) , ps);
					/* cache.applyAuxCache( new WriteGaSetCache( cache.getCacheVal() ) );
					final String stair = ( (WriteGaSetCache)( cache.getAuxCache( WriteGaSetCache.class ) ) ).writeDesc(indx, (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) , ps);
					st = cache.getIncrementVal();
					cache.put(this, st);
					ps.print( AElemVal.class.getSimpleName() );
					ps.print( " " );
					ps.print( st );
					ps.print( " = new " );
					ps.print( AElemVal.class.getSimpleName() );
					ps.print( "( " );
					ps.print( sta );
					ps.print( " , " );
					ps.print( stair );
					ps.print( " , " );
					ps.print( col );
					ps.println( " );" ); */
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
		private class AElemPartial extends SymbolicElem<T, R>
		{

			/**
			 * Constructs the elem.
			 */
			public AElemPartial( ) {
				super( Elem.this.getFac() );
			}

			@Override
			public T eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				return( evalValue.exp(numIterExp) );
			}
			
			@Override
			public T evalCached(
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<T, R>, T> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public T evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}
			
			@Override
			public T evalPartialDerivativeCached(
					ArrayList<? extends Elem<?, ?>> withRespectTo,
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
					HashMap<SCacheKey<T, R>, T> cache)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				throw( new RuntimeException( "NotSupported" ) );
			}

			@Override
			public String writeDesc( WriteElemCache<SymbolicElem<T,R>,SymbolicElemFactory<T,R>> cache , PrintStream ps )
			{
				String st = cache.get( this );
				if( st == null )
				{
					final String sta = fac.writeDesc( (WriteElemCache<T,R>)( cache.getInnerCache() ) , ps);
					/* cache.applyAuxCache( new WriteGaSetCache( cache.getCacheVal() ) );
					final String stair = ( (WriteGaSetCache)( cache.getAuxCache( WriteGaSetCache.class ) ) ).writeDesc(indx, (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) , ps);
					st = cache.getIncrementVal();
					cache.put(this, st);
					ps.print( AElemVal.class.getSimpleName() );
					ps.print( " " );
					ps.print( st );
					ps.print( " = new " );
					ps.print( AElemVal.class.getSimpleName() );
					ps.print( "( " );
					ps.print( sta );
					ps.print( " , " );
					ps.print( stair );
					ps.print( " , " );
					ps.print( col );
					ps.println( " );" ); */
				}
				return( st );
			}
			

		}
		
		
		
		
	}
	
	
	
	/**
	 * Implements an approximate natural logarithm.
	 * @param numIterExp Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn Number of iterations to attempt to converge to the logarithm.
	 * @return An approximate natural logarithm.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T ln( int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		LnEvaluator eval = new LnEvaluator( (T) this , numIterExp , numIterLn );
		return( eval.performEval() );
	}
	
	
	/**
	 * Implements an approximate arctangeant.
	 * @param x The X-coordinate for which to calculate the arctangeant.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return An approximate arctangeant.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T atan2( final T x , int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final ComplexElem<T,R> cplx = new ComplexElem<T,R>( x , (T) this );
		final ComplexElem<T,R> ln = cplx.ln(numIterExp, numIterLn);
		return( ln.getIm() );	
	}
	
	
	/**
	 * Implements an approximate arctangeant.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return An approximate arctangeant.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T atan( int numIterExp , int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( atan2( getFac().identity(), numIterExp, numIterLn ) );
	}
	
	
	/**
	 * Calculates the components of the tangeant.
	 * @param numIter   Number of iterations to build the underlying exponential approximation.
	 * @return ArrayList containing the components of the tangeant.
	 */
	public ArrayList<T> tan2( int numIter )
	{
		ComplexElem<T,R> tmp = new ComplexElem<T,R>( getFac().zero() , (T) this );
		ComplexElem<T,R> btmp = tmp.exp( numIter );
		ArrayList<T> ret = new ArrayList<T>();
		ret.add( btmp.getIm() );
		ret.add( btmp.getRe() );
		return( ret );
	}
	
	
	/**
	 * Calculates an approximate power function using the formula <math display="inline">
     * <mrow>
     *  <msup>
     *          <mi>a</mi>
     *        <mi>b</mi>
     *  </msup>
     *  <mo>=</mo>
     *  <msup>
     *          <mi>e</mi>
     *      <mrow>
     *        <mi>b</mi>
     *        <mfenced open="(" close=")" separators=",">
     *          <mrow>
     *            <mi>ln</mi><mo>&ApplyFunction;</mo>
     *            <mi>a</mi>
     *          </mrow>
     *        </mfenced>
     *      </mrow>
     *  </msup>
     * </mrow>
     * </math>
	 * 
	 * @param b The exponent of the power function.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The calculated power function.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T powL( final T b , final int numIterExp , final int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( ( b.mult( this.ln(numIterExp, numIterLn) ) ).exp( numIterExp ) );
	}
	
	
	/**
	 * Calculates an approximate power function using the formula <math display="inline">
     * <mrow>
     *  <msup>
     *          <mi>a</mi>
     *        <mi>b</mi>
     *  </msup>
     *  <mo>=</mo>
     *  <msup>
     *          <mi>e</mi>
     *      <mrow>
     *        <mfenced open="(" close=")" separators=",">
     *          <mrow>
     *            <mi>ln</mi><mo>&ApplyFunction;</mo>
     *            <mi>a</mi>
     *          </mrow>
     *        </mfenced>
     *        <mi>b</mi>
     *      </mrow>
     *  </msup>
     * </mrow>
     * </math>
	 * 
	 * @param b The exponent of the power function.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The calculated power function.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T powR( final T b , final int numIterExp , final int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( ( ( this.ln(numIterExp, numIterLn) ).mult( b ) ).exp( numIterExp ) );
	}
	
	
	/**
	 * Calculates an approximate power function using the formula <math display="inline">
     * <mrow>
     *  <msup>
     *          <mi>a</mi>
     *        <mi>b</mi>
     *  </msup>
     *  <mo>=</mo>
     *  <msup>
     *          <mi>e</mi>
     *        <mfrac>
     *          <mrow>
     *            <mi>b</mi>
     *            <mfenced open="(" close=")" separators=",">
     *              <mrow>
     *                <mi>ln</mi><mo>&ApplyFunction;</mo>
     *                <mi>a</mi>
     *              </mrow>
     *            </mfenced>
     *            <mo>+</mo>
     *            <mfenced open="(" close=")" separators=",">
     *              <mrow>
     *                <mi>ln</mi><mo>&ApplyFunction;</mo>
     *                <mi>a</mi>
     *              </mrow>
     *            </mfenced>
     *            <mi>b</mi>
     *          </mrow>
     *          <mrow>
     *            <mn>2</mn>
     *          </mrow>
     *        </mfrac>
     *  </msup>
     * </mrow>
     * </math>
	 * 
	 * @param b The exponent of the power function.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The calculated power function.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T powC( final T b , final int numIterExp , final int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final T expn = ( ( b.mult( this.ln(numIterExp, numIterLn) ) ).add( ( this.ln(numIterExp, numIterLn) ).mult( b ) ) ).divideBy( 2 );
		return( expn.exp( numIterExp ) );
	}
	
	
	
	/**
	 * Calculates an approximate arcsin.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The approximate arcsin.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T asin( final int numIterExp , final int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final T identity = getFac().identity();
		final T thisT = (T) this;
		final T oppSide = ( identity.add( ( thisT.mult( thisT ) ).negate() ) ).powR( identity.divideBy( 2 ) , numIterExp , numIterLn );
		final ComplexElem<T,R> cplx = new ComplexElem<T,R>( oppSide , thisT );
		return( ( cplx.ln(numIterExp, numIterLn) ).getIm() );
	}
	
	
	
	/**
	 * Calculates an approximate arccosine.
	 * @param numIterExp  Number of iterations to build the underlying exponential approximation.
	 * @param numIterLn  Number of iterations to build the underlying logarithm approximation.
	 * @return The approximate arccosine.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public T acos( final int numIterExp , final int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final T identity = getFac().identity();
		final T thisT = (T) this;
		final T oppSide = ( identity.add( ( thisT.mult( thisT ) ).negate() ) ).powR( identity.divideBy( 2 ) , numIterExp , numIterLn );
		final ComplexElem<T,R> cplx = new ComplexElem<T,R>( thisT , oppSide );
		return( ( cplx.ln(numIterExp, numIterLn) ).getIm() );
	}
	
	
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public abstract String writeDesc( WriteElemCache<T,R> cache , PrintStream ps );
	
	
	
	/**
	 * Writes MathML ( http://www.w3.org/Math/ ) presentation tags describing the elem to a print stream.
	 * @param pc A description of how to assign precedence for converting to infix notation.
	 * @param ps The print stream to which to write the tags.
	 */
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		pc.handleUnimplementedElem( this , ps );
	}

	/**
	 * Writes MathML ( http://www.w3.org/Math/ ) presentation tags describing the elem, wrapped in the top-level math tag, to a print stream.
	 * @param pc A description of how to assign precedence for converting to infix notation.
	 * @param ps The print stream to which to write the tags.
	 */
	public void writeMathMLWrapped( PrecedenceComparator pc , PrintStream ps )
	{
		ps.print( "<math display=\"inline\">" );
		writeMathML( pc , ps );
		ps.print( "</math>" );
	}
	
	/**
	 * Writes a self-contained HTML file containing MathML ( http://www.w3.org/Math/ ) presentation tags describing the elem.
	 * @param pc A description of how to assign precedence for converting to infix notation.
	 * @param ps The print stream to which to write the tags.
	 */
	public void writeHtmlFile( PrecedenceComparator pc , PrintStream ps )
	{
		ps.println( "<html>" );
		ps.println( "<head>" );
		ps.println( "<title>Title</title>" );
		ps.println( "</head>" );
		ps.println( "<body>" );
		ps.print( "<P>" );
		writeMathMLWrapped( pc , ps );
		ps.println( "" );
		ps.println( "</body>" );
		ps.println( "</html>" );
	}
	
	
	
	/**
	 * Returns approximately whether the elem can be determined to reduce to zero.
	 * 
	 * @param mode Mode determining the extent to which the elem will be reduced to determine if it is zero.
	 * @return  True if the elem can be determined to reduce to zero.
	 * @throws NotInvertibleException 
	 */
	public abstract boolean evalSymbolicZeroApprox( EVAL_MODE mode );
	
	
	
	/**
	 * Returns approximately whether the elem can be determined to reduce to the identity.
	 * 
	 * @param mode Mode determining the extent to which the elem will be reduced to determine if it is the identity.
	 * @return  True if the elem can be determined to reduce to the identity.
	 * @throws NotInvertibleException 
	 */
	public abstract boolean evalSymbolicIdentityApprox( EVAL_MODE mode );
	
	
	/**
	 * Inserts standard configuration items into a Drools session.
	 * 
	 * @param session The session into which to insert the configuration items.
	 */
	protected void insertSessionConfigItems( final StatefulKnowledgeSession session )
	{
		session.insert( new DroolsSession( session ) );
		
		if( LoggingConfiguration.LOGGING_ON )
		{
			session.insert( new LoggingConfiguration() );
		}
		
		if( LoggingConfiguration.EVENT_LOGGING_ON )
		{
			session.addEventListener( generateEventLoggingListener() );
		}
	}
	
	
	/**
	 * Performs a distribute simplify on the elem.
	 * 
	 * @return The result of the simplification.
	 */
	protected T handleDistributeSimplify()
	{
		T prev = (T)( this );
		StatefulKnowledgeSession session = null;
		SymbolicPlaceholder<T,R> place = null;
		while( true )
		{
			try
			{
				session = getDistributeSimplifyKnowledgeBase().newStatefulKnowledgeSession();
		
				insertSessionConfigItems( session );
			
				place = new SymbolicPlaceholder<T,R>( prev );
			
				place.performInserts( session );
					
				session.fireAllRules();
		
				T ret = place.getElem();
		
				session.dispose();
			
				return( ret );
			}
			catch( OutOfMemoryError ex )
			{
				T ret = place != null ? place.getElem() : null;
				
				/*
				 * Always try to dispose the session after running out of memory.
				 */
				try
				{
					if( session != null )
					{
						session.dispose();
					}
				}
				catch( Throwable ex2 )
				{
					// ex2.printStackTrace( System.out );
				}
				
				/*
				 * If no simplifications were completed, exit with exception.
				 */
				if( ( ret == null ) || ( ret == prev ) )
				{
					throw( ex );
				}
				
				/*
				 * If some simplifications completed before the memory limits ran out,
				 * re-run the session and see if it's possible to get farther on the next run.
				 */
				prev = ret;
				session = null;
				place = null;
			}
		}
	}
	
	
	/**
	 * Performs a simpler distribute simplify on the elem.
	 * 
	 * @return The result of the simplification.
	 */
	protected T handleDistributeSimplify2()
	{
		T prev = (T)( this );
		StatefulKnowledgeSession session = null;
		SymbolicPlaceholder<T,R> place = null;
		while( true )
		{
			try
			{
				session = getDistributeSimplify2KnowledgeBase().newStatefulKnowledgeSession();
		
				insertSessionConfigItems( session );
			
				place = new SymbolicPlaceholder<T,R>( prev );
			
				place.performInserts( session );
					
				session.fireAllRules();
		
				T ret = place.getElem();
		
				session.dispose();
			
				return( ret );
			}
			catch( OutOfMemoryError ex )
			{
				T ret = place != null ? place.getElem() : null;
				
				/*
				 * Always try to dispose the session after running out of memory.
				 */
				try
				{
					if( session != null )
					{
						session.dispose();
					}
				}
				catch( Throwable ex2 )
				{
					// ex2.printStackTrace( System.out );
				}
				
				/*
				 * If no simplifications were completed, exit with exception.
				 */
				if( ( ret == null ) || ( ret == prev ) )
				{
					throw( ex );
				}
				
				/*
				 * If some simplifications completed before the memory limits ran out,
				 * re-run the session and see if it's possible to get farther on the next run.
				 */
				prev = ret;
				session = null;
				place = null;
			}
		}
	}
	
	
	
	
	/**
	 * Inserts this elem into a Drools ( http://drools.org ) session.
	 * 
	 * @param session The session in which to insert the elem.
	 */
	public void performInserts( StatefulKnowledgeSession session )
	{
		session.insert( this );
	}
	
	
	/**
	 * Inserts the elem into a Drools ( http://drools.org ) session.
	 * @param ds The session.
	 * @return This elem.
	 */
	public Elem<T,R> insElem( DroolsSession ds )
	{
		ds.insert( this );
		return( this );
	}
	
	
	/**
	 * Generates a listener for logging match events.
	 * 
	 * @return The listener for logging match events.
	 */
	protected DefaultAgendaEventListener generateEventLoggingListener()
	{
		return( new DefaultAgendaEventListener()
		{
			@Override
			public void beforeMatchFired( final BeforeMatchFiredEvent event )
			{
				final Rule rule = event.getMatch().getRule();
				System.out.println( rule.getName() );
			}
		} );
	}
	
	
	
	
	/**
	 * Returns Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 * 
	 * @return Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	public static KnowledgeBase getDistributeSimplifyKnowledgeBase()
	{
		if( distributeSimplifyKnowledgeBase == null )
		{
			OptimizerFactory.setDefaultOptimizer( OptimizerFactory.SAFE_REFLECTIVE );
			
			KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			// Load in the reng support as a separate cross-cutting concern.
			builder.add( ResourceFactory.newClassPathResource( "reng.drl" )  , 
					ResourceType.DRL );
			
			builder.add( ResourceFactory.newClassPathResource( "distributeSimplify.drl" )  , 
					ResourceType.DRL );
			
			if( LoggingConfiguration.LOGGING_ON )
			{
				builder.add( ResourceFactory.newClassPathResource( "logging.drl" )  , 
						ResourceType.DRL );
			}
			
			if( builder.hasErrors() )
			{
				throw( new RuntimeException( builder.getErrors().toString() ) );
			}
			distributeSimplifyKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
			distributeSimplifyKnowledgeBase.addKnowledgePackages( builder.getKnowledgePackages() );
		}
		
		return( distributeSimplifyKnowledgeBase );
	}
	
	
	
	/**
	 * Returns Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 * 
	 * @return Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	public static KnowledgeBase getDistributeSimplify2KnowledgeBase()
	{
		if( distributeSimplify2KnowledgeBase == null )
		{
			OptimizerFactory.setDefaultOptimizer( OptimizerFactory.SAFE_REFLECTIVE );
			
			KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			// Load in the reng support as a separate cross-cutting concern.
			builder.add( ResourceFactory.newClassPathResource( "reng.drl" )  , 
					ResourceType.DRL );
			
			builder.add( ResourceFactory.newClassPathResource( "distributeSimplify2.drl" )  , 
					ResourceType.DRL );
			
			if( LoggingConfiguration.LOGGING_ON )
			{
				builder.add( ResourceFactory.newClassPathResource( "logging.drl" )  , 
						ResourceType.DRL );
			}
			
			if( builder.hasErrors() )
			{
				throw( new RuntimeException( builder.getErrors().toString() ) );
			}
			distributeSimplify2KnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
			distributeSimplify2KnowledgeBase.addKnowledgePackages( builder.getKnowledgePackages() );
		}
		
		return( distributeSimplify2KnowledgeBase );
	}

	
	/**
	 * Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	private static KnowledgeBase distributeSimplifyKnowledgeBase = null;
	
	/**
	 * Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	private static KnowledgeBase distributeSimplify2KnowledgeBase = null;
	
	
}

