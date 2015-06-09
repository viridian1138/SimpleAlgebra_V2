



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




package simplealgebra.ddx.db;



import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomTypeBase;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.ddx.CoordinateSystemFactory;
import simplealgebra.ddx.FlowVectorFactory;
import simplealgebra.ddx.MaterialDerivativeFactory;
import simplealgebra.ddx.MaterialDerivativeFactoryParam;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.et.DerivativeRemap;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.MetricTensorFactory;
import simplealgebra.et.TemporaryIndexFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;



/**
 * HyperGraph type for storing MaterialDerivativeFactory instances.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class MaterialDerivativeFactoryType<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends HGAtomTypeBase {

	
	/**
	 * Constructs the type instance.
	 */
	public MaterialDerivativeFactoryType() {
	}

	
	@Override
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet,
			IncidenceSetRef incidenceSet) {
		HGHandle[] layout = graph.getStore().getLink( handle );
		EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> fac = graph.get( layout[ 0 ] );
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> tensorWithRespectTo = graph.get( layout[ 1 ] );
		CoordinateSystemFactory<Z,R,S> coordVecFac = graph.get( layout[ 2 ] );
		TemporaryIndexFactory<Z> temp = graph.get( layout[ 3 ] );
		MetricTensorFactory<Z,R,S> metric = graph.get( layout[ 4 ] );
		U dim = graph.get( layout[ 5 ] );
		DirectionalDerivativePartialFactory<R,S,K> dfac = graph.get( layout[ 6 ] );
		FlowVectorFactory<R,S,K> flfac = graph.get( layout[ 7 ] );
		SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> derivT = graph.get( layout[ 8 ] );
		DerivativeRemap<Z,R,S> remap = graph.get( layout[ 9 ] );
		if( fac == null )
			throw( new RuntimeException( "Failed" ) );
		if( tensorWithRespectTo == null )
			throw( new RuntimeException( "Failed" ) );
		// coordVecFac is allowed to be null.
		if( temp == null )
			throw( new RuntimeException( "Failed" ) );
		if( metric == null )
			throw( new RuntimeException( "Failed" ) );
		if( dim == null )
			throw( new RuntimeException( "Failed" ) );
		if( dfac == null )
			throw( new RuntimeException( "Failed" ) );
		if( flfac == null )
			throw( new RuntimeException( "Failed" ) );
		if( derivT == null )
			throw( new RuntimeException( "Failed" ) );
		if( remap == null )
			throw( new RuntimeException( "Failed" ) );
		
		MaterialDerivativeFactoryParam<Z,U,R,S,K> param = new MaterialDerivativeFactoryParam<Z,U,R,S,K>();
		
		param.setFac( fac );
		param.setTensorWithRespectTo( tensorWithRespectTo );
		param.setCoordVecFac( coordVecFac );
		param.setTemp( temp );
		param.setMetric( metric );
		param.setDim( dim );
		param.setDfac( dfac );
		param.setFlfac( flfac );
		param.setDerivT( derivT );
		param.setRemap( remap );
		
		
		final MaterialDerivativeFactory<Z,U,R,S,K> et = new MaterialDerivativeFactory<Z,U,R,S,K>( param );
		return( et );
	}

	@Override
	public void release(HGPersistentHandle handle) {
		graph.getStore().removeLink( handle );
	}

	
	@Override
	public HGPersistentHandle store(Object instance) {
		MaterialDerivativeFactory<Z,U,R,S,K> oid = (MaterialDerivativeFactory<Z,U,R,S,K>)( instance );
		HGHandle facHandle = hg.assertAtom(graph, oid.getFac().getFac() );
		HGHandle wrtHandle = hg.assertAtom(graph, oid.getTensorWithRespectTo() );
		HGHandle coordVecFacHandle = hg.assertAtom(graph, oid.getCofac().getCoordVecFac() );
		HGHandle tempHandle = hg.assertAtom(graph, oid.getCofac().getTemp() );
		HGHandle metricHandle = hg.assertAtom(graph, oid.getCofac().getMetric() );
		HGHandle dimHandle = hg.assertAtom(graph, oid.getDim() );
		HGHandle dfacHandle = hg.assertAtom(graph, oid.getCofac().getOdfac().getDfac() );
		HGHandle flfacHandle = hg.assertAtom(graph, oid.getFlfac() );
		HGHandle derivTHandle = hg.assertAtom(graph, oid.getDerivT() );
		HGHandle remapHandle = hg.assertAtom(graph, oid.getCofac().getRemap() );
		
		HGPersistentHandle[] hn = { facHandle.getPersistent() , wrtHandle.getPersistent() ,
				coordVecFacHandle.getPersistent() , tempHandle.getPersistent() , 
				metricHandle.getPersistent() , dimHandle.getPersistent() , 
				dfacHandle.getPersistent() , flfacHandle.getPersistent() ,
				derivTHandle.getPersistent() , remapHandle.getPersistent() };
		return( graph.getStore().store( hn ) );
	}
	
	
	/**
	 * Adds the type to the type system for a graph.
	 * 
	 * @param graph The graph to which to add the type system.
	 */
	public static void initType( HyperGraph graph )
	{
		MaterialDerivativeFactoryType<?,?,?,?,?> type = new MaterialDerivativeFactoryType();
		HGPersistentHandle typeHandle = graph.getHandleFactory().makeHandle();
		graph.getTypeSystem().addPredefinedType( typeHandle , type , MaterialDerivativeFactory.class );
	}


	
	

}

