




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


/**
 * An elem. for which the enclosed elems can be mutated by a Mutator instance.
 * 
 * @author thorngreen
 *
 * @param <T> The enclosed type.
 * @param <U> The elem. type.
 * @param <R> The factory for the elem. type.
 */
public abstract class MutableElem<T extends Elem<T,?>, U extends MutableElem<T,U,?>, R extends ElemFactory<U,R> > 
	extends Elem<U,R> implements Mutable<U,U,T> 
{

	
	/**
	 * Returns a mutator for the elem. type given a mutator for the enclosed type.
	 * 
	 * @param elem The mutator for the enclosed type.
	 * @return A mutator for the elem. type.
	 */
	public Mutator<U> createElemMutator( final Mutator<T> elem )
	{
		final Mutator<U> ret = new Mutator<U>()
		{

			@Override
			public U mutate(U in) throws NotInvertibleException {
				return( in.mutate( elem ) );
			}
			
			@Override
			public boolean exposesDerivatives()
			{
				return( elem.exposesDerivatives() );
			}

			@Override
			public String writeString() {
				return( "mutateElem[ " + elem.writeString() + " ]" );
			}
			
		};
		return( ret );
	}
	
	
}

