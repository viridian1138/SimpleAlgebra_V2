





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






package simplealgebra.et;

import java.io.PrintStream;

import simplealgebra.AbstractCache;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NumDimensions;
import simplealgebra.WriteElemCache;
import simplealgebra.WriteNumDimensionsCache;
import simplealgebra.ddx.DirectionalDerivative;
import simplealgebra.ddx.DirectionalDerivativePartialFactory;
import simplealgebra.ddx.WriteDirectionalDerivativePartialFactoryCache;
import simplealgebra.ga.WriteOrdCache;
import simplealgebra.symbolic.DroolsSession;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicElemFactory;


/**
 * Implements a factory for the tensor <math display="inline">
 * <mrow>
 *  <msub>
 *          <mo>&PartialD;</mo>
 *        <mi>v</mi>
 *  </msub>
 * </mrow>
 * </math> where <math display="inline">
 * <mrow>
 *  <mi>v</mi>
 * </mrow>
 * </math> is a tensor index.  This produces a rank-one tensor
 * with a set of partial derivative operators.  The name of the
 * particular index to be used is passed into the class as a
 * parameter.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 * @param <Z> Type defining the terms for the contravariant and covariant indices.
 * @param <U> The number of dimensions for the index.
 * @param <R> The enclosed type of the tensor.
 * @param <S> The factory for the enclosed type of the tensor.
 * @param <K> The type of the element against which to take partial derivatives.
 */
public class OrdinaryDerivativeFactory<Z extends Object, U extends NumDimensions, R extends Elem<R,?>, S extends ElemFactory<R,S>, K extends Elem<?,?>> {
	
	/**
	 * Constructs the tensor factory.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions for the index.
	 * @param _dfac Factory for generating the partial derivatives of a directional derivative.
	 * @param _remap Parameter describing how to remap the derivative.  Leave as null if no remapping is desired.
	 */
	public OrdinaryDerivativeFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac ,
			DerivativeRemap<Z,R,S> _remap )
	{
		fac = _fac;
		dim = _dim;
		dfac = _dfac;
		remap = _remap;
	}
	
	/**
	 * Constructs the tensor factory for use in a Drools ( <A href="http://drools.org">http://drools.org</A> ) session.
	 * 
	 * @param _fac The factory for the enclosed type.
	 * @param _dim The number of dimensions for the index.
	 * @param _dfac Factory for generating the partial derivatives of a directional derivative.
	 * @param _remap Parameter describing how to remap the derivative.  Leave as null if no remapping is desired.
	 * @param ds The Drools session.
	 */
	public OrdinaryDerivativeFactory( EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
			SymbolicElemFactory<R, S>> _fac , 
			U _dim ,
			DirectionalDerivativePartialFactory<R,S,K> _dfac ,
			DerivativeRemap<Z,R,S> _remap , DroolsSession ds )
	{
		this( _fac , _dim , _dfac , _remap );
		ds.insert( this );
	}
	
	/**
	 * Applies the ordinary derivative to an expression.
	 * 
	 * @param term The expression to which to apply the derivative.
	 * @param derivativeIndex The tensor index of the ordinary derivative.
	 * @return The result of applying the derivative.
	 */
	public SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> 
		getOrdinaryDerivative( SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> term , Z derivativeIndex )
{
		final OrdinaryDerivative<Z,U,R,S,K> ord = new OrdinaryDerivative<Z,U,R,S,K>( fac , derivativeIndex , dim , dfac );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			retA = ord.mult( term );
		
		final SymbolicElem<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>,EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>>
			ret = remap == null ? retA : remap.remap( retA );
		
		return( ret );
}
	
	
	
	
	/**
	 * Writes a description of the instance to the output stream.
	 * 
	 * @param cache Instance cache from which to cache objects.
	 * @param ecache Instance cache from which to cache elems.
	 * @param ps Stream to write the description.
	 * @return String describing the id of the object.
	 */
	public String writeDesc( WriteOrdinaryDerivativeFactoryCache<Z,U,R,S,K> cache , WriteElemCache<EinsteinTensorElem<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>, EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>>> ecache , PrintStream ps )
	{
		String st = cache.get( this );
		if( st == null )
		{
			ecache.applyAuxCache( new WriteDirectionalDerivativePartialFactoryCache( ecache.getCacheVal() ) );
			ecache.applyAuxCache( new WriteNumDimensionsCache( ecache.getCacheVal() ) );
			ecache.applyAuxCache( new WriteDerivativeRemapCache<Z,R,S>( ecache.getCacheVal() ) );
			final String facs = fac.writeDesc( (WriteElemCache)( ecache.getInnerCache() ) , ps);
			final String staiF = dfac.writeDesc( ( (WriteDirectionalDerivativePartialFactoryCache)( ecache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteDirectionalDerivativePartialFactoryCache.class)) ) ) ) , ps);
			final String staDim = dim.writeDesc( (WriteNumDimensionsCache)( ecache.getAuxCache( WriteNumDimensionsCache.class ) ) , ps);
			String remaps = "null";
			if( remap != null )
			{
				remaps = remap.writeDesc( (WriteDerivativeRemapCache)( ecache.getAuxCache( (Class<? extends AbstractCache<?, ?, ?, ?>>) ((Class)(WriteDerivativeRemapCache.class)) ) ) , ps);
			}
			st = cache.getIncrementVal();
			cache.put(this, st);
			ps.print( OrdinaryDerivativeFactory.class.getSimpleName() );
			ps.print( "<" );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Object" );
			ps.print( ">" );
			ps.print( " " );
			ps.print( st );
			ps.print( " = new " );
			ps.print( OrdinaryDerivativeFactory.class.getSimpleName() );
			ps.print( "<" );
			dim.writeTypeString(ps);
			ps.print( "," );
			fac.writeElemTypeString(ps);
			ps.print( "," );
			fac.writeElemFactoryTypeString(ps);
			ps.print( "," );
			ps.print( "? extends Object" );
			ps.print( ">" );
			ps.print( "( " );
			ps.print( facs );
			ps.print( " , " );
			ps.print( staDim );
			ps.print( " , " );
			ps.print( staiF );
			ps.print( " , " );
			ps.print( remaps );
			ps.println( " );" );
		}
		return( st );
		
	}
	
	
	
	
	/**
	 * Gets the tensor index for the ordinary derivative.
	 * 
	 * @return The tensor index for the ordinary derivative.
	 */
	public EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, SymbolicElemFactory<R, S>> getFac() {
		return fac;
	}

	/**
	 * Gets the number of dimensions for the index.
	 * 
	 * @return The number of dimensions for the index.
	 */
	public U getDim() {
		return dim;
	}

	/**
	 * Gets the factory for generating the partial derivatives of a directional derivative.
	 * 
	 * @return The factory for generating the partial derivatives of a directional derivative.
	 */
	public DirectionalDerivativePartialFactory<R, S, K> getDfac() {
		return dfac;
	}

	/**
	 * Gets the function for remapping the derivative after it is calculated.
	 * 
	 * @return The function for remapping the derivative after it is calculated.
	 */
	public DerivativeRemap<Z, R, S> getRemap() {
		return remap;
	}




	/**
	 * The tensor index for the ordinary derivative.
	 */
	private EinsteinTensorElemFactory<Z, SymbolicElem<R, S>, 
		SymbolicElemFactory<R, S>> fac;
	
	/**
	 * The number of dimensions for the index.
	 */
	private U dim;
	
	/**
	 * Factory for generating the partial derivatives of a directional derivative.
	 */
	private DirectionalDerivativePartialFactory<R,S,K> dfac;
	
	/**
	 * Function for remapping the derivative after it is calculated.  Leave as null if no remap is desired.
	 */
	private DerivativeRemap<Z,R,S> remap;

}


