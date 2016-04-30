






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





package simplealgebra.ddx;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.WriteDerivativeRemapCache;
import simplealgebra.et.WriteMetricTensorFactoryCache;
import simplealgebra.et.WriteOrdinaryDerivativeFactoryCache;
import simplealgebra.et.WriteTemporaryIndexFactoryCache;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Implements a tensor for a flow vector.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions over which to express the flow vector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class FlowVectorTensor<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> 
	extends SymbolicElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> {

	/**
	 * Constructs the flow vector tensor.
	 * 
	 * @param _fac Factory for the enclosed type.
	 * @param _index The tensor index for the flow vector.
	 * @param _dim The number of dimensions over which to calculate the flow vector.
	 * @param _dfac Factory for generating the components of the flow vector.
	 */
	public FlowVectorTensor( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			Z _index ,
			U _dim ,
			FlowVectorFactory<R,S,K> _dfac ) 
	{
		super( _fac );
		index = _index;
		dim = _dim;
		dfac = _dfac;
	}
	
	/**
	 * Constructs the flow vector tensor for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _fac Factory for the enclosed type.
	 * @param _index The tensor index for the flow vector.
	 * @param _dim The number of dimensions over which to calculate the flow vector.
	 * @param _dfac Factory for generating the components of the flow vector.
	 * @param ds The Drools session.
	 */
	public FlowVectorTensor( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			Z _index ,
			U _dim ,
			FlowVectorFactory<R,S,K> _dfac , DroolsSession ds ) 
	{
		this( _fac , _index , _dim , _dfac );
		ds.insert( this );
	}

	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException 
	{
		final SymbolicElemFactory<R, S> facC = this.getFac().getFac().getFac();
	
		final ArrayList<Z> contravariantIndices = new ArrayList<Z>();
		final ArrayList<Z> covariantIndices = new ArrayList<Z>();
	
		contravariantIndices.add( index );
	
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> mul = 
				new EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>( facC , 
						contravariantIndices , covariantIndices );
	
		BigInteger cnt = BigInteger.ZERO;
	
		final BigInteger max = dim.getVal();
	
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			final ArrayList<BigInteger> key = new ArrayList<BigInteger>();
			key.add( cnt );
		
			SymbolicElem<R,S> val = dfac.getComponent( cnt );
		
			mul.setVal(key, val);
		}
	
		return( mul );
	}
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> key
			= new SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>( this , implicitSpace );
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> ret = eval( implicitSpace );
		cache.put(key, ret);
		return( ret );
	}

	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( dfac.exposesDerivatives() );
	}
	
	
	@Override
	public FlowVectorTensor<Z,U,R,S,K> cloneThread( final BigInteger threadIndex )
	{
		// The Z index amd the NumDimensions dim are presumed to be immutable.
		final EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> facs = this.getFac().getFac().cloneThread(threadIndex);
		final FlowVectorFactory<R,S,K> dfacs = dfac.cloneThread(threadIndex);
		if( ( facs != this.getFac().getFac() ) || ( dfacs != dfac ) )
		{
			return( new FlowVectorTensor<Z,U,R,S,K>( facs , index , dim , dfacs ) );
		}
		return( this );
	}
	

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>, SymbolicElemFactory<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteFlowVectorFactoryCache<R,S,K>( cache.getCacheVal() ) );
			
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			
			final String derivs = cache.getIncrementVal();
			 ps.print( "final " );
			 ps.print( index.getClass().getSimpleName() );
			 ps.print( " = new " );
			 ps.print( index.getClass().getSimpleName() );
			 ps.print( "( \"" );
			 ps.print( index );
			 ps.println( "\" );" );
			
			final String dims = dim.writeDesc( (WriteNumDimensionsCache)( cache.getAuxCache( WriteNumDimensionsCache.class ) )  , ps );
			
			final String dfacss = dfac.writeDesc( ( (WriteFlowVectorFactoryCache)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteFlowVectorFactoryCache.class)) ) ) ) , ps);
			
		
			
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( FlowVectorTensor.class.getSimpleName() );
			ps.print( "<" );
			ps.print( "? extends Object" );
			ps.print( "," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Elem<?,?>" );
			ps.print( ">" );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( FlowVectorTensor.class.getSimpleName() );
			ps.print( "<" );
			ps.print( "? extends Object" );
			ps.print( "," );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Elem<?,?>" );
			ps.print( ">" );
			ps.print( "( " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( derivs );
			ps.print( " , " );
			ps.print( dims );
			ps.print( " , " );
			ps.print( dfacss );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	/**
	 * Gets the tensor index for the flow vector.
	 * 
	 * @return The tensor index for the flow vector.
	 */
	public Z getIndex() {
		return index;
	}

	/**
	 * Gets the number of dimensions for the index.
	 * 
	 * @return The number of dimensions for the index.
	 */
	public U getDim() {
		return dim;
	}

	/**
	 * Gets the factory for generating the components of the flow vector.
	 * 
	 * @return The factory for generating the components of the flow vector.
	 */
	public FlowVectorFactory<R, S, K> getDfac() {
		return dfac;
	}


	/**
	 * The tensor index for the flow vector.
	 */
	private Z index;
	
	/**
	 * The number of dimensions for the index.
	 */
	private U dim;
	
	/**
	 * Factory for generating the components of the flow vector.
	 */
	private FlowVectorFactory<R,S,K> dfac;
	

}

