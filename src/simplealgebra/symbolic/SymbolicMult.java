




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
import java.util.HashSet;
import java.util.Iterator;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.DerivativeElem;
import simplealgebra.ddx.PartialDerivativeOp;

public class SymbolicMult<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends SymbolicElem<R,S> 
{

	public SymbolicMult( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac )
	{
		super( _fac );
		elemA = _elemA;
		elemB = _elemB;
	}
	
	public SymbolicMult( SymbolicElem<R,S> _elemA , SymbolicElem<R,S> _elemB , S _fac , DroolsSession ds )
	{
		this( _elemA , _elemB , _fac );
		ds.insert( this );
	}
	
	@Override
	public R eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		if( elemA instanceof DerivativeElem )
		{
			return( ( (DerivativeElem<R,S>) elemA ).evalDerivative( elemB , implicitSpace ) );
		}
		R ea = null;
		try
		{
			ea = elemA.eval( implicitSpace );
		}
		catch( MultiplicativeDistributionRequiredException ex )
		{
			if( elemA instanceof SymbolicNegate )
			{
				return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).eval( implicitSpace ).negate() );
			}
			
			if( elemA instanceof SymbolicAdd )
			{
				final SymbolicElem<R,S> ia = ((SymbolicAdd) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicAdd) elemA).getElemB();
				return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).eval( implicitSpace ) );
			}
			
			if( elemA instanceof SymbolicMult )
			{
				final SymbolicElem<R,S> ia = ((SymbolicMult) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicMult) elemA).getElemB();
				return( ( ia.mult( ib.mult( elemB ) ) ).eval( implicitSpace ) );
			}
			
			throw( ex );
		}
		return( ea.mult( elemB.eval( implicitSpace ) ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elemA instanceof PartialDerivativeOp )
		{
			final ArrayList<? extends Elem<?, ?>> newWrt = new ArrayList<Elem<?, ?>>();
			final ArrayList neWW = newWrt;
			Iterator<? extends Elem<?,?>> it = ((PartialDerivativeOp) elemA).getWithRespectTo().iterator();
			while( it.hasNext() )
			{
				neWW.add( it.next() );
			}
			it = withRespectTo.iterator();
			while( it.hasNext() )
			{
				neWW.add( it.next() );
			}
			return( elemB.evalPartialDerivative(newWrt, implicitSpace) );
		}
		if( elemA instanceof DerivativeElem )
		{
			SymbolicElem<R,S> sym = (SymbolicElem<R,S>)(((DerivativeElem) elemA).evalDerivative( elemA , implicitSpace ) );
			return( sym.evalPartialDerivative(withRespectTo, implicitSpace) );
		}
		R lt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.evalPartialDerivative( withRespectTo , implicitSpace );
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivative(withRespectTo, implicitSpace).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				if( elemA instanceof SymbolicMult )
				{
					final SymbolicElem<R,S> ia = ((SymbolicMult<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicMult<R,S>) elemA).getElemB();
					return( ( ia.mult( ib.mult( elemB ) ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				throw( ex );
			}
			lt = ea.mult( elemB.eval(implicitSpace) );
		}
		R rt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.eval(implicitSpace);
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivative(withRespectTo, implicitSpace).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				if( elemA instanceof SymbolicMult )
				{
					final SymbolicElem<R,S> ia = ((SymbolicMult<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicMult<R,S>) elemA).getElemB();
					return( ( ia.mult( ib.mult( elemB ) ) ).evalPartialDerivative( withRespectTo , implicitSpace ) );
				}
				
				throw( ex );
			}
			rt = ea.mult( elemB.evalPartialDerivative( withRespectTo , implicitSpace ) );
		}
		
		return( lt.add( rt ) );
	}

	@Override
	public String writeString( ) {
		return( "mult( " + ( elemA.writeString() ) + " , " + ( elemB.writeString() ) + " )" );
	}
	
	/**
	 * @return the elemA
	 */
	public SymbolicElem<R, S> getElemA() {
		return elemA;
	}

	/**
	 * @return the elemB
	 */
	public SymbolicElem<R, S> getElemB() {
		return elemB;
	}
	
	
	
	@Override
	public boolean symbolicEquals( SymbolicElem<R, S> b )
	{
		if( b instanceof SymbolicMult )
		{
			if( this.getFac().isMultCommutative() )
			{
				boolean aa = this.getElemA().symbolicEquals( ((SymbolicMult<R,S>) b).getElemA() );
				boolean bb = this.getElemB().symbolicEquals( ((SymbolicMult<R,S>) b).getElemB() );
				if( aa && bb )
				{
					return( true );
				}
				
				aa = this.getElemA().symbolicEquals( ((SymbolicMult<R,S>) b).getElemB() );
				bb = this.getElemB().symbolicEquals( ((SymbolicMult<R,S>) b).getElemA() );
				return( aa && bb );
			}
			else
			{
				final ArrayList<SymbolicElem<R,S>> ind0 = new ArrayList<SymbolicElem<R,S>>();
				final ArrayList<SymbolicElem<R,S>> ind1 = new ArrayList<SymbolicElem<R,S>>();
				this.handleMultInsert( ind0 );
				((SymbolicMult) b).handleMultInsert( ind1 );
				if( ind0.size() == ind1.size() )
				{
					int cnt;
					for( cnt = 0 ; cnt < ind0.size() ; cnt++ )
					{
						if( !( ind0.get( cnt ).symbolicEquals( ind1.get( cnt ) ) ) )
						{
							return( false );
						}
					}
					return( true );
				}
			}
		}
		
		return( false );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elemA.performInserts( session );
		elemB.performInserts( session );
		super.performInserts( session );
	}
	
	
	
	public SymbolicMult<R, S> handleMultSimplify( final SymbolicElem<R,S> elA , final SymbolicElem<R,S> elB , final DroolsSession ds )
	{
		final HashSet<Integer> hset = new HashSet<Integer>();
		
		{
			final ArrayList<SymbolicElem<R,S>> ind = new ArrayList<SymbolicElem<R,S>>();
			handleMultInsert( ind );
			int cnt;
			for( cnt = 0 ; cnt < ( ind.size() - 1 ) ; cnt++ )
			{
				final SymbolicElem<R,S> i0 = ind.get( cnt );
				final SymbolicElem<R,S> i1 = ind.get( cnt + 1 );
				if( ( elA == i0 ) && ( elB == i1 ) )
				{
					hset.add( cnt );
					hset.add( cnt + 1 );
				}
			}
		}
		
		
		final int[] index = new int[] { 0 };
		return( handleMultRewrite( index , hset , ds ) );
	}
	
	
	
	private SymbolicMult<R,S> handleMultRewrite( final int[] index , final HashSet<Integer> hset ,
			final DroolsSession session )
	{
		SymbolicElem<R,S> elA = null;
		SymbolicElem<R,S> elB = null;
		
		if( elemA instanceof SymbolicMult )
		{
			elA = ((SymbolicMult) elemA).handleMultRewrite(index, hset, session);
		}
		else
		{
			elA = hset.contains( index[ 0 ] ) ? new SymbolicIdentity<R,S>( fac ) : elemA;
			if( elA != elemA ) session.insert( elA );
			( index[ 0 ] )++;
		}
		
		if( elemB instanceof SymbolicMult )
		{
			elB = ((SymbolicMult) elemB).handleMultRewrite(index, hset, session);
		}
		else
		{
			elB = hset.contains( index[ 0 ] ) ? new SymbolicIdentity<R,S>( fac ) : elemB;
			if( elB != elemB ) session.insert( elB );
			( index[ 0 ] )++;
		}
		
		if( ( elA == elemA ) && ( elB == elemB ) )
		{
			return( this );
		}
		
		SymbolicMult<R,S> ret = new SymbolicMult<R,S>( elA , elB , fac );
		session.insert( ret );
		return( ret );
	}
	
	
	
	private void handleMultInsert( final ArrayList<SymbolicElem<R,S>> ind )
	{
		
		if( elemA instanceof SymbolicMult )
		{
			((SymbolicMult) elemA).handleMultInsert(ind);
		}
		else
		{
			ind.add( elemA );
		}
		
		if( elemB instanceof SymbolicMult )
		{
			((SymbolicMult) elemB).handleMultInsert(ind);
		}
		else
		{
			ind.add( elemB );
		}
		
	}
	

	private SymbolicElem<R,S> elemA;
	private SymbolicElem<R,S> elemB;
	

}

