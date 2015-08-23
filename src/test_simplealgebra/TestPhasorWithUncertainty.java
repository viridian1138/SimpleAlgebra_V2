



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
import simplealgebra.ga.*;
import simplealgebra.meas.ValueWithUncertaintyElem;
import simplealgebra.meas.ValueWithUncertaintyElemFactory;


/**
 * 
 * Test for phasors as used in Electrical Engineering (with experimental uncertainty added).
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
 * ---------------------------- with experimental uncertainty added by author.
 * 
 * 
 * 
 * --------------------------- Goes To --------------------------------
 * 
 * ( - j 40 ohm * Ia - 30 ohm * Ia ) + ( 20 ohm * Ib + j 10 ohm * Ib ) = 120V
 * 
 * ( - 30 ohm * Ia - j 20 ohm * Ia ) + ( - j 30 ohm * Ib - 50 ohm * Ib ) = -120V
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestPhasorWithUncertainty extends TestCase {

	
	
	/**
	 * Returns the input value with a 5% uncertainty.  Based on typical resistors where the actual resistance is within 5% of the stated value.
	 * @param val The input value.
	 * @return The input value with a 5% uncertainty.
	 */
	private ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> valueWithFivePercentError( double val )
	{
		return( new ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>( 
				new DoubleElem( val ) , new DoubleElem( 0.05 * Math.abs( val ) ) )  );
	}
	
	
	/**
	 * Returns the input value with an uncertainty of 0.1.  Based on typical voltimeters where the voltage is known to within 0.1 volts.
	 * @param val The input value.
	 * @return The input value with an uncertainty of 0.1.
	 */
	private ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory> valueWithOneTenthError( double val )
	{
		return( new ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>( 
				new DoubleElem( val ) , new DoubleElem( 0.1 ) )  );
	}
	
	
	/**
	 * Runs the test.
	 * 
	 * @throws NotInvertibleException
	 */
	public void testPhasorExample() throws NotInvertibleException
	{
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory> tl = new ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>( dl );
		
		final ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> cl = 
					new ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( tl );
		
		final TestDimensionTwo td = new TestDimensionTwo();
		
		final GeometricAlgebraOrd<TestDimensionTwo> ord = new GeometricAlgebraOrd<TestDimensionTwo>();
		
		
		final SquareMatrixElemFactory<TestDimensionTwo,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>> se = 
				new SquareMatrixElemFactory<TestDimensionTwo,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>>(cl, td);
		
		
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>> mat = se.zero();
		
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> c00 
			= new ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( valueWithFivePercentError( -30.0 ) , valueWithFivePercentError( -40.0 ) );
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> c01 
			= new ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( valueWithFivePercentError( 20.0 ) , valueWithFivePercentError( 10.0 ) );
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> c10 
			= new ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( valueWithFivePercentError( -30.0 ) , valueWithFivePercentError( -20.0 ) );
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> c11 
			= new ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( valueWithFivePercentError( -50.0 ) , valueWithFivePercentError( -30.0 ) );
		
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> a0 
			= new ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( valueWithOneTenthError( 120.0 ) , tl.zero() );
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> a1
			= new ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>( valueWithOneTenthError( -120.0 ) , tl.zero() );
		
		
		mat.setVal( BigInteger.ZERO , BigInteger.ZERO , c00 );
		
		mat.setVal( BigInteger.ZERO , BigInteger.ONE , c01 );
		
		mat.setVal( BigInteger.ONE , BigInteger.ZERO , c10 );
		
		mat.setVal( BigInteger.ONE , BigInteger.ONE , c11 );
		
		
		
		{
			final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> inv = c01.invertLeft();
			final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> shouldUnity = inv.mult( c01 );
			Assert.assertEquals( 1.0 , shouldUnity.getRe().getValue().getVal() , 1E-5 );
			Assert.assertEquals( 0.0 , shouldUnity.getIm().getValue().getVal() , 1E-5 );
		}
		
		
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>> invert = mat.invertLeft();
		
		
		
		final SquareMatrixElem<TestDimensionTwo,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>> 
			shouldBeIdent = mat.mult( invert );
		
		
		int i;
		int j;
		for( i = 0 ; i < TestDimensionTwo.TWO ; i++ )
		{
			for( j = 0 ; j < TestDimensionTwo.TWO ; j++ )
			{
				final double reMatchVal = i == j ? 1.0 : 0.0;
				Assert.assertEquals( reMatchVal , 
						shouldBeIdent.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getRe().getValue().getVal() , 1E-5 );
				Assert.assertEquals( 0.0 , 
						shouldBeIdent.getVal(BigInteger.valueOf(i) , BigInteger.valueOf(j) ).getIm().getValue().getVal() , 1E-5 );
			}
		}
		
		
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>> vl =
				new GeometricAlgebraMultivectorElemFactory<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>>( cl , td , ord );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>>
			inColumnVect = vl.zero();
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>,ComplexElemFactory<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>>>
			outColumnVect = vl.zero();
		
		
		
		final HashSet<BigInteger> vectA0 = new HashSet<BigInteger>();
		
		final HashSet<BigInteger> vectA1 = new HashSet<BigInteger>();
		
		
		
		vectA0.add( BigInteger.ZERO );
		
		vectA1.add( BigInteger.ONE );
		
		
		
		inColumnVect.setVal( vectA0 , a0 );
		
		inColumnVect.setVal( vectA1 , a1 );
		
		
		
		inColumnVect.colVectorMultLeftDefault(invert, outColumnVect);
		
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> iA = 
				invert.getVal(BigInteger.ZERO, BigInteger.ZERO).mult(a0).add(
						invert.getVal(BigInteger.ZERO, BigInteger.ONE).mult(a1));
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> iB = 
				invert.getVal(BigInteger.ONE, BigInteger.ZERO).mult(a0).add(
						invert.getVal(BigInteger.ONE, BigInteger.ONE).mult(a1));
		
		final ComplexElem<ValueWithUncertaintyElem<DoubleElem,DoubleElemFactory>,ValueWithUncertaintyElemFactory<DoubleElem,DoubleElemFactory>> iC = iA.add( iB );

		
		
		
		Assert.assertEquals( iA.getRe().getValue().getVal() , 
				outColumnVect.getVal( vectA0 ).getRe().getValue().getVal() , 1E-5 );
		
		Assert.assertEquals( iA.getIm().getValue().getVal() , 
				outColumnVect.getVal( vectA0 ).getIm().getValue().getVal() , 1E-5 );
		
		
		
		Assert.assertEquals( iB.getRe().getValue().getVal() , 
				outColumnVect.getVal( vectA1 ).getRe().getValue().getVal() , 1E-5 );
		
		Assert.assertEquals( iB.getIm().getValue().getVal() , 
				outColumnVect.getVal( vectA1 ).getIm().getValue().getVal() , 1E-5 );
		
		
		
		
		Assert.assertEquals( iA.getRe().getUncertainty().getVal() , 
				outColumnVect.getVal( vectA0 ).getRe().getUncertainty().getVal() , 1E-5 );
		
		Assert.assertEquals( iA.getIm().getUncertainty().getVal() , 
				outColumnVect.getVal( vectA0 ).getIm().getUncertainty().getVal() , 1E-5 );
		
		
		
		Assert.assertEquals( iB.getRe().getUncertainty().getVal() , 
				outColumnVect.getVal( vectA1 ).getRe().getUncertainty().getVal() , 1E-5 );
		
		Assert.assertEquals( iB.getIm().getUncertainty().getVal() , 
				outColumnVect.getVal( vectA1 ).getIm().getUncertainty().getVal() , 1E-5 );
		
		
		
		// System.out.println( outColumnVect.getVal( vectA0 ).getRe().getUncertainty().getVal() );
		
		// System.out.println( outColumnVect.getVal( vectA0 ).getIm().getUncertainty().getVal() );
		
		// System.out.println( outColumnVect.getVal( vectA1 ).getRe().getUncertainty().getVal() );
		
		// System.out.println( outColumnVect.getVal( vectA1 ).getIm().getUncertainty().getVal() );
		
		
		
	}
	
	

}


