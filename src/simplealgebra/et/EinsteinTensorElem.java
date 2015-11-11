



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
import java.util.Map.Entry;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;



/**
 * Elem. for representing tensors and tensor-like elements elements using the einstein summation convention.  
 * Note that not all elements in the einstein summation convention are tensors in that not all elements in 
 * the einstein summation convention are coordinate-independent.  However, the definitions in this class 
 * can represent any sum or product in the einstein summation convention.
 * 
 * See http://en.wikipedia.org/wiki/tensor
 * 
 * See http://en.wikipedia.org/wiki/Einstein_Notation
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
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
	 * Defines enumerated commands for Einstein Tensor elems.
	 * 
	 * @author thorngreen
	 *
	 */
	public static enum EinsteinTensorCmd {
		
		/**
		 * An enumerated command for the trace of a rank-two tensor.
		 * 
		 * See https://en.wikipedia.org/wiki/Trace_(linear_algebra)
		 */
		RANK_TWO_TRACE
		
	};
	
	
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
	
	
	/**
	 * Constructs the elem for a scalar input.
	 * 
	 * @param _val The scalar input.
	 * @param _fac The factory for the nested type.
	 */
	public EinsteinTensorElem( R _val , S _fac )
	{
		this( _fac  , new ArrayList<Z>() , new ArrayList<Z>() );
		this.setVal( new ArrayList<BigInteger>() , _val );
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
			for( Z ii : contravariantIndices )
			{
				System.out.println( ii );
			}
			System.out.println( "---" );
			for( Z ii : covariantIndices )
			{
				System.out.println( ii );
			}
			System.out.println( "---" );
			for( Z ii : ib.contravariantIndices )
			{
				System.out.println( ii );
			}
			System.out.println( "---" );
			for( Z ii : ib.covariantIndices )
			{
				System.out.println( ii );
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
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, vali );
		}
		
		
		for( final Entry<ArrayList<BigInteger>,R> ii : b.map.entrySet() )
		{
			ArrayList<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
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
		
		for( final Z ind : contravariantIndices )
		{
			int id = ib.contravariantIndices.indexOf( ind );
			if( id < 0 )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
			am.add( id );
		}
		
		for( final Z ind : covariantIndices )
		{
			int id = ib.covariantIndices.indexOf( ind );
			if( id < 0 )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
			am.add( id + contravariantIndices.size() );
		}
		
		
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		
		for( final Entry<ArrayList<BigInteger>,R> ii : ib.map.entrySet() )
		{
			final ArrayList<BigInteger> el = ii.getKey();
			final R vali = ii.getValue();
			
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
	 * @param param Input parameter.
	 */
	protected void processIndexMatching( final IndexMatchParams<Z> param )
	{
		
		final ArrayList<Z> b_contravariantIndices = param.getB_contravariantIndices();
		final ArrayList<Z> b_covariantIndices = param.getB_covariantIndices();
		final ArrayList<Z> new_contravariantIndices = param.getNew_contravariantIndices();
		final ArrayList<Z> new_covariantIndices = param.getNew_covariantIndices();
		final ArrayList<Integer> matchIndicesA = param.getMatchIndicesA();
		final ArrayList<Integer> matchIndicesB = param.getMatchIndicesB();
		final ArrayList<Integer> nonMatchIndices = param.getNonMatchIndices();
		
		int ocnt = 0;
		
		
		final ArrayList<Z> combinedB = buildCombinedContravariantCovariantIn( b_contravariantIndices , b_covariantIndices );
		
		
		{
			for( final Z tstA : contravariantIndices )
			{
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
			for( final Z tstA : covariantIndices )
			{
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
			for( final Z tstB : b_contravariantIndices )
			{
				if( !( contravariantIndices.contains( tstB ) ) && !( covariantIndices.contains( tstB ) ) )
				{
					new_contravariantIndices.add( tstB );
					nonMatchIndices.add( ocnt );
				}
				ocnt++;
			}
		}
		
		{
			for( final Z tstB : b_covariantIndices )
			{
				if( !( contravariantIndices.contains( tstB ) ) && !( covariantIndices.contains( tstB ) ) )
				{
					new_covariantIndices.add( tstB );
					nonMatchIndices.add( ocnt );
				}
				ocnt++;
			}
		}
		
		
	}
	
	
	
	/**
	 * Input Parameter for processIndexMatching()
	 * 
	 * This documentation should be viewed using Firefox version 33.1.1 or above.
	 * 
	 * @author thorngreen
	 *
	 * @param <Z> The type for the tensor indices.
	 */
	protected static class IndexMatchParams<Z extends Object>
	{
		
		
		
		/**
		 * Gets the contravariant indices of the multiplication argument.
		 * 
		 * @return The contravariant indices of the multiplication argument.
		 */
		public ArrayList<Z> getB_contravariantIndices() {
			return b_contravariantIndices;
		}

		/**
		 * Sets the contravariant indices of the multiplication argument.
		 * 
		 * @param b_contravariantIndices The contravariant indices of the multiplication argument.
		 */
		public void setB_contravariantIndices(ArrayList<Z> b_contravariantIndices) {
			this.b_contravariantIndices = b_contravariantIndices;
		}

		/**
		 * Gets the covariant indices of the multiplication argument.
		 * 
		 * @return The covariant indices of the multiplication argument.
		 */
		public ArrayList<Z> getB_covariantIndices() {
			return b_covariantIndices;
		}

		/**
		 * Sets the covariant indices of the multiplication argument.
		 * 
		 * @param b_covariantIndices The covariant indices of the multiplication argument.
		 */
		public void setB_covariantIndices(ArrayList<Z> b_covariantIndices) {
			this.b_covariantIndices = b_covariantIndices;
		}

		/**
		 * Gets the contravariant indices of the multiplication result.
		 * 
		 * @return The contravariant indices of the multiplication result.
		 */
		public ArrayList<Z> getNew_contravariantIndices() {
			return new_contravariantIndices;
		}

		/**
		 * Gets the covariant indices of the multiplication result.
		 * 
		 * @return The covariant indices of the multiplication result.
		 */
		public ArrayList<Z> getNew_covariantIndices() {
			return new_covariantIndices;
		}

		/**
		 * Gets the list of left-side match indices for the combined key.
		 * 
		 * @return The list of left-side match indices for the combined key.
		 */
		public ArrayList<Integer> getMatchIndicesA() {
			return matchIndicesA;
		}

		/**
		 * Gets the list of right-side match indices for the combined key.
		 * 
		 * @return The list of right-side match indices for the combined key.
		 */
		public ArrayList<Integer> getMatchIndicesB() {
			return matchIndicesB;
		}

		/**
		 * Gets the indices that aren't removed from the multiplication result due to a need to match.
		 * 
		 * @return The indices that aren't removed from the multiplication result due to a need to match.
		 */
		public ArrayList<Integer> getNonMatchIndices() {
			return nonMatchIndices;
		}

		
		/**
		 * Contravariant indices of the multiplication argument.
		 */
		ArrayList<Z> b_contravariantIndices;
		
		/**
		 * Covariant indices of the multiplication argument.
		 */
		ArrayList<Z> b_covariantIndices;
		
		/**
		 * Contravariant indices of the multiplication result.
		 */
		final ArrayList<Z> new_contravariantIndices = new ArrayList<Z>();
		
		/**
		 * Covariant indices of the multiplication result.
		 */
		final ArrayList<Z> new_covariantIndices = new ArrayList<Z>();
		
		/**
		 * List of left-side match indices for the combined key.
		 */
		final ArrayList<Integer> matchIndicesA = new ArrayList<Integer>();
		
		/**
		 * List of right-side match indices for the combined key.
		 */
		final ArrayList<Integer> matchIndicesB = new ArrayList<Integer>();
		
		/**
		 * Indices that aren't removed from the multiplication result due to a need to match.
		 */
		final ArrayList<Integer> nonMatchIndices = new ArrayList<Integer>();
		
	}
	

	
	
	@Override
	public EinsteinTensorElem<Z, R, S> mult(EinsteinTensorElem<Z, R, S> b) {
		
		final IndexMatchParams<Z> param = new IndexMatchParams<Z>();
		
		param.setB_contravariantIndices( b.contravariantIndices );
		param.setB_covariantIndices( b.covariantIndices );
		
		processIndexMatching( param );
		
		final ArrayList<Integer> matchIndicesA = param.getMatchIndicesA();
		final ArrayList<Integer> matchIndicesB = param.getMatchIndicesB();
		final ArrayList<Integer> nonMatchIndices = param.getNonMatchIndices();
		
		final ArrayList<Z> new_contravariantIndices = param.getNew_contravariantIndices();
		final ArrayList<Z> new_covariantIndices = param.getNew_covariantIndices();
		
		final HashMap<ArrayList<BigInteger>,ArrayList<ArrayList<BigInteger>>> matchMap = buildSummationIndexMap( b , matchIndicesB );
		
		final EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>( fac , new_contravariantIndices , new_covariantIndices );
		
		
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			final R val = ii.getValue();
			final ArrayList<BigInteger> matchMapKey = buildSummationIndex( key , matchIndicesA );
			final ArrayList<ArrayList<BigInteger>> mmatch = matchMap.get(matchMapKey);
			if( mmatch != null )
			{
				for( final ArrayList<BigInteger> bkey : mmatch )
				{
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
		
		for( final ArrayList<BigInteger> ii : b.map.keySet() )
		{
			buildSummationIndexMap( ii , matchIndices , matchMap );
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
		
		for( final Integer ii : matchIndices )
		{
			ret.add( combinedIndices.get( ii ) );
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
		
		for( final Z ii : contravariantIndices )
		{
			ret.add( ii );
		}
		
		for( final Z ii : covariantIndices )
		{
			ret.add( ii );
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
		
		for( final BigInteger ii : contravariantIndices )
		{
			ret.add( ii );
		}
		
		for( final BigInteger ii : covariantIndices )
		{
			ret.add( ii );
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
		
		for( final BigInteger ii : akey )
		{
			ret.add( ii );
		}
		
		for( final BigInteger ii : bkey )
		{
			ret.add( ii );
		}
		
		return( ret );
	}
	

	@Override
	public EinsteinTensorElem<Z, R, S> negate() {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, vali.negate() );
		}
		return( ret );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices);
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, mutr.mutate( vali ) );
		}
		return( ret );
	}

	
	@Override
	public EinsteinTensorElem<Z, R, S> invertLeft() throws NotInvertibleException {
		R sum = fac.zero();
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final R val = ii.getValue();
			sum = sum.add( val );
		}
		final R inv = sum.invertLeft();
		final EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , covariantIndices , contravariantIndices );
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			final R val = ii.getValue();
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
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final R val = ii.getValue();
			sum = sum.add( val );
		}
		final R inv = sum.invertRight();
		final EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , covariantIndices , contravariantIndices );
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			final R val = ii.getValue();
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
	public EinsteinTensorElem<Z, R, S> divideBy(BigInteger val) {
		EinsteinTensorElem<Z,R,S> ret = new EinsteinTensorElem<Z,R,S>(fac, contravariantIndices, covariantIndices );
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
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
		
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> key = ii.getKey();
			HashSet<BigInteger> okey = new HashSet<BigInteger>();
			okey.add( key.get( 0 ) );
			R val = ii.getValue();
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
		
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> key = ii.getKey();
			BigInteger column = key.get( 0 );
			R val = ii.getValue();
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
		
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> key = ii.getKey();
			BigInteger row = key.get( 0 );
			R val = ii.getValue();
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
		
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> key = ii.getKey();
			BigInteger row = key.get(0);
			BigInteger column = key.get(1);
			R val = ii.getValue();
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
		for( final Z nxt : contravariantIndices )
		{
			if( !( contravariantReduce.contains( nxt ) ) )
			{
				contravar.add( nxt );
				coI.add( cnt );
			}
			cnt++;
		}
		
		for( final Z nxt : covariantIndices )
		{
			if( !( covariantReduce.contains( nxt ) ) )
			{
				covar.add( nxt );
				coI.add( cnt );
			}
			cnt++;
		}
		
		
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( fac , contravar , covar );
		
		
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> elA = ii.getKey();
			final R val = ii.getValue();
			
			final ArrayList<BigInteger> elB = new ArrayList<BigInteger>();
			
			
			for( Integer ind : coI )
			{
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
	
	
	/**
	 * Returns the trace of a rank-two tensor.  This operation should only be performed with a rank-two tensor.
	 * 
	 * See https://en.wikipedia.org/wiki/Trace_(linear_algebra)
	 * 
	 * @return The trace of the rank-two tensor.
	 */
	public EinsteinTensorElem<Z, R, S> rankTwoTrace()
	{
		R sum = this.getFac().getFac().zero();
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> nxt = ii.getKey();
			if( ( nxt.get( 0 ) ).equals( nxt.get( 1 ) ) )
			{
				sum = sum.add( ii.getValue() );
			}
		}
		return( new EinsteinTensorElem<Z, R, S>( sum , this.getFac().getFac() ) );
	}
	
	
	@Override
	public EinsteinTensorElem<Z, R, S> handleOptionalOp( Object id , ArrayList<EinsteinTensorElem<Z, R, S>> args )  throws NotInvertibleException
	{
		if( id instanceof EinsteinTensorElem.EinsteinTensorCmd )
		{
			switch( (EinsteinTensorElem.EinsteinTensorCmd) id )
			{
				case RANK_TWO_TRACE:
				{
					return( rankTwoTrace( ) );
				}
				// break;
				
			}
		}
		
		return( super.handleOptionalOp(id, args) );
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
	 * Removes a basis from the elem.
	 * 
	 * @param el The index of the elem to remove.
	 */
	public void remove( ArrayList<BigInteger> el )
	{
		map.remove( el );
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
	 * This is used, for instance, when the same term is used in a formula with a different index from its original definition.
	 * 
	 * @param newCovar The new set of index names.
	 * @return The renamed tensor.
	 */
	public EinsteinTensorElem<Z, R, S> regenCovar( ArrayList<Z> newCovar )
	{
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z,R,S>( fac , contravariantIndices , newCovar );
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> key = ii.getKey();
			ret.map.put(key, ii.getValue() );
		}
		return( ret );
	}
	
	
	
	/**
	 * Returns a tensor with the same elements, but with the contravariant indices renamed to a new set of index names.
	 * This is used, for instance, when the same term is used in a formula with a different index from its original definition.
	 * 
	 * @param newContravar The new set of index names.
	 * @return The renamed tensor.
	 */
	public EinsteinTensorElem<Z, R, S> regenContravar( ArrayList<Z> newContravar )
	{
		EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z,R,S>( fac , newContravar , covariantIndices );
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			ArrayList<BigInteger> key = ii.getKey();
			ret.map.put(key, ii.getValue() );
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
	public EinsteinTensorElem<Z, R, S> cloneThread( final BigInteger threadIndex )
	{
		final S facs = this.getFac().getFac().cloneThread(threadIndex);
		// The indices in each array list are presumed to be immutable.
		final ArrayList<Z> contravars = (ArrayList<Z>)( contravariantIndices.clone() );
		final ArrayList<Z> covars = (ArrayList<Z>)( covariantIndices.clone() );
		final EinsteinTensorElem<Z, R, S> ret = new EinsteinTensorElem<Z, R, S>( facs , contravars , covars );
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			final R val = ii.getValue();
			final R vals = val.cloneThread(threadIndex);
			final ArrayList<BigInteger> keys = (ArrayList<BigInteger>)( key.clone() );
			ret.setVal(keys, vals);
		}
		return( ret );
	}
	
	
	
	@Override
	public void validate() throws RuntimeException
	{
		final int TST_SZ = contravariantIndices.size() + covariantIndices.size();
		for( final Entry<ArrayList<BigInteger>,R> ii : map.entrySet() )
		{
			final ArrayList<BigInteger> key = ii.getKey();
			if( key.size() != TST_SZ )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
			final R val = ii.getValue();
			val.validate();
		}
		for( final Z ii : contravariantIndices )
		{
			if( ii == null )
			{
				throw( new RuntimeException( "Mismatch" ) );
			}
		}
		for( final Z ii : covariantIndices )
		{
			if( ii == null )
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
		for( final Z ii : contravariantIndices )
		{
			System.out.println( ii );
		}
		System.out.println( "v<<<<<<<<<<<" );
		for( final Z ii : covariantIndices )
		{
			System.out.println( ii );
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

