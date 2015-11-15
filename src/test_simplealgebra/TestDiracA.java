




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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.algo.*;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ddx.*;
import simplealgebra.ga.*;









/**
 * 
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <mo>&nabla;</mo>
 *  <mi>&Psi;</mi>
 *  <mi>i</mi>
 *  <msub>
 *          <mi>&sigma;</mi>
 *        <mn>3</mn>
 *   </msub>
 *   <mo>-</mo>
 *   <mfrac>
 *     <mrow>
 *       <mi>m</mi>
 *       <mi>c</mi>
 *     </mrow>
 *     <mrow>
 *       <mi>&hbar;</mi>
 *     </mrow>
 *   </mfrac>
 *   <mi>&Psi;</mi>
 *   <msub>
 *           <mi>&gamma;</mi>
 *         <mn>0</mn>
 *   </msub>
 *   <mo>=</mo>
 *   <mn>0</mn>
 * </mrow>
 * </math>
 * 
 * 
 * where <math display="inline">
 * <mrow>
 *  <mi>m</mi>
 * </mrow>
 * </math> and <math display="inline">
 * <mrow>
 *  <mi>&hbar;</mi>
 * </mrow>
 * </math> and <math display="inline">
 * <mrow>
 *  <mi>c</mi>
 * </mrow>
 * </math> are arbitrary constants.
 * 
 * 
 * 
 * See http://en.wikipedia.org/wiki/Spacetime_algebra
 * 
 * See http://quantummechanics.ucsd.edu/ph130a/130_notes/node45.html
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDiracA extends TestCase {
	
	
	/**
	 * Constant representing the number zero.
	 */
	private static final DoubleElem DOUBLE_ZERO = new DoubleElem( 0.0 );
	
	/**
	 * Arbitrary constant.
	 */
	private static final DoubleElem MM = genFromConstDbl( 2.0 );
	
	/**
	 * Constant representing the imaginary pseudoscalar.
	 */
	private static final GeometricAlgebraMultivectorElem<
	TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
	SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
		II = genII();
	
	
	/**
	 * Constant representing the imaginary pseudoscalar times sigma-3.
	 */
	private static final GeometricAlgebraMultivectorElem<
	TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
	SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
		IM3 = genIM3();
	
	
	/**
	 * Generates the constant for the imaginary pseudoscalar.
	 * 
	 * @return The constant for the imaginary pseudoscalar.
	 */
	private static final GeometricAlgebraMultivectorElem<
	TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
	SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
		genII( )
	{
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> de2 = new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( de , tdim , ord );
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> gxxMult
		=  
			( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( 
					genFromConstDbl( 1.0 ) , de ) , seA ) , se2A )
					);
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
		SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ret = new GeometricAlgebraMultivectorElem<
					TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A , tdim , ord );
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			hs.add( BigInteger.ONE );
			hs.add( BigInteger.valueOf( 2 ) );
			hs.add( BigInteger.valueOf( 3 ) );
			ret.setVal( hs , gxxMult );
		}
		
		return( ret );
		
	}
	
	
	
	/**
	 * Generates the constant representing the imaginary pseudoscalar times sigma-3.
	 * 
	 * @return Constant representing the imaginary pseudoscalar times sigma-3.
	 */
	private static final GeometricAlgebraMultivectorElem<
	TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
	SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
	SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
		genIM3( )
	{
		final TestDimensionFour tdim = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> de2 = new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( de , tdim , ord );
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> gxxMult
		=  
			( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( 
					genFromConstDbl( 1.0 ) , de ) , seA ) , se2A )
					);
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
		SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ret = new GeometricAlgebraMultivectorElem<
					TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A , tdim , ord );
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.valueOf( 3 ) );
			ret.setVal( hs , gxxMult );
		}
		
		return( genII().mult( ret ) );
		
	}
	
	
	/**
	 * Returns a multivector scalar that is equal to the parameter value.
	 * 
	 * @param in The value to be assigned to the scalar.
	 * @return The constructed multivector.
	 */
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genFromConst( double in )
	{
		final DoubleElem dd = new DoubleElem( in );
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( dd , da , new TestDimensionFour()  , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		return( ret );
	}
	
	
	/**
	 * Random number generator for producing initial conditions.
	 */
	private static final Random rand = new Random( 4455 );
	
	
	/**
	 * Generates a random multivector where all of the elements are between zero and twice the input value.
	 * @param in The input value.
	 * @return The random multivector.
	 */
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genDiffAll( double in )
	{
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour()  , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final DoubleElem dd = new DoubleElem( in * ( 2.0 * rand.nextDouble() ) );
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
			if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
			if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
			if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
			if( /* ( ( hs.size() ) % 2 ) == 0 */ true )
			{
				ret.setVal( hs , dd );
			}
		}
		return( ret );
	}
	
	
	
	/**
	 * Generates a random multivector where all of the elements of odd grade are between zero and twice the input value, and all elements of even grade are between zero and 2E-8 times the input value..
	 * @param in The input value.
	 * @return The random multivector.
	 */
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genDiffEnt( double in )
	{
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour()  , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final DoubleElem dd = new DoubleElem( in * ( 2.0 * rand.nextDouble() ) );
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
			if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
			if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
			if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
			if( ( ( hs.size() ) % 2 ) == 0 )
			{
				ret.setVal( hs , dd );
			}
			else
			{
				final DoubleElem dd2 = new DoubleElem( 1E-8 * ( 2.0 * rand.nextDouble() ) );
				ret.setVal( hs , dd2 );
			}
		}
		return( ret );
	}
	
	
	/**
	 * Returns a DoubleElem with the value from the parameter.
	 * @param in The input parameter.
	 * @return The generated DoubleElem.
	 */
	private static DoubleElem genFromConstDbl( double in )
	{
		return( new DoubleElem( in ) );
	}
	
	
	
	/**
	 * Arbitrary constant.
	 */
	protected static final DoubleElem HBAR = genFromConstDbl( 0.5 );
	
	/**
	 * Arbitrary constant.
	 */
	protected static final DoubleElem C = genFromConstDbl( 0.004 );
	
	
	
	/**
	 * The total measurement size along the X-Axis.
	 */
	protected static final double TOTAL_X_AXIS_SIZE = 0.1;
	
	/**
	 * The total measurement size along the Y-Axis.
	 */
	protected static final double TOTAL_Y_AXIS_SIZE = 0.1;
	
	/**
	 * The total measurement size along the Z-Axis.
	 */
	protected static final double TOTAL_Z_AXIS_SIZE = 0.1;
	
	
	/**
	 * The number of discretizations on the T-Axis over which to iterate.
	 */
	protected static final int NUM_T_ITER = 7;
	
	/**
	 * The number of discretizations on the X-Axis over which to iterate.
	 */
	protected static final int NUM_X_ITER = 10;
	
	/**
	 * The number of discretizations on the Y-Axis over which to iterate.
	 */
	protected static final int NUM_Y_ITER = 10;
	
	/**
	 * The number of discretizations on the Z-Axis over which to iterate.
	 */
	protected static final int NUM_Z_ITER = 10;
	
	
	
	/**
	 * Size of the T-Axis discretization.
	 */
	protected static final DoubleElem T_HH = genFromConstDbl( 0.0025 );
	
	/**
	 * Size of the X-Axis discretization.
	 */
	protected static final DoubleElem X_HH = genFromConstDbl( TOTAL_X_AXIS_SIZE / NUM_X_ITER /* 0.01 */ );
	
	/**
	 * Size of the Y-Axis discretization.
	 */
	protected static final DoubleElem Y_HH = genFromConstDbl( TOTAL_Y_AXIS_SIZE / NUM_Y_ITER /* 0.01 */ );
	
	/**
	 * Size of the Z-Axis discretization.
	 */
	protected static final DoubleElem Z_HH = genFromConstDbl( TOTAL_Z_AXIS_SIZE / NUM_Z_ITER /* 0.01 */ );
	
	/**
	 * Discretization sizes arrayed by coordinate index.
	 */
	protected static final DoubleElem[] HH = { T_HH , X_HH , Y_HH , Z_HH };
	
	
	
	/**
	 * The halfway iteration point in X.
	 */
	protected static final int HALF_X = NUM_X_ITER / 2;
	
	/**
	 * The halfway iteration point in Y.
	 */
	protected static final int HALF_Y = NUM_Y_ITER / 2;
	
	/**
	 * The halfway iteration point in Z.
	 */
	protected static final int HALF_Z = NUM_Z_ITER / 2;
	
	
	/**
	 * The initial condition radius in X.
	 */
	protected static final double RAD_X = NUM_X_ITER / 10.0;
	
	/**
	 * The initial condition radius in Y.
	 */
	protected static final double RAD_Y = NUM_Y_ITER / 10.0;
	
	/**
	 * The initial condition radius in Z.
	 */
	protected static final double RAD_Z = NUM_Z_ITER / 10.0;
	
	
	
	
	
	/**
	 * Temp step size in the T-direction.
	 */
	protected static final int NSTPT = 1;
	
	
	/**
	 * Temp step size in the X-direction.
	 */
	protected static final int NSTPX = 1;
	
	
	/**
	 * Temp step size in the Y-direction.
	 */
	protected static final int NSTPY = 1;
	
	
	/**
	 * Temp step size in the Z-direction.
	 */
	protected static final int NSTPZ = 1;
	
	
	/**
	 * Indicates whether predictor-corrector should be used while iterating.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	// protected static final boolean USE_PREDICTOR_CORRECTOR = true;
	
	
	
	
	
	
	/**
	 * Result array over which to iterate.
	 */
	protected static final GeometricAlgebraMultivectorElem[][][][] iterArray = new GeometricAlgebraMultivectorElem[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ];
	
	
	
	/**
	 * Discretized index for the T-Axis.
	 */
	protected static final int TV = 0;
	
	/**
	 * Discretized index for the X-Axis.
	 */
	protected static final int XV = 1;
	
	/**
	 * Discretized index for the Y-Axis.
	 */
	protected static final int YV = 2;
	
	/**
	 * Discretized index for the Z-Axis.
	 */
	protected static final int ZV = 3;
	
	
	
	/**
	 * Temporary array in which to generate descent algorithm solutions.
	 * <p>0 = T
	 * <p>1 = X
	 * <p>2 = Y
	 * <p>3 = Z
	 */
	private static GeometricAlgebraMultivectorElem[][][][] tempArray = new GeometricAlgebraMultivectorElem[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void performIterationUpdate( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dbl )
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> va
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va == null )
		{
			tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = dbl;
			return;
		}
		tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = va.add( dbl );
	}
	
	
	
	
	/**
	 * Given a change calculated by a descent algorithm iteration,
	 * applies the change to the temp array.
	 * 
	 * @param dbl The change to apply to the temp array.
	 */
	protected static void setIterationValue( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dbl )
	{
		tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] = dbl;
	}
	
	
	
	
	/**
	 * Returns the result of the descent algorithm iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> getUpdateValue()
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> va
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va != null )
		{
			return( va );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		return( ret );
	}
	
	
	/**
	 * Returns the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @return The value in the temp array.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> getCorrectionValue()
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> va
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( va != null )
		{
			return( va );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		return( ret );
	}
	
	
	/**
	 * Resets the predictor-correction value of the iterations
	 * from the temp array.
	 * 
	 * @param in The value to which to reset.
	 */
	protected static void resetCorrectionValue( final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> in )
	{
		tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] = in;
	}
	
	
	/**
	 * Applies a predictor-corrector process to the temp array.
	 * 
	 * See https://en.wikipedia.org/wiki/Predictor%E2%80%93corrector_method
	 */
	protected static void applyPredictorCorrector()
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vam2
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vam1
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> vam
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ NSTPT * 2 ][ NSTPX ][ NSTPY ][ NSTPZ ] );
		if( vam2 == null )
		{
			DoubleElemFactory da = new DoubleElemFactory();
			vam2 = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		}
		if( vam1 == null )
		{
			DoubleElemFactory da = new DoubleElemFactory();
			vam1 = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		}
		if( vam == null )
		{
			DoubleElemFactory da = new DoubleElemFactory();
			vam = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		}
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> slopePrev 
			= vam1.add( vam2.negate() );
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> slopeNew 
			= vam.add( vam1.negate() );
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> avgSlope 
			= ( slopePrev.add( slopeNew ) ).divideBy( BigInteger.valueOf( 2 ) );
		tempArray[ NSTPT * 2 - 1 ][ NSTPX ][ NSTPY ][ NSTPZ ] = 
				vam2.add( avgSlope );
	}
	
	
	
	
	
	/**
	 * Input parameter for fillTempArrayInner()
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TempArrayFillInnerParam
	{
		
		
		
		
		
		
		/**
		 * Gets the T-Axis index for the center in the iter array.
		 * 
		 * @return The T-Axis index for the center in the iter array.
		 */
		public int getTcnt() {
			return tcnt;
		}


		/**
		 * Sets the T-Axis index for the center in the iter array.
		 * 
		 * @param tcnt The T-Axis index for the center in the iter array.
		 */
		public void setTcnt(int tcnt) {
			this.tcnt = tcnt;
		}


		/**
		 * Gets the X-Axis index for the center in the iter array.
		 * 
		 * @return The X-Axis index for the center in the iter array.
		 */
		public int getXcnt() {
			return xcnt;
		}


		/**
		 * Sets the X-Axis index for the center in the iter array.
		 * 
		 * @param xcnt The X-Axis index for the center in the iter array.
		 */
		public void setXcnt(int xcnt) {
			this.xcnt = xcnt;
		}


		/**
		 * Gets the Y-Axis index for the center in the iter array.
		 * 
		 * @return The Y-Axis index for the center in the iter array.
		 */
		public int getYcnt() {
			return ycnt;
		}


		/**
		 * Sets the Y-Axis index for the center in the iter array.
		 * 
		 * @param ycnt The Y-Axis index for the center in the iter array.
		 */
		public void setYcnt(int ycnt) {
			this.ycnt = ycnt;
		}


		/**
		 * Gets the Z-Axis index for the center in the iter array.
		 * 
		 * @return The Z-Axis index for the center in the iter array.
		 */
		public int getZcnt() {
			return zcnt;
		}


		/**
		 * Sets the Z-Axis index for the center in the iter array.
		 * 
		 * @param zcnt The Z-Axis index for the center in the iter array.
		 */
		public void setZcnt(int zcnt) {
			this.zcnt = zcnt;
		}


		/**
		 * Gets the T-Axis iteration of the array fill.
		 * 
		 * @return The T-Axis iteration of the array fill.
		 */
		public int getTa() {
			return ta;
		}


		/**
		 * Sets the T-Axis iteration of the array fill.
		 * 
		 * @param ta The T-Axis iteration of the array fill.
		 */
		public void setTa(int ta) {
			this.ta = ta;
		}


		/**
		 * Gets the X-Axis iteration of the array fill.
		 * 
		 * @return The X-Axis iteration of the array fill.
		 */
		public int getXa() {
			return xa;
		}


		/**
		 * Sets the X-Axis iteration of the array fill.
		 * 
		 * @param xa The X-Axis iteration of the array fill.
		 */
		public void setXa(int xa) {
			this.xa = xa;
		}


		/**
		 * Gets the Y-Axis iteration of the array fill.
		 * 
		 * @return The Y-Axis iteration of the array fill.
		 */
		public int getYa() {
			return ya;
		}


		/**
		 * Sets the Y-Axis iteration of the array fill.
		 * 
		 * @param ya The Y-Axis iteration of the array fill.
		 */
		public void setYa(int ya) {
			this.ya = ya;
		}


		/**
		 * Gets the Z-Axis iteration of the array fill.
		 * 
		 * @return The Z-Axis iteration of the array fill.
		 */
		public int getZa() {
			return za;
		}


		/**
		 * Sets the Z-Axis iteration of the array fill.
		 * 
		 * @param za The Z-Axis iteration of the array fill.
		 */
		public void setZa(int za) {
			this.za = za;
		}


		/**
		 * The T-Axis index for the center in the iter array.
		 */
		protected int tcnt;
		
		/**
		 * The X-Axis index for the center in the iter array.
		 */
		protected int xcnt;
		
		/**
		 * The Y-Axis index for the center in the iter array.
		 */
		protected int ycnt;
		
		/**
		 * The Z-Axis index for the center in the iter array.
		 */
		protected int zcnt;
		
		/**
		 * The T-Axis iteration of the array fill.
		 */
		protected int ta;
		
		/**
		 * The X-Axis iteration of the array fill.
		 */
		protected int xa;
		
		/**
		 * The Y-Axis iteration of the array fill.
		 */
		protected int ya;
	
		/**
		 * The Z-Axis iteration of the array fill.
		 */
		protected int za;
		
	
	}
	
	
	
	
	
	
	/**
	 * Fills one element of the temp array with an element from the iter array.
	 * 
	 * @param param Input parameter describing where to get the element.
	 */
	protected static void fillTempArrayInner( TempArrayFillInnerParam param )
	{
		final int tcnt = param.getTcnt();
		final int xcnt = param.getXcnt();
		final int ycnt = param.getYcnt();
		final int zcnt = param.getZcnt();
		
		final int ta = param.getTa();
		final int xa = param.getXa();
		final int ya = param.getYa();
		final int za = param.getZa();
		
		final int tv = tcnt + ta;
		final int xv = xcnt + xa;
		final int yv = ycnt + ya;
		final int zv = zcnt + za;
		GeometricAlgebraMultivectorElem av = null;
		if( ( tv >= 0 )  && ( xv >= 0 ) && ( yv >= 0 ) && ( zv >= 0 ) &&
			( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) && ( yv < NUM_Y_ITER ) && ( zv < NUM_Z_ITER )  )
		{
			av = iterArray[ tv ][ xv ][ yv ][ zv ];
		}
		tempArray[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = av;
		
	}
	
	
	
	
	/**
	 * Fills the temp array with elements from the iter array.
	 * 
	 * @param tcnt The T-Axis index for the center in the iter array.
	 * @param xcnt The X-Axis index for the center in the iter array.
	 * @param ycnt The Y-Axis index for the center in the iter array.
	 * @param zcnt The Z-Axis index for the center in the iter array.
	 */
	protected static void fillTempArray( final int tcnt , final int xcnt , final int ycnt , final int zcnt )
	{
		final TempArrayFillInnerParam param = new TempArrayFillInnerParam();
		
		param.setTcnt( tcnt );
		param.setXcnt( xcnt );
		param.setYcnt( ycnt );
		param.setZcnt( zcnt );
		
		for( int ta = -NSTPT ; ta < NSTPT ; ta++ )
		{
			param.setTa( ta );
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				param.setXa( xa );
				for( int ya = -NSTPY ; ya < NSTPY + 1 ; ya++ )
				{
					param.setYa( ya );
					for( int za = -NSTPX ; za < NSTPZ + 1 ; za++ )
					{
						param.setZa( za );
						fillTempArrayInner( param );
					}
				}
			}
		}
		
		
		// Overlay initial seed for iterations.
		for( int xa = 0 ; xa < NSTPX * 2 + 1 ; xa++ )
		{
			for( int ya = 0 ; ya < NSTPY * 2 + 1 ; ya++ )
			{
				for( int za = 0 ; za < NSTPZ * 2 + 1 ; za++ )
				{
					tempArray[ NSTPT * 2 ][ xa ][ ya ][ za ] = tempArray[ NSTPT * 2 - 1 ][ xa ][ ya ][ za ];
				}
			}
		}
		
	}
	
	
	
	
	/**
	 * Test array used to verify that the entire temp array has been filled.
	 */
	private static int[][][][] spatialAssertArray = new int[ NSTPT * 2 + 1 ][ NSTPX * 2 + 1 ][ NSTPY * 2 + 1 ][ NSTPZ * 2 + 1 ];
	
	
	
	/**
	 * Clears the test array used to verify that the entire temp array has been filled.
	 */
	protected static void clearSpatialAssertArray( )
	{
		for( int ta = -NSTPT ; ta < NSTPT + 1 ; ta++ )
		{
			for( int xa = -NSTPX ; xa < NSTPX + 1 ; xa++ )
			{
				for( int ya = -NSTPY ; ya < NSTPY + 1 ; ya++ )
				{
					for( int za = -NSTPZ ; za < NSTPZ + 1 ; za++ )
					{
						spatialAssertArray[ ta + NSTPT ][ xa + NSTPX ][ ya + NSTPY ][ za + NSTPZ ] = 0;
					}
				}
			}
		}
	}
	
	
	
	
	
/**
 * Returns a unit vector pointing down a particular axis.
 * 	
 * @param axis The index of the axis for the vector.
 * @return The unit vector.
 */
private GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genUnitVector( int axis )
{
	final DoubleElem dd = new DoubleElem( 1.0 );
	DoubleElemFactory da = new DoubleElemFactory();
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	HashSet<BigInteger> ind = new HashSet<BigInteger>();
	ind.add( BigInteger.valueOf( axis ) );
	ret.setVal( ind , dd );
	return( ret );
}





/**
 * Returns a unit bivector containing one component from the time axis and one component from a spatial axis.
 * 
 * @param axis The index of the spatial axis.
 * @return The unit bivector.
 */
private GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genTimeBivector( int axis )
{
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>  st = genUnitVector( axis );
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> st0 = genUnitVector( 0 );
	return( st.mult( st0 ) );
}
	
	


	
/**
 * Returns the sum of the squares of all of the multivector elements.
 * 	
 * @param in The input tensor.
 * @return The sum of the squares of the multivector values.
 */	
private double calcMagnitudeSq( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> in )
{
	double db = 0.0;
	Iterator<HashSet<BigInteger>> it = in.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> elem = it.next();
		final DoubleElem dbl = in.get( elem );
		final double vl = dbl.getVal();
		db += vl * vl;
	}
	return( db );
}




/**
 * Returns the square of the magnitude of a DoubleElem.
 * 
 * @param in The DoubleElem.
 * @return The square of the magnitude of the DoubleElem.
 */
private double calcMagnitudeSq( DoubleElem in )
{
	double db = in.getVal();
	return( db * db );
}




/**
 * Returns whether two tensors are exactly equal.
 * 
 * @param a Tensor to be checked for equality.
 * @param b Tensor to be checked for equality.
 * @return True iff. the two tensors are exactly equal.
 */
private boolean calcEq( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a , GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b )
{
	Iterator<HashSet<BigInteger>> it = a.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> elem = it.next();
		final DoubleElem dbla = a.get( elem );
		final DoubleElem dblb = b.get( elem );
		if( ( dbla == null ) || ( dblb == null ) )
		{
			return( false );
		}
		if( dbla.getVal() != dblb.getVal() )
		{
			return( false );
		}
	}
	it = b.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> elem = it.next();
		final DoubleElem dbla = a.get( elem );
		final DoubleElem dblb = b.get( elem );
		if( ( dbla == null ) || ( dblb == null ) )
		{
			return( false );
		}
		if( dbla.getVal() != dblb.getVal() )
		{
			return( false );
		}
	}
	
	return( true );
}
	


/**
 * Defines a directional derivative for the test.
 * 
 * @author thorngreen
 *
 */
private class DDirec extends DirectionalDerivativePartialFactory<
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	Ordinate>
{

	/**
	 * Factory for building the value of the derivative of an ordinate.
	 */
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> de;
	
	/**
	 * Factory for the enclosed type.
	 */
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2;
	
	/**
	 * Constructs the directional derivative factory.
	 * 
	 * @param _de Factory for building the value of the derivative of an ordinate.
	 * @param _se2 Factory for the enclosed type.
	 */
	public DDirec( 
			final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> _de ,
			final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _se2 )
	{
		de = _de;
		se2 = _se2;
	}

	@Override
	public SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			getPartial( BigInteger basisIndex )
	{
		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
		
		wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
		
		SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			ret =
					new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtX );
		
		if( basisIndex.equals( BigInteger.ZERO ) )
		{
			try
			{
				final DoubleElemFactory de2 = new DoubleElemFactory();
				final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de2 );
				final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
				final DoubleElem cinv = C.invertLeft();
				final SymbolicElem<
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
					cmul = ( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( 
							cinv , de2 ) , seA ) , se2A )
							);
				ret = cmul.mult( ret );
			}
			catch( NotInvertibleException ex )
			{
				Assert.assertNull( ex );
			}
		}
			
		
		return( ret );
	}

};



/**
 * Node representing an ordinate of the coordinate space.
 * 
 * @author thorngreen
 *
 */
private class Ordinate extends SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
{
	/**
	 * The number of the ordinate.
	 */
	private int col;

	/**
	 * Constructs the node.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _col The number of the ordinate.
	 */
	public Ordinate(GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> _fac, int _col) {
		super(_fac);
		col = _col;
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "a" + col + "()" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof Ordinate )
		{
			return( col == ( (Ordinate) b ).col );
		}
		return( false );
	}
	
	@Override
	public boolean equals( Object b )
	{
		if( b instanceof Ordinate )
		{
			return( col == ( (Ordinate) b ).col );
		}
		return( false );
	}
	
	@Override
	public int hashCode()
	{
		return( col );
	}
	
	/**
	 * Returns the number of the ordinate.
	 * 
	 * @return The number of the ordinate.
	 */
	public int getCol() {
		return col;
	}

	
}


/**
 * A symbolic elem representing a constant value
 * at the base descent algorithm evaluation level.
 * 
 * @author thorngreen
 *
 */
private static class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( " " + ( this.getElem().getVal() ) );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
	{
		if( b instanceof SymbolicConst )
		{
			final boolean eq = ( getElem().getVal() ) == ( ( (SymbolicConst) b ).getElem().getVal() );
			return( eq );
		}
		return( false );
	}
	
}
	
	

/**
 * An elem representing a symbolic constant at the level of mapping 
 * discretized approximations of the underlying differential
 * equation into expressions (e.g. Jacobian slopes)
 * for descent algorithm evaluations.
 * 
 * @author thorngreen
 *
 */
private static class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public StelemReduction2L( SymbolicElem<DoubleElem,DoubleElemFactory> _elem, 
			SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reduce2L( " );
		getElem().writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof StelemReduction2L )
		{
			return( getElem().symbolicEquals( ( (StelemReduction2L) b ).getElem() ) );
		}
		return( false );
	}
	
}



/**
 * An elem representing a symbolic constant at the level
 * of mapping the underlying differential equation into
 * its discretized approximation.
 * 
 * @author thorngreen
 *
 */
private static class StelemReduction3L extends SymbolicReduction<
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public StelemReduction3L(
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _elem, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reduce3L( " );
		getElem().writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> b )
	{
		if( b instanceof StelemReduction3L )
		{
			return( getElem().symbolicEquals( ( (StelemReduction3L) b ).getElem() ) );
		}
		return( false );
	}
	
}
	


/**
 * A constant coefficient with a numerator and a denominator.
 * 	
 * @author thorngreen
 *
 */
private class CoeffNode
{
	/**
	 * The numerator.
	 */
	private SymbolicElem<DoubleElem,DoubleElemFactory> numer;
	
	/**
	 * The denominator.
	 */
	private SymbolicElem<DoubleElem,DoubleElemFactory> denom;
	
	/**
	 * Constructs the coefficient.
	 * 
	 * @param _numer The numerator.
	 * @param _denom The denominator.
	 */
	public CoeffNode( SymbolicElem<DoubleElem,DoubleElemFactory> _numer , SymbolicElem<DoubleElem,DoubleElemFactory> _denom )
	{
		numer = _numer;
		denom = _denom;
	}
	
	/**
	 * Gets the numerator.
	 * 
	 * @return The numerator.
	 */
	public SymbolicElem<DoubleElem,DoubleElemFactory> getNumer() {
		return numer;
	}
	
	/**
	 * Gets the denominator.
	 * 
	 * @return The denominator.
	 */
	public SymbolicElem<DoubleElem,DoubleElemFactory> getDenom() {
		return denom;
	}
	
}




	/**
	 * Elem representing the discretized equivalent
	 * of one component of the multivector constrained by the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private class BNelem extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
	{
		/**
		 * The index of the component of the multivector.
		 */
		protected HashSet<BigInteger> index;
		

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
		 * @param _index The index of the component of the multivector.
		 */
		public BNelem(DoubleElemFactory _fac, HashMap<Ordinate, BigInteger> _coord , HashSet<BigInteger> _index ) {
			super(_fac, _coord);
			index = _index;
		}
		
		
		/**
		 * Column indices in the discretized space.
		 */
		protected final int[] cols = new int[ 4 ];
		
		/**
		 * Assertion booleans used to verify that all
		 * column indices have been initialized.
		 */
		protected final boolean[] assertCols = new boolean[ 4 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			cols[ 2 ] = 0;
			cols[ 3 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			assertCols[ 2 ] = false;
			assertCols[ 3 ] = false;
			Assert.assertTrue( coord.keySet().size() == 4 );
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate keyCoord = ii.getKey();
				BigInteger coordVal = ii.getValue();
				final int offset = keyCoord.getCol() == 3 ? NSTPZ : keyCoord.getCol() == 2 ? NSTPY : keyCoord.getCol() == 1 ? NSTPX : NSTPT;
				cols[ keyCoord.getCol() ] = coordVal.intValue() + offset;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			Assert.assertTrue( assertCols[ 2 ] );
			Assert.assertTrue( assertCols[ 3 ] );
			DoubleElem ret = null;
			GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> val = 
					TestDiracA.tempArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ];
			if( val == null )
			{
				ret = fac.zero();
			}
			else
			{
				DoubleElem valA = val.get( index );
				if( valA == null )
				{
					ret = fac.zero();
				}
				else
				{
					ret = valA;
				}
			}
			return( ret );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( eval( implicitSpace ) );
		}

		@Override
		public void writeString( PrintStream ps ) {
			String s0 = "bn";
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate key = ii.getKey();
				BigInteger val = ii.getValue();
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "{";
			Iterator<BigInteger> ita = index.iterator();
			while( ita.hasNext() )
			{
				BigInteger b = ita.next();
				s0 = s0 + b + ",";
			}
			s0 = s0 + "}()";
			ps.print( s0 );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
				{
					Ordinate key = ii.getKey();
					BigInteger ka = ii.getValue();
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
	}
	
	
	
/**
 * Elem representing the symbolic expression for 
 * the discretized equivalent
 * of one component of the multivector constrained by the differential equation.
 * The partial derivatives of this elem generate
 * the slopes for producing descent algorithm iterations (e.g. the Jacobian slopes),
 * as opposed to partial derivatives for the underlying differential equation.
 * 
 * @author thorngreen
 *
 */	
private class CNelem extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{
	/**
	 * The index of the component of the multivector.
	 */
	protected HashSet<BigInteger> index;

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _coord Map taking implicit space terms representing ordinates to discrete ordinates of type BigInteger.
	 * @param _index The index of the component of the multivector.
	 */
	public CNelem(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, 
			HashMap<Ordinate, BigInteger> _coord , HashSet<BigInteger> _index ) {
		super(_fac, _coord);
		index = _index;
	}

	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( new BNelem( fac.getFac() , coord , index ) );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> key =
				new SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>( this , implicitSpace );
		final SymbolicElem<DoubleElem, DoubleElemFactory> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		final SymbolicElem<DoubleElem, DoubleElemFactory> ret = eval( implicitSpace );
		cache.put(key, ret);
		return( ret );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		if( withRespectTo.size() > 1 )
		{
			return( fac.zero() );
		}
		Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
		CNelem wrt = (CNelem)( it.next() );
		// final boolean cond = this.symbolicDEval( wrt );
		final boolean cond = this.symbolicEquals( wrt );
		return( cond ? fac.identity() : fac.zero() );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> cache ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		return( evalPartialDerivative( withRespectTo , implicitSpace ) );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		String s0 = "cn";
		for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
		{
			Ordinate key = ii.getKey();
			BigInteger val = ii.getValue();
			s0 = s0 + "[";
			s0 = s0 + key.getCol();
			s0 = s0 + ",";
			s0 = s0 + val.intValue();
			s0 = s0 + "]";
		}
		s0 = s0 + "()";
		ps.print( s0 );
	}
	

//	
//	protected boolean symbolicDEval( 
//			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
//	{
//		if( b instanceof CNelem )
//		{
//			CNelem bn = (CNelem) b;
//			if( coord.keySet().size() != bn.coord.keySet().size() )
//			{
//				return( false );
//			}
//          for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
//			{
//				Ordinate key = ii.getKey();
//				BigInteger ka = ii.getValue();
//				BigInteger kb = bn.coord.get( key );
//				if( ( ka == null ) || ( kb == null ) )
//				{
//					return( false );
//				}
//				if( !( ka.equals( kb ) ) )
//				{
//					return( false );
//				}
//			}
//			return( true );
//		}
//		return( false );
//	}
//	
	
	
	@Override
	public boolean symbolicEquals( 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof CNelem )
		{
			CNelem bn = (CNelem) b;
			if( !( index.equals( bn.index ) ) )
			{
				return( false );
			}
			if( coord.keySet().size() != bn.coord.keySet().size() )
			{
				return( false );
			}
			for( Entry<Ordinate,BigInteger> ii : coord.entrySet() )
			{
				Ordinate key = ii.getKey();
				BigInteger ka = ii.getValue();
				BigInteger kb = bn.coord.get( key );
				if( ( ka == null ) || ( kb == null ) )
				{
					return( false );
				}
				if( !( ka.equals( kb ) ) )
				{
					return( false );
				}
			}
			return( true );
		}
		return( false );
	}
	
}
	
	


/**
 * An elem defining a partial derivative that is evaluated over the discretized space of the test.
 * 
 * @author thorngreen
 *
 */	
private class AStelem extends Stelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{	

/**
 * The index of the multivector component represented by the elem.
 */
protected HashSet<BigInteger> index;
	
/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _index The index of the multivector component represented by the elem.
 */
public AStelem( SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac,
		HashSet<BigInteger> _index ) {
	super(_fac);
	index = _index;
}

@Override
public AStelem cloneInstance() {
	AStelem cl = new AStelem( fac , index );
	Iterator<Ordinate> it = partialMap.keySet().iterator();
	while( it.hasNext() )
	{
		Ordinate key = it.next();
		cl.partialMap.put(key, partialMap.get(key) );
	}
	return( cl );
}

@Override
public SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	
	HashMap<Ordinate,Ordinate> imp = (HashMap<Ordinate,Ordinate>) implicitSpace;
	
	
	HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
	
	
	{
		CoeffNode cf = new CoeffNode( fac.getFac().identity() , fac.getFac().identity() );
		HashMap<Ordinate, BigInteger> key = new HashMap<Ordinate, BigInteger>();
		Iterator<Ordinate> it = imp.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate ae = it.next();
			BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
			key.put( ae , valA );
		}
		spacesA.put( key , cf );
	}
	
	
	{
		Iterator<Ordinate> it = partialMap.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
			final Ordinate ae = it.next();
			final BigInteger numDerivs = partialMap.get( ae );
			applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
			spacesA = spacesB;
		}
	}
	
	
	
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
	
	
	{
		Iterator<HashMap<Ordinate, BigInteger>> it = spacesA.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<Ordinate, BigInteger> spaceAe = it.next();
			CoeffNode coeff = spacesA.get( spaceAe );
			final CNelem an0 = 
					new CNelem( fac.getFac() , spaceAe , index );
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
				an1 = an0.mult( 
						new StelemReduction2L( coeff.getNumer() , fac.getFac() ) );
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> 
				an2 = an1.mult( 
						( new StelemReduction2L( coeff.getDenom() , fac.getFac() ) ).invertLeft() );
			ret = ret.add( an2 );
		}
	}
	
	
	return( ret );
}


@Override
public SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> key =
			new SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>( this , implicitSpace );
	final SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}
	final SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> ret =
			eval( implicitSpace );
	cache.put(key, ret);
	return( ret );
}


@Override
public void writeString( PrintStream ps ) {
	throw( new RuntimeException( "NotSupported" ) );
}


        /**
         * Applies a discretized approximation of a derivative using the following rules where "N"
		 * is the number of derivatives and "h" is the size of the discretization:
		 * <P>
		 * <P> N = 0 -- Zero derivatives have been taken so simply return the original value of  the elem.
		 * <P>
		 * <P> N = 1 -- Use the first derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <mi>h</mi>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Numerical_differentiation)
		 * <P>
		 * <P> N = 2 -- Use the second derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>2</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://en.wikipedia.org/wiki/Second_derivative)
		 * <P>
		 * <P> N = 3 -- Use the third derivative formula <math display="inline">
         * <mrow>
         *   <mfrac>
         *     <mrow>
         *       <mo>-</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>-</mo>
         *       <mn>2</mn>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>-</mo>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *       <mo>+</mo>
         *       <mi>F</mi><mo>&ApplyFunction;</mo>
         *       <mfenced open="(" close=")" separators=",">
         *         <mrow>
         *           <mi>x</mi>
         *           <mo>+</mo>
         *           <mn>2</mn>
         *           <mi>h</mi>
         *         </mrow>
         *       </mfenced>
         *     </mrow>
         *     <mrow>
         *       <mn>2</mn>
         *       <msup>
         *               <mi>h</mi>
         *             <mn>3</mn>
         *       </msup>
         *     </mrow>
         *   </mfrac>
         * </mrow>
         * </math>
		 * <P>
		 * <P> (See http://www.geometrictools.com/Documentation/FiniteDifferences.pdf)
		 * <P>
		 * <P> N > 3 -- Combine results from smaller values of N to build an approximation of a higher-order derivative.
         * 
         * @param implicitSpacesIn The input implicit space containing the discretized approximation function.
         * @param node The ordinate over which to take the derivative.
         * @param numDerivatives The number of derivatives to apply.
         * @param hh The size of the discretization.
         * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
         * @throws MultiplicativeDistributionRequiredException 
         * @throws NotInvertibleException 
         */
protected void applyDerivativeAction( HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesIn , 
		Ordinate node , final int numDerivatives , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	if( numDerivatives > 3 )
	{
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
		applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
		applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
	}
	
	Iterator<HashMap<Ordinate, BigInteger>> it = implicitSpacesIn.keySet().iterator();
	while( it.hasNext() )
	{
		final HashMap<Ordinate, BigInteger> implicitSpace = it.next();
		final CoeffNode coeffNodeIn = implicitSpacesIn.get( implicitSpace );
		
		switch( numDerivatives )
		{
		
		case 0:
			{
				applyAdd( implicitSpace , coeffNodeIn , implicitSpacesOut );
			}
			break;
		
		case 1:
			{
				applyDerivativeAction1( 
						implicitSpace , coeffNodeIn, 
						node , hh , implicitSpacesOut );
			}
			break;
			
		case 2:
			{
				applyDerivativeAction2( 
						implicitSpace , coeffNodeIn, 
						node , hh , implicitSpacesOut );
			}
			break;
			
		case 3:
			{
				applyDerivativeAction3( 
					implicitSpace , coeffNodeIn, 
					node , hh , implicitSpacesOut );
			}
			break;
			
		default:
			{
				throw( new RuntimeException( "Internal Error" ) );
			}
			
		}
	}
	
}



/**
 * Applies a discretized approximation of a first derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction1( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn, 
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( genFromConstDbl( 2.0 ) ), hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
}




/**
 * Applies a discretized approximation of a second derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction2( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn, 
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh ) , hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpace , coeffNodeOut , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
}




/**
 * Applies a discretized approximation of a third derivative.
 * 
 * @param implicitSpace The input implicit space containing the discretized approximation function.
 * @param coeffNodeIn The discretized coefficient onto which to apply the derivative.
 * @param node The ordinate over which to take the derivative.
 * @param hh The size of the discretization.
 * @param implicitSpacesOut The output implicit space containing the discretized approximation function with the derivatives applied.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void applyDerivativeAction3( 
		final HashMap<Ordinate, BigInteger> implicitSpace , final CoeffNode coeffNodeIn, 
		Ordinate node , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
	final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
	
	Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
	while( itA.hasNext() )
	{
		Ordinate ae = itA.next();
		final BigInteger valAe = implicitSpace.get( ae );
		if( node.symbolicEquals( ae ) )
		{
			final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
			final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
			final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
			final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
			implicitSpaceOutM1.put( ae , valAeM1 );
			implicitSpaceOutP1.put( ae , valAeP1 );
			implicitSpaceOutM2.put( ae , valAeM2 );
			implicitSpaceOutP2.put( ae , valAeP2 );
		}
		else
		{
			implicitSpaceOutM1.put( ae , valAe );
			implicitSpaceOutP1.put( ae , valAe );
			implicitSpaceOutM2.put( ae , valAe );
			implicitSpaceOutP2.put( ae , valAe );
		}
	}
	
	final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new SymbolicConst( genFromConstDbl( 2.0 ) , hh.getFac() ) ) , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
			coeffNodeIn.getDenom().mult( new SymbolicConst( hh.mult( hh.mult( hh.mult( genFromConstDbl( 2.0 ) ) ) ), hh.getFac() ) ) );
	
	applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
	applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
	applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
}





/**
 * Adds a coefficient times the input implicit space to the output implicit space.
 * 
 * @param implicitSpace The input implicit space.
 * @param node The coefficient.
 * @param implicitSpacesOut The output implicit space.
 * @throws MultiplicativeDistributionRequiredException 
 * @throws NotInvertibleException 
 */
protected void applyAdd( 
		HashMap<Ordinate, BigInteger> implicitSpace , CoeffNode node ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	CoeffNode prev = implicitSpacesOut.get( implicitSpace );
	
	if( prev == null )
	{
		implicitSpacesOut.put( implicitSpace , node );
		return;
	}
	
	if( ( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() ) &&
		( prev.getDenom().eval( null ).getVal() == node.getDenom().eval( null ).getVal() ) ) 
	{
		SymbolicElem<DoubleElem,DoubleElemFactory> outN = node.getNumer().add( prev.getNumer() );
		CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
		implicitSpacesOut.put( implicitSpace , nxt );
		return;
	}
	
	
	SymbolicElem<DoubleElem,DoubleElemFactory> outDenom = prev.getDenom().mult( node.getDenom() );
	
	SymbolicElem<DoubleElem,DoubleElemFactory> outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
	
	CoeffNode nxt = new CoeffNode( outNumer , outDenom );
	
	implicitSpacesOut.put( implicitSpace , nxt );
}



}
	
	
	
	/**
	 * descent algorithm evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class StelemDescent extends DescentAlgorithmMultiElemRemap<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
	{

		
		
		/**
		 * Constructs the evaluator.
		 * 
		 * @param param The input parameter for the remap.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemDescent(
				final DescentAlgorithmMultiElemRemapParam<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> param )
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super( param , null );
			
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}

		/**
		 * The iteration count for descent algorithm iterations.
		 */
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			
			// System.out.println( this.partialEval.writeString() ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> iterationOffset )
		{
			TestDiracA.performIterationUpdate( iterationOffset );
		}
		
		@Override
		protected void setIterationValue( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> iterationOffset )
		{
			TestDiracA.setIterationValue( iterationOffset );
		}
		
		@Override protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> getIterationValue()
		{
			return( TestDiracA.getUpdateValue() );
		}
		
		/**
		 * Returns whether convergence-wise the new function value should be accepted as an improvement over the old function value.
		 * 
		 * @param lastValue The old function value.
		 * @param nextValue The new function value.
		 * @return True iff. the new function value should be accepted as an improvement over the old function value.
		 */
		protected boolean evalIterationImproved( GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> lastValue ,
				GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> nextValue )
		{
			return( true );
		}
		
		@Override
		public StelemDescent cloneThread( final BigInteger threadIndex )
		{
			throw( new RuntimeException( "Not Supported" ) );
		}
	
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		@Override
//		protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
//			evalPartialDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
//		{
//			GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
//				ev = super.evalPartialDerivative();
//			for( int acnt = 0 ; acnt < 16 ; acnt++ )
//			{
//				final DoubleElem dd = new DoubleElem( 1E-6 * ( 2.0 * rand.nextDouble() ) );
//				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
//				if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
//				if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
//				if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
//				if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
//				DoubleElem dda = ev.getVal( hs );
//				ev.setVal( hs , dd.add( dda ) );
//			}
//			return( ev );
//		}
//		
		
		/**
		 * The internal multivariate descent algorithm.
		 * 
		 * @author thorngreen
		 *
		 */
		protected class StelemDescentEnt extends DescentAlgorithmMultiElemInputParam<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, DoubleElem, DoubleElemFactory>
		{

			@Override
			protected void performIterationUpdate(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> iterationOffset) {
				StelemDescent.this.performIterationUpdateInternal( iterationOffset );
			}
			
			@Override
			protected void setIterationValue(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> iterationOffset) {
				StelemDescent.this.setIterationValueInternal( iterationOffset );
			}
			
			@Override protected GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> getIterationValue()
			{
				return( StelemDescent.this.getIterationValueInternal( ) );
			}

			@Override
			protected boolean iterationsDone() {
				return( StelemDescent.this.iterationsDone() );
			}
			
			@Override
			protected SimplificationType useSimplification() {
				return( StelemDescent.this.useSimplification() );
			}
			
			@Override
			protected boolean useCachedEval() {
				return( StelemDescent.this.useCachedEval() );
			}
			
			@Override
			protected int getMaxIterationsBacktrack() {
				return( StelemDescent.this.getMaxIterationsBacktrack() );
			}
			
			@Override
			protected boolean evalIterationImproved( GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> lastValue ,
					GeometricAlgebraMultivectorElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> nextValue )
			{
				return( StelemDescent.this.evalIterationImproved(lastValue, nextValue) );
			}
			
			
			public StelemDescentEnt()
			{
				super();
			}
			
			
			protected StelemDescentEnt( StelemDescentEnt in , final BigInteger threadIndex )
			{
				super( in , threadIndex );
			}
			
			
			@Override
			public StelemDescentEnt cloneThread( final BigInteger threadIndex )
			{
				return( new StelemDescentEnt( this , threadIndex ) );
			}
			
			
		};
		

		@Override
		protected DescentAlgorithmMultiElem<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, DoubleElem, DoubleElemFactory> genDescent(
				final GenDescentParam param ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			
			final StelemDescentEnt sa = new StelemDescentEnt();
			sa.setFunctions( param.getFunctions() );
			sa.setWithRespectTos( param.getWithRespectTos() );
			sa.setImplicitSpaceFirstLevel( param.getImplicitSpaceFirstLevel() );
			sa.setSfac( param.getSfac() );
			sa.setDim( param.getDim() );
			
			return( new NewtonRaphsonMultiElemNoBacktrack<simplealgebra.algo.DescentAlgorithmMultiElemRemap.Adim, DoubleElem, DoubleElemFactory>( sa , param.getCache() ) );
		}
		
	}
	
	
	
	

/**
 * An elem for performing evaluations
 * at the base descent algorithm level.
 * 
 * @author thorngreen
 *
 */
protected class VEvalElem extends SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>	
{
	
	/**
	 * The multivector to be evalueted by the elem.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
		dval = null;
	

	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dval The multivector to eb evaluated by the elem.
	 */
	public VEvalElem(
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> _fac,
			GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _dval ) {
		super(_fac);
		dval = _dval;
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> ret = 
				new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			ret.setVal( key , dval.get( key ).eval( implicitSpace ) );
		}
		
		return( ret );
	}
	
	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>
			key = new SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>( this , implicitSpace );
		final GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>
			iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> ret = 
				new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> keyA = it.next();
			ret.setVal( keyA , dval.get( keyA ).eval( implicitSpace ) );
		}
		
		cache.put(key, ret);
		return( ret );
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> ret = 
				new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			ret.setVal( key , dval.get( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
		}
		
		return( ret );
	}
	
	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( evalPartialDerivative( withRespectTo , implicitSpace ) );
	}

	@Override
	public void writeString( PrintStream ps ) {
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			ps.print( "\n" + "** " );
			( dval.get( key ) ).writeString( ps );
		}
	}

	
}




/**
 * An elem for evaluating 
 * discretized approximations of the underlying differential
 * equation into expressions (e.g. Jacobian slopes)
 * for descent algorithm evaluations.
 * 
 * @author thorngreen
 *
 */
protected class VEvalElem2 extends SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>	
{

/**
 * The multivector to be evaluated by the elem.
 */
protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>  
	dval = null;

/**
 * Factory for components at the base descent algorithm level.
 */
protected SymbolicElemFactory<DoubleElem,DoubleElemFactory> sefac;

/**
 * Factory for evaluated multivectors at the base descent algorithm level.
 */
protected GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> vefac;



/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _sefac Factory for components at the base descent algorithm level.
 * @param _vefac Factory for evaluated multivectors at the base descent algorithm level.
 * @param _dval The multivector to be evaluated by the elem.
 */
public VEvalElem2(
		SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> _fac,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory> _sefac ,
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> _vefac ,
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> _dval ) {
	super(_fac);
	sefac = _sefac;
	vefac = _vefac;
	dval = _dval;
}

@Override
public SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> key = it.next();
		retA.setVal( key , dval.get( key ).eval( implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>, SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>
		key = new SCacheKey<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>( this , implicitSpace );
	final SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>
		iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}
	
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	
	final HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>>();

	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> keyA = it.next();
		retA.setVal( keyA , dval.get( keyA ).evalCached( implicitSpace , cache2 ) );
	}
	
	final SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>
		ret = new VEvalElem( vefac , retA );
	cache.put(key, ret);
	return( ret );
}

@Override
public SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> evalPartialDerivative(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> key = it.next();
		retA.setVal( key , dval.get( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>, SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	
	final HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElem<DoubleElem, DoubleElemFactory>>();
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> key = it.next();
		retA.setVal( key , dval.get( key ).evalPartialDerivativeCached( withRespectTo , implicitSpace , cache2 ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public void writeString( PrintStream ps ) {
	throw( new RuntimeException( "Not Supported" ) );
}


}




/**
 * An elem for evaluating the underlying differential equation into
 * its discretized approximation.
 * 
 * @author thorngreen
 *
 */
protected class VEvalElem3 extends SymbolicElem<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>>	
{

/**
 * The multivector to be evaluated by the elem.
 */
protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>   
dval = null;

/**
 * Factory for components at the level of discretized approximations.
 */
protected SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> sefac;

/**
 * Factory for evaluated multivectors at the base descent algorithm level.
 */
protected GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> vefac;



/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _sefac Factory for components at the level of discretized approximations.
 * @param _vefac Factory for evaluated multivectors at the base descent algorithm level.
 * @param _dval The multivector to be evaluated by the elem.
 */
public VEvalElem3(
		SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> _fac,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _sefac ,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> _vefac ,
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>> _dval ) {
super(_fac);
sefac = _sefac;
vefac = _vefac;
dval = _dval;
}

@Override
public SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> eval(
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
		new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );

Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
while( it.hasNext() )
{
	final HashSet<BigInteger> key = it.next();
	retA.setVal( key , dval.get( key ).eval( implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>>
		key = new SCacheKey<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>>( this , implicitSpace );
	final SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>
		iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}
	
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );

	final HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>();
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> keyA = it.next();
		retA.setVal( keyA , dval.get( keyA ).evalCached( implicitSpace , cache2 ) );
	}

	final SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>
		ret = new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA );
	cache.put(key, ret);
	return( ret );
}

@Override
public SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> evalPartialDerivative(
	ArrayList<? extends Elem<?, ?>> withRespectTo,
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );

Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
while( it.hasNext() )
{
	final HashSet<BigInteger> key = it.next();
	retA.setVal( key , dval.get( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );

	final HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
		cache2 = new HashMap<SCacheKey<SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>>();
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> key = it.next();
		retA.setVal( key , dval.get( key ).evalPartialDerivativeCached( withRespectTo , implicitSpace , cache2 ) );
	}

	return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public void writeString( PrintStream ps ) {
throw( new RuntimeException( "Not Supported" ) );
}


}




/**
 * Initializes the iter array.
 * 
 * @param d1 The dimensional size to be used for the initialization.
 */
protected void initIterArray( final double d1 )
{
	System.out.println( "Setting Initial Conditions..." );
	long atm = System.currentTimeMillis();
	long atm2 = System.currentTimeMillis();
	for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
	{
		System.out.println( "Initial - " + tcnt );
		for( long acnt = 0 ; acnt < ( (long) NUM_X_ITER ) * NUM_Y_ITER * NUM_Z_ITER ; acnt++ )
		{
			atm2 = System.currentTimeMillis();
			if( atm2 - atm >= 1000 )
			{
				System.out.println( ">> " + acnt );
				atm = atm2;
			}
			
			long ac = acnt;
			final int z = (int)( ac % NUM_Z_ITER );
			ac = ac / NUM_Z_ITER;
			final int y = (int)( ac % NUM_Y_ITER );
			ac = ac / NUM_Y_ITER;
			final int x = (int)( ac % NUM_X_ITER );
			final double dx = ( x - HALF_X ) / RAD_X;
			final double dy = ( y - HALF_Y ) / RAD_Y;
			final double dz = ( z - HALF_Z ) / RAD_Z;
			if( dx * dx + dy * dy + dz * dz < 1.0 )
			{
				iterArray[ tcnt ][ x ][ y ][ z ] = genDiffEnt( 10000.0 * ( d1 * d1 ) );
			}
			else
			{
				iterArray[ tcnt ][ x ][ y ][ z ] = genDiffAll( 1E-8 );
			}
		}
	}
	System.out.println( "Initial Conditions Set..." );
}





/**
 * Initializes the temp array.
 */
protected void initTempArray()
{
	for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
	{
		for( int xcnt = 0 ; xcnt < 2 ; xcnt++ )
		{
			for( int ycnt = 0 ; ycnt < 2 ; ycnt++ )
			{
				for( int zcnt = 0 ; zcnt < 2 ; zcnt++ )
				{
					tempArray[ tcnt ][ xcnt ][ ycnt ][ zcnt ] = genDiffAll( 1E-8 );
				}
			}
		}
	}
}



/**
 * Increments through the discretized space with cache-locality.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
protected class IncrementManager
{
	/**
	 * The current discretized X-coordinate.
	 */
	protected int xcnt = 0;
	
	/**
	 * The current discretized Y-coordinate.
	 */
	protected int ycnt = 0;
	
	/**
	 * The current discretized Z-coordinate.
	 */
	protected int zcnt = 0;
	
	
	/**
	 * Handles the base increment from the X-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementXa()
	{
		ycnt = 0;
		xcnt++;
	}
	
	
	/**
	 * Handles the base increment from the Y-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementYa()
	{
		zcnt = 0;
		if( ycnt < ( NUM_Y_ITER - 1 ) )
		{
			ycnt++;
		}
		else
		{
			handleIncrementXa();
		}
	}
	
	
	/**
	 * Handles the base increment from the Z-Axis.  At the point the increment
	 * reaches a swatch boundary, other axes are potentially incremented.
	 */
	public void handleIncrementZa()
	{
		if( zcnt < ( NUM_Z_ITER - 1 ) )
		{
			zcnt++;
		}
		else
		{
			handleIncrementYa();
		}
	}
	
	
	/**
	 * Restarts the inctrements upon a new T-Axis iteration.
	 */
	public void restartIncrements()
	{
		xcnt = 0;
		ycnt = 0;
		zcnt = 0;
	}
	
	
	
	/**
	 * Gets the current discretized X-coordinate.
	 * 
	 * @return The current discretized X-coordinate.
	 */
	public int getXcnt() {
		return xcnt;
	}



	/**
	 * Gets the current discretized Y-coordinate.
	 * 
	 * @return The current discretized Y-coordinate.
	 */
	public int getYcnt() {
		return ycnt;
	}



	/**
	 * Gets the current discretized Z-coordinate.
	 * 
	 * @return The current discretized Z-coordinate.
	 */
	public int getZcnt() {
		return zcnt;
	}
	

	
}



/**
 * Instance of the IncrementManager used by the performIterationT() method.
 */
protected final IncrementManager im = new IncrementManager();



/**
 * Performs descent iterations for one value of T.
 * 
 * @param tval The value of T over which to iterate.
 * @param descent The descent algorithm to use for the iterations.
 * @param implicitSpace2 The implicit space over which to iterate.
 * @throws NotInvertibleException
 * @throws MultiplicativeDistributionRequiredException
 */
protected void performIterationT( final int tval , final StelemDescent descent , final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 ) 
		throws NotInvertibleException, MultiplicativeDistributionRequiredException
{
	//GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> tmpCorrectionValue = null;
	im.restartIncrements();
	for( long acnt = 0 ; acnt < ( ( (long) NUM_X_ITER ) * NUM_Y_ITER * NUM_Z_ITER ) ; acnt++ )
	{
		// System.out.println( "iter... " + xcnt + " " + ycnt + " " + zcnt );
				
		fillTempArray( tval , im.getXcnt() , im.getYcnt() , im.getZcnt() );
		clearSpatialAssertArray();

		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> 
			ival = TestDiracA.getUpdateValue();
		
		
	
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> 
			err = descent.eval( implicitSpace2 );
				
				
		//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
		//{
		//	tmpCorrectionValue = getCorrectionValue();
		//	applyPredictorCorrector();
		//			
		//	err = descent.eval( implicitSpace2 );
		//}


		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> 
			val = TestDiracA.getUpdateValue();
				
				
		
		if( ( im.getXcnt() == HALF_X ) && ( im.getYcnt() == HALF_Y ) && ( im.getZcnt() == HALF_Z ) )
		{
			System.out.println( "******************" );
			System.out.println( " ( " + im.getXcnt() + " , " + im.getYcnt() + " , " + im.getZcnt() + " ) " );
			System.out.println( Math.sqrt( calcMagnitudeSq( ival ) ) );
			System.out.println( Math.sqrt( calcMagnitudeSq( val ) ) );
			System.out.println( "## " + ( Math.sqrt( calcMagnitudeSq( err ) ) ) );
		}
				
				
		Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ][ 0 ][ 0 ] == 0 );
				
		Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 1 ] > 0 );
				
		Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ][ 1 ][ 1 ] > 0 );
		Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ][ 1 ][ 1 ] > 0 );
		Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 2 ][ 1 ] > 0 );
		Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 2 ] > 0 );
				
		Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ][ 1 ][ 1 ] > 0 );
		Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ][ 1 ][ 1 ] > 0 );
		Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 0 ][ 1 ] > 0 );
		Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 0 ] > 0 );
				
		// for( int xc = 0 ; xc < 2 * NSTPX - 1 ; xc++ )
		// {
		//	for( int yc = 0 ; yc < 2 * NSTPY - 1 ; yc++ )
		//	{
		//		for( int zc = 0 ; zc < 2 * NSTPZ - 1 ; zc++ )
		//		{
		//			if( ( xc == NSTPX ) && ( yc == NSTPY ) && ( zc == NSTPZ ) )
		//			{
		//				Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ][ yc ][ zc ] > 0 );
		//			}
		//			else
		//			{
		//				Assert.assertTrue( spatialAssertArray[ NSTPT * 2 ][ xc ][ yc ][ zc ] == 0 );
		//			}
		//		}
		//	}
		// }
		
		
		// System.out.println( calcMagnitudeSq( val ) );
		// System.out.println( calcMagnitudeSq( err ) );
		Assert.assertTrue( Math.abs( Math.sqrt( calcMagnitudeSq( err ) ) ) < ( 0.01 * Math.abs( Math.sqrt( calcMagnitudeSq( val ) ) ) + 0.01 ) );
		
		//if( USE_PREDICTOR_CORRECTOR && ( tval > 1 ) )
		//{
		//	resetCorrectionValue( tmpCorrectionValue );	
		//}
	
		iterArray[ tval + 1 ][ im.getXcnt() ][ im.getYcnt() ][ im.getZcnt() ] = val;
			
		
		im.handleIncrementZa();
				
	}
}
	


	
	
	
/**
 * 
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <mo>&nabla;</mo>
 *  <mi>&Psi;</mi>
 *  <mi>i</mi>
 *  <msub>
 *          <mi>&sigma;</mi>
 *        <mn>3</mn>
 *   </msub>
 *   <mo>-</mo>
 *   <mfrac>
 *     <mrow>
 *       <mi>m</mi>
 *       <mi>c</mi>
 *     </mrow>
 *     <mrow>
 *       <mi>&hbar;</mi>
 *     </mrow>
 *   </mfrac>
 *   <mi>&Psi;</mi>
 *   <msub>
 *           <mi>&gamma;</mi>
 *         <mn>0</mn>
 *   </msub>
 *   <mo>=</mo>
 *   <mn>0</mn>
 * </mrow>
 * </math>
 * 
 * 
 * where <math display="inline">
 * <mrow>
 *  <mi>m</mi>
 * </mrow>
 * </math> and <math display="inline">
 * <mrow>
 *  <mi>&hbar;</mi>
 * </mrow>
 * </math> and <math display="inline">
 * <mrow>
 *  <mi>c</mi>
 * </mrow>
 * </math> are arbitrary constants.
 * 
 * 
 * 
 * See http://en.wikipedia.org/wiki/Spacetime_algebra
 * 
 * See http://quantummechanics.ucsd.edu/ph130a/130_notes/node45.html
 * 
 *
 */
public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
	    final double cmx = Math.min( X_HH.getVal() , Math.min( Y_HH.getVal() , Z_HH.getVal() ) ) / ( T_HH.getVal() );
	    final double cmxRatio = cmx / C.getVal();
	    if( cmxRatio < 1.0 )
	    {
		    System.out.println( "WARNING: cmxRatio " + cmxRatio );
	    }
	    else
	    {
		    System.out.println( "cmxRatio " + cmxRatio );
	    }
	
	
		final Random rand = new Random( 3344 );
		
		final double d1 = Math.sqrt( calcMagnitudeSq( X_HH ) + calcMagnitudeSq( Y_HH ) + calcMagnitudeSq( Z_HH ) );
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		
		
		
		initIterArray( d1 );
		
		
		initTempArray();
		
	
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> de2 = new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( de , tdim , ord );
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		
		
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
			new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A , tdim , ord );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				g0 = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( se3A , tdim , ord );
		
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
			if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
			if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
			if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
			if( /* ( ( hs.size() ) % 2 ) == 0 */ true )
			{
				final AStelem as = new AStelem( se2A  , hs );
				g0.setVal( hs , as );
			}
		}
		
		
		
		final DDirec ddirec = new DDirec(de2, se2A);
		
		final DirectionalDerivative<
			TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			Ordinate>
			del =
			new DirectionalDerivative<
			TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			Ordinate>( 
					ge , 
					tdim , ord ,
					ddirec );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				del0 = del.eval( null );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gxx0 = del0.mult( g0 );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> energ0 =
					( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( MM.mult( C ).mult( HBAR.invertLeft() ) , de ) , seA ) , se2A )
							).negate();
		
		
		// SymbolicElem<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,
		//	SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>> m1
		//	= m1X.add( gtt0 );
		
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
		SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				generg1 = new GeometricAlgebraMultivectorElem<
						TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
						SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
						SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( se3A , tdim , ord );
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			generg1.setVal( hs , energ0 );
			
		}
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			generg = g0.mult( generg1 );
		
		
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
		SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			gxxMultV = IM3;
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1 = ( gxx0.mult( gxxMultV ) ).add( generg );
		
		
		VEvalElem3 m1 = new VEvalElem3( se2 , se2A , de2 , mg1 );
		
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de2 , TV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , YV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , ZV ) , new Ordinate( de2 , 0 ) );
		
		final SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> 
			s0 = m1.eval( implicitSpace2 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			s00 = ( (VEvalElem2)( s0 ) ).dval;
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>> wrt3 = new HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>>();
		{
			for( int acnt = 0 ; acnt < 16 ; acnt++ )
			{
				final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
				coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( 1 ) );
				coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , YV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , ZV ) , BigInteger.valueOf( 0 ) );
				
				final HashSet<BigInteger> index = new HashSet<BigInteger>();
				if( ( acnt & 1 ) != 0 ) index.add( BigInteger.ZERO );
				if( ( acnt & 2 ) != 0 ) index.add( BigInteger.ONE );
				if( ( acnt & 4 ) != 0 ) index.add( BigInteger.valueOf( 2 ) );
				if( ( acnt & 8 ) != 0 ) index.add( BigInteger.valueOf( 3 ) );
				
				ArrayList<CNelem> ce = new ArrayList<CNelem>();
				ce.add( new CNelem( seA , coord , index ) );
				
				wrt3.put( index , ce );
			}
		}
		
		
		final DescentAlgorithmMultiElemRemapParam<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> param
			= new DescentAlgorithmMultiElemRemapParam<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>();
		
		
		param.setFunctions( s00 );
		param.setWithRespectTosI( wrt3 );
		param.setImplicitSpaceFirstLevel( implicitSpace2 );
		param.setSfac( se2A );
		param.setDim( tdim );
		param.setOrd( new SpacetimeAlgebraOrd<TestDimensionFour>() );
		

		
		
		StelemDescent descent = new StelemDescent( param );
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - 1 ) ; tval++ )
		{
			performIterationT( tval , descent , implicitSpace2 );
		}
		
		// System.out.println( "==============================" ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.out.println( iterArray[ NUM_T_ITER - 1 ][ HALF_X ][ HALF_Y ][ HALF_Z ] ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	
	

	
}


