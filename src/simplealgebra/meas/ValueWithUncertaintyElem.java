


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
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;

/**
 * Elem for a value with an uncertainty (e.g. a measurement error).
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
	public ValueWithUncertaintyElem<R, S> divideBy(int val) {
		return( new ValueWithUncertaintyElem<R,S>( value.divideBy(val) , uncertainty.divideBy(val) ) );
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

