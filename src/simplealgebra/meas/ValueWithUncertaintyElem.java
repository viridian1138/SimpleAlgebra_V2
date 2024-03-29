


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



package simplealgebra.meas;

import java.util.ArrayList;

import simplealgebra.AbsoluteValue;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;

import java.io.PrintStream;
import java.math.BigInteger;

import org.kie.api.runtime.KieSession;

/**
 * Elem for a value with an uncertainty (e.g. a measurement error).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class ValueWithUncertaintyElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends MutableElem<R,ValueWithUncertaintyElem<R,S>,ValueWithUncertaintyElemFactory<R,S>>
	{
	

	@Override
	public ValueWithUncertaintyElem<R, S> add(ValueWithUncertaintyElem<R, S> b) {
		return( new ValueWithUncertaintyElem<R,S>( value.add( b.value ) , uncertainty.add( b.uncertainty ) ) );
	}
	
	
	/**
	 * Returns the absolute value of the input.
	 * 
	 * @param in The input.
	 * @return The absolute value.
	 */
	private R abs( R in )
	{
		try
		{
			ArrayList<R> args = new ArrayList<R>();
			return( in.handleOptionalOp( AbsoluteValue.ABSOLUTE_VALUE , args) );
		}
		catch( NotInvertibleException ex )
		{
			throw( new RuntimeException( "Not Invertible" ) );
		}
	}
	

	@Override
	public ValueWithUncertaintyElem<R, S> mult(ValueWithUncertaintyElem<R, S> b) {
		final R a0 = value.mult( b.value );
		final R a1 = abs( value ).mult( b.uncertainty );
		final R a2 = uncertainty.mult( abs( b.value ) );
		final R a3 = uncertainty.mult( b.uncertainty );
		return( new ValueWithUncertaintyElem<R,S>( a0 , a1.add( a2 ).add( a3 ) ) );
	}

	@Override
	public ValueWithUncertaintyElem<R, S> negate() {
		return( new ValueWithUncertaintyElem<R,S>( value.negate() , uncertainty ) );
	}
	
	@Override
	public ValueWithUncertaintyElem<R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		return( new ValueWithUncertaintyElem<R,S>( mutr.mutate( value ) , mutr.mutate( uncertainty ) ) );
	}

	@Override
	public ValueWithUncertaintyElem<R, S> invertLeft() throws NotInvertibleException {
		final R inv = value.invertLeft();
		final R invSq = abs( inv.mult(inv) );
		return( new ValueWithUncertaintyElem<R,S>( inv , invSq.mult(uncertainty) ) );
	}
	
	@Override
	public ValueWithUncertaintyElem<R, S> invertRight() throws NotInvertibleException {
		final R inv = value.invertRight();
		final R invSq = abs( inv.mult(inv) );
		return( new ValueWithUncertaintyElem<R,S>( inv , uncertainty.mult(invSq) ) );
	}
	
	

	@Override
	public ValueWithUncertaintyElem<R, S> divideBy(BigInteger val) {
		return( new ValueWithUncertaintyElem<R,S>( value.divideBy(val) , uncertainty.divideBy(val) ) );
	}
	
	
	@Override
	public ValueWithUncertaintyElem<R, S> random(PrimitiveRandom in) {
		return( new ValueWithUncertaintyElem<R,S>( value.random(in) , uncertainty ) );
	}
	
	
	@Override
	public Elem<?,?> totalMagnitude()
	{
		return( value.totalMagnitude() );
	}
	
	
	@Override
	public ValueWithUncertaintyElem<R, S> handleOptionalOp( Object id , ArrayList<ValueWithUncertaintyElem<R, S>> args ) throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					return( new ValueWithUncertaintyElem<R, S>( abs( value ) , uncertainty ) );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}

	
	@Override
	public ValueWithUncertaintyElemFactory<R, S> getFac() {
		return( new ValueWithUncertaintyElemFactory<R,S>( (S)( value.getFac() ) ) );
	}
	
	
	
	@Override
	public ValueWithUncertaintyElem<R, S> cloneThread( final BigInteger threadIndex )
	{
		final R val2 = value.cloneThread(threadIndex);
		final R unc2 = uncertainty.cloneThread(threadIndex);
		if( ( value != val2 ) || ( uncertainty != unc2 ) )
		{
			return( new ValueWithUncertaintyElem<R, S>( val2 , unc2 ) );
		}
		return( this );
	}
	
	
	
	@Override
	public ValueWithUncertaintyElem<R,S> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<ValueWithUncertaintyElem<R,S>,ValueWithUncertaintyElemFactory<R,S>> cache )
	{
		final ValueWithUncertaintyElem<R,S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final R val2 = (R) value.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final R unc2 = (R) uncertainty.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( ( value != val2 ) || ( uncertainty != unc2 ) )
		{
			final ValueWithUncertaintyElem<R,S> rtmp = new ValueWithUncertaintyElem<R,S>( val2 , unc2 );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	@Override
	public void performInserts( KieSession session )
	{
		value.performInserts( session );
		uncertainty.performInserts( session );
		super.performInserts( session );
	}
	
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		return( ( value.evalSymbolicZeroApprox(mode) ) && ( uncertainty.evalSymbolicZeroApprox(mode) ) );
	}
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		return( ( value.evalSymbolicIdentityApprox(mode) ) && ( uncertainty.evalSymbolicZeroApprox(mode) ) );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		if( pc.parenNeeded( this ,  value , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		value.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  value , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>&PlusMinus;</mo>" );
		if( pc.parenNeeded( this ,  uncertainty , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		uncertainty.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  uncertainty , true ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<ValueWithUncertaintyElem<R,S>,ValueWithUncertaintyElemFactory<R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String vals = value.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			final String unss = uncertainty.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			this.getFac().writeElemTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			this.getFac().writeElemTypeString( ps );
			ps.print( "( " );
			ps.print( vals );
			ps.print( " , " );
			ps.print( unss );
			ps.println( " );" );
		}
		return( st );
	}
	

	
	/**
	 * Constructs the elem.
	 * 
	 * @param _value The value.
	 * @param _uncertainty The uncertainty of the value.
	 */
	public ValueWithUncertaintyElem( R _value , R _uncertainty )
	{
		value = _value;
		uncertainty = _uncertainty;
	}
	
	

	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	public R getValue() {
		return value;
	}

	/**
	 * Gets the uncertainty of the value.
	 * 
	 * @return The uncertainty of the value.
	 */
	public R getUncertainty() {
		return uncertainty;
	}



	/**
	 * The value.
	 */
	private R value;
	
	/**
	 * The uncertainty of the value.
	 */
	private R uncertainty;

}

