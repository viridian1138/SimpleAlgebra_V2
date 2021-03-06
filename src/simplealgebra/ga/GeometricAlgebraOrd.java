





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
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import simplealgebra.NumDimensions;



/**
 * Defines multiplication rules for Geometric Algebra.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the algebra.
 */
public class GeometricAlgebraOrd<U extends NumDimensions> extends Ord<U> {

	
	@Override
	public boolean calcOrd(HashSet<BigInteger> ka, HashSet<BigInteger> kb, HashSet<BigInteger> el , U dim ) 
	{
		boolean negate = false;
		
		final TreeSet<BigInteger> kaa = new TreeSet<BigInteger>( ka );
		final TreeSet<BigInteger> kbb = new TreeSet<BigInteger>( kb );
		
		
		final int sz = kaa.size() + kbb.size();
		
		
		final BigInteger[] arr = new BigInteger[ sz ];
		
		
		int cnt = 0;
		for( final BigInteger ii : kaa )
		{
			arr[ cnt ] = ii;
			cnt++;
		}
		for( final BigInteger ii : kbb )
		{
			arr[ cnt ] = ii;
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
	public HashSet<BigInteger> suggestNegativeSquare( U dim )
	{

		
		{
			HashSet<BigInteger> k0 = new HashSet<BigInteger>();
			
			for( BigInteger i = BigInteger.ZERO ; i.compareTo( dim.getVal() ) < 0 ; i = i.add( BigInteger.ONE )  )
			{
				k0.add( i );
			}
			
			HashSet<BigInteger> el = new HashSet<BigInteger>();
			final boolean negate = calcOrd( k0 , k0 , el , dim );
			if( negate && ( el.size() == 0 ) )
			{
				return( k0 );
			}
		}
		
		
		if( dim.getVal().compareTo( BigInteger.valueOf( 2 ) ) >= 0 )
		{
			HashSet<BigInteger> k0 = new HashSet<BigInteger>();
			
			k0.add( BigInteger.ZERO );
			k0.add( BigInteger.ONE );
			
			HashSet<BigInteger> el = new HashSet<BigInteger>();
			final boolean negate = calcOrd( k0 , k0 , el , dim );
			if( negate && ( el.size() == 0 ) )
			{
				return( k0 );
			}
		}
		
		
		return( null );
		
	}
	
	
	

}

