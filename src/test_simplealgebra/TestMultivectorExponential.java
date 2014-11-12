



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
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.*;

/**
 * Tests for the Geometric Algebra multivector exponential <math display="inline">
 * <mrow>
 *  <msup>
 *          <mo>e</mo>
 *        <mi>x</mi>
 *  </msup>
 * </mrow>
 * </math>
 * 
 * 
 * Test cases are adapted from "Valence Band of Cubic Semiconductors from Viewpoint of Clifford Algebra"
 * by A. Dargys, Acta Physica Polonica A, Vol. 116 (2009).
 * 
 * (http://przyrbwn.icm.edu.pl/app/pdf/116/a116z219.pdf)
 * 
 * 
 * @author thorngreen
 *
 */
public class TestMultivectorExponential extends TestCase {
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMultivectorExponential() throws NotInvertibleException
	{
		seedTestMultivectorExponential( 1111 );
		seedTestMultivectorExponential( 2222 );
		seedTestMultivectorExponential( 3333 );
		seedTestMultivectorExponential( 4444 );
		seedTestMultivectorExponential( 5555 );
		seedTestMultivectorExponential( 6666 );
		seedTestMultivectorExponential( 7777 );
		seedTestMultivectorExponential( 8888 );
		seedTestMultivectorExponential( 9999 );
		seedTestMultivectorExponential( 11111 );
		seedTestMultivectorExponential( 22222 );
		seedTestMultivectorExponential( 33333 );
		seedTestMultivectorExponential( 44444 );
		seedTestMultivectorExponential( 55555 );
		seedTestMultivectorExponential( 66666 );
		seedTestMultivectorExponential( 77777 );
		seedTestMultivectorExponential( 88888 );
		seedTestMultivectorExponential( 99999 );
	}
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	public void testMultivectorExponentialBivec() throws NotInvertibleException
	{
		seedTestMultivectorExponentialBivec( 1111 );
		seedTestMultivectorExponentialBivec( 2222 );
		seedTestMultivectorExponentialBivec( 3333 );
		seedTestMultivectorExponentialBivec( 4444 );
		seedTestMultivectorExponentialBivec( 5555 );
		seedTestMultivectorExponentialBivec( 6666 );
		seedTestMultivectorExponentialBivec( 7777 );
		seedTestMultivectorExponentialBivec( 8888 );
		seedTestMultivectorExponentialBivec( 9999 );
		seedTestMultivectorExponentialBivec( 11111 );
		seedTestMultivectorExponentialBivec( 22222 );
		seedTestMultivectorExponentialBivec( 33333 );
		seedTestMultivectorExponentialBivec( 44444 );
		seedTestMultivectorExponentialBivec( 55555 );
		seedTestMultivectorExponentialBivec( 66666 );
		seedTestMultivectorExponentialBivec( 77777 );
		seedTestMultivectorExponentialBivec( 88888 );
		seedTestMultivectorExponentialBivec( 99999 );
	}
	
	
	private double sinh( double x )
	{
		return( ( Math.exp( x ) - Math.exp( -x ) ) / 2.0 );
	}
	
	
	private double cosh( double x )
	{
		return( ( Math.exp( x ) + Math.exp( -x ) ) / 2.0 );
	}
	
	private double vmagSq( double[] v )
	{
		double magSq = 0.0;
		int cnt;
		for( cnt = 0 ; cnt < 5 ; cnt++ )
		{
			magSq += v[ cnt ] * v[ cnt ];
		}
		return( magSq );
	}

	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	private void seedTestMultivectorExponential( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> el
			= new GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionFive>() );
		
		final double s = 20.0 * ( rand.nextDouble() - 0.5 );
		
		el.setVal( new HashSet<BigInteger>() , new DoubleElem( s ) );
		
		final double[] v = new double[ 5 ];
		
		int cnt;
		for( cnt = 0 ; cnt < 5 ; cnt++ )
		{
			v[ cnt ] = 20.0 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			h.add( BigInteger.valueOf( cnt ) );
			el.setVal( h , new DoubleElem( v[ cnt ] ) );
		}
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> exp = el.exp( 25 );
		
		
		final double expS = Math.exp( s );
		final double vmag = Math.sqrt( vmagSq( v ) );
		final double sinhVmag = sinh( vmag );
		final double coshVmag = cosh( vmag );
		
		
		
		Assert.assertEquals( expS * sinhVmag , exp.get( new HashSet<BigInteger>() ).getVal() , 1E+1 );
		
		for( cnt = 0 ; cnt < 5 ; cnt++ )
		{
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			h.add( BigInteger.valueOf( cnt ) );
			final double ival = v[ cnt ];
			Assert.assertEquals( expS * ival * coshVmag / vmag , exp.get( h ).getVal() , 1E+1 );
		}
		
		
		Iterator<HashSet<BigInteger>> it = exp.getKeyIterator();
		
		while( it.hasNext() )
		{
			HashSet<BigInteger> h = it.next();
			if( ( h.size() != 0 ) && ( h.size() != 1 ) )
			{
				Assert.assertEquals( 0.0 , exp.get( h ).getVal() , 1E+1 );
			}
		}
		
		
	}
	
	
	
	
	/**
	 * Test method for {@link simplealgebra.Elem#exp()}.
	 */
	private void seedTestMultivectorExponentialBivec( long seed ) throws NotInvertibleException {
		
		final Random rand = new Random();
		
		rand.setSeed( seed );
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> el
			= new GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionFive>() );
		
		final double s = 20.0 * ( rand.nextDouble() - 0.5 );
		
		el.setVal( new HashSet<BigInteger>() , new DoubleElem( s ) );
		
		final double[] v = new double[ 5 ];
		
		int cnt;
		for( cnt = 0 ; cnt < 5 ; cnt++ )
		{
			v[ cnt ] = 20.0 * ( rand.nextDouble() - 0.5 );
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			int cntx;
			for( cntx = 0 ; cntx < 5 ; cntx++ )
			{
				if( cntx != cnt )
				{
					h.add( BigInteger.valueOf( cntx ) );
				}
			}
			el.setVal( h , new DoubleElem( v[ cnt ] ) );
		}
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> exp = el.exp( 25 );
		
		
		final double expS = Math.exp( s );
		final double vmag = Math.sqrt( vmagSq( v ) );
		final double sinhVmag = sinh( vmag );
		final double coshVmag = cosh( vmag );
		
		
		
		Assert.assertEquals( expS * sinhVmag , exp.get( new HashSet<BigInteger>() ).getVal() , 1E+1 );
		
		for( cnt = 0 ; cnt < 5 ; cnt++ )
		{
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			int cntx;
			for( cntx = 0 ; cntx < 5 ; cntx++ )
			{
				if( cntx != cnt )
				{
					h.add( BigInteger.valueOf( cntx ) );
				}
			}
			final double ival = v[ cnt ];
			Assert.assertEquals( expS * ival * coshVmag / vmag , exp.get( h ).getVal() , 1E+1 );
		}
		
		
		Iterator<HashSet<BigInteger>> it = exp.getKeyIterator();
		
		while( it.hasNext() )
		{
			HashSet<BigInteger> h = it.next();
			if( ( h.size() != 0 ) && ( h.size() != 4 ) )
			{
				Assert.assertEquals( 0.0 , exp.get( h ).getVal() , 1E+1 );
			}
		}
		
		
	}
	
	
	
}


