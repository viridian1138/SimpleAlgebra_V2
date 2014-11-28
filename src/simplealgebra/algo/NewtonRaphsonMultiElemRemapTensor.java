






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
import simplealgebra.ga.*;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.et.*;


public abstract class NewtonRaphsonMultiElemRemapTensor<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	
	protected static class Adim extends NumDimensions
	{
		BigInteger dim;
		
		public Adim( BigInteger _dim )
		{
			dim = _dim;
		}

		@Override
		public BigInteger getVal() {
			return( dim );
		}
		
	};
	
	
	
	
	protected HashMap<ArrayList<BigInteger>,BigInteger> inMapFun = new HashMap<ArrayList<BigInteger>,BigInteger>();
	
	
	protected HashMap<HashSet<BigInteger>,ArrayList<BigInteger>> outMapFun = new HashMap<HashSet<BigInteger>,ArrayList<BigInteger>>();
	
	
	protected HashMap<HashSet<BigInteger>,ArrayList<BigInteger>> outMapOffset = new HashMap<HashSet<BigInteger>,ArrayList<BigInteger>>();
	
	
	protected NewtonRaphsonMultiElem<Adim,R,S> newton;
	
	
	
	protected ArrayList<Z> contravariantIndices;
	
	protected ArrayList<Z> covariantIndices;
	
	protected Adim odim;
	
	protected S fac;
	
	
	
	
	public NewtonRaphsonMultiElemRemapTensor( final EinsteinTensorElem<Z,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _functions , 
			final HashMap<ArrayList<BigInteger>,ArrayList<? extends Elem<?,?>>> _withRespectTosI , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel ,
			final SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> _sfac ,
			final ArrayList<Z> _contravariantIndices ,
			final ArrayList<Z> _covariantIndices )
					throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		contravariantIndices = _contravariantIndices;
		covariantIndices = _covariantIndices;
		fac = _sfac.getFac().getFac();
		
		
		
		BigInteger dimCnt = BigInteger.ZERO;
		
		
		Iterator<ArrayList<BigInteger>> it = _functions.getKeyIterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			
			inMapFun.put(key, dimCnt);
			
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( dimCnt );
			
			outMapFun.put( hs , key );
			
			dimCnt = dimCnt.add( BigInteger.ONE );
		}
		
		
		odim = new Adim( dimCnt );
		
		
		
		final GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> ofun =
				new GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
					SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>>( _sfac , odim , new GeometricAlgebraOrd<Adim>() );
		
		
		it = _functions.getKeyIterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			
			BigInteger key2 = inMapFun.get( key );
			
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( key2 );
			
			ofun.setVal( hs , _functions.getVal( key ) );
		}
		
		
		
		final ArrayList<ArrayList<? extends Elem<?,?>>> withRespectTos = new ArrayList<ArrayList<? extends Elem<?,?>>>();
		
		it = _withRespectTosI.keySet().iterator();
		BigInteger wcnt = BigInteger.ZERO;
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			
			withRespectTos.add( _withRespectTosI.get( key ) );
			
			HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( wcnt );
			
			outMapOffset.put( hs , key );
			
			wcnt = wcnt.add( BigInteger.ONE );
		}
		
		
		newton = genNewton( ofun , withRespectTos , implicitSpaceFirstLevel , _sfac.getFac() , odim );
		
	}
	
	
	
	public EinsteinTensorElem<Z,R,S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> sv = newton.eval(implicitSpaceInitialGuess);
		EinsteinTensorElem<Z,R,S> ret =
				new EinsteinTensorElem<Z,R,S>( fac , contravariantIndices , covariantIndices );
		
		Iterator<HashSet<BigInteger>> it = sv.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			ret.setVal( outMapFun.get( key ) , sv.get( key ) );
		}
		
		return( ret );
	}
	
	
	
	protected abstract NewtonRaphsonMultiElem<Adim,R,S> genNewton( final GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _functions , 
			final ArrayList<ArrayList<? extends Elem<?,?>>> _withRespectTos , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel ,
			final SymbolicElemFactory<R,S> _sfac , Adim _dim ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;

	
	
	
	protected void performIterationUpdateInternal( GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,R,S> iterationOffset )
	{
		EinsteinTensorElem<Z,R,S> ret =
				new EinsteinTensorElem<Z,R,S>( fac , contravariantIndices , covariantIndices );
		
		Iterator<HashSet<BigInteger>> it = iterationOffset.getKeyIterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			ret.setVal( outMapOffset.get( key ) , iterationOffset.get( key ) );
		}
		
		performIterationUpdate( ret );
	}
	
	
	
	protected abstract void performIterationUpdate( EinsteinTensorElem<Z,R,S> iterationOffset );
	
	
	
	
	protected abstract boolean iterationsDone( );

	
	
}




