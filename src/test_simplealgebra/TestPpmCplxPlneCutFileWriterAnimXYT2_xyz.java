



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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.Sqrt;
import simplealgebra.ComplexElem.ComplexCmd;
import simplealgebra.constants.CpuInfo;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.DbFastArray4D_Param;
import simplealgebra.store.DrFastArray4D_Dbl;
import simplealgebra.store.RawFileWriter;



/**  
 * Simple test of the RawFileWriter class for complex values.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * Tests generation of the X-Y plane at constant Z.  Calculation of rough time derivatives.  (TestB)
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestPpmCplxPlneCutFileWriterAnimXYT2_xyz extends TestCase {
	
	
	
	
	
	/**
	 * The number of discretizations on the T-Axis.
	 */
	protected static final int NUM_T_ITER = 340; // 295; // 250; // 200; // 100; // IterConstants.LRG_ITER_T;
	
	/**
	 * The number of discretizations on the X-Axis.
	 */
	protected static final int NUM_X_ITER = 165; // 145; // 125; // 100; // 50; // IterConstants.LRG_ITER_X;
	
	/**
	 * The number of discretizations on the Y-Axis.
	 */
	protected static final int NUM_Y_ITER = 165; // 145; // 125; // 100; // 50; // IterConstants.LRG_ITER_Y;
	
	/**
	 * The number of discretizations on the Z-Axis.
	 */
	protected static final int NUM_Z_ITER = 165; // 145; // 125; // 100; // 50; // IterConstants.LRG_ITER_Z;
	

	

	
	
	
	
	/**
	 * Sample writer to test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TstPpmFileWriterAnimB extends RawFileWriter
	{


		
		
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
		 * The Z-Axis cell size.
		 */
		protected static final int ZMULT = 8;
		
		

		
		
		
		/**
		 * Real result array over which to get data.
		 */
		protected DrFastArray4D_Dbl iterArrayRe = null;
		
		
		/**
		 * Imaginary result array over which to get data.
		 */
		protected DrFastArray4D_Dbl iterArrayIm = null;
		
		
		/**
		 * The T-Axis value of the array to write.
		 */
		protected int tval;
		
		
		/**
		 * The calculated maximum value in the file.  Initially 1.0 until the actual max is caucluated.
		 */
		protected double maxVal = 1.0;
		
		
		
		
		/**
		 * Constructs the writer.
		 * 
		 * @param _tval The T-Axis value at which to perform the write.
		 * @throws Throwable
		 */
		public TstPpmFileWriterAnimB( final int _tval ) throws Throwable
		{
			tval = _tval;
			
			String databaseLocationRe = "mydbRe"; // "/media/tgreen/My Passport/mydbRe"; // DatabasePathForTest.FILESPACE_PATH + "mydbRe";
			
			String databaseLocationIm = "mydbIm"; // "/media/tgreen/My Passport/mydbIm"; // DatabasePathForTest.FILESPACE_PATH + "mydbIm";
			
			
			final DbFastArray4D_Param dparam = new DbFastArray4D_Param();
			dparam.setTmult( TMULT );
			dparam.setXmult( XMULT );
			dparam.setYmult( YMULT );
			dparam.setZmult( ZMULT );
			dparam.setTmax( NUM_T_ITER );
			dparam.setXmax( NUM_X_ITER );
			dparam.setYmax( NUM_Y_ITER );
			dparam.setZmax( NUM_Z_ITER );
			
			iterArrayRe = new DrFastArray4D_Dbl( dparam , databaseLocationRe );
			
			iterArrayIm = new DrFastArray4D_Dbl( dparam , databaseLocationIm );
			
			
		}

		
		
		@Override
		protected int bufferSize() {
			return( 8000 );
		}

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final ComplexElem<DoubleElem,DoubleElemFactory> II = new ComplexElem<DoubleElem,DoubleElemFactory>( dfac.zero() , dfac.identity() );
		
		
		protected double getActValRe( int t, int x, int y, int z )throws Throwable
		{
			final double X_HALF = ( getXEnd() ) / 2.0;
			final double Y_HALF = ( getYEnd() ) / 2.0;
			final double Z_HALF = ( getZEnd() ) / 2.0;
			final double xdel = Math.abs( x - X_HALF );
			final double ydel = Math.abs( y - Y_HALF );
			final double zdel = Math.abs( z - Z_HALF );
			double delMag = Math.sqrt( xdel * xdel + ydel * ydel + zdel * zdel );
			if( delMag < 1E-40 )
			{
				delMag = 1E-40;
			}
			ComplexElem<DoubleElem,DoubleElemFactory> xmult = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xdel / delMag ) , new DoubleElem( 0.0 ) );
			ComplexElem<DoubleElem,DoubleElemFactory> ymult = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( ydel / delMag ) , new DoubleElem( 0.0 ) );
			ComplexElem<DoubleElem,DoubleElemFactory> zmult = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( zdel / delMag ) , new DoubleElem( 0.0 ) );
			
			final double xd0re = iterArrayRe.get(t, x-1, y, z);
			final double xd0im = iterArrayIm.get(t, x-1, y, z);
			final double xd0rep = iterArrayRe.get(t, x, y, z);
			final double xd0imp = iterArrayIm.get(t, x, y, z);
			
			final double yd0re = iterArrayRe.get(t, x, y-1, z);
			final double yd0im = iterArrayIm.get(t, x, y-1, z);
			final double yd0rep = xd0rep; // iterArrayRe.get(t, x, y, z);
			final double yd0imp = xd0imp; // iterArrayIm.get(t, x, y, z);
			
			final double zd0re = iterArrayRe.get(t, x, y, z-1);
			final double zd0im = iterArrayIm.get(t, x, y, z-1);
			final double zd0rep = xd0rep; // iterArrayRe.get(t, x, y, z);
			final double zd0imp = xd0imp; // iterArrayIm.get(t, x, y, z);
			
			final double xd0ret = iterArrayRe.get(t-1, x-1, y, z);
			final double xd0imt = iterArrayIm.get(t-1, x-1, y, z);
			final double xd0rept = iterArrayRe.get(t-1, x, y, z);
			final double xd0impt = iterArrayIm.get(t-1, x, y, z);
			
			final double yd0ret = iterArrayRe.get(t-1, x, y-1, z);
			final double yd0imt = iterArrayIm.get(t-1, x, y-1, z);
			final double yd0rept = xd0rept; // iterArrayRe.get(t, x, y, z);
			final double yd0impt = xd0impt; // iterArrayIm.get(t, x, y, z);
			
			final double zd0ret = iterArrayRe.get(t-1, x, y, z-1);
			final double zd0imt = iterArrayIm.get(t-1, x, y, z-1);
			final double zd0rept = xd0rept; // iterArrayRe.get(t, x, y, z);
			final double zd0impt = xd0impt; // iterArrayIm.get(t, x, y, z);
			
			ComplexElem<DoubleElem,DoubleElemFactory> xd0pA = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xd0rep ) , new DoubleElem( xd0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> xd0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xd0re - xd0ret ) , new DoubleElem( xd0im - xd0imt ) );
			ComplexElem<DoubleElem,DoubleElemFactory> xd0p = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xd0rep - xd0rept ) , new DoubleElem( xd0imp - xd0impt ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> yd0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( yd0re - yd0ret ) , new DoubleElem( yd0im - yd0imt ) );
			ComplexElem<DoubleElem,DoubleElemFactory> yd0p = xd0p; // new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( yd0rep ) , new DoubleElem( yd0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> zd0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( zd0re - zd0ret ) , new DoubleElem( zd0im - zd0imt ) );
			ComplexElem<DoubleElem,DoubleElemFactory> zd0p = xd0p; // new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( zd0rep ) , new DoubleElem( zd0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> xd1 = xd0p.add( xd0.negate() );;
			
			ComplexElem<DoubleElem,DoubleElemFactory> yd1 = yd0p.add( yd0.negate() );
			
			ComplexElem<DoubleElem,DoubleElemFactory> zd1 = zd0p.add( zd0.negate() );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d1 = ( xd1.mult( xmult ) ).add( yd1.mult( ymult ) ).add( zd1.mult( zmult ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d2 = xd0pA.handleOptionalOp( ComplexCmd.CONJUGATE_LEFT , null );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d3 = II.negate().negate().mult( d1 );
			
			return( ( d2.mult( d3 ) ).getRe().getVal() );
		}
		
		
		protected double getActValIm( int t, int x, int y, int z )throws Throwable
		{
			final double X_HALF = ( getXEnd() ) / 2.0;
			final double Y_HALF = ( getYEnd() ) / 2.0;
			final double Z_HALF = ( getZEnd() ) / 2.0;
			final double xdel = Math.abs( x - X_HALF );
			final double ydel = Math.abs( y - Y_HALF );
			final double zdel = Math.abs( z - Z_HALF );
			double delMag = Math.sqrt( xdel * xdel + ydel * ydel + zdel * zdel );
			if( delMag < 1E-40 )
			{
				delMag = 1E-40;
			}
			ComplexElem<DoubleElem,DoubleElemFactory> xmult = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xdel / delMag ) , new DoubleElem( 0.0 ) );
			ComplexElem<DoubleElem,DoubleElemFactory> ymult = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( ydel / delMag ) , new DoubleElem( 0.0 ) );
			ComplexElem<DoubleElem,DoubleElemFactory> zmult = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( zdel / delMag ) , new DoubleElem( 0.0 ) );
			
			final double xd0re = iterArrayRe.get(t, x-1, y, z);
			final double xd0im = iterArrayIm.get(t, x-1, y, z);
			final double xd0rep = iterArrayRe.get(t, x, y, z);
			final double xd0imp = iterArrayIm.get(t, x, y, z);
			
			final double yd0re = iterArrayRe.get(t, x, y-1, z);
			final double yd0im = iterArrayIm.get(t, x, y-1, z);
			final double yd0rep = xd0rep; // iterArrayRe.get(t, x, y, z);
			final double yd0imp = xd0imp; // iterArrayIm.get(t, x, y, z);
			
			final double zd0re = iterArrayRe.get(t, x, y, z-1);
			final double zd0im = iterArrayIm.get(t, x, y, z-1);
			final double zd0rep = xd0rep; // iterArrayRe.get(t, x, y, z);
			final double zd0imp = xd0imp; // iterArrayIm.get(t, x, y, z);
			
			final double xd0ret = iterArrayRe.get(t-1, x-1, y, z);
			final double xd0imt = iterArrayIm.get(t-1, x-1, y, z);
			final double xd0rept = iterArrayRe.get(t-1, x, y, z);
			final double xd0impt = iterArrayIm.get(t-1, x, y, z);
			
			final double yd0ret = iterArrayRe.get(t-1, x, y-1, z);
			final double yd0imt = iterArrayIm.get(t-1, x, y-1, z);
			final double yd0rept = xd0rept; // iterArrayRe.get(t, x, y, z);
			final double yd0impt = xd0impt; // iterArrayIm.get(t, x, y, z);
			
			final double zd0ret = iterArrayRe.get(t-1, x, y, z-1);
			final double zd0imt = iterArrayIm.get(t-1, x, y, z-1);
			final double zd0rept = xd0rept; // iterArrayRe.get(t, x, y, z);
			final double zd0impt = xd0impt; // iterArrayIm.get(t, x, y, z);
			
			ComplexElem<DoubleElem,DoubleElemFactory> xd0pA = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xd0rep ) , new DoubleElem( xd0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> xd0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xd0re - xd0ret ) , new DoubleElem( xd0im - xd0imt ) );
			ComplexElem<DoubleElem,DoubleElemFactory> xd0p = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( xd0rep - xd0rept ) , new DoubleElem( xd0imp - xd0impt ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> yd0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( yd0re - yd0ret ) , new DoubleElem( yd0im - yd0imt ) );
			ComplexElem<DoubleElem,DoubleElemFactory> yd0p = xd0p; // new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( yd0rep ) , new DoubleElem( yd0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> zd0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( zd0re - zd0ret ) , new DoubleElem( zd0im - zd0imt ) );
			ComplexElem<DoubleElem,DoubleElemFactory> zd0p = xd0p; // new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( zd0rep ) , new DoubleElem( zd0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> xd1 = xd0p.add( xd0.negate() );;
			
			ComplexElem<DoubleElem,DoubleElemFactory> yd1 = yd0p.add( yd0.negate() );
			
			ComplexElem<DoubleElem,DoubleElemFactory> zd1 = zd0p.add( zd0.negate() );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d1 = ( xd1.mult( xmult ) ).add( yd1.mult( ymult ) ).add( zd1.mult( zmult ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d2 = xd0pA.handleOptionalOp( ComplexCmd.CONJUGATE_LEFT , null );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d3 = II.negate().negate().mult( d1 );
			
			return( ( d2.mult( d3 ) ).getIm().getVal() );
		}
		


		@Override
		protected double getVal( int t, int x, int y, int z )
				throws Throwable {

			final double d0re = getActValRe(t, x, y, z);
			final double d0im = getActValIm(t, x, y, z);
			final double d0r = Math.sqrt( d0re * d0re + d0im * d0im );
			
			final double logVal = Math.log10( d0r );
			
			if( d0r < 1E-30 )
			{
				return( 0.0 );
			}
					
			return( logVal + 30.0 );
		}
		
		protected void getValCplx( int t, int x, int y, int z , double minVal , double maxVal , double[] out )
				throws Throwable {

			final double d0re = getActValRe(t, x, y, z);
			final double d0im = getActValIm(t, x, y, z);
			final double d0r = Math.sqrt( d0re * d0re + d0im * d0im );
			
			final double logVal = Math.log10( d0r );
			
			if( d0r < 1E-30 )
			{
				out[ 0 ] = 0.0;
				out[ 1 ] = 0.0;
				return;
			}

			double rv = ( ( logVal + 30.0 ) - minVal ) / ( maxVal - minVal );
			if( rv < 0.0 ) rv = 0.0;
			out[ 0 ] = ( d0re / d0r ) * rv;
			out[ 1 ] = ( d0im / d0r ) * rv;
		}

		@Override
		protected int getTStrt() {
			return( tval );
		}

		@Override
		protected int getTEnd() {
			return( tval + 1 );
		}

		@Override
		protected int getZStrt() {
			return( 1 );
		}

		@Override
		protected int getZEnd() {
			return( NUM_Z_ITER );
		}

		@Override
		protected int getYStrt() {
			return( 1 );
		}

		@Override
		protected int getYEnd() {
			return( NUM_Y_ITER );
		}

		@Override
		protected int getXStrt() {
			return( 1 );
		}

		@Override
		protected int getXEnd() {
			return( NUM_X_ITER );
		}
		
		
		
		public double calcMinAbs( final int z , final int t ) throws Throwable
		{
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			final int X_STRT = getXStrt();
			final int X_END = getXEnd();
			
			
			double dd = 1E+60;
			
			
			for( int y = Y_STRT ; y < Y_END ; y++ )
			{
				for( int x = X_STRT ; x < X_END ; x++ )
				{
					final double dval = Math.abs( getVal( t , x , y , /* z !!!!!!!!!!!!!!!!!!!! */ x ) );
					if( dval > 1E-30 )
					{
						dd = Math.min( dd , dval );
					}
				}
			}
			
			return( dd );
		}
		
		
		/**
		 * Writes PPM to a file.
		 * 
		 * @param pathName The path to the output file containing positive values.
		 * @throws Throwable
		 */
		public void writePpm( final int t , String pathName ) throws Throwable
		{			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			final int X_STRT = getXStrt();
			final int X_END = getXEnd();
			
			
			final int z = ( Z_STRT + Z_END ) / 2;
			
			
			System.out.println( "Starting calcMax" );
			
			
//			maxVal = this.calcMaxAbs();
			
			
//			System.out.println( "Ended calcMax" );
			
//			System.out.println( maxVal );
			
			
//			if( maxVal < 1E-30 )
//			{
//				maxVal = 1.0;
//			}
			
			
			final long DV = 255;
			
			
			
			final FileOutputStream fo = new FileOutputStream( pathName );
			BufferedOutputStream baos = new BufferedOutputStream( fo );
			
			PrintStream ps = new PrintStream( baos );
			ps.println( "P6" );
			ps.println( "" + ( X_END - X_STRT ) + " " + ( Y_END - Y_STRT ) );
			ps.println( "255" );
			ps.flush();
			ps = null;
			
			
			final double[] dval = new double[ 2 ];
			
			
			double maxVal = this.calcMaxAbs( z , t );
			
			double minVal = this.calcMinAbs( z , t );
				
			System.out.println( ">>>> " + maxVal );
				
			if( maxVal < 1E-30 )
			{
				maxVal = 1.0;
			}
			
			/* getValCplx( 35 , 8 , 8 , 8 , 1.0 , dval );
			
			System.out.println( "q1 " + dval[ 0]  );
			System.out.println( "q2 " + dval[ 1]  ); */
				
			
			for( int y = Y_STRT ; y < Y_END ; y++ )
			{
				for( int x = X_STRT ; x < X_END ; x++ )
				{
					// System.out.println( DV );
					getValCplx( t , x , y , /* z !!!!!!!!!!!!!!!!!!!!!!!! */ x , minVal , maxVal , dval );
							
					int red = 0;
					int green = 0;
					int blue = 0;
							
					if( dval[ 0 ] >= 0.0 )
					{
						blue += (int)( Math.abs( DV * dval[ 0 ] ) );
					}
					else
					{
						red += (int)( Math.abs( DV * dval[ 0 ] ) );
						green += (int)( Math.abs( DV * dval[ 0 ] ) );
						blue += (int)( Math.abs( DV * dval[ 0 ] ) );
					}
					
					if( dval[ 1 ] >= 0.0 )
					{
						red += (int)( Math.abs( DV * dval[ 1 ] ) );
					}
					else
					{
						green += (int)( Math.abs( DV * dval[ 1 ] ) );
					}
							
					baos.write( red );
					baos.write( green );
					baos.write( blue );
							
				}
			}
			
			iterArrayRe.close();
			iterArrayIm.close();
			baos.close();
			
		}
		
		
	}
	
	
	
	/**
	 * Simple test of the RawFileWriter class.
	 * 
	 * @throws Throwable
	 */
	public void testDf3FileWriter() throws Throwable
	{
		final int numCores = CpuInfo.NUM_CPU_CORES;
		final Runnable[] runn = new Runnable[ numCores ];
		final boolean[] b = CpuInfo.createBool( false );
		
		final int T_MAX = NUM_T_ITER;
		
		final int T_OFFSET = 1;
		
		
		final String FILE_PATH_PREFIX = "outDfAnimBPpm";
		
		
		// System.out.println( "Started..." ); 
		for( int ccnt = 0 ; ccnt < numCores ; ccnt++ )
		{
			final int core = ccnt;
			runn[ core ] = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						for( int cnt = core ; cnt < ( T_MAX - 2 /* 1 */ ) ; cnt = cnt + numCores )
						{
							System.out.println( cnt );
			
							final int tval = cnt + T_OFFSET;
			
							String filePath = FILE_PATH_PREFIX + cnt + ".ppm";
		
							TstPpmFileWriterAnimB writer = new TstPpmFileWriterAnimB( tval );
		
							writer.writePpm( tval , filePath );
						}
					}
					catch( Error ex  ) { ex.printStackTrace( System.out ); }
					catch( Throwable ex ) { ex.printStackTrace( System.out ); }
					
					synchronized( this )
					{
						b[ core ] = true;
						this.notify();
					}
					
				}
			};
		}
		
		
		CpuInfo.start( runn );
		CpuInfo.wait( runn , b );
		
		
	}
	

	

	
}



