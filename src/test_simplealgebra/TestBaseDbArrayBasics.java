



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

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.store.BaseDbArray_SingleWrite;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.BigFixedPointElemFactory;
import simplealgebra.bigfixedpoint.Precision;


/**  
 * Tests use of BaseDbArray_SingleWrite.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
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
	 * @throws Throwable
	 */
	public void testBaseDbArrayBasics() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydbE";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		BaseDbArray_SingleWrite<Double> db = new BaseDbArray_SingleWrite<Double>( graph );
		
		
		db.insert( genA() , new Double( 1.111111 ) );
		
		db.insert( genB() , new Double( 2.2222222 ) );
		
		db.insert( genC() , new Double( 3.3333333 ) );
		
		db.insert( genC2() , new Double( 3.6333333 ) );
		
		
		
		Assert.assertEquals( 1.111111 , db.query( genA() ) , 1E-5 );
		
		
		Assert.assertEquals( 2.2222222 , db.query( genB() ) , 1E-5 );
		
		
		Assert.assertEquals( 3.3333333 , db.query( genC() ) , 1E-5 );
		
		
		Assert.assertEquals( 3.6333333 , db.query( genC2() ) , 1E-5 );
		
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );

		
		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	
	
	
	
	/**
	 * Tests basic use of BaseDbArray_SingleWrite.
	 * 
	 * @throws Throwable
	 */
	public void testBaseDbArrayBasicsEl() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		
		BaseDbArray_SingleWrite<DoubleElem> db = new BaseDbArray_SingleWrite<DoubleElem>( graph );
		
		
		db.insert( genA() , new DoubleElem( 1.111111 ) );
		
		db.insert( genB() , new DoubleElem( 2.2222222 ) );
		
		db.insert( genC() , new DoubleElem( 3.3333333 ) );
		
		db.insert( genC2() , new DoubleElem( 3.6333333 ) );
		
		
		
		Assert.assertEquals( 1.111111 , db.query( genA() ).getVal() , 1E-5 );
		
		
		Assert.assertEquals( 2.2222222 , db.query( genB() ).getVal() , 1E-5 );
		
		
		Assert.assertEquals( 3.3333333 , db.query( genC() ).getVal() , 1E-5 );
		
		
		Assert.assertEquals( 3.6333333 , db.query( genC2() ).getVal() , 1E-5 );
		

		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	
	
	
	/**
	 * Tests the ability to store a DoubleElemFactory.
	 * 
	 * @throws Throwable
	 */
	public void testStoreDoubleElemFactory() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		
		HGHandle hndl = graph.add( new DoubleElemFactory() );
		
		
		DoubleElemFactory d = graph.get( hndl );
		
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
	
	}
	
	
	/**
	 * Returns the number <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>151</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @return The value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>151</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	public static BigInteger genPrecVal()
	{
		final BigInteger ten = BigInteger.valueOf( 10 );
		BigInteger vl = ten;
		for( int count = 0 ; count < 150 ; count++ )
		{
			vl = vl.multiply( ten );
		}
		return( vl );
	}
	
	/**
	 * Constant containing the value <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>151</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	public static final BigInteger lrgPrec = genPrecVal();
	
	/**
	 * Constant containing the square of lrgPrec, or <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>302</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 */
	public static final BigInteger lrgPrecSq = lrgPrec.multiply( lrgPrec );
	
	
	/**
	 * Defines a precision of lrgPrec, or one part in <math display="inline">
     * <mrow>
     *   <msup>
     *           <mn>10</mn>
     *         <mn>151</mn>
     *   </msup>
     * </mrow>
     * </math>.
	 * 
	 * @author thorngreen
	 *
	 */
	public static class LrgPrec extends Precision<LrgPrec>
	{

		@Override
		public BigInteger getVal() {
			return( lrgPrec );
		}

		@Override
		public BigInteger getValSquared() {
			return( lrgPrecSq );
		}
		
	}
	
	
	
	/**
	 * Tests the ability to store a tensor of complex numbers.
	 * 
	 * @throws Throwable
	 */
	public void testStoreBasicTypes() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		
		{
			HGHandle hndl = graph.add( new ComplexElemFactory<DoubleElem,DoubleElemFactory>( new DoubleElemFactory() ) );
		
			ComplexElemFactory<DoubleElem,DoubleElemFactory> d = graph.get( hndl );
		
			Assert.assertTrue( d.getFac() != null );
		
			Assert.assertTrue( d.getFac() instanceof DoubleElemFactory );
		}
		
		
		
		{
			HGHandle hndl = graph.add( new BigFixedPointElem<LrgPrec>( 
					lrgPrec.multiply( BigInteger.valueOf( 2 ) ) , new LrgPrec() ) );
			
			BigFixedPointElem<LrgPrec> d = graph.get( hndl );
			
			Assert.assertTrue( d.getPrecVal() != null );
			
			Assert.assertTrue( d.getFac().getPrec() != null );
			
			Assert.assertTrue( d.getFac().getPrec() instanceof LrgPrec );
			
			Assert.assertTrue( d.getPrecVal().equals( lrgPrec.multiply( BigInteger.valueOf( 2 ) ) ) );
			
		}
		
		
		
		{
			HGHandle hndl = graph.add( new BigFixedPointElemFactory<LrgPrec>( 
					new LrgPrec() ) );
			
			BigFixedPointElemFactory<LrgPrec> d = graph.get( hndl );
			
			Assert.assertTrue( d.getPrec() != null );
			
			Assert.assertTrue( d.getPrec() instanceof LrgPrec );
			
		}
		
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
	
	}

	
}



