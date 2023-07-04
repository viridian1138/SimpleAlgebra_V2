





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





package test_simplealgebra.topo_sort_test;


import java.util.ArrayList;
import java.util.Random;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;
import org.mvel2.optimizers.OptimizerFactory;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.symbolic.DroolsSession;



/**
 * Verifies that Drools ( <A href="http://drools.org">http://drools.org</A> ) constructs a topological sort algorithm from a set of heuristics.
 * Note: this is only intended to be notional, and one should use something different when efficient execution is desired.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestTopoSort extends TestCase {


	
	/**
	 * Verifies that Drools ( <A href="http://drools.org">http://drools.org</A> ) constructs a topological sort algorithm from a set of heuristics.
	 * Note: this is only intended to be notional, and one should use something different when efficient execution is desired.
	 */
	public void testSort()
	{
		
		OptimizerFactory.setDefaultOptimizer( OptimizerFactory.SAFE_REFLECTIVE );


		KieServices kieServices = KieServices.Factory.get();
	    KieFileSystem kfs = kieServices.newKieFileSystem();
	    
	    kfs.write( "src/main/resources/sort.drl",
	    		ResourceFactory.newClassPathResource( "test_simplealgebra/topo_sort_test/topo_sort.drl" )  );
		

		KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
	    Results results = kieBuilder.getResults();
	    if( results.hasMessages( Message.Level.ERROR ) )
	    {
	        throw new RuntimeException( results.getMessages().toString() );
	    }
	    
	    KieContainer kieContainer =
	        kieServices.newKieContainer( kieServices.getRepository().getDefaultReleaseId() );
	    KieBase kieBase = kieContainer.getKieBase();
	    
	    KieSession session = kieContainer.newKieSession();
		
		session.insert( new DroolsSession( session ) );
		
		final Random rand = new Random( 65432 );
		
		final int NODE_LST_SZ = 100;
		
		final int NODE_SWAP_SZ = 1000;
		
		final int NODE_LST_OFFSET = 20;
		
		final int NODE_LST_DEP = 10;
		
		final int MAX_DEP_SZ = ( NODE_LST_SZ - NODE_LST_OFFSET ) * NODE_LST_DEP;
		
		final TopoSortNode[] nodes = new TopoSortNode[ NODE_LST_SZ ];
		
		final TopoSortDependency[] deps = new TopoSortDependency[ MAX_DEP_SZ ];
		
		for( int count = 0 ; count < NODE_LST_SZ ; count++ )
		{
			nodes[ count ] = new TopoSortNode();
		}
		
	
		for( int count = NODE_LST_OFFSET ; count < NODE_LST_SZ ; count++ )
		{
			TopoSortNode from = nodes[ count ];
			for( int cnt2 = 0 ; cnt2 < NODE_LST_DEP ; cnt2++ )
			{
				int idx = ( count - 1 ) - rand.nextInt( 15 );
				TopoSortNode to = nodes[ idx ];
				TopoSortDependency dep = new TopoSortDependency( from , to );
				deps[ ( count - NODE_LST_OFFSET ) * NODE_LST_DEP + cnt2 ] = dep;
			}
		}

		
		
		System.out.println( "Randomizing..." );
		
		
		for( int count = 0 ; count < NODE_SWAP_SZ ; count++ )
		{
			int i1 = rand.nextInt( NODE_LST_SZ );
			int i2 = rand.nextInt( NODE_LST_SZ ); 
			TopoSortNode a = nodes[ i1 ];
			nodes[ i1 ] = nodes[ i2 ];
			nodes[ i2 ] = a;
		}
		
		
		for( int count = 0 ; count < NODE_SWAP_SZ * NODE_LST_DEP ; count++ )
		{
			int i1 = rand.nextInt( MAX_DEP_SZ );
			int i2 = rand.nextInt( MAX_DEP_SZ ); 
			TopoSortDependency a = deps[ i1 ];
			deps[ i1 ] = deps[ i2 ];
			deps[ i2 ] = a;
		}
		
		
		
		for( int count = 0 ; count < NODE_LST_SZ ; count++ )
		{
			session.insert( nodes[ count ] );
		}
		
		
		for( int count = 0 ; count < MAX_DEP_SZ ; count++ )
		{
			FactHandle fact = session.insert( deps[ count ] );
			deps[ count ].setFact( fact );
		}
		
		
		
		TopoSortSession placeholder = new TopoSortSession();
		
		session.insert( placeholder );
		
		
		System.out.println( "Starting..." );
		
				
		session.fireAllRules();

		session.dispose();
		
		ArrayList<TopoSortNode> sortedElements = placeholder.getSortedElements();
		
		Assert.assertTrue( sortedElements.size() == NODE_LST_SZ );
		
		
	}
	
	
	

}



