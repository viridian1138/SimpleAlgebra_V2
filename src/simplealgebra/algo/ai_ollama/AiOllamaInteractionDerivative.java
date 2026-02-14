


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
 * Writes a derivative query containing an expression string to the Ollama AI, and then reads the result and returns a matching SymbolicElem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 * @param <R1> The enclosed type for the input SymbolicElems.
 * @param <S1> The factory for the enclosed type for the input SymbolicElems.
 * @param <R2> The enclosed type for the output SymbolicElems.
 * @param <S2> The factory for the enclosed type for the output SymbolicElems.
 */
public class AiOllamaInteractionDerivative<R1 extends Elem<R1,?>, S1 extends ElemFactory<R1,S1>, R2 extends Elem<R2,?>, S2 extends ElemFactory<R2,S2>> extends AiOllamaInteraction<R1, S1, R2, S2> {
	
	/**
	 * The variable with respect to which to take the derivative.  To be compatible with AI training, this should be a single-character name.
	 */
	protected String wrt = "x";

	/**
	 * Constructor.
	 * @param _aiOllamaWriter Instance for generating expression strings for Ollama from SymbolicElems.
	 * @param _aiOllamaParse Instance for parsing a Tac-like syntax from Ollama result strings into SymbolicElems
	 */
	public AiOllamaInteractionDerivative(AiOllamaWriter<R1, S1> _aiOllamaWriter, AiOllamaParse<R2, S2> _aiOllamaParse) {
		super(_aiOllamaWriter, _aiOllamaParse);
	}
	
	/**
	 * Generates an output expression from the AI given an input expression to the AI.
	 * @param in The input expression to the AI.
	 * @param _wrt The variable with respect to which to take the derivative.  To be compatible with AI training, this should be a single-character name.
	 * @return The output expression from the AI.
	 * @throws Throwable Throws an exception upon an unfixable error.
	 */
	public SymbolicElem<R2,S2> generate( SymbolicElem<R1, S1> in , String _wrt ) throws Throwable
	{
		wrt = _wrt;
		return( produce( in ) );
	}

	@Override
	protected String generateCommand(String expr) throws Throwable {
		String cmdPath = AiResourceLocator.locateResourceAsFile( "AiSimpleDerivative.py" );
		String cmdX2 = "python" + " " + cmdPath + " --expr \"" + expr + "\"" + " --wrt \"" + wrt + "\"";
		String cmd = cmdX2;
		System.out.println( cmd );
		return( cmd );
	}

	
}


