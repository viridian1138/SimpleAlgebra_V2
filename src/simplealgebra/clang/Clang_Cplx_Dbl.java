



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




package simplealgebra.clang;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
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
 * Base class for Clang conversion of ComplexElem<DoubleElem,DoubleElemFactory> expressions.
 * 
 * @author tgreen
 *
 */
public abstract class Clang_Cplx_Dbl extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> {

	/**
	 * Constructor.
	 * @param _fac Input factory.
	 */
	public Clang_Cplx_Dbl(ComplexElemFactory<DoubleElem, DoubleElemFactory> _fac , ArrayList<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _dvs ) {
		super(_fac);
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>[] tin = (SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>[])( new SymbolicElem[ 0 ] );
		dvs = _dvs.toArray( tin );
	}
	
	protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>[] dvs;
	
	/**
	 * Evaluates the symbolic expression.  Results are placed in dvalRe and dvalIm.
	 * 
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public abstract void evalD(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace , SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>[] dvals )
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException;
	
	/**
	 * Stores real result of executing evalD().
	 */
	protected double dvalRe = 0.0;
	
	/**
	 * Stores real result of executing evalD().
	 */
	protected double dvalIm = 0.0;

	@Override
	public ComplexElem<DoubleElem, DoubleElemFactory> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException
	{
		evalD( implicitSpace , dvs );
		final DoubleElem dre = new DoubleElem( dvalRe );
		final DoubleElem dim = new DoubleElem( dvalIm );
		return( new ComplexElem<DoubleElem, DoubleElemFactory>( dre , dim ) );
	}

	@Override
	public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> key = 
				new SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>( this , implicitSpace );
		final ComplexElem<DoubleElem, DoubleElemFactory> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final ComplexElem<DoubleElem, DoubleElemFactory> ret = eval( implicitSpace );
		cache.put( key , ret );
		return( ret );
	}

	@Override
	public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "Not Supported" ) );
	}

	@Override
	public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "Not Supported" ) );
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
			PrintStream ps) {
		throw( new RuntimeException( "Not Supported" ) );
	}

	

	
}


