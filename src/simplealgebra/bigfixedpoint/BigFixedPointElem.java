




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
import simplealgebra.BadCreationException;
import simplealgebra.CloneThreadCache;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.PrimitiveRandom;
import simplealgebra.Sqrt;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SymbolicElem.EVAL_MODE;

/**
 * A fixed-point elem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <T> The precision of the elem.
 */
public class BigFixedPointElem<T extends Precision<T>> extends Elem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> implements Comparable<BigFixedPointElem<T>> {
	
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
			throw( new BadCreationException() );
		}
		prec = _prec;
		final BigDecimal vld = new BigDecimal( vl );
		final BigDecimal pvl = new BigDecimal( prec.getVal() );
		final BigDecimal bd = vld.multiply( pvl );
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
			throw( new BadCreationException() );
		}
		return( new BigFixedPointElem<T>( val.divide( vali ) , prec ) );
	}
	
	@Override
	public BigFixedPointElem<T> random(PrimitiveRandom in) {
		return( new BigFixedPointElem<T>( in.nextRandom( val ) , prec ) );
	}
	
	@Override
	protected BigFixedPointElem<T> estimateLnApprox( final int numIterExp ) throws NotInvertibleException
	{
		if( val.compareTo( BigInteger.ZERO ) > 0 )
		{
			try
			{
				return( new BigFixedPointElem( Math.log( toDouble() ) , prec ) );
			}
			catch( Throwable ex )
			{
				try
				{
					final long bitLength = val.bitLength();
					final long precBitLength = prec.getVal().bitLength();
					final BigInteger prval = ( BigInteger.valueOf( bitLength - precBitLength ) ).multiply( prec.getVal() );
					final BigFixedPointElem<T> prd = new BigFixedPointElem<T>( prval , prec );
					final BigFixedPointElem<T> prmul = new BigFixedPointElem<T>( 2.0 / Math.E , prec );
					final BigFixedPointElem<T> ret = prd.mult( prmul );
					return( ret );
				}
				catch( Throwable ex2 )
				{
					return( super.estimateLnApprox( numIterExp ) );
				}
			}
		}
		// return( super.estimateLnApprox( numIterExp ) );
		throw( new NotInvertibleException() );
	}

	@Override
	public BigFixedPointElemFactory<T> getFac() {
		return( new BigFixedPointElemFactory<T>( prec ) );
	}
	
	@Override
	public BigFixedPointElem<T> totalMagnitude()
	{
		return( this.mult( this ) );
	}
	
	@Override
	public int compareTo( BigFixedPointElem<T> in )
	{
		return( this.val.compareTo( in.val ) );
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
	public boolean evalSymbolicZeroApprox( EVAL_MODE mode )
	{
		return( val.equals( BigInteger.ZERO ) );
	}
	
	
	@Override
	public boolean evalSymbolicIdentityApprox( EVAL_MODE mode )
	{
		return( val.equals( prec.getVal() ) );
	}
	
	
	@Override
	public void writeMathML( PrecedenceComparator pc , PrintStream ps )
	{
		if( val.equals( BigInteger.ZERO ) )
		{
			ps.print( "<mn>0.0</mn>" );
			return;
		}
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		final BigInteger TEN = BigInteger.valueOf( 10 );
		final BigInteger TENVAL = prec.getVal().multiply( TEN );
		
		BigInteger exponent = BigInteger.ZERO;
		BigInteger mantissa = val;
		
		while( mantissa.abs().compareTo( TENVAL ) >= 0 )
		{
			mantissa = mantissa.divide( TEN );
			exponent = exponent.add( BigInteger.ONE );
		}
		
		while( mantissa.abs().compareTo( prec.getVal() ) < 0 )
		{
			mantissa = mantissa.multiply( TEN );
			exponent = exponent.subtract( BigInteger.ONE );
		}
		
		BigInteger manPrec = prec.getVal();
		
		final int DOUBLE_FRACTION_SIZE = 52;
		final BigInteger MAX_DBL_MANTISSA = BigInteger.valueOf( ( 1L << ( DOUBLE_FRACTION_SIZE - 1 ) ) - 1 );
		
		while( ( mantissa.abs().compareTo( MAX_DBL_MANTISSA ) > 0 ) || ( manPrec.abs().compareTo( MAX_DBL_MANTISSA ) > 0 ) )
		{
			mantissa = mantissa.divide( TWO );
			manPrec = manPrec.divide( TWO );
		}
		
		
		final double mantissaDbl = ( mantissa.doubleValue() ) / ( manPrec.doubleValue() );
		
		
		if( exponent.equals( BigInteger.ZERO ) )
		{
			ps.print( "<mn>" );
			ps.print( mantissaDbl );
			ps.print( "</mn>" );
		}
		else
		{
			ps.print( "<mrow>" );
			ps.print( "<mn>" );
			ps.print( mantissaDbl );
			ps.print( "</mn>" );
			ps.print( "<mo>&times;</mo>" );
			ps.print( "<msup><mn>10</mn><mn>" );
			ps.print( exponent );
			ps.print( "</mn></msup>" );
			ps.print( "</mrow>" );
		}
	}
	
	
	@Override
	public String writeDesc( WriteElemCache<BigFixedPointElem<T>,BigFixedPointElemFactory<T>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			cache.applyAuxCache( new WriteBigIntegerCache( cache.getCacheVal() ) );
			cache.applyAuxCache( new WritePrecisionCache<T>( cache.getCacheVal() ) );
			final String sta = prec.writeDesc( (WritePrecisionCache<T>)( cache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WritePrecisionCache.class)) ) ) , ps);
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
		BigInteger val = this.val;
		BigInteger prec = this.prec.getVal();
		if( ( Double.isNaN( prec.doubleValue() ) ) || ( Double.isInfinite( prec.doubleValue() ) ) ||
				( Double.isNaN( val.doubleValue() ) ) || ( Double.isInfinite( val.doubleValue() ) ) )
		{
			
			final BigInteger dval = ( ( new BigDecimal( Double.MAX_VALUE ) ).toBigInteger() ).multiply( prec );
			final BigInteger vvalv = ( val.divide( dval ).abs().add( BigInteger.ONE ) );
			final BigInteger vvalp = ( prec.divide( dval ).abs().add( BigInteger.ONE ) );
			final BigInteger vval = vvalv.compareTo( vvalp ) > 0 ? vvalv : vvalp;
			
			val = val.divide( vval );
			prec = prec.divide( vval );
			
			while( ( Double.isNaN( prec.doubleValue() ) ) || ( Double.isInfinite( prec.doubleValue() ) ) ||
					( Double.isNaN( val.doubleValue() ) ) || ( Double.isInfinite( val.doubleValue() ) ) )
			{
				val = val.divide( BigInteger.valueOf( 2 ) );
				prec = prec.divide( BigInteger.valueOf( 2 ) );
			}
		}
		return( ( val.doubleValue() ) / ( prec.doubleValue() ) );
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

