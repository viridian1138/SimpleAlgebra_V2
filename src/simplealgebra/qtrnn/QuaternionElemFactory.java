




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




package simplealgebra.qtrnn;

import java.math.BigInteger;
import java.util.HashSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;

public class QuaternionElemFactory<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<QuaternionElem<U,R,S>, QuaternionElemFactory<U,R,S>> {

	
	public QuaternionElemFactory( S _fac , U _dim )
	{
		fac = _fac;
		dim = _dim;
	}
	
	
	@Override
	public QuaternionElem<U, R, S> identity() {
		QuaternionElem<U, R, S> ret = new QuaternionElem<U, R, S>( fac , dim );
		ret.setVal( new HashSet<BigInteger>(), fac.identity() );
		return( ret );
	}

	@Override
	public QuaternionElem<U, R, S> zero() {
		return( new QuaternionElem<U, R, S>( fac , dim ) );
	}
	
	
	@Override
	public boolean isMultCommutative()
	{
		return( false );
	}
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	public S getFac()
	{
		return( fac );
	}
	
	
	private S fac;
	private U dim;

}



