






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


/**
 * Remaps a tensor of functions to multivariate Newton-Raphson.
 * 
 * @author thorngreen
 *
 * @param <Z> The type of the tensor indices.
 * @param <R> The nested type.
 * @param <S> The factory for the nested type.
 */
public abstract class NewtonRaphsonMultiElemRemapTensor<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> {
	
	
	/**
	 * The dimensionality for Newton-Raphson.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class Adim extends NumDimensions
	{
		/**
		 * The number of dimensions.
		 */
		BigInteger dim;
		
		/**
		 * Constructs the dimension.
		 * 
		 * @param _dim The number of dimensions.
		 */
		public Adim( BigInteger _dim )
		{
			dim = _dim;
		}

		/**
		 * Gets the number of dimensions.
		 * @return The number of dimensions.
		 */
		@Override
		public BigInteger getVal() {
			return( dim );
		}
		
	};
	
	
	
	/**
	 * Mapping from tensor function indices to Newton-Raphson function indices.
	 */
	protected HashMap<ArrayList<BigInteger>,BigInteger> inMapFun = new HashMap<ArrayList<BigInteger>,BigInteger>();
	
	
	/**
	 * Mapping from Newton-Raphson function indices to tensor function indices.
	 */
	protected HashMap<HashSet<BigInteger>,ArrayList<BigInteger>> outMapFun = new HashMap<HashSet<BigInteger>,ArrayList<BigInteger>>();
	
	
	/**
	 * Mapping from Newton-Raphson offset indices to tensor offset indices.
	 */
	protected HashMap<HashSet<BigInteger>,ArrayList<BigInteger>> outMapOffset = new HashMap<HashSet<BigInteger>,ArrayList<BigInteger>>();
	
	
	/**
	 * Multivariate Newton-Raphson.
	 */
	protected NewtonRaphsonMultiElem<Adim,R,S> newton;
	
	
	/**
	 * Contravariant indices for input tensor.
	 */
	protected ArrayList<Z> contravariantIndices;
	
	/**
	 * Covariant indices for input tensor.
	 */
	protected ArrayList<Z> covariantIndices;
	
	/**
	 * Number of dimensions for Newton-Raphson.
	 */
	protected Adim odim;
	
	/**
	 * Factory for enclosed type.
	 */
	protected S fac;
	
	
	
	
	/**
	 * Constructs the remap.
	 * 
	 * @param _functions Input tensor of functions.
	 * @param _withRespectTosI Set of variables to take derivatives with respect to.
	 * @param implicitSpaceFirstLevel Implicit space for the initial eval.
	 * @param _sfac Factory for enclosed type.
	 * @param _contravariantIndices The contravariant indices of the input tensor.
	 * @param _covariantIndices The covariant indices of the input tensor.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
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
	
	
	/**
	 * Runs Newton-Raphson.
	 * 
	 * @param implicitSpaceInitialGuess The implicit space for the initial guess.
	 * @return An iterated result.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
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
	
	
	/**
	 * Constructs a multivariate Newton-Raphson.
	 * 
	 * @param _functions Input vector of functions.
	 * @param _withRespectTos Set of variables to take derivatives with respect to.
	 * @param implicitSpaceFirstLevel Implicit space for the initial eval.
	 * @param _sfac The enclosed factory.
	 * @param _dim The number of dimensions over which to evaluate.
	 * @return An instance of multivariate Newton-Raphson.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	protected abstract NewtonRaphsonMultiElem<Adim,R,S> genNewton( final GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> _functions , 
			final ArrayList<ArrayList<? extends Elem<?,?>>> _withRespectTos , 
			final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceFirstLevel ,
			final SymbolicElemFactory<R,S> _sfac , Adim _dim ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;

	
	
	/**
	 * Updates function parameters based on the internal Newton-Raphson iteration results.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
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
	
	
	/**
	 * Updates function parameters for tensor iteration results.
	 * 
	 * @param iterationOffset The amount to which the iteration has estimated the function parameter should change.
	 */
	protected abstract void performIterationUpdate( EinsteinTensorElem<Z,R,S> iterationOffset );
	
	
	
	/**
	 * Returns whether the iterations have completed.
	 * 
	 * @return True iff. the iterations are to complete.
	 */
	protected abstract boolean iterationsDone( );

	
	
}




