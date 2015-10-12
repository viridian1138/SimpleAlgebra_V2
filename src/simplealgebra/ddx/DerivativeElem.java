




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

import java.util.ArrayList;
import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Element representing a derivative.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type of the derivative.
 * @param <S> Factory for the enclosed type of the derivative.
 */
public abstract class DerivativeElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S>
{
	
	/**
	 * Constructs the derivative.
	 * 
	 * @param _fac Factory for the enclosed type of the derivative.
	 */
	public DerivativeElem(S _fac) {
		super(_fac);
	}

	/**
	 * Evaluates the derivative on an expression.
	 * 
	 * @param in The expression to which to apply the derivative.
	 * @param implicitSpace Implicit parameter space against which to perform the evaluation.
	 * @param cache The elem cache.
	 * @return The result of evaluating the derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public abstract R evalDerivativeCached( SymbolicElem<R,S> in , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	/**
	 * Evaluates the derivative on an expression.
	 * 
	 * @param in The expression to which to apply the derivative.
	 * @param implicitSpace Implicit parameter space against which to perform the evaluation.
	 * @return The result of evaluating the derivative.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public abstract R evalDerivative( SymbolicElem<R,S> in , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		throw( new MultiplicativeDistributionRequiredException() );
	}
	
	@Override
	public R evalCached( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ,
			HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		throw( new MultiplicativeDistributionRequiredException() );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		throw( new MultiplicativeDistributionRequiredException() );
	}
	
	@Override
	public R evalPartialDerivativeCached( ArrayList<? extends Elem<?,?>> withRespectTo , 
			HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace , HashMap<SCacheKey<R, S>, R> cache ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		throw( new MultiplicativeDistributionRequiredException() );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( true );
	}

	
}


