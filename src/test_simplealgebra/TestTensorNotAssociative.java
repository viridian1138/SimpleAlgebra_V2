



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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;



/**
 * Tests counterexample showing tensor index products are not associative by demonstrating that <math display="inline">
 * <mrow>
 *   <mfenced open="(" close=")" separators=",">
 *     <mrow>
 *       <msub>
 *               <mi>A</mi>
 *             <mi>u</mi>
 *       </msub>
 *       <msup>
 *               <mi>B</mi>
 *             <mi>u</mi>
 *       </msup>
 *     </mrow>
 *   </mfenced>
 *   <msub>
 *           <mi>C</mi>
 *         <mi>u</mi>
 *   </msub>
 *   <mo>&ne;</mo>
 *   <msub>
 *           <mi>A</mi>
 *         <mi>u</mi>
 *   </msub>
 *   <mfenced open="(" close=")" separators=",">
 *     <mrow>
 *       <msup>
 *               <mi>B</mi>
 *             <mi>u</mi>
 *       </msup>
 *       <msub>
 *               <mi>C</mi>
 *             <mi>u</mi>
 *       </msub>
 *     </mrow>
 *   </mfenced>
 * </mrow>
 * </math>
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class TestTensorNotAssociative extends TestCase {
	
	
	/**
	 * Returns an empty tensor index list.
	 * 
	 * @return An empty tensor index list.
	 */
	final static ArrayList<String> indicesEmpty()
	{
		return( new ArrayList<String>() );
	}
	
	
	/**
	 * Returns a tensor index list with "u".
	 * 
	 * @return The tensor index list.
	 */
	final static ArrayList<String> indicesU()
	{
		final ArrayList<String> ret = new ArrayList<String>();
		ret.add( "u" );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for component zero of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect0( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.ZERO );
		return( ret );
	}
	
	
	
	/**
	 * Returns an index for component 1 of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect1( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.ONE );
		return( ret );
	}
	
	
	/**
	 * Returns an index for component 2 of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect2( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.valueOf( 2 ) );
		return( ret );
	}
	
	
	/**
	 * Returns an index for component 3 of a rank one tensor.
	 * 
	 * @return The tensor index.
	 */
	final static ArrayList<BigInteger> vect3( )
	{
		final ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		ret.add( BigInteger.valueOf( 3 ) );
		return( ret );
	}
	
	
	
	
	
	/**
	 * Tests counterexample showing tensor index products are not associative by demonstrating that <math display="inline">
     * <mrow>
     *   <mfenced open="(" close=")" separators=",">
     *     <mrow>
     *       <msub>
     *               <mi>A</mi>
     *             <mi>u</mi>
     *       </msub>
     *       <msup>
     *               <mi>B</mi>
     *             <mi>u</mi>
     *       </msup>
     *     </mrow>
     *   </mfenced>
     *   <msub>
     *           <mi>C</mi>
     *         <mi>u</mi>
     *   </msub>
     *   <mo>&ne;</mo>
     *   <msub>
     *           <mi>A</mi>
     *         <mi>u</mi>
     *   </msub>
     *   <mfenced open="(" close=")" separators=",">
     *     <mrow>
     *       <msup>
     *               <mi>B</mi>
     *             <mi>u</mi>
     *       </msup>
     *       <msub>
     *               <mi>C</mi>
     *             <mi>u</mi>
     *       </msub>
     *     </mrow>
     *   </mfenced>
     * </mrow>
     * </math>
	 */
	public void testAssociativeMultA() throws NotInvertibleException
	{
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfA =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfB =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etB =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesU() , indicesEmpty() );
		
		
		
		// final EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory> etfC =
		//		new EinsteinTensorElemFactory<String,DoubleElem,DoubleElemFactory>( de );
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etC =
				new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( de , indicesEmpty() , indicesU() );
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		etC.validate();
		
		
		
		
		final Random rand = new Random( 87654 );
		
		
		
		etA.setVal( vect0() , new DoubleElem( rand.nextDouble() ) );
		
		etB.setVal( vect0() , new DoubleElem( rand.nextDouble() ) );
		
		etC.setVal( vect0() , new DoubleElem( rand.nextDouble() ) );
		
		
		
		etA.setVal( vect1() , new DoubleElem( rand.nextDouble() ) );
		
		etB.setVal( vect1() , new DoubleElem( rand.nextDouble() ) );
		
		etC.setVal( vect1() , new DoubleElem( rand.nextDouble() ) );
		
		
		
		etA.setVal( vect2() , new DoubleElem( rand.nextDouble() ) );
		
		etB.setVal( vect2() , new DoubleElem( rand.nextDouble() ) );
		
		etC.setVal( vect2() , new DoubleElem( rand.nextDouble() ) );
		
		
		
		etA.setVal( vect3() , new DoubleElem( rand.nextDouble() ) );
		
		etB.setVal( vect3() , new DoubleElem( rand.nextDouble() ) );
		
		etC.setVal( vect3() , new DoubleElem( rand.nextDouble() ) );
		
		
		
		
		
		
		etA.validate();
		
		
		etB.validate();
		
		
		etC.validate();
		
		
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etAB =
				etA.mult( etB );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etBC =
				etB.mult( etC );
		
		
		
		
		etAB.validate();
		
		etBC.validate();
		
		
		
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etAB_C =
				etAB.mult( etC );
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> etA_BC =
				etA.mult( etBC );
		
		
		
		
		
		final double d0 = etAB_C.getVal( vect1() ).getVal();
		
		final double d1 = etA_BC.getVal( vect1() ).getVal();
		
		
		
		// System.out.println( d0 );
		
		// System.out.println( d1 );
		
		// The fact that d0 and d1 are different demonstrates that the product is not associative.
		Assert.assertTrue( Math.abs( d0 - d1 ) > 1E-5 );
		
		
	}
	
	
	
	
	
}


