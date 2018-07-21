




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






package test_simplealgebra.run2d_a;



import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Random;

import junit.framework.TestCase;


/**
 * Tests the ability to load text files for a 2-D TensorFlow neural-net model for slope estimation from NNFSA (Neural-Net Generator For Simple Algebra) and evaluates the neural-net for a random input.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestLoad2D extends TestCase {

	

	
	/**
	 * Node representing a TensorFlow-generated neural network.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class NeuralNode
	{
		protected double[][] wvals_h1;
		protected double[][] wvals_h2;
		protected double[][] wvals_h3;
		protected double[][] wvals_h4;
		protected double[][] wvals_out;
	
	
		protected double[] bvals_h1;
		protected double[] bvals_h2;
		protected double[] bvals_h3;
		protected double[] bvals_h4;
		protected double[] bvals_out;
		
		
		protected double[] tvals_h1;
		protected double[] tvals_h2;
		protected double[] tvals_h3;
		protected double[] tvals_h4;
		
		
		/**
		 * Evaluates the neural network.
		 * @param in The neural network input values.
		 * @return The result of evaluationg the neural network.
		 */
		public double eval( final double[] in )
		{
			
			for( int i = 0 ; i < tvals_h1.length ; i++ )
			{
				tvals_h1[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < in.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h1.length ; j++ )
				{
					tvals_h1[ j ] += wvals_h1[ i ][ j ] * in[ i ];
				}
			}
			
			for( int i = 0 ; i < tvals_h1.length ; i++ )
			{
				tvals_h1[ i ] = Math.max( 0.0 , tvals_h1[ i ] + bvals_h1[ i ] );
			}
			
			
			for( int i = 0 ; i < tvals_h2.length ; i++ )
			{
				tvals_h2[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < tvals_h1.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h2.length ; j++ )
				{
					tvals_h2[ j ] += wvals_h2[ i ][ j ] * tvals_h1[ i ];
				}
			}
			
			for( int i = 0 ; i < tvals_h2.length ; i++ )
			{
				tvals_h2[ i ] = Math.max( 0.0 , tvals_h2[ i ] + bvals_h2[ i ] );
			}
			
			
			for( int i = 0 ; i < tvals_h3.length ; i++ )
			{
				tvals_h3[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < tvals_h2.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h3.length ; j++ )
				{
					tvals_h3[ j ] += wvals_h3[ i ][ j ] * tvals_h2[ i ];
				}
			}
			
			
			for( int i = 0 ; i < tvals_h3.length ; i++ )
			{
				tvals_h3[ i ] = Math.max( 0.0 , tvals_h3[ i ] + bvals_h3[ i ] );
			}
			
			
			for( int i = 0 ; i < tvals_h4.length ; i++ )
			{
				tvals_h4[ i ] = 0.0;
			}
			
			
			for( int i = 0 ; i < tvals_h3.length ; i++ )
			{
				for( int j = 0 ; j < tvals_h4.length ; j++ )
				{
					tvals_h4[ j ] += wvals_h4[ i ][ j ] * tvals_h3[ i ];
				}
			}
			
			
			for( int i = 0 ; i < tvals_h4.length ; i++ )
			{
				tvals_h4[ i ] = Math.max( 0.0 , tvals_h4[ i ] + bvals_h4[ i ] );
			}
			
			
			double out = 0.0;
			
			
			
			for( int i = 0 ; i < tvals_h4.length ; i++ )
			{
				out += wvals_out[ i ][ 0 ] * tvals_h4[ i ];
			}
			
			
			
			out = Math.max( 0.0 , out + bvals_out[ 0 ] );
			
			
			
			return( out );
			
			
			
		}
		
		
	}
	
	
	
	
	protected NeuralNode loadNeuralNode( String wfile , String bfile ) throws Throwable
	{
		NeuralNode node = new NeuralNode();
		
		{
			LineNumberReader li = new LineNumberReader( new InputStreamReader( this.getClass().getResourceAsStream( wfile ) ) );
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h1 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h1[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h2 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h2[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h3 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h3[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_h4 = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_h4[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				final int maxJ = Integer.parseInt( li.readLine() );
				
				node.wvals_out = new double[ maxI ][ maxJ ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					for( int j = 0 ; j < maxJ ; j++ )
					{
						node.wvals_out[ i ][ j ] = Double.parseDouble( li.readLine() );
					}
				}
				
			}
			
			

			
		}
		
		
		
		
		
		
		{
			LineNumberReader li = new LineNumberReader( new InputStreamReader( this.getClass().getResourceAsStream( bfile ) ) );
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h1 = new double[ maxI ];
				node.tvals_h1 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h1[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h2 = new double[ maxI ];
				node.tvals_h2 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h2[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h3 = new double[ maxI ];
				node.tvals_h3 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h3[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_h4 = new double[ maxI ];
				node.tvals_h4 = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_h4[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			
			
			
			
			{
				
				li.readLine();
				
				final int maxI = Integer.parseInt( li.readLine() );
				
				node.bvals_out = new double[ maxI ];
				
				for( int i = 0 ; i < maxI ; i++ )
				{
					node.bvals_out[ i ] = Double.parseDouble( li.readLine() );
				}
				
			}
			
			

			
		}
		
		
		
		return( node );
		
	}
	
	
	
	
	protected NeuralNode[][] nodes;

	
	
	
	
	protected void load2D() throws Throwable
	{
		
		
		nodes = new NeuralNode[ MAX_T ][ MAX_X ];
		
		
		final int[] cntrVals = { MAX_T - 1 , ( MAX_X - 1 ) / 2 };
		
		
		
		for( int t = 0 ; t < MAX_T ; t++ )
		{
			final int x = cntrVals[ 1 ];
			if( nodes[ t ][ x ] == null )
			{
				nodes[ t ][ x ] =  loadNeuralNode( "wvals_txt_" + t + "_" + x + "_.txt" , "bvals_txt_" + t + "_" + x + "_.txt" );
			}
		}
		
		
		
		for( int x = 0 ; x < MAX_X ; x++ )
		{
			final int t = cntrVals[ 0 ];
			if( nodes[ t ][ x ] == null )
			{
				nodes[ t ][ x ] =  loadNeuralNode( "wvals_txt_" + t + "_" + x + "_.txt" , "bvals_txt_" + t + "_" + x + "_.txt" );
			}
		}
		
		
		
		/* System.out.println( node.wvals_h1[ 15 ][ 15 ] );
		System.out.println( node.wvals_h2[ 15 ][ 15 ] );
		System.out.println( node.wvals_h3[ 15 ][ 15 ] );
		System.out.println( node.wvals_h4[ 15 ][ 15 ] );
		System.out.println( node.wvals_out[ 15 ][ 0 ] );
		
		System.out.println( node.bvals_h1[ 15 ] );
		System.out.println( node.bvals_h2[ 15 ] );
		System.out.println( node.bvals_h3[ 15 ] );
		System.out.println( node.bvals_h4[ 15 ] );
		System.out.println( node.bvals_out[ 0 ] ); */
		
		
	}
	
	
	
	/**
	 * The maximum of the absolute value of the inputs.
	 */
	protected double absMax = 0.0;
	
	
	
	
	protected void evalNode( final double[] ivals , final int t , final int x , final double[][] out  )
	{
		
		
		System.out.print( out[ t ][ x ] + " // " );
		
		
		if( nodes[ t ][ x ] != null )
		{
			
			final NeuralNode node = nodes[ t ][ x ];
			
			out[ t ][ x ] = node.eval( ivals ) * absMax;
			
		}
		
		
		System.out.println( out[ t ][ x ] );
		
		
	}
	
	
	
	
	
	protected void evalNode( final double[] ivals , final double[][] out  )
	{
		
		
		for( int t = 0 ; t < MAX_T ; t++ )
		{
			for( int x = 0 ; x < MAX_X ; x++ )
			{
				evalNode( ivals , t , x , out  );
			}
		}
		
		
	}
	
	
	
	
	
	
	/**
	 * Tests the ability to load text files for a 2-D TensorFlow neural-net model for slope estimation from NNFSA (Neural-Net Generator For Simple Algebra) and evaluates the neural-net for a random input.
	 * 
	 * @throws Throwable
	 */
	public void testRun2D() throws Throwable 
	{
		
		load2D();
		
		
		final Random rand = new Random( 2345 );
		
		
		
		final double[][] out = new double[ MAX_T ][ MAX_X ];
		final double[] ivals = new double[ ( MAX_T - 1 ) * MAX_X ];
		
		
		for( int t = 0 ; t < ( MAX_T - 1 ) ; t++ )
		{
			for( int x = 0 ; x < MAX_X ; x++ )
			{
				out[ t ][ x ] = rand.nextDouble();
				ivals[ MAX_X * t + x ] = out[ t ][ x ];
				absMax = Math.max( absMax ,  Math.abs( ivals[ MAX_X * t + x ] ) );
			}
		}
		
		
		for( int i = 0 ; i < ivals.length ; i++ )
		{
			ivals[ i ] = ivals[ i ] / absMax;
		}
		
		
		if( absMax < 1E-30 )
		{
			absMax = 1E-30;
		}
		
		
		
		for( int x = 0 ; x < MAX_X ; x++ )
		{
			out[ MAX_T - 1 ][ x ] = out[ MAX_T - 2 ][ x ];
		}
		
		
		
		evalNode( ivals , out  );
		
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Maximum size of the evaluation cell along the T-axis.
	 */
	static final int MAX_T = 5;
	
	/**
	 * Maximum size of the evaluation cell along the X-axis.
	 */
	static final int MAX_X = 5;
	
	
	

	
	
	
	
	

}




