






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

public class PotentialAlteredCSquared<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends AlteredCSquared<R, S> {
	
	SymbolicElemFactory<R, S> fac;
	SymbolicElem<R, S> cSquared;
	SymbolicElem<R, S> t_2Ux;
	
	/**
	 * 
	 * @param _fac
	 * @param _cSquared
	 * @param _t_2Ux -- 2 * U( x ) where U( x ) is the grav. potential along x.
	 * 
	 *  http://physics.stackexchange.com/questions/33950/what-is-the-equation-of-the-gravitational-potential-in-general-relativity
	 *  
	 *  http://en.wikipedia.org/wiki/Metric_tensor_%28general_relativity%29
	 *  
	 */
	public PotentialAlteredCSquared( SymbolicElemFactory<R, S> _fac , SymbolicElem<R, S> _cSquared , SymbolicElem<R, S> _t_2Ux )
	{
		fac = _fac;
		cSquared = _cSquared;
		t_2Ux = _t_2Ux;
	}
	
	@Override
	public SymbolicElem<R, S> getAlteredCSquared( boolean covariantIndic ) throws NotInvertibleException
	{
		return( covariantIndic ? cSquared.negate().add( t_2Ux ) : cSquared.negate().add( t_2Ux ).invertLeft() );
	}

}

