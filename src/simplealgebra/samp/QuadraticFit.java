


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



package simplealgebra.samp;

import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;
import simplealgebra.NumDimensions;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.Ord;



/**
 * Class for generating a simple quadratic fit of the form Y = F( X )..
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the vector of samples (the number of samples).
 * @param <A> The Ord of the vector of samples.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class QuadraticFit<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends
	Sampling<U,A,R,S>
{
	
	/**
	 * The X-Coordinates to be fitted.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> xv; 
	
	/**
	 * The Y-Coordinates to be fitted.
	 */
	protected GeometricAlgebraMultivectorElem<U,A,R,S> yv;
	
	/**
	 * The mean of the X-Coordinate positions.
	 */
	protected R meanX;
	
	/**
	 * The mean of the Y-Coordinate positions.
	 */
	protected R meanY;
	
	/**
	 * The calculated slope coefficient.
	 */
	protected R slope;
	
	/**
	 * The calculated acceleration coefficient.
	 */
	protected R acc;
	
	
	/**
	 * Constructs the class.
	 * 
	 * @param _xv The X-Coordinates to be fitted.
	 * @param _yv The Y-Coordinates to be fitted.
	 */
	public QuadraticFit( GeometricAlgebraMultivectorElem<U,A,R,S> _xv , GeometricAlgebraMultivectorElem<U,A,R,S> _yv )
	{
		xv = _xv;
		yv = _yv;
	}
	
	
	/**
	 * Generates the fit.
	 * 
	 * @throws NotInvertibleException
	 */
	public void generateFit() throws NotInvertibleException
	{
		meanX = mean( xv );
		meanY = mean( yv );
		
		
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> slopesY = new GeometricAlgebraMultivectorElem<U,A,R,S>( xv.getFac().getFac() , 
				xv.getFac().getDim() , xv.getFac().getOrd() );
		slopeTransform( xv , yv , meanX , meanY , slopesY  );
		
		slope = mean( slopesY );
		
		
		
		final GeometricAlgebraMultivectorElem<U,A,R,S> slopesY2 = new GeometricAlgebraMultivectorElem<U,A,R,S>( xv.getFac().getFac() , 
				xv.getFac().getDim() , xv.getFac().getOrd() );
		slopeTransform( xv , slopesY , meanX , slope , slopesY2 );
		
		acc = mean( slopesY2 );
		
		
	}
	
	
	public R eval( R x , R meanX , R meanY , R slope , R acc )
	{
		final R sub = x.add( meanX.negate() );
		final R slA = acc.mult( sub );
		final R slR = ( slA.add( slope ) ).mult( sub );
		final R ret = slR.add( meanY );
		return( ret );
	}

	
	/**
	 * Evaluates the calculated fit function.
	 * 
	 * @param x The input X-Coordinate.
	 * @return The calculated Y-Coordinate from the fit function.
	 */
	public R eval( R x )
	{
		return( eval( x , meanX , meanY , slope , acc ) );
	}


	/**
	 * Gets the mean of the X-Coordinate positions.
	 * 
	 * @return The mean of the X-Coordinate positions.
	 */
	public R getMeanX() {
		return meanX;
	}


	/**
	 * Gets the mean of the Y-Coordinate positions.
	 * 
	 * @return The mean of the Y-Coordinate positions.
	 */
	public R getMeanY() {
		return meanY;
	}


	/**
	 * Gets the calculated slope coefficient.
	 * 
	 * @return The calculated slope coefficient.
	 */
	public R getSlope() {
		return slope;
	}


	/**
	 * Gets the calculated acceleration coefficient.
	 * 
	 * @return The calculated acceleration coefficient.
	 */
	public R getAcc() {
		return acc;
	}


	
}




