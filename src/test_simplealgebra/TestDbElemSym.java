



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

import junit.framework.TestCase;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicIdentity;



/**
 * Tests basic symbolic persistence operations.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestDbElemSym extends TestCase {
	
	
	
	
	
	/**
	 * Generates a test symbolic expression to evaluate persistence/retrieval.
	 * 
	 * @return The test symbolic expression.
	 */
	protected SymbolicElem<DoubleElem,DoubleElemFactory> genSymA()
	{
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final SymbolicIdentity<DoubleElem,DoubleElemFactory> ident = new SymbolicIdentity<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> aident = ident.add( ident );
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> nident = aident.negate();
		
		return( nident );
	}
	
	
	
	/**
	 * Test method for symbolic persistence.
	 */
	public void testSymPersistenceA() throws NotInvertibleException
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
				
		graph = new HyperGraph( databaseLocation );
				
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		HGHandle hndl = graph.add( genSymA() );
		
		// HGHandle hndl2 = graph.add( genSymFac() );
		
		
		SymbolicElem<DoubleElem,DoubleElemFactory>
			sym = graph.get( hndl );
		
//		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,
//				ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
//			ga2 = graph.get( hndl2 );
		
		
//		Assert.assertTrue( et.getContravariantIndices().size() == 0 );
		
//		Assert.assertTrue( et.getCovariantIndices().size() == 2 );
		
//		int cnt = 0;
//		Iterator<HashSet<BigInteger>> it = ga.getKeyIterator();
//		while( it.hasNext() )
//		{
//			HashSet<BigInteger> key = it.next();
//			Assert.assertTrue( ga.getVal( key ) != null );
//			cnt++;
//		}
//		
//		
//		Assert.assertTrue( cnt == 2 );
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
		
	}
	
	
	
	
	
}


