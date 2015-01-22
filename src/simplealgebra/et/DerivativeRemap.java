





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


import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Class for remapping a tensor derivative of rank two, for instance to remove antisymmetric terms.
 * 
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor index.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public abstract class DerivativeRemap<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
{
	
	/**
	 * Remaps the derivative.
	 * 
	 * @param remap The derivative to be remapped.
	 * @return The remapped derivative.
	 * @throws NotInvertibleException
	 */
	public abstract SymbolicElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>  remap( 
			SymbolicElem<EinsteinTensorElem<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,EinsteinTensorElemFactory<Z,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> remap );
	
}


