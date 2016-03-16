




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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;

/**
 * Symbolic elem for the left-side inverse of an elem.  That is to say, one that satisfies: <math display="inline">
 * <mrow>
 *  <msup>
 *          <mi>A</mi>
 *        <mo>-1L</mo>
 *  </msup>
 *  <mi>A</mi>
 *  <mo>=</mo>
 *  <mi>I</mi>
 * </mrow>
 * </math>
 * where <math display="inline">
 * <mrow>
 *  <mi>I</mi>
 * </mrow>
 * </math> is the identity.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicInvertLeft<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 */
	public SymbolicInvertLeft( SymbolicElem<R,S> _elem , S _fac )
	{
		super( _fac );
		elem = _elem;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elem The enclosed elem.
	 * @param _fac The factory for the enclosed elem.
	 * @param ds The Drools session.
	 */
	public SymbolicInvertLeft( SymbolicElem<R,S> _elem , S _fac , DroolsSession ds )
	{
		this( _elem , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elem.eval( implicitSpace ).invertLeft() );
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
		final R ret = elem.evalCached( implicitSpace , cache ).invertLeft();
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final R aL = elem.invertLeft().eval(implicitSpace);
		final R ap = elem.evalPartialDerivative(withRespectTo, implicitSpace);
		final R aR = elem.invertRight().eval(implicitSpace);
		return( aL.mult( ap ).mult( aR ).negate() );
	}
	
	@Override
	public R evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache )
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		final R aL = elem.invertLeft().evalCached(implicitSpace, cache);
		final R ap = elem.evalPartialDerivativeCached(withRespectTo, implicitSpace, cache);
		final R aR = elem.invertRight().evalCached(implicitSpace, cache);
		return( aL.mult( ap ).mult( aR ).negate() );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicInvertLeft<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elems = elem.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elems != elem ) || ( facs != fac ) )
		{
			return( new SymbolicInvertLeft<R,S>( elems , facs ) );
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
			final SymbolicInvertLeft<R,S> rtmp = new SymbolicInvertLeft<R,S>( elems , facs );
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
			final String elems = elem.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicInvertLeft.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicInvertLeft.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( elems );
			ps.print( " , " );
			ps.print( facs );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		ps.print( "<msup>" );
		if( pc.parenNeeded( this ,  elem , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elem.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elem , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mi>-1L</mi></msup>" );
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
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicInvertLeft )
		{
			return( elem.symbolicEquals( ((SymbolicInvertLeft<R,S>) b).getElem() ) );
		}
		
		return( false );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}
	
	
	/**
	 * The enclosed elem.
	 */
	private SymbolicElem<R,S> elem;

}

