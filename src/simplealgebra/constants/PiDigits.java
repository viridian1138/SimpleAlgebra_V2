
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
 * <mi>&pi;</mi>
 * </mrow>
 * </math>.
 * 
 * Adapted from http://codecodex.com/wiki/Calculate_digits_of_pi
 * 
 * @author tgreen
 *
 */
public class PiDigits {
	
	
	private static final int SCALE = 10000;
	
	
	/**
	 * Initial value for the elements of the temporary array.
	 */
	private static final int ARRINIT = 2000;
	
	
	/**
	 * Constant for the number ten.
	 */
	private static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	/**
	 * The numerator for <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 */
	protected BigInteger piNumer = BigInteger.ZERO;
	
	
	/**
	 * The denominator for <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
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
	protected BigInteger piDenom = BigInteger.ONE;
	
	
	
	void piDigits( int digits )
	{
		final int[] arr = new int[ digits + 1 ];
		int carry = 0;
		
		for( int i = 0 ; i <= digits ; ++i )
		{
			arr[ i ] = ARRINIT;
		}
		
		for( int i = digits ; i > 0 ; i -= 14 )
		{
			int sum = 0;
			for( int j = i ; j > 0 ; --j )
			{
				sum = sum * j + SCALE * arr[ j ];
				arr[ j ] = sum % ( j * 2 - 1 );
				sum /= j * 2 - 1;
			}
			
			appendDigits( String.format( "%04d" , carry + sum / SCALE ) );
			carry = sum % SCALE;
		}
		
		piNumer = piNumer.multiply( TEN );
	}
	

	/**
	 * Appends a set of digits to the value of <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 * 
	 * @param in The digits to append.
	 */
	protected void appendDigits( final String in )
	{
		for( int cnt = 0 ; cnt < in.length() ; cnt++ )
		{
			piNumer = ( piNumer.multiply( TEN ) ).add( new BigInteger( "" + ( in.charAt( cnt ) ) ) );
			piDenom = piDenom.multiply( TEN );
		}
	}
	
	
	
	
	
	/**
	 * Gets the numerator for <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 * 
	 * @return The numerator.
	 */
	public BigInteger getPiNumer() {
		return piNumer;
	}


	/**
	 * Gets the denominator for <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 * 
	 * @return The denominator.
	 */
	public BigInteger getPiDenom() {
		return piDenom;
	}

	
	
}

