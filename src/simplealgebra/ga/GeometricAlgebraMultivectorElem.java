



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





package simplealgebra.ga;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.LoggingConfiguration;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicPlaceholder;
import simplealgebra.symbolic.SymbolicZero;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;


/**
 * A sparse representation of an elem similar to a Geometric Algebra multivector.  The precise
 * elem that is produced depends on the Ord.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the multivector.
 * @param <A> The Ord of the multivector.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class GeometricAlgebraMultivectorElem<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends MutableElem<R, GeometricAlgebraMultivectorElem<U,A,R,S>, GeometricAlgebraMultivectorElemFactory<U,A,R,S>>  {

	/**
	 * Defines enumerated commands for Geometric Algebra multivectors.
	 * 
	 * @author thorngreen
	 *
	 */
	public static enum GeometricAlgebraMultivectorCmd {
		
		/**
		 * An enumerated command for a dot product.
		 */
		DOT,
		
		/**
		 * An enumerated command for a "Hestenes" dot product.
		 * See "Geometric Algebra for Computer Scientists" by L. Dorst.
		 */
		DOT_HESTENES,
		
		/**
		 * The left contraction product.
		 * See "Geometric Algebra for Computer Scientists" by L. Dorst.
		 */
		LEFT_CONTRACTION,
		
		/**
		 * The right contraction product.
		 * See "Geometric Algebra for Computer Scientists" by L. Dorst.
		 */
		RIGHT_CONTRACTION,
		
		/**
		 * An enumerated command for a wedge product.
		 */
		WEDGE,
		
		/**
		 * An enumerated command for a cross product.
		 * Note: the cross product only works with vectors in 3-D for a GeometricAlgebraOrd.
		 */
		CROSS,
		
		/**
		 * The GA scalar product.
		 * See "Geometric Algebra for Computer Scientists" by L. Dorst.
		 */
		SCALAR,
		
		/**
		 * An enumerated command for right-side reversion.
		 */
		REVERSE_LEFT,
		
		/**
		 * An enumerated command for left-side reversion.
		 */
		REVERSE_RIGHT
		
	};
	
	
	/**
	 * Exception indicating the failure of the multivector to invert.
	 * 
	 * @author tgreen
	 *
	 */
	public static final class GaInverseException extends NotInvertibleException
	{
		
		/**
		 * The index of the elem related to the inverse failure.
		 */
		protected HashSet<BigInteger> elemNum;
		
		/**
		 * Constructs the exception.
		 * 
		 * @param elemNum_ The index of the elem related to the inverse failure.
		 */
		public GaInverseException( final HashSet<BigInteger> elemNum_ )
		{
			elemNum = elemNum;
		}
		
		@Override
		public String toString()
		{
			
			return( "No GA Inverse For Index " + elemNum );
		}
		
		
		/**
		 * Returns the index of the elem related to the inverse failure.
		 * 
		 * @return The index of the elem related to the inverse failure.
		 */
		public HashSet<BigInteger> getElemNum()
		{
			return( elemNum );
		}
		
	};
	
	
	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions for the multivector.
	 * @param _ord The Ord of the multivector.
	 */
	public GeometricAlgebraMultivectorElem( S _fac , U _dim , A _ord )
	{
		fac = _fac;
		dim = _dim;
		ord = _ord;
	}
	
	
	/**
	 * Constructs the elem for a scalar value.
	 * 
	 * @param _val The scalar value.
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions for the multivector.
	 * @param _ord The Ord of the multivector.
	 */
	public GeometricAlgebraMultivectorElem( R _val , S _fac , U _dim , A _ord )
	{
		this( _fac , _dim , _ord );
		this.setVal( new HashSet<BigInteger>() , _val );
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> add(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, vali );
		}
		
		for( final Entry<HashSet<BigInteger>,R> ii : b.map.entrySet() )
		{
			HashSet<BigInteger> el = ii.getKey();
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

	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> mult(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				if( negate )
				{
					vmul = vmul.negate();
				}
				R vv = ret.get( el );
				if( vv != null )
				{
					ret.setVal(el, vv.add(vmul) );
				}
				else
				{
					ret.setVal(el, vmul );
				}
			}
		}
		
		return( ret );
	}
	

	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> negate() {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, vali.negate() );
		}
		return( ret );
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, mutr.mutate( vali ) );
		}
		return( ret );
	}
	
	
	/**
	 * Represents the left-side term in one symbolic multiplication
	 * in a multivector multiplication so that the multiplication
	 * can be inverted.
	 * 
	 * @author thorngreen
	 *
	 */
	private class AElem extends SymbolicElem<R, S>
	{
		
		/**
		 * The basis vector for the original elem.
		 */
		private HashSet<BigInteger> indx;
		
		/**
		 * The column for the original elem in the square matrix.
		 */
		private int col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _indx The basis vector for the original elem.
		 * @param _col The column for the original elem in the square matrix.
		 */
		public AElem(S _fac, HashSet<BigInteger> _indx, int _col) {
			super(_fac);
			indx = _indx;
			col = _col;
		}

		@Override
		public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public R evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<R, S>, R> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public R evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<R, S>, R> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
		{
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteGaSetCache( cache.getCacheVal() ) );
				final String stair = ( (WriteGaSetCache)( cache.getAuxCache( WriteGaSetCache.class ) ) ).writeDesc(indx, (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( AElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( AElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( stair );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		/**
		 * Returns the basis vector for the original elem.
		 * 
		 * @return The basis vector for the original elem.
		 */
		public HashSet<BigInteger> getIndx() {
			return indx;
		}
		
		/**
		 * Returns the column for the original elem in the square matrix.
		 * 
		 * @return The column for the original elem in the square matrix.
		 */
		public int getCol() {
			return col;
		}

	}
	
	
	/**
	 * Represents the right-side term in one symbolic multiplication
	 * in a multivector multiplication so that the multiplication
	 * can be inverted.
	 * 
	 * @author thorngreen
	 *
	 */
	private class BElem extends SymbolicElem<R, S>
	{
		/**
		 * The basis vector for the original elem.
		 */
		private HashSet<BigInteger> indx;
		
		/**
		 * The column for the original elem in the square matrix.
		 */
		private int col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _indx The basis vector for the original elem.
		 * @param _col The column for the original elem in the square matrix.
		 */
		public BElem(S _fac, HashSet<BigInteger> _indx, int _col) {
			super(_fac);
			indx = _indx;
			col = _col;
		}

		@Override
		public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public R evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<R, S>, R> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public R evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<R, S>, R> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc( WriteElemCache<SymbolicElem<R,S>,SymbolicElemFactory<R,S>> cache , PrintStream ps )
		{
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
				cache.applyAuxCache( new WriteGaSetCache( cache.getCacheVal() ) );
				final String stair = ( (WriteGaSetCache)( cache.getAuxCache( WriteGaSetCache.class ) ) ).writeDesc(indx, (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( BElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( BElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( stair );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		/**
		 * Returns the basis vector for the original elem.
		 * 
		 * @return The basis vector for the original elem.
		 */
		public HashSet<BigInteger> getIndx() {
			return indx;
		}
		
		/**
		 * Returns the column for the original elem in the square matrix.
		 * 
		 * @return The column for the original elem in the square matrix.
		 */
		public int getCol() {
			return col;
		}
		
	}

	
	/**
	 * Maps a symbolic left-multiplication from one basis vector
	 * into a set of matrix coefficients so that the matrix can be inverted.
	 * 
	 * @param in The symbolic expression for the basis vector.
	 * @param row The matrix row corresponding to the basis vector.
	 * @param out The square matrix into which the coefficients are added.
	 */
	private void handleInvertElemLeft( SymbolicElem<R,S> in , 
			BigInteger row , SquareMatrixElem<NumDimensions,R,S> out )
	{
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<R,S> add = (SymbolicAdd<R,S>) in;
			handleInvertElemLeft( add.getElemA() , row , out );
			handleInvertElemLeft( add.getElemB() , row , out );
			return;
		}
		
		boolean negate = false;
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<R,S> neg = (SymbolicNegate<R,S>) in;
			negate = true;
			in = neg.getElem();
		}
		
		SymbolicMult<R,S> mul = (SymbolicMult<R,S>) in;
		
		AElem ae = (AElem)( mul.getElemA() );
		BElem be = (BElem)( mul.getElemB() );
		
		R val = map.get( be.getIndx() );
		
		if( negate )
		{
			val = val.negate();
		}
		
		
		BigInteger col = BigInteger.valueOf( ae.getCol() );
		
		R vl = out.get( row , col );
		
		
		if( vl != null )
		{
			out.setVal(row, col, vl.add(val) );
		}
		else
		{
			out.setVal(row, col, val);
		}
		
	}
	
	
	/**
	 * Maps a symbolic right-multiplication from one basis vector
	 * into a set of matrix coefficients so that the matrix can be inverted.
	 * 
	 * @param in The symbolic expression for the basis vector.
	 * @param row The matrix row corresponding to the basis vector.
	 * @param out The square matrix into which the coefficients are added.
	 */
	private void handleInvertElemRight( SymbolicElem<R,S> in , 
			BigInteger row , SquareMatrixElem<NumDimensions,R,S> out )
	{
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<R,S> add = (SymbolicAdd<R,S>) in;
			handleInvertElemRight( add.getElemA() , row , out );
			handleInvertElemRight( add.getElemB() , row , out );
			return;
		}
		
		boolean negate = false;
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<R,S> neg = (SymbolicNegate<R,S>) in;
			negate = true;
			in = neg.getElem();
		}
		
		SymbolicMult<R,S> mul = (SymbolicMult<R,S>) in;
		
		AElem ae = (AElem)( mul.getElemA() );
		BElem be = (BElem)( mul.getElemB() );
		
		R val = map.get( ae.getIndx() );
		
		if( negate )
		{
			val = val.negate();
		}
		
		
		BigInteger col = BigInteger.valueOf( be.getCol() );
		
		R vl = out.get( row , col );
		
		
		if( vl != null )
		{
			out.setVal(row, col, vl.add(val) );
		}
		else
		{
			out.setVal(row, col, val);
		}
		
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> invertLeft() throws NotInvertibleException {
		
		final SymbolicElemFactory<R, S> fc = new SymbolicElemFactory<R, S>( fac );
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aA
			= new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim , ord );
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aB
			= new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim , ord );
		
		final int inSz = map.keySet().size();
		
		
		ArrayList<HashSet<BigInteger>> cols = new ArrayList<HashSet<BigInteger>>( inSz );
		
		
		
		int count = 0;
		for( final HashSet<BigInteger> key : map.keySet() )
		{
			AElem ae = new AElem( fac , key , count );
			BElem be = new BElem( fac , key , count );
			aA.setVal(key, ae);
			aB.setVal(key, be);
			cols.add( key );
			count++;
		}
		
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aMult = aA.mult( aB );
		
		final int outSz = aMult.map.keySet().size();
		
		if( outSz != inSz )
		{
			throw( new RuntimeException( "Mismatch " + inSz + " " + outSz ) );
		}
		
		final NumDimensions xdim = new NumDimensions()
		{
			public BigInteger getVal()
			{
				return( BigInteger.valueOf( outSz ) );
			}
		};
		
	
		SquareMatrixElemFactory<NumDimensions,R,S> sqfac = new SquareMatrixElemFactory<NumDimensions,R,S>( fac , xdim );
		
		SquareMatrixElem<NumDimensions,R,S> sq = sqfac.zero();
		
		

		int sindex = -1;
		
		
		
		count = 0;
		for( final Entry<HashSet<BigInteger>, SymbolicElem<R, S>> ii : aMult.map.entrySet() )
		{
			HashSet<BigInteger> id = ii.getKey();
			handleInvertElemLeft( ii.getValue() , 
					BigInteger.valueOf(count) , sq );
			if( id.size() == 0 )
			{
				sindex = count;
			}
			count++;
		}
		
		
		SquareMatrixElem<NumDimensions,R,S> sqInv;
		
		
		try
		{
			sqInv = sq.invertLeft();
		}
		catch( SquareMatrixElem.NoPivotException ex )
		{
			final HashSet<BigInteger> index = cols.get( ex.getElemNum().intValue() );
			throw( new GaInverseException( index ) );
		}
		
		
		GeometricAlgebraMultivectorElemFactory<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S> kfac = 
				new GeometricAlgebraMultivectorElemFactory<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S>(fac, xdim, new GeometricAlgebraOrd<NumDimensions>() );
		
		GeometricAlgebraMultivectorElem<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S> ki = kfac.zero();
		GeometricAlgebraMultivectorElem<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S> ko = kfac.zero();
		
		if( sindex >= 0 )
		{
			HashSet<BigInteger> hv = new HashSet<BigInteger>();
			hv.add( BigInteger.valueOf( sindex ) );
			ki.setVal(hv, fac.identity() );
		}
		
		ki.colVectorMultLeftDefault(sqInv, ko);
		
		
		
		
//		SquareMatrixElem<NumDimensions,R,S> tst = sqInv.mult( sq );
//		
//		int xc;
//		int yc;
//		
//		for( xc = 0 ; xc < outSz ; xc++ )
//		{
//			for( yc = 0 ; yc < outSz ; yc++ )
//			{
//				R vl = tst.getVal( BigInteger.valueOf( xc ) , BigInteger.valueOf( yc ) );
//				System.out.println( "#### " + ( (DoubleElem) vl ).getVal() );
//			}
//		}
		
		
		
		GeometricAlgebraMultivectorElem<U,A, R, S> ret = new GeometricAlgebraMultivectorElem<U,A, R, S>(fac, dim, ord);
		
		
		for( count = 0 ; count < outSz ; count++ )
		{
			HashSet<BigInteger> mm = new HashSet<BigInteger>();
			mm.add( BigInteger.valueOf( count ) );
			R val = ko.get( mm );
			if( val != null )
			{
				ret.setVal(cols.get(count), val);
			}
		}
		
		
		return( ret );
		
	}
	
	

	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> invertRight() throws NotInvertibleException {
		
		final SymbolicElemFactory<R, S> fc = new SymbolicElemFactory<R, S>( fac );
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aA
			= new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim , ord );
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aB
			= new GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim , ord );
		
		final int inSz = map.keySet().size();
		
		
		ArrayList<HashSet<BigInteger>> cols = new ArrayList<HashSet<BigInteger>>( inSz );
		
		
		
		int count = 0;
		for( final HashSet<BigInteger> key : map.keySet() )
		{
			AElem ae = new AElem( fac , key , count );
			BElem be = new BElem( fac , key , count );
			aA.setVal(key, ae);
			aB.setVal(key, be);
			cols.add( key );
			count++;
		}
		
		
		final GeometricAlgebraMultivectorElem<U,A, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aMult = aA.mult( aB );
		
		final int outSz = aMult.map.keySet().size();
		
		if( outSz != inSz )
		{
			throw( new RuntimeException( "Mismatch " + inSz + " " + outSz ) );
		}
		
		final NumDimensions xdim = new NumDimensions()
		{
			public BigInteger getVal()
			{
				return( BigInteger.valueOf( outSz ) );
			}
		};
		
	
		SquareMatrixElemFactory<NumDimensions,R,S> sqfac = new SquareMatrixElemFactory<NumDimensions,R,S>( fac , xdim );
		
		SquareMatrixElem<NumDimensions,R,S> sq = sqfac.zero();
		
		

		int sindex = -1;
		
		
		
		count = 0;
		for( final Entry<HashSet<BigInteger>, SymbolicElem<R, S>> ii : aMult.map.entrySet() )
		{
			HashSet<BigInteger> id = ii.getKey();
			handleInvertElemRight( ii.getValue() , 
					BigInteger.valueOf(count) , sq );
			if( id.size() == 0 )
			{
				sindex = count;
			}
			count++;
		}
		
		
		SquareMatrixElem<NumDimensions,R,S> sqInv;
		
		
		try
		{
				sqInv = sq.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.INVERT_LEFT_REV_COEFF , null);
		}
		catch( SquareMatrixElem.NoPivotException ex )
		{
			final HashSet<BigInteger> index = cols.get( ex.getElemNum().intValue() );
			throw( new GaInverseException( index ) );
		}
		
		
		GeometricAlgebraMultivectorElemFactory<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S> kfac = 
				new GeometricAlgebraMultivectorElemFactory<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S>(fac, xdim, new GeometricAlgebraOrd<NumDimensions>() );
		
		GeometricAlgebraMultivectorElem<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S> ki = kfac.zero();
		GeometricAlgebraMultivectorElem<NumDimensions, GeometricAlgebraOrd<NumDimensions>, R, S> ko = kfac.zero();
		
		if( sindex >= 0 )
		{
			HashSet<BigInteger> hv = new HashSet<BigInteger>();
			hv.add( BigInteger.valueOf( sindex ) );
			ki.setVal(hv, fac.identity() );
		}
		
		ki.colVectorMultRight(sqInv, ko);
		
		
		
		
//		SquareMatrixElem<NumDimensions,R,S> tst = sqInv.mult( sq );
//		
//		int xc;
//		int yc;
//		
//		for( xc = 0 ; xc < outSz ; xc++ )
//		{
//			for( yc = 0 ; yc < outSz ; yc++ )
//			{
//				R vl = tst.getVal( BigInteger.valueOf( xc ) , BigInteger.valueOf( yc ) );
//				System.out.println( "#### " + ( (DoubleElem) vl ).getVal() );
//			}
//		}
		
		
		
		GeometricAlgebraMultivectorElem<U,A, R, S> ret = new GeometricAlgebraMultivectorElem<U,A, R, S>(fac, dim, ord);
		
		
		for( count = 0 ; count < outSz ; count++ )
		{
			HashSet<BigInteger> mm = new HashSet<BigInteger>();
			mm.add( BigInteger.valueOf( count ) );
			R val = ko.get( mm );
			if( val != null )
			{
				ret.setVal(cols.get(count), val);
			}
		}
		
		
		return( ret );
		
	}
	
	
	

	

	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> divideBy(BigInteger val) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> el = ii.getKey();
			R vali = ii.getValue();
			ret.setVal(el, vali.divideBy(val) );
		}
		return( ret );
	}
	
	
	/**
	 * Returns a graded part of the multivector.
	 * 
	 * @param grade The index of the graded part.
	 * @return The graded part.
	 */
	public GeometricAlgebraMultivectorElem<U,A, R, S> getGradedPart( BigInteger grade )
	{
		GeometricAlgebraMultivectorElem<U,A, R, S> ret = new GeometricAlgebraMultivectorElem<U,A, R, S>( fac , dim , ord );
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> key = ii.getKey();
			if( grade.equals( BigInteger.valueOf( key.size() ) ) )
			{
				ret.setVal(key, ii.getValue() );
			}
		}
		return( ret );
	}
	
	
	/**
	 * Copies the vector part of a multivector to a row vector in a square matrix.
	 * 
	 * @param row The row into which to copy the multivector.
	 * @param out The square matrix into which the vector part is to be copied.
	 */
	public void vectorPartToRowVector( BigInteger row , SquareMatrixElem<U, R, ?> out )
	{
		GeometricAlgebraMultivectorElem<U,A, R, S> grd = getGradedPart( BigInteger.ONE );
		for( final Entry<HashSet<BigInteger>,R> ii : grd.map.entrySet() )
		{
			HashSet<BigInteger> key = ii.getKey();
			BigInteger column = key.iterator().next();
			out.setVal(row, column, ii.getValue() );
		}
	}
	
	
	/**
	 * Copies the vector part of the multivector to a column vector in a square matrix.
	 * 
	 * @param column The column into which to copy the multivector.
	 * @param out The square matrix into which the vector part is to be copied.
	 */
	public void vectorPartToColumnVector( BigInteger column , SquareMatrixElem<U, R, ?> out )
	{
		GeometricAlgebraMultivectorElem<U,A, R, S> grd = getGradedPart( BigInteger.ONE );
		for( final Entry<HashSet<BigInteger>,R> ii : grd.map.entrySet() )
		{
			HashSet<BigInteger> key = ii.getKey();
			BigInteger row = key.iterator().next();
			out.setVal(row, column, ii.getValue() );
		}
	}
	
	
	/**
	 * Copies the vector part of the multivector to a tensor of rank one.
	 * 
	 * @param out The tensor into which the vector part is to be copied.
	 */
	public void vectorPartToRankOneTensor( EinsteinTensorElem<?,R,?> out )
	{
		if( !( out.getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		GeometricAlgebraMultivectorElem<U,A, R, S> grd = getGradedPart( BigInteger.ONE );
		for( final Entry<HashSet<BigInteger>,R> ii : grd.map.entrySet() )
		{
			HashSet<BigInteger> key = ii.getKey();
			BigInteger indx = key.iterator().next();
			ArrayList<BigInteger> okey = new ArrayList<BigInteger>();
			okey.add( indx );
			out.setVal(okey, ii.getValue() );
		}
	}
	
	
	/**
	 * Returns the dot product of the multivector with the parameter.
	 * 
	 * @param b The right-side argument of the dot product.
	 * @return The result of the dot product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> dot(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				if( el.size() == Math.abs( kb.size() - ka.size() ) )
				{
					if( negate )
					{
						vmul = vmul.negate();
					}
					R vv = ret.get( el );
					if( vv != null )
					{
						ret.setVal(el, vv.add(vmul) );
					}
					else
					{
						ret.setVal(el, vmul );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	/**
	 * Returns the "Hestenes" dot product of the multivector with the parameter.
	 * See "Geometric Algebra for Computer Scientists" by L. Dorst.
	 * 
	 * @param b The right-side argument of the dot product.
	 * @return The result of the dot product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> dotHestenes(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				final int kas = ka.size();
				final int kbs = kb.size();
				if( ( el.size() == Math.abs( kbs - kas ) ) && ( kas != 0 ) && ( kbs != 0 ) )
				{
					if( negate )
					{
						vmul = vmul.negate();
					}
					R vv = ret.get( el );
					if( vv != null )
					{
						ret.setVal(el, vv.add(vmul) );
					}
					else
					{
						ret.setVal(el, vmul );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	/**
	 * Returns the wedge product of the multivector with the parameter.
	 * 
	 * @param b The right-side argument of the wedge product.
	 * @return The result of the wedge product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> wedge(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				if( el.size() == ( ka.size() + kb.size() ) )
				{
					if( negate )
					{
						vmul = vmul.negate();
					}
					R vv = ret.get( el );
					if( vv != null )
					{
						ret.setVal(el, vv.add(vmul) );
					}
					else
					{
						ret.setVal(el, vmul );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	
	/**
	 * Returns the left contraction product of the multivector with the parameter.
	 * 
	 * @param b The right-side argument of the left contraction product.
	 * @return The result of the left contraction product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> leftContraction(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				if( el.size() == ( kb.size() - ka.size() ) )
				{
					if( negate )
					{
						vmul = vmul.negate();
					}
					R vv = ret.get( el );
					if( vv != null )
					{
						ret.setVal(el, vv.add(vmul) );
					}
					else
					{
						ret.setVal(el, vmul );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	
	/**
	 * Returns the right contraction product of the multivector with the parameter.
	 * 
	 * @param b The right-side argument of the right contraction product.
	 * @return The result of the right contraction product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> rightContraction(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				if( el.size() == ( ka.size() - kb.size() ) )
				{
					if( negate )
					{
						vmul = vmul.negate();
					}
					R vv = ret.get( el );
					if( vv != null )
					{
						ret.setVal(el, vv.add(vmul) );
					}
					else
					{
						ret.setVal(el, vmul );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	
	
	
	/**
	 * Returns the scalar product of the multivector with the parameter.
	 * 
	 * @param b The right-side argument of the scalar product.
	 * @return The result of the scalar product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> scalar(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			HashSet<BigInteger> ka = ii.getKey();
			R va = ii.getValue();
			for( final Entry<HashSet<BigInteger>,R> jj : b.map.entrySet() )
			{
				HashSet<BigInteger> kb = jj.getKey();
				R vb = jj.getValue();
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = ord.calcOrd( ka , kb , el , dim );
				if( el.size() == 0 )
				{
					if( negate )
					{
						vmul = vmul.negate();
					}
					R vv = ret.get( el );
					if( vv != null )
					{
						ret.setVal(el, vv.add(vmul) );
					}
					else
					{
						ret.setVal(el, vmul );
					}
				}
			}
		}
		
		return( ret );
	}
	
	
	
	
	/**
	 * Returns the cross product of the multivector with the parameter.
	 * Note: this operation only works with vectors in 3-D for a GeometricAlgebraOrd.
	 * 
	 * @param b The right-side argument of the cross product.
	 * @return The result of the cross product.
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> cross(GeometricAlgebraMultivectorElem<U,A, R, S> b) {
		final GeometricAlgebraMultivectorElem<U,A,R,S> wdg = wedge( b );
		final GeometricAlgebraMultivectorElem<U,A,R,S> mi = new GeometricAlgebraMultivectorElem<U,A,R,S>(fac,dim,ord);
		
		final HashSet<BigInteger> ia = new HashSet<BigInteger>();
		ia.add( BigInteger.ZERO );
		ia.add( BigInteger.ONE );
		ia.add( BigInteger.valueOf( 2 ) );
		
		final R negOne = fac.negativeIdentity();
		
		mi.setVal(ia, negOne);
		
		return( mi.mult( wdg ) );
	}
	
	
	
	/**
	 * Returns the desired result from multiplying the multivector by its reversion.
	 * 
	 * @return The desired result from multiplying the multivector by its reversion.
	 */
	private GeometricAlgebraMultivectorElem<U,A,R,S> calcReversionMultResult()
	{
		R retA = this.getFac().getFac().zero();
		
		for( final R nxt : map.values() )
		{
			retA = retA.add( nxt.mult( nxt ) );
		}
		
		GeometricAlgebraMultivectorElem<U,A,R,S> ret = getFac().zero();
		
		ret.setVal( new HashSet<BigInteger>() , retA );
		
		return( ret );
	}
	
	
	
	/**
	 * Returns the left-side reversion of the multivector.
	 * 
	 * @return The left-side reversion of the multivector.
	 * @throws NotInvertibleException 
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> reverseLeft() throws NotInvertibleException 
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> ra = invertLeft();
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> rb = calcReversionMultResult();
		
		return( rb.mult( ra ) );
	}
	
	
	
	/**
	 * Returns the right-side reversion of the multivector.
	 * 
	 * @return The right-side reversion of the multivector.
	 * @throws NotInvertibleException 
	 */
	private GeometricAlgebraMultivectorElem<U,A, R, S> reverseRight() throws NotInvertibleException 
	{
		final GeometricAlgebraMultivectorElem<U,A,R,S> ra = invertRight();
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> rb = calcReversionMultResult();
		
		return( ra.mult( rb ) );
	}
	
	
	
	/**
	 * Copies the result of multiplying a square matrix by a row vector on the left.
	 * 
	 * @param in The square matrix to be multiplied.
	 * @param rowVectorOut The multivector into which the result of the multiplication is copied.
	 */
	public void rowVectorMult( SquareMatrixElem<U, R, ?> in , 
			GeometricAlgebraMultivectorElem<U,A, R, S> rowVectorOut )
	{
		final GeometricAlgebraMultivectorElem<U,A, R, S> rowVectIn = this.getGradedPart( BigInteger.ONE );
		for( final Entry<HashSet<BigInteger>,R> ii : rowVectIn.map.entrySet() )
		{
			final HashSet<BigInteger> keyK = ii.getKey();
			final R rowVectInVal = ii.getValue();
			final BigInteger k = keyK.iterator().next();
			final GeometricAlgebraMultivectorElem<U,A, R, S> rowVectMat = new GeometricAlgebraMultivectorElem<U,A, R, S>(fac, dim, ord);
			in.rowVectorToGeometricAlgebra(k, rowVectMat);
			for( final Entry<HashSet<BigInteger>,R> jj : rowVectMat.map.entrySet() )
			{
				final HashSet<BigInteger> keyJ = jj.getKey();
				final R rowVectMatVal = jj.getValue();
				final R val = rowVectInVal.mult( rowVectMatVal );
				final R addVal = rowVectorOut.get(keyJ);
				if( addVal != null )
				{
					rowVectorOut.setVal(keyJ, val.add( addVal ) );
				}
				else
				{
					rowVectorOut.setVal(keyJ, val );
				}
			}
		}
	}
	
	/**
	 * Copies the result of multiplying a square matrix by a column vector on the left.
	 * 
	 * @param in The square matrix to be multiplied.
	 * @param colVectorOut The multivector into which the result of the multiplication is copied.
	 */
	public void colVectorMultLeftDefault( SquareMatrixElem<U, R, ?> in , 
			GeometricAlgebraMultivectorElem<U,A, R, S> colVectorOut )
	{
		final GeometricAlgebraMultivectorElem<U,A, R, S> colVectIn = this.getGradedPart( BigInteger.ONE );
		for( final Entry<HashSet<BigInteger>,R> ii : colVectIn.map.entrySet() )
		{
			final HashSet<BigInteger> keyK = ii.getKey();
			final R colVectInVal = ii.getValue();
			final BigInteger k = keyK.iterator().next();
			final GeometricAlgebraMultivectorElem<U,A, R, S> colVectMat = new GeometricAlgebraMultivectorElem<U,A, R, S>(fac, dim, ord);
			in.columnVectorToGeometricAlgebra(k, colVectMat);
			for( final Entry<HashSet<BigInteger>,R> jj : colVectMat.map.entrySet() )
			{
				final HashSet<BigInteger> keyI = jj.getKey();
				final R colVectMatVal = jj.getValue();
				final R val = colVectMatVal.mult( colVectInVal );
				final R addVal = colVectorOut.get(keyI);
				if( addVal != null )
				{
					colVectorOut.setVal(keyI, val.add( addVal ) );
				}
				else
				{
					colVectorOut.setVal(keyI, val );
				}
			}
		}
	}
	
	
	/**
	 * Copies the result of multiplying a square matrix by a column vector on the right.
	 * 
	 * @param in The input square matrix.
	 * @param colVectorOut The multivector into which the result of the multiplication is copied.
	 */
	public void colVectorMultRight( SquareMatrixElem<U, R, ?> in , 
			GeometricAlgebraMultivectorElem<U,A, R, S> colVectorOut )
	{
		final GeometricAlgebraMultivectorElem<U,A, R, S> colVectIn = this.getGradedPart( BigInteger.ONE );
		for( final Entry<HashSet<BigInteger>,R> ii : colVectIn.map.entrySet() )
		{
			final HashSet<BigInteger> keyK = ii.getKey();
			final R colVectInVal = ii.getValue();
			final BigInteger k = keyK.iterator().next();
			final GeometricAlgebraMultivectorElem<U,A, R, S> colVectMat = new GeometricAlgebraMultivectorElem<U,A, R, S>(fac, dim, ord);
			in.columnVectorToGeometricAlgebra(k, colVectMat);
			for( final Entry<HashSet<BigInteger>,R> jj : colVectMat.map.entrySet() )
			{
				final HashSet<BigInteger> keyI = jj.getKey();
				final R colVectMatVal = jj.getValue();
				final R val = colVectInVal.mult( colVectMatVal );
				final R addVal = colVectorOut.get(keyI);
				if( addVal != null )
				{
					colVectorOut.setVal(keyI, val.add( addVal ) );
				}
				else
				{
					colVectorOut.setVal(keyI, val );
				}
			}
		}
	}
	
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> cloneThread( final BigInteger threadIndex )
	{
		// The NumDimensions dim and the Ord ord are presumed to be immutable.
		final S facs = fac.cloneThread( threadIndex );
		final GeometricAlgebraMultivectorElem<U,A, R, S> ret 
			= new GeometricAlgebraMultivectorElem<U,A, R, S>( facs , dim , ord );
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			final HashSet<BigInteger> key = ii.getKey();
			final R val = ii.getValue();
			final HashSet<BigInteger> keyClone = ( HashSet<BigInteger> )( key.clone() );
			ret.setVal( keyClone , val.cloneThread(threadIndex) );
		}
		return( ret );
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U, A, R, S> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<GeometricAlgebraMultivectorElem<U, A, R, S>, GeometricAlgebraMultivectorElemFactory<U, A, R, S>> cache) 
	{
		final GeometricAlgebraMultivectorElem<U, A, R, S> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		// The NumDimensions dim and the Ord ord are presumed to be immutable.
		final S facs = fac.cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) );
		final GeometricAlgebraMultivectorElem<U,A, R, S> ret 
			= new GeometricAlgebraMultivectorElem<U,A, R, S>( facs , dim , ord );
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			final HashSet<BigInteger> key = ii.getKey();
			final R val = ii.getValue();
			final HashSet<BigInteger> keyClone = ( HashSet<BigInteger> )( key.clone() );
			ret.setVal( keyClone , val.cloneThreadCached( threadIndex , (CloneThreadCache)( cache.getInnerCache() ) ) );
		}
		cache.put(this, ret);
		return( ret );
	}
	
	
	
	/**
	 * Cleans enclosed elems that reduce to zero for approx mode.
	 * @return The cleaned version of the multivector.
	 */
	protected GeometricAlgebraMultivectorElem<U, A, R, S> cleanApprox()
	{
		final GeometricAlgebraMultivectorElem<U, A, R, S> ret = this.getFac().zero();
		
		for( Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			if( !( ii.getValue().evalSymbolicZeroApprox( EVAL_MODE.APPROX ) ) )
			{
				ret.setVal( ii.getKey() , ii.getValue() );
			}
		}
		
		return( ret );
	}
	
	
	
	/**
	 * Populates the return elem, and determines if any of the enclosed elems have changed.
	 * 
	 * @param prev The previous elem that was operated upon.
	 * @param place Structure holding all of the participating placeholders.
	 * @param ret Return elem that is populated.
	 * @return True iff. any of the enclosed elems have changed.
	 */
	protected boolean cleanIterateChanged( final GeometricAlgebraMultivectorElem<U, A, R, S> prev ,
			final HashMap<HashSet<BigInteger>,SymbolicPlaceholder<R,S>> place ,
			final GeometricAlgebraMultivectorElem<U, A, R, S> ret )
	{
		boolean changed = false;
		for( Entry<HashSet<BigInteger>,SymbolicPlaceholder<R,S>> ii : place.entrySet() )
		{
			if( !( ii.getValue().getElem().evalSymbolicZeroApprox( EVAL_MODE.APPROX ) ) )
			{
				ret.setVal( ii.getKey() , ii.getValue().getElem() );
				changed = changed || ( ii.getValue().getElem() != prev.map.get( ii.getKey() ) );
			}
			else
			{
				changed = true;
			}
		}
		return( changed );
	}
	
	
	
	/**
	 * Cleans enclosed elems that reduce to zero.
	 * @param mode The extent to simplify whether the enclosed elems are zero.
	 * @return The cleaned version of the multivector.
	 */
	public  GeometricAlgebraMultivectorElem<U, A, R, S> clean( final EVAL_MODE mode )
	{	
		if( mode == EVAL_MODE.APPROX )
		{
			return( cleanApprox() );
		}
		
		GeometricAlgebraMultivectorElem<U, A, R, S> prev = this;
		StatefulKnowledgeSession session = null;
		HashMap<HashSet<BigInteger>,SymbolicPlaceholder<R,S>> place = null;
		while( true )
		{
			try
			{
				session = mode == EVAL_MODE.SIMPLIFY ?
						getDistributeSimplifyKnowledgeBase().newStatefulKnowledgeSession() : 
						getDistributeSimplify2KnowledgeBase().newStatefulKnowledgeSession();
		
				insertSessionConfigItems( session );
				
				place = new HashMap<HashSet<BigInteger>,SymbolicPlaceholder<R,S>>();
			
				for( Entry<HashSet<BigInteger>,R> ii : prev.map.entrySet() )
				{
					place.put( ii.getKey() , new SymbolicPlaceholder<R,S>( ii.getValue() ) );
				}
			
				for( SymbolicPlaceholder<R,S> ii : place.values() )
				{
					ii.performInserts( session );
				}
					
				session.fireAllRules();
				
				final GeometricAlgebraMultivectorElem<U, A, R, S> ret = this.getFac().zero();
		
				for( Entry<HashSet<BigInteger>,SymbolicPlaceholder<R,S>> ii : place.entrySet() )
				{
					if( !( ii.getValue().getElem().evalSymbolicZeroApprox( EVAL_MODE.APPROX ) ) )
					{
						ret.setVal( ii.getKey() , ii.getValue().getElem() );
					}
				}
		
				session.dispose();
			
				return( ret );
			}
			catch( OutOfMemoryError ex )
			{
				boolean changed = false;
				GeometricAlgebraMultivectorElem<U, A, R, S> ret = null;
				if( place != null )
				{
					ret = this.getFac().zero();
					changed = cleanIterateChanged( prev , place , ret );
				}
				
				/*
				 * Always try to dispose the session after running out of memory.
				 */
				try
				{
					if( session != null )
					{
						session.dispose();
					}
				}
				catch( Throwable ex2 )
				{
					// ex2.printStackTrace( System.out );
				}
				
				/*
				 * If no simplifications were completed, exit with exception.
				 */
				if( ( ret == null ) || !changed )
				{
					throw( ex );
				}
				
				/*
				 * If some simplifications completed before the memory limits ran out,
				 * re-run the session and see if it's possible to get farther on the next run.
				 */
				prev = ret;
				session = null;
				place = null;
			}
		}
	}
	
	
	
	@Override
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		if( map.keySet().isEmpty() )
		{
			return( true );
		}
		
		switch( mode )
		{
			case APPROX:
			{
				return( false );
			}
			
			case SIMPLIFY:
			case SIMPLIFY2:
			{
				for( final R val : map.values() )
				{
					if( !( val.evalSymbolicZeroApprox( mode ) ) )
					{
						return( false );
					}
				}
			}
			
		}
		
		
		return( true );
	}
	
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		if( map.keySet().size() == 1 )
		{
			final R val = map.get( new HashSet<BigInteger>() );
			
			if( val != null )
			{
				return( val.evalSymbolicIdentityApprox( mode ) );
			}
		}
		
		return( false );
	}
	
	
	
	@Override
	public String writeDesc( WriteElemCache<GeometricAlgebraMultivectorElem<U,A,R,S>,GeometricAlgebraMultivectorElemFactory<U,A,R,S>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteGaSetCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WriteOrdCache( cache.getCacheVal() ) );
			final String staDim = dim.writeDesc( (WriteNumDimensionsCache)( cache.getAuxCache( WriteNumDimensionsCache.class ) ) , ps);
			final String staOrd = ord.writeDesc( (WriteOrdCache<U>)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteOrdCache.class)) ) ) , dim, ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			this.getFac().writeElemTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			this.getFac().writeElemTypeString( ps );
			ps.print( "( " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( staDim );
			ps.print( " , " );
			ps.print( staOrd );
			ps.println( " );" );
			for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
			{
				final HashSet<BigInteger> key = ii.getKey();
				final R val = ii.getValue();
				final String stt = val.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
				String stair = ( (WriteGaSetCache)( cache.getAuxCache( WriteGaSetCache.class ) ) ).writeDesc(key, (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) , ps);
				ps.print( st );
				ps.print( ".setVal( " );
				ps.print( stair );
				ps.print( " , " );
				ps.print( stt );
				ps.println( " );" );
			}
		}
		return( st );
	}
	
	
	
	/**
	 * Sorted comparable set for writing to MathML.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class MathMLTreeSet extends TreeSet<BigInteger> implements Comparable<TreeSet<BigInteger>>
	{
		
		/**
		 * Constructs the set.
		 * 
		 * @param in The multivector key from which to construct.
		 */
		public MathMLTreeSet( HashSet<BigInteger> in )
		{
			super();
			for( final BigInteger z : in )
			{
				add( z );
			}
		}

		@Override
		public int compareTo( TreeSet<BigInteger> arg0 ) 
		{
			if( size() < arg0.size() )
			{
				return( -1 );
			}
			
			if( size() > arg0.size() )
			{
				return( 1 );
			}
			
			final Iterator<BigInteger> it = arg0.iterator();
			
			for( final BigInteger z : this )
			{
				final BigInteger zarg = it.next();
				
				final int comp = z.compareTo( zarg );
				
				if( comp != 0 )
				{
					return( comp );
				}
			}
			
			return( 0 );
		}
		
		
	}
	
	
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		
		final TreeMap<MathMLTreeSet,R> sortedMap = new TreeMap<MathMLTreeSet,R>();
		
		for( final Entry<HashSet<BigInteger>,R> ii : map.entrySet() )
		{
			final MathMLTreeSet ts = new MathMLTreeSet( ii.getKey() );
			if( !( ii.getValue() instanceof SymbolicZero ) )
			{
				sortedMap.put( ts , ii.getValue() );
			}
		}
		
		boolean first = true;
		
		for( final Entry<MathMLTreeSet,R> ii : sortedMap.entrySet() )
		{
			if( !first )
			{
				ps.print( "<mo>+</mo>" );
			}
			
			
			if( pc.parenNeeded( this ,  ii.getValue() , false ) )
			{
				pc.getParenthesisGenerator().handleParenthesisOpen(ps);
			}
			else
			{
				ps.print( "<mrow>" );
			}
			
			
			ii.getValue().writeMathML(pc, ps);
			
			
			if( pc.parenNeeded( this ,  ii.getValue() , false ) )
			{
				pc.getParenthesisGenerator().handleParenthesisClose(ps);
			}
			else
			{
				ps.print( "</mrow>" );
			}
			
			
			for( final BigInteger b : ii.getKey() )
			{
				ps.print( "<msub><mi>&sigma;</mi><mn>" );
				ps.print( b );
				ps.print( "</mn></msub>" );
			}
			
			first = false;
		}
		
	}
	
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U,A, R, S> handleOptionalOp( Object id , ArrayList<GeometricAlgebraMultivectorElem<U,A, R, S>> args )  throws NotInvertibleException
	{
		if( id instanceof GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd )
		{
			switch( (GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd) id )
			{
				case DOT:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( dot( b ) );
				}
				// break;
				
				case DOT_HESTENES:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( dotHestenes( b ) );
				}
				// break;
				
				case LEFT_CONTRACTION:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( leftContraction( b ) );
				}
				// break;
				
				case RIGHT_CONTRACTION:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( rightContraction( b ) );
				}
				// break;
				
				case WEDGE:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( wedge( b ) );
				}
				// break;
				
				case CROSS:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( cross( b ) );
				}
				// break;
				
				case SCALAR:
				{
					GeometricAlgebraMultivectorElem<U,A, R, S> b = args.get( 0 );
					return( scalar( b ) );
				}
				// break;
				
				case REVERSE_LEFT:
				{
					return( reverseLeft( ) );
				}
				// break;
				
				case REVERSE_RIGHT:
				{
					return( reverseRight( ) );
				}
				// break;
				
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}

	
	@Override
	public GeometricAlgebraMultivectorElemFactory<U,A, R, S> getFac() {
		return( new GeometricAlgebraMultivectorElemFactory<U,A,R,S>( fac , dim , ord ) );
	}
	
	/**
	 * Gets the value at a basis vector.  Null is possible.
	 * 
	 * @param el The basis vector.
	 * @return The value.
	 */
	public R get( HashSet<BigInteger> el )
	{
		return( map.get( el ) );
	}
	
	/**
	 * Gets the value at a basis vector.  Null is not possible.
	 * 
	 * @param el The basis vector.
	 * @return The value.
	 */
	public R getVal( HashSet<BigInteger> el )
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
	public void setVal( HashSet<BigInteger> el , R val )
	{
		map.put(el, val);
	}
	
	
	/**
	 * Removes a basis from the elem.
	 * 
	 * @param el The index of the elem to remove.
	 */
	public void remove( HashSet<BigInteger> el )
	{
		map.remove( el );
	}
	
	
	/**
	 * Returns the set of the basis vectors of the enclosed elems.
	 * 
	 * @return The set of the basis vectors of the enclosed elems.
	 */
	public Iterable<HashSet<BigInteger>> getKeySet()
	{
		return( map.keySet() );
	}
	
	
	
	/**
	 * Returns the entry set of the multivector.
	 * 
	 * @return The entry set of the multivector.
	 */
	public Iterable<Entry<HashSet<BigInteger>, R>> getEntrySet()
	{
		return( map.entrySet() );
	}
	
	
	
	/**
	 * Returns the set of values in the tensor.
	 * 
	 * @return The set of values in the tensor.
	 */
	public Iterable<R> getValueSet()
	{
		return( map.values() );
	}
	
	
	
	/**
	 * The map of basis vectors to enclosed elems.
	 */
	private final HashMap<HashSet<BigInteger>,R> map = new HashMap<HashSet<BigInteger>,R>();
	
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	/**
	 * The number of dimensions in the multivector.
	 */
	private U dim;
	
	/**
	 * The Ord of the multivector.
	 */
	private A ord;
	

}
