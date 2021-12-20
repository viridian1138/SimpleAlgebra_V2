




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






package simplealgebra.culang;

import java.io.PrintStream;

/**
 * Interface for writing source directly into Cuda code generation for complex numbers.
 * 
 * @author tgreen
 *
 */
public interface CuGen_Cplx_Dbl {
	
	/**
	 * Writes source directly into Cuda code generation.
	 * @param tnameRe The temporary variable name to be assigned to for the real part of the result.
	 * @param tnameIm The temporary variable name to be assigned to for the imaginary part of the result.
	 * @param ps The stream to which to write the source code.
	 */
	public void genCuda( final String tnameRe , final String tnameIm , final PrintStream ps );

}


