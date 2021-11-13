



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
 * Tests the ability to generate the parametric conic surface in G-4 in Equation 5.9 of the paper
 * "Projective Geometry with Clifford Algebra" by David Hestenes and
 * Renatus Ziegler, Arizona State University Department of Physics.  
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestBivectorConicG4 extends TestCase {
	
	
	/**
	 * Calculates the dual defined in Equation 2.19 of the paper
	 * "Projective Geometry with Clifford Algebra" by David Hestenes and
     * Renatus Ziegler, Arizona State University Department of Physics.  
	 * @param a The input multivector from which to calculate the dual.
	 * @param I The unit pseudoscalar.
	 * @return The calculated dual.
	 * 
	 * @throws Throwable
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		calcDual(
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a,
				GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> I
				) throws Throwable
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = a.mult( I.invertLeft() );
		return( ret );
	}
	
	
	
	/**
	 * Calculates the meet operator defined in Equation 3.5 of the paper
	 * "Projective Geometry with Clifford Algebra" by David Hestenes and
     * Renatus Ziegler, Arizona State University Department of Physics.  
	 * @param A First argument.
	 * @param B Second argument.
	 * @param I The unit pseudoscalar.
	 * @return The calculated meet.
	 * @throws Throwable
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
	meetOp(
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> A,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> B,
			GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> I
			) throws Throwable
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dualA = calcDual( A , I );
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			dualB = calcDual( B , I );
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		args.add( dualB );
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			prod = dualA.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		/*
		 * According to equation 3.5, the product should be the dual of the meet, or the meet multiplied by I-inverse.
		 * Hence, multiplying the product on the right once by I should yield the meet.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = prod.mult( I );
		
		return ret;
	}
	
	
	
	
	/**
	 * Creates a random vector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random vector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> createRandomVector(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se
			)
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a = se.zero();
		
		for( int cntA = 0 ; cntA < TestDimensionFour.FOUR ; cntA++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cntA ) );
			a.setVal( key , new DoubleElem( rand.nextDouble() ) );
		}
		
		return( a );
	}
	
	
	
	
	/**
	 * Creates a random bivector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random bivector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> createRandomBivector(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se
			) throws Throwable
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a = createRandomVector(rand, se);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b = createRandomVector(rand, se);
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		args.add( b );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> c =
				a.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		
		return( c );
	}
	
	
	
	
	/**
	 * Creates a random trivector.
	 * @param rand Random number generator.
	 * @param se The Factory for producing multivectors.
	 * @return The random trivector.
	 */
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> createRandomTrivector(
			Random rand,
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se
			) throws Throwable
	{
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a = createRandomBivector(rand, se);
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b = createRandomVector(rand, se);
		
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>();
		args.add( b );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> c =
				a.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		
		return( c );
	}
	
	
	
	
	
	
	/**
	 * Tests the ability to generate the parametric conic surface in G-4 in Equation 5.9 of the paper
     * "Projective Geometry with Clifford Algebra" by David Hestenes and
     * Renatus Ziegler, Arizona State University Department of Physics.  
	 * 
	 * @throws NotInvertibleException
	 */
	public void TestBivectorConicG4A( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionFour td = new TestDimensionFour();
		
		final GeometricAlgebraOrd<TestDimensionFour> ord = new GeometricAlgebraOrd<TestDimensionFour>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		/**
		 * The A bivector from equation 5.9 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> A = createRandomBivector(rand, se);
		
		
		/**
		 * The B bivector from equation 5.9 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> B = createRandomBivector(rand, se);	
		
		
		/**
		 * The C bivector from equation 5.9 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> C = createRandomBivector(rand, se);
		
		
		/**
		 * The Phi trivector from equation 5.9 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> Phi = createRandomTrivector(rand, se);
		
		
		/**
		 * The Psi trivector from equation 5.9 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> Psi = createRandomTrivector(rand, se);
		
		
		/**
		 * The Omega trivector from equation 5.9 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> Omega = createRandomTrivector(rand, se);
		
		

		
		/**
		 * The unit pseudoscalar of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> I = se.zero();
		
		final HashSet<BigInteger> hsi = new HashSet<BigInteger>();
		hsi.add( BigInteger.ZERO );
		hsi.add( BigInteger.ONE );
		hsi.add( BigInteger.valueOf( 2 ) );
		hsi.add( BigInteger.valueOf( 3 ) );
		I.setVal( hsi , dl.identity() );
		
		
		
		
		
		final int MAX = 30;
		for( int cntA = 0 ; cntA < MAX ; cntA++ )
		{
			final double lam = (double) cntA / (MAX - 1);
			
			/**
			 * The lambda scalar from equation 5.9 of the projective geometry paper.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> lambda = se.zero();
			
			{
				final HashSet<BigInteger> li = new HashSet<BigInteger>();
				lambda.setVal( li , new DoubleElem( lam ) );
			}
			
			
			
			for( int cntB = 0 ; cntB < MAX ; cntB++ )
			{
				final double muu = (double) cntB / (MAX - 1);
				
				/**
				 * The mu scalar from equation 5.9 of the projective geometry paper.
				 */
				final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> mu = se.zero();
				
				{
					final HashSet<BigInteger> li = new HashSet<BigInteger>();
					mu.setVal( li , new DoubleElem( muu ) );
				}
				

			
			
				

				/**
				 * The p vector from equation 5.9 of the projective geometry paper.
				 */
				final GeometricAlgebraMultivectorElem<TestDimensionFour,GeometricAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> p 
					= meetOp(
						A.add( lambda.mult( B ) ).add( mu.mult( C ) ) ,
						Phi.add( lambda.mult( Psi ) ).add( mu.mult( Omega ) )  ,
							I);
				
				

			
				// Divide x, y, and z by x3 to projectively map to the x3 = 1 volume.
				{
					final HashSet<BigInteger> hs0 = new HashSet<BigInteger>();
					final HashSet<BigInteger> hs1 = new HashSet<BigInteger>();
					final HashSet<BigInteger> hs2 = new HashSet<BigInteger>();
					final HashSet<BigInteger> hs3 = new HashSet<BigInteger>();
					hs0.add( BigInteger.ZERO);
					hs1.add( BigInteger.ONE );
					hs2.add( BigInteger.valueOf( 2 ) );
					hs3.add( BigInteger.valueOf( 3 ) );
					final double xd = p.getVal( hs0 ).getVal();
					final double yd = p.getVal( hs1 ).getVal();
					final double zd = p.getVal( hs2 ).getVal();
					final double x3d = p.getVal( hs3 ).getVal();
					System.out.println( "---" );
					System.out.println( xd / x3d );
					System.out.println( yd / x3d );
					System.out.println( zd / x3d );
				} 
			
			
				// System.out.println( "Run " + cnt );
			}
			
			
		}
		
		
		System.out.println( "Finished" );
		
		
	}
	
	
	
}



