





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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.mvel2.optimizers.OptimizerFactory;
import org.kie.internal.KnowledgeBase;




/**
 * Executes a DaqHg (Drools As a Query language for HyperGraphDB) query.
 * 
 * @author thorngreen
 *
 * @param <T> The type of the objects in the result set for the query.
 */
public class DaqHg<T extends Object> 
{

	public void processDaqHg( String drlPath , 
			DaqHgContext<T> context , ArrayList<T> resultList )
	{
		OptimizerFactory.setDefaultOptimizer( OptimizerFactory.SAFE_REFLECTIVE );
		
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		builder.add( ResourceFactory.newClassPathResource( drlPath )  , 
				ResourceType.DRL );
		
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages( builder.getKnowledgePackages() );
		
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		
		context.setResultList( resultList );
		
		session.insert( context );
				
		session.fireAllRules();

		session.dispose();
		
	}
	
	
}



