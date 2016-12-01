







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





package simplealgebra.prec;


import java.io.PrintStream;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.ParenthesisGenerator;


/**
 * Simple class for W3C MathML ( <A href="http://www.w3.org/Math/">http://www.w3.org/Math/</A> ) parenthesis generation.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class SimpleParenthesisGenerator extends ParenthesisGenerator 
{
	

	
	@Override
	public void handleParenthesisOpen( PrintStream ps )
	{
		ps.print( "<mfenced><mrow>" );
	}
	
	@Override
	public void handleParenthesisClose( PrintStream ps )
	{
		ps.print( "</mrow></mfenced>" );
	}
	
	
}


