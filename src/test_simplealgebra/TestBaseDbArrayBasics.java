



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

import simplealgebra.DoubleElem;
import simplealgebra.NotInvertibleException;
import simplealgebra.store.BaseDbArray_SingleWrite;


/**
 * Tests use of BaseDbArray_SingleWrite.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestBaseDbArrayBasics extends TestCase {
	
	
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
	 * Tests basic use of BaseDbArray_SingleWrite.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testBaseDbArrayBasics() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		
		BaseDbArray_SingleWrite<Double> db = new BaseDbArray_SingleWrite<Double>( graph );
		
		
		db.insert( genA() , new Double( 1.111111 ) );
		
		db.insert( genB() , new Double( 2.2222222 ) );
		
		db.insert( genC() , new Double( 3.3333333 ) );
		
		db.insert( genC2() , new Double( 3.6333333 ) );
		
		
		
		Assert.assertEquals( 1.111111 , db.query( genA() ) , 1E-5 );
		
		
		Assert.assertEquals( 2.2222222 , db.query( genB() ) , 1E-5 );
		
		
		Assert.assertEquals( 3.3333333 , db.query( genC() ) , 1E-5 );
		
		
		Assert.assertEquals( 3.6333333 , db.query( genC2() ) , 1E-5 );
		

		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	
	

	
}



