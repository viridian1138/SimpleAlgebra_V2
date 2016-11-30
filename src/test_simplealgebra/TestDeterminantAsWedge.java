



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
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;


/**
 * Tests the ability to compute a matrix determinant as a series of wedge
 * products.  See Chapter 5, Equation 1.34 of "New Foundations for Classical Mechanics"
 * by David Hestenes.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDeterminantAsWedge extends TestCase {
	
	
	
	/**
	 * The number of dimensions in the test matrix.
	 */
	static final int NINE_INT = 9;
	
	/**
	 * The number of dimensions in the test matrix.
	 */
	static final BigInteger NINE = BigInteger.valueOf( NINE_INT );
	
	
	
	/**
	 * Test class for nine-dimensional problems.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TestDimensionNine extends NumDimensions {

		/**
		 * Constructs the NumDimensions.
		 */
		public TestDimensionNine() {
			// Does Nothing
		}

		@Override
		public BigInteger getVal() {
			return( NINE );
		}

	}
	
	
	
	
	/**
	 * Tests the ability to compute a matrix determinant as a series of wedge
     * products.  See Chapter 5, Equation 1.34 of "New Foundations for Classical Mechanics"
     * by David Hestenes.
	 */
	public void testDeterminantAsWedge( ) throws NotInvertibleException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionNine td = new TestDimensionNine();
		
		final GeometricAlgebraOrd<TestDimensionNine> ord = new GeometricAlgebraOrd<TestDimensionNine>();
		
		final SquareMatrixElem<TestDimensionNine,DoubleElem,DoubleElemFactory> el
			= new SquareMatrixElem<TestDimensionNine,DoubleElem,DoubleElemFactory>( fac , td );
		
		final Random rand = new Random( 66554433 );
		
		
		for( BigInteger i = BigInteger.ZERO ; i.compareTo( NINE ) < 0 ; i = i.add( BigInteger.ONE ) )
		{
			for( BigInteger j = BigInteger.ZERO ; j.compareTo( NINE ) < 0 ; j = j.add( BigInteger.ONE ) )
			{
				el.setVal( i , j , new DoubleElem( rand.nextDouble() ) );
			}
		}
		
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> tempTensor =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>(fac, contravariantIndices, covariantIndices);
		
		
		el.toRankTwoTensor( tempTensor );
		
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionNine,GeometricAlgebraOrd<TestDimensionNine>,DoubleElem,DoubleElemFactory>> list = 
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionNine,GeometricAlgebraOrd<TestDimensionNine>,DoubleElem,DoubleElemFactory>>( NINE_INT );
		
		
		
		for( int i = 0 ; i < NINE_INT ; i++ )
		{
			list.add( new GeometricAlgebraMultivectorElem<TestDimensionNine,GeometricAlgebraOrd<TestDimensionNine>,DoubleElem,DoubleElemFactory>(
					fac, td, ord ) );
		}
		
		
		for( Entry<ArrayList<BigInteger>,DoubleElem> ent : tempTensor.getEntrySet() )
		{
			final ArrayList<BigInteger> key = ent.getKey();
			final GeometricAlgebraMultivectorElem<TestDimensionNine,GeometricAlgebraOrd<TestDimensionNine>,DoubleElem,DoubleElemFactory> elA =
					list.get( key.get( 0 ).intValue() );
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( key.get( 1 ) );
			elA.setVal( hs , ent.getValue() );
		}
		
		
		final DoubleElem det = el.determinant();
		
		
		GeometricAlgebraMultivectorElem<TestDimensionNine,GeometricAlgebraOrd<TestDimensionNine>,DoubleElem,DoubleElemFactory> detB =
				list.get( 0 );
		
	
		for( BigInteger i = BigInteger.ONE ; i.compareTo( NINE ) < 0 ; i = i.add( BigInteger.ONE )  )
		{
			final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionNine, GeometricAlgebraOrd<TestDimensionNine>, DoubleElem, DoubleElemFactory>> args = 
				new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionNine, GeometricAlgebraOrd<TestDimensionNine>, DoubleElem, DoubleElemFactory>>();
			args.add( list.get( i.intValue() ) );
			detB = detB.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
		
		int cnt = 0;
		
		for( DoubleElem detB2 : detB.getValueSet() )
		{
			Assert.assertEquals( det.getVal() , detB2.getVal() , 1E-6 );
			cnt++;
		}
		
		Assert.assertTrue( cnt == 1 );

	
		
	}


	
	
	
	
}



