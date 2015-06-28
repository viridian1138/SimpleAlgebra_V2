



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
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.SymbolicTensorResym;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



/**
 * Tests basic persistence of SymbolicTensorResym.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestDbElemSymbolicTensorResym extends TestCase {
	
	
	
	
	
	/**
	 * Generates a test expression that contains a SymbolicTensorResym.
	 * 
	 * @return The test expression.
	 */
	protected SymbolicTensorResym<String, TestDimensionFour, DoubleElem, DoubleElemFactory> genSymA()
	{
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> ee
			= new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> se =
				new SymbolicElemFactory<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>>( ee );
		
		final SymbolicElem<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>,EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>> elem =
				se.identity().negate();
		
		final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> fac = ee;
		
		final SymbolicTensorResym.ResymType reSym = SymbolicTensorResym.ResymType.RESYM_ANTISYMMETRIC;
		
		final TestDimensionFour dim = new TestDimensionFour();
		
		final SymbolicTensorResym<String, TestDimensionFour, DoubleElem, DoubleElemFactory> ret =
				new SymbolicTensorResym<String, TestDimensionFour, DoubleElem, DoubleElemFactory>( elem , fac , reSym , dim );
		
		return( ret );
	}
	
	
	
	/**
	 * Test method for symbolic persistence.
	 */
	public void testSymPersistenceA() throws NotInvertibleException
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = "mydb";
		HyperGraph graph;
				
		graph = new HyperGraph( databaseLocation );
				
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		HGHandle hndl = graph.add( genSymA() );
		
		// HGHandle hndl2 = graph.add( genSymFac() );
		
		
		SymbolicTensorResym<String, TestDimensionFour, DoubleElem, DoubleElemFactory>
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


