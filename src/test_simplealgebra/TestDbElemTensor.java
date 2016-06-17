



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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;



/**
 * Tests basic tensor persistence operations.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestDbElemTensor extends TestCase {
	
	
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
	 * Generates a rank-2 tensor of complex numbers.
	 * 
	 * @return The rank-2 tensor of complex numbers.
	 */
	protected EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		genTensorA()
		{
			final DoubleElemFactory de = new DoubleElemFactory();
			
			final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce =
					new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
			
			final EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
				et = new EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce , 
						indicesEmpty() , indicesUV() );
			
			for( int i = 0 ; i < 5 ; i++ )
			{
				for( int j = 0 ; j < 5 ; j++ )
				{
					ArrayList<BigInteger> index = matUV( i , j );
					et.setVal( index , new 
						ComplexElem<DoubleElem,DoubleElemFactory>( 
								new DoubleElem( i * j + i ) , new DoubleElem( i * j + j ) ) );
				}
			}
			
			return( et );
			
		}
	
	
	
	/**
	 * Generates a factory for a tensor of complex numbers.
	 * 
	 * @return The tensor of complex numbers.
	 */
	EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		genTensorFac()
	{
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			fac = new EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce );
		
		return( fac );
	}
	
	
	/**
	 * Test method for tensor persistence.
	 */
	public void testTensorPersistenceA() throws NotInvertibleException
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
				
		graph = new HyperGraph( databaseLocation );
				
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		HGHandle hndl = graph.add( genTensorA() );
		
		HGHandle hndl2 = graph.add( genTensorFac() );
		
		
		EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			et = graph.get( hndl );
		
		EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			et2 = graph.get( hndl2 );
		
		
		Assert.assertTrue( et.getContravariantIndices().size() == 0 );
		
		Assert.assertTrue( et.getCovariantIndices().size() == 2 );
		
		int cnt = 0;
		for( final Entry<ArrayList<BigInteger>, ComplexElem<DoubleElem, DoubleElemFactory>> ii : et.getEntrySet() )
		{
			Assert.assertTrue( ii.getKey() != null );
			Assert.assertTrue( ii.getValue() != null );
			cnt++;
		}
		
		
		Assert.assertTrue( cnt == 25 );
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
		
	}
	
	
	
	
	
}


