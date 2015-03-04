




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

import simplealgebra.Elem;
import simplealgebra.ElemFactory;


/**
 * A factory for tensor elems.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class EinsteinTensorElemFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<EinsteinTensorElem<Z,R,S>, EinsteinTensorElemFactory<Z,R,S>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public EinsteinTensorElemFactory( S _fac )
	{
		fac = _fac;
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
	
	
	/**
	 * Returns the factory for the enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
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
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( false );
	}
	
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;

}



