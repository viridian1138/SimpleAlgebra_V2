



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




package simplealgebra.jlang;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.WriteElemCache;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Base class for Jlang conversion of DoubleElem expressions.
 * 
 * @author tgreen
 *
 */
public abstract class Jlang_Dbl extends SymbolicElem<DoubleElem, DoubleElemFactory> {

	/**
	 * Constructor.
	 * @param _fac Input factory.
	 */
	public Jlang_Dbl(DoubleElemFactory _fac) {
		super(_fac);
	}

	@Override
	public abstract DoubleElem eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException;

	@Override
	public DoubleElem evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<DoubleElem, DoubleElemFactory> key = new SCacheKey<DoubleElem, DoubleElemFactory>( this , implicitSpace );
		final DoubleElem iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final DoubleElem ret = eval( implicitSpace );
		cache.put( key , ret );
		return( ret );
	}

	@Override
	public DoubleElem evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "Not Supported" ) );
	}

	@Override
	public DoubleElem evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "Not Supported" ) );
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
			PrintStream ps) {
		throw( new RuntimeException( "Not Supported" ) );
	}

	
}

