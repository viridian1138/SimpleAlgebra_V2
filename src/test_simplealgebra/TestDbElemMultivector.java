



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
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;



/**
 * Tests basic multivector persistence operations.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestDbElemMultivector extends TestCase {
	
	
	
	
	
	/**
	 * Generates a random 4-D multivector of complex numbers.
	 * 
	 * @return The random multivector of complex numbers.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
		ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		genMvecA()
		{
			final DoubleElemFactory de = new DoubleElemFactory();
			
			final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce =
					new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
			
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
					ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
				ga = new GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
						ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce , 
						new TestDimensionFour() , new GeometricAlgebraOrd<TestDimensionFour>() );
			
			for( int i = 0 ; i < 2 ; i++ )
			{
				HashSet<BigInteger> index = new HashSet<BigInteger>();
				index.add( BigInteger.valueOf( i ) );
				ga.setVal( index , new 
					ComplexElem<DoubleElem,DoubleElemFactory>( 
							new DoubleElem( i * i ) , new DoubleElem( i * i + 1 ) ) );
			}
			
			return( ga );
			
		}
	
	
	/**
	 * Returns a factory for a 4-D multivector of complex numbers.
	 * 
	 * @return The factory.
	 */
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
		ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		genMvecFac()
	{
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
				ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			fac = new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
					ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ce ,
							new TestDimensionFour() , new GeometricAlgebraOrd<TestDimensionFour>() );
		
		return( fac );
	}
	
	
	/**
	 * Test method for multivector persistence.
	 */
	public void testMvecPersistenceA() throws NotInvertibleException
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
				
		graph = new HyperGraph( databaseLocation );
				
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		HGHandle hndl = graph.add( genMvecA() );
		
		HGHandle hndl2 = graph.add( genMvecFac() );
		
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
				ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ga = graph.get( hndl );
		
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
				ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ga2 = graph.get( hndl2 );
		
		
//		Assert.assertTrue( et.getContravariantIndices().size() == 0 );
		
//		Assert.assertTrue( et.getCovariantIndices().size() == 2 );
		
		int cnt = 0;
		for( final Entry<HashSet<BigInteger>, ComplexElem<DoubleElem, DoubleElemFactory>> ii : ga.getEntrySet() )
		{
			Assert.assertTrue( ii.getKey() != null );
			Assert.assertTrue( ii.getValue() != null );
			cnt++;
		}
		
		
		Assert.assertTrue( cnt == 2 );
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
		
	}
	
	
	
	
	
}


