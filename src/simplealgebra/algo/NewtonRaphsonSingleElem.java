






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
import simplealgebra.symbolic.*;


public abstract class NewtonRaphsonSingleElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	protected SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> function;
	
	protected R lastValue = null;
	
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = null;
	
	protected SymbolicElem<R,S> eval;
	
	protected SymbolicElem<R,S> partialEval;
	
	
	public NewtonRaphsonSingleElem( final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> _function , 
			final ArrayList<? extends Elem<?,?>> _withRespectTo , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		function = _function;
		eval = function.eval( implicitSpaceFirstLevel );
		partialEval = function.evalPartialDerivative(_withRespectTo, implicitSpaceFirstLevel );
	}
	
	
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		implicitSpace = implicitSpaceInitialGuess;
		lastValue = eval.eval( implicitSpace );
		while( !( iterationsDone() ) )
		{
			performIteration();
		}
		return( lastValue );
	}
	
	
	protected void performIteration() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final R derivative = evalPartialDerivative();
		final R iterationOffset = lastValue.mult( derivative.invertLeft() ).negate();
		
		performIterationUpdate( iterationOffset );
		
		lastValue = eval.eval( implicitSpace );
	}
	
	
	protected R evalPartialDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		return( partialEval.eval( implicitSpace ) );
	}
	
	
	
	protected abstract void performIterationUpdate( R iterationOffset );
	
	
	
	protected abstract boolean iterationsDone( );

}



