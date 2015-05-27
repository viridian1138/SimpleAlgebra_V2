



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



import java.math.BigInteger;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.bigfixedpoint.Precision;



/**
 * HyperGraph type for storing BigFixedPointElem instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class BigFixedPointElemType<T extends Precision> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public BigFixedPointElemType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		BigInteger val = graph.get( layout[ 0 ] );
		T prec = graph.get( layout[ 1 ] );
		if( val == null )
			throw( new RuntimeException( "Failed" ) );
		if( prec == null )
			throw( new RuntimeException( "Failed" ) );
		return( new BigFixedPointElem<T>( val , prec ) );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		BigFixedPointElem<T> oid = (BigFixedPointElem<T>)( instance );
		HGHandle valHandle = hg.assertAtom(graph, oid.getPrecVal() );
		HGHandle precHandle = hg.assertAtom(graph, oid.getFac().getPrec() );
		HGPersistentHandle[] hn = { valHandle.getPersistent() , precHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		BigFixedPointElemType<?> type = new BigFixedPointElemType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , BigFixedPointElem.class );
	}


	
	

}

