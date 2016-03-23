




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





package simplealgebra;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.MultiplicativeDistributionRequiredException;
import simplealgebra.symbolic.PrecedenceComparator;
import simplealgebra.symbolic.SCacheKey;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;

/**
 * Symbolic elem for the left-side conjugate of a complex number defined as satisfying: <math display="block">
 * <mrow>
 *  <msup>
 *        <mrow>
 *          <mfenced open="(" close=")" separators=",">
 *            <mrow>
 *              <mi>a</mi>
 *              <mo>+</mo>
 *              <mi>i</mi>
 *              <mi>b</mi>
 *            </mrow>
 *          </mfenced>
 *        </mrow>
 *      <mrow>
 *        <mo>*</mo>
 *        <mo>L</mo>
 *      </mrow>
 *  </msup>
 *  <mfenced open="(" close=")" separators=",">
 *    <mrow>
 *      <mi>a</mi>
 *      <mo>+</mo>
 *      <mi>i</mi>
 *      <mi>b</mi>
 *    </mrow>
 *  </mfenced>
 *  <mo>=</mo>
 *  <msup>
 *          <mi>a</mi>
 *        <mn>2</mn>
 *  </msup>
 *  <mo>+</mo>
 *  <msup>
 *          <mi>b</mi>
 *        <mn>2</mn>
 *  </msup>
 * </mrow>
 * </math>
 *
 * <P> Note that in SimpleAlgebra this is not necessarily <math display="inline">
 * <mrow>
 *  <mi>a</mi>
 *  <mo>-</mo>
 *  <mi>i</mi>
 *  <mi>b</mi>
 * </mrow>
 * </math> because the product <math display="inline">
 * <mrow>
 *  <mi>a</mi>
 *  <mi>b</mi>
 * </mrow>
 * </math> does not necessarily commute.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class SymbolicConjugateLeft<R extends Elem<R,?>, S extends ElemFactory<R,S>> extends 
	SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> 
{

	/**
	 * Constructs the elem.
	 * 
	 * @param _elem The nested elem.
	 * @param _fac The factory for the nested elem.
	 */
	public SymbolicConjugateLeft( SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> _elem , ComplexElemFactory<R, S> _fac) 
	{
		super( _fac );
		elem = _elem;
	}
	
	
	/**
	 * Constructs the elem for use in a Drools ( http://drools.org ) session.
	 * 
	 * @param _elem The nested elem.
	 * @param _fac The factory for the nested elem.
	 * @param ds The Drools session.
	 */
	public SymbolicConjugateLeft( SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> _elem , ComplexElemFactory<R, S> _fac, DroolsSession ds) 
	{
		this( _elem , _fac );
		ds.insert( this );
	}

	
	@Override
	public ComplexElem<R, S> eval( HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		ArrayList<ComplexElem<R,S>> args = new ArrayList<ComplexElem<R,S>>();
		return( elem.eval( implicitSpace ).handleOptionalOp( ComplexElem.ComplexCmd.CONJUGATE_LEFT , args ) );
	}
	
	
	@Override
	public ComplexElem<R, S> evalCached(
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<ComplexElem<R, S>, ComplexElemFactory<R, S>>, ComplexElem<R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		final SCacheKey<ComplexElem<R, S>, ComplexElemFactory<R, S>> key = new SCacheKey<ComplexElem<R, S>, ComplexElemFactory<R, S>>( this , implicitSpace );
		final ComplexElem<R, S> iret = cache.get( key );
		if( iret != null )
		{
			return( iret );
		}
		ArrayList<ComplexElem<R,S>> args = new ArrayList<ComplexElem<R,S>>();
		final ComplexElem<R, S> ret = elem.evalCached( implicitSpace , cache ).handleOptionalOp( ComplexElem.ComplexCmd.CONJUGATE_LEFT , args );
		cache.put( key , ret );
		return( ret );
	}

	
	@Override
	public ComplexElem<R, S> evalPartialDerivative(
			ArrayList<? extends Elem<?, ?>> withRespectTo , HashMap<? extends Elem<?,?>,? extends Elem<?,?>> implicitSpace ) throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	
	@Override
	public ComplexElem<R, S> evalPartialDerivativeCached(
			ArrayList<? extends Elem<?, ?>> withRespectTo,
			HashMap<? extends Elem<?, ?>, ? extends Elem<?, ?>> implicitSpace,
			HashMap<SCacheKey<ComplexElem<R, S>, ComplexElemFactory<R, S>>, ComplexElem<R, S>> cache)
			throws NotInvertibleException,
			MultiplicativeDistributionRequiredException {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TBD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return null;
	}
	
	
	
	@Override
	public boolean evalSymbolicConstantApprox()
	{
		return( elem.evalSymbolicConstantApprox() );
	}
	
	
	@Override
	public boolean exposesDerivatives()
	{
		return( elem.exposesDerivatives() );
	}
	
	
	@Override
	public SymbolicConjugateLeft<R,S> cloneThread( final BigInteger threadIndex )
	{
		final ComplexElemFactory<R,S> facs = this.getFac().getFac().cloneThread(threadIndex);
		final SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> elems = elem.cloneThread(threadIndex);
		if( ( facs != this.getFac().getFac() ) || ( elems != elem ) )
		{
			return( new SymbolicConjugateLeft<R,S>( elems , facs ) );
		}
		return( this );
	}
	
	
	@Override
	public SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R, S>> cloneThreadCached(
			BigInteger threadIndex,
			CloneThreadCache<SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R, S>>, SymbolicElemFactory<ComplexElem<R, S>, ComplexElemFactory<R, S>>> cache) {
		final SymbolicElem<ComplexElem<R, S>, ComplexElemFactory<R, S>> ctmp = cache.get( this );
		if( ctmp != null )
		{
			return( ctmp );
		}
		final ComplexElemFactory<R,S> facs = this.getFac().getFac().cloneThreadCached(threadIndex, (CloneThreadCache)( cache.getInnerCache() ) );
		final SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> elems = elem.cloneThreadCached(threadIndex, cache);
		if( ( facs != this.getFac().getFac() ) || ( elems != elem ) )
		{
			final SymbolicConjugateLeft<R,S> rtmp = new SymbolicConjugateLeft<R,S>( elems , facs );
			cache.put(this, rtmp);
			return( rtmp );
		}
		cache.put(this, this);
		return( this );
	}

	
	@Override
	public String writeDesc( WriteElemCache<SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>>,SymbolicElemFactory<ComplexElem<R,S>,ComplexElemFactory<R,S>>> cache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			final String elemAs = elem.writeDesc( cache , ps);
			final String facs = fac.writeDesc( (WriteElemCache)( cache.getInnerCache() ) , ps);
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( SymbolicConjugateLeft.class.getSimpleName() );
			ps.print( "<" );
			this.getFac().getFac().getFac().writeElemTypeString(ps);
			ps.print( "," );
			this.getFac().getFac().getFac().writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( SymbolicConjugateLeft.class.getSimpleName() );
			ps.print( "<" );
			this.getFac().getFac().getFac().writeElemTypeString(ps);
			ps.print( "," );
			this.getFac().getFac().getFac().writeElemFactoryTypeString(ps);
			ps.print( ">" );
			ps.print( "( " );
			ps.print( elemAs );
			ps.print( " , " );
			ps.print( facs );
			ps.println( " );" );
		}
		return( st );
	}
	
	
	@Override
	public void writeMathML(
			PrecedenceComparator pc,
			PrintStream ps) {
		ps.print( "<msup>" );
		if( pc.parenNeeded( this ,  elem , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisOpen(ps);
		}
		else
		{
			ps.print( "<mrow>" );
		}
		elem.writeMathML(pc, ps);
		if( pc.parenNeeded( this ,  elem , false ) )
		{
			pc.getParenthesisGenerator().handleParenthesisClose(ps);
		}
		else
		{
			ps.print( "</mrow>" );
		}
		ps.print( "<mo>*L</mo></msup>" );
	}
	
	
	@Override
	public void performInserts( StatefulKnowledgeSession session )
	{
		elem.performInserts( session );
		super.performInserts( session );
	}
	
	
	/**
	 * Returns the nested elem.
	 * 
	 * @return The nested elem.
	 */
	public SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> getElem() {
		return elem;
	}

	/**
	 * The nested elem.
	 */
	private SymbolicElem<ComplexElem<R,S>,ComplexElemFactory<R,S>> elem;


}

