



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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HyperGraph;

import simplealgebra.NotInvertibleException;
import simplealgebra.store.BaseDbArray_SingleWrite;


/**
 * Verifies that separate instances of BaseDbArray_SingleWrite remain independent.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestBaseDbArrayIndependence extends TestCase {
	
	
	/**
	 * Generates a 3-D index for use with the test.
	 * 
	 * @return The 3-D index.
	 */
	protected static ArrayList<BigInteger> genA()
	{
		BigInteger biaa = BigInteger.valueOf( 1025 );
		BigInteger biab = BigInteger.valueOf( 1037 );
		BigInteger biac = BigInteger.valueOf( 1064 );
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( biaa );
		ret.add( biab );
		ret.add( biac );
		
		return( ret );
	}
	
	
	/**
	 * Generates a 3-D index for use with the test.
	 * 
	 * @return The 3-D index.
	 */
	protected static ArrayList<BigInteger> genB()
	{
		BigInteger biaa = BigInteger.valueOf( 2125 );
		BigInteger biab = BigInteger.valueOf( 2137 );
		BigInteger biac = BigInteger.valueOf( 2164 );
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( biaa );
		ret.add( biab );
		ret.add( biac );
		
		return( ret );
	}
	
	
	/**
	 * Generates a 3-D index for use with the test.
	 * 
	 * @return The 3-D index.
	 */
	protected static ArrayList<BigInteger> genC()
	{
		BigInteger biaa = BigInteger.valueOf( 3225 );
		BigInteger biab = BigInteger.valueOf( 3237 );
		BigInteger biac = BigInteger.valueOf( 3264 );
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( biaa );
		ret.add( biab );
		ret.add( biac );
		
		return( ret );
	}
	
	
	
	/**
	 * Generates a 3-D index for use with the test.
	 * 
	 * @return The 3-D index.
	 */
	protected static ArrayList<BigInteger> genC2()
	{
		BigInteger biaa = BigInteger.valueOf( 3264 );
		BigInteger biab = BigInteger.valueOf( 3237 );
		BigInteger biac = BigInteger.valueOf( 3225 );
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( biaa );
		ret.add( biab );
		ret.add( biac );
		
		return( ret );
	}
	
	
	
	/**
	 * Generates a 3-D index for use with the test.
	 * 
	 * @return The 3-D index.
	 */
	protected static ArrayList<BigInteger> genD()
	{
		BigInteger biaa = BigInteger.valueOf( 1111 );
		BigInteger biab = BigInteger.valueOf( 2222 );
		BigInteger biac = BigInteger.valueOf( 3333 );
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( biaa );
		ret.add( biab );
		ret.add( biac );
		
		return( ret );
	}
	
	
	
	/**
	 * Verifies that separate instances of BaseDbArray_SingleWrite remain independent.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testBaseDbArrayIndependence() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		
		BaseDbArray_SingleWrite<Double> dbA = new BaseDbArray_SingleWrite<Double>( graph );
		
		BaseDbArray_SingleWrite<Double> dbB = new BaseDbArray_SingleWrite<Double>( graph );
		
		
		dbA.insert( genA() , new Double( 1.111111 ) );
		
		dbA.insert( genB() , new Double( 2.2222222 ) );
		
		dbA.insert( genC() , new Double( 3.3333333 ) );
		
		dbA.insert( genC2() , new Double( 4.444444444 ) );
		
		
		
		dbB.insert( genA() , new Double( 5.555555 ) );
		
		dbB.insert( genB() , new Double( 6.666666666 ) );
		
		dbB.insert( genC() , new Double( 7.77777777 ) );
		
		dbB.insert( genC2() , new Double( 8.888888888 ) );
		
		
		
		
		
		Assert.assertEquals( 1.111111 , dbA.query( genA() ) , 1E-5 );
		
		
		Assert.assertEquals( 2.2222222 , dbA.query( genB() ) , 1E-5 );
		
		
		Assert.assertEquals( 3.3333333 , dbA.query( genC() ) , 1E-5 );
		
		
		Assert.assertEquals( 4.444444444 , dbA.query( genC2() ) , 1E-5 );
		
		
		
		
		
		Assert.assertEquals( 5.555555 , dbB.query( genA() ) , 1E-5 );
		
		
		Assert.assertEquals( 6.666666666 , dbB.query( genB() ) , 1E-5 );
		
		
		Assert.assertEquals( 7.77777777 , dbB.query( genC() ) , 1E-5 );
		
		
		Assert.assertEquals( 8.888888888 , dbB.query( genC2() ) , 1E-5 );
		
		
		
		

		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	
	

	
}



