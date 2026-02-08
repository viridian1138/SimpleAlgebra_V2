



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

import org.kie.api.runtime.KieSession;

import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;

import java.math.BigInteger;


/**
 * A symbolic elem for the right power function.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicPowR<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elemA The enclosed exponent base.
	 * @param _elemB The enclosed exponent.
	 * @param _fac The factory for the enclosed elem.
	 * @param _ivalExp The number of iterations.
	 * @param _ivalLn The number of iterations.
	 */
	public SymbolicPowR( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , int _ivalExp , int _ivalLn )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
		ivalExp = _ivalExp;
		ivalLn = _ivalLn;
	}
	
	/**
	 * Constructs the elem for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _elemA The enclosed exponent base.
	 * @param _elemB The enclosed exponent.
	 * @param _fac The factory for the enclosed elem.
	 * @param _ivalExp The number of iterations.
	 * @param _ivalLn The number of iterations.
	 * @param ds The Drools session.
	 */
	public SymbolicPowR( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , int _ivalExp , int _ivalLn , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac , _ivalExp , _ivalLn );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		return( elemA.eval( implicitSpace ).powR( elemB.eval( implicitSpace ) , ivalExp , ivalLn ) );
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
		final R ret = elemA.evalCached( implicitSpace , cache ).powR( elemB.evalCached( implicitSpace , cache ) , ivalExp , ivalLn );
		cache.put( key , ret );
		return( ret );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( ( this.getFac().isMultCommutative() ) && ( this.getFac().isMultAssociative() ) )
		{
			SymbolicElem<R,S> derivTerm = ( elemA.ln(ivalExp, ivalLn) ).mult( elemB );
			final R expV = elemA.eval( implicitSpace ).powR( elemB.eval( implicitSpace ) , ivalExp , ivalLn );
			final R dv = derivTerm.evalPartialDerivative( withRespectTo , implicitSpace );
			return( expV.mult( dv ) );
		}
		else
		{
			return( super.powR( elemB , ivalExp , ivalLn ).evalPartialDerivative(withRespectTo, implicitSpace) );
		}
	}
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final SCacheKey<R,S> key = new SCacheKey<R,S>( this , implicitSpace , withRespectTo );
		final R iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		if( ( this.getFac().isMultCommutative() ) && ( this.getFac().isMultAssociative() ) )
		{
			SymbolicElem<R,S> derivTerm = ( elemA.ln(ivalExp, ivalLn) ).mult( elemB );
			final R expV = elemA.evalCached( implicitSpace , cache ).powR( elemB.evalCached( implicitSpace , cache ) , ivalExp , ivalLn );
			final R dv = derivTerm.evalPartialDerivativeCached( withRespectTo , implicitSpace , cache );
			final R ret = expV.mult( dv );
			cache.put(key, ret);
			return( ret );
		}
		else
		{
			final R ret = super.powR( elemB , ivalExp , ivalLn ).evalPartialDerivativeCached(withRespectTo, implicitSpace, cache);
			cache.put(key, ret);
			return( ret );
		}
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( ( elemB.exposesDerivatives() ) || ( elemA.exposesDerivatives() ) );
	}
	
	
	@Override
	public SymbolicPowR<R,S> cloneThread( final BigInteger threadIndex )
	{
		final SymbolicElem<R,S> elemsA = elemA.cloneThread( threadIndex );
		final SymbolicElem<R,S> elemsB = elemB.cloneThread( threadIndex );
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		if( ( elemsA != elemA ) || ( elemsB != elemB ) || ( facs != fac ) )
		{
			return( new SymbolicPowR<R,S>( elemsA , elemsB , facs , ivalExp , ivalLn ) );
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
		final S facs = (S) this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final SymbolicElem<R,S> elemsA = elemA.cloneThreadCached(threadIndex, cache);
		final SymbolicElem<R,S> elemsB = elemB.cloneThreadCached(threadIndex, cache);
		if( ( elemsA != elemA ) || ( elemsB != elemB ) || ( facs != fac ) )
		{
			final SymbolicPowR<R,S> rtmp = new SymbolicPowR<R,S>( elemsA , elemsB , facs , ivalExp , ivalLn );
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
			final String elemsA = elemA.writeDesc( cache , ps);
			final String elemsB = elemB.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicPowR.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicPowR.class.getSimpleName() );
			this.getFac().writeOrdinaryEnclosedType(ps);
			ps.print( "( " );
			ps.print( elemsA );
			ps.print( " , " );
			ps.print( elemsB );
			ps.print( " , " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( ivalExp );
			ps.print( " , " );
			ps.print( ivalLn );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		ps.print( "<mrow><mi>powR</mi><mo>&ApplyFunction;</mo>" );
		pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		elemA.writeMathML(pc, ps);
		ps.print( " , " );
		elemB.writeMathML(pc, ps);
		pc.getParenthesisGenerator().handleParenthesisClose(ps);
		ps.print( "</mrow>" );
	}
	
	
	
	/**
	 * Gets the number of iterations.
	 * 
	 * @return The number of iterations.
	 */
	public int getIvalExp() {
		return ivalExp;
	}
	
	
	
	/**
	 * Gets the number of iterations.
	 * 
	 * @return The number of iterations.
	 */
	public int getIvalLn() {
		return ivalLn;
	}

	/**
	 * Gets the enclosed elem A.
	 * 
	 * @return The enclosed elem A.
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}

	/**
	 * Gets the enclosed elem B.
	 * 
	 * @return The enclosed elem B.
	 */
	public SymbolicElem<R, S> getElemB() {
		return elemB;
	}
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( ( elemA.evalSymbolicConstantApprox() ) && ( elemB.evalSymbolicConstantApprox() ) );
	}

	@Override
	public void performInserts( KieSession session )
	{
		elemA.performInserts( session );
		elemB.performInserts( session );
		super.performInserts( session );
	}
	
	/**
	 * The number of iterations.
	 */
	private int ivalExp;
	
	/**
	 * The number of iterations.
	 */
	private int ivalLn;
	
	/**
	 * The enclosed elem A.
	 */
	private SymbolicElem<R,S> elemA;
	
	/**
	 * The enclosed elem B.
	 */
	private SymbolicElem<R,S> elemB;

}


