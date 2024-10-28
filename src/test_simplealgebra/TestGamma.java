



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

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.algo.ExtendedFactorial;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.ga.Ord;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;


/**
 * Tests the ability to calculate extended factorials.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestGamma extends TestCase {
	
	
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
	 * Builds a fixed-point value from a rational number.
	 * 
	 * @param numerator The numerator of the rational number.
	 * @param denominator The denominator of the rational number.
	 * @return The fixed-point value.
	 */
	static BigFixedPointElem<LrgPrecision> buildElem( BigInteger numerator , BigInteger denominator )
	{
		final BigInteger val = numerator.multiply( baseVal ).divide( denominator );
		return( new BigFixedPointElem<LrgPrecision>( val , lrgPrecision ) );
	}
	
	
	
	
	/**
	 * Test class for calculating the extended factorial of scalars.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class ExtendedFactorialLrg extends ExtendedFactorial<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>  
	{

		/**
		 * Constructor
		 * 
		 * @param _fac The factory for the scalar type.
		 */
		public ExtendedFactorialLrg(
				BigFixedPointElemFactory<LrgPrecision> _fac) {
			super(_fac);
		}
		
		@Override
		protected BigFixedPointElem<LrgPrecision> convert( BigInteger val ) throws NotInvertibleException
		{
			return( buildElem( val , BigInteger.ONE ) );
		}

		@Override
		protected boolean beyondPositiveEpsilon(
				BigFixedPointElem<LrgPrecision> vv) {
			return( vv.toDouble() > 1E-20 );
		}
		
		
		
	}
	
	
	
	
	/**
	 * Test class for calculating the extended factorial of complex numbers.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class ExtendedFactorialComplex extends ExtendedFactorial<ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>,ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructor
		 * 
		 * @param _fac The factory for the complex number type.
		 */
		public ExtendedFactorialComplex(
				ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
		}
		
		@Override
		protected ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> convert( BigInteger val ) throws NotInvertibleException
		{
			return( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					buildElem( val , BigInteger.ONE ) , fac.getFac().zero() ) );
		}

		@Override
		protected boolean beyondPositiveEpsilon(
				ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> vv) {
			return( vv.getRe().toDouble() > 1E-20 );
		}
		
		
		
	}
	
	
	
	
	
	/**
	 * Test class for calculating the extended factorial of multivectors.
	 * 
	 * @author thorngreen
	 *
	 * @param <U> The number of dimensions in the multivector.
	 * @param <A> The Ord of the multivector.
	 */
	protected static class ExtendedFactorialMultivector<U extends NumDimensions, A extends Ord<U>> extends ExtendedFactorial<GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>,GeometricAlgebraMultivectorElemFactory<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructor
		 * 
		 * @param _fac The factory for the multivector type.
		 */
		public ExtendedFactorialMultivector(
				GeometricAlgebraMultivectorElemFactory<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
		}
		
		@Override
		protected GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> convert( BigInteger val ) throws NotInvertibleException
		{
			BigFixedPointElem<LrgPrecision> vale = buildElem( val , BigInteger.ONE );
			GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
				elem = new GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
						vale , fac.getFac() , fac.getDim() , fac.getOrd() );
						
			return( elem );
		}

		@Override
		protected boolean beyondPositiveEpsilon(
				GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> vv) {
			HashSet<BigInteger> scalarKey = new HashSet<BigInteger>();
			return( vv.getVal(scalarKey).toDouble() > 1E-20 );
		}
		
		@Override
		protected GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> calcLnNval( int n , int numIterExp, int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			BigFixedPointElem<LrgPrecision> nv = buildElem( BigInteger.valueOf(n) , BigInteger.ONE );
			BigFixedPointElem<LrgPrecision> nvLn = nv.ln(numIterExp, numIterLn);
			GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
				elem = new GeometricAlgebraMultivectorElem<U, A, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					nvLn , fac.getFac() , fac.getDim() , fac.getOrd() );
			return( elem );
		}
		
		
		
	}
	
	
	
	
	/**
	 * Test class for calculating the extended factorial of matrices.
	 * 
	 * @author thorngreen
	 *
	 * @param <U> The number of dimensions in the matrix.
	 */
	protected static class ExtendedFactorialMatrix<U extends NumDimensions> extends ExtendedFactorial<SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>,SquareMatrixElemFactory<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>>
	{

		/**
		 * Constructor
		 * 
		 * @param _fac The factory for the matrix type.
		 */
		public ExtendedFactorialMatrix(
				SquareMatrixElemFactory<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> _fac) {
			super(_fac);
		}
		
		@Override
		protected SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> convert( BigInteger val ) throws NotInvertibleException
		{
			BigFixedPointElem<LrgPrecision> vale = buildElem( val , BigInteger.ONE );
			SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
				elem = new SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
						vale , fac.getFac() , fac.getDim() );
						
			return( elem );
		}

		@Override
		protected boolean beyondPositiveEpsilon(
				SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> vv) {
			/* HashSet<BigInteger> scalarKey = new HashSet<BigInteger>();
			return( vv.getVal(scalarKey).toDouble() > 1E-20 ); */
			return( true );  // Not sure how to use this with matrices
		}
		
		@Override
		protected SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> calcLnNval( int n , int numIterExp, int numIterLn ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			BigFixedPointElem<LrgPrecision> nv = buildElem( BigInteger.valueOf(n) , BigInteger.ONE );
			BigFixedPointElem<LrgPrecision> nvLn = nv.ln(numIterExp, numIterLn);
			SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
				elem = new SquareMatrixElem<U, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					nvLn , fac.getFac() , fac.getDim() );
			return( elem );
		}
		
		
		
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valB The second value to be checked.
	 * @param tolerance The tolerance.
	 */
	void checkValueLrg( BigFixedPointElem<LrgPrecision> valA , double valB , double tolerance )
	{
		final double vlA = valA.toDouble();
		Assert.assertTrue( Math.abs( vlA - valB ) < tolerance );
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valRe The real part of the second value to be checked.
	 * @param valIm The imaginary part of the second value to be checked.
	 * @param tolerance The tolerance.
	 */
	void chkValueCplx( ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> valA , double valRe , double valIm , double tolerance )
	{
		checkValueLrg( valA.getRe() , valRe , tolerance );
		checkValueLrg( valA.getIm() , valIm , tolerance );
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valRe The real part of the second value to be checked.
	 * @param valIm The imaginary part of the second value to be checked.
	 * @param tolerance The tolerance.
	 */
	void chkValueCplx( ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> valA , double valRe, double tolerance )
	{
		checkValueLrg( valA.getRe() , valRe , tolerance );
		checkValueLrg( valA.getIm() , 0.0 , tolerance );
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valB The second value to be checked.
	 * @param tolerance The tolerance.
	 */
	void chkValueMultivec( GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> valA , double valB , double tolerance )
	{
		final HashSet<BigInteger> scalarKey = new HashSet<BigInteger>();
		
		checkValueLrg( valA.getVal( scalarKey ) , valB , tolerance );
		
		for( Entry<HashSet<BigInteger>, BigFixedPointElem<LrgPrecision>> i : valA.getEntrySet() )
		{
			if( !( i.getKey().equals(scalarKey) ) )
			{
				checkValueLrg( i.getValue() , 0.0 , tolerance );
			}
		}
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valScalar The scalar part of the second value to be checked.
	 * @param valBivector The bivector part of the second value to be checked.
	 * @param tolerance The tolerance.
	 */
	void chkValueMultivec12( GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> valA , double valScalar , double valBivector , double tolerance )
	{
		final HashSet<BigInteger> scalarKey = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> bivectorKey = new HashSet<BigInteger>();
		bivectorKey.add(BigInteger.ONE);
		bivectorKey.add(BigInteger.valueOf(2));
		
		checkValueLrg( valA.getVal( scalarKey ) , valScalar , tolerance );
		
		checkValueLrg( valA.getVal( bivectorKey ) , valBivector , tolerance );
		
		for( Entry<HashSet<BigInteger>, BigFixedPointElem<LrgPrecision>> i : valA.getEntrySet() )
		{
			if( !( i.getKey().equals(scalarKey) ) && !( i.getKey().equals(bivectorKey) ) )
			{
				checkValueLrg( i.getValue() , 0.0 , tolerance );
			}
		}
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valScalar The scalar part of the second value to be checked.
	 * @param valBivector The bivector part of the second value to be checked.
	 * @param tolerance The tolerance.
	 */
	void chkValueMultivec01( GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> valA , double valScalar , double valBivector , double tolerance )
	{
		final HashSet<BigInteger> scalarKey = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> bivectorKey = new HashSet<BigInteger>();
		bivectorKey.add(BigInteger.ZERO);
		bivectorKey.add(BigInteger.ONE);
		
		checkValueLrg( valA.getVal( scalarKey ) , valScalar , tolerance );
		
		checkValueLrg( valA.getVal( bivectorKey ) , valBivector , tolerance );
		
		for( Entry<HashSet<BigInteger>, BigFixedPointElem<LrgPrecision>> i : valA.getEntrySet() )
		{
			if( !( i.getKey().equals(scalarKey) ) && !( i.getKey().equals(bivectorKey) ) )
			{
				checkValueLrg( i.getValue() , 0.0 , tolerance );
			}
		}
	}
	
	
	/**
	 * Asserts that the difference between two values is less than a tolerance.
	 * @param valA The first value to be checked.
	 * @param valScalar The scalar part of the second value to be checked.
	 * @param valBivector The bivector part of the second value to be checked.
	 * @param tolerance The tolerance.
	 * @throws MultiplicativeDistributionRequiredException 
	 * @throws NotInvertibleException 
	 */
	void chkValueMultivec01_12( GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> valA ,
			double inputBivecA , double inputBivecB , double tolerance ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{

		final double bivecMag = Math.sqrt( inputBivecA * inputBivecA + inputBivecB * inputBivecB );
		
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> fac = new ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD );
		ExtendedFactorialComplex ed = new ExtendedFactorialComplex( fac );
		
		ed.init(  new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( Math.PI , lrgPrecision ) , fac.getFac().zero() ) , 10000 );
		
		ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( 
				ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
						new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ) , new BigFixedPointElem<LrgPrecision>( bivecMag , lrgPrecision ) )  , 20, 20) );
		
		
		
		final HashSet<BigInteger> scalarKey = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> bivectorKey01 = new HashSet<BigInteger>();
		bivectorKey01.add(BigInteger.ZERO);
		bivectorKey01.add(BigInteger.ONE);
		
		final HashSet<BigInteger> bivectorKey12 = new HashSet<BigInteger>();
		bivectorKey12.add(BigInteger.ONE);
		bivectorKey12.add(BigInteger.valueOf(2) );
		
		checkValueLrg( valA.getVal( scalarKey ) , extfI.getRe().toDouble() , tolerance );
		
		checkValueLrg( valA.getVal( bivectorKey01 ) , ( extfI.getIm().toDouble() ) * inputBivecA / bivecMag , tolerance );
		
		checkValueLrg( valA.getVal( bivectorKey12 ) , ( extfI.getIm().toDouble() ) * inputBivecB / bivecMag , tolerance );
		
		for( Entry<HashSet<BigInteger>, BigFixedPointElem<LrgPrecision>> i : valA.getEntrySet() )
		{
			if( !( i.getKey().equals(scalarKey) ) && !( i.getKey().equals(bivectorKey01) ) && !( i.getKey().equals(bivectorKey12) ) )
			{
				checkValueLrg( i.getValue() , 0.0 , tolerance );
			}
		}
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of scalars.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialLrg() throws Throwable
	{
		
		BigFixedPointElemFactory<LrgPrecision> fac = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		ExtendedFactorialLrg ed = new ExtendedFactorialLrg( fac );
		
		ed.init(  new BigFixedPointElem<LrgPrecision>( Math.PI , lrgPrecision ) , 10000 );
		
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( -1.5 , lrgPrecision ) , 20, 20) , -2.0 * Math.sqrt( Math.PI ) , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( -0.5 , lrgPrecision ) , 20, 20) , Math.sqrt( Math.PI ) , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ) , 20, 20) , 1.0 , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 0.5 , lrgPrecision ) , 20, 20) , 0.5 * Math.sqrt( Math.PI ) , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 1.0 , lrgPrecision ) , 20, 20) , 1.0 , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 2.0 , lrgPrecision ) , 20, 20) , 2.0 , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 3.0 , lrgPrecision ) , 20, 20) , 6.0 , 0.01 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 4.0 , lrgPrecision ) , 20, 20) , 24.0 , 0.1 );
		checkValueLrg( ed.extendedFactorial( new BigFixedPointElem<LrgPrecision>( 1.5 , lrgPrecision ) , 20, 20) , 1.329554 , 0.01 );
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of complex numbers.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialCplx() throws Throwable
	{
		
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> fac = new ComplexElemFactory<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD );
		ExtendedFactorialComplex ed = new ExtendedFactorialComplex( fac );
		
		ed.init(  new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( Math.PI , lrgPrecision ) , fac.getFac().zero() ) , 10000 );
		
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -1.5 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20) , -2.0 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -0.5 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  Math.sqrt( Math.PI ) , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  1.0 , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.5 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  0.5 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  1.0  , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 2.0 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  2.0 , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 3.0 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  6.0 , 0.01 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 4.0 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  24.0 , 0.1 );
		chkValueCplx(  ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.5 , lrgPrecision ) , fac.getFac().zero() )  , 20, 20)  ,  1.329554 , 0.01 );
		
		ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ) , fac.getFac().identity() )  , 20, 20) );
		chkValueCplx(  extfI , 0.4980156 , -0.1549498 , 0.01 );
		// Value from https://www.youtube.com/watch?v=a9l1E-KhXC4
		
		extfI = ( ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -1.0E-10 , lrgPrecision ) , fac.getFac().identity().negate() )  , 20, 20) );
		chkValueCplx(  extfI , 0.498 , 0.155 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		extfI = ( ed.extendedFactorial( new ComplexElem<BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( fac.getFac().identity().negate() , fac.getFac().identity() )  , 20, 20) );
		chkValueCplx(  extfI , -0.155 , -0.498 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of vectors.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialVector() throws Throwable
	{
		
		TestDimensionThree td = new TestDimensionThree();
		GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td, ord );
		ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>> ed = 
				new ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>>( fac );
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() , fac.getOrd() );
		
		ed.init( elemPi , 10000 );
		

		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemVect = fac.zero();
		
		HashSet<BigInteger> key1 = new HashSet<BigInteger>();
		key1.add( BigInteger.ONE );
		
		elemVect.setVal(key1, new BigFixedPointElem<LrgPrecision>( 1.2, lrgPrecision ) );
		
		
		HashSet<BigInteger> key2 = new HashSet<BigInteger>();
		key2.add( BigInteger.valueOf(2) );
		
		elemVect.setVal(key2, new BigFixedPointElem<LrgPrecision>( 0.7, lrgPrecision ) );
		
		
		// Add a small pseudoscalar so the inverse matrix will square
		HashSet<BigInteger> key12 = new HashSet<BigInteger>();
		key12.add( BigInteger.ONE );
		key12.add( BigInteger.valueOf(2) );
		
		elemVect.setVal(key12, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedFactorial( 
				elemVect  , 20, 20) );
		
		
		// for( final Entry<HashSet<BigInteger>, BigFixedPointElem<LrgPrecision>> i : extfI.getEntrySet() )
		// {
		//	for( final BigInteger j : i.getKey() )
		//	{
		//		System.out.print( j + " " );
		//	}
		//	System.out.println( i.getValue().toDouble() );
		// }
		
		Assert.assertTrue( extfI != null );
		
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of bivectors.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialBivectorA() throws Throwable
	{
		
		TestDimensionThree td = new TestDimensionThree();
		GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td, ord );
		ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>> ed = 
				new ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>>( fac );
		
		final HashSet<BigInteger> keyScalar = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> keyBivector = new HashSet<BigInteger>();
		keyBivector.add(BigInteger.ONE);
		keyBivector.add(BigInteger.valueOf(2));
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() , fac.getOrd() );
		
		ed.init( elemPi , 10000 );
		
		
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -1.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , -2.0 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -0.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 0.5 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 2.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 2.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 3.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 6.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 4.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 24.0 , 0.1 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.329554 , 0.01 );
		
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> elem = fac.zero();
		elem.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ));
		elem.setVal(keyBivector, fac.getFac().identity());
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedFactorial( elem  , 20, 20) );
		chkValueMultivec12( extfI , 0.4980156 , -0.1549498 , 0.01 );
		// Value from https://www.youtube.com/watch?v=a9l1E-KhXC4
		
		
		elem = fac.zero();
		elem.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ));
		elem.setVal(keyBivector, fac.getFac().identity().negate() );
		
		
		extfI = ( ed.extendedFactorial( elem  , 20, 20) );
		chkValueMultivec12( extfI , 0.498 , 0.155 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
		elem = fac.zero();
		elem.setVal(keyScalar, fac.getFac().identity().negate());
		elem.setVal(keyBivector, fac.getFac().identity());
		
		
		extfI = ( ed.extendedFactorial( elem  , 20, 20) );
		chkValueMultivec12( extfI , -0.155 , -0.498 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of bivectors.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialBivectorB() throws Throwable
	{
		
		TestDimensionThree td = new TestDimensionThree();
		GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td, ord );
		ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>> ed = 
				new ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>>( fac );
		
		final HashSet<BigInteger> keyScalar = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> keyBivector = new HashSet<BigInteger>();
		keyBivector.add(BigInteger.ZERO);
		keyBivector.add(BigInteger.ONE);
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() , fac.getOrd() );
		
		ed.init( elemPi , 10000 );
		
		
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -1.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , -2.0 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -0.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 0.5 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 2.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 2.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 3.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 6.0 , 0.01 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 4.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 24.0 , 0.1 );
		chkValueMultivec(  ed.extendedFactorial( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.5 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.329554 , 0.01 );
		
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> elem = fac.zero();
		elem.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ));
		elem.setVal(keyBivector, fac.getFac().identity());
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedFactorial( elem  , 20, 20) );;
		chkValueMultivec01( extfI , 0.4980156 , -0.1549498 , 0.01 );
		// Value from https://www.youtube.com/watch?v=a9l1E-KhXC4
		
		
		elem = fac.zero();
		elem.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ));
		elem.setVal(keyBivector, fac.getFac().identity().negate() );
		
		
		extfI = ( ed.extendedFactorial( elem  , 20, 20) );
		chkValueMultivec01( extfI , 0.498 , 0.155 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
		elem = fac.zero();
		elem.setVal(keyScalar, fac.getFac().identity().negate());
		elem.setVal(keyBivector, fac.getFac().identity());
		
		
		extfI = ( ed.extendedFactorial( elem  , 20, 20) );
		chkValueMultivec01( extfI , -0.155 , -0.498 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of bivectors.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialBivectorC() throws Throwable
	{
		final double bivecValA = 0.7;
		
		final double bivecValB = 1.2;
		
		
		TestDimensionThree td = new TestDimensionThree();
		GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td, ord );
		ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>> ed = 
				new ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>>( fac );
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() , fac.getOrd() );
		
		ed.init( elemPi , 10000 );
		

		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemVect = fac.zero();
		
		
		HashSet<BigInteger> keyScalar = new HashSet<BigInteger>();
		
		elemVect.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		HashSet<BigInteger> key0 = new HashSet<BigInteger>();
		key0.add( BigInteger.ZERO );
		
		elemVect.setVal(key0, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		HashSet<BigInteger> key1 = new HashSet<BigInteger>();
		key1.add( BigInteger.ONE );
		
		elemVect.setVal(key1, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		HashSet<BigInteger> key2 = new HashSet<BigInteger>();
		key2.add( BigInteger.valueOf(2) );
		
		elemVect.setVal(key2, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		
		HashSet<BigInteger> key01 = new HashSet<BigInteger>();
		key01.add( BigInteger.ZERO );
		key01.add( BigInteger.ONE );
		
		elemVect.setVal(key01, new BigFixedPointElem<LrgPrecision>( bivecValA , lrgPrecision ) );
		
		
		
		HashSet<BigInteger> key12 = new HashSet<BigInteger>();
		key12.add( BigInteger.ONE );
		key12.add( BigInteger.valueOf(2) );
		
		elemVect.setVal(key12, new BigFixedPointElem<LrgPrecision>( bivecValB , lrgPrecision ) );
		
		
		
		HashSet<BigInteger> key02 = new HashSet<BigInteger>();
		key02.add( BigInteger.ZERO );
		key02.add( BigInteger.valueOf(2) );
		
		elemVect.setVal(key02, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		
		HashSet<BigInteger> key012 = new HashSet<BigInteger>();
		key012.add( BigInteger.ZERO );
		key012.add( BigInteger.ONE );
		key012.add( BigInteger.valueOf(2) );
		
		elemVect.setVal(key012, new BigFixedPointElem<LrgPrecision>( 1E-10, lrgPrecision ) );
		
		
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedFactorial( 
				elemVect  , 20, 20) );
		
		
		chkValueMultivec01_12( extfI ,
				bivecValA , bivecValB , 0.01 );
		
		
		// for( final Entry<HashSet<BigInteger>, BigFixedPointElem<LrgPrecision>> i : extfI.getEntrySet() )
		// {
		//	for( final BigInteger j : i.getKey() )
		//	{
		//		System.out.print( j + " " );
		//	}
		//	System.out.println( i.getValue().toDouble() );
		// }
		
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate extended factorial of matrices.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedFactorialMatrix() throws Throwable
	{
		
		TestDimensionFour td = new TestDimensionFour();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		SquareMatrixElemFactory<TestDimensionFour, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new SquareMatrixElemFactory<TestDimensionFour, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td );
		ExtendedFactorialMatrix<TestDimensionFour> ed = 
				new ExtendedFactorialMatrix<TestDimensionFour>( fac );
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		SquareMatrixElem<TestDimensionFour, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new SquareMatrixElem<TestDimensionFour, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() );
		
		ed.init( elemPi , 10000 );
		

		SquareMatrixElem<TestDimensionFour, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemVect = fac.zero();
		
		Random rand = new Random( 5432 );
		
		for( BigInteger i = BigInteger.ZERO ; i.compareTo( td.getVal() )  < 0 ; i = i.add(BigInteger.ONE))
		{
			BigFixedPointElem<LrgPrecision> valA = new BigFixedPointElem<LrgPrecision>( 1.0 + rand.nextDouble(), lrgPrecision );
			elemVect.setVal(i, i, valA);
			BigFixedPointElem<LrgPrecision> valB = new BigFixedPointElem<LrgPrecision>( 1.0 + rand.nextDouble(), lrgPrecision );
			BigInteger col = i.add( BigInteger.ONE );
			if(  col.compareTo( td.getVal() )  < 0)
			{
				elemVect.setVal(i, col, valB);
			}
		}
		
		
		// System.out.println( " Started Mat Calc " );
		
		SquareMatrixElem<TestDimensionFour, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedFactorial( 
				elemVect  , 20, 20) );
		
		// System.out.println( " Ended Mat Calc " );
		
		
		// for( BigInteger i = BigInteger.ZERO ; i.compareTo( td.getVal() )  < 0 ; i = i.add(BigInteger.ONE))
		// {
		//	for( BigInteger j = BigInteger.ZERO ; j.compareTo( td.getVal() )  < 0 ; j = j.add(BigInteger.ONE))
		//	{
		//		System.out.print( i + " " );
		//		System.out.print( j + " " );
		//		System.out.println( extfI.getVal(i,j).toDouble() );
		//	}
		// }
		
		Assert.assertTrue( extfI != null );
		
		
	}
	
	
	
	/**
	 * Tests the ability to calculate the approximate gamma function of scalars and bivectors.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedGammaBivectorA() throws Throwable
	{
		
		TestDimensionThree td = new TestDimensionThree();
		GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td, ord );
		ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>> ed = 
				new ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>>( fac );
		
		final HashSet<BigInteger> keyScalar = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> keyBivector = new HashSet<BigInteger>();
		keyBivector.add(BigInteger.ONE);
		keyBivector.add(BigInteger.valueOf(2));
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() , fac.getOrd() );
		
		ed.init( elemPi , 10000 );
		
		
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -1.5 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , -2.0 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( -0.5 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0E-10 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 1.0 , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 0.5 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 0.5 * Math.sqrt( Math.PI ) , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.0 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) ,  1.0 , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 2.0 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) ,  2.0 , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 3.0 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) ,  6.0 , 0.01 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 4.0 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) ,  24.0 , 0.1 );
		chkValueMultivec(  ed.extendedGamma( new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 1.5 + 1.0 , lrgPrecision ) , fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) ,  1.329554 , 0.01 );
		
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> elem = fac.zero();
		elem.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 + 1.0 , lrgPrecision ));
		elem.setVal(keyBivector, fac.getFac().identity());
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedGamma( elem  , 20, 20) );;
		chkValueMultivec12( extfI , 0.4980156 , -0.1549498 , 0.01 );
		// Value from https://www.youtube.com/watch?v=a9l1E-KhXC4
		
		
		elem = fac.zero();
		elem.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 + 1.0 , lrgPrecision ));
		elem.setVal(keyBivector, fac.getFac().identity().negate() );
		
		
		extfI = ( ed.extendedGamma( elem  , 20, 20) );
		chkValueMultivec12( extfI , 0.498 , 0.155 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
		elem = fac.zero();
		elem.setVal(keyScalar, fac.getFac().zero());
		elem.setVal(keyBivector, fac.getFac().identity());
		
		
		extfI = ( ed.extendedGamma( elem  , 20, 20) );
		chkValueMultivec12( extfI , -0.155 , -0.498 , 0.01 );
		// Value from https://springerplus.springeropen.com/articles/10.1186/2193-1801-3-658
		
		
	}
	
	
	
	
	/**
	 * Tests the ability to calculate the approximate binomial coefficients of scalars and bivectors.
	 * 
	 * @throws Throwable
	 */
	public void testExtendedBinomialCoefficientBivectorA() throws Throwable
	{
		
		TestDimensionThree td = new TestDimensionThree();
		GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		BigFixedPointElemFactory<LrgPrecision> facD = new BigFixedPointElemFactory<LrgPrecision>(lrgPrecision);
		GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> 
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( facD, td, ord );
		ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>> ed = 
				new ExtendedFactorialMultivector<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>>( fac );
		
		final HashSet<BigInteger> keyScalar = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> keyBivector = new HashSet<BigInteger>();
		keyBivector.add(BigInteger.ONE);
		keyBivector.add(BigInteger.valueOf(2));
		
		BigFixedPointElem<LrgPrecision> valPi = new BigFixedPointElem<LrgPrecision>( Math.PI, lrgPrecision );
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>
			elemPi = new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( 
					valPi , fac.getFac() , fac.getDim() , fac.getOrd() );
		
		ed.init( elemPi , 10000 );
		
		
		chkValueMultivec(  ed.extendedBinomialCoefficient( 
				new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 4.0 , lrgPrecision ) ,
						fac.getFac() , fac.getDim() , fac.getOrd() )  ,
				new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 2.0 , lrgPrecision ) ,	
						fac.getFac() , fac.getDim() , fac.getOrd() )  , 20, 20) , 6.0 , 0.01 );
		
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> elemBivec = fac.zero();
		elemBivec.setVal(keyScalar, new BigFixedPointElem<LrgPrecision>( 1.0E-10 , lrgPrecision ));
		elemBivec.setVal(keyBivector, fac.getFac().identity());
		
		GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>> extfI = ( ed.extendedBinomialCoefficient( 
				new GeometricAlgebraMultivectorElem<TestDimensionThree, GeometricAlgebraOrd<TestDimensionThree>, BigFixedPointElem<LrgPrecision>, BigFixedPointElemFactory<LrgPrecision>>( new BigFixedPointElem<LrgPrecision>( 4.0 , lrgPrecision ) ,
						fac.getFac() , fac.getDim() , fac.getOrd() )  ,
				elemBivec  , 20, 20) );
		// System.out.println( "vl Re " + ( extfI.getVal(keyScalar).toDouble() ) );
		// System.out.println( "vl Im " + ( extfI.getVal(keyBivector).toDouble() ) );
		
		Assert.assertTrue( extfI != null );
		
	}
	
	

	
	
	
}



