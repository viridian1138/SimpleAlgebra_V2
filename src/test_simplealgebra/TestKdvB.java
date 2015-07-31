





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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ddx.*;







/**
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <mfrac>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *       <mi>u</mi>
 *     </mrow>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *       <mi>t</mi>
 *     </mrow>
 *   </mfrac>
 *   <mo>+</mo>
 *   <mi>u</mi>
 *   <mfrac>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *       <mi>u</mi>
 *     </mrow>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *       <mi>x</mi>
 *     </mrow>
 *   </mfrac>
 *   <mo>=</mo>
 *   <mi>v</mi>
 *   <mfrac>
 *     <mrow>
 *       <msup>
 *               <mo>&PartialD;</mo>
 *             <mn>3</mn>
 *       </msup>
 *       <mi>u</mi>
 *     </mrow>
 *     <mrow>
 *       <mo>&PartialD;</mo>
 *       <msup>
 *               <mi>x</mi>
 *             <mn>3</mn>
 *       </msup>
 *     </mrow>
 *   </mfrac>
 * </mrow>
 * </math>
 *
 * in dimensions (x, t) where "v" is an arbitrary constant.
 *
 *
 * See http://en.wikipedia.org/wiki/Korteweg-de_Vries_equation
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestKdvB extends TestCase {
	
	
	/**
	 * The arbitrary constant.
	 */
	protected static final DoubleElem V = new DoubleElem( 1E-8 );
	
	
	
	
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
	protected static final int NUM_T_ITER = 150;
	
	
	
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
	protected static final int NSTPT = 1;
	
	
	/**
	 * Temp step size in the X-direction.
	 */
	protected static final int NSTPX = 2;
	
	
	/**
	 * Indicates whether predictor-corrector should be used while iterating.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected static final boolean USE_PREDICTOR_CORRECTOR = true;
	
	
	
	/**
	 * Result array over which to iterate.
	 */
	protected static double[][] iterArray = new double[ NUM_T_ITER ][ NUM_X_ITER ];
	
	
	
	/**
	 * Temporary array in which to generate Newton-Raphson solutions.
	 * <p>0 = T
	 * <p>1 = X
	 */
	private static double[][] tempArray = new double[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ];
	
	
	
	/**
	 * Given a change calculated by a Newton-Raphson iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( DoubleElem dbl )
	{
		tempArray[ NSTPT * 2 ][ NSTPX ] += dbl.getVal();
	}
	
	
	/**
	 * Returns the result of the Newton-Raphson iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static double getUpdateValue()
	{
		return( tempArray[ NSTPT * 2 ][ NSTPX ] );
	}
	
	
	/**
	 * Returns the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static double getCorrectionValue()
	{
		return( tempArray[ NSTPT * 2 - 1 ][ NSTPX ] );
	}
	
	
	/**
	 * Resets the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @param in The value to which to reset.
	 */
	protected static void resetCorrectionValue( final double in )
	{
		tempArray[ NSTPT * 2 - 1 ][ NSTPX ] = in;
	}
	
	
	/**
	 * Applies a predictor-corrector process to the temp array.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected static void applyPredictorCorrector()
	{
		final double slopePrev = tempArray[ NSTPT * 2 - 1 ][ NSTPX ]
				- tempArray[ NSTPT * 2 - 2 ][ NSTPX ];
		final double slopeNew = tempArray[ NSTPT * 2 ][ NSTPX ]
				- tempArray[ NSTPT * 2 - 1 ][ NSTPX ];
		final double avgSlope = ( slopePrev + slopeNew ) / 2.0;
		tempArray[ NSTPT * 2 - 1 ][ NSTPX ] = 
				tempArray[ NSTPT * 2 - 2 ][ NSTPX ] + avgSlope;
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
		 * @param tcnt The X-Axis index for the center in the iter array.
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
		 * @param ta The X-Axis iteration of the array fill.
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
			av = iterArray[ tv ][ xv ];
		}
		tempArray[ ta + NSTPT ][ xa + NSTPX ] = av;
		
	}

	
	
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
	 * Node representing an ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private class Ordinate extends SymbolicElem<DoubleElem, DoubleElemFactory>
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
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public void writeString( PrintStream ps ) {
			ps.print( "a" + col + "()" );
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
	private class SymbolicConst extends SymbolicReduction<DoubleElem, DoubleElemFactory>
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
		public void writeString( PrintStream ps ) {
			ps.print( "const( " + getElem().getVal() + " )" );
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
	private class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
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
		public void writeString( PrintStream ps ) {
			ps.print( "reduce2L( " );
			getElem().writeString( ps );
			ps.print( " )" );
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
	private class StelemReduction3L extends SymbolicReduction<
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
		public void writeString( PrintStream ps ) {
			ps.print( "reduce3L( " );
			getElem().writeString( ps );
			ps.print( " )" );
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
	private class CoeffNode
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
	private class BNelem extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
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
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate keyCoord = it.next();
				BigInteger coordVal = coord.get( keyCoord );
				final int offset = keyCoord.getCol() == 1 ? NSTPX : NSTPT;
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			return( new DoubleElem( TestKdvB.tempArray[ cols[ 0 ] ][ cols[ 1 ] ] ) );
		}

		@Override
		public void writeString( PrintStream ps ) {
			String s0 = "bn";
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "()";
			ps.print( s0 );
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
				Iterator<Ordinate> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					Ordinate key = it.next();
					BigInteger ka = coord.get( key );
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
	private class CNelem extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
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
		public void writeString( PrintStream ps ) {
			String s0 = "cn";
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "()";
			ps.print( s0 );
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
				Iterator<Ordinate> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					Ordinate key = it.next();
					BigInteger ka = coord.get( key );
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
			Iterator<Ordinate> it = partialMap.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				cl.partialMap.put(key, partialMap.get(key) );
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
				Iterator<Ordinate> it = imp.keySet().iterator();
				while( it.hasNext() )
				{
					Ordinate ae = it.next();
					BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
					key.put( ae , valA );
				}
				spacesA.put( key , cf );
			}
			
			
			{
				Iterator<Ordinate> it = partialMap.keySet().iterator();
				while( it.hasNext() )
				{
					HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
					final Ordinate ae = it.next();
					final BigInteger numDerivs = partialMap.get( ae );
					applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
					spacesA = spacesB;
				}
			}
			
			
			
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
			
			
			{
				Iterator<HashMap<Ordinate, BigInteger>> it = spacesA.keySet().iterator();
				while( it.hasNext() )
				{
					HashMap<Ordinate, BigInteger> spaceAe = it.next();
					CoeffNode coeff = spacesA.get( spaceAe );
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
		public void writeString( PrintStream ps ) {
			throw( new RuntimeException( "NotSupported" ) );
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
			}
			
			Iterator<HashMap<Ordinate, BigInteger>> it = implicitSpacesIn.keySet().iterator();
			while( it.hasNext() )
			{
				final HashMap<Ordinate, BigInteger> implicitSpace = it.next();
				final CoeffNode coeffNodeIn = implicitSpacesIn.get( implicitSpace );
				
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
			
			Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
			while( itA.hasNext() )
			{
				Ordinate ae = itA.next();
				final BigInteger valAe = implicitSpace.get( ae );
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
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( new DoubleElem( 2.0 ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( new DoubleElem( 2.0 ) ), hh.getFac() ) ) );
			
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
			
			Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
			while( itA.hasNext() )
			{
				Ordinate ae = itA.next();
				final BigInteger valAe = implicitSpace.get( ae );
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
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( new SymbolicConst( new DoubleElem( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
			
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
			
			Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
			while( itA.hasNext() )
			{
				Ordinate ae = itA.next();
				final BigInteger valAe = implicitSpace.get( ae );
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
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( new SymbolicConst( new DoubleElem( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new SymbolicConst( new DoubleElem( 2.0 ) , hh.getFac() ) ) , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( new DoubleElem( 2.0 ) ) ) ), hh.getFac() ) ) );
			
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
	protected class StelemNewton extends NewtonRaphsonSingleElem<DoubleElem,DoubleElemFactory>
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
			super(_function, _withRespectTo, implicitSpaceFirstLevel);
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
			TestKdvB.performIterationUpdate( iterationOffset );
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
		for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
		{
			System.out.println( "Initial - " + tcnt );
			for( long acnt = 0 ; acnt < NUM_X_ITER ; acnt++ )
			{
				atm2 = System.currentTimeMillis();
				if( atm2 - atm >= 1000 )
				{
					System.out.println( ">> " + acnt );
					atm = atm2;
				}
				
				long ac = acnt;
				final int x = (int)( ac % NUM_X_ITER );
				final double dx = ( x - HALF_X ) / RAD_X;
				if( dx * dx < 1.0 )
				{
					iterArray[ tcnt ][ x ] = 10000.0 * ( d1 * d1 );
				}
				else
				{
					iterArray[ tcnt ][ x ] = 0.0;
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
		double tmpCorrectionValue = 0.0;
		// System.out.println( "//// " + tval );
		
		for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
		{
			iterArray[ tval + 1 ][ xcnt ] = iterArray[ tval ][ xcnt ];
		}
					
		for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
		{
			fillTempArray( tval , xcnt );
			clearSpatialAssertArray();
						
										
						
						
						
						
			final double ival = TestKdvB.getUpdateValue();
						
						
					
						
			DoubleElem err = newton.eval( implicitSpace2 );
						
						
			if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			{
				tmpCorrectionValue = getCorrectionValue();
				applyPredictorCorrector();
							
				err = newton.eval( implicitSpace2 );
			}
				
				
			final double val = TestKdvB.getUpdateValue();
						
			if( xcnt == HALF_X )
			{
				System.out.println( "******************" );
				System.out.println( xcnt );
				System.out.println( ival );
				System.out.println( val );
				System.out.println( "## " + ( err.getVal() ) );
			}
						
						
			// Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ] == 0 );
						
			// Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ] > 0 );
						
			// Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ] > 0 );
			// Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ] > 0 );
						
			// Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ] > 0 );
			// Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ] > 0 );
						
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
						
			if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
			{
				resetCorrectionValue( tmpCorrectionValue );
			}
					
			iterArray[ tval + 1 ][ xcnt ] = val;
								
		}
	}
	
	
	
	
	/**
	 * Tests the ability to numerically evaluate the differential equation <math display="inline">
	 * <mrow>
	 *   <mfrac>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *       <mi>u</mi>
	 *     </mrow>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *       <mi>t</mi>
	 *     </mrow>
	 *   </mfrac>
	 *   <mo>+</mo>
	 *   <mi>u</mi>
	 *   <mfrac>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *       <mi>u</mi>
	 *     </mrow>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *       <mi>x</mi>
	 *     </mrow>
	 *   </mfrac>
	 *   <mo>=</mo>
	 *   <mi>v</mi>
	 *   <mfrac>
	 *     <mrow>
	 *       <msup>
	 *               <mo>&PartialD;</mo>
	 *             <mn>3</mn>
	 *       </msup>
	 *       <mi>u</mi>
	 *     </mrow>
	 *     <mrow>
	 *       <mo>&PartialD;</mo>
	 *       <msup>
	 *               <mi>x</mi>
	 *             <mn>3</mn>
	 *       </msup>
	 *     </mrow>
	 *   </mfrac>
	 * </mrow>
	 * </math>
	 *
	 * in dimensions (x, t) where "v" is an arbitrary constant.
	 *
	 *
	 * See http://en.wikipedia.org/wiki/Burgers'_equation
	 *
	 */
	public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final Random rand = new Random( 3344 );
		
		final double d1 = X_HH.getVal();
		
		
		
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
		
		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
		
		wrtX.add( new Ordinate( de , 1 ) );
		
		
		PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate> pa0T 
		= new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtT );
	
	PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate> pa0X 
		= new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtX );

	
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m0T
		= pa0T.mult( as ); 
	
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m0X
		= pa0X.mult( as ); 
	
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1X
		= pa0X.mult( m0X );
	
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m2X
		= pa0X.mult( m1X );
	
	
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
		um0X = as.mult( m0X );
	
	
	final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1T
		= pa0T.mult( m0T );
	
	
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1
		= ( m2X.mult( ( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( V , de ) , se ) , se2 ) ) ) 
				).add( um0X.negate() ).add( m0T.negate() );
	
	
		
		
		
		
		
		
		
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
			coord.put( new Ordinate( de , 0 ) , BigInteger.valueOf( 1 ) );
			coord.put( new Ordinate( de , 1 ) , BigInteger.valueOf( 0 ) );
			wrt3.add( new CNelem( se , coord ) );
		}
		
		
		
		StelemNewton newton = new StelemNewton( s0 , wrt3 , implicitSpace2 );
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - 1 ) ; tval++ )
		{
			performIterationT( tval , newton , implicitSpace2 );
		}
		
		System.out.println( "==============================" );
		System.out.println( iterArray[ NUM_T_ITER - 1 ][ HALF_X ] );
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	

	
}


