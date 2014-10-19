package test_simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.NotInvertibleException;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;




public class TestStelem extends TestCase {
	
	private class AElem extends SymbolicElem<DoubleElem, DoubleElemFactory>
	{
		private int col;

		
		public AElem(DoubleElemFactory _fac, int _col) {
			super(_fac);
			col = _col;
		}

		@Override
		public DoubleElem eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace)
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
		
		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}
		
	}
	
	
	
	
	private class SymbolicConst extends SymbolicElem<DoubleElem, DoubleElemFactory>
	{
		private DoubleElem elem;

		
		public SymbolicConst(DoubleElemFactory _fac, DoubleElem _elem) {
			super(_fac);
			elem = _elem;
		}

		@Override
		public DoubleElem eval( HashMap<Elem<?,?>,Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( elem );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<Elem<?, ?>> withRespectTo , HashMap<Elem<?,?>,Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			return( fac.zero() );
		}

		@Override
		public String writeString() {
			return( "" + elem.getVal() );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof SymbolicConst )
			{
				return( elem.getVal() == ( (SymbolicConst) b ).getElem().getVal() );
			}
			return( false );
		}
		
		/**
		 * @return the elem
		 */
		public DoubleElem getElem() {
			return elem;
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
	
	
	private class ANelem extends Nelem<DoubleElem,DoubleElemFactory,AElem>
	{

		public ANelem(DoubleElemFactory _fac, HashMap<AElem, BigInteger> _coord) {
			super(_fac, _coord);
		}

		@Override
		public DoubleElem eval(HashMap<Elem<?, ?>, Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			double sum = 0.0;
			HashMap ims = implicitSpace;
			HashMap<AElem,AElem> imp = (HashMap<AElem,AElem>) ims;
			Iterator<AElem> it = imp.keySet().iterator();
			while( it.hasNext() )
			{
				AElem keyCoord = it.next();
				AElem coordVal = imp.get( keyCoord );
				double col = coordVal.getCol();
				BigInteger cv = coord.get( keyCoord );
				if( cv != null )
				{
					col += cv.doubleValue();
				}
				double stval = ( col + 1 ) * ( col + 1 ) * ( col + 1 );
				sum += stval;
			}
			return( new DoubleElem( sum ) );
		}

		@Override
		public String writeString() {
			String s0 = "an";
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
			if( b instanceof ANelem )
			{
				ANelem bn = (ANelem) b;
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
	
	
	private class AStelem extends Stelem<DoubleElem,DoubleElemFactory,AElem>
	{

		final DoubleElem h = new DoubleElem( 0.01 );
		
		public AStelem(SymbolicElemFactory<DoubleElem, DoubleElemFactory> _fac) {
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
		public SymbolicElem<DoubleElem, DoubleElemFactory> eval(
				HashMap<Elem<?, ?>, Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			
			
			HashMap ims = implicitSpace;
			HashMap<AElem,AElem> imp = (HashMap<AElem,AElem>) ims;
			
			
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
					applyDerivativeAction( spacesA , ae , numDerivs.intValue() , h , spacesB );
					spacesA = spacesB;
				}
			}
			
			
			
			SymbolicElem<DoubleElem, DoubleElemFactory> ret = fac.zero();
			
			
			{
				Iterator<HashMap<AElem, BigInteger>> it = spacesA.keySet().iterator();
				while( it.hasNext() )
				{
					HashMap<AElem, BigInteger> spaceAe = it.next();
					CoeffNode coeff = spacesA.get( spaceAe );
					ANelem an0 = new ANelem( fac.getFac() , spaceAe );
					SymbolicElem<DoubleElem, DoubleElemFactory> an1 = an0.mult( new SymbolicConst( fac.getFac() , coeff.getNumer() ) );
					SymbolicElem<DoubleElem, DoubleElemFactory> an2 = an1.mult( 
							( new SymbolicConst( fac.getFac() , coeff.getDenom() ) ).invertLeft() );
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
				AElem node , final int numDerivatives , DoubleElem h ,
				HashMap<HashMap<AElem, BigInteger>,CoeffNode> implicitSpacesOut )
		{
			if( numDerivatives > 3 )
			{
				HashMap<HashMap<AElem, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<AElem, BigInteger>,CoeffNode>();
				applyDerivativeAction(implicitSpacesIn, node, 3, h, implicitSpacesMid);
				applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, h, implicitSpacesOut);
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
								coeffNodeIn.getDenom().mult( h ).mult( new DoubleElem( 2.0 ) ) );
						final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
								coeffNodeIn.getDenom().mult( h ).mult( new DoubleElem( 2.0 ) ) );
						
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
								coeffNodeIn.getDenom().mult( h ).mult( h ) );
						final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( new DoubleElem( 2.0 ) ) , 
								coeffNodeIn.getDenom().mult( h ).mult( h ) );
						final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
								coeffNodeIn.getDenom().mult( h ).mult( h ) );
						
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
							coeffNodeIn.getDenom().mult( h ).mult( h ).mult( h ).mult( new DoubleElem( 2.0 ) ) );
					final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( new DoubleElem( 2.0 ) ) , 
							coeffNodeIn.getDenom().mult( h ).mult( h ).mult( h ).mult( new DoubleElem( 2.0 ) ) );
					final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
							coeffNodeIn.getDenom().mult( h ).mult( h ).mult( h ).mult( new DoubleElem( 2.0 ) ) );
					final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
							coeffNodeIn.getDenom().mult( h ).mult( h ).mult( h ).mult( new DoubleElem( 2.0 ) ) );
					
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
	
	
	
	public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		
		DoubleElemFactory de = new DoubleElemFactory();
		
		SymbolicElemFactory<DoubleElem, DoubleElemFactory> se = new SymbolicElemFactory<DoubleElem, DoubleElemFactory>( de );
		
		AStelem as = new AStelem( se );
		
		ArrayList<AElem> wrt = new ArrayList<AElem>();
		
		wrt.add( new AElem( de , 0 ) );
		
		PartialDerivativeOp<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>,AElem> pa0 
			= new PartialDerivativeOp<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>,AElem>( se , wrt );
		
		PartialDerivativeOp<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>,AElem> pa1 
			= new PartialDerivativeOp<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>,AElem>( se , wrt );
		
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> m0
			= pa0.mult( as );
		
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> m1
			= pa1.mult( m0 );
		
		HashMap<AElem,AElem> implicitSpace0 = new HashMap<AElem,AElem>();
		
		HashMap implicitSpace1 = implicitSpace0;
		
		HashMap<Elem<?,?>,Elem<?,?>> implicitSpace2 = (HashMap<Elem<?,?>,Elem<?,?>>) implicitSpace1;
		
		implicitSpace0.put( new AElem( de , 0 ) , new AElem( de , 0 ) );
		
		SymbolicElem<DoubleElem,DoubleElemFactory> s0 = m1.eval( implicitSpace2 );
		
		String s = s0.writeString();
		
		System.out.println( s );
		
		DoubleElem dbl = s0.eval( implicitSpace2 );
		
		// Assert.assertEquals( 100.0 , dbl.getVal() , 1E-4 );
		
		System.out.println( dbl.getVal() );
		
	}
	

	
}


