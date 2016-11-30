



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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;



/**
 * Tests counterexample showing that a multivector of tensors is not associative
 * (although a multivector of doubles would be associative).  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestNestedNotAssociative extends TestCase {
	
	
	/**
	 * Returns an empty tensor index list.
	 * 
	 * @return An empty tensor index list.
	 */
	final static ArrayList<String> indicesEmpty()
	{
		return( new ArrayList<String>() );
	}
	
	
	/**
	 * Returns a tensor index list with "u".
	 * 
	 * @return The tensor index list.
	 */
	final static ArrayList<String> indicesU()
	{
		final ArrayList<String> ret = new ArrayList<String>();
		ret.add( "u" );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for component zero of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect0( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.ZERO );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for component zero of the vector portion of a multivector.
	 * 
	 * @return The multivector index.
	 */
	final static HashSet<BigInteger> vect0G( )
	{
		final HashSet<BigInteger> ret = new HashSet<BigInteger>();
		ret.add( BigInteger.ZERO );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for component 1 of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect1( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.ONE );
		return( ret );
	}
	
	
	/**
	 * Returns an index for component 1 of the vector portion of a multivector.
	 * 
	 * @return The multivector index.
	 */
	final static HashSet<BigInteger> vect1G( )
	{
		final HashSet<BigInteger> ret = new HashSet<BigInteger>();
		ret.add( BigInteger.ONE );
		return( ret );
	}
	
	
	/**
	 * Returns an index for component 2 of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect2( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.valueOf( 2 ) );
		return( ret );
	}
	
	
	/**
	 * Returns an index for component 2 of the vector portion of a multivector.
	 * 
	 * @return The multivector index.
	 */
	final static HashSet<BigInteger> vect2G( )
	{
		final HashSet<BigInteger> ret = new HashSet<BigInteger>();
		ret.add( BigInteger.valueOf( 2 ) );
		return( ret );
	}
	
	
	/**
	 * Returns an index for component 3 of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect3( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.valueOf( 3 ) );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for component 3 of the vector portion of a multivector.
	 * 
	 * @return The multivector index.
	 */
	final static HashSet<BigInteger> vect3G( )
	{
		final HashSet<BigInteger> ret = new HashSet<BigInteger>();
		ret.add( BigInteger.valueOf( 3 ) );
		return( ret );
	}
	
	
	
	/**
	 * Initializes a tensor to random rank one components.
	 * 
	 * @param rand The random number generator.
	 * @param etA The tensor to be initialized.
	 */
	final static void initTensor( Random rand , EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA )
	{
		etA.setVal( vect0() , new DoubleElem( rand.nextDouble() ) );
		
		etA.setVal( vect1() , new DoubleElem( rand.nextDouble() ) );
		
		etA.setVal( vect2() , new DoubleElem( rand.nextDouble() ) );
		
		etA.setVal( vect3() , new DoubleElem( rand.nextDouble() ) );
	}
	
	
	
	/**
	 * Initializes a multivector of covariant rank one tensors to random components.
	 * 
	 * @param rand The random number generator.
	 * @param de Factory for doubles.
	 * @param etA The multivector to be initialized.
	 */
	final static void initGaA( Random rand , final DoubleElemFactory de ,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> etA )
	{
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et0 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et1 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et2 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et3 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		initTensor( rand , et0 );
		
		initTensor( rand , et1 );
		
		initTensor( rand , et2 );
		
		initTensor( rand , et3 );
		
		etA.setVal( vect0G() , et0 );
		
		etA.setVal( vect1G() , et1 );
		
		etA.setVal( vect2G() , et2 );
		
		etA.setVal( vect3G() , et3 );
	}
	
	
	
	/**
	 * Initializes a multivector of contravariant rank one tensors to random components.
	 * 
	 * @param rand The random number generator.
	 * @param de Factory for doubles.
	 * @param etA The multivector to be initialized.
	 */
	final static void initGaB( Random rand , final DoubleElemFactory de ,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> etA )
	{
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et0 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesU() , indicesEmpty() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et1 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesU() , indicesEmpty() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et2 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesU() , indicesEmpty() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et3 =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesU() , indicesEmpty() );
		
		initTensor( rand , et0 );
		
		initTensor( rand , et1 );
		
		initTensor( rand , et2 );
		
		initTensor( rand , et3 );
		
		etA.setVal( vect0G() , et0 );
		
		etA.setVal( vect1G() , et1 );
		
		etA.setVal( vect2G() , et2 );
		
		etA.setVal( vect3G() , et3 );
	}
	
	
	
	
	/**
	 * Tests counterexample showing that a multivector of tensors is not associative
     * (although a multivector of doubles would be associative).
	 */
	public void testAssociativeMultA() throws NotInvertibleException
	{
		
		final TestDimensionFour td4 = new TestDimensionFour();
		
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>( );
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
			gaA = new GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
					EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
					EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>(etA, td4, ord);
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
			gaB = new GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
				EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
				EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>(etA, td4, ord);
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>
			gaC = new GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
				EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,
				EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>(etA, td4, ord);
		
		
		
		
		
		gaA.validate();
		
		
		gaB.validate();
		
		
		gaC.validate();
		
		
		
		
		final Random rand = new Random( 87654 );
		
		
		
		
		
		initGaA( rand , de , gaA );
		
		
		initGaB( rand , de , gaB );
		
		
		initGaA( rand , de , gaC );
		
		
		
		
		gaA.validate();
		
		
		gaB.validate();
		
		
		gaC.validate();
		
		
		
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> gaAB =
				gaA.mult( gaB );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> gaBC =
				gaB.mult( gaC );
		
		
		
		
		gaAB.validate();
		
		gaBC.validate();
		
		
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> gaAB_C =
				gaAB.mult( gaC );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
			EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> gaA_BC =
				gaA.mult( gaBC );
		
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et0p = gaAB_C.getVal( vect1G() );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> et1p = gaA_BC.getVal( vect1G() );
		
		
		
		final double d0 = ( et0p.getVal( vect1() ) ).getVal();
		
		final double d1 = ( et1p.getVal( vect1() ) ).getVal();
		
		
		
		// System.out.println( d0 );
		
		// System.out.println( d1 );
		
		// The fact that d0 and d1 are different demonstrates that the product is not associative.
		Assert.assertTrue( Math.abs( d0 - d1 ) > 1E-5 );
		
		Assert.assertTrue( !( gaA.getFac().isMultAssociative() ) );
		
	}
	
	
	
	
	
}


