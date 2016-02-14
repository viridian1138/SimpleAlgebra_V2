



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




package simplealgebra.symbolic;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

import java.math.BigInteger;


/**
 * A symbolic elem for the sine function.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicSine<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param _ival The number of iterations.
	 */
	public SymbolicSine( SymbolicElem<R,S> _elem , S _fac , int _ival )
	{
		super( _fac );
		elem = _elem;
		ival = _ival;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param _ival The number of iterations.
	 * @param ds The Drools session.
	 */
	public SymbolicSine( SymbolicElem<R,S> _elem , S _fac , int _ival , DroolsSession ds )
	{
		this( _elem , _fac , _ival );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval( implicitSpace ).exp( ival ) );
	}
	
	@Override
	public R evalCached( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ,
			HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final SCacheKey<R,S> key = new SCacheKey<R,S>( this , implicitSpace );
		final R iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final R ret = elem.evalCached( implicitSpace , cache ).exp( ival );
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( ( this.getFac().isMultCommutative() ) && ( this.getFac().isMultAssociative() ) )
		{
			final R sinV = elem.cos( ival ).eval( implicitSpace );
			final R dv = elem.evalPartialDerivative( withRespectTo , implicitSpace );
			return( sinV.mult( dv ) );
		}
		else
		{
			return( super.sin( ival ).evalPartialDerivative(withRespectTo, implicitSpace) );
		}
	}
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( ( this.getFac().isMultCommutative() ) && ( this.getFac().isMultAssociative() ) )
		{
			final R sinV = elem.cos( ival ).evalCached( implicitSpace , cache );
			final R dv = elem.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache );
			return( sinV.mult( dv ) );
		}
		else
		{
			return( super.sin( ival ).evalPartialDerivativeCached(withRespectTo, implicitSpace, cache) );
		}
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicSine<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elems = elem.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elems != elem ) || ( facs != fac ) )
		{
			return( new SymbolicSine<R,S>( elems , facs , ival ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<R,S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache) {
		final SymbolicElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final S facs = this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final SymbolicElem<R,S> elems = elem.cloneThreadCached(threadIndex, cache);
		if( ( elems != elem ) || ( facs != fac ) )
		{
			final SymbolicSine<R,S> rtmp = new SymbolicSine<R,S>( elems , facs , ival );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}

	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "sin( " );
		elem.writeString( ps );
		ps.print( " )" );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		ps.print( "<mrow><mi>sin</mi><mo>&ApplyFunction;</mo>" );
		pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		elem.writeMathML(pc, ps);
		pc.getParenthesisGenerator().handleParenthesisClose(ps);
		ps.print( "</mrow>" );
	}
	
	
	
	/**
	 * Gets the number of iterations.
	 * 
	 * @return The number of iterations.
	 */
	public int getIval() {
		return ival;
	}

	/**
	 * Gets the enclosed elem.
	 * 
	 * @return The enclosed elem.
	 */
	public SymbolicElem<R, S> getElem() {
		return elem;
	}
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elem.evalSymbolicConstantApprox() );
	}

	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}
	
	/**
	 * The number of iterations.
	 */
	private int ival;
	
	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<R,S> elem;

}

