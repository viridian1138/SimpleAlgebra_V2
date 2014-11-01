



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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.MutableElem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.qtrnn.QuaternionElem;

import simplealgebra.symbolic.*;

import simplealgebra.*;


public class GeometricAlgebraMultivectorElem<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> 
	extends MutableElem<R, GeometricAlgebraMultivectorElem<U,R,S>, GeometricAlgebraMultivectorElemFactory<U,R,S>>  {

	public static enum GeometricAlgebraMultivectorCmd {
		DOT,
		WEDGE,
		REVERSE_LEFT,
		REVERSE_RIGHT
	};
	
	public GeometricAlgebraMultivectorElem( S _fac , U _dim )
	{
		fac = _fac;
		dim = _dim;
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U, R, S> add(GeometricAlgebraMultivectorElem<U, R, S> b) {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali );
		}
		
		it = b.map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> el = it.next();
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

	
	@Override
	public GeometricAlgebraMultivectorElem<U, R, S> mult(GeometricAlgebraMultivectorElem<U, R, S> b) {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		
		Iterator<HashSet<BigInteger>> ita = map.keySet().iterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> ka = ita.next();
			R va = map.get( ka );
			Iterator<HashSet<BigInteger>> itb = b.map.keySet().iterator();
			while( itb.hasNext() )
			{
				HashSet<BigInteger> kb = itb.next();
				R vb = b.map.get( kb );
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = calcOrd( ka , kb , el );
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
	
	
	
	private boolean calcOrd( HashSet<BigInteger> ka , HashSet<BigInteger> kb , HashSet<BigInteger> el )
	{
		boolean negate = false;
		
		final TreeSet<BigInteger> kaa = new TreeSet<BigInteger>( ka );
		final TreeSet<BigInteger> kbb = new TreeSet<BigInteger>( kb );
		
		
		final int sz = kaa.size() + kbb.size();
		
		
		final BigInteger[] arr = new BigInteger[ sz ];
		
		
		int cnt = 0;
		Iterator<BigInteger> it = kaa.iterator();
		while( it.hasNext() )
		{
			arr[ cnt ] = it.next();
			cnt++;
		}
		it = kbb.iterator();
		while( it.hasNext() )
		{
			arr[ cnt ] = it.next();
			cnt++;
		}
		
		
		
		boolean chg = true;
		while( chg )
		{
			chg = false;
			for( cnt = 0 ; cnt < ( sz - 1 ) ; cnt++ )
			{
				final BigInteger a0 = arr[ cnt ];
				final BigInteger a1 = arr[ cnt + 1 ];
				if( ( a0 == null ) && ( a1 != null ) )
				{
					arr[ cnt ] = a1;
					arr[ cnt + 1 ] = a0;
					chg = true;
				}
				else
				{
					if( ( a0 != null ) && ( a1 != null ) )
					{
						final int cmp = a0.compareTo( a1 );
						if( cmp == 0 )
						{
							arr[ cnt ] = null;
							arr[ cnt + 1 ] = null;
							chg = true;
						}
						else
						{
							if( cmp > 0 )
							{
								arr[ cnt ] = a1;
								arr[ cnt + 1 ] = a0;
								chg = true;
								negate = !negate;
							}
						}
					}
				}
			}
		}
		
		
		for( cnt = 0 ; cnt < sz ; cnt++ )
		{
			if( arr[ cnt ] != null )
			{
				el.add( arr[ cnt ] );
			}
		}
		
		
		
		
		return( negate );
	}
	

	@Override
	public GeometricAlgebraMultivectorElem<U, R, S> negate() {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali.negate() );
		}
		return( ret );
	}
	
	
	@Override
	public GeometricAlgebraMultivectorElem<U, R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, mutr.mutate( vali ) );
		}
		return( ret );
	}
	
	
	
	private class AElem extends SymbolicElem<R, S>
	{
		private HashSet<BigInteger> indx;
		private int col;

		
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
		public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeString() {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		/**
		 * @return the indx
		 */
		public HashSet<BigInteger> getIndx() {
			return indx;
		}
		
		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	private class BElem extends SymbolicElem<R, S>
	{
		private HashSet<BigInteger> indx;
		private int col;

		
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
		public R evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeString() {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		/**
		 * @return the indx
		 */
		public HashSet<BigInteger> getIndx() {
			return indx;
		}
		
		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}
		
	}

	
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
	public GeometricAlgebraMultivectorElem<U, R, S> invertLeft() throws NotInvertibleException {
		
		final SymbolicElemFactory<R, S> fc = new SymbolicElemFactory<R, S>( fac );
		
		final GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aA
			= new GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim );
		
		final GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aB
			= new GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim );
		
		final int inSz = map.keySet().size();
		
		
		ArrayList<HashSet<BigInteger>> cols = new ArrayList<HashSet<BigInteger>>( inSz );
		
		
		
		int count = 0;
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			AElem ae = new AElem( fac , key , count );
			BElem be = new BElem( fac , key , count );
			aA.setVal(key, ae);
			aB.setVal(key, be);
			cols.add( key );
			count++;
		}
		
		
		final GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aMult = aA.mult( aB );
		
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
		Iterator<HashSet<BigInteger>> ita = aMult.map.keySet().iterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> id = ita.next();
			handleInvertElemLeft( aMult.get( id ) , 
					BigInteger.valueOf(count) , sq );
			if( id.size() == 0 )
			{
				sindex = count;
			}
			count++;
		}
		
		
		SquareMatrixElem<NumDimensions,R,S> sqInv = sq.invertLeft();
		
		
		GeometricAlgebraMultivectorElemFactory<NumDimensions, R, S> kfac = 
				new GeometricAlgebraMultivectorElemFactory<NumDimensions, R, S>(fac, xdim);
		
		GeometricAlgebraMultivectorElem<NumDimensions, R, S> ki = kfac.zero();
		GeometricAlgebraMultivectorElem<NumDimensions, R, S> ko = kfac.zero();
		
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
		
		
		
		GeometricAlgebraMultivectorElem<U, R, S> ret = new GeometricAlgebraMultivectorElem<U, R, S>(fac, dim);
		
		
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
	public GeometricAlgebraMultivectorElem<U, R, S> invertRight() throws NotInvertibleException {
		
		final SymbolicElemFactory<R, S> fc = new SymbolicElemFactory<R, S>( fac );
		
		final GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aA
			= new GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim );
		
		final GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aB
			= new GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>>( fc , dim );
		
		final int inSz = map.keySet().size();
		
		
		ArrayList<HashSet<BigInteger>> cols = new ArrayList<HashSet<BigInteger>>( inSz );
		
		
		
		int count = 0;
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			AElem ae = new AElem( fac , key , count );
			BElem be = new BElem( fac , key , count );
			aA.setVal(key, ae);
			aB.setVal(key, be);
			cols.add( key );
			count++;
		}
		
		
		final GeometricAlgebraMultivectorElem<U, SymbolicElem<R,S>, SymbolicElemFactory<R,S>> aMult = aA.mult( aB );
		
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
		Iterator<HashSet<BigInteger>> ita = aMult.map.keySet().iterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> id = ita.next();
			handleInvertElemRight( aMult.get( id ) , 
					BigInteger.valueOf(count) , sq );
			if( id.size() == 0 )
			{
				sindex = count;
			}
			count++;
		}
		
		
		SquareMatrixElem<NumDimensions,R,S> sqInv = sq.handleOptionalOp( SquareMatrixElem.SquareMatrixCmd.INVERT_LEFT_REV_COEFF , null);
		
		
		GeometricAlgebraMultivectorElemFactory<NumDimensions, R, S> kfac = 
				new GeometricAlgebraMultivectorElemFactory<NumDimensions, R, S>(fac, xdim);
		
		GeometricAlgebraMultivectorElem<NumDimensions, R, S> ki = kfac.zero();
		GeometricAlgebraMultivectorElem<NumDimensions, R, S> ko = kfac.zero();
		
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
		
		
		
		GeometricAlgebraMultivectorElem<U, R, S> ret = new GeometricAlgebraMultivectorElem<U, R, S>(fac, dim);
		
		
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
	public GeometricAlgebraMultivectorElem<U, R, S> divideBy(int val) {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> el = it.next();
			R vali = map.get( el );
			ret.setVal(el, vali.divideBy(val) );
		}
		return( ret );
	}
	
	
	
	public void toQuaternion( QuaternionElem<U, R, ?> out )
	{
		R v0 = null;
		R vl = null;
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			R val = map.get(key);
			if( !( dim.equals( BigInteger.valueOf( key.size() ) ) ) )
			{
				out.setVal(key, val);
			}
			else
			{
				if( key.size() == 0 )
				{
					v0 = val;
				}
				vl = val;
			}
		}
		if( vl != null )
		{
			vl = vl.negate();
			HashSet<BigInteger> key = new HashSet<BigInteger>();
			if( v0 == null )
			{
				out.setVal(key, vl);
			}
			else
			{
				out.setVal(key, v0.add(vl));
			}
		}
	}
	
	
	public GeometricAlgebraMultivectorElem<U, R, S> getGradedPart( BigInteger grade )
	{
		GeometricAlgebraMultivectorElem<U, R, S> ret = new GeometricAlgebraMultivectorElem<U, R, S>( fac , dim );
		Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			if( grade.equals( BigInteger.valueOf( key.size() ) ) )
			{
				ret.setVal(key, map.get(key) );
			}
		}
		return( ret );
	}
	
	
	public void vectorPartToRowVector( BigInteger row , SquareMatrixElem<U, R, ?> out )
	{
		GeometricAlgebraMultivectorElem<U, R, S> grd = getGradedPart( BigInteger.ONE );
		Iterator<HashSet<BigInteger>> it = grd.map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			BigInteger column = key.iterator().next();
			out.setVal(row, column, grd.map.get(key) );
		}
	}
	
	
	public void vectorPartToColumnVector( BigInteger column , SquareMatrixElem<U, R, ?> out )
	{
		GeometricAlgebraMultivectorElem<U, R, S> grd = getGradedPart( BigInteger.ONE );
		Iterator<HashSet<BigInteger>> it = grd.map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			BigInteger row = key.iterator().next();
			out.setVal(row, column, grd.map.get(key) );
		}
	}
	
	
	public void vectorPartToRankOneTensor( EinsteinTensorElem<?,R,?> out )
	{
		if( !( out.getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		GeometricAlgebraMultivectorElem<U, R, S> grd = getGradedPart( BigInteger.ONE );
		Iterator<HashSet<BigInteger>> it = grd.map.keySet().iterator();
		while( it.hasNext() )
		{
			HashSet<BigInteger> key = it.next();
			BigInteger indx = key.iterator().next();
			ArrayList<BigInteger> okey = new ArrayList<BigInteger>();
			okey.add( indx );
			out.setVal(okey, grd.map.get(key));
		}
	}
	
	
	private GeometricAlgebraMultivectorElem<U, R, S> dot(GeometricAlgebraMultivectorElem<U, R, S> b) {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		
		Iterator<HashSet<BigInteger>> ita = map.keySet().iterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> ka = ita.next();
			R va = map.get( ka );
			Iterator<HashSet<BigInteger>> itb = b.map.keySet().iterator();
			while( itb.hasNext() )
			{
				HashSet<BigInteger> kb = itb.next();
				R vb = b.map.get( kb );
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = calcOrd( ka , kb , el );
				final int maxGrd = Math.max( ka.size() , kb.size() );
				if( el.size() <= maxGrd )
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
	
	
	private GeometricAlgebraMultivectorElem<U, R, S> wedge(GeometricAlgebraMultivectorElem<U, R, S> b) {
		GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
		
		Iterator<HashSet<BigInteger>> ita = map.keySet().iterator();
		while( ita.hasNext() )
		{
			HashSet<BigInteger> ka = ita.next();
			R va = map.get( ka );
			Iterator<HashSet<BigInteger>> itb = b.map.keySet().iterator();
			while( itb.hasNext() )
			{
				HashSet<BigInteger> kb = itb.next();
				R vb = b.map.get( kb );
				R vmul = va.mult( vb );
				HashSet<BigInteger> el = new HashSet<BigInteger>();
				final boolean negate = calcOrd( ka , kb , el );
				final int maxGrd = Math.max( ka.size() , kb.size() );
				if( el.size() > maxGrd )
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
	
	
	
	private boolean negateFromSize( final int sz )
	{
		final int acnt = sz * ( sz - 1 ) / 2;
		
		return( ( acnt % 2 ) == 1 );
	}
	
	
	
	private GeometricAlgebraMultivectorElem<U, R, S> reverseLeft()
	{
		if( fac.isMultCommutative() )
		{
			GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
			Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
			while( it.hasNext() )
			{
				final HashSet<BigInteger> el = it.next();
				final R vali = map.get( el );
				final boolean neg = negateFromSize( el.size() );
				final R valo = neg ? vali.negate() : vali;
				ret.setVal(el, valo );
			}
			return( ret );
		}
		else
		{
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			return( null );
		}
	}
	
	
	
	
	private GeometricAlgebraMultivectorElem<U, R, S> reverseRight()
	{
		if( fac.isMultCommutative() )
		{
			GeometricAlgebraMultivectorElem<U,R,S> ret = new GeometricAlgebraMultivectorElem<U,R,S>(fac,dim);
			Iterator<HashSet<BigInteger>> it = map.keySet().iterator();
			while( it.hasNext() )
			{
				final HashSet<BigInteger> el = it.next();
				final R vali = map.get( el );
				final boolean neg = negateFromSize( el.size() );
				final R valo = neg ? vali.negate() : vali;
				ret.setVal(el, valo );
			}
			return( ret );
		}
		else
		{
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			return( null );
		}
	}
	
	
	
	
	public void rowVectorMult( SquareMatrixElem<U, R, ?> in , 
			GeometricAlgebraMultivectorElem<U, R, S> rowVectorOut )
	{
		final GeometricAlgebraMultivectorElem<U, R, S> rowVectIn = this.getGradedPart( BigInteger.ONE );
		final Iterator<HashSet<BigInteger>> it = rowVectIn.map.keySet().iterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> keyK = it.next();
			final R rowVectInVal = rowVectIn.get( keyK );
			final BigInteger k = keyK.iterator().next();
			final GeometricAlgebraMultivectorElem<U, R, S> rowVectMat = new GeometricAlgebraMultivectorElem<U, R, S>(fac, dim);
			in.rowVectorToGeometricAlgebra(k, rowVectMat);
			final Iterator<HashSet<BigInteger>> ita = rowVectMat.map.keySet().iterator();
			while( ita.hasNext() )
			{
				final HashSet<BigInteger> keyJ = ita.next();
				final R rowVectMatVal = rowVectMat.get( keyJ );
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
	
	
	public void colVectorMultLeftDefault( SquareMatrixElem<U, R, ?> in , 
			GeometricAlgebraMultivectorElem<U, R, S> colVectorOut )
	{
		final GeometricAlgebraMultivectorElem<U, R, S> colVectIn = this.getGradedPart( BigInteger.ONE );
		final Iterator<HashSet<BigInteger>> it = colVectIn.map.keySet().iterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> keyK = it.next();
			final R colVectInVal = colVectIn.get( keyK );
			final BigInteger k = keyK.iterator().next();
			final GeometricAlgebraMultivectorElem<U, R, S> colVectMat = new GeometricAlgebraMultivectorElem<U, R, S>(fac, dim);
			in.columnVectorToGeometricAlgebra(k, colVectMat);
			final Iterator<HashSet<BigInteger>> ita = colVectMat.map.keySet().iterator();
			while( ita.hasNext() )
			{
				final HashSet<BigInteger> keyI = ita.next();
				final R colVectMatVal = colVectMat.get( keyI );
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
	
	
	
	public void colVectorMultRight( SquareMatrixElem<U, R, ?> in , 
			GeometricAlgebraMultivectorElem<U, R, S> colVectorOut )
	{
		final GeometricAlgebraMultivectorElem<U, R, S> colVectIn = this.getGradedPart( BigInteger.ONE );
		final Iterator<HashSet<BigInteger>> it = colVectIn.map.keySet().iterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> keyK = it.next();
			final R colVectInVal = colVectIn.get( keyK );
			final BigInteger k = keyK.iterator().next();
			final GeometricAlgebraMultivectorElem<U, R, S> colVectMat = new GeometricAlgebraMultivectorElem<U, R, S>(fac, dim);
			in.columnVectorToGeometricAlgebra(k, colVectMat);
			final Iterator<HashSet<BigInteger>> ita = colVectMat.map.keySet().iterator();
			while( ita.hasNext() )
			{
				final HashSet<BigInteger> keyI = ita.next();
				final R colVectMatVal = colVectMat.get( keyI );
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
	public GeometricAlgebraMultivectorElem<U, R, S> handleOptionalOp( Object id , ArrayList<GeometricAlgebraMultivectorElem<U, R, S>> args )  throws NotInvertibleException
	{
		if( id instanceof GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd )
		{
			switch( (GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd) id )
			{
				case DOT:
				{
					GeometricAlgebraMultivectorElem<U, R, S> b = args.get( 0 );
					return( dot( b ) );
				}
				// break;
				
				case WEDGE:
				{
					GeometricAlgebraMultivectorElem<U, R, S> b = args.get( 0 );
					return( wedge( b ) );
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
	public GeometricAlgebraMultivectorElemFactory<U, R, S> getFac() {
		return( new GeometricAlgebraMultivectorElemFactory<U,R,S>( fac , dim ) );
	}
	
	
	public R get( HashSet<BigInteger> el )
	{
		return( map.get( el ) );
	}
	
	
	public R getVal( HashSet<BigInteger> el )
	{
		R val = map.get( el );
		return( val != null ? val : fac.zero() );
	}
	
	public void setVal( HashSet<BigInteger> el , R val )
	{
		map.put(el, val);
	}
	
	public Iterator<HashSet<BigInteger>> getKeyIterator()
	{
		return( map.keySet().iterator() );
	}
	
	
	
	private final HashMap<HashSet<BigInteger>,R> map = new HashMap<HashSet<BigInteger>,R>();
	
	
	private S fac;
	private U dim;
	

}
