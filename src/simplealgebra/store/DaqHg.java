





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








package simplealgebra.store;

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




/**
 * Executes a DaqHg (Drools As a Query language for HyperGraphDB) query.  Uses Drools ( <A href="http://drools.org">http://drools.org</A> ) and HyperGraph ( <A href="http://hypergraphdb.org">http://hypergraphdb.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the objects in the result set for the query.
 */
public class DaqHg<T extends Object> 
{

	/**
	 * Executes a DaqHg (Drools As a Query language for HyperGraphDB) query.
	 * 
	 * @param drlPath The path to the Drools query description.
	 * @param context The context for the query execution.
	 * @param resultHandler The handler for the query results.
	 */
	public void processDaqHg( String drlPath , 
			DaqHgContext<T> context , DaqHgResultHandler<T> resultHandler )
	{
		OptimizerFactory.setDefaultOptimizer( OptimizerFactory.SAFE_REFLECTIVE );


		KieServices kieServices = KieServices.Factory.get();
	    KieFileSystem kfs = kieServices.newKieFileSystem();
	    
	    kfs.write( "src/main/resources/sort.drl",
	    		ResourceFactory.newClassPathResource( drlPath )  );


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
		context.setResultHandler( resultHandler );
		
		session.insert( context );
				
		session.fireAllRules();

		session.dispose();
		
	}
	
	
}



