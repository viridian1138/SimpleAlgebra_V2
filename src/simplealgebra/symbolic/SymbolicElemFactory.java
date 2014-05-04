




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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;

public class SymbolicElemFactory<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<SymbolicElem<R,S>, SymbolicElemFactory<R,S>> {

	public SymbolicElemFactory( S _fac )
	{
		fac = _fac;
	}
	
	@Override
	public SymbolicElem<R, S> identity() {
		return( new SymbolicIdentity<R,S>( fac ) );
	}

	@Override
	public SymbolicElem<R, S> zero() {
		return( new SymbolicZero<R,S>( fac ) );
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

