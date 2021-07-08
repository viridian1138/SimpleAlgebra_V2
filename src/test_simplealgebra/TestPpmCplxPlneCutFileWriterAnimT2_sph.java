



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
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.DbFastArray4D_Param;
import simplealgebra.store.DrFastArray4D_Dbl;
import simplealgebra.store.RawFileWriter;



/**  
 * Simple test of the RawFileWriter class for complex values.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * Tests generation of the projection of a semi-spherical area onto a plane.  No integration of values across the boundary.  Calculation of rough time derivatives.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestPpmCplxPlneCutFileWriterAnimT2_sph extends TestCase {
	
	
	
	
	
	/**
	 * The number of discretizations on the T-Axis.
	 */
	protected static final int NUM_T_ITER = 250; // 200; // 100; // IterConstants.LRG_ITER_T;
	
	/**
	 * The number of discretizations on the X-Axis.
	 */
	protected static final int NUM_X_ITER = 125; // 100; // 50; // IterConstants.LRG_ITER_X;
	
	/**
	 * The number of discretizations on the Y-Axis.
	 */
	protected static final int NUM_Y_ITER = 125; // 100; // 50; // IterConstants.LRG_ITER_Y;
	
	/**
	 * The number of discretizations on the Z-Axis.
	 */
	protected static final int NUM_Z_ITER = 125; // 100; // 50; // IterConstants.LRG_ITER_Z;
	

	

	
	
	
	
	/**
	 * Sample writer to test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TstPpmFileWriterAnimT2_sph extends RawFileWriter
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
		public TstPpmFileWriterAnimT2_sph( final int _tval ) throws Throwable
		{
			tval = _tval;
			
			String databaseLocationRe = DatabasePathForTest.FILESPACE_PATH + "mydbRe";
			
			String databaseLocationIm = DatabasePathForTest.FILESPACE_PATH + "mydbIm";
			
			
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

		

		protected static HashSet<BigInteger> cnst( BigInteger in )
		{
			HashSet<BigInteger> xs = new HashSet<BigInteger>();
			xs.add( in );
			return( xs );
		}
		
		
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		final ComplexElem<DoubleElem,DoubleElemFactory> II = new ComplexElem<DoubleElem,DoubleElemFactory>( dfac.zero() , dfac.identity() );
		
		
		final TestDimensionThree td3 = new TestDimensionThree();
		final GeometricAlgebraOrd<TestDimensionThree> ga3 = new GeometricAlgebraOrd<TestDimensionThree>();
		final GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> g3fac 
			= new  GeometricAlgebraMultivectorElemFactory<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>( dfac, td3, ga3 );
		
		final BigInteger AX = BigInteger.ZERO;
		final BigInteger AY = BigInteger.ONE;
		final BigInteger AZ = BigInteger.valueOf( 2 );
		
		final double AM_SZV = Math.sqrt( 1.0 / 3.0 );
		final DoubleElem AM_SZ = new DoubleElem( AM_SZV );
		
		final HashSet<BigInteger> axs = cnst( AX );
		final HashSet<BigInteger> ays = cnst( AY );
		final HashSet<BigInteger> azs = cnst( AZ );
		

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
		
		
		protected GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> mapPt(
				GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> in ) throws Throwable 
		{
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ix = g3fac.zero();
			ix.setVal( axs , dfac.identity() );
			ix = ix.invertLeft();
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> iy = g3fac.zero();
			iy.setVal( axs , new DoubleElem( 1.0 ) );
			iy.setVal( ays , new DoubleElem( 0.28 ) );
			iy.setVal( azs , new DoubleElem( 0.28 ) );
			iy = calcUnit( iy );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> im = ix.mult( iy );
			return( in.mult( im ) );
		}
		
		
		protected double getActValRe( int t, int x, int y, int z )throws Throwable
		{
			final double d0rem = iterArrayRe.get(t-1, x, y, z);
			final double d0imm = iterArrayIm.get(t-1, x, y, z);
			final double d0re = iterArrayRe.get(t, x, y, z);
			final double d0im = iterArrayIm.get(t, x, y, z);
			final double d0rep = iterArrayRe.get(t+1, x, y, z);
			final double d0imp = iterArrayIm.get(t+1, x, y, z);
			
			ComplexElem<DoubleElem,DoubleElemFactory> d0m = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( d0rem ) , new DoubleElem( d0imm ) );
			ComplexElem<DoubleElem,DoubleElemFactory> d0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( d0re ) , new DoubleElem( d0im ) );
			ComplexElem<DoubleElem,DoubleElemFactory> d0p = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( d0rep ) , new DoubleElem( d0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d1 = ( d0p.add( d0.negate() ) ).add( ( d0.add( d0m.negate() ) ).negate() );
			d1 = d1.negate();
			
			ComplexElem<DoubleElem,DoubleElemFactory> d2 = d0.handleOptionalOp( ComplexCmd.CONJUGATE_LEFT , null );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d3 = II.mult( d1 );
			
			return( ( d2.mult( d3 ) ).getRe().getVal() );
		}
		
		
		protected double getActValIm( int t, int x, int y, int z )throws Throwable
		{
			final double d0rem = iterArrayRe.get(t-1, x, y, z);
			final double d0imm = iterArrayIm.get(t-1, x, y, z);
			final double d0re = iterArrayRe.get(t, x, y, z);
			final double d0im = iterArrayIm.get(t, x, y, z);
			final double d0rep = iterArrayRe.get(t+1, x, y, z);
			final double d0imp = iterArrayIm.get(t+1, x, y, z);
			
			ComplexElem<DoubleElem,DoubleElemFactory> d0m = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( d0rem ) , new DoubleElem( d0imm ) );
			ComplexElem<DoubleElem,DoubleElemFactory> d0 = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( d0re ) , new DoubleElem( d0im ) );
			ComplexElem<DoubleElem,DoubleElemFactory> d0p = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( d0rep ) , new DoubleElem( d0imp ) );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d1 = ( d0p.add( d0.negate() ) ).add( ( d0.add( d0m.negate() ) ).negate() );
			d1 = d1.negate();
			
			ComplexElem<DoubleElem,DoubleElemFactory> d2 = d0.handleOptionalOp( ComplexCmd.CONJUGATE_LEFT , null );
			
			ComplexElem<DoubleElem,DoubleElemFactory> d3 = II.mult( d1 );
			
			return( ( d2.mult( d3 ) ).getIm().getVal() );
		}
		
		
		
		
		
		@Override
		protected double getVal( int t, int x, int y, int z )
				throws Throwable {

			final double d0re = getActValRe( t, x, y, z);
			final double d0im = getActValIm( t, x, y, z);
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

			final double d0re = getActValRe( t, x, y, z);
			final double d0im = getActValIm( t, x, y, z);
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
			return( 0 );
		}

		@Override
		protected int getZEnd() {
			return( NUM_Z_ITER );
		}

		@Override
		protected int getYStrt() {
			return( 0 );
		}

		@Override
		protected int getYEnd() {
			return( NUM_Y_ITER );
		}

		@Override
		protected int getXStrt() {
			return( 0 );
		}

		@Override
		protected int getXEnd() {
			return( NUM_X_ITER );
		}
		
		
		
		
		protected int xvv;
		protected int yvv;
		protected int zvv;
		
		
		protected void calcSphLocs( final int yy , final int zz ) throws Throwable
		{
			final int x2ii = NUM_X_ITER - 1;
			final int y2ii = yy;
			final int z2ii = zz;
			
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> ix = g3fac.zero();
			ix.setVal( axs , new DoubleElem( x2ii ) );
			ix.setVal( ays , new DoubleElem( y2ii ) );
			ix.setVal( azs , new DoubleElem( z2ii ) );
			GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> iy = mapPt( ix );
			
			double x2 = iy.getVal( axs ).getVal();
			double y2 = iy.getVal( ays ).getVal();
			double z2 = iy.getVal( azs ).getVal();
			
			final double RAD_X = NUM_X_ITER / 10.0;
			
			final double dmark = RAD_X;
			
			final double HALF_X = NUM_X_ITER / 2.0;
			
			final double x1 = HALF_X;
			final double y1 = HALF_X;
			final double z1 = HALF_X;
			
			
			final double xvi = x2 - x1;
			final double yvi = y2 - y1;
			final double zvi = z2 - z1;
			
			final double vilen = Math.sqrt( xvi * xvi + yvi * yvi + zvi * zvi );
			
			final double xv = xvi / vilen;
			final double yv = yvi / vilen;
			final double zv = zvi / vilen;
			
			
			double dx = dmark * xv;
			double dy = dmark * yv;
			double dz = dmark * zv;
			
			
			int dxi = (int) dx;
			int dyi = (int) dy;
			int dzi = (int) dz;
			
			while( Math.sqrt( dxi * dxi + dyi * dyi + dzi * dzi ) < dmark )
			{
				dx += 0.1 * xv;
				dy += 0.1 * yv;
				dz += 0.1 * zv;
				
				dxi = (int) dx;
				dyi = (int) dy;
				dzi = (int) dz;
			}
			
			
			xvv = (int)( dx + x1 );
			yvv = (int)( dy + y1 );
			zvv = (int)( dz + z1 );
			
			// System.out.println( "%%% " + xvv + " " + yvv + " " + zvv );
		}
		
		
		
		
		
		/**
		 * Calculates the maximum absolute value in the dataset.
		 * @param t The T-index for which to calculate.
		 * 
		 * @throws Throwable
		 */
		@Override
		public double calcMaxAbs( final int x0 , final int t ) throws Throwable
		{
			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			calcSphLocs( Y_STRT , Z_STRT );
			double dd = Math.abs( getVal( t , xvv , yvv , zvv ) );
			
			
			for( int zz = Z_STRT ; zz < Z_END ; zz++ )
			{
				for( int yy = Y_STRT ; yy < Y_END ; yy++ )
				{
					calcSphLocs( yy , zz );
					// System.out.println( "%%% " + xvv + " " + yvv + " " + zvv );
					// System.out.println( getVal( t , xvv , yvv , zvv ) );
					dd = Math.max( dd , Math.abs( getVal( t , xvv , yvv , zvv ) ) );
				}
			}
			
			return( dd );
		}
		
		
		
		public double calcMinAbs( final int t ) throws Throwable
		{
			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			
			double dd = 1E+60;
			
			
			for( int zz = Z_STRT ; zz < Z_END ; zz++ )
			{
				for( int yy = Y_STRT ; yy < Y_END ; yy++ )
				{
					calcSphLocs( yy , zz );
					final double dval = Math.abs( getVal( t , xvv , yvv , zvv ) );
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
		public void writePpm( final int iter , String pathName ) throws Throwable
		{			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			final int X_STRT = getXStrt();
			final int X_END = getXEnd();
			
			
			final double ut = ( (double) iter ) / ( NUM_T_ITER - 1 );
			
			final int t = iter;
			
			final double u0 = 0.50; // 0.40;
			final double u1 = 0.30;
			
			
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
			
			
			double maxVal = this.calcMaxAbs( 0 , t );
			
			double minVal = this.calcMinAbs( t );
				
			System.out.println( ">>>> " + maxVal );
				
			if( maxVal < 1E-30 )
			{
				maxVal = 1.0;
			}
			
			/* getValCplx( 35 , 8 , 8 , 8 , 1.0 , dval );
			
			System.out.println( "q1 " + dval[ 0]  );
			System.out.println( "q2 " + dval[ 1]  ); */
				
			
			for( int zz = Z_STRT ; zz < Z_END ; zz++ )
			{
				for( int yy = Y_STRT ; yy < Y_END ; yy++ )
				{
					// System.out.println( DV );
					calcSphLocs( yy , zz );
					getValCplx( t , xvv , yvv , zvv , minVal , maxVal , dval );
					
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
		
		final int T_OFFSET = 0;
		
		
		final String FILE_PATH_PREFIX = "outDfAnimPpm";
		
		
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
						for( int cnt = core ; cnt < ( T_MAX - 1 ) ; cnt = cnt + numCores )
						{
							System.out.println( cnt );
			
							final int tval = cnt + T_OFFSET;
			
							String filePath = FILE_PATH_PREFIX + cnt + ".ppm";
		
							TstPpmFileWriterAnimT2_sph writer = new TstPpmFileWriterAnimT2_sph( tval );
		
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



