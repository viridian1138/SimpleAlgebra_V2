



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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.*;


/**
 * Tests the wedge product between two bivectors.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestBivectorWedge extends TestCase {
	
	
	
	/**
	 * Tests the wedge product between two bivectors.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testBivectorWedgeA( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final GeometricAlgebraOrd<TestDimensionFive> ord = new GeometricAlgebraOrd<TestDimensionFive>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mvA = se.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> mvB = se.zero();
		
		
		
		for( int cntA = 0 ; cntA < TestDimensionFive.FIVE ; cntA++ )
		{
			for( int cntB = cntA +1 ; cntB < TestDimensionFive.FIVE ; cntB++ )
			{
				final HashSet<BigInteger> key = new HashSet<BigInteger>();
				key.add( BigInteger.valueOf( cntA ) );
				key.add( BigInteger.valueOf( cntB ) );
				mvA.setVal( key , new DoubleElem( rand.nextDouble() ) );
				mvB.setVal( key , new DoubleElem( rand.nextDouble() ) );
			}
		}
		
		
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>>();
		
		args.add( mvB );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> 
			prod = mvA.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> e : prod.getEntrySet() )
		{
			Assert.assertTrue( e.getKey().size() == 4 );
		}
		
		
	}
	
	
	
}



