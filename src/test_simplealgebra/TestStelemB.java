





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





package test_simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.algo.NewtonRaphsonSingleElem;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElemFactory;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ga.*;
import simplealgebra.ddx.*;




public class TestStelemB extends TestCase {
	
	
	
	protected static final DoubleElem C = new DoubleElem( 0.05 );
	
	
	
	protected static final DoubleElem T_HH = new DoubleElem( 0.0025 );
	
	protected static final DoubleElem X_HH = new DoubleElem( 0.01 );
	
	protected static final DoubleElem[] HH = { T_HH , X_HH };
	
	
	
	protected static final int NUM_X_ITER = 25;
	
	
	protected static final int NUM_T_ITER = 400;
	
	
	protected static double[][] iterArray = new double[ NUM_T_ITER ][ NUM_X_ITER ];
	
	
	
	/**
	 * 0 = T
	 * 1 = X
	 */
	private static double[][] tempArray = new double[ 3 ][ 3 ];
	
	
	
	protected static void performIterationUpdate( DoubleElem dbl )
	{
		tempArray[ 2 ][ 1 ] += dbl.getVal();
	}
	
	
	protected static double getUpdateValue()
	{
		return( tempArray[ 2 ][ 1 ] );
	}

	
	
	
	protected static void fillTempArray( final int tcnt , final int xcnt )
	{
		for( int ta = -1 ; ta < 2 ; ta++ )
		{
			for( int xa = -1 ; xa < 2 ; xa++ )
			{
				final int tv = tcnt + ta;
				final int xv = xcnt + xa;
				double av = 0.0;
				if( ( tv >= 0 )  && ( xv >= 0 ) && 
						( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) )
				{
					av = iterArray[ tv ][ xv ];
				}
				tempArray[ ta + 1 ][ xa + 1 ] = av;
			}
		}
	}
	
	
	
	private static int[][] spatialAssertArray = new int[ 3 ][ 3 ];
	
	
	
	protected static void clearSpatialAssertArray( )
	{
		for( int ta = -1 ; ta < 2 ; ta++ )
		{
			for( int xa = -1 ; xa < 2 ; xa++ )
			{
				spatialAssertArray[ ta + 1 ][ xa + 1 ] = 0;
			}
		}
	}
	
	
	
	private class DDirec extends DirectionalDerivativePartialFactory<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		AElem>
	{
		DoubleElemFactory de;
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2;
		
		public DDirec( 
				final DoubleElemFactory _de ,
				final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _se2 )
		{
			de = _de;
			se2 = _se2;
		}

		public PartialDerivativeOp<
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			AElem> getPartial( BigInteger basisIndex )
		{
			final ArrayList<AElem> wrtX = new ArrayList<AElem>();
			
			wrtX.add( new AElem( de , 1 + basisIndex.intValue() ) );
			
			return( new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem>( se2 , wrtX ) );
		}

	};
	
	
	
	
	private class AElem extends SymbolicElem<DoubleElem, DoubleElemFactory>
	{
		private int col;

		
		public AElem(DoubleElemFactory _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public String writeString() {
			return( "a" + col + "()" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof AElem )
			{
				return( col == ( (AElem) b ).col );
			}
			return( false );
		}
		
		@Override
		public boolean equals( Object b )
		{
			if( b instanceof AElem )
			{
				return( col == ( (AElem) b ).col );
			}
			return( false );
		}
		
		@Override
		public int hashCode()
		{
			return( col );
		}
		
		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	private class SymbolicConst extends SymbolicReduction<DoubleElem, DoubleElemFactory>
	{

		public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public String writeString() {
			return( "const( " + getElem().getVal() + " )" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof SymbolicConst )
			{
				return( getElem().getVal() == ( (SymbolicConst) b ).getElem().getVal() );
			}
			return( false );
		}
		
	}
	
	
	
	private class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>>
	{

		public StelemReduction2L(SymbolicElem<DoubleElem, DoubleElemFactory> _elem, SymbolicElemFactory<DoubleElem, DoubleElemFactory> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public String writeString() {
			return( "reduce2L( " + getElem().writeString() + " )" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>,SymbolicElemFactory<DoubleElem, DoubleElemFactory>> b )
		{
			if( b instanceof StelemReduction2L )
			{
				return( getElem().symbolicEquals( ( (StelemReduction2L) b ).getElem() ) );
			}
			return( false );
		}
		
	}
	
	
	
	private class StelemReduction3L extends SymbolicReduction<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
	{

		public StelemReduction3L(
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _elem, 
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
			super(_elem, _fac);
		}
		
		@Override
		public String writeString() {
			return( "reduce3L( " + getElem().writeString() + " )" );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> b )
		{
			if( b instanceof StelemReduction3L )
			{
				return( getElem().symbolicEquals( ( (StelemReduction3L) b ).getElem() ) );
			}
			return( false );
		}
		
	}
	
	
	
	private class CoeffNode
	{
		private DoubleElem numer;
		private DoubleElem denom;
		
		public CoeffNode( DoubleElem _numer , DoubleElem _denom )
		{
			numer = _numer;
			denom = _denom;
		}
		
		/**
		 * @return the numer
		 */
		public DoubleElem getNumer() {
			return numer;
		}
		/**
		 * @return the denom
		 */
		public DoubleElem getDenom() {
			return denom;
		}
		
	}
	
	
	private class BNelem extends Nelem<DoubleElem,DoubleElemFactory,AElem>
	{

		public BNelem(DoubleElemFactory _fac, HashMap<AElem, BigInteger> _coord) {
			super(_fac, _coord);
		}
		
		
		protected final int[] cols = new int[ 2 ];
		
		protected final boolean[] assertCols = new boolean[ 2 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			Assert.assertTrue( coord.keySet().size() == 2 );
			Iterator<AElem> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				AElem keyCoord = it.next();
				BigInteger coordVal = coord.get( keyCoord );
				cols[ keyCoord.getCol() ] = coordVal.intValue() + 1;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			return( new DoubleElem( TestStelemB.tempArray[ cols[ 0 ] ][ cols[ 1 ] ] ) );
		}

		@Override
		public String writeString() {
			String s0 = "bn";
			Iterator<AElem> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				AElem key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "()";
			return( s0 );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof BNelem )
			{
				BNelem bn = (BNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				Iterator<AElem> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					AElem key = it.next();
					BigInteger ka = coord.get( key );
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
	}
	
	
	
	private class CNelem extends Nelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory>,AElem>
	{

		public CNelem(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, HashMap<AElem, BigInteger> _coord) {
			super(_fac, _coord);
		}

		@Override
		public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( new BNelem( fac.getFac() , coord ) );
		}
		
		
		@Override
		public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
			if( withRespectTo.size() > 1 )
			{
				return( fac.zero() );
			}
			Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
			CNelem wrt = (CNelem)( it.next() );
			final boolean cond = this.symbolicEquals( wrt );
			return( cond ? fac.identity() : fac.zero() );
		}
		

		@Override
		public String writeString() {
			String s0 = "cn";
			Iterator<AElem> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				AElem key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "()";
			return( s0 );
		}
		
		
		@Override
		public boolean symbolicEquals( 
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof CNelem )
			{
				CNelem bn = (CNelem) b;
				if( coord.keySet().size() != bn.coord.keySet().size() )
				{
					return( false );
				}
				Iterator<AElem> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					AElem key = it.next();
					BigInteger ka = coord.get( key );
					BigInteger kb = bn.coord.get( key );
					if( ( ka == null ) || ( kb == null ) )
					{
						return( false );
					}
					if( !( ka.equals( kb ) ) )
					{
						return( false );
					}
				}
				return( true );
			}
			return( false );
		}
		
	}
	
	
	
	private class AStelem extends Stelem<SymbolicElem<DoubleElem,DoubleElemFactory>,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory>,AElem>
	{	
		public AStelem(SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
			super(_fac);
		}

		@Override
		public AStelem cloneInstance() {
			AStelem cl = new AStelem( fac );
			Iterator<AElem> it = partialMap.keySet().iterator();
			while( it.hasNext() )
			{
				AElem key = it.next();
				cl.partialMap.put(key, partialMap.get(key) );
			}
			return( cl );
		}

		@Override
		public SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>, 
			SymbolicElemFactory<DoubleElem,DoubleElemFactory>> eval(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			
			HashMap<AElem,AElem> imp = (HashMap<AElem,AElem>) implicitSpace;
			
			
			HashMap<HashMap<AElem, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<AElem, BigInteger>,CoeffNode>();
			
			
			{
				CoeffNode cf = new CoeffNode( new DoubleElem( 1.0 ) , new DoubleElem( 1.0 ) );
				HashMap<AElem, BigInteger> key = new HashMap<AElem, BigInteger>();
				Iterator<AElem> it = imp.keySet().iterator();
				while( it.hasNext() )
				{
					AElem ae = it.next();
					BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
					key.put( ae , valA );
				}
				spacesA.put( key , cf );
			}
			
			
			{
				Iterator<AElem> it = partialMap.keySet().iterator();
				while( it.hasNext() )
				{
					HashMap<HashMap<AElem, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<AElem, BigInteger>,CoeffNode>();
					final AElem ae = it.next();
					final BigInteger numDerivs = partialMap.get( ae );
					applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
					spacesA = spacesB;
				}
			}
			
			
			
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>, 
				SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
			
			
			{
				Iterator<HashMap<AElem, BigInteger>> it = spacesA.keySet().iterator();
				while( it.hasNext() )
				{
					HashMap<AElem, BigInteger> spaceAe = it.next();
					CoeffNode coeff = spacesA.get( spaceAe );
					final CNelem an0 = 
							new CNelem( fac.getFac() , spaceAe );
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
						an1 = an0.mult( 
								new StelemReduction2L( new SymbolicConst( coeff.getNumer() , fac.getFac().getFac() ) , fac.getFac() ) );
					SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> 
						an2 = an1.mult( 
								( new StelemReduction2L( new SymbolicConst( coeff.getDenom() , fac.getFac().getFac() ) , fac.getFac() ) ).invertLeft() );
					ret = ret.add( an2 );
				}
			}
			
			
			return( ret );
		}

		@Override
		public String writeString() {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		
		
		protected void applyDerivativeAction( HashMap<HashMap<AElem, BigInteger>,CoeffNode> implicitSpacesIn , 
				AElem node , final int numDerivatives , DoubleElem hh ,
				HashMap<HashMap<AElem, BigInteger>,CoeffNode> implicitSpacesOut )
		{
			if( numDerivatives > 3 )
			{
				HashMap<HashMap<AElem, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<AElem, BigInteger>,CoeffNode>();
				applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
				applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
			}
			
			Iterator<HashMap<AElem, BigInteger>> it = implicitSpacesIn.keySet().iterator();
			while( it.hasNext() )
			{
				final HashMap<AElem, BigInteger> implicitSpace = it.next();
				final CoeffNode coeffNodeIn = implicitSpacesIn.get( implicitSpace );
				
				switch( numDerivatives )
				{
				
				case 0:
					{
						applyAdd( implicitSpace , coeffNodeIn , implicitSpacesOut );
					}
					break;
				
				case 1:
					{
						final HashMap<AElem, BigInteger> implicitSpaceOutM1 = new HashMap<AElem, BigInteger>();
						final HashMap<AElem, BigInteger> implicitSpaceOutP1 = new HashMap<AElem, BigInteger>();
						
						Iterator<AElem> itA = implicitSpace.keySet().iterator();
						while( itA.hasNext() )
						{
							AElem ae = itA.next();
							final BigInteger valAe = implicitSpace.get( ae );
							if( node.symbolicEquals( ae ) )
							{
								final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
								final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
								implicitSpaceOutM1.put( ae , valAeM1 );
								implicitSpaceOutP1.put( ae , valAeP1 );
							}
							else
							{
								implicitSpaceOutM1.put( ae , valAe );
								implicitSpaceOutP1.put( ae , valAe );
							}
						}
						
						final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
								coeffNodeIn.getDenom().mult( hh ).mult( new DoubleElem( 2.0 ) ) );
						final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
								coeffNodeIn.getDenom().mult( hh ).mult( new DoubleElem( 2.0 ) ) );
						
						applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
						applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
					}
					break;
					
				case 2:
					{
						final HashMap<AElem, BigInteger> implicitSpaceOutM1 = new HashMap<AElem, BigInteger>();
						final HashMap<AElem, BigInteger> implicitSpaceOutP1 = new HashMap<AElem, BigInteger>();
						
						Iterator<AElem> itA = implicitSpace.keySet().iterator();
						while( itA.hasNext() )
						{
							AElem ae = itA.next();
							final BigInteger valAe = implicitSpace.get( ae );
							if( node.symbolicEquals( ae ) )
							{
								final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
								final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
								implicitSpaceOutM1.put( ae , valAeM1 );
								implicitSpaceOutP1.put( ae , valAeP1 );
							}
							else
							{
								implicitSpaceOutM1.put( ae , valAe );
								implicitSpaceOutP1.put( ae , valAe );
							}
						}
						
						final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer() , 
								coeffNodeIn.getDenom().mult( hh ).mult( hh ) );
						final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( new DoubleElem( 2.0 ) ) , 
								coeffNodeIn.getDenom().mult( hh ).mult( hh ) );
						final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
								coeffNodeIn.getDenom().mult( hh ).mult( hh ) );
						
						applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
						applyAdd( implicitSpace , coeffNodeOut , implicitSpacesOut );
						applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
					}
					break;
					
				case 3:
				{
					final HashMap<AElem, BigInteger> implicitSpaceOutM1 = new HashMap<AElem, BigInteger>();
					final HashMap<AElem, BigInteger> implicitSpaceOutP1 = new HashMap<AElem, BigInteger>();
					final HashMap<AElem, BigInteger> implicitSpaceOutM2 = new HashMap<AElem, BigInteger>();
					final HashMap<AElem, BigInteger> implicitSpaceOutP2 = new HashMap<AElem, BigInteger>();
					
					Iterator<AElem> itA = implicitSpace.keySet().iterator();
					while( itA.hasNext() )
					{
						AElem ae = itA.next();
						final BigInteger valAe = implicitSpace.get( ae );
						if( node.symbolicEquals( ae ) )
						{
							final BigInteger valAeM1 = valAe.subtract( BigInteger.ONE );
							final BigInteger valAeP1 = valAe.add( BigInteger.ONE );
							final BigInteger valAeM2 = valAe.subtract( BigInteger.valueOf( 2 ) );
							final BigInteger valAeP2 = valAe.add( BigInteger.valueOf( 2 ) );
							implicitSpaceOutM1.put( ae , valAeM1 );
							implicitSpaceOutP1.put( ae , valAeP1 );
							implicitSpaceOutM2.put( ae , valAeM2 );
							implicitSpaceOutP2.put( ae , valAeP2 );
						}
						else
						{
							implicitSpaceOutM1.put( ae , valAe );
							implicitSpaceOutP1.put( ae , valAe );
							implicitSpaceOutM2.put( ae , valAe );
							implicitSpaceOutP2.put( ae , valAe );
						}
					}
					
					final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( new DoubleElem( 2.0 ) ) , 
							coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( new DoubleElem( 2.0 ) ) );
					final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new DoubleElem( 2.0 ) ) , 
							coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( new DoubleElem( 2.0 ) ) );
					final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
							coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( new DoubleElem( 2.0 ) ) );
					final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
							coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( new DoubleElem( 2.0 ) ) );
					
					applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
					applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
					applyAdd( implicitSpaceOutM2 , coeffNodeOutM2 , implicitSpacesOut );
					applyAdd( implicitSpaceOutP2 , coeffNodeOutP2 , implicitSpacesOut );
				}
				break;
					
				}
			}
		}
		
		
		protected void applyAdd( 
				HashMap<AElem, BigInteger> implicitSpace , CoeffNode node ,
				HashMap<HashMap<AElem, BigInteger>,CoeffNode> implicitSpacesOut )
		{
			CoeffNode prev = implicitSpacesOut.get( implicitSpace );
			
			if( prev == null )
			{
				implicitSpacesOut.put( implicitSpace , node );
				return;
			}
			
			if( prev.getDenom().getVal() == node.getDenom().getVal() )
			{
				DoubleElem outN = node.getNumer().add( prev.getNumer() );
				CoeffNode nxt = new CoeffNode( outN , prev.getDenom() );
				implicitSpacesOut.put( implicitSpace , nxt );
				return;
			}
			
			
			DoubleElem outDenom = prev.getDenom().mult( node.getDenom() );
			
			DoubleElem outNumer = ( node.getDenom().mult( prev.getNumer() ) ).add( prev.getDenom().mult( node.getNumer() ) );
			
			CoeffNode nxt = new CoeffNode( outNumer , outDenom );
			
			implicitSpacesOut.put( implicitSpace , nxt );
		}
		
		
		
	}
	
	
	
	
	protected class StelemNewton extends NewtonRaphsonSingleElem<DoubleElem,DoubleElemFactory>
	{

		public StelemNewton(
				SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _function,
				ArrayList<? extends Elem<?, ?>> _withRespectTo, 
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super(_function, _withRespectTo, implicitSpaceFirstLevel);
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}
		
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( DoubleElem iterationOffset )
		{
			TestStelemB.performIterationUpdate( iterationOffset );
		}
		
	}
	
	
	
	
	public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final Random rand = new Random( 3344 );
		
		final double d1 = X_HH.getVal();
		
		final TestDimensionOne tdim = new TestDimensionOne();
		
		
		
		for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
		{
			// for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			// {
			//	iterArray[ tcnt ][ xcnt ] = rand.nextDouble();
			// }
			iterArray[ tcnt ][ 12 ] = 10000.0 * ( d1 * d1 );
		}
		
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final SymbolicElemFactory<DoubleElem, DoubleElemFactory> se = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2 =
				new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( se );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2 );
		
		
		final AStelem as = new AStelem( se2 );
		
		final ArrayList<AElem> wrtT = new ArrayList<AElem>();
		
		wrtT.add( new AElem( de , 0 ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final GeometricAlgebraMultivectorElemFactory<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
			new GeometricAlgebraMultivectorElemFactory<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3 , tdim );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				g0 = new GeometricAlgebraMultivectorElem<
					TestDimensionOne, 
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( se3 , tdim );
		
		g0.setVal( new HashSet<BigInteger>() , as );
		
		
		final DDirec ddirec = new DDirec(de, se2);
		
		final DirectionalDerivative<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			AElem>
			del =
			new DirectionalDerivative<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			AElem>( 
					ge , 
					tdim ,
					ddirec );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				del0 = del.eval( null );
		
		
		
		final PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem> pa0T 
			= new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem>( se2 , wrtT );
		
		// final PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem> pa0X 
		//	= new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,AElem>( se2 , wrtX );
	
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m0T
			= pa0T.mult( as ); 
		
		// SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m0X
		//	= pa0X.mult( as ); 
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gxx0 = del0.mult( g0 );
		
		final ArrayList<GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>> args0 
				= new ArrayList<GeometricAlgebraMultivectorElem<
					TestDimensionOne, 
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>>();
		args0.add( gxx0 );
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gxx1 = del0.handleOptionalOp( GeometricAlgebraMultivectorElem.GeometricAlgebraMultivectorCmd.DOT , args0 );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1T
			= pa0T.mult( m0T );
		
		//SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1X
		//	= pa0X.mult( m0X );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> gtt0
			= m1T.mult( 
					( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( C.mult( C ) , de ) , se ) , se2 )
							).invertLeft() ).negate();
		
		
		// SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		//	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1
		//	= m1X.add( gtt0 );
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gtt = new GeometricAlgebraMultivectorElem<
					TestDimensionOne, 
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( se3 , tdim );
		
		gtt.setVal( new HashSet<BigInteger>() , gtt0 );
		
		
		
		final GeometricAlgebraMultivectorElem<
			TestDimensionOne, 
			SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1 = gxx1.add( gtt );
		
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> m1
			= mg1.get( new HashSet<BigInteger>() );
		
		
		
		
		
		
		
		final HashMap<AElem,AElem> implicitSpace0 = new HashMap<AElem,AElem>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new AElem( de , 0 ) , new AElem( de , 0 ) );
		implicitSpace0.put( new AElem( de , 1 ) , new AElem( de , 0 ) );
		
		final SymbolicElem<
			SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> s0 = m1.eval( implicitSpace2 );
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final ArrayList<Elem<?, ?>> wrt3 = new ArrayList<Elem<?, ?>>();
		{
			final HashMap<AElem, BigInteger> coord = new HashMap<AElem, BigInteger>();
			coord.put( new AElem( de , 0 ) , BigInteger.valueOf( 1 ) );
			coord.put( new AElem( de , 1 ) , BigInteger.valueOf( 0 ) );
			wrt3.add( new CNelem( se , coord ) );
		}
		
		
		
		StelemNewton newton = new StelemNewton( s0 , wrt3 , implicitSpace2 );
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - 1 ) ; tval++ )
		{
			for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			{
				iterArray[ tval + 1 ][ xcnt ] = iterArray[ tval ][ xcnt ];
			}
			
			for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			{
				fillTempArray( tval , xcnt );
				clearSpatialAssertArray();
				
								
				
				
				
				
				final double ival = TestStelemB.getUpdateValue();
				
				
			
				
				DoubleElem err = newton.eval( implicitSpace2 );
		
		
				final double val = TestStelemB.getUpdateValue();
				
				if( xcnt == 12 )
				{
					System.out.println( "******************" );
					System.out.println( xcnt );
					System.out.println( ival );
					System.out.println( val );
					System.out.println( "## " + ( err.getVal() ) );
				}
				
				
				Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ] == 0 );
				
				Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ] > 0 );
				
				Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ] > 0 );
				Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ] > 0 );
				
				Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ] > 0 );
				Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ] > 0 );
				
				
				Assert.assertTrue( Math.abs( err.getVal() ) < ( 0.01 * Math.abs( val ) + 0.01 ) );
				
			
				iterArray[ tval + 1 ][ xcnt ] = val;
						
			}
			
		}
		
		System.out.println( "==============================" );
		System.out.println( iterArray[ NUM_T_ITER - 1 ][ 10 ] );
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	

	
}


