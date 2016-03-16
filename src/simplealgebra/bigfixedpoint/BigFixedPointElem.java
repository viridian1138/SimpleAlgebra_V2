




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






package simplealgebra.bigfixedpoint;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import simplealgebra.AbsoluteValue;
import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.Sqrt;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteNumDimensionsCache;

/**
 * A fixed-point elem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The precision of the elem.
 */
public class BigFixedPointElem<T extends Precision<T>> extends Elem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> {
	
	/**
	 * Constructs the elem.
	 * 
	 * @param _val The fixed-point value of the elem.
	 * @param _prec The precision of the elem.
	 */
	public BigFixedPointElem( BigInteger _val , T _prec )
	{
		val = _val;
		prec = _prec;
	}
	
	/**
	 * Constructs the elem.
	 * 
	 * @param vl The double-precision value of the elem.
	 * @param _prec The precision of the elem.
	 */
	public BigFixedPointElem( double vl , T _prec ) 
	{
		if( Double.isNaN( vl ) || Double.isInfinite( vl ) )
		{
			throw( new RuntimeException( "NaN" ) );
		}
		prec = _prec;
		final BigDecimal bd = new BigDecimal( vl * prec.getVal().doubleValue() );
		final BigInteger _val = bd.toBigInteger();
		val = _val;
	}

	@Override
	public BigFixedPointElem<T> add(BigFixedPointElem<T> b) {
		return( new BigFixedPointElem<T>( val.add( b.val ) , prec ) );
	}

	@Override
	public BigFixedPointElem<T> mult(BigFixedPointElem<T> b) {
		return( new BigFixedPointElem<T>( val.multiply( b.val ).divide( prec.getVal() ) , prec ) );
	}

	@Override
	public BigFixedPointElem<T> negate() {
		return( new BigFixedPointElem<T>( val.negate() , prec ) );
	}

	@Override
	public BigFixedPointElem<T> invertLeft() throws NotInvertibleException {
		if( val.equals( BigInteger.ZERO ) )
		{
			throw( new NotInvertibleException() );
		}
		return( new BigFixedPointElem<T>( prec.getValSquared().divide( val ) , prec ) );
	}
	
	@Override
	public BigFixedPointElem<T> invertRight() throws NotInvertibleException {
		if( val.equals( BigInteger.ZERO ) )
		{
			throw( new NotInvertibleException() );
		}
		return( new BigFixedPointElem<T>( prec.getValSquared().divide( val ) , prec ) );
	}

	@Override
	public BigFixedPointElem<T> divideBy(BigInteger vali) {
		if( vali.equals( BigInteger.ZERO ) )
		{
			throw( new RuntimeException( "NaN" ) );
		}
		return( new BigFixedPointElem<T>( val.divide( vali ) , prec ) );
	}

	@Override
	public BigFixedPointElemFactory<T> getFac() {
		return( new BigFixedPointElemFactory<T>( prec ) );
	}
	
	@Override
	public BigFixedPointElem<T> cloneThread( final BigInteger threadIndex )
	{
		final T precs = prec.cloneThread( threadIndex );
		if( precs != prec )
		{
			return( new BigFixedPointElem<T>( val , precs ) );
		}
		return( this );
	}
	
	
	@Override
	public BigFixedPointElem<T> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> cache) {
		final BigFixedPointElem<T> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final T precs = prec.cloneThread( threadIndex );
		if( precs != prec )
		{
			final BigFixedPointElem<T> rtmp = new BigFixedPointElem<T>( val , precs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<BigFixedPointElem<T>,BigFixedPointElemFactory<T>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteNumDimensionsCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WritePrecisionCache<T>( cache.getCacheVal() ) );
			final String sta = prec.writeDesc( (WritePrecisionCache<T>)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) WritePrecisionCache.class ) ) , ps);
			String stai = ( (WriteBigIntegerCache)( cache.getAuxCache( WriteBigIntegerCache.class ) ) ).writeDesc( val , ps );
			st = cache.getIncrementVal();
			cache.put(this, st);
			this.getFac().writeElemTypeString( ps );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			this.getFac().writeElemTypeString( ps );
			ps.print( "( " );
			ps.print( stai );
			ps.print( " , " );
			ps.print( sta );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	/**
	 * Returns the square root of the elem.
	 * 
	 * @return The square root of the elem.
	 * @throws NotInvertibleException
	 */
	protected BigFixedPointElem<T> sqrt() throws NotInvertibleException
	{
		final int valc = val.compareTo( BigInteger.ZERO );
		if( valc < 0 )
		{
			throw( new NotInvertibleException() );
		}
		if( valc == 0 )
		{
			return( this );
		}
		
		BigFixedPointElem<T> strt = new BigFixedPointElem<T>( BigInteger.ONE , prec );
		BigFixedPointElem<T> end = new BigFixedPointElem<T>( 
				val.compareTo( prec.getVal() ) > 0 ? val : prec.getVal() , prec );
		BigFixedPointElem<T> pivot = new BigFixedPointElem<T>( 
				( strt.getPrecVal().add( end.getPrecVal() ) ).divide( BigInteger.valueOf( 2 ) ) , prec );
		int pval = pivot.mult( pivot ).getPrecVal().compareTo( this.getPrecVal() );
		while( pval != 0 )
		{
			if( pval > 0 )
			{
				end = pivot;
			}
			else
			{
				strt = pivot;
			}
			final BigInteger prevpiv = pivot.getPrecVal();
			pivot = new BigFixedPointElem<T>( 
				( strt.getPrecVal().add( end.getPrecVal() ) ).divide( BigInteger.valueOf( 2 ) ) , prec );
			if( prevpiv.compareTo( pivot.getPrecVal() ) == 0 )
			{
				return( pivot );
			}
			pval = pivot.mult( pivot ).getPrecVal().compareTo( this.getPrecVal() );
		}
		return( pivot );
	}
	
	
	@Override
	public BigFixedPointElem<T> handleOptionalOp( Object id , ArrayList<BigFixedPointElem<T>> args ) throws NotInvertibleException
	{
		if( id instanceof AbsoluteValue )
		{
			switch( (AbsoluteValue) id )
			{
				case ABSOLUTE_VALUE:
				{
					return( new BigFixedPointElem<T>( val.abs() , prec ) );
				}
				// break;
			}
		}
		
		if( id instanceof Sqrt )
		{
			switch( (Sqrt) id )
			{
				case SQRT:
				{
					return( sqrt() );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
	}
	
	
	/**
	 * Returns the double-precision equivalent of the elem.
	 * 
	 * @return The double-precision equivalent of the elem.
	 */
	public double toDouble( )
	{
		return( ( val.doubleValue() ) / ( prec.getVal().doubleValue() ) );
	}
	
	
	/**
	 * Returns the value at the specified precision.
	 * @return The value at the specified precision.
	 */
	public BigInteger getPrecVal()
	{
		return( val );
	}

	
	/**
	 * The value of the elem.
	 */
	BigInteger val;
	
	/**
	 * The precision of the elem.
	 */
	T prec;

}

