


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



/**
 * Constants used to configure culang compilation for a particular machine.
 * 
 * @author tgreen
 *
 */
public class CulangConstants {


	/**
	 * Command to compile the culang source code.  Modify this to suit the OS, OS version, and compiler on the destination machine.
	 */
	public static final String CULANG_NATIVE_COMPILATION_COMMAND = "g++ ";

	/**
	 * Tag to output culang-compiled code.  Modify this to suit the OS, OS version, and compiler on the destination machine.
	 */
	public static final String CULANG_NATIVE_LINK_OUTPUT = " -o ";

	/**
	 * Tag to output culang-compiled code.  Modify this to suit the OS, OS version, and compiler on the destination machine.
	 */
	public static final String CULANG_NATIVE_LINK_OUTPUT2 = " -I/usr/local/cuda/include -L/usr/local/cuda/lib64 -lnvrtc -lnvrtc-builtins -lcuda -lpthread -lm";
	
	
}



