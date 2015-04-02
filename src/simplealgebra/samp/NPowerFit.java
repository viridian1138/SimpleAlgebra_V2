


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
 * Class for generating a simple N-th power fit of the form Y = F( X )..
 * 
 * @author thorngreen
 *
 * @param <U> The number of dimensions in the vector of samples (the number of samples).
 * @param <A> The Ord of the vector of samples.
 * @param <R> The enclosed type.
 * @param <S> The factory for the enclosed type.
 */
public class NPowerFit<U extends NumDimensions, A extends Ord<U>, R extends Elem<R,?>, S extends ElemFactory<R,S>> extends
	Sampling<U,A,R,S>
{
	
	/**
	 * The power of the fit.
	 */
	protected int n;
	
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
	 * The calculated slope coefficients.
	 */
	protected R slopes[];
	
	
	/**
	 * Constructs the class.
	 * 
	 * @param _n The power of the fit.
	 * @param _xv The X-Coordinates to be fitted.
	 * @param _yv The Y-Coordinates to be fitted.
	 */
	public NPowerFit( int _n , GeometricAlgebraMultivectorElem<U,A,R,S> _xv , GeometricAlgebraMultivectorElem<U,A,R,S> _yv )
	{
		n = _n;
		xv = _xv;
		yv = _yv;
		slopes = (R[])( new Elem<?,?>[ n ] );
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
		
		R mY = meanY;
		GeometricAlgebraMultivectorElem<U,A,R,S> yyv = yv;
		
		
		for( int cnt = 0 ; cnt < n ; cnt++ )
		{
			final GeometricAlgebraMultivectorElem<U,A,R,S> slY = new GeometricAlgebraMultivectorElem<U,A,R,S>( xv.getFac().getFac() , 
					xv.getFac().getDim() , xv.getFac().getOrd() );
			slopeTransform( xv , yyv , meanX , mY , slY  );
			
			final R slp = mean( slY );
			yyv = slY;
			mY = slp;
			slopes[ cnt ] = slp;
		}
		
	}
	
	
	/**
	 * Evaluates the fit function.
	 * 
	 * @param x The input X-Coordinate.
	 * @param meanX The mean of the X-Coordinate positions.
	 * @param meanY The mean of the Y-Coordinate positions.
	 * @param slopes The slope coefficients.
	 * @return The calculated Y-Coordinate from the fit function.
	 */
	public R eval( R x , R meanX , R meanY , R slopes[] )
	{
		final R sub = x.add( meanX.negate() );
		R sum = xv.getFac().getFac().zero();
		for( int cnt = n - 1 ; cnt >= 0 ; cnt-- )
		{
			sum = ( sum.add( slopes[ cnt ] ) ).mult( sub );
		}
		final R ret = sum.add( meanY );
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
		return( eval( x , meanX , meanY , slopes ) );
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
	 * Gets the calculated slope coefficients.
	 * 
	 * @return The calculated slope coefficients.
	 */
	public R[] getSlopes() {
		return slopes;
	}


	
}




