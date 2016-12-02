




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
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Implements a partial derivative <math display="inline">
 * <mrow>
 *  <mfrac>
 *    <mrow>
 *      <mo>&PartialD;</mo>
 *    </mrow>
 *    <mrow>
 *      <mo>&PartialD;</mo>
 *      <mi>x</mi>
 *    </mrow>
 *  </mfrac>
 * </mrow>
 * </math> as used in Calculus.
 * 
 * See <A href="http://en.wikipedia.org/wiki/Partial_derivative">http://en.wikipedia.org/wiki/Partial_derivative</A>
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class PartialDerivativeOp<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends DerivativeElem<R,S>
{

	/**
	 * Constructs the partial derivative.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _withRespectTo The variable(s) over which to take the partial derivative.
	 */
	public PartialDerivativeOp( S _fac , ArrayList<K> _withRespectTo )
	{
		super( _fac );
		withRespectTo = _withRespectTo;
	}
	
	/**
	 * Constructs the partial derivative.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _withRespectTo The variable(s) over which to take the partial derivative.
	 * @param ds The Drools session.
	 */
	public PartialDerivativeOp( S _fac , ArrayList<K> _withRespectTo , DroolsSession ds )
	{
		this( _fac , _withRespectTo );
		ds.insert( this );
	}
	
	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		return( ( b.isSymbolicZero() || b.isSymbolicIdentity() ) ? this.getFac().zero() : super.mult( b ) );
	}
	
	@Override
	public R evalDerivative( SymbolicElem<R,S> in , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( in.evalPartialDerivative( withRespectTo , implicitSpace ) );
	}	
	
	@Override
	public R evalDerivativeCached( SymbolicElem<R,S> in , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( in.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache ) );
	}	
	
	
	@Override
	public PartialDerivativeOp<R,S,K> cloneThread( final BigInteger threadIndex )
	{
		// The indices of the ArrayList are presumed to be immutable.
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		final ArrayList<K> wrts = (ArrayList<K>)( withRespectTo.clone() );
		return( new PartialDerivativeOp<R,S,K>( facs , wrts ) );
	}
	
	
	@Override
	public SymbolicElem<R, S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<R, S>, SymbolicElemFactory<R, S>> cache) {
		final SymbolicElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		// The indices of the ArrayList are presumed to be immutable.
		final S facs = this.getFac().getFac().cloneThreadCached( threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final ArrayList<K> wrts = (ArrayList<K>)( withRespectTo.clone() );
		final PartialDerivativeOp<R,S,K> rtmp = new PartialDerivativeOp<R,S,K>( facs , wrts );
		cache.put(this, rtmp);
		return( rtmp );
	}
	

	@Override
	public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			cache.applyAuxCache( new WriteKListCache<K>( cache.getCacheVal() ) );
			String staKList = ( (WriteKListCache<K>)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteKListCache.class)) ) ) ).writeDesc(withRespectTo , (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( PartialDerivativeOp.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( PartialDerivativeOp.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( staKList );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		final int sz = withRespectTo.size();
		if( sz > 1 )
		{
			ps.print( "<mfrac><mrow><msup><mo>&part;</mo><mn>" );
			ps.print( "" + sz );
			ps.print( "</mn></msup></mrow><mrow>" );
		}
		else
		{
			ps.print( "<mfrac><mrow><mo>&part;</mo></mrow><mrow>" );
		}
		for( final K nxt : withRespectTo )
		{
			if( nxt instanceof SymbolicElem )
			{
				ps.print( "<mrow><mo>&part;</mo>" );
				( (SymbolicElem) nxt ).writeMathML( pc , ps );
				ps.print( "</mrow>" );
			}
			else
			{
				ps.print( "<mrow><mo>&part;</mo><mi>" );
				ps.print( nxt );
				ps.print( "</mi></mrow>" );
			}
		}
		ps.print( "</mrow></mfrac>" );
	}
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof PartialDerivativeOp )
		{
			PartialDerivativeOp bp = (PartialDerivativeOp) b;
			if( withRespectTo.size() == bp.withRespectTo.size() )
			{
				return( symbolicEqualsComp( bp ) );
			}
		}
		
		return( false );
	}
	
	
	
	/**
	 * Handles the evaluation of symbolic equality after the caller 
	 * reules out the preliminary conditions.
	 * 
	 * @param bp The partial derivative to be compared.
	 * @return True iff. this is equal to bp.
	 */
	private boolean symbolicEqualsComp( PartialDerivativeOp bp )
	{
		for( int cnt = 0 ; cnt < withRespectTo.size() ; cnt++ )
		{
			final K elA = withRespectTo.get( cnt );
			final Object elB = bp.withRespectTo.get( cnt );
			if( ( elA instanceof SymbolicElem ) && ( elB instanceof SymbolicElem ) )
			{
				if( !( ( (SymbolicElem) elA ).symbolicEquals( (SymbolicElem) elB ) ) )
				{
					return( false );
				}
			}
			else
			{
				if( !( elA.equals( elB ) ) )
				{
					return( false );
				}
			}
		}
		
		return( true );
	}
	
	
	
	/**
	 * Gets the variable(s) over which to take the partial derivative.
	 * 
	 * @return The variable(s) over which to take the partial derivative.
	 */
	public ArrayList<K> getWithRespectTo() {
		return withRespectTo;
	}
	
	/**
	 * The variable(s) over which to take the partial derivative.
	 */
	private ArrayList<K> withRespectTo;

}

