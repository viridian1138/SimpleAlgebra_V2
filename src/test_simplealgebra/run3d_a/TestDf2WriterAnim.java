



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




package test_simplealgebra.run3d_a;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashSet;

import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.constants.CpuInfo;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.DbFastArray3D_Param;
import simplealgebra.store.DrFastArray3D_Dbl;
import test_simplealgebra.DatabasePathForTest;
import test_simplealgebra.IterConstants;
import test_simplealgebra.TestDimensionThree;



/**  
 * Simple test of the RawFileWriter class for real.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDf2WriterAnim extends TestCase {
	
	
	
	
	
	/**
	 * The number of discretizations on the T-Axis.
	 */
	protected static final int NUM_T_ITER = 1000; // IterConstants.LRG_ITER_T;
	
	/**
	 * The number of discretizations on the X-Axis.
	 */
	protected static final int NUM_X_ITER = IterConstants.LRG_ITER_X;
	
	/**
	 * The number of discretizations on the Y-Axis.
	 */
	protected static final int NUM_Y_ITER = IterConstants.LRG_ITER_Y;
	
	

	

	
	
	/**
	 * Generates a 3-D vector from its ordinates.
	 * @param x The X-Ordinate.
	 * @param y The Y-Ordinate.
	 * @param z The Z-Ordinate.
	 * @return The 3-D vector.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> convToVector( double x , double y , double z )
	{
		final DoubleElemFactory de = new DoubleElemFactory();
		final TestDimensionThree td = new TestDimensionThree();
		final GeometricAlgebraOrd<TestDimensionThree> ord = new GeometricAlgebraOrd<TestDimensionThree>( );
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>(de, td, ord);
		
		{
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			h.add( BigInteger.ZERO );
			ret.setVal( h , new DoubleElem( x ) );
		}
		
		{
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			h.add( BigInteger.ONE );
			ret.setVal( h , new DoubleElem( y ) );
		}
		
		{
			final HashSet<BigInteger> h = new HashSet<BigInteger>();
			h.add( BigInteger.valueOf( 2 ) );
			ret.setVal( h , new DoubleElem( z ) );
		}
		
		return( ret );
	}
	
	
	
	

	/**
	 * Generates the unit vector from the input.
	 * @param in The input vector.
	 * @return The unit vector.
	 */
	protected static GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> calcUnit( GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> in )
		throws NotInvertibleException
	{
		final DoubleElemFactory de = new DoubleElemFactory();
				
		DoubleElem total = de.zero();
		
		for( final DoubleElem dd : in.getValueSet() )
		{
			total = total.add( dd.mult( dd ) );
		}
		
		// final ArrayList<DoubleElem> args = new ArrayList<DoubleElem>();
		
		final DoubleElem invSqrtTotal = ( new DoubleElem( Math.sqrt( total.getVal() ) ) ).invertLeft();
		
		final Mutator<DoubleElem> mutr = new Mutator<DoubleElem>()
		{

			@Override
			public DoubleElem mutate(DoubleElem in)
					throws NotInvertibleException {
				return( in.mult( invSqrtTotal ) );
			}

			@Override
			public boolean exposesDerivatives() {
				return( false );
			}

			@Override
			public String writeString() {
				return( "Mult" );
			}

			@Override
			public Mutator<DoubleElem> cloneThread(BigInteger threadIndex) {
				throw( new RuntimeException( "Not Supported" ) );
			}
			
		};
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			ret = in.mutate( mutr );
		
		
		return( ret );
		
	}
	
	
	
	
	
	/**
	 * The T-Axis cell size.
	 */
	protected static final int TMULT = 8;
	
	/**
	 * The X-Axis cell size.
	 */
	protected static final int XMULT = 8;
	
	/**
	 * The Y-Axis cell size.
	 */
	protected static final int YMULT = 8;
	
	
	

	
	
	
	/**
	 * Simple test of the RawFileWriter class.
	 * 
	 * @throws Throwable
	 */
	public void testDf2FileWriter() throws Throwable
	{
		final int numCores = CpuInfo.NUM_CPU_CORES;
		final Runnable[] runn = new Runnable[ numCores ];
		final boolean[] b = CpuInfo.createBool( false );
		
		final int T_MAX = NUM_T_ITER;
		
		final int T_OFFSET = 0;
		
		
		
		
		String databaseLocationRe = DatabasePathForTest.FILESPACE_PATH + "mydbH";
							
							
		final DbFastArray3D_Param dparam = new DbFastArray3D_Param();
		dparam.setTmult( TMULT );
		dparam.setXmult( XMULT );
		dparam.setYmult( YMULT );
		dparam.setTmax( NUM_T_ITER );
		dparam.setXmax( NUM_X_ITER );
		dparam.setYmax( NUM_Y_ITER );
							
		DrFastArray3D_Dbl iterArrayRe = new DrFastArray3D_Dbl( dparam , databaseLocationRe );
							
							
							
		for( int tval = 0 ; tval < NUM_T_ITER ; ++tval )
		{
			
			System.out.println( tval );
			
			double maxAbs = 0.0;
			
			for( int xx = 0 ; xx < NUM_X_ITER ; xx++ )
			{
				for( int yy = 0 ; yy < NUM_Y_ITER ; yy++ )
				{
					final double v = iterArrayRe.get( tval , xx , yy );
					if( Math.abs( v ) > maxAbs ) maxAbs = Math.abs( v ) ;
				}
			}
			
			
			File ofilePpm = new File( "outAnim_" + tval + "+f" + ".ppm" );
			FileOutputStream fo = new FileOutputStream( ofilePpm );
			BufferedOutputStream baos = new BufferedOutputStream( fo );
			
			PrintStream ps = new PrintStream( baos );
			ps.println( "P6" );
			ps.println( "" + ( NUM_X_ITER ) + " " + ( NUM_Y_ITER ) );
			ps.println( "255" );
			ps.flush();
			ps = null;
			
			for( int y = NUM_Y_ITER - 1 ; y >= 0 ; y-- )
			{
				// System.out.println( "** " + y  + " / " + ( NUM_Y_ITER ) );
				for( int x = 0 ; x < NUM_X_ITER ; x++ )
				{
					final double v = iterArrayRe.get( tval , x , y );
					
					int red = 0;
					int green = 0;
					int blue = 0;
					
					if( v > 0 )
					{
						red = (int)( 255 * ( v ) / ( maxAbs ) );
					}
					
					if( v < 0 )
					{
						green = (int)( 255 * ( -v ) / ( maxAbs ) );
					}
					
					final Color col = new Color( red , green , blue );
					
					baos.write( col.getRed() );
					baos.write( col.getGreen() );
					baos.write( col.getBlue() );
				}
			}
			
			baos.close();
			
			
		}
							
							
							
							
		iterArrayRe.close();
							
							
		
		
		
	}
	

	

	
}



