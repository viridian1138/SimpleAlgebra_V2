





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





package test_simplealgebra.run2d_a;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HyperGraph;

import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.store.DbFastArray2D_Dbl;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import test_simplealgebra.DatabasePathForTest;
import test_simplealgebra.IterConstants;
import test_simplealgebra.TestDimensionOne;






/**
 * Experimental version of TestStelemB_DB that uses TensorFlow-trained neural networks.
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestStelemB_DB_Exp_NN extends TestCase {
	
	
	/**
	 * The arbitrary constant.
	 */
	protected static final DoubleElem C = new DoubleElem( 0.05 );
	
	
	
	
	/**
	 * The total measurement size along the X-Axis.
	 */
	protected static final double TOTAL_X_AXIS_SIZE = 0.1;

	
	/**
	 * The number of discretizations on the X-Axis over which to iterate.
	 */
	protected static final int NUM_X_ITER = 25;
	
	
	/**
	 * The number of discretizations on the T-Axis over which to iterate.
	 */
	protected static final int NUM_T_ITER = 400;
	
	
	
	/**
	 * Size of the T-Axis discretization.
	 */
	protected static final DoubleElem T_HH = new DoubleElem( 0.0025 );
	
	/**
	 * Size of the X-Axis discretization.
	 */
	protected static final DoubleElem X_HH = new DoubleElem( TOTAL_X_AXIS_SIZE / NUM_X_ITER /* 0.01 */ );
	
	/**
	 * Discretization sizes arrayed by coordinate index.
	 */
	protected static final DoubleElem[] HH = { T_HH , X_HH };
	
	
	
	/**
	 * The halfway iteration point in X.
	 */
	protected static final int HALF_X = NUM_X_ITER / 2;
	
	
	/**
	 * The initial condition radius in X.
	 */
	protected static final double RAD_X = NUM_X_ITER / 10.0;
	
	
	
	
	/**
	 * Temp step size in the T-direction.
	 */
	protected static final int NSTPT = 2;
	
	
	/**
	 * Temp step size in the X-direction.
	 */
	protected static final int NSTPX = 2;
	
	
	/**
	 * Indicates whether predictor-corrector should be used while iterating.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	// protected static final boolean USE_PREDICTOR_CORRECTOR = true;
	
	
	
	/**
	 * Indicates whether a form of nonlinear numerical viscosity should be used while iterating.
	 */
	// protected static final boolean APPLY_NUMERICAL_VISCOSITY = true;
	
	
	
	/**
	 * Result array over which to iterate.
	 */
	protected static DbFastArray2D_Dbl iterArray = null;
	
	
	
	/**
	 * Temporary array in which to store the current Newton-Raphson load state.
	 * <p>0 = T
	 * <p>1 = X
	 */
	private static double[][] tempArrayLoad = new double[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ];
	
	
	
	/**
	 * Temporary array in which to generate Newton-Raphson solutions.
	 * <p>0 = T
	 * <p>1 = X
	 */
	private static double[][] tempArrayGen = new double[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ];
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( DoubleElem dbl )
	{
		tempArrayGen[ NSTPT * 2 ][ NSTPX ] += dbl.getVal();
	}
	
	
	/**
	 * The iteration cache value.
	 */
	protected static double iterationValueCache = 0.0;
	
	
	/**
	 * Places the current iteration value in the cache.
	 */
	protected static void cacheIterationValue()
	{
		iterationValueCache = tempArrayGen[ NSTPT * 2 ][ NSTPX ];
	}
	
	
	/**
	 * Sets the current iteration value to the value in the cache.
	 */
	protected static void retrieveIterationValue()
	{
		tempArrayGen[ NSTPT * 2 ][ NSTPX ] = iterationValueCache;
	}
	
	
	/**
	 * Returns the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static double getUpdateValue()
	{
		return( tempArrayGen[ NSTPT * 2 ][ NSTPX ] );
	}
	
	
	

	/**
	 * Approximate maximum change allowed by nonlinear viscosity.
	 */
	final static double MAX_CHG = 0.05;
	
	/**
	 * Multiplicative inverse of MAX_CHG.
	 */
	final static double I_MAX_CHG = 1.0 / MAX_CHG;
	
	/**
	 * Size of change below which numerical viscosity isn't applied.
	 */
	final static double NUMERICAL_VISCOSITY_EXIT_CUTOFF = 1E-5;
	
	
	
	
	/**
	 * Applies a form of nonlinear numerical viscosity.
	 */
	protected static void applyNumericViscosity()
	{
		final double delt = tempArrayGen[ NSTPT * 2 ][ NSTPX ]
			- tempArrayGen[ NSTPT * 2 - 1 ][ NSTPX ];
		final double adelt = Math.abs( delt );
		if( adelt < NUMERICAL_VISCOSITY_EXIT_CUTOFF )
		{
			return;
		}
		final double iadelt = 1.0 / adelt;
		final double iadiv = Math.sqrt( iadelt * iadelt + I_MAX_CHG * I_MAX_CHG );
		final double adiv = 1.0 / iadiv;
		tempArrayGen[ NSTPT * 2 ][ NSTPX ] =
				tempArrayGen[ NSTPT * 2 - 1 ][ NSTPX ] +
				( delt > 0.0 ? adiv : -adiv );
	}
	
	
		
	
	
	
	
	
	
	/**
	 * Input parameter for fillTempArrayInner()
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TempArrayFillInnerParam
	{
		
		
		
		
		
		
		/**
		 * Gets the T-Axis index for the center in the iter array.
		 * 
		 * @return The T-Axis index for the center in the iter array.
		 */
		public int getTcnt() {
			return tcnt;
		}


		/**
		 * Sets the T-Axis index for the center in the iter array.
		 * 
		 * @param tcnt The T-Axis index for the center in the iter array.
		 */
		public void setTcnt(int tcnt) {
			this.tcnt = tcnt;
		}


		/**
		 * Gets the X-Axis index for the center in the iter array.
		 * 
		 * @return The X-Axis index for the center in the iter array.
		 */
		public int getXcnt() {
			return xcnt;
		}


		/**
		 * Sets the X-Axis index for the center in the iter array.
		 * 
		 * @param xcnt The X-Axis index for the center in the iter array.
		 */
		public void setXcnt(int xcnt) {
			this.xcnt = xcnt;
		}



		/**
		 * Gets the T-Axis iteration of the array fill.
		 * 
		 * @return The T-Axis iteration of the array fill.
		 */
		public int getTa() {
			return ta;
		}


		/**
		 * Sets the T-Axis iteration of the array fill.
		 * 
		 * @param ta The T-Axis iteration of the array fill.
		 */
		public void setTa(int ta) {
			this.ta = ta;
		}


		/**
		 * Gets the X-Axis iteration of the array fill.
		 * 
		 * @return The X-Axis iteration of the array fill.
		 */
		public int getXa() {
			return xa;
		}


		/**
		 * Sets the X-Axis iteration of the array fill.
		 * 
		 * @param xa The X-Axis iteration of the array fill.
		 */
		public void setXa(int xa) {
			this.xa = xa;
		}



		/**
		 * The T-Axis index for the center in the iter array.
		 */
		protected int tcnt;
		
		/**
		 * The X-Axis index for the center in the iter array.
		 */
		protected int xcnt;
		
		/**
		 * The T-Axis iteration of the array fill.
		 */
		protected int ta;
		
		/**
		 * The X-Axis iteration of the array fill.
		 */
		protected int xa;
		
	
	}
	
	
	
	/**
	 * Fills one element of the temp array with an element from the iter array.
	 * 
	 * @param param Input parameter describing where to get the element.
	 */
	protected static void fillTempArrayInner( TempArrayFillInnerParam param )
	{
		final int tcnt = param.getTcnt();
		final int xcnt = param.getXcnt();
		
		final int ta = param.getTa();
		final int xa = param.getXa();
		
		final int tv = tcnt + ta;
		final int xv = xcnt + xa;
		double av = 0.0;
		if( ( tv >= 0 )  && ( xv >= 0 ) && 
				( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) )
		{
			av = iterArray.get( tv , xv );
		}
		tempArrayLoad[ ta + NSTPT ][ xa + NSTPX ] = av;
		
	}
	
	

	
	/**
	 * Node representing a TensorFlow-generated neural network.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class NeuralNode
	{
		protected double[][] wvals_h1;
		protected double[][] wvals_h2;
		protected double[][] wvals_h3;
		protected double[][] wvals_h4;
		protected double[][] wvals_out;
	
	
		protected double[] bvals_h1;
		protected double[] bvals_h2;
		protected double[] bvals_h3;
		protected double[] bvals_h4;
		protected double[] bvals_out;
		
		
		protected double[] tvals_h1;
		protected double[] tvals_h2;
		protected double[] tvals_h3;
		protected double[] tvals_h4;
		
		
		/**
		 * Evaluates the neural network.
		 * @param in The neural network input values.
		 * @return The result of evaluationg the neural network.
		 */
		public double eval( final double[] in )
		{
			
			for( int i = 0 ; i < tvals_h1.length ; i++ )
			{
				tvals_h1[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < in.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h1.length ; j++ )
				{
					tvals_h1[ j ] += wvals_h1[ i ][ j ] * in[ i ];
				}
			}
			
			for( int i = 0 ; i < tvals_h1.length ; i++ )
			{
				tvals_h1[ i ] = Math.max( 0.0 , tvals_h1[ i ] + bvals_h1[ i ] );
			}
			
			
			for( int i = 0 ; i < tvals_h2.length ; i++ )
			{
				tvals_h2[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < tvals_h1.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h2.length ; j++ )
				{
					tvals_h2[ j ] += wvals_h2[ i ][ j ] * tvals_h1[ i ];
				}
			}
			
			for( int i = 0 ; i < tvals_h2.length ; i++ )
			{
				tvals_h2[ i ] = Math.max( 0.0 , tvals_h2[ i ] + bvals_h2[ i ] );
			}
			
			
			for( int i = 0 ; i < tvals_h3.length ; i++ )
			{
				tvals_h3[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < tvals_h2.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h3.length ; j++ )
				{
					tvals_h3[ j ] += wvals_h3[ i ][ j ] * tvals_h2[ i ];
				}
			}
			
			
			for( int i = 0 ; i < tvals_h3.length ; i++ )
			{
				tvals_h3[ i ] = Math.max( 0.0 , tvals_h3[ i ] + bvals_h3[ i ] );
			}
			
			
			for( int i = 0 ; i < tvals_h4.length ; i++ )
			{
				tvals_h4[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < tvals_h3.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h4.length ; j++ )
				{
					tvals_h4[ j ] += wvals_h4[ i ][ j ] * tvals_h3[ i ];
				}
			}
			
			
			for( int i = 0 ; i < tvals_h4.length ; i++ )
			{
				tvals_h4[ i ] = Math.max( 0.0 , tvals_h4[ i ] + bvals_h4[ i ] );
			}
			
			
			double out = 0.0;
			
			
			
			for( int i = 0 ; i < tvals_h4.length ; i++ )
			{
				out += wvals_out[ i ][ 0 ] * tvals_h4[ i ];
			}
			
			
			
			out = Math.max( 0.0 , out + bvals_out[ 0 ] );
			
			
			
			return( out );
			
			
			
		}
		
		
	}
	
	
	
	
	protected static NeuralNode loadNeuralNode( String wfile , String bfile ) throws Throwable
	{
		NeuralNode node = new NeuralNode();
		
		{
			LineNumberReader li = new LineNumberReader( new InputStreamReader( TestStelemB_DB_Exp_NN.class.getResourceAsStream( wfile ) ) );
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h1 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h1[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h2 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h2[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h3 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h3[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h4 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h4[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_out = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_out[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			

			
		}
		
		
		
		
		
		
		{
			LineNumberReader li = new LineNumberReader( new InputStreamReader( TestStelemB_DB_Exp_NN.class.getResourceAsStream( bfile ) ) );
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h1 = new double[ maxI ];
				node.tvals_h1 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h1[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h2 = new double[ maxI ];
				node.tvals_h2 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h2[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h3 = new double[ maxI ];
				node.tvals_h3 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h3[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h4 = new double[ maxI ];
				node.tvals_h4 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h4[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_out = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_out[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			

			
		}
		
		
		
		return( node );
		
	}
	
	
	
	
	
	
	protected static final NeuralNode[][] nodes = new NeuralNode[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ];
	
	protected static final double[] nnvals = new double[ ( ( NSTPT * 2 + 1 ) - 1 ) * ( NSTPX * 2 + 1 ) ];

	protected static double nnAbsMax = 0.0;
	
	
	
	
	
	
	protected static void evalNnNode( final int t , final int x  )
	{
		
		
		// System.out.print( out[ t ][ x ] + " // " );
		
		
		if( nodes[ t ][ x ] != null )
		{
			
			final NeuralNode node = nodes[ t ][ x ];
			
			tempArrayGen[ t ][ x ] = node.eval( nnvals ) * nnAbsMax;
			
		}
		else
		{
			tempArrayGen[ t ][ x ] = tempArrayLoad[ t ][ x ];
		}
		
		
		// System.out.println( out[ t ][ x ] );
		
		
	}
	
	
	
	
	
	
	
	// Fix derivatives !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	

	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 */
	protected static void fillTempArray( final int tcnt , final int xcnt )
	{
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				fillTempArrayInner( param );
			}
		}
		
		
		nnAbsMax = 0.0;
		
		for( int t = 0 ; t < ( ( NSTPT * 2 + 1 ) - 1 ) ; t++ )
		{
			for( int x = 0 ; x < ( 2 * NSTPX + 1 ) ; x++ )
			{
				nnvals[ ( 2 * NSTPX + 1 ) * t + x ] = tempArrayLoad[ t ][ x ];
				nnAbsMax = Math.max( nnAbsMax ,  Math.abs( nnvals[ ( 2 * NSTPX + 1 ) * t + x ] ) );
			}
		}
		
		
		
		if( nnAbsMax < 1E-30 )
		{
			nnAbsMax = 1E-30;
		}
		
		
		// Overlay initial seed for iterations.
		for( int xa = 0 ; xa < NSTPX * 2 + 1 ; xa++ )
		{
			for( int ta = 0 ; ta < NSTPT * 2 + 1 ; ta++ )
			{
				evalNnNode( ta , xa );
			}
		}
		
		
		for( int x = 0 ; x < ( 2 * NSTPX + 1 ) ; x++ )
		{
			tempArrayGen[ ( 2 * NSTPT + 1 ) - 2 ][ x ] = tempArrayGen[ ( 2 * NSTPT + 1 ) - 1 ][ x ];
		}
		
		
	}
	
	
	
	/**
	 * Test array used to verify that the entire temp array has been filled.
	 */
	private static int[][] spatialAssertArray = new int[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ];
	
	
	
	/**
	 * Clears the test array used to verify that the entire temp array has been filled.
	 */
	protected static void clearSpatialAssertArray( )
	{
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
		{
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				spatialAssertArray[ ta + NSTPT ][ xa + NSTPX ] = 0;
			}
		}
	}
	
	
	
	/**
	 * Cache for symbolic constants.
	 * 
	 * @author tgreen
	 *
	 */
	private static class SymbolicConstCache
	{
		
		/**
		 * Map representing the cache.
		 */
		protected static HashMap<Double,SymbolicConst> map = new HashMap<Double,SymbolicConst>();
		
		/**
		 * Returns a cached SymbolicConst representing a DoubleElem.
		 * 
		 * @param in The DoubleElem to be represented.
		 * @param _fac The factory for DoubleElem instances.
		 * @return The cached SymbolicConst.
		 */
		public static SymbolicConst get(  DoubleElem in , DoubleElemFactory _fac )
		{
			SymbolicConst cnst = map.get( in.getVal() );
			if( cnst == null )
			{
				cnst = new SymbolicConst( in , _fac );
				map.put( in.getVal() , cnst );
			}
			return( cnst );
		}
		
		/**
		 * Clears the cache.
		 */
		public static void clearCache()
		{
			map.clear();
		}
		
	}
	
	
	
	/**
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class DDirec extends DirectionalDerivativePartialFactory<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		Ordinate>
	{
		/**
		 * Factory for building the value of the derivative of an ordinate.
		 */
		DoubleElemFactory de;
		
		/**
		 * Factory for the enclosed type.
		 */
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2;
		
		/**
		 * Constructs the directional derivative factory.
		 * 
		 * @param _de Factory for building the value of the derivative of an ordinate.
		 * @param _se2 Factory for the enclosed type.
		 */
		public DDirec( 
				final DoubleElemFactory _de ,
				final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _se2 )
		{
			de = _de;
			se2 = _se2;
		}

		@Override
		public PartialDerivativeOp<
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			Ordinate> getPartial( BigInteger basisIndex )
		{
			final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
			
			wrtX.add( new Ordinate( de , 1 + basisIndex.intValue() ) );
			
			return( new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtX ) );
		}

	};
	
	
	
	/**
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class Ordinate extends SymbolicElem<DoubleElem, DoubleElemFactory>
	{
		/**
		 * The number of the ordinate.
		 */
		private int col;

		/**
		 * Constructs the node.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The number of the ordinate.
		 */
		public Ordinate(DoubleElemFactory _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem,DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( Ordinate.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( Ordinate.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof Ordinate )
			{
				return( col == ( (Ordinate) b ).col );
			}
			return( false );
		}
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof Ordinate )
			{
				return( col == ( (Ordinate) b ).col );
			}
			return( false );
		}
		
		@Override
		public int hashCode()
		{
			return( col );
		}
		
		/**
		 * Returns the number of the ordinate.
		 * 
		 * @return The number of the ordinate.
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	/**
	 * A symbolic elem representing a constant value
	 * at the base Newton-Raphson evaluation level.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class SymbolicConst extends SymbolicReduction<DoubleElem, DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof SymbolicConst )
			{
				return( getElem().getVal() == ( (SymbolicConst) b ).getElem().getVal() );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * An elem representing a symbolic constant at the level of mapping 
	 * discretized approximations of the underlying differential
	 * equation into expressions (e.g. Jacobian slopes)
	 * for Newton-Raphson evaluations.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction2L(SymbolicElem<DoubleElem, DoubleElemFactory> _elem, SymbolicElemFactory<DoubleElem, DoubleElemFactory> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> b )
		{
			if( b instanceof StelemReduction2L )
			{
				return( getElem().symbolicEquals( ( (StelemReduction2L) b ).getElem() ) );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * An elem representing a symbolic constant at the level
	 * of mapping the underlying differential equation into
	 * its discretized approximation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class StelemReduction3L extends SymbolicReduction<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _elem The constant to be represented.
		 * @param _fac The factory for the constant.
		 */
		public StelemReduction3L(
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _elem, 
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> b )
		{
			if( b instanceof StelemReduction3L )
			{
				return( getElem().symbolicEquals( ( (StelemReduction3L) b ).getElem() ) );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * A constant coefficient with a numerator and a denominator.
	 * 	
	 * @author thorngreen
	 *
	 */
	private static class CoeffNode
	{
		/**
		 * The numerator.
		 */
		private SymbolicElem<DoubleElem,DoubleElemFactory> numer;
		
		/**
		 * The denominator.
		 */
		private SymbolicElem<DoubleElem,DoubleElemFactory> denom;
		
		/**
		 * Constructs the coefficient.
		 * 
		 * @param _numer The numerator.
		 * @param _denom The denominator.
		 */
		public CoeffNode( SymbolicElem<DoubleElem,DoubleElemFactory> _numer , SymbolicElem<DoubleElem,DoubleElemFactory> _denom )
		{
			numer = _numer;
			denom = _denom;
		}
		
		/**
		 * Gets the numerator.
		 * 
		 * @return The numerator.
		 */
		public SymbolicElem<DoubleElem,DoubleElemFactory> getNumer() {
			return numer;
		}
		
		/**
		 * Gets the denominator.
		 * 
		 * @return The denominator.
		 */
		public SymbolicElem<DoubleElem,DoubleElemFactory> getDenom() {
			return denom;
		}
		
	}
	
	
	/**
	 * Elem representing the discretized equivalent 
	 * of the value constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class BNelem extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 */
		public BNelem(DoubleElemFactory _fac, HashMap<Ordinate, BigInteger> _coord) {
			super(_fac, _coord);
		}
		
		
		/**
		 * Column indices in the discretized space.
		 */
		protected final int[] cols = new int[ 2 ];
		
		/**
		 * Assertion booleans used to verify that all
		 * column indices have been initialized.
		 */
		protected final boolean[] assertCols = new boolean[ 2 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			Assert.assertTrue( coord.keySet().size() == 2 );
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate keyCoord = ii.getKey();
				BigInteger coordVal = ii.getValue();
				final int offset = keyCoord.getCol() == 1 ? NSTPX : ( NSTPT + 1 );
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			return( new DoubleElem( TestStelemB_DB_Exp_NN.tempArrayGen[ cols[ 0 ] ][ cols[ 1 ] ] ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String strr = cache.getIncrementVal();
				ps.print( "final HashMap<Ordinate, BigInteger> " );
				ps.print( strr );
				ps.println( " = new HashMap<Ordinate, BigInteger>();" );
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					final String stai = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( ii.getValue() , ps );
					final String sl = ii.getKey().writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
					ps.print( strr );
					ps.print( ".put( " );
					ps.print( sl );
					ps.print( " , " );
					ps.print( stai );
					ps.println( " );" );
				}
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BNelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BNelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( strr );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
		
		@Override 
		public int hashCode()
		{
			int sum = 0;
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate key = ii.getKey();
				BigInteger ka = ii.getValue();
				sum += key.hashCode();
				sum += ka.hashCode();
			}
			return( sum );
		}
		
		
	}
	
	
	
	/**
	 * Elem representing the symbolic expression for 
	 * the discretized equivalent
	 * of the value constrained by the differential equation.
	 * The partial derivatives of this elem generate
	 * the slopes for producing Newton-Raphson iterations (e.g. the Jacobian slopes),
	 * as opposed to partial derivatives for the underlying differential equation.
	 * 
	 * @author thorngreen
	 *
	 */	
	private static class CNelem extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 */
		public CNelem(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, HashMap<Ordinate, BigInteger> _coord) {
			super(_fac, _coord);
		}

		@Override
		public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( new BNelem( fac.getFac() , coord ) );
		}
		
		
		@Override
		public SymbolicElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> key =
					new SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( this , implicitSpace );
			final SymbolicElem<DoubleElem, DoubleElemFactory> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<DoubleElem, DoubleElemFactory> ret = eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		
		
		@Override
		public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			if( withRespectTo.size() > 1 )
			{
				return( fac.zero() );
			}
			Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
			CNelem wrt = (CNelem)( it.next() );
			final boolean cond = this.symbolicEquals( wrt );
			return( cond ? fac.identity() : fac.zero() );
		}
		
		
		@Override
		public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			final SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> key =
					new SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( this , implicitSpace , withRespectTo );
			final SymbolicElem<DoubleElem, DoubleElemFactory> iret = cache.get( key );
			if( iret != null ) 
			{
				return( iret );
			}
			final SymbolicElem<DoubleElem, DoubleElemFactory> ret = evalPartialDerivative( withRespectTo , implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String strr = cache.getIncrementVal();
				ps.print( "final HashMap<Ordinate, BigInteger> " );
				ps.print( strr );
				ps.println( " = new HashMap<Ordinate, BigInteger>();" );
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					final String stai = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( ii.getValue() , ps );
					final String sl = ii.getKey().writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
					ps.print( strr );
					ps.print( ".put( " );
					ps.print( sl );
					ps.print( " , " );
					ps.print( stai );
					ps.println( " );" );
				}
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( CNelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( CNelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( strr );
				ps.println( " );" );
			}
			return( st );
		}
		
		
		@Override
		public boolean symbolicEquals( 
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof CNelem )
			{
				CNelem bn = (CNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
	}
	
	
	
	/**
	 * An elem defining a partial derivative that is evaluated over the discretized space of the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private class AStelem extends Stelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
	{	
		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public AStelem(SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
			super(_fac);
		}

		@Override
		public AStelem cloneInstance() {
			AStelem cl = new AStelem( fac );
			for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
			{
				Ordinate key = ii.getKey();
				cl.partialMap.put(key, ii.getValue() );
			}
			return( cl );
		}

		@Override
		public SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem,DoubleElemFactory>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			
			HashMap<Ordinate,Ordinate> imp = (HashMap<Ordinate,Ordinate>) implicitSpace;
			
			
			HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
			
			
			{
				CoeffNode cf = new CoeffNode( fac.getFac().identity() , fac.getFac().identity() );
				HashMap<Ordinate, BigInteger> key = new HashMap<Ordinate, BigInteger>();
				for( final Ordinate ae : imp.keySet() )
				{
					BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
					key.put( ae , valA );
				}
				spacesA.put( key , cf );
			}
			
			
			{
				for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
				{
					HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
					final Ordinate ae = ii.getKey();
					final BigInteger numDerivs = ii.getValue();
					applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
					spacesA = spacesB;
				}
			}
			
			
			
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
			
			
			{
				for( final Entry<HashMap<Ordinate, BigInteger>, CoeffNode> ii : spacesA.entrySet() )
				{
					HashMap<Ordinate, BigInteger> spaceAe = ii.getKey();
					CoeffNode coeff = ii.getValue();
					final CNelem an0 = 
							new CNelem( fac.getFac() , spaceAe );
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
						an1 = an0.mult( 
								new StelemReduction2L( coeff.getNumer() , fac.getFac() ) );
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> 
						an2 = an1.mult( 
								( new StelemReduction2L( coeff.getDenom() , fac.getFac() ) ).invertLeft() );
					ret = ret.add( an2 );
				}
			}
			
			
			return( ret );
		}
		
		
		@Override
		public SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			final SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> key =
					new SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>( this , implicitSpace );
			final SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> iret = cache.get( key );
			if( iret != null )
			{
				return( iret );
			}
			final SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> ret =
					eval( implicitSpace );
			cache.put(key, ret);
			return( ret );
		}
		

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
				final String sta = fac.writeDesc( (WriteElemCache< SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> , SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> >)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AStelem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AStelem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
				for( Entry<Ordinate,BigInteger> ii : partialMap.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger val = ii.getValue();
					final String stav = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( val , ps );
					final String stak = key.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
					ps.print( st );
					ps.print( ".partialMap.put( " );
					ps.print( stak );
					ps.print( " , " );
					ps.print( stav );
					ps.println( " );" );
				}
			}
			return( st );
		}
		
		
		/**
		 * Applies a discretized approximation of a derivative using the following rules where "N"
		 * is the number of derivatives and "h" is the size of the discretization:
		 * <P>
		 * <P> N = 0 -- Zero derivatives have been taken so simply return the original value of  the elem.
		 * <P>
		 * <P> N = 1 -- Use the first derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <mi>h</mi>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Numerical_differentiation)
		 * <P>
		 * <P> N = 2 -- Use the second derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>2</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Second_derivative)
		 * <P>
		 * <P> N = 3 -- Use the third derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>3</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://www.geometrictools.com/Documentation/FiniteDifferences.pdf)
		 * <P>
		 * <P> N > 3 -- Combine results from smaller values of N to build an approximation of a higher-order derivative.
		 * 
		 * @param implicitSpacesIn The input implicit space containing the discretized approximation function.
		 * @param node The ordinate over which to take the derivative.
		 * @param numDerivatives The number of derivatives to apply.
		 * @param hh The size of the discretization.
		 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
		 * @throws MultiplicativeDistributionRequiredException 
		 * @throws NotInvertibleException 
		 */
		protected void applyDerivativeAction( HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesIn , 
				Ordinate node , final int numDerivatives , DoubleElem hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			if( numDerivatives > 3 )
			{
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
				applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
				applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
				return;
			}
			
			for( final Entry<HashMap<Ordinate, BigInteger>,CoeffNode> ii : implicitSpacesIn.entrySet() )
			{
				final HashMap<Ordinate, BigInteger> implicitSpace = ii.getKey();
				final CoeffNode coeffNodeIn = ii.getValue();
				
				switch( numDerivatives )
				{
				
				case 0:
					{
						applyAdd( implicitSpace , coeffNodeIn , implicitSpacesOut );
					}
					break;
				
				case 1:
					{
						applyDerivativeAction1( 
								implicitSpace , coeffNodeIn, 
								node , hh , implicitSpacesOut );
					}
					break;
					
				case 2:
					{
						applyDerivativeAction2( 
								implicitSpace , coeffNodeIn, 
								node , hh , implicitSpacesOut );
					}
					break;
					
				case 3:
					{
						applyDerivativeAction3( 
								implicitSpace , coeffNodeIn, 
								node , hh , implicitSpacesOut );
					}
					break;
					
				default:
					{
						throw( new RuntimeException( "Internal Error" ) );
					}
					
				}
			}
		}
		
		
		
		/**
		 * Applies a discretized approximation of a first derivative.
		 * 
		 * @param implicitSpace The input implicit space containing the discretized approximation function.
		 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
		 * @param node The ordinate over which to take the derivative.
		 * @param hh The size of the discretization.
		 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		protected void applyDerivativeAction1( 
				final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
				Ordinate node , DoubleElem hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			
			for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
			{
				Ordinate ae = ii.getKey();
				final BigInteger valAe = ii.getValue();
				if( node.symbolicEquals( ae ) )
				{
					final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
					final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
					implicitSpaceOutM1.put( ae , valAeM1 );
					implicitSpaceOutP1.put( ae , valAeP1 );
				}
				else
				{
					implicitSpaceOutM1.put( ae , valAe );
					implicitSpaceOutP1.put( ae , valAe );
				}
			}
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( new DoubleElem( 2.0 ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( new DoubleElem( 2.0 ) ), hh.getFac() ) ) );
			
			applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
			applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
		}
		
		
		
		
		/**
		 * Applies a discretized approximation of a second derivative.
		 * 
		 * @param implicitSpace The input implicit space containing the discretized approximation function.
		 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
		 * @param node The ordinate over which to take the derivative.
		 * @param hh The size of the discretization.
		 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		protected void applyDerivativeAction2( 
				final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
				Ordinate node , DoubleElem hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			
			for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
			{
				Ordinate ae = ii.getKey();
				final BigInteger valAe = ii.getValue();
				if( node.symbolicEquals( ae ) )
				{
					final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
					final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
					implicitSpaceOutM1.put( ae , valAeM1 );
					implicitSpaceOutP1.put( ae , valAeP1 );
				}
				else
				{
					implicitSpaceOutM1.put( ae , valAe );
					implicitSpaceOutP1.put( ae , valAe );
				}
			}
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( SymbolicConstCache.get( new DoubleElem( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh ) , hh.getFac() ) ) );
			
			applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
			applyAdd( implicitSpace , coeffNodeOut , implicitSpacesOut );
			applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
		}
		
		
		
		
		/**
		 * Applies a discretized approximation of a third derivative.
		 * 
		 * @param implicitSpace The input implicit space containing the discretized approximation function.
		 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
		 * @param node The ordinate over which to take the derivative.
		 * @param hh The size of the discretization.
		 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		protected void applyDerivativeAction3( 
				final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn ,
				Ordinate node , DoubleElem hh ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
			
			for( final Entry<Ordinate,BigInteger> ii : implicitSpace.entrySet() )
			{
				Ordinate ae = ii.getKey();
				final BigInteger valAe = ii.getValue();
				if( node.symbolicEquals( ae ) )
				{
					final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
					final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
					final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
					final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
					implicitSpaceOutM1.put( ae , valAeM1 );
					implicitSpaceOutP1.put( ae , valAeP1 );
					implicitSpaceOutM2.put( ae , valAeM2 );
					implicitSpaceOutP2.put( ae , valAeP2 );
				}
				else
				{
					implicitSpaceOutM1.put( ae , valAe );
					implicitSpaceOutP1.put( ae , valAe );
					implicitSpaceOutM2.put( ae , valAe );
					implicitSpaceOutP2.put( ae , valAe );
				}
			}
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( SymbolicConstCache.get( new DoubleElem( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( SymbolicConstCache.get( new DoubleElem( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( SymbolicConstCache.get( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			
			applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
			applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
			applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
			applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
		}
		
		
		
		
		
		/**
		 * Adds a coefficient times the input implicit space to the output implicit space.
		 * 
		 * @param implicitSpace The input implicit space.
		 * @param node The coefficient.
		 * @param implicitSpacesOut The output implicit space.
		 * @throws MultiplicativeDistributionRequiredException 
		 * @throws NotInvertibleException 
		 */
		protected void applyAdd( 
				HashMap<Ordinate, BigInteger> implicitSpace , CoeffNode node ,
				HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			CoeffNode prev = implicitSpacesOut.get( implicitSpace );
			
			if( prev == null )
			{
				implicitSpacesOut.put( implicitSpace , node );
				return;
			}
			
			if( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() )
			{
				SymbolicElem<DoubleElem,DoubleElemFactory> outN = node.getNumer().add( prev.getNumer() );
				CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
				implicitSpacesOut.put( implicitSpace , nxt );
				return;
			}
			
			
			SymbolicElem<DoubleElem,DoubleElemFactory> outDenom = prev.getDenom().mult( node.getDenom() );
			
			SymbolicElem<DoubleElem,DoubleElemFactory> outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
			
			CoeffNode nxt = new CoeffNode( outNumer , outDenom );
			
			implicitSpacesOut.put( implicitSpace , nxt );
		}
		
		
		
	}
	
	
	
	/**
	 * Newton-Raphson evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class StelemNewton extends NewtonRaphsonSingleElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the evaluator.
		 * 
		 * @param _function The function over which to evaluate Netwon-Raphson.
		 * @param _withRespectTo The variable over which to evaluate the derivative of the function.
		 * @param implicitSpaceFirstLevel The initial implicit space over which to take the function and its derivative.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemNewton(
				SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _function,
				ArrayList<? extends Elem<?, ?>> _withRespectTo, 
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super(_function, _withRespectTo, implicitSpaceFirstLevel, null);
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}
		
		/**
		 * The iteration count for Newton-Raphson iterations.
		 */
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( DoubleElem iterationOffset )
		{
			TestStelemB_DB_Exp_NN.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void cacheIterationValue()
		{
			TestStelemB_DB_Exp_NN.cacheIterationValue();
		}
		
		@Override
		protected void retrieveIterationValue()
		{
			TestStelemB_DB_Exp_NN.retrieveIterationValue();
		}
		
		/**
		 * Copies an instance for cloneThread();
		 * 
		 * @param in The instance to copy.
		 * @param threadIndex The index of the thread for which to clone.
		 */
		protected StelemNewton( final StelemNewton in , final BigInteger threadIndex )
		{
			super( in , threadIndex );
		}
		
		@Override
		public StelemNewton cloneThread( final BigInteger threadIndex )
		{
			throw( new RuntimeException( "Not Supported" ) );
		}

		@Override
		public NewtonRaphsonSingleElem<DoubleElem, DoubleElemFactory> cloneThreadCached(
				CloneThreadCache<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				CloneThreadCache<?, ?> cacheImplicit, BigInteger threadIndex) {
			throw( new RuntimeException( "Not Supported" ) );
		}
		
	}
	
	
	
	
	/**
	 * Initializes the iter array.
	 * 
	 * @param d1 The dimensional size to be used for the initialization.
	 */
	protected void initIterArray( final double d1 )
	{
		System.out.println( "Setting Initial Conditions..." );
		long atm = System.currentTimeMillis();
		long atm2 = System.currentTimeMillis();
		for( int tcnt = 0 ; tcnt < 2 * NSTPT ; tcnt++ )
		{
			System.out.println( "Initial - " + tcnt );
			for( long acnt = 0 ; acnt < NUM_X_ITER ; acnt++ )
			{
				atm2 = System.currentTimeMillis();
				if( atm2 - atm >= IterConstants.INIT_UPDATE_DELAY )
				{
					System.out.println( ">> " + acnt );
					atm = atm2;
				}
				
				long ac = acnt;
				final int x = (int)( ac % NUM_X_ITER );
				final double dx = ( x - HALF_X ) / RAD_X;
				if( dx * dx < 1.0 )
				{
					iterArray.set( tcnt , x , 10000.0 * ( d1 * d1 ) );
				}
				else
				{
					iterArray.set( tcnt , x , 0.0 );
				}
			}
		}
		System.out.println( "Initial Conditions Set..." );
	}
	
	
	
	
	/**
	 * Performs descent iterations for one value of T.
	 * 
	 * @param tval The value of T over which to iterate.
	 * @param newton The descent algorithm to use for the iterations.
	 * @param implicitSpace2 The implicit space over which to iterate.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected void performIterationT( final int tval , final StelemNewton newton , final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 ) 
			throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		//double tmpCorrectionValue = 0.0;
		for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
		{
			fillTempArray( tval , xcnt );
			clearSpatialAssertArray();
			
							
			
			
			
			
			final double ival = TestStelemB_DB_Exp_NN.getUpdateValue();
			
			
		
			
			DoubleElem err = newton.eval( implicitSpace2 );
			
			// if( APPLY_NUMERICAL_VISCOSITY )
			// {
			//	applyNumericViscosity();
			// }
			
			
			//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			//{
			//	tmpCorrectionValue = getCorrectionValue();
			//	applyPredictorCorrector();
			//	
			//
			//	err = newton.eval( implicitSpace2 );
			//
			// if( APPLY_NUMERICAL_VISCOSITY )
			// {
			//	applyNumericViscosity();
			// }
			//}
	
	
			final double val = TestStelemB_DB_Exp_NN.getUpdateValue();
			
			if( xcnt == HALF_X )
			{
				System.out.println( "******************" );
				System.out.println( xcnt );
				System.out.println( ival );
				System.out.println( val );
				System.out.println( "## " + ( err.getVal() ) );
			}
			
			
//			Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ] == 0 );
			
//			Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ] > 0 );
			
//			Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ] > 0 );
//			Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ] > 0 );
			
//			Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ] > 0 );
//			Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ] > 0 );
			
			// for( int xc = 0 ; xc < 2 * NSTPX - 1 ; xc++ )
		    // {
			//	if( ( xc == NSTPX ) )
			//	{
			//		Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ] > 0 );
			//	}
			//	else
			//	{
			//		Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ] == 0 );
			//	}
			// }
			
			
			Assert.assertTrue( Math.abs( err.getVal() ) < ( 0.01 * Math.abs( val ) + 0.01 ) );
			
			//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			//{
			//	resetCorrectionValue( tmpCorrectionValue );	
			//}
		
			iterArray.set( tval + NSTPT , xcnt , val );
					
		}
		
	}
	
	
	
	
	
	
	
	
	protected static void loadNeuralNet() throws Throwable
	{
		
		
		final int[] cntrVals = { 2 * NSTPT + 1 , ( ( 2 * NSTPX + 1 ) - 1 ) / 2 };
		
		
		
		for( int t = 0 ; t < 2 * NSTPT + 1 ; t++ )
		{
			final int x = cntrVals[ 1 ];
			if( nodes[ t ][ x ] == null )
			{
				nodes[ t ][ x ] =  loadNeuralNode( "wvals_txt_" + t + "_" + x + "_.txt" , "bvals_txt_" + t + "_" + x + "_.txt" );
			}
		}
		
		
		
		for( int x = 0 ; x <  2 * NSTPX + 1 ; x++ )
		{
			final int t = cntrVals[ 0 ];
			if( nodes[ t ][ x ] == null )
			{
				nodes[ t ][ x ] =  loadNeuralNode( "wvals_txt_" + t + "_" + x + "_.txt" , "bvals_txt_" + t + "_" + x + "_.txt" );
			}
		}
		
		
		
		/* System.out.println( node.wvals_h1[ 15 ][ 15 ] );
		System.out.println( node.wvals_h2[ 15 ][ 15 ] );
		System.out.println( node.wvals_h3[ 15 ][ 15 ] );
		System.out.println( node.wvals_h4[ 15 ][ 15 ] );
		System.out.println( node.wvals_out[ 15 ][ 0 ] );
		
		System.out.println( node.bvals_h1[ 15 ] );
		System.out.println( node.bvals_h2[ 15 ] );
		System.out.println( node.bvals_h3[ 15 ] );
		System.out.println( node.bvals_h4[ 15 ] );
		System.out.println( node.bvals_out[ 0 ] ); */
		
		
	}
	
	
	
	
	/**
	 * Tests the ability to numerically evaluate the differential equation <math display="inline">
	 * <mrow>
	 *  <msup>
	 *          <mo>&nabla;</mo>
	 *        <mn>2</mn>
	 *  </msup>
	 *  <mi>&phi;</mi>
	 *  <mo>-</mo>
	 *  <mfrac>
	 *    <mrow>
	 *      <mn>1</mn>
	 *    </mrow>
	 *    <mrow>
	 *      <msup>
	 *              <mi>c</mi>
	 *            <mn>2</mn>
	 *      </msup>
	 *    </mrow>
	 *  </mfrac>
	 *  <mfrac>
	 *    <mrow>
	 *      <msup>
	 *              <mo>&PartialD;</mo>
	 *            <mn>2</mn>
	 *      </msup>
	 *    </mrow>
	 *    <mrow>
	 *      <mo>&PartialD;</mo>
	 *      <msup>
	 *              <mi>t</mi>
	 *            <mn>2</mn>
	 *      </msup>
	 *    </mrow>
	 *  </mfrac>
	 *  <mi>&phi;</mi>
	 *  <mo>=</mo>
	 *  <mn>0</mn>
	 * </mrow>
	 * </math>
	 *
	 * in dimensions (x, t) where "c" is an arbitrary constant.
	 *
	 *
	 */	
	public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException, Throwable
	{
		final double cmx = X_HH.getVal() / ( T_HH.getVal() );
		final double cmxRatio = cmx / C.getVal();
		if( cmxRatio < 1.0 )
		{
			System.out.println( "WARNING: cmxRatio " + cmxRatio );
		}
		else
		{
			System.out.println( "cmxRatio " + cmxRatio );
		}
		
		
		loadNeuralNet();
		
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydbC";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		iterArray = new DbFastArray2D_Dbl( graph , 4 , 4 , NUM_T_ITER , NUM_X_ITER );
		
		
		final Random rand = new Random( 3344 );
		
		final double d1 = X_HH.getVal();
		
		final TestDimensionOne tdim = new TestDimensionOne();
		
		final GeometricAlgebraOrd<TestDimensionOne> ord = new GeometricAlgebraOrd<TestDimensionOne>();
		
		
		initIterArray( d1 );
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> se = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2 =
				new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( se );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2 );
		
		
		final AStelem as = new AStelem( se2 );
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de , 0 ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final GeometricAlgebraMultivectorElemFactory<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
			new GeometricAlgebraMultivectorElemFactory<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3 , tdim , ord );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				g0 = new GeometricAlgebraMultivectorElem<
					TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( as , se3 , tdim , ord );
		
		
		final DDirec ddirec = new DDirec(de, se2);
		
		final DirectionalDerivative<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			Ordinate>
			del =
			new DirectionalDerivative<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			Ordinate>( 
					ge , 
					tdim , ord ,
					ddirec );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				del0 = del.eval( null );
		
		
		
		final PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate> pa0T 
			= new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtT );
		
		// final PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem> pa0X 
		//	= new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem>( se2 , wrtX );
	
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m0T
			= pa0T.mult( as ); 
		
		// SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m0X
		//	= pa0X.mult( as ); 
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gxx0 = del0.mult( g0 );
		
		final ArrayList<GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>> args0 
				= new ArrayList<GeometricAlgebraMultivectorElem<
					TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>>();
		args0.add( gxx0 );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gxx1 = del0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args0 );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1T
			= pa0T.mult( m0T );
		
		//SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1X
		//	= pa0X.mult( m0X );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> gtt0
			= m1T.mult( 
					( new StelemReduction3L( new StelemReduction2L( SymbolicConstCache.get( C.mult( C ) , de ) , se ) , se2 )
							).invertLeft() ).negate();
		
		
		// SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1
		//	= m1X.add( gtt0 );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gtt = new GeometricAlgebraMultivectorElem<
					TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( gtt0 , se3 , tdim , ord );
		
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne,GeometricAlgebraOrd<TestDimensionOne>, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1 = gxx1.add( gtt );
		
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1
			= mg1.get( new HashSet<BigInteger>() );
		
		
		
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de , 0 ) , new Ordinate( de , 0 ) );
		implicitSpace0.put( new Ordinate( de , 1 ) , new Ordinate( de , 0 ) );
		
		final SymbolicElem<
			SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> s0 = m1.eval( implicitSpace2 );
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final ArrayList<Elem<?, ?>> wrt3 = new ArrayList<Elem<?, ?>>();
		{
			final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
			coord.put( new Ordinate( de , 0 ) , BigInteger.valueOf( NSTPT - 1 ) );
			coord.put( new Ordinate( de , 1 ) , BigInteger.valueOf( 0 ) );
			wrt3.add( new CNelem( se , coord ) );
		}
		
		
		
		StelemNewton newton = new StelemNewton( s0 , wrt3 , implicitSpace2 );
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - NSTPT ) ; tval++ )
		{
			performIterationT( tval , newton , implicitSpace2 );
		}
		
		System.out.println( "==============================" );
		System.out.println( iterArray.get( NUM_T_ITER - 1 , HALF_X ) );
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		iterArray.close();
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
	}
	

	
}


