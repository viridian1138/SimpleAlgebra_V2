
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

package simplealgebra.constants;



import java.math.BigInteger;



/**
 * Class for calculating the digits of <math display="inline">
 * <mrow>
 * <mi>e</mi>
 * </mrow>
 * </math>.
 * 
 * Adapted from http://stackoverflow.com/questions/3028282/an-efficient-way-to-compute-mathematical-constant-e
 * 
 * @author tgreen
 *
 */
public class EDigits {
	
	
//	/**
//	 * Initial value for the elements of the temporary array.
//	 */
//	private static final int ARRINIT = 2000;
//	
	
	/**
	 * Constant for the number ten.
	 */
	private static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	/**
	 * The numerator for <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 */
	protected BigInteger eNumer = BigInteger.ZERO;
	
	
	/**
	 * The denominator for <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.  This is always in the form of <math display="inline">
     * <mrow>
     *  <msup>
     *          <mn>10</mn>
     *        <mi>n</mi>
     *  </msup>
     * </mrow>
     * </math>.
	 */
	protected BigInteger eDenom = BigInteger.ONE;
	
	
	
	void eDigits( int digits )
	{
		int N = digits + 9;
		final int[] a = new int[ digits + 9 ];
		int x = 0;
		a[ 0 ] = 0;
		a[ 1 ] = 2;
		
		for( int n = 2 ; n < N ; ++n )
		{
			a[ n ] = 1;
		}
		
		for( ; N > 9 ; --N )
		{
			for( int n = N - 1 ; n > 0 ; --n )
			{
				a[ n ] = x % n;
				x = 10 * a[ n - 1 ] + x / n;
			}
			
			appendDigits( String.format( "%d" , x ) );
		}
		
		eNumer = eNumer.multiply( TEN );
	}
	

	/**
	 * Appends a set of digits to the value of <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 * 
	 * @param in The digits to append.
	 */
	protected void appendDigits( final String in )
	{
		for( int cnt = 0 ; cnt < in.length() ; cnt++ )
		{
			eNumer = ( eNumer.multiply( TEN ) ).add( new BigInteger( "" + ( in.charAt( cnt ) ) ) );
			eDenom = eDenom.multiply( TEN );
		}
	}
	
	
	
	
	
	/**
	 * Gets the numerator for <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 * 
	 * @return The numerator.
	 */
	public BigInteger getENumer() {
		return eNumer;
	}


	/**
	 * Gets the denominator for <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 * 
	 * @return The denominator.
	 */
	public BigInteger getEDenom() {
		return eDenom;
	}

	
	
}

