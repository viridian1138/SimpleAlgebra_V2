



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




package simplealgebra.constants;





/**
 * Information about the local machine's CPU(s).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 * 
 */
public class CpuInfo {
	
	
	/**
	 * The number of accessible cores on the local machine's CPU(s).
	 */
	public static final int NUM_CPU_CORES = 2;

	
	/**
	 * Returns a boolean arrayed to the number of cores.
	 * 
	 * @param val The value to whicch to set the booleans.
	 * @return The returned array.
	 */
	public static boolean[] createBool( final boolean val )
	{
		final int numCores = NUM_CPU_CORES;
		final boolean[] ret = new boolean[ numCores ];
		int count;
		for( count = 0 ; count < numCores ; count++ )
		{
			ret[ count ] = val;
		}
		return( ret );
	}
	
	
	/**
	 * Starts a runnable for each core.
	 * It is presumed that the OS will symmetrically distribute the execution across the cores.
	 * 
	 * @param r The array of runnables.
	 */
	public static void start( final Runnable[] r )
	{
		final int numCores = NUM_CPU_CORES;
		int count;
		for( count = 0 ; count < numCores ; count++ )
		{
			( new Thread( r[ count ] ) ).start();
		}
	}
	
	
	/**
	 * Waits until all cores are complete.
	 * 
	 * @param runn The runnables to complete.
	 * @param fini Booleans indicating whether the runnables are complete.
	 * @throws Throwable
	 */
	public static void wait( final Runnable[] runn, final boolean[] fini ) throws Throwable
	{
		final int numCores = NUM_CPU_CORES;
		int count;
		for( count = 0 ; count < numCores ; count++ )
		{
			if( !( fini[ count ] ) )
			{
				synchronized( runn[ count ] )
				{
					while( !( fini[ count ] ) )
					{
						( runn[ count ] ).wait();
					}
				}
			}
		}
	}
	
	
}


