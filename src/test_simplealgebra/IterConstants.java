




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


/**
 * Constants controlling the number of iterations for some large tests.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class IterConstants
{
	
	/**
	 * Controls whether to use larger iteration constants.
	 */
	public static final boolean LRG_ITER_SW = false;
	
	/**
	 * Controls whether to use a larger T-Axis iteration count.
	 */
	public static final boolean USE_LARGER_LRG_T = true;
	
	/**
	 * The number of discretizations on the X-Axis over which to iterate.
	 */
	public static final int LRG_ITER_X = LRG_ITER_SW ? 200 : 20;
	
	/**
	 * The number of discretizations on the Y-Axis over which to iterate.
	 */
	public static final int LRG_ITER_Y = LRG_ITER_SW ? 200 : 20;
	
	/**
	 * The number of discretizations on the Z-Axis over which to iterate.
	 */
	public static final int LRG_ITER_Z = LRG_ITER_SW ? 200 : 20;
	
	/**
	 * The number of discretizations on the T-Axis over which to iterate.
	 */
	public static final int LRG_ITER_T = LRG_ITER_SW ? 
			( USE_LARGER_LRG_T ? 400 : 200 ) : 40; // 400
	
			
}



