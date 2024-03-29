




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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import simplealgebra.bigfixedpoint.WritePrecisionCache;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Factory for square matrix elems.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the square matrix.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SquareMatrixElemFactory<U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends ElemFactory<SquareMatrixElem<U,R,S>, SquareMatrixElemFactory<U,R,S>> {

	
	/**
	 * Constructs the factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions.
	 */
	public SquareMatrixElemFactory( S _fac , U _dim )
	{
		fac = _fac;
		dim = _dim;
	}
	
	
	@Override
	public SquareMatrixElem<U, R, S> identity() {
		final BigInteger max = dim.getVal();
		SquareMatrixElem<U, R, S> ret = new SquareMatrixElem<U, R, S>( fac , dim );
		BigInteger cnt = BigInteger.ZERO;
		for( cnt = BigInteger.ZERO ; cnt.compareTo(max) < 0 ; cnt = cnt.add( BigInteger.ONE ) )
		{
			ret.setVal(cnt, cnt, fac.identity());
		}
		return( ret );
	}

	@Override
	public SquareMatrixElem<U, R, S> zero() {
		return( new SquareMatrixElem<U, R, S>( fac , dim ) );
	}
	
	
	@Override
	public SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> handleSymbolicOptionalOp( Object id , 
			ArrayList<SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>>> args )  throws NotInvertibleException
	{
		if( id instanceof SquareMatrixElem.SquareMatrixCmd )
		{
			switch( (SquareMatrixElem.SquareMatrixCmd) id )
			{
			
				case INVERT_LEFT_REV_COEFF:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> arg
						= args.get( 0 );
					return( new SymbolicInvertLeftRevCoeff<U,R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
				
				case INVERT_RIGHT_REV_COEFF:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> arg
						= args.get( 0 );
					return( new SymbolicInvertRightRevCoeff<U,R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
			
				case MULT_REV_COEFF:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> argA
						= args.get( 0 );
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> argB
						= args.get( 1 );
					return( new SymbolicMultRevCoeff<U,R,S>( argA , argB , argA.getFac().getFac() ) );
				}
				// break;
				
				case TRANSPOSE:
				{
					SymbolicElem<SquareMatrixElem<U, R, S>, SquareMatrixElemFactory<U,R,S>> arg
						= args.get( 0 );
					return( new SymbolicTranspose<U,R,S>( arg , arg.getFac().getFac() ) );
				}
				// break;
			}
		}
		
		return( super.handleSymbolicOptionalOp(id, args) );
	}
	
	@Override
	public boolean isMultCommutative()
	{
		return( false );
	}
	
	
	@Override
	public boolean isNestedMultCommutative()
	{
		return( fac.isMultCommutative() );
	}
	
	
	@Override
	public boolean isMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	@Override
	public boolean isNestedMultAssociative()
	{
		return( fac.isMultAssociative() );
	}
	
	
	@Override
	public Elem<?,?> totalMagnitudeZero()
	{
		return( fac.zero() );
	}
	
	
	@Override
	public SquareMatrixElemFactory<U,R,S> cloneThread( final BigInteger threadIndex )
	{
		S sfac = fac.cloneThread(threadIndex);
		if( fac != sfac )
		{
			// The NumDimensions dim is presumed to be immutable.
			return( new SquareMatrixElemFactory<U,R,S>( sfac , dim ) );
		}
		return( this );
	}
	
	
	
	@Override
	public SquareMatrixElemFactory<U,R,S> cloneThreadCached( final BigInteger threadIndex , final CloneThreadCache<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> cache )
	{
		final SquareMatrixElemFactory<U,R,S> ctmp = cache.getFac( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		S sfac = (S) fac.cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		if( fac != sfac )
		{
			// The NumDimensions dim is presumed to be immutable.
			final SquareMatrixElemFactory<U,R,S> rtmp = new SquareMatrixElemFactory<U,R,S>( sfac , dim );
			cache.putFac(this, rtmp);
			return( rtmp );
		}
		cache.putFac(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<SquareMatrixElem<U,R,S>,SquareMatrixElemFactory<U,R,S>> cache , PrintStream ps )
	{
		String st = cache.getFac( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<R,S>)( cache.getInnerCache() ) , ps);
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			final String sta2 = dim.writeDesc( (WriteNumDimensionsCache)( cache.getAuxCache( WriteNumDimensionsCache.class ) ) , ps);
			st = cache.getIncrementVal();
			cache.putFac(this, st);
			writeElemFactoryTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			writeElemFactoryTypeString( ps );
			ps.print( "( " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( sta2 );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public void writeElemTypeString( PrintStream ps )
	{
		ps.print( SquareMatrixElem.class.getSimpleName() );
		ps.print( "<" );
		dim.writeTypeString(ps);
		ps.print( "," );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	@Override
	public void writeElemFactoryTypeString( PrintStream ps )
	{
		ps.print( SquareMatrixElemFactory.class.getSimpleName() );
		ps.print( "<" );
		dim.writeTypeString(ps);
		ps.print( "," );
		fac.writeElemTypeString(ps);
		ps.print( "," );
		fac.writeElemFactoryTypeString(ps);
		ps.print( ">" );
	}
	
	
	
	@Override
	public Iterator<SquareMatrixElem<U,R,S>> getApproxLnUnit()
	{
		final BigInteger MAX = this.getDim().getVal();
		final BigInteger MAXSQ = MAX.multiply( MAX );
		
		final HashMap<BigInteger,Iterator<R>> mapi = new HashMap<BigInteger,Iterator<R>>();
		
		for( BigInteger i = BigInteger.ZERO ; i.compareTo( MAXSQ ) < 0 ; i = i.add( BigInteger.ONE ) )
		{
			mapi.put( i , getFac().getApproxLnUnit() );
		}
		
		final HashMap<BigInteger,R> mapr = new HashMap<BigInteger,R>();
		
		
		for( BigInteger i = BigInteger.ONE ; i.compareTo( MAXSQ ) < 0 ; i = i.add( BigInteger.ONE ) )
		{
			mapr.put( i , mapi.get( i ).next() );
		}
		
		
		return( new Iterator<SquareMatrixElem<U,R,S>>()
				{
			
			
					/**
					 * Increments iterators across 
					 */
					protected void engageIncrement()
					{
						for( BigInteger i = BigInteger.ZERO ; i.compareTo( MAXSQ ) < 0 ; i = i.add( BigInteger.ONE ) )
						{
							if( mapi.get( i ).hasNext() )
							{
								mapr.put( i , mapi.get( i ).next() );
								return;
							}
							else
							{
								mapi.put( i , fac.getApproxLnUnit() );
								mapr.put( i , mapi.get( i ).next() );
							}
						}
					}
					
					
					@Override
					public boolean hasNext() 
					{
						for( BigInteger i = BigInteger.ZERO ; i.compareTo( MAXSQ ) < 0 ; i = i.add( BigInteger.ONE ) )
						{
							if( mapi.get( i ).hasNext() )
							{
								return( true );
							}
						}
						return( false );
					}

					@Override
					public SquareMatrixElem<U,R,S> next() 
					{
						if( mapi.get( BigInteger.ZERO ).hasNext() )
						{
							mapr.put( BigInteger.ZERO , mapi.get( BigInteger.ZERO ).next() );
						}
						else
						{
							engageIncrement();
							mapi.put( BigInteger.ZERO , fac.getApproxLnUnit() );
							mapr.put( BigInteger.ZERO , mapi.get( BigInteger.ZERO ).next() );
						}
						
						SquareMatrixElem<U,R,S> ret = zero();
						for( BigInteger i = BigInteger.ZERO ; i.compareTo( MAXSQ ) < 0 ; i = i.add( BigInteger.ONE ) )
						{
							ret.setVal( i.divide( MAX ) , i.mod( MAX ) , mapr.get( i ) );
						}
						return( ret );
						
					}

					@Override
					public void remove() {
						throw( new RuntimeException( "Not Supported" ) );
					}
					
				} );
	}
	
	
	/**
	 * Returns the factory for the enclosed type.
	 * 
	 * @return The factory for the enclosed type.
	 */
	public S getFac()
	{
		return( fac );
	}
	
	
	/**
	 * Returns the number of dimensions.
	 * 
	 * @return The number of dimensions.
	 */
	public U getDim()
	{
		return( dim );
	}
	
	
	/**
	 * The factory for the enclosed type.
	 */
	private S fac;
	
	/**
	 * The number of dimensions in the square matrix.
	 */
	private U dim;

}



