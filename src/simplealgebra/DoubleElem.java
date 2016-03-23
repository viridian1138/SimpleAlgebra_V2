



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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.symbolic.PrecedenceComparator;

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
	public DoubleElem divideBy(BigInteger val) {
		if( val.equals( BigInteger.ZERO ) )
		{
			throw( new RuntimeException( "NaN" ) );
		}
		final DoubleElem de = new DoubleElem( d / val.doubleValue() );
		return( de );
	}
	
	@Override
	public DoubleElem cloneThread( final BigInteger threadIndex )
	{
		return( this );
	}
	
	@Override
	public DoubleElem cloneThreadCached( final BigInteger threadIndex , CloneThreadCache<DoubleElem,DoubleElemFactory> cache )
	{
		return( this );
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
		
		if( id instanceof Sqrt )
		{
			switch( (Sqrt) id )
			{
				case SQRT:
				{
					final double dd = Math.sqrt( d );
					if( Double.isNaN( dd ) || Double.isInfinite( dd ) )
					{
						throw( new NotInvertibleException() );
					}
					return( new DoubleElem( dd ) );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		if( d == 0.0 )
		{
			ps.print( "<mn>0.0</mn>" );
			return;
		}
		
		final double expd = Math.log10( Math.abs( d ) );
		int exponent = (int) expd;
		final double div = Math.exp( exponent * Math.log( 10.0 ) );
		double mantissa = ( d / div );
		
		while( Math.abs( mantissa ) >= 10.0 )
		{
			mantissa = mantissa / 10.0;
			exponent++;
		}
		
		while( Math.abs( mantissa ) < 1.0 )
		{
			mantissa = mantissa * 10.0;
			exponent--;
		}
		
		
		if( exponent == 0 )
		{
			ps.print( "<mn>" );
			ps.print( mantissa );
			ps.print( "</mn>" );
		}
		else
		{
			ps.print( "<mrow>" );
			ps.print( "<mn>" );
			ps.print( mantissa );
			ps.print( "</mn>" );
			ps.print( "<mo>&times;</mo>" );
			ps.print( "<msup><mn>10</mn><mn>" );
			ps.print( exponent );
			ps.print( "</mn></msup>" );
			ps.print( "</mrow>" );
		}
		
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<DoubleElem,DoubleElemFactory> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			st = cache.getIncrementVal();
			cache.put(this, st);
			this.getFac().writeElemTypeString(ps);
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			this.getFac().writeElemTypeString(ps);
			ps.print( "( Double.doubleValue( \"" );
			ps.print( d );
			ps.println( "\" ) );" );
		}
		return( st );
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
