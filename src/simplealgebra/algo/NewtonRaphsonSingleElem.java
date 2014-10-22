






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






package simplealgebra.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;


public abstract class NewtonRaphsonSingleElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	protected SymbolicElem<R,S> function;
	
	protected R lastValue = null;
	
	protected ArrayList<Elem<?,?>> withRespectTo;
	
	protected HashMap<Elem<?,?>,Elem<?,?>> implicitSpace = null;
	
	protected Elem<?,?> keyElem;
	
	
	public NewtonRaphsonSingleElem( final SymbolicElem<R,S> _function , 
			final ArrayList<Elem<?,?>> _withRespectTo , final Elem<?,?> _keyElem )
	{
		function = _function;
		withRespectTo = _withRespectTo;
		keyElem = _keyElem;
	}
	
	
	public R eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		implicitSpace = implicitSpaceInitialGuess;
		lastValue = function.eval( implicitSpace );
		while( !( iterationsDone() ) )
		{
			performIteration();
		}
		return( lastValue );
	}
	
	
	protected void performIteration() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final R derivative = function.evalPartialDerivative(withRespectTo, implicitSpace);
		final R iterationOffset = lastValue.mult( derivative.invertLeft() ).negate();
		
		final HashMap<Elem<?,?>,Elem<?,?>> newImplicitSpace = new HashMap<Elem<?,?>,Elem<?,?>>();
		
		Iterator<Elem<?,?>> it = implicitSpace.keySet().iterator();
		while( it.hasNext() )
		{
			Elem<?,?> kv = it.next();
			if( kv.equals( keyElem ) )
			{
				R valA = (R)( implicitSpace.get( kv ) );
				valA = valA.add( iterationOffset );
				newImplicitSpace.put( kv , valA );
			}
			else
			{
				newImplicitSpace.put( kv , implicitSpace.get( kv ) );
			}
		}
		
		implicitSpace = newImplicitSpace;
		
		lastValue = function.eval( implicitSpace );
	}
	
	
	protected abstract boolean iterationsDone( );

}



