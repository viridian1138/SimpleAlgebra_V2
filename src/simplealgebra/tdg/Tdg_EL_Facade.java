



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





package simplealgebra.tdg;

import java.math.BigInteger;

import simplealgebra.ComplexElem;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.NotInvertibleException;



/**
 * A facade that makes class simplealgebra.tdg.Tdg_EL easier to use.
 * 
 * @author tgreen
 *
 * @param <R> The enclosed elem type.
 * @param <S> The factory for the enclosed elem type.
 */
public class Tdg_EL_Facade<R extends Elem<R,?>, S extends ElemFactory<R,S>>
{
	
	/**
	 * The Tdg_EL that is being abstracted.
	 */
	protected Tdg_EL<R,S> tdg;
	
	
	/**
	 * Constructs the facade.
	 * @param _pi The number PI to the desired number of digits.
	 */
	public Tdg_EL_Facade( ComplexElem<R,S> _pi ) throws NotInvertibleException
	{
		tdg = new Tdg_EL( _pi );
	}
	
	
	/**
	 * Returns the surface volume of a hypersphere.
	 * @param radius The radius of the hypersphere.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The surface volume of the hypersphere.
	 * @throws NotInvertibleException
	 */
	public R calcHypersphereSurfaceVolume( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM2( BigInteger.valueOf( 4 ) , radius , numIter ) );
	}
	
	
	/**
	 * Returns the surface area of a sphere.
	 * @param radius The radius of the sphere.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The surface area of the sphere.
	 * @throws NotInvertibleException
	 */
	public R calcSphereSurfaceArea( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM2( BigInteger.valueOf( 3 ) , radius , numIter ) );
	}
	
	
	/**
	 * Returns the circumfrence of a circle.
	 * @param radius The radius of the circle.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The circumfrence of the circle.
	 * @throws NotInvertibleException
	 */
	public R calcCircleCircumfrence( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM2( BigInteger.valueOf( 2 ) , radius , numIter ) );
	}
	
	
	/**
	 * Returns the length of a line segment.
	 * @param radius The radius from the line segment center to an endpoint.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The length of the line segment.
	 * @throws NotInvertibleException
	 */
	public R calcLineSegmentLength( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM1( BigInteger.valueOf( 1 ) , radius , numIter ) );
	}
	
	
	/**
	 * Returns the area of a circle.
	 * @param radius The radius of the circle.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The area of the circle.
	 * @throws NotInvertibleException
	 */
	public R calcCircleArea( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM1( BigInteger.valueOf( 2 ) , radius , numIter ) );
	}
	
	
	/**
	 * Returns the volume of a sphere.
	 * @param radius The radius of the sphere.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The volume of the sphere.
	 * @throws NotInvertibleException
	 */
	public R calcSphereVolume( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM1( BigInteger.valueOf( 3 ) , radius , numIter ) );
	}
	
	
	/**
	 * Returns the hypervolume of a hypersphere.
	 * @param radius The radius of the hypersphere.
	 * @param numIter The number of iterations to use when calculating exponentials.
	 * @return The hypervolume of the hypersphere.
	 * @throws NotInvertibleException
	 */
	public R calcHypersphereHypervolume( final R radius , final int numIter ) throws NotInvertibleException
	{
		return( tdg.calcM1( BigInteger.valueOf( 4 ) , radius , numIter ) );
	}
	
	
	/**
	 * Gets the Tdg_EL that is being abstracted.
	 * @return The Tdg_EL that is being abstracted.
	 */
	public Tdg_EL<R,S> getTdg_EL()
	{
		return( tdg );
	}
	
	
	
}


