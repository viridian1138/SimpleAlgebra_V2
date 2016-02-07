

package simplealgebra.prec;


import java.util.*;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.*;
import simplealgebra.SymbolicConjugateLeft;
import simplealgebra.SymbolicConjugateRight;
import simplealgebra.SymbolicInvertLeftRevCoeff;
import simplealgebra.SymbolicInvertRightRevCoeff;
import simplealgebra.SymbolicMultRevCoeff;
import simplealgebra.SymbolicTranspose;
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
// import simplealgebra.symbolic.SymbolicPlaceholder;
// import simplealgebra.symbolic.SymbolicReduction;






/**
 * A default description of how to assign precedence for converting to infix notation.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed elem type.
 * @param <S> The factory for the enclosed elem type.
 */
public class DefaultPrecedenceComparator<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends PrecedenceComparator<R,S> {
	
	
	
	protected static class OperatorNode
	{
		
		protected Class<? extends SymbolicElem> operatorClass;
		
		
		public OperatorNode( Class<? extends SymbolicElem> cls )
		{
			operatorClass = cls;
		}
		
		
		public Class<? extends SymbolicElem> getOperatorClass()
		{
			return( operatorClass );
		}
		
		
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( true );
		}
		
		
		public boolean isItimesCompatible()
		{
			return( true );
		}
		
		
	};
	
	
	
	
	protected static class MultiCharIdentOperator extends OperatorNode
	{
		
		public MultiCharIdentOperator( Class<? extends SymbolicElem> cls )
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
	
	
	
	
	protected static class SimpleUnaryOperator extends OperatorNode
	{
		
		public SimpleUnaryOperator( Class<? extends SymbolicElem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( precA > precB );
		}
		
	}
	
	
	
	protected static class SuperscriptingOperator extends OperatorNode
	{
		
		public SuperscriptingOperator( Class<? extends SymbolicElem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( !( b instanceof SuperscriptingOperator ) );
		}
		
	}
	
	
	
	
	protected static class NegateOperator extends SimpleUnaryOperator
	{
		
		public NegateOperator( Class<? extends SymbolicElem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean isItimesCompatible()
		{
			return( false );
		}
		
	}
	
	
	
	protected static class SymmetricBinaryOperator extends OperatorNode
	{
		
		public SymmetricBinaryOperator( Class<? extends SymbolicElem> cls )
		{
			super( cls );
		}
		
		@Override
		public boolean parenNeeded( final int precA , final int precB , OperatorNode b , boolean after )
		{
			return( after ? precA >= precB : precA > precB );
		}
		
	}
	
	
	
	protected static class ItimesOperator extends SymmetricBinaryOperator
	{
		
		public ItimesOperator( Class<? extends SymbolicElem> cls )
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
	protected final HashSet<Class<? extends SymbolicElem>> enclosedOrTerminalSymbolsA = new HashSet<Class<? extends SymbolicElem>>();
	
	
	/**
	 * Set of terminal symbol classes for the child node in the expression tree.
	 */
	protected final HashSet<Class<? extends SymbolicElem>> enclosedOrTerminalSymbolsB = new HashSet<Class<? extends SymbolicElem>>();
	
	
	
	
	/**
	 * Gets the precedence number of the elem, where larger numbers indicate higher precedence.
	 * 
	 * @param clss The elem to check.
	 * @return The precedence value, of -1 if no precedence entry was found.
	 */
	protected int getPrecedenceNumber( SymbolicElem<R,S> clss )
	{
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
	 * @param clss The elem to check.
	 * @return The precedence node, or null if no precedence node was found.
	 */
	protected OperatorNode getOperatorNode( SymbolicElem<R,S> clss )
	{
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
			
			hs.add( new SymmetricBinaryOperator( SymbolicAdd.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SymmetricBinaryOperator( SymbolicMultRevCoeff.class ) );
			hs.add( new ItimesOperator( SymbolicMult.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SymmetricBinaryOperator( SymbolicDot.class ) );
			hs.add( new SymmetricBinaryOperator( SymbolicDotHestenes.class ) );
			hs.add( new SymmetricBinaryOperator( SymbolicScalar.class ) );
			hs.add( new SymmetricBinaryOperator( SymbolicLeftContraction.class ) );
			hs.add( new SymmetricBinaryOperator( SymbolicRightContraction.class ) );
			
			operatorPrecedence.add( hs );
		}
		
		
		
		
		{
			final HashSet<OperatorNode> hs = new HashSet<OperatorNode>();
			
			hs.add( new SymmetricBinaryOperator( SymbolicWedge.class ) );
			hs.add( new SymmetricBinaryOperator( SymbolicCross.class ) );
			
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
		enclosedOrTerminalSymbolsA.add( SymbolicAbsoluteValue.class );
		enclosedOrTerminalSymbolsA.add( PartialDerivativeOp.class );
		enclosedOrTerminalSymbolsA.add( DirectionalDerivative.class );
		enclosedOrTerminalSymbolsA.add( FlowVectorTensor.class );
		enclosedOrTerminalSymbolsA.add( SymbolicDivideBy.class );
		enclosedOrTerminalSymbolsA.add( OrdinaryDerivative.class );
		enclosedOrTerminalSymbolsA.add( CovariantDerivative.class );
	}
	
	
	
	/**
	 * Generates the default initialization of the set of terminal symbol classes for the child node in the expression tree.
	 */
	protected void defaultEnclosedOrTerminalSymbolsBInit()
	{
		enclosedOrTerminalSymbolsB.add( SymbolicSqrt.class );
		enclosedOrTerminalSymbolsB.add( SymbolicAbsoluteValue.class );
		enclosedOrTerminalSymbolsB.add( PartialDerivativeOp.class );
		enclosedOrTerminalSymbolsB.add( DirectionalDerivative.class );
		enclosedOrTerminalSymbolsB.add( FlowVectorTensor.class );
		enclosedOrTerminalSymbolsB.add( SymbolicDivideBy.class );
		enclosedOrTerminalSymbolsB.add( OrdinaryDerivative.class );
		enclosedOrTerminalSymbolsB.add( CovariantDerivative.class );
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
		
			initialized = true;
		}
	}
	

	
	@Override
	public boolean parenNeeded( SymbolicElem<R,S> a , SymbolicElem<R,S> b , boolean after )
	{
		
		if( !initialized )
		{
			init();
		}
		
		
		for( final Class<? extends SymbolicElem> ii : enclosedOrTerminalSymbolsA )
		{
			if( ii.isInstance( a ) )
			{
				return( false );
			}
		}
		
		
		for( final Class<? extends SymbolicElem> ii : enclosedOrTerminalSymbolsB )
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

	
}



