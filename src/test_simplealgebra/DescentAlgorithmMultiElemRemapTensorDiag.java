




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






package test_simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.algo.DescentAlgorithmMultiElemRemapTensor;
import simplealgebra.algo.DescentAlgorithmMultiElemRemapTensorParam;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Test class that constructs a version of DescentAlgorithmMultiElemRemapTensor for diagonals of
 * rank-two tensors.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public abstract class DescentAlgorithmMultiElemRemapTensorDiag<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends
		DescentAlgorithmMultiElemRemapTensor<Z, R, S> {

	/**
	 * Constructs the remap.
	 * 
	 * @param param Input parameters for the remap.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	public DescentAlgorithmMultiElemRemapTensorDiag(
			final DescentAlgorithmMultiElemRemapTensorParam<Z,R,S> param )
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		super( param );
	}
	
	
	/**
	 * Returns true iff. the tensor key is on the diagonal.
	 * 
	 * @param key The tensor key.  The key is assumed to be for a rank-two tensor.
	 * @return True iff. the tensor key is on the diagonal.
	 */
	protected boolean keyPasses( final ArrayList<BigInteger> key )
	{
		final BigInteger k0 = key.get( 0 );
		final BigInteger k1 = key.get( 1 );
		return( k0.equals( k1 ) );
	}
	
	
	@Override
	protected void mapFunsInputToOutput( final EinsteinTensorElem<Z,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions ,
			final GeometricAlgebraMultivectorElem<Adim,GeometricAlgebraOrd<Adim>,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
				SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> ofun )
	{
		final Iterator<ArrayList<BigInteger>> it = functions.getKeyIterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			
			if( keyPasses( key ) )
			{
				BigInteger key2 = inMapFun.get( key );
			
				HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( key2 );
			
				ofun.setVal( hs , functions.getVal( key ) );
			}
		}
	}
	
	
	
	@Override
	protected BigInteger mapDimCnt( final EinsteinTensorElem<Z,SymbolicElem<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>,
			SymbolicElemFactory<SymbolicElem<R,S>,SymbolicElemFactory<R,S>>> functions )
	{
		BigInteger dimCnt = BigInteger.ZERO;
		
		
		final Iterator<ArrayList<BigInteger>> it = functions.getKeyIterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			
			if( keyPasses( key ) )
			{
				inMapFun.put(key, dimCnt);
			
				HashSet<BigInteger> hs = new HashSet<BigInteger>();
				hs.add( dimCnt );
			
				outMapFun.put( hs , key );
			
				dimCnt = dimCnt.add( BigInteger.ONE );
			}
		}
		
		return( dimCnt );
	}

	
	
}


