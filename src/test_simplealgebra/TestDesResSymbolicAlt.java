






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

import junit.framework.Assert;
import junit.framework.TestCase;
import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.Mutator;
import simplealgebra.NotInvertibleException;
import simplealgebra.SquareMatrixElem;
import simplealgebra.WriteElemCache;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.PartialDerivativeOp;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.et.EinsteinTensorElemFactory;
import simplealgebra.et.EmFieldTensorFactoryAlt;
import simplealgebra.et.RankTwoDeterminantFactory;
import simplealgebra.et.SimpleCurveMetricTensorFactory;
import simplealgebra.et.VectorPotentialFactory;
import simplealgebra.prec.DefaultPrecedenceComparator;
import simplealgebra.symbolic.*;


import java.io.*;



/**
 * Tests the ability to generate terms with the determinant of the metric tensor.  Uses JUnit ( <A href="http://junit.org">http://junit.org</A> ).
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDesResSymbolicAlt extends TestCase 
{

	/**
	 * Elem representing the square of the speed of light.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class CSquaredElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public CSquaredElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof CSquaredElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public boolean isPartialDerivativeZero()
		{
			return( true );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( CSquaredElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( CSquaredElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msup><mi>c</mi><mn>2</mn></msup>" );
		}
		
	}
	
	
	
	/**
	 * Elem representing the potential term.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class T_2UxElem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public T_2UxElem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof T_2UxElem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( T_2UxElem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( T_2UxElem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<mn>2</mn><mo>&InvisibleTimes;</mo><mi>U</mi><mfenced><mi>x</mi></mfenced>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 0-th component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class A0_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A0_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A0_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( A0_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( A0_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>0</mn></msub>" );
		}
		
	}
	
	
	/**
	 * Node representing the 1st component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class A1_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A1_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A1_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( A1_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( A1_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>1</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 2nd component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class A2_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A2_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A2_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( A2_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( A2_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>2</mn></msub>" );
		}
		
	}
	
	
	
	
	
	
	/**
	 * Node representing the 3rd component of the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class A3_Elem extends SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public A3_Elem(ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac ) {
			super(_fac);
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public ComplexElem<DoubleElem,DoubleElemFactory> evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public ComplexElem<DoubleElem, DoubleElemFactory> evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, ComplexElem<DoubleElem, DoubleElemFactory>> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> b )
		{
			if( b instanceof A3_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<ComplexElem<DoubleElem, DoubleElemFactory>,ComplexElemFactory<DoubleElem, DoubleElemFactory>>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( A3_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( A3_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>A</mi><mn>3</mn></msub>" );
		}
		
	}
	
	
	
	
	/**
	 * Factory for the vector potential.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class VectFac extends VectorPotentialFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * The factory for the enclosed type.
		 */
		ComplexElemFactory<DoubleElem,DoubleElemFactory> fac;
		
		/**
		 * Constructs the factory.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public VectFac( ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac )
		{
			fac = _fac;
		}
		
		@Override
		public SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> getVectorPotential( BigInteger basisIndex )
		{
			
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				return( new A0_Elem( fac ) );
			}
			
			
			if( basisIndex.equals( BigInteger.ONE ) )
			{
				return( new A1_Elem( fac ) );
			}
			
			
			if( basisIndex.equals( BigInteger.valueOf( 2 ) ) )
			{
				return( new A2_Elem( fac ) );
			}
			
			
			return( new A3_Elem( fac ) );
		}

	};
	
	
	
	/**
	 * Node representing the 0-th ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class X0_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X0_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
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
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X0_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( X0_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( X0_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>0</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 1st ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class X1_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X1_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
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
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X1_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( X1_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( X1_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>1</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 2nd ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class X2_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X2_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
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
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X2_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( X2_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( X2_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>2</mn></msub>" );
		}
		
	}
	
	
	
	/**
	 * Node representing the 3rd ordinate of the coordinate space.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class X3_Elem extends SymbolicElem<DoubleElem,DoubleElemFactory>
	{

		/**
		 * Constructs the elem.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public X3_Elem(DoubleElemFactory _fac ) {
			super(_fac);
		}

		@Override
		public DoubleElem eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalCached(
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}

		@Override
		public DoubleElem evalPartialDerivative(ArrayList<? extends Elem<?, ?>> withRespectTo, HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public DoubleElem evalPartialDerivativeCached(
				ArrayList<? extends Elem<?, ?>> withRespectTo,
				HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
				HashMap<SCacheKey<DoubleElem, DoubleElemFactory>, DoubleElem> cache)
				throws NotInvertibleException,
				MultiplicativeDistributionRequiredException {
			throw( new RuntimeException( "NotSupported" ) );
		}
		
		@Override
		public boolean symbolicEquals( SymbolicElem<DoubleElem,DoubleElemFactory> b )
		{
			if( b instanceof X3_Elem )
			{
				return( true );
			}
			
			return( false );
		}
		
		@Override
		public String writeDesc(
				WriteElemCache<SymbolicElem<DoubleElem, DoubleElemFactory>, SymbolicElemFactory<DoubleElem, DoubleElemFactory>> cache,
				PrintStream ps) {
			String st = cache.get( this );
			if( st == null )
			{
				final String sta = fac.writeDesc( (WriteElemCache<DoubleElem, DoubleElemFactory>)( cache.getInnerCache() ) , ps);
				st = cache.getIncrementVal();
				cache.put(this, st);
				ps.print( X3_Elem.class.getSimpleName() );
				ps.print( " " );
				ps.print( st );
				ps.print( " = new " );
				ps.print( X3_Elem.class.getSimpleName() );
				ps.print( "( " );
				ps.print( sta );
				ps.println( " );" );
			}
			return( st );
		}
		
		@Override
		public void writeMathML( PrecedenceComparator pc , PrintStream ps )
		{
			ps.print( "<msub><mi>x</mi><mn>3</mn></msub>" );
		}
		
	}
	
	
	
	
	/**
	 * Defines a directional derivative for the test.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class DirecFac extends DirectionalDerivativePartialFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
	{

		/**
		 * The factory for the enclosed type.
		 */
		ComplexElemFactory<DoubleElem,DoubleElemFactory> fac;
		
		/**
		 * Constructs the directional derivative factory.
		 * 
		 * @param _fac The factory for the enclosed type.
		 */
		public DirecFac( ComplexElemFactory<DoubleElem,DoubleElemFactory> _fac )
		{
			fac = _fac;
		}
		
		@Override
		public PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, SymbolicElem<DoubleElem, DoubleElemFactory>> getPartial(
				BigInteger basisIndex) {
			
			if( basisIndex.equals( BigInteger.ZERO ) )
			{
				ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
				tmp.add( new X0_Elem( new DoubleElemFactory() ) );
				return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
						SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
			}
			
			
			if( basisIndex.equals( BigInteger.ONE ) )
			{
				ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
				tmp.add( new X1_Elem( new DoubleElemFactory() ) );
				return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
						SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
			}
			
			
			if( basisIndex.equals( BigInteger.valueOf( 2 ) ) )
			{
				ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
				tmp.add( new X2_Elem( new DoubleElemFactory() ) );
				return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
						SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
			}
			
			
			ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>> tmp = new ArrayList<SymbolicElem<DoubleElem, DoubleElemFactory>>();
			tmp.add( new X3_Elem( new DoubleElemFactory() ) );
			return( new PartialDerivativeOp<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>, 
					SymbolicElem<DoubleElem, DoubleElemFactory>>( fac , tmp ) );
		}
		
	}
	
	
	
	/**
	 * Provides precedence comparison rules for the test.  Used when generating MathML.
	 * 
	 * @author thorngreen
	 *
	 */
	private static class PrecCompare extends DefaultPrecedenceComparator
	{
		
		
		@Override
		protected void enclosedOrTerminalSymbolsAInit()
		{
			defaultEnclosedOrTerminalSymbolsAInit();
			enclosedOrTerminalSymbolsA.add( T_2UxElem.class );
		}
		
		@Override
		protected void enclosedOrTerminalSymbolsBInit()
		{
			defaultEnclosedOrTerminalSymbolsBInit();
			enclosedOrTerminalSymbolsB.add( A0_Elem.class );
			enclosedOrTerminalSymbolsB.add( A1_Elem.class );
			enclosedOrTerminalSymbolsB.add( A2_Elem.class );
			enclosedOrTerminalSymbolsB.add( A3_Elem.class );
			enclosedOrTerminalSymbolsB.add( CSquaredElem.class );
			enclosedOrTerminalSymbolsB.add( T_2UxElem.class );
		}
		
	}
	
	
	
	/**
	 * Tests the ability to generate terms with the determinant of the metric tensor.
	 */
	public void testDesResSymbolicAlt() throws NotInvertibleException
	{
		final TestDimensionFour td = new TestDimensionFour();
		
		final DoubleElemFactory dl = new DoubleElemFactory();
		
		final ComplexElemFactory<DoubleElem,DoubleElemFactory> ce =
				new ComplexElemFactory<DoubleElem,DoubleElemFactory>( dl );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			cSquared = new CSquaredElem( ce );
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			t_2Ux = new T_2UxElem( ce );
		
		
		final SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> ye = 
				new SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>(ce);
		
		
		SimpleCurveMetricTensorFactory<Object, ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			scfac = new SimpleCurveMetricTensorFactory<Object, ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>( ye , cSquared , t_2Ux );
		
		
		final Object z0 = new Object()
		{
			
		};
		
		
		final Object z1 = new Object()
		{
			
		};
		
		
		
		final RankTwoDeterminantFactory<Object, TestDimensionFour, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> detF =
			new RankTwoDeterminantFactory<Object, TestDimensionFour, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>( td );
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> 
			metric = scfac.getMetricTensor( true, z0, z1, td.getVal() );
		
		
		SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> det
			= detF.getDeterminantComponent( metric ).negate();
		
		
		
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			d2M = det.distributeSimplify2();
		
		
		
		final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
			d2Msqrt = new SymbolicSqrt<ComplexElem<DoubleElem, DoubleElemFactory>, 
				ComplexElemFactory<DoubleElem, DoubleElemFactory>>( d2M , ce );
		
		
		
		final Object uu = new Object()
		{
			
		};
		
		
		final Object aa = new Object()
		{
			
		};
		
		
		final Object bb = new Object()
		{
			
		};
		
		
		final Object vv = new Object()
		{
			
		};
		
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> 
			firstMetric = scfac.getMetricTensor( false , uu, aa , td.getVal() );
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>> 
			lastMetric = scfac.getMetricTensor( false , bb , vv , td.getVal() );
		
		
		VectFac vectFac = new VectFac( ce );
		
		
		DirecFac direcFac = new DirecFac( ce );
		
		
		EmFieldTensorFactoryAlt<Object, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>, SymbolicElem<DoubleElem,DoubleElemFactory>>
			emFldFac = new EmFieldTensorFactoryAlt<Object, ComplexElem<DoubleElem,DoubleElemFactory>, ComplexElemFactory<DoubleElem,DoubleElemFactory>, 
			SymbolicElem<DoubleElem,DoubleElemFactory>>(ye, direcFac, vectFac);
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
			emFld = emFldFac.getEmFld(aa, bb, td.getVal() );
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
			fmult = ( firstMetric.mult( emFld ) ).mult( lastMetric );
		
		
		EinsteinTensorElem<Object, SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>, SymbolicElemFactory<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>
			fmutate = fmult.mutate( new Mutator<SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>>()
			{

				@Override
				public String writeString( ) {
					// TODO Auto-generated method stub
					return( "mutate mult." );
				}
				
				@Override
				public boolean exposesDerivatives()
				{
					return( false );
				}

				@Override
				public SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> mutate(
						SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>> in)
						throws NotInvertibleException {
					return( in.mult( d2Msqrt ) );
				}

				@Override
				public Mutator<SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>> cloneThread(
						BigInteger threadIndex) {
					throw( new RuntimeException( "Not Supported" ) );
				}
				
			} );
		
	
		// System.out.println( d3.writeString() );
		
		
		
		Assert.assertTrue( fmutate != null );
		
		
		final PrecCompare comp = new PrecCompare();
		
		
		// System.out.println( "***" );
		int i;
		int j;
		for( i = 0 ; i < TestDimensionFour.FOUR ; i++ )
		{
			for( j = 0 ; j < TestDimensionFour.FOUR ; j++ )
			{
				BigInteger ii = BigInteger.valueOf( i );
				BigInteger jj = BigInteger.valueOf( j );
				ArrayList<BigInteger> el = new ArrayList<BigInteger>();
				el.add( ii );
				el.add( jj );
				
				final SymbolicElem<ComplexElem<DoubleElem, DoubleElemFactory>, ComplexElemFactory<DoubleElem, DoubleElemFactory>>
					valI = fmutate.getVal( el );
				Assert.assertTrue( valI != null );
		//		valI.writeMathMLWrapped( comp , System.out );
				final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
					valA = valI.distributeSimplify2();
		//		valA.writeMathMLWrapped( comp , System.out );
				final SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
					val = valA.distributeSimplify();
				System.out.print( "<P>" );
				val.writeMathMLWrapped( comp , System.out );
				System.out.println( "" );
				
				if( ii.equals( jj ) )
				{
					Assert.assertTrue( !( val instanceof SymbolicZero ) );
				}
				
				// System.out.println( "" + i + " " + j + " " + ( val.writeString() ) );
			}
		}
		
		
		
	}
	
	
	
	
	
}


