




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





package simplealgebra.ddx;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Factory for generating a tensor for the coordinate system.
 * 
 * @author thorngreen
 *
 * @param <Z> The type for the tensor indices.
 * @param <R> The enclosed type of the coordinates.
 * @param <S> Factory for the enclosed type of the coordinates.
 */
public abstract class CoordinateSystemFactory<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>>
{

	/**
	 * Returns the coordinate system.
	 * 
	 * @param index The tensor index for the coordinates.
	 * @return The coordinate system.
	 */
	public abstract SymbolicElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
		EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> genCoord( Z index );

}

