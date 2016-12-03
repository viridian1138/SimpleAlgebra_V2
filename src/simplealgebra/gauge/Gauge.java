





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






package simplealgebra.gauge;


import java.math.BigInteger;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Factory for returning the components of a gauge.
 * 
 * <P>
 * <P> See <A href="http://en.wikipedia.org/wiki/Gauge_fixing">http://en.wikipedia.org/wiki/Gauge_fixing</A>
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public abstract class Gauge<R extends Elem<R,?>, S extends ElemFactory<R,S>> {

	/**
	 * Returns a component of the gauge.
	 * 
	 * @param numElem The number of dimensions for the gauge.
	 * @return The component of the gauge.
	 * @throws NotInvertibleException
	 */
	public abstract SymbolicElem<R, S> getGauge( BigInteger numElem ) throws NotInvertibleException;
	
}


