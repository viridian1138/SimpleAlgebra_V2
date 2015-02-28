



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.ga.SpacetimeAlgebraOrd;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicNegate;



/**
 * Tests pseudoscalar identities.
 * 
 * @author thorngreen
 * 
 */
public class TestPseudoscalar extends TestCase {
	

	
	
	
	/**
	 * Tests pseudoscalar squares to -1 in 4-D
	 */
	public void testPseudoscalar4D() throws NotInvertibleException
	{
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> dl2 = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( dl );
		
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(dl2, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 0 ) );
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		keyA.add( BigInteger.valueOf( 3 ) );
		
		
		mv.setVal( keyA , dl2.identity() );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> mv2 = mv.mult( mv );
		
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> d2v = mv2.get( new HashSet<BigInteger>() );
		
		
		Assert.assertTrue( d2v instanceof SymbolicNegate );
		
		
		final SymbolicNegate<DoubleElem,DoubleElemFactory> d2vv = (SymbolicNegate<DoubleElem,DoubleElemFactory>) d2v;
		
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> d2vva = d2vv.getElem();
		
		
		Assert.assertTrue( d2vva instanceof SymbolicIdentity );
		
		
	}
	
	
	
	
	/**
	 * Tests pseudoscalar squares to -1 in 3-D
	 */
	public void testPseudoscalar3D() throws NotInvertibleException
	{
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> dl2 = 
				new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( dl );
		
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>(dl2, td, ord);
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> mv = se.zero();
		
		HashSet<BigInteger> keyA = new HashSet<BigInteger>();
		
		keyA.add( BigInteger.valueOf( 0 ) );
		keyA.add( BigInteger.valueOf( 1 ) );
		keyA.add( BigInteger.valueOf( 2 ) );
		
		
		mv.setVal( keyA , dl2.identity() );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> mv2 = mv.mult( mv );
		
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> d2v = mv2.get( new HashSet<BigInteger>() );
		
		
		Assert.assertTrue( d2v instanceof SymbolicNegate );
		
		
		final SymbolicNegate<DoubleElem,DoubleElemFactory> d2vv = (SymbolicNegate<DoubleElem,DoubleElemFactory>) d2v;
		
		
		final SymbolicElem<DoubleElem,DoubleElemFactory> d2vva = d2vv.getElem();
		
		
		Assert.assertTrue( d2vva instanceof SymbolicIdentity );
		
		
	}
	
	
	
	
}


