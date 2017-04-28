



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



import java.lang.reflect.Field;
import java.util.Random;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomType;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.DefaultPrimitiveRandom;



/**
 * HyperGraph ( <A href="http://hypergraphdb.org">http://hypergraphdb.org</A> ) type for storing DefaultPrimitiveRandom instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DefaultPrimitiveRandomType extends HGAtomTypeBase {

	/**
	 * Constructs the type instance.
	 */
	public DefaultPrimitiveRandomType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGAtomType type = graph.getTypeSystem().getAtomType( Long.class );
		return( new DefaultPrimitiveRandom( new Random( (Long)( type.make(handle, null, null) ) ) ) );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		HGAtomType type = graph.getTypeSystem().getAtomType( Long.class );
		type.release( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		HGAtomType type = graph.getTypeSystem().getAtomType( Long.class );
		Random rand = ( (DefaultPrimitiveRandom) instance ).getRand();
		try {
			Field field = Random.class.getDeclaredField("seed");
			field.setAccessible(true);
			Long rnd = field.getLong( rand );
			return( type.store( rnd ) );
		} catch (Throwable ex) {
			ex.printStackTrace(System.out);
		}
		return( null );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		DefaultPrimitiveRandomType type = new DefaultPrimitiveRandomType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , DefaultPrimitiveRandom.class );
	}
	

}

