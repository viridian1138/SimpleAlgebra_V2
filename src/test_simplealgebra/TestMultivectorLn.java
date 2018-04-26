






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
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;

/**
 * Tests for multivector logarithms.
 * 
 * @author tgreen
 *
 */
public class TestMultivectorLn extends TestCase {

	/**
	 * Simple test for a multivector log for a 5-D multivector.
	 * @throws MultiplicativeDistributionRequiredException 
	 */
	public void testMultivectorLnSimple( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		final TestDimensionFive td = new TestDimensionFive();
		
		final double s = -3.0;
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> el
			= new GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory>( new DoubleElem( s ) , fac , td , new GeometricAlgebraOrd<TestDimensionFive>() );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> ln
			= el.ln( 20 , 20 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFive,GeometricAlgebraOrd<TestDimensionFive>,DoubleElem,DoubleElemFactory> exp = ln.exp( 20 );
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> ii : exp.getEntrySet() )
		{
			HashSet<BigInteger> h = ii.getKey();
			Assert.assertEquals( el.getVal( ii.getKey() ).getVal()  , ii.getValue().getVal() , 1E-5 );
		}
		
		
		for( final Entry<HashSet<BigInteger>, DoubleElem> ii : el.getEntrySet() )
		{
			HashSet<BigInteger> h = ii.getKey();
			Assert.assertEquals( exp.getVal( ii.getKey() ).getVal()  , ii.getValue().getVal() , 1E-5 );
		}
		
		
	}
	

}

