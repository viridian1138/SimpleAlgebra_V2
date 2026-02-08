




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

import simplealgebra.*;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.*;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.*;


/**
 * Writes a query containing an expression string to the Ollama AI, and then reads the result and returns a matching SymbolicElem.
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
public abstract class AiOllamaInteraction<R1 extends Elem<R1,?>, S1 extends ElemFactory<R1,S1>, R2 extends Elem<R2,?>, S2 extends ElemFactory<R2,S2>>
{

	/**
	 * Instance for generating expression strings for Ollama from SymbolicElems.
	 */
	protected AiOllamaWriter<R1,S1> aiOllamaWriter;
	
	/**
	 * Instance for parsing a Tac-like syntax from Ollama result strings into SymbolicElems
	 */
	protected AiOllamaParse<R2,S2> aiOllamaParse;
	
	
	/**
	 * Constructor.
	 * @param _aiOllamaWriter Instance for generating expression strings for Ollama from SymbolicElems.
	 * @param _aiOllamaParse Instance for parsing a Tac-like syntax from Ollama result strings into SymbolicElems
	 */
	public AiOllamaInteraction( AiOllamaWriter<R1,S1> _aiOllamaWriter , AiOllamaParse<R2,S2> _aiOllamaParse )
	{
		aiOllamaWriter = _aiOllamaWriter;
		aiOllamaParse = _aiOllamaParse;
	}
	
	
	/**
	 * Generates an AI command given an input expression
	 * @param in The input expression
	 * @return The output command string
	 * @throws Throwable
	 */
	protected String generateCommand( SymbolicElem<R1, S1> in ) throws Throwable
	{
		String expr = aiOllamaWriter.generateString(in);
				
		return( generateCommand( expr ) );
	}
	
	
	/**
	 * Generates an AI command given an input expression
	 * @param expr The input expression string
	 * @return The output command string
	 * @throws Throwable
	 */
	protected abstract String generateCommand( String expr ) throws Throwable;
	
	
	
	/**
	 * Generates an output expression from the AI given an input expression to the AI.
	 * @param in The input expression to the AI.
	 * @return The output expression from the AI.
	 * @throws Throwable Throws an exception upon an unfixable error.
	 */
	public SymbolicElem<R2,S2> generate( SymbolicElem<R1, S1> in ) throws Throwable
	{
		String cmd = generateCommand( in );
		
		
		ArrayList<String> stdOutArr = new ArrayList<String>();
		
		ArrayList<String> stdErrArr = new ArrayList<String>();
		
		
		
		
		Process proc = Runtime.getRuntime().exec( cmd );
		
		LineNumberReader stdOut = new LineNumberReader( new InputStreamReader( proc.getInputStream() ) );
		
		LineNumberReader stdErr = new LineNumberReader( new InputStreamReader( proc.getErrorStream() ) );
		
		
		String line = stdOut.readLine();
		while( line != null )
		{
			stdOutArr.add( line );
			line = stdOut.readLine();
		}
		
		
		line = stdErr.readLine();
		while( line != null )
		{
			stdErrArr.add( line );
			line = stdErr.readLine();
		}
		
		
		
		ArrayList<String> ar = new ArrayList<String>();
		boolean done = false;
		Iterator<String> it = stdOutArr.iterator();
		while( ( it.hasNext() ) && ( !done ) )
		{
			line = it.next();
			StringTokenizer st = new StringTokenizer( line );
			
			if( st.hasMoreTokens() )
			{
				String token = st.nextToken();
				if( token.equals( "Final:" ) )
				{
					done = true;
					ar.add( line );
				}
				else
				{
					ar.add( line );
				}
			}
		}
		
		if( !done )
		{
			
			System.out.println( "***************** Err ***********************" );
			
			Iterator<String> ita = stdErrArr.iterator();
			while( ita.hasNext() )
			{
				System.out.println( ita.next() );
			}
			
			System.out.println( "***************** Out ***********************" );
			
			ita = stdOutArr.iterator();
			while( ita.hasNext() )
			{
				System.out.println( ita.next() );
			}
			
			throw( new RuntimeException( "Failed To Parse" ) );
		}
		
		
		
		SymbolicElem<R2,S2> ret = aiOllamaParse.genParsedExpr( ar );
		
		return( ret );
	}
	
	
	
}


