



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





package simplealgebra.et;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;



/**
 * Elem. for a tensor in Einstein Notation as described in General Relativity.
 * 
 * 
 * See http://en.wikipedia.org/wiki/Einstein_Notation
 * 
 * 
 * @author thorngreen
 *
 * @param <Z> The type for the tensor indices.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class EinsteinTensorElem<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends MutableElem<R, EinsteinTensorElem<Z,R,S>, EinsteinTensorElemFactory<Z,R,S>>  {

	
	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the nested type.
	 * @param _contravariantIndices The list of contravariant indices.
	 * @param _covariantIndices The list of covariant indices.
	 */
	public EinsteinTensorElem( S _fac , ArrayList<Z> _contravariantIndices ,
			ArrayList<Z> _covariantIndices )
	{
		fac = _fac;
		contravariantIndices = _contravariantIndices;
		covariantIndices = _covariantIndices;
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> add(final EinsteinTensorElem<Z, R, S> ib) {
		
		if( ( map.keySet().size() == 0 ) && ( covariantIndices.size() == 0 ) && ( contravariantIndices.size() == 0 ) )
		{
			return( ib );
		}
		
		if( ( ib.map.keySet().size() == 0 ) && ( ib.covariantIndices.size() == 0 ) && ( ib.contravariantIndices.size() == 0 ) )
		{
			return( this );
		}
		
		
		if( ( covariantIndices.size() != ib.covariantIndices.size() ) 
				|| ( contravariantIndices.size() != ib.contravariantIndices.size() ) )
		{
			System.out.println( "***" );
			Iterator<Z> it = contravariantIndices.iterator();
			while( it.hasNext() )
			{
				System.out.println( it.next() );
			}
			System.out.println( "---" );
			it = covariantIndices.iterator();
			while( it.hasNext() )
			{
				System.out.println( it.next() );
			}
			System.out.println( "---" );
			it = ib.contravariantIndices.iterator();
			while( it.hasNext() )
			{
				System.out.println( it.next() );
			}
			System.out.println( "---" );
			it = ib.covariantIndices.iterator();
			while( it.hasNext() )
			{
				System.out.println( it.next() );
			}
			System.out.println( "***" );
			throw( new RuntimeException( "Mismatch" ) );
		}
		
		
		EinsteinTensorElem<Z, R, S> b = ib;
		
		
		if( !( covariantIndices.equals( ib.covariantIndices ) ) 
				|| !( contravariantIndices.equals( ib.contravariantIndices ) ) )
		{
			b = additionRemap( ib );
		}
		
		
		
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali );
		}
		
		it = b.map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> el = it.next();
			R vali = b.map.get( el );
			R vv = ret.get( el );
			if( vv != null )
			{
				ret.setVal(el, vv.add(vali) );
			}
			else
			{
				ret.setVal(el, vali );
			}
		}
		
		return( ret );
	}
	
	
	
	/**
	 * In instances where the parameter ib has the same set of indices as the tensor
	 * but in a different order, returns a copy the parameter where the ordering of the
	 * indices has been remapped to match.
	 * 
	 * @param ib The parameter to remap.
	 * @return The remapped version of the parameter.
	 */
	protected EinsteinTensorElem<Z, R, S> additionRemap(EinsteinTensorElem<Z, R, S> ib)
	{
		final ArrayList<Integer> am = new ArrayList<Integer>();
		
		Iterator<Z> itz = contravariantIndices.iterator();
		
		while( itz.hasNext() )
		{
			Z ind = itz.next();
			int id = ib.contravariantIndices.indexOf( ind );
			if( id < 0 )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
			am.add( id );
		}
		
		itz = covariantIndices.iterator();
		
		while( itz.hasNext() )
		{
			Z ind = itz.next();
			int id = ib.covariantIndices.indexOf( ind );
			if( id < 0 )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
			am.add( id + contravariantIndices.size() );
		}
		
		
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		
		Iterator<ArrayList<BigInteger>> it = ib.map.keySet().iterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> el = it.next();
			final R vali = ib.map.get( el );
			
			final ArrayList<BigInteger> el2 = new ArrayList<BigInteger>();
			
			for( int cnt = 0 ; cnt < el.size() ; cnt++ )
			{
				el2.add( el.get( am.get( cnt ) ) );
			}
			
			ret.setVal( el2 , vali );
			
		}
		
		return( ret );
	}
	
	
	
	/**
	 * Builds index mapping for a tensor multiplication.
	 * 
	 * This implementation assumes that repeated tensor indices happen exactly twice, and on opposite sides of the multiplication.
	 * 
	 * @param b_contravariantIndices Contravariant indices of the multiplication argument.
	 * @param b_covariantIndices Covariant indices of the multiplication argument.
	 * @param new_contravariantIndices Contravariant indices of the multiplication result.
	 * @param new_covariantIndices Covariant indices of the multiplication result.
	 * @param matchIndicesA List of left-side match indices for the combined key.
	 * @param matchIndicesB List of right-side match indices for the combined key.
	 * @param nonMatchIndices Indices that aren't removed from the multiplication result due to a need to match.
	 */
	protected void processIndexMatching( ArrayList<Z> b_contravariantIndices , ArrayList<Z> b_covariantIndices ,
			ArrayList<Z> new_contravariantIndices , ArrayList<Z> new_covariantIndices , 
			ArrayList<Integer> matchIndicesA , ArrayList<Integer> matchIndicesB , ArrayList<Integer> nonMatchIndices )
	{
		int ocnt = 0;
		
		
		ArrayList<Z> combinedB = buildCombinedContravariantCovariantIn( b_contravariantIndices , b_covariantIndices );
		
		
		{
			Iterator<Z> it = contravariantIndices.iterator();
			while( it.hasNext() )
			{
				Z tstA = it.next();
				final int indB = combinedB.indexOf( tstA );
				if( indB < 0 )
				{
					new_contravariantIndices.add( tstA );
					nonMatchIndices.add( ocnt );
				}
				else
				{
					matchIndicesA.add( ocnt );
					matchIndicesB.add( indB );
					if( indB >= b_contravariantIndices.size() )
					{
						// Does Nothing.
					}
					else
					{
						new_contravariantIndices.add( tstA );
						nonMatchIndices.add( ocnt );
					}
				}
				ocnt++;
			}
		}
		
		
		{
			Iterator<Z> it = covariantIndices.iterator();
			while( it.hasNext() )
			{
				Z tstA = it.next();
				final int indB = combinedB.indexOf( tstA );
				if( indB < 0 )
				{
					new_covariantIndices.add( tstA );
					nonMatchIndices.add( ocnt );
				}
				else
				{
					matchIndicesA.add( ocnt );
					matchIndicesB.add( indB );
					if( indB >= b_contravariantIndices.size() )
					{
						new_covariantIndices.add( tstA );
						nonMatchIndices.add( ocnt );
					}
					else
					{
						// Does Nothing.
					}
				}
				ocnt++;
			}
		}
		
		
		{
			Iterator<Z> it = b_contravariantIndices.iterator();
			while( it.hasNext() )
			{
				Z tstB = it.next();
				if( !( contravariantIndices.contains( tstB ) ) && !( covariantIndices.contains( tstB ) ) )
				{
					new_contravariantIndices.add( tstB );
					nonMatchIndices.add( ocnt );
				}
				ocnt++;
			}
		}
		
		{
			Iterator<Z> it = b_covariantIndices.iterator();
			while( it.hasNext() )
			{
				Z tstB = it.next();
				if( !( contravariantIndices.contains( tstB ) ) && !( covariantIndices.contains( tstB ) ) )
				{
					new_covariantIndices.add( tstB );
					nonMatchIndices.add( ocnt );
				}
				ocnt++;
			}
		}
		
		
	}
	

	
	
	@Override
	public EinsteinTensorElem<Z, R, S> mult(EinsteinTensorElem<Z, R, S> b) {
		
		ArrayList<Integer> matchIndicesA = new ArrayList<Integer>();
		ArrayList<Integer> matchIndicesB = new ArrayList<Integer>();
		ArrayList<Integer> nonMatchIndices = new ArrayList<Integer>();
		
		ArrayList<Z> new_contravariantIndices = new ArrayList<Z>();
		ArrayList<Z> new_covariantIndices = new ArrayList<Z>();
		
		processIndexMatching( b.contravariantIndices , b.covariantIndices ,
				new_contravariantIndices , new_covariantIndices , matchIndicesA , matchIndicesB , nonMatchIndices );
		
		HashMap<ArrayList<BigInteger>,ArrayList<ArrayList<BigInteger>>> matchMap = buildSummationIndexMap( b , matchIndicesB );
		
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>( fac , new_contravariantIndices , new_covariantIndices );
		
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			final R val = map.get(key);
			ArrayList<BigInteger> matchMapKey = buildSummationIndex( key , matchIndicesA );
			final ArrayList<ArrayList<BigInteger>> mmatch = matchMap.get(matchMapKey);
			if( mmatch != null )
			{
				Iterator<ArrayList<BigInteger>> ita = mmatch.iterator();
				while( ita.hasNext() )
				{
					ArrayList<BigInteger> bkey = ita.next();
					final R bval = b.map.get( bkey );
					final R muval = val.mult( bval );
					final ArrayList<BigInteger> combinedAB = buildCombinedAB( key , bkey );
					final ArrayList<BigInteger> placeMapKey = buildSummationIndex( combinedAB , nonMatchIndices );
					final R nval = ret.map.get( placeMapKey );
					if( nval != null )
					{
						ret.map.put(placeMapKey, nval.add( muval ) );
					}
					else
					{
						ret.map.put(placeMapKey, muval );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	
	/**
	 * Populates a map of enclosed elems keyed by unique keys for matches.
	 * 
	 * @param b The tensor from which to generate the map.
	 * @param matchIndices The list of indices in combinedCovariantContravariant that need to match part of the other argument of the multiplication.
	 * @return The map indexed by match keys.
	 */
	protected HashMap<ArrayList<BigInteger>,ArrayList<ArrayList<BigInteger>>> buildSummationIndexMap( EinsteinTensorElem<Z, R, S> b , ArrayList<Integer> matchIndices )
	{
		HashMap<ArrayList<BigInteger>,ArrayList<ArrayList<BigInteger>>> matchMap = new HashMap<ArrayList<BigInteger>,ArrayList<ArrayList<BigInteger>>>();
		
		Iterator<ArrayList<BigInteger>> it = b.map.keySet().iterator();
		
		while( it.hasNext() )
		{
			buildSummationIndexMap( it.next() , matchIndices , matchMap );
		}
		
		return( matchMap );
	}
	
	
	
	/**
	 * Populates one node in a map of enclosed elems keyed by unique keys for matches.
	 * 
	 * @param combinedCovariantContravariant Combined covariant/contravariant indices.
	 * @param matchIndices The list of indices in combinedCovariantContravariant that need to match part of the other argument of the multiplication.
	 * @param matchMap The map to be populated with the new node.
	 */
	protected void buildSummationIndexMap( ArrayList<BigInteger> combinedCovariantContravariant , ArrayList<Integer> matchIndices , 
			HashMap<ArrayList<BigInteger>,ArrayList<ArrayList<BigInteger>>> matchMap )
	{
		ArrayList<BigInteger> summationIndex = buildSummationIndex( combinedCovariantContravariant , matchIndices );
		ArrayList<ArrayList<BigInteger>> match = matchMap.get( summationIndex );
		if( match == null )
		{
			match = new ArrayList<ArrayList<BigInteger>>();
			matchMap.put( summationIndex , match );
		}
		match.add( combinedCovariantContravariant );
	}
	
	
	
	/**
	 * Builds a unique key for matches.
	 * 
	 * @param combinedIndices Combined covariant/contravariant indices.
	 * @param matchIndices The list of indices in combinedIndices that need to match part of the other argument of the multiplication.
	 * @return Returns the unique key for matches.
	 */
	protected ArrayList<BigInteger> buildSummationIndex( ArrayList<BigInteger> combinedIndices , ArrayList<Integer> matchIndices )
	{
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		
		Iterator<Integer> it = matchIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( combinedIndices.get( it.next() ) );
		}
		
		return( ret );
	}
	
	
	/**
	 * Builds a single combined index list containing the contents of both the contravariant indices and covariant indices.
	 * 
	 * @param contravariantIndices Input contravariant indices.
	 * @param covariantIndices Input covariant indices.
	 * @return The concatenation of the two index lists.
	 */
	protected ArrayList<Z> buildCombinedContravariantCovariantIn( ArrayList<Z> contravariantIndices , ArrayList<Z> covariantIndices )
	{
		ArrayList<Z> ret = new ArrayList<Z>();
		
		Iterator<Z> it = contravariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		it = covariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		return( ret );
	}
	
	
	/**
	 * Builds a single combined index list containing the contents of both the contravariant indices and covariant indices.
	 * 
	 * @param contravariantIndices Input contravariant indices.
	 * @param covariantIndices Input covariant indices.
	 * @return The concatenation of the two index lists.
	 */
	protected ArrayList<BigInteger> buildCombinedContravariantCovariantVl( ArrayList<BigInteger> contravariantIndices , ArrayList<BigInteger> covariantIndices )
	{
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		
		Iterator<BigInteger> it = contravariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		it = covariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		return( ret );
	}
	
	
	/**
	 * Builds a single combined key from keys obtained from enclosed elems from either side of the multiplication.
	 * 
	 * @param akey Key from a nested elem from the left-side of the multiplication.
	 * @param bkey Key from a nested elem from the right-side of the multiplication.
	 * @return The concatenation of the two keys.
	 */
	protected ArrayList<BigInteger> buildCombinedAB( ArrayList<BigInteger> akey , ArrayList<BigInteger> bkey )
	{
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		
		Iterator<BigInteger> it = akey.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		it = bkey.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		return( ret );
	}
	

	@Override
	public EinsteinTensorElem<Z, R, S> negate() {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali.negate() );
		}
		return( ret );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, mutr.mutate( vali ) );
		}
		return( ret );
	}

	
	@Override
	public EinsteinTensorElem<Z, R, S> invertLeft() throws NotInvertibleException {
		R sum = fac.zero();
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			final R val = map.get( key );
			sum = sum.add( val );
		}
		final R inv = sum.invertLeft();
		final EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , covariantIndices , contravariantIndices );
		it = map.keySet().iterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			final R val = map.get( key );
			final ArrayList<BigInteger> key2 = new ArrayList<BigInteger>();
			for( int cnt = 0 ; cnt < covariantIndices.size() + contravariantIndices.size() ; cnt++ )
			{
				if( cnt < contravariantIndices.size() )
				{
					key2.add( key.get( cnt + covariantIndices.size() ) );
				}
				else
				{
					key2.add( key.get( cnt - contravariantIndices.size() ) );
				}
			}
			ret.setVal( key2 , inv.mult( val ) );
		}
		return( ret );
	}
	
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> invertRight() throws NotInvertibleException {
		R sum = fac.zero();
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			final R val = map.get( key );
			sum = sum.add( val );
		}
		final R inv = sum.invertRight();
		final EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , covariantIndices , contravariantIndices );
		it = map.keySet().iterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			final R val = map.get( key );
			final ArrayList<BigInteger> key2 = new ArrayList<BigInteger>();
			for( int cnt = 0 ; cnt < covariantIndices.size() + contravariantIndices.size() ; cnt++ )
			{
				if( cnt < contravariantIndices.size() )
				{
					key2.add( key.get( cnt + covariantIndices.size() ) );
				}
				else
				{
					key2.add( key.get( cnt - contravariantIndices.size() ) );
				}
			}
			ret.setVal( key2 , val.mult( inv ) );
		}
		return( ret );
	}
	


	@Override
	public EinsteinTensorElem<Z, R, S> divideBy(int val) {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices );
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali.divideBy(val) );
		}
		return( ret );
	}
	
	
	/**
	 * Returns the rank of the tensor.
	 * 
	 * @return The rank of the tensor.
	 */
	public BigInteger getTensorRank()
	{
		return( BigInteger.valueOf( contravariantIndices.size() + covariantIndices.size() ) );
	}
	
	
	/**
	 * Copies the enclosed elems. in a tensor of rank one to a Geometric Algebra multivector.
	 * 
	 * @param out The multivector into which to copy the elems.
	 */
	public void rankOneTensorToGeometricAlgebra( GeometricAlgebraMultivectorElem<?,?,R,?> out )
	{
		if( !( getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			HashSet<BigInteger> okey = new HashSet<BigInteger>();
			okey.add( key.get( 0 ) );
			R val = map.get( key );
			out.setVal(okey, val);
		}
	}
	
	
	/**
	 * Copies the enclosed elems. in a tensor of rank one to a row vector of a square matrix.
	 * 
	 * @param row The row in which to copy the elems.
	 * @param out The square matrix into which to copy the elems.
	 */
	public void rankOneTensorToRowVector( BigInteger row , SquareMatrixElem<?,R,?> out )
	{
		if( !( getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			BigInteger column = key.get( 0 );
			R val = map.get( key );
			out.setVal(row, column, val);
		}
	}
	
	
	/**
	 * Copies the enclosed elems. in a tensor of rank one to a column vector of a square matrix.
	 * 
	 * @param column The column in which to copy the elems.
	 * @param out The square matrix into which to copy the elems.
	 */
	public void rankOneTensorToColumnVector( BigInteger column , SquareMatrixElem<?,R,?> out )
	{
		if( !( getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			BigInteger row = key.get( 0 );
			R val = map.get( key );
			out.setVal(row, column, val);
		}
	}
	
	
	/**
	 * Copies the enclosed elems. in a tensor of rank two to a square matrix.
	 * 
	 * @param out The square matrix into which to copy the elems.
	 */
	public void rankTwoTensorToSquareMatrix( SquareMatrixElem<?,R,?> out )
	{
		if( !( getTensorRank().equals( BigInteger.valueOf( 2 ) ) ) )
		{
			throw( new RuntimeException( "Not a Rank Two Tensor." ) );
		}
		
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			BigInteger row = key.get(0);
			BigInteger column = key.get(1);
			R val = map.get( key );
			out.setVal(row, column, val);
		}
	}
	
	
	/**
	 * Returns a copy of the tensor with a set of indices removed.
	 * 
	 * @param contravariantReduce The contravariant indices to be removed.
	 * @param covariantReduce The covariant indices to be removed.
	 * @return A copy of the tensor with the indices removed.
	 */
	public EinsteinTensorElem<Z, R, S> indexReduction( HashSet<Z> contravariantReduce , HashSet<Z> covariantReduce )
	{
		final ArrayList<Z> contravar = new ArrayList<Z>();
		final ArrayList<Z> covar = new ArrayList<Z>();
		
		final ArrayList<Integer> coI = new ArrayList<Integer>();
		
		int cnt = 0;
		Iterator<Z> it = contravariantIndices.iterator();
		while( it.hasNext() )
		{
			Z nxt = it.next();
			if( !( contravariantReduce.contains( nxt ) ) )
			{
				contravar.add( nxt );
				coI.add( cnt );
			}
			cnt++;
		}
		
		it = covariantIndices.iterator();
		while( it.hasNext() )
		{
			Z nxt = it.next();
			if( !( covariantReduce.contains( nxt ) ) )
			{
				covar.add( nxt );
				coI.add( cnt );
			}
			cnt++;
		}
		
		
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , contravar , covar );
		
		
		Iterator<ArrayList<BigInteger>> it2 = map.keySet().iterator();
		while( it2.hasNext() )
		{
			final ArrayList<BigInteger> elA = it2.next();
			final R val = map.get( elA );
			
			final ArrayList<BigInteger> elB = new ArrayList<BigInteger>();
			
			
			Iterator<Integer> it3 = coI.iterator();
			while( it3.hasNext() )
			{
				final int ind = it3.next();
				elB.add( elA.get( ind ) );
			}
			
			
			final R av = ret.get( elB );
			if( av != null )
			{
				ret.map.put(elB, av.add(val));
			}
			else
			{
				ret.map.put(elB, val);
			}
			
		}
		
		
		return( ret );
		
	}

	
	@Override
	public EinsteinTensorElemFactory<Z, R, S> getFac() {
		return( new EinsteinTensorElemFactory<Z,R,S>( fac ) );
	}
	
	
	/**
	 * Gets the value at a basis vector.  Null is possible.
	 * 
	 * @param el The basis vector.
	 * @return The value.
	 */
	private R get( ArrayList<BigInteger> el )
	{
		return( map.get( el ) );
	}
	
	
	/**
	 * Gets the value at a basis vector.  Null is not possible.
	 * 
	 * @param el The basis vector.
	 * @return The value.
	 */
	public R getVal( ArrayList<BigInteger> el )
	{
		R val = map.get( el );
		return( val != null ? val : fac.zero() );
	}
	
	
	/**
	 * Sets a basis for the elem.
	 * 
	 * @param el The basis vector.
	 * @param val The value to be set.
	 */
	public void setVal( ArrayList<BigInteger> el , R val )
	{
		map.put(el, val);
	}
	
	
	/**
	 * Returns the list of contravariant indices for the tensor.
	 * 
	 * @return The list of contravariant indices for the tensor.
	 */
	public ArrayList<Z> getContravariantIndices() {
		return contravariantIndices;
	}


	/**
	 * Returns the list of covariant indices for the tensor.
	 * 
	 * @return The list of covariant indices for the tensor.
	 */
	public ArrayList<Z> getCovariantIndices() {
		return covariantIndices;
	}
	
	
	
	/**
	 * Returns a tensor with the same elements, but with the covariant indices renamed to a new set of index names.
	 * 
	 * @param newCovar The new set of index names.
	 * @return The renamed tensor.
	 */
	public EinsteinTensorElem<Z, R, S> regenCovar( ArrayList<Z> newCovar )
	{
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z,R,S>( fac , contravariantIndices , newCovar );
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> key = it.next();
			ret.map.put(key, map.get( key ) );
		}
		return( ret );
	}
	
	
	
	/**
	 * Returns the iterator of index keys for iterating through the set of sparse elements in the tensor.
	 * 
	 * @return The iterator of index keys for iterating through the set of sparse elements in the tensor.
	 */
	public Iterator<ArrayList<BigInteger>> getKeyIterator()
	{
		return( map.keySet().iterator() );
	}
	
	
	
	@Override
	public void validate() throws RuntimeException
	{
		final int TST_SZ = contravariantIndices.size() + covariantIndices.size();
		final Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			final ArrayList<BigInteger> key = it.next();
			if( key.size() != TST_SZ )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
			final R val = map.get( key );
			val.validate();
		}
		Iterator<Z> it2 = contravariantIndices.iterator();
		while( it2.hasNext() )
		{
			if( it2.next() == null )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
		}
		it2 = covariantIndices.iterator();
		while( it2.hasNext() )
		{
			if( it2.next() == null )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
		}
	}
	
	
	
	/**
	 * Debug method for printing the indices in the tensor.
	 */
	public void printIndices()
	{
		System.out.println( "^>>>>>>>>>>>" );
		Iterator<Z> its = contravariantIndices.iterator();
		while( its.hasNext() )
		{
			System.out.println( its.next() );
		}
		System.out.println( "v<<<<<<<<<<<" );
		its = covariantIndices.iterator();
		while( its.hasNext() )
		{
			System.out.println( its.next() );
		}
		System.out.println( "]]]]]]]]]]]" );
	}
	
	


	
	
	
	/**
	 * Sparse map of elements in the tensor.
	 */
	private final HashMap<ArrayList<BigInteger>,R> map = new HashMap<ArrayList<BigInteger>,R>();
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	/**
	 * The contravariant indices of the tensor.
	 */
	private ArrayList<Z> contravariantIndices;
	
	/**
	 * The covariant indices of the tensor.
	 */
	private ArrayList<Z> covariantIndices;
	

}

