



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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;

/**
 * 
 * Test for Dirac bra-ket notation as defined on the following page:
 * 
 * http://en.wikipedia.org/wiki/Bra-ket_notation
 * 
 * 
 * and as used in the book "Quantum Mechanics: The Theoretical Minimum" by Leonard Susskind.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDiracBraKetNotation extends TestCase {
	
	
	
	/**
	 * Mutator for performing a left-conjugate.
	 * 
	 * @author thorngreen
	 *
	 * @param <R> The enclosed type.
	 * @param <S> The factory for the enclosed type.
	 */
	protected static class ConjugateLeftMutator<R extends Elem<R,?>, S extends ElemFactory<R,S>> 
		implements Mutator<ComplexElem<R,S>>
	{

		@Override
		public ComplexElem<R, S> mutate(ComplexElem<R, S> in) throws NotInvertibleException {
			final ArrayList<ComplexElem<R, S>> args = new ArrayList<ComplexElem<R, S>>();
			return( in.handleOptionalOp(ComplexElem.ComplexCmd.CONJUGATE_LEFT, args) );
		}
		
		@Override
		public boolean exposesDerivatives()
		{
			return( false );
		}

		@Override
		public String writeString() {
			return( "conjugateLeft[]" );
		}
		
	}
	
	
	
	/**
	 * Mutator for performing a right-conjugate.
	 * 
	 * @author thorngreen
	 *
	 * @param <R> The enclosed type.
	 * @param <S> The factory for the enclosed type.
	 */
	protected static class ConjugateRightMutator<R extends Elem<R,?>, S extends ElemFactory<R,S>> 
		implements Mutator<ComplexElem<R,S>>
{

	@Override
	public ComplexElem<R, S> mutate(ComplexElem<R, S> in) throws NotInvertibleException {
		final ArrayList<ComplexElem<R, S>> args = new ArrayList<ComplexElem<R, S>>();
		return( in.handleOptionalOp(ComplexElem.ComplexCmd.CONJUGATE_RIGHT, args) );
	}
	
	@Override
	public boolean exposesDerivatives()
	{
		return( false );
	}

	@Override
	public String writeString() {
		return( "conjugateRight[]" );
	}
	
}
	
	
	/**
	 * Converts a 4-D Ket vector to a Bra vector.
	 * @param in Input Ket vector.
	 * @return Output Bra vector.
	 * @throws NotInvertibleException
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		convertKetToBra4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws NotInvertibleException
	{
		return( in.mutate( new ConjugateLeftMutator<DoubleElem,DoubleElemFactory>() ) );
	}
	
	
	/**
	 * Converts a 4-D Bra vector to a Ket vector.
	 * @param in Input Bra vector.
	 * @return Output Ket vector.
	 * @throws NotInvertibleException
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		converBraToKet4D( GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws NotInvertibleException
	{
		return( in.mutate( new ConjugateLeftMutator<DoubleElem,DoubleElemFactory>() ) );
	}
	
	
	
	
	/**
	 * Converts a 3-D Ket vector to a Bra vector.
	 * @param in Input Ket vector.
	 * @return Output Bra vector.
	 * @throws NotInvertibleException
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		convertKetToBra3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws NotInvertibleException
	{
		return( in.mutate( new ConjugateLeftMutator<DoubleElem,DoubleElemFactory>() ) );
	}


	/**
	 * Converts a 3-D Bra vector to a Ket vector.
	 * @param in Input Bra vector.
	 * @return Output Ket vector.
	 * @throws NotInvertibleException
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
		converBraToKet3D( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws NotInvertibleException
	{
		return( in.mutate( new ConjugateLeftMutator<DoubleElem,DoubleElemFactory>() ) );
	}
	
	
	
	
	
	/**
	 * Test bra-ket for 4-D spacetime.
	 */
	 public void testBraKetSpacetime( ) throws NotInvertibleException {
		
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cl = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dl );
		

		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> vl =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cl , td , ord );
		
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> a0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -3.0 ) , new DoubleElem( -4.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> a1 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 2.0 ) , new DoubleElem( 1.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> a2 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -3.0 ) , new DoubleElem( -2.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> a3 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -5.0 ) , new DoubleElem( -3.0 ) );
		
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> b0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -4.0 ) , new DoubleElem( -9.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> b1 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 4.0 ) , new DoubleElem( 5.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> b2 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -6.0 ) , new DoubleElem( -5.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> b3 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -1.0 ) , new DoubleElem( -1.0 ) );
		
		
		
		final HashSet<BigInteger> e0 = new HashSet<BigInteger>();
		e0.add( BigInteger.ZERO );
		
		
		final HashSet<BigInteger> e1 = new HashSet<BigInteger>();
		e1.add( BigInteger.ONE );
		
		
		final HashSet<BigInteger> e2 = new HashSet<BigInteger>();
		e2.add( BigInteger.valueOf( 2 ) );
		
		
		final HashSet<BigInteger> e3 = new HashSet<BigInteger>();
		e3.add( BigInteger.valueOf( 3 ) );
		
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ketVectorA = vl.zero();
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ketVectorB = vl.zero();
		
		
		
		ketVectorA.setVal(e0, a0 );
		ketVectorA.setVal(e1, a1 );
		ketVectorA.setVal(e2, a2 );
		ketVectorA.setVal(e3, a3 );
		
		
		ketVectorB.setVal(e0, b0 );
		ketVectorB.setVal(e1, b1 );
		ketVectorB.setVal(e2, b2 );
		ketVectorB.setVal(e3, b3 );
		
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			braVectorA = convertKetToBra4D( ketVectorA );
		

		
		// Perform the inner product of the Bra vector and the Ket vector.
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>();
		args.add( ketVectorB );
		
		GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> resultMvec = braVectorA.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT, args);
		
		
		ComplexElem<DoubleElem,DoubleElemFactory> result = resultMvec.getVal( new HashSet<BigInteger>() );
		
	
		// System.out.println( result.getRe().getVal() );
		
		// System.out.println( result.getIm().getVal() );
		
		
				
		Assert.assertEquals( 97.0 , 
				result.getRe().getVal() , 1E-10 );
				
		Assert.assertEquals( 22.0 , 
				result.getIm().getVal() , 1E-10 );
		
		
	}
	 
	 
	 
	 
	 
	 	/**
		 * Test bra-ket for 3-D space.
		 */
		 public void testBraKetSpace( ) throws NotInvertibleException {
			
			
			final TestDimensionThree td = new TestDimensionThree();
			
			final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
			
			final DoubleElemFactory dl = new DoubleElemFactory();
			
			final ComplexElemFactory<DoubleElem,DoubleElemFactory> cl = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dl );
			

			
			
			final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> vl =
					new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cl , td , ord );
			
			
			
			final ComplexElem<DoubleElem,DoubleElemFactory> a0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -3.0 ) , new DoubleElem( -4.0 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> a1 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 2.0 ) , new DoubleElem( 1.0 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> a2 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -3.0 ) , new DoubleElem( -2.0 ) );
						
			
			
			final ComplexElem<DoubleElem,DoubleElemFactory> b0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -4.0 ) , new DoubleElem( -9.0 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> b1 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 4.0 ) , new DoubleElem( 5.0 ) );
			
			final ComplexElem<DoubleElem,DoubleElemFactory> b2 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -6.0 ) , new DoubleElem( -5.0 ) );
					
			
			
			final HashSet<BigInteger> e0 = new HashSet<BigInteger>();
			e0.add( BigInteger.ZERO );
			
			
			final HashSet<BigInteger> e1 = new HashSet<BigInteger>();
			e1.add( BigInteger.ONE );
			
			
			final HashSet<BigInteger> e2 = new HashSet<BigInteger>();
			e2.add( BigInteger.valueOf( 2 ) );
			
		
			
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
				ketVectorA = vl.zero();
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
				ketVectorB = vl.zero();
			
			
			
			ketVectorA.setVal(e0, a0 );
			ketVectorA.setVal(e1, a1 );
			ketVectorA.setVal(e2, a2 );
			
			
			ketVectorB.setVal(e0, b0 );
			ketVectorB.setVal(e1, b1 );
			ketVectorB.setVal(e2, b2 );
			
			
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
				braVectorA = convertKetToBra3D( ketVectorA );
			

			
			// Perform the inner product of the Bra vector and the Ket vector.
			
			final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>();
			args.add( ketVectorB );
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>, ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> resultMvec = braVectorA.handleOptionalOp(GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT, args);
			
			
			ComplexElem<DoubleElem,DoubleElemFactory> result = resultMvec.getVal( new HashSet<BigInteger>() );
			
		
//			System.out.println( result.getRe().getVal() );
			
//			System.out.println( result.getIm().getVal() );
			
			
					
			Assert.assertEquals( 89.0 , 
					result.getRe().getVal() , 1E-10 );
					
			Assert.assertEquals( 20.0 , 
					result.getIm().getVal() , 1E-10 );
			
			
		}

	
}

