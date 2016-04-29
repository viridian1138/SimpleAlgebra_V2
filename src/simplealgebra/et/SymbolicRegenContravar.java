



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




package simplealgebra.et;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Symbolic elem. for returning a tensor with the same elements as the argument, but with
 * the contravariant indices renamed to a new set of index names.
 * This is used, for instance, when the same term is used in a formula with a different index from its original definition.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicRegenContravar<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The argument.
	 * @param _fac The factory for the enclosed type.
	 * @param _newContravar The new list of names for the contravariant indices.
	 */
	public SymbolicRegenContravar( SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elem , 
			EinsteinTensorElemFactory<Z,R,S> _fac , ArrayList<Z> _newContravar )
	{
		super( _fac );
		elem = _elem;
		newContravar = _newContravar;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elem The argument.
	 * @param _fac The factory for the enclosed type.
	 * @param _newContravar The new list of names for the contravariant indices.
	 * @param ds The Drools session.
	 */
	public SymbolicRegenContravar( SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elem , 
			EinsteinTensorElemFactory<Z,R,S> _fac , ArrayList<Z> _newContravar , DroolsSession ds  )
	{
		this( _elem , _fac , _newContravar );
		ds.insert( this );
	}
	
	
	
	@Override
	public EinsteinTensorElem<Z,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval( implicitSpace ).regenContravar( newContravar ) );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, EinsteinTensorElem<Z, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> key =
				new SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>( this , implicitSpace );
		final EinsteinTensorElem<Z, R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final EinsteinTensorElem<Z, R, S> ret = elem.evalCached( implicitSpace , cache ).regenContravar( newContravar );
		cache.put(key, ret);
		return( ret );
		
	}
	
	
	@Override
	public EinsteinTensorElem<Z,R,S> evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( elem.evalPartialDerivative( withRespectTo , implicitSpace ).regenContravar( newContravar ) );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, EinsteinTensorElem<Z, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( elem.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ).regenContravar( newContravar ) );
	}
	
	
	@Override
	public SymbolicRegenContravar<Z,R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elems = elem.cloneThread(threadIndex);
		final EinsteinTensorElemFactory<Z,R,S> facs = fac.cloneThread(threadIndex);
		// The indices inside the array list are presumed to be immutable.
		final ArrayList<Z> contravars = (ArrayList<Z>)( newContravar.clone() );
		return( new SymbolicRegenContravar<Z,R,S>( elems , facs , contravars ) );
	}
	
	
	@Override
	public SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, SymbolicElemFactory<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>> cache) {
		final SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> 
			elems = elem.cloneThreadCached( threadIndex , cache );
		final EinsteinTensorElemFactory<Z,R,S> facs = this.getFac().getFac().cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) );
		// The indices inside the array list are presumed to be immutable.
		final ArrayList<Z> contravars = (ArrayList<Z>)( newContravar.clone() );
		final SymbolicRegenContravar<Z,R,S> rtmp = new SymbolicRegenContravar<Z,R,S>( elems , facs , contravars );
		cache.put(this, rtmp);
		return( rtmp );
	}
	

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, SymbolicElemFactory<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteZListCache<Z>( cache.getCacheVal() ) );
			final String elemAs = elem.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			String staZListContravar = ( (WriteZListCache<Z>)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteZListCache.class)) ) ) ).writeDesc( newContravar , ps );
			String sl = cache.getIncrementVal();
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicIndexReduction.class.getSimpleName() );
			ps.print( "<? extends Object," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicIndexReduction.class.getSimpleName() );
			ps.print( "<? extends Object," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( "( " );
			ps.print( elemAs );
			ps.print( " , " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( staZListContravar );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}
	
	
	/**
	 * Gets the new list of names for the contravariant indices.
	 * 
	 * @return The new list of names for the contravariant indices.
	 */
	public ArrayList<Z> getNewContravar() {
		return newContravar;
	}

	/**
	 * Gets the argument to be renamed.
	 * 
	 * @return The argument to be renamed.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>> getElem() {
		return elem;
	}


	/**
	 * The new list of names for the contravariant indices.
	 */
	private ArrayList<Z> newContravar;
	
	/**
	 * The argument to be renamed.
	 */
	private SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elem;


}

