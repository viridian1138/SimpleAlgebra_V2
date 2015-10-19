



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

import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicSqrt;


/**
 * Factory for double elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DoubleElemFactory extends ElemFactory<DoubleElem, DoubleElemFactory> {

	@Override
	public DoubleElem identity() {
		return( new DoubleElem( 1.0 ) );
	}

	@Override
	public DoubleElem zero() {
		return( new DoubleElem( 0.0 ) );
	}
	
	@Override
	public boolean isMultCommutative()
	{
		return( true );
	}
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( true );
	}
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( true );
	}
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( true );
	}
	
	@Override
	public DoubleElemFactory cloneThread( final BigInteger threadIndex )
	{
		return( this );
	}
	
	@Override
	public DoubleElemFactory cloneThreadCached( final BigInteger threadIndex , CloneThreadCache<DoubleElem,DoubleElemFactory> cache )
	{
		return( this );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem, DoubleElemFactory> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> args )  throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					SymbolicElem<DoubleElem, DoubleElemFactory> arg
						= args.get( 0 );
					return( new SymbolicAbsoluteValue<DoubleElem, DoubleElemFactory>( arg , arg.getFac().getFac() ) );
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
					SymbolicElem<DoubleElem, DoubleElemFactory> arg
						= args.get( 0 );
					return( new SymbolicSqrt<DoubleElem, DoubleElemFactory>( arg , arg.getFac().getFac() ) );
				}
				// break;
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	
	
	/**
	 * Constructs the factory.
	 */
	public DoubleElemFactory()
	{
	}

	
}
