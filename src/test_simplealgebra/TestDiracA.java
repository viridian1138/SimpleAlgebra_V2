




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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.algo.*;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ddx.*;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.ga.*;








public class TestDiracA extends TestCase {
	
	
	private static final DoubleElem DOUBLE_ZERO = new DoubleElem( 0.0 );
	
	private static final DoubleElem MM = genFromConstDbl( 2.0 );
	
	private static final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> II = genII();
	
	
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genII( )
	{
		final DoubleElem dd = new DoubleElem( 1.0 );
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		HashSet<BigInteger> ind = new HashSet<BigInteger>();
		ind.add( BigInteger.ZERO );
		ind.add( BigInteger.ONE );
		ind.add( BigInteger.valueOf( 2 ) );
		ind.add( BigInteger.valueOf( 3 ) );
		ret.setVal( ind , dd );
		return( ret );
	}
	
	
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genFromConst( double in )
	{
		final DoubleElem dd = new DoubleElem( in );
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour()  , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		ret.setVal( new HashSet<BigInteger>() , dd );
		return( ret );
	}
	
	
	
	private static final Random rand = new Random( 4455 );
	
	
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genDiffAll( double in )
	{
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour()  , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final DoubleElem dd = new DoubleElem( in * ( 2.0 * rand.nextDouble() ) );
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
			if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
			if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
			if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
			if( /* ( ( hs.size() ) % 2 ) == 0 */ true )
			{
				ret.setVal( hs , dd );
			}
		}
		return( ret );
	}
	
	
	
	private static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genDiffEnt( double in )
	{
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour()  , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final DoubleElem dd = new DoubleElem( in * ( 2.0 * rand.nextDouble() ) );
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
			if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
			if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
			if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
			if( ( ( hs.size() ) % 2 ) == 0 )
			{
				ret.setVal( hs , dd );
			}
			else
			{
				final DoubleElem dd2 = new DoubleElem( 1E-8 * ( 2.0 * rand.nextDouble() ) );
				ret.setVal( hs , dd2 );
			}
		}
		return( ret );
	}
	
	
	private static DoubleElem genFromConstDbl( double in )
	{
		return( new DoubleElem( in ) );
	}
	
	
	
	protected static final DoubleElem HBAR = genFromConstDbl( 0.5 );
	
	protected static final DoubleElem C = genFromConstDbl( 0.004 );
	
	
	
	protected static final DoubleElem T_HH = genFromConstDbl( 0.0025 );
	
	protected static final DoubleElem X_HH = genFromConstDbl( 0.01 );
	
	protected static final DoubleElem Y_HH = genFromConstDbl( 0.01 );
	
	protected static final DoubleElem Z_HH = genFromConstDbl( 0.01 );
	
	protected static final DoubleElem[] HH = { T_HH , X_HH , Y_HH , Z_HH };
	
	
	
	
	
	
	protected static final int NUM_T_ITER = 7;
	
	protected static final int NUM_X_ITER = 10;
	
	protected static final int NUM_Y_ITER = 10;
	
	protected static final int NUM_Z_ITER = 10;
	
	
	
	
	protected static final GeometricAlgebraMultivectorElem[][][][] iterArray = new GeometricAlgebraMultivectorElem[ NUM_T_ITER ][ NUM_X_ITER ][ NUM_Y_ITER ][ NUM_Z_ITER ];
	
	
	
	protected static final int TV = 0;
	protected static final int XV = 1;
	protected static final int YV = 2;
	protected static final int ZV = 3;
	
	
	
	/**
	 * 0 = T
	 * 1 = X
	 * 2 = Y
	 * 3 = Z
	 */
	private static GeometricAlgebraMultivectorElem[][][][] tempArray = new GeometricAlgebraMultivectorElem[ 3 ][ 3 ][ 3 ][ 3 ];
	
	
	
	
	
	
	protected static void performIterationUpdate( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> dbl )
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> va
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ 2 ][ 1 ][ 1 ][ 1 ] );
		if( va == null )
		{
			tempArray[ 2 ][ 1 ][ 1 ][ 1 ] = dbl;
			return;
		}
		tempArray[ 2 ][ 1 ][ 1 ][ 1 ] = va.add( dbl );
	}
	
	
	
	
	protected static GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> getUpdateValue()
	{
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> va
			= (GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>)( tempArray[ 2 ][ 1 ][ 1 ][ 1 ] );
		if( va != null )
		{
			return( va );
		}
		DoubleElemFactory da = new DoubleElemFactory();
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
			ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		return( ret );
	}
	
	
	
	
	protected static void fillTempArray( final int tcnt , final int xcnt , final int ycnt , final int zcnt )
	{
		for( int ta = -1 ; ta < 2 ; ta++ )
		{
			for( int xa = -1 ; xa < 2 ; xa++ )
			{
				for( int ya = -1 ; ya < 2 ; ya++ )
				{
					for( int za = -1 ; za < 2 ; za++ )
					{
						final int tv = tcnt + ta;
						final int xv = xcnt + xa;
						final int yv = ycnt + ya;
						final int zv = zcnt + za;
						GeometricAlgebraMultivectorElem av = null;
						if( ( tv >= 0 )  && ( xv >= 0 ) && ( yv >= 0 ) && ( zv >= 0 ) &&
							( tv < NUM_T_ITER ) && ( xv < NUM_X_ITER ) && ( yv < NUM_Y_ITER ) && ( zv < NUM_Z_ITER )  )
						{
							av = iterArray[ tv ][ xv ][ yv ][ zv ];
						}
						tempArray[ ta + 1 ][ xa + 1 ][ ya + 1 ][ za + 1 ] = av;
					}
				}
			}
		}
	}
	
	
	
	
	
	private static int[][][][] spatialAssertArray = new int[ 3 ][ 3 ][ 3 ][ 3 ];
	
	
	
	protected static void clearSpatialAssertArray( )
	{
		for( int ta = -1 ; ta < 2 ; ta++ )
		{
			for( int xa = -1 ; xa < 2 ; xa++ )
			{
				for( int ya = -1 ; ya < 2 ; ya++ )
				{
					for( int za = -1 ; za < 2 ; za++ )
					{
						spatialAssertArray[ ta + 1 ][ xa + 1 ][ ya + 1 ][ za + 1 ] = 0;
					}
				}
			}
		}
	}
	
	
	
	
	
	
private GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genUnitVector( int axis )
{
	final DoubleElem dd = new DoubleElem( 1.0 );
	DoubleElemFactory da = new DoubleElemFactory();
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
		ret = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( da , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	HashSet<BigInteger> ind = new HashSet<BigInteger>();
	ind.add( BigInteger.valueOf( axis ) );
	ret.setVal( ind , dd );
	return( ret );
}





private GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> genTimeBivector( int axis )
{
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>  st = genUnitVector( axis );
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> st0 = genUnitVector( 0 );
	return( st.mult( st0 ) );
}
	
	


	
	
private double calcMagnitudeSq( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> in )
{
	double db = 0.0;
	Iterator<HashSet<BigInteger>> it = in.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> elem = it.next();
		final DoubleElem dbl = in.get( elem );
		final double vl = dbl.getVal();
		db += vl * vl;
	}
	return( db );
}




private double calcMagnitudeSq( DoubleElem in )
{
	double db = in.getVal();
	return( db * db );
}




private boolean calcEq( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> a , GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> b )
{
	Iterator<HashSet<BigInteger>> it = a.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> elem = it.next();
		final DoubleElem dbla = a.get( elem );
		final DoubleElem dblb = b.get( elem );
		if( ( dbla == null ) || ( dblb == null ) )
		{
			return( false );
		}
		if( dbla.getVal() != dblb.getVal() )
		{
			return( false );
		}
	}
	it = b.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> elem = it.next();
		final DoubleElem dbla = a.get( elem );
		final DoubleElem dblb = b.get( elem );
		if( ( dbla == null ) || ( dblb == null ) )
		{
			return( false );
		}
		if( dbla.getVal() != dblb.getVal() )
		{
			return( false );
		}
	}
	
	return( true );
}
	


/**
 * Defines a directional derivative for the test.
 * 
 * @author thorngreen
 *
 */
private class DDirec extends DirectionalDerivativePartialFactory<
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
	Ordinate>
{

	/**
	 * Factory for building the value of the derivative of an ordinate.
	 */
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> de;
	
	/**
	 * Factory for the enclosed type.
	 */
	SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2;
	
	/**
	 * Constructs the directional derivative factory.
	 * 
	 * @param _de Factory for building the value of the derivative of an ordinate.
	 * @param _se2 Factory for the enclosed type.
	 */
	public DDirec( 
			final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> _de ,
			final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _se2 )
	{
		de = _de;
		se2 = _se2;
	}

	@Override
	public SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			getPartial( BigInteger basisIndex )
	{
		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
		
		wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
		
		SymbolicElem<
		SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
			ret =
					new PartialDerivativeOp<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
					SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,Ordinate>( se2 , wrtX );
		
		if( basisIndex.equals( BigInteger.ZERO ) )
		{
			try
			{
				final DoubleElemFactory de2 = new DoubleElemFactory();
				final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de2 );
				final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
				final DoubleElem cinv = C.invertLeft();
				final SymbolicElem<
				SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,
				SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>
					cmul = ( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( 
							cinv , de2 ) , seA ) , se2A )
							);
				ret = cmul.mult( ret );
			}
			catch( NotInvertibleException ex )
			{
				Assert.assertNull( ex );
			}
		}
			
		
		return( ret );
	}

};



/**
 * Node representing an ordinate of the coordinate space.
 * 
 * @author thorngreen
 *
 */
private class Ordinate extends SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>
{
	/**
	 * The number of the ordinate.
	 */
	private int col;

	/**
	 * Constructs the node.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _col The number of the ordinate.
	 */
	public Ordinate(GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> _fac, int _col) {
		super(_fac);
		col = _col;
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "a" + col + "()" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof Ordinate )
		{
			return( col == ( (Ordinate) b ).col );
		}
		return( false );
	}
	
	@Override
	public boolean equals( Object b )
	{
		if( b instanceof Ordinate )
		{
			return( col == ( (Ordinate) b ).col );
		}
		return( false );
	}
	
	@Override
	public int hashCode()
	{
		return( col );
	}
	
	/**
	 * Returns the number of the ordinate.
	 * 
	 * @return The number of the ordinate.
	 */
	public int getCol() {
		return col;
	}
	
}


/**
 * A symbolic elem representing a constant value.
 * 
 * @author thorngreen
 *
 */
private class SymbolicConst extends SymbolicReduction<DoubleElem,DoubleElemFactory>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public SymbolicConst(DoubleElem _elem, DoubleElemFactory _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( " " + ( this.getElem().getVal() ) );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
	{
		if( b instanceof SymbolicConst )
		{
			final boolean eq = ( getElem().getVal() ) == ( ( (SymbolicConst) b ).getElem().getVal() );
			return( eq );
		}
		return( false );
	}
	
}
	
	
	
private class StelemReduction2L extends SymbolicReduction<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
{

	public StelemReduction2L( SymbolicElem<DoubleElem,DoubleElemFactory> _elem, 
			SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public void writeString( PrintStream ps ) {
		ps.print( "reduce2L( " );
		getElem().writeString( ps );
		ps.print( " )" );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
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
	public void writeString( PrintStream ps ) {
		ps.print( "reduce3L( " );
		getElem().writeString( ps );
		ps.print( " )" );
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
	


/**
 * A constant coefficient with a numerator and a denominator.
 * 	
 * @author thorngreen
 *
 */
private class CoeffNode
{
	/**
	 * The numerator.
	 */
	private DoubleElem numer;
	
	/**
	 * The denominator.
	 */
	private DoubleElem denom;
	
	/**
	 * Constructs the coefficient.
	 * 
	 * @param _numer The numerator.
	 * @param _denom The denominator.
	 */
	public CoeffNode( DoubleElem _numer , DoubleElem _denom )
	{
		numer = _numer;
		denom = _denom;
	}
	
	/**
	 * Gets the numerator.
	 * 
	 * @return The numerator.
	 */
	public DoubleElem getNumer() {
		return numer;
	}
	
	/**
	 * Gets the denominator.
	 * 
	 * @return The denominator.
	 */
	public DoubleElem getDenom() {
		return denom;
	}
	
}


	
	private class BNelem extends Nelem<DoubleElem,DoubleElemFactory,Ordinate>
	{
		protected HashSet<BigInteger> index;
		

		public BNelem(DoubleElemFactory _fac, HashMap<Ordinate, BigInteger> _coord , HashSet<BigInteger> _index ) {
			super(_fac, _coord);
			index = _index;
		}
		
		
		protected final int[] cols = new int[ 4 ];
		
		protected final boolean[] assertCols = new boolean[ 4 ];
		

		@Override
		public DoubleElem eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			cols[ 0 ] = 0;
			cols[ 1 ] = 0;
			cols[ 2 ] = 0;
			cols[ 3 ] = 0;
			assertCols[ 0 ] = false;
			assertCols[ 1 ] = false;
			assertCols[ 2 ] = false;
			assertCols[ 3 ] = false;
			Assert.assertTrue( coord.keySet().size() == 4 );
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate keyCoord = it.next();
				BigInteger coordVal = coord.get( keyCoord );
				cols[ keyCoord.getCol() ] = coordVal.intValue() + 1;
				assertCols[ keyCoord.getCol() ] = true;
			}
			( spatialAssertArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ] )++;
			Assert.assertTrue( assertCols[ 0 ] );
			Assert.assertTrue( assertCols[ 1 ] );
			Assert.assertTrue( assertCols[ 2 ] );
			Assert.assertTrue( assertCols[ 3 ] );
			DoubleElem ret = null;
			GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> val = 
					TestDiracA.tempArray[ cols[ 0 ] ][ cols[ 1 ] ][ cols[ 2 ] ][ cols[ 3 ] ];
			if( val == null )
			{
				ret = fac.zero();
			}
			else
			{
				DoubleElem valA = val.get( index );
				if( valA == null )
				{
					ret = fac.zero();
				}
				else
				{
					ret = valA;
				}
			}
			return( ret );
		}

		@Override
		public void writeString( PrintStream ps ) {
			String s0 = "bn";
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
				BigInteger val = coord.get( key );
				s0 = s0 + "[";
				s0 = s0 + key.getCol();
				s0 = s0 + ",";
				s0 = s0 + val.intValue();
				s0 = s0 + "]";
			}
			s0 = s0 + "{";
			Iterator<BigInteger> ita = index.iterator();
			while( ita.hasNext() )
			{
				BigInteger b = ita.next();
				s0 = s0 + b + ",";
			}
			s0 = s0 + "}()";
			ps.print( s0 );
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
				Iterator<Ordinate> it = coord.keySet().iterator();
				while( it.hasNext() )
				{
					Ordinate key = it.next();
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
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{
	protected HashSet<BigInteger> index;

	public CNelem(SymbolicElemFactory<DoubleElem,DoubleElemFactory> _fac, 
			HashMap<Ordinate, BigInteger> _coord , HashSet<BigInteger> _index ) {
		super(_fac, _coord);
		index = _index;
	}

	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> eval(HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		return( new BNelem( fac.getFac() , coord , index ) );
	}
	
	
	@Override
	public SymbolicElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws MultiplicativeDistributionRequiredException, NotInvertibleException {
		if( withRespectTo.size() > 1 )
		{
			return( fac.zero() );
		}
		Iterator<? extends Elem<?,?>> it = withRespectTo.iterator();
		CNelem wrt = (CNelem)( it.next() );
		// final boolean cond = this.symbolicDEval( wrt );
		final boolean cond = this.symbolicEquals( wrt );
		return( cond ? fac.identity() : fac.zero() );
	}
	

	@Override
	public void writeString( PrintStream ps ) {
		String s0 = "cn";
		Iterator<Ordinate> it = coord.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate key = it.next();
			BigInteger val = coord.get( key );
			s0 = s0 + "[";
			s0 = s0 + key.getCol();
			s0 = s0 + ",";
			s0 = s0 + val.intValue();
			s0 = s0 + "]";
		}
		s0 = s0 + "()";
		ps.print( s0 );
	}
	
	
	protected boolean symbolicDEval( 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof CNelem )
		{
			CNelem bn = (CNelem) b;
			if( coord.keySet().size() != bn.coord.keySet().size() )
			{
				return( false );
			}
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
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
	
	
	@Override
	public boolean symbolicEquals( 
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof CNelem )
		{
			CNelem bn = (CNelem) b;
			if( !( index.equals( bn.index ) ) )
			{
				return( false );
			}
			if( coord.keySet().size() != bn.coord.keySet().size() )
			{
				return( false );
			}
			Iterator<Ordinate> it = coord.keySet().iterator();
			while( it.hasNext() )
			{
				Ordinate key = it.next();
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
	SymbolicElemFactory<DoubleElem,DoubleElemFactory>,Ordinate>
{	
	
protected HashSet<BigInteger> index;
	
public AStelem( SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _fac,
		HashSet<BigInteger> _index ) {
	super(_fac);
	index = _index;
}

@Override
public AStelem cloneInstance() {
	AStelem cl = new AStelem( fac , index );
	Iterator<Ordinate> it = partialMap.keySet().iterator();
	while( it.hasNext() )
	{
		Ordinate key = it.next();
		cl.partialMap.put(key, partialMap.get(key) );
	}
	return( cl );
}

@Override
public SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	
	HashMap<Ordinate,Ordinate> imp = (HashMap<Ordinate,Ordinate>) implicitSpace;
	
	
	HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesA = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
	
	
	{
		CoeffNode cf = new CoeffNode( genFromConstDbl( 1.0 ) , genFromConstDbl( 1.0 ) );
		HashMap<Ordinate, BigInteger> key = new HashMap<Ordinate, BigInteger>();
		Iterator<Ordinate> it = imp.keySet().iterator();
		while( it.hasNext() )
		{
			Ordinate ae = it.next();
			BigInteger valA = BigInteger.valueOf( imp.get( ae ).getCol() );
			key.put( ae , valA );
		}
		spacesA.put( key , cf );
	}
	
	
	{
		Iterator<Ordinate> it = partialMap.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> spacesB = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
			final Ordinate ae = it.next();
			final BigInteger numDerivs = partialMap.get( ae );
			applyDerivativeAction( spacesA , ae , numDerivs.intValue() , HH[ ae.getCol() ] , spacesB );
			spacesA = spacesB;
		}
	}
	
	
	
	SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> ret = fac.zero();
	
	
	{
		Iterator<HashMap<Ordinate, BigInteger>> it = spacesA.keySet().iterator();
		while( it.hasNext() )
		{
			HashMap<Ordinate, BigInteger> spaceAe = it.next();
			CoeffNode coeff = spacesA.get( spaceAe );
			final CNelem an0 = 
					new CNelem( fac.getFac() , spaceAe , index );
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
public void writeString( PrintStream ps ) {
	throw( new RuntimeException( "NotSupported" ) );
}



protected void applyDerivativeAction( HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesIn , 
		Ordinate node , final int numDerivatives , DoubleElem hh ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut )
{
	if( numDerivatives > 3 )
	{
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesMid = new HashMap<HashMap<Ordinate, BigInteger>,CoeffNode>();
		applyDerivativeAction(implicitSpacesIn, node, 3, hh, implicitSpacesMid);
		applyDerivativeAction(implicitSpacesMid, node, numDerivatives-3, hh, implicitSpacesOut);
	}
	
	Iterator<HashMap<Ordinate, BigInteger>> it = implicitSpacesIn.keySet().iterator();
	while( it.hasNext() )
	{
		final HashMap<Ordinate, BigInteger> implicitSpace = it.next();
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
				final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
				final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
				
				Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
				while( itA.hasNext() )
				{
					Ordinate ae = itA.next();
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
						coeffNodeIn.getDenom().mult( hh ).mult( genFromConstDbl( 2.0 ) ) );
				final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer() , 
						coeffNodeIn.getDenom().mult( hh ).mult( genFromConstDbl( 2.0 ) ) );
				
				applyAdd( implicitSpaceOutM1 , coeffNodeOutM1 , implicitSpacesOut );
				applyAdd( implicitSpaceOutP1 , coeffNodeOutP1 , implicitSpacesOut );
			}
			break;
			
		case 2:
			{
				final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
				final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
				
				Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
				while( itA.hasNext() )
				{
					Ordinate ae = itA.next();
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
				final CoeffNode coeffNodeOut = new CoeffNode(  coeffNodeIn.getNumer().negate().mult( genFromConstDbl( 2.0 ) ) , 
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
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP1 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutM2 = new HashMap<Ordinate, BigInteger>();
			final HashMap<Ordinate, BigInteger> implicitSpaceOutP2 = new HashMap<Ordinate, BigInteger>();
			
			Iterator<Ordinate> itA = implicitSpace.keySet().iterator();
			while( itA.hasNext() )
			{
				Ordinate ae = itA.next();
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
			
			final CoeffNode coeffNodeOutM1 = new CoeffNode(  coeffNodeIn.getNumer().mult( genFromConstDbl( 2.0 ) ) , 
					coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( genFromConstDbl( 2.0 ) ) );
			final CoeffNode coeffNodeOutP1 = new CoeffNode( coeffNodeIn.getNumer().negate().mult( genFromConstDbl( 2.0 ) ) , 
					coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( genFromConstDbl( 2.0 ) ) );
			final CoeffNode coeffNodeOutM2 = new CoeffNode(  coeffNodeIn.getNumer().negate() , 
					coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( genFromConstDbl( 2.0 ) ) );
			final CoeffNode coeffNodeOutP2 = new CoeffNode( coeffNodeIn.getNumer() , 
					coeffNodeIn.getDenom().mult( hh ).mult( hh ).mult( hh ).mult( genFromConstDbl( 2.0 ) ) );
			
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
		HashMap<Ordinate, BigInteger> implicitSpace , CoeffNode node ,
		HashMap<HashMap<Ordinate, BigInteger>,CoeffNode> implicitSpacesOut )
{
	CoeffNode prev = implicitSpacesOut.get( implicitSpace );
	
	if( prev == null )
	{
		implicitSpacesOut.put( implicitSpace , node );
		return;
	}
	
	if( ( prev.getDenom().getVal() == node.getDenom().getVal() ) &&
		( prev.getDenom().getVal() == node.getDenom().getVal() ) ) 
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
	
	
	
	/**
	 * Newton-Raphson evaluator for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	protected class StelemNewton extends NewtonRaphsonMultiElemRemap<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
	{

		
		
		/**
		 * Constructs the evaluator.
		 * 
		 * @param _functions Input multivector of functions.
		 * @param _withRespectTosI Set of variables to take derivatives with respect to.
		 * @param implicitSpaceFirstLevel Implicit space for the initial eval.
		 * @param _sfac Factory for enclosed type.
		 * @param _dim The number of dimensions in the multivector.
		 * @param _ord The input Ord.
		 * @throws NotInvertibleException
		 * @throws MultiplicativeDistributionRequiredException
		 */
		public StelemNewton(
				GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> _functions,
				HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>> _withRespectTosI,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel,
				SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> _sfac,
				TestDimensionFour _dim,
				SpacetimeAlgebraOrd<TestDimensionFour> _ord)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			super(_functions, _withRespectTosI, implicitSpaceFirstLevel, _sfac, _dim, _ord);
			
			// System.out.println( "**" );
			// System.out.println( this.partialEval.writeString() );
		}

		/**
		 * The iteration count for Newton-Raphson iterations.
		 */
		protected int intCnt = 0;

		@Override
		protected boolean iterationsDone() {
			intCnt++;
			return( intCnt > 20 );
		}
		
		@Override
		public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpaceInitialGuess ) throws NotInvertibleException, MultiplicativeDistributionRequiredException
		{
			intCnt = 0;
			
			// System.out.println( this.partialEval.writeString() ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			return( super.eval(implicitSpaceInitialGuess) );
		}
		
		@Override
		protected void performIterationUpdate( GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> iterationOffset )
		{
			TestDiracA.performIterationUpdate( iterationOffset );
		}
	
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		@Override
//		protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> 
//			evalPartialDerivative() throws NotInvertibleException, MultiplicativeDistributionRequiredException
//		{
//			GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>
//				ev = super.evalPartialDerivative();
//			for( int acnt = 0 ; acnt < 16 ; acnt++ )
//			{
//				final DoubleElem dd = new DoubleElem( 1E-6 * ( 2.0 * rand.nextDouble() ) );
//				final HashSet<BigInteger> hs = new HashSet<BigInteger>();
//				if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
//				if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
//				if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
//				if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
//				DoubleElem dda = ev.getVal( hs );
//				ev.setVal( hs , dd.add( dda ) );
//			}
//			return( ev );
//		}
//		
		
		/**
		 * The internal multivariate Newton-Raphson.
		 * 
		 * @author thorngreen
		 *
		 */
		protected class StelemNewtonEnt extends NewtonRaphsonMultiElem<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim, DoubleElem, DoubleElemFactory>
		{

			/**
			 * Constructs the evaluator.
			 * 
			 * @param _functions The functions over which to evaluate Netwon-Raphson.
			 * @param _withRespectTos The set of variables over which to take derivatives.
			 * @param implicitSpaceFirstLevel The initial implicit space over which to take the functions and their derivatives.
			 * @param _sfac The factory for the enclosed type.
			 * @param _dim The number of dimensions over which to evaluate Newton-Raphson.
			 * @throws NotInvertibleException
			 * @throws MultiplicativeDistributionRequiredException
			 */
			public StelemNewtonEnt(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> _functions,
					ArrayList<ArrayList<? extends Elem<?, ?>>> _withRespectTos,
					HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel,
					SymbolicElemFactory<DoubleElem, DoubleElemFactory> _sfac,
					simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim _dim)
					throws NotInvertibleException,
					MultiplicativeDistributionRequiredException {
				super(_functions, _withRespectTos, implicitSpaceFirstLevel, _sfac, _dim);
			}

			@Override
			protected void performIterationUpdate(
					GeometricAlgebraMultivectorElem<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim>, DoubleElem, DoubleElemFactory> iterationOffset) {
				StelemNewton.this.performIterationUpdateInternal( iterationOffset );
			}

			@Override
			protected boolean iterationsDone() {
				return( StelemNewton.this.iterationsDone() );
			}
			
		};
		

		@Override
		protected NewtonRaphsonMultiElem<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim, DoubleElem, DoubleElemFactory> genNewton(
				GeometricAlgebraMultivectorElem<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim, GeometricAlgebraOrd<simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> _functions,
				ArrayList<ArrayList<? extends Elem<?, ?>>> _withRespectTos,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpaceFirstLevel,
				SymbolicElemFactory<DoubleElem, DoubleElemFactory> _sfac,
				simplealgebra.algo.NewtonRaphsonMultiElemRemap.Adim _dim ) throws NotInvertibleException, MultiplicativeDistributionRequiredException {
			
			return( new StelemNewtonEnt( _functions, _withRespectTos, implicitSpaceFirstLevel , _sfac , _dim ) );
		}
		
	}
	
	
	
	
	
protected class VEvalElem extends SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>	
{
	
	protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>
		dval = null;
	

	public VEvalElem(
			GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> _fac,
			GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _dval ) {
		super(_fac);
		dval = _dval;
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> ret = 
				new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			ret.setVal( key , dval.get( key ).eval( implicitSpace ) );
		}
		
		return( ret );
	}

	@Override
	public GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> ret = 
				new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>( new DoubleElemFactory() , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			ret.setVal( key , dval.get( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
		}
		
		return( ret );
	}

	@Override
	public void writeString( PrintStream ps ) {
		Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
		while( it.hasNext() )
		{
			final HashSet<BigInteger> key = it.next();
			ps.print( "\n" + "** " );
			( dval.get( key ) ).writeString( ps );
		}
	}
	
}





protected class VEvalElem2 extends SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>	
{

protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>  
	dval = null;

protected SymbolicElemFactory<DoubleElem,DoubleElemFactory> sefac;

protected GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> vefac;




public VEvalElem2(
		SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>> _fac,
		SymbolicElemFactory<DoubleElem,DoubleElemFactory> _sefac ,
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> _vefac ,
		GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> _dval ) {
	super(_fac);
	sefac = _sefac;
	vefac = _vefac;
	dval = _dval;
}

@Override
public SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> key = it.next();
		retA.setVal( key , dval.get( key ).eval( implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> evalPartialDerivative(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );
	
	Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
	while( it.hasNext() )
	{
		final HashSet<BigInteger> key = it.next();
		retA.setVal( key , dval.get( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
	}
	
	return( new VEvalElem( vefac , retA ) );
}

@Override
public void writeString( PrintStream ps ) {
	throw( new RuntimeException( "Not Supported" ) );
}

}





protected class VEvalElem3 extends SymbolicElem<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>>	
{

protected GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>   
dval = null;

protected SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> sefac;

protected GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> vefac;




public VEvalElem3(
		SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
		GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> _fac,
		SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> _sefac ,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory> _vefac ,
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>> _dval ) {
super(_fac);
sefac = _sefac;
vefac = _vefac;
dval = _dval;
}

@Override
public SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> eval(
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
		new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );

Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
while( it.hasNext() )
{
	final HashSet<BigInteger> key = it.next();
	retA.setVal( key , dval.get( key ).eval( implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,
	GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> evalPartialDerivative(
	ArrayList<? extends Elem<?, ?>> withRespectTo,
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
	GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> retA = 
			new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( sefac , new TestDimensionFour() , new SpacetimeAlgebraOrd<TestDimensionFour>() );

Iterator<HashSet<BigInteger>> it = dval.getKeyIterator();
while( it.hasNext() )
{
	final HashSet<BigInteger> key = it.next();
	retA.setVal( key , dval.get( key ).evalPartialDerivative( withRespectTo , implicitSpace ) );
}

return( new VEvalElem2( fac.getFac() , sefac.getFac() , vefac , retA ) );
}

@Override
public void writeString( PrintStream ps ) {
throw( new RuntimeException( "Not Supported" ) );
}

}
	


	
	
	
public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException
	{
		final Random rand = new Random( 3344 );
		
		final double d1 = Math.sqrt( calcMagnitudeSq( X_HH ) + calcMagnitudeSq( Y_HH ) + calcMagnitudeSq( Z_HH ) );
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		
		{
			for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
			{
				for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
				{
					for( int ycnt = 0 ; ycnt < NUM_Y_ITER ; ycnt++ )
					{
						for( int zcnt = 0 ; zcnt < NUM_Z_ITER ; zcnt++ )
						{
							iterArray[ tcnt ][ xcnt ][ ycnt ][ zcnt ] = genDiffAll( 1E-8 );
						}
					}
				}
			}
		}
		
		
		{
			for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
			{
				for( int xcnt = 0 ; xcnt < 2 ; xcnt++ )
				{
					for( int ycnt = 0 ; ycnt < 2 ; ycnt++ )
					{
						for( int zcnt = 0 ; zcnt < 2 ; zcnt++ )
						{
							tempArray[ tcnt ][ xcnt ][ ycnt ][ zcnt ] = genDiffAll( 1E-8 );
						}
					}
				}
			}
		}
		
		
		for( int tcnt = 0 ; tcnt < 2 ; tcnt++ )
		{
			// for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			// {
			//	iterArray[ tcnt ][ xcnt ] = rand.nextDouble();
			// }
			iterArray[ tcnt ][ 5 ][ 5 ][ 5 ] = genDiffEnt( 10000.0 * ( d1 * d1 ) );
		}
		
		
		final DoubleElemFactory de = new DoubleElemFactory();
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory> de2 = new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>( de , tdim , ord );
		
		final SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>> se = new SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>( de2 );
		
		final SymbolicElemFactory<DoubleElem,DoubleElemFactory> seA = new SymbolicElemFactory<DoubleElem,DoubleElemFactory>( de );
		
		final SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>> se2 =
				new SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>> se2A = new SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>( seA );
		
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>> se3 =
				new SymbolicElemFactory<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>( se2A );
		
		
		
		
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			ge =
			new GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A , tdim , ord );
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				g0 = new GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( se3A , tdim , ord );
		
		for( int acnt = 0 ; acnt < 16 ; acnt++ )
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			if( ( acnt & 1 ) != 0 ) hs.add( BigInteger.ZERO );
			if( ( acnt & 2 ) != 0 ) hs.add( BigInteger.ONE );
			if( ( acnt & 4 ) != 0 ) hs.add( BigInteger.valueOf( 2 ) );
			if( ( acnt & 8 ) != 0 ) hs.add( BigInteger.valueOf( 3 ) );
			if( /* ( ( hs.size() ) % 2 ) == 0 */ true )
			{
				final AStelem as = new AStelem( se2A  , hs );
				g0.setVal( hs , as );
			}
		}
		
		
		
		final DDirec ddirec = new DDirec(de2, se2A);
		
		final DirectionalDerivative<
			TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			Ordinate>
			del =
			new DirectionalDerivative<
			TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
			SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>, 
			Ordinate>( 
					ge , 
					tdim , ord ,
					ddirec );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				del0 = del.eval( null );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				gxx0 = del0.mult( g0 );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> energ0 =
					( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( MM.mult( C ).mult( HBAR.invertLeft() ) , de ) , seA ) , se2A )
							).negate();
		
		
		// SymbolicElem<SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>,
		//	SymbolicElemFactory<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>,GeometricAlgebraMultivectorElemFactory<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem,DoubleElemFactory>>>> m1
		//	= m1X.add( gtt0 );
		
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
		SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				generg1 = new GeometricAlgebraMultivectorElem<
						TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
						SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
						SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
					( se3A , tdim , ord );
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ZERO );
			generg1.setVal( hs , energ0 );
			
		}
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			generg = g0.mult( generg1 );
		
		
		final SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>> gxxMult
			=  
				( new StelemReduction3L( new StelemReduction2L( new SymbolicConst( 
						genFromConstDbl( 1.0 ) , de ) , seA ) , se2A )
						);
		
		
		final GeometricAlgebraMultivectorElem<
		TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
		SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
			gxxMultV = new GeometricAlgebraMultivectorElem<
					TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>,
					SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>, 
					SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>( se3A , tdim , ord );
		
		
		{
			final HashSet<BigInteger> hs = new HashSet<BigInteger>();
			hs.add( BigInteger.ONE );
			hs.add( BigInteger.valueOf( 2 ) );
			gxxMultV.setVal( hs , gxxMult );
		}
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,SymbolicElem<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<SymbolicElem<DoubleElem,DoubleElemFactory>,SymbolicElemFactory<DoubleElem,DoubleElemFactory>>>>
				mg1 = ( gxx0.mult( gxxMultV ) ).add( generg );
		
		
		VEvalElem3 m1 = new VEvalElem3( se2 , se2A , de2 , mg1 );
		
		
		
		
		
		final HashMap<Ordinate,Ordinate> implicitSpace0 = new HashMap<Ordinate,Ordinate>();
		
		final HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace2 = implicitSpace0;
		
		implicitSpace0.put( new Ordinate( de2 , TV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , XV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , YV ) , new Ordinate( de2 , 0 ) );
		implicitSpace0.put( new Ordinate( de2 , ZV ) , new Ordinate( de2 , 0 ) );
		
		final SymbolicElem<SymbolicElem<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>, GeometricAlgebraMultivectorElemFactory<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, DoubleElem, DoubleElemFactory>>> 
			s0 = m1.eval( implicitSpace2 );
		
		final GeometricAlgebraMultivectorElem<TestDimensionFour, SpacetimeAlgebraOrd<TestDimensionFour>, SymbolicElem<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>>> 
			s00 = ( (VEvalElem2)( s0 ) ).dval;
		
		// String s = s0.writeString();
		
		// System.out.println( s );
		
		
		final HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>> wrt3 = new HashMap<HashSet<BigInteger>, ArrayList<? extends Elem<?, ?>>>();
		{
			for( int acnt = 0 ; acnt < 16 ; acnt++ )
			{
				final HashMap<Ordinate, BigInteger> coord = new HashMap<Ordinate, BigInteger>();
				coord.put( new Ordinate( de2 , TV ) , BigInteger.valueOf( 1 ) );
				coord.put( new Ordinate( de2 , XV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , YV ) , BigInteger.valueOf( 0 ) );
				coord.put( new Ordinate( de2 , ZV ) , BigInteger.valueOf( 0 ) );
				
				final HashSet<BigInteger> index = new HashSet<BigInteger>();
				if( ( acnt & 1 ) != 0 ) index.add( BigInteger.ZERO );
				if( ( acnt & 2 ) != 0 ) index.add( BigInteger.ONE );
				if( ( acnt & 4 ) != 0 ) index.add( BigInteger.valueOf( 2 ) );
				if( ( acnt & 8 ) != 0 ) index.add( BigInteger.valueOf( 3 ) );
				
				ArrayList<CNelem> ce = new ArrayList<CNelem>();
				ce.add( new CNelem( seA , coord , index ) );
				
				wrt3.put( index , ce );
			}
		}
		
		
		StelemNewton newton = new StelemNewton( s00 ,
				wrt3 , implicitSpace2 , se2A ,
				tdim , new SpacetimeAlgebraOrd<TestDimensionFour>() );
		
		
		for( int tval = 1 ; tval < ( NUM_T_ITER - 1 ) ; tval++ )
		{
			for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			{
				for( int ycnt = 0 ; ycnt < NUM_Y_ITER ; ycnt++ )
				{
					for( int zcnt = 0 ; zcnt < NUM_Z_ITER ; zcnt++ )
					{
						iterArray[ tval + 1 ][ xcnt ][ ycnt ][ zcnt ] = iterArray[ tval ][ xcnt ][ ycnt ][ zcnt ];
					}
				}
			}
			
			for( int xcnt = 0 ; xcnt < NUM_X_ITER ; xcnt++ )
			{
				for( int ycnt = 0 ; ycnt < NUM_Y_ITER ; ycnt++ )
				{
					for( int zcnt = 0 ; zcnt < NUM_Z_ITER ; zcnt++ )
					{
						// System.out.println( "iter... " + xcnt + " " + ycnt + " " + zcnt );
						
						fillTempArray( tval , xcnt , ycnt , zcnt );
						clearSpatialAssertArray();
		
				
						final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> 
							ival = TestDiracA.getUpdateValue();
				
				
			
				
						GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> err = newton.eval( implicitSpace2 );
		
		
						final GeometricAlgebraMultivectorElem<TestDimensionFour,SpacetimeAlgebraOrd<TestDimensionFour>,DoubleElem, DoubleElemFactory> 
							val = TestDiracA.getUpdateValue();
						
						
				
						if( ( xcnt == 5 ) && ( ycnt == 5 ) && ( zcnt == 5 ) )
						{
							System.out.println( "******************" );
							System.out.println( " ( " + xcnt + " , " + ycnt + " , " + zcnt + " ) " );
							System.out.println( Math.sqrt( calcMagnitudeSq( ival ) ) );
							System.out.println( Math.sqrt( calcMagnitudeSq( val ) ) );
							System.out.println( "## " + ( Math.sqrt( calcMagnitudeSq( err ) ) ) );
						}
						
						
						Assert.assertTrue( spatialAssertArray[ 0 ][ 0 ][ 0 ][ 0 ] == 0 );
						
						Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 1 ] > 0 );
						
						Assert.assertTrue( spatialAssertArray[ 2 ][ 1 ][ 1 ][ 1 ] > 0 );
						Assert.assertTrue( spatialAssertArray[ 1 ][ 2 ][ 1 ][ 1 ] > 0 );
						Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 2 ][ 1 ] > 0 );
						Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 2 ] > 0 );
						
						Assert.assertTrue( spatialAssertArray[ 0 ][ 1 ][ 1 ][ 1 ] > 0 );
						Assert.assertTrue( spatialAssertArray[ 1 ][ 0 ][ 1 ][ 1 ] > 0 );
						Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 0 ][ 1 ] > 0 );
						Assert.assertTrue( spatialAssertArray[ 1 ][ 1 ][ 1 ][ 0 ] > 0 );
				
				
						// System.out.println( calcMagnitudeSq( val ) );
						// System.out.println( calcMagnitudeSq( err ) );
						Assert.assertTrue( Math.abs( Math.sqrt( calcMagnitudeSq( err ) ) ) < ( 0.01 * Math.abs( Math.sqrt( calcMagnitudeSq( val ) ) ) + 0.01 ) );
				
			
						iterArray[ tval + 1 ][ xcnt ][ ycnt ][ zcnt ] = val;
					}
					
				}
						
			}
			
		}
		
		// System.out.println( "==============================" ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// System.out.println( iterArray[ NUM_T_ITER - 1 ][ 10 ][ 5 ][ 5 ] ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Assert.assertTrue( Math.abs( val - ( -1.450868 ) ) < 0.01 ); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
	}
	
	

	
}


