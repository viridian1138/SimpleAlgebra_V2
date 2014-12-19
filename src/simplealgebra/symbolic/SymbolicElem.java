




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

import java.util.ArrayList;
import java.util.HashMap;

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

public abstract class SymbolicElem<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends Elem<SymbolicElem<R,S>, SymbolicElemFactory<R,S>> {

	abstract public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	abstract public R evalPartialDerivative( ArrayList<? extends Elem<?,?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException;
	
	abstract public String writeString( );
	
	
	public SymbolicElem( S _fac )
	{
		fac = _fac;
	}

	
	@Override
	public SymbolicElem<R, S> add(SymbolicElem<R, S> b) {
		return( b.isSymbolicZero() ? this : new SymbolicAdd<R,S>( this , b , fac ) );
	}

	@Override
	public SymbolicElem<R, S> mult(SymbolicElem<R, S> b) {
		return( b.isSymbolicZero() ? b : b.isSymbolicIdentity() ? this : new SymbolicMult<R,S>( this , b , fac ) );
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
	public SymbolicElem<R, S> divideBy(int val) {
		return( new SymbolicDivideBy<R,S>( this , fac , val ) );
	}

	@Override
	public SymbolicElemFactory<R, S> getFac() {
		return( new SymbolicElemFactory<R,S>( fac ) );
	}
	
	protected boolean isSymbolicZero()
	{
		return( false );
	}
	
	protected boolean isSymbolicIdentity()
	{
		return( false );
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
					StatefulKnowledgeSession session = getDistributeSimplifyKnowledgeBase().newStatefulKnowledgeSession();
					
					session.insert( new DroolsSession( session ) );
					
					if( LoggingConfiguration.LOGGING_ON )
					{
						session.insert( new LoggingConfiguration() );
					}
						
					SymbolicPlaceholder<R,S> place = new SymbolicPlaceholder<R,S>( this , fac );
						
					place.performInserts( session );
								
					session.fireAllRules();
					
					SymbolicElem<R, S> ret = place.getElem();
					
					session.dispose();
						
					return( ret );
				}
				// break;
				
				
				case DISTRIBUTE_SIMPLIFY2:
				{
					StatefulKnowledgeSession session = getDistributeSimplify2KnowledgeBase().newStatefulKnowledgeSession();
					
					session.insert( new DroolsSession( session ) );
					
					if( LoggingConfiguration.LOGGING_ON )
					{
						session.insert( new LoggingConfiguration() );
					}
						
					SymbolicPlaceholder<R,S> place = new SymbolicPlaceholder<R,S>( this , fac );
						
					place.performInserts( session );
								
					session.fireAllRules();
					
					SymbolicElem<R, S> ret = place.getElem();
					
					session.dispose();
						
					return( ret );
				}
				// break;
				
			}
		}
		
		return( getFac().getFac().handleSymbolicOptionalOp(id, args) );
	}
	
	
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
	
	
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		throw( new RuntimeException( "Not Supported " + this ) );
	}
	
	
	protected SymbolicElem<R,S> distSimp( ) throws NotInvertibleException
	{
		return( this.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null) );
	}
	
	
	public void performInserts( StatefulKnowledgeSession session )
	{
		session.insert( this );
	}
	
	
	protected S fac;
	
	
	
	
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
	
	
	private static KnowledgeBase distributeSimplifyKnowledgeBase = null;
	private static KnowledgeBase distributeSimplify2KnowledgeBase = null;
	
	
}


