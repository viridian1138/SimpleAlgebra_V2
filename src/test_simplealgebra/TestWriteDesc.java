






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
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.Sqrt;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.meas.ValueWithUncertaintyElem;
import simplealgebra.meas.ValueWithUncertaintyElemFactory;
import simplealgebra.prec.DefaultPrecedenceComparator;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicOps;
import simplealgebra.symbolic.SymbolicSqrt;
import test_simplealgebra.TestMandelbrotSet.LrgPrecision;




/**
 * Tests for writing elems to a PrintStream.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestWriteDesc extends TestCase 
{

	
	/**
	 * Constant containing the number ten.
	 */
	static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	/**
	 * Returns the number <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>, where X is the input parameter.
	 * 
	 * @param cnt The input parameter.
	 * @return The value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *       <mrow>
     *         <mi>X</mi>
     *         <mo>+</mo>
     *         <mn>1</mn>
     *       </mrow>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static BigInteger calcVal( final int cnt )
	{
		BigInteger ret = TEN;
		for( int i = 0 ; i < cnt ; i++ )
		{
			ret = ret.multiply( TEN );
		}
		return( ret );
	}
	

	
	/**
	 * Constant containing the value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * Largest possible double is around <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>308</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	static final BigInteger baseVal = calcVal( 800 );
	
	
	/**
	 * Constant containing the square of baseVal, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>1602</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	protected static final BigInteger finalBaseValSq = baseVal.multiply( baseVal );
	
	
	/**
	 * Defines a precision of baseVal, or one part in <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>801</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static final class LrgPrecision extends Precision<LrgPrecision>
	{
		@Override
		public BigInteger getVal()
		{
			return( baseVal );
		}
		
		@Override
		public BigInteger getValSquared()
		{
			return( finalBaseValSq );
		}
		
	}
	
	
	/**
	 * A constant defining the large precision.
	 */
	static final LrgPrecision lrgPrecision = new LrgPrecision();
	
	
	
	
	/**
	 * A component of the "A" vector.
	 * 
	 * @author tgreen
	 *
	 */
	private class AElem extends SymbolicElem<DoubleElem, DoubleElemFactory>
	{
		
		/**
		 * The vector ordinate index.
		 */
		private int col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The vector ordinate index.
		 */
		public AElem(DoubleElemFactory _fac, int _col) {
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
		public String writeDesc( WriteElemCache<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> cache , PrintStream ps )
		{
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem,DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<mrow><msub><mi>a</mi><mn>" + col + "</mn></msub></mrow>" );
		}
		
		/**
		 * Returns the vector ordinate index.
		 * 
		 * @return The vector ordinate index.
		 */
		public int getCol() {
			return col;
		}

	}
	
	
	
	

	/**
	 * A component of the "B" vector.
	 * 
	 * @author tgreen
	 *
	 */
	private class BElem extends SymbolicElem<DoubleElem, DoubleElemFactory>
	{
		
		/**
		 * The vector ordinate index.
		 */
		private int col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The vector ordinate index.
		 */
		public BElem(DoubleElemFactory _fac, int _col) {
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
		public String writeDesc( WriteElemCache<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> cache , PrintStream ps )
		{
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem,DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<mrow><msub><mi>b</mi><mn>" + col + "</mn></msub></mrow>" );
		}
		
		/**
		 * Returns the vector ordinate index.
		 * 
		 * @return The vector ordinate index.
		 */
		public int getCol() {
			return col;
		}

	}
	
	
	
	
	/**
	 * Provides precedence comparison rules for the test.  Used when generating MathML.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class PrecCompare extends DefaultPrecedenceComparator
	{
		
		@Override
		protected void enclosedOrTerminalSymbolsAInit()
		{
			defaultEnclosedOrTerminalSymbolsAInit();
		}
		
		
		
		@Override
		protected void enclosedOrTerminalSymbolsBInit()
		{
			defaultEnclosedOrTerminalSymbolsBInit();
			enclosedOrTerminalSymbolsB.add( AElem.class );
			enclosedOrTerminalSymbolsB.add( BElem.class );
		}
		
	}
	
	
	
	
	/**
	 * Tests the writing of elems to a PrintStream.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testWriteDesc() throws NotInvertibleException
	{
		final DefaultPrecedenceComparator dp = new PrecCompare();
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		String aa = null;
		
		
		System.out.println( "***" );
		
		aa = ye.writeDesc( ye.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final DoubleElem d = new DoubleElem( 1.2345E5 );
		
		final DoubleElem d2 = new DoubleElem( -5.4321E5 );
		
		
		aa = d.writeDesc( d.getFac().generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		d.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		aa = de.writeDesc( de.generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		
		
		final BigFixedPointElemFactory<LrgPrecision> dlf = new BigFixedPointElemFactory<LrgPrecision>( lrgPrecision );
		
		
		aa = dlf.writeDesc( dlf.generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		
		
		final BigFixedPointElem<LrgPrecision> ddlf = new BigFixedPointElem<LrgPrecision>( BigInteger.valueOf( 1 ) , lrgPrecision);
		
		aa = ddlf.writeDesc( ddlf.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		ddlf.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		
		final AElem a1 = new AElem( de , 1 );
		
		aa = a1.writeDesc( a1.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		a1.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		
		final BElem b2 = new BElem( de , 2 );
		
		aa = b2.writeDesc( b2.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		b2.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		
		aa = ce.writeDesc( ce.generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> cd = new ComplexElem<DoubleElem,DoubleElemFactory>( d , d2 );
		
		
		aa = cd.writeDesc( cd.getFac().generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		cd.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		final ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> cse2 = new ComplexElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( ye );
		
		
		aa = cse2.writeDesc( cse2.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		
		final ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> csd2 = new ComplexElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( a1 , b2 );
		
		aa = csd2.writeDesc( csd2.getFac().generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		csd2.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		
		
		final ValueWithUncertaintyElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> csv2 = new ValueWithUncertaintyElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( ye );
		
		
		aa = csv2.writeDesc( csv2.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		
		final ValueWithUncertaintyElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> cssv2 = new ValueWithUncertaintyElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( a1 , b2 );
		
		aa = cssv2.writeDesc( cssv2.getFac().generateWriteElemCache() , System.out );

		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		cssv2.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		
		
		final ComplexElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> ce2 = new ComplexElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce );
		
		
		aa = ce2.writeDesc( ce2.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> cd2 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -2.3 ) , new DoubleElem( -4.6 ) );
		
		
		aa = cd2.writeDesc( cd2.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		cd2.writeMathMLWrapped(dp, System.out);
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		final ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> cd2a =
				new ComplexElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cd , cd2 );
		
		aa = cd2a.writeDesc( cd2a.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		
		
		
		
		final ValueWithUncertaintyElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> ve2 = new ValueWithUncertaintyElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce );
		
		
		aa = ve2.writeDesc( ve2.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final ValueWithUncertaintyElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> cv2a =
				new ValueWithUncertaintyElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cd , cd2 );
		
		aa = cv2a.writeDesc( cv2a.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final SquareMatrixElemFactory<TestDimensionFour,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sqe2 = new SquareMatrixElemFactory<TestDimensionFour,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce , td );
		
		
		aa = sqe2.writeDesc( sqe2.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final SquareMatrixElem<TestDimensionFour,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sq2a =
				new SquareMatrixElem<TestDimensionFour,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce, td );
		
		sq2a.setVal( BigInteger.valueOf( 1 ) , BigInteger.valueOf( 2 ) , cd );
		
		sq2a.setVal( BigInteger.valueOf( 2 ) , BigInteger.valueOf( 3 ) , cd2 );
		
		aa = sq2a.writeDesc( sq2a.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		sq2a.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>( );
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ge = new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce, td, ord);
		
		aa = ge.writeDesc( ge.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ge2 = new GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce, td, ord);
	
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( 1 ) );
			ind.add( BigInteger.valueOf( 2 ) );
			ge2.setVal( ind , cd );
		}
		
		{
			final HashSet<BigInteger> ind = new HashSet<BigInteger>();
			ind.add( BigInteger.valueOf( 2 ) );
			ind.add( BigInteger.valueOf( 3 ) );
			ge2.setVal( ind , cd2 );
		}
		
		aa = ge2.writeDesc( ge2.getFac().generateWriteElemCache() , System.out );
	
		System.out.println( "### " + aa );
	
		System.out.println( "***" );
		
		ge2.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
		
		
		final EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ee = new EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce);
		
		aa = ee.writeDesc( ee.generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
		final ArrayList<String> contravars = new ArrayList<String>();
		
		final ArrayList<String> covars = new ArrayList<String>();
		
		covars.add( "i" );
		
		covars.add( "j" );

		final EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ed = new EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce, contravars, covars);
		
		{
			final ArrayList<BigInteger> ind = new ArrayList<BigInteger>();
			ind.add( BigInteger.valueOf( 4 ) );
			ind.add( BigInteger.valueOf( 5 ) );
			ed.setVal( ind , cd );
		}
		
		{
			final ArrayList<BigInteger> ind = new ArrayList<BigInteger>();
			ind.add( BigInteger.valueOf( 5 ) );
			ind.add( BigInteger.valueOf( 6 ) );
			ed.setVal( ind , cd2 );
		}
		
		
		aa = ed.writeDesc( ed.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		ed.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	/**
	 * Test for writing the components of a cross-product.
	 * 
	 * @throws Throwable
	 */
	public void testWriteDescCrossProduct() throws Throwable
	{
		final DefaultPrecedenceComparator dp = new PrecCompare();
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> ye = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>(dl);
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>( );
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			ge = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(ye, td, ord);

		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			ga1 = new GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(ye, td, ord);


		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			ga2 = new GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(ye, td, ord);
		
		for( int cnt = 0 ; cnt < 3 ; cnt++ )
		{
			final AElem aa = new AElem( dl , cnt );
			final BElem ba = new BElem( dl , cnt );
			final BigInteger ii = BigInteger.valueOf( cnt );
			final HashSet<BigInteger> set = new HashSet<BigInteger>();
			set.add( ii );
			ga1.setVal(set, aa);
			ga2.setVal(set, ba);
		}
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>();
		
		args.add( ga2 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			ga3a = ga1.handleOptionalOp( GeometricAlgebraMultivectorCmd.CROSS , args );
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
			ga3 = new GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(ye, td, ord);

		
		for( final Entry<HashSet<BigInteger>, SymbolicElem<DoubleElem, DoubleElemFactory>> ii : ga3a.getEntrySet() )
		{
			final SymbolicElem<DoubleElem, DoubleElemFactory> nv = ii.getValue().handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY2 , null);
					
			ga3.setVal( ii.getKey() , nv );
		}
		
		
		String aa = ga3.writeDesc( ga3.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		ga3.writeMathMLWrapped( dp , System.out );
		
		System.out.println( "" );
		
		System.out.println( "***" );
		
		
	}
	
	

	
}


