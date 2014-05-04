




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






package simplealgebra.bigfixedpoint;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.AbsoluteValue;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;

public class BigFixedPointElem<T extends Precision> extends Elem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> {
	
	public BigFixedPointElem( BigInteger _val , T _prec )
	{
		val = _val;
		prec = _prec;
	}
	
	public BigFixedPointElem( double vl , T _prec ) 
	{
		final BigDecimal bd = new BigDecimal( vl * prec.getVal().doubleValue() );
		final BigInteger _val = bd.toBigInteger();
		val = _val;
		prec = _prec;
	}

	@Override
	public BigFixedPointElem<T> add(BigFixedPointElem<T> b) {
		return( new BigFixedPointElem<T>( val.add( b.val ) , prec ) );
	}

	@Override
	public BigFixedPointElem<T> mult(BigFixedPointElem<T> b) {
		return( new BigFixedPointElem<T>( val.multiply( b.val ).divide( prec.getVal() ) , prec ) );
	}

	@Override
	public BigFixedPointElem<T> negate() {
		return( new BigFixedPointElem<T>( val.negate() , prec ) );
	}

	@Override
	public BigFixedPointElem<T> invertLeft() throws NotInvertibleException {
		if( val.equals( BigInteger.ZERO ) )
		{
			throw( new NotInvertibleException() );
		}
		return( new BigFixedPointElem<T>( prec.getValSquared().divide( val ) , prec ) );
	}
	
	@Override
	public BigFixedPointElem<T> invertRight() throws NotInvertibleException {
		if( val.equals( BigInteger.ZERO ) )
		{
			throw( new NotInvertibleException() );
		}
		return( new BigFixedPointElem<T>( prec.getValSquared().divide( val ) , prec ) );
	}

	@Override
	public BigFixedPointElem<T> divideBy(int vali) {
		return( new BigFixedPointElem<T>( val.divide( BigInteger.valueOf( vali ) ) , prec ) );
	}

	@Override
	public BigFixedPointElemFactory<T> getFac() {
		return( new BigFixedPointElemFactory<T>( prec ) );
	}
	
	@Override
	public BigFixedPointElem<T> handleOptionalOp( Object id , ArrayList<BigFixedPointElem<T>> args ) throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					return( new BigFixedPointElem<T>( val.abs() , prec ) );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	public double toDouble( )
	{
		return( ( val.doubleValue() ) / ( prec.getVal().doubleValue() ) );
	}

	
	BigInteger val;
	T prec;
	

}

