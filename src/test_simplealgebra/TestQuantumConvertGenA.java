




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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.AbstractCache;
import simplealgebra.CloneThreadCache;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.SquareMatrixElem;
import simplealgebra.WriteBigIntegerCache;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteElemCache.IntVal;
import simplealgebra.algo.*;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.stelem.Nelem;
import simplealgebra.stelem.Stelem;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicDivideBy;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;
import simplealgebra.symbolic.SymbolicInvertLeft;
import simplealgebra.symbolic.SymbolicInvertRight;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.ddx.*;
import simplealgebra.ga.*;
import simplealgebra.et.*;









/**
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 *  <mo>=</mo>
 *  <mn>0</mn>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 * </mrow>
 * </math> is the Einstein tensor in 4-D.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * 
 * 
 * See http://en.wikipedia.org/wiki/Einstein_tensor
 *
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestQuantumConvertGenA extends TestCase {
	
	
	/**
	 * ComplexElem<DoubleElem,DoubleElemFactory> representing the constant zero.
	 */
	private static final ComplexElem<DoubleElem,DoubleElemFactory> DOUBLE_ZERO = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( 0.0 ) , new DoubleElem( 0.0 ) );
	
	/**
	 * ComplexElem<DoubleElem,DoubleElemFactory> representing the constant 2.
	 */
	private static final ComplexElem<DoubleElem,DoubleElemFactory> MM = genFromConstDbl( 2.0 );
	
	/**
	 * The number of elements in the rank two tensor.
	 */
	private static final int SQ_TENSOR_SZ = ( TestDimensionFour.FOUR ) * ( TestDimensionFour.FOUR );
	
	/**
	 * Random number multiplier size.
	 */
	private static final double RAND_SIZE = 1E-19;
	
	
	/**
	 * Returns a tensor scalar that is equal to the parameter value.
	 * 
	 * @param in The value to be assigned to the scalar.
	 * @return The constructed tensor.
	 */
	private static EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> genFromConst( double in )  // Rank Zero.
	{
		DoubleElemFactory de2a = new DoubleElemFactory();
		final ComplexElem<DoubleElem,DoubleElemFactory> dd = new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( in ) , de2a.zero() );
		ComplexElemFactory<DoubleElem,DoubleElemFactory> da = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de2a );
		EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			ret = new EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( da , new ArrayList() , new ArrayList() );
		ret.setVal( new ArrayList<BigInteger>() , dd );
		return( ret );
	}
	
	
	/**
	 * Random number generator for producing initial conditions.
	 */
	private static final Random rand = new Random( 4455 );
	
	
	
	
	
	
	/**
	 * Returns a tensor equal to the input value.
	 * @param in The input value.
	 * @return The generated tensor.
	 */
	private static ComplexElem<DoubleElem,DoubleElemFactory> genFromConstDbl( double in )
	{
		return( new ComplexElem<DoubleElem,DoubleElemFactory>( new DoubleElem( in ) , new DoubleElem( 0.0 ) ) );
	}
	
	
	
	
	
	/**
	 * Discretized index for the T-Axis.
	 */
	protected static final int TV = 0;
	
	/**
	 * Discretized index for the X-Axis.
	 */
	protected static final int XV = 1;
	
	/**
	 * Discretized index for the Y-Axis.
	 */
	protected static final int YV = 2;
	
	/**
	 * Discretized index for the Z-Axis.
	 */
	protected static final int ZV = 3;
	
	









/**
 * Cache for symbolic constants.
 * 
 * @author tgreen
 *
 */
private static class SymbolicConstCache
{
	
	/**
	 * Map representing the cache.
	 */
	protected static HashMap<ArrayList<Double>,SymbolicConst> map = new HashMap<ArrayList<Double>,SymbolicConst>();
	
	/**
	 * Returns a cached SymbolicConst representing a ComplexElem<DoubleElem,DoubleElemFactory>.
	 * 
	 * @param in The ComplexElem<DoubleElem,DoubleElemFactory> to be represented.
	 * @param _fac The factory for ComplexElem<DoubleElem,DoubleElemFactory> instances.
	 * @return The cached SymbolicConst.
	 */
	public static SymbolicConst get(  final ComplexElem<DoubleElem,DoubleElemFactory> in , ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac )
	{
		final ArrayList<Double> inv = new ArrayList<Double>();
		inv.add( in.getRe().getVal() );
		inv.add( in.getIm().getVal() );
		
		SymbolicConst cnst = map.get( inv );
		if( cnst == null )
		{
			cnst = new SymbolicConst( in , _fac );
			map.put( inv , cnst );
		}
		return( cnst );
	}
	
	/**
	 * Clears the cache.
	 */
	public static void clearCache()
	{
		map.clear();
	}
	
}
	


/**
 * Defines a directional derivative for the test.
 * 
 * @author thorngreen
 *
 */
private static class DDirec extends DirectionalDerivativePartialFactory<
	SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
	Ordinate>
{
	/**
	 * Factory for building the value of the derivative of an ordinate.
	 */
	EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>> de;
	
	/**
	 * Factory for the enclosed type.
	 */
	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2;
	
	/**
	 * Constructs the directional derivative factory.
	 * 
	 * @param _de Factory for building the value of the derivative of an ordinate.
	 * @param _se2 Factory for the enclosed type.
	 */
	public DDirec( 
			final EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>> _de ,
			final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _se2 )
	{
		de = _de;
		se2 = _se2;
	}

	@Override
	public SymbolicElem<
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
			getPartial( BigInteger basisIndex )
	{
		final ArrayList<Ordinate> wrtX = new ArrayList<Ordinate>();
		
		wrtX.add( new Ordinate( de , basisIndex.intValue() ) );
		
		SymbolicElem<
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
			ret =
					new PartialDerivativeOp<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,Ordinate>( se2 , wrtX );
		
		if( basisIndex.equals( BigInteger.ZERO ) )
		{
//			try
			{
				final DoubleElemFactory de2a = new DoubleElemFactory();
				final ComplexElemFactory<DoubleElem,DoubleElemFactory> de2 = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de2a );
				final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de2 );
				final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2A = new SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( seA );
				// final ComplexElem<DoubleElem,DoubleElemFactory> cinv = C.invertLeft();
				final SymbolicElem<
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
					// cmul = ( new StelemReduction3L( new StelemReduction2L( SymbolicConstCache.get( 
					//		cinv , de2 ) , seA ) , se2A )
					//		);
					cmul = ( new CsStelem( se2A , true ) ).invertLeft();
				ret = cmul.mult( ret );
			}
//			catch( NotInvertibleException ex )
//			{
//				Assert.assertNull( ex );
//			}
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
private static class Ordinate extends SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
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
	public Ordinate(EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _fac, int _col) {
		super(_fac);
		col = _col;
	}

	@Override
	public EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, EinsteinTensorElemFactory<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}
	
	@Override
	public EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, EinsteinTensorElemFactory<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		throw( new RuntimeException( "NotSupported" ) );
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, EinsteinTensorElemFactory<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, EinsteinTensorElemFactory<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> cache,
			PrintStream ps) {
		String st = cache.get( this );
		if( st == null )
		{
			final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( Ordinate.class.getSimpleName() );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( Ordinate.class.getSimpleName() );
			ps.print( "( " );
			ps.print( sta );
			ps.print( " , " );
			ps.print( col );
			ps.println( " );" );
		}
		return( st );
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> b )
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
 * A symbolic elem representing a constant value
 * at the base descent algorithm evaluation level.
 * 
 * @author thorngreen
 *
 */
private static class SymbolicConst extends SymbolicReduction<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public SymbolicConst(ComplexElem<DoubleElem,DoubleElemFactory> _elem, ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
	{
		if( b instanceof SymbolicConst )
		{
			final boolean eqA = ( getElem().getRe().getVal() ) == ( ( (SymbolicConst) b ).getElem().getRe().getVal() );
			final boolean eqB = ( getElem().getIm().getVal() ) == ( ( (SymbolicConst) b ).getElem().getIm().getVal() );
			return( eqA && eqB );
		}
		return( false );
	}
	
}
	
	

/**
 * An elem representing a symbolic constant at the level of mapping 
 * discretized approximations of the underlying differential
 * equation into expressions (e.g. Jacobian slopes)
 * for descent algorithm evaluations.
 * 
 * @author thorngreen
 *
 */
private static class StelemReduction2L extends SymbolicReduction<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public StelemReduction2L( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _elem, 
			SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> b )
	{
		if( b instanceof StelemReduction2L )
		{
			return( getElem().symbolicEquals( ( (StelemReduction2L) b ).getElem() ) );
		}
		return( false );
	}
	
}



/**
 * An elem representing a symbolic constant at the level
 * of mapping the underlying differential equation into
 * its discretized approximation.
 * 
 * @author thorngreen
 *
 */
private static class StelemReduction3L extends SymbolicReduction<
	SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The constant to be represented.
	 * @param _fac The factory for the constant.
	 */
	public StelemReduction3L(
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _elem, 
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _fac) {
		super(_elem, _fac);
	}
	
	@Override
	public boolean symbolicEquals( SymbolicElem<
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> b )
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
private static class CoeffNode
{
	/**
	 * The numerator.
	 */
	private SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> numer;
	
	/**
	 * The denominator.
	 */
	private SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> denom;
	
	/**
	 * Constructs the coefficient.
	 * 
	 * @param _numer The numerator.
	 * @param _denom The denominator.
	 */
	public CoeffNode( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _numer , SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> _denom )
	{
		numer = _numer;
		denom = _denom;
	}
	
	/**
	 * Gets the numerator.
	 * 
	 * @return The numerator.
	 */
	public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> getNumer() {
		return numer;
	}
	
	/**
	 * Gets the denominator.
	 * 
	 * @return The denominator.
	 */
	public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> getDenom() {
		return denom;
	}
	
}







private static class CsNelem extends SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
{

	
	protected int[] densValues;
	
	
	protected BigInteger numer = BigInteger.ONE;
	
	
	protected BigInteger denom = BigInteger.ONE;



	public void advanceDens( int dens_index , int dens_value )
	{
		densValues[ dens_index ] += dens_value;
	}
	
	
	public int getDen( int dens_index )
	{
		return( densValues[ dens_index ] );
	}
	
	
	public CsNelem(
			int[] dens,
			SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _fac ) {
		super(_fac);
		
		densValues = new int[ CsStelem.NUM_DENS ];
		
		for( int cnt = 0 ; cnt < CsStelem.NUM_DENS ; cnt++ )
		{
			densValues[ cnt ] = dens[ cnt ];
		}
	}
	
	
	public CsNelem(
			SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _fac ) {
		super(_fac);
		
		densValues = new int[ CsStelem.NUM_DENS ];
		
	}
	
	
	@Override
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
	mult( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in )
	{
		
		if( in instanceof CNelem )
		{
			CNelem ret = new CNelem( this.fac );
			for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
			{
				ret.densValues[ cnt ] = ( (CNelem) in ).densValues[ cnt ];
			}
			ret.advanceDens( AStelem.C_DENS ,  this.densValues[ CsStelem.CS_DENS ] );
			ret.advanceDens( AStelem.NEGATION_DENS ,  this.densValues[ CsStelem.CS_NEGATION_DENS ] );
			ret.numer = this.numer.multiply( ( (CNelem) in ).numer );
			ret.denom = this.denom.multiply( ( (CNelem) in ).denom );
			return( ret );
		}
		if( in instanceof CsNelem )
		{
			CsNelem ret = new CsNelem( this.fac );
			for( int cnt = 0 ; cnt < CsStelem.NUM_DENS ; cnt++ )
			{
				ret.densValues[ cnt ] = this.densValues[ cnt ] + ( (CsNelem) in ).densValues[ cnt ];
			}
			ret.numer = this.numer.multiply( ( (CsNelem) in ).numer );
			ret.denom = this.denom.multiply( ( (CsNelem) in ).denom );
			return( ret );
		}
		else
		{
			return( super.mult( in ) );
		}
		
	}
	
	
	
	
	@Override 
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	divideBy( BigInteger in )
	{
		CsNelem ret = new CsNelem( this.fac );
		for( int cnt = 0 ; cnt < CsStelem.NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.numer = this.numer;
		ret.denom = this.denom.multiply( in );
		return( ret );
	}
			
	
	
	
	@Override 
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	negate()
	{
		CsNelem ret = new CsNelem( this.fac );
		for( int cnt = 0 ; cnt < CsStelem.NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.advanceDens( CsStelem.CS_NEGATION_DENS , 1 );
		ret.numer = this.numer;
		ret.denom = this.denom;
		return( ret );
	}
	
	
	


	@Override 
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	invertLeft()
	{
		
		CsNelem ret = new CsNelem( this.fac );
		for( int cnt = 0 ; cnt < CsStelem.NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.densValues[ CsStelem.CS_DENS ] = - this.densValues[ CsStelem.CS_DENS ];
		ret.numer = this.denom;
		ret.denom = this.numer;
		System.out.println( "###### " + (  ret.densValues[ CsStelem.CS_DENS ] ) );
		System.out.println( this.densValues[ CsStelem.CS_DENS ] );
		return( ret );
		
	}
	
	
	


	@Override 
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	invertRight()
	{
		
		CsNelem ret = new CsNelem( this.fac );
		for( int cnt = 0 ; cnt < CsStelem.NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.densValues[ CsStelem.CS_DENS ] = - this.densValues[ CsStelem.CS_DENS ];
		ret.numer = this.denom;
		ret.denom = this.numer;
		System.out.println( "###### " + (  ret.densValues[ CsStelem.CS_DENS ] ) );
		System.out.println( this.densValues[ CsStelem.CS_DENS ] );
		return( ret );
		
	}		

	
	

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
			PrintStream ps) {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}
	
	
	
}







private static class CNelem extends SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
{

	
	protected int[] densValues;
	

	
	
	protected BigInteger numer = BigInteger.ONE;
	
	
	protected BigInteger denom = BigInteger.ONE;
	



	public void advanceDens( int dens_index , int dens_value )
	{
		densValues[ dens_index ] += dens_value;
	}
	
	
	public int getDen( int dens_index )
	{
		return( densValues[ dens_index ] );
	}
	
	
	public CNelem(
			int[] dens,
			SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _fac ) {
		super(_fac);
		
		densValues = new int[ AStelem.NUM_DENS ];
		
		for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
		{
			densValues[ cnt ] = dens[ cnt ];
		}
	}
	
	
	public CNelem(
			SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> _fac ) {
		super(_fac);
		
		densValues = new int[ AStelem.NUM_DENS ];
		
	}
	
	
	
	
	
	@Override
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	divideBy( BigInteger in )
	{
		CNelem ret = new CNelem( this.fac );
		for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.numer = this.numer;
		ret.denom = this.denom.multiply( in );
		return( ret );
	}
	
	
	
	
	@Override
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	mult( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in )
	{
		
		if( in instanceof CNelem )
		{
			CNelem ret = new CNelem( this.fac );
			for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
			{
				ret.densValues[ cnt ] = this.densValues[ cnt ] + ( (CNelem) in ).densValues[ cnt ];
			}
			ret.numer = this.numer.multiply( ( (CNelem) in ).numer );
			ret.denom = this.denom.multiply( ( (CNelem) in ).denom );
			return( ret );
		}
		else
		if( in instanceof CsNelem )
		{
			CNelem ret = new CNelem( this.fac );
			for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
			{
				ret.densValues[ cnt ] = this.densValues[ cnt ];
			}
			ret.advanceDens( AStelem.C_DENS , ( (CsNelem) in ).densValues[ CsStelem.CS_DENS ]  );
			ret.advanceDens( AStelem.NEGATION_DENS , ( (CsNelem) in ).densValues[ CsStelem.CS_NEGATION_DENS ]  );
			ret.numer = this.numer.multiply( ( (CsNelem) in ).numer );
			ret.denom = this.denom.multiply( ( (CsNelem) in ).denom );
			return( ret );
		}
		else
		{
			return( super.mult( in ) );
		}
		
	}
	
	
	
	
	
	@Override
	public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	negate()
	{
		CNelem ret = new CNelem( this.fac );
		for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.advanceDens( AStelem.NEGATION_DENS , 1 );
		ret.numer = this.numer;
		ret.denom = this.denom;
		return( ret );
	}
	

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> eval(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache)
			throws NotInvertibleException, MultiplicativeDistributionRequiredException {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}

	@Override
	public String writeDesc(
			WriteElemCache<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> cache,
			PrintStream ps) {
		
		throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
		
	}
	
	
	
}








/**
* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
* 
* @author thorngreen
*
*/	
private static class CsStelem extends SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
{	




public static final int CS_DENS = 0;

public static final int CS_NEGATION_DENS = 1;

public static final int NUM_DENS = 2;



protected int[] densValues = { 0 , 0 };





protected BigInteger numer = BigInteger.ONE;


protected BigInteger denom = BigInteger.ONE;





public void advanceDens( int dens_index , int dens_value )
{
densValues[ dens_index ] += dens_value;
}





/**
* Constructs the elem.
* 
* @param _fac The factory for the enclosed type.
* @param _index The index of the tensor component represented by the elem.
*/
public CsStelem( SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _fac ) {
super(_fac);
}



public CsStelem( SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _fac , boolean bool ) {
super(_fac);
this.advanceDens( CS_DENS , 1 );
}


@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> eval(
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {


CsNelem ret = new CsNelem( densValues , fac.getFac() );
ret.numer = this.numer;
ret.denom = this.denom;


 return( ret );

}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
divideBy( BigInteger in )
{
	CsStelem ret = new CsStelem( this.fac );
	for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
	{
		ret.densValues[ cnt ] = this.densValues[ cnt ];
	}
	ret.numer = this.numer;
	ret.denom = this.denom.multiply( in );
	return( ret );
	
}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
mult( SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> in )
{
	
if( in instanceof AStelem )
{
	AStelem ret = new AStelem( this.fac ,  ( (AStelem) in ).getIndex() );
	for( int cnt = 0 ; cnt < AStelem.NUM_DENS ; cnt++ )
	{
		ret.densValues[ cnt ] = ( (AStelem) in ).densValues[ cnt ];
	}
	ret.advanceDens( AStelem.C_DENS ,  this.densValues[ CsStelem.CS_DENS ] );
	ret.advanceDens( AStelem.NEGATION_DENS ,  this.densValues[ CsStelem.CS_NEGATION_DENS ] );
	ret.numer = this.numer.multiply( ( (AStelem) in ).numer );
	ret.denom = this.denom.multiply( ( (AStelem) in ).denom );
	return( ret );
}
if( in instanceof CsStelem )
{
	CsStelem ret = new CsStelem( this.fac );
	for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
	{
		ret.densValues[ cnt ] = this.densValues[ cnt ] + ( (CsStelem) in ).densValues[ cnt ];
	}
	ret.numer = this.numer.multiply( ( (CsStelem) in ).numer );
	ret.denom = this.denom.multiply( ( (CsStelem) in ).denom );
	return( ret );
}
else
{
	return( super.mult( in ) );
}

}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
negate( ) 
{
CsStelem ret = new CsStelem( this.fac );
for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
{
	ret.densValues[ cnt ] = this.densValues[ cnt ];
}
ret.advanceDens( CsStelem.CS_NEGATION_DENS , 1 );
ret.numer = this.numer;
ret.denom = this.denom;
return( ret );
}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
invertLeft( ) 
{
CsStelem ret = new CsStelem( this.fac );
for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
{
	ret.densValues[ cnt ] = this.densValues[ cnt ];
}
ret.densValues[ CsStelem.CS_DENS ] = - this.densValues[ CsStelem.CS_DENS ];
ret.numer = this.denom;
ret.denom = this.numer;
System.out.println( "###### " + (  ret.densValues[ CsStelem.CS_DENS ] ) );
System.out.println( this.densValues[ CsStelem.CS_DENS ] );
return( ret );
}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
invertRight( ) 
{
CsStelem ret = new CsStelem( this.fac );
for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
{
	ret.densValues[ cnt ] = this.densValues[ cnt ];
}
ret.densValues[ CsStelem.CS_DENS ] = - this.densValues[ CsStelem.CS_DENS ];
ret.numer = this.denom;
ret.denom = this.numer;
System.out.println( "###### " + (  ret.densValues[ CsStelem.CS_DENS ] ) );
System.out.println( this.densValues[ CsStelem.CS_DENS ] );
return( ret );
}




@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> evalCached(
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
	HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> cache)
	throws NotInvertibleException,
	MultiplicativeDistributionRequiredException {
final SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> key =
		new SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( this , implicitSpace );
final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> iret = cache.get( key );
if( iret != null )
{
	return( iret );
}
final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> ret =
		eval( implicitSpace );
cache.put(key, ret);
return( ret );
}


@Override
public String writeDesc(
	WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> cache,
	PrintStream ps) {

throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );

}





@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivative(
	ArrayList<? extends Elem<?, ?>> withRespectTo,
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
	throws NotInvertibleException, MultiplicativeDistributionRequiredException {

  return( fac.zero() );
}





@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivativeCached(
	ArrayList<? extends Elem<?, ?>> withRespectTo,
	HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
	HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> cache)
	throws NotInvertibleException, MultiplicativeDistributionRequiredException {

throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );

}





}









	

	
	


/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author thorngreen
 *
 */	
private static class AStelem extends SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
{	

/**
 * The index of the tensor component represented by the elem.
 */
protected ArrayList<BigInteger> index;




public static final int PSI_DENS = 0;

public static final int C_DENS = 1;

public static final int HBAR_SQUARED_DENS = 2;

public static final int M_SQUARED_DENS = 3;

public static final int NEGATION_DENS = 4;

public static final int T_PARTIAL_DENS = 5;

public static final int X_PARTIAL_DENS = 6;

public static final int Y_PARTIAL_DENS = 7;

public static final int Z_PARTIAL_DENS = 8;

public static final int NUM_DENS = 9;



protected int[] densValues = { 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 };





protected BigInteger numer = BigInteger.ONE;


protected BigInteger denom = BigInteger.ONE;




public void advanceDens( int dens_index , int dens_value )
{
	densValues[ dens_index ] += dens_value;
}



public ArrayList<BigInteger> getIndex()
{
	return( index );
}




/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _index The index of the tensor component represented by the elem.
 */
public AStelem( SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> _fac,
		ArrayList<BigInteger> _index ) {
	super(_fac);
	index = _index;
}


@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	
	
	CNelem ret = new CNelem( densValues , fac.getFac() );
	ret.numer = this.numer;
	ret.denom = this.denom;
	
	
	
	
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	
	
	
	
	return( ret );
	
	
}




@Override
public  SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
divideBy( BigInteger in )
{
	AStelem ret = new AStelem( this.fac , index );
	for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
	{
		ret.densValues[ cnt ] = this.densValues[ cnt ];
	}
	ret.numer = this.numer;
	ret.denom = this.denom.multiply( in );
	return( ret );
}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
mult( SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> in )
{
	if( in instanceof AStelem )
	{
		AStelem ret = new AStelem( this.fac , index );
		for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ] + ( (AStelem) in ).densValues[ cnt ];
		}
		ret.numer = this.numer.multiply( ( (AStelem) in ).numer );
		ret.denom = this.denom.multiply( ( (AStelem) in ).denom );
		return( ret );
	}
	else
	if( in instanceof CsStelem )
	{
		AStelem ret = new AStelem( this.fac , index );
		for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
		{
			ret.densValues[ cnt ] = this.densValues[ cnt ];
		}
		ret.advanceDens( AStelem.C_DENS , ( (CsStelem) in ).densValues[ CsStelem.CS_DENS ]  );
		ret.advanceDens( AStelem.NEGATION_DENS , ( (CsStelem) in ).densValues[ CsStelem.CS_NEGATION_DENS ]  );
		ret.numer = this.numer.multiply( ( (CsStelem) in ).numer );
		ret.denom = this.denom.multiply( ( (CsStelem) in ).denom );
		return( ret );
	}
	else
	{
		return( super.mult( in ) );
	}
}




@Override
public SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
negate( ) 
{
	AStelem ret = new AStelem( this.fac , index );
	for( int cnt = 0 ; cnt < NUM_DENS ; cnt++ )
	{
		ret.densValues[ cnt ] = this.densValues[ cnt ];
	}
	ret.advanceDens( AStelem.NEGATION_DENS , 1 );
	ret.numer = this.numer;
	ret.denom = this.denom;
	return( ret );
}




@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	final SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> key =
			new SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( this , implicitSpace );
	final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> iret = cache.get( key );
	if( iret != null )
	{
		return( iret );
	}
	final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>> ret =
			eval( implicitSpace );
	cache.put(key, ret);
	return( ret );
}


@Override
public String writeDesc(
		WriteElemCache<SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> cache,
		PrintStream ps) {
	
	throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
	
}





@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivative(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException, MultiplicativeDistributionRequiredException {
	
	CNelem ret = new CNelem( densValues , fac.getFac() );
	ret.numer = this.numer;
	ret.denom = this.denom;
	
	
	for( Elem<?,?>  wrt : withRespectTo )
	{
		final Ordinate ord = (Ordinate) wrt;
		final int col = ord.getCol();
		ret.advanceDens( AStelem.T_PARTIAL_DENS + col , 1 );
	}
	
	
	
//	ArrayList<Integer> hm = new ArrayList<Integer>();
//	hm.add( ret.getDen( AStelem.T_PARTIAL_DENS ) );
//	hm.add( ret.getDen( AStelem.X_PARTIAL_DENS ) );
//	hm.add( ret.getDen( AStelem.Y_PARTIAL_DENS ) );
//	hm.add( ret.getDen( AStelem.Z_PARTIAL_DENS ) );
//	
//	
//	int[] valsb = hmm.get( hm );
//	if( valsb == null )
//	{
//		valsb = new int[ AStelem.NUM_DENS ];
//	}
//	
//	
//	
//	valsb[ AStelem.T_PARTIAL_DENS ] = ret.getDen( AStelem.T_PARTIAL_DENS );
//	valsb[ AStelem.X_PARTIAL_DENS ] = ret.getDen( AStelem.X_PARTIAL_DENS );
//	valsb[ AStelem.Y_PARTIAL_DENS ] = ret.getDen( AStelem.Y_PARTIAL_DENS );
//	valsb[ AStelem.Z_PARTIAL_DENS ] = ret.getDen( AStelem.Z_PARTIAL_DENS );
//	
//	
//	
//	valsb[ AStelem.HBAR_SQUARED_DENS ] = Math.max( valsb[ AStelem.HBAR_SQUARED_DENS ]  , Math.abs( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) );
//	valsb[ AStelem.M_SQUARED_DENS ] = Math.max( valsb[ AStelem.M_SQUARED_DENS ]  , Math.abs( ret.getDen( AStelem.M_SQUARED_DENS ) ) );
//	
//	
//	if( ( ( ret.getDen( AStelem.Y_PARTIAL_DENS ) ) == 2 ) && ( ( ret.getDen( AStelem.Z_PARTIAL_DENS ) ) == 2 )  )
//	{
//		System.out.println( "************ " + ( ret.getDen( AStelem.T_PARTIAL_DENS ) ) + " // " + ( ret.getDen( AStelem.X_PARTIAL_DENS ) ) + " // " + ( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) + " // " + ( ret.getDen( AStelem.M_SQUARED_DENS ) ) );
//	}
//	
//	
//	
//	hmm.put( hm , valsb );
	
	
	
	
	return( ret );
	
}





@Override
public SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>, SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> cache)
		throws NotInvertibleException, MultiplicativeDistributionRequiredException {
	
	throw( new RuntimeException( "Not Supported!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" ) );
	
}





}
	
	
	
	
	








/**
 * Generates temporary indices for use in tensor arithmetic.
 * 
 * @author thorngreen
 *
 */
protected static class TestTemporaryIndexFactory extends TemporaryIndexFactory<String>
{
	/**
	 * The current temporary index.
	 */
	protected static int tempIndex = 0;

	@Override
	public String getTemp() {
		String ret = "temp" + tempIndex;
		tempIndex++;
		return( ret );
	}
	
}





/**
 * An symbolic elem for a tensor.  Used to represent the metric tensor.
 * 
 * @author tgreen
 *
 */
protected static class SymbolicMetricTensor extends SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>	
{

/**
 * The tensor to be represented by the symbolic elem.
 */
protected EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
	dval = null;


/**
 * Constructs the elem.
 * 
 * @param _fac The factory for the enclosed type.
 * @param _dval The tensor to be represented by the symbolic elem.
 */
public SymbolicMetricTensor(
		EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> _fac,
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
		_dval ) {
	super( _fac );
	dval = _dval;
}

@Override
public EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
	eval(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	return( dval );
}

@Override
public EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> evalCached(
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	return( dval );
}

@Override
public EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
	evalPartialDerivative(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	throw( new RuntimeException( "NotSupported" ) );
}

@Override
public EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> evalPartialDerivativeCached(
		ArrayList<? extends Elem<?, ?>> withRespectTo,
		HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
		HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>> cache)
		throws NotInvertibleException,
		MultiplicativeDistributionRequiredException {
	throw( new RuntimeException( "NotSupported" ) );
}

@Override
public String writeDesc(
		WriteElemCache<SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, SymbolicElemFactory<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>> cache,
		PrintStream ps) {
	String st = cache.get( this );
	if( st == null )
	{
		final String sta = fac.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>,EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>)( cache.getInnerCache() ) , ps);
		final String dvals = dval.writeDesc( (WriteElemCache<EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>,EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>)( cache.getInnerCache() ) , ps);
		st = cache.getIncrementVal();
		cache.put(this, st);
		ps.print( SymbolicMetricTensor.class.getSimpleName() );
		ps.print( " " );
		ps.print( st );
		ps.print( " = new " );
		ps.print( SymbolicMetricTensor.class.getSimpleName() );
		ps.print( "( " );
		ps.print( sta );
		ps.print( " , " );
		ps.print( dvals );
		ps.println( " );" );
	}
	return( st );
}


}





/**
 * Factory for generating an instance of the metric tensor.
 * 
 * @author thorngreen
 *
 */
protected static class TestMetricTensorFactory extends MetricTensorInvertingFactory<String, TestDimensionFour,
	SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
	SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
	>
{
	

	@Override
	public SymbolicElem<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>> getMetricTensor(
			boolean icovariantIndices, String index0, String index1) 
	{	
		final TestDimensionFour td = new TestDimensionFour();
		final DoubleElemFactory de2a = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> de = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de2a );
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		if( icovariantIndices ) covariantIndices.add( index0 ); else contravariantIndices.add( index0 );
		if( icovariantIndices ) covariantIndices.add( index1 ); else contravariantIndices.add( index1 );
		
		// final EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> de2 = new EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de );
		
		// final SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se = new SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( de2 );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de );
		
		// final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> se2 =
		//		new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2A = new SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( seA );
		
		
		// final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> se3 =
		//		new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se2A );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			ge =
				new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se3A );
	
		
		
		EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			g0 = new EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				( se3A , contravariantIndices , covariantIndices );
		

		for( int acnt = 0 ; acnt < SQ_TENSOR_SZ ; acnt++ )
		{
			if( ( acnt % TestDimensionFour.FOUR ) == ( acnt / TestDimensionFour.FOUR ) )
			{
				final ArrayList<BigInteger> ab2a = new ArrayList<BigInteger>();
				ab2a.add( BigInteger.ZERO );
				ab2a.add( BigInteger.ZERO );
				final ArrayList<BigInteger> ab2b = new ArrayList<BigInteger>();
				ab2b.add( BigInteger.ZERO );
				ab2b.add( BigInteger.ZERO );
				final AStelem aasa1 = new AStelem( se2A  , ab2a );
				final AStelem aasa2 = new AStelem( se2A  , ab2b );
				aasa1.advanceDens( AStelem.PSI_DENS , 1 );
				aasa2.advanceDens( AStelem.PSI_DENS , 1 );
				
				final ArrayList<BigInteger> ab = new ArrayList<BigInteger>();
				ab.add( BigInteger.valueOf( acnt / TestDimensionFour.FOUR ) );
				ab.add( BigInteger.valueOf( acnt % TestDimensionFour.FOUR ) );
				final int aar = acnt % TestDimensionFour.FOUR;
				SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
					t1 = aasa1;
				SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
					t2 = aasa2;
				aasa2.advanceDens( AStelem.T_PARTIAL_DENS + aar , 2 );
				
				
				
				
				if( aar == 0 )
				{
					aasa1.advanceDens( AStelem.C_DENS , 4 );
					aasa1.advanceDens( AStelem.NEGATION_DENS , 1 );
				}
				else
				{
					aasa1.advanceDens( AStelem.C_DENS , 4 );
				}
				
				
				if( aar == 0 )
				{
					aasa2.advanceDens( AStelem.HBAR_SQUARED_DENS , 1 );
					aasa2.advanceDens( AStelem.M_SQUARED_DENS , -1 );
				}
				else
				{
					aasa2.advanceDens( AStelem.HBAR_SQUARED_DENS , 1 );
					aasa2.advanceDens( AStelem.M_SQUARED_DENS , -1 );
					aasa1.advanceDens( AStelem.C_DENS , 2 );
				}
				
				g0.setVal( ab , t1.add( t2 ) );
				aasa2.advanceDens( AStelem.NEGATION_DENS , 1 );
			}
		}
		

		if( !icovariantIndices )
		{
			g0 = genMatrixInverseLeft( td , se3A , g0 );
		}
		
		
		final SymbolicMetricTensor seval = new SymbolicMetricTensor( ge , g0 );
		
		
		return( seval );
		
	}
	
	
	@Override
	public TestMetricTensorFactory cloneThread( final BigInteger threadIndex )
	{
		return( this );
	}

	
}





protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> distributeNode( 
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in , CsNelem multv )
{
	if( in instanceof SymbolicAdd )
	{
		SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> add = 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( in );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = distributeNode( add.getElemA() , multv );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e2 = distributeNode( add.getElemB() , multv );
		return( e1.add( e2 ) );
	}
	else if( in instanceof SymbolicNegate )
	{
		SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> neg = 
				(SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = distributeNode( neg.getElem() , multv );
		return( e1.negate() );
	}
	else if( in instanceof SymbolicDivideBy )
	{
		SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> div = 
				(SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = distributeNode( div.getElem() , multv );
		return( e1.divideBy( div.getIval() ) );
	}
	else
	{
		// System.out.println( "::::: " + in );
		return( multv.mult( in ) );
	}
	
}




protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> distributeNodeSym( 
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in , 
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> multv )
{
	if( in instanceof SymbolicAdd )
	{
		SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> add = 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( in );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeSym( add.getElemA() , multv );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e2 = distributeNodeSym( add.getElemB() , multv );
		return( e1.add( e2 ) );
	}
	else if( in instanceof SymbolicNegate )
	{
		SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> neg = 
				(SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeSym( neg.getElem() , multv );
		return( e1.negate() );
	}
	else if( in instanceof SymbolicDivideBy )
	{
		SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> div = 
				(SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeSym( div.getElem() , multv );
		return( e1.divideBy( div.getIval() ) );
	}
	else
	{
		// System.out.println( "::::: " + in );
		return( multv.mult( in ) );
	}
	
}






protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> distributeNodeNegate( 
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in ) throws NotInvertibleException
{
	if( in instanceof SymbolicAdd )
	{
		SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> add = 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( in );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeNegate( add.getElemA() );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e2 = distributeNodeNegate( add.getElemB() );
		return( e1.add( e2 ) );
	}
	else if( in instanceof SymbolicNegate )
	{
		SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> neg = 
				(SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeNegate( neg.getElem() );
		return( e1.negate() );
	}
	else if( in instanceof SymbolicDivideBy )
	{
		SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> div = 
				(SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeNegate( div.getElem() );
		return( e1.divideBy( div.getIval() ) );
	}
	else if( in instanceof SymbolicInvertLeft )
	{
		SymbolicInvertLeft<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> div = 
				(SymbolicInvertLeft<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeNegate( div.getElem() );
		return( e1.invertLeft() );
	}
	else if( in instanceof SymbolicInvertRight )
	{
		SymbolicInvertRight<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> div = 
				(SymbolicInvertRight<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeNegate( div.getElem() );
		return( e1.invertRight() );
	}
	else if( in instanceof SymbolicMult )
	{
		SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> mult = 
				(SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
			e1 = distributeNodeNegate( mult.getElemA() );
		return( e1.mult( mult.getElemB() ) );
	}
	else
	{
		// System.out.println( "::::: " + in );
		return( in.negate() );
	}
	
}






protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
	distributeNodeDivideBy( 
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in , 
			BigInteger idiv ) throws NotInvertibleException
{
	if( in instanceof SymbolicAdd )
	{
		SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> add = 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( in );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
				distributeNodeDivideBy( add.getElemA() , idiv );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e2 = 
				distributeNodeDivideBy( add.getElemB() , idiv );
		return( e1.add( e2 ) );
	}
	else if( in instanceof SymbolicNegate )
	{
		SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> neg = 
				(SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
				distributeNodeDivideBy( neg.getElem() , idiv );
		return( e1.negate() );
	}
	else if( in instanceof SymbolicDivideBy )
	{
		SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> div = 
				(SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
				distributeNodeDivideBy( div.getElem() , idiv );
		return( e1.divideBy( div.getIval() ) );
	}
	else if( in instanceof SymbolicInvertLeft )
	{
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
				distributeNodeDivideBy( distNode( in ) , idiv );
		return( e1 );
	}
	else if( in instanceof SymbolicInvertRight )
	{
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
				distributeNodeDivideBy( distNode( in ) , idiv );
		return( e1 );
	}
	else if( in instanceof SymbolicMult )
	{
		SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> mult = 
				(SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
				distributeNodeDivideBy( mult.getElemA() , idiv );
		return( e1.mult( mult.getElemB() ) );
	}
	else
	{
		// System.out.println( "::::: " + in );
		return( in.negate() );
	}
	
}





protected static void parseMult( SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> mult , 
		ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> els , 
		ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> others )
{
	
	if( mult.getElemA() instanceof SymbolicMult )
	{
		parseMult( (SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( mult.getElemA() ) , 
				els , others );
	}
	else if( ( mult.getElemA() instanceof CNelem ) || ( mult.getElemA() instanceof CsNelem ) )
	{
		if( els.get( 0 ) == null )
		{
			els.set( 0 , mult.getElemA() );
		}
		else
		{
			// System.out.println( "Got!!!!!!!!!!!!!!!!!!" );
			els.set( 0 , els.get( 0 ).mult( mult.getElemA() ) );
		}
	}
	else
	{
		others.add( mult.getElemA() );
	}
	
	
	
	if( mult.getElemB() instanceof SymbolicMult )
	{
		parseMult( (SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( mult.getElemB() ) , 
				els , others );
	}
	else if( ( mult.getElemB() instanceof CNelem ) || ( mult.getElemB() instanceof CsNelem ) )
	{
		if( els.get( 0 ) == null )
		{
			els.set( 0 , mult.getElemB() );
		}
		else
		{
			// System.out.println( "Got!!!!!!!!!!!!!!!!!!" );
			els.set( 0 , els.get( 0 ).mult( mult.getElemB() ) );
		}
	}
	else
	{
		others.add( mult.getElemB() );
	}
	
	
	
}





protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> expandAroundIdentity( 
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in )
{
	
	final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
		ident = new CsNelem( in.getFac().getFac() );
	
	// f( x ) = 1 - ( x - 1 ) + ( x - 1 ) ^ 2 - ( x - 1 ) ^ 3 ...
	
	final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> t0 = 
			ident;
	
	final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> t1 = 
			in.add( ident.negate() );
	
	final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> ret = 
			t0.add( t1.negate() );
	
	return( ret );
}




protected static CsNelem srchCsNelem( 
		SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in )
{
	
	if( in.getElemA() instanceof CsNelem )
	{
		return( (CsNelem)( in.getElemA() ) );
	}
	
	if( in.getElemB() instanceof CsNelem )
	{
		return( (CsNelem)( in.getElemB() ) );
	}
	
	if( in.getElemA() instanceof SymbolicAdd )
	{
		CsNelem el = srchCsNelem( 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( in.getElemA() ) );
		if( el != null ) return( el );
	}
	
	if( in.getElemB() instanceof SymbolicAdd )
	{
		CsNelem el = srchCsNelem( 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( in.getElemB() ) );
		if( el != null ) return( el );
	}
	
	
	return( null );
	
	
}





protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> parseInvert( 
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in ) throws NotInvertibleException
{
	if( in instanceof CsNelem )
	{
		CsNelem nd = (CsNelem) in;
		return( nd.invertLeft() );
	}
	
	
	if( in instanceof SymbolicAdd )
	{
		SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> av = 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		CsNelem nel = srchCsNelem( av );
		if( nel != null )
		{
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				ninv = nel.invertLeft();
			return( distNode( ninv.mult( expandAroundIdentity( ninv.mult( in ) ) ) ) );
		}
 	}
	
	
	return( distNode( expandAroundIdentity( in ) ) );
}




static boolean debugOn = false;




protected static SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>
	distNode( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in  ) throws NotInvertibleException
{
	
	if( debugOn )
	{
		if( !( in instanceof SymbolicAdd ) && !( in instanceof CNelem ) && !( in instanceof CsNelem ) )
		{
			System.out.println( in );
		}
	}
	
	if( in instanceof SymbolicAdd )
	{
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = distNode( ( 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemA()  );
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e2 = distNode( ( 
				(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemB()  );
		return( e1.add( e2 ) );
	}
	else
	if( in instanceof SymbolicMult )
	{
		SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> s1 = 
				(SymbolicMult<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		if( ( s1.getElemA() instanceof CsNelem ) && ( s1.getElemB() instanceof SymbolicAdd ) )
		{
			// System.out.println( "CaughtA..." );
			return( distNode( distributeNode( s1.getElemB() , (CsNelem)( s1.getElemA() ) ) ) );
		}
		else if( ( s1.getElemA() instanceof SymbolicAdd ) && ( s1.getElemB() instanceof CsNelem ) )
		{
			// System.out.println( "CaughtB..." );
			return( distNode( distributeNode( s1.getElemA() , (CsNelem)( s1.getElemB() ) ) ) );
		}
		else if( s1.getElemB() instanceof SymbolicAdd )
		{
			return( distNode( distributeNodeSym( s1.getElemB() , s1.getElemA() ) ) );
		}
		else if( s1.getElemA() instanceof SymbolicAdd )
		{
			return( distNode( distributeNodeSym( s1.getElemA() , s1.getElemB() ) ) );
		}
		else if( ( s1.getElemA() instanceof CsNelem ) && ( s1.getElemB() instanceof SymbolicDivideBy ) )
		{
			return( distNode( distributeNode( s1.getElemB() , (CsNelem)( s1.getElemA() ) ) ) );
		}
		else if( ( s1.getElemA() instanceof SymbolicDivideBy ) && ( s1.getElemB() instanceof CsNelem ) )
		{
			return( distNode( distributeNode( s1.getElemA() , (CsNelem)( s1.getElemB() ) ) ) );
		}
		else if( ( s1.getElemA() instanceof SymbolicMult ) || ( s1.getElemB() instanceof SymbolicMult ) )
		{
			ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> els = 
					new ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>();
			els.add( null );
			ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> others = 
					new ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>();
			parseMult( s1 , els , others );
			if( els.get( 0 ) != null )
			{
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
					a = els.get( 0 );
				for( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
						b : others )
				{
					a = a.mult( distNode( b ) );
				}
				return( a );
			}
			else
			{
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
					e1 = distNode( s1.getElemA() );
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
					e2 = distNode( s1.getElemB() );
				return( e1.mult( e2 ) );
			}
		}
		else
		{
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				e1 = distNode( s1.getElemA() );
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				e2 = distNode( s1.getElemB() );
			return( e1.mult( e2 ) );
		}
	}
	else
	if( in instanceof SymbolicNegate )
	{
		final SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> sneg = 
				(SymbolicNegate<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		return( distNode( distributeNodeNegate( sneg.getElem() ) ) );
	}
	else
	if( in instanceof SymbolicDivideBy )
	{
		final SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> sdiv = 
				(SymbolicDivideBy<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in;
		if( sdiv.getElem() instanceof SymbolicAdd )
		{
			if( debugOn )
			{
				System.out.println( "FoundB..." );
				debugOn = false;
			}
			final SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> sdd = 
					(SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>)( sdiv.getElem() );
			final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				ea = distNode( sdd.getElemA().divideBy( sdiv.getIval() ) );
			final SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				eb = distNode( sdd.getElemB().divideBy( sdiv.getIval() ) );
			return( ea.add( eb ) );
		}
		else
		{
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> 
				e1 = distNode( sdiv.getElem() );
			return( e1.divideBy( sdiv.getIval() ) );
		}
		
	}
	else
	if( in instanceof SymbolicInvertLeft )
	{
		try
		{
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
					distNode( ( (SymbolicInvertLeft<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElem() );
			return( distNode( parseInvert( e1 ) ) );
		}
		catch( NotInvertibleException ex )
		{
			throw( new RuntimeException( "Failed..." ) );
		}
	}
	else
	if( in instanceof SymbolicInvertRight )
	{
		try
		{
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> e1 = 
					distNode( ( (SymbolicInvertRight<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElem() );
			return( distNode( parseInvert( e1 ) ) );
		}
		catch( NotInvertibleException ex )
		{
			throw( new RuntimeException( "Failed..." ) );
		}
	}
	else
	{
		if( !( in instanceof CsNelem ) && !( in instanceof CNelem ) )
		{
			throw( new RuntimeException( "FailA." ) );
		}
		return( in );
	}
	
}






protected static void hndlNode( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in , 
		ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> stk , 
		final HashMap<ArrayList<Integer>,ArrayList<CNelem>> hmm )
{
	ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> stk2 = 
			new ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>( stk );
	stk2.add( in );
	if( in instanceof SymbolicAdd )
	{
		hndlNode( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemA() , 
				stk2 , hmm );
		hndlNode( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemB() , 
				stk2 , hmm );
	}
	else
	if( in instanceof SymbolicMult )
	{
		// hndlNode( ( (SymbolicMult) in ).getElemA() , stk2  );
		// hndlNode( ( (SymbolicMult) in ).getElemB() , stk2  );
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicNegate )
	{
		// hndlNode( ( (SymbolicNegate) in ).getElem() , stk2 );
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicDivideBy )
	{
		// hndlNode( ( (SymbolicDivideBy) in ).getElem() , stk2 );
		throw( new RuntimeException( "Fail." ) );
	}
	else 
	if( in instanceof CsNelem )
	{
//		System.out.println( "*** " + ( stk.get( stk.size() - 1 ) ) );
//		System.out.println( ( (CsNelem) in ).densValues[ CsStelem.CS_DENS ] );
//		if( ( stk.get( stk.size() - 1 ) ) instanceof SymbolicMult )
//		{
//			SymbolicMult smm = (SymbolicMult)( ( stk.get( stk.size() - 1 ) ) );
//			System.out.println( "))))))))))))" );
//			for( Object i : stk )
//			{
//				System.out.println( i );
//			}
//			System.out.println( ">>>>> " + ( smm.getElemA() ) );
//			System.out.println( ">>>>> " + ( smm.getElemB() ) );
//		}
		throw( new RuntimeException( "Fail." ) );
	}
	else 
	if( in instanceof CNelem )
	{
		CNelem ret = (CNelem) in;
		ArrayList<Integer> hm = new ArrayList<Integer>();
		hm.add( ret.getDen( AStelem.T_PARTIAL_DENS ) );
		hm.add( ret.getDen( AStelem.X_PARTIAL_DENS ) );
		hm.add( ret.getDen( AStelem.Y_PARTIAL_DENS ) );
		hm.add( ret.getDen( AStelem.Z_PARTIAL_DENS ) );
		
		
		ArrayList<CNelem> valsb = hmm.get( hm );
		if( valsb == null )
		{
			// valsb = new int[ AStelem.NUM_DENS ];
			valsb = new ArrayList<CNelem>();
		}
		
		
		
//		valsb[ AStelem.T_PARTIAL_DENS ] = ret.getDen( AStelem.T_PARTIAL_DENS );
//		valsb[ AStelem.X_PARTIAL_DENS ] = ret.getDen( AStelem.X_PARTIAL_DENS );
//		valsb[ AStelem.Y_PARTIAL_DENS ] = ret.getDen( AStelem.Y_PARTIAL_DENS );
//		valsb[ AStelem.Z_PARTIAL_DENS ] = ret.getDen( AStelem.Z_PARTIAL_DENS );
		
		
		
//		valsb[ AStelem.HBAR_SQUARED_DENS ] = Math.max( valsb[ AStelem.HBAR_SQUARED_DENS ]  , Math.abs( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) );
//		valsb[ AStelem.M_SQUARED_DENS ] = Math.max( valsb[ AStelem.M_SQUARED_DENS ]  , Math.abs( ret.getDen( AStelem.M_SQUARED_DENS ) ) );
//		valsb[ AStelem.C_DENS ] = Math.max( valsb[ AStelem.C_DENS ]  , Math.abs( ret.getDen( AStelem.C_DENS ) ) );
//		
		
//		if( ( ( ret.getDen( AStelem.Y_PARTIAL_DENS ) ) == 2 ) && ( ( ret.getDen( AStelem.Z_PARTIAL_DENS ) ) == 2 )  )
//		{
//			System.out.println( ">>>>>>>>>>>>>>>>>>>>>> " + ( ret.getDen( AStelem.T_PARTIAL_DENS ) ) + " // " + ( ret.getDen( AStelem.X_PARTIAL_DENS ) ) + " // " + ( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) + " // " + ( ret.getDen( AStelem.M_SQUARED_DENS ) ) );
//		}
		
		
		valsb.add( (CNelem) in );
		
		
		hmm.put( hm , valsb );
	}
	else
	{
		// System.out.println( in );
	}
	
}






protected static void hndlNode2( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in , 
		DoubleElem cval , DoubleElem hbsval , DoubleElem msval ,
		final HashMap<ArrayList<Integer>,DoubleElem> hmm2 ) throws NotInvertibleException
{
	
	if( in instanceof SymbolicAdd )
	{
		hndlNode2( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemA() , 
				cval , hbsval , msval , hmm2 );
		hndlNode2( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemB() , 
				cval , hbsval , msval , hmm2 );
	}
	else
	if( in instanceof SymbolicMult )
	{
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicNegate )
	{
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicDivideBy )
	{
		throw( new RuntimeException( "Fail." ) );
	}
	else 
	if( in instanceof CsNelem )
	{
		System.out.println( in );
		throw( new RuntimeException( "Fail." ) );
//		System.out.println( "*** " + ( stk.get( stk.size() - 1 ) ) );
//		System.out.println( ( (CsNelem) in ).densValues[ CsStelem.CS_DENS ] );
//		if( ( stk.get( stk.size() - 1 ) ) instanceof SymbolicMult )
//		{
//			SymbolicMult smm = (SymbolicMult)( ( stk.get( stk.size() - 1 ) ) );
//			System.out.println( "))))))))))))" );
//			for( Object i : stk )
//			{
//				System.out.println( i );
//			}
//			System.out.println( ">>>>> " + ( smm.getElemA() ) );
//			System.out.println( ">>>>> " + ( smm.getElemB() ) );
//		}
	}
	else 
	if( in instanceof CNelem )
	{
		CNelem ret = (CNelem) in;
		ArrayList<Integer> hm = new ArrayList<Integer>();
		hm.add( ret.getDen( AStelem.T_PARTIAL_DENS ) );
		hm.add( ret.getDen( AStelem.X_PARTIAL_DENS ) );
		hm.add( ret.getDen( AStelem.Y_PARTIAL_DENS ) );
		hm.add( ret.getDen( AStelem.Z_PARTIAL_DENS ) );
		
		
		DoubleElem valsb = hmm2.get( hm );
		if( valsb == null )
		{
			// valsb = new int[ AStelem.NUM_DENS ];
			valsb = new DoubleElem( 0.0 );
		}
		
		
		
		DoubleElem cexp = new DoubleElem( 1.0 );
		
		for( int cnt = 0 ; cnt < Math.abs( ret.getDen( AStelem.C_DENS ) ) ; cnt++ )
		{
			cexp = cexp.mult( cval );
		}
		if( ret.getDen( AStelem.C_DENS ) < 0 )
		{
			cexp = cexp.invertLeft();
		}
		
		
		
		DoubleElem hbsexp = new DoubleElem( 1.0 );
		
		for( int cnt = 0 ; cnt < Math.abs( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) ; cnt++ )
		{
			hbsexp = hbsexp.mult( hbsval );
		}
		if( ret.getDen( AStelem.HBAR_SQUARED_DENS ) < 0 )
		{
			hbsexp = hbsexp.invertLeft();
		}
		
		
		
		DoubleElem msexp = new DoubleElem( 1.0 );
		
		for( int cnt = 0 ; cnt < Math.abs( ret.getDen( AStelem.M_SQUARED_DENS ) ) ; cnt++ )
		{
			msexp = msexp.mult( msval );
		}
		if( ret.getDen( AStelem.M_SQUARED_DENS ) < 0 )
		{
			msexp = msexp.invertLeft();
		}
		
		

		DoubleElem dvalA = cexp.mult( hbsexp ).mult( msexp );
		
		
		if( ( ret.getDen( AStelem.NEGATION_DENS ) ) % 2 != 0 )
		{
			dvalA = dvalA.negate();
		}
		

		
		dvalA = dvalA.divideBy( ret.denom ).mult( ( new DoubleElem( 1.0 ) ).divideBy( ret.numer ).invertLeft()  );
		
		
		
		valsb = valsb.add( dvalA );
		
		
		hmm2.put( hm , valsb );
	}
	else
	{
		// System.out.println( in );
	}
	
}






protected static int hndlSrchM( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in ) throws NotInvertibleException
{
	
	if( in instanceof SymbolicAdd )
	{
		final int a = hndlSrchM( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemA() );
		final int b = hndlSrchM( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemB() );
		return( Math.max(a, b) );
	}
	else
	if( in instanceof SymbolicMult )
	{
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicNegate )
	{
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicDivideBy )
	{
		throw( new RuntimeException( "Fail." ) );
	}
	else 
	if( in instanceof CsNelem )
	{
		System.out.println( in );
		throw( new RuntimeException( "Fail." ) );
//		System.out.println( "*** " + ( stk.get( stk.size() - 1 ) ) );
//		System.out.println( ( (CsNelem) in ).densValues[ CsStelem.CS_DENS ] );
//		if( ( stk.get( stk.size() - 1 ) ) instanceof SymbolicMult )
//		{
//			SymbolicMult smm = (SymbolicMult)( ( stk.get( stk.size() - 1 ) ) );
//			System.out.println( "))))))))))))" );
//			for( Object i : stk )
//			{
//				System.out.println( i );
//			}
//			System.out.println( ">>>>> " + ( smm.getElemA() ) );
//			System.out.println( ">>>>> " + ( smm.getElemB() ) );
//		}
	}
	else 
	if( in instanceof CNelem )
	{
		CNelem ret = (CNelem) in;
		return( ret.getDen( AStelem.M_SQUARED_DENS ) );
	}
	else
	{
		System.out.println( in );
		throw( new RuntimeException( "Fail." ) );
	}
	
}












protected static void hndlNodeSrchMv( SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> in , 
		ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> stk , 
		final HashMap<ArrayList<Integer>,ArrayList<CNelem>> hmmx , final int multv )
{
	ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>> stk2 = 
			new ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>( stk );
	stk2.add( in );
	if( in instanceof SymbolicAdd )
	{
		hndlNodeSrchMv( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemA() , 
				stk2 , hmmx , multv );
		hndlNodeSrchMv( ( (SymbolicAdd<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>) in ).getElemB() , 
				stk2 , hmmx , multv );
	}
	else
	if( in instanceof SymbolicMult )
	{
		// hndlNode( ( (SymbolicMult) in ).getElemA() , stk2  );
		// hndlNode( ( (SymbolicMult) in ).getElemB() , stk2  );
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicNegate )
	{
		// hndlNode( ( (SymbolicNegate) in ).getElem() , stk2 );
		throw( new RuntimeException( "Fail." ) );
	}
	else
	if( in instanceof SymbolicDivideBy )
	{
		// hndlNode( ( (SymbolicDivideBy) in ).getElem() , stk2 );
		throw( new RuntimeException( "Fail." ) );
	}
	else 
	if( in instanceof CsNelem )
	{
//		System.out.println( "*** " + ( stk.get( stk.size() - 1 ) ) );
//		System.out.println( ( (CsNelem) in ).densValues[ CsStelem.CS_DENS ] );
//		if( ( stk.get( stk.size() - 1 ) ) instanceof SymbolicMult )
//		{
//			SymbolicMult smm = (SymbolicMult)( ( stk.get( stk.size() - 1 ) ) );
//			System.out.println( "))))))))))))" );
//			for( Object i : stk )
//			{
//				System.out.println( i );
//			}
//			System.out.println( ">>>>> " + ( smm.getElemA() ) );
//			System.out.println( ">>>>> " + ( smm.getElemB() ) );
//		}
		throw( new RuntimeException( "Fail." ) );
	}
	else 
	if( in instanceof CNelem )
	{
		CNelem ret = (CNelem) in;
		if( ret.getDen( AStelem.M_SQUARED_DENS ) == multv )
		{
			ArrayList<Integer> hm = new ArrayList<Integer>();
			hm.add( ret.getDen( AStelem.T_PARTIAL_DENS ) );
			hm.add( ret.getDen( AStelem.X_PARTIAL_DENS ) );
			hm.add( ret.getDen( AStelem.Y_PARTIAL_DENS ) );
			hm.add( ret.getDen( AStelem.Z_PARTIAL_DENS ) );
		
		
			ArrayList<CNelem> valsb = hmmx.get( hm );
			if( valsb == null )
			{
				// valsb = new int[ AStelem.NUM_DENS ];
				valsb = new ArrayList<CNelem>();
			}
		
		
		
//		valsb[ AStelem.T_PARTIAL_DENS ] = ret.getDen( AStelem.T_PARTIAL_DENS );
//		valsb[ AStelem.X_PARTIAL_DENS ] = ret.getDen( AStelem.X_PARTIAL_DENS );
//		valsb[ AStelem.Y_PARTIAL_DENS ] = ret.getDen( AStelem.Y_PARTIAL_DENS );
//		valsb[ AStelem.Z_PARTIAL_DENS ] = ret.getDen( AStelem.Z_PARTIAL_DENS );
		
		
		
//		valsb[ AStelem.HBAR_SQUARED_DENS ] = Math.max( valsb[ AStelem.HBAR_SQUARED_DENS ]  , Math.abs( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) );
//		valsb[ AStelem.M_SQUARED_DENS ] = Math.max( valsb[ AStelem.M_SQUARED_DENS ]  , Math.abs( ret.getDen( AStelem.M_SQUARED_DENS ) ) );
//		valsb[ AStelem.C_DENS ] = Math.max( valsb[ AStelem.C_DENS ]  , Math.abs( ret.getDen( AStelem.C_DENS ) ) );
//		
		
//		if( ( ( ret.getDen( AStelem.Y_PARTIAL_DENS ) ) == 2 ) && ( ( ret.getDen( AStelem.Z_PARTIAL_DENS ) ) == 2 )  )
//		{
//			System.out.println( ">>>>>>>>>>>>>>>>>>>>>> " + ( ret.getDen( AStelem.T_PARTIAL_DENS ) ) + " // " + ( ret.getDen( AStelem.X_PARTIAL_DENS ) ) + " // " + ( ret.getDen( AStelem.HBAR_SQUARED_DENS ) ) + " // " + ( ret.getDen( AStelem.M_SQUARED_DENS ) ) );
//		}
		
		
			valsb.add( (CNelem) in );
		
		
			hmmx.put( hm , valsb );
		}
	}
	else
	{
		// System.out.println( in );
	}
	
}



	


/**
 * Tests the ability to numerically evaluate the differential equation <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 *  <mo>=</mo>
 *  <mn>0</mn>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *   <msub>
 *           <mi>G</mi>
 *       <mrow>
 *         <mi>u</mi>
 *         <mi>v</mi>
 *       </mrow>
 *   </msub>
 * </mrow>
 * </math> is the Einstein tensor.
 * 
 * 
 * 
 * See http://en.wikipedia.org/wiki/Einstein_tensor
 *
 *
 */
public void testStelemSimple() throws NotInvertibleException, MultiplicativeDistributionRequiredException, FileNotFoundException
	{
	    
		
		final TestDimensionFour tdim = new TestDimensionFour();
		
		// final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
		
		
		

		
		
		final DoubleElemFactory de2a = new DoubleElemFactory();
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> de = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de2a );
		
		final ArrayList<String> contravariantIndices = new ArrayList<String>();
		final ArrayList<String> covariantIndices = new ArrayList<String>();
		covariantIndices.add( "u" );
		covariantIndices.add( "v" );
		
		final EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> de2 = new EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de );
		
		final SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se = new SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( de2 );
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de );
		
		final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> se2 =
				new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se );
		
		final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2A = new SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( seA );
		
		
		// final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> se3 =
		//		new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se2 );
		
		final SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
			se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se2A );
		
		
		
		
		
		final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
		
		wrtT.add( new Ordinate( de2 , TV ) );
		
		// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
		
		// wrtX.add( new AElem( de , 1 ) );
		
		
		final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			ge =
			new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se3A );
		
		
		
		
		final DDirec ddirec = new DDirec(de2, se2A);
		
		
		
		final OrdinaryDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			Ordinate> odf =
			new OrdinaryDerivativeFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				Ordinate>( ge , tdim , ddirec , null );
		
		
		
		MetricTensorFactory<String, SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
			metricZ = new TestMetricTensorFactory( );
		
//		EinsteinTensorFactory<String,TestDimensionFour,
//				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//				Ordinate> 
//				et = new EinsteinTensorFactory<String,TestDimensionFour,
//					SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//					SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//					Ordinate>
//					( ge , metricZ , new TestTemporaryIndexFactory() , odf );
		
		
		QuantumConversionTestFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				Ordinate> 
				et = new QuantumConversionTestFactory<String,TestDimensionFour,
					SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
					SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
					Ordinate>
				( ge , metricZ , new TestTemporaryIndexFactory() , odf );
		
		
		System.out.println( "Reached #1..." );
		
		
		HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>> 
			cache3A = new HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>();
		
		
		final EinsteinTensorElem<
			String,
			SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
			SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				curv = et.getEinsteinTensor( "u" , "v" ).evalCached( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ , cache3A );
	
		
		cache3A = null;
		
		
		System.out.println( "Reached #2..." );
		
		
		final EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
				mg1 = curv;
		
		
		ArrayList<BigInteger> vve = new ArrayList<BigInteger>();
		vve.add( BigInteger.ZERO );
		vve.add( BigInteger.ZERO );
		
		SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
				svvA = mg1.getVal( vve );
		
		
		System.out.println( svvA ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		System.out.println( "Reached #3..." );
		
	
		
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> svvB = svvA.eval( null );
		
		
		System.out.println( svvB ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		svvB = svvB.distributeSimplify2();
		
		
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );
		
		debugOn = true;
		
		System.out.println( "RunDist..." );
		svvB = distNode( svvB );

		
		PrintStream ops = new PrintStream( new FileOutputStream( "outC.txt" ) );
		
		
		HashMap<ArrayList<Integer>,ArrayList<CNelem>> hmm = new HashMap<ArrayList<Integer>,ArrayList<CNelem>>();
		
		
		hndlNode( svvB , new ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>() , hmm );
		
		
		for( final Entry<ArrayList<Integer>, ArrayList<CNelem>> grr : hmm.entrySet()  )
		{
			
			ArrayList<Integer> arr = grr.getKey();
			
			
			if( /* ( arr.get( 0 ) == 0 ) && ( arr.get( 1 ) == 0 ) && 
					( arr.get( 2 ) == 0 ) && ( arr.get( 3 ) == 0 ) */ /* true */ false )
			{
					
				
				ops.println( "************ " + ( arr.get( 0 ) ) + " // " + ( arr.get( 1 ) ) + " // " 
						+ ( arr.get( 2 ) ) + " // " + ( arr.get( 3 ) ) );
				
				ops.println( grr.getValue().size() );
				
				
				for( final CNelem aa : grr.getValue() )
				{
					
					
					BigInteger numer = ( ( ( aa.densValues[ AStelem.NEGATION_DENS ] ) & 1 ) != 0 ) ? aa.numer : aa.numer.negate();
					
					BigInteger denom = aa.denom;
					
					ops.print( "************ ************  ( " + numer + " / " + denom + " ) " );
					
					
					if( aa.densValues[ AStelem.HBAR_SQUARED_DENS ] != 0 )
					{
						ops.print( "( HB ^ " + ( 2 * aa.densValues[ AStelem.HBAR_SQUARED_DENS ] ) + " ) " );
					}
					
					
					if( aa.densValues[ AStelem.M_SQUARED_DENS ] != 0 )
					{
						ops.print( "( M ^ " + ( 2 * aa.densValues[ AStelem.M_SQUARED_DENS ] ) + " ) " );
					}
					
					
					if( aa.densValues[ AStelem.C_DENS ] != 0 )
					{
						ops.print( "( C ^ " + ( aa.densValues[ AStelem.C_DENS ] ) + " ) " );
					}
					
					
					ops.println( "" );
					break;
					
				}
						
						
					//	" **** " 
					//	+ ( arr[ AStelem.HBAR_SQUARED_DENS ] ) + " // " + ( arr[ AStelem.M_SQUARED_DENS ] ) + " // " + ( arr[ AStelem.C_DENS ] ) );
			}
			

			
		}
		
		
		final HashMap<ArrayList<Integer>,DoubleElem> hmm2 = new HashMap<ArrayList<Integer>,DoubleElem>();
		
		
		hndlNode2( svvB , new DoubleElem( 0.1 ) , new DoubleElem( 0.2 ) , new DoubleElem( 0.4 ) , hmm2 );
		
		
		
		for( final Entry<ArrayList<Integer>, DoubleElem> grr : hmm2.entrySet()  )
		{
			if( ( Math.abs( grr.getValue().getVal() ) ) > 1E-8 )
			{
				ArrayList<Integer> arr = grr.getKey();
			
				ops.println( "************ " + ( arr.get( 0 ) ) + " // " + ( arr.get( 1 ) ) + " // " 
						+ ( arr.get( 2 ) ) + " // " + ( arr.get( 3 ) ) );
			
				ops.println( grr.getValue().getVal() );
			}
			
		}
		
		
		
		
		final int mval = hndlSrchM( svvB );
		
		
		System.out.println( "Mval" );
		
		System.out.println( mval );
		
		
		
		
		hmm = new HashMap<ArrayList<Integer>,ArrayList<CNelem>>();
		
		
		hndlNodeSrchMv( svvB , new ArrayList<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>>>() , hmm , mval );
		
		
		for( final Entry<ArrayList<Integer>, ArrayList<CNelem>> grr : hmm.entrySet()  )
		{
			
			ArrayList<Integer> arr = grr.getKey();
			
			
			if( /* ( arr.get( 0 ) == 0 ) && ( arr.get( 1 ) == 0 ) && 
					( arr.get( 2 ) == 0 ) && ( arr.get( 3 ) == 0 ) */ /* true */ true )
			{
					
				
				ops.println( "************ " + ( arr.get( 0 ) ) + " // " + ( arr.get( 1 ) ) + " // " 
						+ ( arr.get( 2 ) ) + " // " + ( arr.get( 3 ) ) );
				
				ops.println( grr.getValue().size() );
				
				
				for( final CNelem aa : grr.getValue() )
				{
					
					
					BigInteger numer = ( ( ( aa.densValues[ AStelem.NEGATION_DENS ] ) & 1 ) != 0 ) ? aa.numer : aa.numer.negate();
					
					BigInteger denom = aa.denom;
					
					ops.print( "************ ************  ( " + numer + " / " + denom + " ) " );
					
					
					if( aa.densValues[ AStelem.HBAR_SQUARED_DENS ] != 0 )
					{
						ops.print( "( HB ^ " + ( 2 * aa.densValues[ AStelem.HBAR_SQUARED_DENS ] ) + " ) " );
					}
					
					
					if( aa.densValues[ AStelem.M_SQUARED_DENS ] != 0 )
					{
						ops.print( "( M ^ " + ( 2 * aa.densValues[ AStelem.M_SQUARED_DENS ] ) + " ) " );
					}
					
					
					if( aa.densValues[ AStelem.C_DENS ] != 0 )
					{
						ops.print( "( C ^ " + ( aa.densValues[ AStelem.C_DENS ] ) + " ) " );
					}
					
					
					ops.println( "" );
					break;
					
				}
						
						
					//	" **** " 
					//	+ ( arr[ AStelem.HBAR_SQUARED_DENS ] ) + " // " + ( arr[ AStelem.M_SQUARED_DENS ] ) + " // " + ( arr[ AStelem.C_DENS ] ) );
			}
			

			
		}
		
		
		
		ops.close();
		
		
		
		
	}






public void genCoeff( final DoubleElem cval , final DoubleElem hbsval , final DoubleElem msval , final HashMap<ArrayList<Integer>,DoubleElem> hmm2 ) throws NotInvertibleException, MultiplicativeDistributionRequiredException, FileNotFoundException
{
    
	
	final TestDimensionFour tdim = new TestDimensionFour();
	
	// final SpacetimeAlgebraOrd<TestDimensionFour> ord = new SpacetimeAlgebraOrd<TestDimensionFour>();
	
	
	

	
	
	final DoubleElemFactory de2a = new DoubleElemFactory();
	final ComplexElemFactory<DoubleElem,DoubleElemFactory> de = new ComplexElemFactory<DoubleElem,DoubleElemFactory>( de2a );
	
	final ArrayList<String> contravariantIndices = new ArrayList<String>();
	final ArrayList<String> covariantIndices = new ArrayList<String>();
	covariantIndices.add( "u" );
	covariantIndices.add( "v" );
	
	final EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> de2 = new EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de );
	
	final SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se = new SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( de2 );
	
	final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> seA = new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( de );
	
	final SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> se2 =
			new SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se );
	
	final SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> se2A = new SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( seA );
	
	
	// final SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>> se3 =
	//		new SymbolicElemFactory<SymbolicElem<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<EinsteinTensorElem<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,EinsteinTensorElemFactory<String,ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se2 );
	
	final SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
		se3A = new SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>( se2A );
	
	
	
	
	
	final ArrayList<Ordinate> wrtT = new ArrayList<Ordinate>();
	
	wrtT.add( new Ordinate( de2 , TV ) );
	
	// final ArrayList<AElem> wrtX = new ArrayList<AElem>();
	
	// wrtX.add( new AElem( de , 1 ) );
	
	
	final EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
		ge =
		new EinsteinTensorElemFactory<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>( se3A );
	
	
	
	
	final DDirec ddirec = new DDirec(de2, se2A);
	
	
	
	final OrdinaryDerivativeFactory<String,TestDimensionFour,
		SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
		Ordinate> odf =
		new OrdinaryDerivativeFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			Ordinate>( ge , tdim , ddirec , null );
	
	
	
	MetricTensorFactory<String, SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>> 
		metricZ = new TestMetricTensorFactory( );
	
//	EinsteinTensorFactory<String,TestDimensionFour,
//			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//			Ordinate> 
//			et = new EinsteinTensorFactory<String,TestDimensionFour,
//				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
//				Ordinate>
//				( ge , metricZ , new TestTemporaryIndexFactory() , odf );
	
	
	QuantumConversionTestFactory<String,TestDimensionFour,
			SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
			Ordinate> 
			et = new QuantumConversionTestFactory<String,TestDimensionFour,
				SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,
				Ordinate>
			( ge , metricZ , new TestTemporaryIndexFactory() , odf );
	
	
	System.out.println( "Reached #1..." );
	
	
	HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>> 
		cache3A = new HashMap<SCacheKey<EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>, EinsteinTensorElemFactory<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>, EinsteinTensorElem<String, SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>, SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>>();
	
	
	final EinsteinTensorElem<
		String,
		SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>, 
		SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			curv = et.getEinsteinTensor( "u" , "v" ).evalCached( null /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */ , cache3A );

	
	cache3A = null;
	
	
	System.out.println( "Reached #2..." );
	
	
	final EinsteinTensorElem<String,SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>,SymbolicElemFactory<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>>
			mg1 = curv;
	
	
	ArrayList<BigInteger> vve = new ArrayList<BigInteger>();
	vve.add( BigInteger.ZERO );
	vve.add( BigInteger.ZERO );
	
	SymbolicElem<SymbolicElem<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>,SymbolicElemFactory<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>,SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>>
			svvA = mg1.getVal( vve );
	
	
	System.out.println( svvA ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	
	System.out.println( "Reached #3..." );
	

	
	SymbolicElem<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> svvB = svvA.eval( null );
	
	
	System.out.println( svvB ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	
	svvB = svvB.distributeSimplify2();
	
	
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );
	
	debugOn = true;
	
	System.out.println( "RunDist..." );
	svvB = distNode( svvB );

	
	
	
	
	
	hndlNode2( svvB , cval , hbsval , msval , hmm2 );
	
	
	
	
}
	
	


	
}


