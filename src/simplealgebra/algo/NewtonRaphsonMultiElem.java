






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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


public abstract class NewtonRaphsonMultiElem<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions;
	
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> lastValues = null;
	
	protected HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace = null;
	
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> evals;
	
	protected SquareMatrixElem<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> partialEvalJacobian;
	
	protected U dim;
	
	protected SymbolicElemFactory<R,S> sfac;
	
	
	public NewtonRaphsonMultiElem( final GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _functions , 
			final ArrayList<ArrayList<? extends Elem<?,?>>> _withRespectTos , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel ,
			final SymbolicElemFactory<R,S> _sfac ,
			final U _dim )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		functions = _functions;
		dim = _dim;
		sfac = _sfac;
		Iterator<HashSet<BigInteger>> ita = functions.getKeyIterator();
		evals = new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>( 
				_sfac , _dim , new GeometricAlgebraOrd<U>() );
		while( ita.hasNext() )
		{
			HashSet<BigInteger> key = ita.next();
			evals.setVal( key , functions.get( key ).eval( implicitSpaceFirstLevel ) );
		}
		partialEvalJacobian = new SquareMatrixElem<U,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>(_sfac, _dim);
		final Iterator<ArrayList<? extends Elem<?, ?>>> itb = _withRespectTos.iterator();
		BigInteger bcnt = BigInteger.ZERO;
		while( itb.hasNext() )
		{
			final BigInteger key = bcnt;
			bcnt = bcnt.add( BigInteger.ONE );
			final ArrayList<? extends Elem<?,?>> withRespectTo = itb.next();
			ita = functions.getKeyIterator();
			while( ita.hasNext() )
			{
				final HashSet<BigInteger> key2A = ita.next();
				final BigInteger key2 = key2A.iterator().next();
				final SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> fun = _functions.get( key2A );
				final SymbolicElem<R,S> evalP = fun.evalPartialDerivative( withRespectTo , implicitSpaceFirstLevel );
				partialEvalJacobian.setVal( key2 , key , evalP );
			}
		}
	}
	
	
	public GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		implicitSpace = implicitSpaceInitialGuess;
		lastValues = evalValues();
		while( !( iterationsDone() ) )
		{
			performIteration();
		}
		return( lastValues );
	}
	
	
	protected void performIteration() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final SquareMatrixElem<U,R,S> derivativeJacobian = evalPartialDerivativeJacobian();
		final SquareMatrixElem<U,R,S> derivativeJacobianInverse = derivativeJacobian.invertLeft();
		
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset =
				new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S>(
						sfac.getFac(), dim, new GeometricAlgebraOrd<U>() );
		lastValues.colVectorMultLeftDefault( derivativeJacobianInverse , iterationOffset );
		iterationOffset = iterationOffset.negate();
		
		performIterationUpdate( iterationOffset );
		
		lastValues = evalValues();
	}
	
	
	protected GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> evalValues( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> ret = new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S>(
				sfac.getFac(), dim, new GeometricAlgebraOrd<U>() );
		
		Iterator<HashSet<BigInteger>> ita = evals.getKeyIterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> key = ita.next();
			ret.setVal( key , evals.get( key ).eval( implicitSpace ) );
		}
		
		return( ret );
	}
	
	
	protected SquareMatrixElem<U,R,S> evalPartialDerivativeJacobian() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final SquareMatrixElem<U,R,S> evalJacobian = new SquareMatrixElem<U,R,S>( sfac.getFac() , dim );
		
		final Iterator<HashSet<BigInteger>> ita = functions.getKeyIterator();
		while( ita.hasNext() )
		{
			final HashSet<BigInteger> key2A = ita.next();
			final BigInteger key2 = key2A.iterator().next();
			final GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>> row = 
						new GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,SymbolicElem<R,S>,SymbolicElemFactory<R,S>>(
								sfac , dim, new GeometricAlgebraOrd<U>() );
			partialEvalJacobian.rowVectorToGeometricAlgebra( key2 , row );
			final Iterator<HashSet<BigInteger>> itb = row.getKeyIterator();
			while( itb.hasNext() )
			{
				final HashSet<BigInteger> keyA = itb.next();
				final BigInteger key = keyA.iterator().next();
				evalJacobian.setVal( key2 , key , partialEvalJacobian.get( key2 , key ).eval( implicitSpace ) );
			}
		}
		
		return( evalJacobian );
	}
	
	
	
	protected abstract void performIterationUpdate( GeometricAlgebraMultivectorElem<U,GeometricAlgebraOrd<U>,R,S> iterationOffset );
	
	
	
	protected abstract boolean iterationsDone( );

}



