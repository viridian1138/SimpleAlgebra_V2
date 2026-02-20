




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

import java.util.*;


/**
 * Takes in Tac-like output from an Ollama AI interaction and parses it into a matching SymbolicElem.
 * 
 * Input text format is generally similar to Tac (Three Address Code), but may have more than three addresses.
 * 
 * Ollama AI sends an infix SymPy expression to the calling Python script.  The Python script then converts the expression from infix to something Tac-like before generating a result.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 * @param <R2> The enclosed type.
 * @param <S2> The factory for the enclosed type.
 */
public abstract class AiOllamaParse<R2 extends Elem<R2,?>, S2 extends ElemFactory<R2,S2>>
{


	/**
	 * Interface for mapping a particular collection of Tac-like tokens from a text line to a SymbolicElem
	 * @author tgreen
	 *
	 */
	protected abstract class SymHandler
	{
		/**
		 * Interface for mapping a particular collection of Tac-like tokens from a text line to a SymbolicElem
		 * @param st The input tokenizer of the tokens to be parsed.
		 * @return The matching SymbolicElem.
		 * @throws Throwable Throws an exception upon parsing entries that are incompatible with the algebra in the SymbolicElemFactory.  For instance a use of "i" when the SymbolicElemFactory only has reals.
		 */
		public abstract SymbolicElem<R2,S2> hndl( StringTokenizer st ) throws Throwable;
	}
	
	
	
	/**
	 * Interface for mapping a particular collection of Tac-like tokens from a text line to a SymbolicElem
	 * @author tgreen
	 *
	 */
	protected abstract class ConstHandler
	{
		/**
		 * Interface for mapping a particular constant to a SymbolicElem
		 * @param str The constant name.
		 * @return The matching SymbolicElem.
		 * @throws Throwable Throws an exception upon parsing entries that are incompatible with the algebra in the SymbolicElemFactory.  For instance a use of "i" when the SymbolicElemFactory only has reals.
		 */
		public abstract SymbolicElem<R2,S2> hndl( String str ) throws Throwable;
	}



	/**
	 * The factory for the output SymbolicElem type.
	 */
	protected SymbolicElemFactory<R2,S2> outputParseFac;
	
	/**
	 * Boolean indicating whether this is parsing an indefinite integral, and hence should be checked for the presence of a constant of integration.
	 */
	protected boolean integrationParse;
	
	
	/**
	 * Map for getting the SymbolicElem matching the "temporary variable" name from a Tac-like entry.
	 */
	protected HashMap<String,SymbolicElem<R2,S2>> outputMap;
	
	/**
	 * The current like of Tac-like text that is being parsed.
	 */
	protected String currentLine;
	
	/**
	 * Boolean indicating whether a constant of integration was found in the parsing of Tac-like text.
	 */
	protected boolean foundIntegConst = false;
	
	
	
	/**
	 * Map for getting a SymHandler for a particular leading entry (i.e. the "instruction") in a Tac-like text line
	 */
	protected HashMap<String,SymHandler> hndl;
	
	
	
	/**
	 * Map for getting a ConstHandler for a constant entry on the Tac-like line
	 */
	protected HashMap<String,ConstHandler> hndlConst;
	
	
	
	/**
	 * Initializes the hndl map.
	 */
	protected void initHndl()
	{
		hndl = new HashMap<String,SymHandler>();
		
		
		
		hndl.put( "Zero" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateZero() );
				}
			
			} );
		
		
		
		hndl.put( "One" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateIdentity() );
				}
			
			} );
		
		
		
		hndl.put( "NegativeOne" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateNegativeIdentity() );
				}
			
			} );
		
		
		
		hndl.put( "ImaginaryUnit" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateImaginaryNumber() );
				}
			
			} );
		
		
		
		hndl.put( "Half" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateDivideOne( 2 ) );
				}
			
			} );
		
		
		
		hndl.put( "Add" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( processAdd( st ) );
				}
			
			} );
		
		
		
		hndl.put( "Mul" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( processMult( st ) );
				}
			
			} );
		
		
		
		hndl.put( "divideOne" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						Integer divideBy = Integer.parseInt( token );
						return( generateDivideOne( divideBy.intValue() ) );
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "invert" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( st.hasMoreTokens() )
						{
							token = st.nextToken();
							SymbolicElem<R2,S2> tv = outputMap.get(token);
							if( tv == null )
							{
								throw( new RuntimeException( "Fail" ) );
							}
							return( generateInvert( tv ) );
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "Integer" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals("<<>>") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								Double constLiteral = Double.parseDouble( token );
								return( generateConstLiteral( constLiteral.intValue() ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "Float" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals("<<>>") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								Double constLiteral = Double.parseDouble( token );
								return( generateConstLiteral( constLiteral.doubleValue() ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "Symbol" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals("<<>>") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								if( isIntegrationConst( token ) )
								{
									return( generateIntegrationConst() );
								}
								else
								{
									ConstHandler cnst = hndlConst.get( token );
									
									if( cnst != null )
									{
										return( cnst.hndl( token ) );
									}
									else
									{
										return( generateVar( token ) );
									}
								}
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "Pi" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generatePi() );
				}
			
			} );
		
		
		
		hndl.put( "pi" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generatePi() );
				}
			
			} );
		
		
		
		hndl.put( "Exp1" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateE() );
				}
			
			} );
		
		
		
		hndl.put( "e" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					return( generateE() );
				}
			
			} );
		
		
		
		hndl.put( "exp" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateExp( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "sin" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateSin( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "cos" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateCos( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "sinh" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateSinh( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "cosh" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateCosh( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "log" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateLn( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		// In case the AI hallucinates
		hndl.put( "ln" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateLn( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		// In case the AI hallucinates
		hndl.put( "sqrt" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tv = outputMap.get(token);
								if( tv == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								return( generateSqrt( tv ) );
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		
		hndl.put( "Pow" ,
			new SymHandler()
			{

				@Override
				public SymbolicElem<R2, S2> hndl(StringTokenizer st) throws Throwable {
					if( st.hasMoreTokens() )
					{
						String token = st.nextToken();
						if( token.equals(",") )
						{
							if( st.hasMoreTokens() )
							{
								token = st.nextToken();
								SymbolicElem<R2,S2> tva = outputMap.get(token);
								if( tva == null )
								{
									throw( new RuntimeException( "Fail" ) );
								}
								if( st.hasMoreTokens() )
								{
									token = st.nextToken();
									if( token.equals(",") )
									{
										if( st.hasMoreTokens() )
										{
											token = st.nextToken();
											SymbolicElem<R2,S2> tvb = outputMap.get(token);
											if( tvb == null )
											{
												throw( new RuntimeException( "Fail" ) );
											}
											return( generatePow( tva , tvb ) );
										}
									}
								}			
							}
						}
					}
					return( null );
				}
			
			} );
		
		
		// Override this method to support "I" or "i".
		// Note that depending on the algebra the letter "I" could either denote e.g. imaginary number, identity matrix, or electric current.
		// For imaginary numbers the generateImaginaryNumber() method may also need to be overridden.
		
	}
	
	
	
	/**
	 * Initializes the hndlConst map.
	 */
	protected void initHndlConst()
	{
		hndlConst = new HashMap<String,ConstHandler>();
		
		
		
		hndlConst.put( "pi" , 
				new ConstHandler()
				{

					@Override
					public SymbolicElem<R2, S2> hndl(String str) throws Throwable {
						return( generatePi() );
					}
			
				} );
		
		
		
		hndlConst.put( "Pi" , 
				new ConstHandler()
				{

					@Override
					public SymbolicElem<R2, S2> hndl(String str) throws Throwable {
						return( generatePi() );
					}
			
				} );
		
		
		
		hndlConst.put( "e" , 
				new ConstHandler()
				{

					@Override
					public SymbolicElem<R2, S2> hndl(String str) throws Throwable {
						return( generateE() );
					}
			
				} );
		
		
		// Override this method to support "I" or "i".
		// Note that depending on the algebra the letter "I" could either denote e.g. imaginary number, identity matrix, or electric current.
		// For imaginary numbers the generateImaginaryNumber() method may also need to be overridden.
		
	}
	
	
	
	
	/**
	 * Constructor.
	 * @param _outputParseFac The factory for the output SymbolicElem type.
	 * @param _integrationParse Boolean indicating whether this is parsing an indefinite integral, and hence should be checked for the presence of a constant of integration.
	 */
	public AiOllamaParse(SymbolicElemFactory<R2,S2> _outputParseFac , boolean _integrationParse )
	{
		outputParseFac = _outputParseFac;
		integrationParse = _integrationParse;
		outputMap = new HashMap<String,SymbolicElem<R2,S2>>();
		initHndl();
		initHndlConst();
	}
	
	
	
	/**
	 * Maps Tac-like tokens for addition to a SymbolicElem
	 * @param st The input tokenizer of the tokens to be parsed.
	 * @return The matching SymbolicElem
	 */
	protected SymbolicElem<R2,S2> processAdd( StringTokenizer st )
	{
		SymbolicElem<R2,S2> ret = null;
		
		
		while( st.hasMoreTokens() )
		{
			String token = st.nextToken();
			if( !( token.equals( "," ) ) )
			{
				SymbolicElem<R2,S2> tv = outputMap.get(token);
				if( tv == null )
				{
					throw( new RuntimeException( "Fail" ) );
				}
				if( ret == null )
				{
					ret = tv;
				}
				else
				{
					ret = ret.add( tv );
				}
			}
		}
		
		
		if( ret == null )
		{
			return( generateUnparsed() );
		}
		
		
		return( ret );
	}
	
	
	
	/**
	 * Maps Tac-like tokens for multiplication to a SymbolicElem
	 * @param st The input tokenizer of the tokens to be parsed.
	 * @return The matching SymbolicElem
	 */
	protected SymbolicElem<R2,S2> processMult( StringTokenizer st )
	{
		SymbolicElem<R2,S2> ret = null;
		
		
		while( st.hasMoreTokens() )
		{
			String token = st.nextToken();
			if( !( token.equals( "," ) ) )
			{
				SymbolicElem<R2,S2> tv = outputMap.get(token);
				if( tv == null )
				{
					throw( new RuntimeException( "Fail" ) );
				}
				if( ret == null )
				{
					ret = tv;
				}
				else
				{
					ret = ret.mult( tv );
				}
			}
		}
		
		
		if( ret == null )
		{
			return( generateUnparsed() );
		}
		
		
		return( ret );
	}
	
	
	
	/**
	 * Given the presumption that the previous tokens in the tokenizer had the "temporary variable" of the Tac-like line and the "=" assignment operator of the Tac-like line, attempts to parse the content to the right of the equal sign.
	 * @param st The input tokenizer for the tokens to be parsed.
	 * @return The matching SymbolicElem for the parsed tokens.
	 * @throws Throwable Throws an exception upon parsing entries that are incompatible with the algebra in the SymbolicElemFactory.  For instance a use of "i" when the SymbolicElemFactory only has reals.
	 */
	protected SymbolicElem<R2,S2> processLineTokens( StringTokenizer st ) throws Throwable
	{
		SymbolicElem<R2,S2> ret = null;
		
		if( st.hasMoreTokens() )
		{
			String token = st.nextToken();
			
			SymHandler hn = hndl.get( token );
			
			if( hn != null )
			{
				ret = hn.hndl(st);
				if( ret != null )
				{
					return( ret );
				}
			}
			
			
			
		}
		
		return( generateUnparsed() );
	}
	
	
	
	/**
	 * Given the presumption that the previous token in the tokenizer had the "temporary variable" of the Tac-like line, attempts to find the subsequent equal sign and parse the following Tac-like terms containing the content to the right of the equal sign
	 * @param st The input tokenizer for the tokens to be parsed.
	 * @return The matching SymbolicElem for the parsed tokens.
	 * @throws Throwable Throws an exception upon parsing entries that are incompatible with the algebra in the SymbolicElemFactory.  For instance a use of "i" when the SymbolicElemFactory only has reals.
	 */
	protected SymbolicElem<R2,S2> processEqTokens( StringTokenizer st ) throws Throwable
	{
		SymbolicElem<R2,S2> ret = null;
		
		if( st.hasMoreTokens() )
		{
			String token = st.nextToken();
			
			if( token.equals( "=" ) )
			{
				return( processLineTokens( st ) );
			}
		}
		
		return( generateUnparsed() );
	}
	
	
	
	/**
	 * Parses an ArrayList of Tac-like text with each string of the ArrayList being one line of text, and returns the matching SymbolicElem.  Does not include constant of integration.
	 * @param in The input ArrayList of text strings.
	 * @return The matching SymbolicElem.
	 * @throws Throwable Throws an exception upon parsing entries that are incompatible with the algebra in the SymbolicElemFactory.  For instance a use of "i" when the SymbolicElemFactory only has reals.
	 */
	public SymbolicElem<R2,S2> genParsedExprComp( ArrayList<String> in ) throws Throwable
	{
		
		final int max = in.size();
		for( int count = 0 ; count < max - 1 ; count++ )
		{
			String line = in.get(count);
			currentLine = line;
			StringTokenizer st = new StringTokenizer(line);
			if( st.hasMoreTokens() )
			{
				String tacName = st.nextToken();
				SymbolicElem<R2,S2> exp = processEqTokens( st );
				outputMap.put(tacName, exp);
			}
		}
		
		
		SymbolicElem<R2,S2> ret = null;
		
		
		if( max > 0 )
		{
			String line = in.get( max - 1 );
			currentLine = line;
			StringTokenizer st = new StringTokenizer( line );
			if( st.hasMoreTokens() )
			{
				String token = st.nextToken();
				if( token.equals( "Final:" ) )
				{
					if( st.hasMoreTokens() )
					{
						token = st.nextToken();
						ret = outputMap.get(token);
						if( ret == null )
						{
							throw( new RuntimeException( "Fail" ) );
						}
					}
				}
			}
		}
		
		
		if( ret == null )
		{
			throw( new RuntimeException( "Fail" ) );
		}
		return( ret );
	}
	
	
	
	/**
	 * Parses an ArrayList of Tac-like text with each string of the ArrayList being one line of text, and returns the matching SymbolicElem.
	 * @param in The input ArrayList of text strings.
	 * @return The matching SymbolicElem.
	 * @throws Throwable Throws an exception upon parsing entries that are incompatible with the algebra in the SymbolicElemFactory.  For instance a use of "i" when the SymbolicElemFactory only has reals.
	 */
	public SymbolicElem<R2,S2> genParsedExpr( ArrayList<String> in ) throws Throwable
	{
		
		SymbolicElem<R2,S2> ret = genParsedExprComp( in );
		
		
		if( integrationParse && !foundIntegConst )
		{
			ret = ret.add( generateIntegrationConst() );
		}
		
		
		if( ret == null )
		{
			throw( new RuntimeException( "Fail" ) );
		}
		return( ret );
	}
	
	
	
	/**
	 * Handles an addition operation from a Tac-like line
	 * @param t1 The first argument to be added.
	 * @param t2 The second argument to be added.
	 * @return SymbolicElem containing the result of the add.
	 */
	protected SymbolicElem<R2,S2> generateAdd( SymbolicElem<R2,S2> t1 , SymbolicElem<R2,S2> t2 )
	{
		return(  t1.add(t2) );
	}
	
	
	/**
	 * Handles an multiplication operation from a Tac-like line
	 * @param t1 The first argument to be multiplied.
	 * @param t2 The second argument to be multiplied.
	 * @return SymbolicElem containing the result of the multiply.
	 */
	protected SymbolicElem<R2,S2> generateMult( SymbolicElem<R2,S2> t1 , SymbolicElem<R2,S2> t2 )
	{
		return(  t1.mult(t2) );
	}
	
	
	/**
	 * Handles an identity declaration from the Tac-like line.
	 * @return SymbolicElem containing the identity.
	 */
	protected SymbolicElem<R2,S2> generateIdentity()
	{
		return(outputParseFac.identity() );
	}
	
	
	/**
	 * Handles an negative identity declaration from the Tac-like line.
	 * @return SymbolicElem containing the negative identity.
	 */
	protected SymbolicElem<R2,S2> generateNegativeIdentity()
	{
		return( ( outputParseFac.identity( ) ).negate() );
	}
	
	
	/**
	 * Handles a zero declaration from the Tac-like line.
	 * @return SymbolicElem containing the zero.
	 */
	protected SymbolicElem<R2,S2> generateZero()
	{
		return(outputParseFac.zero() );
	}
	
	
	/**
	 * Handles an imaginary number from the Tac-like line.
	 * Default behavior is to throw an exception.
	 * Override this method to provide imaginary number support.
	 * @return SymbolicElem containing the zero.
	 */
	protected SymbolicElem<R2,S2> generateImaginaryNumber() throws Throwable
	{
		throw( new ImaginaryNotSupportedException() );
	}
	
	
	/**
	 * Handles a declaration to divide by a constant integer (e.g. 1/2, 1/3) from the Tac-like line.
	 * @param div The integer by which to divide.
	 * @return SymbolicElem corresponding to the identity divided by the input number.
	 */
	protected SymbolicElem<R2,S2> generateDivideOne( int div )
	{
		return( ( outputParseFac.identity( ) ).divideBy(div) );
	}
	
	
	/**
	 * Handles an inverse operation from the Tac-like line.
	 * @param tv The argument to invert.
	 * @return SymbolicElem corresponding to the inverse.
	 * @throws Throwable Can potentially declare NotInvertible, but this typically isn't generated by a SymbolicElem until an eval is performed.
	 */
	protected SymbolicElem<R2,S2> generateInvert( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.invertLeft() );
	}
	
	
	/**
	 * Handles an exponential, i.e. e ^ x, operation from the Tac-like line.
	 * @param tv The argument to use as the exponent.
	 * @return SymbolicElem corresponding to the exponential.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateExp( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.exp( 10 ) );
	}
	
	
	/**
	 * Handles a sine operation from the Tac-like line.
	 * @param tv The argument for the sine.
	 * @return SymbolicElem corresponding to the sine.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateSin( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.sin( 10 ) );
	}
	
	
	/**
	 * Handles a cosine operation from the Tac-like line.
	 * @param tv The argument for the cosine.
	 * @return SymbolicElem corresponding to the cosine.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateCos( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.cos( 10 ) );
	}
	
	
	/**
	 * Handles a sinh operation from the Tac-like line.
	 * @param tv The argument for the sinh.
	 * @return SymbolicElem corresponding to the sinh.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateSinh( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.sinh( 10 ) );
	}
	
	
	/**
	 * Handles a cosh operation from the Tac-like line.
	 * @param tv The argument for the cosh.
	 * @return SymbolicElem corresponding to the cosh.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateCosh( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.cosh( 10 ) );
	}
	
	
	/**
	 * Handles a natural logarithm operation from the Tac-like line.
	 * @param tv The argument for the natural logarithm.
	 * @return SymbolicElem corresponding to the natural logarithm.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateLn( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		return( tv.ln( 10 , 10 ) );
	}
	
	
	/**
	 * Handles a square root operation from the Tac-like line.
	 * @param tv The argument for the square root.
	 * @return SymbolicElem corresponding to the square root.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generateSqrt( SymbolicElem<R2,S2> tv ) throws Throwable
	{
		final SymbolicSqrt<R2,S2> elem = new SymbolicSqrt<R2,S2>( tv , outputParseFac.getFac() );
		return( elem );
	}
	
	
	/**
	 * Handles an exponential, i.e. a ^ b, operation from the Tac-like line.
	 * @param tva The first argument of the exponential operator.
	 * @param tvb The second argument of the exponential operator.
	 * @return SymbolicElem corresponding to the exponential operator.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected SymbolicElem<R2,S2> generatePow( SymbolicElem<R2,S2> tva , SymbolicElem<R2,S2> tvb ) throws Throwable
	{
		return( tva.powL( tvb , 10 , 10 ) );
	}
	
	
	/**
	 * Handles a use of a non-integer constant literal from the Tac-like line.
	 * @return SymbolicElem corresponding to the non-integer constant literal.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected abstract SymbolicElem<R2,S2> generateConstLiteral( double val );
	
	
	/**
	 * Handles a use of a integer constant literal from the Tac-like line.
	 * @return SymbolicElem corresponding to the integer constant literal.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected abstract SymbolicElem<R2,S2> generateConstLiteral( int val );
	
	
	/**
	 * Handles the case where the declaration on the Tac-like line is not recognized.
	 * @return The identity as a placeholder.
	 */
	protected SymbolicElem<R2,S2> generateUnparsed()
	{
		System.out.println( currentLine );
		return( outputParseFac.identity() );
	}
	
	
	/**
	 * Handles a use of a variable from the Tac-like line.
	 * @return SymbolicElem corresponding to the variable.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected abstract SymbolicElem<R2,S2> generateVar( String name );
	
	
	/**
	 * Handles a use of a constant of integration from the Tac-like line.
	 * @return SymbolicElem corresponding to the constant of integration.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected abstract SymbolicElem<R2,S2> generateIntegrationConst();
	
	
	/**
	 * Handles a use of pi from the Tac-like line.
	 * @return SymbolicElem corresponding to pi.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected abstract SymbolicElem<R2,S2> generatePi();
	
	
	/**
	 * Handles a natural exponent base declaration from the Tac-like line.
	 * @return SymbolicElem corresponding to the natural base exponent.
	 * @throws Throwable Generated in the event the operation can not be performed.
	 */
	protected abstract SymbolicElem<R2,S2> generateE();
	
	
	
	/**
	 * Returns true if the name matches a name for a constant of integration.  The behavior of this method can be overriden by subclasses to change the parsing.
	 * @param in The name of the symbol from the Tac-like line.
	 * @return True if there is a match to a constant of integration.  Also updates member variable foundIntegConst.
	 */
	protected boolean isIntegrationConst( String in )
	{
		boolean ret = integrationParse && ( in.equals( "C" ) );
		foundIntegConst = foundIntegConst || ret;
		return( ret );
	}
	
	
	
	
}


