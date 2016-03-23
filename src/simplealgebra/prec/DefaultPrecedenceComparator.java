

package simplealgebra.prec;


import java.util.*;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.meas.ValueWithUncertaintyElem;
import simplealgebra.symbolic.*;
import simplealgebra.ComplexElem;
import simplealgebra.DoubleElem;
import simplealgebra.SquareMatrixElem;
import simplealgebra.SymbolicConjugateLeft;
import simplealgebra.SymbolicConjugateRight;
import simplealgebra.SymbolicInvertLeftRevCoeff;
import simplealgebra.SymbolicInvertRightRevCoeff;
import simplealgebra.SymbolicMultRevCoeff;
import simplealgebra.SymbolicTranspose;
import simplealgebra.bigfixedpoint.BigFixedPointElem;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.ddx.FlowVectorTensor;
// import simplealgebra.ddx.MaterialDerivativeFactory;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.CovariantDerivative;
// import simplealgebra.ddx.CovariantDerivativeFactory;
// import simplealgebra.et.SymbolicIndexReduction;
// import simplealgebra.et.SymbolicRankTwoTrace;
// import simplealgebra.et.SymbolicRegenContravar;
// import simplealgebra.et.SymbolicRegenCovar;
// import simplealgebra.et.SymbolicTensorResym;
import simplealgebra.et.OrdinaryDerivative;
import simplealgebra.ga.SymbolicReverseLeft;
import simplealgebra.ga.SymbolicReverseRight;
import simplealgebra.ga.SymbolicDot;
import simplealgebra.ga.SymbolicDotHestenes;
import simplealgebra.ga.SymbolicLeftContraction;
import simplealgebra.ga.SymbolicRightContraction;
import simplealgebra.ga.SymbolicWedge;
import simplealgebra.ga.SymbolicCross;
import simplealgebra.ga.SymbolicScalar;

// import simplealgebra.symbolic.SymbolicMutable;






/**
 * A default description of how to assign precedence for converting to infix notation.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DefaultPrecedenceComparator extends PrecedenceComparator {
	
	
	/**
	 * Node representing an operator in a precedence relationship.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class OperatorNode
	{
		
		/**
		 * The class of the operator.
		 */
		protected Class<? extends Elem> operatorClass;
		
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public OperatorNode( Class<? extends Elem> cls )
		{
			operatorClass = cls;
		}
		
		
		/**
		 * Gets the class of the operator.
		 * 
		 * @return The class of the operator.
		 */
		public Class<? extends Elem> getOperatorClass()
		{
			return( operatorClass );
		}
		
		
		/**
		 * Returns true iff. a parenthesis is required to convert to infix, where "this" is the parent node.
		 * @param precA The precedence of the parent node in the elem tree.
		 * @param precB The precedence of the child node in the elem tree.
		 * @param b The child node in the elem tree.
		 * @param after Whether the child is written after the parent in infix notation.
		 * @return Whether infix notation requires a parenthesis for the child.
		 */
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( true );
		}
		
		
		/**
		 * Returns whether the operator is compatible with being used in "invisible operator" multiplication.
		 * 
		 * @return True iff. the operator is compatible with being used in "invisible operator" multiplication.
		 */
		public boolean isItimesCompatible()
		{
			return( true );
		}
		
		
	};
	
	
	
	/**
	 * Node for a multi-character identifier.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class MultiCharIdentOperator extends OperatorNode
	{
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public MultiCharIdentOperator( Class<? extends Elem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( false );
		}
		
		@Override
		public boolean isItimesCompatible()
		{
			return( false );
		}
		
	}
	
	
	
	/**
	 * Node for a basic unary operator.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class SimpleUnaryOperator extends OperatorNode
	{
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public SimpleUnaryOperator( Class<? extends Elem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( precA > precB );
		}
		
	}
	
	
	/**
	 * Node for a unary operator that superscripts its operand.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class SuperscriptingOperator extends OperatorNode
	{
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public SuperscriptingOperator( Class<? extends Elem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( !( b instanceof SuperscriptingOperator ) );
		}
		
	}
	
	
	
	/**
	 * Node for unary negation.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class NegateOperator extends SimpleUnaryOperator
	{
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public NegateOperator( Class<? extends Elem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean isItimesCompatible()
		{
			return( false );
		}
		
	}
	
	
	
	/**
	 * Node for a basic binary operator.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class SimpleBinaryOperator extends OperatorNode
	{
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public SimpleBinaryOperator( Class<? extends Elem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( after ? precA >= precB : precA > precB );
		}
		
	}
	
	
	
	/**
	 * Node for binary invisible multiplication.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class ItimesOperator extends SimpleBinaryOperator
	{
		
		/**
		 * Constructs the node.
		 * 
		 * @param cls The class of the operator.
		 */
		public ItimesOperator( Class<? extends Elem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( ( super.parenNeeded(precA, precB, b, after) ) || ( ( b != null ) && ( !( b.isItimesCompatible() ) ) ) );
		}
		
	}
	
	
	
	/**
	 * The operator precedence table of the comparator.
	 */
	protected final ArrayList<HashSet<OperatorNode>> operatorPrecedence = new ArrayList<HashSet<OperatorNode>>();
	
	
	
	/**
	 * Set of terminal symbol classes for the parent node in the expression tree.
	 */
	protected final HashSet<Class<? extends Elem>> enclosedOrTerminalSymbolsA = new HashSet<Class<? extends Elem>>();
	
	
	/**
	 * Set of terminal symbol classes for the child node in the expression tree.
	 */
	protected final HashSet<Class<? extends Elem>> enclosedOrTerminalSymbolsB = new HashSet<Class<? extends Elem>>();
	
	/**
	 * The parenthesis generator.
	 */
	protected ParenthesisGenerator parenthesisGenerator = null;
	
	
	
	
	/**
	 * Gets the precedence number of the elem, where larger numbers indicate higher precedence.
	 * 
	 * @param clssA The elem to check.
	 * @return The precedence value, of -1 if no precedence entry was found.
	 */
	protected int getPrecedenceNumber( final Elem<?,?> clssA )
	{
		Elem<?,?> clss = clssA;
		while( clss instanceof SymbolicReduction )
		{
			final Elem<?,?> ob = ( (SymbolicReduction<?,?>) clss ).getElem();
			if( ob instanceof Elem )
			{
				clss = (Elem<?,?>) ob;
			}
			else
			{
				return( -1 );
			}
		}
		
		for( int cnt = 0 ; cnt < operatorPrecedence.size() ; cnt++ )
		{
			final HashSet<OperatorNode> hs = operatorPrecedence.get( cnt );
			for( final OperatorNode op : hs )
			{
				if( op.getOperatorClass().isInstance( clss ) )
				{
					return( cnt );
				}
			}
		}
		
		return( -1 );
	}
	
	
	
	
	/**
	 * Gets the precedence node of the elem.
	 * 
	 * @param clssA The elem to check.
	 * @return The precedence node, or null if no precedence node was found.
	 */
	protected OperatorNode getOperatorNode( final Elem<?,?> clssA )
	{
		Elem<?,?> clss = clssA;
		while( clss instanceof SymbolicReduction )
		{
			final Elem<?,?> ob = ( (SymbolicReduction<?,?>) clss ).getElem();
			if( ob instanceof Elem )
			{
				clss = (Elem<?,?>) ob;
			}
			else
			{
				return( new MultiCharIdentOperator( clss.getClass() ) );
			}
		}
		
		for( int cnt = 0 ; cnt < operatorPrecedence.size() ; cnt++ )
		{
			final HashSet<OperatorNode> hs = operatorPrecedence.get( cnt );
			for( final OperatorNode op : hs )
			{
				if( op.getOperatorClass().isInstance( clss ) )
				{
					return( op );
				}
			}
		}
		
		return( null );
	}
	
	
	
	
	/**
	 * Generates the default initialization of the operator precedence table.
	 */
	protected void defaultOperatorInit()
	{
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SimpleBinaryOperator( ValueWithUncertaintyElem.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SimpleBinaryOperator( ComplexElem.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SimpleBinaryOperator( SymbolicAdd.class ) );
			hs.add( new MultiCharIdentOperator( DoubleElem.class ) );
			hs.add( new MultiCharIdentOperator( BigFixedPointElem.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SimpleBinaryOperator( SymbolicMultRevCoeff.class ) );
			hs.add( new ItimesOperator( SymbolicMult.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SimpleBinaryOperator( SymbolicDot.class ) );
			hs.add( new SimpleBinaryOperator( SymbolicDotHestenes.class ) );
			hs.add( new SimpleBinaryOperator( SymbolicScalar.class ) );
			hs.add( new SimpleBinaryOperator( SymbolicLeftContraction.class ) );
			hs.add( new SimpleBinaryOperator( SymbolicRightContraction.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SimpleBinaryOperator( SymbolicWedge.class ) );
			hs.add( new SimpleBinaryOperator( SymbolicCross.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SuperscriptingOperator( SymbolicInvertLeft.class ) );
			hs.add( new SuperscriptingOperator( SymbolicInvertRight.class ) );
			hs.add( new SuperscriptingOperator( SymbolicReverseLeft.class ) );
			hs.add( new SuperscriptingOperator( SymbolicReverseRight.class ) );
			hs.add( new SuperscriptingOperator( SymbolicConjugateLeft.class ) );
			hs.add( new SuperscriptingOperator( SymbolicConjugateRight.class ) );
			hs.add( new SuperscriptingOperator( SymbolicInvertLeftRevCoeff.class ) );
			hs.add( new SuperscriptingOperator( SymbolicInvertRightRevCoeff.class ) );
			hs.add( new SuperscriptingOperator( SymbolicTranspose.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new NegateOperator( SymbolicNegate.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new MultiCharIdentOperator( SymbolicSine.class ) );
			hs.add( new MultiCharIdentOperator( SymbolicCosine.class ) );
			hs.add( new MultiCharIdentOperator( SymbolicIdentity.class ) );
			hs.add( new MultiCharIdentOperator( SymbolicZero.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
	}
	
	
	
	/**
	 * Generates the default initialization of the set of terminal symbol classes for the parent node in the expression tree.
	 */
	protected void defaultEnclosedOrTerminalSymbolsAInit()
	{
		enclosedOrTerminalSymbolsA.add( SymbolicSqrt.class );
		enclosedOrTerminalSymbolsA.add( SymbolicExponential.class );
		enclosedOrTerminalSymbolsA.add( SymbolicAbsoluteValue.class );
		enclosedOrTerminalSymbolsA.add( PartialDerivativeOp.class );
		enclosedOrTerminalSymbolsA.add( DirectionalDerivative.class );
		enclosedOrTerminalSymbolsA.add( FlowVectorTensor.class );
		enclosedOrTerminalSymbolsA.add( SymbolicDivideBy.class );
		enclosedOrTerminalSymbolsA.add( OrdinaryDerivative.class );
		enclosedOrTerminalSymbolsA.add( CovariantDerivative.class );
		enclosedOrTerminalSymbolsA.add( SquareMatrixElem.class );
	}
	
	
	
	/**
	 * Generates the default initialization of the set of terminal symbol classes for the child node in the expression tree.
	 */
	protected void defaultEnclosedOrTerminalSymbolsBInit()
	{
		enclosedOrTerminalSymbolsB.add( SymbolicSqrt.class );
		enclosedOrTerminalSymbolsB.add( SymbolicExponential.class );
		enclosedOrTerminalSymbolsB.add( SymbolicAbsoluteValue.class );
		enclosedOrTerminalSymbolsB.add( PartialDerivativeOp.class );
		enclosedOrTerminalSymbolsB.add( DirectionalDerivative.class );
		enclosedOrTerminalSymbolsB.add( FlowVectorTensor.class );
		enclosedOrTerminalSymbolsB.add( SymbolicDivideBy.class );
		enclosedOrTerminalSymbolsB.add( OrdinaryDerivative.class );
		enclosedOrTerminalSymbolsB.add( CovariantDerivative.class );
		enclosedOrTerminalSymbolsB.add( SquareMatrixElem.class );
	}
	
	
	
	/**
	 * Initializes the set of terminal symbol classes for the parent node in the expression tree.
	 */
	protected void enclosedOrTerminalSymbolsAInit()
	{
		defaultEnclosedOrTerminalSymbolsAInit();
	}
	
	
	
	/**
	 * Initializes the set of terminal symbol classes for the child node in the expression tree.
	 */
	protected void enclosedOrTerminalSymbolsBInit()
	{
		defaultEnclosedOrTerminalSymbolsBInit();
	}
	
	
	
	
	/**
	 * Initializes the operator precedence table.
	 */
	protected void operatorInit()
	{
		defaultOperatorInit();
	}
	
	
	
	/**
	 * Initializes the parenthesis generator.
	 */
	protected void parenthesisGeneratorInit()
	{
		parenthesisGenerator = new DefaultParenthesisGenerator();
	}
	
	
	
	/**
	 * Indicates whether the comparator is initialized.
	 */
	protected boolean initialized = false;
	
	
	
	/**
	 * Initializes the comparator.
	 */
	protected void init()
	{
		if( !initialized )
		{
			operatorInit();
			enclosedOrTerminalSymbolsAInit();
			enclosedOrTerminalSymbolsBInit();
			parenthesisGeneratorInit();
		
			initialized = true;
		}
	}
	

	
	@Override
	public boolean parenNeeded( Elem<?,?> a , Elem<?,?> b , boolean after )
	{
		
		if( !initialized )
		{
			init();
		}
		
		
		for( final Class<? extends Elem> ii : enclosedOrTerminalSymbolsA )
		{
			if( ii.isInstance( a ) )
			{
				return( false );
			}
		}
		
		
		for( final Class<? extends Elem> ii : enclosedOrTerminalSymbolsB )
		{
			if( ii.isInstance( b ) )
			{
				return( false );
			}
		}
		
		
		OperatorNode ao = getOperatorNode( a );
		int ai = getPrecedenceNumber( a );
		OperatorNode bo = getOperatorNode( b );
		int bi = getPrecedenceNumber( b );
		
		if( ao != null )
		{
			return( ao.parenNeeded( ai , bi , bo , after ) );
		}
		
		
		return( true );
	}
	
	
	@Override
	public ParenthesisGenerator getParenthesisGenerator()
	{
		return( parenthesisGenerator );
	}

	
}



