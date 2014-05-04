



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

import simplealgebra.symbolic.SymbolicElem;

public class ComplexElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<ComplexElem<R,S>, ComplexElemFactory<R,S>> {

	
	
	public ComplexElemFactory( S _fac )
	{
		fac = _fac;
	}

	@Override
	public ComplexElem<R, S> identity() {
		return( new ComplexElem<R,S>( fac.identity() , fac.zero() ) );
	}

	@Override
	public ComplexElem<R, S> zero() {
		return( new ComplexElem<R,S>( fac.zero() , fac.zero() ) );
	}
	
	
	@Override
	public SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof ComplexElem.ComplexCmd )
		{
			switch( (ComplexElem.ComplexCmd) id )
			{
				case CONJUGATE_LEFT:
				{
					SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>> arg
						= args.get( 0 );
					return( new SymbolicConjugateLeft<R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
				
				case CONJUGATE_RIGHT:
				{
					SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R,S>> arg
						= args.get( 0 );
					return( new SymbolicConjugateRight<R,S>( arg , arg.getFac().getFac() ) );
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

