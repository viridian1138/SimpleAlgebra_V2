




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






import java.util.HashMap;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicCosh;
import simplealgebra.symbolic.SymbolicCosine;
import simplealgebra.symbolic.SymbolicDivideBy;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicExponential;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicInvertLeft;
import simplealgebra.symbolic.SymbolicInvertRight;
import simplealgebra.symbolic.SymbolicLogarithm;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicPowL;
import simplealgebra.symbolic.SymbolicPowR;
import simplealgebra.symbolic.SymbolicPowC;
import simplealgebra.symbolic.SymbolicSine;
import simplealgebra.symbolic.SymbolicSinh;
import simplealgebra.symbolic.SymbolicSqrt;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Generates a matching expression string for Ollama given a SymbolicElem. 
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 * @param <R1> The enclosed type.
 * @param <S1> The factory for the enclosed type.
 */
public class AiOllamaWrite<R1 extends Elem<R1,?>, S1 extends ElemFactory<R1,S1>> extends AiOllamaWriter<R1,S1> {
	
	
	
	/**
	 * Enumeration of the precedence relationships in the output string generation.
	 * 
	 * @author tgreen
	 *
	 */
	protected enum WritePrecedence
	{
		/**
		 * Precedence for a single term or a parenthesized expression.
		 */
		AS_IF_SINGLE( 0 ),
		
		/**
		 * Precedence for negation.
		 */
		NEGATION( 1 ),
		
		/**
		 * Precedence for normal expression exponent operator.  The AI will most likely be trained to only understand the single usual operator.
		 */
		EXPONENT( 2 ),
		
		/**
		 * Precedence for multiplication.
		 */
		MULTIPLICATION( 3 ),
		
		/**
		 * Precedence for addition.
		 */
		ADDITION( 4 );
		
		/**
		 * The numerical precedence value.
		 */
		private int val;
		
		/**
		 * Constructor.
		 * @param _val The numerical precedence value.
		 */
		private WritePrecedence( int _val )
		{
			val = _val;
		}
		
		/**
		 * Gets the numerical precedence value.
		 * @return The numerical precedence value.
		 */
		public int getVal()
		{
			return( val );
		}
	}
	
	
	
	/**
	 * A node containing an expression string and an associated operator precedence
	 * 
	 * @author tgreen
	 *
	 */
	protected class PrecedenceNode
	{
		
		/**
		 * The operator precedence
		 */
		WritePrecedence writePrecedence;
		
		/**
		 * The expression string
		 */
		String expression;
		
		/**
		 * Constructor.
		 * @param _writePrecedence he operator precedence
		 * @param _expression The expression string
		 */
		public PrecedenceNode( WritePrecedence _writePrecedence , String _expression )
		{
			writePrecedence = _writePrecedence;
			expression = _expression;
		}
		
		/**
		 * Gets the operator precedence.
		 * @return The operator precedence
		 */
		public WritePrecedence getWritePrecedence() {
			return writePrecedence;
		}
		
		/**
		 * Gets the expression string
		 * @return The expression string.
		 */
		public String getExpression() {
			return expression;
		}
		
	}

	
	
	


	/**
	 * Interface for mapping a particular SymbolicElem to a PrecedenceNode
	 * @author tgreen
	 *
	 */
	protected abstract class SymHandler
	{
		/**
		 * Maps a particular SymbolicElem to a PrecedenceNode
		 * @param in The input SymbolicElem
		 * @return The output PrecedenceNode
		 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
		 */
		public abstract PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable;
	}
	
	
	

	/**
	 * The factory for the input SymbolicElem type.
	 */
	protected SymbolicElemFactory<R1,S1> inputParseFac;
	
	
	/**
	 * Map for getting a SymHandler for a particular SymbolicElem class name
	 */
	protected HashMap<String,SymHandler> hndl;
	
	
	
	
	
	

	
	
	/**
	 * Initializes the hndl map.
	 */
	protected void initHndl()
	{
		hndl = new HashMap<String,SymHandler>();
		
		
		
		hndl.put( SymbolicPowL.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateExponentL( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicPowR.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateExponentR( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicPowC.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateExponentR( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicNegate.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateNegate( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicMult.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateMult( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicAdd.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateAdd( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicLogarithm.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateLogarithm( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicInvertLeft.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateInvertLeft( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicInvertRight.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateInvertRight( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicDivideBy.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateDivideBy( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicZero.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateZero( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicIdentity.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateIdentity( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicSqrt.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateSqrt( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicSinh.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateSinh( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicCosh.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateCosh( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicSine.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateSine( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicCosine.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateCosine( in ) );
				}
			
			} );
		
		
		hndl.put( SymbolicExponential.class.getName() ,
			new SymHandler()
			{

				@Override
				public PrecedenceNode hndl( SymbolicElem<R1,S1> in ) throws Throwable {
					return( generateExponential( in ) );
				}
			
			} );
		
	}
	
	
	
	
	

	
	
	
	/**
	 * Constructor
	 * @param _inputParseFac The factory for the input SymbolicElem type.
	 */
	public AiOllamaWrite(SymbolicElemFactory<R1,S1> _inputParseFac )
	{
		inputParseFac = _inputParseFac;
		initHndl();
	}
	
	
	
	
	
	/**
	 * Generates a PrecedenceNode for an input SymbolicElem.
	 * @param in The input SymbolicElem
	 * @return The output PrecedenceNode
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode gen( SymbolicElem<R1,S1> in ) throws Throwable
	{
		final String cname = in.getClass().getName();
		final SymHandler sym = hndl.get( cname );
		if( sym != null )
		{
			return( sym.hndl( in ) );
		}
		
		return( generateUnhandled( in ) );
	}
	
	
	
	

	@Override
	public String generateString(SymbolicElem<R1, S1> in) throws Throwable {
		PrecedenceNode prec = gen( in );
		String expr = prec.getExpression();
		if( expr == null )
		{
			throw( new RuntimeException( "Fail" ) );
		}
		return( expr );
	}
	
	
	
	/**
	 * Puts parentheses around a PrecedenceNode string if the input operator precedence is higher
	 * @param in The input PrecedenceNode.
	 * @param prec The input precedence.
	 * @return The processed output PrecedenceNode.
	 */
	protected PrecedenceNode genPrecPrecedenceNode( PrecedenceNode in , WritePrecedence prec )
	{
		if( in.getWritePrecedence().getVal() <= prec.getVal() )
		{
			return( in );
		}
		
		System.out.println( in.getWritePrecedence() );
		System.out.println( prec );
		
		String expStr = "( " + ( in.getExpression() ) + " )";
		
		return( new PrecedenceNode( WritePrecedence.AS_IF_SINGLE , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is PowL.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateExponentL( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicPowL<R1,S1> node = (SymbolicPowL<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.EXPONENT;
		
		final PrecedenceNode base = genPrecPrecedenceNode( gen( node.getElemA() ) , WritePrecedence.NEGATION );
		
		final PrecedenceNode exp = genPrecPrecedenceNode( gen( node.getElemB() ) , WritePrecedence.NEGATION );
		
		final String expStr = ( base.getExpression() ) + " ** " + ( exp.getExpression() );
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is PowR.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateExponentR( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicPowR<R1, S1> node = (SymbolicPowR<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.EXPONENT;
		
		final PrecedenceNode base = genPrecPrecedenceNode( gen( node.getElemA() ) , WritePrecedence.NEGATION );
		
		final PrecedenceNode exp = genPrecPrecedenceNode( gen( node.getElemB() ) , WritePrecedence.NEGATION );
		
		final String expStr = ( base.getExpression() ) + " ** " + ( exp.getExpression() );
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is Negation.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateNegate( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicNegate<R1, S1> node = (SymbolicNegate<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.NEGATION;
		
		final PrecedenceNode inNode = gen( node.getElem() );
		
		final String iExprStr = inNode.getExpression();
		
		// Fix up cases where a negation already exists.
		if( ( iExprStr.startsWith( "-( " ) ) && ( iExprStr.endsWith( " )" ) ) )
		{
			String outStr = iExprStr.substring( 1 );
			return( new PrecedenceNode( WritePrecedence.AS_IF_SINGLE , outStr ) );
		}
		
		PrecedenceNode elem = genPrecPrecedenceNode( inNode , prec );
		
		// Fix up other cases where a negation already exists.
		if( elem.getExpression().startsWith( "-" ) )
		{
			PrecedenceNode nd = new PrecedenceNode( WritePrecedence.AS_IF_SINGLE , "( " + ( elem.getExpression() ) + " )" );
			elem = nd;
		}
		
		final String expStr = "-" + ( elem.getExpression() );
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is Multiplication.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateMult( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicMult<R1, S1> node = (SymbolicMult<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.MULTIPLICATION;
		
		final PrecedenceNode elemA = genPrecPrecedenceNode( gen( node.getElemA() ) , prec );
		
		final PrecedenceNode elemB = genPrecPrecedenceNode( gen( node.getElemB() ) , prec );
		
		final String expStr = ( elemA.getExpression() ) + " * " + ( elemB.getExpression() );
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is Addition.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateAdd( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicAdd<R1, S1> node = (SymbolicAdd<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.ADDITION;
		
		final PrecedenceNode elemA = genPrecPrecedenceNode( gen( node.getElemA() ) , prec );
		
		final PrecedenceNode elemB = genPrecPrecedenceNode( gen( node.getElemB() ) , prec );
		
		final String expStr = ( elemA.getExpression() ) + " + " + ( elemB.getExpression() );
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is natural logarithm.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateLogarithm( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicLogarithm<R1, S1> node = (SymbolicLogarithm<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "ln( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is left-inverse.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateInvertLeft( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicInvertLeft<R1, S1> node = (SymbolicInvertLeft<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = genPrecPrecedenceNode( gen( node.getElem() ) , WritePrecedence.EXPONENT );
		
		final String expStr = "( 1 / " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is right-inverse.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateInvertRight( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicInvertRight<R1, S1> node = (SymbolicInvertRight<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = genPrecPrecedenceNode( gen( node.getElem() ) , WritePrecedence.EXPONENT );
		
		final String expStr = "( 1 / " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is divide by integer constant (e.g. 1/2, 1/3.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateDivideBy( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicDivideBy<R1, S1> node = (SymbolicDivideBy<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = genPrecPrecedenceNode( gen( node.getElem() ) , WritePrecedence.EXPONENT );
		
		final String expStr = "( " + ( elem.getExpression() ) + " / " + ( node.getIval() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is Zero.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateZero( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicZero<R1, S1> node = (SymbolicZero<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final String expStr = "0";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is identity.
	 * The default generates "1" for factory types such as SymbolicElem<DoubleElem or SymbolicElem<GeometricAlgebraMultivectorElem<DoubleElem
	 * 
	 * For other factory types where "I" replaces "1" such as SymbolicElem<SquareMatrixElem<DoubleElem or SymbolicElem<GeometricAlgebraMultivectorElem<SquareMatrixElem<DoubleElem
	 * then override the method to return generateIdentityAsI().
	 * 
	 * Note that "I" can potentially conflict with uses of the imaginary number.
	 * 
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateIdentity( SymbolicElem<R1,S1> in ) throws Throwable
	{
		return( generateIdentityAsOne( in ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is identity using one.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateIdentityAsOne( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicIdentity<R1, S1> node = (SymbolicIdentity<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final String expStr = "1";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is identity using I.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateIdentityAsI( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicIdentity<R1, S1> node = (SymbolicIdentity<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final String expStr = "I";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is square-root.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateSqrt( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicSqrt<R1, S1> node = (SymbolicSqrt<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "sqrt( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is sinh.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateSinh( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicSinh<R1, S1> node = (SymbolicSinh<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "sinh( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is cosh.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateCosh( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicCosh<R1, S1> node = (SymbolicCosh<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "cosh( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is sine.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateSine( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicSine<R1, S1> node = (SymbolicSine<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "sin( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is cosine.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateCosine( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicCosine<R1, S1> node = (SymbolicCosine<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "cos( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is an exponential e ^ x.
	 * @param in The input SymbolicElem.
	 * @return The matching PrecedenceNode.
	 * @throws Throwable Throws an exception if e.g. the expression has a non-commutative operator (such as wedge product) and it's known that the current generation of Ollama AIs are not trained to process that.
	 */
	protected PrecedenceNode generateExponential( SymbolicElem<R1,S1> in ) throws Throwable
	{
		SymbolicExponential<R1, S1> node = (SymbolicExponential<R1,S1>) in;
		final WritePrecedence prec = WritePrecedence.AS_IF_SINGLE;
		
		final PrecedenceNode elem = gen( node.getElem() );
		
		final String expStr = "exp( " + ( elem.getExpression() ) + " )";
		
		return( new PrecedenceNode( prec , expStr ) );
	}
	
	
	
	/**
	 * Handles the case where the input SymbolicElem is not recognized.
	 * @param in The input SymbolicElem.
	 * @return Not used.  Throws exception instead.
	 * @throws Throwable Throws an exception indicating that the operator was not recognized.
	 */
	protected PrecedenceNode generateUnhandled( SymbolicElem<R1,S1> in ) throws Throwable
	{
		throw( new RuntimeException( "Fail (Write).  Not recognized " + in ) );
	}
	
	
	
	
	

	
}




