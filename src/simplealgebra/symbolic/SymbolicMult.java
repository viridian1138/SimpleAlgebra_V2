




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
	
	@Override
	public R eval( ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		if( elemA instanceof DerivativeElem )
		{
			return( ( (DerivativeElem<R,S>) elemA ).evalDerivative( elemB ) );
		}
		R ea = null;
		try
		{
			ea = elemA.eval();
		}
		catch( MultiplicativeDistributionRequiredException ex )
		{
			if( elemA instanceof SymbolicNegate )
			{
				return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).eval().negate() );
			}
			
			if( elemA instanceof SymbolicAdd )
			{
				final SymbolicElem<R,S> ia = ((SymbolicAdd) elemA).getElemA();
				final SymbolicElem<R,S> ib = ((SymbolicAdd) elemA).getElemB();
				return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).eval() );
			}
			
			throw( ex );
		}
		return( ea.mult( elemB.eval() ) );
	}
	
	@Override
	public R evalPartialDerivative( ArrayList<Elem<?, ?>> withRespectTo ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		if( elemA instanceof PartialDerivativeOp )
		{
			ArrayList<Elem<?, ?>> newWrt = new ArrayList<Elem<?, ?>>();
			Iterator<Elem<?,?>> it = ((PartialDerivativeOp) elemA).getWithRespectTo().iterator();
			while( it.hasNext() )
			{
				newWrt.add( it.next() );
			}
			it = withRespectTo.iterator();
			while( it.hasNext() )
			{
				newWrt.add( it.next() );
			}
			return( elemB.evalPartialDerivative(newWrt) );
		}
		if( elemA instanceof DerivativeElem )
		{
			SymbolicElem<R,S> sym = (SymbolicElem<R,S>)(((DerivativeElem) elemA).evalDerivative( elemA ) );
			return( sym.evalPartialDerivative(withRespectTo) );
		}
		R lt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.evalPartialDerivative( withRespectTo );
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivative(withRespectTo).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivative( withRespectTo ) );
				}
				
				throw( ex );
			}
			lt = ea.mult( elemB.eval() );
		}
		R rt = null;
		{
			R ea = null;
			try
			{
				ea = elemA.eval();
			}
			catch( MultiplicativeDistributionRequiredException ex )
			{
				if( elemA instanceof SymbolicNegate )
				{
					return( ((SymbolicNegate<R,S>) elemA).getElem().mult(elemB).evalPartialDerivative(withRespectTo).negate() );
				}
				
				if( elemA instanceof SymbolicAdd )
				{
					final SymbolicElem<R,S> ia = ((SymbolicAdd<R,S>) elemA).getElemA();
					final SymbolicElem<R,S> ib = ((SymbolicAdd<R,S>) elemA).getElemB();
					return( ( ia.mult( elemB ) ).add( ib.mult( elemB ) ).evalPartialDerivative( withRespectTo ) );
				}
				
				throw( ex );
			}
			rt = ea.mult( elemB.evalPartialDerivative( withRespectTo ) );
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
	public SymbolicElem<R, S> handleOptionalOp( Object id , ArrayList<SymbolicElem<R, S>> args ) throws NotInvertibleException
	{
		if( id instanceof SymbolicOps )
		{
			switch( (SymbolicOps) id )
			{
				case DISTRIBUTE_SIMPLIFY:
				{
					SymbolicMult<R,S> ths = this;
					SymbolicElem<R,S> ra = elemA.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
					SymbolicElem<R,S> rb = elemB.handleOptionalOp( SymbolicOps.DISTRIBUTE_SIMPLIFY , null);
					if( ( elemA != ra ) || ( elemB != rb ) )
					{
						ths = new SymbolicMult<R,S>( ra , rb , fac );
					}
					
					if( ths.elemA instanceof SymbolicZero )
					{
						return( ths.elemA );
					}
					
					if( ths.elemB instanceof SymbolicZero )
					{
						return( ths.elemB );
					}
					
					if( ths.elemA instanceof SymbolicIdentity )
					{
						return( ths.elemB );
					}
					
					if( ths.elemB instanceof SymbolicIdentity )
					{
						return( ths.elemA );
					}
					
					if( ths.elemA instanceof SymbolicNegate )
					{
						final SymbolicMult<R,S> am = 
								new SymbolicMult<R,S>( ( (SymbolicNegate<R,S>)(ths.elemA) ).getElem() , ths.elemB , fac );
						return( ( new SymbolicNegate<R,S>( am , fac ) ).handleOptionalOp(SymbolicOps.DISTRIBUTE_SIMPLIFY, args) );
					}
					
					if( this.elemB instanceof SymbolicNegate )
					{
						final SymbolicMult<R,S> am = 
								new SymbolicMult<R,S>( ths.elemA , ( (SymbolicNegate<R,S>)(ths.elemB) ).getElem() , fac );
						return( ( new SymbolicNegate<R,S>( am , fac ) ).handleOptionalOp(SymbolicOps.DISTRIBUTE_SIMPLIFY, args) );
					}
					
					if( ths.elemA instanceof SymbolicAdd )
					{
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					}
					
					if( ths.elemB instanceof SymbolicAdd )
					{
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					}
					
					if( ths.elemA instanceof SymbolicInvertLeft )
					{
						SymbolicElem<R,S> aa = ((SymbolicInvertLeft<R,S>) ths.elemA).getElem();
						if( aa.symbolicEquals( ths.elemB ) )
						{
							return( this.getFac().identity() );
						}
					}
					
					if( ths.elemB instanceof SymbolicInvertRight )
					{
						SymbolicElem<R,S> bb = ((SymbolicInvertRight<R,S>) ths.elemB).getElem();
						if( ths.elemA.symbolicEquals( bb ) )
						{
							return( this.getFac().identity() );
						}
					}
					
					
					if( this.getFac().isMultCommutative() )
					{
						if( ths.elemA instanceof SymbolicInvertRight )
						{
							SymbolicElem<R,S> aa = ((SymbolicInvertRight<R,S>) ths.elemA).getElem();
							if( aa.symbolicEquals( ths.elemB ) )
							{
								return( this.getFac().identity() );
							}
						}
						
						if( ths.elemB instanceof SymbolicInvertLeft )
						{
							SymbolicElem<R,S> bb = ((SymbolicInvertLeft<R,S>) ths.elemB).getElem();
							if( ths.elemA.symbolicEquals( bb ) )
							{
								return( this.getFac().identity() );
							}
						}
					}
					
					return( ths );
				}
				// break;
			}
		}
		
		return( super.handleOptionalOp(id, args) );
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
				boolean aa = this.getElemA().symbolicEquals( ((SymbolicMult<R,S>) b).getElemA() );
				boolean bb = this.getElemB().symbolicEquals( ((SymbolicMult<R,S>) b).getElemB() );
				return( aa && bb );
			}
		}
		
		return( false );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session , int levels )
	{
		if( levels >= 0 )
		{
			elemA.performInserts( session , levels - 1 );
			elemB.performInserts( session , levels - 1 );
			super.performInserts( session , levels );
		}
	}

	private SymbolicElem<R,S> elemA;
	private SymbolicElem<R,S> elemB;
	

}

