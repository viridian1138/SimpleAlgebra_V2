

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


/**
 * Classes for converting some symbolic expressions to Jlang (Java Language) bytecode
 * for execution-time performance optimization.
 * <p>
 * Bytecode conversion can produce benefits 
 * by eliminating object creation and eliminating Elem-to-Elem
 * method calls. It is presumed that if object creation cannot be eliminated, then
 * there probably isn't a huge benefit to only eliminating individual Elem-to-Elem
 * method calls.  As a result, the implementation will focus on cases where object
 * creation can be eliminated by converting DoubleElem to a primitive Jlang type.
 * <p>
 * Rules:
 * <p>
 * SymbolicReduction: Is is presumed for the purposes of compilation that SymbolicReductions
 * have constant values, and will not change once Jlang conversion is initiated.  To use a
 * SymbolicElem that can change values after Jlang conversion and have the changes reflected, 
 * subclass SymbolicElem directly instead of subclassing SymbolicReduction.
 * <p>
 * SymbolicDivideBy: It ia presumed that there will be no attempts to divide by values
 * larger than a signed 64-bit number.  Othwerwise, compilation will fail.
 * <p>
 * @author tgreen
 *
 */
package simplealgebra.jlang;

