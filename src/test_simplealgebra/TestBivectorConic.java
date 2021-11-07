



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
 * Tests the ability to generate the parametric conic in Equation 4.18 of the paper
 * "Projective Geometry with Clifford Algebra" by David Hestenes and
 * Renatus Ziegler, Arizona State University Department of Physics.  
 * Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestBivectorConic extends TestCase {
	
	
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
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
		calcDual(
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> a,
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> I
				) throws Throwable
	{
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
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
	protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
	meetOp(
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> A,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> B,
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> I
			) throws Throwable
	{
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			dualA = calcDual( A , I );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			dualB = calcDual( B , I );
		final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
			args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
		args.add( dualB );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			prod = dualA.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		/*
		 * According to equation 3.5, the product should be the dual of the meet, or the meet multiplied by I-inverse.
		 * Hence, multiplying the product on the right once by I should yield the meet.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = prod.mult( I );
		
		return ret;
	}
	
	
	
	
	
	
	/**
	 * Tests the ability to generate the parametric conic in Equation 4.18 of the paper
     * "Projective Geometry with Clifford Algebra" by David Hestenes and
     * Renatus Ziegler, Arizona State University Department of Physics.  
	 * 
	 * @throws NotInvertibleException
	 */
	public void testBivectorConicA( ) throws Throwable
	{
		Random rand = new Random( 5432 );
		
		final TestDimensionThree td = new TestDimensionThree();
		
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> se = 
				new GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(dl, td, ord);
		
		
		/**
		 * The a-vector from equation 4.18 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> a = se.zero();
		
		/**
		 * The b-vector from equation 4.18 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> b = se.zero();
		
		/**
		 * The a-prime-vector from equation 4.18 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ap = se.zero();
		
		/**
		 * The b-prime-vector from equation 4.18 of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> bp = se.zero();
		

		
		/**
		 * The unit pseudoscalar of the projective geometry paper.
		 */
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> I = se.zero();
		
		final HashSet<BigInteger> hsi = new HashSet<BigInteger>();
		hsi.add( BigInteger.ZERO );
		hsi.add( BigInteger.ONE );
		hsi.add( BigInteger.valueOf( 2 ) );
		I.setVal( hsi , dl.identity() );
		
		
		
		for( int cntA = 0 ; cntA < TestDimensionThree.THREE ; cntA++ )
		{
			final HashSet<BigInteger> key = new HashSet<BigInteger>();
			key.add( BigInteger.valueOf( cntA ) );
			a.setVal( key , new DoubleElem( rand.nextDouble() ) );
			b.setVal( key , new DoubleElem( rand.nextDouble() ) );
			ap.setVal( key , new DoubleElem( rand.nextDouble() ) );
			bp.setVal( key , new DoubleElem( rand.nextDouble() ) );
		}
		
		
		

		
		
		/**
		 * The A-bivector from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> A = null;
		
		/**
		 * The B-bivector from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> B = null;
		
		/**
		 * The A-prime-bivector from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> Ap = null;
		
		/**
		 * The B-prime-bivector from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> Bp = null;
		
		
		
		
		{
			final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			args.add( b );
			A =  a.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
		
		
		
		
		{
			final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			args.add( bp );
			B =  a.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
		
		
		
		
		{
			final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			args.add( b );
			Ap =  ap.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
		
		
		
		
		{
			final ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>> 
				args = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			args.add( bp );
			Bp =  ap.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.WEDGE , args );
		}
		
		
		/**
		 * The A-meet-A-prime result from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> AmeetAp 
			= meetOp( A , Ap , I );
		
		/**
		 * The A-meet-B-prime result from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> AmeetBp 
			= meetOp( A , Bp , I );
		
		/**
		 * The B-meet-A-prime result from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> BmeetAp 
			= meetOp( B , Ap , I );
		
		/**
		 * The B-meet-B-prime result from equation 4.18 of the projective geometry paper.
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> BmeetBp 
			= meetOp( B , Bp , I );
		
		
		/**
		 * The addition of AmeetBp and BmeetAp
		 */
		GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> AmeetBpPlusBmeetAp
			= AmeetBp.add( BmeetAp );
		
		
		
		final int MAX = 100;
		for( int cnt = 0 ; cnt < MAX ; cnt++ )
		{
			final double lam = (double) cnt / (MAX - 1);
			
			/**
			 * The lambda scalar from equation 4.18 of the projective geometry paper.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lambda = se.zero();
			
			{
				final HashSet<BigInteger> li = new HashSet<BigInteger>();
				lambda.setVal( li , new DoubleElem( lam ) );
			}
			
			
			/**
			 * The lambda-squared scalar from equation 4.18 of the projective geometry paper.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> lambdaSq 
				= lambda.mult( lambda );
			
			
			
			/**
			 * The x vector from equation 4.18 of the projective geometry paper.
			 */
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> x 
				= AmeetAp
					.add( lambda.mult( AmeetBpPlusBmeetAp ) )
					.add( lambdaSq.mult( BmeetBp ) );
			
			
			
			
			// Divide x and y by z to projectively map to the z = 1 plane.
			{
				final HashSet<BigInteger> hs0 = new HashSet<BigInteger>();
				final HashSet<BigInteger> hs1 = new HashSet<BigInteger>();
				final HashSet<BigInteger> hs2 = new HashSet<BigInteger>();
				hs0.add( BigInteger.ZERO);
				hs1.add( BigInteger.ONE );
				hs2.add( BigInteger.valueOf( 2 ) );
				final double xd = x.getVal( hs0 ).getVal();
				final double yd = x.getVal( hs1 ).getVal();
				final double zd = x.getVal( hs2 ).getVal();
				System.out.println( "---" );
				System.out.println( xd / zd );
				System.out.println( yd / zd );
			} 
			
			
			// System.out.println( "Run " + cnt );
			
		}
		
		
		System.out.println( "Finished" );
		
		
	}
	
	
	
}



