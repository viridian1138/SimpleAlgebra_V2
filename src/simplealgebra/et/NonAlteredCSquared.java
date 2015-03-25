






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
 * Factory for an ordinary square of the speed of light.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class NonAlteredCSquared<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends AlteredCSquared<R, S> {
	
	/**
	 * Factory for symbolic elems.
	 */
	SymbolicElemFactory<R, S> fac;
	
	/**
	 * The square of the speed of light.
	 */
	SymbolicElem<R, S> cSquared;
	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac Factory for symbolic elems.
	 * @param _cSquared The square of the speed of light.
	 *  
	 */
	public NonAlteredCSquared( SymbolicElemFactory<R, S> _fac , SymbolicElem<R, S> _cSquared )
	{
		fac = _fac;
		cSquared = _cSquared;
	}
	
	@Override
	public SymbolicElem<R, S> getAlteredCSquared( boolean covariantIndic ) throws NotInvertibleException
	{
		return( covariantIndic ? cSquared.negate() : cSquared.invertLeft().negate() );
	}

}

