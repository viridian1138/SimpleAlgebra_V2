



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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HyperGraph;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.store.DbElem;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;


/**
 * Tests use of DbElem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDbElemDbl extends TestCase {
	

	
	
	/**
	 * Tests basic use of DbElem
	 * 
	 * @throws Throwable
	 */
	public void testDbElemDbl( ) throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		
		
		final DbElem<DoubleElem,DoubleElemFactory> da = 
				new DbElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 1.111111 ) , graph);
		
		
		final DbElem<DoubleElem,DoubleElemFactory> db = 
				new DbElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 2.2222222 ) , graph);
		
		
		final DbElem<DoubleElem,DoubleElemFactory> dc = 
				new DbElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 3.3333333 ) , graph);
		
		
		
		
		Assert.assertEquals( da.query().getVal() , 1.111111 , 1E-5 );
		
		Assert.assertEquals( db.query().getVal() , 2.2222222 , 1E-5 );
		
		Assert.assertEquals( dc.query().getVal() , 3.3333333 , 1E-5 );
		
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	
	
	
	
	/**
	 * Tests basic use of DbElem
	 * 
	 * @throws Throwable
	 */
	public void testDbElemCplx( ) throws Throwable
	{
		// System.out.println( "Started..." ); 
		
		String databaseLocation = DatabasePathForTest.DATABASE_PATH + "mydb";
		HyperGraph graph;
				
		graph = new HyperGraph( databaseLocation );
				
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
				
				
				
				
		final DbElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> da = 
				new DbElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( 
						new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 1.111111 ) , new DoubleElem( 2.222222 ) ) , graph);
				
				
		final DbElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> db = 
				new DbElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( 
						new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 3.3333333 ) , new DoubleElem( 4.44444444 ) ) , graph);
				
				
		final DbElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> dc = 
				new DbElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( 
						new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 5.5555555 ) , new DoubleElem( 6.6666666 ) ) , graph);
				
				
				
				
		Assert.assertEquals( da.query().getRe().getVal() , 1.111111 , 1E-5 );
				
		Assert.assertEquals( da.query().getIm().getVal() , 2.222222 , 1E-5 );
				
		Assert.assertEquals( db.query().getRe().getVal() , 3.3333333 , 1E-5 );
		
		Assert.assertEquals( db.query().getIm().getVal() , 4.44444444 , 1E-5 );
		
		Assert.assertEquals( dc.query().getRe().getVal() , 5.5555555 , 1E-5 );
		
		Assert.assertEquals( dc.query().getIm().getVal() , 6.6666666 , 1E-5 );
				
				
			
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
				
		// System.out.println( "Done..." ); 
		
	}


	
}




