





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





package test_simplealgebra;



import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

import simplealgebra.NumDimensions;
import simplealgebra.ga.Ord;


/**
 * Defines multiplication rules for a test algebra where left/right inverses are different.  This should be used for tests only, and only in 2-D.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * Note: unlike standard Geometric Algebra elems, this Ord produces a non-standard elem that is not associative.  Hence, operations that rely on associativity may not work properly.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the algebra.
 */
public class TestInvOrd<U extends NumDimensions> extends Ord<U> {

	
	@Override
	public boolean calcOrd(HashSet<BigInteger> ka, HashSet<BigInteger> kb, HashSet<BigInteger> el , U dim ) 
	{
		
		if( ( ka.size() == 1 ) && ( kb.size() == 1 ) )
		{
			BigInteger ba = ka.iterator().next();
			BigInteger bb = kb.iterator().next();
			if( ba.equals( BigInteger.ZERO ) )
			{
				if( bb.equals( BigInteger.ZERO ) )
				{
					el.add( BigInteger.ONE );
					return( false );
				}
				else
				{
					return( false );
				}
			}
			else
			{
				if( bb.equals( BigInteger.ZERO ) )
				{
					return( true );
				}
				else
				{
					el.add( BigInteger.ZERO );
					return( false );
				}
			}
		}
		
		
		if( ( ka.size() + kb.size() ) > 1 )
		{
			throw( new RuntimeException( "Inconsistent" ) );
		}
		
		
		for( final BigInteger ii : ka )
		{
			el.add( ii );
		}
		
		
		
		for( final BigInteger ii : kb )
		{
			el.add( ii );
		}

		
		return( false );
	}
	
	
	
	
	
	@Override
	public HashSet<BigInteger> suggestNegativeSquare( U dim )
	{	
		return( null );	
	}
	
	
		
	
	

}


