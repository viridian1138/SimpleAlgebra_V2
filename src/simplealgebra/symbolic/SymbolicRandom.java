



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

import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.WritePrimitiveRandomCache;
import simplealgebra.bigfixedpoint.WritePrecisionCache;

import java.math.BigInteger;


/**
 * A symbolic elem for a random number function.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicRandom<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param _ival The random number generator.
	 */
	public SymbolicRandom( SymbolicElem<R,S> _elem , S _fac , PrimitiveRandom _ival )
	{
		super( _fac );
		elem = _elem;
		ival = _ival;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param _ival The random number generator.
	 * @param ds The Drools session.
	 */
	public SymbolicRandom( SymbolicElem<R,S> _elem , S _fac , PrimitiveRandom _ival , DroolsSession ds )
	{
		this( _elem , _fac , _ival );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval( implicitSpace ).random( ival ) );
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
		final R ret = elem.evalCached( implicitSpace , cache ).random( ival );
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		throw( new RuntimeException( "Not supported.  Random numbers don't have well-defined derivatives." ) );
	}
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		throw( new RuntimeException( "Not supported.  Random numbers don't have well-defined derivatives." ) );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicRandom<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elems = elem.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elems != elem ) || ( facs != fac ) )
		{
			return( new SymbolicRandom<R,S>( elems , facs , ival ) );
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
			final SymbolicRandom<R,S> rtmp = new SymbolicRandom<R,S>( elems , facs , ival );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}

	
	@Override
	public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WritePrimitiveRandomCache( cache.getCacheVal() ) );
			final String elems = elem.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String stai = ival.writeDesc( (WritePrimitiveRandomCache)( cache.getAuxCache( WritePrimitiveRandomCache.class ) )  , ps );
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicRandom.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicRandom.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( elems );
			ps.print( " , " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( stai );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		ps.print( "<mrow><mi>random</mi><mo>&ApplyFunction;</mo>" );
		pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		elem.writeMathML(pc, ps);
		pc.getParenthesisGenerator().handleParenthesisClose(ps);
		ps.print( "</mrow>" );
	}
	
	
	
	/**
	 * Gets the random number generator.
	 * 
	 * @return The random number generator.
	 */
	public PrimitiveRandom getIval() {
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
	 * The random number generator.
	 */
	private PrimitiveRandom ival;
	
	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<R,S> elem;

}

