



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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;



/**
 * Tests basic tensor operations.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestTensorBasics extends TestCase {
	
	
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
	 * Returns a tensor index list with "v".
	 * 
	 * @return The tensor index list.
	 */
	final static ArrayList<String> indicesV()
	{
		final ArrayList<String> ret = new ArrayList<String>();
		ret.add( "v" );
		return( ret );
	}
	
	
	/**
	 * Returns a tensor index list with "(u,v)".
	 * 
	 * @return The tensor index list.
	 */
	final static ArrayList<String> indicesUV()
	{
		final ArrayList<String> ret = new ArrayList<String>();
		ret.add( "u" );
		ret.add( "v" );
		return( ret );
	}
	
	
	/**
	 * Returns a tensor index list with "(v,u)".
	 * 
	 * @return The tensor index list.
	 */
	final static ArrayList<String> indicesVU()
	{
		final ArrayList<String> ret = new ArrayList<String>();
		ret.add( "v" );
		ret.add( "u" );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for the component of a rank zero tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> scalar( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
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
	 * Returns an index for a rank 2 tensor at (u,v).
	 * 
	 * @param u The u index.
	 * @param v The v index.
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> matUV( int u , int v )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.valueOf( u ) );
		ret.add( BigInteger.valueOf( v ) );
		return( ret );
	}
	
	
	/**
	 * Returns an index for a rank 2 tensor at (v,u).
	 * 
	 * @param u The u index.
	 * @param v The v index.
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> matVU( int u , int v )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.valueOf( v ) );
		ret.add( BigInteger.valueOf( u ) );
		return( ret );
	}
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorMultA() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesU() , indicesEmpty() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		etA.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		etB.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		
		
		etA.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		etB.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		
		etA.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		etB.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		
		etA.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		etB.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		Assert.assertTrue( Math.abs( etA.getVal( vect1() ).getVal() - 5.0 ) < 0.001 );
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.mult( etB );
		
		
		etC.validate();
		
		
		
		int kcnt = 0;
		DoubleElem elem = null;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 0 );
			elem = ii.getValue();
		}
		
		Assert.assertTrue( kcnt == 1 );
		
		
		final double expectedAnswer = 3 * 3 + 5 * 5 + 7 * 7 + 13 * 13;
		
		Assert.assertTrue( Math.abs( elem.getVal() - expectedAnswer ) < 0.001 );
		
		
	}
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorMultB() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesV() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		etA.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		etB.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		
		
		etA.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		etB.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		
		etA.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		etB.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		
		etA.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		etB.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		Assert.assertTrue( Math.abs( etA.getVal( vect1() ).getVal() - 5.0 ) < 0.001 );
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.mult( etB );
		
		
		etC.validate();
		
		
		final int[] tstArr = { 3 , 5 , 7 , 13 };
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 2 );
			final int ind0 = key.get( 0 ).intValue();
			final int ind1 = key.get( 1 ).intValue();
			DoubleElem elem = ii.getValue();
			final double expectedAnswer = tstArr[ ind0 ] * tstArr[ ind1 ];
			Assert.assertTrue( Math.abs( elem.getVal() - expectedAnswer ) < 0.001 );
		}
		
		Assert.assertTrue( kcnt == 16 );
		
		
		
	}
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorMultC() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		etA.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		etB.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		
		
		etA.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		etB.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		
		etA.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		etB.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		
		etA.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		etB.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		Assert.assertTrue( Math.abs( etA.getVal( vect1() ).getVal() - 5.0 ) < 0.001 );
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.mult( etB );
		
		
		
		etC.validate();
		
		
		final int[] tstArr = { 3 , 5 , 7 , 13 };
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 1 );
			final int ind0 = key.get( 0 ).intValue();
			DoubleElem elem = ii.getValue();
			final double expectedAnswer = tstArr[ ind0 ] * tstArr[ ind0 ];
			Assert.assertTrue( Math.abs( elem.getVal() - expectedAnswer ) < 0.001 );
		}
		
		Assert.assertTrue( kcnt == 4 );
		
		
		
	}
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorMultD() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesEmpty() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		etA.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		
		
		
		etA.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		
		
		etA.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		
		
		etA.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		
		
		etB.setVal( scalar() , new DoubleElem( 5.0 ) );
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		Assert.assertTrue( Math.abs( etA.getVal( vect1() ).getVal() - 5.0 ) < 0.001 );
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.mult( etB );
		
		
		
		
		etC.validate();
		
		
		final int[] tstArr = { 3 , 5 , 7 , 13 };
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 1 );
			final int ind0 = key.get( 0 ).intValue();
			DoubleElem elem = ii.getValue();
			final double expectedAnswer = tstArr[ ind0 ] * 5.0;
			Assert.assertTrue( Math.abs( elem.getVal() - expectedAnswer ) < 0.001 );
		}
		
		Assert.assertTrue( kcnt == 4 );
		
		
		
	}
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorMultE() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		etA.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		
		
		
		
		etA.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		
		
		
		etA.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		
		
		
		etA.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		Assert.assertTrue( Math.abs( etA.getVal( vect1() ).getVal() - 5.0 ) < 0.001 );
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.mult( etB );
		
		
		
		etC.validate();
		
		
		final int[] tstArr = { 3 , 5 , 7 , 13 };
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 1 );
			final int ind0 = key.get( 0 ).intValue();
			DoubleElem elem = ii.getValue();
			final double expectedAnswer = tstArr[ ind0 ] * tstArr[ ind0 ];
			Assert.assertTrue( Math.abs( elem.getVal() - expectedAnswer ) < 0.001 );
		}
		
		Assert.assertTrue( kcnt == 0 );
		
		
		
	}
	
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorMultF() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		etB.setVal( vect0() , new DoubleElem( 3.0 ) );
		
		
		
		
		
		etB.setVal( vect1() , new DoubleElem( 5.0 ) );
		
		
		
		
		etB.setVal( vect2() , new DoubleElem( 7.0 ) );
		
		
		
		
		etB.setVal( vect3() , new DoubleElem( 13.0 ) );
		
		
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		// Assert.assertTrue( Math.abs( etA.getVal( vect1() ).getVal() - 5.0 ) < 0.001 );
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.mult( etB );
		
		
		
		etC.validate();
		
		
		final int[] tstArr = { 3 , 5 , 7 , 13 };
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 1 );
			final int ind0 = key.get( 0 ).intValue();
			DoubleElem elem = ii.getValue();
			final double expectedAnswer = tstArr[ ind0 ] * tstArr[ ind0 ];
			Assert.assertTrue( Math.abs( elem.getVal() - expectedAnswer ) < 0.001 );
		}
		
		Assert.assertTrue( kcnt == 0 );
		
		
		
	}
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorAddA() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesUV() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesVU() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		final Random rnd = new Random( 9876 );
		
		
		for( int u = 0 ; u < 4 ; u++ )
		{
			for( int v = 0 ; v < 4 ; v++ )
			{
				etA.setVal( matUV( u , v ) , new DoubleElem( rnd.nextDouble() ) );
				etB.setVal( matVU( u , v ) , new DoubleElem( rnd.nextDouble() ) );
			}
		}
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.add( etB );
		
		
		
		etC.validate();
		
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 2 );
		}
		
		Assert.assertTrue( kcnt == 16 );
		
		
		
		for( int u = 0 ; u < 4 ; u++ )
		{
			for( int v = 0 ; v < 4 ; v++ )
			{
				DoubleElem da = etA.getVal( matUV( u , v ) );
				DoubleElem db = etB.getVal( matVU( u , v ) );
				DoubleElem dc = etC.getVal( matUV( u , v ) );
				Assert.assertTrue( Math.abs( ( da.getVal() + db.getVal() ) - dc.getVal() ) < 0.001 );
			}
		}
		
		
		
	}
	
	
	
	
	/**
	 * Test method for tensor multiplication.
	 */
	public void testTensorAddB() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesUV() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesUV() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		final Random rnd = new Random( 9876 );
		
		
		for( int u = 0 ; u < 4 ; u++ )
		{
			for( int v = 0 ; v < 4 ; v++ )
			{
				etA.setVal( matUV( u , v ) , new DoubleElem( rnd.nextDouble() ) );
				etB.setVal( matUV( u , v ) , new DoubleElem( rnd.nextDouble() ) );
			}
		}
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				etA.add( etB );
		
		
		
		etC.validate();
		
		
		int kcnt = 0;
		for( final Entry<ArrayList<BigInteger>, DoubleElem> ii : etC.getEntrySet() )
		{
			kcnt++;
			ArrayList<BigInteger> key = ii.getKey();
			Assert.assertTrue( key.size() == 2 );
		}
		
		Assert.assertTrue( kcnt == 16 );
		
		
		
		for( int u = 0 ; u < 4 ; u++ )
		{
			for( int v = 0 ; v < 4 ; v++ )
			{
				DoubleElem da = etA.getVal( matUV( u , v ) );
				DoubleElem db = etB.getVal( matUV( u , v ) );
				DoubleElem dc = etC.getVal( matUV( u , v ) );
				Assert.assertTrue( Math.abs( ( da.getVal() + db.getVal() ) - dc.getVal() ) < 0.001 );
			}
		}
		
		
		
	}
	
	
	
	
}


