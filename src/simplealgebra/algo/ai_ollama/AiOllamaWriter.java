




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






package simplealgebra.algo.ai_ollama;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicElem;

/**
 * Interface for generating a matching expression string for Ollama given a SymbolicElem. 
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 * @param <R1> The enclosed type.
 * @param <S1> The factory for the enclosed type.
 */
public abstract class AiOllamaWriter<R1 extends Elem<R1,?>, S1 extends ElemFactory<R1,S1>> {
	
	/**
	 * Generates a matching expression string for Ollama given a SymbolicElem. 
	 * @param in The input SymbolicElem.
	 * @return The matching expression string.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	public abstract String generateString( SymbolicElem<R1,S1> in ) throws Throwable;

}

