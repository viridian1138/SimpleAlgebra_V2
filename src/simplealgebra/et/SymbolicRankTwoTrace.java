




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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Symbolic elem for the trace of a rank-two tensor.
 * 
 * See https://en.wikipedia.org/wiki/Trace_(linear_algebra)
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> The type for the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicRankTwoTrace<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> 
{

	/**
	 * Constructs the trace.
	 * 
	 * @param _elemA The argument of the trace.
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicRankTwoTrace( 
			SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elemA , 
			EinsteinTensorElemFactory<Z, R, S> _fac) 
	{
		super( _fac );
		elemA = _elemA;
	}
	
	
	/**
	 * Constructs the trace for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elemA The argument of the trace.
	 * @param _fac The factory for the enclosed type.
	 * @param ds The Drools session.
	 */
	public SymbolicRankTwoTrace( 
			SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> _elemA , 
			EinsteinTensorElemFactory<Z, R, S> _fac ,
			DroolsSession ds ) 
	{
		this( _elemA , _fac );
		ds.insert( this );
	}

	
	@Override
	public EinsteinTensorElem<Z, R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<EinsteinTensorElem<Z,R,S>> args = new ArrayList<EinsteinTensorElem<Z,R,S>>();
		return( elemA.eval( implicitSpace ).handleOptionalOp( EinsteinTensorElem.EinsteinTensorCmd.RANK_TWO_TRACE , args ) );
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
		ArrayList<EinsteinTensorElem<Z,R,S>> args = new ArrayList<EinsteinTensorElem<Z,R,S>>();
		final EinsteinTensorElem<Z, R, S> ret = elemA.evalCached( implicitSpace , cache ).handleOptionalOp( EinsteinTensorElem.EinsteinTensorCmd.RANK_TWO_TRACE , args );
		cache.put(key, ret);
		return( ret );
		
	}

	
	@Override
	public EinsteinTensorElem<Z, R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<Z, R, S>, EinsteinTensorElemFactory<Z, R, S>>, EinsteinTensorElem<Z, R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elemA.evalSymbolicConstantApprox() );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elemA.exposesDerivatives() );
	}

	
	@Override
	public SymbolicRankTwoTrace<Z,R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>>
			elemAs = elemA.cloneThread(threadIndex);
		final EinsteinTensorElemFactory<Z, R, S>
			facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elemAs != elemA ) || ( facs != this.getFac().getFac() ) )
		{
			return( new SymbolicRankTwoTrace<Z,R,S>( elemAs , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "trace( " );
		elemA.writeString( ps );
		ps.print( " )" );
	}
	
	
	/**
	 * Returns the argument of the trace.
	 * 
	 * @return The argument of the trace.
	 */
	public SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> getElemA() {
		return elemA;
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		super.performInserts( session );
	}

	
	/**
	 * The argument of the trace.
	 */
	private SymbolicElem<EinsteinTensorElem<Z,R,S>,EinsteinTensorElemFactory<Z,R,S>> elemA;
	
}
