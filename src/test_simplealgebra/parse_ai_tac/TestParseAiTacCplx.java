





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





package test_simplealgebra.parse_ai_tac;



import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.*;
import java.net.URL;
import java.util.*;

import simplealgebra.*;

import simplealgebra.symbolic.*;
import simplealgebra.algo.ai_ollama.*;


/**
 * Verifies classes related to use of Ollama for AI manipulation of expressions.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class TestParseAiTacCplx extends TestCase {
	
	
	
	/**
	 * Symbolic elem for one component of the tensor upon which to apply the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class ConstLiteralElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The index of the component.
		 */
		private ComplexElem<DoubleElem,DoubleElemFactory> col;
		
		/**
		 * The index of the component.
		 */
		private Integer intSeed;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The index of the component.
		 */
		public ConstLiteralElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac, ComplexElem<DoubleElem,DoubleElemFactory> _col , Integer _intSeed) {
			super(_fac);
			col = _col;
			intSeed = _intSeed;
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return(col);
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return(col);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( ConstLiteralElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( ConstLiteralElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.print( " , " );
				ps.print( intSeed );
				ps.println( " );" );
			}
			return( st );
		}
		
		/**
		 * Gets the index of the component.
		 * 
		 * @return The index of the component.
		 */
		public ComplexElem<DoubleElem,DoubleElemFactory> getCol() {
			return col;
		}
		
		/**
		 * Gets the index of the component.
		 * 
		 * @return The index of the component.
		 */
		public Integer getIntSeed() {
			return intSeed;
		}
		
	}
	
	
	
	
	
	/**
	 * Symbolic elem for one component of the tensor upon which to apply the differential equation.
	 * 
	 * NOTE: The AI is going to be trained almost entirely by people who only use single-letter variable names.  Hence
	 * the AI is essentially only going to understand single-variable names.  For instance, it will be much more likely
	 * to recognize "xy" as x multiplied by y rather than as a single variable called "xy".  Hence when generating variable
	 * definitions for the writer only single-letter variable names should be used.  Also, only particular letters should
	 * be used.  For instance, the training of the AI is usually going to recognize "d" as a differential term rather than
	 * a separate variable.  Also, "C" will usually be interpreted as a constant of integration rather than a separate variable.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class VarElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The index of the component.
		 */
		private String col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The index of the component.
		 */
		public VarElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac, String _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( VarElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( VarElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , \"" );
				ps.print( col );
				ps.println( "\" );" );
			}
			return( st );
		}
		
		/**
		 * Gets the index of the component.
		 * 
		 * For using the Writer this should be a single-letter name.  Please see the note at the top of the class.
		 * 
		 * @return The index of the component.
		 */
		public String getCol() {
			return col;
		}
		
	}
	
	
	
	
	
	/**
	 * Symbolic elem for constant val to use in expression.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class ConstValElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{
		/**
		 * The index of the component.
		 */
		private String col;

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The index of the component.
		 */
		public ConstValElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac, String _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( VarElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( VarElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.print( " , " );
				ps.print( col );
				ps.println( " );" );
			}
			return( st );
		}
		
		/**
		 * Gets the index of the component.
		 * 
		 * @return The index of the component.
		 */
		public String getCol() {
			return col;
		}
		
	}
	
	
	
	
	/**
	 * Symbolic elem for one component of the tensor upon which to apply the differential equation.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class ConstIntegrationElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{
		
		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 * @param _col The index of the component.
		 */
		public ConstIntegrationElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, ComplexElem<DoubleElem,DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<simplealgebra.DoubleElem, DoubleElemFactory>, ComplexElemFactory<simplealgebra.DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( ConstIntegrationElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( ConstIntegrationElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
	}
	
	
	
	/**
	 * Test parser for Ollama responses.
	 * 
	 * @author Thorn
	 *
	 */
	protected class TestAiOllamaParse extends AiOllamaParse<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{
		
		/**
		 * Whether to parse power to a constant as an iterated product of multiplications rather than a power function
		 */
		boolean useIteratedPow;

		/**
		 * Constructor
		 * @param _outputParseFac The factory for the output SymbolicElem type.
		 * @param integrationParse Boolean indicating whether this is parsing an indefinite integral, and hence should be checked for the presence of a constant of integration.
		 * @param _useIteratedPow Whether to parse power to a constant as an iterated product of multiplications rather than a power function
		 */
		public TestAiOllamaParse(
				SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _outputParseFac,
				boolean integrationParse , boolean _useIteratedPow) 
		{
			super( _outputParseFac, integrationParse);
			useIteratedPow = _useIteratedPow;
		}

		
		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generateConstLiteral(double val) {
			DoubleElem vl = new DoubleElem( val );
			ComplexElem<DoubleElem,DoubleElemFactory> vlc = new ComplexElem<DoubleElem,DoubleElemFactory>( vl , outputParseFac.getFac().getFac().zero() );
			return new ConstLiteralElem( outputParseFac.getFac(), vlc , null );
		}

		
		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generateConstLiteral(int val) {
			DoubleElem vl = new DoubleElem( val );
			ComplexElem<DoubleElem,DoubleElemFactory> vlc = new ComplexElem<DoubleElem,DoubleElemFactory>( vl , outputParseFac.getFac().getFac().zero() );
			return new ConstLiteralElem( outputParseFac.getFac(), vlc , new Integer(val) );
		}


		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generateVar(String name) {
			return new VarElem( outputParseFac.getFac(), name );
		}


		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generateIntegrationConst() {
			return new ConstIntegrationElem( outputParseFac.getFac() );
		}


		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generatePi() {
			DoubleElem vl = new DoubleElem( Math.PI );
			ComplexElem<DoubleElem,DoubleElemFactory> vlc = new ComplexElem<DoubleElem,DoubleElemFactory>( vl , outputParseFac.getFac().getFac().zero() );
			return new ConstLiteralElem( outputParseFac.getFac(), vlc , null );
		}


		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generateE() {
			DoubleElem vl = new DoubleElem( Math.E );
			ComplexElem<DoubleElem,DoubleElemFactory> vlc = new ComplexElem<DoubleElem,DoubleElemFactory>( vl , outputParseFac.getFac().getFac().zero() );
			return new ConstLiteralElem( outputParseFac.getFac(), vlc , null );
		}
		
		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generateImaginaryNumber()
		{
			// This could be parsed into its own elem, but for this test the code will just parse to an imaginary number literal.
			ComplexElem<DoubleElem,DoubleElemFactory> vlc = new ComplexElem<DoubleElem,DoubleElemFactory>( outputParseFac.getFac().getFac().zero() , outputParseFac.getFac().getFac().identity() );
			return new ConstLiteralElem( outputParseFac.getFac(), vlc , null );
		}
		
		
		@Override
		protected SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> generatePow( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> tva , SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> tvb ) throws Throwable
		{
			if( useIteratedPow )
			{
				if( tvb instanceof ConstLiteralElem )
				{
					ConstLiteralElem tvbb = (ConstLiteralElem)( tvb );
					if( tvbb.getIntSeed() != null )
					{
						if( tvbb.getIntSeed() != 0 )
						{
							SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> exp = tva;
							for( int cnt = 1 ; cnt < Math.abs( tvbb.getIntSeed() ) ; cnt++ )
							{
								exp = exp.mult( tva );
							}
							if( tvbb.getIntSeed() < 0 )
							{
								exp = exp.invertLeft();
							}
							return( exp );
						}
					}
				}
			}
			
			return( super.generatePow( tva , tvb ) );
		}
		
		
	}
	
	
	
	
	/**
	 * Test writer for generating Ollama expressions.
	 * 
	 * @author Thorn
	 *
	 */
	protected class TestAiOllamaWrite extends AiOllamaWrite<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructor
		 * @param _inputParseFac The factory for the input SymbolicElem type.
		 */
		public TestAiOllamaWrite(SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _inputParseFac) {
			super(_inputParseFac);
		}
		
		
		
		

		@Override
		protected void initHndl()
		{
			super.initHndl();
			
			
			
			hndl.put( VarElem.class.getName() ,
				new SymHandler()
				{

					@Override
					public AiOllamaWrite<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>.PrecedenceNode hndl(
							SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in) throws Throwable {
						return( generateVar( in ) );
					}

					
				
				} );
			
			
			
			hndl.put( ConstValElem.class.getName() ,
				new SymHandler()
				{

					@Override
					public AiOllamaWrite<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>.PrecedenceNode hndl(
							SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in) throws Throwable {
						return( generateConst( in ) );
					}

					
				
				} );
		}
		
		
		
		
		/**
		 * Handles the case where the input SymbolicElem is a variable
		 * 
		 * NOTE: The AI is going to be trained almost entirely by people who only use single-letter variable names.  Hence
		 * the AI is essentially only going to understand single-variable names.  For instance, it will be much more likely
		 * to recognize "xy" as x multiplied by y rather than as a single variable called "xy".  Hence when generating variable
		 * definitions for the writer only single-letter variable names should be used.  Also, only particular letters should
		 * be used.  For instance, the training of the AI is usually going to recognize "d" as a differential term rather than
		 * a separate variable.  Also, "C" will usually be interpreted as a constant of integration rather than a separate variable.
		 * 
		 * @param in The input SymbolicElem.
		 * @return The matching PrecedenceNode.
		 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
		 */
		protected PrecedenceNode generateVar( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws Throwable
		{
			VarElem node = (VarElem) in;
			final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
			
			final String expStr = node.getCol();
			
			return( new PrecedenceNode( prec , expStr ) );
		}
		
		
		
		
		/**
		 * Handles the case where the input SymbolicElem is a constant literal.
		 * @param in The input SymbolicElem.
		 * @return The matching PrecedenceNode.
		 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
		 */
		protected PrecedenceNode generateConst( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws Throwable
		{
			ConstValElem node = (ConstValElem) in;
			final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
			
			final String expStr = node.getCol();
			
			return( new PrecedenceNode( prec , expStr ) );
		}
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacPoly() throws Throwable
	{

		
		String expectedStr = "4 * x ** 3 + 5 * x ** 2 + 7 * x";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		

		
		ConstValElem constTwo = new ConstValElem( cfac, "2" );
		
		
		ConstValElem constThree = new ConstValElem( cfac, "3" );
		
		
		ConstValElem constFour = new ConstValElem( cfac, "4" );
		
		
		ConstValElem constFive = new ConstValElem( cfac, "5" );
		
		
		ConstValElem constSeven = new ConstValElem( cfac, "7" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> firstTerm = constFour.mult( var.powR( constThree , 10 , 10 ) );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> secondTerm = constFive.mult( var.powR( constTwo , 10 , 10 ) );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> thirdTerm = constSeven.mult( var );
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = firstTerm.add( secondTerm ).add( thirdTerm );
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacXSin2X() throws Throwable
	{

		
		String expectedStr = "x * sin( 2 * x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		

		
		ConstValElem constTwo = new ConstValElem( cfac, "2" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sinMul = ( constTwo.mult( var ) ).sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = var.mult( sinMul );
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacLog2X() throws Throwable
	{

		
		String expectedStr = "ln( 2 * x + 1 )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		

		
		ConstValElem constTwo = new ConstValElem( cfac, "2" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> lnMul = ( ( constTwo.mult( var ) ).add( sfac.identity() ) ).ln( 10 , 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = lnMul;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacExpX() throws Throwable
	{

		
		String expectedStr = "exp( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.exp( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacSinX() throws Throwable
	{

		
		String expectedStr = "sin( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacCosX() throws Throwable
	{

		
		String expectedStr = "cos( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.cos( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacSinhX() throws Throwable
	{

		
		String expectedStr = "sinh( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sinh( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacCoshX() throws Throwable
	{

		
		String expectedStr = "cosh( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.cosh( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacInvertXTimesX() throws Throwable
	{

		
		String expectedStr = "( 1 / x ) * x";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> invT = ( var.invertLeft() ).mult( var );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = invT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacSqrtX() throws Throwable
	{

		
		String expectedStr = "sqrt( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = new SymbolicSqrt(var, sfac);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacConstTimesX() throws Throwable
	{

		
		String expectedStr = "3.45 * x";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		ConstValElem val = new ConstValElem(cfac, "3.45");
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = val.mult( var );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacETimesX() throws Throwable
	{

		
		String expectedStr = "e * x";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		ConstValElem val = new ConstValElem(cfac, "e");
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = val.mult( var );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacPiTimesX() throws Throwable
	{

		
		String expectedStr = "pi * x";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		ConstValElem val = new ConstValElem(cfac, "pi");
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = val.mult( var );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacCosY() throws Throwable
	{

		
		String expectedStr = "cos( y )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "y" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.cos( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Verifies generating an Ollama-compatible expression string from a SymbolicElem.
	 */
	public void testWriteAiTacMinusSinX() throws Throwable
	{

		
		String expectedStr = "-sin( x )";

		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		
		VarElem var = new VarElem( cfac, "x" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT.negate();
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		String str = write.generateString( elem );
		
		
		System.out.println( expectedStr );
		
		System.out.println( str );
		
		
		
		Assert.assertEquals( expectedStr , str );
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Verifies parsing of TAC-like expressions.
	 */
	public void testParseAiTacCplx() throws Throwable
	{
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		InputStream is = TestParseAiTacCplx.class.getResourceAsStream( "ArchiveC_FinalResults.txt" );
		
		
		LineNumberReader liA = new LineNumberReader( new InputStreamReader( is ) );
		
		
		String line = liA.readLine();
		while( line != null )
		{
			if( line.equals( "=== TAC-like output ===" ) )
			{
				ArrayList<String> ar = readArrayList( liA );
				TestAiOllamaParse test = new TestAiOllamaParse( sfac , true , true );
				SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expr = test.genParsedExpr( ar );
				Assert.assertTrue( expr != null );
			}
			line = liA.readLine();
		}
		

		
		
		
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Verifies parsing of TAC-like expressions for imaginary numbers.
	 */
	public void testParseAiTacImCplx() throws Throwable
	{
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		
		InputStream is = TestParseAiTacCplx.class.getResourceAsStream( "ImUnit_Reduced.txt" );
		
		
		LineNumberReader liA = new LineNumberReader( new InputStreamReader( is ) );
		
		
		String line = liA.readLine();
		while( line != null )
		{
			if( line.equals( "=== TAC-like output ===" ) )
			{
				ArrayList<String> ar = readArrayList( liA );
				TestAiOllamaParse test = new TestAiOllamaParse( sfac , true , true );
				SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expr = test.genParsedExpr( ar );
				Assert.assertTrue( expr != null );
			
				
				/* System.out.println( "Done." );
				
				String aa = expr.writeDesc( expr.getFac().generateWriteElemCache() , System.out );
				
				System.out.println( "### " + aa );
				
				System.out.println( "***" ); */
				
			}
			line = liA.readLine();
		}
		

		
		
		
		
		
		
		System.out.println( "Done" );
		
		
	}
	
	
	
	
	
	/**
	 * Test class for running a derivative through Ollama
	 * @author Thorn
	 *
	 */
	protected class TestAiOllamaInteractionDerivative extends AiOllamaInteractionDerivative<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{
		
		/**
		 * Constructor.
		 * @param _aiOllamaWriter Instance for generating expression strings for Ollama from SymbolicElems.
		 * @param _aiOllamaParse Instance for parsing a Tac-like syntax from Ollama result strings into SymbolicElems
		 */
		public TestAiOllamaInteractionDerivative(AiOllamaWriter<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _aiOllamaWriter,
				AiOllamaParse<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _aiOllamaParse) {
			super(_aiOllamaWriter, _aiOllamaParse);
		}
		
	}
	
	
	
	
	
	/**
	 * Test class for running a definite integral through Ollama
	 * @author Thorn
	 *
	 */
	protected class TestAiOllamaInteractionIntegral extends AiOllamaInteractionIntegral<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructor.
		 * @param _aiOllamaWriter Instance for generating expression strings for Ollama from SymbolicElems.
		 * @param _aiOllamaParse Instance for parsing a Tac-like syntax from Ollama result strings into SymbolicElems
		 */
		public TestAiOllamaInteractionIntegral(AiOllamaWriter<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _aiOllamaWriter,
				AiOllamaParse<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _aiOllamaParse) {
			super(_aiOllamaWriter, _aiOllamaParse);
		}
		
	}
	
	
	
	
	/**
	 * Tests running a derivative through Ollama
	 * @throws Throwable Throws an exception if e.g. not able to parse
	 */
	public void testAiDerivativeInteractionX() throws Throwable
	{
		

		final String varName = "x";
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		

		TestAiOllamaParse testParse = new TestAiOllamaParse( sfac , false , true );
		
		

		
		VarElem var = new VarElem( cfac, varName );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT.negate();
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		
		
		
		TestAiOllamaInteractionDerivative deriv = new TestAiOllamaInteractionDerivative(write, testParse);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> output = deriv.generate( elem , varName );
		
		
		System.out.println( "Done." );
		
		String aa = output.writeDesc( output.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	
	/**
	 * Tests running a derivative through Ollama
	 * @throws Throwable Throws an exception if e.g. not able to parse
	 */
	public void testAiDerivativeInteractionCplxX() throws Throwable
	{
		

		final String varName = "x";
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		

		TestAiOllamaParse testParse = new TestAiOllamaParse( sfac , false , true );
		
		

		
		VarElem var = new VarElem( cfac, varName );
		
		// For now just use an elem that will write "i" as its "variable name"
		VarElem imag = new VarElem( cfac, "i" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = ( imag.mult( var ) ).exp( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		
		
		
		TestAiOllamaInteractionDerivative deriv = new TestAiOllamaInteractionDerivative(write, testParse);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> output = deriv.generate( elem , varName );
		
		
		System.out.println( "Done." );
		
		String aa = output.writeDesc( output.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	
	/**
	 * Tests running a derivative through Ollama
	 * @throws Throwable Throws an exception if e.g. not able to parse
	 */
	public void testAiDerivativeInteractionY() throws Throwable
	{
		

		final String varName = "y";
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		

		TestAiOllamaParse testParse = new TestAiOllamaParse( sfac , false , true );
		
		

		
		VarElem var = new VarElem( cfac, varName );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT.negate();
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		
		
		
		TestAiOllamaInteractionDerivative deriv = new TestAiOllamaInteractionDerivative(write, testParse);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> output = deriv.generate( elem , varName );
		
		
		System.out.println( "Done." );
		
		String aa = output.writeDesc( output.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	/**
	 * Tests running a definite integral through Ollama
	 * @throws Throwable Throws an exception if e.g. not able to parse
	 */
	public void testAiIntegralInteractionX() throws Throwable
	{
		

		final String varName = "x";
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		

		TestAiOllamaParse testParse = new TestAiOllamaParse( sfac , true , true );
		
		

		
		VarElem var = new VarElem( cfac, varName );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT.negate();
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		
		
		
		TestAiOllamaInteractionIntegral integ = new TestAiOllamaInteractionIntegral(write, testParse);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> output = integ.generate( elem , varName );
		
		
		System.out.println( "Done." );
		
		String aa = output.writeDesc( output.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	/**
	 * Tests running a definite integral through Ollama
	 * @throws Throwable Throws an exception if e.g. not able to parse
	 */
	public void testAiIntegralInteractionCplxX() throws Throwable
	{
		

		final String varName = "x";
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		

		TestAiOllamaParse testParse = new TestAiOllamaParse( sfac , true , true );
		
		

		
		VarElem var = new VarElem( cfac, varName );
		
		// For now just use an elem that will write "i" as its "variable name"
		VarElem imag = new VarElem( cfac, "i" );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = ( imag.mult( var ) ).exp( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT;
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		
		
		
		TestAiOllamaInteractionIntegral integ = new TestAiOllamaInteractionIntegral(write, testParse);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> output = integ.generate( elem , varName );
		
		
		System.out.println( "Done." );
		
		String aa = output.writeDesc( output.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	/**
	 * Tests running a definite integral through Ollama
	 * @throws Throwable Throws an exception if e.g. not able to parse
	 */
	public void testAiIntegralInteractionY() throws Throwable
	{
		

		final String varName = "y";
		
		final DoubleElemFactory dfac = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> cfac = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dfac );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> sfac = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( cfac );
		
		

		TestAiOllamaParse testParse = new TestAiOllamaParse( sfac , true , true );
		
		

		
		VarElem var = new VarElem( cfac, varName );
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> expT = var.sin( 10 );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> fullTerm = expT.negate();
		
		
		
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> elem = fullTerm;
		
		TestAiOllamaWrite write = new TestAiOllamaWrite(sfac);
		
		
		
		
		TestAiOllamaInteractionIntegral integ = new TestAiOllamaInteractionIntegral(write, testParse);
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> output = integ.generate( elem , varName );
		
		
		System.out.println( "Done." );
		
		String aa = output.writeDesc( output.getFac().generateWriteElemCache() , System.out );
		
		System.out.println( "### " + aa );
		
		System.out.println( "***" );
		
		
	}
	
	
	
	/**
	 * Reads a list of TAC-like lines into an ArrayList of strings.
	 * @param li Reader containing the TAC-like lines.
	 * @return Output ArrayList of strings.
	 * @throws Throwable Throws exceptions upon e.g. parsing failures.
	 */
	protected ArrayList<String> readArrayList( LineNumberReader li ) throws Throwable
	{
		ArrayList<String> ar = new ArrayList<String>();
		boolean done = false;
		String line = li.readLine();
		while( ( line != null ) && ( !done ) )
		{
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
			
			if( !done )
			{
				line = li.readLine();
			}
		}
		if( !done )
		{
			throw( new RuntimeException( "Failed To Parse" ) );
		}
		return( ar );
	}
	
	
	

}





