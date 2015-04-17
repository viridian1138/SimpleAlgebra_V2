




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





package simplealgebra.symbolic;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;

/**
 * A symbolic elem.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public abstract class SymbolicElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends Elem<SymbolicElem<R,S>, SymbolicElemFactory<R,S>> {

	/**
	 * Evaluates the symbolic expression.
	 * 
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @return The result of the evaluation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	abstract public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	/**
	 * Evaluates the partial derivative of the symbolic expression.
	 * 
	 * @param withRespectTo The variable over which to evaluate the derivative.
	 * @param implicitSpace The implicit space over which to evaluate the expression.
	 * @return The result of the evaluation.
	 * @throws NotInvertibleException
	 * @throws MultiplicativeDistributionRequiredException
	 */
	abstract public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	/**
	 * Writes a string representation of the elem to a print stream.
	 * @param ps The print stream to which to write the elem.
	 */
	abstract public void writeString( PrintStream ps );
	
	/**
	 * Writes MathML ( http://www.w3.org/Math/ ) presentation tags describing the elem to a print stream.
	 * @param pc A description of how to assign precedence for converting to infix notation.
	 * @param ps The print stream to which to write the tags.
	 */
	public void writeMathML( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		pc.handleUnimplementedElem( this , ps );
	}
	
	/**
	 * Constructs the elem.
	 * 
	 * @param _fac The factory for the enclosed type.
	 */
	public SymbolicElem( S _fac )
	{
		fac = _fac;
	}

	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "Add Zero B" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b.isSymbolicZero() ? this : new SymbolicAdd<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		// This simplification has a parallel implementation in the "Mult Zero B" rules in 
		// distributeSimplify.drl and distributeSimplify2.drl
		return( b.isSymbolicZero() ? b : new SymbolicMult<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> negate() {
		return( new SymbolicNegate<R,S>( this , fac ) );
	}

	@Override
	public SymbolicElem<R, S> invertLeft() throws NotInvertibleException {
		return( new SymbolicInvertLeft<R,S>( this , fac ) );
	}
	
	@Override
	public SymbolicElem<R, S> invertRight() throws NotInvertibleException {
		return( new SymbolicInvertRight<R,S>( this , fac ) );
	}

	@Override
	public SymbolicElem<R, S> divideBy(BigInteger val) {
		return( new SymbolicDivideBy<R,S>( this , fac , val ) );
	}

	@Override
	public SymbolicElemFactory<R, S> getFac() {
		return( new SymbolicElemFactory<R,S>( fac ) );
	}
	
	/**
	 * Returns true iff. the elem is a symbolic zero.
	 * 
	 * @return True iff. the elem is a symbolic zero.
	 */
	public boolean isSymbolicZero()
	{
		return( false );
	}
	
	/**
	 * Returns true iff. the elem is a symbolic identity.
	 * 
	 * @return True iff. the elem is a symbolic identity.
	 */
	public boolean isSymbolicIdentity()
	{
		return( false );
	}
	
	
	/**
	 * Returns true if the elem exposes derivatives to elems by which it is multiplied.
	 * 
	 * @return True if the elem exposes derivatives to elems by which it is multiplied.
	 */
	public boolean exposesDerivatives()
	{
		return( false );
	}
	
	
	/**
	 * Returns whether the partial derivative of the elem is zero.
	 * 
	 * @return True if the partial derivative of the elem is zero.
	 */
	public boolean isPartialDerivativeZero()
	{
		return( false );
	}
	
	
	/**
	 * Writes MathML ( http://www.w3.org/Math/ ) presentation tags describing the elem, wrapped in the top-level math tag, to a print stream.
	 * @param pc A description of how to assign precedence for converting to infix notation.
	 * @param ps The print stream to which to write the tags.
	 */
	public void writeMathMLWrapped( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		ps.print( "<math display=\"inline\">" );
		writeMathML( pc , ps );
		ps.print( "</math>" );
	}
	
	/**
	 * Writes a self-contained HTML file containing MathML ( http://www.w3.org/Math/ ) presentation tags describing the elem.
	 * @param pc A description of how to assign precedence for converting to infix notation.
	 * @param ps The print stream to which to write the tags.
	 */
	public void writeHtmlFile( PrecedenceComparator<R,S> pc , PrintStream ps )
	{
		ps.println( "<html>" );
		ps.println( "<head>" );
		ps.println( "<title>Title</title>" );
		ps.println( "</head>" );
		ps.println( "<body>" );
		ps.print( "<P>" );
		writeMathMLWrapped( pc , ps );
		ps.println( "" );
		ps.println( "</body>" );
		ps.println( "</html>" );
	}
	
	
	
	@Override
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		if( id instanceof SymbolicOps )
		{
			switch( (SymbolicOps) id )
			{
				case DISTRIBUTE_SIMPLIFY:
				{
					return( handleDistributeSimplify() );
				}
				// break;
				
				
				case DISTRIBUTE_SIMPLIFY2:
				{
					return( handleDistributeSimplify2() );
				}
				// break;
				
			}
		}
		
		return( getFac().getFac().handleSymbolicOptionalOp(id, args) );
	}
	
	
	/**
	 * Returns whether the elem can be determined to be a symbolic constant.
	 * 
	 * @return True iff the elem can be determined to be a symbolic constant.
	 */
	public boolean evalSymbolicConstant()
	{
		return( false );
	}
	
	
	/**
	 * Performs a distribute simplify on the elem.
	 * 
	 * @return The result of the simplification.
	 */
	protected SymbolicElem<R, S> handleDistributeSimplify()
	{
		SymbolicElem<R,S> prev = this;
		StatefulKnowledgeSession session = null;
		SymbolicPlaceholder<R,S> place = null;
		while( true )
		{
			try
			{
				session = getDistributeSimplifyKnowledgeBase().newStatefulKnowledgeSession();
		
				session.insert( new DroolsSession( session ) );
		
				if( LoggingConfiguration.LOGGING_ON )
				{
					session.insert( new LoggingConfiguration() );
				}
				
				if( LoggingConfiguration.ENENT_LOGGING_ON )
				{
					session.addEventListener( generateEventLoggingListener() );
				}
			
				place = new SymbolicPlaceholder<R,S>( prev , fac );
			
				place.performInserts( session );
					
				session.fireAllRules();
		
				SymbolicElem<R, S> ret = place.getElem();
		
				session.dispose();
			
				return( ret );
			}
			catch( OutOfMemoryError ex )
			{
				SymbolicElem<R, S> ret = place != null ? place.getElem() : null;
				
				/*
				 * Always try to dispose the session after running out of memory.
				 */
				try
				{
					if( session != null )
					{
						session.dispose();
					}
				}
				catch( Throwable ex2 )
				{
					ex2.printStackTrace( System.out );
				}
				
				/*
				 * If no simplifications were completed, exit with exception.
				 */
				if( ( ret == null ) || ( ret == prev ) )
				{
					throw( ex );
				}
				
				/*
				 * If some simplifications completed before the memory limits ran out,
				 * re-run the session and see if it's possible to get farther on the next run.
				 */
				prev = ret;
				session = null;
				place = null;
			}
		}
	}
	
	
	/**
	 * Performs a simpler distribute simplify on the elem.
	 * 
	 * @return The result of the simplification.
	 */
	protected SymbolicElem<R, S> handleDistributeSimplify2()
	{
		SymbolicElem<R,S> prev = this;
		StatefulKnowledgeSession session = null;
		SymbolicPlaceholder<R,S> place = null;
		while( true )
		{
			try
			{
				session = getDistributeSimplify2KnowledgeBase().newStatefulKnowledgeSession();
		
				session.insert( new DroolsSession( session ) );
			
				if( LoggingConfiguration.LOGGING_ON )
				{
					session.insert( new LoggingConfiguration() );
				}
				
				if( LoggingConfiguration.ENENT_LOGGING_ON )
				{
					session.addEventListener( generateEventLoggingListener() );
				}
			
				place = new SymbolicPlaceholder<R,S>( prev , fac );
			
				place.performInserts( session );
					
				session.fireAllRules();
		
				SymbolicElem<R, S> ret = place.getElem();
		
				session.dispose();
			
				return( ret );
			}
			catch( OutOfMemoryError ex )
			{
				SymbolicElem<R, S> ret = place != null ? place.getElem() : null;
				
				/*
				 * Always try to dispose the session after running out of memory.
				 */
				try
				{
					if( session != null )
					{
						session.dispose();
					}
				}
				catch( Throwable ex2 )
				{
					ex2.printStackTrace( System.out );
				}
				
				/*
				 * If no simplifications were completed, exit with exception.
				 */
				if( ( ret == null ) || ( ret == prev ) )
				{
					throw( ex );
				}
				
				/*
				 * If some simplifications completed before the memory limits ran out,
				 * re-run the session and see if it's possible to get farther on the next run.
				 */
				prev = ret;
				session = null;
				place = null;
			}
		}
	}
	
	
	/**
	 * Returns whether this expression is equal to the one in the parameter, simplifying both sides first.
	 * 
	 * @param b The expression to be compared.
	 * @return True if the expressions are found to be equal, false otherwise.
	 */
	public boolean extSymbolicEquals( SymbolicElem<R, S> b ) throws NotInvertibleException
	{
		SymbolicElem<R, S> aa = this.distSimp();
		SymbolicElem<R, S> bb = b.distSimp();
		boolean ret = aa.symbolicEquals( bb );
		if( ret )
		{
//			System.out.println( "AAAAA" );
		}
		return( ret );
	}
	
	
	/**
	 * Returns whether this expression is equal to the one in the parameter.
	 * 
	 * @param b The expression to be compared.
	 * @return True if the expressions are found to be equal, false otherwise.
	 */
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		throw( new RuntimeException( "Not Supported " + this ) );
	}
	
	
	/**
	 * Simplifies the symbolic expression.
	 * 
	 * @return The simplified expression.
	 * @throws NotInvertibleException
	 */
	protected SymbolicElem<R,S> distSimp( ) throws NotInvertibleException
	{
		return( this.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null) );
	}
	
	
	/**
	 * Inserts this elem into a Drools ( http://drools.org ) session.
	 * 
	 * @param session The session in which to insert the elem.
	 */
	public void performInserts( StatefulKnowledgeSession session )
	{
		session.insert( this );
	}
	
	
	/**
	 * Inserts the elem into a Drools ( http://drools.org ) session.
	 * @param ds The session.
	 * @return This elem.
	 */
	public SymbolicElem<R,S> insSym( DroolsSession ds )
	{
		ds.insert( this );
		return( this );
	}
	
	
	
	/**
	 * The factory for the enclosed type.
	 */
	protected S fac;
	
	
	/**
	 * Generates a listener for logging match events.
	 * 
	 * @return The listener for logging match events.
	 */
	protected DefaultAgendaEventListener generateEventLoggingListener()
	{
		return( new DefaultAgendaEventListener()
		{
			@Override
			public void beforeMatchFired( final BeforeMatchFiredEvent event )
			{
				final Rule rule = event.getMatch().getRule();
				System.out.println( rule.getName() );
			}
		} );
	}
	
	
	/**
	 * Returns Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 * 
	 * @return Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	public static KnowledgeBase getDistributeSimplifyKnowledgeBase()
	{
		if( distributeSimplifyKnowledgeBase == null )
		{
			KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			builder.add( ResourceFactory.newClassPathResource( "distributeSimplify.drl" )  , 
					ResourceType.DRL );
			
			if( LoggingConfiguration.LOGGING_ON )
			{
				builder.add( ResourceFactory.newClassPathResource( "logging.drl" )  , 
						ResourceType.DRL );
			}
			
			if( builder.hasErrors() )
			{
				throw( new RuntimeException( builder.getErrors().toString() ) );
			}
			distributeSimplifyKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
			distributeSimplifyKnowledgeBase.addKnowledgePackages( builder.getKnowledgePackages() );
		}
		
		return( distributeSimplifyKnowledgeBase );
	}
	
	
	/**
	 * Returns Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 * 
	 * @return Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	public static KnowledgeBase getDistributeSimplify2KnowledgeBase()
	{
		if( distributeSimplify2KnowledgeBase == null )
		{
			KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			builder.add( ResourceFactory.newClassPathResource( "distributeSimplify2.drl" )  , 
					ResourceType.DRL );
			
			if( LoggingConfiguration.LOGGING_ON )
			{
				builder.add( ResourceFactory.newClassPathResource( "logging.drl" )  , 
						ResourceType.DRL );
			}
			
			if( builder.hasErrors() )
			{
				throw( new RuntimeException( builder.getErrors().toString() ) );
			}
			distributeSimplify2KnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
			distributeSimplify2KnowledgeBase.addKnowledgePackages( builder.getKnowledgePackages() );
		}
		
		return( distributeSimplify2KnowledgeBase );
	}
	
	
	/**
	 * Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	private static KnowledgeBase distributeSimplifyKnowledgeBase = null;
	
	/**
	 * Drools ( http://drools.org ) knowledge base for algebraic simplification.
	 */
	private static KnowledgeBase distributeSimplify2KnowledgeBase = null;
	
	
}


