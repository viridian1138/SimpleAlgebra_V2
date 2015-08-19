



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

import junit.framework.TestCase;
import simplealgebra.constants.CpuInfo;
import simplealgebra.store.DbFastArray4D_Param;
import simplealgebra.store.DrFastArray4D_Dbl;
import simplealgebra.store.RawFileWriter;



/**  
 * Simple test of the RawFileWriter class.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestRawFileWriterAnim extends TestCase {
	
	
	
	/**
	 * Sample writer to test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class TstRawFileWriterAnim extends RawFileWriter
	{

		/**
		 * Boolean indicating whether to subract a center value.
		 */
		protected static final boolean USE_CENTER = true;
		
		/**
		 * The center value to potentially be subtracted.
		 */
		protected static final double CENTER_VALUE = 0.0;
		
		/**
		 * Boolean indicating whether to output the absolute value of the input quantities.
		 */
		protected static final boolean USE_ABSOLUTE_VALUE = true;
		
		
		
		/**
		 * The number of discretizations on the T-Axis.
		 */
		protected static final int NUM_T_ITER = 400;
		
		/**
		 * The number of discretizations on the X-Axis.
		 */
		protected static final int NUM_X_ITER = 200;
		
		/**
		 * The number of discretizations on the Y-Axis.
		 */
		protected static final int NUM_Y_ITER = 200;
		
		/**
		 * The number of discretizations on the Z-Axis.
		 */
		protected static final int NUM_Z_ITER = 200;
		
		
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
		 * Result array over which to get data.
		 */
		protected DrFastArray4D_Dbl iterArray = null;
		
		
		/**
		 * The T-Axis value of the array to write.
		 */
		protected int tval;
		
		
		
		/**
		 * Constructs the writer.
		 * 
		 * @param _tval The T-Axis value at which to perform the write.
		 * @throws Throwable
		 */
		public TstRawFileWriterAnim( final int _tval ) throws Throwable
		{
			tval = _tval;
			
			String databaseLocation = "mydbJ";
			
			final DbFastArray4D_Param dparam = new DbFastArray4D_Param();
			dparam.setTmult( TMULT );
			dparam.setXmult( XMULT );
			dparam.setYmult( YMULT );
			dparam.setZmult( ZMULT );
			dparam.setTmax( NUM_T_ITER );
			dparam.setXmax( NUM_X_ITER );
			dparam.setYmax( NUM_Y_ITER );
			dparam.setZmax( NUM_Z_ITER );
			
			iterArray = new DrFastArray4D_Dbl( dparam , databaseLocation );
		}

		
		
		@Override
		protected int bufferSize() {
			return( 8000 );
		}

		@Override
		protected double getVal( int t, int x, int y, int z)
				throws Throwable {
			final double d0 = iterArray.get(t, x, y, z);
			final double d1 = USE_CENTER ? d0 - CENTER_VALUE : d0;
			final double d2 = USE_ABSOLUTE_VALUE ? Math.abs( d1 ) : d1;
			return( d2 );
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
			return( 200 );
		}

		@Override
		protected int getYStrt() {
			return( 0 );
		}

		@Override
		protected int getYEnd() {
			return( 100 );
		}

		@Override
		protected int getXStrt() {
			return( 0 );
		}

		@Override
		protected int getXEnd() {
			return( 200 );
		}
		
		
		@Override
		public void writeDouble( String pathName ) throws Throwable
		{
			super.writeDouble( pathName );
			iterArray.close();
		}
		
		
		@Override
		public void writeFloat( String pathName ) throws Throwable
		{
			super.writeFloat( pathName );
			iterArray.close();
		}
		
		
	}
	
	
	
	/**
	 * Simple test of the RawFileWriter class.
	 * 
	 * @throws Throwable
	 */
	public void testRawFileWriter() throws Throwable
	{
		final int numCores = CpuInfo.NUM_CPU_CORES;
		final Runnable[] runn = new Runnable[ numCores ];
		final boolean[] b = CpuInfo.createBool( false );
		
		final int T_MAX = 200;
		
		final int T_OFFSET = 0;
		
		final String FILE_PATH_PREFIX = "outRawAnim";
		
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
						for( int cnt = core ; cnt < T_MAX ; cnt = cnt + numCores )
						{
							final int tval = cnt + T_OFFSET;
			
							String filePath = FILE_PATH_PREFIX + cnt + ".raw";
		
							TstRawFileWriterAnim writer = new TstRawFileWriterAnim( tval );
		
							writer.writeDouble( filePath );
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



