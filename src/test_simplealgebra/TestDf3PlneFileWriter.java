



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
public class TestDf3PlneFileWriter extends TestCase {
	
	
	
	
	
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
	protected static class TstDf3PlneFileWriter extends RawFileWriter
	{

		/**
		 * Boolean indicating whether to subract a center value.
		 */
		protected static final boolean USE_CENTER = true; // true;
		
		/**
		 * The center value to potentially be subtracted.
		 */
		protected static final double CENTER_VALUE = 0.05; // 0.0;

		
		
		
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
		public TstDf3PlneFileWriter() throws Throwable
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
			return( d1 / maxVal );
		}

		@Override
		protected int getTStrt() {
			return( 197 /* 165 */ /* 123 */ /* 42 */ /* 61 */ /* 41 */ /* 197 */ /* 123 */ /*61 */ );
		}

		@Override
		protected int getTEnd() {
			return( 198 /* 166 */ /* 124 */ /* 43 */ /* 62 */ /* 42 */ /* 198 */ /* 124 */ /* 62 */ );
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
		 * Writes DF3 data to a pair of files.
		 * 
		 * @param pathNamePl The path to the file containing positive values.
		 * @param pathNameNe The path to the file containing negative values.
		 * @throws Throwable
		 */
		public void writeDf3( String pathNamePl , String pathNameNe ) throws Throwable
		{
			final int T_STRT = getTStrt();
			final int T_END = getTEnd();
			
			final int Z_STRT = getZStrt();
			final int Z_END = getZEnd();
			
			final int Y_STRT = getYStrt();
			final int Y_END = getYEnd();
			
			final int X_STRT = getXStrt();
			final int X_END = getXEnd();
			
			
			System.out.println( "Starting calcMax" );
			
			
			maxVal = this.calcMaxAbs();
			
			
			System.out.println( "Ended calcMax" );
			
			System.out.println( maxVal );
			
			
			if( maxVal < 1E-30 )
			{
				maxVal = 1.0;
			}
			
			
			final long DV = 1L << 31;
			
			
			
			final FileOutputStream foPl = new FileOutputStream( pathNamePl );
			final BufferedOutputStream boPl = new BufferedOutputStream( foPl , bufferSize() );
			final DataOutputStream dsPl = new DataOutputStream( boPl );
			
			final FileOutputStream foNe = new FileOutputStream( pathNameNe );
			final BufferedOutputStream boNe = new BufferedOutputStream( foNe , bufferSize() );
			final DataOutputStream dsNe = new DataOutputStream( boNe );
			
			
			
			
			dsPl.writeShort( X_END - X_STRT );
			
			dsPl.writeShort( Y_END - Y_STRT );
			
			dsPl.writeShort( Z_END - Z_STRT );
			
			
			dsNe.writeShort( X_END - X_STRT );
			
			dsNe.writeShort( Y_END - Y_STRT );
			
			dsNe.writeShort( Z_END - Z_STRT );
			
			
			
			
			for( int t = T_STRT ; t < T_END ; t++ )
			{
				for( int z = Z_STRT ; z < Z_END ; z++ )
				{
					for( int y = Y_STRT ; y < Y_END ; y++ )
					{
						for( int x = X_STRT ; x < X_END ; x++ )
						{
							// System.out.println( DV );
							long val = (long)( DV * ( getVal( t , x , y , z ) ) );
							if( val < 0.0 )
							{
								dsNe.writeInt( (int)( -val ) );
								dsPl.writeInt( 0 );
							}
							else
							{
								dsNe.writeInt( 0 );
								dsPl.writeInt( (int)( val ) );
							}
							
						}
					}
				}
			}
			
			iterArray.close();
			dsPl.close();
			dsNe.close();
			
		}
		
		
	}
	
	
	
	/**
	 * Simple test of the RawFileWriter class.
	 * 
	 * @throws Throwable
	 */
	public void testDf3PlneFileWriter() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String filePathPl = "outDfPl.df3";
		
		String filePathNe = "outDfNe.df3";
		
		TstDf3PlneFileWriter writer = new TstDf3PlneFileWriter();
		
		writer.writeDf3( filePathPl , filePathNe );
		
	}
	

	

	
}



