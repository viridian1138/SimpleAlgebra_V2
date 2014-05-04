



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
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicElem;

public class ValueWithUncertaintyElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<ValueWithUncertaintyElem<R,S>, ValueWithUncertaintyElemFactory<R,S>> {

	
	
	public ValueWithUncertaintyElemFactory( S _fac )
	{
		fac = _fac;
	}

	@Override
	public ValueWithUncertaintyElem<R, S> identity() {
		return( new ValueWithUncertaintyElem<R,S>( fac.identity() , fac.zero() ) );
	}

	@Override
	public ValueWithUncertaintyElem<R, S> zero() {
		return( new ValueWithUncertaintyElem<R,S>( fac.zero() , fac.zero() ) );
	}
	
	
	@Override
	public SymbolicElem<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					SymbolicElem<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>> arg
						= args.get( 0 );
					return( new SymbolicAbsoluteValue<ValueWithUncertaintyElem<R, S>, ValueWithUncertaintyElemFactory<R,S>>( arg , arg.getFac().getFac() ) );
				}
				// break;
				
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	
	@Override
	public boolean isMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	public S getFac()
	{
		return( fac );
	}
	
	
	private S fac;

}

