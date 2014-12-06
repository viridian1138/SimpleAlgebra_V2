




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




package simplealgebra.et;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Element describing a tensor as defined in General Relativity.
 */
public class EinsteinTensorElemFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<EinsteinTensorElem<Z,R,S>, EinsteinTensorElemFactory<Z,R,S>> {

	
	public EinsteinTensorElemFactory( S _fac , ArrayList<Z> _contravariantIndices ,
			ArrayList<Z> _covariantIndices )
	{
		fac = _fac;
		contravariantIndices = _contravariantIndices;
		covariantIndices = _covariantIndices;
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> identity() {
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , new ArrayList<Z>() , new ArrayList<Z>() );
		ret.setVal( new ArrayList<BigInteger>(), fac.identity() );
		return( ret );
	}

	@Override
	public EinsteinTensorElem<Z, R, S> zero() {
		return( new EinsteinTensorElem<Z, R, S>( fac , new ArrayList<Z>() , new ArrayList<Z>() ) );
	}
	
	
	public S getFac()
	{
		return( fac );
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
	
	
	private S fac;
	private ArrayList<Z> contravariantIndices;
	private ArrayList<Z> covariantIndices;

}



