



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
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SquareMatrixElemFactory;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;


/**
 * 
 * Test for phasors as used in Electrical Engineering.
 * 
 * 
 * Phasor example from:
 * 
 * http://www.chegg.com/homework-help/questions-and-answers/use-mesh-analysis-find-phasor-currents-ia-ib-q887126
 * 
 * - j 40 ohm * Ia - 30 ohm * Ia + 20 ohm * Ib + j 10 ohm * Ib - 120V = 0
 * 
 * 120V - j 10 ohm * Ib - 20 ohm * Ib - 30 ohm * Ic - j 20 ohm * Ic
 * 
 * Ia + Ib = Ic
 * 
 * 
 * --------------------------- Goes To --------------------------------
 * 
 * ( - j 40 ohm * Ia - 30 ohm * Ia ) + ( 20 ohm * Ib + j 10 ohm * Ib ) = 120V
 * 
 * ( - 30 ohm * Ia - j 20 ohm * Ia ) + ( - j 30 ohm * Ib - 50 ohm * Ib ) = -120V
 * 
 * @author thorngreen
 *
 */
public class TestPhasorExample extends TestCase {

	
	
	public void testPhasorExample() throws NotInvertibleException
	{
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cl = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dl );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		
		final SquareMatrixElemFactory<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> se = 
				new SquareMatrixElemFactory<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(cl, td);
		
		
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> mat = se.zero();
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> c00 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -30.0 ) , new DoubleElem( -40.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> c01 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 20.0 ) , new DoubleElem( 10.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> c10 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -30.0 ) , new DoubleElem( -20.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> c11 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -50.0 ) , new DoubleElem( -30.0 ) );
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> a0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 120.0 ) , new DoubleElem( 0.0 ) );
		
		final ComplexElem<DoubleElem,DoubleElemFactory> a1 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( -120.0 ) , new DoubleElem( 0.0 ) );
		
		
		mat.setVal( BigInteger.ZERO , BigInteger.ZERO , c00 );
		
		mat.setVal( BigInteger.ZERO , BigInteger.ONE , c01 );
		
		mat.setVal( BigInteger.ONE , BigInteger.ZERO , c10 );
		
		mat.setVal( BigInteger.ONE , BigInteger.ONE , c11 );
		
		
		
		{
			final ComplexElem<DoubleElem,DoubleElemFactory> inv = c01.invertLeft();
			final ComplexElem<DoubleElem,DoubleElemFactory> shouldUnity = inv.mult( c01 );
			Assert.assertEquals( 1.0 , shouldUnity.getRe().getVal() , 1E-5 );
			Assert.assertEquals( 0.0 , shouldUnity.getIm().getVal() , 1E-5 );
		}
		
		
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> invert = mat.invertLeft();
		
		
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> 
			shouldBeIdent = mat.mult( invert );
		
		
		int i;
		int j;
		for( i = 0 ; i < 2 ; i++ )
		{
			for( j = 0 ; j < 2 ; j++ )
			{
				final double reMatchVal = i == j ? 1.0 : 0.0;
				Assert.assertEquals( reMatchVal , 
						shouldBeIdent.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getRe().getVal() , 1E-5 );
				Assert.assertEquals( 0.0 , 
						shouldBeIdent.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getIm().getVal() , 1E-5 );
			}
		}
		
		
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> vl =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cl , td );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			inColumnVect = vl.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			outColumnVect = vl.zero();
		
		
		
		final HashSet<BigInteger> vectA0 = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> vectA1 = new HashSet<BigInteger>();
		
		
		
		vectA0.add( BigInteger.ZERO );
		
		vectA1.add( BigInteger.ONE );
		
		
		
		inColumnVect.setVal( vectA0 , a0 );
		
		inColumnVect.setVal( vectA1 , a1 );
		
		
		
		inColumnVect.colVectorMultLeftDefault(invert, outColumnVect);
		
		
		final ComplexElem<DoubleElem,DoubleElemFactory> iA = 
				invert.getVal(BigInteger.ZERO, BigInteger.ZERO).mult(a0).add(
						invert.getVal(BigInteger.ZERO, BigInteger.ONE).mult(a1));
		
		final ComplexElem<DoubleElem,DoubleElemFactory> iB = 
				invert.getVal(BigInteger.ONE, BigInteger.ZERO).mult(a0).add(
						invert.getVal(BigInteger.ONE, BigInteger.ONE).mult(a1));
		
		final ComplexElem<DoubleElem,DoubleElemFactory> iC = iA.add( iB );

		
		
		
		Assert.assertEquals( iA.getRe().getVal() , 
				outColumnVect.getVal( vectA0 ).getRe().getVal() , 1E-5 );
		
		Assert.assertEquals( iA.getIm().getVal() , 
				outColumnVect.getVal( vectA0 ).getIm().getVal() , 1E-5 );
		
		
		
		Assert.assertEquals( iB.getRe().getVal() , 
				outColumnVect.getVal( vectA1 ).getRe().getVal() , 1E-5 );
		
		Assert.assertEquals( iB.getIm().getVal() , 
				outColumnVect.getVal( vectA1 ).getIm().getVal() , 1E-5 );
		
		
		
		
	}
	
	

}


