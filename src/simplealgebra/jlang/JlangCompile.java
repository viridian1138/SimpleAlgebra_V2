




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






package simplealgebra.jlang;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Class for converting some symbolic expressions to Clang (C Language) native code
 * for execution-time performance optimization.
 * 
 * @author tgreen
 *
 */
public class JlangCompile<R extends Elem<R,?>, S extends ElemFactory<R,S>> {

	/**
	 * Constructor.
	 */
	public JlangCompile() {
	}
	
	
	/**
	 * Attempts to convert a SymbolicElem to clang.
	 * @param in The SymbolicElem to be converted.
	 * @return The clang-converted SymbolicElem, or the original SymbolicElem if clang-conversion failed.
	 */
	public SymbolicElem<R,S> attemptJlangCompile( SymbolicElem<R,S> in )
	{
		
		try
		{
			final S fac = in.getFac().getFac();
			
			if( fac instanceof DoubleElemFactory )
			{
				SymbolicElem<DoubleElem,DoubleElemFactory> ds = (SymbolicElem<DoubleElem,DoubleElemFactory>) in;
				SymbolicElem<DoubleElem,DoubleElemFactory> dout = Jlang.jlang_Dbl( ds );
				SymbolicElem<R,S> dout2 = (SymbolicElem<R,S>) dout;
				return( dout2 );
			}
			
			if( fac instanceof ComplexElemFactory )
			{
				ComplexElemFactory<?,?> cfac = (ComplexElemFactory) fac;
				if( cfac.getFac() instanceof DoubleElemFactory )
				{
					SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> ds = 
							(SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
					SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> dout =
							Jlang.jlang_Cplx_Dbl( ds );
					SymbolicElem<R,S> dout2 = (SymbolicElem<R,S>) dout;
					return( dout2 );
				}
			}
			
		}
		catch(  Throwable ex )
		{
			ex.printStackTrace( System.out );
		}
		
		
		return( in );
		
	}

	
}


