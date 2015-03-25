



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





package simplealgebra;

import java.util.ArrayList;

/**
 * An elem for doubles.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DoubleElem extends Elem<DoubleElem, DoubleElemFactory> {

	@Override
	public DoubleElem add(DoubleElem b) {
		return( new DoubleElem( d + b.d ) );
	}

	@Override
	public DoubleElem mult(DoubleElem b) {
		return( new DoubleElem( d * b.d ) );
	}

	@Override
	public DoubleElem negate() {
		return( new DoubleElem( - d ) );
	}

	@Override
	public DoubleElem invertLeft() throws NotInvertibleException {
		final double dv = 1.0 / d;
		if( Double.isNaN( dv ) || Double.isInfinite( dv ) )
		{
			throw( new NotInvertibleException() );
		}
		final DoubleElem de = new DoubleElem( dv );
		return( de );
	}
	
	@Override
	public DoubleElem invertRight() throws NotInvertibleException {
		final double dv = 1.0 / d;
		if( Double.isNaN( dv ) || Double.isInfinite( dv ) )
		{
			throw( new NotInvertibleException() );
		}
		final DoubleElem de = new DoubleElem( dv );
		return( de );
	}

	@Override
	public DoubleElem divideBy(int val) {
		if( val == 0 )
		{
			throw( new RuntimeException( "NaN" ) );
		}
		final DoubleElem de = new DoubleElem( d / val );
		return( de );
	}

	@Override
	public DoubleElemFactory getFac() {
		return( new DoubleElemFactory() );
	}
	
	@Override
	public DoubleElem handleOptionalOp( Object id , ArrayList<DoubleElem> args ) throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					return( new DoubleElem( Math.abs( d ) ) );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	
	/**
	 * Constructs the elem.
	 * 
	 * @param _d The double value.
	 */
	public DoubleElem( double _d )
	{
		if( Double.isNaN( _d ) || Double.isInfinite( _d ) )
		{
			throw( new RuntimeException( "NaN" ) );
		}
		d = _d;
	}
	
	
	
	/**
	 * Gets the double value.
	 * 
	 * @return The double value.
	 */
	public double getVal()
	{
		return( d );
	}
	

	/**
	 * The double value.
	 */
	private double d;
	

}
