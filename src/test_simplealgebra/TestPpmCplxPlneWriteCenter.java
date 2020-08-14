



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
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.Sqrt;
import simplealgebra.constants.CpuInfo;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.DbFastArray4D_Param;
import simplealgebra.store.DrFastArray4D_Dbl;
import simplealgebra.store.RawFileWriter;



/**  
 * Simple test of the RawFileWriter class for complex values.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * Writes the value at the center for each time.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestPpmCplxPlneWriteCenter extends TestCase {
	
	
	
	
	
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
	protected static class TstPpmFileWriterAnim extends RawFileWriter
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
		public TstPpmFileWriterAnim( final int _tval ) throws Throwable
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

		@Override
		protected double getVal( int t, int x, int y, int z )
				throws Throwable {

			final double d0re = iterArrayRe.get(t, x, y, z);
			final double d0im = iterArrayIm.get(t, x, y, z);
			final double d0r = Math.sqrt( d0re * d0re + d0im * d0im );
			
			if( ( d0r / maxVal ) < 1E-30 )
			{
				return( 0.0 );
			}
					
			return( Math.log10( d0r / maxVal ) + 30.0 );
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
		
		
		
		/**
		 * Writes PPM to a file.
		 * 
		 * @param pathName The path to the output file containing positive values.
		 * @throws Throwable
		 */
		public void writePpm( final int t ) throws Throwable
		{			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			final int X_STRT = getXStrt();
			final int X_END = getXEnd();
			
			
			final int x = ( X_STRT + X_END ) / 2;
			final int y = ( Y_STRT + Y_END ) / 2;
			final int z = ( Z_STRT + Z_END ) / 2;
			
			
			System.out.println( iterArrayRe.get(t, x, y, z) + "  //  " + iterArrayIm.get(t, x, y, z) );
			System.out.println( ( iterArrayRe.get(t+1, x, y, z) - iterArrayRe.get(t, x, y, z) ) + "  //  " + ( iterArrayIm.get(t+1, x, y, z) - iterArrayIm.get(t, x, y, z) ) );
			
		}
		
		
	}
	
	
	
	/**
	 * Simple test of the RawFileWriter class.
	 * 
	 * @throws Throwable
	 */
	public void testDf3FileWriter() throws Throwable
	{
		
		final int T_MAX = NUM_T_ITER;
		
		final int T_OFFSET = 0;
		
		
		final String FILE_PATH_PREFIX = "outDfAnimPpm";
		
		
		
		for( int cnt = 0 ; cnt < ( T_MAX - 1 ) ; cnt++ )
		{
			System.out.println( cnt );
			
			final int tval = cnt + T_OFFSET;
		
			TstPpmFileWriterAnim writer = new TstPpmFileWriterAnim( tval );
		
			writer.writePpm( tval );
		}
		
		
		
	}
	

	

	
}



