



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
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.DbFastArray4D_Param;
import simplealgebra.store.DrFastArray4D_Tensor44_Dbl;
import simplealgebra.store.RawFileWriter;



/**  
 * Simple test of the RawFileWriter class.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDf3TensorPlneCutFileWriterAnim extends TestCase {
	
	
	
	
	/**
	 * The number of discretizations on the T-Axis.
	 */
	protected static final int NUM_T_ITER = 9;
	
	/**
	 * The number of discretizations on the X-Axis.
	 */
	protected static final int NUM_X_ITER = 16;
	
	/**
	 * The number of discretizations on the Y-Axis.
	 */
	protected static final int NUM_Y_ITER = 16;
	
	/**
	 * The number of discretizations on the Z-Axis.
	 */
	protected static final int NUM_Z_ITER = 16;
	
	
	
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
	 * Sample writer to test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected static class TstDf3FileWriterAnim extends RawFileWriter
	{

		
		/**
		 * Boolean indicating whether to subract a center value.
		 */
		protected static final boolean USE_CENTER = true; // true;
		
		/**
		 * The center value to potentially be subtracted.
		 */
		protected static final double CENTER_VALUE = -0.004 * 0.004; // 0.0;
		
		
		

		
		
		/**
		 * The T-Axis cell size.
		 */
		protected static final int TMULT = 4;
		
		/**
		 * The X-Axis cell size.
		 */
		protected static final int XMULT = 4;
		
		/**
		 * The Y-Axis cell size.
		 */
		protected static final int YMULT = 4;
		
		/**
		 * The Z-Axis cell size.
		 */
		protected static final int ZMULT = 4;
		
		
		
		/**
		 * The vector for the center point.
		 */
		protected final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			CNTR_OFFSET = convToVector( 0.5 * NUM_X_ITER , 0.5 * NUM_Y_ITER , 0.5 * NUM_Z_ITER ).negate();
		
		
		/**
		 * The vector for the viewer position.
		 */
		protected final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
			VIEWER_DIR = calcUnit( convToVector( 2.0 , 1.0 , 0.0 ) );
		
		
		
		protected final double DOT_PRODUCT_MIN_COSINE =
			Math.cos( Math.toRadians( 60.0 ) );
		
		
		
		/**
		 * Result array over which to get data.
		 */
		protected DrFastArray4D_Tensor44_Dbl iterArray = null;
		
		
		/**
		 * The T-Axis value of the array to write.
		 */
		protected int tval;
		
		
		/**
		 * The calculated maximum value in the file.  Initially 1.0 until the actual max is caucluated.
		 */
		protected double maxVal = 1.0;
		
		
		/**
		 * The unit vector to the viewer.
		 */
		protected ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>
			unitToViewerArg;
		
		
		
		
		/**
		 * Constructs the writer.
		 * 
		 * @param _tval The T-Axis value at which to perform the write.
		 * @throws Throwable
		 */
		public TstDf3FileWriterAnim( final int _tval ) throws Throwable
		{
			tval = _tval;
			
			
			final DoubleElemFactory de = new DoubleElemFactory();
			
			final ArrayList<String> contravariantIndices = new ArrayList<String>();
			final ArrayList<String> covariantIndices = new ArrayList<String>();
			covariantIndices.add( "u" );
			covariantIndices.add( "v" );
			
			
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
			
			iterArray = new DrFastArray4D_Tensor44_Dbl( dparam , de, 
					(ArrayList<BigInteger>)(contravariantIndices.clone()), 
					(ArrayList<BigInteger>)(covariantIndices.clone()), 
					databaseLocation );
			
			final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory> 
				unitToViewer = calcUnit( VIEWER_DIR );
		
			unitToViewerArg = new ArrayList<GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>>();
			
			unitToViewerArg.add( unitToViewer );
			
			
		}

		
		
		@Override
		protected int bufferSize() {
			return( 8000 );
		}

		@Override
		protected double getVal( int t, int x, int y, int z)
				throws Throwable {
			try
			{
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					posOffset = convToVector( x , y , z ).add( CNTR_OFFSET );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					unitOffset = calcUnit( posOffset );
				final GeometricAlgebraMultivectorElem<TestDimensionThree,GeometricAlgebraOrd<TestDimensionThree>,DoubleElem,DoubleElemFactory>
					dotp = unitOffset.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , unitToViewerArg );
				final DoubleElem dotResult = dotp.get( new HashSet<BigInteger>() );
				if( dotResult.getVal() < DOT_PRODUCT_MIN_COSINE )
				{
					final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>
						dret = iterArray.get(t, x, y, z);
					final ArrayList<BigInteger> index = new ArrayList<BigInteger>();
					index.add( BigInteger.ZERO );
					index.add( BigInteger.ZERO );
					final DoubleElem d0a = dret.getVal( index );
					final double d0 = d0a.getVal();
					final double d1 = USE_CENTER ? d0 - CENTER_VALUE : d0;
					return( d1 / maxVal );
				}
				else
				{
					return( 0.0 );
				}
			}
			catch( NotInvertibleException ex )
			{
				return( 0.0 );
			}
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
	public void testDf3FileWriter() throws Throwable
	{
		final int numCores = CpuInfo.NUM_CPU_CORES;
		final Runnable[] runn = new Runnable[ numCores ];
		final boolean[] b = CpuInfo.createBool( false );
		
		final int T_MAX = NUM_T_ITER;
		
		final int T_OFFSET = 0;
		
		
		final String FILE_PATH_PREFIX_PL = "outDfAnimPl";
		
		final String FILE_PATH_PREFIX_NE = "outDfAnimNe";
		
		
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
							System.out.println( cnt );
			
							final int tval = cnt + T_OFFSET;
			
							String filePathPl = FILE_PATH_PREFIX_PL + cnt + ".df3";
			
							String filePathNe = FILE_PATH_PREFIX_NE + cnt + ".df3";
		
							TstDf3FileWriterAnim writer = new TstDf3FileWriterAnim( tval );
		
							writer.writeDf3( filePathPl , filePathNe );
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



