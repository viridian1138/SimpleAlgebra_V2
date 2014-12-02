



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
 * Element describing a tensor as defined in General Relativity.
 */
public class EinsteinTensorElem<Z extends Object, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends MutableElem<R, EinsteinTensorElem<Z,R,S>, EinsteinTensorElemFactory<Z,R,S>>  {

	
	public EinsteinTensorElem( S _fac , ArrayList<Z> _contravariantIndices ,
			ArrayList<Z> _covariantIndices )
	{
		fac = _fac;
		contravariantIndices = _contravariantIndices;
		covariantIndices = _covariantIndices;
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> add(EinsteinTensorElem<Z, R, S> b) {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, covariantIndices , contravariantIndices);
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
	 * This implementation assumes that repeated tensor indices happen exactly twice, and on opposite sides of the multiplication.
	 */
	protected void processIndexMatching( ArrayList<Z> b_contravariantIndices , ArrayList<Z> b_covariantIndices ,
			ArrayList<Z> new_contravariantIndices , ArrayList<Z> new_covariantIndices , 
			ArrayList<Integer> matchIndicesA , ArrayList<Integer> matchIndicesB , ArrayList<Integer> nonMatchIndices )
	{
		int ocnt = 0;
		
		
		ArrayList<Z> combinedB = buildCombinedCovariantContravariantIn( b_contravariantIndices , b_covariantIndices );
		
		
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
			Iterator<ArrayList<BigInteger>> ita = matchMap.get(matchMapKey).iterator();
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
		
		return( ret );
	}
	
	
	
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
	
	
	protected ArrayList<Z> buildCombinedCovariantContravariantIn( ArrayList<Z> covariantIndices , ArrayList<Z> contravariantIndices )
	{
		ArrayList<Z> ret = new ArrayList<Z>();
		
		Iterator<Z> it = covariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		it = contravariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		return( ret );
	}
	
	
	protected ArrayList<BigInteger> buildCombinedCovariantContravariantVl( ArrayList<BigInteger> covariantIndices , ArrayList<BigInteger> contravariantIndices )
	{
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		
		Iterator<BigInteger> it = covariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		it = contravariantIndices.iterator();
		while( it.hasNext() )
		{
			ret.add( it.next() );
		}
		
		return( ret );
	}
	
	
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
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, covariantIndices , contravariantIndices);
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
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, covariantIndices , contravariantIndices);
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
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, covariantIndices , contravariantIndices);
		Iterator<ArrayList<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			ArrayList<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali.divideBy(val) );
		}
		return( ret );
	}
	
	
	public BigInteger getTensorRank()
	{
		return( BigInteger.valueOf( contravariantIndices.size() + covariantIndices.size() ) );
	}
	
	
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
		return( new EinsteinTensorElemFactory<Z,R,S>( fac , covariantIndices , contravariantIndices ) );
	}
	
	
	private R get( ArrayList<BigInteger> el )
	{
		return( map.get( el ) );
	}
	
	
	public R getVal( ArrayList<BigInteger> el )
	{
		R val = map.get( el );
		return( val != null ? val : fac.zero() );
	}
	
	public void setVal( ArrayList<BigInteger> el , R val )
	{
		map.put(el, val);
	}
	
	
	/**
	 * @return the contravariantIndices
	 */
	public ArrayList<Z> getContravariantIndices() {
		return contravariantIndices;
	}


	/**
	 * @return the covariantIndices
	 */
	public ArrayList<Z> getCovariantIndices() {
		return covariantIndices;
	}
	
	
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
	}


	
	
	
	private final HashMap<ArrayList<BigInteger>,R> map = new HashMap<ArrayList<BigInteger>,R>();
	
	
	private S fac;
	private ArrayList<Z> contravariantIndices;
	private ArrayList<Z> covariantIndices;
	

}

