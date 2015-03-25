




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





package simplealgebra.stelem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;


/**
 * An elem defining a term that is evaluated over a discretized space such as the discretized
 * space in the Euler Method.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 * @param <K> The type of the implicit space terms that are to be mapped into discretized coordinates.
 */
public abstract class Nelem<R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> extends SymbolicElem<R,S> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _coord Map taking implicit space terms to discrete ordinates of type BigInteger.
	 */
	public Nelem( S _fac , HashMap<K,BigInteger> _coord )
	{
		super( _fac );
		coord = _coord;
	}
	
	/**
	 * Constructs the elem. for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _coord Map taking implicit space terms to discrete ordinates of type BigInteger.
	 * @param ds The Drools session.
	 */
	public Nelem( S _fac , HashMap<K,BigInteger> _coord , DroolsSession ds )
	{
		this( _fac , _coord );
		ds.insert( this );
	}
	
	@Override
	public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	
	/**
	 * Map taking implicit space terms to discrete ordinates of type BigInteger.
	 */
	protected HashMap<K,BigInteger> coord = new HashMap<K,BigInteger>();

}


