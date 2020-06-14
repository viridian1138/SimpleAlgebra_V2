


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




package simplealgebra.clang;



/**
 * Constants used to configure clang compilation for a particular machine.
 * 
 * @author tgreen
 *
 */
public class ClangConstants {


	/**
	 * Classpath to use for Javac and Javah (and maybe in the future "Javac -h") compilation.
	 * Modify this based on where SimpleAlgebra is being built.
	 */
	public static final String CLANG_SIMPLEALGEBRA_CLASSPATH = "/home/SimpleAlgebra/classes:.";

	/**
	 * Command to compile the clang source code.  Modify this to suit the OS, OS version, and compiler on the destination machine.
	 */
	public static final String CLANG_NATIVE_COMPILATION_COMMAND = "g++ -c -fPIC -O3 -I/usr/lib/jvm/java-7-openjdk-amd64/include ";

	/**
	 * Command to link clang-compiled code.  Modify this to suit the OS, OS version, and compiler on the destination machine.
	 */
	public static final String CLANG_NATIVE_LINK_COMMAND = "g++ -shared -fPIC -o ";
	
	/**
	 * Command to run the Javac compiler.
	 */
	public static final String CLANG_JAVAC_COMMAND = "javac -cp ";
	
	/**
	 * Command to run the Javah command (or perhaps "javac -h ." at some point in the future).
	 */
	public static final String CLANG_JAVAH_COMMAND = "javah -cp ";
	
	
}


