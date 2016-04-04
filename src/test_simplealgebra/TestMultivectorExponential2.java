package test_simplealgebra;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.prec.DefaultPrecedenceComparator;

/**
 * Tests exponentials across a five-dimensional complex space.
 * 
 * @author tgreen
 *
 */
public class TestMultivectorExponential2 extends TestCase {
	
	
	/**
	 * Tests an exponential across a five-dimensional complex space.
	 * 
	 * @author tgreen
	 *
	 */
	public void testMultivectorExponential2()
	{
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> elA
			= new GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionFive>() );
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			elA.setVal( hs , new DoubleElem( 3.2 ) );
		}
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			elA.setVal( hs , new DoubleElem( 1.1 ) );
		}
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> elB
			= new GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionFive>() );
	
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			elB.setVal( hs , new DoubleElem( 1.1 ) );
		}
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.valueOf( 2 ) );
			elB.setVal( hs , new DoubleElem( 4.4 ) );
		}
		
		
		final ComplexElem<GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>> el
			= new ComplexElem<GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>>( elA , elB );
		
		
		final ComplexElem<GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>> res
			= el.exp( 25 );
		
		// res.writeMathML( new DefaultPrecedenceComparator() , System.out );
		
		Assert.assertTrue( res != null );
		
	}
	

}

