



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

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.store.DbElem;
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
		
		String databaseLocation = "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		
		
		final DbElem<DoubleElem,DoubleElemFactory> da = 
				new DbElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 1.111111 ) , graph);
		
		
		final DbElem<DoubleElem,DoubleElemFactory> db = 
				new DbElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 2.2222222 ) , graph);
		
		
		final DbElem<DoubleElem,DoubleElemFactory> dc = 
				new DbElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 3.3333333 ) , graph);
		
		
		
		
		Assert.assertEquals( da.query().getVal() , 1.111111 , 1E-5 );
		
		Assert.assertEquals( db.query().getVal() , 2.2222222 , 1E-5 );
		
		Assert.assertEquals( dc.query().getVal() , 3.3333333 , 1E-5 );
		
		
		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	


	
}




