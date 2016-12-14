



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

import junit.framework.TestCase;
import simplealgebra.store.DbFastArray4D_Param;
import simplealgebra.store.DrFastArray4D_Dbl;
import simplealgebra.store.RawFileWriter;



/**  
 * Simple test of the RawFileWriter class for DF3.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDf3FileWriter extends TestCase {
	
	
	
	
	
	/**
	 * The number of discretizations on the T-Axis.
	 */
	protected static final int NUM_T_ITER = IterConstants.LRG_ITER_T;
	
	/**
	 * The number of discretizations on the X-Axis.
	 */
	protected static final int NUM_X_ITER = IterConstants.LRG_ITER_X;
	
	/**
	 * The number of discretizations on the Y-Axis.
	 */
	protected static final int NUM_Y_ITER = IterConstants.LRG_ITER_Y;
	
	/**
	 * The number of discretizations on the Z-Axis.
	 */
	protected static final int NUM_Z_ITER = IterConstants.LRG_ITER_Z;
	

	
	

	
	
	/**
	 * Sample writer to test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TstDf3FileWriter extends RawFileWriter
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
		 * The calculated maximum value in the file.  Initially 1.0 until the actual max is caucluated.
		 */
		protected double maxVal = 1.0;
		
		
		
		/**
		 * Constructs the writer.
		 * @throws Throwable
		 */
		public TstDf3FileWriter() throws Throwable
		{
			String databaseLocation = DatabasePathForTest.FILESPACE_PATH + "mydbJ";
			
			
			
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
			return( d2 / maxVal );
		}

		@Override
		protected int getTStrt() {
			return( 13 );
		}

		@Override
		protected int getTEnd() {
			return( 14 );
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
			return( NUM_Y_ITER / 2 );
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
		 * Writes DF3 data to a file.
		 * 
		 * @param pathName The path to the file.
		 * @throws Throwable
		 */
		public void writeDf3( String pathName ) throws Throwable
		{
			final int T_STRT = getTStrt();
			final int T_END = getTEnd();
			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			final int X_STRT = getXStrt();
			final int X_END = getXEnd();
			
			
			maxVal = this.calcMax();
			
			if( maxVal < 1E-30 )
			{
				maxVal = 1.0;
			}
			
			
			final long DV = 1L << 31;
			
			
			
			final FileOutputStream fo = new FileOutputStream( pathName );
			final BufferedOutputStream bo = new BufferedOutputStream( fo , bufferSize() );
			final DataOutputStream ds = new DataOutputStream( bo );
			
			
			ds.writeShort( X_END - X_STRT );
			
			ds.writeShort( Y_END - Y_STRT );
			
			ds.writeShort( Z_END - Z_STRT );
			
			
			
			
			for( int t = T_STRT ; t < T_END ; t++ )
			{
				for( int z = Z_STRT ; z < Z_END ; z++ )
				{
					for( int y = Y_STRT ; y < Y_END ; y++ )
					{
						for( int x = X_STRT ; x < X_END ; x++ )
						{
							System.out.println( DV );
							long val = (long)( DV * ( getVal( t , x , y , z ) ) );
							ds.writeInt( (int) val );
						}
					}
				}
			}
			
			iterArray.close();
			ds.close();
			
		}
		
		
	}
	
	
	
	/**
	 * Simple test of the RawFileWriter class.
	 * 
	 * @throws Throwable
	 */
	public void testDf3FileWriter() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String filePath = "outDf.df3";
		
		TstDf3FileWriter writer = new TstDf3FileWriter();
		
		writer.writeDf3( filePath );
		
	}
	

	

	
}



