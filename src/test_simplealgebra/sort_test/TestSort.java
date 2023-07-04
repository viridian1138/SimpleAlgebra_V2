





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





package test_simplealgebra.sort_test;


import java.util.Random;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.mvel2.optimizers.OptimizerFactory;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.symbolic.DroolsSession;



/**
 * Verifies that Drools ( <A href="http://drools.org">http://drools.org</A> ) constructs a sorting algorithm from a set of heuristics.
 * Note: this is only intended to be notional, and one should use something different when efficient execution is desired.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestSort extends TestCase {


	
	/**
	 * Verifies that Drools ( <A href="http://drools.org">http://drools.org</A> ) constructs a sorting algorithm from a set of heuristics.
	 * Note: this is only intended to be notional, and one should use something different when efficient execution is desired.
	 */
	public void testSort()
	{
		
		OptimizerFactory.setDefaultOptimizer( OptimizerFactory.SAFE_REFLECTIVE );
		

		KieServices kieServices = KieServices.Factory.get();
	    KieFileSystem kfs = kieServices.newKieFileSystem();
	    
	    kfs.write( "src/main/resources/sort.drl",
	    		ResourceFactory.newClassPathResource( "test_simplealgebra/sort_test/sort.drl" )  );


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
		
		SortNode<DoubleElem> currentSort = null;
		
		for( int count = 0 ; count < 10 ; count++ )
		{
			SortNode<DoubleElem> node = new SortNode<DoubleElem>( new DoubleElem( rand.nextDouble() ) , currentSort );
			currentSort = node;
			session.insert( node );
			session.insert( node.sortValue );
		}
		
		SortPlaceholder<DoubleElem> placeholder = new SortPlaceholder<DoubleElem>( currentSort );
		
		session.insert( placeholder );
				
		session.fireAllRules();

		session.dispose();
		
		SortNode<DoubleElem> prev = null;
		SortNode<DoubleElem> nxt = placeholder.getElem();
		
		while( nxt != null )
		{
			prev = nxt;
			nxt = prev.next;
			
			if( ( prev != null ) && ( nxt != null ) )
			{
				Assert.assertTrue( nxt.sortValue.compareTo( prev.sortValue ) >= 0 );
			}
			
		}
		
		
	}
	
	
	

}



