






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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;



/**
 * Tests the ability to find the intersection between lines in 2-D.
 * 
 * @author tgreen
 *
 */
public class TestLineIntersection extends TestCase {




/**
 * Tests the ability to find the intersection between lines in 2-D.
 * The formula for this is from Equation 99 in "Research Issues of Geometry-Based Visual Languages and Some Solutions", Dissertation, Arizona State University, 1999.
 * 
 * The first line is defined by the point b0, and the direction d0.
 * 
 * The second line is defined by the point b1, and the direction d1.
 * 
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
public void testLineIntersection( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
	
	final DoubleElemFactory fac = new DoubleElemFactory();
	
	final TestDimensionTwo td = new TestDimensionTwo();
	
	final HashSet<BigInteger> sca = new HashSet<BigInteger>();
	final HashSet<BigInteger> e0 = new HashSet<BigInteger>();
	final HashSet<BigInteger> e1 = new HashSet<BigInteger>();
	
	e0.add( BigInteger.ZERO );
	e1.add( BigInteger.ONE );
	
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> b0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b0.setVal( e0 ,  new DoubleElem( 0.33 ) );
	b0.setVal( e1 ,  new DoubleElem( 0.33 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> b1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	b1.setVal( e0 ,  new DoubleElem( 0.55 ) );
	b1.setVal( e1 ,  new DoubleElem( 0.35 ) );
	

	
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d0
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d0.setVal( e0 ,  new DoubleElem( 0.33 ) );
	d0.setVal( e1 ,  new DoubleElem( 0.80 ) );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d1
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
	d1.setVal( e0 ,  new DoubleElem( -0.33 ) );
	d1.setVal( e1 ,  new DoubleElem( 0.70 ) );
	

	
	DoubleElem d0l = (DoubleElem)( d0.totalMagnitude() );
	DoubleElem d1l = (DoubleElem)( d1.totalMagnitude() );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d0lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d0lm.setVal( sca ,  d0l.powR( new DoubleElem( -0.5 ), 20 , 20 ) );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> d1lm
		= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	d1lm.setVal( sca ,  d1l.powR( new DoubleElem( -0.5 ), 20 , 20 ) );
	

	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> ta
		= d0.mult( d0lm );
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> tb
		= d1.mult( d1lm );
	
	
	
	final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>> argtb
		= new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>>();
	argtb.add( tb );
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> denom
		= ta.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , argtb );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> numer
		= ( b1.add( b0.negate() ) ).handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , argtb );
	
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> div
		= numer.mult( denom.invertLeft() );
	
	
	final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> p
		= ( ta.mult( div ) ).add( b0 );
	
	
	System.out.println( p.getVal( e0 ).getVal() );
	System.out.println( p.getVal( e1 ).getVal() );
	
}




	
	
	

}

