


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
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Writes a simplification query containing an expression string to the Ollama AI, and then reads the result and returns a matching SymbolicElem.
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
public class AiOllamaInteractionSimplification<R1 extends Elem<R1,?>, S1 extends ElemFactory<R1,S1>, R2 extends Elem<R2,?>, S2 extends ElemFactory<R2,S2>> extends AiOllamaInteraction<R1, S1, R2, S2> {
	
	/**
	 * Constructor.
	 * @param _aiOllamaWriter Instance for generating expression strings for Ollama from SymbolicElems.
	 * @param _aiOllamaParse Instance for parsing a Tac-like syntax from Ollama result strings into SymbolicElems
	 */
	public AiOllamaInteractionSimplification(AiOllamaWriter<R1, S1> _aiOllamaWriter, AiOllamaParse<R2, S2> _aiOllamaParse) {
		super(_aiOllamaWriter, _aiOllamaParse);
	}
	
	/**
	 * The factory for producing instances of the input type.
	 */
	protected SymbolicElemFactory<R1, S1> fac = null;
	
	
	/**
	 * Generates an output expression from the AI given an input expression to the AI.
	 * @param in The input expression to the AI.
	 * @return The output expression from the AI.
	 * @throws Throwable Throws an exception upon an unfixable error.
	 */
	public SymbolicElem<R2,S2> generate( SymbolicElem<R1, S1> in ) throws Throwable
	{
		fac = in.getFac();
		return( produce( in ) );
	}

	@Override
	protected String generateCommand(String expr) throws Throwable {
		String cmdPath = AiResourceLocator.locateResourceAsFile( "AiSimpleSimplification.py" );
		String cmdX2 = "python" + " " + cmdPath + " --expr \"" + expr + "\"";
		if( !( fac.isMultCommutative() ) )
		{
			cmdX2 = cmdX2 + " --nonCommutative 1";
		}
		if( !( fac.isMultAssociative() ) )
		{
			cmdX2 = cmdX2 + " --nonAssociatve 1";
		}
		String cmd = cmdX2;
		System.out.println( cmd );
		return( cmd );
	}

	
}


