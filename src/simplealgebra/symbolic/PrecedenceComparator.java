






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





package simplealgebra.symbolic;


import java.io.PrintStream;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;


/**
 * A description of how to assign precedence for converting to infix notation.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 */
public abstract class PrecedenceComparator extends Object {
	
	
	/**
	 * Returns true iff. a parenthesis is required to convert to infix.
	 * @param a The parent node in the elem tree.
	 * @param b The child node in the elem tree.
	 * @param after Whether the child is written after the parent in infix notation.
	 * @return Whether infix notation requires a parenthesis for the child.
	 */
	public abstract boolean parenNeeded( Elem<?,?> a , Elem<?,?> b , boolean after );
	
	
	/**
	 * Gets the parenthesis generator.
	 * 
	 * @return The parenthesis generator.
	 */
	public abstract ParenthesisGenerator getParenthesisGenerator();
	

	/**
	 * Handles MathML ( http://www.w3.org/Math/ ) presentations that are not supported.
	 * 
	 * @param elem The unsupported element.
	 */
	public void handleUnimplementedElem( Elem<?, ?> elem , PrintStream ps )
	{
		System.out.println( "Not Supported : " + ( elem.getClass().getName() ) );
		throw( new RuntimeException( "NotSupported" ) );
	}
	
}


