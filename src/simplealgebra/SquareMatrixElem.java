



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





package simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicZero;


/**
 * An elem for a sparse square matrix.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the matrix.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SquareMatrixElem<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	MutableElem<R,SquareMatrixElem<U,R,S>, SquareMatrixElemFactory<U,R,S>> {
	
	/**
	 * Defines enumerated commands for square matrices.
	 * 
	 * @author thorngreen
	 *
	 */
	public static enum SquareMatrixCmd {
		
		/**
		 * Enumerated command to left-invert for reversed nested elems.
		 */
		INVERT_LEFT_REV_COEFF,
		
		/**
		 * Enumerated command to right-invert for reversed nested elems.
		 */
		INVERT_RIGHT_REV_COEFF,
		
		/**
		 * Enumerated command for multiplication by the parameter with reversed nested elems.
		 */
		MULT_REV_COEFF,
		
		/**
		 * Enumerated command for the transpose of the square matrix.
		 */
		TRANSPOSE
	};

	
	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the nested elem.
	 * @param _dim The number of dimensions.
	 */
	public SquareMatrixElem( S _fac , U _dim )
	{
		fac = _fac;
		dim = _dim;
	}
	
	
	@Override
	public SquareMatrixElem<U, R, S> add(SquareMatrixElem<U, R, S> b) {
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		
		{
			Iterator<BigInteger> rowi = rowMap.keySet().iterator();
			while( rowi.hasNext() )
			{
				BigInteger row = rowi.next();
				HashMap<BigInteger,R> subMap = rowMap.get( row );
				Iterator<BigInteger> coli = subMap.keySet().iterator();
				while( coli.hasNext() )
				{
					BigInteger col = coli.next();
					R vali = subMap.get( col );
					ret.setVal(row, col, vali );
				}
			}
		}
		
		
		{
			Iterator<BigInteger> rowi = b.rowMap.keySet().iterator();
			while( rowi.hasNext() )
			{
				BigInteger row = rowi.next();
				HashMap<BigInteger,R> subMap = b.rowMap.get( row );
				Iterator<BigInteger> coli = subMap.keySet().iterator();
				while( coli.hasNext() )
				{
					BigInteger col = coli.next();
					R vali = subMap.get( col );
					R valp = ret.get( row , col );
					if( valp == null  )
					{
						ret.setVal(row, col, vali);
					}
					else
					{
						ret.setVal(row, col, valp.add( vali ) );
					}
				}
			}
		}
		
		return( ret );
	}

	
	@Override
	public SquareMatrixElem<U, R, S> mult(SquareMatrixElem<U, R, S> b) {
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		
		Iterator<BigInteger> rowi = rowMap.keySet().iterator();
		while( rowi.hasNext() )
		{
			BigInteger row = rowi.next();
			HashMap<BigInteger,R> subMapRowa = rowMap.get( row );
			Iterator<BigInteger> colj = b.columnMap.keySet().iterator();
			while( colj.hasNext() )
			{
				BigInteger col = colj.next();
				HashMap<BigInteger,R> subMapColb = b.columnMap.get( col );
				R val = null;
				Iterator<BigInteger> vk = ( subMapRowa.size() < subMapColb.size() ) ? 
						subMapRowa.keySet().iterator() : subMapColb.keySet().iterator();
				while( vk.hasNext() )
				{
					BigInteger k = vk.next();
					R av = subMapRowa.get( k );
					R bv = subMapColb.get( k );
					if( ( av != null ) && ( bv != null ) )
					{
						R prod = av.mult( bv );
						if( val == null )
						{
							val = prod;
						}
						else
						{
							val = val.add( prod );
						}
					}
				}
				if( val != null )
				{
					ret.setVal(row, col, val);
				}
			}
		}
		return( ret );
		
	}
	
	
	/**
	 * Returns a multiplication by the parameter with reversed nested elems.
	 * 
	 * @param b The input parameter.
	 * @return The result of the multiplication.
	 */
	private SquareMatrixElem<U, R, S> multRevCoeff(SquareMatrixElem<U, R, S> b) {
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		
		Iterator<BigInteger> rowi = rowMap.keySet().iterator();
		while( rowi.hasNext() )
		{
			BigInteger row = rowi.next();
			HashMap<BigInteger,R> subMapRowa = rowMap.get( row );
			Iterator<BigInteger> colj = b.columnMap.keySet().iterator();
			while( colj.hasNext() )
			{
				BigInteger col = colj.next();
				HashMap<BigInteger,R> subMapColb = b.columnMap.get( col );
				R val = null;
				Iterator<BigInteger> vk = ( subMapRowa.size() < subMapColb.size() ) ? 
						subMapRowa.keySet().iterator() : subMapColb.keySet().iterator();
				while( vk.hasNext() )
				{
					BigInteger k = vk.next();
					R av = subMapRowa.get( k );
					R bv = subMapColb.get( k );
					if( ( av != null ) && ( bv != null ) )
					{
						R prod = bv.mult( av );
						if( val == null )
						{
							val = prod;
						}
						else
						{
							val = val.add( prod );
						}
					}
				}
				if( val != null )
				{
					ret.setVal(row, col, val);
				}
			}
		}
		return( ret );
		
	}
	

	@Override
	public SquareMatrixElem<U, R, S> negate() {
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		Iterator<BigInteger> rowi = rowMap.keySet().iterator();
		while( rowi.hasNext() )
		{
			BigInteger row = rowi.next();
			HashMap<BigInteger,R> subMap = rowMap.get( row );
			Iterator<BigInteger> coli = subMap.keySet().iterator();
			while( coli.hasNext() )
			{
				BigInteger col = coli.next();
				R vali = subMap.get( col );
				ret.setVal(row, col, vali.negate() );
			}
		}
		return( ret );
	}
	
	
	@Override
	public SquareMatrixElem<U, R, S> mutate( Mutator<R> mutr ) throws NotInvertibleException {
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		Iterator<BigInteger> rowi = rowMap.keySet().iterator();
		while( rowi.hasNext() )
		{
			BigInteger row = rowi.next();
			HashMap<BigInteger,R> subMap = rowMap.get( row );
			Iterator<BigInteger> coli = subMap.keySet().iterator();
			while( coli.hasNext() )
			{
				BigInteger col = coli.next();
				R vali = subMap.get( col );
				ret.setVal(row, col, mutr.mutate( vali ) );
			}
		}
		return( ret );
	}
	
	
	
	@Override
	public SquareMatrixElem<U, R, S> invertRight() throws NotInvertibleException {
		SquareMatrixElem<U,R,S> copy = icopy();
		return( copy.iinvertRight() );
	}
	
	
	/**
	 * Returns a right-side inverse of reversed nested elems.
	 * 
	 * @return A right-side inverse of reversed nested elems.
	 * @throws NotInvertibleException
	 */
	private SquareMatrixElem<U, R, S> invertRightRevCoeff() throws NotInvertibleException {
		SquareMatrixElem<U,R,S> copy = icopy();
		return( copy.iinvertRightRevCoeff() );
	}
	

	@Override
	public SquareMatrixElem<U, R, S> invertLeft() throws NotInvertibleException {
		SquareMatrixElem<U,R,S> copy = icopy();
		return( copy.iinvertLeft() );
	}
	
	
	/**
	 * Returns a left-side inverse of reversed nested elems.
	 * 
	 * @return A left-side inverse of reversed nested elems.
	 * @throws NotInvertibleException
	 */
	private SquareMatrixElem<U, R, S> invertLeftRevCoeff() throws NotInvertibleException {
		SquareMatrixElem<U,R,S> copy = icopy();
		return( copy.iinvertLeftRevCoeff() );
	}
	
	
	/**
	 * Returns a right-side inverse.  Leaves the "this" matrix reduced to the identity.
	 * 
	 * @return A right-side inverse.
	 * @throws NotInvertibleException
	 */
	private SquareMatrixElem<U, R, S> iinvertRight() throws NotInvertibleException
	{
//		for( int cntx = 0 ; cntx < 6 ; cntx++ ) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		{
//			System.out.print( " [ " );
//			for( int cnty = 0 ; cnty < 6 ; cnty++ )
//			{
//				System.out.print( " " );
//				DoubleElem d = (DoubleElem)( this.getVal( BigInteger.valueOf( cnty ) , BigInteger.valueOf( cntx ) ) );
//				System.out.print( d.getVal() );
//			}
//			System.out.println( " ] " );
//		 }
//		 System.out.println( "" );
		
		SquareMatrixElem<U,R,S> ret = getFac().identity();
		
		final BigInteger max = dim.getVal();
		BigInteger cnt = BigInteger.ZERO;
		while( cnt.compareTo( max ) < 0 )
		{
//			System.out.println( ">>>> " + cnt ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			System.out.println( "" );
//			for( int cntx = 0 ; cntx < 6 ; cntx++ ) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			{
//				System.out.print( " [ " );
//				for( int cnty = 0 ; cnty < 6 ; cnty++ )
//				{
//					System.out.print( " " );
//					DoubleElem d = (DoubleElem)( this.getVal( BigInteger.valueOf( cnty ) , BigInteger.valueOf( cntx ) ) );
//					System.out.print( d.getVal() );
//				}
//				System.out.println( " ] " );
//			 }
//			 System.out.println( "" );
			 
			final R mv = setUpColumnRight( cnt , ret );
			
//			System.out.println( "#### " + cnt ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			System.out.println( "" );
//			for( int cntx = 0 ; cntx < 6 ; cntx++ ) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			{
//				System.out.print( " [ " );
//				for( int cnty = 0 ; cnty < 6 ; cnty++ )
//				{
//					System.out.print( " " );
//					DoubleElem d = (DoubleElem)( this.getVal( BigInteger.valueOf( cnty ) , BigInteger.valueOf( cntx ) ) );
//					System.out.print( d.getVal() );
//				}
//				System.out.println( " ] " );
//			 }
//			 System.out.println( "" );
//			 
//			 System.out.println( "** " + ( (DoubleElem) mv ).getVal() );
			
			multiplyThroughColumnRight( cnt , mv );
			ret.multiplyThroughColumnRight( cnt , mv);
			
			if( mv instanceof SymbolicElem )
			{
				if( !( this.getVal(cnt, cnt) instanceof SymbolicIdentity ) )
				{
//					SymbolicMult ae = (SymbolicMult)( this.getVal(cnt, cnt) );
//					SymbolicInvertRight sr = (SymbolicInvertRight)( ae.getElemB() );
//					if( sr.getElem() == ae.getElemA() )
//					{
						this.setVal(cnt, cnt, fac.identity() );
//					}
//					else
//					{
//						throw( new RuntimeException( "Fail." ) );
//					}
				}
			}
			
			BigInteger srcCol = cnt;
			BigInteger destCol = BigInteger.ZERO;
			while( destCol.compareTo( max ) < 0 )
			{
				if( destCol.compareTo(srcCol) != 0 )
				{
					R mult = this.getVal(srcCol, destCol);
					columnSubtractRight( srcCol , destCol , mult );
					ret.columnSubtractRight(srcCol, destCol, mult);
					
					if( mult instanceof SymbolicElem )
					{
						R elAA = this.getVal(srcCol, destCol);
						if( elAA instanceof SymbolicZero )
						{
							// Drop Through
						}
						else
						{
							this.setVal(srcCol, destCol, fac.zero() );
						}
	//					else if( elAA instanceof SymbolicAdd )
	//					{
	//						SymbolicAdd el = (SymbolicAdd)( elAA );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicNegate elB = (SymbolicNegate)( el.getElemB() );
	//						SymbolicMult elbm = (SymbolicMult)( elB.getElem() );
	//						if( ! ( elbm.getElemA() instanceof SymbolicIdentity ) )
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					
	//						if( elbm.getElemB() == elA )
	//						{
	//							this.setVal(srcCol, destCol, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else if( elAA instanceof SymbolicNegate )
	//					{
	//						SymbolicMult el = (SymbolicMult)( ( (SymbolicNegate) elAA ).getElem() );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicElem elB = el.getElemB();
	//						if( ( elA instanceof SymbolicZero ) || ( elB instanceof SymbolicZero ) )
	//						{
	//							this.setVal(srcCol, destCol, fac.zero() );
	//						}
	//						else
	//						{
	//							// System.out.println( ((SymbolicElem)(elAA)).writeString() );
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else
	//					{
	//						// System.out.println( ((SymbolicElem)(elAA)).writeString() );
	//						throw( new RuntimeException( "Fail" ) );
	//					}
					}
					
				}
				
				destCol = destCol.add( BigInteger.ONE );
			}
			
			cnt = cnt.add( BigInteger.ONE );
		}
		
		return( ret );
	}
	
	
	/**
	 * Returns a right-side inverse of reversed nested elems.  Leaves the "this" matrix reduced to the identity.
	 * 
	 * @return A right-side inverse of reversed nested elems.
	 * @throws NotInvertibleException
	 */
	private SquareMatrixElem<U, R, S> iinvertRightRevCoeff() throws NotInvertibleException
	{
		SquareMatrixElem<U,R,S> ret = getFac().identity();
		
		final BigInteger max = dim.getVal();
		BigInteger cnt = BigInteger.ZERO;
		while( cnt.compareTo( max ) < 0 )
		{
			final R mv = setUpColumnLeft( cnt , ret );
			
			multiplyThroughColumnLeft( cnt , mv );
			ret.multiplyThroughColumnLeft( cnt , mv);
			
			if( mv instanceof SymbolicElem )
			{
				if( !( this.getVal(cnt, cnt) instanceof SymbolicIdentity ) )
				{
//					SymbolicMult ae = (SymbolicMult)( this.getVal(cnt, cnt) );
//					SymbolicInvertLeft sl = (SymbolicInvertLeft)( ae.getElemA() );
//					if( sl.getElem() == ae.getElemB() )
//					{
						this.setVal(cnt, cnt, fac.identity() );
//					}
//					else
//					{
//						throw( new RuntimeException( "Fail." ) );
//					}
				}
			}
			
			BigInteger srcCol = cnt;
			BigInteger destCol = BigInteger.ZERO;
			while( destCol.compareTo( max ) < 0 )
			{
				if( destCol.compareTo(srcCol) != 0 )
				{
					R mult = this.getVal(srcCol, destCol);
					columnSubtractLeft( srcCol , destCol , mult );
					ret.columnSubtractLeft(srcCol, destCol, mult);
					
					if( mult instanceof SymbolicElem )
					{
						R elAA = this.getVal(srcCol, destCol);
						if( elAA instanceof SymbolicZero )
						{
							// Drop Through
						}
						else
						{
							this.setVal(srcCol, destCol, fac.zero() );
						}
	//					else if( elAA instanceof SymbolicAdd )
	//					{
	//						SymbolicAdd el = (SymbolicAdd)( elAA );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicNegate elB = (SymbolicNegate)( el.getElemB() );
	//						SymbolicMult elbm = (SymbolicMult)( elB.getElem() );
	//						if( ! ( elbm.getElemB() instanceof SymbolicIdentity ) )
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					
	//						if( elbm.getElemA() == elA )
	//						{
	//							this.setVal(srcCol, destCol, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else if( elAA instanceof SymbolicNegate )
	//					{
	//						SymbolicMult el = (SymbolicMult)( ( (SymbolicNegate) elAA ).getElem() );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicElem elB = el.getElemB();
	//						if( ( elA instanceof SymbolicZero ) || ( elB instanceof SymbolicZero ) )
	//						{
	//							this.setVal(srcCol, destCol, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else
	//					{
	//						// System.out.println( ((SymbolicElem)(elAA)).writeString() );
	//						throw( new RuntimeException( "Fail" ) );
	//					}
					}
					
				}
				
				destCol = destCol.add( BigInteger.ONE );
			}
			
			cnt = cnt.add( BigInteger.ONE );
		}
		
		return( ret );
	}
	
	
	/**
	 * Returns a left-side inverse.  Leaves the "this" matrix reduced to the identity.
	 * 
	 * @return A left-side inverse.
	 * @throws NotInvertibleException
	 */
	private SquareMatrixElem<U, R, S> iinvertLeft() throws NotInvertibleException
	{
//		for( int cntx = 0 ; cntx < 6 ; cntx++ ) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		{
//			System.out.print( " [ " );
//			for( int cnty = 0 ; cnty < 6 ; cnty++ )
//			{
//				System.out.print( " " );
//				DoubleElem d = (DoubleElem)( this.getVal( BigInteger.valueOf( cntx ) , BigInteger.valueOf( cnty ) ) );
//				System.out.print( d.getVal() );
//			}
//			System.out.println( " ] " );
//		 }
//		 System.out.println( "" );
		
		SquareMatrixElem<U,R,S> ret = getFac().identity();
		
		final BigInteger max = dim.getVal();
		BigInteger cnt = BigInteger.ZERO;
		while( cnt.compareTo( max ) < 0 )
		{
//			System.out.println( ">>>> " + cnt ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			
//			System.out.println( "" );
//			for( int cntx = 0 ; cntx < 6 ; cntx++ ) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			{
//				System.out.print( " [ " );
//				for( int cnty = 0 ; cnty < 6 ; cnty++ )
//				{
//					System.out.print( " " );
//					DoubleElem d = (DoubleElem)( this.getVal( BigInteger.valueOf( cntx ) , BigInteger.valueOf( cnty ) ) );
//					System.out.print( d.getVal() );
//				}
//				System.out.println( " ] " );
//			 }
//			 System.out.println( "" );
			
			final R mv = setUpRowLeft( cnt , ret );
			
//			System.out.println( "#### " + cnt ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			
//			System.out.println( "" );
//			for( int cntx = 0 ; cntx < 6 ; cntx++ ) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			{
//				System.out.print( " [ " );
//				for( int cnty = 0 ; cnty < 6 ; cnty++ )
//				{
//					System.out.print( " " );
//					DoubleElem d = (DoubleElem)( this.getVal( BigInteger.valueOf( cntx ) , BigInteger.valueOf( cnty ) ) );
//					System.out.print( d.getVal() );
//				}
//				System.out.println( " ] " );
//			 }
//			 System.out.println( "" );
//			
//			System.out.println( "** " + ( (DoubleElem) mv ).getVal() );
			
			multiplyThroughRowLeft( cnt , mv );
			ret.multiplyThroughRowLeft( cnt , mv);
			
			if( mv instanceof SymbolicElem )
			{
				if( !( this.getVal(cnt, cnt) instanceof SymbolicIdentity ) )
				{
//					SymbolicMult ae = (SymbolicMult)( this.getVal(cnt, cnt) );
//					SymbolicInvertLeft sl = (SymbolicInvertLeft)( ae.getElemA() );
//					if( sl.getElem() == ae.getElemB() )
//					{
						this.setVal(cnt, cnt, fac.identity() );
//					}
//					else
//					{
//						throw( new RuntimeException( "Fail." ) );
//					}
				}
			}
			
			BigInteger srcRow = cnt;
			BigInteger destRow = BigInteger.ZERO;
			while( destRow.compareTo( max ) < 0 )
			{
				if( destRow.compareTo(srcRow) != 0 )
				{
					R mult = this.getVal(destRow, srcRow);
					rowSubtractLeft( srcRow , destRow , mult );
					ret.rowSubtractLeft(srcRow, destRow, mult);
					
					if( mult instanceof SymbolicElem )
					{
						R elAA = this.getVal(destRow, srcRow);
						if( elAA instanceof SymbolicZero )
						{
							// Drop Through
						}
						else
						{
							this.setVal(destRow, srcRow, fac.zero() );
						}
	//					else if( elAA instanceof SymbolicAdd )
	//					{
	//						SymbolicAdd el = (SymbolicAdd)( elAA );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicNegate elB = (SymbolicNegate)( el.getElemB() );
	//						SymbolicMult elbm = (SymbolicMult)( elB.getElem() );
	//						if( ! ( elbm.getElemB() instanceof SymbolicIdentity ) )
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					
	//						if( elbm.getElemA() == elA )
	//						{
	//							this.setVal(destRow, srcRow, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else if( elAA instanceof SymbolicNegate )
	//					{
	//						SymbolicMult el = (SymbolicMult)( ( (SymbolicNegate) elAA ).getElem() );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicElem elB = el.getElemB();
	//						if( ( elA instanceof SymbolicZero ) || ( elB instanceof SymbolicZero ) )
	//						{
	//							this.setVal(destRow, srcRow, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else
	//					{
	//						System.out.println( ((SymbolicElem)(elAA)).writeString() );
	//						throw( new RuntimeException( "Fail" ) );
	//					}
					}
					
				}
				
				destRow = destRow.add( BigInteger.ONE );
			}
			
			cnt = cnt.add( BigInteger.ONE );
		}
		
		return( ret );
	}
	
	
	/**
	 * Returns a left-side inverse of reversed nested elems.  Leaves the "this" matrix reduced to the identity.
	 * 
	 * @return A left-side inverse of reversed nested elems.
	 * @throws NotInvertibleException
	 */
	private SquareMatrixElem<U, R, S> iinvertLeftRevCoeff() throws NotInvertibleException
	{
		SquareMatrixElem<U,R,S> ret = getFac().identity();
		
		final BigInteger max = dim.getVal();
		BigInteger cnt = BigInteger.ZERO;
		while( cnt.compareTo( max ) < 0 )
		{
			final R mv = setUpRowRight( cnt , ret );
			
			multiplyThroughRowRight( cnt , mv );
			ret.multiplyThroughRowRight( cnt , mv);
			
			if( mv instanceof SymbolicElem )
			{
				if( !( this.getVal(cnt, cnt) instanceof SymbolicIdentity ) )
				{
	//				SymbolicMult ae = (SymbolicMult)( this.getVal(cnt, cnt) );
	//				SymbolicInvertRight sr = (SymbolicInvertRight)( ae.getElemB() );
	//				if( ae.getElemA() == sr.getElem() )
	//				{
						this.setVal(cnt, cnt, fac.identity() );
	//				}
	//				else
	//				{
	//					throw( new RuntimeException( "Fail." ) );
	//				}
				}
			}
			
			BigInteger srcRow = cnt;
			BigInteger destRow = BigInteger.ZERO;
			while( destRow.compareTo( max ) < 0 )
			{
				if( destRow.compareTo(srcRow) != 0 )
				{
					R mult = this.getVal(destRow, srcRow);
					rowSubtractRight( srcRow , destRow , mult );
					ret.rowSubtractRight(srcRow, destRow, mult);
					
					if( mult instanceof SymbolicElem )
					{
						R elAA = this.getVal(destRow, srcRow);
						if( elAA instanceof SymbolicZero )
						{
							// Drop Through
						}
						else
						{
							this.setVal(destRow, srcRow, fac.zero() );
						}
	//					else if( elAA instanceof SymbolicAdd )
	//					{
	//						SymbolicAdd el = (SymbolicAdd)( this.getVal(destRow, srcRow) );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicNegate elB = (SymbolicNegate)( el.getElemB() );
	//						SymbolicMult elbm = (SymbolicMult)( elB.getElem() );
	//						if( ! ( elbm.getElemA() instanceof SymbolicIdentity ) )
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					
	//						if( elbm.getElemB() == elA )
	//						{
	//							this.setVal(destRow, srcRow, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else if( elAA instanceof SymbolicNegate )
	//					{
	//						SymbolicMult el = (SymbolicMult)( ( (SymbolicNegate) elAA ).getElem() );
	//						SymbolicElem elA = el.getElemA();
	//						SymbolicElem elB = el.getElemB();
	//						if( ( elA instanceof SymbolicZero ) || ( elB instanceof SymbolicZero ) )
	//						{
	//							this.setVal(destRow, srcRow, fac.zero() );
	//						}
	//						else
	//						{
	//							throw( new RuntimeException( "Fail." ) );
	//						}
	//					}
	//					else
	//					{
	//						// System.out.println( ((SymbolicElem)(elAA)).writeString() );
	//						throw( new RuntimeException( "Fail" ) );
	//					}
					}
					
				}
				
				destRow = destRow.add( BigInteger.ONE );
			}
			
			cnt = cnt.add( BigInteger.ONE );
		}
		
		return( ret );
	}
	
	
	/**
	 * Subtracts from destCol the mult parameter times the srcCol.
	 * 
	 * @param srcCol The source column.
	 * @param destCol The destination column.
	 * @param mult The multiplier parameter.
	 */
	private void columnSubtractLeft( BigInteger srcCol , BigInteger destCol , R mult )
	{
		HashMap<BigInteger,R> subMapSrc = columnMap.get( srcCol );
		HashMap<BigInteger,R> subMapDest = columnMap.get( destCol );
		if( subMapSrc != null )
		{
			Iterator<BigInteger> it = subMapSrc.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger row = it.next();
				R srcVal = subMapSrc.get( row );
				R srcMultNegated = mult.mult( srcVal ).negate();
				R dstVal = subMapDest.get( row );
				if( dstVal != null )
				{
					this.setVal(row, destCol, dstVal.add(srcMultNegated));
				}
				else
				{
					this.setVal(row, destCol, srcMultNegated);
				}
			}
		}
	}
	
	
	/**
	 * Subtracts from destRow the mult parameter times the srcRow.
	 * 
	 * @param srcRow The source row.
	 * @param destRow The destination row.
	 * @param mult The multiplier parameter.
	 */
	private void rowSubtractLeft( BigInteger srcRow , BigInteger destRow , R mult )
	{
		HashMap<BigInteger,R> subMapSrc = rowMap.get( srcRow );
		HashMap<BigInteger,R> subMapDest = rowMap.get( destRow );
		if( subMapSrc != null )
		{
			Iterator<BigInteger> it = subMapSrc.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger col = it.next();
				R srcVal = subMapSrc.get( col );
				R srcMultNegated = mult.mult( srcVal ).negate();
				R dstVal = subMapDest.get( col );
				if( dstVal != null )
				{
					this.setVal(destRow, col, dstVal.add(srcMultNegated));
				}
				else
				{
					this.setVal(destRow, col, srcMultNegated);
				}
			}
		}
	}
	
	
	/**
	 * Subtracts from destCol the srcCol * mult.
	 * 
	 * @param srcCol The source column.
	 * @param destCol The destination column.
	 * @param mult The multiplier parameter.
	 */
	private void columnSubtractRight( BigInteger srcCol , BigInteger destCol , R mult )
	{
		HashMap<BigInteger,R> subMapSrc = columnMap.get( srcCol );
		HashMap<BigInteger,R> subMapDest = columnMap.get( destCol );
		if( subMapSrc != null )
		{
			Iterator<BigInteger> it = subMapSrc.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger row = it.next();
				R srcVal = subMapSrc.get( row );
				R srcMultNegated = srcVal.mult( mult ).negate();
				R dstVal = subMapDest.get( row );
				if( dstVal != null )
				{
					this.setVal(row, destCol, dstVal.add(srcMultNegated));
				}
				else
				{
					this.setVal(row, destCol, srcMultNegated);
				}
			}
		}
	}
	
	
	/**
	 * Subtracts from destRow the srcRow times mult.
	 * 
	 * @param srcRow The source row.
	 * @param destRow The destination row.
	 * @param mult The multiplier parameter.
	 */
	private void rowSubtractRight( BigInteger srcRow , BigInteger destRow , R mult )
	{
		HashMap<BigInteger,R> subMapSrc = rowMap.get( srcRow );
		HashMap<BigInteger,R> subMapDest = rowMap.get( destRow );
		if( subMapSrc != null )
		{
			Iterator<BigInteger> it = subMapSrc.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger col = it.next();
				R srcVal = subMapSrc.get( col );
				R srcMultNegated = srcVal.mult( mult ).negate();
				R dstVal = subMapDest.get( col );
				if( dstVal != null )
				{
					this.setVal(destRow, col, dstVal.add(srcMultNegated));
				}
				else
				{
					this.setVal(destRow, col, srcMultNegated);
				}
			}
		}
	}
	
	
	/**
	 * Left-multiplies all values in a column by a parameter.
	 * 
	 * @param col The column.
	 * @param mv The value by which to multiply.
	 */
	private void multiplyThroughColumnLeft( BigInteger col , R mv )
	{
		HashMap<BigInteger,R> subMap = columnMap.get( col );
		if( subMap != null )
		{
			Iterator<BigInteger> it = subMap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger row = it.next();
				R val = subMap.get( row );
				if( val != null )
				{
					R pval = mv.mult( val );
					this.setVal(row, col, pval);
				}
			}
		}
	}
	
	
	/**
	 * Left-multiplies all values in a row by a parameter.
	 * 
	 * @param row The row.
	 * @param mv The value by which to multiply.
	 */
	private void multiplyThroughRowLeft( BigInteger row , R mv )
	{
		HashMap<BigInteger,R> subMap = rowMap.get( row );
		if( subMap != null )
		{
			Iterator<BigInteger> it = subMap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger col = it.next();
				R val = subMap.get( col );
				if( val != null )
				{
					R pval = mv.mult( val );
					this.setVal(row, col, pval);
				}
			}
		}
	}
	
	
	/**
	 * Right-multiplies all values in a column by a parameter.
	 * 
	 * @param col The column.
	 * @param mv The value by which to multiply.
	 */
	private void multiplyThroughColumnRight( BigInteger col , R mv )
	{
		HashMap<BigInteger,R> subMap = columnMap.get( col );
		if( subMap != null )
		{
			Iterator<BigInteger> it = subMap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger row = it.next();
				R val = subMap.get( row );
				if( val != null )
				{
					R pval = val.mult( mv );
					this.setVal(row, col, pval);
				}
			}
		}
	}
	
	
	/**
	 * Right-multiplies all values in a row by a parameter.
	 * 
	 * @param row The row.
	 * @param mv The value by which to multiply.
	 */
	private void multiplyThroughRowRight( BigInteger row , R mv )
	{
		HashMap<BigInteger,R> subMap = rowMap.get( row );
		if( subMap != null )
		{
			Iterator<BigInteger> it = subMap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger col = it.next();
				R val = subMap.get( col );
				if( val != null )
				{
					R pval = val.mult( mv );
					this.setVal(row, col, pval);
				}
			}
		}
	}
	
	
	/**
	 * If necessary, replaces the i-th column with a column that can be used as the left-side i-th pivot.  Then returns the left-inverse of 
	 * the value at [i,i].
	 * 
	 * @param coli The i-th column.
	 * @param ret The matrix on which to perform identical operations to build the inverse.
	 * @return The left-inverse of the value at [i,i].
	 * @throws NotInvertibleException
	 */
	private R setUpColumnLeft( final BigInteger coli , final SquareMatrixElem<U,R,S> ret ) throws NotInvertibleException
	{
		try
		{
			final R ap = this.get(coli, coli);
			if( ap == null )
			{
				throw( new NotInvertibleException() );
			}
			final R tp = ap.invertLeft();
			return( tp );
		}
		catch( NotInvertibleException ex )
		{
			final BigInteger max = dim.getVal();
			BigInteger cnt = coli.add( BigInteger.ONE );
			while( cnt.compareTo( max ) < 0 )
			{
				try
				{
					final R ap = this.get(cnt, coli);
					if( ap == null )
					{
						throw( new NotInvertibleException() );
					}
					final R tp = ap.invertLeft();
					exchangeColumns( coli , cnt );
					ret.exchangeColumns( coli , cnt );
					return( tp );
				}
				catch( NotInvertibleException ex2 )
				{
					cnt = cnt.add( BigInteger.ONE );
				}
			}
			throw( new NotInvertibleException() );
		}
	}
	
	
	/**
	 * If necessary, replaces the i-th row with a row that can be used as the left-side i-th pivot.  Then returns the left-inverse of 
	 * the value at [i,i].
	 * 
	 * @param rowi The i-th row.
	 * @param ret The matrix on which to perform identical operations to build the inverse.
	 * @return The left-inverse of the value at [i,i].
	 * @throws NotInvertibleException
	 */
	private R setUpRowLeft( final BigInteger rowi , final SquareMatrixElem<U,R,S> ret ) throws NotInvertibleException
	{
		try
		{
			final R ap = this.get(rowi, rowi);
			if( ap == null )
			{
				throw( new NotInvertibleException() );
			}
			final R tp = ap.invertLeft();
			return( tp );
		}
		catch( NotInvertibleException ex )
		{
			final BigInteger max = dim.getVal();
			BigInteger cnt = rowi.add( BigInteger.ONE );
			while( cnt.compareTo( max ) < 0 )
			{
				try
				{
					final R ap = this.get(cnt, rowi);
					if( ap == null )
					{
						throw( new NotInvertibleException() );
					}
					final R tp = ap.invertLeft();
					exchangeRows( rowi , cnt );
					ret.exchangeRows( rowi , cnt );
					return( tp );
				}
				catch( NotInvertibleException ex2 )
				{
					cnt = cnt.add( BigInteger.ONE );
				}
			}
			throw( new NotInvertibleException() );
		}
	}
	
	/**
	 * If necessary, replaces the i-th column with a column that can be used as the right-side i-th pivot.  Then returns the right-inverse of 
	 * the value at [i,i].
	 * 
	 * @param coli The i-th column.
	 * @param ret The matrix on which to perform identical operations to build the inverse.
	 * @return The right-inverse of the value at [i,i].
	 * @throws NotInvertibleException
	 */
	private R setUpColumnRight( final BigInteger coli , final SquareMatrixElem<U,R,S> ret ) throws NotInvertibleException
	{
		try
		{
			final R ap = this.get(coli, coli);
			if( ap == null )
			{
				throw( new NotInvertibleException() );
			}
			final R tp = ap.invertRight();
			return( tp );
		}
		catch( NotInvertibleException ex )
		{
			final BigInteger max = dim.getVal();
			BigInteger cnt = coli.add( BigInteger.ONE );
			while( cnt.compareTo( max ) < 0 )
			{
				try
				{
					final R ap = this.get(coli, cnt);
					if( ap == null )
					{
						throw( new NotInvertibleException() );
					}
					final R tp = ap.invertRight();
					exchangeColumns( coli , cnt );
					ret.exchangeColumns( coli , cnt );
					return( tp );
				}
				catch( NotInvertibleException ex2 )
				{
					cnt = cnt.add( BigInteger.ONE );
				}
			}
			throw( new NotInvertibleException() );
		}
	}
	
	
	/**
	 * If necessary, replaces the i-th row with a row that can be used as the right-side i-th pivot.  Then returns the right-inverse of 
	 * the value at [i,i].
	 * 
	 * @param rowi The i-th row.
	 * @param ret The matrix on which to perform identical operations to build the inverse.
	 * @return The right-inverse of the value at [i,i].
	 * @throws NotInvertibleException
	 */
	private R setUpRowRight( final BigInteger rowi , final SquareMatrixElem<U,R,S> ret ) throws NotInvertibleException
	{
		try
		{
			final R ap = this.get(rowi, rowi);
			if( ap == null )
			{
				throw( new NotInvertibleException() );
			}
			final R tp = ap.invertRight();
			return( tp );
		}
		catch( NotInvertibleException ex )
		{
			final BigInteger max = dim.getVal();
			BigInteger cnt = rowi.add( BigInteger.ONE );
			while( cnt.compareTo( max ) < 0 )
			{
				try
				{
					final R ap = this.get(cnt, rowi);
					if( ap == null )
					{
						throw( new NotInvertibleException() );
					}
					final R tp = ap.invertRight();
					exchangeRows( rowi , cnt );
					ret.exchangeRows( rowi , cnt );
					return( tp );
				}
				catch( NotInvertibleException ex2 )
				{
					cnt = cnt.add( BigInteger.ONE );
				}
			}
			throw( new NotInvertibleException() );
		}
	}
	
	
	/**
	 * Exchanges two columns in the matrix.
	 * 
	 * @param cola The first column to exchange.
	 * @param colb The second column to exchange.
	 */
	private void exchangeColumns( final BigInteger cola , final BigInteger colb )
	{
		HashMap<BigInteger,R> subA = columnMap.get( cola );
		HashMap<BigInteger,R> subB = columnMap.get( colb );
		
		HashMap<BigInteger,R> subAA = (HashMap<BigInteger, R>) (subA != null ? subA.clone() : null);
		HashMap<BigInteger,R> subBB = (HashMap<BigInteger, R>) (subB != null ? subB.clone() : null);
		
		eraseAllColumnValues( cola , subAA );
		eraseAllColumnValues( colb , subBB );
		
		setAllColumnValues( cola , subBB );
		setAllColumnValues( colb , subAA );
	}
	
	
	/**
	 * Exchanges two rows in the matrix.
	 * 
	 * @param rowa The first row to exchange.
	 * @param rowb The second row to exchange.
	 */
	private void exchangeRows( final BigInteger rowa , final BigInteger rowb )
	{
		HashMap<BigInteger,R> subA = rowMap.get( rowa );
		HashMap<BigInteger,R> subB = rowMap.get( rowb );
		
		HashMap<BigInteger,R> subAA = (HashMap<BigInteger, R>) (subA != null ? subA.clone() : null);
		HashMap<BigInteger,R> subBB = (HashMap<BigInteger, R>) (subB != null ? subB.clone() : null);
		
		eraseAllRowValues( rowa , subAA );
		eraseAllRowValues( rowb , subBB );
		
		setAllRowValues( rowa , subBB );
		setAllRowValues( rowb , subAA );
	}
	
	
	/**
	 * Copies all values from a row map into a particular column.
	 * 
	 * @param col The column.
	 * @param rmap The row map.
	 */
	private void setAllColumnValues( final BigInteger col , HashMap<BigInteger,R> rmap )
	{
		if( rmap != null )
		{
			Iterator<BigInteger> it = rmap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger row = it.next();
				this.setVal(row, col, rmap.get( row ) );
			}
		}
	}
	
	
	/**
	 * Copies all values from a column map into a particular row.
	 * 
	 * @param row The row.
	 * @param cmap The column map.
	 */
	private void setAllRowValues( final BigInteger row , HashMap<BigInteger,R> cmap )
	{
		if( cmap != null )
		{
			Iterator<BigInteger> it = cmap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger col = it.next();
				this.setVal(row, col, cmap.get( col ) );
			}
		}
	}
	
	
	/**
	 * Removes all values for a column from the sparse collection.
	 * 
	 * @param col The column.
	 * @param rmap The row map at the column.
	 */
	private void eraseAllColumnValues( final BigInteger col , HashMap<BigInteger,R> rmap )
	{
		if( rmap != null )
		{
			Iterator<BigInteger> it = rmap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger row = it.next();
				eraseVal( row , col );
			}
		}
	}
	
	
	/**
	 * Removes all values for a row from the sparse collection.
	 * 
	 * @param row The row.
	 * @param cmap The column map at the row.
	 */
	private void eraseAllRowValues( final BigInteger row , HashMap<BigInteger,R> cmap )
	{
		if( cmap != null )
		{
			Iterator<BigInteger> it = cmap.keySet().iterator();
			while( it.hasNext() )
			{
				BigInteger col = it.next();
				eraseVal( row , col );
			}
		}
	}
	
	
	/**
	 * Removes a value from the sparse collection.
	 * 
	 * @param row The matrix row.
	 * @param col The matrix column.
	 */
	private void eraseVal( final BigInteger row , final BigInteger col )
	{
		HashMap<BigInteger,R> subMap = rowMap.get( row );
		if( subMap != null )
		{
			subMap.remove( col );
			
			if( subMap.keySet().size() == 0 )
			{
				rowMap.remove( row );
			}
		}
		
		
		subMap = columnMap.get( col );
		if( subMap != null )
		{
			subMap.remove( row );
			
			if( subMap.keySet().size() == 0 )
			{
				columnMap.remove( col );
			}
		}
	}
	
	
	/**
	 * Returns a clone of the matrix.
	 * 
	 * @return The clone of the matrix.
	 */
	private SquareMatrixElem<U, R, S> icopy()
	{
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		Iterator<BigInteger> rowi = rowMap.keySet().iterator();
		while( rowi.hasNext() )
		{
			BigInteger row = rowi.next();
			HashMap<BigInteger,R> subMap = rowMap.get( row );
			Iterator<BigInteger> coli = subMap.keySet().iterator();
			while( coli.hasNext() )
			{
				BigInteger col = coli.next();
				R vali = subMap.get( col );
				ret.setVal(row, col, vali );
			}
		}
		return( ret );
	}

	
	@Override
	public SquareMatrixElem<U, R, S> divideBy(int val) {
		SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
		Iterator<BigInteger> rowi = rowMap.keySet().iterator();
		while( rowi.hasNext() )
		{
			BigInteger row = rowi.next();
			HashMap<BigInteger,R> subMap = rowMap.get( row );
			Iterator<BigInteger> coli = subMap.keySet().iterator();
			while( coli.hasNext() )
			{
				BigInteger col = coli.next();
				R vali = subMap.get( col );
				ret.setVal(row, col, vali.divideBy( val ) );
			}
		}
		return( ret );
	}

	@Override
	public SquareMatrixElemFactory<U, R, S> getFac() {
		return( new SquareMatrixElemFactory<U,R,S>( fac , dim ) );
	}
	
	
	/**
	 * Gets a value in the matrix.  Null is possible.
	 * 
	 * @param row The matrix row.
	 * @param col The matrix column.
	 * @return The value.
	 */
	public R get( BigInteger row , BigInteger col )
	{
		HashMap<BigInteger,R> subMap = rowMap.get( row );
		if( subMap != null )
		{
			R val = subMap.get( col );
			if( val != null )
			{
				return( val );
			}
		}
		return( null );
	}
	
	
	/**
	 * Gets a value in the matrix.  Null is not possible.
	 * 
	 * @param row The matrix row.
	 * @param col The matrix column.
	 * @return The value.
	 */
	public R getVal( BigInteger row , BigInteger col )
	{
		HashMap<BigInteger,R> subMap = rowMap.get( row );
		if( subMap != null )
		{
			R val = subMap.get( col );
			if( val != null )
			{
				return( val );
			}
		}
		return( fac.zero() );
	}
	
	
	/**
	 * Sets a value in the matrix.
	 * 
	 * @param row The matrix row.
	 * @param col The matrix column.
	 * @param val The value to set.
	 */
	public void setVal( BigInteger row , BigInteger col , R val )
	{
		HashMap<BigInteger,R> subMap = rowMap.get( row );
		if( subMap == null )
		{
			subMap = new HashMap<BigInteger,R>();
			rowMap.put(row, subMap);
		}
		
		subMap.put(col, val);
		
		
		subMap = columnMap.get( col );
		if( subMap == null )
		{
			subMap = new HashMap<BigInteger,R>();
			columnMap.put(col, subMap);
		}
		
		subMap.put(row, val);
	}
	
	
	/**
	 * Copies a column vector into a Geometric Algebra multivector.
	 * 
	 * @param row The column from which to copy the elements.
	 * @param out The multivector into which the matrix elements are copied.
	 */
	public void columnVectorToGeometricAlgebra( BigInteger column , GeometricAlgebraMultivectorElem<U,?,R,?> out )
	{
		HashMap<BigInteger,R> atCol = columnMap.get( column );
		Iterator<BigInteger> it = atCol.keySet().iterator();
		while( it.hasNext() )
		{
			final BigInteger indx = it.next();
			final R val = atCol.get( indx );
			final HashSet<BigInteger> el = new HashSet<BigInteger>();
			el.add( indx );
			out.setVal(el, val);
		}
	}
	
	
	/**
	 * Copies a row vector into a Geometric Algebra multivector.
	 * 
	 * @param row The row from which to copy the elements.
	 * @param out The multivector into which the matrix elements are copied.
	 */
	public void rowVectorToGeometricAlgebra( BigInteger row , GeometricAlgebraMultivectorElem<U,?,R,?> out )
	{
		HashMap<BigInteger,R> atRow = rowMap.get( row );
		Iterator<BigInteger> it = atRow.keySet().iterator();
		while( it.hasNext() )
		{
			final BigInteger indx = it.next();
			final R val = atRow.get( indx );
			final HashSet<BigInteger> el = new HashSet<BigInteger>();
			el.add( indx );
			out.setVal(el, val);
		}
	}
	
	
	/**
	 * Copies a column vector into a tensor of rank one.
	 * 
	 * @param row The column from which to copy the elements.
	 * @param out The tensor into which the matrix elements are copied.
	 */
	public void columnVectorToRankOneTensor( BigInteger column , EinsteinTensorElem<?,R,?> out )
	{
		if( !( out.getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		HashMap<BigInteger,R> atCol = columnMap.get( column );
		Iterator<BigInteger> it = atCol.keySet().iterator();
		while( it.hasNext() )
		{
			final BigInteger indx = it.next();
			final R val = atCol.get( indx );
			final ArrayList<BigInteger> el = new ArrayList<BigInteger>();
			el.add( indx );
			out.setVal(el, val);
		}
	}
	
	
	/**
	 * Copies a row vector into a tensor of rank one.
	 * 
	 * @param row The row from which to copy the elements.
	 * @param out The tensor into which the matrix elements are copied.
	 */
	public void rowVectorToRankOneTensor( BigInteger row , EinsteinTensorElem<?,R,?> out )
	{
		if( !( out.getTensorRank().equals( BigInteger.ONE ) ) )
		{
			throw( new RuntimeException( "Not a Rank One Tensor." ) );
		}
		
		HashMap<BigInteger,R> atRow = rowMap.get( row );
		Iterator<BigInteger> it = atRow.keySet().iterator();
		while( it.hasNext() )
		{
			final BigInteger indx = it.next();
			final R val = atRow.get( indx );
			final ArrayList<BigInteger> el = new ArrayList<BigInteger>();
			el.add( indx );
			out.setVal(el, val);
		}
	}
	
	
	/**
	 * Copies the matrix elements to tensor of rank two.
	 * 
	 * @param out The tensor into which the matrix elements are copied.
	 */
	public void toRankTwoTensor( EinsteinTensorElem<?,R,?> out )
	{
		if( !( out.getTensorRank().equals( BigInteger.valueOf( 2 ) ) ) )
		{
			throw( new RuntimeException( "Not a Rank Two Tensor." ) );
		}
		
		Iterator<BigInteger> itr = rowMap.keySet().iterator();
		while( itr.hasNext() )
		{
			BigInteger row = itr.next();
			HashMap<BigInteger,R> atRow = rowMap.get( row );
			Iterator<BigInteger> it = atRow.keySet().iterator();
			while( it.hasNext() )
			{
				final BigInteger column = it.next();
				final R val = atRow.get( column );
				final ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( row );
				el.add( column );
				out.setVal(el, val);
			}
		}
	}
	
	
	
	/**
	 * Returns the determinant of the matrix.
	 * 
	 * This should only be used when R is commutative.
	 * 
	 * Adapted from:
	 * 
	 * http://stackoverflow.com/questions/16602350/calculating-matrix-determinant
	 * 
	 * @return The determinant of the matrix.
	 */
	public R determinant( )
    {
        R res;

        // Trivial 1x1 matrix
        if ( dim.getVal().equals( BigInteger.ONE ) )
        {
            res = this.getVal(BigInteger.ZERO, BigInteger.ZERO);
        }
        // Trivial 2x2 matrix
        else if ( dim.getVal().equals( BigInteger.valueOf( 2 ) ) )
        {
        	final R t0 = ( this.getVal( BigInteger.ZERO ,  BigInteger.ZERO ) ).mult( this.getVal( BigInteger.ONE ,  BigInteger.ONE ) );
        	final R t1 = ( this.getVal( BigInteger.ONE ,  BigInteger.ZERO ) ).mult( this.getVal( BigInteger.ZERO ,  BigInteger.ONE ) );
            res = t0.add( t1.negate() );
        }
        // NxN matrix
        else
        {
            res = fac.zero();
            final Iterator<BigInteger> itj = columnMap.keySet().iterator();
            while( itj.hasNext() )
            {
            	final BigInteger j1 = itj.next();
            	final SquareMatrixElem<NumDimensions,R,S> m = genDeterminantSubMatrix( j1 );
            	R tmp = ( this.getVal( BigInteger.ZERO , j1 ) ).mult( m.determinant() );
            	if( ( j1.add( BigInteger.valueOf( 2 ) ) ).mod( BigInteger.valueOf( 2 ) ).equals( BigInteger.ONE ) )
            		tmp = tmp.negate();
                res = res.add( tmp );
            }
        }
        
        return res;
    }
	
	
	
	/**
	 * Calculates a sub-matrix removing the index j1 for the purpose of generating the determinant.
	 * 
	 * @param j1 The index to be removed.
	 * @return The sub-matrix.
	 */
	private SquareMatrixElem<NumDimensions,R,S> genDeterminantSubMatrix( BigInteger j1 )
	{
		
		final NumDimensions r = new NumDimensions()
		{
			@Override
			public BigInteger getVal() {
				return( dim.getVal().subtract( BigInteger.ONE ) );
			}	
		};
		
		
		final SquareMatrixElem<NumDimensions,R,S> ret = new SquareMatrixElem<NumDimensions,R,S>( fac , r );
		
		
		 final Iterator<BigInteger> iti = rowMap.keySet().iterator();
		 while( iti.hasNext() )
         {
			     final BigInteger i = iti.next();
			     if( i.equals( BigInteger.ZERO ) )
                     continue;
                 final HashMap<BigInteger,R> ccmap = rowMap.get( i );
                 final Iterator<BigInteger> itj = ccmap.keySet().iterator();
                 while( itj.hasNext() )
                 {
                   final BigInteger j = itj.next();
                   if( j.equals( j1 ) )
                           continue;
                   BigInteger j2 = j;
                   if( j.compareTo( j1 ) > 0 )
                	   j2 = j2.subtract( BigInteger.ONE );
                   ret.setVal( i.subtract( BigInteger.ONE ) , j2 , this.get(i, j) );
                 }
         }
		
		
		return( ret );
	}
	
	
	
	
	@Override
	public void validate() throws RuntimeException
	{
		Iterator<BigInteger> itr = rowMap.keySet().iterator();
		while( itr.hasNext() )
		{
			BigInteger row = itr.next();
			HashMap<BigInteger,R> atRow = rowMap.get( row );
			Iterator<BigInteger> it = atRow.keySet().iterator();
			while( it.hasNext() )
			{
				final BigInteger column = it.next();
				final R val = atRow.get( column );
				final R val2 = columnMap.get( column ).get( row );
				if( val != val2 )
				{
					throw( new RuntimeException( "Mismatch" ) );
				}
				val.validate();
			}
		}
		
		itr = columnMap.keySet().iterator();
		while( itr.hasNext() )
		{
			BigInteger column = itr.next();
			HashMap<BigInteger,R> atColumn = columnMap.get( column );
			Iterator<BigInteger> it = atColumn.keySet().iterator();
			while( it.hasNext() )
			{
				final BigInteger row = it.next();
				final R val = atColumn.get( row );
				final R val2 = rowMap.get( row ).get( column );
				if( val != val2 )
				{
					throw( new RuntimeException( "Mismatch" ) );
				}
				val.validate();
			}
		}
		
	}
	
	
	@Override
	public SquareMatrixElem<U, R, S> handleOptionalOp( Object id , ArrayList<SquareMatrixElem<U, R, S>> args )  throws NotInvertibleException
	{
		if( id instanceof SquareMatrixElem.SquareMatrixCmd )
		{
			switch( (SquareMatrixElem.SquareMatrixCmd) id )
			{
			
				case INVERT_LEFT_REV_COEFF:
				{
					SquareMatrixElem<U,R,S> ret = invertLeftRevCoeff( );
					return( ret );
				}
				// break;
				
				case INVERT_RIGHT_REV_COEFF:
				{
					SquareMatrixElem<U,R,S> ret = invertRightRevCoeff( );
					return( ret );
				}
				// break;
			
				case MULT_REV_COEFF:
				{
					SquareMatrixElem<U,R,S> ret = multRevCoeff( args.get(0) );
					return( ret );
				}
				// break;
			
				case TRANSPOSE:
				{
					SquareMatrixElem<U,R,S> ret = new SquareMatrixElem<U,R,S>(fac,dim);
					Iterator<BigInteger> rowi = rowMap.keySet().iterator();
					while( rowi.hasNext() )
					{
						BigInteger row = rowi.next();
						HashMap<BigInteger,R> subMap = rowMap.get( row );
						Iterator<BigInteger> coli = subMap.keySet().iterator();
						while( coli.hasNext() )
						{
							BigInteger col = coli.next();
							R val = subMap.get( col );
							ret.setVal(col, row, val );
						}
					}
					return( ret );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	/**
	 * The column mapping for the matrix elements.
	 */
	private final HashMap<BigInteger,HashMap<BigInteger,R>> columnMap = new HashMap<BigInteger,HashMap<BigInteger,R>>();
	
	/**
	 * The row mapping for the matrix elements.
	 */
	private final HashMap<BigInteger,HashMap<BigInteger,R>> rowMap = new HashMap<BigInteger,HashMap<BigInteger,R>>();
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	/**
	 * The number of dimensions in the matrix.
	 */
	private U dim;
	

}
