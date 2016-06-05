
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
 * Class for calculating the digits of  <math display="inline">
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
	
	
	private static final int ARRINIT = 2000;
	
	
	private static final BigInteger TEN = BigInteger.valueOf( 10 );
	
	
	protected BigInteger piNumer = BigInteger.ZERO;
	
	
	protected BigInteger piDenom = BigInteger.ONE;
	
	
	void piDigits( int digits )
	{
		int[] arr = new int[ digits + 1 ];
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
	
	
	protected void appendDigits( final String in )
	{
		for( int cnt = 0 ; cnt < in.length() ; cnt++ )
		{
			piNumer = ( piNumer.multiply( TEN ) ).add( new BigInteger( "" + ( in.charAt( cnt ) ) ) );
			piDenom = piDenom.multiply( TEN );
		}
	}
	
	
	
	
	
	/**
	 * @return the piNumer
	 */
	public BigInteger getPiNumer() {
		return piNumer;
	}


	/**
	 * @return the piDenom
	 */
	public BigInteger getPiDenom() {
		return piDenom;
	}

	
	
}
